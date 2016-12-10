package com.ygg.admin.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.axis.client.Call;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.log4j.Logger;

import com.ygg.admin.sdk.montnets.common.EnvironmentTypeEnum;
import com.ygg.admin.sdk.montnets.common.ISms;
import com.ygg.admin.sdk.montnets.common.MULTIX_MT;
import com.ygg.admin.sdk.montnets.common.MethodTypeEnum;
import com.ygg.admin.sdk.montnets.common.RPT_PACK;
import com.ygg.admin.sdk.montnets.common.ValidateParamTool;

/**
 * 梦网短信 
 * @author xiongl
 *
 */
public abstract class MontnetsUtil
{
    private static Logger logger = Logger.getLogger(MontnetsUtil.class);
    
    HttpClient httpClient = null;
    
    Call call = null;
    
    boolean bKeepAlive = false;
    
    ISms sms = null;
    
    //登录名
    String username = null;
    
    //密码
    String password = null;
    
    //服务地址
    String host = null;
    
    //扩展子号 （不带请填星号*，长度不大于6位）
    String subPort = "*";
    
    //用户自定义流水号，不带请输入0（流水号范围-（2^63）……2^63-1）
    String customMsgId = "0";
    
    //通道号（可填完整,可不填,可填*,可只填扩展）
    String spNumber = "*";
    
    //每批发送短信最大条数
    final int PAGESIZE = 100;
    
    public abstract void init(Integer methodType, Integer envType) throws Exception;
    
    
    @SuppressWarnings("deprecation")
    private void destory()
    {
        sms = null;
        call = null;
        host = null;
        username = null;
        password = null;
        if (httpClient != null)
        {
            httpClient.getConnectionManager().shutdown();
        }
    }
    
    /**
     * 查询短信剩余可用条数，默认GET请求，调用生产帐号
     * @return
     * @throws Exception
     */
    public int getBalance()
        throws Exception
    {
        return getBalance(MethodTypeEnum.GET.getValue(), EnvironmentTypeEnum.PROD.getValue());
        
    }
    
    /**
     * 查询短信剩余可用条数
     * @param envType：1：生产帐号，2：服务帐号
     * @return
     * @throws Exception
     */
    public int getBalance(Integer envType)
        throws Exception
    {
        return getBalance(MethodTypeEnum.GET.getValue(), envType);
        
    }
    
    /**
     * 查询短信剩余可用条数
     * @param methodType：请求方法类型，1：GET请求；2：POST请求；3：SOAP请求
     * @param envType：服务类型，1：生产帐号；2：服务帐号
     * @return
     * @throws Exception
     */
    public int getBalance(Integer methodType, Integer envType)
        throws Exception
    {
        try
        {
            init(methodType, envType);
            StringBuffer param = new StringBuffer("");
            int status = 0;
            if (methodType == null || MethodTypeEnum.GET.getValue() == methodType || MethodTypeEnum.POST.getValue() == methodType)
            {
                status = sms.QueryBalance(param, username, password, host, bKeepAlive, httpClient);
            }
            else if (MethodTypeEnum.SOAP.getValue() == methodType)
            {
                status = sms.QueryBalance(param, username, password, host, bKeepAlive, call);
            }
            else
            {
                throw new Exception("【梦网短信】查询短信可用条数参数错误：" + methodType);
            }
            if (status == 0)
            {
                logger.debug("【梦网短信】查询短信可用条数成功：" + param.toString());
                return Integer.valueOf(param.toString()).intValue();
            }
            else
            {
                throw new Exception("【梦网短信】查询短信可用条数失败：errorCode=" + param.toString());
            }
        }
        catch (Exception e)
        {
            throw new Exception("【梦网短信】查询短信剩余条数失败");
        }
        finally
        {
            destory();
        }
        
    }
    
    /**
     * 发送单条短信，默认用GET方式、生产帐号发送
     * @param phoneNumber：手机号
     * @param smsContent：短信内容
     */
    public void sendSms(String phoneNumber, String smsContent)
    {
        sendSms(phoneNumber, smsContent, MethodTypeEnum.GET.getValue(), EnvironmentTypeEnum.PROD.getValue());
    }
    
