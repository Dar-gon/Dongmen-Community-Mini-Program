package com.community.dto;

import lombok.Data;

/**
 * 分页请求DTO
 */
@Data
public class PageDTO {

    /**
     * 当前页码，默认第1页
     */
    private Integer current = 1;

    /**
     * 每页大小，默认10条
     */
    private Integer size = 10;

    /**
     * 搜索关键词
     */
    private String keyword;
}
