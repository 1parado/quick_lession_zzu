package com.ly.service;

import com.github.pagehelper.PageInfo;
import com.ly.pojo.Announcement;

import java.util.List;

public interface AnnouncementService {
    int insertAnnouncement(Announcement announcement, int is, Long account);

    PageInfo<Announcement> getAnnouncement(int page, int size, int status);

    Announcement getAnnouncementOne(int status);

    int updateAnnouncement(Announcement announcement);

    int deleteAnnouncementById(int id);

    int deleteAnnouncementByIdTrue(int id);

    int reAnnouncement(int id);

    List<Announcement> getAllPublish();

    PageInfo<Announcement> getTeacherAnnouncement(int page, int size, int status, Long tno);

    int updateAnnouncementToPublished(Announcement announcement);
}
