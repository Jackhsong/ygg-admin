package com.ygg.admin.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * poi相关uitl
 * @author zhangyb
 *
 */
public class POIUtil
{
    private static Logger log = Logger.getLogger(POIUtil.class);
    
    //    /**
    //     * 根据指定流获取所有sheet数据，返回的数据中不包括标题
    //     * @param inputStream  输入流
    //     * @param hasTitle   文件是否含标题。
    //     * @return
    //     */
    //    public static Map<String, Object> getAllSheetData(InputStream inputStream, boolean hasTitle)
    //    {
    //        Map<String, Object> allSheetDataMap = new HashMap<String, Object>();
    //        List<Map<String, Object>> sheetDataList = new ArrayList<Map<String, Object>>();
    //        int sheetNum = 0;
    //        try
    //        {
    //            Workbook wb = WorkbookFactory.create(inputStream);
    //            int maxSheetNumIndex = wb.getNumberOfSheets() - 1;
    //            for (int index = 0; index <= maxSheetNumIndex; index++)
    //            {
    //                Map<String, Object> sheetData = getSheetDataAtIndex(wb, index, hasTitle);
    //                if (sheetData != null)
    //                {
    //                    sheetDataList.add(sheetData);
    //                    sheetNum++;
    //                }
    //            }
    //            allSheetDataMap.put("sheetNum", sheetNum);
    //            allSheetDataMap.put("sheetDataList", sheetDataList);
    //            return allSheetDataMap;
    //        }
    //        catch (Exception e)
    //        {
    //            log.error("解析excel失败 -- 根据 Workbook 获取sheet数据", e);
    //            return null;
    //        }
    //    }
    
    /**
     * 根据 Workbook 获取sheet数据，返回的数据中不包括标题
     * @param wb   Workbook
     * @param index    sheet位置
     * @param hasTitle    文件是否含标题。
     * @return
     */
    public static Map<String, Object> getSheetDataAtIndex(Workbook wb, int index, boolean hasTitle)
    {
        try
        {
            Map<String, Object> sheetMap = new HashMap<String, Object>();
            List<Map<String, Object>> rowList = new ArrayList<>();
            Sheet sheet = wb.getSheetAt(index);
            int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
            int firstRowNum = sheet.getFirstRowNum();
            int lastRowNum = sheet.getLastRowNum();
            int rowNum = 0;//行数
            if ((firstRowNum == lastRowNum) && ((hasTitle && (physicalNumberOfRows == 1)) || (!hasTitle && (physicalNumberOfRows == 0))))
            {
                rowNum = 0;
            }
            else
            {
                rowNum = hasTitle ? physicalNumberOfRows - 1 : physicalNumberOfRows;
            }
            int rowIndex = 0;
            for (Row row : sheet)
            {
                if (hasTitle && (rowIndex++ == 0))
                {
                    continue;
                }
                Map<String, Object> rowMap = new HashMap<String, Object>();
                for (Cell cell : row)
                {
                    int colnumIndex = cell.getColumnIndex();
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    rowMap.put("cell" + colnumIndex, cell.getStringCellValue().trim());
                }
                rowList.add(rowMap);
            }
            sheetMap.put("rowNum", rowNum);
            sheetMap.put("data", rowList);
            return sheetMap;
        }
        catch (Exception e)
        {
            log.error("解析excel失败 -- 根据 Workbook 获取sheet数据", e);
            return null;
        }
    }
    
    /**
     * 根据指定流获取sheet数据，返回的数据中不包括标题
     * @param inputStream    输入流
     * @param index    sheet位置
     * @param hasTitle   文件是否含标题。
     * @return
     */
    public static Map<String, Object> getSheetDataAtIndex(InputStream inputStream, int index, boolean hasTitle)
    {
        try
        {
            Workbook wb = WorkbookFactory.create(inputStream);
            Map<String, Object> sheetMap = getSheetDataAtIndex(wb, index, hasTitle);
            return sheetMap;
        }
        catch (Exception e)
        {
            log.error("解析excel失败 -- 根据指定流获取sheet数据", e);
            return null;
        }
    }

    public static SXSSFWorkbook createSXSSFWorkbookTemplate(String[] title)
    {
        // 产生工作簿对象
        SXSSFWorkbook wb = new SXSSFWorkbook(500);
        // 产生工作表对象
        Sheet sheet = wb.createSheet();
        Row row = sheet.createRow(0);
        for (int i = 0; i < title.length; i++)
        {
            Cell cell = row.createCell(i);
            cell.setCellValue(title[i]);
        }
        return wb;
    }
    
    /**
     * 创建 2007 workbook 模板
     * @return
     */
    public static Workbook createXSSFWorkbookTemplate(String[] title)
    {
        // 产生工作簿对象
        Workbook wb = new XSSFWorkbook();
        // 产生工作表对象
        Sheet sheet = wb.createSheet();
        Row row = sheet.createRow(0);
        for (int i = 0; i < title.length; i++)
        {
            Cell cell = row.createCell(i);
            cell.setCellValue(title[i]);
        }
        return wb;
    }
    
    /**
     * 创建 2003 workbook 模板
     * @param title
     * @return
     */
    public static Workbook createHSSFWorkbookTemplate(String[] title)
    {
        // 产生工作簿对象
        Workbook wb = new HSSFWorkbook();
        // 产生工作表对象
        Sheet sheet = wb.createSheet();
        Row row = sheet.createRow(0);
        for (int i = 0; i < title.length; i++)
        {
            Cell cell = row.createCell(i);
            cell.setCellValue(title[i]);
        }
        return wb;
    }
    
}
