package com.test.oracle;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class DDLGenerator {

    /**
     * 根据表信息和数据库类型生成完整的 DDL 脚本
     *
     * @param table  表结构信息
     * @param dbType 数据库类型： "mysql" 或 "oracle"
     * @return CREATE TABLE 及相关 DDL 语句
     */
    public static String generateCreateTable(TableInfo table, String dbType) {
        if (table == null || table.getTableName() == null) {
            throw new IllegalArgumentException("表信息不能为空");
        }
        dbType = dbType.toLowerCase();
        switch (dbType) {
            case "mysql":
                return generateMySql(table);
            case "oracle":
                return generateOracle(table);
            default:
                throw new UnsupportedOperationException("不支持的数据库类型：" + dbType);
        }
    }

    // ================= MySQL DDL =================
    private static String generateMySql(TableInfo table) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE `").append(table.getTableName()).append("` (\n");

        // 列定义
        List<String> colDefs = new ArrayList<>();
        for (ColumnInfo col : table.getColumns()) {
            colDefs.add(buildMySqlColumn(col, table.getPrimaryKeys()));
        }

        // 主键（多列）
        List<String> pks = table.getPrimaryKeys();
        if (pks != null && !pks.isEmpty()) {
            String pkCols = pks.stream().map(c -> "`" + c + "`").collect(Collectors.joining(", "));
            colDefs.add("PRIMARY KEY (" + pkCols + ")");
        }

        sb.append(String.join(",\n", colDefs));

        // 表注释
        sb.append("\n)");
        if (table.getRemarks() != null && !table.getRemarks().isEmpty()) {
            sb.append(" COMMENT='").append(escape(table.getRemarks())).append("'");
        }
        sb.append(";\n");

        // 生成列注释（MySQL 已在列定义中处理，不再单独生成）
        // 生成索引
        if (table.getIndexes() != null && !table.getIndexes().isEmpty()) {
            for (IndexInfo idx : table.getIndexes()) {
                // 跳过主键索引（已包含在表定义中）
                if (idx.getIndexName().equalsIgnoreCase("PRIMARY")) {
                    continue;
                }
                sb.append(buildMySqlIndex(idx, table.getTableName()));
            }
        }

        return sb.toString();
    }

    private static String buildMySqlColumn(ColumnInfo col, List<String> primaryKeys) {
        StringBuilder sb = new StringBuilder();
        sb.append("  `").append(col.getColumnName()).append("` ");

        // 数据类型 + 长度
        String type = col.getTypeName().toUpperCase();
        int len = col.getDataLength();
        sb.append(type);
        if (needsLength(type, len)) {
            sb.append("(").append(len).append(")");
        }

        // NOT NULL
        if (col.getNullable() != null && !col.getNullable()) {
            sb.append(" NOT NULL");
        } else {
            sb.append(" NULL");  // 显式声明 NULL（可选）
        }

        // DEFAULT
        if (col.getDefaultValue() != null && !col.getDefaultValue().isEmpty()) {
            sb.append(" DEFAULT ").append(col.getDefaultValue());
        }

        // 单列主键（若是复合主键，则在表定义末尾统一声明）
        if (primaryKeys != null && primaryKeys.size() == 1
                && primaryKeys.get(0).equals(col.getColumnName())) {
            sb.append(" PRIMARY KEY");
        }

        // 列注释
        if (col.getRemarks() != null && !col.getRemarks().isEmpty()) {
            sb.append(" COMMENT '").append(escape(col.getRemarks())).append("'");
        }

        return sb.toString();
    }

    private static String buildMySqlIndex(IndexInfo idx, String tableName) {
        String colList = idx.getColumns().stream()
                .map(c -> "`" + c + "`")
                .collect(Collectors.joining(", "));
        if (idx.isUnique()) {
            return String.format("CREATE UNIQUE INDEX `%s` ON `%s` (%s);\n",
                    idx.getIndexName(), tableName, colList);
        } else {
            return String.format("CREATE INDEX `%s` ON `%s` (%s);\n",
                    idx.getIndexName(), tableName, colList);
        }
    }

    // ================= Oracle DDL =================
    private static String generateOracle(TableInfo table) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ").append(table.getTableName()).append(" (\n");

        // 列定义
        List<String> colDefs = new ArrayList<>();
        for (ColumnInfo col : table.getColumns()) {
            colDefs.add(buildOracleColumn(col, table.getPrimaryKeys()));
        }

        // 主键（多列）
        List<String> pks = table.getPrimaryKeys();
        if (pks != null && !pks.isEmpty()) {
            String pkCols = pks.stream().map(DDLGenerator::quoteOracleName).collect(Collectors.joining(", "));
            colDefs.add("  CONSTRAINT " + table.getTableName() + "_PK PRIMARY KEY (" + pkCols + ")");
        }

        sb.append(String.join(",\n", colDefs));
        sb.append("\n)");

        // Oracle 不支持表级注释在 CREATE TABLE 内，使用单独的 COMMENT ON
        sb.append(";\n");
        if (table.getRemarks() != null && !table.getRemarks().isEmpty()) {
            sb.append(String.format("COMMENT ON TABLE %s IS '%s';\n",
                    table.getTableName(), escape(table.getRemarks())));
        }

        // 列注释
        for (ColumnInfo col : table.getColumns()) {
            if (col.getRemarks() != null && !col.getRemarks().isEmpty()) {
                sb.append(String.format("COMMENT ON COLUMN %s.%s IS '%s';\n",
                        table.getTableName(), col.getColumnName(), escape(col.getRemarks())));
            }
        }

        // 索引
        if (table.getIndexes() != null && !table.getIndexes().isEmpty()) {
            for (IndexInfo idx : table.getIndexes()) {
                // 跳过主键索引
                if (idx.getIndexName().toUpperCase().endsWith("_PK") ||
                        idx.getIndexName().equalsIgnoreCase("PRIMARY")) {
                    continue;
                }
                sb.append(buildOracleIndex(idx, table.getTableName()));
            }
        }

        return sb.toString();
    }

    private static String buildOracleColumn(ColumnInfo col, List<String> primaryKeys) {
        StringBuilder sb = new StringBuilder();
        sb.append("  ").append(col.getColumnName()).append(" ");

        String type = col.getTypeName().toUpperCase();
        int len = col.getDataLength();
        sb.append(type);
        if (needsLength(type, len)) {
            sb.append("(").append(len).append(")");
        }

        // DEFAULT
        if (col.getDefaultValue() != null && !col.getDefaultValue().isEmpty()) {
            sb.append(" DEFAULT ").append(col.getDefaultValue());
        }

        // NOT NULL
        if (col.getNullable() != null && !col.getNullable()) {
            sb.append(" NOT NULL");
        }

        // 单列主键
        if (primaryKeys != null && primaryKeys.size() == 1
                && primaryKeys.get(0).equals(col.getColumnName())) {
            sb.append(" PRIMARY KEY");
        }

        return sb.toString();
    }

    private static String buildOracleIndex(IndexInfo idx, String tableName) {
        String colList = idx.getColumns().stream()
                .map(DDLGenerator::quoteOracleName)
                .collect(Collectors.joining(", "));
        if (idx.isUnique()) {
            return String.format("CREATE UNIQUE INDEX %s ON %s (%s);\n",
                    idx.getIndexName(), tableName, colList);
        } else {
            return String.format("CREATE INDEX %s ON %s (%s);\n",
                    idx.getIndexName(), tableName, colList);
        }
    }

    // ================= 通用工具方法 =================
    private static boolean needsLength(String type, int length) {
        // 不需要显式长度的类型（Oracle / MySQL 通用）
        Set<String> noLengthTypes = new HashSet<>(Arrays.asList(
                "DATE", "TIMESTAMP", "CLOB", "BLOB", "LONG", "XMLTYPE",
                "JSON", "TEXT", "LONGTEXT", "MEDIUMTEXT", "TINYTEXT",
                "BOOLEAN", "BOOL", "FLOAT", "DOUBLE", "REAL",
                "YEAR", "TIME", "DATETIME", "TINYBLOB", "MEDIUMBLOB", "LONGBLOB"
        ));
        if (length <= 0 || noLengthTypes.contains(type.toUpperCase())) {
            return false;
        }
        return true;
    }

    private static String quoteOracleName(String name) {
        // Oracle 默认大写，这里不添加双引号以保持简洁
        return name;
    }

    private static String escape(String s) {
        return s.replace("'", "''");  // SQL 单引号转义
    }

    /**
     * 将单个表的 DDL 保存到 .sql 文件
     *
     * @param table    表结构
     * @param dbType   数据库类型 "mysql" 或 "oracle"
     * @param filePath 输出文件路径（建议 .sql 后缀）
     */
    public static void saveToFile(TableInfo table, String dbType, String filePath) throws IOException {
        String ddl = generateCreateTable(table, dbType);
        writeStringToFile(ddl, filePath);
    }

    /**
     * 将多个表的 DDL 保存到一个 .sql 文件
     *
     * @param tables   表结构列表
     * @param dbType   数据库类型
     * @param filePath 输出文件路径
     */
    public static void saveAllToFile(List<TableInfo> tables, String dbType, String filePath) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (TableInfo table : tables) {
            sb.append(generateCreateTable(table, dbType)).append("\n");
        }
        writeStringToFile(sb.toString(), filePath);
    }

    /**
     * 将 DDL 字符串直接写入文件（UTF-8编码）
     */
    private static void writeStringToFile(String content, String filePath) throws IOException {
        File file = new File(filePath);
        // 创建父目录
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write(content);
            writer.flush();
        }
    }
}
