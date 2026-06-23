package com.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.entity.ActivitySignup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 活动报名Mapper接口
 */
@Mapper
public interface ActivitySignupMapper extends BaseMapper<ActivitySignup> {

    /**
     * 根据活动ID和用户ID查询报名记录
     */
    ActivitySignup selectByActivityAndUser(@Param("activityId") Long activityId, @Param("userId") Long userId);

    /**
     * 根据用户ID查询报名记录列表
     */
    List<ActivitySignup> selectByUserId(@Param("userId") Long userId);

    /**
     * 更新签到时间
     */
    int updateCheckIn(@Param("id") Long id, @Param("checkInTime") LocalDateTime checkInTime);
}
