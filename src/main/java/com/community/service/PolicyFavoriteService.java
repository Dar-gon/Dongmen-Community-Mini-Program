package com.community.service;

import com.community.vo.PageResult;
import com.community.vo.PolicyVO;

public interface PolicyFavoriteService {
    boolean toggleFavorite(Long userId, Long policyId);
    boolean isFavorited(Long userId, Long policyId);
    PageResult<PolicyVO> getMyFavorites(Long userId, Integer current, Integer size);
}
