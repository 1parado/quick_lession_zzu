package com.ly.pojo;

import java.io.Serializable;
import java.util.Objects;


/**
 * grade
 */

public class Grade implements Serializable {
    private Integer id;

    /**
     * 课程代码
     */
    private String courseCode;

    /**
     * 学生学号
     */
    private Long sno;

    /**
     * 成绩
     */
    private Double grade;

    /**
     * 考试结果状态（1正常，0缺考，2作弊）
     */
    private Integer state;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public Long getSno() {
        return sno;
    }

    public void setSno(Long sno) {
        this.sno = sno;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grade grade1 = (Grade) o;
        return Objects.equals(id, grade1.id) && Objects.equals(courseCode, grade1.courseCode) && Objects.equals(sno, grade1.sno) && Objects.equals(grade, grade1.grade) && Objects.equals(state, grade1.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, courseCode, sno, grade, state);
    }

    @Override
    public String toString() {
        return "Grade{" +
                "id=" + id +
                ", courseCode='" + courseCode + '\'' +
                ", sno=" + sno +
                ", grade=" + grade +
                ", state=" + state +
                '}';
    }
}