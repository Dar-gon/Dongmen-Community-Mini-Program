package com.community.controller;

import com.community.dto.PetitionDTO;
import com.community.service.PetitionService;
import com.community.vo.PageResult;
import com.community.vo.PetitionVO;
import com.community.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * 民情工单控制器
 */
@RestController
@RequestMapping("/api/petition")
@RequiredArgsConstructor
@Tag(name = "民情民意", description = "民情工单相关接口")
public class PetitionController {

    private final PetitionService petitionService;

    @PostMapping
    @Operation(summary = "提交工单")
    public Result<Void> submit(@RequestAttribute("userId") Long userId,
                               @RequestBody @Valid PetitionDTO petitionDTO) {
        petitionService.submitPetition(userId, petitionDTO);
        return Result.success();
    }

    @GetMapping("/list")
    @Operation(summary = "我的工单列表")
    public Result<PageResult<PetitionVO>> list(@RequestAttribute("userId") Long userId,
                                               @RequestParam(required = false) Integer status,
                                               @RequestParam(defaultValue = "1") Integer current,
                                               @RequestParam(defaultValue = "10") Integer size) {
        PageResult<PetitionVO> pageResult = petitionService.getMyPetitions(userId, status, current, size);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    @Operation(summary = "工单详情")
    public Result<PetitionVO> detail(@PathVariable Long id) {
        PetitionVO petitionVO = petitionService.getPetitionDetail(id);
        return Result.success(petitionVO);
    }

    @PostMapping("/{id}/rate")
    @Operation(summary = "工单满意度评价")
    public Result<Void> ratePetition(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Integer rating = (Integer) body.get("rating");
        String content = (String) body.get("content");
        petitionService.ratePetition(id, rating, content);
        return Result.success();
    }
}
