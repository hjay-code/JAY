package com.test.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据库表数据类
 * 用于存储从数据库查询的数据（包含多行记录）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableData {
    
    /**
     * 表名
     */
    private String tableName;
    
    /**
     * 列名列表
     */
    private List<String> columnNames = new ArrayList<>();
    
    /**
     * 数据行列表，每行是一个Map（列名 -> 值）
     */
    private List<Map<String, Object>> rows = new ArrayList<>();
    
    /**
     * 总行数
     */
    private int totalRows;
    
    /**
     * 添加一行数据
     *
     * @param rowData 一行数据（列名 -> 值的映射）
     */
    public void addRow(Map<String, Object> rowData) {
        this.rows.add(rowData);
        this.totalRows = rows.size();
    }
    
    /**
     * 设置列名
     *
     * @param columnNames 列名列表
     */
    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }
    
    /**
     * 获取指定行的数据
     *
     * @param rowIndex 行索引（从0开始）
     * @return 该行数据的Map
     */
    public Map<String, Object> getRow(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < rows.size()) {
            return rows.get(rowIndex);
        }
        return null;
    }
    
    /**
     * 获取所有行数据
     *
     * @return 所有行数据的列表
     */
    public List<Map<String, Object>> getAllRows() {
        return rows;
    }
    
    /**
     * 打印表格数据
     */
    public void printTable() {
        System.out.println("========== " + tableName + " ==========");
        System.out.println("Total Rows: " + totalRows);
        System.out.println();
        
        if (rows.isEmpty()) {
            System.out.println("(No data)");
            return;
        }
        
        // 打印列名
        StringBuilder header = new StringBuilder();
        for (String columnName : columnNames) {
            header.append(String.format("%-20s", columnName));
        }
        System.out.println(header.toString());
        
        // 打印分隔线
        System.out.println("-".repeat(columnNames.size() * 20));
        
        // 打印数据行
        for (Map<String, Object> row : rows) {
            StringBuilder rowStr = new StringBuilder();
            for (String columnName : columnNames) {
                Object value = row.get(columnName);
                String displayValue = value == null ? "NULL" : value.toString();
                rowStr.append(String.format("%-20s", displayValue));
            }
            System.out.println(rowStr.toString());
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TableData{tableName='").append(tableName).append("', ");
        sb.append("columns=").append(columnNames.size()).append(", ");
        sb.append("rows=").append(totalRows).append("}");
        return sb.toString();
    }
}
