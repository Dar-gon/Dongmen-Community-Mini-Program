package com.community.config;

import com.community.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    public WebConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/auth/login",
                        "/api/auth/register",
                        "/api/policy/list",
                        "/api/policy/*",
                        "/api/policy/*/isfavorite",
                        "/api/volunteer/activity/list",
                        "/api/volunteer/activity/[0-9]*",
                        "/api/groupbuy/product/list",
                        "/api/groupbuy/product/[0-9]*",
                        "/api/exchange/goods/list",
                        "/api/exchange/goods/[0-9]*",
                        "/doc.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**"
                );
    }
}
