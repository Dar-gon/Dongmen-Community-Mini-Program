package com.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 团购订单Mapper接口
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 根据订单编号查询
     */
    Order selectByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 生成订单编号
     */
    String generateOrderNo();

    /**
     * 根据用户ID和状态查询订单列表
     */
    List<Order> selectByUserId(@Param("userId") Long userId, @Param("status") Integer status);
}
