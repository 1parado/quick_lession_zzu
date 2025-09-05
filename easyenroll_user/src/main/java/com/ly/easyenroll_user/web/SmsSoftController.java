package com.ly.easyenroll_user.web;

import com.ly.easyenroll_user.client.SmsWebClient;
import com.ly.easyenroll_user.mapper.SUserMapper;
import com.ly.easyenroll_user.mapper.SmsMapper;
import com.ly.easyenroll_user.pojo.Sms;
import com.ly.easyenroll_user.result.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sms")
public class SmsSoftController {

    @Autowired
    private SmsMapper smsMapper;

    @Autowired
    private SUserMapper sUserMapper;

    /**
     * 获取所有的短信数据
     */
    @GetMapping("/sms")
    public R getAll() {
        List<Sms> smss = smsMapper.selectAll();
        return R.success(smss);
    }


    /**
     * 获取单个短信
     */
    @GetMapping("/sms/{id}")
    public R getOne(@PathVariable int id){
        Sms sms = smsMapper.selectByPrimaryKey(id);
        return R.success(sms);
    }

    /**
     * 设置短信是否已读
     */
    @PutMapping("/read/{id}")
    public R setRead(@PathVariable int id){
        int i = smsMapper.updateRead(id);
        if (i != 1) {
            return R.error("设置是否已读失败");
        }
        return R.success("设置是否已读成功");
    }


    /**
     * 获得所有用户手机号
     */
    @GetMapping("/user")
    public R getAllPhone() {
        List<Long> phones = sUserMapper.selectAllPhone();
        return R.success(phones);
    }

    /**
     * 切换用户时（切换手机号）将新的手机号传来
     */
    @GetMapping("/phone/{phone}")
    public R getPhone(@PathVariable Long phone){
        //SmsWebClient.phoneNumber = String.valueOf(phone);
        return R.success("成功");
    }
}
