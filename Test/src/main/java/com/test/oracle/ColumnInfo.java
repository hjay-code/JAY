package com.test.oracle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColumnInfo {
    private String columnName;        // 列名
    private String typeName;          // 数据类型（如 VARCHAR2、INT）
    private int dataLength;           // 长度
    private Boolean nullable;         // 是否可空
    private String defaultValue;      // 默认值
    private String remarks;           // 列注释
    private boolean primaryKey;       // 是否主键

    public ColumnInfo(String columnName){
        this.columnName = columnName;
    }
}
