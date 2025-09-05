package com.ly.service;

import com.github.pagehelper.PageInfo;
import com.ly.dto.CourseStatusDTO;
import com.ly.po.TaskCoursePO;
import com.ly.po.TaskPO;
import com.ly.pojo.SeckillTasks;

import java.util.List;

public interface TaskService {
    PageInfo<TaskPO> getTask(int page, int size);

    int updateTask(TaskPO taskPO);

    int deleteTaskById(Integer id);

    int insertTask(TaskPO taskPO);

    PageInfo<TaskCoursePO> getAllTask(int page, int size);

    void batchUpdateStatus(List<CourseStatusDTO> statusUpdates);

    List<SeckillTasks> getAllTaskList();

    PageInfo<TaskPO> searchTask(int page, int size, String searchText);

    void updateCache();
}
