package com.community.controller;

import com.community.dto.LoginDTO;
import com.community.dto.WxLoginDTO;
import com.community.service.UserService;
import com.community.vo.LoginVO;
import com.community.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "认证管理", description = "登录注册相关接口")
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<LoginVO> login(@RequestBody @Valid LoginDTO loginDTO) {
        LoginVO loginVO = userService.login(loginDTO);
        return Result.success(loginVO);
    }

    @PostMapping("/wx-login")
    @Operation(summary = "微信登录（开发环境模拟）")
    public Result<LoginVO> wxLogin(@RequestBody @Valid WxLoginDTO wxLoginDTO) {
        LoginVO loginVO = userService.wxLogin(wxLoginDTO);
        return Result.success(loginVO);
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public Result<Void> register(@RequestBody @Valid LoginDTO loginDTO) {
        userService.login(loginDTO);
        return Result.success();
    }
}
