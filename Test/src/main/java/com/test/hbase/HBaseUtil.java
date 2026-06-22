package com.test.hbase;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.*;

/**
 * HBase数据库操作工具类
 * 提供常用的HBase CRUD操作、表管理等功能
 */
@Slf4j
public class HBaseUtil {

    private static final String ZOOKEEPER_QUORUM = "localhost:2181";
    private static final String CLIENT_PORT = "2181";

    /**
     * Hadoop 主目录，Windows 环境下必须设置，指向包含 winutils.exe 和 hadoop.dll 的本地路径
     */
    private static final String HADOOP_HOME_DIR = "D:/hadoop";

    private static Configuration configuration;
    private static Connection connection;
    private static Admin admin;

    // 静态初始化块，加载HBase配置
    static {
        try {
            // Windows 环境下必须设置 hadoop.home.dir，否则会报错：HADOOP_HOME and hadoop.home.dir are unset
            if (System.getProperty("hadoop.home.dir") == null) {
                System.setProperty("hadoop.home.dir", HADOOP_HOME_DIR);
                log.info("设置 hadoop.home.dir = {}", HADOOP_HOME_DIR);
            }

            configuration = HBaseConfiguration.create();
            configuration.set("hbase.zookeeper.quorum", ZOOKEEPER_QUORUM);
            configuration.set("hbase.zookeeper.property.clientPort", CLIENT_PORT);

            connection = ConnectionFactory.createConnection(configuration);
            admin = connection.getAdmin();

            log.info("HBase连接初始化成功");
        } catch (IOException e) {
            log.error("HBase连接初始化失败: {}", e.getMessage());
            throw new RuntimeException("HBase连接初始化失败", e);
        }
    }

    /**
     * 获取Configuration对象
     *
     * @return Configuration对象
     */
    public static Configuration getConfiguration() {
        return configuration;
    }

    /**
     * 获取Connection对象
     *
     * @return Connection对象
     */
    public static Connection getConnection() {
        return connection;
    }

    /**
     * 获取Admin对象
     *
     * @return Admin对象
     */
    public static Admin getAdmin() {
        return admin;
    }

