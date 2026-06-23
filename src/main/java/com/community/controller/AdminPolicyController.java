package com.community.controller;

import com.community.annotation.RequireRole;
import com.community.dto.PolicyDTO;
import com.community.entity.Policy;
import com.community.mapper.PolicyMapper;
import com.community.service.PolicyService;
import com.community.service.UserService;
import com.community.vo.PageResult;
import com.community.vo.PolicyVO;
import com.community.vo.Result;
import com.community.vo.UserVO;
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
 * 管理员政策管理控制器
 */
@RestController
@RequestMapping("/api/admin/policy")
@RequiredArgsConstructor
@Tag(name = "管理员-政策管理", description = "管理员政策管理接口")
@RequireRole({"admin"})
public class AdminPolicyController {

    private final PolicyService policyService;
    private final UserService userService;
    private final PolicyMapper policyMapper;

    @PostMapping
    @Operation(summary = "发布政策")
    public Result<Void> create(@RequestAttribute("userId") Long userId,
                               @RequestBody @Valid PolicyDTO policyDTO) {
        if (policyDTO.getPublisherName() == null || policyDTO.getPublisherName().isEmpty()) {
            try {
                UserVO userVO = userService.getUserInfo(userId);
                if (userVO != null && userVO.getRealName() != null) {
                    policyDTO.setPublisherName(userVO.getRealName());
                }
            } catch (Exception e) {}
        }
        policyService.createPolicy(userId, policyDTO);
        return Result.success();
    }

    @PutMapping("/{id}")
    @Operation(summary = "编辑政策")
    public Result<Void> update(@PathVariable Long id,
                               @RequestBody @Valid PolicyDTO policyDTO) {
        policyService.updatePolicy(id, policyDTO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除政策")
    public Result<Void> delete(@PathVariable Long id) {
        policyService.deletePolicy(id);
        return Result.success();
    }

    @GetMapping("/list")
    @Operation(summary = "管理员查看全部政策（含未发布）")
    public Result<PageResult<PolicyVO>> allPolicies(
            @RequestParam(required = false) Integer category,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<Policy> page = new Page<>(current, size);
        QueryWrapper<Policy> qw = new QueryWrapper<>();
        if (category != null) qw.eq("category", category);
        if (status != null) qw.eq("status", status);
        qw.orderByDesc("top").orderByDesc("create_time");
        Page<Policy> result = policyMapper.selectPage(page, qw);
        List<PolicyVO> voList = result.getRecords().stream().map(p -> {
            PolicyVO vo = new PolicyVO();
            vo.setId(p.getId());
            vo.setTitle(p.getTitle());
            vo.setContent(p.getContent());
            vo.setSummary(p.getSummary());
            vo.setCoverImg(p.getCoverImg());
            vo.setCategory(p.getCategory());
            vo.setCategoryName(p.getCategoryName());
            vo.setTop(p.getTop());
            vo.setViewCount(p.getViewCount());
            vo.setPublisherId(p.getPublisherId());
            vo.setPublisherName(p.getPublisherName());
            vo.setPublishTime(p.getPublishTime());
            vo.setCreateTime(p.getCreateTime());
            return vo;
        }).collect(Collectors.toList());
        return Result.success(new PageResult<>(result.getTotal(), voList, current, size));
    }

    @PutMapping("/{id}/top")
    @Operation(summary = "置顶/取消置顶")
    public Result<Void> toggleTop(@PathVariable Long id) {
        Policy policy = policyMapper.selectById(id);
        if (policy == null) return Result.fail("政策不存在");
        policy.setTop(policy.getTop() == 1 ? 0 : 1);
        policyMapper.updateById(policy);
        return Result.success();
    }
}
