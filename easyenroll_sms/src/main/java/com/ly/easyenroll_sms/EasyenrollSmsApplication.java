package com.ly.easyenroll_sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;


@SpringBootApplication
@MapperScan("com.ly.easyenroll_sms.mapper")
public class EasyenrollSmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyenrollSmsApplication.class, args);
    }

}