    /**
     * 判断表是否存在
     *
     * @param tableName 表名
     * @return 表是否存在
     */
    public static boolean isTableExists(String tableName) {
        try {
            TableName table = TableName.valueOf(tableName);
            boolean exists = admin.tableExists(table);
            log.debug("表 {} 存在状态: {}", tableName, exists);
            return exists;
        } catch (IOException e) {
            log.error("检查表是否存在失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 创建表（单列族）
     *
     * @param tableName 表名
     * @param columnFamily 列族名
     * @return 是否创建成功
     */
    public static boolean createTable(String tableName, String columnFamily) {
        return createTable(tableName, new String[]{columnFamily});
    }

    /**
     * 创建表（多列族）
     *
     * @param tableName 表名
     * @param columnFamilies 列族名数组
     * @return 是否创建成功
     */
    public static boolean createTable(String tableName, String[] columnFamilies) {
        try {
            TableName table = TableName.valueOf(tableName);

            if (admin.tableExists(table)) {
                log.warn("表 {} 已存在", tableName);
                return false;
            }

            TableDescriptorBuilder tableDescriptor = TableDescriptorBuilder.newBuilder(table);

            for (String cf : columnFamilies) {
                ColumnFamilyDescriptor columnFamily = ColumnFamilyDescriptorBuilder.newBuilder(
                        Bytes.toBytes(cf)
                ).build();
                tableDescriptor.setColumnFamily(columnFamily);
            }

            admin.createTable(tableDescriptor.build());
            log.info("创建表成功: {}", tableName);
            return true;
        } catch (IOException e) {
            log.error("创建表失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 删除表
     *
     * @param tableName 表名
     * @return 是否删除成功
     */
    public static boolean deleteTable(String tableName) {
        try {
            TableName table = TableName.valueOf(tableName);

            if (!admin.tableExists(table)) {
                log.warn("表 {} 不存在", tableName);
                return false;
            }

            if (admin.isTableEnabled(table)) {
                admin.disableTable(table);
                log.debug("表 {} 已禁用", tableName);
            }

            admin.deleteTable(table);
            log.info("删除表成功: {}", tableName);
            return true;
        } catch (IOException e) {
            log.error("删除表失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 禁用表
     *
     * @param tableName 表名
     * @return 是否禁用成功
     */
    public static boolean disableTable(String tableName) {
        try {
            TableName table = TableName.valueOf(tableName);

            if (!admin.tableExists(table)) {
                log.warn("表 {} 不存在", tableName);
                return false;
            }

            if (!admin.isTableDisabled(table)) {
                admin.disableTable(table);
            }

            log.info("禁用表成功: {}", tableName);
            return true;
        } catch (IOException e) {
            log.error("禁用表失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 启用表
     *
     * @param tableName 表名
     * @return 是否启用成功
     */
    public static boolean enableTable(String tableName) {
        try {
            TableName table = TableName.valueOf(tableName);

            if (!admin.tableExists(table)) {
                log.warn("表 {} 不存在", tableName);
                return false;
            }

            if (!admin.isTableEnabled(table)) {
                admin.enableTable(table);
            }

            log.info("启用表成功: {}", tableName);
            return true;
        } catch (IOException e) {
            log.error("启用表失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 获取所有表名列表
     *
     * @return 表名列表
     */
    public static List<String> getAllTableNames() {
        List<String> tableNames = new ArrayList<>();
        try {
            TableName[] tables = admin.listTableNames();
            for (TableName table : tables) {
                tableNames.add(table.getNameAsString());
            }
            log.info("获取所有表名成功，共 {} 个表", tableNames.size());
            return tableNames;
        } catch (IOException e) {
            log.error("获取所有表名失败: {}", e.getMessage());
            return tableNames;
        }
    }

    /**
     * 插入或更新数据（单条）
     *
     * @param tableName 表名
     * @param rowKey 行键
     * @param columnFamily 列族
     * @param qualifier 列限定符
     * @param value 值
     * @return 是否成功
     */
    public static boolean putData(String tableName, String rowKey, String columnFamily,
                                  String qualifier, String value) {
        Table table = null;
        try {
            table = connection.getTable(TableName.valueOf(tableName));
            Put put = new Put(Bytes.toBytes(rowKey));
            put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(qualifier), Bytes.toBytes(value));

            table.put(put);
            log.debug("插入数据成功: table={}, rowKey={}, cf={}, qualifier={}",
                    tableName, rowKey, columnFamily, qualifier);
            return true;
        } catch (IOException e) {
            log.error("插入数据失败: {}", e.getMessage());
            return false;
        } finally {
            closeTable(table);
        }
    }

    /**
     * 批量插入数据
     *
     * @param tableName 表名
     * @param puts Put对象列表
     * @return 是否成功
     */
    public static boolean putBatch(String tableName, List<Put> puts) {
        Table table = null;
        try {
            table = connection.getTable(TableName.valueOf(tableName));
            table.put(puts);
            log.info("批量插入数据成功: table={}, 数量={}", tableName, puts.size());
            return true;
        } catch (IOException e) {
            log.error("批量插入数据失败: {}", e.getMessage());
            return false;
        } finally {
            closeTable(table);
        }
    }

    /**
     * 根据rowKey查询数据（整行）
     *
     * @param tableName 表名
     * @param rowKey 行键
     * @return 结果Map，key为"cf:qualifier"，value为值
     */
    public static Map<String, String> getData(String tableName, String rowKey) {
        Table table = null;
        try {
            table = connection.getTable(TableName.valueOf(tableName));
            Get get = new Get(Bytes.toBytes(rowKey));
            Result result = table.get(get);

            Map<String, String> resultMap = new HashMap<>();
            for (Cell cell : result.listCells()) {
                String family = Bytes.toString(CellUtil.cloneFamily(cell));
                String qualifier = Bytes.toString(CellUtil.cloneQualifier(cell));
                String value = Bytes.toString(CellUtil.cloneValue(cell));
                resultMap.put(family + ":" + qualifier, value);
            }

            log.debug("查询数据成功: table={}, rowKey={}", tableName, rowKey);
            return resultMap;
        } catch (IOException e) {
            log.error("查询数据失败: {}", e.getMessage());
            return Collections.emptyMap();
        } finally {
            closeTable(table);
        }
    }

    /**
     * 根据rowKey和列族查询数据
     *
     * @param tableName 表名
     * @param rowKey 行键
     * @param columnFamily 列族
     * @return 结果Map，key为qualifier，value为值
     */
    public static Map<String, String> getDataByFamily(String tableName, String rowKey, String columnFamily) {
        Table table = null;
        try {
            table = connection.getTable(TableName.valueOf(tableName));
            Get get = new Get(Bytes.toBytes(rowKey));
            get.addFamily(Bytes.toBytes(columnFamily));
            Result result = table.get(get);

            Map<String, String> resultMap = new HashMap<>();
            for (Cell cell : result.listCells()) {
                String qualifier = Bytes.toString(CellUtil.cloneQualifier(cell));
                String value = Bytes.toString(CellUtil.cloneValue(cell));
                resultMap.put(qualifier, value);
            }

            log.debug("查询数据成功: table={}, rowKey={}, cf={}", tableName, rowKey, columnFamily);
            return resultMap;
        } catch (IOException e) {
            log.error("查询数据失败: {}", e.getMessage());
            return Collections.emptyMap();
        } finally {
            closeTable(table);
        }
    }

    /**
     * 根据rowKey、列族、列限定符查询数据
     *
     * @param tableName 表名
     * @param rowKey 行键
     * @param columnFamily 列族
     * @param qualifier 列限定符
     * @return 值
     */
    public static String getDataByColumn(String tableName, String rowKey, String columnFamily, String qualifier) {
        Table table = null;
        try {
            table = connection.getTable(TableName.valueOf(tableName));
            Get get = new Get(Bytes.toBytes(rowKey));
            get.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(qualifier));
            Result result = table.get(get);

            Cell cell = result.getColumnLatestCell(Bytes.toBytes(columnFamily), Bytes.toBytes(qualifier));
            if (cell != null) {
                String value = Bytes.toString(CellUtil.cloneValue(cell));
                log.debug("查询数据成功: table={}, rowKey={}, cf={}, qualifier={}",
                        tableName, rowKey, columnFamily, qualifier);
                return value;
            }

            return null;
        } catch (IOException e) {
            log.error("查询数据失败: {}", e.getMessage());
            return null;
        } finally {
            closeTable(table);
        }
    }

    /**
     * 扫描全表数据
     *
     * @param tableName 表名
     * @return 结果List，每个元素是一个Map，包含rowKey和数据
     */
    public static List<Map<String, Object>> scanTable(String tableName) {
        Table table = null;
        ResultScanner scanner = null;
        try {
            table = connection.getTable(TableName.valueOf(tableName));
            Scan scan = new Scan();
            scanner = table.getScanner(scan);

            List<Map<String, Object>> resultList = new ArrayList<>();
            for (Result result : scanner) {
                Map<String, Object> rowMap = new LinkedHashMap<>();
                rowMap.put("rowKey", Bytes.toString(result.getRow()));

                for (Cell cell : result.listCells()) {
                    String family = Bytes.toString(CellUtil.cloneFamily(cell));
                    String qualifier = Bytes.toString(CellUtil.cloneQualifier(cell));
                    String value = Bytes.toString(CellUtil.cloneValue(cell));
                    rowMap.put(family + ":" + qualifier, value);
                }

                resultList.add(rowMap);
            }

            log.info("扫描表成功: table={}, 行数={}", tableName, resultList.size());
            return resultList;
        } catch (IOException e) {
            log.error("扫描表失败: {}", e.getMessage());
            return Collections.emptyList();
        } finally {
            closeScanner(scanner);
            closeTable(table);
        }
    }

    /**
     * 根据前缀扫描数据
     *
     * @param tableName 表名
     * @param rowKeyPrefix 行键前缀
     * @return 结果List
     */
    public static List<Map<String, Object>> scanByPrefix(String tableName, String rowKeyPrefix) {
        Table table = null;
        ResultScanner scanner = null;
        try {
            table = connection.getTable(TableName.valueOf(tableName));
            Scan scan = new Scan();
            Filter filter = new PrefixFilter(Bytes.toBytes(rowKeyPrefix));
            scan.setFilter(filter);
            scanner = table.getScanner(scan);

            List<Map<String, Object>> resultList = new ArrayList<>();
            for (Result result : scanner) {
                Map<String, Object> rowMap = new LinkedHashMap<>();
                rowMap.put("rowKey", Bytes.toString(result.getRow()));

                for (Cell cell : result.listCells()) {
                    String family = Bytes.toString(CellUtil.cloneFamily(cell));
                    String qualifier = Bytes.toString(CellUtil.cloneQualifier(cell));
                    String value = Bytes.toString(CellUtil.cloneValue(cell));
                    rowMap.put(family + ":" + qualifier, value);
                }

                resultList.add(rowMap);
            }

            log.info("按前缀扫描成功: table={}, prefix={}, 行数={}", tableName, rowKeyPrefix, resultList.size());
            return resultList;
        } catch (IOException e) {
            log.error("按前缀扫描失败: {}", e.getMessage());
            return Collections.emptyList();
        } finally {
            closeScanner(scanner);
            closeTable(table);
        }
    }

    /**
     * 根据rowKey范围扫描数据
     *
     * @param tableName 表名
     * @param startRow 起始行键（包含）
     * @param stopRow 结束行键（不包含）
     * @return 结果List
     */
    public static List<Map<String, Object>> scanByRange(String tableName, String startRow, String stopRow) {
        Table table = null;
        ResultScanner scanner = null;
        try {
            table = connection.getTable(TableName.valueOf(tableName));
            Scan scan = new Scan();
            scan.withStartRow(Bytes.toBytes(startRow));
            scan.withStopRow(Bytes.toBytes(stopRow));
            scanner = table.getScanner(scan);

            List<Map<String, Object>> resultList = new ArrayList<>();
            for (Result result : scanner) {
                Map<String, Object> rowMap = new LinkedHashMap<>();
                rowMap.put("rowKey", Bytes.toString(result.getRow()));

                for (Cell cell : result.listCells()) {
                    String family = Bytes.toString(CellUtil.cloneFamily(cell));
                    String qualifier = Bytes.toString(CellUtil.cloneQualifier(cell));
                    String value = Bytes.toString(CellUtil.cloneValue(cell));
                    rowMap.put(family + ":" + qualifier, value);
                }

                resultList.add(rowMap);
            }

            log.info("按范围扫描成功: table={}, start={}, stop={}, 行数={}",
                    tableName, startRow, stopRow, resultList.size());
            return resultList;
        } catch (IOException e) {
            log.error("按范围扫描失败: {}", e.getMessage());
            return Collections.emptyList();
        } finally {
            closeScanner(scanner);
            closeTable(table);
        }
    }

    /**
     * 删除数据（整行）
     *
     * @param tableName 表名
     * @param rowKey 行键
     * @return 是否成功
     */
    public static boolean deleteRow(String tableName, String rowKey) {
        Table table = null;
        try {
            table = connection.getTable(TableName.valueOf(tableName));
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            table.delete(delete);

            log.info("删除行成功: table={}, rowKey={}", tableName, rowKey);
            return true;
        } catch (IOException e) {
            log.error("删除行失败: {}", e.getMessage());
            return false;
        } finally {
            closeTable(table);
        }
    }

    /**
     * 删除指定列的数据
     *
     * @param tableName 表名
     * @param rowKey 行键
     * @param columnFamily 列族
     * @param qualifier 列限定符
     * @return 是否成功
     */
    public static boolean deleteColumn(String tableName, String rowKey, String columnFamily, String qualifier) {
        Table table = null;
        try {
            table = connection.getTable(TableName.valueOf(tableName));
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            delete.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(qualifier));
            table.delete(delete);

            log.info("删除列成功: table={}, rowKey={}, cf={}, qualifier={}",
                    tableName, rowKey, columnFamily, qualifier);
            return true;
        } catch (IOException e) {
            log.error("删除列失败: {}", e.getMessage());
            return false;
        } finally {
            closeTable(table);
        }
    }

    /**
     * 批量删除数据
     *
     * @param tableName 表名
     * @param rowKeys 行键列表
     * @return 是否成功
     */
    public static boolean deleteBatch(String tableName, List<String> rowKeys) {
        Table table = null;
        try {
            table = connection.getTable(TableName.valueOf(tableName));
            List<Delete> deletes = new ArrayList<>();

            for (String rowKey : rowKeys) {
                deletes.add(new Delete(Bytes.toBytes(rowKey)));
            }

            table.delete(deletes);
            log.info("批量删除成功: table={}, 数量={}", tableName, deletes.size());
            return true;
        } catch (IOException e) {
            log.error("批量删除失败: {}", e.getMessage());
            return false;
        } finally {
            closeTable(table);
        }
    }

    /**
     * 关闭Table资源
     *
     * @param table Table对象
     */
    private static void closeTable(Table table) {
        if (table != null) {
            try {
                table.close();
            } catch (IOException e) {
                log.error("关闭Table失败: {}", e.getMessage());
            }
        }
    }

    /**
     * 关闭ResultScanner资源
     *
     * @param scanner ResultScanner对象
     */
    private static void closeScanner(ResultScanner scanner) {
        if (scanner != null) {
            scanner.close();
        }
    }

    /**
     * 关闭所有连接资源
     */
    public static void close() {
        try {
            if (admin != null) {
                admin.close();
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
            log.info("HBase连接已关闭");
        } catch (IOException e) {
            log.error("关闭HBase连接失败: {}", e.getMessage());
        }
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        log.info("========== 测试HBase连接 ==========");

        // 测试：列出所有表
        log.info("\n========== 列出所有表 ==========");
        List<String> tables = getAllTableNames();
        log.info("当前HBase中的表: {}", tables);

        // 测试：创建表
        log.info("\n========== 创建表 ==========");
        String tableName = "test_user";
        String[] columnFamilies = {"info", "address"};
        createTable(tableName, columnFamilies);

        // 测试：检查表是否存在
        log.info("\n========== 检查表是否存在 ==========");
        boolean exists = isTableExists(tableName);
        log.info("表 {} 是否存在: {}", tableName, exists);

        // 测试：插入数据
        log.info("\n========== 插入数据 ==========");
        putData(tableName, "user001", "info", "name", "张三");
        putData(tableName, "user001", "info", "age", "25");
        putData(tableName, "user001", "info", "email", "zhangsan@example.com");
        putData(tableName, "user001", "address", "city", "北京");
        putData(tableName, "user001", "address", "street", "朝阳区");

        putData(tableName, "user002", "info", "name", "李四");
        putData(tableName, "user002", "info", "age", "30");
        putData(tableName, "user002", "info", "email", "lisi@example.com");
        putData(tableName, "user002", "address", "city", "上海");
        putData(tableName, "user002", "address", "street", "浦东新区");

        // 测试：查询单条数据
        log.info("\n========== 查询单条数据 ==========");
        Map<String, String> userData = getData(tableName, "user001");
        log.info("user001的数据: {}", userData);

        // 测试：查询指定列族
        log.info("\n========== 查询指定列族 ==========");
        Map<String, String> infoData = getDataByFamily(tableName, "user001", "info");
        log.info("user001的info列族数据: {}", infoData);

        // 测试：查询指定列
        log.info("\n========== 查询指定列 ==========");
        String name = getDataByColumn(tableName, "user001", "info", "name");
        log.info("user001的姓名: {}", name);

        // 测试：扫描全表
        log.info("\n========== 扫描全表 ==========");
        List<Map<String, Object>> allData = scanTable(tableName);
        for (Map<String, Object> row : allData) {
            log.info("行数据: {}", row);
        }

        // 测试：按前缀扫描
        log.info("\n========== 按前缀扫描 ==========");
        List<Map<String, Object>> prefixData = scanByPrefix(tableName, "user");
        log.info("按前缀'user'扫描结果，共 {} 行", prefixData.size());

        // 测试：按范围扫描
        log.info("\n========== 按范围扫描 ==========");
        List<Map<String, Object>> rangeData = scanByRange(tableName, "user001", "user003");
        log.info("按范围扫描结果，共 {} 行", rangeData.size());

        // 测试：删除指定列
        log.info("\n========== 删除指定列 ==========");
        deleteColumn(tableName, "user001", "info", "email");
        Map<String, String> afterDelete = getData(tableName, "user001");
        log.info("删除email后的数据: {}", afterDelete);

        // 测试：删除整行
        log.info("\n========== 删除整行 ==========");
        deleteRow(tableName, "user002");
        List<Map<String, Object>> afterDeleteRow = scanTable(tableName);
        log.info("删除user002后的数据，共 {} 行", afterDeleteRow.size());

        // 测试：删除表
        log.info("\n========== 删除表 ==========");
        deleteTable(tableName);
        log.info("表 {} 已删除", tableName);

        // 关闭连接
        close();
    }
}
