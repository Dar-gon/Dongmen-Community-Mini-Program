package com.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.community.dto.ReviewDTO;
import com.community.entity.User;
import com.community.entity.VolunteerApply;
import com.community.exception.BusinessException;
import com.community.mapper.UserMapper;
import com.community.mapper.VolunteerApplyMapper;
import com.community.service.VolunteerApplyService;
import com.community.vo.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VolunteerApplyServiceImpl implements VolunteerApplyService {

    private final VolunteerApplyMapper applyMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public void submitApply(Long userId, VolunteerApply apply) {
        // 检查是否已有待审核申请
        QueryWrapper<VolunteerApply> qw = new QueryWrapper<>();
        qw.eq("user_id", userId).eq("status", 0);
        if (applyMapper.selectCount(qw) > 0) {
            throw new BusinessException("您已有一条待审核的申请，请耐心等待");
        }
        apply.setUserId(userId);
        apply.setStatus(0);
        applyMapper.insert(apply);
    }

    @Override
    public List<VolunteerApply> getMyApplies(Long userId) {
        QueryWrapper<VolunteerApply> qw = new QueryWrapper<>();
        qw.eq("user_id", userId).orderByDesc("create_time");
        return applyMapper.selectList(qw);
    }

    @Override
    public PageResult<VolunteerApply> getAllApplies(Integer status, Integer current, Integer size) {
        Page<VolunteerApply> page = new Page<>(current, size);
        QueryWrapper<VolunteerApply> qw = new QueryWrapper<>();
        if (status != null && status >= 0) qw.eq("status", status);
        qw.orderByDesc("create_time");
        Page<VolunteerApply> result = applyMapper.selectPage(page, qw);
        return new PageResult<>(result.getTotal(), result.getRecords(), current, size);
    }

    @Override
    @Transactional
    public void reviewApply(Long id, ReviewDTO dto, Long reviewerId) {
        VolunteerApply apply = applyMapper.selectById(id);
        if (apply == null) throw new BusinessException("申请记录不存在");
        if (apply.getStatus() != 0) throw new BusinessException("该申请已处理");

        apply.setStatus(dto.getStatus());
        apply.setReviewerId(reviewerId);
        apply.setReviewRemark(dto.getRemark());
        apply.setReviewTime(LocalDateTime.now());
        applyMapper.updateById(apply);

        // 审核通过：更新用户角色为volunteer
        if (dto.getStatus() == 1) {
            User user = userMapper.selectById(apply.getUserId());
            if (user != null) {
                user.setRole("volunteer");
                userMapper.updateById(user);
            }
        }
    }
}
