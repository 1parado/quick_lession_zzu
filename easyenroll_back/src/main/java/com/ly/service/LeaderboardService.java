package com.ly.service;

import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;

public interface LeaderboardService {
    void updateScore(String sno, double score);

    Set<ZSetOperations.TypedTuple<String>> getTopN(long topN);

    long getUserRank(String sno);

    Double getUserScore(String sno);

    void addLeaderboardData();
}
