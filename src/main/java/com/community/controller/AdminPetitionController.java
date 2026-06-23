package com.community.controller;

import com.community.annotation.RequireRole;
import com.community.entity.PetitionLog;
import com.community.service.PetitionService;
import com.community.vo.PageResult;
import com.community.vo.PetitionVO;
import com.community.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员工单管理控制器
 */
@RestController
@RequestMapping("/api/admin/petition")
@RequiredArgsConstructor
@Tag(name = "管理员-工单管理", description = "管理员工单管理接口")
@RequireRole({"admin"})
public class AdminPetitionController {

    private final PetitionService petitionService;

    @GetMapping("/list")
    @Operation(summary = "所有工单列表")
    public Result<PageResult<PetitionVO>> list(@RequestParam(required = false) Integer status,
                                               @RequestParam(required = false) String keyword,
                                               @RequestParam(defaultValue = "1") Integer current,
                                               @RequestParam(defaultValue = "10") Integer size) {
        PageResult<PetitionVO> pageResult = petitionService.getAllPetitions(status, keyword, current, size);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    @Operation(summary = "工单详情")
    public Result<PetitionVO> detail(@PathVariable Long id) {
        PetitionVO petitionVO = petitionService.getPetitionDetail(id);
        return Result.success(petitionVO);
    }

    @PostMapping("/{id}/assign")
    @Operation(summary = "分派工单")
    public Result<Void> assign(@PathVariable Long id,
                               @RequestAttribute("userId") Long userId,
                               @RequestParam Long handlerId,
                               @RequestParam String handlerName) {
        petitionService.assignPetition(id, userId, handlerId, handlerName);
        return Result.success();
    }

    @PostMapping("/{id}/process")
    @Operation(summary = "开始处理")
    public Result<Void> process(@PathVariable Long id) {
        petitionService.processPetition(id);
        return Result.success();
    }

    @PostMapping("/{id}/resolve")
    @Operation(summary = "办结工单")
    public Result<Void> resolve(@PathVariable Long id,
                                @RequestParam(required = false) String note) {
        petitionService.resolvePetition(id, note);
        return Result.success();
    }

    @PostMapping("/{id}/archive")
    @Operation(summary = "归档工单")
    public Result<Void> archive(@PathVariable Long id) {
        petitionService.archivePetition(id);
        return Result.success();
    }

    @GetMapping("/{id}/logs")
    @Operation(summary = "获取工单日志")
    public Result<List<PetitionLog>> logs(@PathVariable Long id) {
        List<PetitionLog> logs = petitionService.getPetitionLogs(id);
        return Result.success(logs);
    }
}
