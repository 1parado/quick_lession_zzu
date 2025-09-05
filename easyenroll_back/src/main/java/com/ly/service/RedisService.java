package com.ly.service;

import java.util.concurrent.TimeUnit;

public interface RedisService {

    void setValue(String key, Object value);

    Object getValue(String key);

    Boolean removeValue(String key);

    Boolean expire(String key, Long timeOut, TimeUnit timeUnit);

    void setValueEx(String key, String value, long expire, TimeUnit timeUnit);

    boolean isSeckilled(Long studentSno, String courseCode);

    void markSeckilled(Long studentSno, String courseCode);

    void clear(Long studentSno, String courseCode);
}
