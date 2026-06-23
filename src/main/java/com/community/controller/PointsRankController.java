package com.community.controller;

import com.community.service.PointsRankService;
import com.community.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/points/rank")
@RequiredArgsConstructor
@Tag(name = "积分排行榜")
public class PointsRankController {
    private final PointsRankService pointsRankService;

    @GetMapping("/top")
    @Operation(summary = "排行榜Top N")
    public Result<List<Map<String, Object>>> topRanks(
            @RequestParam(defaultValue = "10") int top) {
        return Result.success(pointsRankService.getTopRanks(top));
    }

    @GetMapping("/my")
    @Operation(summary = "我的排名")
    public Result<Map<String, Object>> myRank(
            @RequestAttribute(value = "userId", required = false) Long userId) {
        if (userId == null) return Result.success(null);
        Long rank = pointsRankService.getUserRank(userId);
        Integer points = pointsRankService.getUserPoints(userId);
        Map<String, Object> info = new java.util.HashMap<>();
        info.put("rank", rank);
        info.put("points", points);
        return Result.success(info);
    }

    @GetMapping("/count")
    @Operation(summary = "排行榜总人数")
    public Result<Long> rankCount() {
        return Result.success(pointsRankService.getRankCount());
    }
}
