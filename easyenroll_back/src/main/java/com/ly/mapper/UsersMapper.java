package com.ly.mapper;

import com.ly.pojo.Users;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UsersMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Users record);

    int insertSelective(Users record);

    Users selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Users record);

    int updateByPrimaryKey(Users record);

    Users selectByAccount(Long account);

    int updateByAccountAndPasswordById(Integer userId, Long sno);

    int updatePasswordByAccount(Users user);

    int deleteByAccount(Long sno);

    Integer selectIdByAccount(Long account);
}