package com.test.oracle;

import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TableToWordExporter {

    /**
     * 将多个表的字段结构导出到一个 Word 文档
     */
    public static void exportToWord(List<TableInfo> tables, String filePath) throws Exception {
        try (XWPFDocument doc = new XWPFDocument()) {
            for (int i = 0; i < tables.size(); i++) {
                addTableInfo(doc, tables.get(i));
                if (i < tables.size() - 1) {
                    doc.createParagraph(); // 表间空行
                }
            }
            try (FileOutputStream out = new FileOutputStream(filePath)) {
                doc.write(out);
            }
        }
    }

    /**
     * 将单个表的字段结构导出到 Word
     */
    public static void exportSingleTable(TableInfo table, String filePath) throws Exception {
        try (XWPFDocument doc = new XWPFDocument()) {
            addTableInfo(doc, table);
            try (FileOutputStream out = new FileOutputStream(filePath)) {
                doc.write(out);
            }
        }
    }

    // ---------- 内部构建方法 ----------

    private static void addTableInfo(XWPFDocument doc, TableInfo table) {
        // 第一行：表名（加粗、较大字号）
        XWPFParagraph titlePara = doc.createParagraph();
        titlePara.setAlignment(ParagraphAlignment.LEFT);
        titlePara.setSpacingAfter(200);
        XWPFRun runTitle = titlePara.createRun();
        runTitle.setBold(true);
        runTitle.setFontSize(14);
        runTitle.setFontFamily("宋体");
        runTitle.setText(table.getTableName());

        // 第二行：表注释（若有）
        if (table.getRemarks() != null && !table.getRemarks().isEmpty()) {
            XWPFParagraph remarkPara = doc.createParagraph();
            remarkPara.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun runRemark = remarkPara.createRun();
            runRemark.setItalic(true);
            runRemark.setFontSize(11);
            runRemark.setFontFamily("宋体");
            runRemark.setText("注释：" + table.getRemarks());
        }

        // 创建字段表格
        XWPFTable fieldTable = doc.createTable();
        fieldTable.setWidth("100%");
        while (fieldTable.getNumberOfRows() > 0){
            fieldTable.removeRow(0);
        }

        // 设置表格表头：中文名、列名、类型、长度
        String[] headers = {"中文名", "列名", "类型", "长度"};
        XWPFTableRow headerRow = fieldTable.getRow(0);
        for (int i = 0; i < headers.length; i++) {
            XWPFTableCell cell = headerRow.getCell(i);
            setCellText(cell, headers[i], true);
            setCellBackground(cell, "B4C6E7"); // 浅蓝色背景
        }

        // 填充数据行
        List<ColumnInfo> columns = table.getColumns();
        for (ColumnInfo col : columns) {
            XWPFTableRow row = fieldTable.createRow();
            setCellText(row.getCell(0), col.getRemarks() != null ? col.getRemarks() : "");
            setCellText(row.getCell(1), col.getColumnName());
            setCellText(row.getCell(2), col.getTypeName());
            setCellText(row.getCell(3), col.getDataLength() == 0 ? "" : String.valueOf(col.getDataLength()));
        }
    }

    // 设置单元格文字
    private static void setCellText(XWPFTableCell cell, String text) {
        setCellText(cell, text, false);
    }

    private static void setCellText(XWPFTableCell cell, String text, boolean bold) {
        XWPFParagraph para = cell.getParagraphs().get(0);
        para.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = para.createRun();
        run.setFontFamily("宋体");
        run.setFontSize(10);
        run.setBold(bold);
        run.setText(text != null ? text : "");
    }

    // 设置单元格背景色
    private static void setCellBackground(XWPFTableCell cell, String hexColor) {
        CTTc ctTc = cell.getCTTc();
        CTTcPr tcPr = ctTc.isSetTcPr() ? ctTc.getTcPr() : ctTc.addNewTcPr();
        CTShd shd = tcPr.isSetShd() ? tcPr.getShd() : tcPr.addNewShd();
        shd.setVal(STShd.CLEAR);
        shd.setFill(hexColor);
    }


}