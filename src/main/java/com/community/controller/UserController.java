package com.community.controller;

import com.community.dto.UserUpdateDTO;
import com.community.service.UserService;
import com.community.vo.Result;
import com.community.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户信息相关接口")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    @Operation(summary = "获取用户信息")
    public Result<UserVO> getProfile(@RequestAttribute("userId") Long userId) {
        UserVO userVO = userService.getUserInfo(userId);
        return Result.success(userVO);
    }

    @PutMapping("/profile")
    @Operation(summary = "更新用户信息")
    public Result<Void> updateProfile(@RequestAttribute("userId") Long userId,
                                      @RequestBody UserUpdateDTO updateDTO) {
        userService.updateUserInfo(userId, updateDTO);
        return Result.success();
    }

    @PostMapping("/volunteer/register")
    @Operation(summary = "注册志愿者")
    public Result<Void> registerVolunteer(@RequestAttribute("userId") Long userId) {
        userService.registerAsVolunteer(userId);
        return Result.success();
    }
}
