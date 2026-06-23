package com.community.service;

import com.community.dto.*;
import com.community.vo.*;

public interface UserService {
    LoginVO login(LoginDTO loginDTO);
    LoginVO wxLogin(WxLoginDTO wxLoginDTO);
    UserVO getUserInfo(Long userId);
    void updateUserInfo(Long userId, UserUpdateDTO updateDTO);
    void registerAsVolunteer(Long userId);
    StatisticsVO getAdminStatistics();
}
