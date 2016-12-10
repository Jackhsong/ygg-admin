package com.ygg.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.code.SmsServiceTypeEnum;
import com.ygg.admin.dao.RecordDao;
import com.ygg.admin.service.SmsService;
import com.ygg.admin.util.*;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.*;

@Service
public class SmsServiceImpl implements SmsService
{
    
    Logger log = Logger.getLogger(SmsServiceImpl.class);
    
    @Resource
    private RecordDao recordDao;
    
    @Override
    public double queryMoney()
    {
        try
        {
            return YimeiUtil.getBalance();
        }
        catch (Exception e)
        {
            log.error("查询亿美短信余额失败", e);
            return -1;
        }
        
    }
    
    @Override
    public int send(Map<String, Object> para)
    {
        Integer type = (Integer)para.get("type");
        if (type == null)
        {
            type = CommonEnum.SmsTypeEnum.CUSTOM.ordinal();
        }
        Integer serviceType = (Integer)para.get("serviceType");
        String[] strArr = (String[])para.get("phone");
        String content = para.get("content") + "";
        try
        {
            YimeiUtil.sendSMS(strArr, content, 5);
        }
        catch (Exception e)
        {
            log.error("发送短信失败", e);
            return 0;
        }
        // 插入记录
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("mobileNumber", strArr[0]);
        map.put("type", type);
        map.put("serviceType", serviceType == null ? SmsServiceTypeEnum.YIMEI.getValue() : serviceType.intValue());
        if (content.length() > 200)
        {
            map.put("content", content.substring(0, 190) + "...");
        }
        else
        {
            map.put("content", content);
        }
        try
        {
            recordDao.insertSmsContentRecord(map);
        }
        catch (Exception e)
        {
            log.error("发送短信失败插入记录失败", e);
        }
        return 1;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public int sendMontnets(Map<String, Object> para)
        throws Exception
    {
        Integer type = (Integer)para.get("type");
        if (type == null)
        {
            type = CommonEnum.SmsTypeEnum.CUSTOM.ordinal();
        }
        Integer serviceType = (Integer)para.get("serviceType");
        String phoneNumber = para.get("phoneNumber") == null ? "" : para.get("phoneNumber") + "";
        String content = para.get("content") == null ? "" : para.get("content") + "";
        Set<String> phoneNumberSet = para.get("phoneNumberSet") == null ? new HashSet<String>() : (Set<String>)para.get("phoneNumberSet");
        if (StringUtils.isNotEmpty(phoneNumber))
        {
            new MontnetsGGJUtil().sendSms(phoneNumber, content);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("mobileNumber", phoneNumber);
            map.put("type", type);
            map.put("serviceType", serviceType == null ? SmsServiceTypeEnum.MENGWANG.getValue() : (Integer)serviceType);
            if (content.length() > 200)
            {
                map.put("content", content.substring(0, 190) + "...");
            }
            else
            {
                map.put("content", content);
            }
            recordDao.insertSmsContentRecord(map);
        }
        if (phoneNumberSet != null && phoneNumberSet.size() > 0)
        {
            new MontnetsGGJUtil().sendSms(phoneNumberSet, content);
            for (String phone : phoneNumberSet)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("mobileNumber", phone);
                map.put("type", type);
                map.put("serviceType", serviceType == null ? SmsServiceTypeEnum.MENGWANG.getValue() : (Integer)serviceType);
                if (content.length() > 200)
                {
                    map.put("content", content.substring(0, 190) + "...");
                }
                else
                {
                    map.put("content", content);
                }
                recordDao.insertSmsContentRecord(map);
            }
        }
        return 1;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public int sendMontnetsTuan(Map<String, Object> para)
        throws Exception
    {
        Integer type = (Integer)para.get("type");
        if (type == null)
        {
            type = CommonEnum.SmsTypeEnum.CUSTOM.ordinal();
        }
        Integer serviceType = (Integer)para.get("serviceType");
        String phoneNumber = para.get("phoneNumber") == null ? "" : para.get("phoneNumber") + "";
        String content = para.get("content") == null ? "" : para.get("content") + "";
        Set<String> phoneNumberSet = para.get("phoneNumberSet") == null ? new HashSet<String>() : (Set<String>)para.get("phoneNumberSet");
        if (StringUtils.isNotEmpty(phoneNumber))
        {
            new MontnetsTuanUtil().sendSms(phoneNumber, content);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("mobileNumber", phoneNumber);
            map.put("type", type);
            map.put("serviceType", serviceType == null ? SmsServiceTypeEnum.MENGWANG.getValue() : (Integer)serviceType);
            if (content.length() > 200)
            {
                map.put("content", content.substring(0, 190) + "...");
            }
            else
            {
                map.put("content", content);
            }
            recordDao.insertSmsContentRecord(map);
        }
        if (phoneNumberSet != null && phoneNumberSet.size() > 0)
        {
            new MontnetsTuanUtil().sendSms(phoneNumberSet, content);
            for (String phone : phoneNumberSet)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("mobileNumber", phone);
                map.put("type", type);
                map.put("serviceType", serviceType == null ? SmsServiceTypeEnum.MENGWANG.getValue() : (Integer)serviceType);
                if (content.length() > 200)
                {
                    map.put("content", content.substring(0, 190) + "...");
                }
                else
                {
                    map.put("content", content);
                }
                recordDao.insertSmsContentRecord(map);
            }
        }
        return 1;
    }

