package com.ly.web;

import com.github.pagehelper.PageInfo;
import com.ly.annotation.RoleRequire;
import com.ly.po.StudentsGradePO;
import com.ly.pojo.Students;
import com.ly.result.R;
import com.ly.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/stu")
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * 获取学生列表
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/student")
    @RoleRequire({"ADMIN","STUDENT"})
    public R getStudentList(@RequestParam int page, @RequestParam int size) {
        PageInfo<Students> pageInfo = studentService.getStudent(page, size);
        return R.success(pageInfo.getList(), String.valueOf(pageInfo.getTotal()));
    }

    /**
     * 搜索学生
     */
    @GetMapping("/student/search/{searchText}")
    @RoleRequire("ADMIN")
    public R searchStudentList(@RequestParam int page, @RequestParam int size, @PathVariable String searchText) {
        PageInfo<Students> pageInfo = studentService.getSearchStudent(page, size, searchText);
        return R.success(pageInfo.getList(), String.valueOf(pageInfo.getTotal()));
    }

    /**
     * 获取学生列表（指定课程id,教师tno）
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/student/{courseId}")
    @RoleRequire({"TEACHER"})
    public R getStudentListByCourseId(
            @PathVariable Integer courseId,
            @RequestParam int page,
            @RequestParam int size) {
        //思路：根据courseId去查询selections表，得到学生id集合
        //根据学生的id集合，拿到学生列表
        PageInfo<Students> pageInfo = studentService.getStudentByCourseId(page, size, courseId);
        return R.success(pageInfo.getList(), String.valueOf(pageInfo.getTotal()));
    }
    /**
     * 获取学生成绩列表（根据课程））
     */
    @GetMapping("/student/exam/{courseId}")
    @RoleRequire({"TEACHER"})
    public R getStudentGradeListByCourseId(
            @PathVariable Integer courseId,
            @RequestParam int page,
            @RequestParam int size) {
        //思路：根据courseId去查询selections表，得到学生id集合
        //根据学生的id集合，拿到学生列表
        PageInfo<StudentsGradePO> pageInfo = studentService.getStudentGradeByCourseId(page, size, courseId);

        return R.success(pageInfo.getList(), String.valueOf(pageInfo.getTotal()));
    }

    /**
     * 根据账号拿id
     * @param account
     * @return
     */
    @GetMapping("/student/getid/{account}")
    public R getStudentIdByAccount(@PathVariable Long account){
        int id = studentService.getStudentIdByAccount(account);
        return R.success(id);
    }

    /**
     * 更新学生信息
     * @param student
     * @return
     */
    @PutMapping("/student")
    @RoleRequire("ADMIN")
    public R updateStudent(@RequestBody Students student) {
        int i = studentService.updateStudent(student);
        if(i == 2){
            //学号重复
            return R.error("学号重复");
        }else if (i != 1) {
            return R.error("error");
        }
        return R.success("success");
    }

    /**
     * 删除学生
     * @param id
     * @return
     */
    @DeleteMapping("/student/{id}")
    @RoleRequire("ADMIN")
    public R deleteStudent(@PathVariable Integer id) {
        int i = studentService.deleteStudentById(id);
        if(i != 1) {
            return R.error("error");
        }
        return R.success("success");
    }

    /**
     * 新增学生
     * @param students
     * @return
     */
    @PostMapping("/student")
    @RoleRequire("ADMIN")
    public R insertStudent(@RequestBody Students students) {
        System.out.println(students);
        int i = studentService.insertStudent(students);
        if (i == 2) {
            return R.error("学号重复");
        }
        if(i != 1) {
            return R.error("error");
        }
        return R.success("success");
    }

    /**
     * 获取联想搜索数据
     * @param keyword
     * @return
     */
    @GetMapping("/student/suggestions")
    @RoleRequire("ADMIN")
    public R suggestions(@RequestParam String keyword) {
        List<Students> studentsList = studentService.selectStudentByKeyword(keyword);
        return R.success(studentsList);
    }


}
