package com.ygg.admin.sdk.montnets.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.axis.client.Call;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import com.ygg.admin.sdk.montnets.common.ISms;
import com.ygg.admin.sdk.montnets.common.MO_PACK;
import com.ygg.admin.sdk.montnets.common.MULTIX_MT;
import com.ygg.admin.sdk.montnets.common.RPT_PACK;
import com.ygg.admin.sdk.montnets.common.SmsTool;
import com.ygg.admin.sdk.montnets.common.StaticValue;
import com.ygg.admin.sdk.montnets.common.ValidateParamTool;
import com.ygg.admin.sdk.montnets.httpget.CHttpGet;
import com.ygg.admin.sdk.montnets.httppost.CHttpPost;
import com.ygg.admin.sdk.montnets.httpsoap.CHttpSoap;
import com.ygg.admin.sdk.montnets.httpsoap.Sms;
import com.ygg.admin.sdk.montnets.httpsoap.SmsLocator;
import com.ygg.admin.sdk.montnets.httpsoap.SmsSoap;

public class Test
{
    
    private static String strUserId = null;
    
    private static String strPwd = null;
    
    private static String ip = null;
    
    private static String port = null;
    
    private static boolean bKeepAlive = false;
    
    private static BufferedReader breader = null;
    
    private static HttpClient httpClient = null;
    
    private static Call call = null;
    
    private static String host = "http://61.130.7.220:8023";
    
