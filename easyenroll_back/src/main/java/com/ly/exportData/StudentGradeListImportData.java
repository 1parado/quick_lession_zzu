package com.ly.exportData;

import com.alibaba.excel.annotation.ExcelProperty;

import java.util.Objects;

public class StudentGradeListImportData {
    @ExcelProperty("课程代码")
    private String courseCode;

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("学号")
    private String sno;

    @ExcelProperty("成绩")
    private Double grade;

    @ExcelProperty("考试状态")
    private int state; //1正常，0缺考，2作弊


    public StudentGradeListImportData() {
    }

    public StudentGradeListImportData(String courseCode, String name, String sno, Double grade, int state) {
        this.courseCode = courseCode;
        this.name = name;
        this.sno = sno;
        this.grade = grade;
        this.state = state;
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

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentGradeListImportData that = (StudentGradeListImportData) o;
        return state == that.state && Objects.equals(courseCode, that.courseCode) && Objects.equals(name, that.name) && Objects.equals(sno, that.sno) && Objects.equals(grade, that.grade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseCode, name, sno, grade, state);
    }

    @Override
    public String toString() {
        return "StudentGradeListImportData{" +
                "courseCode='" + courseCode + '\'' +
                ", name='" + name + '\'' +
                ", sno=" + sno +
                ", grade=" + grade +
                ", state=" + state +
                '}';
    }
}
