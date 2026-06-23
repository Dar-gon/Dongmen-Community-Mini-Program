package com.community.controller;

import com.community.annotation.RequireRole;
import com.community.entity.ExchangeGoods;
import com.community.service.ExchangeService;
import com.community.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员兑换管理控制器
 */
@RestController
@RequestMapping("/api/admin/exchange")
@RequiredArgsConstructor
@Tag(name = "管理员-兑换管理", description = "管理员兑换管理接口")
@RequireRole({"admin"})
public class AdminExchangeController {

    private final ExchangeService exchangeService;

    @PostMapping("/goods")
    @Operation(summary = "创建兑换商品")
    public Result<Void> createGoods(@RequestBody ExchangeGoods exchangeGoods) {
        exchangeService.createGoods(exchangeGoods);
        return Result.success();
    }

    @PutMapping("/goods/{id}")
    @Operation(summary = "编辑兑换商品")
    public Result<Void> updateGoods(@PathVariable Long id,
                                    @RequestBody ExchangeGoods exchangeGoods) {
        exchangeService.updateGoods(id, exchangeGoods);
        return Result.success();
    }

    @PutMapping("/goods/{id}/status")
    @Operation(summary = "更新商品状态")
    public Result<Void> updateGoodsStatus(@PathVariable Long id,
                                          @RequestParam Integer status) {
        exchangeService.updateGoodsStatus(id, status);
        return Result.success();
    }
}
