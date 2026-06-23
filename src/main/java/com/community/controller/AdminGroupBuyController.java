package com.community.controller;

import com.community.annotation.RequireRole;
import com.community.dto.ProductDTO;
import com.community.entity.Product;
import com.community.mapper.ProductMapper;
import com.community.service.GroupBuyService;
import com.community.vo.OrderVO;
import com.community.vo.PageResult;
import com.community.vo.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 管理员团购管理控制器
 */
@RestController
@RequestMapping("/api/admin/groupbuy")
@RequiredArgsConstructor
@Tag(name = "管理员-团购管理", description = "管理员团购管理接口")
@RequireRole({"admin"})
public class AdminGroupBuyController {

    private final GroupBuyService groupBuyService;
    private final ProductMapper productMapper;

    @PostMapping("/product")
    @Operation(summary = "创建商品")
    public Result<Void> createProduct(@RequestBody @Valid ProductDTO productDTO) {
        groupBuyService.createProduct(productDTO);
        return Result.success();
    }

    @GetMapping("/product/list")
    @Operation(summary = "管理员查看全部商品")
    public Result<PageResult<Product>> allProducts(
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<Product> page = new Page<>(current, size);
        QueryWrapper<Product> qw = new QueryWrapper<>();
        if (status != null) qw.eq("status", status);
        qw.orderByDesc("create_time");
        Page<Product> result = productMapper.selectPage(page, qw);
        return Result.success(new PageResult<>(result.getTotal(), result.getRecords(), current, size));
    }

    @PutMapping("/product/{id}")
    @Operation(summary = "编辑商品")
    public Result<Void> updateProduct(@PathVariable Long id,
                                      @RequestBody @Valid ProductDTO productDTO) {
        groupBuyService.updateProduct(id, productDTO);
        return Result.success();
    }

    @PutMapping("/product/{id}/status")
    @Operation(summary = "更新商品状态")
    public Result<Void> updateProductStatus(@PathVariable Long id,
                                            @RequestParam Integer status) {
        groupBuyService.updateProductStatus(id, status);
        return Result.success();
    }

    @GetMapping("/order/list")
    @Operation(summary = "所有订单列表")
    public Result<PageResult<OrderVO>> allOrders(@RequestParam(required = false) Integer status,
                                                  @RequestParam(required = false) String keyword,
                                                  @RequestParam(defaultValue = "1") Integer current,
                                                  @RequestParam(defaultValue = "10") Integer size) {
        PageResult<OrderVO> pageResult = groupBuyService.getAllOrders(status, keyword, current, size);
        return Result.success(pageResult);
    }

    @PostMapping("/order/{id}/complete")
    @Operation(summary = "完成订单")
    public Result<Void> completeOrder(@PathVariable Long id) {
        groupBuyService.completeOrder(id);
        return Result.success();
    }
}
