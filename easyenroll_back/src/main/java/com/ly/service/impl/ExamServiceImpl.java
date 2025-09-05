package com.ly.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ly.mapper.*;
import com.ly.po.CoursesPO;
import com.ly.po.ExamPO;
import com.ly.pojo.Exam;
import com.ly.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ExamServiceImpl implements ExamService {

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private CoursesMapper coursesMapper;

    @Autowired
    private TeachersMapper teachersMapper;

    @Autowired
    private StudentsMapper studentsMapper;

    @Autowired
    private SelectionsMapper selectionsMapper;

    @Override
    public ExamPO getExamPOByCourseId(int courseId) {
        return examMapper.selectPOByCourseId(courseId);
    }

    @Override
    public int insertExam(ExamPO examPO) {
        Exam exam = new Exam();
        exam.setExamCode(examPO.getCourseCode() + System.currentTimeMillis());
        exam.setCourseCode(examPO.getCourseCode());
        exam.setStartTime(examPO.getExamStartTime());
        exam.setEndTime(examPO.getExamEndTime());
        if (examPO.getState() == null) {
            exam.setState(1);
        } else {
            exam.setState(examPO.getState());
        }
        return examMapper.insert(exam);
    }

    @Override
    public int updateExam(ExamPO examPO) {
        Exam exam = new Exam();
        exam.setExamCode(examPO.getCourseCode() + System.currentTimeMillis());
        exam.setCourseCode(examPO.getCourseCode());
        exam.setStartTime(examPO.getExamStartTime());
        exam.setEndTime(examPO.getExamEndTime());
        if (examPO.getState() == null) {
            exam.setState(1);
        } else {
            exam.setState(examPO.getState());
        }
        return examMapper.updateByCourseCode(exam);
    }

    @Override
    public PageInfo<ExamPO> getAllExam(int page, int size) {
        PageHelper.startPage(page, size);

        List<ExamPO> examPOList = examMapper.selectPOAll();

        PageInfo<ExamPO> pageInfo = PageInfo.of(examPOList);
        return pageInfo;
    }

    @Override
    public PageInfo<ExamPO> getAllExamByStudent(Long sno, int page, int size) {
        PageHelper.startPage(page, size);

        //根据sno获得学生id（student表）
        Integer studentId = studentsMapper.selectIdBySno(sno);
        //根据学生id获得课程id集合(selection表)
        List<Integer> courseIds = selectionsMapper.selectCourseIdsByStudentId(studentId);
        //根据课程id集合获得课程代码集合
        List<String> courseCodes = new ArrayList<>();
        for (Integer courseId : courseIds) {
            String courseCode = coursesMapper.selectCourseCodeByIdPO(courseId);
            courseCodes.add(courseCode);
        }
        // 根据course_code集合获得examPO集合（exam表）
        List<ExamPO> examPOList = examMapper.selectPOAllByCourseCodes(courseCodes);

        PageInfo<ExamPO> pageInfo = PageInfo.of(examPOList);
        return pageInfo;
    }

    @Override
    public int deleteExamByCourseId(int courseId) {
        String courseCode = coursesMapper.selectCourseCodeByIdPO(courseId);
        return examMapper.deleteByCourseCode(courseCode);
    }

    @Override
    public int containtCourse(int courseId, Long tno) {
        //根据tno找到所对应的courseid集合
        Integer teacherId = teachersMapper.selectIdByTno(tno);
        List<Integer> courseIds = coursesMapper.selectIdByTeacherId(teacherId);
        //遍历该集合
        //判断是否和courseId相等，只要有相等的就OK
        for (Integer id : courseIds) {
            if (id == courseId) {
                return 0;
            }
        }
        return 1;
    }


}
