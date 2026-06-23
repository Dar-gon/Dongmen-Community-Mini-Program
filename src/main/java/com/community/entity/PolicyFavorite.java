package com.community.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_policy_favorite")
public class PolicyFavorite {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long policyId;
    private LocalDateTime createTime;
}
