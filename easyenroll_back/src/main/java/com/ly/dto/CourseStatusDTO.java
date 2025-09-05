package com.ly.dto;

import java.io.Serializable;

public class CourseStatusDTO {
    private int courseId;
    private String status;

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
