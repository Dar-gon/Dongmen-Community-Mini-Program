package com.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.entity.VolunteerTeamMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 志愿者团队成员Mapper接口
 */
@Mapper
public interface VolunteerTeamMemberMapper extends BaseMapper<VolunteerTeamMember> {

    /**
     * 根据团队ID查询成员列表
     */
    List<VolunteerTeamMember> selectByTeamId(@Param("teamId") Long teamId);

    /**
     * 根据团队ID和用户ID查询成员
     */
    VolunteerTeamMember selectByTeamAndUser(@Param("teamId") Long teamId, @Param("userId") Long userId);

    /**
     * 增加团队成员数量
     */
    int incrementMemberCount(@Param("teamId") Long teamId);
}
