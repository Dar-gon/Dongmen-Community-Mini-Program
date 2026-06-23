package com.community.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 兑换商品VO
 */
@Data
public class ExchangeGoodsVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 封面图片URL
     */
    private String coverImg;

    /**
     * 所需积分
     */
    private Integer integralPrice;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 兑换人数
     */
    private Integer exchangeCount;

    /**
     * 分类
     */
    private String category;

    /**
     * 状态：0下架 1上架
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
