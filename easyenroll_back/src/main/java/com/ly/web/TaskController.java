package com.ly.web;

import com.github.pagehelper.PageInfo;
import com.ly.annotation.RoleRequire;
import com.ly.dto.CourseStatusDTO;
import com.ly.po.TaskCoursePO;
import com.ly.po.TaskPO;
import com.ly.result.R;
import com.ly.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    /**
     * 获取任务列表
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/task")
    @RoleRequire({"ADMIN"})
    public R getTaskList(@RequestParam int page, @RequestParam int size) {
        PageInfo<TaskPO> pageInfo = taskService.getTask(page, size);
        return R.success(pageInfo.getList(), String.valueOf(pageInfo.getTotal()));
    }

    /**
     * 搜索任务列表
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/task/search/{searchText}")
    @RoleRequire({"ADMIN"})
    public R searchTaskList(@RequestParam int page, @RequestParam int size, @PathVariable String searchText) {
        PageInfo<TaskPO> pageInfo = taskService.searchTask(page, size, searchText);
        return R.success(pageInfo.getList(), String.valueOf(pageInfo.getTotal()));
    }

    /**
     * 获取秒杀课程全部相关信息，列表
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/task/all")
    @RoleRequire({"STUDENT"})
    public R getAllTaskList(@RequestParam int page, @RequestParam int size) {
        PageInfo<TaskCoursePO> pageInfo = taskService.getAllTask(page, size);
        return R.success(pageInfo.getList(), String.valueOf(pageInfo.getTotal()));
    }

    /**
     * 更新任务信息
     * @param taskPO
     * @return
     */
    @PutMapping("/task")
    @RoleRequire("ADMIN")
    public R updateTask(@RequestBody TaskPO taskPO) {
        int i = taskService.updateTask(taskPO);
        if (i == 3) {
            return R.error("超额");
        } else if (i == 1) {
            return R.success("success");
        }

        return R.error("error");
    }

    /**
     * 删除任务
     * @param id
     * @return
     */
    @DeleteMapping("/task/{id}")
    @RoleRequire("ADMIN")
    public R deleteTask(@PathVariable Integer id) {
        int i = taskService.deleteTaskById(id);
        if(i != 1) {
            return R.error("error");
        }
        return R.success("success");
    }

    /**
     * 新增任务（新增成功后同步缓存数据）
     * @param taskPO
     * @return
     */
    @PostMapping("/task")
    @RoleRequire("ADMIN")
    public R insertTask(@RequestBody TaskPO taskPO) {
        //System.out.println(taskPO);
        int i = taskService.insertTask(taskPO);
        if (i == 2) {
            return R.error("任务重复");
        } else if (i == 3) {
            return R.error("超额");
        } else if (i == 1) {
            //同步缓存数据
            taskService.updateCache();
            return R.success("success");

        }

        return R.error("error");

    }



    @PostMapping("/task/updateStatus")
    public R updateCourseStatus(@RequestBody List<CourseStatusDTO> statusUpdates) {
        try {
            taskService.batchUpdateStatus(statusUpdates);
            return R.success("状态更新成功");
        } catch (Exception e) {
            return R.error("状态更新失败");
        }
    }

}
