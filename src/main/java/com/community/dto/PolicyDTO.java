package com.community.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 政策公告DTO
 */
@Data
public class PolicyDTO {

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空")
    private String title;

    /**
     * 内容（富文本）
     */
    @NotBlank(message = "内容不能为空")
    private String content;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 封面图片URL
     */
    private String coverImg;

    /**
     * 分类：1政策 2通知 3公告
     */
    private Integer category;

    /**
     * 是否置顶：0否 1是
     */
    private Integer top;

    /**
     * 发布人姓名
     */
    private String publisherName;
}
