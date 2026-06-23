package com.community.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 活动报名实体
 */
@Data
@TableName("t_activity_signup")
public class ActivitySignup {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 用户手机号
     */
    private String userPhone;

    /**
     * 签到状态：0未签到 1已签到
     */
    private Integer checkInStatus;

    /**
     * 签到时间
     */
    private LocalDateTime checkInTime;

    /**
     * 获得积分
     */
    private Integer earnedPoints;

    /**
     * 状态：0已报名 1已完成 2已取消
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
