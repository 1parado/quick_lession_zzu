package com.ly.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ly.mapper.*;
import com.ly.po.CourseGradePO;
import com.ly.pojo.Courses;
import com.ly.pojo.Grade;
import com.ly.pojo.Selections;
import com.ly.pojo.Students;
import com.ly.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class GradeServiceImpl implements GradeService {

    @Autowired
    private GradeMapper gradeMapper;

    @Autowired
    private StudentsMapper studentsMapper;

    @Autowired
    private TeachersMapper teachersMapper;

    @Autowired
    private CoursesMapper coursesMapper;

    @Autowired
    private SelectionsMapper selectionsMapper;

    @Override
    public PageInfo<CourseGradePO> getGradePOBySno(Long sno, int page, int size) {
        PageHelper.startPage(page, size);

        //根据course_code连表查询
        List<CourseGradePO> gradePOList = gradeMapper.selectPOBySno(sno);

        PageInfo<CourseGradePO> pageInfo = PageInfo.of(gradePOList);
        return pageInfo;
    }

    @Override
    public int check(Long no, String code) {
        if (no == null) {
            return 2;
        }
        Students user = studentsMapper.selectBySno(no);
        if (user == null) {
            //说明该编号不是学号, 是教师
            //判断教师是否教授该课程
            Integer teacherId = teachersMapper.selectIdByTno(no);
            Courses courses = coursesMapper.selectByCourseCodeAndTeacherId(code, teacherId);
            if (courses == null) {
                //没有找到，说明该教师没有教授这门课，不许加入
                return 3;
            }
        } else {
            //说明该编号是学号，是学生

            //判断给学生对于该课程是否选择过
            Integer studentId = studentsMapper.selectIdBySno(no);
            Integer courseId = coursesMapper.selectIdByCourseCode(code);
            Selections selections = selectionsMapper.selectByStudentIdAndCourseId(courseId, studentId);
            if (selections == null) {
                //没有选择不许加入
                return 3;
            }

            //判断是否在成绩表中有相关课程（代码）的记录
            /*Grade grade = gradeMapper.selectBySnoByCourseCode(no, code);
            if (grade == null) {
                //没有成绩，不许加入
                return 3;
            }*/
        }

        return 1;
    }
}
