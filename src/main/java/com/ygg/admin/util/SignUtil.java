package com.ygg.admin.util;

import java.security.MessageDigest;

/**
 * 签名相关util
 */
public class SignUtil
{
    public static String md5Uppercase(String plainText)
        throws Exception
    {
        char[] feedArray = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        MessageDigest msgDigest = MessageDigest.getInstance("MD5");
        msgDigest.update(plainText.getBytes("UTF-8"));
        byte[] bytes = msgDigest.digest();
        char[] out = new char[16 * 2];
        for (int i = 0, j = 0; i < 16; i++)
        {
            out[j++] = feedArray[bytes[i] >>> 4 & 0xf];
            out[j++] = feedArray[bytes[i] & 0xf];
        }
        String md5Str = new String(out);
        return md5Str;
    }

    public static void main(String[] args)
        throws Exception
    {
        System.out.println(md5Uppercase("7964EBB846D3EA8A97B"));
    }
}
