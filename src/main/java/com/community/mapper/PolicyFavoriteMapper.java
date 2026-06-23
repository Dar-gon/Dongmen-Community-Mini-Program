package com.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.entity.PolicyFavorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PolicyFavoriteMapper extends BaseMapper<PolicyFavorite> {
    @Select("SELECT COUNT(*) FROM t_policy_favorite WHERE user_id=#{userId} AND policy_id=#{policyId}")
    int countByUserAndPolicy(@Param("userId") Long userId, @Param("policyId") Long policyId);
}
