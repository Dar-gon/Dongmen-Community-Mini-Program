package com.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.entity.VolunteerTeam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 志愿者团队Mapper接口
 */
@Mapper
public interface VolunteerTeamMapper extends BaseMapper<VolunteerTeam> {

    /**
     * 根据负责人ID查询团队列表
     */
    List<VolunteerTeam> selectByLeaderId(@Param("leaderId") Long leaderId);
}
