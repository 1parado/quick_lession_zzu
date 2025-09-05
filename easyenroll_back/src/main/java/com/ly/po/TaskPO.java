package com.ly.po;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class TaskPO implements Serializable {
    /**
     * 秒杀任务ID
     */
    private Integer id;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 课程状态
     */
    private String status;

    /**
     * 名额（放出的名额）
     */
    private Long store;

    /**
     * 容量
     */
    private Integer capacity;

    /**
     * 剩余名额
     */
    private Integer remain;

    /**
     * 课程名称
     */
    private String name;


    /**
     * 课程代码
     */
    private String courseCode;

    /**
     * 开课学院
     */
    private String college;

    /**
     * 开课学期
     */
    private String semester;

    /**
     * 任课教师名称
     */
    private String teacherName;

    /**
     * 任课教师工号
     */
    private Long tno;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getStore() {
        return store;
    }

    public void setStore(Long store) {
        this.store = store;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getRemain() {
        return remain;
    }

    public void setRemain(Integer remain) {
        this.remain = remain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public Long getTno() {
        return tno;
    }

    public void setTno(Long tno) {
        this.tno = tno;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskPO taskPO = (TaskPO) o;
        return Objects.equals(id, taskPO.id) && Objects.equals(startTime, taskPO.startTime) && Objects.equals(endTime, taskPO.endTime) && Objects.equals(status, taskPO.status) && Objects.equals(store, taskPO.store) && Objects.equals(capacity, taskPO.capacity) && Objects.equals(remain, taskPO.remain) && Objects.equals(name, taskPO.name) && Objects.equals(courseCode, taskPO.courseCode) && Objects.equals(college, taskPO.college) && Objects.equals(semester, taskPO.semester) && Objects.equals(teacherName, taskPO.teacherName) && Objects.equals(tno, taskPO.tno);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startTime, endTime, status, store, capacity, remain, name, courseCode, college, semester, teacherName, tno);
    }

    @Override
    public String toString() {
        return "TaskPO{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", status='" + status + '\'' +
                ", store=" + store +
                ", capacity=" + capacity +
                ", remain=" + remain +
                ", name='" + name + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", college='" + college + '\'' +
                ", semester='" + semester + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", tno=" + tno +
                '}';
    }
}
