package com.community.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class PetitionExcel {
    @ExcelProperty("工单编号")
    private String orderNo;
    @ExcelProperty("标题")
    private String title;
    @ExcelProperty("分类")
    private String category;
    @ExcelProperty("提交人")
    private String userName;
    @ExcelProperty("状态")
    private String statusText;
    @ExcelProperty("处理人")
    private String handlerName;
    @ExcelProperty("提交时间")
    private String createTime;
    @ExcelProperty("办结时间")
    private String resolvedTime;
}
