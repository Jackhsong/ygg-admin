package com.ygg.admin.excel;

import java.util.List;

public class WorksheetTest
{
    private String sheet;

    private int columnNum;

    private int rowNum;

    private List<Row> rows;

    public String getSheet()
    {
        return sheet;
    }

    public void setSheet(String sheet)
    {
        this.sheet = sheet;
    }

    public List<Row> getRows()
    {
        return rows;
    }

    public void setRows(List<Row> rows)
    {
        this.rows = rows;
    }

    public int getColumnNum()
    {
        return columnNum;
    }

    public void setColumnNum(int columnNum)
    {
        this.columnNum = columnNum;
    }

    public int getRowNum()
    {
        return rowNum;
    }

    public void setRowNum(int rowNum)
    {
        this.rowNum = rowNum;
    }

}
