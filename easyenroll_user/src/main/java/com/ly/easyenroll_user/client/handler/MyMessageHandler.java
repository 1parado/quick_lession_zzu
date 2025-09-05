package com.ly.easyenroll_user.client.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ly.easyenroll_user.mapper.SmsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.ly.easyenroll_user.pojo.Sms;

import java.util.Date;


@Component
public class MyMessageHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private SmsMapper smsMapper;

    // 建立连接后调用
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("已连接到服务器: " + session.getUri());

        // 设置会话参数，防止过早关闭
    }

    // 当接收到来自服务端的消息时被调用
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("客户端收到消息：" + payload);

        try {
            // 解析收到的短信
            Sms sms = objectMapper.readValue(payload, com.ly.easyenroll_user.pojo.Sms.class);
            System.out.println("收到短信内容: " + sms.getContent());

            //将短信保存起来
            sms.setDatereceive(new Date());
            sms.setStatus(3);
            sms.setType(1);
            smsMapper.insert(sms);

            // 发送确认响应
            String response = "{\"status\":\"received\",\"messageId\":" + sms.getId() + "}";
            session.sendMessage(new TextMessage(response));
            System.out.println("已发送确认响应");
        } catch (Exception e) {
            System.err.println("消息处理错误: " + e.getMessage());
        }
    }

    // 当传输过程中发生错误时被调用
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.err.println("传输错误: " + exception.getMessage());
    }

    // 当连接关闭时被调用（无论正常关闭还是异常关闭）
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("连接已关闭: " + closeStatus.getReason());
    }
}