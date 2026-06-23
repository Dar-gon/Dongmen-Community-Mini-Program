package com.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.entity.ExchangeRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 积分兑换记录Mapper接口
 */
@Mapper
public interface ExchangeRecordMapper extends BaseMapper<ExchangeRecord> {

    /**
     * 根据用户ID查询兑换记录列表
     */
    List<ExchangeRecord> selectByUserId(@Param("userId") Long userId);
}
