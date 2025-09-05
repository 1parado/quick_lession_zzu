package com.ly.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ly.dto.SelectionAndCode;
import com.ly.mapper.*;
import com.ly.po.CoursesPO;
import com.ly.po.TaskCoursePO;
import com.ly.pojo.Courses;
import com.ly.pojo.Selections;
import com.ly.pojo.Students;
import com.ly.service.RedisService;
import com.ly.service.SeckillService;
import com.ly.service.SelectionService;
import com.ly.util.Constant;
import com.ly.util.SeckillLocalCache;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class SelectionServiceImpl implements SelectionService {

    @Autowired
    private CoursesMapper coursesMapper;

    @Autowired
    private SelectionsMapper selectionsMapper;

    @Autowired
    private StudentsMapper studentsMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private SeckillTasksMapper tasksMapper;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private TeachersMapper teachersMapper;

    @Override
    public int saveNoSeckillSelection(Selections selections) {

        //设置选择时间为当前时间
        selections.setSelectionTime(new Date());

        //判断所选择课程的前置课程是否已经选择
        //通过courseId去查找courses表，找到记录后获取它的pre_course字段
        //若是为空，则直接放行
        //若是不为空，拿到这个前置课程id，结合studentId去查找selections表，若有，则说明前置条件达成，没有则返回“前置课程为选择”
        Integer preCourseId = coursesMapper.selectPreCourseIdById(selections.getCourseId());
        if (preCourseId != null) {
            {
                Selections preSelection = selectionsMapper.selectByStudentIdAndCourseId(preCourseId, selections.getStudentId());
                if (preSelection == null) {
                    return 2;//表示"前置课程未选择"
                }
            }
        }

        //判断 该课程 与 对应学生的所选课程的时间是否冲突， 冲突则返回return 3;"时间冲突"
        //根据 学生id 去selections表中拿到 所有对应的课程id，
        // 本课程id 去 courses表中拿到课程对象，开始遍历，去courses表中拿到一个一个拿到课程对象，对比class_time字段，只要有一样的直接返回时间冲突
        List<Selections> list = selectionsMapper.selectByStudentId(selections.getStudentId());
        Courses currentCourse = coursesMapper.selectByPrimaryKey(selections.getCourseId());
        for(Selections selection : list) {
            Courses course = coursesMapper.selectByPrimaryKey(selection.getCourseId());
            if (currentCourse.getClassTime().equals(course.getClassTime()) && currentCourse.getSemester().equals(course.getSemester())){
                return 3;//表示时间冲突
            }
        }

        int i = selectionsMapper.insertSelective(selections);

        return i == 1 ? 1 : 0;
    }

    @Override
    public List<Selections> getListByStudentId(Integer studentId) {
        return selectionsMapper.selectByStudentId(studentId);
    }

    @Override
    public List<SelectionAndCode> getListCodeByStudentId(Integer studentId) {
        List<SelectionAndCode> list = new ArrayList<>();
        List<Selections> selections = selectionsMapper.selectByStudentId(studentId);
        for(Selections selection : selections) {
            String courseCode = coursesMapper.selectCourseCodeByIdPO(selection.getCourseId());
            SelectionAndCode selectionAndCode = new SelectionAndCode();
            selectionAndCode.setCourseCode(courseCode);
            selectionAndCode.setStudentId(selection.getStudentId());
            selectionAndCode.setId(selection.getId());
            selectionAndCode.setSelectionTime(selection.getSelectionTime());
            selectionAndCode.setSemester(selection.getSemester());
            selectionAndCode.setStatus(selection.getStatus());
            selectionAndCode.setIsSeckill(selection.getIsSeckill());
            list.add(selectionAndCode);
        }
        return list;
    }

    @Override
    public int deleteStatusByCourseIdAndStudentId(Integer preCourseId, Integer studentId) {
        RLock lock = redissonClient.getLock("seckill:lock:" + preCourseId + "_" + studentId);
        //判断isSeckill字段值为1时清空缓存
        Integer isSeckill = selectionsMapper.selectIsSeckillByCourseIdAndStudentId(preCourseId, studentId);
        if (isSeckill == 1) {
            Students students = studentsMapper.selectByPrimaryKey(studentId);
            Long studentSno = students.getSno();
            String courseCode = coursesMapper.selectCourseCodeByIdPO(preCourseId);

            try {
                if (lock.tryLock(5, 30, TimeUnit.SECONDS)) {
                    //恢复库存
                    String key = Constant.SECKILL_STOCK_PREFIX + courseCode;
                    redisTemplate.opsForValue().increment(key, 1L);
                    //恢复数据库
                    tasksMapper.updateStockBycourseIdAdd(preCourseId);
                    //更新course表的remain字段（加一）
                    coursesMapper.updateRemainByCourseIdAdd(preCourseId);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
            //清空缓存
            SeckillLocalCache.clear(studentSno, courseCode);
            redisService.clear(studentSno, courseCode);
            //清除Redis中的秒杀结果信息
            String key1 = Constant.SECKILL_STATUS_PREFIX + studentSno + "_" + courseCode;
            redisService.removeValue(key1);

            //重新缓存数据
            //从数据库加载所有商品信息
            List<TaskCoursePO> taskList = tasksMapper.selectAllTask();
            redisService.setValue(Constant.SECKILL_LIST, taskList);
            //加载入Redis中
            for (TaskCoursePO seckillTasks : taskList) {

                //拼一个Redis的Key
                String key = Constant.SECKILL_STOCK_PREFIX + seckillTasks.getCourseCode();

                //使用String存储
                redisService.setValue(key, seckillTasks.getStore());

                Date now = new Date();

                //设置过期时间
                Long d = seckillTasks.getEndTime().getTime() - now.getTime();
                redisService.expire(key, d, TimeUnit.SECONDS);
            }

        }

        //判断当前课程是否是其他课程的前置，若是，获得该课程
        List<Integer> ids = coursesMapper.selectIdByPreCourseId(preCourseId);
        // 只有当ids不为空时才执行删除
        if (ids != null && !ids.isEmpty()) {
            selectionsMapper.deleteByIds(ids, studentId);
        }

        return selectionsMapper.deleteByCourseIdAndStudentId(preCourseId, studentId);
    }

    @Override
    public PageInfo<CoursesPO> getCourseByNormal(int page, int size, Integer studentId) {
        PageHelper.startPage(page, size);
        List<Selections> selections = selectionsMapper.selectByStudentIdByNormal(studentId);

        // 如果没有任何选课记录，返回空分页结果
        if (selections == null || selections.isEmpty()) {
            return new PageInfo<>(Collections.emptyList());
        }

        List<Integer> courseIdList = new ArrayList<>();
        for (Selections selection : selections) {
            courseIdList.add(selection.getCourseId());
        }

        List<CoursesPO> courses = coursesMapper.selectCourseByCourseId(courseIdList);
        PageInfo<CoursesPO> pageInfo = PageInfo.of(courses);
        return pageInfo;
    }

    @Override
    public List<CoursesPO> getCourseByNormalFull(Integer studentId) {

        List<Selections> selections = selectionsMapper.selectByStudentIdByNormal(studentId);
        // 如果没有任何选课记录，返回空分页结果
        if (selections == null || selections.isEmpty()) {
            return new ArrayList<>();
        }

        List<Integer> courseIdList = new ArrayList<>();
        for (Selections selection : selections) {
            courseIdList.add(selection.getCourseId());
        }

        List<CoursesPO> courses = coursesMapper.selectCourseByCourseId(courseIdList);
        return courses;

    }

    @Override
    public List<CoursesPO> getCourseByNormalNoPage(Long account) {
        Integer studentId = studentsMapper.selectIdBySno(account);
        List<Selections> selections = selectionsMapper.selectByStudentIdByNormal(studentId);
        // 如果没有任何选课记录，返回空分页结果
        if (selections == null || selections.isEmpty()) {
            return new ArrayList<>();
        }
        List<Integer> courseIdList = new ArrayList<>();
        for (Selections selection : selections) {
            courseIdList.add(selection.getCourseId());
        }
        List<CoursesPO> courses = coursesMapper.selectCourseByCourseId(courseIdList);
        return courses;
    }

    @Override
    public PageInfo<CoursesPO> getTeacherCourseByNormal(int page, int size, Long tno) {
        PageHelper.startPage(page, size);
        Integer teacherId = teachersMapper.selectIdByTno(tno);

        List<CoursesPO> courses = coursesMapper.selectCourseByTeacherId(teacherId);
        PageInfo<CoursesPO> pageInfo = PageInfo.of(courses);
        return pageInfo;
    }

    @Override
    public List<CoursesPO> getTeacherCourseByNormalNoPage(Long tno) {
        Integer teacherId = teachersMapper.selectIdByTno(tno);
        List<CoursesPO> courses = coursesMapper.selectCourseByTeacherId(teacherId);
        return courses;
    }

    @Override
    public boolean isSeckilled(Long studentSno, String courseCode) {
        //根据studentSno获取studentId
        Integer studentId = studentsMapper.selectIdBySno(studentSno);
        //根据courseCode获取courseId
        Integer courseId = coursesMapper.selectIdByCourseCode(courseCode);

        return selectionsMapper.existsSelection(studentId, courseId);
    }

    @Override
    public Integer batchSelect(List<Students> selectedStudents, List<Courses> selectedCourses) {
        // 该批课程的课程id集合
        List<Integer> courseIds = new ArrayList<>();
        // 给批课程的课程时间集合
        Set<String> courseTimes = new HashSet<>();
        for (Courses course : selectedCourses) {
            courseIds.add(course.getId());
            courseTimes.add(course.getClassTime() + "," + course.getSemester());
        }
        boolean isRepeat = false;
        if (courseTimes.size() != courseIds.size()) {
            isRepeat = true;
        }

        for (int i = 0; i < selectedStudents.size(); i++) {
            //遍历学生
            for (int j = 0; j < selectedCourses.size(); j++) {
                //为每一个学生遍历一次课程
                Students student = selectedStudents.get(i);
                Courses course = selectedCourses.get(j);
                System.out.println(i + "," + student + "---->" + j + "," + course);

                Selections selections = new Selections();
                selections.setSemester(course.getSemester());
                selections.setCourseId(course.getId());
                selections.setStudentId(student.getId());
                int k = saveBatch(selections, courseIds, isRepeat);
                if (k != 1) {
                    return k;
                }
            }
        }
        return 1;
    }

    private int saveBatch(Selections selections, List<Integer> courseIds, boolean isRepeat) {
        selections.setSelectionTime(new Date());
        selections.setStatus("NORMAL");
        selections.setIsSeckill(0);

        //判断前置课程选择与否之前先判断前置课程是否包含在这一批课程中，若包含则不进行以下判断
        Integer preCourseId = coursesMapper.selectPreCourseIdById(selections.getCourseId());
        boolean contains = courseIds.contains(preCourseId);
        if (!contains) {
            //如果不包含,去判断前置课程是否已选择
            //判断所选择课程的前置课程是否已经选择
            //通过courseId去查找courses表，找到记录后获取它的pre_course字段
            //若是为空，则直接放行
            //若是不为空，拿到这个前置课程id，结合studentId去查找selections表，若有，则说明前置条件达成，没有则返回“前置课程为选择”
            if (preCourseId != null) {
                {
                    Selections preSelection = selectionsMapper.selectByStudentIdAndCourseId(preCourseId, selections.getStudentId());
                    if (preSelection == null) {
                        return -1;//表示"前置课程未选择"
                    }
                }
            }
        }

        //先要判断这一批课程中有没有时间冲突
        if (isRepeat) {
            return -2;//表示时间冲突
        }

        //判断当前课程是否被当前学生选择过

        //判断 该课程 与 对应学生的已经所选课程的时间是否冲突， 冲突则返回return 3;"时间冲突"
        //根据 学生id 去selections表中拿到 所有对应的课程id，
        // 本课程id 去 courses表中拿到课程对象，开始遍历，去courses表中拿到一个一个拿到课程对象，对比class_time字段，只要有一样的直接返回时间冲突
        boolean isRepeatInset = false;
        List<Selections> list = selectionsMapper.selectByStudentId(selections.getStudentId());
        Courses currentCourse = coursesMapper.selectByPrimaryKey(selections.getCourseId());
        for(Selections selection : list) {
            if (!Objects.equals(selection.getCourseId(), currentCourse.getId())) {
                //不重复插入时，才进行时间冲突判断
                Courses course = coursesMapper.selectByPrimaryKey(selection.getCourseId());
                if (currentCourse.getClassTime().equals(course.getClassTime()) && currentCourse.getSemester().equals(course.getSemester())){
                    return -2;//表示时间冲突
                }
            } else {
                isRepeatInset = true;
            }

        }
        int i = 1;
        if (!isRepeatInset){
            i = selectionsMapper.insertSelective(selections);

        }
        return i == 1 ? 1 : 0;
    }
}
