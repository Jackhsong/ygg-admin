package com.ygg.admin.sdk.montnets.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class ValidateParamTool
{
    /**
     * 验证账号是否合法
     * @param strUserId
     * @return
     */
    public static boolean validateUserId(String strUserId)
    {
        try
        {
            if (StringUtils.isNotEmpty(strUserId) && strUserId.length() == 6)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }
    }
    
    /**
     * 验证密码是否合法
     * @param strUserId
     * @return
     */
    public static boolean validatePwd(String strPwd)
    {
        try
        {
            if (StringUtils.isNotEmpty(strPwd) && strPwd.length() <= 32)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }
        
    }
    
    /**
     * 验证流水号是否合法
     * @param strUserMsgId
     * @return
     */
    public static boolean validateUserMsgId(String strUserMsgId)
    {
        try
        {
            if (StringUtils.isNotEmpty(strUserMsgId) && SmsTool.isSignDigit(strUserMsgId))
            {
                try
                {
                    //转换成整型，如果报错，就越界。
                    Long.parseLong(strUserMsgId);
                    return true;
                }
                catch (Exception e)
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }
    }
    
    /**
     * 验证信息内容是否合法
     * @param strMessage
     * @return
     */
    public static boolean validateMessage(String strMessage)
    {
        try
        {
            if (StringUtils.isNotEmpty(strMessage) && strMessage.length() <= 350)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }
    }
    
    /**
     * 验证扩展子号是否合法
     * @param strSubPort
     * @return
     */
    public static boolean validateSubPort(String strSubPort)
    {
        try
        {
            if (StringUtils.isNotEmpty(strSubPort) && ("*".equals(strSubPort) || SmsTool.isUnSignDigit(strSubPort)) && strSubPort.length() < 7)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }
    }
    
    /**
     * 验证通道号是否合法
     * @param strSpNumber
     * @return
     */
    public static boolean validateSpNumber(String strSpNumber)
    {
        try
        {
            if (StringUtils.isNotEmpty(strSpNumber) && ("*".equals(strSpNumber) || (SmsTool.isUnSignDigit(strSpNumber) && strSpNumber.length() <= 20)))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }
    }
    
    /**
     * 验证手机号码是否合法
     * @param strMobile
     * @return
     */
    public static boolean validateMobile(String strMobile)
    {
        try
        {
            if (StringUtils.isNotEmpty(strMobile) && strMobile.length() == 11 && SmsTool.isUnSignDigit(strMobile))
            {
                Pattern p = null;
                Matcher m = null;
                boolean b = false;
                p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
                m = p.matcher(strMobile);
                b = m.matches();
                return b;
            }
            else
            {
                return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }
    }
    
    /**
     * 验证是否是以英文逗号隔开的100个手机号码
     * @return
     */
    public static boolean validateMobiles(String strMobiles)
    {
        try
        {
            if (StringUtils.isNotEmpty(strMobiles))
            {
                String[] arrMobiles = strMobiles.split(",");
                if (arrMobiles.length > 0 && arrMobiles.length <= 100)
                {
                    for (int i = 0; i < arrMobiles.length; i++)
                    {
                        if ("".equals(arrMobiles[i]) || arrMobiles[i].length() != 11 || !SmsTool.isUnSignDigit(arrMobiles[i]))
                        {
                            return false;
                        }
                        else
                        {
                            Pattern p = null;
                            Matcher m = null;
                            boolean b = false;
                            p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
                            m = p.matcher(arrMobiles[i]);
                            b = m.matches();
                            return b;
                        }
                    }
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }
        //都没有被拦截，证明手机号码符合要求
        return true;
    }
}
