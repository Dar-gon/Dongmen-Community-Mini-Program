package com.community.service.impl;

import com.community.dto.*;
import com.community.entity.User;
import com.community.exception.BusinessException;
import com.community.mapper.UserMapper;
import com.community.service.UserService;
import com.community.util.JwtUtil;
import com.community.util.RedisUtil;
import com.community.common.Constants;
import com.community.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        User user = userMapper.selectByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        if (user.getStatus() != 1) {
            throw new BusinessException("账号已被禁用");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        redisUtil.set(Constants.REDIS_USER_TOKEN + user.getId(), token, 3600, java.util.concurrent.TimeUnit.SECONDS);

        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);

        UserVO userVO = buildUserVO(user);
        loginVO.setUserInfo(userVO);

        return loginVO;
    }

    @Override
    public LoginVO wxLogin(WxLoginDTO wxLoginDTO) {
        // 开发环境模拟微信登录：使用code作为mock openid
        String mockOpenid = "wx_" + wxLoginDTO.getCode();
        User user = userMapper.selectByOpenid(mockOpenid);
        if (user == null) {
            // 创建新用户
            user = new User();
            user.setOpenid(mockOpenid);
            user.setUsername("wx_" + mockOpenid.substring(0, Math.min(8, mockOpenid.length())));
            user.setRealName("微信用户");
            user.setRole("resident");
            user.setStatus(1);
            userMapper.insert(user);
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        redisUtil.set(Constants.REDIS_USER_TOKEN + user.getId(), token, 3600, java.util.concurrent.TimeUnit.SECONDS);

        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);

        UserVO userVO = buildUserVO(user);
        loginVO.setUserInfo(userVO);

        return loginVO;
    }

    @Override
    public UserVO getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return buildUserVO(user);
    }

    @Override
    public void updateUserInfo(Long userId, UserUpdateDTO updateDTO) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        if (updateDTO.getNickname() != null) {
            user.setRealName(updateDTO.getNickname());
        }
        if (updateDTO.getAvatar() != null) {
            user.setAvatar(updateDTO.getAvatar());
        }
        if (updateDTO.getPhone() != null) {
            user.setPhone(updateDTO.getPhone());
        }
        if (updateDTO.getRealName() != null) {
            user.setRealName(updateDTO.getRealName());
        }
        if (updateDTO.getGender() != null) {
            user.setGender(updateDTO.getGender());
        }

        userMapper.updateById(user);
    }

    @Override
    public void registerAsVolunteer(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if ("volunteer".equals(user.getRole()) || "admin".equals(user.getRole())) {
            throw new BusinessException("已是志愿者或管理员");
        }
        user.setRole("volunteer");
        userMapper.updateById(user);
    }

    @Override
    public StatisticsVO getAdminStatistics() {
        StatisticsVO vo = new StatisticsVO();
        vo.setTotalUsers(userMapper.selectCount(null));
        vo.setTotalVolunteers(userMapper.selectCount(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<User>().eq("role", "volunteer")));
        // 其他统计字段根据实际表查询，暂设默认值
        vo.setTotalPetitions(0L);
        vo.setTotalOrders(0L);
        vo.setTotalPoints(0L);
        return vo;
    }

    private UserVO buildUserVO(User user) {
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setUsername(user.getUsername());
        userVO.setRealName(user.getRealName());
        userVO.setAvatar(user.getAvatar());
        userVO.setPhone(user.getPhone());
        userVO.setRole(user.getRole());
        userVO.setPoints(user.getPoints());
        userVO.setGender(user.getGender());
        userVO.setVolunteerHours(user.getVolunteerHours());
        return userVO;
    }
}
