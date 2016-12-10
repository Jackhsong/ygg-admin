package com.ygg.admin.util;

import java.util.UUID;

public class UUIDGenerator
{
    
    /**
     * 直接生成
     * 
     * @return
     */
    public static String generateUUID()
    {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
    
    /**
     * 去掉了－后的UUID
     * 
     * @return
     */
    public static String getUUID()
    {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        // 去掉"-"符号
        String temp = str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);
        return temp;
    }
    
    /**
     * 获得指定数组的UUID,可作为数据库的primaryKey
     * 
     * @param number
     * @return
     */
    public static String[] getUUID(int number)
    {
        if (number < 1)
        {
            return null;
        }
        String[] ss = new String[number];
        for (int i = 0; i < number; i++)
        {
            ss[i] = getUUID();
        }
        return ss;
    }
    
    /*
     * public static void main(String[] args) { String[] ss = getUUID(10); for (int i = 0; i < 10; i++) {
     * System.out.println("ss["+i+"]  =====  "+ss[i]); } }
     */
}
