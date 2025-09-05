package com.ly.web;

import com.github.pagehelper.PageInfo;
import com.ly.annotation.RoleRequire;
import com.ly.po.CoursesPO;
import com.ly.pojo.Courses;
import com.ly.pojo.Students;
import com.ly.result.R;
import com.ly.service.CourseService;
import com.ly.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cou")
public class CourseController {

    @Autowired
    private CourseService courseService;

    /**
     * 获取课程列表(分页)
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/course")
    public R getCourseList(@RequestParam int page, @RequestParam int size) {
        PageInfo<CoursesPO> pageInfo = courseService.getCourse(page, size);
        return R.success(pageInfo.getList(), String.valueOf(pageInfo.getTotal()));
    }

    /**
     * 获取普通课程列表（分页）
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/course/noseckill")
    public R getCourseNoSeckillList(@RequestParam int page, @RequestParam int size) {
        PageInfo<CoursesPO> pageInfo = courseService.getNoSeckillCourse(page, size);
        return R.success(pageInfo.getList(), String.valueOf(pageInfo.getTotal()));
    }

    /**
     * 获取秒杀课程列表（分页）
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/course/seckill")
    public R getCourseSeckillList(@RequestParam int page, @RequestParam int size) {
        PageInfo<CoursesPO> pageInfo = courseService.getSeckillCourse(page, size);
        return R.success(pageInfo.getList(), String.valueOf(pageInfo.getTotal()));
    }

    /**
     * 获取课程列表(不分页)
     * @return
     */
    @GetMapping("/course/list")
    public R getCourseList() {
        List<CoursesPO> list = courseService.getCourseList();
        return R.success(list);
    }

    /**
     * 获取搜索的数据(仅普通课程)
     */
    @GetMapping("/course/search/{searchText}")
    public R getCourseSearch(@RequestParam int page, @RequestParam int size, @PathVariable String searchText) {
        PageInfo<CoursesPO> pageInfo = courseService.getSearchCourse(page, size, searchText);
        return R.success(pageInfo.getList(), String.valueOf(pageInfo.getTotal()));
    }

    /**
     * 获取搜索的数据
     */
    @GetMapping("/course/search/all/{searchText}")
    public R getAllCourseSearch(@RequestParam int page, @RequestParam int size, @PathVariable String searchText) {
        PageInfo<CoursesPO> pageInfo = courseService.getSearchAllCourse(page, size, searchText);
        return R.success(pageInfo.getList(), String.valueOf(pageInfo.getTotal()));
    }


    /**
     * 更新课程信息
     * @param coursePO
     * @return
     */
    @PutMapping("/course")
    @RoleRequire({"ADMIN"})
    public R updateCourse(@RequestBody CoursesPO coursePO) {
        int i = courseService.updateCourse(coursePO);
        if(i == 2){
            //课程代码重复
            return R.error("课程代码重复");
        }else if (i != 1) {
            return R.error("error");
        }
        return R.success("success");
    }

    /**
     * 删除课程
     * @param id
     * @return
     **/
    @DeleteMapping("/course/{id}")
    @RoleRequire({"ADMIN"})
    public R deleteCourse(@PathVariable Integer id) {
        int i = courseService.deleteCourseById(id);
        if(i != 1) {
            return R.error("error");
        }
        return R.success("success");
    }

    /**
     * 新增课程
     * @param coursePO
     * @return
    **/
    @PostMapping("/course")
    @RoleRequire({"ADMIN"})
    public R insertCourse(@RequestBody CoursesPO coursePO) {
        System.out.println(coursePO);
        int i = courseService.insertCourse(coursePO);
        if (i == 2) {
            return R.error("课程代码重复");
        }
        if(i != 1) {
            return R.error("error");
        }
        return R.success("success");
    }

    /**
     * 获取指定教师课程列表(分页)
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/course/{tno}")
    public R getTeacherCourseList(@PathVariable Long tno, @RequestParam int page, @RequestParam int size) {
        PageInfo<CoursesPO> pageInfo = courseService.getTeacherCourse(page, size, tno);
        return R.success(pageInfo.getList(), String.valueOf(pageInfo.getTotal()));
    }

    /**
     * 获取指定教师课程列表(分页),只获取有考试安排的课程
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/course/exam/{tno}")
    @RoleRequire("TEACHER")
    public R getTeacherCourseListByExam(@PathVariable Long tno, @RequestParam int page, @RequestParam int size) {
        PageInfo<CoursesPO> pageInfo = courseService.getTeacherCourseByExam(page, size, tno);
        return R.success(pageInfo.getList(), String.valueOf(pageInfo.getTotal()));
    }

    @GetMapping("/course/all")
    public R getAllCourseList() {
        List<Courses> coursesList = courseService.getAllCourseList();
        return R.success(coursesList);
    }

}
