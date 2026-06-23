package com.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.community.entity.Policy;
import com.community.entity.PolicyFavorite;
import com.community.mapper.PolicyFavoriteMapper;
import com.community.mapper.PolicyMapper;
import com.community.service.PolicyFavoriteService;
import com.community.vo.PageResult;
import com.community.vo.PolicyVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PolicyFavoriteServiceImpl implements PolicyFavoriteService {

    private final PolicyFavoriteMapper favoriteMapper;
    private final PolicyMapper policyMapper;

    @Override
    @Transactional
    public boolean toggleFavorite(Long userId, Long policyId) {
        int count = favoriteMapper.countByUserAndPolicy(userId, policyId);
        if (count > 0) {
            // 已收藏 -> 取消
            QueryWrapper<PolicyFavorite> qw = new QueryWrapper<>();
            qw.eq("user_id", userId).eq("policy_id", policyId);
            favoriteMapper.delete(qw);
            return false;
        } else {
            // 未收藏 -> 收藏
            PolicyFavorite fav = new PolicyFavorite();
            fav.setUserId(userId);
            fav.setPolicyId(policyId);
            favoriteMapper.insert(fav);
            return true;
        }
    }

    @Override
    public boolean isFavorited(Long userId, Long policyId) {
        return favoriteMapper.countByUserAndPolicy(userId, policyId) > 0;
    }

    @Override
    public PageResult<PolicyVO> getMyFavorites(Long userId, Integer current, Integer size) {
        // 查询收藏的政策ID列表
        Page<PolicyFavorite> favPage = new Page<>(current, size);
        QueryWrapper<PolicyFavorite> favQw = new QueryWrapper<>();
        favQw.eq("user_id", userId).orderByDesc("create_time");
        Page<PolicyFavorite> favResult = favoriteMapper.selectPage(favPage, favQw);

        List<Long> policyIds = favResult.getRecords().stream()
                .map(PolicyFavorite::getPolicyId).collect(Collectors.toList());

        if (policyIds.isEmpty()) {
            return new PageResult<>(0L, List.of(), current, size);
        }

        // 查询对应政策
        QueryWrapper<Policy> pQw = new QueryWrapper<>();
        pQw.in("id", policyIds).eq("status", 1);
        List<Policy> policies = policyMapper.selectList(pQw);

        List<PolicyVO> voList = policies.stream().map(p -> {
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
            vo.setPublisherName(p.getPublisherName());
            vo.setPublisherId(p.getPublisherId());
            vo.setPublishTime(p.getPublishTime());
            return vo;
        }).collect(Collectors.toList());

        return new PageResult<>(favResult.getTotal(), voList, current, size);
    }
}
