package com.ly.po;

import java.io.Serializable;
import java.util.Date;

public class TaskCoursePO implements Serializable {
    private static final long serialVersionUID = 1L;

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
    private Object courseType;

    /**
     * 开课学院
     */
    private String college;

    /**
     * 容量
     */
    private Integer capacity;

    /**
     * 剩余名额
     */
    private Integer remain;


    /**
     * 开课学期
     */
    private String semester;

    /**
     * 课程描述
     */
    private String description;

    /**
     * 教师姓名
     */
    private String teacherName;

    /**
     * 上课时间（eg：每周一，第一、二节课）
     */
    private String classTime;

    /**
     * 上课地点（ge：勤学楼A-602）
     */
    private String classLocation;

    /**
     * 周次范围i（eg：1-16周）
     */
    private String weekRange;

    /**
     * 前置课程代码
     */
    private String preCourseCode;

    /**
     * 任课教师工号
     */
    private Long tno;

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

    public Object getCourseType() {
        return courseType;
    }

    public void setCourseType(Object courseType) {
        this.courseType = courseType;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
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

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getClassTime() {
        return classTime;
    }

    public void setClassTime(String classTime) {
        this.classTime = classTime;
    }

    public String getClassLocation() {
        return classLocation;
    }

    public void setClassLocation(String classLocation) {
        this.classLocation = classLocation;
    }

    public String getWeekRange() {
        return weekRange;
    }

    public void setWeekRange(String weekRange) {
        this.weekRange = weekRange;
    }

    public String getPreCourseCode() {
        return preCourseCode;
    }

    public void setPreCourseCode(String preCourseCode) {
        this.preCourseCode = preCourseCode;
    }

    public Long getTno() {
        return tno;
    }

    public void setTno(Long tno) {
        this.tno = tno;
    }

    @Override
    public String toString() {
        return "TaskCoursePO{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", status='" + status + '\'' +
                ", store=" + store +
                ", courseCode='" + courseCode + '\'' +
                ", name='" + name + '\'' +
                ", credit=" + credit +
                ", courseType=" + courseType +
                ", college='" + college + '\'' +
                ", capacity=" + capacity +
                ", remain=" + remain +
                ", semester='" + semester + '\'' +
                ", description='" + description + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", classTime='" + classTime + '\'' +
                ", classLocation='" + classLocation + '\'' +
                ", weekRange='" + weekRange + '\'' +
                ", preCourseCode='" + preCourseCode + '\'' +
                ", tno=" + tno +
                '}';
    }
}
