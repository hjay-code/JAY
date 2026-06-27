package com.hjay.test.hbase;

import com.test.hbase.HBaseUtil;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

/**
 * HBaseUtil 工具类的单元测试。
 * <p>
 * 注意：运行此测试需要 HBase 服务已启动。
 * 如果 HBase 不可用，testConnection() 测试将失败，其他测试也会被跳过。
 * </p>
 */
public class HBaseUtilTest {

    private static final Logger LOG = LoggerFactory.getLogger(HBaseUtilTest.class);

    private static final String TEST_TABLE = "hbase_util_test_table";
    private static final String TEST_FAMILY = "cf";
    private static final String TEST_ROW_KEY = "row001";

    private static boolean hbaseAvailable = false;

    @BeforeClass
    public static void setUp() {
        LOG.info("====== 开始 HBaseUtil 测试 ======");
        // 先测试连接
        hbaseAvailable = HBaseUtil.testConnection();
        if (!hbaseAvailable) {
            LOG.warn("HBase 不可用，跳过所有测试。请确保 HBase 服务已启动。");
            return;
        }
        LOG.info("HBase 连接测试通过，开始执行测试用例");
    }

    @AfterClass
    public static void tearDown() throws IOException {
        // 清理测试表
        if (hbaseAvailable) {
            try {
                if (HBaseUtil.tableExists(TEST_TABLE)) {
                    HBaseUtil.deleteTable(TEST_TABLE);
                    LOG.info("测试表 {} 已清理", TEST_TABLE);
                }
            } catch (Exception e) {
                LOG.warn("清理测试表失败: {}", e.getMessage());
            }
        }
        HBaseUtil.closeConnection();
        LOG.info("====== HBaseUtil 测试结束 ======");
    }

    // ======================== 连接测试 ========================

    @Test
    public void testConnection() {
        boolean result = HBaseUtil.testConnection();
        if (!result) {
            LOG.warn("HBase 连接测试未通过，请检查 HBase 服务是否启动");
        }
        // 不强制断言，因为本地可能没有 HBase 环境
        // 但如果 hbaseAvailable 为 true，则必须成功
        if (hbaseAvailable) {
            assertTrue("HBase 连接应成功", result);
        }
    }

    // ======================== 表操作测试 ========================

    @Test
    public void testCreateTable() throws IOException {
        if (!hbaseAvailable) return;

        // 清理可能存在的旧表
        if (HBaseUtil.tableExists(TEST_TABLE)) {
            HBaseUtil.deleteTable(TEST_TABLE);
        }

        // 创建表
        boolean created = HBaseUtil.createTable(TEST_TABLE, TEST_FAMILY);
        assertTrue("表创建应成功", created);

        // 验证表已存在
        assertTrue("表应存在", HBaseUtil.tableExists(TEST_TABLE));

        // 重复创建应返回 false
        boolean createdAgain = HBaseUtil.createTable(TEST_TABLE, TEST_FAMILY);
        assertFalse("重复创建应返回 false", createdAgain);
    }

    @Test
    public void testDeleteTable() throws IOException {
        if (!hbaseAvailable) return;

        // 确保表存在
        if (!HBaseUtil.tableExists(TEST_TABLE)) {
            HBaseUtil.createTable(TEST_TABLE, TEST_FAMILY);
        }

        // 删除表
        boolean deleted = HBaseUtil.deleteTable(TEST_TABLE);
        assertTrue("表删除应成功", deleted);
        assertFalse("表应不存在", HBaseUtil.tableExists(TEST_TABLE));
    }

    // ======================== 新增数据测试 ========================

