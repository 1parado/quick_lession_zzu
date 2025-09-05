package com.ly.mapper;

import com.ly.pojo.SmsTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SmsTemplateMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SmsTemplate record);

    int insertSelective(SmsTemplate record);

    SmsTemplate selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SmsTemplate record);

    int updateByPrimaryKey(SmsTemplate record);

    List<SmsTemplate> selectByType(String type);

    void updateStatusByType(String type);

    SmsTemplate selectByTypeByStatus(String template, int status);
}