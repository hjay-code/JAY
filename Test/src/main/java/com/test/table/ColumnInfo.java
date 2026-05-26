package com.test.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据库列信息类
 * 用于存储表的列结构信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColumnInfo {
    
    /**
     * 列名
     */
    private String columnName;
    
    /**
     * 数据类型（如 VARCHAR, INT, TIMESTAMP等）
     */
    private String dataType;
    
    /**
     * 列大小/长度
     */
    private int columnSize;
    
    /**
     * 小数位数（数值类型）
     */
    private int decimalDigits;
    
    /**
     * 是否允许为空
     */
    private boolean nullable;
    
    /**
     * 是否为主键
     */
    private boolean primaryKey;
    
    /**
     * 默认值
     */
    private String defaultValue;
    
    /**
     * 列注释
     */
    private String remarks;
    
    /**
     * 是否自增
     */
    private boolean autoIncrement;
    
    @Override
    public String toString() {
        return String.format("ColumnInfo{name='%s', type='%s', size=%d, nullable=%s, primary=%s}",
                columnName, dataType, columnSize, nullable ? "Y" : "N", 
                primaryKey ? "Y" : "N");
    }
}
