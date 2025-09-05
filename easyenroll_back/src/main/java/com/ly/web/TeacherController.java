package com.ly.web;

import com.github.pagehelper.PageInfo;
import com.ly.annotation.RoleRequire;

import com.ly.mapper.CoursesMapper;
import com.ly.pojo.Teachers;
import com.ly.result.R;
import com.ly.service.CourseService;
import com.ly.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tea")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CoursesMapper coursesMapper;


    /**
     * 获取教师列表
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/teacher")
    @RoleRequire("ADMIN")
    public R getTeacherList(@RequestParam int page, @RequestParam int size) {
        PageInfo<Teachers> pageInfo = teacherService.getTeacher(page, size);
        return R.success(pageInfo.getList(), String.valueOf(pageInfo.getTotal()));
    }

    /**
     * 搜索教师列表
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/teacher/search/{searchText}")
    @RoleRequire("ADMIN")
    public R searchTeacherList(@RequestParam int page, @RequestParam int size, @PathVariable String searchText) {
        PageInfo<Teachers> pageInfo = teacherService.searchTeacher(page, size, searchText);
        return R.success(pageInfo.getList(), String.valueOf(pageInfo.getTotal()));
    }

    /**
     * 获取教师列表（不分页）（没用）
     * @return
     */
    @GetMapping("/teacher/list")
    @RoleRequire("ADMIN")
    public R getTeacherList() {
        List<Teachers> list = teacherService.getTeacherList();
        return R.success(list);
    }

    /**
     * 更新教师信息
     * @param teachers
     * @return
     */
    @PutMapping("/teacher")
    @RoleRequire("ADMIN")
    public R updateTeacher(@RequestBody Teachers teachers) {
        int i = teacherService.updateTeacher(teachers);
        if(i == 2){
            //学号重复
            return R.error("工号重复");
        }else if (i != 1) {
            return R.error("error");
        }
        return R.success("success");
    }

    /**
     * 删除教师
     * @param id
     * @return
     */
    @DeleteMapping("/teacher/{id}")
    @RoleRequire("ADMIN")
    public R deleteTeacher(@PathVariable Integer id) {
        //删除教师时，删除课程表关联的任课教师信息
        //级联删除相关课程
        //int courseId = coursesMapper.selectIdByTeacherId(id);
        System.out.println("教师id" + id);
        List<Integer> courseIds = coursesMapper.selectIdByTeacherId(id);
        for (Integer courseId : courseIds) {
            int i1 = courseService.deleteCourseById(courseId);
            if (i1 != 1) {
                return R.error("error");
            }
        }

        int i2 = teacherService.deleteTeacherById(id);

        if(i2 != 1) {
            return R.error("error");
        }
        return R.success("success");
    }

    /**
     * 新增教师
     * @param teachers
     * @return
     */
    @PostMapping("/teacher")
    @RoleRequire("ADMIN")
    public R insertTeacher(@RequestBody Teachers teachers) {
        System.out.println(teachers);
        int i = teacherService.insertTeacher(teachers);
        if (i == 2) {
            return R.error("工号重复");
        }
        if(i != 1) {
            return R.error("error");
        }
        return R.success("success");
    }
}
