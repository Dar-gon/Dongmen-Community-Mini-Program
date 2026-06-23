package com.community.interceptor;

import com.community.annotation.RequireRole;
import com.community.exception.UnauthorizedException;
import com.community.exception.ForbiddenException;
import com.community.util.JwtUtil;
import com.community.util.RedisUtil;
import com.community.common.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

/**
 * 认证拦截器
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    public AuthInterceptor(JwtUtil jwtUtil, RedisUtil redisUtil) {
        this.jwtUtil = jwtUtil;
        this.redisUtil = redisUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 放行OPTIONS请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 非方法处理器直接放行
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        // 获取Token
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("未登录或登录已过期");
        }

        String token = authHeader.substring(7);

        // 验证Token
        if (!jwtUtil.validateToken(token)) {
            throw new UnauthorizedException("Token无效或已过期");
        }

        // 检查Token是否在Redis中（防止Token被注销后仍可使用）
        Long userId = jwtUtil.getUserId(token);
        String storedToken = redisUtil.get(Constants.REDIS_USER_TOKEN + userId);
        if (storedToken == null || !storedToken.equals(token)) {
            throw new UnauthorizedException("登录已过期，请重新登录");
        }

        // 获取用户角色
        String role = jwtUtil.getRole(token);

        // 检查角色权限
        RequireRole requireRole = handlerMethod.getMethodAnnotation(RequireRole.class);
        if (requireRole != null) {
            String[] allowedRoles = requireRole.value();
            boolean hasRole = Arrays.asList(allowedRoles).contains(role);
            if (!hasRole) {
                throw new ForbiddenException("权限不足，无法访问");
            }
        }

        // 将用户信息存入请求属性
        request.setAttribute("userId", userId);
        request.setAttribute("username", jwtUtil.getUsername(token));
        request.setAttribute("role", role);

        return true;
    }
}
