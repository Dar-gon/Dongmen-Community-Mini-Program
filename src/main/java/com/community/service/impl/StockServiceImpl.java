package com.community.service.impl;

import com.community.service.StockService;
import com.community.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final RedisUtil redisUtil;

    // Redis Key前缀
    private static final String GROUP_STOCK_KEY = "group:stock:";
    private static final String EXCHANGE_STOCK_KEY = "exchange:stock:";

    /**
     * 团购商品库存原子扣减（Redis DECR）
     * @return true扣减成功，false库存不足
     */
    @Override
    public boolean deductGroupStock(Long productId, int quantity) {
        String key = GROUP_STOCK_KEY + productId;
        Long remaining = redisUtil.decrement(key, quantity);
        if (remaining != null && remaining >= 0) {
            return true;
        }
        // 库存不足，回滚
        redisUtil.increment(key, quantity);
        return false;
    }

    @Override
    public void restoreGroupStock(Long productId, int quantity) {
        String key = GROUP_STOCK_KEY + productId;
        redisUtil.increment(key, quantity);
    }

    /**
     * 积分商品库存原子扣减
     */
    @Override
    public boolean deductExchangeStock(Long goodsId, int quantity) {
        String key = EXCHANGE_STOCK_KEY + goodsId;
        Long remaining = redisUtil.decrement(key, quantity);
        if (remaining != null && remaining >= 0) {
            return true;
        }
        redisUtil.increment(key, quantity);
        return false;
    }

    @Override
    public void restoreExchangeStock(Long goodsId, int quantity) {
        String key = EXCHANGE_STOCK_KEY + goodsId;
        redisUtil.increment(key, quantity);
    }

    @Override
    public void initGroupStock(Long productId, int stock) {
        String key = GROUP_STOCK_KEY + productId;
        redisUtil.set(key, stock);
    }

    @Override
    public void initExchangeStock(Long goodsId, int stock) {
        String key = EXCHANGE_STOCK_KEY + goodsId;
        redisUtil.set(key, stock);
    }

    @Override
    public Integer getGroupStock(Long productId) {
        String key = GROUP_STOCK_KEY + productId;
        return redisUtil.get(key);
    }

    @Override
    public Integer getExchangeStock(Long goodsId) {
        String key = EXCHANGE_STOCK_KEY + goodsId;
        return redisUtil.get(key);
    }
}
