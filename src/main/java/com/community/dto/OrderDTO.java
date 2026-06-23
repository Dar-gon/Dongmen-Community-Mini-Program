package com.community.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 下单请求DTO
 */
@Data
public class OrderDTO {

    /**
     * 商品ID
     */
    @NotNull(message = "商品ID不能为空")
    private Long productId;

    /**
     * 数量
     */
    @NotNull(message = "数量不能为空")
    private Integer quantity;

    /**
     * 收货地址
     */
    @NotBlank(message = "收货地址不能为空")
    private String address;

    /**
     * 收货人
     */
    @NotBlank(message = "收货人不能为空")
    private String receiver;

    /**
     * 收货电话
     */
    @NotBlank(message = "收货电话不能为空")
    private String receiverPhone;

    /**
     * 使用积分
     */
    private Integer usedPoints;

    /**
     * 备注
     */
    private String remark;
}
