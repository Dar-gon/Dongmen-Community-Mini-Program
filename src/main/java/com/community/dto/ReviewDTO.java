package com.community.dto;

import lombok.Data;

@Data
public class ReviewDTO {
    private Integer status; // 1通过 2驳回
    private String remark;
}
