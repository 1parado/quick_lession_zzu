package com.ly.mapper;

import com.ly.pojo.Like;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Like record);

    int insertSelective(Like record);

    Like selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Like record);

    int updateByPrimaryKey(Like record);

    Like selectByLikerNoAndCommentId(Long likerNo, Integer commentId);

    void deleteByLikerNoAndCommentId(Long likerNo, Integer commentId);

    void deleteByCommentId(int commentId);
}