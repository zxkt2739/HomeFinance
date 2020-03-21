package com.example.module1.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义拦截器需要实现HandlerInterceptor接口，覆写其preHandle()默认方法，在该方法中自定义拦截规则，
 * 然后需要在配置类（需要继承WebMvcConfigurationSupport抽象类）中来配置该自定义拦截器
 */
@Component
public class MyInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        System.out.println("-----this is my interceptor----");
        System.out.println(request.getRequestURI());

        // 在拦截器中自定义拦截规则
        String requestURI = request.getRequestURI();
        if (requestURI.contains("/dd")) {
            throw new Exception("you are not allowed to view this page.");
        } else {
            return true;
        }
    }
}
