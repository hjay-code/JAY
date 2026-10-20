package com.test.oracle;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

@Slf4j
public class Main {
    public static void main(String[] args) throws Exception {
//        MysqlTest();


        OracleTest();
    }

    public static void OracleTest() throws Exception{
        // Oracle 连接信息（请替换为实际值）
        String url = "jdbc:oracle:thin:@//localhost:1521/ORCLPDB";  // 服务名方式
        // 或者 SID 方式：jdbc:oracle:thin:@localhost:1521:ORCL
        String user = "SCOTT";       // 你的 Oracle 用户名
        String password = "tiger";   // 密码

        Connection conn = DriverManager.getConnection(url, user, password);

        // 自动识别为 Oracle 并创建对应 Reader
        IDbMetaReader reader = MetaReaderFactory.createReader(conn);

        // Oracle 中 schema 就是用户名（通常大写）
        String schema = user.toUpperCase();  // "SCOTT"

        // 1. 获取该 schema 下所有表
        List<TableInfo> tables = reader.getTables(conn, schema);
        log.info("=== {} === 用户下的表", schema);
        for (TableInfo tb : tables) {
            log.info("表明：{}， 注释： {}", tb.getTableName(), tb.getRemarks());
        }

        // 2. 查看某张表的详细信息（以 EMP 表为例）
        String targetTable = "EMP";
        TableInfo detail = reader.getTableDetail(conn, schema, targetTable);
        System.out.println("\n========== 表详情: " + detail.getTableName() + " ==========");
        System.out.println("表注释: " + detail.getRemarks());
        System.out.println("主键列: " + detail.getPrimaryKeys());

        System.out.println("\n--- 列信息 ---");
        for (ColumnInfo col : detail.getColumns()) {
            System.out.printf(
                    "列名: %-12s 类型: %-10s 长度: %-5d 可空: %-5s 默认值: %-10s 注释: %s 主键: %s%n",
                    col.getColumnName(),
                    col.getTypeName(),
                    col.getDataLength(),
                    col.getNullable(),
                    col.getDefaultValue(),
                    col.getRemarks(),
                    col.isPrimaryKey()
            );
        }

        System.out.println("\n--- 索引信息 ---");
        for (IndexInfo idx : detail.getIndexes()) {
            System.out.printf("索引名: %-20s 唯一: %-5s 包含列: %s%n",
                    idx.getIndexName(),
                    idx.isUnique(),
                    idx.getColumns()
            );
        }

        conn.close();
    }

    public static void MysqlTest() throws Exception {
        // 1. 获取连接（以 MySQL 为例）
        String url = "jdbc:mysql://localhost:3306/yourdb?useUnicode=true&characterEncoding=utf-8";
        String user = "root";
        String password = "root";
        Connection conn = DriverManager.getConnection(url, user, password);

        // 2. 创建对应的 Reader
        IDbMetaReader reader = MetaReaderFactory.createReader(conn);

        // 3. 获取所有表（schema 为数据库名）
        List<TableInfo> tables = reader.getTables(conn, "yourdb");
        for (TableInfo tb : tables) {
            System.out.println("表: " + tb.getTableName() + " 注释: " + tb.getRemarks());
        }

        // 4. 获取某张表详情
        TableInfo detail = reader.getTableDetail(conn, "yourdb", "user");
        System.out.println("\n=== 表: " + detail.getTableName() + " ===");
        System.out.println("注释: " + detail.getRemarks());
        System.out.println("主键列: " + detail.getPrimaryKeys());

        System.out.println("\n字段列表:");
        for (ColumnInfo col : detail.getColumns()) {
            System.out.printf("列名: %s, 类型: %s(%d), 可空: %s, 默认值: %s, 注释: %s, 主键: %s%n",
                    col.getColumnName(), col.getTypeName(), col.getDataLength(),
                    col.getNullable(), col.getDefaultValue(), col.getRemarks(), col.isPrimaryKey());
        }

        System.out.println("\n索引列表:");
        for (IndexInfo idx : detail.getIndexes()) {
            System.out.printf("索引名: %s, 唯一: %s, 列: %s%n",
                    idx.getIndexName(), idx.isUnique(), idx.getColumns());
        }

        conn.close();
    }
}