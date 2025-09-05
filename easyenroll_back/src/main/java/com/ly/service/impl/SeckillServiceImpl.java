package com.ly.service.impl;

import com.ly.mapper.SelectionsMapper;
import com.ly.message.SeckillMessage;
import com.ly.service.RedisService;
import com.ly.service.SeckillService;
import com.ly.service.SelectionService;
import com.ly.util.Constant;
import com.ly.util.PathUtil;
import com.ly.util.SeckillLocalCache;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Transactional
@Service
public class SeckillServiceImpl implements SeckillService {

    private static final Logger log = LoggerFactory.getLogger(SeckillServiceImpl.class);

    @Autowired
    private RedisService redisService;

    @Autowired
    private SelectionService selectionService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public String getDynamicPath(Long studentSno, String courseCode) {
        if (studentSno == null || courseCode == null) {
            return null;
        }

        //生成随机字符串
        String raw = PathUtil.buildRawString(studentSno, courseCode);
        String path = PathUtil.generateSecurePath(raw);

        //将path存入Redis中，设置有效期,默认为30s
        String key = PathUtil.buildRedisKey(studentSno, courseCode);
        redisService.setValueEx(key, path, Constant.SECKILL_PATH_EXPIRE, TimeUnit.SECONDS);

        return path;
    }

    @Override
    public boolean checkSeckillPath(Long studentSno, String courseCode, String path) {
        if(studentSno == null || courseCode == null || StringUtils.isEmpty(path)) {
            return false;
        }

        // 从Redis获取存储的路径
        String key = PathUtil.buildRedisKey(studentSno, courseCode);
        String realPath = (String) redisService.getValue(key);

        // 安全比较（防止时序攻击）
        boolean isValid = PathUtil.safeStringEquals(path, realPath);

        // 4. 验证成功则重置key
        if (isValid) {
            redisService.setValueEx(key, path, Constant.SECKILL_PATH_EXPIRE, TimeUnit.SECONDS);
        }
        return isValid;
    }

    @Override
    public boolean checkRepeatSeckill(Long studentSno, String courseCode) {
        //从本地内存中校验
        if (SeckillLocalCache.isSeckilled(studentSno, courseCode)) {
            log.info("命中本地内存，重复");
            return false;
        }
        log.info("未命中本地内存");

        //从Redis中校验
        if (redisService.isSeckilled(studentSno, courseCode)) {
            //回写本地内存
            SeckillLocalCache.markSeckilled(studentSno, courseCode);
            log.info("命中Redis，重复");
            return false;
        }
        log.info("未命中Redis");

        //从数据库中校验
        if (selectionService.isSeckilled(studentSno, courseCode)) {
            //回写本地内存和Redis
            SeckillLocalCache.markSeckilled(studentSno, courseCode);
            redisService.markSeckilled(studentSno, courseCode);
            log.info("命中数据库，重复");
            return false;
        }
        log.info("未命中数据库");

        return true;
    }

    @Override
    public boolean seckill(Long studentSno, String courseCode) {
        //拿到存储库存的key
        String key = Constant.SECKILL_STOCK_PREFIX + courseCode;
        String overKey = Constant.SECKILL_STOCK_OVER + courseCode;

        //使用原子类缓存一个库存售尽标志，实现高效、无锁化的线程安全操作
        // 检查是否已售罄（使用Redis存储售罄状态）
        Object isOver = redisService.getValue(overKey);
        if (Boolean.TRUE.equals(isOver)) {
            log.info("课程 {} 已售罄", courseCode);
            return false;
        }

        //第一次判断库存
        //这里用到原子类AtomicLong
        Long stock = (Long) redisService.getValue(key);
        if (stock <= 0) {
            log.info("第一次，库存不足");
            redisService.setValue(overKey, true);
            return false;
        }
        log.info("第一次，库存足够" + stock);

        //Redis预减库存
        redisTemplate.opsForValue().decrement(key, 1L);

        Long newStock = (Long) redisService.getValue(key);
        if (newStock < 0) {
            //库存不足，恢复数据
            redisTemplate.opsForValue().increment(key, 1L);
            log.info("第二次，库存不足");
            redisService.setValue(overKey, true);
            return false;
        }
        log.info("第二次，库存足够" + newStock);

        //更新选课信息到缓存中，以便快速检查是否重复
        SeckillLocalCache.markSeckilled(studentSno, courseCode);
        redisService.markSeckilled(studentSno, courseCode);

        //异步MQ，写入数据库

        //构建Message对象
        SeckillMessage myMessage = new SeckillMessage(studentSno, courseCode);
        Message<SeckillMessage> message = MessageBuilder.withPayload(myMessage).build();

        //发送事务消息
        rocketMQTemplate.sendMessageInTransaction("select-topic", message, null);

        return true;
    }

    @Override
    public String checkSeckillStatus(Long studentSno, String courseCode) {
        //查询Redis
        String key = Constant.SECKILL_STATUS_PREFIX + studentSno + "_" + courseCode;
        String value =(String) redisService.getValue(key);
        if ("1".equals(value)) {
            return "1";
        }

        //查询数据库
        boolean is = selectionService.isSeckilled(studentSno, courseCode);
        if (is) {
            return "1";
        }
        return courseCode;
    }
}
