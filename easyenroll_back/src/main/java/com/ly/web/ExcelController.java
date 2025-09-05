package com.ly.web;

import com.alibaba.excel.EasyExcel;
import com.ly.annotation.RoleRequire;
import com.ly.exportData.StudentListImportData;
import com.ly.listener.StudentDataListener;
import com.ly.mapper.StudentsMapper;
import com.ly.mapper.UsersMapper;
import com.ly.result.R;
import com.ly.service.ExcelService;
import jakarta.servlet.http.HttpServletResponse;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/exc")
public class ExcelController {

    @Autowired
    private ExcelService excelService;


    /**
     * 导出课程表
     * @param account
     * @param role
     * @param currentSemester
     * @param response
     * @throws Exception
     */
    @GetMapping("/export/course/{account}/{role}/{currentSemester}")
    @RoleRequire({"STUDENT","TEACHER"})
    public void exportCourseTable(@PathVariable Long account, @PathVariable String role, @PathVariable String currentSemester, HttpServletResponse response) throws Exception {
        //System.out.println(currentSemester);2025年下学期
        excelService.exportCourseTable(account, role, currentSemester, response);
    }

    /**
     * 导出教师所教课程的学生列表
     * @param courseId
     * @param response
     */
    @GetMapping("/export/student/{courseId}")
    @RoleRequire({"TEACHER"})
    public void exportStudentList(@PathVariable Integer courseId, HttpServletResponse response) throws Exception {
        excelService.exportStudentList(courseId, response);
    }

    /**
     * 导出所有课程信息
     * @param response
     */
    @GetMapping("/export/course")
    @RoleRequire("ADMIN")
    public void exportCourseList(HttpServletResponse response) throws Exception {
        excelService.exportCourseList(response);
    }

    /**
     * 导出所有学生信息
     */
    @GetMapping("/export/student")
    @RoleRequire("ADMIN")
    public void exportStudentList(HttpServletResponse response) throws Exception {
        excelService.exportStudentListAll(response);
    }

    /**
     * 导出学生所有成绩信息
     */
    @GetMapping("/export/grade/{sno}")
    @RoleRequire("STUDENT")
    public void exportGradeList(@PathVariable Long sno, HttpServletResponse response) throws Exception {
        excelService.exportGradeBySno(sno, response);
    }

    /**
     * 导出所有教师信息
     */
    @GetMapping("/export/teacher")
    @RoleRequire("ADMIN")
    public void exportTeacherList(HttpServletResponse response) throws Exception {
        excelService.exportTeacherList(response);
    }

    /**
     * 导入学生信息
     */
    @PostMapping("/import/student")
    @RoleRequire({"ADMIN", "TEACHER"})
    public R importStudentList(@RequestParam("file") MultipartFile file) {
        try {
            excelService.importStudent(file);
            return R.success("导入完成");
        } catch (Exception e) {
            return R.error("导入失败: " + e.getMessage());
        }
    }

    /**
     * 导入学生成绩信息
     */
    @PostMapping("/import/student/grade")
    @RoleRequire("TEACHER")
    public R importStudentGradeList(@RequestParam("file") MultipartFile file) {
        try {
            excelService.importStudentGrade(file);
            return R.success("导入完成");
        } catch (Exception e) {
            return R.error(500, "导入失败: " + e.getMessage());
        }
    }

    /**
     * 获得学生信息导入模板
     * @param response
     * @throws IOException
     */
    @GetMapping("/template/student")
    public void exportStudentTemplate(HttpServletResponse response) throws IOException {
        excelService.exportStudentTemplate(response);
    }

    /**
     * 获得学生成绩信息导入模板
     * @param response
     * @throws IOException
     */
    @GetMapping("/template/student/grade")
    public void exportStudentGradeTemplate(HttpServletResponse response) throws IOException {
        excelService.exportStudentGradeTemplate(response);
    }

    /**
     * 导入教师信息
     */
    @PostMapping("/import/teacher")
    @RoleRequire("ADMIN")
    public R importTeacherList(@RequestParam("file") MultipartFile file) {
        try {
            excelService.importTeacher(file);
            return R.success("导入完成");
        } catch (Exception e) {
            return R.error("导入失败: " + e.getMessage());
        }
    }

    /**
     * 获得教师信息导入模板
     * @param response
     * @throws IOException
     */
    @GetMapping("/template/teacher")
    public void exportTeacherTemplate(HttpServletResponse response) throws IOException {
        excelService.exportTeacherTemplate(response);
    }



}
