package com.hjay.html.template;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseHtmlTemplateUtil {

    // 数据库连接参数（需要根据实际情况修改）
    private static final String DB_URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String DB_USER = "your_username";
    private static final String DB_PASSWORD = "your_password";

    /**
     * 从classpath读取HTML模板文件
     * @param templatePath 模板文件路径
     * @return 模板内容
     */
    public static String readTemplate(String templatePath) throws IOException {
        try (InputStream inputStream = DatabaseHtmlTemplateUtil.class.getClassLoader().getResourceAsStream(templatePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            
            if (inputStream == null) {
                throw new FileNotFoundException("Template file not found: " + templatePath);
            }
            
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }

    /**
     * 根据数据生成HTML报告
     * @param templateContent 模板内容
     * @param dataList 数据列表
     * @return 填充数据后的HTML内容
     */
    public static String generateReport(String templateContent, List<ReportData> dataList) {
        StringBuilder tableRows = new StringBuilder();
        
        // 为每个数据项生成表格行
        for (ReportData data : dataList) {
            tableRows.append("        <tr>\n")
                    .append("            <td>").append(data.getId()).append("</td>\n")
                    .append("            <td>").append(data.getName()).append("</td>\n")
                    .append("        </tr>\n");
        }
        
        // 在模板中找到<tbody>标签的位置，并在其中插入数据行
        int tbodyStart = templateContent.indexOf("<tbody>") + "<tbody>".length();
        int tbodyEnd = templateContent.indexOf("</tbody>");
        
        if (tbodyStart > -1 && tbodyEnd > -1) {
            // 保留表头行
            String headerRow = extractHeaderRow(templateContent.substring(tbodyStart, tbodyEnd));
            String updatedContent = templateContent.substring(0, tbodyStart) 
                    + System.lineSeparator() + headerRow + System.lineSeparator()
                    + tableRows.toString()
                    + templateContent.substring(tbodyEnd);
            return updatedContent;
        }
        
        return templateContent;
    }

    /**
     * 提取表头行
     */
    private static String extractHeaderRow(String tbodyContent) {
        int trStart = tbodyContent.indexOf("<tr>");
        int trEnd = tbodyContent.indexOf("</tr>") + "</tr>".length();
        
        if (trStart >= 0 && trEnd > trStart) {
            return tbodyContent.substring(trStart, trEnd);
        }
        return "";
    }

    /**
     * 从数据库获取数据
     * @return 数据列表
     */
    public static List<ReportData> getDataFromDatabase() {
        List<ReportData> dataList = new ArrayList<>();
        
        // JDBC查询示例
        String query = "SELECT id, name FROM your_table_name";
        
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                dataList.add(new ReportData(id, name));
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
        
        return dataList;
    }

    /**
     * 完整的报告生成流程
     * @param templatePath 模板文件路径
     * @return 生成的HTML报告
     */
    public static String generateFullReport(String templatePath) {
        try {
            // 读取模板
            String templateContent = readTemplate(templatePath);
            
            // 从数据库获取数据
            List<ReportData> dataList = getDataFromDatabase();
            
            // 生成报告
            return generateReport(templateContent, dataList);
        } catch (IOException e) {
            throw new RuntimeException("Error generating report", e);
        }
    }
}
