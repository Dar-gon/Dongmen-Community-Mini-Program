package com.community.vo;

import lombok.Data;

/**
 * 统计数据VO（管理后台用）
 */
@Data
public class StatisticsVO {

    /**
     * 用户总数
     */
    private Long totalUsers;

    /**
     * 志愿者总数
     */
    private Long totalVolunteers;

    /**
     * 工单总数
     */
    private Long totalPetitions;

    /**
     * 订单总数
     */
    private Long totalOrders;

    /**
     * 积分发放总数
     */
    private Long totalPoints;
}
