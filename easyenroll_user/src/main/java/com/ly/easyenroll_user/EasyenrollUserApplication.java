package com.ly.easyenroll_user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ly.easyenroll_user.mapper")
public class EasyenrollUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyenrollUserApplication.class, args);
    }

}
