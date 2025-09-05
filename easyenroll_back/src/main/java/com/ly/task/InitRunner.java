package com.ly.task;


import com.ly.mapper.SeckillTasksMapper;
import com.ly.po.TaskCoursePO;
import com.ly.pojo.SeckillTasks;
import com.ly.service.LeaderboardService;
import com.ly.service.RedisService;
import com.ly.service.TaskService;
import com.ly.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Component
public class InitRunner implements CommandLineRunner {
    //在项目启动时执行热点数据缓存到Redis中的操作

    @Autowired
    private RedisService redisService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private SeckillTasksMapper tasksMapper;

    @Autowired
    private LeaderboardService leaderboardService;

    @Override
    public void run(String... args) throws Exception {
        catchSeckillCourse();

        //缓存一个 发送短信功能的 开关
        String SmsSwitchKey = Constant.SWITCH_SMS;
        redisService.setValue(SmsSwitchKey, "true");

        //缓存 排行榜 数据；（sno 和 平均成绩）
        leaderboardService.addLeaderboardData();
        System.out.println("成功缓存排行榜数据");

    }

    private void catchSeckillCourse(){
        //从数据库加载所有商品信息
        List<TaskCoursePO> taskList = tasksMapper.selectAllTask();
        redisService.setValue(Constant.SECKILL_LIST, taskList);
        //加载入Redis中
        for (TaskCoursePO seckillTasks : taskList) {
            System.out.println(seckillTasks);
            //拼一个Redis的Key
            String key = Constant.SECKILL_STOCK_PREFIX + seckillTasks.getCourseCode();
            System.out.println(key);
            //使用String存储
            redisService.setValue(key, seckillTasks.getStore());

            //Date now = new Date();

            //设置过期时间
            //Long d = seckillTasks.getEndTime().getTime() - now.getTime();
            //redisService.expire(key, d, TimeUnit.SECONDS);
        }

        //log.info("成功缓存 {} 个秒杀商品到Redis", goodsList.size());
        System.out.println("成功缓存 " + taskList.size() + "个秒杀任务到Redis");
    }
}
