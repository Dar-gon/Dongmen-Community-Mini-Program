package com.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.entity.Petition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 民情工单Mapper接口
 */
@Mapper
public interface PetitionMapper extends BaseMapper<Petition> {

    /**
     * 根据工单编号查询
     */
    Petition selectByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 根据用户ID和状态查询工单列表
     */
    List<Petition> selectByUserId(@Param("userId") Long userId, @Param("status") Integer status);

    /**
     * 生成工单编号
     */
    String generateOrderNo();
}
