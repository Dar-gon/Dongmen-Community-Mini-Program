package com.community.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 民情工单日志实体
 */
@Data
@TableName("t_petition_log")
public class PetitionLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long petitionId;
    private String action;
    private Long operatorId;
    private String operatorName;
    private String content;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
