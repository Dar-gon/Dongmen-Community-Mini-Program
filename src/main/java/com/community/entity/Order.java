package com.community.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 团购订单实体
 */
@Data
@TableName("t_order")
public class Order {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 订单编号
     */
    private String orderNo;

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
     * 总金额
     */
    private BigDecimal totalAmount;

    /**
     * 积分抵扣金额
     */
    private BigDecimal pointsDeductionAmount;

    /**
     * 实际支付金额
     */
    private BigDecimal actualAmount;

    /**
     * 使用积分
     */
    private Integer usedPoints;

    /**
     * 收货地址
     */
    private String address;

    /**
     * 收货人
     */
    private String receiver;

    /**
     * 收货电话
     */
    private String receiverPhone;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态：0待支付 1已支付 2已完成 3已取消
     */
    private Integer status;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 完成时间
     */
    private LocalDateTime completeTime;

    /**
     * 取消时间
     */
    private LocalDateTime cancelTime;

    /**
     * 取消原因
     */
    private String cancelReason;

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
