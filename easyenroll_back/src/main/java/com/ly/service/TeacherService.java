package com.ly.service;

import com.github.pagehelper.PageInfo;
import com.ly.pojo.Teachers;

import java.util.List;

public interface TeacherService {
    Teachers getTeacherByTno(Long account);

    PageInfo<Teachers> getTeacher(int page, int size);

    int updateTeacher(Teachers teachers);

    int deleteTeacherById(Integer id);

    int insertTeacher(Teachers teachers);

    int deleteOnCourseById(Integer id);

    List<Teachers> getTeacherList();

    PageInfo<Teachers> searchTeacher(int page, int size, String searchText);
}
