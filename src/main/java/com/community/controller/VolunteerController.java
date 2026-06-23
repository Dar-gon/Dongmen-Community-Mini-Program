package com.community.controller;

import com.community.annotation.RequireRole;
import com.community.entity.ActivitySignup;
import com.community.entity.VolunteerTeam;
import com.community.service.VolunteerService;
import com.community.vo.ActivityVO;
import com.community.vo.PageResult;
import com.community.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 志愿服务控制器
 */
@RestController
@RequestMapping("/api/volunteer")
@RequiredArgsConstructor
@Tag(name = "志愿服务", description = "志愿服务相关接口")
@RequireRole({"volunteer", "admin"})
public class VolunteerController {

    private final VolunteerService volunteerService;

    @GetMapping("/activity/list")
    @Operation(summary = "活动列表")
    public Result<PageResult<ActivityVO>> activityList(
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestAttribute(value = "userId", required = false) Long userId) {
        PageResult<ActivityVO> pageResult = volunteerService.getActivityList(status, current, size, userId);
        return Result.success(pageResult);
    }

    @GetMapping("/activity/{id}")
    @Operation(summary = "活动详情")
    public Result<ActivityVO> activityDetail(@PathVariable Long id,
                                             @RequestAttribute(value = "userId", required = false) Long userId) {
        ActivityVO activityVO = volunteerService.getActivityDetail(id, userId);
        return Result.success(activityVO);
    }

    @PostMapping("/activity/signup")
    @Operation(summary = "报名活动")
    public Result<Void> signupActivity(@RequestAttribute("userId") Long userId,
                                       @RequestParam Long activityId) {
        volunteerService.signupActivity(userId, activityId);
        return Result.success();
    }

    @PostMapping("/activity/checkin")
    @Operation(summary = "现场核验")
    public Result<Void> checkinActivity(@RequestAttribute("userId") Long userId,
                                        @RequestParam Long signupId) {
        volunteerService.checkinActivity(userId, signupId);
        return Result.success();
    }

    @GetMapping("/activity/my-signups")
    @Operation(summary = "我的报名记录")
    public Result<List<ActivitySignup>> mySignups(@RequestAttribute("userId") Long userId) {
        List<ActivitySignup> signups = volunteerService.getMySignups(userId);
        return Result.success(signups);
    }

    @GetMapping("/team/list")
    @Operation(summary = "志愿小队列表")
    public Result<List<VolunteerTeam>> teamList() {
        List<VolunteerTeam> teams = volunteerService.getTeamList();
        return Result.success(teams);
    }

    @PostMapping("/team/create")
    @Operation(summary = "创建志愿小队")
    public Result<Void> createTeam(@RequestAttribute("userId") Long userId,
                                   @RequestParam String name,
                                   @RequestParam(required = false) String description) {
        volunteerService.createTeam(userId, name, description);
        return Result.success();
    }

    @PostMapping("/team/apply")
    @Operation(summary = "申请加入小队")
    public Result<Void> applyTeam(@RequestAttribute("userId") Long userId,
                                  @RequestParam Long teamId) {
        volunteerService.applyJoinTeam(userId, teamId);
        return Result.success();
    }

    @GetMapping("/team/my-teams")
    @Operation(summary = "我的小队")
    public Result<List<VolunteerTeam>> myTeams(@RequestAttribute("userId") Long userId) {
        List<VolunteerTeam> teams = volunteerService.getMyTeams(userId);
        return Result.success(teams);
    }
}
