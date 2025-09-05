package com.ly.pojo;

import java.io.Serializable;
import java.util.Objects;

/**
 * like
 */

public class Like implements Serializable {
    private Integer id;

    /**
     * 点赞者编号
     */
    private Long likerNo;

    /**
     * 点赞评论的id
     */
    private Integer commentId;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getLikerNo() {
        return likerNo;
    }

    public void setLikerNo(Long likerNo) {
        this.likerNo = likerNo;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    @Override
    public String toString() {
        return "Like{" +
                "id=" + id +
                ", likerNo=" + likerNo +
                ", commentId=" + commentId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Like like = (Like) o;
        return Objects.equals(id, like.id) && Objects.equals(likerNo, like.likerNo) && Objects.equals(commentId, like.commentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, likerNo, commentId);
    }
}