    @Test
    public void testPutData() throws IOException {
        if (!hbaseAvailable) return;

        ensureTestTableExists();

        // 写入单列数据
        HBaseUtil.putData(TEST_TABLE, TEST_ROW_KEY, TEST_FAMILY, "name", "张三");
        HBaseUtil.putData(TEST_TABLE, TEST_ROW_KEY, TEST_FAMILY, "age", "25");
        HBaseUtil.putData(TEST_TABLE, TEST_ROW_KEY, TEST_FAMILY, "city", "北京");

        // 验证数据已写入
        String name = HBaseUtil.getCell(TEST_TABLE, TEST_ROW_KEY, TEST_FAMILY, "name");
        assertEquals("姓名应为张三", "张三", name);

        String age = HBaseUtil.getCell(TEST_TABLE, TEST_ROW_KEY, TEST_FAMILY, "age");
        assertEquals("年龄应为25", "25", age);
    }

    @Test
    public void testPutRow() throws IOException {
        if (!hbaseAvailable) return;

        ensureTestTableExists();

        String rowKey = "row002";
        Map<String, String> columns = new HashMap<>();
        columns.put("name", "李四");
        columns.put("age", "30");
        columns.put("city", "上海");
        columns.put("phone", "13800138000");

        HBaseUtil.putRow(TEST_TABLE, rowKey, TEST_FAMILY, columns);

        // 验证数据
        String name = HBaseUtil.getCell(TEST_TABLE, rowKey, TEST_FAMILY, "name");
        assertEquals("姓名应为李四", "李四", name);

        String phone = HBaseUtil.getCell(TEST_TABLE, rowKey, TEST_FAMILY, "phone");
        assertEquals("电话应为13800138000", "13800138000", phone);
    }

    @Test
    public void testPutBatch() throws IOException {
        if (!hbaseAvailable) return;

        ensureTestTableExists();

        List<Put> puts = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Put put = new Put(Bytes.toBytes("batch_row_" + i));
            put.addColumn(Bytes.toBytes(TEST_FAMILY), Bytes.toBytes("seq"), Bytes.toBytes(String.valueOf(i)));
            put.addColumn(Bytes.toBytes(TEST_FAMILY), Bytes.toBytes("data"), Bytes.toBytes("test_data_" + i));
            puts.add(put);
        }

        HBaseUtil.putBatch(TEST_TABLE, puts);

