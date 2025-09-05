package com.ly.service;

import com.github.pagehelper.PageInfo;
import com.ly.po.StudentsGradePO;
import com.ly.pojo.Students;
import com.ly.result.R;

import java.util.List;

public interface StudentService {
    Students selectStudentById(Integer id);

    Students getStudentBySno(Long account);

    PageInfo<Students> getStudent(int page, int size);

    int updateStudent(Students student);

    int deleteStudentById(Integer id);

    int insertStudent(Students students);

    int getStudentIdByAccount(Long account);

    List<Students> selectStudentByKeyword(String keyword);

    PageInfo<Students> getStudentByCourseId(int page, int size, Integer courseId);

    PageInfo<Students> getSearchStudent(int page, int size, String searchText);

    PageInfo<StudentsGradePO> getStudentGradeByCourseId(int page, int size, Integer courseId);
}
