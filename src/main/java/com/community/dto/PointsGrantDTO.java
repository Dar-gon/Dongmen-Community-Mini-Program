package com.community.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 积分发放DTO（管理员用）
 */
@Data
public class PointsGrantDTO {

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 积分数量
     */
    @NotNull(message = "积分数量不能为空")
    private Integer amount;

    /**
     * 描述
     */
    @NotBlank(message = "描述不能为空")
    private String description;
}
