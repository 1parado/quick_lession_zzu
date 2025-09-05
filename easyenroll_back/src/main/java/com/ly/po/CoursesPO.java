package com.ly.po;

import java.io.Serializable;
import java.util.Objects;

public class CoursesPO implements Serializable {

    private Integer id;

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
     * 容量
     */
    private Integer capacity;

    /**
     * 剩余名额
     */
    private Integer remain;

    /**
     * 是否是秒杀课程，是则为1，否则为0
     */
    private Integer isSeckill;

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

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "CoursesPO{" +
                "id=" + id +
                ", courseCode='" + courseCode + '\'' +
                ", name='" + name + '\'' +
                ", credit=" + credit +
                ", courseType=" + courseType +
                ", college='" + college + '\'' +
                ", capacity=" + capacity +
                ", remain=" + remain +
                ", isSeckill=" + isSeckill +
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoursesPO coursesPO = (CoursesPO) o;
        return Objects.equals(id, coursesPO.id) && Objects.equals(courseCode, coursesPO.courseCode) && Objects.equals(name, coursesPO.name) && Objects.equals(credit, coursesPO.credit) && Objects.equals(courseType, coursesPO.courseType) && Objects.equals(college, coursesPO.college) && Objects.equals(capacity, coursesPO.capacity) && Objects.equals(remain, coursesPO.remain) && Objects.equals(isSeckill, coursesPO.isSeckill) && Objects.equals(semester, coursesPO.semester) && Objects.equals(description, coursesPO.description) && Objects.equals(teacherName, coursesPO.teacherName) && Objects.equals(classTime, coursesPO.classTime) && Objects.equals(classLocation, coursesPO.classLocation) && Objects.equals(weekRange, coursesPO.weekRange) && Objects.equals(preCourseCode, coursesPO.preCourseCode) && Objects.equals(tno, coursesPO.tno);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, courseCode, name, credit, courseType, college, capacity, remain, isSeckill, semester, description, teacherName, classTime, classLocation, weekRange, preCourseCode, tno);
    }

    public Long getTno() {
        return tno;
    }

    public void setTno(Long tno) {
        this.tno = tno;
    }

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

    public Integer getIsSeckill() {
        return isSeckill;
    }

    public void setIsSeckill(Integer isSeckill) {
        this.isSeckill = isSeckill;
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
}
