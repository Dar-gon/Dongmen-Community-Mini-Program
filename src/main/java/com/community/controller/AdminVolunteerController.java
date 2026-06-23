package com.community.controller;

import com.community.annotation.RequireRole;
import com.community.dto.ActivityDTO;
import com.community.dto.PointsGrantDTO;
import com.community.entity.Activity;
import com.community.entity.ActivitySignup;
import com.community.mapper.ActivityMapper;
import com.community.mapper.ActivitySignupMapper;
import com.community.service.VolunteerService;
import com.community.vo.ActivityVO;
import com.community.vo.PageResult;
import com.community.vo.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 管理员志愿管理控制器
 */
@RestController
@RequestMapping("/api/admin/volunteer")
@RequiredArgsConstructor
@Tag(name = "管理员-志愿管理", description = "管理员志愿管理接口")
@RequireRole({"admin"})
public class AdminVolunteerController {

    private final VolunteerService volunteerService;
    private final ActivityMapper activityMapper;
    private final ActivitySignupMapper signupMapper;

    @PostMapping("/activity")
    @Operation(summary = "发布活动")
    public Result<Void> createActivity(@RequestAttribute("userId") Long userId,
                                       @RequestBody @Valid ActivityDTO activityDTO) {
        volunteerService.createActivity(userId, activityDTO);
        return Result.success();
    }

    @GetMapping("/activity/list")
    @Operation(summary = "管理员查看所有活动")
    public Result<PageResult<ActivityVO>> allActivities(
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<Activity> page = new Page<>(current, size);
        QueryWrapper<Activity> qw = new QueryWrapper<>();
        if (status != null) qw.eq("status", status);
        qw.orderByDesc("create_time");
        Page<Activity> result = activityMapper.selectPage(page, qw);
        List<ActivityVO> voList = result.getRecords().stream().map(a -> {
            ActivityVO vo = new ActivityVO();
            vo.setId(a.getId());
            vo.setTitle(a.getTitle());
            vo.setDescription(a.getDescription());
            vo.setCoverImg(a.getCoverImg());
            vo.setActivityTime(a.getActivityTime());
            vo.setActivityEndTime(a.getActivityEndTime());
            vo.setLocation(a.getLocation());
            vo.setMaxParticipants(a.getMaxParticipants());
            vo.setCurrentParticipants(a.getCurrentParticipants());
            vo.setIntegralReward(a.getIntegralReward());
            vo.setStatus(a.getStatus());
            vo.setPublisherName(a.getPublisherName());
            vo.setCreateTime(a.getCreateTime());
            return vo;
        }).collect(Collectors.toList());
        return Result.success(new PageResult<>(result.getTotal(), voList, current, size));
    }

    @PutMapping("/activity/{id}")
    @Operation(summary = "编辑活动")
    public Result<Void> updateActivity(@PathVariable Long id,
                                       @RequestBody @Valid ActivityDTO activityDTO) {
        Activity activity = activityMapper.selectById(id);
        if (activity == null) return Result.fail("活动不存在");
        activity.setTitle(activityDTO.getTitle());
        activity.setDescription(activityDTO.getDescription());
        activity.setActivityTime(activityDTO.getActivityTime());
        activity.setActivityEndTime(activityDTO.getActivityEndTime());
        activity.setLocation(activityDTO.getLocation());
        activity.setMaxParticipants(activityDTO.getMaxParticipants());
        activity.setIntegralReward(activityDTO.getIntegralReward());
        activityMapper.updateById(activity);
        return Result.success();
    }

    @PutMapping("/activity/{id}/status")
    @Operation(summary = "上下架活动")
    public Result<Void> updateActivityStatus(@PathVariable Long id,
                                             @RequestParam Integer status) {
        Activity activity = activityMapper.selectById(id);
        if (activity == null) return Result.fail("活动不存在");
        activity.setStatus(status);
        activityMapper.updateById(activity);
        return Result.success();
    }

    @PostMapping("/activity/{id}/checkin")
    @Operation(summary = "核验志愿者签到")
    public Result<Void> checkIn(@PathVariable Long id,
                                @RequestParam Long userId,
                                @RequestParam(required = false) Integer earnedPoints) {
        QueryWrapper<ActivitySignup> qw = new QueryWrapper<>();
        qw.eq("activity_id", id).eq("user_id", userId);
        ActivitySignup signup = signupMapper.selectOne(qw);
        if (signup == null) return Result.fail("未找到报名记录");
        signup.setCheckInStatus(1);
        signup.setCheckInTime(LocalDateTime.now());
        if (earnedPoints != null) signup.setEarnedPoints(earnedPoints);
        signupMapper.updateById(signup);
        // 发放积分
        if (earnedPoints != null && earnedPoints > 0) {
            volunteerService.grantPoints(userId, earnedPoints, "活动签到积分", "activity_checkin", null);
        }
        return Result.success();
    }

    @PostMapping("/points/grant")
    @Operation(summary = "发放积分")
    public Result<Void> grantPoints(@RequestBody @Valid PointsGrantDTO pointsGrantDTO) {
        volunteerService.grantPoints(pointsGrantDTO.getUserId(), pointsGrantDTO.getAmount(),
                pointsGrantDTO.getDescription(), "admin_grant", null);
        return Result.success();
    }
}
