package com.test.jdbc;

import com.test.table.ColumnInfo;
import com.test.table.TableData;
import com.test.table.TableInfo;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * MySQL JDBC连接工具类
 */
@Slf4j
public class JdbcUtil {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    
    // 数据库连接配置（请根据实际情况修改）
    private static final String URL = "jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=UTC&characterEncoding=utf8&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";

    // 加载驱动
    static {
        try {
            Class.forName(DRIVER);
            log.info("MySQL驱动加载成功");
        } catch (ClassNotFoundException e) {
            log.error("MySQL驱动加载失败: {}", e.getMessage());
            throw new RuntimeException("MySQL驱动加载失败", e);
        }
    }

    /**
     * 获取数据库连接
     *
     * @return Connection对象
     * @throws SQLException 数据库异常
     */
    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        log.debug("获取数据库连接成功");
        return connection;
    }

    /**
     * 关闭数据库资源
     *
     * @param resultSet ResultSet对象
     * @param statement Statement对象
     * @param connection Connection对象
     */
    public static void close(ResultSet resultSet, Statement statement, Connection connection) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
            log.debug("数据库资源已关闭");
        } catch (SQLException e) {
            log.error("关闭数据库资源失败: {}", e.getMessage());
        }
    }

    /**
     * 关闭数据库资源（重载方法，无ResultSet）
     *
     * @param statement Statement对象
     * @param connection Connection对象
     */
    public static void close(Statement statement, Connection connection) {
        close(null, statement, connection);
    }

    /**
     * 测试数据库连接
     *
     * @return 是否连接成功
     */
    public static boolean testConnection() {
        Connection conn = null;
        try {
            conn = getConnection();
            log.info("数据库连接测试成功！");
            return true;
        } catch (SQLException e) {
            log.error("数据库连接测试失败: {}", e.getMessage());
            return false;
        } finally {
            close(null, null, conn);
        }
    }

    /**
     * 示例：查询数据
     *
     * @param sql SQL查询语句
     * @param params 参数数组
     */
    public static void query(String sql, Object... params) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            // 设置参数
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }

            rs = pstmt.executeQuery();

            // 处理结果集
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (rs.next()) {
                StringBuilder row = new StringBuilder();
                for (int i = 1; i <= columnCount; i++) {
                    row.append(metaData.getColumnName(i)).append(": ")
                       .append(rs.getObject(i)).append("\t");
                }
                log.info(row.toString());
            }
        } catch (SQLException e) {
            log.error("查询数据失败: {}", e.getMessage());
        } finally {
            close(rs, pstmt, conn);
        }
    }

    /**
     * 示例：更新数据（INSERT、UPDATE、DELETE）
     *
     * @param sql SQL更新语句
     * @param params 参数数组
     * @return 影响的行数
     */
    public static int update(String sql, Object... params) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            // 设置参数
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }

            int rows = pstmt.executeUpdate();
            log.info("影响行数: {}", rows);
            return rows;
        } catch (SQLException e) {
            log.error("更新数据失败: {}", e.getMessage());
            return 0;
        } finally {
            close(pstmt, conn);
        }
    }

    /**
     * 获取表的完整结构信息
     *
     * @param tableName 表名
     * @return TableInfo对象，包含表的所有列信息
     */
    public static TableInfo getTableInfo(String tableName) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName(tableName);
        
        try {
            conn = getConnection();
            DatabaseMetaData metaData = conn.getMetaData();
            
            // 获取表注释
            ResultSet tableRs = metaData.getTables(conn.getCatalog(), null, tableName, new String[]{"TABLE"});
            if (tableRs.next()) {
                tableInfo.setTableComment(tableRs.getString("REMARKS"));
            }
            tableRs.close();
            
            // 获取列信息
            rs = metaData.getColumns(conn.getCatalog(), null, tableName, null);
            while (rs.next()) {
                ColumnInfo column = new ColumnInfo();
                column.setColumnName(rs.getString("COLUMN_NAME"));
                column.setDataType(rs.getString("TYPE_NAME"));
                column.setColumnSize(rs.getInt("COLUMN_SIZE"));
                column.setDecimalDigits(rs.getInt("DECIMAL_DIGITS"));
                column.setNullable(rs.getInt("NULLABLE") == DatabaseMetaData.columnNullable);
                column.setDefaultValue(rs.getString("COLUMN_DEF"));
                column.setRemarks(rs.getString("REMARKS"));
                column.setAutoIncrement("YES".equals(rs.getString("IS_AUTOINCREMENT")));
                
                tableInfo.addColumn(column);
            }
            rs.close();
            
            // 获取主键信息
            rs = metaData.getPrimaryKeys(conn.getCatalog(), null, tableName);
            while (rs.next()) {
                String pkName = rs.getString("COLUMN_NAME");
                tableInfo.getPrimaryKeys().add(pkName);
                
                // 更新对应列的主键标记
                ColumnInfo column = tableInfo.getColumnByName(pkName);
                if (column != null) {
                    column.setPrimaryKey(true);
                }
            }
            rs.close();
            
            log.info("获取表结构成功: {}", tableInfo.toString());
            return tableInfo;
            
        } catch (SQLException e) {
            log.error("获取表结构失败: {}", e.getMessage());
            return tableInfo;
        } finally {
            close(rs, pstmt, conn);
        }
    }
    
    /**
     * 查询表数据并封装为TableData对象
     *
     * @param tableName 表名
     * @param condition WHERE条件（可选）
     * @param params 条件参数
     * @return TableData对象，包含查询结果
     */
    public static TableData getTableData(String tableName, String condition, Object... params) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        TableData tableData = new TableData();
        tableData.setTableName(tableName);
        
        try {
            conn = getConnection();
            
            // 构建SQL
            String sql = "SELECT * FROM " + tableName;
            if (condition != null && !condition.isEmpty()) {
                sql += " WHERE " + condition;
            }
            
            pstmt = conn.prepareStatement(sql);
            
            // 设置参数
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }
            
            rs = pstmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            // 获取列名
            List<String> columnNames = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }
            tableData.setColumnNames(columnNames);
            
            // 获取数据行
            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), rs.getObject(i));
                }
                tableData.addRow(row);
            }
            
            log.info("查询表数据成功: {}", tableData.toString());
            return tableData;
            
        } catch (SQLException e) {
            log.error("查询表数据失败: {}", e.getMessage());
            return tableData;
        } finally {
            close(rs, pstmt, conn);
        }
    }

    /**
     * 获取数据库中所有表的表名列表
     *
     * @return 表名列表
     */
    public static List<String> getAllTableNames() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        List<String> tableNames = new ArrayList<>();
        
        try {
            conn = getConnection();
            DatabaseMetaData metaData = conn.getMetaData();
            
            // 获取所有表（只包括普通表，不包括视图）
            rs = metaData.getTables(conn.getCatalog(), null, null, new String[]{"TABLE"});
            
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                tableNames.add(tableName);
            }
            
            log.info("获取所有表名成功，共 {} 个表", tableNames.size());
            return tableNames;
            
        } catch (SQLException e) {
            log.error("获取所有表名失败: {}", e.getMessage());
            return tableNames;
        } finally {
            close(rs, pstmt, conn);
        }
    }

    /**
     * 获取数据库中所有表的完整结构信息
     *
     * @return TableInfo列表，每个元素包含一个表的完整结构信息
     */
    public static List<TableInfo> getAllTablesInfo() {
        List<TableInfo> allTablesInfo = new ArrayList<>();
        
        try {
            // 先获取所有表名
            List<String> tableNames = getAllTableNames();
            
            log.info("开始获取 {} 个表的详细结构信息...", tableNames.size());
            
            // 遍历每个表，获取其结构信息
            for (String tableName : tableNames) {
                TableInfo tableInfo = getTableInfo(tableName);
                if (tableInfo != null && tableInfo.getColumnCount() > 0) {
                    allTablesInfo.add(tableInfo);
                }
            }
            
            log.info("获取所有表结构成功，共 {} 个表", allTablesInfo.size());
            return allTablesInfo;
            
        } catch (Exception e) {
            log.error("获取所有表结构失败: {}", e.getMessage());
            return allTablesInfo;
        }
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试数据库连接
        log.info("========== 测试数据库连接 ==========");
        testConnection();

        // 示例：创建表
        log.info("\n========== 创建表示例 ==========");
        String createTableSql = "CREATE TABLE IF NOT EXISTS user (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(50) NOT NULL, " +
                "age INT, " +
                "email VARCHAR(100), " +
                "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
        update(createTableSql);

        // 示例：插入数据
        log.info("\n========== 插入数据 ==========");
        String insertSql = "INSERT INTO user (name, age, email) VALUES (?, ?, ?)";
        update(insertSql, "张三", 25, "zhangsan@example.com");
        update(insertSql, "李四", 30, "lisi@example.com");

        // 示例：查询数据
        log.info("\n========== 查询数据 ==========");
        String selectSql = "SELECT * FROM user";
        query(selectSql);

        // 示例：更新数据
        log.info("\n========== 更新数据 ==========");
        String updateSql = "UPDATE user SET age = ? WHERE name = ?";
        update(updateSql, 26, "张三");

        // 再次查询验证更新
        log.info("\n========== 验证更新 ==========");
        query(selectSql);

        // 示例：删除数据
        log.info("\n========== 删除数据 ==========");
        String deleteSql = "DELETE FROM user WHERE name = ?";
        update(deleteSql, "李四");

        // 最终查询
        log.info("\n========== 最终结果 ==========");
        query(selectSql);
        

    }
}
