package com.ly.exportData;

import com.alibaba.excel.annotation.ExcelProperty;

public class StudentListExportData {
    @ExcelProperty("ID")
    private Integer id;

    @ExcelProperty("UserId")
    private Integer user_id;

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
    private String input_time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
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

    public String getInput_time() {
        return input_time;
    }

    public void setInput_time(String input_time) {
        this.input_time = input_time;
    }

    @Override
    public String toString() {
        return "StudentListExportData{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", sno=" + sno +
                ", college='" + college + '\'' +
                ", major='" + major + '\'' +
                ", credit=" + credit +
                ", phone=" + phone +
                ", input_time='" + input_time + '\'' +
                '}';
    }
}
