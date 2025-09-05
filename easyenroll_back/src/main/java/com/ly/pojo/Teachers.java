package com.ly.pojo;

import java.io.Serializable;
import java.util.Objects;

import lombok.Data;

/**
 * teachers
 */

public class Teachers implements Serializable {
    private Integer id;

    /**
     * 关联users表的id
     */
    private Integer userId;

    /**
     * 工号
     */
    private Long tno;

    /**
     * 教师姓名
     */
    private String name;

    /**
     * 教师性别
     */
    private String gender;

    /**
     * 教师年龄
     */
    private Integer age;

    /**
     * 教师所属学院
     */
    private String college;

    /**
     * 教师职称
     */
    private String title;

    /**
     * 教师联系方式
     */
    private Long phone;

    private static final long serialVersionUID = 1L;

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

    public Long getTno() {
        return tno;
    }

    public void setTno(Long tno) {
        this.tno = tno;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teachers teachers = (Teachers) o;
        return Objects.equals(id, teachers.id) && Objects.equals(userId, teachers.userId) && Objects.equals(tno, teachers.tno) && Objects.equals(name, teachers.name) && Objects.equals(gender, teachers.gender) && Objects.equals(age, teachers.age) && Objects.equals(college, teachers.college) && Objects.equals(title, teachers.title) && Objects.equals(phone, teachers.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, tno, name, gender, age, college, title, phone);
    }

    @Override
    public String toString() {
        return "Teachers{" +
                "id=" + id +
                ", userId=" + userId +
                ", tno=" + tno +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", college='" + college + '\'' +
                ", title='" + title + '\'' +
                ", phone=" + phone +
                '}';
    }
}