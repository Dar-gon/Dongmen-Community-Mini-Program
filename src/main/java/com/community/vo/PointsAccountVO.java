package com.community.vo;

import lombok.Data;

/**
 * 积分账户VO
 */
@Data
public class PointsAccountVO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 当前余额
     */
    private Integer balance;

    /**
     * 累计获得
     */
    private Integer totalEarned;

    /**
     * 累计消费
     */
    private Integer totalSpent;
}
