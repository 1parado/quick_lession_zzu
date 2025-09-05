package com.ly.pojo;

import java.io.Serializable;
import java.util.Objects;


/**
 * comment
 */

public class Comment implements Serializable {
    private Integer id;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论者姓名
     */
    private String reviewerName;

    /**
     * 评论者编号
     */
    private Long reviewerNo;

    /**
     * 点赞数默认0
     */
    private Long likeCount;

    /**
     * 相关的贴子id
     */
    private Integer relatedPostId;

    /**
     * 回复的评论id
     */
    private Integer relatedCommentId;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public Long getReviewerNo() {
        return reviewerNo;
    }

    public void setReviewerNo(Long reviewerNo) {
        this.reviewerNo = reviewerNo;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getRelatedPostId() {
        return relatedPostId;
    }

    public void setRelatedPostId(Integer relatedPostId) {
        this.relatedPostId = relatedPostId;
    }

    public Integer getRelatedCommentId() {
        return relatedCommentId;
    }

    public void setRelatedCommentId(Integer relatedCommentId) {
        this.relatedCommentId = relatedCommentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id) && Objects.equals(content, comment.content) && Objects.equals(reviewerName, comment.reviewerName) && Objects.equals(reviewerNo, comment.reviewerNo) && Objects.equals(likeCount, comment.likeCount) && Objects.equals(relatedPostId, comment.relatedPostId) && Objects.equals(relatedCommentId, comment.relatedCommentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, reviewerName, reviewerNo, likeCount, relatedPostId, relatedCommentId);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", reviewerName='" + reviewerName + '\'' +
                ", reviewerNo=" + reviewerNo +
                ", likeCount=" + likeCount +
                ", relatedPostId=" + relatedPostId +
                ", relatedCommentId=" + relatedCommentId +
                '}';
    }
}