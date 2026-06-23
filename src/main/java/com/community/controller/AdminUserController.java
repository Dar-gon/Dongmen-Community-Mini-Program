package com.community.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.community.annotation.RequireRole;
import com.community.entity.User;
import com.community.mapper.UserMapper;
import com.community.vo.PageResult;
import com.community.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

@RestController
@RequestMapping("/api/admin/user")
@RequiredArgsConstructor
@Tag(name = "管理人员管理")
@RequireRole({"admin"})
public class AdminUserController {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/list")
    @Operation(summary = "管理人员列表")
    public Result<PageResult<User>> list(
            @RequestParam(required = false) String role,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<User> page = new Page<>(current, size);
        QueryWrapper<User> qw = new QueryWrapper<>();
        if (role != null && !role.isEmpty()) qw.eq("role", role);
        qw.orderByDesc("create_time");
        Page<User> result = userMapper.selectPage(page, qw);
        return Result.success(new PageResult<>(result.getTotal(), result.getRecords(), current, size));
    }

    @PostMapping
    @Operation(summary = "添加人员")
    public Result<Void> add(@RequestBody User user) {
        if (user.getUsername() == null || user.getPassword() == null) {
            return Result.fail("用户名和密码不能为空");
        }
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("username", user.getUsername());
        if (userMapper.selectCount(qw) > 0) {
            return Result.fail("用户名已存在");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) user.setRole("resident");
        user.setStatus(1);
        userMapper.insert(user);
        return Result.success();
    }

    @PutMapping("/{id}")
    @Operation(summary = "编辑人员")
    public Result<Void> update(@PathVariable Long id, @RequestBody User user) {
        User existing = userMapper.selectById(id);
        if (existing == null) return Result.fail("用户不存在");
        if (user.getRealName() != null) existing.setRealName(user.getRealName());
        if (user.getPhone() != null) existing.setPhone(user.getPhone());
        if (user.getRole() != null) existing.setRole(user.getRole());
        userMapper.updateById(existing);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除人员")
    public Result<Void> delete(@PathVariable Long id) {
        userMapper.deleteById(id);
        return Result.success();
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "启用/禁用")
    public Result<Void> toggleStatus(@PathVariable Long id, @RequestParam Integer status) {
        User user = userMapper.selectById(id);
        if (user == null) return Result.fail("用户不存在");
        user.setStatus(status);
        userMapper.updateById(user);
        return Result.success();
    }

    @PutMapping("/{id}/password")
    @Operation(summary = "重置密码")
    public Result<Void> resetPassword(@PathVariable Long id) {
        User user = userMapper.selectById(id);
        if (user == null) return Result.fail("用户不存在");
        user.setPassword(passwordEncoder.encode("123456"));
        userMapper.updateById(user);
        return Result.success();
    }
}
