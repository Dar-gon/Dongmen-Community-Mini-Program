package com.community.service;

public interface StockService {
    // 团购商品库存原子扣减
    boolean deductGroupStock(Long productId, int quantity);

    // 团购商品库存回滚
    void restoreGroupStock(Long productId, int quantity);

    // 积分商品库存原子扣减
    boolean deductExchangeStock(Long goodsId, int quantity);

    // 积分商品库存回滚
    void restoreExchangeStock(Long goodsId, int quantity);

    // 初始化团购商品库存到Redis
    void initGroupStock(Long productId, int stock);

    // 初始化积分商品库存到Redis
    void initExchangeStock(Long goodsId, int stock);

    // 获取团购商品库存
    Integer getGroupStock(Long productId);

    // 获取积分商品库存
    Integer getExchangeStock(Long goodsId);
}
