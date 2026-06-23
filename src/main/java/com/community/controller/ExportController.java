package com.community.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.community.entity.Order;
import com.community.entity.OrderItem;
import com.community.entity.Petition;
import com.community.excel.OrderExcel;
import com.community.excel.PetitionExcel;
import com.community.mapper.OrderItemMapper;
import com.community.mapper.OrderMapper;
import com.community.mapper.PetitionMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/export")
@RequiredArgsConstructor
@Tag(name = "数据导出")
public class ExportController {

    private final PetitionMapper petitionMapper;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @GetMapping("/petitions")
    @Operation(summary = "导出工单台账")
    public void exportPetitions(HttpServletResponse response) throws IOException {
        List<Petition> petitions = petitionMapper.selectList(
            new QueryWrapper<Petition>().orderByDesc("create_time"));

        List<PetitionExcel> list = new ArrayList<>();
        for (Petition p : petitions) {
            PetitionExcel e = new PetitionExcel();
            e.setOrderNo(p.getOrderNo());
            e.setTitle(p.getTitle());
            e.setCategory(p.getCategory());
            e.setUserName(p.getUserName());
            e.setStatusText(getStatusText(p.getStatus()));
            e.setHandlerName(p.getHandlerName());
            e.setCreateTime(String.valueOf(p.getCreateTime()));
            e.setResolvedTime(p.getResolvedTime() != null ? String.valueOf(p.getResolvedTime()) : "");
            list.add(e);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("民情工单台账", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), PetitionExcel.class).sheet("工单台账").doWrite(list);
    }

    @GetMapping("/orders")
    @Operation(summary = "导出团购订单")
    public void exportOrders(HttpServletResponse response) throws IOException {
        List<Order> orders = orderMapper.selectList(
            new QueryWrapper<Order>().orderByDesc("create_time"));

        // 批量查询所有订单的明细，按orderId分组
        List<Long> orderIds = orders.stream().map(Order::getId).collect(Collectors.toList());
        Map<Long, List<OrderItem>> itemsMap = Map.of();
        if (!orderIds.isEmpty()) {
            QueryWrapper<OrderItem> itemQw = new QueryWrapper<>();
            itemQw.in("order_id", orderIds);
            List<OrderItem> allItems = orderItemMapper.selectList(itemQw);
            itemsMap = allItems.stream().collect(Collectors.groupingBy(OrderItem::getOrderId));
        }

        List<OrderExcel> list = new ArrayList<>();
        for (Order o : orders) {
            List<OrderItem> items = itemsMap.getOrDefault(o.getId(), List.of());
            if (items.isEmpty()) {
                // 没有明细也导出一条记录
                OrderExcel e = new OrderExcel();
                e.setOrderNo(o.getOrderNo());
                e.setUserName(o.getUserName());
                e.setProductName("");
                e.setQuantity(0);
                e.setActualAmount(o.getActualAmount());
                e.setStatusText(getOrderStatusText(o.getStatus()));
                e.setCreateTime(String.valueOf(o.getCreateTime()));
                list.add(e);
            } else {
                // 每个明细一行
                for (OrderItem item : items) {
                    OrderExcel e = new OrderExcel();
                    e.setOrderNo(o.getOrderNo());
                    e.setUserName(o.getUserName());
                    e.setProductName(item.getProductName());
                    e.setQuantity(item.getQuantity());
                    e.setActualAmount(o.getActualAmount());
                    e.setStatusText(getOrderStatusText(o.getStatus()));
                    e.setCreateTime(String.valueOf(o.getCreateTime()));
                    list.add(e);
                }
            }
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("团购订单台账", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), OrderExcel.class).sheet("团购订单").doWrite(list);
    }

    private String getStatusText(Integer status) {
        if (status == null) return "未知";
        return switch (status) {
            case 0 -> "待处理";
            case 1 -> "已分派";
            case 2 -> "处理中";
            case 3 -> "已办结";
            case 4 -> "已归档";
            default -> "未知";
        };
    }

    private String getOrderStatusText(Integer status) {
        if (status == null) return "未知";
        return switch (status) {
            case 0 -> "待支付";
            case 1 -> "已支付";
            case 2 -> "已完成";
            case 3 -> "已取消";
            default -> "未知";
        };
    }
}
