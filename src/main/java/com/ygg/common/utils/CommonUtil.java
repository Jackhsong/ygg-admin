package com.ygg.common.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class CommonUtil
{
    public static Logger log = Logger.getLogger(CommonUtil.class);
    
    /**
     * 平台签名密钥
     */
    public static final String SIGN_KEY = "yangegegegeyan";
    
    /**
     * 将输入流转为字节数组格式的对象
     * 
     * @param in 输入流对象
     * @return
     */
    public static byte[] convertInputStream2Bytes(InputStream in)
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int length = 0;
        byte[] buffer = new byte[1024];
        
        try
        {
            while ((length = in.read(buffer)) != -1)
            {
                out.write(buffer, 0, length);
            }
            
            return out.toByteArray();
        }
        catch (IOException e)
        {
            log.error("read from inputstream error.", e);
        }
        finally
        {
            try
            {
                out.close();
            }
            catch (IOException e)
            {
                log.error("close the outputstream error.", e);
            }
        }
        
        return null;
    }
    
    /**
     * 将输入流转为字符串格式的对象
     * 
     * @param in 输入流对象
     * @return
     */
    public static String convertInputStream2Str(InputStream in)
    {
        String result = null;
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int length = 0;
        byte[] buffer = new byte[1024];
        
        try
        {
            while ((length = in.read(buffer)) != -1)
            {
                out.write(buffer, 0, length);
            }
            
            result = new String(out.toByteArray());
        }
        catch (IOException e)
        {
            log.error("read from inputstream error.", e);
        }
        finally
        {
            try
            {
                out.close();
            }
            catch (IOException e)
            {
                log.error("close the outputstream error.", e);
            }
        }
        
        return result;
    }
    
    /**
     * 生成指定长度的随机数字
     *
     * @param 随机数字长度
     * @return 验证通过返回true
     */
    public static String GenerateRandomCode(int length)
    {
        String result = "";
        for (int i = 0; i < length; i++)
        {
            result += nextInt(0, 9);
        }
        return result;
    }
    
    /**
     * 生成一个唯一的UUID
     *
     * @return
     */
    public static String generateUUID()
    {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }
    
    private static int nextInt(final int min, final int max)
    {
        Random rand = new Random();
        int tmp = Math.abs(rand.nextInt());
        return tmp % (max - min + 1) + min;
    }
    
    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str)
    {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }
    
    /**
     * 电话号码验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isPhone(String str)
    {
        Pattern p1 = null, p2 = null;
        Matcher m = null;
        boolean b = false;
        p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$"); // 验证带区号的
        p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$"); // 验证没有区号的
        if (str.length() > 9)
        {
            m = p1.matcher(str);
            b = m.matches();
        }
        else
        {
            m = p2.matcher(str);
            b = m.matches();
        }
        return b;
    }
    
    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str)
    {
        if (str == null || str.equals(""))
        {
            return true;
        }
        return false;
    }
    
    /**
     * 把字符串转换成md5
     *
     * @param str
     * @return
     */
    public static String strToMD5(String str)
        throws UnsupportedEncodingException
    {
        
        try
        {
            byte[] input;
            input = str.getBytes("UTF-8");
            return bytesToHex(bytesToMD5(input));
        }
        catch (UnsupportedEncodingException e)
        {
            log.error("strToMD5编码不支持!", e);
            throw e;
        }
    }
    
    /**
     * 把字节数组转成16进位制数
     *
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes)
    {
        StringBuffer md5str = new StringBuffer();
        // 把数组每一字节换成16进制连成md5字符串
        int digital;
        for (int i = 4; i < bytes.length - 4; i++)
        {
            digital = bytes[i];
            if (digital < 0)
            {
                digital += 256;
            }
            if (digital < 16)
            {
                md5str.append("0");
            }
            md5str.append(Integer.toHexString(digital));
        }
        return md5str.toString().toUpperCase();
    }
    
    /**
     * 把字节数组转换成md5
     *
     * @param input
     * @return
     */
    public static byte[] bytesToMD5(byte[] input)
    {
        // String md5str = null;
        byte[] buff = null;
        try
        {
            // 创建一个提供信息摘要算法的对象，初始化为md5算法对象
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算后获得字节数组
            buff = md.digest(input);
            // 把数组每一字节换成16进制连成md5字符串
            // md5str = bytesToHex(buff);
        }
        catch (Exception e)
        {
            log.error(e);
        }
        return buff;
    }
    
    /**
     * 将字符串格式装换成DATE类型
     *
     * @param date
     * @param type 被转换的字符串格式，如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Date string2Date(String date, String type)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(type);
        Date time = null;
        try
        {
            time = sdf.parse(date);
        }
        catch (ParseException e)
        {
            log.error("字符串格式装换成DATE类型出错", e);
        }
        return time;
    }
    
    /**
     * 将DATE类型装换成字符串格式
     *
     * @param date
     * @param type 要转换成的字符串格式，如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String date2String(Date date, String type)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(type);
        String time = sdf.format(date);
        return time;
    }
    
    /**
     * 根据长度转换成SQL设值串，如：?,?,?,?
     *
     * @param length
     * @return
     */
    public static String changeSqlValueByLen(int length)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++)
        {
            sb.append("?,");
        }
        if (sb.length() > 0)
        {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
    
    /**
     * 验证字符串是否全是数字
     *
     * @param str
     * @return
     */
    public static boolean validateStrIsNum(String str)
    {
        for (int i = 0; i < str.length(); i++)
        {
            if (Character.isDigit(str.charAt(i)))
            {
                continue;
            }
            else
            {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 获取当前年和周
     *
     * @return 年份+周数，201414
     */
    public static String getYearAndWeek()
    {
        Calendar cl = Calendar.getInstance();
        int week = cl.get(Calendar.WEEK_OF_YEAR);
        cl.add(Calendar.DAY_OF_MONTH, -7);
        int year = cl.get(Calendar.YEAR);
        if (week < cl.get(Calendar.WEEK_OF_YEAR))
        {
            year += 1;
        }
        return (year + "") + (week + "");
    }
    
    /**
     * 生成一个唯一的事务ID
     *
     * @return
     */
    public synchronized static Long generateTransactionId()
    {
        try
        {
            Thread.sleep(1);
        }
        catch (InterruptedException e)
        {
            log.error("getTransactionId出错！", e);
        }
        return Long.parseLong(System.currentTimeMillis() + "2");
    }
    
    /**
     * 生成一个唯一的订单id
     *
     * @return
     */
    public synchronized static String generateOrderNumber()
    {
        try
        {
            Thread.sleep(1);
        }
        catch (InterruptedException e)
        {
            log.error(e);
        }
        Calendar curr = Calendar.getInstance();
        
        StringBuffer suffix =
            new StringBuffer(
                ((curr.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000) + (curr.get(Calendar.MINUTE) * 60 * 1000) + (curr.get(Calendar.SECOND) * 1000) + curr.get(Calendar.MILLISECOND))
                    + "");
        int len = suffix.toString().length();
        if (len >= 0 && len < 8)
        {
            int size = 8 - len;
            for (int i = 0; i < size; i++)
                suffix.insert(0, "0");
        }
        return CommonUtil.date2String(curr.getTime(), "yyyyMMdd").substring(2) + suffix + "2";
    }
    
    /**
     * 是否微信brower
     * 
     * @param userAgent
     * @return
     */
    public static boolean isWeiXinBrower(String userAgent)
    {
        
        // log.info("userAgent is: " + userAgent);
        boolean flag = false;
        if (userAgent == null || userAgent.equals(""))
            return flag;
        userAgent = userAgent.toLowerCase();
        
        if (userAgent.contains("micromessenger"))
        {
            flag = true;
        }
        return flag;
    }
    
    public static String readTxtFile(String filePath)
    {
        StringBuffer sb = new StringBuffer();
        File file = null;
        InputStreamReader read = null;
        BufferedReader bufferedReader = null;
        try
        {
            String encoding = "UTF-8";
            file = new File(filePath);
            if (file.isFile() && file.exists()) // 判断文件是否存在
            {
                read = new InputStreamReader(new FileInputStream(file), encoding); // 考虑到编码格式
                bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null)
                {
                    sb.append(lineTxt + "\n");
                }
                
            }
            else
            {
                log.error("----readTxtFile---找不到指定的文件");
                
            }
        }
        catch (Exception e)
        {
            log.error("---readTxtFile----读取文件内容出错", e);
        }
        finally
        {
            try
            {
                bufferedReader.close();
                read.close();
            }
            catch (IOException e)
            {
                log.error("IOException", e);
            }
        }
        return sb.toString();
    }
    
    public static boolean isNumeric(String str)
    {
        if (str == null || str.equals(""))
            return false;
        
        boolean flag = true;
        char[] array = str.toCharArray();
        for (char c : array)
        {
            
            if (c == '.' || (c >= '0' && c <= '9'))
                continue;
            else
            {
                flag = false;
                break;
            }
            
        }
        
        return flag;
    }
    
    public static byte[] file2byte(String filePath)
    {
        byte[] buffer = null;
        FileInputStream fis = null;
        File file = null;
        ByteArrayOutputStream bos = null;
        try
        {
            file = new File(filePath);
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            
            buffer = bos.toByteArray();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (fis != null)
                    fis.close();
                if (bos != null)
                    bos.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return buffer;
    }
    
    public static File byte2File(byte[] byteArray)
    {
        File f = new File("temp");
        
        if (f.exists())
        {
            f.delete();
        }
        try
        {
            f.createNewFile();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(f);
            fos.write(byteArray);
            
            // f.delete(); // delete it after using it
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (fos != null)
                    fos.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            
        }
        return f;
    }
    
}
