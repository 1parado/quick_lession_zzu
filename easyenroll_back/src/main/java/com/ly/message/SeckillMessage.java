package com.ly.message;

import java.io.Serializable;

public class SeckillMessage implements Serializable {

    private static final long serialVersionUID = 1L; // 序列化版本号

    private Long studentSno;

    private String courseCode;

    public SeckillMessage() {
    }

    public SeckillMessage(Long studentSno, String courseCode) {
        this.studentSno = studentSno;
        this.courseCode = courseCode;
    }

    @Override
    public String toString() {
        return "SeckillMessage{" +
                "studentSno=" + studentSno +
                ", courseCode='" + courseCode + '\'' +
                '}';
    }

    public Long getStudentSno() {
        return studentSno;
    }

    public void setStudentSno(Long studentSno) {
        this.studentSno = studentSno;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
}
