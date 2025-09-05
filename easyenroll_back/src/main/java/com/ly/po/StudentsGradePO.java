package com.ly.po;

import java.io.Serializable;
import java.util.Objects;

public class StudentsGradePO implements Serializable {

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

    /**
     * 成绩
     */
    private Double grade;

    /**
     * 考试结果状态（1正常，0缺考，2作弊）
     */
    private Integer state;

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

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentsGradePO that = (StudentsGradePO) o;
        return Objects.equals(id, that.id) && Objects.equals(userId, that.userId) && Objects.equals(name, that.name) && Objects.equals(gender, that.gender) && Objects.equals(age, that.age) && Objects.equals(sno, that.sno) && Objects.equals(college, that.college) && Objects.equals(major, that.major) && Objects.equals(credit, that.credit) && Objects.equals(phone, that.phone) && Objects.equals(inputTime, that.inputTime) && Objects.equals(grade, that.grade) && Objects.equals(state, that.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, name, gender, age, sno, college, major, credit, phone, inputTime, grade, state);
    }

    @Override
    public String toString() {
        return "StudentsGradePO{" +
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
                ", grade=" + grade +
                ", state=" + state +
                '}';
    }
}
