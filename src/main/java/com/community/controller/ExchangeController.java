package com.community.controller;

import com.community.entity.ExchangeRecord;
import com.community.service.ExchangeService;
import com.community.vo.ExchangeGoodsVO;
import com.community.vo.PageResult;
import com.community.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 积分兑换控制器
 */
@RestController
@RequestMapping("/api/exchange")
@RequiredArgsConstructor
@Tag(name = "积分兑换", description = "积分兑换相关接口")
public class ExchangeController {

    private final ExchangeService exchangeService;

    @GetMapping("/goods/list")
    @Operation(summary = "兑换商品列表")
    public Result<PageResult<ExchangeGoodsVO>> goodsList(@RequestParam(defaultValue = "1") Integer current,
                                                         @RequestParam(defaultValue = "10") Integer size) {
        PageResult<ExchangeGoodsVO> pageResult = exchangeService.getGoodsList(current, size);
        return Result.success(pageResult);
    }

    @GetMapping("/goods/{id}")
    @Operation(summary = "兑换商品详情")
    public Result<ExchangeGoodsVO> goodsDetail(@PathVariable Long id) {
        ExchangeGoodsVO goodsVO = exchangeService.getGoodsDetail(id);
        return Result.success(goodsVO);
    }

    @PostMapping("/exchange")
    @Operation(summary = "兑换商品")
    public Result<Void> exchange(@RequestAttribute("userId") Long userId,
                                 @RequestParam Long goodsId) {
        exchangeService.exchange(userId, goodsId);
        return Result.success();
    }

    @GetMapping("/record/list")
    @Operation(summary = "我的兑换记录")
    public Result<PageResult<ExchangeRecord>> myRecords(@RequestAttribute("userId") Long userId,
                                                        @RequestParam(defaultValue = "1") Integer current,
                                                        @RequestParam(defaultValue = "10") Integer size) {
        PageResult<ExchangeRecord> pageResult = exchangeService.getMyRecords(userId, current, size);
        return Result.success(pageResult);
    }
}
