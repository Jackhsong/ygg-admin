package com.ygg.admin.util;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import sun.misc.BASE64Decoder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ygg.admin.code.SaleWindowEnum;
import com.ygg.admin.entity.SaleWindowEntity;
import com.ygg.admin.entity.User;
import com.ygg.admin.service.UserService;
import com.ygg.admin.service.impl.UserServiceImpl;

public class CommonUtil
{
    public static Logger log = Logger.getLogger(CommonUtil.class);

    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    private static Random random = new Random();

    private static Gson gson = new Gson();
    
    /**
     * 生成唯一的外部订单号
     */
    public static long generateOutNumber()
    {
        try
        {
            Thread.sleep(1);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        String number = "70" + System.currentTimeMillis() + random.nextInt(10);
        return Long.valueOf(number);
    }
    
    /**
     * 判断订单号是手工订单还是正常订单
     *
     * @param number
     * @return 1 : 手工订单 (201505077204531)，2 : 正常订单
     */
    public static int estimateOrderNumber(String number)
    {
        // 这个时间后才有手动订单
        Date earlyDate = string2Date("2015-05-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
        String n2to4 = number.substring(2, 4); // 手动订单是年份，正常订单是月份。此方法有效期为84年， 2015 - 2099
        if (n2to4.startsWith("0"))
        {
            n2to4 = n2to4.substring(1);
        }
        if (Integer.parseInt(n2to4) >= 15)
        {
            String dateStr = number.substring(0, 8);
            Date currOrderDate = string2Date(dateStr, "yyyyMMdd");
            if (currOrderDate.before(earlyDate))
            {
                return 2;
            }
            return 1;
        }
        else
        {
            // 正常订单
            return 2;
        }
    }
    
    public static String generateCode()
    {
        String hexlong = random.nextInt(10) + "" + System.currentTimeMillis() + "" + random.nextInt(10);
        return Long.toHexString(Long.valueOf(hexlong));
    }
    
    /**
     * 检查收货地址姓名是否要求
     * 
     * @param content
     * @return
     */
    public static boolean validateReceiveName(String content)
    {
        // 长度只允许2-5个字符
        if (content == null || content.length() < 2 || content.length() > 5)
        {
            return false;
        }
        // 只允许中文名字
        String patt1 = "^[\\u4e00-\\u9fa5]+$";
        if (!matchPatt(content, patt1))
        {
            return false;
        }
        // 不允许 开头第1个字是"小","阿"
        if (content.startsWith("小") || content.startsWith("阿"))
        {
            return false;
        }
        // 不允许 小姐\u5c0f\u59d0 ； 女士\u5973\u58eb ； 先生\u5148\u751f
        if (content.indexOf("小姐") > -1 || content.indexOf("女士") > -1 || content.indexOf("先生") > -1)
        {
            return false;
        }
        // 不允许所有中文字相同
        char[] arr = content.toCharArray();
        Set s = new HashSet();
        for (char c : arr)
        {
            s.add(c);
        }
        if (s.size() == 1)
        {
            return false;
        }
        return true;
    }
    
    public static boolean matchPatt(String content, String patt)
    {
        Pattern pattern = Pattern.compile(patt);
        Matcher matcher = pattern.matcher(content);
        return matcher.matches();
    }
    
    /**
     * 生成指定长度的随机数字
     * 
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
            input = str.getBytes(CommonConstant.CHARACTER_ENCODING);
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
            e.printStackTrace();
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
     * 生成一个唯一的UUID
     * 
     * @return
     */
    public static String generateUUID()
    {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
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
        return Long.parseLong(System.currentTimeMillis() + CommonConstant.PLATFORM_IDENTITY_CODE);
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
            Thread.sleep(100);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        Calendar curr = Calendar.getInstance();
        StringBuffer suffix =
            new StringBuffer(
                ((curr.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000) + (curr.get(Calendar.MINUTE) * 60 * 1000) + (curr.get(Calendar.SECOND) * 1000) + curr.get(Calendar.MILLISECOND))
                    + "");
        int size = suffix.length();
        for (int i = 8; i > size; i--)
        {
            suffix.insert(0, "0");
        }
        return CommonUtil.date2String(curr.getTime(), "yyyyMMdd") + suffix.substring(0, 6) + CommonConstant.PLATFORM_IDENTITY_CODE;
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
     * 获取今日特卖日期
     * 
     * @return yyyyMMdd
     */
    public static int getNowSaleDate()
    {
        Calendar cl = Calendar.getInstance();
        int currentHour = cl.get(Calendar.HOUR_OF_DAY);
        if (currentHour < CommonConstant.SALE_REFRESH_HOUR)
        {
            cl.add(Calendar.DAY_OF_YEAR, -1);
        }
        return Integer.parseInt(date2String(cl.getTime(), "yyyyMMdd"));
    }
    
    /**
     * 获取即将开始日期
     * 
     * @return yyyyMMdd
     */
    public static int getLaterSaleDate()
    {
        Calendar cl = Calendar.getInstance();
        int currentHour = cl.get(Calendar.HOUR_OF_DAY);
        if (currentHour < CommonConstant.SALE_REFRESH_HOUR)
        {
            cl.add(Calendar.DAY_OF_YEAR, -1);
        }
        cl.add(Calendar.DAY_OF_YEAR, 1);
        return Integer.parseInt(date2String(cl.getTime(), "yyyyMMdd"));
    }
    
    /**
     * 转换float为平台支持的小数位
     * 
     * @return
     */
    public static float transformFloat(float f)
    {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return Float.parseFloat(decimalFormat.format(f));
    }
    
    /**
     * 获取今日特卖到明日特卖剩余的秒数
     * 
     * @return
     */
    public static int getNowEndSecond()
    {
        Calendar now = Calendar.getInstance();
        Calendar tomorrow = Calendar.getInstance();
        int currentHour = now.get(Calendar.HOUR_OF_DAY);
        if (currentHour < CommonConstant.SALE_REFRESH_HOUR)
        {
            tomorrow.set(Calendar.HOUR_OF_DAY, 10);
        }
        else
        {
            tomorrow.add(Calendar.DAY_OF_YEAR, 1);
            tomorrow.set(Calendar.HOUR_OF_DAY, 10);
        }
        return (int)((tomorrow.getTimeInMillis() - now.getTimeInMillis()) / 1000);
    }

    /**
     * 获取今日晚场特卖日期
     *
     * @return yyyyMMdd
     */
    public static int getNowSaleDateNight()
    {
        Calendar cl = Calendar.getInstance();
        int currentHour = cl.get(Calendar.HOUR_OF_DAY);
        if (currentHour < CommonConstant.SALE_REFRESH_HOUR_NIGHT)
        {
            cl.add(Calendar.DAY_OF_YEAR, -1);
        }
        return Integer.parseInt(date2String(cl.getTime(), "yyyyMMdd"));
    }
    
    public static void main(String[] args)
    {
        try
        {
            // F49B0CBF19B81639 2F59C376AB13202F
            // System.out.println(strToMD5("{'type':'3','nickname':'易超','name':'o6wjmtyWNWlGN5IbtiSzPzS5o360'}"
            // + CommonConstant.SIGN_KEY));
            
            //System.out.println(strToMD5("{\"type\":\"3\",\"nickname\":\"易超\",\"name\":\"o6wjmtyWNWlGN5IbtiSzPzS5o360\"}" + CommonConstant.SIGN_KEY));
            System.out.println(string2Date("0000-00-00 00:00:00", "yyyy-MM-dd HH:mm:ss"));
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * set to list
     * @param set
     * @return
     */
    public static <T> List<T> setToList(Set<T> set)
    {
        return new ArrayList<T>(set);
    }
    
    public static String getRemoteIpAddr(HttpServletRequest request)
    {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.length() > 15)
        {
            if (ip.indexOf(",") > 0)
            {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }
    
    public static int randomIntCode(int n)
    {
        if (n < 1)
            return 0;
        int factor = (int)Math.pow(10, n - 1);
        return (int)((Math.random() * 9 + 1) * factor);
    }
    
    public static String randomStrCode(int n)
    {
        String current = System.currentTimeMillis() + "";
        StringBuffer sb = new StringBuffer();
        for (int i = current.length() - 1; i >= 0; i--)
        {
            sb.append(current.charAt(i));
        }
        return sb.substring(0, n);
    }
    
    // 将 s 进行 BASE64 编码
    public static String getBASE64(String s)
    {
        if (s == null)
            return null;
        return (new sun.misc.BASE64Encoder()).encode(s.getBytes());
    }
    
    // 将 BASE64 编码的字符串 s 进行解码
    public static String getStringFromBASE64(String s)
    {
        if (s == null)
            return null;
        BASE64Decoder decoder = new BASE64Decoder();
        try
        {
            byte[] b = decoder.decodeBuffer(s);
            return new String(b);
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
    /**
     * 将二进制数据编码为BASE64字符串
     * 
     * @param binaryData
     * @return
     */
    public static String getBASE64(byte[] binaryData)
    {
        try
        {
            return new String(Base64.encodeBase64(binaryData), "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            return null;
        }
    }
    
    /**
     * 将BASE64字符串恢复为二进制数据
     * 
     * @param base64String
     * @return
     */
    public static byte[] getBytesFromBASE64(String base64String)
    {
        try
        {
            return Base64.decodeBase64(base64String.getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            return null;
        }
    }
    
    /**
     * 分割List
     * @param list
     * @param pageSize
     * @return
     */
    public static <T> List<List<T>> splitList(List<T> list, int pageSize)
    {
        int listSize = list.size();
        int page = (listSize + (pageSize - 1)) / pageSize;// 页数  
        List<List<T>> listArray = new ArrayList<List<T>>();// 创建list,用来保存分割后的sublist  
        for (int i = 0; i < page; i++)
        {
            // 按照数组大小遍历  
            List<T> subList = new ArrayList<T>();
            for (int j = 0; j < listSize; j++)
            {
                int pageIndex = (j + pageSize) / pageSize;// 当前记录的页码(第几页)  
                if (pageIndex == (i + 1))
                {
                    //当前记录的页码等于要放入的页码时  
                    subList.add(list.get(j)); //放入分割后的list(subList)  
                }
                if ((j + 1) == ((i + 1) * pageSize))
                {
                    // 当放满一页时退出当前循环  
                    break;
                }
            }
            listArray.add(subList);
        }
        return listArray;
    }
    
    //String MD5加密
    public static String ecodeByMD5(String originstr)
    {
        String result = null;
        
        //用来将字节转换成 16 进制表示的字符
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        
        if (originstr != null)
        {
            try
            {
                //返回实现指定摘要算法的 MessageDigest 对象
                MessageDigest md = MessageDigest.getInstance("MD5");
                
                //使用utf-8编码将originstr字符串编码并保存到source字节数组
                byte[] source = originstr.getBytes(CommonConstant.CHARACTER_ENCODING);
                
                //使用指定的 byte 数组更新摘要
                md.update(source);
                
                //通过执行诸如填充之类的最终操作完成哈希计算，结果是一个128位的长整数
                byte[] tmp = md.digest();
                
                //用16进制数表示需要32位
                char[] str = new char[32];
                
                for (int i = 0, j = 0; i < 16; i++)
                {
                    //j表示转换结果中对应的字符位置，从第一个字节开始，对 MD5 的每一个字节转换成 16 进制字符
                    byte b = tmp[i];
                    
                    //取字节中高 4 位的数字转换，无符号右移运算符>>> ，它总是在左边补0，0x代表它后面的是十六进制的数字. f转换成十进制就是15
                    str[j++] = hexDigits[b >>> 4 & 0xf];
                    
                    // 取字节中低 4 位的数字转换
                    str[j++] = hexDigits[b & 0xf];
                }
                result = new String(str);//结果转换成字符串用于返回
            }
            catch (NoSuchAlgorithmException e)
            {
                e.printStackTrace();
            }
            catch (UnsupportedEncodingException e)
            {
                //不支持字符编码异常
                e.printStackTrace();
            }
        }
        return result;
        
    }
    
    /**
     * 获取当前登录用户Id
     * 
     * @return 当前登录用户Id
     */
    public static int getCurrentUser()
    {
        int userId = 0;
        User user = null;
        UserService userService = new UserServiceImpl();
        try
        {
            String username = SecurityUtils.getSubject().getPrincipal() + "";
            user = userService.findByUsername(username);
            if (user != null)
            {
                userId = user.getId();
            }
        }
        catch (Exception e)
        {
            log.error("获取当前登录用户失败", e);
        }
        finally
        {
            userService = null;
            user = null;
        }
        return userId;
    }
    
    /**
     * 将request中的参数包装成对象
     * @param bean
     * @param request
     */
    public static void wrapParamter2Entity(Object bean, HttpServletRequest request)
    {
        Map<String, Object> properties = new HashMap<String, Object>();
        try {
            Enumeration<String> names = request.getParameterNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                String[] value = request.getParameterValues(name);
                if (value.length <= 1) {
                    properties.put(name, value);
                } else {
                    properties.put(name, Arrays.toString(value).replaceAll("\\[", "").replaceAll("\\]", ""));
                }

            }
            BeanUtils.populate(bean, properties);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * TODO 将String[] 参数包装成List对象返回
     * @param bean
     * @param args
     * @return
     */
    public static List<Object> wrapParamter2List(Object bean, String... args)
    {
        return null;
    }
    
    public static <T> String list2String(List<T> list, String separator)
    {
        StringBuffer result = new StringBuffer();
        if (list == null || list.isEmpty())
        {
            return result.toString();
        }
        for (T t : list)
        {
            if (result.length() == 0)
            {
                result.append(t.toString());
            }
            else
            {
                result.append(separator).append(t.toString());
            }
        }
        return result.toString();
    }
    
    public static int getSaleWindowStatus(SaleWindowEntity swe)
    {
        String suffix = "";
        if (swe.getSaleTimeType() == SaleWindowEnum.SALE_TIME_TYPE.SALE_10.getCode())
        {
            suffix = "100000";
        }
        else if (swe.getSaleTimeType() == SaleWindowEnum.SALE_TIME_TYPE.SALE_20.getCode())
        {
            suffix = "200000";
        }
        
        DateTime startTime = DateTime.parse(swe.getStartTime() + suffix, DateTimeFormat.forPattern("yyyyMMddHHmmss"));
        DateTime endTime = DateTime.parse(swe.getEndTime() + suffix, DateTimeFormat.forPattern("yyyyMMddHHmmss")).plusDays(1);
        
        if (startTime.isAfterNow())
        {
            return SaleWindowEnum.SALE_WINDOW_STATUS.TO_START.getCode();
        }
        else if (startTime.isBeforeNow() && endTime.isAfterNow())
        {
            return SaleWindowEnum.SALE_WINDOW_STATUS.IN_PROGRESS.getCode();
        }
        else
        {
            return SaleWindowEnum.SALE_WINDOW_STATUS.FINISHED.getCode();
        }
    }
    
    /**
     * 转换byte到16进制
     *
     * @param b 要转换的byte
     * @return 16进制格式
     */
    private static String byteToHexString(byte b)
    {
        int n = b;
        if (n < 0)
        {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * 转换字节数组为16进制字串
     *
     * @param b 字节数组
     * @return 16进制字串
     */
    public static String byteArrayToHexString(byte[] b)
    {
        StringBuilder resultSb = new StringBuilder();
        for (byte aB : b)
        {
            resultSb.append(byteToHexString(aB));
        }
        return resultSb.toString();
    }

    /**
     * MD5编码
     *
     * @param origin 原始字符串
     * @return 经过MD5加密之后的结果
     */
    public static String md5Encode(String origin)
    {
        String resultString = null;
        try
        {
            resultString = origin;
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(resultString.getBytes("UTF-8"));
            resultString = byteArrayToHexString(md.digest());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return resultString;
    }

    public static String objectToXml(Object bean)
    {
        XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        xStreamForRequestPostData.alias("xml", bean.getClass());
        return xStreamForRequestPostData.toXML(bean);
    }

    // 读取指定的信息
    public static String readString(String ResourceBundleStr, String name)
    {
        String result = "";
        try
        {
            if (StringUtils.isEmpty(ResourceBundleStr))
            {
                ResourceBundleStr = "config";
            }
            ResourceBundle rb = ResourceBundle.getBundle(ResourceBundleStr);
            result = rb.getString(name);
        }
        catch (Exception e)
        {
            System.out.println("读取指定配置信息" + name + "失败!");
        }
        return result;
    }

    /**
     * 生成用户的16位大写md5密钥
     * @param accountId
     * @return
     */
    public static String generateAccountSign(int accountId)
            throws Exception
    {
        String signSuffix = "JiaGengDuoQian18664573290";
        String plainText = accountId + signSuffix;

        String sign = plainText;
        for (int i = 0; i < 3; i++)
        {
            sign = strToMD5(sign);
        }
        return sign;
    }
    
    
    /**
     * 生成t.cn短链接根据长连接
     * 
     * @param num
     * @return
     * @throws IOException
     * @throws HttpException
     */
    public static String generateTCNShortUrl(String longUrl)
        throws HttpException, IOException
    {
        PostMethod post = new PostMethod("http://api.t.sina.com.cn/short_url/shorten.json");
        HttpClient client = new HttpClient();
        NameValuePair[] params = new NameValuePair[2];
        params[0] = new NameValuePair("source", "637873839");
        params[1] = new NameValuePair("url_long", longUrl);
        post.setRequestBody(params);
        client.executeMethod(post);
        InputStream in = post.getResponseBodyAsStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String result = br.readLine();
        JSONArray param = JSON.parseArray(result);
        return param.getJSONObject(0).getString("url_short");
    }


    /**
     * 对象转map
     * @param object
     * @return
     */
    public static Map<String, Object> object2Map(Object object)
        throws IllegalAccessException
    {
        Map<String, Object> resultMap = new HashMap<>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field f : fields)
        {
            f.setAccessible(true);
            if (f.get(object) != null && f.get(object) != "")
            {
                resultMap.put(f.getName(), f.get(object));
            }
        }
        return resultMap;
    }

    public static JSONObject parseXml(String xml)
            throws Exception
    {
        // 将解析结果存储在HashMap中
        JSONObject jsonObject = new JSONObject();

        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(new ByteArrayInputStream(xml.getBytes()));
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();

        // 遍历所有子节点
        for (Element e : elementList)
        {
            jsonObject.put(e.getName(), e.getText());
        }
        return jsonObject;
    }

    public static <T> T parseRequestBody(HttpServletRequest request, Class<T> type)
        throws IOException
    {
        int size = request.getContentLength();
        InputStream is = request.getInputStream();
        if (size > 0)
        {
            Reader reader = new InputStreamReader(is);
            Writer writer = new StringWriter(size);
            char[] buf = new char[1024];
            int n = 0;
            while ((n = reader.read(buf)) != -1)
            {
                writer.write(buf, 0, n);
            }
            return gson.fromJson(writer.toString(),type);
        }
        return null;
    }
}
