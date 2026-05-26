package com.test.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库表信息类
 * 用于存储表的完整结构信息（包括所有列）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableInfo {
    
    /**
     * 表名
     */
    private String tableName;
    
    /**
     * 表注释/描述
     */
    private String tableComment;
    
    /**
     * 列信息列表
     */
    private List<ColumnInfo> columns = new ArrayList<>();
    
    /**
     * 主键列名列表
     */
    private List<String> primaryKeys = new ArrayList<>();
    
    /**
     * 添加列信息
     *
     * @param column 列信息对象
     */
    public void addColumn(ColumnInfo column) {
        this.columns.add(column);
    }
    
    /**
     * 获取列数量
     *
     * @return 列的数量
     */
    public int getColumnCount() {
        return columns.size();
    }
    
    /**
     * 根据列名获取列信息
     *
     * @param columnName 列名
     * @return 列信息对象，不存在则返回null
     */
    public ColumnInfo getColumnByName(String columnName) {
        return columns.stream()
                .filter(col -> col.getColumnName().equalsIgnoreCase(columnName))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * 获取所有列名
     *
     * @return 列名数组
     */
    public String[] getAllColumnNames() {
        return columns.stream()
                .map(ColumnInfo::getColumnName)
                .toArray(String[]::new);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Table: ").append(tableName).append("\n");
        if (tableComment != null && !tableComment.isEmpty()) {
            sb.append("Comment: ").append(tableComment).append("\n");
        }
        sb.append("Columns:\n");
        for (ColumnInfo column : columns) {
            sb.append("  - ").append(column.toString()).append("\n");
        }
        if (!primaryKeys.isEmpty()) {
            sb.append("Primary Keys: ").append(String.join(", ", primaryKeys)).append("\n");
        }
        return sb.toString();
    }
}
