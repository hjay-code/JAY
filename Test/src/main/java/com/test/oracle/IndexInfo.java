package com.test.oracle;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndexInfo {
    private String indexName;         // 索引名
    private boolean unique;           // 是否唯一索引
    private List<String> columns = new ArrayList<>(); // 索引包含的列（保持顺序）

    public IndexInfo(String indexNamem , boolean uniquem){
        this.indexName = indexNamem;
        this.unique = uniquem;
    }

    public void addColumn(String column){
        columns.add(column);
    }
}
