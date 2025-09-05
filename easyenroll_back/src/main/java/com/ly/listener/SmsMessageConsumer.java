package com.ly.listener;

import com.ly.pojo.Sms;
import com.ly.util.Constant;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Component
@RocketMQMessageListener(
        topic = Constant.TOPIC_SMS,
        consumerGroup = "sms-consumer-group"
)
public class SmsMessageConsumer implements RocketMQListener<Sms> {

    @Autowired
    private RestTemplate restTemplate; // 注入 RestTemplate
    @Override
    public void onMessage(Sms sms) {
        System.out.println(sms);
        processSms(sms);

    }
    private void processSms(Sms sms) {
        try {
            //处理sms对象
            sms.setDatesend(new Date());

            // 调用第三方短信服务API（如阿里云、腾讯云短信）
            System.out.println("正在向" + sms.getReceiver() + "发送短信：" + sms.getContent());
            // 这里我选择模拟一个第三方短信服务系统
            // 模拟调用短信接口
            restTemplate.postForObject(Constant.SMS_SERVICE_URL, sms, Void.class);
            //restTemplate.getForObject("http://localhost:8089/sms/test", Void.class);

            System.out.println("短信发送成功");

        } catch (Exception e) {
            System.out.println("短信发送异常，手机号：" + sms.getReceiver());
            // 这里可以根据异常类型决定是否重试
            // 如果抛出异常，消息会根据重试策略重新投递
        }
    }
}
