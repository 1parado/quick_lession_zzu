package com.ly.web;

import com.github.pagehelper.PageInfo;
import com.ly.annotation.RoleRequire;
import com.ly.po.CoursesPO;
import com.ly.po.ExamPO;
import com.ly.result.R;
import com.ly.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exam")
public class ExamController {

    @Autowired
    private ExamService examService;

    /**
     * 根据课程id去获取考试安排数据PO
     */
    @GetMapping("/exam/course/{courseId}")
    @RoleRequire("TEACHER")
    public R getExamPO(@PathVariable int courseId) {

        ExamPO examPO = examService.getExamPOByCourseId(courseId);
        if (examPO != null){
            return R.success(examPO);
        } else {
            return R.error("获取失败");
        }
    }

    /**
     * 获得全部的考试安排数据（PO）
     */
    @GetMapping("/exam/course")
    @RoleRequire("TEACHER")
    public R getAllExamPO(@RequestParam int page, @RequestParam int size) {
        PageInfo<ExamPO> pageInfo = examService.getAllExam(page, size);
        return R.success(pageInfo.getList(), String.valueOf(pageInfo.getTotal()));
    }

    /**
     * 获得指定学生对应的全部的考试安排数据（PO）
     */
    @GetMapping("/exam/{sno}")
    @RoleRequire("STUDENT")
    public R getAllExamPOByStudent(@PathVariable Long sno, @RequestParam int page, @RequestParam int size) {
        PageInfo<ExamPO> pageInfo = examService.getAllExamByStudent(sno, page, size);
        return R.success(pageInfo.getList(), String.valueOf(pageInfo.getTotal()));
    }

    /**
     * 新增考试安排
     * @param examPO
     * @return
     */
    @PostMapping("/exam")
    @RoleRequire("TEACHER")
    public R saveExam(@RequestBody ExamPO examPO) {
        int i = examService.insertExam(examPO);
        if (i == 1) {
            return R.success("保存成功");
        } else {
            return R.error("保存失败");
        }
    }

    /**
     * 修改考试安排
     * @param examPO
     * @return
     */
    @PutMapping("/exam")
    @RoleRequire("TEACHER")
    public R updateExam(@RequestBody ExamPO examPO) {
        int i = examService.updateExam(examPO);
        if (i == 1) {
            return R.success("保存成功");
        } else {
            return R.error("保存失败");
        }
    }

    /**
     * 删除指定考试安排
     */
    @DeleteMapping("/exam/{courseId}/{tno}")
    @RoleRequire("TEACHER")
    public R deleteExam(@PathVariable int courseId, @PathVariable Long tno) {
        //先判断这个教师工号所教授的课程中是否包含该课程id，若不，直接返回错误
        int j = examService.containtCourse(courseId, tno);
        if (j == 1) {
            //包含
            return R.error("无权限");
        }
        int i = examService.deleteExamByCourseId(courseId);
        if (i == 1) {
            return R.success("删除成功");
        } else {
            return R.error("删除失败");
        }
    }


}
