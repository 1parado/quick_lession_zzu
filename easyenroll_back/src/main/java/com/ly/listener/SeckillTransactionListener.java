package com.ly.listener;

import com.ly.exception.BusinessException;
import com.ly.mapper.CoursesMapper;
import com.ly.mapper.SeckillTasksMapper;
import com.ly.mapper.SelectionsMapper;
import com.ly.mapper.StudentsMapper;
import com.ly.message.SeckillMessage;
import com.ly.po.CoursesPO;
import com.ly.po.TaskCoursePO;
import com.ly.pojo.Selections;
import com.ly.service.RedisService;
import com.ly.util.Constant;
import com.ly.util.JSONUtils;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RocketMQTransactionListener
public class SeckillTransactionListener implements RocketMQLocalTransactionListener {

    private static final Logger log = LoggerFactory.getLogger(SeckillTransactionListener.class);

    @Autowired
    private StudentsMapper studentsMapper;

    @Autowired
    private CoursesMapper coursesMapper;

    @Autowired
    private SelectionsMapper selectionsMapper;

    @Autowired
    private SeckillTasksMapper tasksMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedissonClient redissonClient;


    /**
     * 本地事务
     * @param message
     * @param o
     * @return
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        try {
            //解析message
            byte[] payloadBytes = (byte[]) message.getPayload();
            String s = new String(payloadBytes, StandardCharsets.UTF_8);
            SeckillMessage seckillMessage = JSONUtils.toBean(s, SeckillMessage.class);


            //保存selection到数据库
            Integer studentId = studentsMapper.selectIdBySno(seckillMessage.getStudentSno());
            CoursesPO coursesPO = coursesMapper.selectByCourseCodePO(seckillMessage.getCourseCode());
            Integer courseId = coursesPO.getId();

            // 加锁
            RLock lock = redissonClient.getLock("seckill:lock:" + courseId + "_" + studentId);

            String semester = coursesPO.getSemester();
            Date selectionTime = new Date();
            String status = "NORMAL";
            Integer isSeckill = 1;
            Selections selections = new Selections(null, studentId, courseId, semester, selectionTime, status, isSeckill);

            try {
                lock.tryLock(5,30,TimeUnit.SECONDS);

                selectionsMapper.insertSelective(selections);

                //更新库存
                tasksMapper.updateStockBycourseId(courseId);

                //更新course表的remain字段（减一）
                coursesMapper.updateRemainByCourseId(courseId);

                lock.unlock();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }

            //更新秒杀结果信息到redis,方便前端轮询
            String key = Constant.SECKILL_STATUS_PREFIX + seckillMessage.getStudentSno() + "_" + seckillMessage.getCourseCode();
            redisService.setValueEx(key, String.valueOf(1) , Constant.SECKILL_STATUS_EXPIRE, TimeUnit.SECONDS);

            //重新缓存数据
            //从数据库加载所有商品信息
            List<TaskCoursePO> taskList = tasksMapper.selectAllTask();
            redisService.setValue(Constant.SECKILL_LIST, taskList);
            //加载入Redis中
            for (TaskCoursePO seckillTasks : taskList) {

                //拼一个Redis的Key
                String key1 = Constant.SECKILL_STOCK_PREFIX + seckillTasks.getCourseCode();

                //使用String存储
                redisService.setValue(key1, seckillTasks.getStore());

                Date now = new Date();

                //设置过期时间
                Long d = seckillTasks.getEndTime().getTime() - now.getTime();
                redisService.expire(key1, d, TimeUnit.SECONDS);
            }

            log.info("本地事务执行成功，消息：{}", message);
            return RocketMQLocalTransactionState.COMMIT; // 提交事务，消息对消费者可见
        } catch (Exception e) {
            log.error("本地事务执行失败，消息：{}", message, e);
            return RocketMQLocalTransactionState.ROLLBACK; // 回滚事务，消息被丢弃
        } finally {

        }
    }

    /**
     *事务状态回查，如果Broker在一定时间内未收到确切回复（提交/回滚，会回调此方法）
     * @param message
     * @return
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        log.warn("事务状态回查，消息：{}", message);
        return RocketMQLocalTransactionState.UNKNOWN;
    }
}
