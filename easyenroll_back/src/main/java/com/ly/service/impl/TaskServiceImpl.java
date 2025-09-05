package com.ly.service.impl;

import cn.hutool.cron.task.Task;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ly.dto.CourseStatusDTO;
import com.ly.mapper.CoursesMapper;
import com.ly.mapper.SeckillTasksMapper;
import com.ly.po.CoursesPO;
import com.ly.po.TaskCoursePO;
import com.ly.po.TaskPO;
import com.ly.pojo.SeckillTasks;
import com.ly.pojo.Students;
import com.ly.pojo.Users;
import com.ly.service.RedisService;
import com.ly.service.TaskService;
import com.ly.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    @Autowired
    private SeckillTasksMapper taskMapper;

    @Autowired
    private CoursesMapper courseMapper;

    @Autowired
    private RedisService redisService;


    @Override
    public PageInfo<TaskPO> getTask(int page, int size) {
        PageHelper.startPage(page, size);
        List<TaskPO> taskPO = taskMapper.selectTask();
        //拿到数据以后，判断数据的status是否需要更新
        for (TaskPO taskPO1 : taskPO) {
            String status = taskPO1.getStatus();
            Date now = new Date();
            if (taskPO1.getStartTime().after(now)) {
                taskPO1.setStatus("NOT_STARTED");//任务"未开始"
            } else if (taskPO1.getEndTime().before(now)) {
                taskPO1.setStatus("FINISHED");//任务"已结束"
            } else {
                taskPO1.setStatus("ONGOING");//任务进行中
            }

            if (!status.equals(taskPO1.getStatus())) {
                //更新
                SeckillTasks seckillTasks = new SeckillTasks();
                seckillTasks.setStock(taskPO1.getStore());
                seckillTasks.setStartTime(taskPO1.getStartTime());
                seckillTasks.setEndTime(taskPO1.getEndTime());
                seckillTasks.setCourseId(courseMapper.selectIdByCourseCode(taskPO1.getCourseCode()));
                seckillTasks.setStatus(taskPO1.getStatus());

                taskMapper.updateByPrimaryKey(seckillTasks);

                //重新查
                taskPO = taskMapper.selectTask();
            }
        }

        PageInfo<TaskPO> pageInfo = PageInfo.of(taskPO);
        return pageInfo;
    }

    @Override
    public PageInfo<TaskPO> searchTask(int page, int size, String searchText) {
        PageHelper.startPage(page, size);
        List<TaskPO> taskPO = taskMapper.searchTask(searchText);
        PageInfo<TaskPO> pageInfo = PageInfo.of(taskPO);
        return pageInfo;
    }

    @Override
    public int updateTask(TaskPO taskPO) {

        //判断名额是否不超过余额
        if (taskPO.getStore() > taskPO.getRemain()) {
            return 3;//超额
        }

        SeckillTasks seckillTasks = new SeckillTasks();
        seckillTasks.setStock(taskPO.getStore());
        seckillTasks.setStartTime(taskPO.getStartTime());
        seckillTasks.setEndTime(taskPO.getEndTime());

        Date now = new Date();
        if (seckillTasks.getStartTime().after(now)) {
            seckillTasks.setStatus("NOT_STARTED");//任务"未开始"
        } else if (seckillTasks.getEndTime().before(now)) {
            seckillTasks.setStatus("FINISHED");//任务"已结束"
        } else {
            seckillTasks.setStatus("ONGOING");//任务进行中
        }
        seckillTasks.setCourseId(courseMapper.selectIdByCourseCode(taskPO.getCourseCode()));
        int i = taskMapper.updateByPrimaryKey(seckillTasks);
        //只需要赋值名额，开始时间、结束时间即可

        //更新到缓存中
        List<TaskCoursePO> taskList = taskMapper.selectAllTask();
        redisService.removeValue(Constant.SECKILL_LIST);
        redisService.setValue(Constant.SECKILL_LIST, taskList);

        return i == 1 ? 1 : 0;
    }

    @Override
    public int deleteTaskById(Integer id) {
        return taskMapper.deleteByPrimaryKey(id);
    }

    boolean isOne = true;

    @Override
    public int insertTask(TaskPO taskPO) {
        //先判断是否唯一，根据course_id判断

        CoursesPO coursesPO = courseMapper.selectByCourseCodePO(taskPO.getCourseCode());
        Integer id = coursesPO.getId();
        isOne = true;

        List<SeckillTasks> tasks = taskMapper.selectAll();
        tasks.forEach((task) -> {
            if (Objects.equals(id, task.getCourseId())){
                //说明重复
                isOne = false;
            }
        });

        if (!isOne) {
            //重复
            return 2;
        }

        //判断名额是否不超过余额
        if (taskPO.getStore() > taskPO.getRemain()) {
            return 3;//超额
        }

        SeckillTasks seckillTasks = new SeckillTasks();
        seckillTasks.setCourseId(id);
        seckillTasks.setEndTime(taskPO.getEndTime());
        seckillTasks.setStartTime(taskPO.getStartTime());
        //判断时间，决定status
        /**
         * start < now < end
         * now < start
         * end < now
         */
        Date now = new Date();
        if (seckillTasks.getStartTime().after(now)) {
            seckillTasks.setStatus("NOT_STARTED");//任务"未开始"
        } else if (seckillTasks.getEndTime().before(now)) {
            seckillTasks.setStatus("FINISHED");//任务"已结束"
        } else {
            seckillTasks.setStatus("ONGOING");//任务进行中
        }
        seckillTasks.setStock(taskPO.getStore());

        int i = taskMapper.insert(seckillTasks);

        //更新到缓存中
        List<TaskCoursePO> taskList = taskMapper.selectAllTask();
        redisService.removeValue(Constant.SECKILL_LIST);
        redisService.setValue(Constant.SECKILL_LIST, taskList);

        return i == 1 ? 1 : 0;

    }

    @Override
    public PageInfo<TaskCoursePO> getAllTask(int page, int size) {
        PageHelper.startPage(page, size);
        //先去redis中取数据
        List<TaskCoursePO> taskCoursePOs = (List) redisService.getValue(Constant.SECKILL_LIST);
        System.out.println("去Redis查询列表数据");
        if (taskCoursePOs == null) {
            System.out.println("去数据库查询列表数据");
            taskCoursePOs = taskMapper.selectAllTask();
        }
        PageInfo<TaskCoursePO> pageInfo = PageInfo.of(taskCoursePOs);
        return pageInfo;
    }

    @Override
    public void batchUpdateStatus(List<CourseStatusDTO> statusUpdates) {
        statusUpdates.forEach(dto -> {
            taskMapper.updateStatus(dto.getCourseId(), dto.getStatus());
        });
    }

    @Override
    public List<SeckillTasks> getAllTaskList() {
        return taskMapper.selectList();
    }

    @Override
    public void updateCache() {
        //从数据库加载所有商品信息
        List<TaskCoursePO> taskList = taskMapper.selectAllTask();
        redisService.removeValue(Constant.SECKILL_LIST);
        redisService.setValue(Constant.SECKILL_LIST, taskList);
        //加载入Redis中
        for (TaskCoursePO seckillTasks : taskList) {

            //拼一个Redis的Key
            String key = Constant.SECKILL_STOCK_PREFIX + seckillTasks.getCourseCode();

            //先删除
            redisService.removeValue(key);

            //使用String存储
            redisService.setValue(key, seckillTasks.getStore());

            Date now = new Date();

            //设置过期时间
            Long d = seckillTasks.getEndTime().getTime() - now.getTime();
            redisService.expire(key, d, TimeUnit.SECONDS);
        }

        //log.info("成功缓存 {} 个秒杀商品到Redis", goodsList.size());
        System.out.println("成功缓存 " + taskList.size() + "个秒杀任务到Redis");
    }
}
