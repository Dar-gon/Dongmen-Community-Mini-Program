package com.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.entity.ExchangeGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 积分兑换商品Mapper接口
 */
@Mapper
public interface ExchangeGoodsMapper extends BaseMapper<ExchangeGoods> {

    /**
     * 更新库存
     */
    int updateStock(@Param("id") Long id, @Param("quantity") Integer quantity);

    /**
     * 增加兑换人数
     */
    int incrementExchangeCount(@Param("id") Long id);
}
