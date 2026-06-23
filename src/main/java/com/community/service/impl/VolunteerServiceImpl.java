package com.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.community.dto.ActivityDTO;
import com.community.entity.*;
import com.community.exception.BusinessException;
import com.community.mapper.*;
import com.community.service.PointsRankService;
import com.community.service.VolunteerService;
import com.community.vo.*;
import com.community.vo.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VolunteerServiceImpl implements VolunteerService {

    private final ActivityMapper activityMapper;
    private final ActivitySignupMapper activitySignupMapper;
    private final VolunteerTeamMapper volunteerTeamMapper;
    private final VolunteerTeamMemberMapper volunteerTeamMemberMapper;
    private final PointsAccountMapper pointsAccountMapper;
    private final PointsTransactionMapper pointsTransactionMapper;
    private final PointsRankService pointsRankService;

    @Override
    public PageResult<ActivityVO> getActivityList(Integer status, Integer current, Integer size, Long userId) {
        QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", 0);
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        queryWrapper.orderByDesc("create_time");

        long total = activityMapper.selectCount(queryWrapper);

        int offset = (current - 1) * size;
        queryWrapper.last("LIMIT " + offset + ", " + size);
        List<Activity> activities = activityMapper.selectList(queryWrapper);

        List<ActivityVO> voList = activities.stream().map(activity -> {
            ActivityVO vo = new ActivityVO();
            vo.setId(activity.getId());
            vo.setTitle(activity.getTitle());
            vo.setDescription(activity.getDescription());
            vo.setCoverImg(activity.getCoverImg());
            vo.setActivityTime(activity.getActivityTime());
            vo.setActivityEndTime(activity.getActivityEndTime());
            vo.setLocation(activity.getLocation());
            vo.setMaxParticipants(activity.getMaxParticipants());
            vo.setCurrentParticipants(activity.getCurrentParticipants());
            vo.setIntegralReward(activity.getIntegralReward());
            vo.setStatus(activity.getStatus());
            vo.setPublisherId(activity.getPublisherId());
            vo.setPublisherName(activity.getPublisherName());
            vo.setCreateTime(activity.getCreateTime());
            vo.setUpdateTime(activity.getUpdateTime());
            if (userId != null) {
                ActivitySignup signup = activitySignupMapper.selectByActivityAndUser(activity.getId(), userId);
                vo.setIsSignedUp(signup != null);
                vo.setIsCanSignUp(activity.getStatus() == 0 && signup == null);
            }
            return vo;
        }).collect(Collectors.toList());

        return new PageResult<>(total, voList, current, size);
    }

    @Override
    public ActivityVO getActivityDetail(Long id, Long userId) {
        Activity activity = activityMapper.selectById(id);
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }

        ActivityVO vo = new ActivityVO();
        vo.setId(activity.getId());
        vo.setTitle(activity.getTitle());
        vo.setDescription(activity.getDescription());
        vo.setCoverImg(activity.getCoverImg());
        vo.setActivityTime(activity.getActivityTime());
        vo.setActivityEndTime(activity.getActivityEndTime());
        vo.setLocation(activity.getLocation());
        vo.setMaxParticipants(activity.getMaxParticipants());
        vo.setCurrentParticipants(activity.getCurrentParticipants());
        vo.setIntegralReward(activity.getIntegralReward());
        vo.setStatus(activity.getStatus());
        vo.setPublisherId(activity.getPublisherId());
        vo.setPublisherName(activity.getPublisherName());
        vo.setCreateTime(activity.getCreateTime());
        vo.setUpdateTime(activity.getUpdateTime());
        if (userId != null) {
            ActivitySignup signup = activitySignupMapper.selectByActivityAndUser(id, userId);
            vo.setIsSignedUp(signup != null);
            vo.setIsCanSignUp(activity.getStatus() == 0 && signup == null);
        }
        return vo;
    }

    @Override
    @Transactional
    public void createActivity(Long userId, ActivityDTO activityDTO) {
        Activity activity = new Activity();
        activity.setTitle(activityDTO.getTitle());
        activity.setDescription(activityDTO.getDescription());
        activity.setCoverImg(activityDTO.getCoverImg());
        activity.setActivityTime(activityDTO.getActivityTime());
        activity.setActivityEndTime(activityDTO.getActivityEndTime());
        activity.setLocation(activityDTO.getLocation());
        activity.setMaxParticipants(activityDTO.getMaxParticipants());
        activity.setIntegralReward(activityDTO.getIntegralReward());
        activity.setCurrentParticipants(0);
        activity.setStatus(0); // 报名中
        activity.setPublisherId(userId);
        activityMapper.insert(activity);
    }

    @Override
    @Transactional
    public void signupActivity(Long userId, Long activityId) {
        Activity activity = activityMapper.selectById(activityId);
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }
        if (activity.getStatus() != 0) {
            throw new BusinessException("活动不在报名阶段");
        }
        if (activity.getMaxParticipants() != null &&
                activity.getCurrentParticipants() >= activity.getMaxParticipants()) {
            throw new BusinessException("报名人数已满");
        }
        if (activitySignupMapper.selectByActivityAndUser(activityId, userId) != null) {
            throw new BusinessException("您已报名该活动");
        }

        ActivitySignup signup = new ActivitySignup();
        signup.setUserId(userId);
        signup.setActivityId(activityId);
        signup.setStatus(0); // 待签到
        signup.setCheckInStatus(0);
        activitySignupMapper.insert(signup);

        activityMapper.incrementParticipants(activityId);
    }

    @Override
    @Transactional
    public void checkinActivity(Long userId, Long signupId) {
        ActivitySignup signup = activitySignupMapper.selectById(signupId);
        if (signup == null) {
            throw new BusinessException("报名记录不存在");
        }
        if (signup.getStatus() != 0) {
            throw new BusinessException("该记录已签到或已取消");
        }

        activitySignupMapper.updateCheckIn(signupId, LocalDateTime.now());
        signup.setStatus(1); // 已完成
        signup.setCheckInStatus(1);
        activitySignupMapper.updateById(signup);

        // 发放积分
        grantPoints(signup.getUserId(), 10, "活动签到", "activity", signup.getActivityId());
    }

    @Override
    @Transactional
    public void cancelSignup(Long userId, Long signupId) {
        ActivitySignup signup = activitySignupMapper.selectById(signupId);
        if (signup == null) {
            throw new BusinessException("报名记录不存在");
        }
        if (!signup.getUserId().equals(userId)) {
            throw new BusinessException("无权取消他人的报名");
        }
        if (signup.getStatus() != 0) {
            throw new BusinessException("已签到的记录不能取消");
        }

        signup.setStatus(2); // 已取消
        activitySignupMapper.updateById(signup);
        activityMapper.decrementParticipants(signup.getActivityId());
    }

    @Override
    public List<ActivitySignup> getMySignups(Long userId) {
        return activitySignupMapper.selectByUserId(userId);
    }

    @Override
    public List<VolunteerTeam> getTeamList() {
        QueryWrapper<VolunteerTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", 0);
        return volunteerTeamMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public void createTeam(Long userId, String name, String description) {
        QueryWrapper<VolunteerTeam> nameCheck = new QueryWrapper<>();
        nameCheck.eq("name", name).eq("deleted", 0);
        if (volunteerTeamMapper.selectCount(nameCheck) > 0) {
            throw new BusinessException("小队名称已存在");
        }
        VolunteerTeam team = new VolunteerTeam();
        team.setName(name);
        team.setDescription(description);
        team.setLeaderId(userId);
        team.setMemberCount(1);
        team.setStatus(1);
        volunteerTeamMapper.insert(team);

        // 创建者自动成为队长
        VolunteerTeamMember member = new VolunteerTeamMember();
        member.setTeamId(team.getId());
        member.setUserId(userId);
        member.setRole("leader");
        member.setStatus(1);
        volunteerTeamMemberMapper.insert(member);
        volunteerTeamMemberMapper.incrementMemberCount(team.getId());
    }

    @Override
    @Transactional
    public void applyJoinTeam(Long userId, Long teamId) {
        if (volunteerTeamMemberMapper.selectByTeamAndUser(teamId, userId) != null) {
            throw new BusinessException("您已申请加入该小队");
        }
        VolunteerTeamMember member = new VolunteerTeamMember();
        member.setTeamId(teamId);
        member.setUserId(userId);
        member.setRole("member");
        member.setStatus(0); // 待审核
        volunteerTeamMemberMapper.insert(member);
    }

    @Override
    @Transactional
    public void approveJoinTeam(Long teamId, Long userId, Long operatorId) {
        VolunteerTeamMember member = volunteerTeamMemberMapper.selectByTeamAndUser(teamId, userId);
        if (member == null) {
            throw new BusinessException("未找到该申请记录");
        }
        member.setStatus(1); // 已通过
        volunteerTeamMemberMapper.updateById(member);
        volunteerTeamMemberMapper.incrementMemberCount(teamId);
    }

    @Override
    public List<VolunteerTeam> getMyTeams(Long userId) {
        // 查询用户作为队长的团队
        QueryWrapper<VolunteerTeam> leaderQuery = new QueryWrapper<>();
        leaderQuery.eq("leader_id", userId).eq("deleted", 0);
        List<VolunteerTeam> teams = volunteerTeamMapper.selectList(leaderQuery);

        // 查询用户作为成员的团队
        QueryWrapper<VolunteerTeamMember> memberQuery = new QueryWrapper<>();
        memberQuery.eq("user_id", userId).eq("status", 1);
        List<VolunteerTeamMember> memberships = volunteerTeamMemberMapper.selectList(memberQuery);

        for (VolunteerTeamMember membership : memberships) {
            boolean alreadyIncluded = teams.stream().anyMatch(t -> t.getId().equals(membership.getTeamId()));
            if (!alreadyIncluded) {
                VolunteerTeam team = volunteerTeamMapper.selectById(membership.getTeamId());
                if (team != null && team.getDeleted() == 0) {
                    teams.add(team);
                }
            }
        }
        return teams;
    }

    @Override
    public PointsAccountVO getPointsAccount(Long userId) {
        PointsAccount account = pointsAccountMapper.selectById(userId);
        if (account == null) {
            // 创建积分账户
            account = new PointsAccount();
            account.setUserId(userId);
            account.setBalance(0);
            account.setTotalEarned(0);
            account.setTotalSpent(0);
            pointsAccountMapper.insert(account);
        }

        PointsAccountVO vo = new PointsAccountVO();
        vo.setUserId(userId);
        vo.setBalance(account.getBalance());
        vo.setTotalEarned(account.getTotalEarned());
        vo.setTotalSpent(account.getTotalSpent());
        return vo;
    }

    @Override
    public PageResult<PointsTransactionVO> getPointsHistory(Long userId, String type, Integer current, Integer size) {
        QueryWrapper<PointsTransaction> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        if (type != null) {
            queryWrapper.eq("type", type);
        }
        queryWrapper.orderByDesc("create_time");

        long total = pointsTransactionMapper.selectCount(queryWrapper);

        int offset = (current - 1) * size;
        queryWrapper.last("LIMIT " + offset + ", " + size);
        List<PointsTransaction> transactions = pointsTransactionMapper.selectList(queryWrapper);

        List<PointsTransactionVO> voList = transactions.stream().map(t -> {
            PointsTransactionVO vo = new PointsTransactionVO();
            vo.setId(t.getId());
            vo.setUserId(t.getUserId());
            vo.setAmount(t.getAmount());
            vo.setType(t.getType());
            vo.setDescription(t.getDescription());
            vo.setSourceType(t.getSourceType());
            vo.setSourceId(t.getSourceId());
            vo.setBalanceAfter(t.getBalanceAfter());
            vo.setCreateTime(t.getCreateTime());
            return vo;
        }).collect(Collectors.toList());

        return new PageResult<>(total, voList, current, size);
    }

    @Override
    @Transactional
    public void grantPoints(Long userId, Integer amount, String description, String sourceType, Long sourceId) {
        PointsAccount account = pointsAccountMapper.selectById(userId);
        if (account == null) {
            account = new PointsAccount();
            account.setUserId(userId);
            account.setBalance(0);
            account.setTotalEarned(0);
            account.setTotalSpent(0);
            pointsAccountMapper.insert(account);
        }

        account.setBalance(account.getBalance() + amount);
        account.setTotalEarned(account.getTotalEarned() + amount);
        pointsAccountMapper.updateById(account);

        PointsTransaction transaction = new PointsTransaction();
        transaction.setUserId(userId);
        transaction.setAmount(amount);
        transaction.setType("earn");
        transaction.setDescription(description);
        transaction.setSourceType(sourceType);
        transaction.setSourceId(sourceId);
        transaction.setBalanceAfter(account.getBalance());
        pointsTransactionMapper.insert(transaction);

        // 同步积分排行榜
        pointsRankService.updateRank(userId, amount);
    }
}
