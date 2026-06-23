package com.community.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 民情工单实体
 */
@Data
@TableName("t_petition")
public class Petition {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 工单编号（自动生成）
     */
    private String orderNo;

    /**
     * 问题标题
     */
    private String title;

    /**
     * 问题描述
     */
    private String content;

    /**
     * 问题分类
     */
    private String category;

    /**
     * 问题图片（JSON数组）
     */
    private String images;

    /**
     * 问题地址
     */
    private String address;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 是否匿名：0否 1是
     */
    private Integer isAnonymous;

    /**
     * 提交用户ID
     */
    private Long userId;

    /**
     * 提交用户姓名
     */
    private String userName;

    /**
     * 状态：0已提交 1已分派 2处理中 3已办结 4已归档
     */
    private Integer status;

    /**
     * 分派人ID
     */
    private Long assignerId;

    /**
     * 分派人姓名
     */
    private String assignerName;

    /**
     * 处理人ID
     */
    private Long handlerId;

    /**
     * 处理人姓名
     */
    private String handlerName;

    /**
     * 办结时间
     */
    private LocalDateTime resolvedTime;

    /**
     * 办结说明
     */
    private String resolvedNote;

    /**
     * 满意度评价（1-5）
     */
    private Integer rating;

    /**
     * 评价内容
     */
    private String ratingContent;

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

    /**
     * 逻辑删除标志
     */
    @TableLogic
    private Integer deleted;
}
