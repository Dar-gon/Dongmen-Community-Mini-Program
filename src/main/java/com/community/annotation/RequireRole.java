package com.community.annotation;

import java.lang.annotation.*;

/**
 * 角色权限注解
 * 标注在Controller方法上，用于权限校验
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireRole {

    /**
     * 允许访问的角色列表
     */
    String[] value();
}
