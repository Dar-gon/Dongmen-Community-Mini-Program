package com.community.controller;

import com.community.dto.OrderDTO;
import com.community.service.GroupBuyService;
import com.community.vo.OrderVO;
import com.community.vo.PageResult;
import com.community.vo.ProductVO;
import com.community.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 社区团购控制器
 */
@RestController
@RequestMapping("/api/groupbuy")
@RequiredArgsConstructor
@Tag(name = "社区团购", description = "社区团购相关接口")
public class GroupBuyController {

    private final GroupBuyService groupBuyService;

    @GetMapping("/product/list")
    @Operation(summary = "商品列表")
    public Result<PageResult<ProductVO>> productList(@RequestParam(required = false) String category,
                                                     @RequestParam(defaultValue = "1") Integer current,
                                                     @RequestParam(defaultValue = "10") Integer size) {
        PageResult<ProductVO> pageResult = groupBuyService.getProductList(category, current, size);
        return Result.success(pageResult);
    }

    @GetMapping("/product/{id}")
    @Operation(summary = "商品详情")
    public Result<ProductVO> productDetail(@PathVariable Long id) {
        ProductVO productVO = groupBuyService.getProductDetail(id);
        return Result.success(productVO);
    }

    @PostMapping("/order")
    @Operation(summary = "下单")
    public Result<OrderVO> createOrder(@RequestAttribute("userId") Long userId,
                                       @RequestBody @Valid OrderDTO orderDTO) {
        OrderVO orderVO = groupBuyService.createOrder(userId, orderDTO);
        return Result.success(orderVO);
    }

    @GetMapping("/order/list")
    @Operation(summary = "我的订单")
    public Result<PageResult<OrderVO>> myOrders(@RequestAttribute("userId") Long userId,
                                                @RequestParam(required = false) Integer status,
                                                @RequestParam(defaultValue = "1") Integer current,
                                                @RequestParam(defaultValue = "10") Integer size) {
        PageResult<OrderVO> pageResult = groupBuyService.getMyOrders(userId, status, current, size);
        return Result.success(pageResult);
    }

    @GetMapping("/order/{id}")
    @Operation(summary = "订单详情")
    public Result<OrderVO> orderDetail(@PathVariable Long id,
                                       @RequestAttribute("userId") Long userId) {
        OrderVO orderVO = groupBuyService.getOrderDetail(id, userId);
        return Result.success(orderVO);
    }

    @PostMapping("/order/{id}/cancel")
    @Operation(summary = "取消订单")
    public Result<Void> cancelOrder(@PathVariable Long id,
                                    @RequestAttribute("userId") Long userId) {
        groupBuyService.cancelOrder(id, userId);
        return Result.success();
    }
}
