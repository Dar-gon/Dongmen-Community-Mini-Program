package com.community.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 微信登录请求DTO
 */
@Data
public class WxLoginDTO {

    /**
     * 微信登录code
     */
    @NotBlank(message = "code不能为空")
    private String code;
}
