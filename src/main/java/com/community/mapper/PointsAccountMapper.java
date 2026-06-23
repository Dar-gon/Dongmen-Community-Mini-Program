package com.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.entity.PointsAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 积分账户Mapper接口
 */
@Mapper
public interface PointsAccountMapper extends BaseMapper<PointsAccount> {

    /**
     * 增加积分余额
     */
    int addBalance(@Param("userId") Long userId, @Param("amount") Integer amount);

    /**
     * 扣减积分余额
     */
    int deductBalance(@Param("userId") Long userId, @Param("amount") Integer amount);
}
