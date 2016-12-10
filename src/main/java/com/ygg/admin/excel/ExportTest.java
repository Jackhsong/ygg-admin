package com.ygg.admin.excel;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

public class ExportTest
{
    public static void main(String[] args) throws Exception
    {
        ExportTest template = new ExportTest();
        template.output();
    }



    public void output()
        throws FileNotFoundException
    {
        long startTimne = System.currentTimeMillis();
        StringTemplateGroup stGroup = new StringTemplateGroup("stringTemplate");
        //写入excel文件头部信息
        StringTemplate head = stGroup.getInstanceOf("com/ygg/admin/excel/head");
        File file = new File("D:/output(打开提示文件格式与扩展名不符，选是即可).xls");
        PrintWriter writer = new PrintWriter(new BufferedOutputStream(new FileOutputStream(file)));
        writer.print(head.toString());
        writer.flush();

        int sheets = 1;
        //excel单表最大行数是65535
        int maxRowNum = 10000;
        for (int i = 0; i < sheets; i++)
        {
            StringTemplate body = stGroup.getInstanceOf("com/ygg/admin/excel/body");
            WorksheetTest worksheet = new WorksheetTest();
            worksheet.setSheet("数据");
            worksheet.setColumnNum(3);
            worksheet.setRowNum(maxRowNum);
            List<Row> rows = new ArrayList<Row>();
            for (int j = 0; j < maxRowNum; j++)
            {
                Row row = new Row();
//                row.setName1("150610569809861");
//                row.setName2("1.02");
//                row.setName3("718795109939");
                rows.add(row);
            }
            worksheet.setRows(rows);
            body.setAttribute("worksheet", worksheet);
            writer.print(body.toString());
            writer.flush();
            rows.clear();
            rows = null;
            worksheet = null;
            body = null;
            Runtime.getRuntime().gc();
        }
        //写入excel文件尾部
        writer.print("</Workbook>");
        writer.flush();
        writer.close();
        System.out.println("生成excel文件完成");
        long endTime = System.currentTimeMillis();
        System.out.println("用时=" + ((endTime - startTimne) / 1000) + "秒");
    }

}