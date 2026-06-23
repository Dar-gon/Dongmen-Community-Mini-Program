package com.community.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 活动VO
 */
@Data
public class ActivityVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 活动标题
     */
    private String title;

    /**
     * 活动描述
     */
    private String description;

    /**
     * 封面图片URL
     */
    private String coverImg;

    /**
     * 活动时间
     */
    private LocalDateTime activityTime;

    /**
     * 活动结束时间
     */
    private LocalDateTime activityEndTime;

    /**
     * 活动地点
     */
    private String location;

    /**
     * 最大参与人数
     */
    private Integer maxParticipants;

    /**
     * 当前参与人数
     */
    private Integer currentParticipants;

    /**
     * 积分奖励
     */
    private Integer integralReward;

    /**
     * 状态：0未开始 1进行中 2已结束
     */
    private Integer status;

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

    /**
     * 当前用户是否已报名
     */
    private Boolean isSignedUp;

    /**
     * 当前用户是否可以报名
     */
    private Boolean isCanSignUp;
}
