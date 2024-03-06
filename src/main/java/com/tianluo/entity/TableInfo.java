package com.tianluo.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class TableInfo {
    @ExcelProperty("数据库")
    private String dataBaseName;
    @ExcelProperty("表名")
    private String tableName;
    @ExcelProperty("表中文名")
    private String tableNameCn;
    @ExcelProperty("列名")
    private String column;
    @ExcelProperty("数据类型")
    private String columnType;
    @ExcelProperty("数据长度")
    private Integer columnLength;
    @ExcelProperty("注释")
    private String comment;

}
