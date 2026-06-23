package com.community.vo;

import lombok.Data;

/**
 * 用户信息VO
 */
@Data
public class UserVO {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 性别（0未知 1男 2女）
     */
    private Integer gender;

    /**
     * 用户角色
     */
    private String role;

    /**
     * 积分余额
     */
    private Integer points;

    /**
     * 志愿时长（小时）
     */
    private Integer volunteerHours;
}
