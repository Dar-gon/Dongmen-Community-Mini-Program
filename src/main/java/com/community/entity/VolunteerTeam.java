package com.community.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 志愿者团队实体
 */
@Data
@TableName("t_volunteer_team")
public class VolunteerTeam {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String description;
    private Long leaderId;
    private String leaderName;
    private Integer memberCount;
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;
}
