package com.ly.service;

import com.github.pagehelper.PageInfo;
import com.ly.dto.SelectionAndCode;
import com.ly.po.CoursesPO;
import com.ly.pojo.Courses;
import com.ly.pojo.Selections;
import com.ly.pojo.Students;

import java.util.List;

public interface SelectionService {
    int saveNoSeckillSelection(Selections selections);

    List<Selections> getListByStudentId(Integer studentId);

    List<SelectionAndCode> getListCodeByStudentId(Integer studentId);



    int deleteStatusByCourseIdAndStudentId(Integer courseId, Integer studentId);


    PageInfo<CoursesPO> getCourseByNormal(int page, int size, Integer studentId);

    boolean isSeckilled(Long studentSno, String courseCode);

    Integer batchSelect(List<Students> selectedStudents, List<Courses> selectedCourses);

    PageInfo<CoursesPO> getTeacherCourseByNormal(int page, int size, Long tno);

    List<CoursesPO> getCourseByNormalNoPage(Long account);

    List<CoursesPO> getTeacherCourseByNormalNoPage(Long tno);

    List<CoursesPO> getCourseByNormalFull(Integer studentId);

    String getRelatedCoursesStats(String courseName);
}
