package com.test.oracle;

import java.sql.Connection;
import java.util.List;

public interface IDbMetaReader {
    /**
     * 获取指定 schema 下的所有表基础信息（包含注释）
     * @param conn   数据库连接
     * @param schema 模式名（Oracle: 用户名/大写, MySQL: 数据库名）
     * @return 表信息列表
     */
    List<TableInfo> getTables(Connection conn, String schema) throws Exception;

    /**
     * 获取一张表的完整信息（列、主键、索引）
     * @param conn      数据库连接
     * @param schema    模式名
     * @param tableName 表名
     * @return 完整的表信息
     */
    TableInfo getTableDetail(Connection conn, String schema, String tableName) throws Exception;
}