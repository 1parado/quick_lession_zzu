package com.ly.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

// JwtConfig.java
@Configuration
@ConfigurationProperties(prefix = "jwt")
//用于将外部配置文件中的前缀为“jwt”的属性批量绑定到 Java 对象中
public class JwtConfig {
    private String secret;       // 密钥
    private Long expire;         // 过期时间(秒)
    private String header;       // 请求头名称
    private String tokenPrefix;  // token前缀

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }
}
