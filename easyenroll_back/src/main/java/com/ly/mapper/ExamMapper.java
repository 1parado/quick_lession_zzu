package com.ly.mapper;

import com.ly.po.ExamPO;
import com.ly.pojo.Exam;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Exam record);

    int insertSelective(Exam record);

    Exam selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Exam record);

    int updateByPrimaryKey(Exam record);

    ExamPO selectPOByCourseId(int courseId);

    int updateByCourseCode(Exam exam);

    List<ExamPO> selectPOAll();


    int deleteByCourseCode(String courseCode);

    List<ExamPO> selectPOAllByCourseCodes(List<String> courseCodes);
}