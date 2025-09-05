package com.ly.service;

public interface SeckillService {
    String getDynamicPath(Long studentSno, String courseCode);

    boolean checkSeckillPath(Long studentSno, String courseCode, String path);

    boolean checkRepeatSeckill(Long studentSno, String courseCode);

    boolean seckill(Long studentSno, String courseCode);

    String checkSeckillStatus(Long studentSno, String courseCode);
}
