package com.ly.pojo;

import java.io.Serializable;
import java.util.Objects;


/**
 * pre_selection
 */

public class PreSelection implements Serializable {
    private Integer id;

    /**
     * 学生id
     */
    private Integer studentId;

    /**
     * 课程代码
     */
    private String courseCode;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PreSelection that = (PreSelection) o;
        return Objects.equals(id, that.id) && Objects.equals(studentId, that.studentId) && Objects.equals(courseCode, that.courseCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, studentId, courseCode);
    }

    @Override
    public String toString() {
        return "PreSelection{" +
                "id=" + id +
                ", studentId=" + studentId +
                ", courseCode='" + courseCode + '\'' +
                '}';
    }
}