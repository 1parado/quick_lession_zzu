package com.ly.task;

import com.ly.mapper.*;
import com.ly.po.DraftPO;
import com.ly.pojo.Announcement;
import com.ly.pojo.NotifyTime;
import com.ly.pojo.SeckillTasks;
import com.ly.pojo.SmsTemplate;
import com.ly.service.LeaderboardService;
import com.ly.service.RedisService;
import com.ly.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.ly.util.TemplateUtil.renderTemplate;

@Component
public class ScheduledTasks {

    @Autowired
    private RedisService redisService;

    @Autowired
    private AnnouncementMapper announcementMapper;

    @Autowired
    private NotifyTimeMapper notifyTimeMapper;

    @Autowired
    private TeachersMapper teachersMapper;

    @Autowired
    private SmsTemplateMapper smsTemplateMapper;

    @Autowired
    private LeaderboardService leaderboardService;

    @Autowired
    private SeckillTasksMapper seckillTasksMapper;


    // 每5分钟执行一次
    @Scheduled(cron = "0 */5 * * * ?")
    public void executeEveryFiveMinutes() {
        System.out.println("定时扫描：{发送教师短信；清除垃圾数据；更行排行榜缓存}");
        //做分钟循环检查草稿箱公告决定是否发送短信(给教师)（根据预计发布时间提前发布 5 - 120 min 不等）

        //针对学生来说，是发布时进行立即通知
        sendSms();

        //清除notify_time表（设置提前通知时间）的旧数据,根据draft_id去查找announcement表拿到publish_time对比现在，若现在超过当时，删除
        clearNotifyTime();

        //定时更新缓存中的排行榜数据
        leaderboardService.addLeaderboardData();
    }

    // 每1分钟执行一次
    //@Scheduled(cron = "0 */1 * * * ?")
    //public void executeEveryOneMinutes() {
        //System.out.println("定时扫描：{预选抢课}");
    //}



    //每天00点执行一次
    @Scheduled(cron = "0 0 0 * * ?")
    //@Scheduled(cron = "0 */1 * * * ?")
    public void executeEveryOneDay() {
        System.out.println("定时扫描：{删除过期公告表}");
        //每天00：00：00执行一次

        //公告过期
        //扫描公告表，对比已发布公告的publish_time和现在时间对比，超过一年的删除
        clearPublishAnnouncement();
    }

    private void sendSms() {
        //判断是否开启自动短信通知功能
        if (redisService.getValue(Constant.SWITCH_SMS).equals("false")) {
            return;
        }

        //扫描草稿箱公告，拿到所有预计发布时间在此时之后的草稿箱公告
        List<DraftPO> draftPOS = announcementMapper.selectDraftByTimeByNoAdmin();
        for (DraftPO draftPO : draftPOS) {
            // 先判断发布人是谁，如果是admin，则不去发送短信（管理员不需要）
            if (draftPO.getPublicName().equals("admin")) {
                continue;
            }

            //还应该判断当前教师是否填写了手机号，若没有填写，也不去发送（不知道往哪里发送）
            // 有BUG不是很严重先不修，先根据教师名称查找吧，警醒：注意使用唯一标识比如说教师工号
            Long phone = teachersMapper.selectPhoneByName(draftPO.getPublicName());
            if (phone == null) {
                continue;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(draftPO.getPublishTime());
            calendar.add(Calendar.MINUTE, -draftPO.getTime()); // 减去n分钟
            Date newDate = calendar.getTime();


            //有一个问题：发送过的不再发送，可以同时比较 公告id + 手机号 ，存入缓存，2小时过期（120min）

            //判断是否近期发送过（两个小时）
            if (redisService.getValue(Constant.SMS_REPEAT + draftPO.getId() + "_" + phone) != null) {
                //说明重复
                continue;
            }

            if (newDate.before(new Date())) {
                //编辑内容，结合模板
                SmsTemplate smsTemplate = smsTemplateMapper.selectByTypeByStatus("TeacherTemplate", 1);
                System.out.println(smsTemplate);
                String component = renderTemplate(smsTemplate.getComponent(), draftPO.getPublicName(), draftPO.getPublishTime());
                System.out.println("短信内容：" + component);

                //发送短信

                //记录入缓存，公告id + 手机号 ， 过期时间 2h ， 在发送前检查
                redisService.setValueEx(Constant.SMS_REPEAT + draftPO.getId() + "_" + phone, "1", 2, TimeUnit.HOURS);
            }
        }
    }

    private void clearNotifyTime() {
        System.out.println("定时清理冗余数据");
        List<NotifyTime> notifyTimeList = notifyTimeMapper.selectAll();
        for(NotifyTime notifyTime : notifyTimeList) {
            //对每一个notifyTime进行检查
            Announcement announcement = announcementMapper.selectByPrimaryKey(notifyTime.getDraftId());
            //留五分钟容错
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, 5); // 加 5 分钟
            Date currentTimePlus5Minutes = calendar.getTime();
            if (announcement.getPublishTime().before(currentTimePlus5Minutes)){
                //删除
                notifyTimeMapper.deleteByDraftId(announcement.getId());
            }
        }
    }



    private void clearPublishAnnouncement() {
        //拿到已发布公告
        List<Announcement> announcementList = announcementMapper.selectPublished();

        //遍历已发布公告，对比时间
        for (Announcement announcement : announcementList) {
            if (isExpire(announcement.getPublishTime())) {
                //早于一年前，过期，删除
                announcementMapper.deleteByPrimaryKey(announcement.getId());
            }
        }
    }

    private boolean isExpire(Date publishTime) {
        if (publishTime == null) {
            return true; // 或者根据业务需求返回相应值
        }

        // 将Date转换为LocalDateTime
        LocalDateTime publishDateTime = publishTime.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        // 获取一年前的当前时间
        LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);

        // 比较发布时间是否早于一年前
        return publishDateTime.isBefore(oneYearAgo);
    }



}
