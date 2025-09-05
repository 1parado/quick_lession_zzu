package com.ly.dto;

import com.ly.pojo.Courses;
import com.ly.pojo.Students;

import java.util.List;

public class BatchSelectDTO {
    private List<Students> selectedStudents;
    private List<Courses> selectedCourses;

    public List<Students> getSelectedStudents() {
        return selectedStudents;
    }

    public void setSelectedStudents(List<Students> selectedStudents) {
        this.selectedStudents = selectedStudents;
    }

    public List<Courses> getSelectedCourses() {
        return selectedCourses;
    }

    public void setSelectedCourses(List<Courses> selectedCourses) {
        this.selectedCourses = selectedCourses;
    }
}
