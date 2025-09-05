package com.ly.service.impl;

import com.ly.mapper.CommentMapper;
import com.ly.mapper.LikeMapper;
import com.ly.pojo.Like;
import com.ly.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeMapper likeMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public int like(Like like) {
        // 查询是否点赞过，决定是点赞还是取消点赞
        Like like1 = likeMapper.selectByLikerNoAndCommentId(like.getLikerNo(), like.getCommentId());
        if (like1 == null) {
            //为空，说明未点赞过，此处执行点赞操作
            //记录（删除）点赞信息到点赞记录表，更新（加/减）评论表相关评论的点赞数。
            likeMapper.insert(like);
            commentMapper.updateLikeCountByCommentIdAdd(like.getCommentId());

            return 1;//表示点赞成功
        } else {
            //取消点赞
            likeMapper.deleteByLikerNoAndCommentId(like.getLikerNo(), like.getCommentId());
            commentMapper.updateLikeCountByCommentIdSub(like.getCommentId());

            return 2;//表示取消点赞成功
        }
    }

    @Override
    public void deleteByCommentId(int commentId) {
        likeMapper.deleteByCommentId(commentId);
    }
}
