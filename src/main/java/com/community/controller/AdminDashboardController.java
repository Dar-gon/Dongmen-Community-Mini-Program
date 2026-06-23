package com.community.controller;

import com.community.service.UserService;
import com.community.vo.Result;
import com.community.vo.StatisticsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
@Tag(name = "管理后台统计")
public class AdminDashboardController {
    private final UserService userService;

    @GetMapping("/statistics")
    @Operation(summary = "管理后台统计数据")
    public Result<StatisticsVO> statistics() {
        return Result.success(userService.getAdminStatistics());
    }
}
