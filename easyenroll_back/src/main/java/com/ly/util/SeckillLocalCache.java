package com.ly.util;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.ly.mapper.StudentsMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

public class SeckillLocalCache {


    //通过Caffeine创建本地缓存
    private final static Cache<String, Boolean> SECKILL_CACHE = Caffeine.newBuilder()
            .maximumSize(100_000)          // 容量限制（条目数）
            .expireAfterWrite(Constant.ISREPEAT_LOCAL_EXPIRE, TimeUnit.HOURS) // 写入后过期时间
            .recordStats()                // 开启统计（可选）
            .build();

    public static boolean isSeckilled(Long studentSno, String courseCode) {
        String key = studentSno + "_" + courseCode;
        return SECKILL_CACHE.getIfPresent(key) != null;
    }

    public static void markSeckilled(Long studentSno, String courseCode) {
        String key = studentSno + "_" + courseCode;
        SECKILL_CACHE.put(key, true);
    }

    public static void clear(Long studentSno, String courseCode) {
        String key = studentSno + "_" + courseCode;
        SECKILL_CACHE.invalidate(key);
    }
}
