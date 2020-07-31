package com.example.module2.filter;

import com.example.module2.annotation.AccessLimit;
import com.example.module2.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义拦截器需要实现HandlerInterceptor接口，覆写其preHandle()默认方法，在该方法中自定义拦截规则，
 * 然后需要在配置类（需要继承WebMvcConfigurationSupport抽象类）中来配置该自定义拦截器
 *
 */
@Component
public class MyInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisUtils redisUtils;



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        System.out.println("-----this is my interceptor----");
        System.out.println(request.getRequestURI());

        // 判断请求如果不属于方法就放行
        if (!(handler instanceof HandlerMethod)) return true;

        HandlerMethod hm = (HandlerMethod) handler;
        // 获取handler方法上的注解，如果没有注解就放行
        AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
        if (accessLimit == null) return true;

        int seconds = accessLimit.seconds();
        int maxCount = accessLimit.maxCount();
        boolean login = accessLimit.needLogin();
        String key = request.getRequestURI();
        // 如果需要登录
        if (login) {
            // 获取登录的session进行判断
            // ...
            key += "" + "1"; // 这里假设用户是1，项目中是动态获取的userId
        }

        // 从redis中获取用户访问的次数
        Integer count = (Integer) redisUtils.get(key);
        if (count == null) {
            redisUtils.set(key, 1, seconds);
        } else if (count < maxCount) {
            // 这里不对，有bug
            redisUtils.set(key, ++count, seconds);
        } else {
            response.setContentType("application/json;charset=UTF-8");
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write("访问过于频繁".getBytes());
            outputStream.flush();
            outputStream.close();
            return false;
        }
        return true;
    }
}
