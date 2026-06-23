package com.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.community.dto.OrderDTO;
import com.community.dto.ProductDTO;
import com.community.entity.Order;
import com.community.entity.OrderItem;
import com.community.entity.Product;
import com.community.exception.BusinessException;
import com.community.mapper.OrderItemMapper;
import com.community.mapper.OrderMapper;
import com.community.mapper.ProductMapper;
import com.community.service.GroupBuyService;
import com.community.service.StockService;
import com.community.vo.OrderItemVO;
import com.community.vo.OrderVO;
import com.community.vo.PageResult;
import com.community.vo.ProductVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupBuyServiceImpl implements GroupBuyService {

    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final StockService stockService;

    @Override
    public PageResult<ProductVO> getProductList(String category, Integer current, Integer size) {
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", 0);
        if (category != null && !category.isEmpty()) {
            queryWrapper.eq("category", category);
        }
        queryWrapper.orderByDesc("create_time");

        long total = productMapper.selectCount(queryWrapper);

        int offset = (current - 1) * size;
        queryWrapper.last("LIMIT " + offset + ", " + size);
        List<Product> products = productMapper.selectList(queryWrapper);

        List<ProductVO> voList = products.stream().map(this::convertProductToVO).collect(Collectors.toList());
        return new PageResult<>(total, voList, current, size);
    }

    @Override
    public ProductVO getProductDetail(Long id) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException("商品不存在");
        }
        return convertProductToVO(product);
    }

    @Override
    @Transactional
    public OrderVO createOrder(Long userId, OrderDTO orderDTO) {
        Product product = productMapper.selectById(orderDTO.getProductId());
        if (product == null) {
            throw new BusinessException("商品不存在");
        }
        if (product.getStatus() != 1) {
            throw new BusinessException("商品已下架");
        }
        if (product.getStock() < orderDTO.getQuantity()) {
            throw new BusinessException("库存不足");
        }

        // 创建订单
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderNo(generateOrderNo());
        order.setTotalAmount(product.getPrice().multiply(BigDecimal.valueOf(orderDTO.getQuantity())));
        order.setActualAmount(order.getTotalAmount());
        order.setStatus(0); // 待支付
        order.setReceiver(orderDTO.getReceiver());
        order.setReceiverPhone(orderDTO.getReceiverPhone());
        order.setAddress(orderDTO.getAddress());
        order.setRemark(orderDTO.getRemark());
        if (orderDTO.getUsedPoints() != null) {
            order.setUsedPoints(orderDTO.getUsedPoints());
        }
        orderMapper.insert(order);

        // 创建订单项
        OrderItem item = new OrderItem();
        item.setOrderId(order.getId());
        item.setProductId(product.getId());
        item.setProductName(product.getName());
        item.setProductImg(product.getCoverImg());
        item.setPrice(product.getPrice());
        item.setQuantity(orderDTO.getQuantity());
        item.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(orderDTO.getQuantity())));
        orderItemMapper.insert(item);

        // 扣减库存（通过StockService原子操作）
        if (!stockService.deductGroupStock(product.getId(), orderDTO.getQuantity())) {
            throw new BusinessException("库存不足");
        }
        productMapper.increaseSales(product.getId(), orderDTO.getQuantity());

        return convertOrderToVO(order, List.of(item));
    }

    @Override
    public PageResult<OrderVO> getMyOrders(Long userId, Integer status, Integer current, Integer size) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        queryWrapper.orderByDesc("create_time");

        long total = orderMapper.selectCount(queryWrapper);

        int offset = (current - 1) * size;
        queryWrapper.last("LIMIT " + offset + ", " + size);
        List<Order> orders = orderMapper.selectList(queryWrapper);

        List<OrderVO> voList = orders.stream().map(order -> {
            List<OrderItem> items = orderItemMapper.selectByOrderId(order.getId());
            return convertOrderToVO(order, items);
        }).collect(Collectors.toList());

        return new PageResult<>(total, voList, current, size);
    }

    @Override
    public OrderVO getOrderDetail(Long id, Long userId) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException("无权查看该订单");
        }
        List<OrderItem> items = orderItemMapper.selectByOrderId(id);
        return convertOrderToVO(order, items);
    }

    @Override
    @Transactional
    public void cancelOrder(Long id, Long userId) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException("无权取消该订单");
        }
        if (order.getStatus() != 0) {
            throw new BusinessException("当前状态不允许取消");
        }

        order.setStatus(3); // 已取消
        order.setCancelTime(LocalDateTime.now());
        orderMapper.updateById(order);

        // 恢复库存
        List<OrderItem> items = orderItemMapper.selectByOrderId(id);
        for (OrderItem item : items) {
            productMapper.updateStock(item.getProductId(), -item.getQuantity());
        }
    }

    @Override
    @Transactional
    public void completeOrder(Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (order.getStatus() != 1) {
            throw new BusinessException("当前状态不允许完成");
        }

        order.setStatus(2); // 已完成
        order.setCompleteTime(LocalDateTime.now());
        orderMapper.updateById(order);
    }

    @Override
    @Transactional
    public void createProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setCoverImg(productDTO.getCoverImg());
        product.setImages(productDTO.getImages());
        product.setCategory(productDTO.getCategory());
        product.setPrice(productDTO.getPrice());
        product.setOriginalPrice(productDTO.getOriginalPrice());
        product.setUnit(productDTO.getUnit());
        product.setStock(productDTO.getStock());
        product.setPointsDeduction(productDTO.getPointsDeduction());
        product.setMerchantId(productDTO.getMerchantId());
        product.setMerchantName(productDTO.getMerchantName());
        product.setStatus(0); // 待上架
        productMapper.insert(product);
    }

    @Override
    @Transactional
    public void updateProduct(Long id, ProductDTO productDTO) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException("商品不存在");
        }

        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setCoverImg(productDTO.getCoverImg());
        product.setImages(productDTO.getImages());
        product.setCategory(productDTO.getCategory());
        product.setPrice(productDTO.getPrice());
        product.setOriginalPrice(productDTO.getOriginalPrice());
        product.setUnit(productDTO.getUnit());
        product.setStock(productDTO.getStock());
        product.setPointsDeduction(productDTO.getPointsDeduction());
        product.setMerchantId(productDTO.getMerchantId());
        product.setMerchantName(productDTO.getMerchantName());
        productMapper.updateById(product);
    }

    @Override
    @Transactional
    public void updateProductStatus(Long id, Integer status) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException("商品不存在");
        }
        product.setStatus(status);
        productMapper.updateById(product);
    }

    @Override
    public PageResult<OrderVO> getAllOrders(Integer status, String keyword, Integer current, Integer size) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.and(w -> w.like("order_no", keyword).or().like("user_name", keyword));
        }
        queryWrapper.orderByDesc("create_time");

        long total = orderMapper.selectCount(queryWrapper);

        int offset = (current - 1) * size;
        queryWrapper.last("LIMIT " + offset + ", " + size);
        List<Order> orders = orderMapper.selectList(queryWrapper);

        List<OrderVO> voList = orders.stream().map(order -> {
            List<OrderItem> items = orderItemMapper.selectByOrderId(order.getId());
            return convertOrderToVO(order, items);
        }).collect(Collectors.toList());

        return new PageResult<>(total, voList, current, size);
    }

    private String generateOrderNo() {
        String dateStr = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "TB" + dateStr + String.format("%04d", (int) (Math.random() * 10000));
    }

    private ProductVO convertProductToVO(Product product) {
        ProductVO vo = new ProductVO();
        vo.setId(product.getId());
        vo.setName(product.getName());
        vo.setDescription(product.getDescription());
        vo.setCoverImg(product.getCoverImg());
        vo.setImages(product.getImages());
        vo.setCategory(product.getCategory());
        vo.setPrice(product.getPrice());
        vo.setOriginalPrice(product.getOriginalPrice());
        vo.setUnit(product.getUnit());
        vo.setStock(product.getStock());
        vo.setSales(product.getSales());
        vo.setPointsDeduction(product.getPointsDeduction());
        vo.setMerchantId(product.getMerchantId());
        vo.setMerchantName(product.getMerchantName());
        vo.setMerchantStatus(product.getMerchantStatus());
        vo.setStatus(product.getStatus());
        vo.setSort(product.getSort());
        vo.setCreateTime(product.getCreateTime());
        vo.setUpdateTime(product.getUpdateTime());
        return vo;
    }

    private OrderVO convertOrderToVO(Order order, List<OrderItem> items) {
        OrderVO vo = new OrderVO();
        vo.setId(order.getId());
        vo.setOrderNo(order.getOrderNo());
        vo.setUserId(order.getUserId());
        vo.setUserName(order.getUserName());
        vo.setUserPhone(order.getUserPhone());
        vo.setTotalAmount(order.getTotalAmount());
        vo.setPointsDeductionAmount(order.getPointsDeductionAmount());
        vo.setActualAmount(order.getActualAmount());
        vo.setUsedPoints(order.getUsedPoints());
        vo.setAddress(order.getAddress());
        vo.setReceiver(order.getReceiver());
        vo.setReceiverPhone(order.getReceiverPhone());
        vo.setRemark(order.getRemark());
        vo.setStatus(order.getStatus());
        vo.setPayTime(order.getPayTime());
        vo.setCompleteTime(order.getCompleteTime());
        vo.setCancelTime(order.getCancelTime());
        vo.setCancelReason(order.getCancelReason());
        vo.setCreateTime(order.getCreateTime());
        vo.setUpdateTime(order.getUpdateTime());

        if (items != null) {
            List<OrderItemVO> itemVOs = items.stream().map(item -> {
                OrderItemVO itemVO = new OrderItemVO();
                itemVO.setId(item.getId());
                itemVO.setOrderId(item.getOrderId());
                itemVO.setProductId(item.getProductId());
                itemVO.setProductName(item.getProductName());
                itemVO.setProductImg(item.getProductImg());
                itemVO.setPrice(item.getPrice());
                itemVO.setQuantity(item.getQuantity());
                itemVO.setSubtotal(item.getSubtotal());
                itemVO.setCreateTime(item.getCreateTime());
                return itemVO;
            }).collect(Collectors.toList());
            vo.setItems(itemVOs);
        } else {
            vo.setItems(new ArrayList<>());
        }
        return vo;
    }
}
