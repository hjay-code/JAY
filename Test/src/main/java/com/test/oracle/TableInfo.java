package com.test.oracle;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableInfo {
    private String tableName;        // 表名
    private String remarks;          // 表注释
    private List<ColumnInfo> columns = new ArrayList<>();  // 列信息
    private List<IndexInfo> indexes = new ArrayList<>();   // 索引信息
    private List<String> primaryKeys = new ArrayList<>();  // 主键列名

    public TableInfo(String tableName) {
        this.tableName = tableName;
    }

    public void addColumn(ColumnInfo column){
        this.columns.add(column);
    }

    public void addPrimaryKey(String columnName){
        this.primaryKeys.add(columnName);
    }

    public void addIndex(IndexInfo indexInfo){
        this.indexes.add(indexInfo);
    }
}