    /**
     * 发送单条短信，默认用生产帐号发送
     * @param phoneNumber：手机号
     * @param smsContent：短信内容
     * @param methodType：发送方式，1：GET请求；2：POST请求；3：SOAP请求
     */
    public void sendSms(String phoneNumber, String smsContent, Integer methodType)
    {
        sendSms(phoneNumber, smsContent, methodType, EnvironmentTypeEnum.PROD.getValue());
    }
    
    /**
     * 发送单条短信
     * @param phoneNumber：手机号
     * @param smsContent：短信内容
     * @param methodType：请求方法类型，1：GET请求；2：POST请求；3：SOAP请求
     * @param envType：帐号类型，1：生产帐号；2：服务帐号
     */
    public void sendSms(String phoneNumber, String smsContent, Integer methodType, Integer envType)
    {
        try
        {
            init(methodType, envType);
            if (!ValidateParamTool.validateMobile(phoneNumber))
            {
                throw new Exception("【梦网短信】发送短信手机号错误：" + phoneNumber);
            }
            if (!ValidateParamTool.validateMessage(smsContent))
            {
                throw new Exception("【梦网短信】发送短信短信内容错误：短信内容为空，或超过350个汉字");
            }
            //平台返回的流水号
            StringBuffer param = new StringBuffer("");
            int status = -1;
            if (methodType == null || MethodTypeEnum.GET.getValue() == methodType || MethodTypeEnum.POST.getValue() == methodType)
            {
                status = sms.SendSms(param, username, password, host, phoneNumber, smsContent, subPort, customMsgId, bKeepAlive, httpClient);
            }
            else if (MethodTypeEnum.SOAP.getValue() == methodType)
            {
                status = sms.SendSms(param, username, password, host, phoneNumber, smsContent, subPort, customMsgId, bKeepAlive, call);
            }
            else
            {
                throw new Exception("【梦网短信】发送短信类型参数错误：" + methodType);
            }
            if (status == 0)
            {
                logger.debug("【梦网短信】短信发送成功，流水号为：" + param.toString());
            }
            else
            {
                logger.error("【梦网短信】短信发送失败，errorCode=" + param.toString());
            }
        }
        catch (Exception e)
        {
            logger.error("【梦网短信】发送短信失败", e);
        }
        finally
        {
            destory();
        }
        
    }
    
    /**
     * 批量发送短信：默认用生产帐号、GET方式发送
     * @param phoneNumbers：手机号
     * @param smsContent：短信内容
     */
    public void sendSms(List<String> phoneNumbers, String smsContent)
    {
        sendSms(phoneNumbers, smsContent, MethodTypeEnum.GET.getValue(), EnvironmentTypeEnum.PROD.getValue());
    }
    
    /**
     * 批量发送短信：默认用GET方式发送
     * @param phoneNumbers
     * @param smsContent
     * @param envType：帐号类型，1：生产帐号；2：服务帐号
     */
    public void sendSms(List<String> phoneNumbers, String smsContent, Integer envType)
    {
        sendSms(phoneNumbers, smsContent, MethodTypeEnum.GET.getValue(), envType);
    }
    
    /**
     * 批量发送短信：手机号允许重复
     * @param phoneNumbers：手机号集合
     * @param smsContext：短信内容
     * @param methodType：请求方法类型，1：GET请求；2：POST请求；3：SOAP请求
     * @param envType：帐号类型，1：生产帐号；2：服务帐号
     **/
    public void sendSms(List<String> phoneNumbers, String smsContent, Integer methodType, Integer envType)
    {
        try
        {
            if (phoneNumbers == null || phoneNumbers.isEmpty())
            {
                throw new Exception("【梦网短信】发送短信参数错误：手机号为空");
            }
            sendSms(new HashSet<String>(phoneNumbers), smsContent, methodType, envType);
        }
        catch (Exception e)
        {
            logger.error("【梦网短信】批量发送短信失败", e);
        }
        finally
        {
            destory();
        }
        
    }
    
    /**
     * 批量发送短信
     * @param phoneNumbers：手机号
     * @param smsContent：短信内容
     */
    public void sendSms(Set<String> phoneNumbers, String smsContent)
    {
        sendSms(phoneNumbers, smsContent, MethodTypeEnum.GET.getValue(), EnvironmentTypeEnum.PROD.getValue());
    }
    
