package com.ly;

import com.ly.util.SimpleTokenBucket;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class EasyenrollBackApplicationTests {

    public static void main(String[] args) throws InterruptedException {
        contextLoads();
    }

    static void contextLoads() throws InterruptedException {
        // 测试令牌桶：容量2，每秒补充1个令牌
        SimpleTokenBucket bucket = new SimpleTokenBucket(2, 1, 1, TimeUnit.SECONDS);

        // 模拟连续请求
        for (int i = 1; i <= 10; i++) {
            System.out.println("请求" + i + ": " +
                    (bucket.tryAcquire() ? "通过" : "被限流"));
            Thread.sleep(300); // 每300ms请求一次
        }
    }

}
