package com.community.controller;

import com.community.service.PolicyService;
import com.community.vo.PageResult;
import com.community.vo.PolicyVO;
import com.community.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 政策公告控制器
 */
@RestController
@RequestMapping("/api/policy")
@RequiredArgsConstructor
@Tag(name = "政务公开", description = "政策公告相关接口")
public class PolicyController {

    private final PolicyService policyService;

    @GetMapping("/list")
    @Operation(summary = "获取政策列表")
    public Result<PageResult<PolicyVO>> list(@RequestParam(required = false) Integer category,
                                              @RequestParam(defaultValue = "1") Integer current,
                                              @RequestParam(defaultValue = "10") Integer size) {
        PageResult<PolicyVO> pageResult = policyService.getPolicyList(category, current, size);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取政策详情")
    public Result<PolicyVO> detail(@PathVariable Long id) {
        PolicyVO policyVO = policyService.getPolicyDetail(id);
        return Result.success(policyVO);
    }
}
