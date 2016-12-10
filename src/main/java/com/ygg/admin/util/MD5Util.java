package com.ygg.admin.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


public class MD5Util
{
	static Logger logger = Logger.getLogger(MD5Util.class);
    
    private static String byteArrayToHexString(byte b[])
    {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
            resultSb.append(byteToHexString(b[i]));
        
        return resultSb.toString();
    }
    
    private static String byteToHexString(byte b)
    {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }
    
    public static String MD5Encode(String origin, String charsetname)
    {
        String resultString = null;
        try
        {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname))
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            else
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
        }
        catch (Exception exception)
        {
        }
        return resultString;
    }
    
    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    public static String getNonceStr()
    {
        Random random = new Random();
        return MD5Util.MD5Encode(String.valueOf(random.nextInt(10000)), "UTF-8");
    }
    
    /**
     * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
     * 
     * @param strxml
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    public static Map<String, Object> doXMLParse(String strxml)
    {
        if (null == strxml || "".equals(strxml))
        {
            return null;
        }
        
        InputStream in = null;
        Map<String, Object> m = null;
        try
        {
            m = new HashMap<String, Object>();
            in = String2Inputstream(strxml);
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(in);
            Element root = doc.getRootElement();
            List list = root.getChildren();
            Iterator it = list.iterator();
            while (it.hasNext())
            {
                Element e = (Element)it.next();
                String k = e.getName();
                String v = "";
                List children = e.getChildren();
                if (children.isEmpty())
                {
                    v = e.getTextNormalize();
                }
                else
                {
                    v = getChildrenText(children);
                }
                
                m.put(k, v);
            }
        }
        catch (Exception e)
        {
            logger.error("doXMLParse--error--is:", e);
        }
        finally
        {
            // 关闭流
            try
            {
                in.close();
            }
            catch (IOException e)
            {
                logger.error("doXMLParse---ioclose--error--is:", e);
            }
        }
        
        return m;
    }
    public static InputStream String2Inputstream(String str)
    {
        return new ByteArrayInputStream(str.getBytes());
    }
    /**
     * 获取子结点的xml
     * 
     * @param children
     * @return String
     */
    public static String getChildrenText(List children)
    {
        StringBuffer sb = new StringBuffer();
        if (!children.isEmpty())
        {
            Iterator it = children.iterator();
            while (it.hasNext())
            {
                Element e = (Element)it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if (!list.isEmpty())
                {
                    sb.append(getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }
        
        return sb.toString();
    }
}
