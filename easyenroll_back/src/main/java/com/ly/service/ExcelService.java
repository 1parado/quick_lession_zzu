package com.ly.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ExcelService {
    void exportCourseTable(Long account, String role, String currentSemester, HttpServletResponse response) throws Exception;

    void exportStudentList(Integer courseId, HttpServletResponse response) throws Exception;

    void exportCourseList(HttpServletResponse response) throws Exception;

    void exportStudentListAll(HttpServletResponse response) throws Exception;

    void exportTeacherList(HttpServletResponse response) throws Exception;

    void exportStudentTemplate(HttpServletResponse response) throws IOException;

    void importStudent(MultipartFile file) throws IOException;

    void importTeacher(MultipartFile file) throws IOException;

    void exportTeacherTemplate(HttpServletResponse response) throws IOException;

    void importStudentGrade(MultipartFile file) throws IOException;

    void exportStudentGradeTemplate(HttpServletResponse response) throws IOException;

    void exportGradeBySno(Long sno, HttpServletResponse response) throws Exception;
}