    /**
     * 批量发送短信：默认用GET方式发送
     * @param phoneNumbers
     * @param smsContent
     * @param envType：帐号类型，1：生产帐号；2：服务帐号
     */
    public void sendSms(Set<String> phoneNumbers, String smsContent, Integer envType)
    {
        sendSms(phoneNumbers, smsContent, MethodTypeEnum.GET.getValue(), envType);
    }
    
    /**
     * 批量发送短信：手机号不允许重复
     * @param phoneNumbers：手机号集合
     * @param smsContext：短信内容
     * @param methodType：请求方法类型，1：GET请求；2：POST请求；3：SOAP请求
     * @param envType：帐号类型，1：生产帐号；2：服务帐号
     */
    public void sendSms(Set<String> phoneNumbers, String smsContent, Integer methodType, Integer envType)
    {
        try
        {
            init(methodType, envType);
            if (phoneNumbers == null || phoneNumbers.size() == 0)
            {
                throw new Exception("【梦网短信】发送短信参数错误：手机号为空");
            }
            
            int status = -1;
            //平台返回的流水号
            StringBuffer param = new StringBuffer("");
            List<List<String>> splitNumberList = CommonUtil.splitList(new ArrayList<String>(phoneNumbers), PAGESIZE);
            for (List<String> numberList : splitNumberList)
            {
                StringBuilder phones = new StringBuilder("");
                for (String phoneNumber : numberList)
                {
                    if (!ValidateParamTool.validateMobile(phoneNumber))
                    {
                        throw new Exception("【梦网短信】发送短信手机号错误：" + phoneNumber);
                    }
                    if (StringUtils.isEmpty(phones.toString()))
                    {
                        phones.append(phoneNumber);
                    }
                    else
                    {
                        phones.append(",").append(phoneNumber);
                    }
                }
                if (!ValidateParamTool.validateMessage(smsContent))
                {
                    throw new Exception("【梦网短信】发送短信短信内容错误：短信内容为空，或超过350个汉字");
                }
                if (methodType == null || MethodTypeEnum.GET.getValue() == methodType || MethodTypeEnum.POST.getValue() == methodType)
                {
                    status = sms.SendSms(param, username, password, host, phones.toString(), smsContent, subPort, customMsgId, bKeepAlive, httpClient);
                    if (status != 0)
                    {
                        break;
                    }
                }
                else if (MethodTypeEnum.SOAP.getValue() == methodType)
                {
                    status = sms.SendSms(param, username, password, host, phones.toString(), smsContent, subPort, customMsgId, bKeepAlive, call);
                    if (status != 0)
                    {
                        break;
                    }
                }
                else
                {
                    throw new Exception("【梦网短信】发送短信类型参数错误：" + methodType);
                }
            }
            
            if (status == 0)
            {
                logger.debug("【梦网短信】短信发送成功，流水号为：" + param.toString());
            }
            else
            {
                logger.error("【梦网短信】短信发送失败，errorCode=" + param.toString());
            }
        }
        catch (Exception e)
        {
            logger.error("【梦网短信】批量发送短信失败", e);
        }
        finally
        {
            destory();
        }
    }
    
    /**
     * 不同内容批量发送短息,默认使用GET方式、生产帐号发送
     * @param phoneAndContentMap：key为手机号，value为短信内容
     */
    public void sendMultContentSms(List<Map<String, String>> phoneAndContentList)
    {
        sendMultContentSms(phoneAndContentList, MethodTypeEnum.GET.getValue(), EnvironmentTypeEnum.PROD.getValue());
    }
    
    /**
     * 不同内容批量发送短息，默认使用GET方式发送
     * @param phoneAndContentMap：key为手机号，value为短信内容
     * @param envType：帐号类型，1：生产帐号；2：服务帐号
     */
    public void sendMultContentSms(List<Map<String, String>> phoneAndContentList, Integer envType)
    {
        sendMultContentSms(phoneAndContentList, MethodTypeEnum.GET.getValue(), envType);
    }
    
