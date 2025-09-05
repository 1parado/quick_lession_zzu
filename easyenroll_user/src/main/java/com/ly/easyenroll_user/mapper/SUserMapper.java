package com.ly.easyenroll_user.mapper;

import com.ly.easyenroll_user.pojo.SUser;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SUser record);

    int insertSelective(SUser record);

    SUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SUser record);

    int updateByPrimaryKey(SUser record);

    List<Long> selectAllPhone();
}