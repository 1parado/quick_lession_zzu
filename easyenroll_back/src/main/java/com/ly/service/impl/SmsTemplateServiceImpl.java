package com.ly.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ly.mapper.AnnouncementMapper;
import com.ly.mapper.NotifyTimeMapper;
import com.ly.mapper.SmsTemplateMapper;
import com.ly.po.DraftPO;
import com.ly.pojo.Announcement;
import com.ly.pojo.NotifyTime;
import com.ly.pojo.SmsTemplate;
import com.ly.service.SmsTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SmsTemplateServiceImpl implements SmsTemplateService {

    @Autowired
    private SmsTemplateMapper smsTemplateMapper;

    @Autowired
    private NotifyTimeMapper notifyTimeMapper;

    @Autowired
    private AnnouncementMapper announcementMapper;

    @Override
    public int saveTemplate(SmsTemplate smsTemplate) {
        return smsTemplateMapper.insertSelective(smsTemplate);
    }

    @Override
    public List<SmsTemplate> getStudentTemplate() {
        return smsTemplateMapper.selectByType("StudentTemplate");
    }

    @Override
    public List<SmsTemplate> getTeacherTemplate() {
        return smsTemplateMapper.selectByType("TeacherTemplate");
    }

    @Override
    public void updateStatusByStudent(SmsTemplate smsTemplate) {
        //更新同类型的其他模板状态为0
        smsTemplateMapper.updateStatusByType("StudentTemplate");
        //更新当前模板状态为1
        smsTemplateMapper.updateByPrimaryKeySelective(smsTemplate);
    }

    @Override
    public void updateStatusByTeacher(SmsTemplate smsTemplate) {
        //更新同类型的其他模板状态为0
        smsTemplateMapper.updateStatusByType("TeacherTemplate");
        //更新当前模板状态为1
        smsTemplateMapper.updateByPrimaryKeySelective(smsTemplate);
    }

    @Override
    public int updateByStudentTemplate(SmsTemplate smsTemplate) {
        return smsTemplateMapper.updateByPrimaryKey(smsTemplate);
    }

    @Override
    public int updateByTeacherTemplate(SmsTemplate smsTemplate) {
        return smsTemplateMapper.updateByPrimaryKey(smsTemplate);
    }

    @Override
    public int deleteStudentTemplate(int id) {
        return smsTemplateMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteTeacherTemplate(int id) {
        return smsTemplateMapper.deleteByPrimaryKey(id);
    }

    @Override
    public PageInfo<DraftPO> getDraftPO(int page, int size, int i) {
        PageHelper.startPage(page, size);
        List<DraftPO> draftPOs = announcementMapper.selectDraftByTimeByNoAdmin();
        PageInfo<DraftPO> pageInfo = PageInfo.of(draftPOs);
        return pageInfo;
    }

    @Override
    public int setNotifyTime(DraftPO draftPO, int time) {
        NotifyTime notifyTime = new NotifyTime();
        notifyTime.setTime(time);
        notifyTime.setDraftId(draftPO.getId());
        //先去查找有没有，有则该，无则增加
        NotifyTime notifyTime2 = notifyTimeMapper.selectByDraftId(draftPO.getId());
        if (notifyTime2 != null) {
            return notifyTimeMapper.updateByDraftId(notifyTime);
        } else {
            return notifyTimeMapper.insertSelective(notifyTime);
        }
    }
}
