package com.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.community.dto.PolicyDTO;
import com.community.entity.Policy;
import com.community.exception.BusinessException;
import com.community.mapper.PolicyMapper;
import com.community.service.PolicyService;
import com.community.vo.PageResult;
import com.community.vo.PolicyVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PolicyServiceImpl implements PolicyService {

    private final PolicyMapper policyMapper;

    @Override
    public PageResult<PolicyVO> getPolicyList(Integer category, Integer current, Integer size) {
        int offset = (current - 1) * size;
        List<Policy> policies = policyMapper.selectListByCategory(category, offset, size);

        QueryWrapper<Policy> countWrapper = new QueryWrapper<>();
        countWrapper.eq("deleted", 0);
        if (category != null) {
            countWrapper.eq("category", category);
        }
        long total = policyMapper.selectCount(countWrapper);

        List<PolicyVO> voList = policies.stream().map(policy -> {
            PolicyVO vo = new PolicyVO();
            vo.setId(policy.getId());
            vo.setTitle(policy.getTitle());
            vo.setContent(policy.getContent());
            vo.setSummary(policy.getSummary());
            vo.setCoverImg(policy.getCoverImg());
            vo.setCategory(policy.getCategory());
            vo.setCategoryName(getCategoryName(policy.getCategory()));
            vo.setTop(policy.getTop());
            vo.setViewCount(policy.getViewCount());
            vo.setStatus(policy.getStatus());
            vo.setPublishTime(policy.getPublishTime());
            vo.setPublisherId(policy.getPublisherId());
            vo.setPublisherName(policy.getPublisherName());
            vo.setCreateTime(policy.getCreateTime());
            vo.setUpdateTime(policy.getUpdateTime());
            return vo;
        }).collect(Collectors.toList());

        return new PageResult<>(total, voList, current, size);
    }

    @Override
    public PolicyVO getPolicyDetail(Long id) {
        Policy policy = policyMapper.selectById(id);
        if (policy == null) {
            throw new BusinessException("政策公告不存在");
        }

        // 自增浏览次数
        policyMapper.incrementViewCount(id);

        PolicyVO vo = new PolicyVO();
        vo.setId(policy.getId());
        vo.setTitle(policy.getTitle());
        vo.setContent(policy.getContent());
        vo.setSummary(policy.getSummary());
        vo.setCoverImg(policy.getCoverImg());
        vo.setCategory(policy.getCategory());
        vo.setCategoryName(getCategoryName(policy.getCategory()));
        vo.setTop(policy.getTop());
        vo.setViewCount(policy.getViewCount() + 1);
        vo.setStatus(policy.getStatus());
        vo.setPublishTime(policy.getPublishTime());
        vo.setPublisherId(policy.getPublisherId());
        vo.setPublisherName(policy.getPublisherName());
        vo.setCreateTime(policy.getCreateTime());
        vo.setUpdateTime(policy.getUpdateTime());
        return vo;
    }

    @Override
    @Transactional
    public void createPolicy(Long userId, PolicyDTO policyDTO) {
        Policy policy = new Policy();
        policy.setTitle(policyDTO.getTitle());
        policy.setContent(policyDTO.getContent());
        policy.setSummary(policyDTO.getSummary());
        policy.setCoverImg(policyDTO.getCoverImg());
        policy.setCategory(policyDTO.getCategory());
        policy.setTop(policyDTO.getTop());
        policy.setPublisherId(userId);
        policy.setPublisherName(policyDTO.getPublisherName());
        policy.setViewCount(0);
        policy.setStatus(1);
        policyMapper.insert(policy);
    }

    @Override
    @Transactional
    public void updatePolicy(Long id, PolicyDTO policyDTO) {
        Policy policy = policyMapper.selectById(id);
        if (policy == null) {
            throw new BusinessException("政策公告不存在");
        }

        policy.setTitle(policyDTO.getTitle());
        policy.setContent(policyDTO.getContent());
        policy.setSummary(policyDTO.getSummary());
        policy.setCoverImg(policyDTO.getCoverImg());
        policy.setCategory(policyDTO.getCategory());
        policy.setTop(policyDTO.getTop());
        policyMapper.updateById(policy);
    }

    @Override
    @Transactional
    public void deletePolicy(Long id) {
        Policy policy = policyMapper.selectById(id);
        if (policy == null) {
            throw new BusinessException("政策公告不存在");
        }
        policyMapper.deleteById(id);
    }

    private String getCategoryName(Integer category) {
        if (category == null) return "未分类";
        switch (category) {
            case 1: return "政策解读";
            case 2: return "社区公告";
            case 3: return "便民信息";
            default: return "其他";
        }
    }
}
