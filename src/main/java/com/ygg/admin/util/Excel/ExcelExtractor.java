package com.ygg.admin.util.Excel;

import com.google.common.base.CharMatcher;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-4-7
 */

public class ExcelExtractor {

    private static final List<String> XLS =
            Lists.newArrayList("application/vnd.ms-excel", "application/wps-office.xls");

    private static final String XLSX =
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * <p> The given excel file should include a first row as header to declare json property name
     * that each column stands for, which also should exist in the given target object. </p> <p> The
     * given target object should be resolvable by ,
     * and setting 'ignoreUnknownProperty = True' is preferred. </p>
     *
     * @param excel {@link org.springframework.web.multipart.MultipartFile} which should include a
     *              '*.xls' or '*.xlsx' file
     * @param type  {@link T} as target object that each row in excel should be reflected to
     * @param <T>   must be a  resolvable Type
     * @return a {@link java.util.List} of {@link T} as given target objects
     */
    public static <T> List<T> extractFrom(MultipartFile excel, Class<T> type)
            throws IllegalArgumentException {

        List<T> result = Lists.newArrayList();
        Workbook workbook;
        Sheet sheet;
        try {
            String contentType = excel.getContentType();
            if (isXLS(contentType)) {
                workbook = new HSSFWorkbook(excel.getInputStream());
            } else if (contentType.contains(XLSX)) {
                workbook = new XSSFWorkbook(excel.getInputStream());
            } else {
                workbook = new XSSFWorkbook(excel.getInputStream());
//                throw new IllegalArgumentException("unknown file type:" + contentType);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("invalid excel file, " + e.getMessage());
        }

        sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIter = sheet.rowIterator();
        // prepare keys
        Row row = rowIter.next();
        List<Cell> cells = Lists.newArrayList(row.cellIterator());
        List<String> keys = Lists.transform(cells, new Function<Cell, String>() {
            @Override
            public String apply(Cell cell) {
                return CharMatcher.WHITESPACE.trimFrom(cell.getStringCellValue());
            }
        });
        if (keys.size() == 0) {
            throw new IllegalArgumentException("invalid keys");
        }
        //Based on keys from first row, parsing per row -> map -> json -> given type object
        int lineNum = 0;
        while (rowIter.hasNext()) {
            row = rowIter.next();
            lineNum++;
            Map<String, Object> map = Maps.newHashMap();
            for (int i = row.getFirstCellNum(); i < row.getLastCellNum() && i < keys.size(); i++) {
                Object value;
                Cell cell = row.getCell(i);
                if (cell == null) {
                    map.put(keys.get(i), "");
                    continue;
                }
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_NUMERIC:
                        double number = cell.getNumericCellValue();
                        // distinguish integer and fraction
                        long flooredNumber = (long) Math.floor(number);
                        if (number > flooredNumber) {
                            value = number;
                        } else {
                            value = flooredNumber;
                        }
                        break;
                    case Cell.CELL_TYPE_STRING:
                        value = StringUtils.trimToEmpty(cell.getStringCellValue());
                        break;
                    case Cell.CELL_TYPE_BLANK:
                        value = "";
                        break;
                    case Cell.CELL_TYPE_BOOLEAN:
                        value = cell.getBooleanCellValue();
                        break;
                    default:
                        value = "";
                        break;
                }
                map.put(keys.get(i), value);
            }
            try {
                String data = mapper.writeValueAsString(map);  // write to a json string
                T record = mapper.readValue(data, type);  // map json to object by jackson
                result.add(record);
            } catch (Exception e) {
                throw new IllegalArgumentException("invalid data format at line<" + lineNum + ">");
            }
        }

        return result;
    }


    public static <T> List<T> extractFrom(MultipartFile excel, Class<T> type, Map<String,String> mappedHeader)
            throws IllegalArgumentException {

        List<T> result = Lists.newArrayList();
        Workbook workbook;
        Sheet sheet;
        try {
            String contentType = excel.getContentType();
            if (isXLS(contentType)) {
                workbook = new HSSFWorkbook(excel.getInputStream());
            } else if (contentType.contains(XLSX)) {
                workbook = new XSSFWorkbook(excel.getInputStream());
            } else {
                workbook = new XSSFWorkbook(excel.getInputStream());
//                throw new IllegalArgumentException("unknown file type:" + contentType);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("invalid excel file, " + e.getMessage());
        }

        sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIter = sheet.rowIterator();
        // prepare keys
        Row row = rowIter.next();
        List<Cell> cells = Lists.newArrayList(row.cellIterator());
        List<String> keys = Lists.transform(cells, new Function<Cell, String>() {
            @Override
            public String apply(Cell cell) {
                return CharMatcher.WHITESPACE.trimFrom(cell.getStringCellValue());
            }
        });
        if (keys.size() == 0) {
            throw new IllegalArgumentException("invalid keys");
        }
        //Based on keys from first row, parsing per row -> map -> json -> given type object
        int lineNum = 0;
        while (rowIter.hasNext()) {
            row = rowIter.next();
            lineNum++;
            Map<String, Object> map = Maps.newHashMap();
            for (int i = row.getFirstCellNum(); i < row.getLastCellNum() && i < keys.size(); i++) {
                Object value;
                Cell cell = row.getCell(i);
                if (cell == null) {
                    continue;
                }
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_NUMERIC:
                        double number = cell.getNumericCellValue();
                        // distinguish integer and fraction
                        long flooredNumber = (long) Math.floor(number);
                        if (number > flooredNumber) {
                            value = number;
                        } else {
                            value = flooredNumber;
                        }
                        break;
                    case Cell.CELL_TYPE_STRING:
                        value = StringUtils.trimToEmpty(cell.getStringCellValue());
                        break;
                    case Cell.CELL_TYPE_BLANK:
                        value = "";
                        break;
                    case Cell.CELL_TYPE_BOOLEAN:
                        value = cell.getBooleanCellValue();
                        break;
                    default:
                        value = "";
                        break;
                }
                if(value instanceof  String){
                    if(StringUtils.isEmpty((String) value))
                        continue;
                }
                if(mappedHeader.containsKey(keys.get(i))){
                    map.put(mappedHeader.get(keys.get(i)), value);
                }else{
                    map.put(keys.get(i), value);
                }
            }
            try {
                String data = mapper.writeValueAsString(map);  // write to a json string
                T record = mapper.readValue(data, type);  // map json to object by jackson
                result.add(record);
            } catch (Exception e) {
                throw new IllegalArgumentException("invalid data format at line<" + lineNum + ">" + e.getMessage(),e);
            }
        }

        return result;
    }

    public static boolean isXLS(String contentType) {
        for (String xls : XLS) {
            if (contentType.equals(xls)) {
                return true;
            }
        }
        return false;
    }

}
