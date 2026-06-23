package com.community.service;

import com.community.dto.*;
import com.community.entity.ActivitySignup;
import com.community.entity.VolunteerTeam;
import com.community.vo.*;
import com.community.vo.PageResult;
import java.util.List;

public interface VolunteerService {
    PageResult<ActivityVO> getActivityList(Integer status, Integer current, Integer size, Long userId);
    ActivityVO getActivityDetail(Long id, Long userId);
    void createActivity(Long userId, ActivityDTO activityDTO);
    void signupActivity(Long userId, Long activityId);
    void checkinActivity(Long userId, Long signupId);
    void cancelSignup(Long userId, Long signupId);
    List<ActivitySignup> getMySignups(Long userId);

    // 志愿小队
    List<VolunteerTeam> getTeamList();
    void createTeam(Long userId, String name, String description);
    void applyJoinTeam(Long userId, Long teamId);
    void approveJoinTeam(Long teamId, Long userId, Long operatorId);
    List<VolunteerTeam> getMyTeams(Long userId);

    // 积分
    PointsAccountVO getPointsAccount(Long userId);
    PageResult<PointsTransactionVO> getPointsHistory(Long userId, String type, Integer current, Integer size);
    void grantPoints(Long userId, Integer amount, String description, String sourceType, Long sourceId);
}
