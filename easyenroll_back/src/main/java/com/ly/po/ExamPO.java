package com.ly.po;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class ExamPO implements Serializable {
    /**
     * 课程id
     */
    private int id;
    /**
     * 课程代码
     */
    private String courseCode;

    /**
     * 课程名称
     */
    private String name;

    /**
     * 课程学分
     */
    private Double credit;

    /**
     * 课程类型（必修/选修/通识）
     */
    private String courseType;

    /**
     * 开课学院
     */
    private String college;

    /**
     * 考试开始时间
     */
    private Date examStartTime;

    /**
     * 考试结束时间
     */
    private Date examEndTime;

    /**
     * 考试状态（1正常，0取消，2延迟）
     */
    private Integer state;

    private static final long serialVersionUID = 1L;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public Date getExamStartTime() {
        return examStartTime;
    }

    public void setExamStartTime(Date examStartTime) {
        this.examStartTime = examStartTime;
    }

    public Date getExamEndTime() {
        return examEndTime;
    }

    public void setExamEndTime(Date examEndTime) {
        this.examEndTime = examEndTime;
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
        ExamPO examPO = (ExamPO) o;
        return id == examPO.id && Objects.equals(courseCode, examPO.courseCode) && Objects.equals(name, examPO.name) && Objects.equals(credit, examPO.credit) && Objects.equals(courseType, examPO.courseType) && Objects.equals(college, examPO.college) && Objects.equals(examStartTime, examPO.examStartTime) && Objects.equals(examEndTime, examPO.examEndTime) && Objects.equals(state, examPO.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, courseCode, name, credit, courseType, college, examStartTime, examEndTime, state);
    }

    @Override
    public String toString() {
        return "ExamPO{" +
                "id=" + id +
                ", courseCode='" + courseCode + '\'' +
                ", name='" + name + '\'' +
                ", credit=" + credit +
                ", courseType='" + courseType + '\'' +
                ", college='" + college + '\'' +
                ", examStartTime=" + examStartTime +
                ", examEndTime=" + examEndTime +
                ", state=" + state +
                '}';
    }
}
