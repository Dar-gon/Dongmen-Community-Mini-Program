package com.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.community.entity.ExchangeGoods;
import com.community.entity.ExchangeRecord;
import com.community.entity.PointsAccount;
import com.community.entity.PointsTransaction;
import com.community.exception.BusinessException;
import com.community.mapper.ExchangeGoodsMapper;
import com.community.mapper.ExchangeRecordMapper;
import com.community.mapper.PointsAccountMapper;
import com.community.mapper.PointsTransactionMapper;
import com.community.service.ExchangeService;
import com.community.service.StockService;
import com.community.vo.ExchangeGoodsVO;
import com.community.vo.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeServiceImpl implements ExchangeService {

    private final ExchangeGoodsMapper exchangeGoodsMapper;
    private final ExchangeRecordMapper exchangeRecordMapper;
    private final PointsAccountMapper pointsAccountMapper;
    private final PointsTransactionMapper pointsTransactionMapper;
    private final StockService stockService;

    @Override
    public PageResult<ExchangeGoodsVO> getGoodsList(Integer current, Integer size) {
        QueryWrapper<ExchangeGoods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", 0);
        queryWrapper.orderByAsc("sort");

        long total = exchangeGoodsMapper.selectCount(queryWrapper);

        int offset = (current - 1) * size;
        queryWrapper.last("LIMIT " + offset + ", " + size);
        List<ExchangeGoods> goodsList = exchangeGoodsMapper.selectList(queryWrapper);

        List<ExchangeGoodsVO> voList = goodsList.stream().map(this::convertGoodsToVO).collect(Collectors.toList());
        return new PageResult<>(total, voList, current, size);
    }

    @Override
    public ExchangeGoodsVO getGoodsDetail(Long id) {
        ExchangeGoods goods = exchangeGoodsMapper.selectById(id);
        if (goods == null) {
            throw new BusinessException("兑换商品不存在");
        }
        return convertGoodsToVO(goods);
    }

    @Override
    @Transactional
    public void exchange(Long userId, Long goodsId) {
        ExchangeGoods goods = exchangeGoodsMapper.selectById(goodsId);
        if (goods == null) {
            throw new BusinessException("兑换商品不存在");
        }
        if (goods.getStatus() != 1) {
            throw new BusinessException("商品不可兑换");
        }
        if (goods.getStock() <= 0) {
            throw new BusinessException("库存不足");
        }

        // 检查积分是否充足
        PointsAccount account = pointsAccountMapper.selectById(userId);
        if (account == null || account.getBalance() < goods.getIntegralPrice()) {
            throw new BusinessException("积分不足");
        }

        // 扣减积分
        account.setBalance(account.getBalance() - goods.getIntegralPrice());
        account.setTotalSpent(account.getTotalSpent() + goods.getIntegralPrice());
        pointsAccountMapper.updateById(account);

        // 记录积分消费
        PointsTransaction transaction = new PointsTransaction();
        transaction.setUserId(userId);
        transaction.setAmount(-goods.getIntegralPrice());
        transaction.setType("exchange");
        transaction.setDescription("兑换商品: " + goods.getName());
        transaction.setSourceType("exchange");
        transaction.setSourceId(goodsId);
        transaction.setBalanceAfter(account.getBalance());
        pointsTransactionMapper.insert(transaction);

        // 扣减库存（通过StockService原子操作）
        if (!stockService.deductExchangeStock(goodsId, 1)) {
            throw new BusinessException("库存不足");
        }
        exchangeGoodsMapper.incrementExchangeCount(goodsId);

        // 创建兑换记录
        ExchangeRecord record = new ExchangeRecord();
        record.setUserId(userId);
        record.setGoodsId(goodsId);
        record.setGoodsName(goods.getName());
        record.setUsedPoints(goods.getIntegralPrice());
        record.setStatus(0); // 待领取
        exchangeRecordMapper.insert(record);
    }

    @Override
    public PageResult<ExchangeRecord> getMyRecords(Long userId, Integer current, Integer size) {
        QueryWrapper<ExchangeRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("create_time");

        long total = exchangeRecordMapper.selectCount(queryWrapper);

        int offset = (current - 1) * size;
        queryWrapper.last("LIMIT " + offset + ", " + size);
        List<ExchangeRecord> records = exchangeRecordMapper.selectList(queryWrapper);
        return new PageResult<>(total, records, current, size);
    }

    @Override
    @Transactional
    public void createGoods(ExchangeGoods goods) {
        goods.setStatus(0); // 待上架
        goods.setExchangeCount(0);
        exchangeGoodsMapper.insert(goods);
    }

    @Override
    @Transactional
    public void updateGoods(Long id, ExchangeGoods goods) {
        ExchangeGoods existingGoods = exchangeGoodsMapper.selectById(id);
        if (existingGoods == null) {
            throw new BusinessException("商品不存在");
        }

        goods.setId(id);
        exchangeGoodsMapper.updateById(goods);
    }

    @Override
    @Transactional
    public void updateGoodsStatus(Long id, Integer status) {
        ExchangeGoods goods = exchangeGoodsMapper.selectById(id);
        if (goods == null) {
            throw new BusinessException("商品不存在");
        }

        goods.setStatus(status);
        exchangeGoodsMapper.updateById(goods);
    }

    private ExchangeGoodsVO convertGoodsToVO(ExchangeGoods goods) {
        ExchangeGoodsVO vo = new ExchangeGoodsVO();
        vo.setId(goods.getId());
        vo.setName(goods.getName());
        vo.setDescription(goods.getDescription());
        vo.setCoverImg(goods.getCoverImg());
        vo.setIntegralPrice(goods.getIntegralPrice());
        vo.setStock(goods.getStock());
        vo.setExchangeCount(goods.getExchangeCount());
        vo.setCategory(goods.getCategory());
        vo.setStatus(goods.getStatus());
        vo.setSort(goods.getSort());
        vo.setCreateTime(goods.getCreateTime());
        vo.setUpdateTime(goods.getUpdateTime());
        return vo;
    }
}
