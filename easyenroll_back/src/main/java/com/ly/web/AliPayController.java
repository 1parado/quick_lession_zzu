package com.ly.web;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.ly.mapper.PaymentSelectionMapper;
import com.ly.po.PaymentPO;
import com.ly.pojo.PaymentSelection;
import com.ly.result.R;
import com.ly.service.PaymentSelectionService;
import com.ly.service.PaymentService;
import com.ly.util.PayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/alipay")
public class AliPayController {

    @Autowired
    private PayUtil payUtil;

    @Autowired
    private PaymentSelectionService paymentSelectionService;

    @Autowired
    private PaymentService paymentService;

    private PaymentPO paymentPO = null;
    private String tokens = "";

    @ResponseBody
    @PostMapping("/pay")
    public String alipay(@RequestHeader String Authorization, @RequestBody PaymentPO paymentPO) throws AlipayApiException {
        //先生成缴费信息（status=0）
        paymentSelectionService.generateSelection(paymentPO);

        this.tokens = Authorization;
        this.paymentPO = paymentPO;
        //生成订单号
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String outTradeNo = paymentPO.getId() + "_" + paymentPO.getPayerId();
        //缴费项目id_学生学号
        BigDecimal totalAmount = paymentPO.getAmount();
        String subject = "欢迎你";

        // 修改缴费信息状态
        //paymentSelectionService.updateStatus(paymentPO.getId(), paymentPO.getPayerId());
        // 使缴费项目的已缴费人数更新
        //paymentService.updatePaid_count(paymentPO.getId());

        //调用封装好的方法（给支付宝接口发送请求）
        //订单号，金额，主题
        String s = payUtil.sendRequestToAlipay(outTradeNo, totalAmount, subject);

        return s;
    }





}