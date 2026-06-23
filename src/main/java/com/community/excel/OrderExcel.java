package com.community.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderExcel {
    @ExcelProperty("订单号")
    private String orderNo;
    @ExcelProperty("用户")
    private String userName;
    @ExcelProperty("商品")
    private String productName;
    @ExcelProperty("数量")
    private Integer quantity;
    @ExcelProperty("金额")
    private BigDecimal actualAmount;
    @ExcelProperty("状态")
    private String statusText;
    @ExcelProperty("下单时间")
    private String createTime;
}
