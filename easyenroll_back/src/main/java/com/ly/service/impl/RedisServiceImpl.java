package com.ly.service.impl;

import com.ly.service.RedisService;
import com.ly.util.Constant;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Boolean removeValue(String key) {
        return redisTemplate.delete(key);
    }

    @Override
    public Boolean expire(String key, Long timeOut, TimeUnit timeUnit) {
        return redisTemplate.expire(key, timeOut, timeUnit);
    }

    @Override
    public void setValueEx(String key, String path, long seckillPathExpire, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, path, seckillPathExpire, timeUnit);
    }

    @Override
    public boolean isSeckilled(Long studentSno, String courseCode) {
        String key = buildKey(studentSno);
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, courseCode));
    }

    @Override
    public void markSeckilled(Long studentSno, String courseCode) {
        String key = buildKey(studentSno);
        redisTemplate.opsForSet().add(key, courseCode);
        // 设置过期时间（秒杀活动结束时间+缓冲期）
        redisTemplate.expire(key, Constant.ISREPEAT_REDIS_EXPIRE, TimeUnit.HOURS);
    }

    private String buildKey(Long studentSno) {
        return "seckill:isRepeat:" + studentSno;
    }

    @Override
    public void clear(Long studentSno, String courseCode) {
        String key = buildKey(studentSno);
        redisTemplate.opsForSet().remove(key, courseCode);
    }
}