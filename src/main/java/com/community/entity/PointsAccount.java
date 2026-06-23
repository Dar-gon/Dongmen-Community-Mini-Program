package com.community.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 积分账户实体
 */
@Data
@TableName("t_points_account")
public class PointsAccount {

    /**
     * 用户ID（主键）
     */
    @TableId(type = IdType.INPUT)
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

    /**
     * 乐观锁版本号
     */
    @Version
    private Integer version;

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
