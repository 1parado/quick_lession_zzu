package com.ly.web;


import com.ly.pojo.Message;
import com.ly.result.R;
import com.ly.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;


    /**
     * 获取所有课程相关的留言
     * @param courseCode
     * @return
     */
    @GetMapping("/message/{courseCode}")
    public R getAllMessage(@PathVariable String courseCode) {
        List<Message> messageList = messageService.selectByCourseCode(courseCode);
        return R.success(messageList);
    }

    /**
     * 添加留言
     * @param message
     * @return
     */
    @PostMapping("/message")
    public R saveMessage(@RequestBody Message message) {
        messageService.insert(message);
        return R.success("保存成功");
    }

    @DeleteMapping("/message/{messageId}")
    public R deleteById(@PathVariable int messageId) {
        messageService.deleteById(messageId);
        return R.success("删除成功");
    }
}
