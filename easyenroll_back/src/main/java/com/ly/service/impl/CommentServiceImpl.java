package com.ly.service.impl;

import com.ly.mapper.CommentMapper;
import com.ly.mapper.StudentsMapper;
import com.ly.mapper.TeachersMapper;
import com.ly.pojo.Comment;
import com.ly.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private StudentsMapper studentsMapper;

    @Autowired
    private TeachersMapper teachersMapper;

    @Override
    public List<Comment> getCommentBySize(int postId, int offset, int pageSize) {
        return commentMapper.selectPartByPostIdBySize(postId, offset, pageSize);
    }

    @Override
    public List<Comment> getComment(int postId) {
        return commentMapper.selectAllByPostId(postId);
    }

    @Override
    public void saveComment(Comment comment) {
        Long no = comment.getReviewerNo();
        String name = studentsMapper.selectNameBySno(no);
        if (name == null) {
            name = teachersMapper.selectNameByTno(no);
        }

        comment.setLikeCount(0L);
        comment.setReviewerName(name);

        int i =commentMapper.insert(comment);
    }

    @Override
    public void deleteById(int commentId) {
        commentMapper.deleteByPrimaryKey(commentId);
        //删除相关的回复
        //根据现有的commentId，作为relatedCommentId拿到评论列表，删除
        commentMapper.deleteByRelatedCommentId(commentId);
    }
}
