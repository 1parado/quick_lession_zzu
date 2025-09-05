package com.ly.interceptor;

import com.ly.exception.BusinessException;
import com.ly.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.List;

@Component
public class JwtAuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行OPTIONS请求
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            return true;
        }

        // 检查是否是无需验证的路径（如登录）
        if (isExcludePath(request.getRequestURI())) {
            return true;
        }

        // 从请求头获取token
        String token = jwtUtils.getTokenFromHeader(request);
        if (StringUtils.isBlank(token)) {
            throw new BusinessException(401, "未提供认证Token");
        }

        // 验证token
        Claims claims = jwtUtils.getClaimsByToken(token);
        if (claims == null || jwtUtils.isTokenExpired(claims)) {
            throw new BusinessException(401, "Token无效或已过期");
        }

        // 将用户信息存入request
        //request.setAttribute("currentUserId", claims.getSubject());


        return true;
    }

    private boolean isExcludePath(String requestURI) {
        // 定义不需要验证的路径
        List<String> excludePaths = Arrays.asList(
                "/auth/login",
                "/pay/alipay"
        );

        return excludePaths.stream().anyMatch(requestURI::startsWith);
    }

}