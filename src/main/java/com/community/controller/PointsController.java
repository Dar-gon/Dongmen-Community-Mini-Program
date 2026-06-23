package com.community.controller;

import com.community.annotation.RequireRole;
import com.community.service.VolunteerService;
import com.community.vo.PageResult;
import com.community.vo.PointsAccountVO;
import com.community.vo.PointsTransactionVO;
import com.community.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 积分控制器
 */
@RestController
@RequestMapping("/api/points")
@RequiredArgsConstructor
@Tag(name = "积分管理", description = "积分相关接口")
@RequireRole({"volunteer", "admin"})
public class PointsController {

    private final VolunteerService volunteerService;

    @GetMapping("/account")
    @Operation(summary = "获取积分账户")
    public Result<PointsAccountVO> account(@RequestAttribute("userId") Long userId) {
        PointsAccountVO accountVO = volunteerService.getPointsAccount(userId);
        return Result.success(accountVO);
    }

    @GetMapping("/history")
    @Operation(summary = "积分流水记录")
    public Result<PageResult<PointsTransactionVO>> history(@RequestAttribute("userId") Long userId,
                                                           @RequestParam(required = false) String type,
                                                           @RequestParam(defaultValue = "1") Integer current,
                                                           @RequestParam(defaultValue = "10") Integer size) {
        PageResult<PointsTransactionVO> pageResult = volunteerService.getPointsHistory(userId, type, current, size);
        return Result.success(pageResult);
    }
}
