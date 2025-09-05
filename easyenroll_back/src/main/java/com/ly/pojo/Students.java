package com.ly.pojo;

import java.io.Serializable;
import java.util.Objects;

import lombok.Data;

/**
 * students
 */

public class Students implements Serializable {
    private Integer id;

    /**
     * 管理users表的id
     */
    private Integer userId;

    /**
     * 学生姓名
     */
    private String name;

    /**
     * 学生性别
     */
    private String gender;

    /**
     * 学生年龄
     */
    private Integer age;

    /**
     * 学号，关联users表的account
     */
    private Long sno;

    /**
     * 学院
     */
    private String college;

    /**
     * 专业
     */
    private String major;

    /**
     * 学分
     */
    private Double credit;

    /**
     * 联系方式
     */
    private Long phone;

    /**
     * 入学年月
     */
    private String inputTime;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "Students{" +
                "id=" + id +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", sno=" + sno +
                ", college='" + college + '\'' +
                ", major='" + major + '\'' +
                ", credit=" + credit +
                ", phone=" + phone +
                ", inputTime='" + inputTime + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Students students = (Students) o;
        return Objects.equals(id, students.id) && Objects.equals(userId, students.userId) && Objects.equals(name, students.name) && Objects.equals(gender, students.gender) && Objects.equals(age, students.age) && Objects.equals(sno, students.sno) && Objects.equals(college, students.college) && Objects.equals(major, students.major) && Objects.equals(credit, students.credit) && Objects.equals(phone, students.phone) && Objects.equals(inputTime, students.inputTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, name, gender, age, sno, college, major, credit, phone, inputTime);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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