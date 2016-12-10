package com.ygg.admin.util;

import java.rmi.RemoteException;
import java.util.Arrays;

import org.apache.log4j.Logger;

import com.ygg.admin.sdk.yimei.SingletonClient;

public class YimeiUtil
{
    
    public static Logger log = Logger.getLogger(YimeiUtil.class);
    
    /**
     * 发送短信
     * 
     * @param str
     * @return 验证通过返回true
     */
    public static void sendSMS(String[] mobiles, String smsContent, int smsPriority)
        throws Exception
    {
        int i;
        try
        {
            i = SingletonClient.getClient().sendSMS(mobiles, CommonConstant.SMS_PREFIX + smsContent, "", smsPriority);
            if (i != 0)
            {
                throw new Exception("发送短信失败！mobiles：" + Arrays.toString(mobiles) + ",smsContent:" + smsContent + ",i:" + i);
            }
        }
        catch (RemoteException e)
        {
            log.warn("发送短信失败！mobiles：" + Arrays.toString(mobiles) + ",smsContent:" + smsContent);
            throw new Exception(e);
        }
    }
    
    public static void sendSMS(String mobile, String smsContent, int smsPriority)
        throws Exception
    {
        int i;
        try
        {
            i = SingletonClient.getClient().sendSMS(new String[] {mobile}, CommonConstant.SMS_PREFIX + smsContent, "", smsPriority);
            if (i != 0)
            {
                throw new Exception("发送短信失败！mobiles：" + mobile + ",smsContent:" + smsContent + ",i:" + i);
            }
        }
        catch (RemoteException e)
        {
            log.warn("发送短信失败！mobiles：" + mobile + ",smsContent:" + smsContent);
            throw new Exception(e);
        }
    }
    
    /**
     * 获取软件序列号的余额
     */
    public static double getBalance()
        throws Exception
    {
        try
        {
            double a = SingletonClient.getClient().getBalance();
            return a;
        }
        catch (Exception e)
        {
            log.warn("查询余额失败！");
            throw new Exception(e);
        }
    }
}
