package com.community.service.impl;

import com.community.service.PointsRankService;
import com.community.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PointsRankServiceImpl implements PointsRankService {

    private final RedisUtil redisUtil;

    // Redis Key: 志愿积分年度排行榜
    private static final String RANK_KEY = "volunteer:rank:year:";

    @Override
    public void updateRank(Long userId, Integer points) {
        String key = RANK_KEY + Calendar.getInstance().get(Calendar.YEAR);
        redisUtil.increment(key, userId.toString(), points);
    }

    @Override
    public List<Map<String, Object>> getTopRanks(int top) {
        String key = RANK_KEY + Calendar.getInstance().get(Calendar.YEAR);
        // ZREVRANGE WITHSCORES 获取排名（从高到低）
        Set<Object> members = redisUtil.opsForZSet().reverseRange(key, 0, top - 1);
        if (members == null || members.isEmpty()) return Collections.emptyList();

        List<Map<String, Object>> list = new ArrayList<>();
        int rank = 1;
        for (Object member : members) {
            String userId = member.toString();
            Double score = redisUtil.opsForZSet().score(key, userId);
            Map<String, Object> item = new HashMap<>();
            item.put("rank", rank++);
            item.put("userId", Long.parseLong(userId));
            item.put("points", score != null ? score.intValue() : 0);
            list.add(item);
        }
        return list;
    }

    @Override
    public Long getUserRank(Long userId) {
        String key = RANK_KEY + Calendar.getInstance().get(Calendar.YEAR);
        Long rank = redisUtil.opsForZSet().reverseRank(key, userId.toString());
        return rank != null ? rank + 1 : null; // 排名从0开始，+1转为从1开始
    }

    @Override
    public Integer getUserPoints(Long userId) {
        String key = RANK_KEY + Calendar.getInstance().get(Calendar.YEAR);
        Double score = redisUtil.opsForZSet().score(key, userId.toString());
        return score != null ? score.intValue() : 0;
    }

    @Override
    public Long getRankCount() {
        String key = RANK_KEY + Calendar.getInstance().get(Calendar.YEAR);
        return redisUtil.opsForZSet().zCard(key);
    }
}
