package com.ly.mapper;

import com.ly.pojo.CourseRating;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRatingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CourseRating record);

    int insertSelective(CourseRating record);

    CourseRating selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CourseRating record);

    int updateByPrimaryKey(CourseRating record);

    List<CourseRating> selectAll();

    List<CourseRating> selectByCourseCodeList(List<String> courseCodeList);

    void deleteAll();
}