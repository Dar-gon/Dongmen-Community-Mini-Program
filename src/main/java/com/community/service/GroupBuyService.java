package com.community.service;

import com.community.dto.*;
import com.community.vo.*;
import com.community.vo.PageResult;
import java.util.List;

public interface GroupBuyService {
    PageResult<ProductVO> getProductList(String category, Integer current, Integer size);
    ProductVO getProductDetail(Long id);
    OrderVO createOrder(Long userId, OrderDTO orderDTO);
    PageResult<OrderVO> getMyOrders(Long userId, Integer status, Integer current, Integer size);
    OrderVO getOrderDetail(Long id, Long userId);
    void cancelOrder(Long id, Long userId);
    void completeOrder(Long id);

    // 管理员
    void createProduct(ProductDTO productDTO);
    void updateProduct(Long id, ProductDTO productDTO);
    void updateProductStatus(Long id, Integer status);
    PageResult<OrderVO> getAllOrders(Integer status, String keyword, Integer current, Integer size);
}
