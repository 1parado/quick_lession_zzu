package com.ly.exportData;

import com.alibaba.excel.annotation.ExcelProperty;

public class TeacherListImportData {

    /**
     * 教师姓名
     */
    @ExcelProperty("姓名")
    private String name;

    /**
     * 工号
     */
    @ExcelProperty("工号")
    private Long tno;

    /**
     * 教师性别
     */
    @ExcelProperty("性别")
    private String gender;

    /**
     * 教师年龄
     */
    @ExcelProperty("年龄")
    private Integer age;

    /**
     * 教师所属学院
     */
    @ExcelProperty("学院")
    private String college;

    /**
     * 教师职称
     */
    @ExcelProperty("职称")
    private String title;

    /**
     * 教师联系方式
     */
    @ExcelProperty("联系方式")
    private Long phone;

    public TeacherListImportData(String name, Long tno, String gender, Integer age, String college, String title, Long phone) {
        this.name = name;
        this.tno = tno;
        this.gender = gender;
        this.age = age;
        this.college = college;
        this.title = title;
        this.phone = phone;
    }

    public TeacherListImportData() {
    }

    @Override
    public String toString() {
        return "TeacherListImportData{" +
                "name='" + name + '\'' +
                ", tno=" + tno +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", college='" + college + '\'' +
                ", title='" + title + '\'' +
                ", phone=" + phone +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTno() {
        return tno;
    }

    public void setTno(Long tno) {
        this.tno = tno;
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

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }
}
