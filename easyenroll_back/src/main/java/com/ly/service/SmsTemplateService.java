package com.ly.service;

import com.github.pagehelper.PageInfo;
import com.ly.po.DraftPO;
import com.ly.pojo.SmsTemplate;

import java.util.List;

public interface SmsTemplateService {
    int saveTemplate(SmsTemplate smsTemplate);

    List<SmsTemplate> getStudentTemplate();

    List<SmsTemplate> getTeacherTemplate();

    void updateStatusByStudent(SmsTemplate smsTemplate);

    void updateStatusByTeacher(SmsTemplate smsTemplate);

    int updateByStudentTemplate(SmsTemplate smsTemplate);

    int updateByTeacherTemplate(SmsTemplate smsTemplate);

    int deleteStudentTemplate(int id);

    int deleteTeacherTemplate(int id);

    PageInfo<DraftPO> getDraftPO(int page, int size, int i);

    int setNotifyTime(DraftPO draftPO, int time);
}
