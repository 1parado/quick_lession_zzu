package com.ly.util;

import java.net.URI;

public class Constant {
    public static final String SALT = "SALT_2028_8_9";
    public static final String SECKILL_PATH_PREFIX = "seckill:path:";
    public static final long SECKILL_PATH_EXPIRE = 30;
    public static final String SECKILL_STOCK_PREFIX = "seckill:store:";
    public static final String SECKILL_LIST = "seckill:list:";
    public static final long ISREPEAT_REDIS_EXPIRE = 1;
    public static final long ISREPEAT_LOCAL_EXPIRE = 1;
    public static final String SECKILL_STATUS_PREFIX = "seckill:status:";
    public static final long SECKILL_STATUS_EXPIRE = 60;
    public static final String SWITCH_SMS = "switch:sms";
    public static final String SMS_REPEAT = "sms:repeat:";
    public static final Long PHONE_SCHOOLE = 88888888L;
    public static final String TOPIC_SMS = "SMS_TOPIC";
    public static final String SMS_SERVICE_URL = "http://localhost:8089/sms/sms";
    public static final Object LEADERBOARD_KEY = "leaderboard:key";
    public static final Object LEADERBOARD_TIME_KEY = "leaderboard:key:time";
    public static final String PREDATA_PREFIX = "predata:key:";
    public static final String SECKILL_STOCK_OVER = "seckill:store:over:";
    public static Long MAXTimestamp = 2000000000000L;
}
