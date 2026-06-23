package com.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.entity.PetitionLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 民情工单日志Mapper接口
 */
@Mapper
public interface PetitionLogMapper extends BaseMapper<PetitionLog> {

    /**
     * 根据工单ID查询日志列表
     */
    List<PetitionLog> selectByPetitionId(@Param("petitionId") Long petitionId);
}
