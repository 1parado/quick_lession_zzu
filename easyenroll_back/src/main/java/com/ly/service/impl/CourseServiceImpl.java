package com.ly.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ly.mapper.CoursesMapper;
import com.ly.mapper.ExamMapper;
import com.ly.mapper.SeckillTasksMapper;
import com.ly.mapper.TeachersMapper;
import com.ly.po.CoursesPO;
import com.ly.pojo.Courses;
import com.ly.pojo.Students;
import com.ly.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CoursesMapper coursesMapper;

    @Autowired
    private TeachersMapper teachersMapper;

    @Autowired
    private SeckillTasksMapper taskMapper;

    @Autowired
    private ExamMapper examMapper;

    @Override
    public PageInfo<CoursesPO> getCourse(int page, int size) {
        PageHelper.startPage(page, size);
        List<CoursesPO> courses = coursesMapper.selectCourse();
        PageInfo<CoursesPO> pageInfo = PageInfo.of(courses);
        return pageInfo;
    }

    @Override
    public PageInfo<CoursesPO> getTeacherCourse(int page, int size, Long tno) {
        PageHelper.startPage(page, size);
        Integer teacherId = teachersMapper.selectIdByTno(tno);
        List<CoursesPO> courses = coursesMapper.selectCourseByTeacherId(teacherId);
        PageInfo<CoursesPO> pageInfo = PageInfo.of(courses);
        return pageInfo;
    }

    @Override
    public PageInfo<CoursesPO> getTeacherCourseByExam(int page, int size, Long tno) {
        PageHelper.startPage(page, size);

        Integer teacherId = teachersMapper.selectIdByTno(tno);

        //根据课程code去exam表中查找state属性，若为3，则显示
        List<CoursesPO> courses = coursesMapper.selectCourseByTeacherIdByExam(teacherId);

        PageInfo<CoursesPO> pageInfo = PageInfo.of(courses);
        return pageInfo;
    }

    @Override
    public PageInfo<CoursesPO> getSeckillCourse(int page, int size) {
        PageHelper.startPage(page, size);
        List<CoursesPO> courses = coursesMapper.selectCourseByIsSeckill();
        PageInfo<CoursesPO> pageInfo = PageInfo.of(courses);
        return pageInfo;
    }

    @Override
    public PageInfo<CoursesPO> getNoSeckillCourse(int page, int size) {
        PageHelper.startPage(page, size);
        List<CoursesPO> courses = coursesMapper.selectCourseByNOIsSeckill();
        PageInfo<CoursesPO> pageInfo = PageInfo.of(courses);
        return pageInfo;
    }

    @Override
    public PageInfo<CoursesPO> getSearchCourse(int page, int size, String searchText) {
        PageHelper.startPage(page, size);
        List<CoursesPO> courses = coursesMapper.selectCourseBySearch(searchText);
        PageInfo<CoursesPO> pageInfo = PageInfo.of(courses);
        return pageInfo;
    }

    @Override
    public PageInfo<CoursesPO> getSearchAllCourse(int page, int size, String searchText) {
        PageHelper.startPage(page, size);
        List<CoursesPO> courses = coursesMapper.selectAllCourseBySearch(searchText);
        PageInfo<CoursesPO> pageInfo = PageInfo.of(courses);
        return pageInfo;
    }

    @Override
    public int updateCourse(CoursesPO coursePO) {
        //更新前先判断数据是否合理（在前端已经判断过是否合法）
        //根据 code 找到一个 course ,若找到且不为自己 就 重复了
        CoursesPO s = coursesMapper.selectByCourseCodePO(coursePO.getCourseCode());
        if (s != null && !s.getCourseCode().equals(coursePO.getCourseCode())){
            //说明重复了
            return 2;
        }

        // 判断有没有更新course_code字段，若有则查找并更新course中pre_course为旧code的课程
        // 根据id去查寻课程代码， 与 coursePO 的 课程代码比较
        /*String oldCode = coursesMapper.selectCourseCodeByIdPO(coursePO.getId());
        String newCode = coursePO.getCourseCode();
        if (!oldCode.equals(newCode)) {
            //说明code值发生改变，更新course中pre_course为旧code的课程
            // 查找pre_course字段值为oldCode的课程，更新pre_course为newCode
            int i2 = coursesMapper.updatePreCourseByoldCode(oldCode,newCode);
            if (i2 != 1) {
                return 0;
            }
        }*/
        //更新本课程
        Courses courses = new Courses();
        courses.setId(coursePO.getId());
        courses.setCourseCode(coursePO.getCourseCode());
        courses.setCourseType(coursePO.getCourseType());
        courses.setCapacity(coursePO.getCapacity());
        courses.setClassLocation(coursePO.getClassLocation());
        courses.setClassTime(coursePO.getClassTime());
        courses.setCollege(coursePO.getCollege());
        courses.setCredit(coursePO.getCredit());
        courses.setDescription(coursePO.getDescription());
        courses.setIsSeckill(coursePO.getIsSeckill());
        courses.setName(coursePO.getName());
        courses.setRemain(coursePO.getRemain());
        courses.setSemester(coursePO.getSemester());
        courses.setWeekRange(coursePO.getWeekRange());

        courses.setPreCourse(coursesMapper.selectIdByCourseCode(coursePO.getPreCourseCode()));
        courses.setTeacherId(teachersMapper.selectIdByTno(coursePO.getTno()));

        int i1 = coursesMapper.updateByPrimaryKey(courses);

        if (i1== 1) {
            return 1;
        }
        return 0;
    }

    @Override
    public int deleteCourseById(Integer id) {
        //课程删除时，删除相关课程的pre_course
        //相关课程：本课程id == 相关课程的 pre_course
        coursesMapper.updateByPreCourseId(id);

        //课程删除时，删除相关的秒杀任务
        taskMapper.deleteByCourseId(id);

        //删除
        int i2 = coursesMapper.deleteByPrimaryKey(id);

        if (i2 != 0) {
            return 1;
        }
        return 0;
    }

    @Override
    public int insertCourse(CoursesPO coursePO) {
        //更新前先判断数据是否合理（在前端已经判断过是否合法）
        //根据 code 找到一个 course ,若找到且不为自己 就 重复了
        CoursesPO s = coursesMapper.selectByCourseCodePO(coursePO.getCourseCode());
        if (s != null){
            //说明重复了
            return 2;
        }

        //新增本课程
        Courses courses = new Courses();
        courses.setId(coursePO.getId());
        courses.setCourseCode(coursePO.getCourseCode());
        courses.setCourseType(coursePO.getCourseType());
        courses.setCapacity(coursePO.getCapacity());
        courses.setClassLocation(coursePO.getClassLocation());
        courses.setClassTime(coursePO.getClassTime());
        courses.setCollege(coursePO.getCollege());
        courses.setCredit(coursePO.getCredit());
        courses.setDescription(coursePO.getDescription());
        courses.setIsSeckill(coursePO.getIsSeckill());
        courses.setName(coursePO.getName());
        courses.setRemain(coursePO.getRemain());
        courses.setSemester(coursePO.getSemester());
        courses.setWeekRange(coursePO.getWeekRange());

        courses.setPreCourse(coursesMapper.selectIdByCourseCode(coursePO.getPreCourseCode()));
        courses.setTeacherId(teachersMapper.selectIdByTno(coursePO.getTno()));

        int i1 = coursesMapper.insert(courses);

        if (i1 == 1) {
            return 1;
        }
        return 0;
    }

    @Override
    public List<CoursesPO> getCourseList() {
        List<CoursesPO> list = coursesMapper.selectCourseByIsSeckill();
        return list;
    }

    @Override
    public List<Courses> getAllCourseList() {
        return coursesMapper.selectAll();
    }
}
