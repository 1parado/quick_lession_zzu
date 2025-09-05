package com.ly.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ly.annotation.SmsOperation;
import com.ly.mapper.AnnouncementMapper;
import com.ly.mapper.TeachersMapper;
import com.ly.pojo.Announcement;
import com.ly.pojo.Students;
import com.ly.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class AnnouncementServiceImpl implements AnnouncementService {

    @Autowired
    private AnnouncementMapper announcementMapper;

    @Autowired
    private TeachersMapper teachersMapper;

    /**
     *
     *
     * is == 2
     * @param announcement
     * @param is
     * @param account
     * @return
     */
    @SmsOperation
    @Override
    public int insertAnnouncement(Announcement announcement, int is, Long account) {
        if (announcement.getPublishTime() == null) {
            announcement.setPublishTime(new Date());
        }
        if (account.equals(123456L) || account == null) {
            //是管理员
            announcement.setPublicName("admin");
        } else {
            //是教师
            announcement.setPublicName(teachersMapper.selectNameByTno(account));
        }
        if (is == 1) {
            //状态为DRAFT
            announcement.setStatus("DRAFT");
            return announcementMapper.insert(announcement);
        } else if (is == 2) {
            //状态为PUBLISHED
            announcement.setStatus("PUBLISHED");
            return announcementMapper.insert(announcement);
        } else if (is == 0) {
            //状态为DELETED
            announcement.setStatus("DELETED");
            return announcementMapper.insert(announcement);
        }
        return 0;
    }

    @Override
    public PageInfo<Announcement> getAnnouncement(int page, int size, int status) {

        PageHelper.startPage(page, size);
        List<Announcement> announcement = new ArrayList<>();
        if (status == 2) {
            //已发布状态
            announcement = announcementMapper.selectAnnouncementByStatus("PUBLISHED");
        } else if (status == 1) {
            //草稿状态
            announcement = announcementMapper.selectAnnouncementByStatus("DRAFT");
        } else if (status == 0) {
            //已删除状态
            announcement = announcementMapper.selectAnnouncementByStatus("DELETED");
        } else {
            //全查
            announcement = announcementMapper.selectAnnouncement();
        }
        PageInfo<Announcement> pageInfo = PageInfo.of(announcement);
        return pageInfo;
    }

    @Override
    public PageInfo<Announcement> getTeacherAnnouncement(int page, int size, int status, Long tno) {
        String teacherName = teachersMapper.selectNameByTno(tno);
        PageHelper.startPage(page, size);
        List<Announcement> announcement = new ArrayList<>();
        if (status == 2) {
            //已发布状态
            announcement = announcementMapper.selectAnnouncementByStatusByTeacher("PUBLISHED", teacherName);
        } else if (status == 1) {
            //草稿状态
            announcement = announcementMapper.selectAnnouncementByStatusByTeacher("DRAFT", teacherName);
        } else if (status == 0) {
            //已删除状态
            announcement = announcementMapper.selectAnnouncementByStatusByTeacher("DELETED", teacherName);
        } else {
            //全查
            announcement = announcementMapper.selectAnnouncementByTeacher(teacherName);
        }
        PageInfo<Announcement> pageInfo = PageInfo.of(announcement);
        return pageInfo;
    }

    @Override
    public List<Announcement> getAllPublish() {
        return announcementMapper.selectAnnouncementByStatus("PUBLISHED");
    }

    @Override
    public Announcement getAnnouncementOne(int status) {
        return  announcementMapper.selectOne();
    }

    @Override
    public int updateAnnouncement(Announcement announcement) {
        //announcement.setPublishTime(new Date());
        //announcement.setStatus("PUBLISHED");
        int i1 = announcementMapper.updateByPrimaryKey(announcement);
        return i1 == 1 ? 1 : 0;
    }

    /**
     *
     *
     *
     * @param announcement
     * @return
     */
    @SmsOperation
    @Override
    public int updateAnnouncementToPublished(Announcement announcement) {
        announcement.setPublishTime(new Date());
        announcement.setStatus("PUBLISHED");
        int i1 = announcementMapper.updateByPrimaryKey(announcement);
        return i1 == 1 ? 1 : 0;
    }

    @Override
    public int deleteAnnouncementById(int id) {
        //改变状态
        Date now = new Date();
        int i = announcementMapper.updateById(id, now);
        return i == 1 ? 1 : 0;
    }

    @Override
    public int deleteAnnouncementByIdTrue(int id) {
        int i = announcementMapper.deleteByPrimaryKey(id);
        return i == 1 ? 1 : 0;
    }

    @Override
    public int reAnnouncement(int id) {
        Date now = new Date();
        int i = announcementMapper.updateDraftById(id, now);
        return i == 1 ? 1 : 0;
    }
}
