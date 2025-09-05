package com.ly.util;

import com.ly.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// JwtUtils.java
@Component
public class JwtUtils {
    @Autowired
    private JwtConfig jwtConfig;

    /**
     * 获取签名密钥
     * @return
     */
    private Key getSigningKey() {
        byte[] keyBytes = jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //JWT 由三部分组成：Header.Payload.Signature。
    //Signature 部分就是使用这个密钥对 Header + Payload 进行签名，防止数据被篡改。

    /**
     * 生成JWT token
     */
    public String generateToken(String account, String role) {

        return Jwts.builder()
                .subject(account)  //设置主题（通常是用户名、账号）
                .issuedAt(new Date())   // 设置签发时间
                .expiration(new Date(System.currentTimeMillis() + jwtConfig.getExpire() * 1000))//设置过期时间
                .claim("role", role)  // 添加自定义数据，角色，根据账号去DB查询
                .signWith(getSigningKey())  // 使用密钥签名
                .compact(); // 生成最终的 JWT 字符串
    }

    /**
     * 解析JWT token
     */
    public Claims getClaimsByToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(getSigningKey()) //设置用于验证签名的密钥
                    .build()
                    .parseClaimsJws(token)// 解析并验证 JWS
                    .getBody();// 提取 JWT 的 payload 部分作为 Claims 对象返回
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 验证token是否过期
     */
    public boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());//将过期时间与当前时间比较：返回 true 表示已过期，返回 false 表示仍有效
    }

    /**
     * 从请求头中获取token
     */
    public String getTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader(jwtConfig.getHeader());
        if (StringUtils.isNotBlank(header)){
            return header.replace(jwtConfig.getTokenPrefix(), "");
        }
        return null;
    }
}
