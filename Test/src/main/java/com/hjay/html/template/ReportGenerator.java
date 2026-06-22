package com.hjay.html.template;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ReportGenerator {

    public static void main(String[] args) {
        try {
            // 生成报告
            String reportHtml = HtmlTemplateUtil.generateFullReport("template/report.html");
            
            // 输出到控制台
            System.out.println("Generated Report:");
            System.out.println(reportHtml);
            
            // 保存到文件
            saveToFile(reportHtml, "generated_report.html");
            
            System.out.println("\nReport generated successfully!");
        } catch (Exception e) {
            System.err.println("Error generating report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 将生成的HTML保存到文件
     */
    private static void saveToFile(String content, String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println(content);
            System.out.println("Report saved to: " + fileName);
        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
        }
    }
}
