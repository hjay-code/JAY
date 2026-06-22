package com.hjay.html.template;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class HtmlTemplateUtil {

    /**
     * 从classpath读取HTML模板文件
     * @param templatePath 模板文件路径
     * @return 模板内容
     */
    public static String readTemplate(String templatePath) throws IOException {
        try (InputStream inputStream = HtmlTemplateUtil.class.getClassLoader().getResourceAsStream(templatePath);
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
     * 从数据库获取数据（示例方法，需要根据实际数据库配置修改）
     * @return 数据列表
     */
    public static List<ReportData> getDataFromDatabase() {
        // 这里是示例数据，实际应用中应该从数据库查询
        // 例如使用JDBC连接到数据库并执行查询
        
        // 示例数据
        return List.of(
            new ReportData(100, "Jerry"),
            new ReportData(101, "Tom"),
            new ReportData(102, "Alice"),
            new ReportData(103, "Bob")
        );
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
            
            // 获取数据（实际应用中应从数据库获取）
            List<ReportData> dataList = getDataFromDatabase();
            
            // 生成报告
            return generateReport(templateContent, dataList);
        } catch (IOException e) {
            throw new RuntimeException("Error generating report", e);
        }
    }
}
