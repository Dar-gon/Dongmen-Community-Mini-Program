package com.community.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 民情工单DTO
 */
@Data
public class PetitionDTO {

    /**
     * 问题标题
     */
    @NotBlank(message = "标题不能为空")
    private String title;

    /**
     * 问题描述
     */
    @NotBlank(message = "内容不能为空")
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
}
