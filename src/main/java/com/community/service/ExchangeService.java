package com.community.service;

import com.community.vo.*;
import com.community.entity.ExchangeRecord;
import com.community.entity.ExchangeGoods;
import com.community.vo.PageResult;

public interface ExchangeService {
    PageResult<ExchangeGoodsVO> getGoodsList(Integer current, Integer size);
    ExchangeGoodsVO getGoodsDetail(Long id);
    void exchange(Long userId, Long goodsId);
    PageResult<ExchangeRecord> getMyRecords(Long userId, Integer current, Integer size);

    // 管理员
    void createGoods(ExchangeGoods goods);
    void updateGoods(Long id, ExchangeGoods goods);
    void updateGoodsStatus(Long id, Integer status);
}
