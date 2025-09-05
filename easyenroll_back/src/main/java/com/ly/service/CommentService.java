package com.ly.service;

import com.ly.pojo.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getCommentBySize(int postId, int offset, int pageSize);

    void saveComment(Comment comment);

    void deleteById(int commentId);

    List<Comment> getComment(int postId);
}
