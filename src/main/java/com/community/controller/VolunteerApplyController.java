package com.community.controller;

import com.community.annotation.RequireRole;
import com.community.dto.ReviewDTO;
import com.community.entity.VolunteerApply;
import com.community.service.VolunteerApplyService;
import com.community.vo.PageResult;
import com.community.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "志愿者申请")
public class VolunteerApplyController {

    private final VolunteerApplyService applyService;

    /** 居民提交志愿者申请（需登录） */
    @PostMapping("/api/volunteer/apply")
    @Operation(summary = "提交志愿者申请")
    public Result<Void> submit(@RequestAttribute("userId") Long userId,
                               @RequestBody VolunteerApply apply) {
        applyService.submitApply(userId, apply);
        return Result.success();
    }

    /** 我的申请记录（需登录） */
    @GetMapping("/api/volunteer/apply/my")
    @Operation(summary = "我的申请记录")
    public Result<?> myApplies(@RequestAttribute("userId") Long userId) {
        return Result.success(applyService.getMyApplies(userId));
    }

    /** 管理员查看所有申请（仅管理员） */
    @GetMapping("/api/admin/volunteer/apply/list")
    @RequireRole({"admin"})
    @Operation(summary = "管理员查看所有申请")
    public Result<PageResult<VolunteerApply>> allApplies(
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(applyService.getAllApplies(status, current, size));
    }

    /** 管理员审核申请（仅管理员） */
    @PostMapping("/api/admin/volunteer/apply/{id}/review")
    @RequireRole({"admin"})
    @Operation(summary = "审核志愿者申请")
    public Result<Void> review(@PathVariable Long id,
                               @RequestAttribute("userId") Long userId,
                               @RequestBody ReviewDTO dto) {
        applyService.reviewApply(id, dto, userId);
        return Result.success();
    }
}
