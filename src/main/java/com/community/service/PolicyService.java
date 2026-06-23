package com.community.service;

import com.community.dto.*;
import com.community.vo.*;
import com.community.vo.PageResult;

public interface PolicyService {
    PageResult<PolicyVO> getPolicyList(Integer category, Integer current, Integer size);
    PolicyVO getPolicyDetail(Long id);
    void createPolicy(Long userId, PolicyDTO policyDTO);
    void updatePolicy(Long id, PolicyDTO policyDTO);
    void deletePolicy(Long id);
}
