package com.ly.config;

import com.ly.interceptor.JwtAuthenticationInterceptor;
import com.ly.interceptor.RoleInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtAuthenticationInterceptor jwtAuthenticationInterceptor;

    @Autowired
    private RoleInterceptor roleInterceptor;

    // 定义不需要拦截的路径
    public static final List<String> EXCLUDE_PATHS = Arrays.asList(
            "/auth/login",
            "/pay/alipay"
    );

    /**
     * 注册拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthenticationInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(EXCLUDE_PATHS);//不拦截

        registry.addInterceptor(roleInterceptor)
                .addPathPatterns("/**");
    }

    // 可以添加其他MVC配置
}