package com.ly.exportData;

import com.alibaba.excel.annotation.ExcelProperty;

import java.util.Objects;

public class GradeExportData {
    @ExcelProperty("课程名")
    private String name;

    @ExcelProperty("课程代码")
    private String courseCode;

    @ExcelProperty("成绩")
    private Double grade;

    @ExcelProperty("状态")
    private String stateText;

    public GradeExportData() {
    }

    public GradeExportData(String name, String courseCode, Double grade, String stateText) {
        this.name = name;
        this.courseCode = courseCode;
        this.grade = grade;
        this.stateText = stateText;
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

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public String getStateText() {
        return stateText;
    }

    public void setStateText(String stateText) {
        this.stateText = stateText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GradeExportData that = (GradeExportData) o;
        return Objects.equals(name, that.name) && Objects.equals(courseCode, that.courseCode) && Objects.equals(grade, that.grade) && Objects.equals(stateText, that.stateText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, courseCode, grade, stateText);
    }

    @Override
    public String toString() {
        return "GradeExportData{" +
                "name='" + name + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", grade=" + grade +
                ", stateText='" + stateText + '\'' +
                '}';
    }
}
