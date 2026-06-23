package com.community.service;

import com.community.dto.*;
import com.community.entity.PetitionLog;
import com.community.vo.*;
import com.community.vo.PageResult;
import java.util.List;

public interface PetitionService {
    void submitPetition(Long userId, PetitionDTO petitionDTO);
    PageResult<PetitionVO> getMyPetitions(Long userId, Integer status, Integer current, Integer size);
    PetitionVO getPetitionDetail(Long id);
    void assignPetition(Long id, Long assignerId, Long handlerId, String handlerName);
    void processPetition(Long id);
    void resolvePetition(Long id, String note);
    void archivePetition(Long id);
    PageResult<PetitionVO> getAllPetitions(Integer status, String keyword, Integer current, Integer size);
    List<PetitionLog> getPetitionLogs(Long petitionId);
    void ratePetition(Long id, Integer rating, String content);
}
