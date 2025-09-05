package com.ly;

import com.ly.util.SimpleTokenBucket;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@SpringBootApplication
@MapperScan("com.ly.mapper")
@ComponentScan(basePackages = {"com.ly", "org.apache.rocketmq"})
@EnableScheduling
public class EasyenrollBackApplication {

    public static void main(String[] args) throws InterruptedException {
        // 设置JVM默认时区为UTC（与数据库一致）
        //TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        SpringApplication.run(EasyenrollBackApplication.class, args);

    }

}
