package com.test.table;


import com.test.jdbc.JdbcUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

    public static void main(String[] args) {
        // ========== 测试获取表结构 ==========
        log.info("\n========== 获取表结构信息 ==========");
        TableInfo tableInfo = JdbcUtil.getTableInfo("user");
        System.out.println(tableInfo.toString());

        // ========== 测试获取表数据 ==========
        log.info("\n========== 获取表数据 ==========");
        TableData tableData = JdbcUtil.getTableData("user", null);
        tableData.printTable();

        // ========== 测试带条件查询表数据 ==========
        log.info("\n========== 带条件查询表数据 ==========");
        TableData filteredData = JdbcUtil.getTableData("user", "age > ?", 25);
        filteredData.printTable();
    }
}
