package com.test.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HBase 工具类，提供对 HBase 的增删查改及连接测试功能。
 * <p>
 * 使用前需确保 HBase 服务已启动，并在 hbase-site.xml 或代码中正确配置 ZooKeeper 地址。
 * </p>
 */
public class HBaseUtil {

    private static final Logger LOG = LoggerFactory.getLogger(HBaseUtil.class);

    private static Connection connection = null;

    // 默认配置：可根据实际情况修改或通过 hbase-site.xml 加载
    private static final String DEFAULT_ZOOKEEPER_QUORUM = "localhost:2181";
    private static final String DEFAULT_ZOOKEEPER_ZNODE_PARENT = "/hbase";

    /**
     * 静态初始化块：处理 Windows 下 Hadoop home 未设置的问题
     * <p>
     * HBase 客户端在 Windows 上需要 hadoop.home.dir 或 HADOOP_HOME 环境变量，
     * 用于加载 Hadoop 本地库（winutils.exe 等）。
     * 如果未设置，自动创建一个临时目录作为 hadoop.home.dir。
     * </p>
     */
    static {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            String hadoopHome = System.getProperty("hadoop.home.dir");
            if (hadoopHome == null || hadoopHome.trim().isEmpty()) {
                // 检查环境变量 HADOOP_HOME
                String envHadoopHome = System.getenv("HADOOP_HOME");
                if (envHadoopHome != null && !envHadoopHome.trim().isEmpty()) {
                    System.setProperty("hadoop.home.dir", envHadoopHome);
                    LOG.info("使用环境变量 HADOOP_HOME: {}", envHadoopHome);
                } else {
                    // 使用项目目录下的 hadoop 目录作为 fallback
                    String fallbackHome = "D:\\hadoop\\winutils-master\\hadoop-2.7.7";
                    System.setProperty("hadoop.home.dir", fallbackHome);
                    LOG.info("hadoop.home.dir 未设置，使用临时目录: {}", fallbackHome);
                    LOG.warn("建议从 https://github.com/steveloughran/winutils 下载 winutils.exe 并放置在 {}/bin/ 目录下", fallbackHome);
                }
            }
            // 解决 Windows 下权限检查错误
            System.setProperty("HADOOP_HOME", System.getProperty("hadoop.home.dir"));
        }
    }

    /**
     * 私有构造方法，防止实例化
     */
    private HBaseUtil() {
    }

    // ======================== 连接管理 ========================

    /**
     * 初始化 HBase 配置
     *
     * @param quorum     ZooKeeper 地址（格式：host:port），传 null 使用默认值
     * @param znodeParent ZNode 父节点，传 null 使用默认值
     * @return HBase Configuration
     */
    public static Configuration initConfig(String quorum, String znodeParent) {
        Configuration config = HBaseConfiguration.create();
        if (quorum != null && !quorum.trim().isEmpty()) {
            config.set(HConstants.ZOOKEEPER_QUORUM, quorum);
        } else {
            config.set(HConstants.ZOOKEEPER_QUORUM, DEFAULT_ZOOKEEPER_QUORUM);
        }
        if (znodeParent != null && !znodeParent.trim().isEmpty()) {
            config.set(HConstants.ZOOKEEPER_ZNODE_PARENT, znodeParent);
        } else {
            config.set(HConstants.ZOOKEEPER_ZNODE_PARENT, DEFAULT_ZOOKEEPER_ZNODE_PARENT);
        }
        // 连接超时设置（毫秒）
        config.setInt(HConstants.HBASE_CLIENT_OPERATION_TIMEOUT, 30000);
        config.setInt(HConstants.HBASE_CLIENT_SCANNER_TIMEOUT_PERIOD, 30000);

        // 禁用 Hadoop Shell 的本地库检查（Windows 下避免 HADOOP_HOME 报错）
        config.setBoolean("hadoop.security.authentication", false);
        config.set("hadoop.security.authorization", "false");
        return config;
    }

    /**
     * 获取 HBase 连接（单例模式，使用默认配置）
     *
     * @return HBase Connection
     * @throws IOException 连接异常
     */
    public static synchronized Connection getConnection() throws IOException {
        if (connection == null || connection.isClosed()) {
            Configuration config = initConfig(null, null);
            connection = ConnectionFactory.createConnection(config);
            LOG.info("HBase 连接已创建");
        }
        return connection;
    }

    /**
     * 使用自定义配置获取 HBase 连接
     *
     * @param config HBase Configuration
     * @return HBase Connection
     * @throws IOException 连接异常
     */
    public static synchronized Connection getConnection(Configuration config) throws IOException {
        if (connection == null || connection.isClosed()) {
            connection = ConnectionFactory.createConnection(config);
            LOG.info("HBase 连接已创建（自定义配置）");
        }
        return connection;
    }

    /**
     * 使用指定 ZooKeeper 地址获取连接
     *
     * @param quorum ZooKeeper 地址
     * @return HBase Connection
     * @throws IOException 连接异常
     */
    public static synchronized Connection getConnection(String quorum) throws IOException {
        Configuration config = initConfig(quorum, null);
        return getConnection(config);
    }

    /**
     * 关闭 HBase 连接
     */
    public static synchronized void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                LOG.info("HBase 连接已关闭");
            } catch (IOException e) {
                LOG.error("关闭 HBase 连接失败", e);
            } finally {
                connection = null;
            }
        }
    }

    /**
     * 连接测试
     *
     * @return true 表示连接成功，false 表示连接失败
     */
    public static boolean testConnection() {
        return testConnection(null, null);
    }

    /**
     * 连接测试（指定 ZooKeeper 地址）
     *
     * @param quorum ZooKeeper 地址
     * @return true 表示连接成功，false 表示连接失败
     */
    public static boolean testConnection(String quorum) {
        return testConnection(quorum, null);
    }

    /**
     * 连接测试（指定 ZooKeeper 地址和 ZNode 父节点）
     *
     * @param quorum      ZooKeeper 地址
     * @param znodeParent ZNode 父节点
     * @return true 表示连接成功，false 表示连接失败
     */
    public static boolean testConnection(String quorum, String znodeParent) {
        try {
            Configuration config = initConfig(quorum, znodeParent);
            Connection testConn = ConnectionFactory.createConnection(config);
            // 通过获取表名列表来验证连接是否可用
            Admin admin = testConn.getAdmin();
            TableName[] tableNames = admin.listTableNames();
            LOG.info("HBase 连接测试成功，当前共有 {} 张表", tableNames.length);
            for (TableName tn : tableNames) {
                LOG.debug("  表: {}", tn.getNameAsString());
            }
            admin.close();
            testConn.close();
            return true;
        } catch (Exception e) {
            LOG.error("HBase 连接测试失败: {}", e.getMessage(), e);
            return false;
        }
    }

    // ======================== 表操作 ========================

    /**
     * 创建表（单列族）
     *
     * @param tableName    表名
     * @param columnFamily 列族名
     * @return true 表示创建成功
     * @throws IOException 操作异常
     */
    public static boolean createTable(String tableName, String columnFamily) throws IOException {
        return createTable(tableName, new String[]{columnFamily});
    }

    /**
     * 创建表（多列族）
     *
     * @param tableName     表名
     * @param columnFamilies 列族名数组
     * @return true 表示创建成功
     * @throws IOException 操作异常
     */
    public static boolean createTable(String tableName, String[] columnFamilies) throws IOException {
        Connection conn = getConnection();
        Admin admin = conn.getAdmin();
        TableName tn = TableName.valueOf(tableName);

        if (admin.tableExists(tn)) {
            LOG.warn("表 {} 已存在", tableName);
            admin.close();
            return false;
        }

        TableDescriptorBuilder builder = TableDescriptorBuilder.newBuilder(tn);
        for (String cf : columnFamilies) {
            ColumnFamilyDescriptorBuilder cfBuilder =
                    ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(cf));
            builder.setColumnFamily(cfBuilder.build());
        }
        admin.createTable(builder.build());
        LOG.info("表 {} 创建成功，列族: {}", tableName, String.join(", ", columnFamilies));
        admin.close();
        return true;
    }

    /**
     * 删除表
     *
     * @param tableName 表名
     * @return true 表示删除成功
     * @throws IOException 操作异常
     */
    public static boolean deleteTable(String tableName) throws IOException {
        Connection conn = getConnection();
        Admin admin = conn.getAdmin();
        TableName tn = TableName.valueOf(tableName);

        if (!admin.tableExists(tn)) {
            LOG.warn("表 {} 不存在", tableName);
            admin.close();
            return false;
        }

        // 先禁用表，再删除
        if (!admin.isTableDisabled(tn)) {
            admin.disableTable(tn);
        }
        admin.deleteTable(tn);
        LOG.info("表 {} 删除成功", tableName);
        admin.close();
        return true;
    }

    /**
     * 判断表是否存在
     *
     * @param tableName 表名
     * @return true 表示表存在
     * @throws IOException 操作异常
     */
    public static boolean tableExists(String tableName) throws IOException {
        Connection conn = getConnection();
        Admin admin = conn.getAdmin();
        boolean exists = admin.tableExists(TableName.valueOf(tableName));
        admin.close();
        return exists;
    }

    // ======================== 新增 / 修改数据（Put） ========================

    /**
     * 插入或更新一行数据
     *
     * @param tableName  表名
     * @param rowKey     行键
     * @param family     列族
     * @param qualifier  列限定符
     * @param value      值
     * @throws IOException 操作异常
     */
    public static void putData(String tableName, String rowKey, String family,
                               String qualifier, String value) throws IOException {
        Connection conn = getConnection();
        try (Table table = conn.getTable(TableName.valueOf(tableName))) {
            Put put = new Put(Bytes.toBytes(rowKey));
            put.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(value));
            table.put(put);
            LOG.debug("数据写入成功: table={}, rowKey={}, {}=>{}:{} => {}",
                    tableName, rowKey, family, qualifier, value);
        }
    }

    /**
     * 插入或更新一行数据（多列）
     *
     * @param tableName 表名
     * @param rowKey    行键
     * @param family    列族
     * @param columns   列数据 Map（列限定符 -> 值）
     * @throws IOException 操作异常
     */
    public static void putRow(String tableName, String rowKey, String family,
                              Map<String, String> columns) throws IOException {
        Connection conn = getConnection();
        try (Table table = conn.getTable(TableName.valueOf(tableName))) {
            Put put = new Put(Bytes.toBytes(rowKey));
            byte[] familyBytes = Bytes.toBytes(family);
            for (Map.Entry<String, String> entry : columns.entrySet()) {
                put.addColumn(familyBytes, Bytes.toBytes(entry.getKey()), Bytes.toBytes(entry.getValue()));
            }
            table.put(put);
            LOG.debug("批量数据写入成功: table={}, rowKey={}, 共 {} 列", tableName, rowKey, columns.size());
        }
    }

    /**
     * 批量插入多行数据
     *
     * @param tableName 表名
     * @param puts      Put 对象列表
     * @throws IOException 操作异常
     */
    public static void putBatch(String tableName, List<Put> puts) throws IOException {
        Connection conn = getConnection();
        try (Table table = conn.getTable(TableName.valueOf(tableName))) {
            table.put(puts);
            LOG.debug("批量写入 {} 行数据成功", puts.size());
        }
    }

    // ======================== 查询数据（Get） ========================

    /**
     * 根据行键查询一行数据
     *
     * @param tableName 表名
     * @param rowKey    行键
     * @return Result 对象，包含所有列族数据
     * @throws IOException 操作异常
     */
    public static Result getRow(String tableName, String rowKey) throws IOException {
        Connection conn = getConnection();
        try (Table table = conn.getTable(TableName.valueOf(tableName))) {
            Get get = new Get(Bytes.toBytes(rowKey));
            return table.get(get);
        }
    }

    /**
     * 根据行键查询指定列族的数据
     *
     * @param tableName 表名
     * @param rowKey    行键
     * @param family    列族
     * @return Result 对象
     * @throws IOException 操作异常
     */
    public static Result getRow(String tableName, String rowKey, String family) throws IOException {
        Connection conn = getConnection();
        try (Table table = conn.getTable(TableName.valueOf(tableName))) {
            Get get = new Get(Bytes.toBytes(rowKey));
            get.addFamily(Bytes.toBytes(family));
            return table.get(get);
        }
    }

    /**
     * 根据行键查询指定列的数据
     *
     * @param tableName 表名
     * @param rowKey    行键
     * @param family    列族
     * @param qualifier 列限定符
     * @return 单元格的字符串值，若不存在返回 null
     * @throws IOException 操作异常
     */
    public static String getCell(String tableName, String rowKey, String family,
                                 String qualifier) throws IOException {
        Connection conn = getConnection();
        try (Table table = conn.getTable(TableName.valueOf(tableName))) {
            Get get = new Get(Bytes.toBytes(rowKey));
            get.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier));
            Result result = table.get(get);
            Cell cell = result.getColumnLatestCell(Bytes.toBytes(family), Bytes.toBytes(qualifier));
            if (cell == null) {
                return null;
            }
            return Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
        }
    }

    // ======================== 扫描数据（Scan） ========================

    /**
     * 全表扫描
     *
     * @param tableName 表名
     * @return ResultScanner
     * @throws IOException 操作异常
     */
    public static ResultScanner scanTable(String tableName) throws IOException {
        Connection conn = getConnection();
        Table table = conn.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        return table.getScanner(scan);
    }

    /**
     * 带过滤器的扫描
     *
     * @param tableName 表名
     * @param filter    HBase Filter
     * @return ResultScanner
     * @throws IOException 操作异常
     */
    public static ResultScanner scanWithFilter(String tableName, Filter filter) throws IOException {
        Connection conn = getConnection();
        Table table = conn.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        scan.setFilter(filter);
        return table.getScanner(scan);
    }

    /**
     * 范围扫描（指定起始和结束行键）
     *
     * @param tableName  表名
     * @param startRow   起始行键（包含）
     * @param stopRow    结束行键（不包含）
     * @return ResultScanner
     * @throws IOException 操作异常
     */
    public static ResultScanner scanByRange(String tableName, String startRow,
                                            String stopRow) throws IOException {
        Connection conn = getConnection();
        Table table = conn.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        scan.withStartRow(Bytes.toBytes(startRow));
        scan.withStopRow(Bytes.toBytes(stopRow));
        return table.getScanner(scan);
    }

    /**
     * 将 ResultScanner 转换为可读的字符串列表（用于展示）
     *
     * @param scanner ResultScanner
     * @return 每行数据的可读信息列表
     * @throws IOException 操作异常
     */
    public static List<String> resultScannerToStringList(ResultScanner scanner) throws IOException {
        List<String> resultList = new ArrayList<>();
        for (Result result : scanner) {
            StringBuilder sb = new StringBuilder();
            sb.append("rowKey=").append(Bytes.toString(result.getRow()));
            for (Cell cell : result.listCells()) {
                String family = Bytes.toString(cell.getFamilyArray(), cell.getFamilyOffset(), cell.getFamilyLength());
                String qualifier = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
                String value = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                sb.append(", ").append(family).append(":").append(qualifier).append("=").append(value);
            }
            resultList.add(sb.toString());
        }
        return resultList;
    }

    // ======================== 删除数据（Delete） ========================

    /**
     * 删除一行数据
     *
     * @param tableName 表名
     * @param rowKey    行键
     * @throws IOException 操作异常
     */
    public static void deleteRow(String tableName, String rowKey) throws IOException {
        Connection conn = getConnection();
        try (Table table = conn.getTable(TableName.valueOf(tableName))) {
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            table.delete(delete);
            LOG.debug("删除行成功: table={}, rowKey={}", tableName, rowKey);
        }
    }

    /**
     * 删除指定行中某个列族的所有列
     *
     * @param tableName 表名
     * @param rowKey    行键
     * @param family    列族
     * @throws IOException 操作异常
     */
    public static void deleteFamily(String tableName, String rowKey, String family) throws IOException {
        Connection conn = getConnection();
        try (Table table = conn.getTable(TableName.valueOf(tableName))) {
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            delete.addFamily(Bytes.toBytes(family));
            table.delete(delete);
            LOG.debug("删除列族成功: table={}, rowKey={}, family={}", tableName, rowKey, family);
        }
    }

    /**
     * 删除指定行中某一列的特定版本
     *
     * @param tableName 表名
     * @param rowKey    行键
     * @param family    列族
     * @param qualifier 列限定符
     * @throws IOException 操作异常
     */
    public static void deleteQualifier(String tableName, String rowKey, String family,
                                       String qualifier) throws IOException {
        Connection conn = getConnection();
        try (Table table = conn.getTable(TableName.valueOf(tableName))) {
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            delete.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier));
            table.delete(delete);
            LOG.debug("删除列成功: table={}, rowKey={}, {}=>{}",
                    tableName, rowKey, family, qualifier);
        }
    }

    /**
     * 批量删除多行数据
     *
     * @param tableName 表名
     * @param rowKeys   行键列表
     * @throws IOException 操作异常
     */
    public static void deleteBatch(String tableName, List<String> rowKeys) throws IOException {
        Connection conn = getConnection();
        try (Table table = conn.getTable(TableName.valueOf(tableName))) {
            List<Delete> deletes = new ArrayList<>();
            for (String rowKey : rowKeys) {
                deletes.add(new Delete(Bytes.toBytes(rowKey)));
            }
            table.delete(deletes);
            LOG.debug("批量删除 {} 行成功", rowKeys.size());
        }
    }

    /**
     * 释放 ResultScanner 资源
     *
     * @param scanner ResultScanner
     */
    public static void closeScanner(ResultScanner scanner) {
        if (scanner != null) {
            scanner.close();
        }
    }
}