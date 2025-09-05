package com.ly.mapper;

import com.ly.pojo.PreSelection;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreSelectionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PreSelection record);

    int insertSelective(PreSelection record);

    PreSelection selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PreSelection record);

    int updateByPrimaryKey(PreSelection record);

    void deleteByStudentId(int studentId);

    List<Integer> selectStudentIdsByCourse(String courseCode);

    void deleteByStudentIdAndCourseCode(Integer studentId, String courseCode);
}