package com.community.service;

import com.community.dto.ReviewDTO;
import com.community.entity.VolunteerApply;
import com.community.vo.PageResult;
import java.util.List;

public interface VolunteerApplyService {
    void submitApply(Long userId, VolunteerApply apply);
    List<VolunteerApply> getMyApplies(Long userId);
    PageResult<VolunteerApply> getAllApplies(Integer status, Integer current, Integer size);
    void reviewApply(Long id, ReviewDTO dto, Long reviewerId);
}
