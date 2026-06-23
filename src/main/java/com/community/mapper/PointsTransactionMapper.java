package com.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.entity.PointsTransaction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 积分流水Mapper接口
 */
@Mapper
public interface PointsTransactionMapper extends BaseMapper<PointsTransaction> {

    /**
     * 根据用户ID和类型查询流水记录
     */
    List<PointsTransaction> selectByUserId(@Param("userId") Long userId, @Param("type") String type);
}