    @SuppressWarnings("unchecked")
    @Override
    public int sendMontnetsGlobal(Map<String, Object> para)
            throws Exception
    {
        Integer type = (Integer)para.get("type");
        if (type == null)
        {
            type = CommonEnum.SmsTypeEnum.CUSTOM.ordinal();
        }
        Integer serviceType = (Integer)para.get("serviceType");
        String phoneNumber = para.get("phoneNumber") == null ? "" : para.get("phoneNumber") + "";
        String content = para.get("content") == null ? "" : para.get("content") + "";
        Set<String> phoneNumberSet = para.get("phoneNumberSet") == null ? new HashSet<String>() : (Set<String>)para.get("phoneNumberSet");
        if (StringUtils.isNotEmpty(phoneNumber))
        {
            new MontnetsGlobalUtil().sendSms(phoneNumber, content);
            Map<String, Object> map = new HashMap<>();
            map.put("mobileNumber", phoneNumber);
            map.put("type", type);
            map.put("serviceType", serviceType == null ? SmsServiceTypeEnum.MENGWANG.getValue() : serviceType);
            if (content.length() > 200)
            {
                map.put("content", content.substring(0, 190) + "...");
            }
            else
            {
                map.put("content", content);
            }
            recordDao.insertSmsContentRecord(map);
        }
        if (phoneNumberSet != null && phoneNumberSet.size() > 0)
        {
            new MontnetsGlobalUtil().sendSms(phoneNumberSet, content);
            for (String phone : phoneNumberSet)
            {
                Map<String, Object> map = new HashMap<>();
                map.put("mobileNumber", phone);
                map.put("type", type);
                map.put("serviceType", serviceType == null ? SmsServiceTypeEnum.MENGWANG.getValue() : serviceType);
                if (content.length() > 200)
                {
                    map.put("content", content.substring(0, 190) + "...");
                }
                else
                {
                    map.put("content", content);
                }
                recordDao.insertSmsContentRecord(map);
            }
        }
        return 1;
    }
    
    @Override
    public String jsoninfo(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = recordDao.findSmsContentRecordByPara(para);
        int total = 0;
        if (reList.size() > 0)
        {
            total = recordDao.countSmsContentRecordByPara(para);
            for (Map<String, Object> map : reList)
            {
                map.put("typeStr", CommonEnum.SmsTypeEnum.getDescriptionByOrdinal(Integer.valueOf(map.get("type") + "")));
                map.put("serviceType", SmsServiceTypeEnum.getName(Integer.valueOf(map.get("serviceType") + "")));
                map.put("sendTimeStr", map.get("sendTime").toString());
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rows", reList);
        map.put("total", total);
        return JSON.toJSONString(map);
    }
    
}
