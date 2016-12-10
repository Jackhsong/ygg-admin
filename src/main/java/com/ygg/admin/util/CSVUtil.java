package com.ygg.admin.util;

import com.csvreader.CsvReader;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CVS相关uitl
 * 
 * @author zhangyb
 *        
 */
public class CSVUtil
{
    private static Logger log = Logger.getLogger(CSVUtil.class);
    
    public static Map<String, Object> getCVSData(InputStream inputStream, boolean hasTitle)
        throws Exception
    {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> rowList = new ArrayList<>();
        CsvReader r = new CsvReader(inputStream, Charset.forName("GBK")); // 生成CsvReader对象，以，为分隔符，GBK编码方式
        if (hasTitle)
            r.readHeaders(); // 跳过表头
        ArrayList<String[]> csvList = new ArrayList<>(); // 用来保存数据
        while (r.readRecord())
        {
            csvList.add(r.getValues());
        }
        r.close();
        for (String[] s : csvList)
        {
            Map<String, Object> row = new HashMap<>();
            for (int i = 0; i < s.length; i++)
            {
                row.put("cell" + i, s[i].trim());
            }
            rowList.add(row);
        }
        result.put("rowNum", csvList.size());
        result.put("data", rowList);
        return result;
    }
}
