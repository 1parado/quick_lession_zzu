package com.ly.service.impl;

import com.ly.mapper.MessageMapper;
import com.ly.mapper.StudentsMapper;
import com.ly.mapper.TeachersMapper;
import com.ly.pojo.Message;
import com.ly.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private StudentsMapper studentsMapper;

    @Autowired
    private TeachersMapper teachersMapper;

    @Override
    public List<Message> selectByCourseCode(String courseCode) {
        return messageMapper.selectByCourseCode(courseCode);
    }

    @Override
    public void insert(Message message) {
        //前端给了数据：courseCode，content，account

        String name = studentsMapper.selectNameBySno(message.getAccount());
        if (name == null) {
            //说明是工号不是学号
            message.setType(0);
            name = teachersMapper.selectNameByTno(message.getAccount());
        } else {
            message.setType(1);
        }
        //说明是学号不是工号
        message.setName(name);
        message.setCreateTime(new Date());

        messageMapper.insert(message);
    }

    @Override
    public void deleteById(int messageId) {
        messageMapper.deleteByPrimaryKey(messageId);
    }
}
