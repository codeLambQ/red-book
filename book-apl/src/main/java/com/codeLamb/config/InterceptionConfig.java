package com.codeLamb.config;

import com.codeLamb.interceptor.PassPortInterceptor;
import com.codeLamb.interceptor.UserTokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author codeLamb
 */
@Configuration
public class InterceptionConfig implements WebMvcConfigurer {

    @Bean
    public PassPortInterceptor passPortInterceptor() {
        return new PassPortInterceptor();
    }

    @Bean
    public UserTokenInterceptor userTokenInterceptor() {
        return new UserTokenInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passPortInterceptor())
                .addPathPatterns("/passport/getSMSCode");

        registry.addInterceptor(userTokenInterceptor())
                .addPathPatterns("/userInfo/modifyImage")
                .addPathPatterns("/userInfo/modifyUserInfo");
    }
}
