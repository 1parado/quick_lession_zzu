package com.ly.service;

import com.github.pagehelper.PageInfo;
import com.ly.po.CoursesPO;
import com.ly.po.ExamPO;

public interface ExamService {
    ExamPO getExamPOByCourseId(int courseId);


    int insertExam(ExamPO examPO);

    int updateExam(ExamPO examPO);

    PageInfo<ExamPO> getAllExam(int page, int size);

    int deleteExamByCourseId(int courseId);

    int containtCourse(int courseId, Long tno);

    PageInfo<ExamPO> getAllExamByStudent(Long sno, int page, int size);

}
