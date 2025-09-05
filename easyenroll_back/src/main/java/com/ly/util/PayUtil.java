package com.ly.util;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.ly.result.R;
import com.ly.service.PaymentSelectionService;
import com.ly.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class PayUtil {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentSelectionService paymentSelectionService;

    //appid
    private final String APP_ID = "9021000151623438";
    //应用私钥
    private final String APP_PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCaGNiiHoo8pzFrVvFnJrBu88mifUSwunZBqWfFzqwWWSoEqkysEf1uPn4ahIPmREpi3ROpzF+j+1aNJiZg2ltZqWghJp4N14dgOM3XSq8Lsc1e5jQ3c0GmmwplHzM0OJDyWZPYQAfRC89uNg3/YKR8fCd+VlQIN3Bb0WG0XAK29Ty83pgIKpeUHeoCYNi+a6F8R4kZ1LhCYbxACLjK8IDnP8yaqC4oLC/1OQAFobwHVfLukMOfych45eyLlrKVfXD/KI0xoEgEG8jYzZOOTiKWGlKMAx8x6g554Cwi2DUDHciVHEPFkF/c7FmrrXSzim6/et5zaPDcJ1x+oRLTqZc1AgMBAAECggEAJkd1yQ/9FwQRarC2VwFtYqJ8vDiSD7TCG52RgbnNVd/gaDhFw5jUc8ZiXQP+H/1m3Rg+AjgaqgmNfpKzheu4NZW3oESPjrCzmFuEBq8uvKD2e1SENCIVdOWet3AAsEA9AXbvGxEM4AknBiG3aHEBa0sMJH4dcc0TxDtHIh6ZLglq1aHc1gdosZI3ScSChAr1/MQtkboIjl+eZ4wrbCx+WYRmgO5gLlj/JfNQdJH6TL85Ms6dTeAjuKUEb3Exm2Gj7snFptGmZd/UYFILc609lMonS6W3GjAqBKG/GfzcL54YT9xGwHgwUtT+9O5qyocnEXNTYRla8CT5DSzzn8nZAQKBgQDJCTvTnSHRvgRAucf/nneelMLG+uTpc0Pb8Vvr1qpRSt8RNgw4JyMqT08wUEG3cubKtcG2TqifCtDQT8I6T91tBceWfgVYavlEh253zgNHm/eHG278Cl9gdvnjED9wwCnWKmaC5GbspW/FE1dE/LVRq518t0rjRoNQ1cwwq1VyiQKBgQDEOkrLlx9vxlRlSiw3dwuhKcfFrRhnpdofGAm4EPUhzeY0M5jMao/KqkcNmmp6GH+rRaSn+n6a41CorjfViuOUgokGCipj1++oq9hx1Uo05CAWs60gxAvvObBCWLrjAwLrBuEs/dxStpXWe7ImQ7I+Bv7k34SkJEw4KIxpMMwETQKBgHRdyuqHxt6wfae1ogXdhWIeu8wPyFc1KC90uv99Z2Lc0tgUj+glB17jP3Q40IFOmjb2FGXhjAtbaFlcAZJdTk9gDBKZaaCvcw/u0Mu6TP2zUwk6oPh1TJX66FQ9wGsUYGMece4MJWOPs23A7z/4gz/99ZAOTOPXmgU9bA8z7ab5AoGAWW9wE6bHTzyHzrqluInrpQLT6IbiLqtq5O0h0OD5uPSNTZGOvOx8qRQ+2jZc/VUZK0jMqTsEZTfh1V/CVvO8kj2Eklojw+nxc2EPf6KE9eJbtb0i1WJFRj121mZ2l20R8WjrTIRP3OsXE/DA26vTG2440rwFSoK4sofYp0D0W6kCgYBmnr3+zk9O9JpXzctfZBZILbrSDCzL4XyRB2aNC45psRLEYk1Y6HyOwSh9ipes/YkAnYn0TQtXs7npkhuRcP/NVeYLkq8zZH0G+E0+ILVDAvvi1hTkJ+y8hjFiD3gCkRCDx9WocDx2hv8h7wNnPCVLZUJ4w5q69EZZ0alzGg19qQ==";

    private final String CHARSET = "UTF-8";
    // 支付宝公钥
    private final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnfDxFS64Vac19Eofyk+poEOVkIOrCx//prPRqzdxuiU0wc/73FRxt3UO44IGbY+rbiAqVWJ6SHVjwD/wj5otSihFzIb2o/QjOCMykD64FaBJ/Gv14M3UWmukW9cmrW+yjG8SuqyX0TqP4Ze77lopzFxHgLd2NQ2hOZ7Vy2zuBeXRMbDwQvPKDNpUh2TNnxHHmL+BRXEjU6/IJii1hl/mfVEwcGBa1+lc+YIYFSqrpkgK2jSaNXub9WYau+kGN0S6IQ95rrC3bFeXKz+UNbXP4PG5nk4XQJNswtvFW46aIfEd93FoQ8rTlKBcu12BjN0Fo+EYbqYlkEq6tLzYTtZ7VwIDAQAB";
    //这是沙箱接口路径,正式路径为https://openapi.alipay.com/gateway.do
    private final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private final String FORMAT = "JSON";
    //签名方式
    private final String SIGN_TYPE = "RSA2";
    //支付宝异步通知路径,付款完毕后会异步调用本项目的方法,必须为公网地址
    private final String NOTIFY_URL = "http://d4ee694f.natappfree.cc/pay/alipay?status=1";
    //支付宝同步通知路径,也就是当付款完毕后跳转本项目的页面,可以不是公网地址
    private final String RETURN_URL = "http://localhost:5173/studentPayment";
    private AlipayClient alipayClient = null;
    //支付宝官方提供的接口
    public String sendRequestToAlipay(String outTradeNo, BigDecimal totalAmount, String subject) throws AlipayApiException {
        //获得初始化的AlipayClient
        alipayClient = new DefaultAlipayClient(GATEWAY_URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(RETURN_URL);
        alipayRequest.setNotifyUrl(NOTIFY_URL);

        //商品描述（可空）
        String body = "商品描述";
        alipayRequest.setBizContent("{\"out_trade_no\":\"" + outTradeNo + "\","  // 注意字段名是 out_trade_no
                + "\"total_amount\":\"" + totalAmount.setScale(2, RoundingMode.HALF_UP).toString() + "\","  // 金额保留两位小数并转为字符串
                + "\"subject\":\"" + subject + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");  // 确保 product_code 必填

        //请求
        String result = alipayClient.pageExecute(alipayRequest).getBody();
        System.out.println("返回的结果是："+result );

        return result;
    }

    // 通过订单编号查询支付状态
    public boolean query(String outTradeNo) {
        try {
            // 创建查询请求
            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", outTradeNo);
            request.setBizContent(bizContent.toString());

            // 执行查询请求
            AlipayTradeQueryResponse response = alipayClient.execute(request);

            // 判断查询是否成功且交易状态为成功
            if (response.isSuccess()) {
                String tradeStatus = response.getTradeStatus();
                // 判断交易状态是否为交易成功（TRADE_SUCCESS）或交易完成（TRADE_FINISHED）
                if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
                    System.out.println("支付宝接口调用成功，订单支付成功");
                    return true;
                } else {
                    System.out.println("支付宝接口调用成功，但订单未支付成功，当前状态: " + tradeStatus);
                    return false;
                }
            } else {
                System.out.println("支付宝查询接口调用失败，错误信息: " + response.getSubMsg());
                return false;
            }
        } catch (AlipayApiException e) {
            System.out.println("支付宝查询接口调用异常: " + e.getMessage());
            return false;
        }
    }
}
