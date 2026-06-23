package com.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.community.dto.PetitionDTO;
import com.community.entity.Petition;
import com.community.entity.PetitionLog;
import com.community.exception.BusinessException;
import com.community.mapper.PetitionLogMapper;
import com.community.mapper.PetitionMapper;
import com.community.service.PetitionService;
import com.community.vo.PageResult;
import com.community.vo.PetitionVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PetitionServiceImpl implements PetitionService {

    private final PetitionMapper petitionMapper;
    private final PetitionLogMapper petitionLogMapper;

    @Override
    @Transactional
    public void submitPetition(Long userId, PetitionDTO petitionDTO) {
        Petition petition = new Petition();
        petition.setUserId(userId);
        petition.setTitle(petitionDTO.getTitle());
        petition.setContent(petitionDTO.getContent());
        petition.setCategory(petitionDTO.getCategory());
        petition.setImages(petitionDTO.getImages());
        petition.setAddress(petitionDTO.getAddress());
        petition.setLongitude(petitionDTO.getLongitude());
        petition.setLatitude(petitionDTO.getLatitude());
        petition.setContactPhone(petitionDTO.getContactPhone());
        petition.setIsAnonymous(petitionDTO.getIsAnonymous());
        petition.setStatus(0); // 已提交
        petition.setOrderNo(generateOrderNo());

        petitionMapper.insert(petition);
        addLog(petition.getId(), userId, "submit", "提交工单");
    }

    @Override
    public PageResult<PetitionVO> getMyPetitions(Long userId, Integer status, Integer current, Integer size) {
        QueryWrapper<Petition> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("deleted", 0);
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        queryWrapper.orderByDesc("create_time");

        long total = petitionMapper.selectCount(queryWrapper);

        queryWrapper.last("LIMIT " + (current - 1) * size + ", " + size);
        List<Petition> petitions = petitionMapper.selectList(queryWrapper);

        List<PetitionVO> voList = petitions.stream().map(this::convertToVO).collect(Collectors.toList());
        return new PageResult<>(total, voList, current, size);
    }

    @Override
    public PetitionVO getPetitionDetail(Long id) {
        Petition petition = petitionMapper.selectById(id);
        if (petition == null) {
            throw new BusinessException("工单不存在");
        }
        return convertToVO(petition);
    }

    @Override
    @Transactional
    public void assignPetition(Long id, Long assignerId, Long handlerId, String handlerName) {
        Petition petition = petitionMapper.selectById(id);
        if (petition == null) {
            throw new BusinessException("工单不存在");
        }
        if (petition.getStatus() != 0 && petition.getStatus() != 1) {
            throw new BusinessException("当前状态不允许分派");
        }

        petition.setHandlerId(handlerId);
        petition.setHandlerName(handlerName);
        petition.setStatus(1); // 已分派
        petitionMapper.updateById(petition);

        addLog(id, assignerId, "assign", "分派给 " + handlerName);
    }

    @Override
    @Transactional
    public void processPetition(Long id) {
        Petition petition = petitionMapper.selectById(id);
        if (petition == null) {
            throw new BusinessException("工单不存在");
        }
        if (petition.getStatus() != 1) {
            throw new BusinessException("当前状态不允许处理");
        }

        petition.setStatus(2); // 处理中
        petitionMapper.updateById(petition);

        addLog(id, petition.getHandlerId(), "process", "开始处理");
    }

    @Override
    @Transactional
    public void resolvePetition(Long id, String note) {
        Petition petition = petitionMapper.selectById(id);
        if (petition == null) {
            throw new BusinessException("工单不存在");
        }
        if (petition.getStatus() != 2) {
            throw new BusinessException("当前状态不允许办结");
        }

        petition.setStatus(3); // 已办结
        petition.setResolvedNote(note);
        petition.setResolvedTime(LocalDateTime.now());
        petitionMapper.updateById(petition);

        addLog(id, petition.getHandlerId(), "resolve", "办结: " + note);
    }

    @Override
    @Transactional
    public void archivePetition(Long id) {
        Petition petition = petitionMapper.selectById(id);
        if (petition == null) {
            throw new BusinessException("工单不存在");
        }
        if (petition.getStatus() != 3) {
            throw new BusinessException("只有已办结的工单才能归档");
        }

        petition.setStatus(4); // 已归档
        petitionMapper.updateById(petition);
    }

    @Override
    public PageResult<PetitionVO> getAllPetitions(Integer status, String keyword, Integer current, Integer size) {
        QueryWrapper<Petition> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", 0);
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.and(w -> w.like("title", keyword).or().like("order_no", keyword));
        }
        queryWrapper.orderByDesc("create_time");

        long total = petitionMapper.selectCount(queryWrapper);

        queryWrapper.last("LIMIT " + (current - 1) * size + ", " + size);
        List<Petition> petitions = petitionMapper.selectList(queryWrapper);

        List<PetitionVO> voList = petitions.stream().map(this::convertToVO).collect(Collectors.toList());
        return new PageResult<>(total, voList, current, size);
    }

    @Override
    public List<PetitionLog> getPetitionLogs(Long petitionId) {
        return petitionLogMapper.selectByPetitionId(petitionId);
    }

    @Override
    @Transactional
    public void ratePetition(Long id, Integer rating, String content) {
        Petition petition = petitionMapper.selectById(id);
        if (petition == null) throw new BusinessException("工单不存在");
        petition.setRating(rating);
        petition.setRatingContent(content);
        petitionMapper.updateById(petition);
    }

    private String generateOrderNo() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "GZ" + dateStr + String.format("%04d", (int) (Math.random() * 10000));
    }

    private void addLog(Long petitionId, Long userId, String action, String content) {
        PetitionLog log = new PetitionLog();
        log.setPetitionId(petitionId);
        log.setOperatorId(userId);
        log.setAction(action);
        log.setContent(content);
        petitionLogMapper.insert(log);
    }

    private PetitionVO convertToVO(Petition petition) {
        PetitionVO vo = new PetitionVO();
        vo.setId(petition.getId());
        vo.setOrderNo(petition.getOrderNo());
        vo.setUserId(petition.getUserId());
        vo.setUserName(petition.getUserName());
        vo.setTitle(petition.getTitle());
        vo.setContent(petition.getContent());
        vo.setCategory(petition.getCategory());
        vo.setImages(petition.getImages());
        vo.setAddress(petition.getAddress());
        vo.setLongitude(petition.getLongitude());
        vo.setLatitude(petition.getLatitude());
        vo.setContactPhone(petition.getContactPhone());
        vo.setIsAnonymous(petition.getIsAnonymous());
        vo.setStatus(petition.getStatus());
        vo.setAssignerId(petition.getAssignerId());
        vo.setAssignerName(petition.getAssignerName());
        vo.setHandlerId(petition.getHandlerId());
        vo.setHandlerName(petition.getHandlerName());
        vo.setResolvedTime(petition.getResolvedTime());
        vo.setResolvedNote(petition.getResolvedNote());
        vo.setRating(petition.getRating());
        vo.setRatingContent(petition.getRatingContent());
        vo.setCreateTime(petition.getCreateTime());
        vo.setUpdateTime(petition.getUpdateTime());
        return vo;
    }
}
