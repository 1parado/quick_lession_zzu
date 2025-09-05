package com.ly.easyenroll_sms.web;

import com.ly.easyenroll_sms.mapper.SmsMapper;
import com.ly.easyenroll_sms.pojo.Sms;
import com.ly.easyenroll_sms.result.R;
import com.ly.easyenroll_sms.server.SmsWebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private SmsMapper smsMapper;

    @PostMapping("/sms")
    public void sms(@RequestBody Sms sms) {
        try {
            System.out.println(sms);
            //拿到短信了
            //给几个人发短信就调用几次这个方法

            //持久化短信到数据库
            sms.setType(1);
            sms.setStatus(0);
            sms.setDatereceive(new Date());
            smsMapper.insert(sms);

            //WebSocket长连接到用户系统
            SmsWebSocketServer.sendMessageToUser(String.valueOf(sms.getReceiver()), sms);

            smsMapper.updateByPrimaryKey(sms);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @GetMapping("/sms")
    public R getSms() {
        List<Sms> smss = smsMapper.selectAll();
        return R.success(smss);
    }

    @GetMapping("/test")
    public void test() {
        System.out.println("test");
    }

}
