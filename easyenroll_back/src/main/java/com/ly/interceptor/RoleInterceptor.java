package com.ly.interceptor;

import com.ly.annotation.RoleRequire;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.ly.web.AuthController.role;

@Component
public class RoleInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是控制器方法直接放行
        if(!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        //获取方法上的注解
        RoleRequire roleAnnotation  = handlerMethod.getMethodAnnotation(RoleRequire.class);

        //如果方法没有注解，检查类上的注解
        if (roleAnnotation == null) {
            roleAnnotation = handlerMethod.getBeanType().getAnnotation(RoleRequire.class);
        }

        //若都无注解，不需要权限控制，通用
        if (roleAnnotation == null) {
            return true;
        }

        //验证角色
        boolean hasPermission = false;
        for( int i = 0; i < roleAnnotation.value().length; i++) {
           if (role.equals(roleAnnotation.value()[i])){
               hasPermission = true;
           }
        }
        //如果没有角色权限
        if (!hasPermission) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{'code': 403, 'message': '权限不足'}");
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }

        return true;
    }
}
