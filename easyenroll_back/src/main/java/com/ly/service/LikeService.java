package com.ly.service;

import com.ly.pojo.Like;

public interface LikeService {
    int like(Like like);

    void deleteByCommentId(int commentId);
}
