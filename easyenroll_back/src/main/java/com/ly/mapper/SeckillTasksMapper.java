package com.ly.mapper;

import cn.hutool.aop.aspects.Aspect;
import cn.hutool.cron.task.Task;
import com.ly.po.TaskCoursePO;
import com.ly.po.TaskPO;
import com.ly.pojo.SeckillTasks;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface SeckillTasksMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SeckillTasks record);

    int insertSelective(SeckillTasks record);

    SeckillTasks selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SeckillTasks record);

    int updateByPrimaryKey(SeckillTasks record);

    int updateAllByPrimaryKeyPO(TaskPO taskPO);

    List<TaskPO> selectTask();

    List<SeckillTasks> selectAll();

    void deleteByCourseId(Integer id);

    List<TaskCoursePO> selectAllTask();

    void updateStatus(int courseId, String status);

    List<SeckillTasks> selectList();

    int updateStockBycourseId(Integer courseId);

    void updateStockBycourseIdAdd(Integer preCourseId);

    List<TaskPO> searchTask(String searchText);

    List<SeckillTasks> selectNotStarted();

    List<SeckillTasks> selectOnGoing();

    Date selectStartTimeByCourseId(Integer courseId);

    Date selectEndTimeByCourseId(Integer courseId);
}