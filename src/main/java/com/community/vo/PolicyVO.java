package com.community.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 政策公告VO
 */
@Data
public class PolicyVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容（富文本）
     */
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
     * 分类名称
     */
    private String categoryName;

    /**
     * 是否置顶：0否 1是
     */
    private Integer top;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 状态：0草稿 1已发布
     */
    private Integer status;

    /**
     * 发布时间
     */
    private LocalDateTime publishTime;

    /**
     * 发布人ID
     */
    private Long publisherId;

    /**
     * 发布人姓名
     */
    private String publisherName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
