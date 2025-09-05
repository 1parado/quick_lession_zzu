package com.ly.pojo;

import java.io.Serializable;
import java.util.Objects;

/**
 * course_rating
 */

public class CourseRating implements Serializable {
    private Integer id;

    /**
     * 课程名
     */
    private String courseName;

    /**
     * 课程代码
     */
    private String courseCode;

    /**
     * 综合负载评价
     */
    private Double rating;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseRating that = (CourseRating) o;
        return Objects.equals(id, that.id) && Objects.equals(courseName, that.courseName) && Objects.equals(courseCode, that.courseCode) && Objects.equals(rating, that.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, courseName, courseCode, rating);
    }

    @Override
    public String toString() {
        return "CourseRating{" +
                "id=" + id +
                ", courseName='" + courseName + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", rating=" + rating +
                '}';
    }
}