    /**
     * 不同内容批量发送短息
     * @param phoneAndContentList,手机号和短信内容的集合，每个元素为Map,Map中key=phone,存放手机号，key=content,存放短信内容
     * @param methodType：请求方法类型，1：GET请求；2：POST请求；3：SOAP请求
     * @param envType：帐号类型，1：生产帐号；2：服务帐号
     */
    public void sendMultContentSms(List<Map<String, String>> phoneAndContentList, Integer methodType, Integer envType)
    {
        try
        {
            init(methodType, envType);
            if (phoneAndContentList == null || phoneAndContentList.isEmpty())
            {
                throw new Exception("【梦网短信】批量发送短信参数错误：手机号和短信内容为空");
            }
            
            List<MULTIX_MT> multixMts = new ArrayList<MULTIX_MT>();
            Map<String, List<String>> phoneAndContentMap = new HashMap<>();
            
            //将短信内容相同的手机号放在同一个List中
            for (Map<String, String> map : phoneAndContentList)
            {
                String phoneNumber = map.get("phone");
                String content = map.get("content");
                List<String> phonesList = phoneAndContentMap.get(content);
                if (phonesList == null)
                {
                    phonesList = new ArrayList<>();
                    phonesList.add(phoneNumber);
                    phoneAndContentMap.put(content, phonesList);
                }
                else
                {
                    phonesList.add(phoneNumber);
                }
            }
            
            //过滤内容不同、手机号不同的短信
            Iterator<Entry<String, List<String>>> iterator = phoneAndContentMap.entrySet().iterator();
            while (iterator.hasNext())
            {
                Entry<String, List<String>> entry = iterator.next();
                String content = entry.getKey();
                List<String> phoneNumbers = entry.getValue();
                if (phoneNumbers == null || phoneNumbers.size() == 0)
                {
                    iterator.remove();
                }
                else if (phoneNumbers.size() == 1)
                {
                    MULTIX_MT multixMt = new MULTIX_MT();
                    if (!ValidateParamTool.validateMessage(content))
                    {
                        throw new Exception("【梦网短信】发送短信短信内容错误：短信内容为空，或超过350个汉字");
                    }
                    if (!ValidateParamTool.validateMobile(phoneNumbers.get(0)))
                    {
                        throw new Exception("【梦网短信】发送短信参数错误：手机号错误");
                    }
                    multixMt.setStrMobile(phoneNumbers.get(0));
                    multixMt.setStrBase64Msg(content);
                    multixMt.setStrUserMsgId(customMsgId);
                    multixMt.setStrSpNumber(spNumber);
                    multixMts.add(multixMt);
                    iterator.remove();
                }
            }
            
            //发送内容不同的短信
            int status = -1;
            if (multixMts.size() > 0)
            {
                //每批发送最大100条
                List<List<MULTIX_MT>> splitList = CommonUtil.splitList(multixMts, PAGESIZE);
                for (List<MULTIX_MT> mlts : splitList)
                {
                    StringBuffer param = new StringBuffer("");
                    if (methodType == null || MethodTypeEnum.GET.getValue() == methodType || MethodTypeEnum.POST.getValue() == methodType)
                    {
                        status = sms.SendMultixSms(param, username, password, host, mlts, bKeepAlive, httpClient);
                    }
                    else if (MethodTypeEnum.SOAP.getValue() == methodType)
                    {
                        status = sms.SendMultixSms(param, username, password, host, mlts, bKeepAlive, call);
                    }
                    else
                    {
                        throw new Exception("【梦网短信】发送短信类型参数错误：" + methodType);
                    }
                    if (status == 0)
                    {
                        logger.debug("【梦网短信】短信发送成功，流水号为：" + param.toString());
                    }
                    else
                    {
                        logger.error("【梦网短信】短信发送失败，errorCode=" + param.toString());
                    }
                }
            }
            
            //发送内容相同的短信
            if (phoneAndContentMap.size() > 0)
            {
                for (Entry<String, List<String>> entry : phoneAndContentMap.entrySet())
                {
                    String content = entry.getKey();
                    List<String> phoneNumbers = entry.getValue();
                    if (phoneNumbers != null && phoneNumbers.size() > 0)
                    {
                        List<List<String>> splitNumbers = CommonUtil.splitList(phoneNumbers, PAGESIZE);
                        for (List<String> phoneNumber : splitNumbers)
                        {
                            sendSms(phoneNumber, content, methodType, envType);
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error("【梦网短信】批量发送短信失败", e);
        }
        finally
        {
            destory();
        }
    }
    
    /**
     * 获取短信发送状态信息，默认用生产帐号、GET方式发送
     */
    public List<Map<String, String>> getReport()
    {
        return getReport(MethodTypeEnum.GET.getValue(), EnvironmentTypeEnum.PROD.getValue());
    }
    
    /**
     * 获取短信发送状态信息，默认用GET方式发送
     * @param envType：帐号类型，1：生产帐号；2：服务帐号
     */
    public List<Map<String, String>> getReport(Integer envType)
    {
        return getReport(MethodTypeEnum.GET.getValue(), envType);
    }
    
    /**
     * 获取短信发送状态信息
     * @param methodType：请求方法类型，1：GET请求；2：POST请求；3：SOAP请求
     * @param envType：帐号类型，1：生产帐号；2：服务帐号
     */
    public List<Map<String, String>> getReport(Integer methodType, Integer envType)
    {
        List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
        try
        {
            init(methodType, envType);
            List<RPT_PACK> rptPackList = null;
            if (methodType == null || MethodTypeEnum.GET.getValue() == methodType || MethodTypeEnum.POST.getValue() == methodType)
            {
                rptPackList = sms.GetRpt(username, password, host, bKeepAlive, httpClient);
            }
            else if (MethodTypeEnum.SOAP.getValue() == methodType)
            {
                rptPackList = sms.GetRpt(username, password, host, bKeepAlive, call);
            }
            else
            {
                throw new Exception("【梦网短信】获取状态报告参数错误：" + methodType);
            }
            if (rptPackList == null || rptPackList.size() == 0)
            {
                return null;
            }
            for (RPT_PACK rpt : rptPackList)
            {
                Map<String, String> report = new HashMap<String, String>();
                report.put("time", rpt.getStrMoTime());/*状态报告时间*/
                report.put("msgId", rpt.getStrPtMsgId());/*消息编号*/
                report.put("mobile", rpt.getStrMobile());/*手机号*/
                report.put("spNumber", rpt.getStrSpNumber());/*通道号*/
                report.put("customMsgId", rpt.getStrUserMsgId());/*用户自定义的消息编号*/
                report.put("status", rpt.getnStatus() + "");/*发送状态,0:成功 非0:失败*/
                report.put("errorCode", rpt.getStrErCode());/*状态报告错误代码*/
                resultList.add(report);
                logger.debug(report.toString());
            }
            return resultList;
        }
        catch (Exception e)
        {
            logger.error("【梦网短信】获取状态报告失败", e);
            return null;
        }
        finally
        {
            destory();
        }
        
    }
    
    public static void main(String[] args)
        throws Exception
    {
        //getBalance(EnvironmentTypeEnum.PROD.getValue());
        //getBalance(EnvironmentTypeEnum.SERVICE.getValue());
        
        //18664573290
        //sendSms("15968835661", "这是一条测试短信");
        
        /*List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("phone", "15968835661");
        map1.put("content", "test2");
        list.add(map1);
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("phone", "13738043225");
        map2.put("content", "test2");
        list.add(map2);
        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("phone", "18664573290");
        map3.put("content", "这是一条测试短信");
        list.add(map3);*/
        //sendMultContentSms(list);
        //sendSms(phones, "这是一条测试短信", EnvironmentTypeEnum.PROD.getValue());
        //sendSms(phones, "这是一条测试短信", EnvironmentTypeEnum.SERVICE.getValue());
        //        System.out.println(getBalance(EnvironmentTypeEnum.PROD.getValue()));
        //getBalance(EnvironmentTypeEnum.SERVICE.getValue());
        //        List<Map<String, String>> reportMap1 = getReport(EnvironmentTypeEnum.PROD.getValue());
        //        if (reportMap1 != null)
        //        {
        //            for (Map<String, String> report : reportMap1)
        //            {
        //                logger.info(report.toString());
        //            }
        //        }
//        List<Map<String, String>> reportMap2 = getReport(EnvironmentTypeEnum.SERVICE.getValue());
//        if (reportMap2 != null)
//        {
//            for (Map<String, String> report : reportMap2)
//            {
//                logger.info(report.toString());
//            }
//        }
        
    }
}
