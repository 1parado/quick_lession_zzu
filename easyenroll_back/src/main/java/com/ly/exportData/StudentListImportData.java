package com.ly.exportData;

import com.alibaba.excel.annotation.ExcelProperty;

public class StudentListImportData {
    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("性别")
    private String gender;

    @ExcelProperty("年龄")
    private Integer age;

    @ExcelProperty("学号")
    private Long sno;

    @ExcelProperty("学院")
    private String college;

    @ExcelProperty("专业")
    private String major;

    @ExcelProperty("学分")
    private Double credit;

    @ExcelProperty("联系方式")
    private Long phone;

    @ExcelProperty("入学时间")
    private String inputTime;

    public StudentListImportData() {
    }

    public StudentListImportData(String name, String gender, Integer age, Long sno, String college, String major, Double credit, Long phone, String inputTime) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.sno = sno;
        this.college = college;
        this.major = major;
        this.credit = credit;
        this.phone = phone;
        this.inputTime = inputTime;
    }

    @Override
    public String toString() {
        return "StudentListImportData{" +
                "name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", sno=" + sno +
                ", college='" + college + '\'' +
                ", major='" + major + '\'' +
                ", credit=" + credit +
                ", phone='" + phone + '\'' +
                ", inputTime='" + inputTime + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Long getSno() {
        return sno;
    }

    public void setSno(Long sno) {
        this.sno = sno;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public String getInputTime() {
        return inputTime;
    }

    public void setInputTime(String inputTime) {
        this.inputTime = inputTime;
    }
}
