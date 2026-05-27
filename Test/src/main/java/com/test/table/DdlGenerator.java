package com.test.table;

import com.test.jdbc.JdbcUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * DDL生成工具类
 * 用于根据数据库表结构生成CREATE TABLE语句并导出为.sql文件
 */
@Slf4j
public class DdlGenerator {

    /**
     * 生成单个表的DDL语句
     *
     * @param tableInfo 表信息对象
     * @return DDL语句字符串
     */
    public static String generateTableDdl(TableInfo tableInfo) {
        StringBuilder ddl = new StringBuilder();
        
        // 添加注释
        ddl.append("-- ============================================\n");
        ddl.append("-- 表名: ").append(tableInfo.getTableName()).append("\n");
        if (tableInfo.getTableComment() != null && !tableInfo.getTableComment().isEmpty()) {
            ddl.append("-- 说明: ").append(tableInfo.getTableComment()).append("\n");
        }
        ddl.append("-- ============================================\n");
        
        // CREATE TABLE语句开始
        ddl.append("CREATE TABLE `").append(tableInfo.getTableName()).append("` (\n");
        
        List<ColumnInfo> columns = tableInfo.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            ColumnInfo column = columns.get(i);
            ddl.append("    ");
            
            // 列名
            ddl.append("`").append(column.getColumnName()).append("` ");
            
            // 数据类型（包含大小和精度）
            ddl.append(column.getDataType());
            if (column.getColumnSize() > 0) {
                if ("DECIMAL".equalsIgnoreCase(column.getDataType()) || 
                    "NUMERIC".equalsIgnoreCase(column.getDataType())) {
                    ddl.append("(").append(column.getColumnSize());
                    if (column.getDecimalDigits() > 0) {
                        ddl.append(",").append(column.getDecimalDigits());
                    }
                    ddl.append(")");
                } else if (!"DATETIME".equalsIgnoreCase(column.getDataType()) &&
                           !"TIMESTAMP".equalsIgnoreCase(column.getDataType()) &&
                           !"DATE".equalsIgnoreCase(column.getDataType()) &&
                           !"TIME".equalsIgnoreCase(column.getDataType())) {
                    ddl.append("(").append(column.getColumnSize()).append(")");
                }
            }
            
            // 是否允许为空
            if (!column.isNullable()) {
                ddl.append(" NOT NULL");
            }
            
            // 默认值
            if (column.getDefaultValue() != null && !column.getDefaultValue().isEmpty()) {
                ddl.append(" DEFAULT ").append(column.getDefaultValue());
            }
            
            // 自增
            if (column.isAutoIncrement()) {
                ddl.append(" AUTO_INCREMENT");
            }
            
            // 注释
            if (column.getRemarks() != null && !column.getRemarks().isEmpty()) {
                ddl.append(" COMMENT '").append(escapeSql(column.getRemarks())).append("'");
            }
            
            // 逗号分隔（最后一列不加逗号）
            if (i < columns.size() - 1) {
                ddl.append(",");
            }
            ddl.append("\n");
        }
        
        // 主键约束
        if (!tableInfo.getPrimaryKeys().isEmpty()) {
            ddl.append("    PRIMARY KEY (");
            for (int i = 0; i < tableInfo.getPrimaryKeys().size(); i++) {
                if (i > 0) {
                    ddl.append(", ");
                }
                ddl.append("`").append(tableInfo.getPrimaryKeys().get(i)).append("`");
            }
            ddl.append(")");
            ddl.append(",\n");
        }
        
        // 移除最后一个逗号并添加结束括号
        if (ddl.charAt(ddl.length() - 2) == ',') {
            ddl.setLength(ddl.length() - 2);
            ddl.append("\n");
        }
        
        ddl.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci");
        
        // 表注释
        if (tableInfo.getTableComment() != null && !tableInfo.getTableComment().isEmpty()) {
            ddl.append(" COMMENT='").append(escapeSql(tableInfo.getTableComment())).append("'");
        }
        
        ddl.append(";");
        ddl.append("\n\n");
        
