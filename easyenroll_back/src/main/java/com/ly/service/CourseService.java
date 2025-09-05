package com.ly.service;

import com.github.pagehelper.PageInfo;
import com.ly.po.CoursesPO;
import com.ly.pojo.Courses;

import java.util.List;

public interface CourseService {
    PageInfo<CoursesPO> getCourse(int page, int size);

    int updateCourse(CoursesPO coursePO);

    int deleteCourseById(Integer id);

    int insertCourse(CoursesPO coursePO);

    List<CoursesPO> getCourseList();

    PageInfo<CoursesPO> getSeckillCourse(int page, int size);

    PageInfo<CoursesPO> getNoSeckillCourse(int page, int size);

    PageInfo<CoursesPO> getTeacherCourse(int page, int size, Long tno);

    PageInfo<CoursesPO> getSearchCourse(int page, int size, String searchText);

    PageInfo<CoursesPO> getSearchAllCourse(int page, int size, String searchText);

    PageInfo<CoursesPO> getTeacherCourseByExam(int page, int size, Long tno);

    List<Courses> getAllCourseList();
}
