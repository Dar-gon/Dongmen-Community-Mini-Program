




package com.community.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * 志愿活动DTO
 */
@Data
public class ActivityDTO {

    /**
     * 活动标题
     */
    @NotBlank(message = "标题不能为空")
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
     * 积分奖励
     */
    private Integer integralReward;
}