        return ddl.toString();
    }

    /**
     * 批量生成多个表的DDL语句
     *
     * @param tableInfos 表信息列表
     * @return 所有表的DDL语句
     */
    public static String generateMultipleTablesDdl(List<TableInfo> tableInfos) {
        StringBuilder ddl = new StringBuilder();
        
        // 添加文件头注释
        ddl.append("-- ============================================\n");
        ddl.append("-- DDL脚本文件\n");
        ddl.append("-- 生成时间: ").append(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date())).append("\n");
        ddl.append("-- 表数量: ").append(tableInfos.size()).append("\n");
        ddl.append("-- ============================================\n\n");
        
        for (TableInfo tableInfo : tableInfos) {
            ddl.append(generateTableDdl(tableInfo));
        }
        
        return ddl.toString();
    }

    /**
     * 将DDL语句导出到文件
     *
     * @param ddl DDL语句
     * @param filePath 文件路径
     * @return 是否成功
     */
    public static boolean exportToFile(String ddl, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(ddl);
            log.info("DDL文件导出成功: {}", filePath);
            return true;
        } catch (IOException e) {
            log.error("DDL文件导出失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 根据表名生成DDL并导出到文件
     *
     * @param tableName 表名
     * @param outputDir 输出目录
     * @return 生成的文件路径
     */
    public static String generateAndExport(String tableName, String outputDir) {
        log.info("开始生成表 [{}] 的DDL...", tableName);
        
        // 获取表结构信息
        TableInfo tableInfo = JdbcUtil.getTableInfo(tableName);
        
        if (tableInfo == null || tableInfo.getColumnCount() == 0) {
            log.error("表 [{}] 不存在或无列信息", tableName);
            return null;
        }
        
        // 生成DDL
        String ddl = generateTableDdl(tableInfo);
        
        // 构建文件路径
        Path path = Paths.get(outputDir, tableName + ".sql");
        String filePath = path.toString();
        
        // 导出到文件
        if (exportToFile(ddl, filePath)) {
            log.info("表 [{}] 的DDL生成成功: {}", tableName, filePath);
            return filePath;
        }
        
        return null;
    }

    /**
     * 批量生成多个表的DDL并导出到一个文件
     *
     * @param tableNames 表名数组
     * @param outputFile 输出文件路径（包含文件名）
     * @return 是否成功
     */
    public static boolean generateBatchAndExport(String[] tableNames, String outputFile) {
        log.info("开始批量生成 {} 个表的DDL...", tableNames.length);
        
        List<TableInfo> tableInfos = new java.util.ArrayList<>();
        
        for (String tableName : tableNames) {
            TableInfo tableInfo = JdbcUtil.getTableInfo(tableName);
            if (tableInfo != null && tableInfo.getColumnCount() > 0) {
                tableInfos.add(tableInfo);
            } else {
                log.warn("表 [{}] 不存在或无列信息，已跳过", tableName);
            }
        }
        
        if (tableInfos.isEmpty()) {
            log.error("没有有效的表信息可以生成DDL");
            return false;
        }
        
        // 生成所有表的DDL
        String ddl = generateMultipleTablesDdl(tableInfos);
        
        // 导出到文件
        if (exportToFile(ddl, outputFile)) {
            log.info("批量DDL生成成功，共 {} 个表: {}", tableInfos.size(), outputFile);
            return true;
        }
        
        return false;
    }

    /**
     * 转义SQL字符串中的特殊字符
     *
     * @param str 原始字符串
     * @return 转义后的字符串
     */
    private static String escapeSql(String str) {
        if (str == null) {
            return "";
        }
        return str.replace("'", "''")
                  .replace("\\", "\\\\")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r");
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试1：生成单个表的DDL并打印到控制台
        log.info("========== 测试1：生成单个表DDL ==========");
        TableInfo userTable = JdbcUtil.getTableInfo("user");
        String ddl = generateTableDdl(userTable);
        System.out.println(ddl);
        
        // 测试2：生成单个表的DDL并导出到文件
        log.info("\n========== 测试2：导出单个表DDL到文件 ==========");
        String outputPath = "D:/code/JAY/Test/ddl_output";
        File dir = new File(outputPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String filePath = generateAndExport("user", outputPath);
        if (filePath != null) {
            log.info("文件已生成: {}", filePath);
        }
        
        // 测试3：批量生成多个表的DDL并导出到一个文件
        log.info("\n========== 测试3：批量生成多个表DDL ==========");

        String[] tables = {"user"}; // 可以添加更多表名
        String batchOutputFile = outputPath + "/all_tables.sql";
        boolean success = generateBatchAndExport(tables, batchOutputFile);
        if (success) {
            log.info("批量DDL文件已生成: {}", batchOutputFile);
        }

        // 测试4：生成所有表的DDL生成文件
        log.info("\n========== 测试4：生成所有表DDL ==========");
        List<TableInfo> allTables = JdbcUtil.getAllTablesInfo();
        String allDdl = generateMultipleTablesDdl(allTables);
        boolean allSuccess = exportToFile(allDdl, outputPath + "/all_tables_2.sql");
        if (allSuccess) {
            log.info("所有表DDL已生成: {}", outputPath + "/all_tables.sql");
        }

        log.info("测试结束");
    }
}
