package com.community.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果
 */
@Data
public class PageResult<T> implements Serializable {

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 当前页数据
     */
    private List<T> records;

    /**
     * 当前页码
     */
    private Integer current;

    /**
     * 每页大小
     */
    private Integer size;

    /**
     * 总页数
     */
    private Integer pages;

    public PageResult() {
    }

    public PageResult(Long total, List<T> records, Integer current, Integer size) {
        this.total = total;
        this.records = records;
        this.current = current;
        this.size = size;
        this.pages = (int) Math.ceil((double) total / size);
    }

    public static <T> PageResult<T> of(Long total, List<T> records, Integer current, Integer size) {
        return new PageResult<>(total, records, current, size);
    }
}
