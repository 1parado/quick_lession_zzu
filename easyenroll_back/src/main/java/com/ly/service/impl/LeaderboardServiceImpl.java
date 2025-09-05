package com.ly.service.impl;

import com.ly.mapper.GradeMapper;
import com.ly.mapper.StudentsMapper;
import com.ly.service.LeaderboardService;
import com.ly.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class LeaderboardServiceImpl implements LeaderboardService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private GradeMapper gradeMapper;

    @Autowired
    private StudentsMapper studentsMapper;

    /**
     * 更新用户分数
     * @param sno
     * @param score
     */
    @Override
    public void updateScore(String sno, double score) {
        // 如果需要实现“时间优先”，可以在这里处理最终分数
        // double finalScore = calculateFinalScore(score, System.currentTimeMillis());
        redisTemplate.opsForZSet().add(Constant.LEADERBOARD_KEY, sno, score);
    }

    /**
     * 获取排行榜前N名
     * @param topN
     * @return
     */
    @Override
    public Set<ZSetOperations.TypedTuple<String>> getTopN(long topN) {
        // 从0到topN-1
        return redisTemplate.opsForZSet().reverseRangeWithScores(Constant.LEADERBOARD_KEY, 0, topN - 1);
    }

    /**
     * 获取我的排名
     * @param sno
     * @return
     */
    @Override
    public long getUserRank(String sno) {
        System.out.println(sno);
        Long rank = redisTemplate.opsForZSet().reverseRank(Constant.LEADERBOARD_KEY, String.valueOf((sno)));
        System.out.println(rank);
        return rank == null ? -1 : rank + 1; // ZREVRANK返回从0开始的排名，+1后更符合常识
    }

    /**
     * 获取我的分数
     * @param sno
     * @return
     */
    @Override
    public Double getUserScore(String sno) {
        return redisTemplate.opsForZSet().score(Constant.LEADERBOARD_KEY, String.valueOf(sno));
    }

    @Override
    public void addLeaderboardData() {
        //找到所有sno
        List<Long> snoList = studentsMapper.selectAllSno();
        //在grade表，根据sno找到所有的grade字段值，取平均
        for (Long sno : snoList) {
            Double avgGrade = gradeMapper.selectAvgGradesBySno(sno);
            //存入缓存
            redisTemplate.opsForZSet().add(Constant.LEADERBOARD_KEY, String.valueOf(sno), avgGrade);
        }
    }
}
