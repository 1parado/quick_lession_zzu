package com.ly.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


/**
 * exam
 */

public class Exam implements Serializable {
    private Integer id;

    /**
     * 考试代码
     */
    private String examCode;

    /**
     * 课程代码
     */
    private String courseCode;

    /**
     * 考试开始时间
     */
    private Date startTime;

    /**
     * 考试结束时间
     */
    private Date endTime;

    /**
     * 考试状态（1正常，0取消，2延迟）
     */
    private Integer state;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExamCode() {
        return examCode;
    }

    public void setExamCode(String examCode) {
        this.examCode = examCode;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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
        Exam exam = (Exam) o;
        return Objects.equals(id, exam.id) && Objects.equals(examCode, exam.examCode) && Objects.equals(courseCode, exam.courseCode) && Objects.equals(startTime, exam.startTime) && Objects.equals(endTime, exam.endTime) && Objects.equals(state, exam.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, examCode, courseCode, startTime, endTime, state);
    }

    @Override
    public String toString() {
        return "Exam{" +
                "id=" + id +
                ", examCode='" + examCode + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", state=" + state +
                '}';
    }
}