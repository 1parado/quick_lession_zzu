package com.ly.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


/**
 * post_page
 */

public class Page implements Serializable {
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 关联课程名
     */
    private String mentionedCourseName;

    /**
     * 关联课程代码
     */
    private String mentionedCourseCode;

    /**
     * 贴子具体内容
     */
    private String content;

    /**
     * 发布人
     */
    private String publishName;

    /**
     * 发布人编号
     */
    private Long publishSno;

    /**
     * 发布时间
     */
    private Date publishTime;

    /**
     * 评论数默认为0
     */
    private Long commentCount;

    /**
     * 贴子类型（评价贴1，任意贴0）
     */
    private Integer type;

    /**
     * 综合评价（默认为0）
     */
    private Double rating;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMentionedCourseName() {
        return mentionedCourseName;
    }

    public void setMentionedCourseName(String mentionedCourseName) {
        this.mentionedCourseName = mentionedCourseName;
    }

    public String getMentionedCourseCode() {
        return mentionedCourseCode;
    }

    public void setMentionedCourseCode(String mentionedCourseCode) {
        this.mentionedCourseCode = mentionedCourseCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublishName() {
        return publishName;
    }

    public void setPublishName(String publishName) {
        this.publishName = publishName;
    }

    public Long getPublishSno() {
        return publishSno;
    }

    public void setPublishSno(Long publishSno) {
        this.publishSno = publishSno;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Page page = (Page) o;
        return Objects.equals(id, page.id) && Objects.equals(title, page.title) && Objects.equals(mentionedCourseName, page.mentionedCourseName) && Objects.equals(mentionedCourseCode, page.mentionedCourseCode) && Objects.equals(content, page.content) && Objects.equals(publishName, page.publishName) && Objects.equals(publishSno, page.publishSno) && Objects.equals(publishTime, page.publishTime) && Objects.equals(commentCount, page.commentCount) && Objects.equals(type, page.type) && Objects.equals(rating, page.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, mentionedCourseName, mentionedCourseCode, content, publishName, publishSno, publishTime, commentCount, type, rating);
    }

    @Override
    public String toString() {
        return "Page{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", mentionedCourseName='" + mentionedCourseName + '\'' +
                ", mentionedCourseCode='" + mentionedCourseCode + '\'' +
                ", content='" + content + '\'' +
                ", publishName='" + publishName + '\'' +
                ", publishSno=" + publishSno +
                ", publishTime=" + publishTime +
                ", commentCount=" + commentCount +
                ", type=" + type +
                ", rating=" + rating +
                '}';
    }
}