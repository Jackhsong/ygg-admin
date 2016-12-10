package com.ygg.admin.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.code.SmsServiceTypeEnum;
import com.ygg.admin.dao.RecordDao;
import com.ygg.admin.service.SmsService;
import com.ygg.admin.util.CommonUtil;
import com.ygg.admin.util.MontnetsGGJUtil;
import com.ygg.admin.util.MontnetsGlobalUtil;
import com.ygg.admin.util.MontnetsTuanUtil;
import com.ygg.admin.util.MontnetsUtil;
import com.ygg.admin.util.POIUtil;

@Controller
@RequestMapping("/sms")
public class SmsController
{
    
    Logger log = Logger.getLogger(SmsController.class);
    
    @Resource
    private SmsService smsService;
    
    @Resource
    private RecordDao recordDao;
    
    @RequestMapping("/list")
    public ModelAndView list(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("sms/list");
        return mv;
    }
    
    /**
     * 短信发送内容
     * 
     * @throws Exception
     */
    @RequestMapping(value = "/jsonInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonInfo(HttpServletRequest request, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "30") int rows,
        @RequestParam(value = "mobileNumber", required = false, defaultValue = "") String mobileNumber,
        @RequestParam(value = "sendTimeBegin", required = false, defaultValue = "") String sendTimeBegin,
        @RequestParam(value = "sendTimeEnd", required = false, defaultValue = "") String sendTimeEnd, @RequestParam(value = "type", required = false, defaultValue = "0") int type)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        if (!"".equals(mobileNumber))
        {
            para.put("mobileNumber", mobileNumber);
        }
        if (!"".equals(sendTimeBegin))
        {
            para.put("sendTimeBegin", sendTimeBegin);
        }
        if (!"".equals(sendTimeEnd))
        {
            para.put("sendTimeEnd", sendTimeEnd);
        }
        if (type != 0)
        {
            para.put("type", type);
        }
        String jsonInfoString = smsService.jsoninfo(para);
        return jsonInfoString;
    }
    
    @RequestMapping("/sendForm")
    public ModelAndView sendForm(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("sms/send");
        double balance = smsService.queryMoney();
        int montnets = new MontnetsGGJUtil().getBalance();
        int montnetsTuan = new MontnetsTuanUtil().getBalance();
        int montnetsGlobal = new MontnetsGlobalUtil().getBalance();
        mv.addObject("balance", (balance > 0) ? balance + "" : "查询失败");
        mv.addObject("montnets", (montnets > 0) ? montnets + "" : "查询失败");
        mv.addObject("montnetsTuan", (montnetsTuan > 0) ? montnetsTuan + "" : "查询失败");
        mv.addObject("montnetsGlobal", (montnetsGlobal > 0) ? montnetsGlobal + "" : "查询失败");
        return mv;
    }
    
    /**
     * 发送亿美短信
     * 
     * @param request
     * @param phone
     * @param content
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "send", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "短信管理-发送亿美短信")
    public String send(HttpServletRequest request, String content, @RequestParam(value = "phoneFile", required = false) MultipartFile file,
        @RequestParam(value = "phone", required = false, defaultValue = "") String phone)
        throws Exception
    {
        Map<String, Object> para = null;
        if (!"".equals(phone))
        {
            // 手机不为空
            para = new HashMap<String, Object>();
            String[] arr = null;
            arr = new String[1];
            arr[0] = phone;
            if (!CommonUtil.isMobile(phone))
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("status", 0);
                map.put("msg", " 手机号码不符合格式");
                return JSON.toJSONString(map);
            }
            para.put("phone", arr);
            para.put("content", content);
            para.put("serviceType", SmsServiceTypeEnum.YIMEI.getValue());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", 1);
            map.put("msg", " 发送成功");
            int resultStatus = smsService.send(para);
            if (resultStatus != 1)
            {
                map.put("msg", "发送失败");
            }
            return JSON.toJSONString(map);
        }
        else
        {
            StringBuilder sb = new StringBuilder();
            int okNum = 0;
            int wrongNum = 0;
            if (file == null)
            {
                Map<String, Object> resultMap = new HashMap<String, Object>();
                resultMap.put("status", 0);
                resultMap.put("msg", "文件为空");
                return JSON.toJSONString(resultMap);
            }
            Map<String, Object> data = POIUtil.getSheetDataAtIndex(file.getInputStream(), 0, true);
            if (data == null)
            {
                Map<String, Object> resultMap = new HashMap<String, Object>();
                resultMap.put("status", 0);
                resultMap.put("msg", "文件为空");
                return JSON.toJSONString(resultMap);
            }
            List<Map<String, Object>> dataList = (List<Map<String, Object>>)data.get("data");
            Set<String> phoneSet = new HashSet<String>();
            for (Map<String, Object> mp : dataList)
            {
                if (mp.get("cell0") != null)
                {
                    phoneSet.add(mp.get("cell0") + "");
                }
            }
            for (String currPhone : phoneSet)
            {
                if (CommonUtil.isMobile(currPhone))
                {
                    para = new HashMap<String, Object>();
                    String[] arr = null;
                    arr = new String[1];
                    arr[0] = currPhone;
                    para.put("phone", arr);
                    para.put("content", content);
                    para.put("serviceType", SmsServiceTypeEnum.YIMEI.getValue());
                    int resultStatus = smsService.send(para);
                    if (resultStatus == 0)
                    {
                        sb.append(currPhone).append(",");
                        wrongNum++;
                    }
                    else
                    {
                        okNum++;
                    }
                }
                else
                {
                    wrongNum++;
                    sb.append(currPhone).append(",");
                }
            }
            //            workbook.close();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", 1);
            log.info("批量发送短信失败列表：" + sb.toString());
            map.put("msg", " 发送结果:成功" + okNum + "条,失败" + wrongNum + "条,失败短信:" + sb.toString());
            return JSON.toJSONString(map);
        }
    }
    
    /**
     * 发送梦网短信
     * @param content
     * @param file
     * @param phone
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "sendMontnets", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "短信管理-发送左岸城堡梦网短信")
    public String sendMontnets(@RequestParam(value = "content", required = true) String content,//
        @RequestParam(value = "phoneFile", required = false) MultipartFile file,//
        @RequestParam(value = "phone", required = false, defaultValue = "") String phone)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("serviceType", SmsServiceTypeEnum.MENGWANG.getValue());
            para.put("content", content);
            if (!"".equals(phone))
            {
                if (!CommonUtil.isMobile(phone))
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", " 手机号码不符合格式");
                    return JSON.toJSONString(resultMap);
                }
                para.put("phoneNumber", phone);
                int resultStatus = smsService.sendMontnets(para);
                if (resultStatus == 1)
                {
                    resultMap.put("status", 1);
                    resultMap.put("msg", " 发送成功");
                }
                else
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "发送失败");
                }
            }
            else
            {
                if (file == null)
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "文件为空");
                    return JSON.toJSONString(resultMap);
                }
                Map<String, Object> data = POIUtil.getSheetDataAtIndex(file.getInputStream(), 0, true);
                if (data == null)
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "文件为空");
                    return JSON.toJSONString(resultMap);
                }
                
                List<Map<String, Object>> dataList = (List<Map<String, Object>>)data.get("data");
                Set<String> phoneSet = new HashSet<String>();
                for (Map<String, Object> mp : dataList)
                {
                    if (mp.get("cell0") != null)
                    {
                        phoneSet.add(mp.get("cell0") + "");
                    }
                }
                para.put("phoneNumberSet", phoneSet);
                int resultStatus = smsService.sendMontnets(para);
                if (resultStatus == 1)
                {
                    resultMap.put("status", 1);
                    resultMap.put("msg", " 发送成功");
                }
                else
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "发送失败");
                }
            }
        }
        catch (Exception e)
        {
            log.error("发送左岸城堡梦网短信失败", e);
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "sendMontnetsTuan", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "短信管理-发送左岸城堡梦网短信")
    public String sendMontnetsTuan(@RequestParam(value = "content", required = true) String content,//
        @RequestParam(value = "phoneFile", required = false) MultipartFile file,//
        @RequestParam(value = "phone", required = false, defaultValue = "") String phone)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("serviceType", SmsServiceTypeEnum.MENGWANG.getValue());
            para.put("content", content);
            if (!"".equals(phone))
            {
                if (!CommonUtil.isMobile(phone))
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", " 手机号码不符合格式");
                    return JSON.toJSONString(resultMap);
                }
                para.put("phoneNumber", phone);
                int resultStatus = smsService.sendMontnetsTuan(para);
                if (resultStatus == 1)
                {
                    resultMap.put("status", 1);
                    resultMap.put("msg", " 发送成功");
                }
                else
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "发送失败");
                }
            }
            else
            {
                if (file == null)
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "文件为空");
                    return JSON.toJSONString(resultMap);
                }
                Map<String, Object> data = POIUtil.getSheetDataAtIndex(file.getInputStream(), 0, true);
                if (data == null)
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "文件为空");
                    return JSON.toJSONString(resultMap);
                }
                
                List<Map<String, Object>> dataList = (List<Map<String, Object>>)data.get("data");
                Set<String> phoneSet = new HashSet<String>();
                for (Map<String, Object> mp : dataList)
                {
                    if (mp.get("cell0") != null)
                    {
                        phoneSet.add(mp.get("cell0") + "");
                    }
                }
                para.put("phoneNumberSet", phoneSet);
                int resultStatus = smsService.sendMontnetsTuan(para);
                if (resultStatus == 1)
                {
                    resultMap.put("status", 1);
                    resultMap.put("msg", " 发送成功");
                }
                else
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "发送失败");
                }
            }
        }
        catch (Exception e)
        {
            log.error("发送左岸城堡梦网短信失败", e);
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "sendMontnetsGlobal", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "短信管理-发送左岸城堡梦网短信")
    public String sendMontnetsGlobal(@RequestParam(value = "content", required = true) String content,//
        @RequestParam(value = "phoneFile", required = false) MultipartFile file,//
        @RequestParam(value = "phone", required = false, defaultValue = "") String phone)
    {
        Map<String, Object> resultMap = new HashMap<>();
        try
        {
            Map<String, Object> para = new HashMap<>();
            para.put("serviceType", SmsServiceTypeEnum.MENGWANG.getValue());
            para.put("content", content);
            if (!"".equals(phone))
            {
                if (!CommonUtil.isMobile(phone))
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", " 手机号码不符合格式");
                    return JSON.toJSONString(resultMap);
                }
                para.put("phoneNumber", phone);
                int resultStatus = smsService.sendMontnetsGlobal(para);
                if (resultStatus == 1)
                {
                    resultMap.put("status", 1);
                    resultMap.put("msg", " 发送成功");
                }
                else
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "发送失败");
                }
            }
            else
            {
                if (file == null)
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "文件为空");
                    return JSON.toJSONString(resultMap);
                }
                Map<String, Object> data = POIUtil.getSheetDataAtIndex(file.getInputStream(), 0, true);
                if (data == null)
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "文件为空");
                    return JSON.toJSONString(resultMap);
                }
                
                List<Map<String, Object>> dataList = (List<Map<String, Object>>)data.get("data");
                Set<String> phoneSet = new HashSet<String>();
                for (Map<String, Object> mp : dataList)
                {
                    if (mp.get("cell0") != null)
                    {
                        phoneSet.add(mp.get("cell0") + "");
                    }
                }
                para.put("phoneNumberSet", phoneSet);
                int resultStatus = smsService.sendMontnetsGlobal(para);
                if (resultStatus == 1)
                {
                    resultMap.put("status", 1);
                    resultMap.put("msg", " 发送成功");
                }
                else
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "发送失败");
                }
            }
        }
        catch (Exception e)
        {
            log.error("发送左岸城堡梦网短信失败", e);
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 批量发货处理
     * 
     * @param file
     * @param request
     * @return
     */
    // @RequestMapping(value="/batchSend", method=RequestMethod.POST,
    // produces="application/json;charset=UTF-8")
    // @ResponseBody
    // public String batchSend(HttpServletRequest request,
    // @RequestParam("phoneFile") MultipartFile file,
    // @RequestParam(value="importType",required=false,defaultValue="0") int
    // importType
    // )throws Exception{
    //
    // }
}
