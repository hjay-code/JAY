package com.test.oracle;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

public class MetaReaderFactory {
    public static IDbMetaReader createReader(Connection conn) throws Exception {
        DatabaseMetaData meta = conn.getMetaData();
        String productName = meta.getDatabaseProductName().toLowerCase();
        if (productName.contains("oracle")) {
            return new OracleMetaReader();
        } else if (productName.contains("mysql")) {
            return new MySqlMetaReader();
        } else {
            throw new UnsupportedOperationException("不支持的数据库: " + productName);
        }
    }
}
