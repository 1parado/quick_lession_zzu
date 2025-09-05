package com.ly.util;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;

public class PathUtil {
    /**
     * 构建原始字符串
     */
    public static String buildRawString(Long studentSno, String courseCode) {
        return String.format("%d_%s_%d_%s",
                studentSno,
                courseCode,
                System.currentTimeMillis(),
                UUID.randomUUID().toString().replace("-", ""));
    }

    /**
     * 生成安全路径（MD5+盐值）
     */
    public static String generateSecurePath(String raw) {
        String salt = Constant.SALT; // 建议配置在配置文件中
        return DigestUtils.md5DigestAsHex(
                (salt + raw).getBytes(StandardCharsets.UTF_8)
        );
    }

    /**
     * 构建Redis key
     */
    public static String buildRedisKey(Long studentSno, String courseCode) {
        return Constant.SECKILL_PATH_PREFIX + studentSno + "_" + courseCode;
    }

    /**
     * 安全字符串比较（防止时序攻击）
     */
    public static boolean safeStringEquals(String a, String b) {
        if (a == null || b == null) {
            return false;
        }
        return MessageDigest.isEqual(
                a.getBytes(StandardCharsets.UTF_8),
                b.getBytes(StandardCharsets.UTF_8)
        );
    }
}
