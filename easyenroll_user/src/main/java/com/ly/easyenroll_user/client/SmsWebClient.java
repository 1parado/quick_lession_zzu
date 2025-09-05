package com.ly.easyenroll_user.client;

import com.ly.easyenroll_user.client.handler.MyMessageHandler;
import com.ly.easyenroll_user.mapper.SUserMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SmsWebClient {

    @Autowired
    private MyMessageHandler myMessageHandler;

    @Autowired
    private SUserMapper sUserMapper;

    // 存储所有连接的ConnectionManager，便于管理
    private static ConcurrentHashMap<String, WebSocketConnectionManager> connectionManagerMap = new ConcurrentHashMap<>();

    // 在项目启动后为所有用户建立连接
    @PostConstruct
    public void connectAllUsers() {
        // 1. 从数据库获取所有用户的手机号
        List<Long> longs = sUserMapper.selectAllPhone();

        List<String> allPhoneNumbers = new ArrayList<>();

        for (Long l : longs) {
            allPhoneNumbers.add(String.valueOf(l));
        }

        // 如果没有获取到手机号，可以使用默认的或记录日志
        if (allPhoneNumbers == null || allPhoneNumbers.isEmpty()) {
            System.out.println("未找到用户手机号，使用默认手机号");
            allPhoneNumbers = List.of("15834356385"); // 默认值
        }

        // 2. 为每个手机号创建WebSocket连接
        for (String phoneNumber : allPhoneNumbers) {
            connectSingleUser(phoneNumber);
        }

        System.out.println("已为 " + allPhoneNumbers.size() + " 个用户建立WebSocket连接");
    }

    /**
     * 为单个用户建立WebSocket连接
     * @param phoneNumber 手机号
     */
    public void connectSingleUser(String phoneNumber) {
        try {
            String serverWebSocketUrl = "ws://localhost:8089/sms-websocket/" + phoneNumber;

            WebSocketClient client = new StandardWebSocketClient();
            WebSocketConnectionManager connectionManager = new WebSocketConnectionManager(
                    client,
                    myMessageHandler,
                    serverWebSocketUrl
            );

            // 配置连接管理器
            connectionManager.setAutoStartup(true);

            // 存储连接管理器以便后续管理
            connectionManagerMap.put(phoneNumber, connectionManager);

            // 启动连接
            connectionManager.start();

            System.out.println("WebSocket连接已启动: " + serverWebSocketUrl);

        } catch (Exception e) {
            System.err.println("为用户 " + phoneNumber + " 建立连接失败: " + e.getMessage());
        }
    }

    /**
     * 动态添加新用户连接
     * @param phoneNumber 新用户手机号
     */
    public void addUserConnection(String phoneNumber) {
        if (!connectionManagerMap.containsKey(phoneNumber)) {
            connectSingleUser(phoneNumber);
        }
    }

    /**
     * 移除用户连接
     * @param phoneNumber 要移除的用户手机号
     */
    public void removeUserConnection(String phoneNumber) {
        WebSocketConnectionManager manager = connectionManagerMap.get(phoneNumber);
        if (manager != null) {
            manager.stop();
            connectionManagerMap.remove(phoneNumber);
            System.out.println("已移除用户 " + phoneNumber + " 的WebSocket连接");
        }
    }

    /**
     * 获取所有活跃连接
     */
    public static ConcurrentHashMap<String, WebSocketConnectionManager> getConnectionManagerMap() {
        return connectionManagerMap;
    }
}