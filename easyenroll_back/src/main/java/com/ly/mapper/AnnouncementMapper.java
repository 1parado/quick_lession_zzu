package com.ly.mapper;

import com.ly.po.DraftPO;
import com.ly.pojo.Announcement;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface AnnouncementMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Announcement record);

    int insertSelective(Announcement record);

    Announcement selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Announcement record);

    int updateByPrimaryKey(Announcement record);

    List<Announcement> selectAnnouncement();

    List<Announcement> selectAnnouncementByStatus(String status);

    Announcement selectOne();

    int updateById(int id, Date now);

    int updateDraftById(int id, Date now);

    List<Announcement> selectAnnouncementByStatusByTeacher(String status, String teacherName);

    List<Announcement> selectAnnouncementByTeacher(String teacherName);

    List<DraftPO> selectDraftByTime();

    List<DraftPO> selectDraftByTimeByNoAdmin();

    List<Announcement> selectPublished();
}