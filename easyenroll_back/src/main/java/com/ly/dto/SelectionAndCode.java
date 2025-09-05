package com.ly.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * selections
 */
public class SelectionAndCode implements Serializable {

    public String courseCode;

    private Integer id;

    /**
     * 学生id
     */
    private Integer studentId;

    /**
     * 课程id
     */
    private Integer courseId;

    /**
     * 学期
     */
    private String semester;

    /**
     * 选课时间
     */
    private Date selectionTime;

    /**
     * 选课状态（正常/已退课）默认为normal
     */
    private Object status;

    /**
     * 是否是秒杀课程，是则为1，否则为0，默认为0
     */
    private Integer isSeckill;

    public SelectionAndCode() {
    }

    public SelectionAndCode(String courseCode, Integer id, Integer studentId, Integer courseId, String semester, Date selectionTime, Object status, Integer isSeckill) {
        this.courseCode = courseCode;
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.semester = semester;
        this.selectionTime = selectionTime;
        this.status = status;
        this.isSeckill = isSeckill;
    }

    private static final long serialVersionUID = 1L;

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

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

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Date getSelectionTime() {
        return selectionTime;
    }

    public void setSelectionTime(Date selectionTime) {
        this.selectionTime = selectionTime;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public Integer getIsSeckill() {
        return isSeckill;
    }

    public void setIsSeckill(Integer isSeckill) {
        this.isSeckill = isSeckill;
    }
}