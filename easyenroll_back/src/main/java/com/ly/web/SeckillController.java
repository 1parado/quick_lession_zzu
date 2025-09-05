package com.ly.web;

import com.ly.mapper.CoursesMapper;
import com.ly.mapper.SeckillTasksMapper;
import com.ly.mapper.StudentsMapper;
import com.ly.result.R;
import com.ly.service.RedisService;
import com.ly.service.SeckillService;
import com.ly.util.Constant;
import com.ly.util.SimpleTokenBucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/seckill")
public class SeckillController {

    private static final Logger log = LoggerFactory.getLogger(SeckillController.class);

    @Autowired
    private RedisService redisService;

    @Autowired
    private StudentsMapper studentsMapper;

    @Autowired
    private SeckillTasksMapper seckillTasksMapper;

    @Autowired
    private CoursesMapper coursesMapper;

    //创建令牌桶
    /**
     * 容量为10
     * 每次刷新5个令牌
     * 刷新间隔为1s
     */
    private final SimpleTokenBucket tokenBucket = new SimpleTokenBucket(
            10,
            5,
            1,
            TimeUnit.SECONDS
    );

    @Autowired
    private SeckillService seckillService;

    /**
     * 根据学生学号 + 课程代码 获取 动态秒杀地址
     * @param studentSno
     * @param courseCode
     * @return
     */
    @GetMapping("/seckill/{studentSno}/{courseCode}")
    public R getDynamicPath(
            @PathVariable Long studentSno,
            @PathVariable String courseCode
    ) {
        System.out.println(studentSno);
        System.out.println(courseCode);
        try {
            String path = seckillService.getDynamicPath(studentSno, courseCode);
            return R.success(path);
        } catch (Exception e) {
            return R.error("获取动态地址失败");
        }
    }

    /**
     * 根据学生学号 + 课程代码 批量获取 动态秒杀地址
     * @param studentSno
     * @param courseCode
     * @return
     */
    @GetMapping("/seckill/batch/{studentId}")
    public R getBatchDynamicPath(
            @PathVariable int studentId
    ) {
        //根据学生id，获得其所预选的所有课程，先判断课程是否可以抢选，（是否在时间内，是否重复，...）
        List<String> courseCodes = (List<String>) redisService.getValue(Constant.PREDATA_PREFIX + studentId);
        //这里选择使用 学号 + 课程列表中的第一个课程 来生成path
        //注意：生成path的主要目的是防止恶意的频繁请求
        Long sno = studentsMapper.selectSnoById(studentId);
        try {
            String path = seckillService.getDynamicPath(sno, courseCodes.get(0));
            return R.success(path);
        } catch (Exception e) {
            return R.error("获取动态地址失败");
        }
    }

    /**
     *  秒杀
     * @param path
     * @param studentSno
     * @param courseCode
     * @return
     */
    @GetMapping("/seckill/path/{path}/{studentSno}/{courseCode}")
    public R seckill(
            @PathVariable String path,
            @PathVariable Long studentSno,
            @PathVariable String courseCode
            ) {
        if (!tokenBucket.tryAcquire()) {
            return R.error(429,"系统繁忙，请稍后再试");
        }
        //验证path
        boolean isPathCurrent = seckillService.checkSeckillPath(studentSno, courseCode, path);
        if (!isPathCurrent) {
            return R.error("秒杀地址错误");
        }

        //检查重复秒杀
        boolean isNoRepeat = seckillService.checkRepeatSeckill(studentSno, courseCode);
        if (!isNoRepeat) {
            return R.error("重复秒杀");
        }

        //秒杀核心逻辑
        boolean isSuccess = seckillService.seckill(studentSno,courseCode);
        if (!isSuccess) {
            return R.error("秒杀失败");
        }
        return R.success("秒杀成功");
    }

    /**
     *  批量秒杀
     * @param path
     * @param studentSno
     * @param
     * @return
     */
    @GetMapping("/seckill/batch/path/{path}/{studentSno}")
    public R batchSeckill(
            @PathVariable String path,
            @PathVariable Long studentSno
    ) {

        //拿到前端的学生学号，和path

        if (!tokenBucket.tryAcquire()) {
            return R.error(429,"系统繁忙，请稍后再试");
        }
        Integer studentId = studentsMapper.selectIdBySno(studentSno);
        List<String> courseCodes = (List<String>) redisService.getValue(Constant.PREDATA_PREFIX + studentId);

        //验证path
        boolean isPathCurrent = seckillService.checkSeckillPath(studentSno, courseCodes.get(0), path);
        if (!isPathCurrent) {
            return R.error("秒杀地址错误");
        }

        //开始批量秒杀（微观串行）
        StringBuilder message = new StringBuilder();
        Boolean ifSuccess = false;
        for (String courseCode : courseCodes) {

            //判断时间是否合适
            Integer courseId = coursesMapper.selectIdByCourseCode(courseCode);

            if (seckillTasksMapper.selectStartTimeByCourseId(courseId).after(new Date())) {
                //未开始阶段
                message.append("选课未开始：").append(courseCode);
                continue;
            } else if (seckillTasksMapper.selectEndTimeByCourseId(courseId).before(new Date())) {
                //已结束阶段
                message.append("选课已结束：").append(courseCode);
                continue;
            }

            //检查重复秒杀
            boolean isNoRepeat = seckillService.checkRepeatSeckill(studentSno, courseCode);
            if (!isNoRepeat) {
                message.append("重复秒杀：").append(courseCode);
                continue;
                //return R.error("重复秒杀");
            }

            //秒杀核心逻辑
            boolean isSuccess = seckillService.seckill(studentSno,courseCode);
            if (!isSuccess) {
                message.append("秒杀失败：").append(courseCode);
                continue;
                //return R.error("秒杀失败");
            }
            ifSuccess = true;
        }
        if (ifSuccess) {
            return R.success("抢课成功");
        }
        return R.error(message.toString());

    }


    /**
     * 监控秒杀结果
     * @param studentSno
     * @param courseCode
     * @return
     */
    @GetMapping("/seckill/status")
    public R checkStatus(@RequestParam Long studentSno, @RequestParam String courseCode) {
        log.info("监控选课结果");
        String status = seckillService.checkSeckillStatus(studentSno, courseCode);
        if (status.equals("1")) {
            return R.success(1);
        }
        return R.success(status);
    }

    /**
     * 监控批量秒杀结果
     * @param
     * @param
     * @return
     */
    @GetMapping("/seckill/status/batch/{studentId}")
    public R checkBatchStatus(@PathVariable int studentId) {
        List<String> courseCodes = (List<String>) redisService.getValue(Constant.PREDATA_PREFIX + studentId);
        Long studentSno = studentsMapper.selectSnoById(studentId);
        String message = "1";
        for (String courseCode : courseCodes) {
            log.info("监控批量选课结果:" + courseCode);
            // 返回 1 成功 ； 课程代码转的数字，失败
            String status = seckillService.checkSeckillStatus(studentSno, courseCode);
            if (!status.equals("1")) {
                //message存储失败的课程的代码
                message = status;
            }
        }

        //删除Redis中的预选数据
        redisService.removeValue(Constant.PREDATA_PREFIX + studentId);

        return R.success(message);
    }
}
