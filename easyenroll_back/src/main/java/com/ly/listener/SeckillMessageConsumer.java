package com.ly.listener;

import com.ly.message.SeckillMessage;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(
        topic = "select-topic",
        consumerGroup = "select-consumer-group",
        consumeMode = ConsumeMode.ORDERLY,  // 顺序消费
        messageModel = MessageModel.CLUSTERING,
        consumeTimeout = 3000,
        maxReconsumeTimes = 3  // 最大重试次数
)
public class SeckillMessageConsumer implements RocketMQListener<SeckillMessage> {

    private static final Logger log = LoggerFactory.getLogger(SeckillMessageConsumer.class);

    @Override
    public void onMessage(SeckillMessage seckillMessage) {
        log.info("消费者接受到消息，{}", seckillMessage);
    }
}
