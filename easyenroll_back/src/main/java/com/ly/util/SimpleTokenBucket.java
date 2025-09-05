package com.ly.util;

import java.util.concurrent.TimeUnit;

public class SimpleTokenBucket {
    private final long capacity;    //桶容量
    private final long refillTokens;    //每次补充的令牌数
    private final long refillPeriod;    //补充周期
    private long availableTokens;   //可用令牌
    private long lastRefillTime;    //上次补充的时间

    public SimpleTokenBucket(long capacity, long refillTokens, long refillPeriod, TimeUnit unit) {
        this.capacity = capacity;
        this.refillTokens = refillTokens;
        this.refillPeriod = unit.toMillis(refillPeriod);
        this.availableTokens = capacity;
        this.lastRefillTime = System.currentTimeMillis();
    }

    public synchronized boolean tryAcquire() {
        //补充令牌
        refill();
        if (availableTokens > 0) {
            availableTokens --;
            return true;
        }
        return false;
    }

    private void refill() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastRefillTime;
        //  如果距离上次补充的时间超过补充周期，就进行补充
        if (elapsed > refillPeriod) {
            //补充次数
            long refillCount = elapsed / refillPeriod;
            // 补充 补充次数*一次补充的个数，但最大不得超过令牌桶容量
            availableTokens = Math.min(capacity, availableTokens + refillCount * refillTokens);
            lastRefillTime = now;
        }
    }
}
