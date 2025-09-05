package com.ly.exportData;

import com.alibaba.excel.annotation.ExcelProperty;

public class CourseListExportData {
    @ExcelProperty("ID")
    private Integer id;

    /**
     * 课程代码
     */
    @ExcelProperty("课程代码")
    private String courseCode;

    /**
     * 课程名称
     */
    @ExcelProperty("课程名")
    private String name;

    /**
     * 课程学分
     */
    @ExcelProperty("课程学分")
    private Double credit;

    /**
     * 课程类型（必修/选修/通识）
     */
    @ExcelProperty("课程类型")
    private String courseType;

    /**
     * 开课学院
     */
    @ExcelProperty("学院")
    private String college;

    /**
     * 容量
     */
    @ExcelProperty("容量")
    private Integer capacity;

    /**
     * 剩余名额
     */
    @ExcelProperty("剩余名额")
    private Integer remain;

    /**
     * 课程描述
     */
    @ExcelProperty("课程描述")
    private String description;

    /**
     * 是否是秒杀课程，是则为1，否则为0
     */
    @ExcelProperty("是否需要抢选")
    private Integer isSeckill;

    /**
     * 教师姓名
     */
    @ExcelProperty("任课教师姓名")
    private String teacherName;

    /**
     * 任课教师工号
     */
    @ExcelProperty("任课教师工号")
    private Long tno;

    /**
     * 开课学期
     */
    @ExcelProperty("开设学期")
    private String semester;

    /**
     * 上课时间（eg：每周一，第一、二节课）
     */
    @ExcelProperty("上课时间")
    private String classTime;

    /**
     * 上课地点（ge：勤学楼A-602）
     */
    @ExcelProperty("上课地点")
    private String classLocation;

    /**
     * 周次范围i（eg：1-16周）
     */
    @ExcelProperty("周次范围")
    private String weekRange;

    /**
     * 前置课程代码
     */
    @ExcelProperty("前置课程代码")
    private String preCourseCode;

    @Override
    public String toString() {
        return "CourseListExportData{" +
                "id=" + id +
                ", courseCode='" + courseCode + '\'' +
                ", name='" + name + '\'' +
                ", credit=" + credit +
                ", courseType=" + courseType +
                ", college='" + college + '\'' +
                ", capacity=" + capacity +
                ", remain=" + remain +
                ", description='" + description + '\'' +
                ", isSeckill=" + isSeckill +
                ", teacherName='" + teacherName + '\'' +
                ", tno=" + tno +
                ", semester='" + semester + '\'' +
                ", classTime='" + classTime + '\'' +
                ", classLocation='" + classLocation + '\'' +
                ", weekRange='" + weekRange + '\'' +
                ", preCourseCode='" + preCourseCode + '\'' +
                '}';
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIsSeckill() {
        return isSeckill;
    }

    public void setIsSeckill(Integer isSeckill) {
        this.isSeckill = isSeckill;
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

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
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
