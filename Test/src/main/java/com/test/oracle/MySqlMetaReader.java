package com.test.oracle;

import java.sql.*;
import java.util.*;

public class MySqlMetaReader implements IDbMetaReader {

    @Override
    public List<TableInfo> getTables(Connection conn, String schema) throws Exception {
        List<TableInfo> tables = new ArrayList<>();
        String sql = "SELECT TABLE_NAME, TABLE_COMMENT FROM information_schema.TABLES " +
                "WHERE TABLE_SCHEMA = ? AND TABLE_TYPE = 'BASE TABLE'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TableInfo ti = new TableInfo(rs.getString("TABLE_NAME"));
                    ti.setRemarks(rs.getString("TABLE_COMMENT"));
                    tables.add(ti);
                }
            }
        }
        return tables;
    }

    @Override
    public TableInfo getTableDetail(Connection conn, String schema, String tableName) throws Exception {
        TableInfo table = new TableInfo(tableName);

        // 1. 表注释
        String commentSql = "SELECT TABLE_COMMENT FROM information_schema.TABLES " +
                "WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?";
        try (PreparedStatement ps = conn.prepareStatement(commentSql)) {
            ps.setString(1, schema);
            ps.setString(2, tableName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) table.setRemarks(rs.getString("TABLE_COMMENT"));
        }

        // 2. 列信息
        String colSql =
                "SELECT COLUMN_NAME, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH, IS_NULLABLE, COLUMN_DEFAULT, COLUMN_COMMENT " +
                        "FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ? ORDER BY ORDINAL_POSITION";
        try (PreparedStatement ps = conn.prepareStatement(colSql)) {
            ps.setString(1, schema);
            ps.setString(2, tableName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ColumnInfo col = new ColumnInfo(rs.getString("COLUMN_NAME"));
                col.setTypeName(rs.getString("DATA_TYPE"));
                col.setDataLength(rs.getInt("CHARACTER_MAXIMUM_LENGTH")); // 注意数值类型长度需另处理
                col.setNullable("YES".equals(rs.getString("IS_NULLABLE")));
                col.setDefaultValue(rs.getString("COLUMN_DEFAULT"));
                col.setRemarks(rs.getString("COLUMN_COMMENT"));
                table.addColumn(col);
            }
        }

        // 3. 主键 (通过 KEY_COLUMN_USAGE 中 CONSTRAINT_NAME='PRIMARY')
        String pkSql =
                "SELECT COLUMN_NAME FROM information_schema.KEY_COLUMN_USAGE " +
                        "WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ? AND CONSTRAINT_NAME = 'PRIMARY' " +
                        "ORDER BY ORDINAL_POSITION";
        Set<String> pkSet = new HashSet<>();
        try (PreparedStatement ps = conn.prepareStatement(pkSql)) {
            ps.setString(1, schema);
            ps.setString(2, tableName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String col = rs.getString("COLUMN_NAME");
                table.addPrimaryKey(col);
                pkSet.add(col);
            }
        }
        for (ColumnInfo col : table.getColumns()) {
            if (pkSet.contains(col.getColumnName())) {
                col.setPrimaryKey(true);
            }
        }

        // 4. 索引信息 (information_schema.STATISTICS)
        String idxSql =
                "SELECT INDEX_NAME, NON_UNIQUE, COLUMN_NAME FROM information_schema.STATISTICS " +
                        "WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ? ORDER BY INDEX_NAME, SEQ_IN_INDEX";
        try (PreparedStatement ps = conn.prepareStatement(idxSql)) {
            ps.setString(1, schema);
            ps.setString(2, tableName);
            ResultSet rs = ps.executeQuery();
            Map<String, IndexInfo> indexMap = new LinkedHashMap<>();
            while (rs.next()) {
                String idxName = rs.getString("INDEX_NAME");
                boolean nonUnique = rs.getBoolean("NON_UNIQUE");
                String colName = rs.getString("COLUMN_NAME");
                indexMap.computeIfAbsent(idxName, k -> new IndexInfo(idxName, !nonUnique))
                        .addColumn(colName);
            }
            for (IndexInfo idx : indexMap.values()) {
                table.addIndex(idx);
            }
        }
        return table;
    }
}