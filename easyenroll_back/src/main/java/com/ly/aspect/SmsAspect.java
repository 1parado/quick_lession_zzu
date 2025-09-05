package com.ly.aspect;


import com.ly.exception.SmsException;
import com.ly.mapper.SmsTemplateMapper;
import com.ly.mapper.StudentsMapper;
import com.ly.pojo.Announcement;
import com.ly.pojo.Sms;
import com.ly.pojo.SmsTemplate;
import com.ly.pojo.Students;
import com.ly.service.RedisService;
import com.ly.util.Constant;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


import static com.ly.util.TemplateUtil.renderTemplate;

@Aspect
@Component
public class SmsAspect {

    @Autowired
    private RedisService redisService;

    @Autowired
    private StudentsMapper studentsMapper;

    @Autowired
    private SmsTemplateMapper smsTemplateMapper;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @AfterReturning(
            pointcut = "@annotation(com.ly.annotation.SmsOperation)",
            returning = "result"
    )
    public void afterSmsOperation(JoinPoint joinPoint, Object result) {
        System.out.println(result);

        Object[] args = joinPoint.getArgs();
        Announcement announcement = new Announcement();
        if (args != null && args.length > 0 && args[0] instanceof Announcement) {
            announcement = (Announcement) args[0];
            System.out.println("获取到Announcement对象:");
            System.out.println("ID: " + announcement.getId());
            System.out.println("标题: " + announcement.getTitle());
            System.out.println("发布状态: " + announcement.getStatus());
            System.out.println("发布时间: " + announcement.getPublishTime());
            System.out.println("content: " + announcement.getContent());
            System.out.println("publicName: " + announcement.getPublicName());
        }

        //判断是否开启自动短信通知功能
        if (redisService.getValue(Constant.SWITCH_SMS).equals("false")) {
            return;
        }

        //找到所有要发送的对象（学生）（有手机号）
        List<Students> studentsList = studentsMapper.selectByHasPhone();

        for (Students student : studentsList) {
            //System.out.println(student);

            //给每一个学生发送短信

            //结合模板,拿到学生选中模板
            SmsTemplate smsTemplate = smsTemplateMapper.selectByTypeByStatus("StudentTemplate", 1);
            //System.out.println(smsTemplate);
            //分析模板
            String component = renderTemplate(smsTemplate.getComponent(), student.getName(), new Date());
            //System.out.println("短信内容：" + component);

            //准备短信实体类
            /*
                id              唯一标识
                sender          发送方手机号（这里决定使用全校统一的号码进行发送）
                receiver        接收者手机号（只有单发，因为我的逻辑就是遍历所有接受者，一个一个处理短信）

                subject         主题（标题？主要用于彩信MMS或电子邮件模拟）
                type            类型（1：接收，2：发送，3：草稿）
                status          状态（短信状态：0-发送中，1-发送成功，2-发送失败，3-已送达（对于接收的短信，此字段可忽略或设为已送达））

                dateSend        发送时间
                dateReceive     接收时间

                threadId; // 会话线程ID (同一个对话的所有短信拥有相同的threadId)
                read; // 是否已读 (0-未读，1-已读)

             */
            Sms sms = new Sms();
            sms.setSender(Constant.PHONE_SCHOOLE);
            sms.setReceiver(student.getPhone());
            sms.setType(2);
            sms.setStatus(0);
            sms.setRead(0);
            sms.setContent(component);

            String topic = Constant.TOPIC_SMS;

            Message<Sms> message = MessageBuilder
                    .withPayload(sms)
                    .setHeader("KEYS", "sms_key_" + sms.getReceiver()) // 设置业务Key（非常重要！）
                    .build();

            try {
                // 使用同步发送确保重要消息不丢失，并获取结果
                SendResult sendResult = rocketMQTemplate.syncSend(topic, message);
                System.out.println("短信消息发送成功。MsgId: {" + sendResult.getMsgId() + "}, Key: {" + "sms_key_" + sms.getReceiver() + "}");
            } catch (Exception e) {
                System.out.println("短信消息发送失败。Key: {" + "sms_key_" + sms.getReceiver() + "}," + e);
                // 这里可以加入重试逻辑或降级处理
                // 发送失败就抛一个短信发送失败异常
                throw new SmsException();
            }

        }
        /*
        规范模板语句
        针对学生：
            {{name}}        学生姓名
            {{publishTime}}     发布时间


        针对教师：
            {{name}}        教师姓名
            {{publishTime}}         预计发布时间

         */
    }

}
