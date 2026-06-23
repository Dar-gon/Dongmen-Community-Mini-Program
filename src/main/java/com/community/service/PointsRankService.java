package com.community.service;

import java.util.List;
import java.util.Map;

public interface PointsRankService {
    // 更新用户积分到排行榜
    void updateRank(Long userId, Integer points);

    // 获取年度积分排行榜 Top N
    List<Map<String, Object>> getTopRanks(int top);

    // 获取用户排名
    Long getUserRank(Long userId);

    // 获取用户积分
    Integer getUserPoints(Long userId);

    // 获取排行榜总数
    Long getRankCount();
}
