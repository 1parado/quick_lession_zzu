package com.ly.service;

import com.ly.pojo.Message;

import java.util.List;

public interface MessageService {
    List<Message> selectByCourseCode(String courseCode);

    void insert(Message message);

    void deleteById(int messageId);
}
