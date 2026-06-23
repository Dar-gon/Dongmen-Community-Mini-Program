package com.community.controller;

import com.community.service.PolicyFavoriteService;
import com.community.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "政策收藏")
public class PolicyFavoriteController {

    private final PolicyFavoriteService favoriteService;

    /** 收藏/取消收藏切换 */
    @PostMapping("/api/policy/{id}/favorite")
    @Operation(summary = "收藏/取消收藏")
    public Result<Boolean> toggle(@RequestAttribute("userId") Long userId,
                                  @PathVariable("id") Long policyId) {
        return Result.success(favoriteService.toggleFavorite(userId, policyId));
    }

    /** 查看是否已收藏 */
    @GetMapping("/api/policy/{id}/isfavorite")
    @Operation(summary = "是否已收藏")
    public Result<Boolean> isFavorited(@RequestAttribute(value = "userId", required = false) Long userId,
                                       @PathVariable("id") Long policyId) {
        if (userId == null) {
            return Result.success(false);
        }
        return Result.success(favoriteService.isFavorited(userId, policyId));
    }

    /** 我的收藏列表 */
    @GetMapping("/api/policy/favorite/list")
    @Operation(summary = "我的收藏列表")
    public Result<?> myFavorites(@RequestAttribute("userId") Long userId,
                                 @RequestParam(defaultValue = "1") Integer current,
                                 @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(favoriteService.getMyFavorites(userId, current, size));
    }
}
