package com.example.module1.config;

import com.example.module1.filter.MyInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class MyConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private MyInterceptor myInterceptor;

    /**
     * 添加自定义拦截器
     * @param registry
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(myInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/error");
    }
}
