package com.ygg.admin.util.Excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author lorabit
 * @since 16-4-7
 */
public class ExcelMaker {

    // Content-Type in HttpResponse to be set for xls
    public static final String XLS = "application/vnd.ms-excel";

    // Content-Type in HttpResponse to be set for xlsx
    public static final String XLSX =
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private List<String> headers;

    private List<?> contents;

    private ExcelFileType type;

    private Workbook workbook;

    private String suffix;

    private List<String> displayHeaders;

    /**
     * @param headers  json property names
     * @param contents each Object in contents list should include all json property names that
     *                 headers declared
     */
    private ExcelMaker(List<?> contents, List<String> headers) {
        this.contents = contents;
        this.headers = headers;

        if (type == ExcelFileType.XLS) {  // actually type must be null at this time
            workbook = new HSSFWorkbook();
            this.type = ExcelFileType.XLS;
        } else {
            workbook = new XSSFWorkbook();
            this.type = ExcelFileType.XLSX;
        }
        this.suffix = type.suffix;
    }

    /**
     * @param headers  json property names
     * @param contents each Object in contents list should include all json property names that
     *                 headers declared
     * @param type     type of excel to be made
     */
    private ExcelMaker(List<String> headers, List<Object> contents, ExcelFileType type) {
        this.contents = contents;
        this.headers = headers;

        if (type == ExcelFileType.XLSX) {
            workbook = new XSSFWorkbook();
        } else {
            workbook = new HSSFWorkbook();
        }
        this.type = type;
        this.suffix = type.suffix;
    }

    public static ExcelMaker from(List<?> contents, List<String> headers) {
        checkNotNull(contents, "contents shouldn't be null");
        checkNotNull(headers, "headers shouldn't be null");
        return new ExcelMaker(contents, headers);
    }

    public ExcelMaker andFrom(List<?> contents, List<String> headers) {
        checkNotNull(contents, "contents shouldn't be null");
        checkNotNull(headers, "headers shouldn't be null");

        this.contents = contents;
        this.headers = headers;
        this.displayHeaders = null;
        return this;
    }

    public ExcelMaker displayHeaders(List<String> displayHeaders) {
        this.displayHeaders = displayHeaders;
        return this;
    }

    public ExcelMaker resultType(ExcelFileType type) {
        if (type == this.type) {
            return this;
        }

        if (type == ExcelFileType.XLSX) {
            workbook = new XSSFWorkbook();
        } else {
            workbook = new HSSFWorkbook();
        }
        this.type = type;
        this.suffix = this.type.suffix;
        return this;
    }

    private boolean contextReady() {

        if (headers == null) {
            return false;
        } else if (contents == null ) {
            return false;
        } else if (workbook == null) {
            return false;
        }
        return true;
    }

    /**
     * make the excel file based on headers and contents which should have been set
     *
     * @return ExcelMaker itself
     * @throws IllegalArgumentException only support String, Integer, Long, Double, Date, null as cell
     *                                  data.
     */
    public ExcelMaker make() throws IllegalArgumentException {
        return make(null);
    }

    /**
     * make the excel file based on headers and contents which should have been set
     *
     * @param sheetName specified sheet name
     * @return ExcelMaker itself
     * @throws IllegalArgumentException only support String, Integer, Long, Double, Date, null as cell
     *                                  data.
     */
    public ExcelMaker make(String sheetName) throws IllegalArgumentException {

        //check if headers and contents set
        if (!contextReady()) {
            throw new IllegalArgumentException("invalid headers or contents");
        }

        Sheet sheet = sheetName == null ? workbook.createSheet() : workbook.createSheet(sheetName);

        // write the display headers as the 1st row in the sheet
        int rowNum = 0;
        Row headerRow = sheet.createRow(rowNum++);
        if (displayHeaders != null) {
            for (int cellNum = 0; cellNum < displayHeaders.size(); cellNum++) {
                String header = displayHeaders.get(cellNum);
                Cell cell = headerRow.createCell(cellNum);
                cell.setCellValue(header);
            }
        } else {  // if display headers not set, use headers as default
            for (int cellNum = 0; cellNum < headers.size(); cellNum++) {
                String header = headers.get(cellNum);
                Cell cell = headerRow.createCell(cellNum);
                cell.setCellValue(header);
            }
        }

        //parse each Object in content list and write as a row
        for (Object content : contents) {
            Map<String, Object> headerMap = objectMapper.convertValue(content,
                    new TypeReference<Map<String, Object>>() {
                    });
            Row row = sheet.createRow(rowNum++);
            // only find properties that declared by headers
            for (int cellNum = 0; cellNum < headers.size(); cellNum++) {
                String header = headers.get(cellNum);
                Cell cell = row.createCell(cellNum);
                Object obj = headerMap.get(header);
                if (obj == null) {
                    obj = "";
                }
                if (obj instanceof String) {
                    cell.setCellValue((String) obj);
                } else if (obj instanceof Integer) {
                    cell.setCellValue((Integer) obj);
                } else if (obj instanceof Long) {
                    cell.setCellValue((Long) obj);
                } else if (obj instanceof Double) {
                    cell.setCellValue((Double) obj);
                } else if (obj instanceof Date) {
                    cell.setCellValue((Date) obj);
                } else {
                    throw new IllegalArgumentException("unsupported cell type");
                }
            }
        }
        return this;
    }

    /**
     * write the excel that have been made into a OutputStream
     *
     * @param os destination OutputStream
     * @return ExcelMaker itself
     */
    public ExcelMaker writeTo(OutputStream os)
            throws NullPointerException, IllegalArgumentException, IOException {
        checkNotNull(workbook, "workbook isn't created yet");
        if (workbook.getNumberOfSheets() == 0) {
            this.make();
        }
        workbook.write(os);
        return this;
    }

    /**
     * create excel file with given file name
     *
     * @param dirIncludedFileName file name that should include directory of the the file
     * @return target file
     */
    public File create(String dirIncludedFileName)
            throws NullPointerException, IllegalArgumentException, IOException {
        File file = new File(dirIncludedFileName);
        FileOutputStream os = new FileOutputStream(file);
        System.out.println(file.getAbsolutePath());
        this.writeTo(os);
        os.close();
        return file;
    }

    public List<String> getDisplayHeaders() {
        return displayHeaders;
    }

    public ExcelFileType getResultType() {
        return type;
    }

    public String getSuffix() {
        return suffix;
    }

    public enum ExcelFileType {

        XLS("xls"),
        XLSX("xlsx");

        public final String suffix;

        ExcelFileType(String suffix) {

            this.suffix = suffix;
        }
    }

}
