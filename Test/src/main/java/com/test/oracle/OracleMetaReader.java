package com.test.oracle;

import java.sql.*;
import java.util.*;

public class OracleMetaReader implements IDbMetaReader {

    @Override
    public List<TableInfo> getTables(Connection conn, String schema) throws Exception {
        List<TableInfo> tables = new ArrayList<>();
        String sql = "SELECT TABLE_NAME, COMMENTS FROM ALL_TAB_COMMENTS WHERE OWNER = ? AND TABLE_TYPE = 'TABLE'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema.toUpperCase());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TableInfo ti = new TableInfo(rs.getString("TABLE_NAME"));
                    ti.setRemarks(rs.getString("COMMENTS"));
                    tables.add(ti);
                }
            }
        }
        return tables;
    }

    @Override
    public TableInfo getTableDetail(Connection conn, String schema, String tableName) throws Exception {
        TableInfo table = new TableInfo(tableName.toUpperCase());

        // 1. 表注释
        String commentSql = "SELECT COMMENTS FROM ALL_TAB_COMMENTS WHERE OWNER = ? AND TABLE_NAME = ?";
        try (PreparedStatement ps = conn.prepareStatement(commentSql)) {
            ps.setString(1, schema.toUpperCase());
            ps.setString(2, tableName.toUpperCase());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) table.setRemarks(rs.getString("COMMENTS"));
        }

        // 2. 列信息（含注释）
        String colSql =
                "SELECT c.COLUMN_NAME, c.DATA_TYPE, c.DATA_LENGTH, c.NULLABLE, c.DATA_DEFAULT, m.COMMENTS " +
                        "FROM ALL_TAB_COLUMNS c LEFT JOIN ALL_COL_COMMENTS m " +
                        "ON c.OWNER = m.OWNER AND c.TABLE_NAME = m.TABLE_NAME AND c.COLUMN_NAME = m.COLUMN_NAME " +
                        "WHERE c.OWNER = ? AND c.TABLE_NAME = ? ORDER BY c.COLUMN_ID";
        try (PreparedStatement ps = conn.prepareStatement(colSql)) {
            ps.setString(1, schema.toUpperCase());
            ps.setString(2, tableName.toUpperCase());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ColumnInfo col = new ColumnInfo(rs.getString("COLUMN_NAME"));
                col.setTypeName(rs.getString("DATA_TYPE"));
                col.setDataLength(rs.getInt("DATA_LENGTH"));
                col.setNullable("Y".equals(rs.getString("NULLABLE")));
                col.setDefaultValue(rs.getString("DATA_DEFAULT"));
                col.setRemarks(rs.getString("COMMENTS"));
                table.addColumn(col);
            }
        }

        // 3. 主键
        String pkSql =
                "SELECT cols.COLUMN_NAME FROM ALL_CONSTRAINTS cons, ALL_CONS_COLUMNS cols " +
                        "WHERE cons.CONSTRAINT_NAME = cols.CONSTRAINT_NAME AND cons.CONSTRAINT_TYPE = 'P' " +
                        "AND cons.OWNER = ? AND cons.TABLE_NAME = ? ORDER BY cols.POSITION";
        Set<String> pkSet = new HashSet<>();
        try (PreparedStatement ps = conn.prepareStatement(pkSql)) {
            ps.setString(1, schema.toUpperCase());
            ps.setString(2, tableName.toUpperCase());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String colName = rs.getString("COLUMN_NAME");
                table.addPrimaryKey(colName);
                pkSet.add(colName);
            }
        }

        // 标记列是否为主键
        for (ColumnInfo col : table.getColumns()) {
            if (pkSet.contains(col.getColumnName())) {
                col.setPrimaryKey(true);
            }
        }

        // 4. 索引信息
        String idxSql =
                "SELECT i.INDEX_NAME, i.UNIQUENESS, ic.COLUMN_NAME " +
                        "FROM ALL_INDEXES i JOIN ALL_IND_COLUMNS ic ON i.INDEX_NAME = ic.INDEX_NAME AND i.OWNER = ic.INDEX_OWNER " +
                        "WHERE i.OWNER = ? AND i.TABLE_NAME = ? ORDER BY i.INDEX_NAME, ic.COLUMN_POSITION";
        try (PreparedStatement ps = conn.prepareStatement(idxSql)) {
            ps.setString(1, schema.toUpperCase());
            ps.setString(2, tableName.toUpperCase());
            ResultSet rs = ps.executeQuery();
            Map<String, IndexInfo> indexMap = new LinkedHashMap<>();
            while (rs.next()) {
                String idxName = rs.getString("INDEX_NAME");
                boolean unique = "UNIQUE".equalsIgnoreCase(rs.getString("UNIQUENESS"));
                String colName = rs.getString("COLUMN_NAME");
                indexMap.computeIfAbsent(idxName, k -> new IndexInfo(idxName, unique))
                        .addColumn(colName);
            }
            for (IndexInfo idx : indexMap.values()) {
                table.addIndex(idx);
            }
        }
        return table;
    }
}