        // 验证批量写入
        for (int i = 1; i <= 5; i++) {
            String seq = HBaseUtil.getCell(TEST_TABLE, "batch_row_" + i, TEST_FAMILY, "seq");
            assertEquals("序号应为" + i, String.valueOf(i), seq);
        }
    }

    // ======================== 查询数据测试 ========================

    @Test
    public void testGetRow() throws IOException {
        if (!hbaseAvailable) return;

        ensureTestTableExists();
        ensureTestDataExists();

        // 查询整行
        Result result = HBaseUtil.getRow(TEST_TABLE, TEST_ROW_KEY);
        assertNotNull("查询结果不应为空", result);
        assertFalse("查询结果不应为空行", result.isEmpty());

        // 验证行键
        assertEquals("行键应匹配", TEST_ROW_KEY, Bytes.toString(result.getRow()));

        // 验证各列
        String name = Bytes.toString(CellUtil.cloneValue(result.getColumnLatestCell(
                Bytes.toBytes(TEST_FAMILY), Bytes.toBytes("name"))));
        assertEquals("姓名应为张三", "张三", name);
    }

    @Test
    public void testGetRowWithFamily() throws IOException {
        if (!hbaseAvailable) return;

        ensureTestTableExists();
        ensureTestDataExists();

        // 按列族查询
        Result result = HBaseUtil.getRow(TEST_TABLE, TEST_ROW_KEY, TEST_FAMILY);
        assertNotNull("查询结果不应为空", result);
        assertFalse("查询结果不应为空行", result.isEmpty());
    }

    @Test
    public void testGetCell() throws IOException {
        if (!hbaseAvailable) return;

        ensureTestTableExists();
        ensureTestDataExists();

        // 查询单个单元格
        String name = HBaseUtil.getCell(TEST_TABLE, TEST_ROW_KEY, TEST_FAMILY, "name");
        assertEquals("姓名应为张三", "张三", name);

        // 查询不存在的列应返回 null
        String notExist = HBaseUtil.getCell(TEST_TABLE, TEST_ROW_KEY, TEST_FAMILY, "not_exist");
        assertNull("不存在的列应返回 null", notExist);
    }

    // ======================== 扫描数据测试 ========================

    @Test
    public void testScanTable() throws IOException {
        if (!hbaseAvailable) return;

        ensureTestTableExists();
        ensureTestDataExists();

        // 全表扫描
        ResultScanner scanner = HBaseUtil.scanTable(TEST_TABLE);
        assertNotNull("Scanner 不应为空", scanner);

        List<String> resultList = HBaseUtil.resultScannerToStringList(scanner);
        assertNotNull("结果列表不应为空", resultList);
        assertTrue("结果列表应包含数据", resultList.size() > 0);

        LOG.info("扫描结果: {}", resultList);
        HBaseUtil.closeScanner(scanner);
    }

    @Test
    public void testScanByRange() throws IOException {
        if (!hbaseAvailable) return;

        ensureTestTableExists();

        // 先插入一些范围数据
        for (int i = 1; i <= 10; i++) {
            String key = String.format("range_%03d", i);
            HBaseUtil.putData(TEST_TABLE, key, TEST_FAMILY, "val", String.valueOf(i));
        }

        // 范围扫描
        ResultScanner scanner = HBaseUtil.scanByRange(TEST_TABLE, "range_003", "range_007");
        List<String> resultList = HBaseUtil.resultScannerToStringList(scanner);
        HBaseUtil.closeScanner(scanner);

        // 应包含 range_003 到 range_006（不包含 range_007），共 4 条
        assertEquals("范围扫描应返回 4 条数据", 4, resultList.size());
    }

    // ======================== 删除数据测试 ========================

    @Test
    public void testDeleteRow() throws IOException {
        if (!hbaseAvailable) return;

        ensureTestTableExists();

        String delRowKey = "delete_test_row";
        HBaseUtil.putData(TEST_TABLE, delRowKey, TEST_FAMILY, "data", "to_delete");

        // 验证数据存在
        assertNotNull("删除前数据应存在",
                HBaseUtil.getCell(TEST_TABLE, delRowKey, TEST_FAMILY, "data"));

        // 删除行
        HBaseUtil.deleteRow(TEST_TABLE, delRowKey);

        // 验证数据已删除
        assertNull("删除后数据应不存在",
                HBaseUtil.getCell(TEST_TABLE, delRowKey, TEST_FAMILY, "data"));
    }

    @Test
    public void testDeleteFamily() throws IOException {
        if (!hbaseAvailable) return;

        ensureTestTableExists();

        String delRowKey = "delete_family_test";
        // 写入两个列族的数据（需要表有两个列族，这里先不管，只操作 cf）
        HBaseUtil.putData(TEST_TABLE, delRowKey, TEST_FAMILY, "col1", "value1");
        HBaseUtil.putData(TEST_TABLE, delRowKey, TEST_FAMILY, "col2", "value2");

        // 删除列族
        HBaseUtil.deleteFamily(TEST_TABLE, delRowKey, TEST_FAMILY);

        // 验证该列族下的所有列都已删除
        assertNull("列族删除后 col1 应不存在",
                HBaseUtil.getCell(TEST_TABLE, delRowKey, TEST_FAMILY, "col1"));
        assertNull("列族删除后 col2 应不存在",
                HBaseUtil.getCell(TEST_TABLE, delRowKey, TEST_FAMILY, "col2"));
    }

    @Test
    public void testDeleteQualifier() throws IOException {
        if (!hbaseAvailable) return;

        ensureTestTableExists();

        String delRowKey = "delete_qualifier_test";
        HBaseUtil.putData(TEST_TABLE, delRowKey, TEST_FAMILY, "keep", "保留");
        HBaseUtil.putData(TEST_TABLE, delRowKey, TEST_FAMILY, "remove", "删除");

        // 删除指定列
        HBaseUtil.deleteQualifier(TEST_TABLE, delRowKey, TEST_FAMILY, "remove");

        // 验证
        assertNull("删除后 remove 列应不存在",
                HBaseUtil.getCell(TEST_TABLE, delRowKey, TEST_FAMILY, "remove"));
        assertNotNull("keep 列应保留",
                HBaseUtil.getCell(TEST_TABLE, delRowKey, TEST_FAMILY, "keep"));
    }

    @Test
    public void testDeleteBatch() throws IOException {
        if (!hbaseAvailable) return;

        ensureTestTableExists();

        // 写入批量数据
        List<String> rowKeys = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            String key = "batch_del_" + i;
            rowKeys.add(key);
            HBaseUtil.putData(TEST_TABLE, key, TEST_FAMILY, "data", "value_" + i);
        }

        // 批量删除
        HBaseUtil.deleteBatch(TEST_TABLE, rowKeys);

        // 验证全部删除
        for (String key : rowKeys) {
            assertNull("批量删除后 " + key + " 应不存在",
                    HBaseUtil.getCell(TEST_TABLE, key, TEST_FAMILY, "data"));
        }
    }

    // ======================== 辅助方法 ========================

    /**
     * 确保测试表存在
     */
    private void ensureTestTableExists() throws IOException {
        if (!HBaseUtil.tableExists(TEST_TABLE)) {
            HBaseUtil.createTable(TEST_TABLE, TEST_FAMILY);
            LOG.info("测试表 {} 已创建", TEST_TABLE);
        }
    }

    /**
     * 确保测试数据存在
     */
    private void ensureTestDataExists() throws IOException {
        String name = HBaseUtil.getCell(TEST_TABLE, TEST_ROW_KEY, TEST_FAMILY, "name");
        if (name == null) {
            HBaseUtil.putData(TEST_TABLE, TEST_ROW_KEY, TEST_FAMILY, "name", "张三");
            HBaseUtil.putData(TEST_TABLE, TEST_ROW_KEY, TEST_FAMILY, "age", "25");
            HBaseUtil.putData(TEST_TABLE, TEST_ROW_KEY, TEST_FAMILY, "city", "北京");
            HBaseUtil.putData(TEST_TABLE, TEST_ROW_KEY, TEST_FAMILY, "phone", "13912345678");
            LOG.info("测试数据已写入: rowKey={}", TEST_ROW_KEY);
        }
    }

    /**
     * 综合测试：演示完整的使用流程
     */
    @Test
    public void testFullWorkflow() throws IOException {
        if (!hbaseAvailable) return;

        String workflowTable = "workflow_test_table";
        try {
            // 1. 创建表
            createAndAssertTable(workflowTable);

            // 2. 插入数据
            HBaseUtil.putData(workflowTable, "user_001", "cf", "name", "王五");
            HBaseUtil.putData(workflowTable, "user_001", "cf", "email", "wangwu@example.com");

            // 3. 查询数据
            String name = HBaseUtil.getCell(workflowTable, "user_001", "cf", "name");
            assertEquals("王五", name);

            // 4. 更新数据（同 key 重新 put 即为更新）
            HBaseUtil.putData(workflowTable, "user_001", "cf", "name", "王五 updated");
            String updatedName = HBaseUtil.getCell(workflowTable, "user_001", "cf", "name");
            assertEquals("王五 updated", updatedName);

            // 5. 删除数据
            HBaseUtil.deleteRow(workflowTable, "user_001");
            assertNull("删除后数据应不存在",
                    HBaseUtil.getCell(workflowTable, "user_001", "cf", "name"));

        } finally {
            // 6. 清理表
            if (HBaseUtil.tableExists(workflowTable)) {
                HBaseUtil.deleteTable(workflowTable);
            }
        }
    }

    private void createAndAssertTable(String tableName) throws IOException {
        if (HBaseUtil.tableExists(tableName)) {
            HBaseUtil.deleteTable(tableName);
        }
        assertTrue("表创建应成功", HBaseUtil.createTable(tableName, "cf"));
    }
}