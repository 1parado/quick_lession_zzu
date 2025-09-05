package com.ly.mapper;

import com.ly.pojo.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);

    List<Comment> selectPartByPostIdBySize(int postId, int offset, int pageSize);

    void deleteByRelatedCommentId(int commentId);

    List<Comment> selectAllByPostId(int postId);

    void updateLikeCountByCommentIdAdd(Integer commentId);

    void updateLikeCountByCommentIdSub(Integer commentId);
}