    public static void main(String arg[])
        throws IOException
    {
        
        try
        {
            breader = new BufferedReader(new InputStreamReader(System.in, "GBK"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        //IP地址
        System.out.println("请输入：");
        System.out.print("WEB服务器地址：");
        boolean flagIP = false;
        do
        {
            ip = breader.readLine();
            if (ip != null && !"".equals(ip) && ip.trim().length() < 16)
            {
                flagIP = true;
                StaticValue.ip = ip;
            }
            else
            {
                System.out.print("WEB服务器地址输入不合法，请重新输入：");
            }
        } while (!flagIP);
        
        //端口
        System.out.print("WEB服务器端口：");
        boolean flagPort = false;
        do
        {
            port = breader.readLine();
            if (port != null && !"".equals(port) && SmsTool.isUnSignDigit(port))
            {
                flagPort = true;
                StaticValue.port = port;
            }
            else
            {
                System.out.print("WEB服务器端口输入不合法，请重新输入：");
            }
            host = "http://" + ip + ":" + port;
        } while (!flagPort);
        
        //用户账号
        System.out.print("用户账号：");
        boolean flagUserId = false;
        do
        {
            strUserId = breader.readLine();
            if (ValidateParamTool.validateUserId(strUserId))
            {
                flagUserId = true;
            }
            else
            {
                System.out.print("用户账号输入不合法，请重新输入：");
            }
        } while (!flagUserId);
        
        //用户密码
        System.out.print("用户密码：");
        boolean flagPwd = false;
        do
        {
            strPwd = breader.readLine();
            if (ValidateParamTool.validatePwd(strPwd))
            {
                flagPwd = true;
            }
            else
            {
                System.out.print("用户密码输入不合法，请重新输入：");
            }
        } while (!flagPwd);
        
        //以下为5个方法3种不同请求方式的调用例子
        System.out.println("请选择接口类型：");
        System.out.println("1:  短信息发送接口（相同内容群发，可自定义流水号）");
        System.out.println("2:  短信息发送接口（不同内容群发，可自定义不同流水号，自定义不同扩展号）");
        System.out.println("3:  查询余额接口");
        System.out.println("4:  获取上行接口");
        System.out.println("5:  获取状态报告接口");
        System.out.println("6:  退出应用程序");
        System.out.print("接口类型：");
        String str;
        
        httpClient = new DefaultHttpClient();
        Sms service = new SmsLocator(host);
        try
        {
            SmsSoap client = service.getSmsSoap();
            call = client.createCall();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        do
        {
            
            boolean flagtype = false;
            do
            {
                str = breader.readLine();
                if (str != null && !"".equals(str) && ("1".equals(str) || "2".equals(str) || "3".equals(str) || "4".equals(str) || "5".equals(str) || "6".equals(str)))
                {
                    flagtype = true;
                }
                else
                {
                    System.out.println("接口类型输入错误，请重新输入！");
                    System.out.print("接口类型：");
                }
            } while (!flagtype);
            
            if (str.equals("1"))
            {
                //短信息发送接口（相同内容群发，可自定义流水号）
                SendSms();
            }
            else if (str.equals("2"))
            {
                //短信息发送接口（不同内容群发，可自定义不同流水号，自定义不同扩展号）
                SendMultixSms();
            }
            else if (str.equals("3"))
            {
                //查询余额接口
                QueryBalance();
            }
            else if (str.equals("4"))
            {
                //获取上行接口
                GetMo();
            }
            else if (str.equals("5"))
            {
                //获取状态报告接口
                GetRpt();
            }
            System.out.println("请选择接口类型：");
            System.out.println("1:  短信息发送接口（相同内容群发，可自定义流水号）");
            System.out.println("2:  短信息发送接口（不同内容群发，可自定义不同流水号，自定义不同扩展号）");
            System.out.println("3:  查询余额接口");
            System.out.println("4:  获取上行接口");
            System.out.println("5:  获取状态报告接口");
            System.out.println("6:  退出应用程序");
            System.out.print("接口类型：");
            
        } while (!str.equals("6"));
        
        //关闭连接
        if (httpClient != null)
        {
            httpClient.getConnectionManager().shutdown();
        }
        System.out.print("程序已退出！");
    }
    
    /**
     * 短信息发送接口（相同内容群发，可自定义流水号）
     */
    public static void SendSms()
    {
        try
        {
            System.out.println("请选择请求方式：");
            System.out.println("1: Get请求方式");
            System.out.println("2: Post请求方式");
            System.out.println("3: Soap请求方式");
            System.out.print("请求方式：");
            String type = null;
            boolean flag1 = false;
            do
            {
                type = breader.readLine();
                if (type != null && !"".equals(type) && (("1").equals(type) || ("2").equals(type) || ("3").equals(type)))
                {
                    flag1 = true;
                }
                else
                {
                    System.out.println("请求方式输入错误，请重新输入！");
                    System.out.print("请求方式：");
                }
            } while (!flag1);
            
            System.out.println("请输入手机号码（多个号码用英文逗号分隔，最多100个号码）");
            System.out.print("手机号码：");
            boolean falgp = false;
            String phone;
            do
            {
                boolean flagg = true;
                phone = breader.readLine();
                if (phone != null && !"".equals(phone))
                {
                    String[] phones = phone.split(",");
                    if (phones.length > 0 && phones.length <= 100)
                    {
                        for (int i = 0; i < phones.length; i++)
                        {
                            //如果输入的对象号码不合法则要重新输入
                            if ("".equals(phones[i]) || phones[i].length() != 11 || !SmsTool.isUnSignDigit(phones[i]))
                            {
                                flagg = false;
                                break;
                            }
                        }
                        if (flagg)
                        {
                            falgp = true;
                        }
                        else
                        {
                            System.out.println("手机号码输入不合法，请重新输入");
                            System.out.println("手机号码：");
                        }
                    }
                    else
                    {
                        System.out.println("手机号码个数超过100个，请重新输入");
                        System.out.println("手机号码：");
                    }
                }
                else
                {
                    System.out.println("手机号码输入不合法，请重新输入");
                    System.out.println("手机号码：");
                }
            } while (!falgp);
            System.out.println("请输入短信内容（内容长度不大于350个汉字）");
            System.out.print("短信内容：");
            boolean flag3 = false;
            String strMessage;
            do
            {
                strMessage = breader.readLine();
                if (ValidateParamTool.validateMessage(strMessage))
                {
                    flag3 = true;
                }
                else if (strMessage == null || "".equals(strMessage))
                {
                    System.out.println("短信长度为0，请重新输入");
                    System.out.print("短信内容：");
                }
                else
                {
                    System.out.println("短信长度超过350个汉字，请重新输入");
                    System.out.print("短信内容：");
                }
            } while (!flag3);
            
            System.out.println("请输入扩展子号 （不带请填星号*，长度不大于6位）");
            System.out.print("扩展子号：");
            boolean flag4 = false;
            String strSubPort = null;
            do
            {
                strSubPort = breader.readLine();
                if (ValidateParamTool.validateSubPort(strSubPort))
                {
                    flag4 = true;
                }
                else
                {
                    System.out.println("扩展子号输入不合法，请重新输入");
                    System.out.print("扩展子号：");
                }
            } while (!flag4);
            
            System.out.println("请输入用户自定义流水号，不带请输入0（流水号范围-（2^63）……2^63-1）");
            System.out.print("流水号：");
            boolean flag5 = false;
            String strUserMsgId = null;
            do
            {
                strUserMsgId = breader.readLine();
                if (ValidateParamTool.validateUserMsgId(strUserMsgId))
                {
                    flag5 = true;
                }
                else
                {
                    System.out.println("流水号输入不合法，请重新输入");
                    System.out.print("流水号：");
                }
            } while (!flag5);
            
            if (type.equals("3"))
            {
                ISms sms = new CHttpSoap();
                StringBuffer strPtMsgId = new StringBuffer("");
                //短信息发送接口（相同内容群发，可自定义流水号）SOAP请求,call为空，则是短连接,call不为空，则是长连接。
                int result = sms.SendSms(strPtMsgId, strUserId, strPwd, host, phone, strMessage, strSubPort, strUserMsgId, bKeepAlive, call);
                if (result == 0)
                {
                    System.out.println("发送成功：" + strPtMsgId.toString());
                }
                else
                {
                    System.out.println("发送失败：" + strPtMsgId.toString());
                }
            }
            else if (type.equals("2"))
            {
                ISms sms = new CHttpPost();
                StringBuffer strPtMsgId = new StringBuffer("");
                //短信息发送接口（相同内容群发，可自定义流水号）POST请求,httpClient为空，则是短连接,httpClient不为空，则是长连接。
                int result = sms.SendSms(strPtMsgId, strUserId, strPwd, host, phone, strMessage, strSubPort, strUserMsgId, bKeepAlive, httpClient);
                if (result == 0)
                {
                    System.out.println("发送成功：" + strPtMsgId.toString());
                }
                else
                {
                    System.out.println("发送失败：" + strPtMsgId.toString());
                }
            }
            else if (type.equals("1"))
            {
                ISms sms = new CHttpGet();
                StringBuffer strPtMsgId = new StringBuffer("");
                //短信息发送接口（相同内容群发，可自定义流水号）GET请求,httpClient为空，则是短连接,httpClient不为空，则是长连接。
                int result = sms.SendSms(strPtMsgId, strUserId, strPwd, host, phone, strMessage, strSubPort, strUserMsgId, bKeepAlive, httpClient);
                if (result == 0)
                {
                    System.out.println("发送成功：" + strPtMsgId.toString());
                }
                else
                {
                    System.out.println("发送失败：" + strPtMsgId.toString());
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 短信息发送接口（不同内容群发，可自定义不同流水号，自定义不同扩展号）
     */
    public static void SendMultixSms()
    {
        try
        {
            System.out.println("请选择请求方式：");
            System.out.println("1: Get请求方式");
            System.out.println("2: Post请求方式");
            System.out.println("3: Soap请求方式");
            System.out.print("请求方式：");
            String type = null;
            boolean flag1 = false;
            do
            {
                type = breader.readLine();
                if (type != null && !"".equals(type) && (("1").equals(type) || ("2").equals(type) || ("3").equals(type)))
                {
                    flag1 = true;
                }
                else
                {
                    System.out.println("请求方式输入错误，请重新输入！");
                    System.out.print("请求方式：");
                }
            } while (!flag1);
            
            List<MULTIX_MT> multixMts = new ArrayList<MULTIX_MT>();
            boolean flag = false;
            //手机号码数量
            int mobileCount = 0;
            do
            {
                MULTIX_MT multixMt = new MULTIX_MT();
                System.out.println("请输入一个手机号码");
                System.out.print("手机号码：");
                boolean falgp = false;
                String strMobile = null;
                do
                {
                    strMobile = breader.readLine();
                    if (ValidateParamTool.validateMobile(strMobile))
                    {
                        falgp = true;
                        multixMt.setStrMobile(strMobile);
                    }
                    else
                    {
                        System.out.println("手机号码输入不合法，请重新输入");
                        System.out.print("手机号码：");
                    }
                } while (!falgp);
                
                System.out.println("请输入短信内容（内容长度不大于350个汉字）");
                System.out.print("短信内容：");
                boolean flag3 = false;
                String strMessage;
                do
                {
                    strMessage = breader.readLine();
                    if (ValidateParamTool.validateMessage(strMessage))
                    {
                        flag3 = true;
                        multixMt.setStrBase64Msg(strMessage);
                    }
                    else if (strMessage == null || "".equals(strMessage))
                    {
                        System.out.println("短信长度为0，请重新输入");
                        System.out.print("短信内容：");
                    }
                    else
                    {
                        System.out.println("短信长度超过350个汉字，请重新输入");
                        System.out.print("短信内容：");
                    }
                } while (!flag3);
                
                System.out.println("请输入通道号，不需要请输入*");
                System.out.print("通道号：");
                boolean flag4 = false;
                String strSpNumber = null;
                do
                {
                    strSpNumber = breader.readLine();
                    if (ValidateParamTool.validateSpNumber(strSpNumber))
                    {
                        flag4 = true;
                    }
                    else
                    {
                        System.out.println("通道号输入不合法，请重新输入");
                        System.out.print("通道号：");
                    }
                } while (!flag4);
                multixMt.setStrSpNumber(strSpNumber);
                
                System.out.println("请输入用户自定义流水号，不带请输入0（流水号范围-（2^63）……2^63-1）");
                boolean flag5 = false;
                String strUserMsgId = null;
                do
                {
                    strUserMsgId = breader.readLine();
                    if (ValidateParamTool.validateUserMsgId(strUserMsgId))
                    {
                        flag5 = true;
                    }
                    else
                    {
                        System.out.println("流水号输入不合法，请重新输入");
                        System.out.print("流水号：");
                    }
                } while (!flag5);
                multixMt.setStrUserMsgId(strUserMsgId);
                
                multixMts.add(multixMt);
                //手机号码数目加1
                mobileCount = mobileCount + 1;
                boolean flag2 = false;
                System.out.print("继续添加（Y/N）：");
                do
                {
                    String yn = breader.readLine();
                    if ("y".equals(yn) || "Y".equals(yn))
                    {
                        if (mobileCount >= 100)
                        {
                            System.out.println("手机号码个数已经有100个，不能继续添加，请重新输入");
                            System.out.print("继续添加（Y/N）：");
                        }
                        else
                        {
                            flag = true;
                            flag2 = true;
                        }
                    }
                    else if ("n".equals(yn) || "N".equals(yn))
                    {
                        flag = false;
                        flag2 = true;
                    }
                    else
                    {
                        System.out.println("继续添加（Y/N）输入不合法，请重新输入");
                        System.out.print("继续添加（Y/N）：");
                    }
                } while (!flag2);
                
            } while (flag);
            
            if (type.equals("3"))
            {
                ISms sms = new CHttpSoap();
                StringBuffer strPtMsgId = new StringBuffer("");
                //短信息发送接口（不同内容群发，可自定义不同流水号，自定义不同扩展号）SOAP请求,call为空，则是短连接,call不为空，则是长连接。
                int result = sms.SendMultixSms(strPtMsgId, strUserId, strPwd, host, multixMts, bKeepAlive, call);
                
                if (result == 0)
                {
                    System.out.println("发送成功：" + strPtMsgId.toString());
                }
                else
                {
                    System.out.println("发送失败：" + strPtMsgId.toString());
                }
            }
            else if (type.equals("2"))
            {
                ISms sms = new CHttpPost();
                StringBuffer strPtMsgId = new StringBuffer("");
                //短信息发送接口（不同内容群发，可自定义不同流水号，自定义不同扩展号）POST请求,httpClient为空，则是短连接,httpClient不为空，则是长连接。
                int result = sms.SendMultixSms(strPtMsgId, strUserId, strPwd, host, multixMts, bKeepAlive, httpClient);
                if (result == 0)
                {
                    System.out.println("发送成功：" + strPtMsgId.toString());
                }
                else
                {
                    System.out.println("发送失败：" + strPtMsgId.toString());
                }
            }
            else if (type.equals("1"))
            {
                ISms sms = new CHttpGet();
                StringBuffer strPtMsgId = new StringBuffer("");
                //短信息发送接口（不同内容群发，可自定义不同流水号，自定义不同扩展号）GET请求,httpClient为空，则是短连接,httpClient不为空，则是长连接。
                int result = sms.SendMultixSms(strPtMsgId, strUserId, strPwd, host, multixMts, bKeepAlive, httpClient);
                if (result == 0)
                {
                    System.out.println("发送成功：" + strPtMsgId.toString());
                }
                else
                {
                    System.out.println("发送失败：" + strPtMsgId.toString());
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
    /**
     * 查询余额接口
     */
    public static void QueryBalance()
    {
        try
        {
            System.out.println("请选择请求方式：");
            System.out.println("1: Get请求方式");
            System.out.println("2: Post请求方式");
            System.out.println("3: Soap请求方式");
            System.out.print("请求方式：");
            boolean flag = false;
            String type = null;
            do
            {
                type = breader.readLine();
                if (type != null && !"".equals(type) && (("1").equals(type) || ("2").equals(type) || ("3").equals(type)))
                {
                    flag = true;
                }
                else
                {
                    System.out.println("请求方式输入错误，请重新输入！");
                    System.out.print("请求方式：");
                }
            } while (!flag);
            
            if (type.equals("3"))
            {
                ISms sms = new CHttpSoap();
                StringBuffer nBalance = new StringBuffer("");
                //查询余额接口SOAP请求,call为空，则是短连接,call不为空，则是长连接。
                int result = sms.QueryBalance(nBalance, strUserId, strPwd, host, bKeepAlive, call);
                if (result == 0)
                {
                    System.out.println("查询成功：" + nBalance.toString());
                }
                else
                {
                    System.out.println("查询失败：" + nBalance.toString());
                }
                
            }
            else if (type.equals("2"))
            {
                //查询余额接口POST请求,httpClient为空，则是短连接,httpClient不为空，则是长连接。
                ISms sms = new CHttpPost();
                StringBuffer nBalance = new StringBuffer("");
                int result = sms.QueryBalance(nBalance, strUserId, strPwd, host, bKeepAlive, httpClient);
                if (result == 0)
                {
                    System.out.println("查询成功：" + nBalance.toString());
                }
                else
                {
                    System.out.println("查询失败：" + nBalance.toString());
                }
            }
            else if (type.equals("1"))
            {
                //查询余额接口GET请求,httpClient为空，则是短连接,httpClient不为空，则是长连接。
                ISms sms = new CHttpGet();
                StringBuffer nBalance = new StringBuffer("");
                int result = sms.QueryBalance(nBalance, strUserId, strPwd, host, bKeepAlive, httpClient);
                if (result == 0)
                {
                    System.out.println("查询成功：" + nBalance.toString());
                }
                else
                {
                    System.out.println("查询失败：" + nBalance.toString());
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取上行接口
     */
    public static void GetMo()
    {
        try
        {
            System.out.println("请选择请求方式：");
            System.out.println("1: Get请求方式");
            System.out.println("2: Post请求方式");
            System.out.println("3: Soap请求方式");
            System.out.print("请求方式：");
            boolean flag = false;
            String type = null;
            do
            {
                type = breader.readLine();
                if (type != null && !"".equals(type) && (("1").equals(type) || ("2").equals(type) || ("3").equals(type)))
                {
                    flag = true;
                }
                else
                {
                    System.out.println("请求方式输入错误，请重新输入！");
                    System.out.print("请求方式：");
                }
            } while (!flag);
            
            if (type.equals("3"))
            {
                //获取上行接口SOAP请求,call为空，则是短连接,call不为空，则是长连接。
                ISms sms = new CHttpSoap();
                List<MO_PACK> moPackList = sms.GetMo(strUserId, strPwd, host, bKeepAlive, call);
                new SmsTool().printMoPack(moPackList);
            }
            else if (type.equals("2"))
            {
                ISms sms = new CHttpPost();
                //获取上行接口POST请求,httpClient为空，则是短连接,httpClient不为空，则是长连接。
                List<MO_PACK> moPackList = sms.GetMo(strUserId, strPwd, host, bKeepAlive, httpClient);
                new SmsTool().printMoPack(moPackList);
                
            }
            else if (type.equals("1"))
            {
                ISms sms = new CHttpGet();
                //获取上行接口GET请求,httpClient为空，则是短连接,httpClient不为空，则是长连接。
                List<MO_PACK> moPackList = sms.GetMo(strUserId, strPwd, host, bKeepAlive, httpClient);
                new SmsTool().printMoPack(moPackList);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取状态报告接口
     */
    public static void GetRpt()
    {
        try
        {
            System.out.println("请选择请求方式：");
            System.out.println("1: Get请求方式");
            System.out.println("2: Post请求方式");
            System.out.println("3: Soap请求方式");
            System.out.print("请求方式：");
            boolean flag = false;
            String type = null;
            do
            {
                type = breader.readLine();
                if (type != null && !"".equals(type) && (("1").equals(type) || ("2").equals(type) || ("3").equals(type)))
                {
                    flag = true;
                }
                else
                {
                    System.out.println("请求方式输入错误，请重新输入！");
                    System.out.print("请求方式：");
                }
            } while (!flag);
            
            if (type.equals("3"))
            {
                ISms sms = new CHttpSoap();
                //获取状态报告接口SOAP请求,call为空，则是短连接,call不为空，则是长连接。
                List<RPT_PACK> rptPackList = sms.GetRpt(strUserId, strPwd, host, bKeepAlive, call);
                new SmsTool().printRptPack(rptPackList);
            }
            else if (type.equals("2"))
            {
                ISms sms = new CHttpPost();
                //获取状态报告接口POST请求,httpClient为空，则是短连接,httpClient不为空，则是长连接。
                List<RPT_PACK> rptPackList = sms.GetRpt(strUserId, strPwd, host, bKeepAlive, httpClient);
                new SmsTool().printRptPack(rptPackList);
            }
            else if (type.equals("1"))
            {
                //获取状态报告接口GET请求,httpClient为空，则是短连接,httpClient不为空，则是长连接。
                ISms sms = new CHttpGet();
                List<RPT_PACK> rptPackList = sms.GetRpt(strUserId, strPwd, host, bKeepAlive, httpClient);
                new SmsTool().printRptPack(rptPackList);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
