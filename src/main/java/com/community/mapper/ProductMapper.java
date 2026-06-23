package com.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 团购商品Mapper接口
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    /**
     * 更新库存
     */
    int updateStock(@Param("id") Long id, @Param("quantity") Integer quantity);

    /**
     * 增加销量
     */
    int increaseSales(@Param("id") Long id, @Param("quantity") Integer quantity);
}
