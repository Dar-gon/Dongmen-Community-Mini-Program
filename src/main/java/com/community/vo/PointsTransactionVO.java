package com.community.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 积分流水VO
 */
@Data
public class PointsTransactionVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 类型：earn-获得 spend-消费 exchange-兑换 refund-退款
     */
    private String type;

    /**
     * 积分数量（正数=获得，负数=消费）
     */
    private Integer amount;

    /**
     * 来源：activity-活动 order-订单 redeem-兑换 admin-管理员发放
     */
    private String sourceType;

    /**
     * 来源ID（关联业务表）
     */
    private Long sourceId;

    /**
     * 操作后余额
     */
    private Integer balanceAfter;

    /**
     * 描述
     */
    private String description;

    /**
     * 操作人ID
     */
    private Long operatorId;

    /**
     * 操作人姓名
     */
    private String operatorName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
