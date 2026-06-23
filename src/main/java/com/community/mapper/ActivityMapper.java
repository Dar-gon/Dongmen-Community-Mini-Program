package com.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.entity.Activity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 志愿活动Mapper接口
 */
@Mapper
public interface ActivityMapper extends BaseMapper<Activity> {

    /**
     * 增加参与人数
     */
    int incrementParticipants(@Param("id") Long id);

    /**
     * 减少参与人数
     */
    int decrementParticipants(@Param("id") Long id);
}
