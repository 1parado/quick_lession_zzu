package com.ly.mapper;

import com.ly.pojo.NotifyTime;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotifyTimeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(NotifyTime record);

    int insertSelective(NotifyTime record);

    NotifyTime selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(NotifyTime record);

    int updateByPrimaryKey(NotifyTime record);

    NotifyTime selectByDraftId(Integer id);

    int updateByDraftId(NotifyTime notifyTime);


    List<NotifyTime> selectAll();

    void deleteByDraftId(Integer id);
}