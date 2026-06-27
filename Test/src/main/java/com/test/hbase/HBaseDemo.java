package com.test.hbase;

import com.test.hbase.HBaseUtil;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HBaseUtil 使用示例 / 快速测试入口类。
 * <p>
 * 运行前请确保 HBase 服务已启动，ZooKeeper 默认连接 localhost:2181。
 * 可在 VM options 中指定 ZooKeeper 地址：-Dzk.quorum=myhost:2181
 * </p>
 * <p>
 * Windows 下需设置 hadoop.home.dir：
 * 方式1（推荐）：从 https://github.com/steveloughran/winutils 下载对应版本的 winutils.exe
 *              放到项目目录 target/hadoop-tmp/bin/ 下即可自动加载
 * 方式2：在 VM options 中添加 -Dhadoop.home.dir=你的路径
 * 方式3：设置环境变量 HADOOP_HOME
 * </p>
 */
public class HBaseDemo {

    private static final Logger LOG = LoggerFactory.getLogger(HBaseDemo.class);

    // 可通过 -Dzk.quorum=host:port 指定 ZooKeeper 地址
    private static final String ZK_QUORUM = System.getProperty("zk.quorum", "localhost:2181");

    public static void main(String[] args) {
        LOG.info("===== HBase 工具类功能演示 =====");
        LOG.info("ZooKeeper: {}", ZK_QUORUM);


        // 0. 环境诊断
        diagnoseEnvironment();

        // 1. 连接测试
        testConnection();

        // 2. 完整 CRUD 流程
        demoCrud();
    }


    /**
     * 环境诊断：检查可能阻止连接的问题
     */
    private static void diagnoseEnvironment() {
        LOG.info("--- 0. 环境诊断 ---");

        // 检查 hosts 文件是否能解析常见的主机名
        String[] hostnamesToCheck = {"base-server", "localhost", System.getProperty("user.name") + "-server"};
        for (String hostname : hostnamesToCheck) {
            try {
                java.net.InetAddress addr = java.net.InetAddress.getByName(hostname);
                LOG.info("  主机名 '{}' 解析为: {} ({})", hostname, addr.getHostAddress(), addr.getCanonicalHostName());
            } catch (java.net.UnknownHostException e) {
                LOG.warn("  主机名 '{}' ❌ 无法解析！", hostname);
                if ("base-server".equals(hostname)) {
                    LOG.warn("    └── 请在 hosts 文件中添加一行: 127.0.0.1 base-server");
                    LOG.warn("    └── 文件位置: C:\\Windows\\System32\\drivers\\etc\\hosts");
                }
            }
        }

        // 检查 HADOOP_HOME
        String hadoopHome = System.getProperty("hadoop.home.dir");
        LOG.info("  hadoop.home.dir = {}", hadoopHome);
        String envHadoop = System.getenv("HADOOP_HOME");
        LOG.info("  HADOOP_HOME env = {}", envHadoop);
    }


    /**
     * 连接测试
     */
    private static void testConnection() {
        LOG.info("--- 1. 连接测试 ---");
        boolean connected = HBaseUtil.testConnection(ZK_QUORUM);
        if (connected) {
            LOG.info("✅ HBase 连接成功！");
        } else {
            LOG.error("❌ HBase 连接失败，请检查 HBase 服务是否启动");
        }
    }

    /**
     * CRUD 流程演示
     */
    private static void demoCrud() {
        LOG.info("--- 2. CRUD 操作演示 ---");

        String tableName = "hbase_demo_user";
        String family = "info";

        try {
            // ===== Create =====
            LOG.info("[Create] 创建表: {}", tableName);
            if (HBaseUtil.tableExists(tableName)) {
                HBaseUtil.deleteTable(tableName);
            }
            HBaseUtil.createTable(tableName, family);
            LOG.info("✅ 表创建成功");

            // ===== 写入单列 =====
            LOG.info("[Put] 写入数据");
            HBaseUtil.putData(tableName, "user001", family, "name", "张三");
            HBaseUtil.putData(tableName, "user001", family, "age", "28");
            HBaseUtil.putData(tableName, "user001", family, "city", "北京");

            // ===== 写入多列 =====
            Map<String, String> columns = new HashMap<>();
            columns.put("name", "李四");
            columns.put("age", "35");
            columns.put("city", "上海");
            columns.put("phone", "13800138000");
            HBaseUtil.putRow(tableName, "user002", family, columns);
            LOG.info("✅ 数据写入成功");

            // ===== Read (Get) =====
            LOG.info("[Get] 查询数据");
            String name = HBaseUtil.getCell(tableName, "user001", family, "name");
            LOG.info("user001.name = {}", name);

            Result result = HBaseUtil.getRow(tableName, "user002");
            LOG.info("user002 完整数据:");
            result.listCells().forEach(cell ->
                    LOG.info("  {}:{} = {}",
                            Bytes.toString(cell.getFamilyArray(), cell.getFamilyOffset(), cell.getFamilyLength()),
                            Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength()),
                            Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()))
            );

            // ===== Update (同 key put 即为更新) =====
            LOG.info("[Update] 更新数据");
            HBaseUtil.putData(tableName, "user001", family, "age", "29");
            String updatedAge = HBaseUtil.getCell(tableName, "user001", family, "age");
            LOG.info("user001.age (更新后) = {}", updatedAge);

            // ===== Scan =====
            LOG.info("[Scan] 全表扫描");
            ResultScanner scanner = HBaseUtil.scanTable(tableName);
            List<String> scanResult = HBaseUtil.resultScannerToStringList(scanner);
            scanResult.forEach(row -> LOG.info("  扫描结果: {}", row));
            HBaseUtil.closeScanner(scanner);

            // ===== Delete =====
            LOG.info("[Delete] 删除数据");
            HBaseUtil.deleteQualifier(tableName, "user001", family, "city");
            String deletedCity = HBaseUtil.getCell(tableName, "user001", family, "city");
            LOG.info("user001.city (删除后) = {}", deletedCity);

            // 清理
            HBaseUtil.deleteTable(tableName);
            LOG.info("✅ 演示表已清理");

        } catch (Exception e) {
            LOG.error("CRUD 操作失败", e);
        }
    }
}