package com.ly.mapper;

import com.ly.dto.GradeDTO;
import com.ly.po.CourseGradePO;
import com.ly.pojo.Grade;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Grade record);

    int insertSelective(Grade record);

    Grade selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Grade record);

    int updateByPrimaryKey(Grade record);

    Grade selectBySnoByCourseCode(Long sno, String courseCode);

    List<CourseGradePO> selectPOBySno(Long sno);

    List<GradeDTO> selectGradeDTOBySno(Long sno);

    Double selectAvgGradesBySno(Long sno);
}