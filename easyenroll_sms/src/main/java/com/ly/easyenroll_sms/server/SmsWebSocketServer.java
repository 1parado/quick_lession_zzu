package com.ly.easyenroll_sms.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ly.easyenroll_sms.mapper.SmsMapper;
import com.ly.easyenroll_sms.pojo.Sms;
import jakarta.websocket.*;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/sms-websocket/{phoneNumber}")
public class SmsWebSocketServer {
    // 使用线程安全的Map来存储 session 和 手机号 的映射关系
    private static ConcurrentHashMap<String, Session> sessionMap = new ConcurrentHashMap<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 连接建立成功调用的方法
     * @param session 客户端的WebSocket Session
     * @param phoneNumber 路径参数中的手机号
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("phoneNumber") String phoneNumber) {
        try {
            // 设置会话超时时间（单位：毫秒）
            session.setMaxIdleTimeout(0); // 0 表示永不超时

            sessionMap.put(phoneNumber, session);
            System.out.println("用户[" + phoneNumber + "]已连接, 会话ID: " + session.getId());
        } catch (Exception e) {
            System.err.println("设置会话参数失败: " + e.getMessage());
        }
    }

    /**
     * 连接关闭调用的方法
     * @param phoneNumber 路径参数中的手机号
     */
    @OnClose
    public void onClose(@PathParam("phoneNumber") String phoneNumber) {
        // 1. 从Map中移除映射关系
        sessionMap.remove(phoneNumber);
        System.out.println("用户[" + phoneNumber + "]连接断开");
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送的消息
     */
    @OnMessage
    public void onMessage(Session session, String message, @PathParam("phoneNumber") String phoneNumber) {
        System.out.println("收到来自用户[" + phoneNumber + "]的消息: " + message);
        // 可以处理客户端发送的确认消息等

    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.err.println("WebSocket错误: " + error.getMessage());
        error.printStackTrace();
    }


    /**
     * 这是最重要的方法：供外部业务代码调用，用于向指定用户发送短信
     * 例如：当短信队列中有新短信时，调用此方法
     * @param phoneNumber 目标手机号
     * @param sms 短信对象
     */
    public static void sendMessageToUser(String phoneNumber, Sms sms) {
        try {
            //处理sms
            sms.setType(2);
            sms.setDatesend(new Date());

            // 1. 根据手机号从Map中获取对应的Session
            Session session = sessionMap.get(phoneNumber);
            // 2. 检查Session是否存在且是打开的


            if (session != null && session.isOpen()) {
                // 3. 使用Session的Basic API发送文本消息
                //session.getBasicRemote().sendText(messageContent);
                //session.getBasicRemote().sendObject(sms);

                // 3. 将Sms对象转换为JSON字符串
                String jsonMessage = objectMapper.writeValueAsString(sms);
                // 4. 发送文本消息
                session.getBasicRemote().sendText(jsonMessage);
                sms.setStatus(1);
            } else {
                // 3 如果Session不存在或已关闭，可以记录日志或进行其他处理（如存入待发送队列）
                System.out.println("用户 " + phoneNumber + " 不在线，短信无法实时送达。");
                sms.setStatus(2);
            }



        } catch (Exception e) {
            e.printStackTrace();
            sms.setStatus(2);
        }
    }

}
