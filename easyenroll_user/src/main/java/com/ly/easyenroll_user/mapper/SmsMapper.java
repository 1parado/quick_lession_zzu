package com.ly.easyenroll_user.mapper;

import com.ly.easyenroll_user.pojo.Sms;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SmsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Sms record);

    int insertSelective(Sms record);

    Sms selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Sms record);

    int updateByPrimaryKey(Sms record);

    List<Sms> selectAll();

    int updateRead(int id);
}