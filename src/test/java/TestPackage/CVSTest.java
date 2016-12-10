package TestPackage;

import com.alibaba.fastjson.JSON;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CVSTest
{
    public static void main(String[] args)
        throws Exception
    {
        Map<String,Object> result = new HashMap<>();
        List<Map<String, Object>> rowList = new ArrayList<>();
//        CsvReader r = new CsvReader(inputStream, Charset.forName("GBK")); // 生成CsvReader对象，以，为分隔符，GBK编码方式
        CsvReader r = new CsvReader("D:\\yangege\\1需求\\外部订单合并\\贝贝导出.csv", ',', Charset.forName("GBK"));
        r.readHeaders(); // 跳过表头
        ArrayList<String[]> csvList = new ArrayList<>(); //用来保存数据
        while (r.readRecord())
        {
            csvList.add(r.getValues());
        }
        r.close();
        for (String[] s : csvList)
        {
            Map<String,Object> row = new HashMap<>();
            for (int i = 0; i < s.length; i++)
            {
                row.put("cell" + i, s[i].trim());
            }
            rowList.add(row);
        }
        result.put("rowNum", csvList.size());
        result.put("data", rowList);
        System.out.println(JSON.toJSONString(rowList));
    }
    
    public static void readCVS()
        throws Exception
    {
        // 生成CsvReader对象，以，为分隔符，GBK编码方式
        CsvReader r = new CsvReader("D:\\yangege\\1需求\\外部订单合并\\贝贝导出.csv", ',', Charset.forName("GBK"));
        // 读取表头
        r.readHeaders();
        // 逐条读取记录，直至读完
        ArrayList<String[]> csvList = new ArrayList<>(); //用来保存数据
        while (r.readRecord())
        {
            csvList.add(r.getValues());
        }
        r.close();
        for (String[] s : csvList)
        {
            System.out.println("订单编号：" + s[6] + ", ");
        }
    }
    
    public static void writeCVS()
        throws Exception
    {
        CsvWriter wr = new CsvWriter("D:\\yangege\\1需求\\外部订单合并\\test.csv", ',', Charset.forName("GBK"));
        String[] contents = {"Lily", "五一", "90", "女"};
        wr.writeRecord(contents);
        wr.close();
        System.out.println("写入ok");
    }
    
}
