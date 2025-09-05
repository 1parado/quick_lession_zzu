package com.ly.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * message
 */
public class Message implements Serializable {
    private Integer id;

    /**
     * 留言者账号
     */
    private Long account;

    /**
     * 留言者姓名
     */
    private String name;

    /**
     * 留言内容
     */
    private String content;

    /**
     * 留言时间
     */
    private Date createTime;

    /**
     * 留言课程
     */
    private String courseCode;

    /**
     * 留言类型(学生是1，教师是0)
     */
    private Integer type;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getAccount() {
        return account;
    }

    public void setAccount(Long account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(id, message.id) && Objects.equals(account, message.account) && Objects.equals(name, message.name) && Objects.equals(content, message.content) && Objects.equals(createTime, message.createTime) && Objects.equals(courseCode, message.courseCode) && Objects.equals(type, message.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, account, name, content, createTime, courseCode, type);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", account=" + account +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", courseCode='" + courseCode + '\'' +
                ", type=" + type +
                '}';
    }
}