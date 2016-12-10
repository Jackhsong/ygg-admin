package com.ygg.admin.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.code.OrderEnum;
import com.ygg.admin.config.YggAdminProperties;
import com.ygg.admin.entity.OrderQuestionEntity;
import com.ygg.admin.entity.OrderQuestionTemplateEntity;
import com.ygg.admin.entity.User;
import com.ygg.admin.service.OrderQuestionService;
import com.ygg.admin.service.UserService;
import com.ygg.admin.util.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.SecurityUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.*;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/orderQuestion")
public class OrderQuestionController
{
    private static Logger logger = Logger.getLogger(OrderQuestionController.class);
    
    @Resource
    private OrderQuestionService orderQuestionService;
    
    @Resource
    private UserService userService;
    
    @RequestMapping("/list")
    public ModelAndView list(@RequestParam(value = "status", required = false, defaultValue = "1") int status)
    {
        ModelAndView mv = new ModelAndView("orderQuestion/list");
        try
        {
            List<User> userList = new ArrayList<>();
            userList.addAll(userService.findUserByRole("客服主管"));
            userList.addAll(userService.findUserByRole("客服"));
            mv.addObject("status", status + "");
            mv.addObject("userList", userList);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    @RequestMapping(value = "/jsonQuestionListInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonQuestionListInfo(//
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,//
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows,//
        @RequestParam(value = "orderNo", required = false, defaultValue = "") String orderNo,//
        @RequestParam(value = "accountId", required = false, defaultValue = "-1") int accountId,//
        @RequestParam(value = "accountName", required = false, defaultValue = "") String accountName,//
        @RequestParam(value = "sellerId", required = false, defaultValue = "-1") int sellerId,//
        @RequestParam(value = "orderType", required = false, defaultValue = "-1") int orderType,//
        @RequestParam(value = "createUser", required = false, defaultValue = "-1") int createUser,//
        @RequestParam(value = "customerStatus", required = false, defaultValue = "-1") int customerStatus,//
        @RequestParam(value = "sellerStatus", required = false, defaultValue = "-1") int sellerStatus,//
        @RequestParam(value = "templateId", required = false, defaultValue = "-1") int templateId,//
        @RequestParam(value = "questionId", required = false, defaultValue = "-1") int questionId,//
        @RequestParam(value = "questionDesc", required = false, defaultValue = "") String questionDesc,//
        @RequestParam(value = "status", required = false, defaultValue = "1") int status,//
        @RequestParam(value = "createTimeBegin", required = false, defaultValue = "") String createTimeBegin,//
        @RequestParam(value = "createTimeEnd", required = false, defaultValue = "") String createTimeEnd)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            if (!"".equals(orderNo))
            {
                para.put("orderNo", orderNo);
            }
            if (accountId != -1)
            {
                para.put("accountId", accountId);
            }
            if (!"".equals(accountName))
            {
                para.put("accountName", "%" + accountName + "%");
            }
            if (sellerId != -1)
            {
                para.put("sellerId", sellerId);
            }
            if (createUser != -1)
            {
                para.put("createUser", createUser);
            }
            if (orderType != -1)
            {
                if (orderType == 1) //左岸城堡 包括燕网
                {
                    para.put("orderTypeList",
                            Lists.newArrayList(OrderEnum.ORDER_TYPE.GEGEJIA.getCode(),OrderEnum.ORDER_TYPE.YANWANG.getCode()));
                } else if (orderType == 2) // 左岸城堡 包括全球购
                {
                    para.put("orderTypeList",
                            Lists.newArrayList(OrderEnum.ORDER_TYPE.GEGETUAN.getCode(), OrderEnum.ORDER_TYPE.GEGETUAN_QQG.getCode()));
                } else if (orderType == 3)  // 左岸城堡

                {
                    para.put("orderTypeList",  Lists.newArrayList(OrderEnum.ORDER_TYPE.HUANQIUBUSHOU.getCode()));
                }
            }
            if (templateId != -1)
            {
                para.put("templateId", templateId);
            }
            if (questionId != -1)
            {
                para.put("questionId", questionId);
            }
            if (!"".equals(questionDesc))
            {
                para.put("questionDesc", "%" + questionDesc + "%");
            }
            if (customerStatus != -1)
            {
                para.put("customerStatus", customerStatus);
            }
            if (sellerStatus != -1)
            {
                para.put("sellerStatus", sellerStatus);
            }
            para.put("status", status);
            if (!"".equals(createTimeBegin))
            {
                para.put("createTimeBegin", createTimeBegin);
            }
            if (!"".equals(createTimeEnd))
            {
                para.put("createTimeEnd", createTimeEnd);
            }
            resultMap = orderQuestionService.jsonQuestionListInfo(para);
        }
        catch (Exception e)
        {
            logger.error("异步加载订单问题模版列表出错了", e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/orderQuestionInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String orderQuestionInfo(@RequestParam(value = "orderId", required = true) int orderId)
    {
        String result = null;
        try
        {
            result = orderQuestionService.findOrderQuestionInfoStr(orderId);
        }
        catch (Exception e)
        {
            logger.error("加载订单id=" + orderId + "客服登记问题出错了", e);
        }
        return result;
    }
    
    @RequestMapping("/templateList")
    public ModelAndView templateList()
    {
        ModelAndView mv = new ModelAndView("orderQuestion/templateList");
        return mv;
    }
    
    /**
     * 订单问题模版列表
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "/jsonQuestionTemplateInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonQuestionTemplateInfo(//
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,//
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            resultMap = orderQuestionService.jsonQuestionTemplateInfo(para);
        }
        catch (Exception e)
        {
            logger.error("异步加载订单问题模版列表出错了", e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 新增问题模板
     * @param name
     * @return
     */
    @RequestMapping(value = "/saveTemplate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "订单问题管理-新增订单问题类型")
    public String saveTemplate(@RequestParam(value = "id", required = false, defaultValue = "0") int id, @RequestParam(value = "name", required = true) String name,
        @RequestParam(value = "limitHour", required = true) String limitHour)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            name = name.trim();
            limitHour = limitHour.trim();
            if (!Pattern.matches("^\\d*.?\\d*$", limitHour))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "时限只能为整数或小数");
                return JSON.toJSONString(resultMap);
            }
            if (id == 0)
            {
                //新增验证是否重复
                OrderQuestionTemplateEntity template = orderQuestionService.findOrderQuestionTemplateByName(name);
                if (template != null)
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "名称为 '" + name + "'的问题类型已经存在");
                    return JSON.toJSONString(resultMap);
                }
            }
            int result = orderQuestionService.saveQuestionTemplate(id, name, limitHour);
            if (result == 1)
            {
                resultMap.put("status", 1);
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "保存出错");
            }
        }
        catch (Exception e)
        {
            logger.error("新增问题类型:" + name + "出错了", e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存出错");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/updateOrderQuestionTemplateStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "订单问题管理-修改订单问题类型状态")
    public String updateOrderQuestionTemplateStatus(@RequestParam(value = "id", required = true) String id, @RequestParam(value = "isAvailable", required = true) int isAvailable)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("idList", Arrays.asList(id.split(",")));
            para.put("isAvailable", isAvailable);
            
            int resultStatus = orderQuestionService.updateOrderQuestionTemplateStatus(para);
            if (resultStatus == 1)
            {
                resultMap.put("status", 1);
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "保存失败");
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 新增订单问题
     * @param request
     * @param orderId
     * @param templateId
     * @param questionDesc
     * @param timeLimitType
     * @param customTime
     * @return
     */
    @RequestMapping(value = "/addOrderQuestion", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "订单问题管理-新增订单问题")
    public String addOrderQuestion(HttpServletRequest request,//
        @RequestParam(value = "orderId", required = false, defaultValue = "0") int orderId,//
        @RequestParam(value = "templateId", required = false, defaultValue = "0") int templateId,//
        @RequestParam(value = "questionDesc", required = false, defaultValue = "") String questionDesc,//
        @RequestParam(value = "customTime", required = false, defaultValue = "") String customTime//
    )
    {
        String[] imageArray = request.getParameterValues("image");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            //参数验证
            if (orderId == 0)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "订单ID不能为空");
                return JSON.toJSONString(resultMap);
            }
            if (templateId == 0)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "请选择问题类型");
                return JSON.toJSONString(resultMap);
            }
            if (StringUtils.isEmpty(questionDesc))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "问题描述不能为空");
                return JSON.toJSONString(resultMap);
            }
            
            OrderQuestionTemplateEntity template = orderQuestionService.findOrderQuestionTemplateById(templateId);
            if (template == null)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "所选问题类型不存在或已被停用");
                return JSON.toJSONString(resultMap);
            }
            
            String username = SecurityUtils.getSubject().getPrincipal() + "";
            User user = userService.findByUsername(username);
            OrderQuestionEntity oqe = new OrderQuestionEntity();
            oqe.setOrderId(orderId);
            oqe.setQuestionTemplateId(templateId);
            oqe.setQuestionDesc(questionDesc);
            oqe.setTimeLimitType(3);//需求有调整，这里都按照自定时间
            
            //自定义时间取当前时间加上模版中的limit_hour
            int limitMinute = Float.valueOf(template.getLimitHour() * 60).intValue();//template中的limit_hour会有0.5小时
            oqe.setCustomTime(DateTime.now().plusMinutes(limitMinute).toString("yyyy-MM-dd HH:mm:ss"));
            oqe.setCreateUser(user == null ? Integer.parseInt(YggAdminProperties.getInstance().getPropertie("admin_account_id")) : user.getId());
            StringBuilder sb = new StringBuilder(DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
            sb.append("&nbsp;").append(user == null ? "匿名用户" : user.getRealname()).append("&nbsp;").append(questionDesc).append("<br/>");
            oqe.setCustomerDealDetail(sb.toString());
            int result = orderQuestionService.addOrderQuestion(oqe, imageArray);
            if (result == 1)
            {
                resultMap.put("status", 1);
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "保存失败");
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存出错");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/jsonOrderQuestionTemplate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonOrderQuestionTemplateCode(@RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable)
    {
        List<Map<String, String>> codeList = new ArrayList<Map<String, String>>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("isAvailable", isAvailable);
            Map<String, Object> resultMap = orderQuestionService.jsonQuestionTemplateInfo(para);
            List<Map<String, Object>> list = (List<Map<String, Object>>)resultMap.get("rows");
            Map<String, String> mapAll = new HashMap<String, String>();
            if (isAvailable == -1)
            {
                mapAll.put("code", "0");
                mapAll.put("text", "全部");
                codeList.add(mapAll);
            }
            for (Map<String, Object> entity : list)
            {
                Map<String, String> map = new HashMap<String, String>();
                map.put("code", entity.get("id") + "");
                map.put("text", entity.get("name") + "");
                codeList.add(map);
            }
            
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        return JSON.toJSONString(codeList);
    }
    
    /**
     * 查看问题详细信息
     * @param id：问题Id
     * @return
     */
    @RequestMapping("/orderQuestionDetail/{questionId}")
    public ModelAndView orderQuestionDetail(@PathVariable("questionId") int id)
    {
        ModelAndView mv = new ModelAndView("orderQuestion/orderQuestionDetail");
        try
        {
            Map<String, Object> detail = orderQuestionService.findOrderQuestionDetailInfo(id);
            if (detail == null || detail.size() == 0)
            {
                mv.setViewName("error/404");
                return mv;
            }
            mv.addObject("detail", detail);
        }
        catch (Exception e)
        {
            logger.error("查看问题questionId=" + id + "出错了", e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    /**
     * 更新问题进度
     * @param request
     * @param questionId：问题Id
     * @param content：处理内容
     * @param status：状态，1进行中，2已完结
     * @param dealType：处理类型，1:顾客问题，2:商家问题
     * @return
     */
    @RequestMapping(value = "/updateOrderQuestionProgress", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "订单问题管理-更新订单问题进度")
    public String updateOrderQuestionProgress(HttpServletRequest request,//
        @RequestParam(value = "questionId", required = false, defaultValue = "0") int questionId,//
        @RequestParam(value = "content", required = false, defaultValue = "") String content,//
        @RequestParam(value = "status", required = false, defaultValue = "1") int status,//
        @RequestParam(value = "dealType", required = false, defaultValue = "1") int dealType, //
        @RequestParam(value = "isMark", required = false, defaultValue = "0") int isMark)
    {
        String[] imageArray = request.getParameterValues("image");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            //参数验证
            if (questionId == 0)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "问题ID不能为空");
                return JSON.toJSONString(resultMap);
            }
            if (StringUtils.isBlank(content))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "问题描述不能为空");
                return JSON.toJSONString(resultMap);
            }
            
            String username = SecurityUtils.getSubject().getPrincipal() + "";
            User user = userService.findByUsername(username);
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("questionId", questionId);
            para.put("content", content);
            para.put("status", status);
            para.put("dealType", dealType);
            para.put("createUser", user == null ? Integer.parseInt(YggAdminProperties.getInstance().getPropertie("admin_account_id")) : user.getId());
            StringBuilder sb = new StringBuilder(DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
            sb.append("&nbsp;").append(user == null ? "匿名用户" : user.getRealname()).append("&nbsp;").append(content).append("<br/>");
            para.put("dealDetail", sb.toString());
            para.put("isMark", isMark);
            
            int result = orderQuestionService.updateOrderQuestionProgress(para, imageArray);
            if (result == 1)
            {
                resultMap.put("status", 1);
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "保存失败");
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存出错");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/export")
    @ControllerLog(description = "订单问题管理-导出订单问题")
    public void export(HttpServletRequest request, HttpServletResponse response, //
        @RequestParam(value = "orderNo", required = false, defaultValue = "") String orderNo,//
        @RequestParam(value = "accountId", required = false, defaultValue = "-1") int accountId,//
        @RequestParam(value = "accountName", required = false, defaultValue = "") String accountName,//
        @RequestParam(value = "sellerId", required = false, defaultValue = "-1") int sellerId,//
        @RequestParam(value = "orderType", required = false, defaultValue = "-1") int orderType,//
        @RequestParam(value = "createUser", required = false, defaultValue = "-1") int createUser,//
        @RequestParam(value = "customerStatus", required = false, defaultValue = "-1") int customerStatus,//
        @RequestParam(value = "sellerStatus", required = false, defaultValue = "-1") int sellerStatus,//
        @RequestParam(value = "templateId", required = false, defaultValue = "-1") int templateId,//
        @RequestParam(value = "questionId", required = false, defaultValue = "-1") int questionId,//
        @RequestParam(value = "questionDesc", required = false, defaultValue = "") String questionDesc,//
        @RequestParam(value = "status", required = false, defaultValue = "1") int status,//
        @RequestParam(value = "createTimeBegin", required = false, defaultValue = "") String createTimeBegin,//
        @RequestParam(value = "createTimeEnd", required = false, defaultValue = "") String createTimeEnd)
    {
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (!"".equals(orderNo))
            {
                para.put("orderNo", orderNo);
            }
            if (accountId != -1)
            {
                para.put("accountId", accountId);
            }
            if (!"".equals(accountName))
            {
                para.put("accountName", "%" + accountName + "%");
            }
            if (sellerId != -1)
            {
                para.put("sellerId", sellerId);
            }
            if (createUser != -1)
            {
                para.put("createUser", createUser);
            }
            if (orderType != -1)
            {
                if (orderType == 1) //左岸城堡 包括燕网
                {
                    para.put("orderTypeList",
                            Lists.newArrayList(OrderEnum.ORDER_TYPE.GEGEJIA.getCode(),OrderEnum.ORDER_TYPE.YANWANG.getCode()));
                } else if (orderType == 2) // 左岸城堡 包括全球购
                {
                    para.put("orderTypeList",
                            Lists.newArrayList(OrderEnum.ORDER_TYPE.GEGETUAN.getCode(), OrderEnum.ORDER_TYPE.GEGETUAN_QQG.getCode()));
                } else if (orderType == 3)  // 左岸城堡

                {
                    para.put("orderTypeList",  Lists.newArrayList(OrderEnum.ORDER_TYPE.HUANQIUBUSHOU.getCode()));
                }
            }
            if (templateId != -1)
            {
                para.put("templateId", templateId);
            }
            if (questionId != -1)
            {
                para.put("questionId", questionId);
            }
            if (!"".equals(questionDesc))
            {
                para.put("questionDesc", "%" + questionDesc + "%");
            }
            if (customerStatus != -1)
            {
                para.put("customerStatus", customerStatus);
            }
            if (sellerStatus != -1)
            {
                para.put("sellerStatus", sellerStatus);
            }
            para.put("status", status);
            if (!"".equals(createTimeBegin))
            {
                para.put("createTimeBegin", createTimeBegin);
            }
            if (!"".equals(createTimeEnd))
            {
                para.put("createTimeEnd", createTimeEnd);
            }
            Map<String, Object> resultMap = orderQuestionService.jsonQuestionListInfo(para);
            
            List<Map<String, Object>> result = (List<Map<String, Object>>)resultMap.get("rows");
            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("客服问题登记" + (status == 1 ? "(进行中)" : "(已完成)"));
            String[] str = {"问题Id", "顾客问题进度", "顾客回复时限", "顾客问题描述", "商家对接进度", "商家对接描述", "问题类型", "创建者",
                    "订单编号", "订单状态","订单类型","付款时间", "发货时间", "订单总价", "实付金额", "收货人", "收货手机", "商家", "发货地"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            for (int i = 0; i < result.size(); i++)
            {
                Map<String, Object> om = result.get(i);
                Row r = sheet.createRow(i + 1);
                int num = 0;
                r.createCell(num++).setCellValue(om.get("questionId") + "");
                r.createCell(num++).setCellValue(om.get("customerStatusStr") + "");
                r.createCell(num++).setCellValue(om.get("leftTime") + "");
                r.createCell(num++).setCellValue((om.get("customerDealDetail") + "").replaceAll("&nbsp;", " ").replaceAll("<br/>", "     "));
                r.createCell(num++).setCellValue(om.get("sellerStatusStr") + "");
                r.createCell(num++).setCellValue((om.get("sellerDealDetail") + "").replaceAll("&nbsp;", " ").replaceAll("<br/>", "     "));
                r.createCell(num++).setCellValue(om.get("templateName") + "");
                r.createCell(num++).setCellValue(om.get("createUser") + "");
                r.createCell(num++).setCellValue(om.get("orderNo") + "");
                r.createCell(num++).setCellValue(om.get("oStatusDescripton") + "");
                r.createCell(num++).setCellValue(om.get("orderType") + "");
                r.createCell(num++).setCellValue(om.get("payTime") + "");
                r.createCell(num++).setCellValue(om.get("sendTime") + "");
                r.createCell(num++).setCellValue(om.get("totalPrice") + "");
                r.createCell(num++).setCellValue(om.get("realPrice") + "");
                r.createCell(num++).setCellValue(om.get("raFullName") + "");
                r.createCell(num++).setCellValue(om.get("raMobileNumber") + "");
                r.createCell(num++).setCellValue(om.get("sellerName") + "");
                r.createCell(num++).setCellValue(om.get("sendAddress") + "");
            }
            
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = "客服问题登记" + (status == 1 ? "（进行中）" : "（已完成）");
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            
        }
        catch (Exception e)
        {
            logger.error("客服问题登记出错了！！！", e);
        }
        finally
        {
            if (fOut != null)
            {
                try
                {
                    fOut.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            if (workbook != null)
            {
                try
                {
                    workbook.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @RequestMapping(value = "/updateOrderQuestionMark", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "订单问题管理-更新订单问题标记状态")
    public String updateOrderQuestionMark(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "isMark", required = false, defaultValue = "0") int isMark)
    {
        
        try
        {
            return orderQuestionService.updateOrderQuestionMark(id, isMark);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", "保存出错");
            return JSON.toJSONString(resultMap);
        }
    }
    
    @RequestMapping(value = "/updateOrderQuestionPushStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "订单问题管理-推送订单问题给商家")
    public String updateOrderQuestionPushStatus(@RequestParam(value = "id", required = false, defaultValue = "0") int id)
    {
        try
        {
            return orderQuestionService.updateOrderQuestionPushStatus(id);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", "保存出错");
            return JSON.toJSONString(resultMap);
        }
    }
}
