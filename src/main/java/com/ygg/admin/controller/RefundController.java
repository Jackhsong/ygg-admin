package com.ygg.admin.controller;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.code.*;
import com.ygg.admin.entity.*;
import com.ygg.admin.service.*;
import com.ygg.admin.util.CommonEnum;
import com.ygg.admin.util.CommonEnum.OrderAppChannelEnum;
import com.ygg.admin.util.DateTimeUtil;
import com.ygg.admin.util.ImageUtil;
import com.ygg.admin.util.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author zhangyb
 *         
 */
@Controller
@RequestMapping("/refund")
public class RefundController
{
    
    @Resource
    private RefundService refundService;
    
    @Resource
    private UserService userService;
    
    @Resource
    private SellerService sellerService;

    Logger log = Logger.getLogger(RefundController.class);
    
    /**
     * 全部退款列表
     * 
     * @return
     */
    @RequestMapping("/listAll")
    public ModelAndView listRefund(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("refund/listAll");
        return mv;
    }
    
    /**
     * 仅退款订单列表
     * 
     * @return
     */
    @RequestMapping("/listOnlyReturnMoney")
    public ModelAndView listOnlyReturnMoney(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("refund/listOnlyReturnMoney");
        return mv;
    }
    
    /**
     * 退款和退货订单列表
     * 
     * @return
     */
    @RequestMapping("/listReturnMoneyAndGoods")
    public ModelAndView listReturnMoneyAndGoods(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("refund/listReturnMoneyAndGoods");
        return mv;
    }
    
    /**
     * 打款和收货处理 列表
     */
    @RequestMapping(value = "/jsonAll", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonAll(HttpServletRequest request, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "source", required = true) int source, // 1:listAll;2:listOnlyReturnMoney;3:listReturnMoneyAndGoods
        @RequestParam(value = "number", required = false, defaultValue = "0") long number, // 订单号
        @RequestParam(value = "status", required = false, defaultValue = "0") int status, // 退款状态
        @RequestParam(value = "orderStatus", required = false, defaultValue = "0") int orderStatus, // 订单状态
        @RequestParam(value = "searchOrderChannel", required = false, defaultValue = "0") int searchOrderChannel, // 订单来源
                                                                                                                  // 0全部，1：左岸城堡，2左岸城堡，3左岸城堡

        @RequestParam(value = "operationStatus", required = false, defaultValue = "-1") int operationStatus, // 商家订单导出状态
        @RequestParam(value = "type", required = false, defaultValue = "0") int type, // 退款类型
        @RequestParam(value = "refundProportionStatus", required = false, defaultValue = "-1") int refundProportionStatus, // 退款分摊状态
                                                                                                                           // -1：全部
                                                                                                                           // 0:
                                                                                                                           // 未分摊
                                                                                                                           // 1:已分摊
        @RequestParam(value = "startTime", required = false, defaultValue = "") String startTime, // 退款申请时间
        @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime, // 退款申请时间
        @RequestParam(value = "startCheckTime", required = false, defaultValue = "") String startCheckTime, // 操作时间
                                                                                                            // start
        @RequestParam(value = "endCheckTime", required = false, defaultValue = "") String endCheckTime, // 操作时间 end
        @RequestParam(value = "moneyStatus", required = false, defaultValue = "0") int moneyStatus, // 打款状态
        @RequestParam(value = "receiveType", required = false, defaultValue = "0") int receiveType, // 确认收货状态
        @RequestParam(value = "name", required = false, defaultValue = "") String name, // 用户名
        @RequestParam(value = "receiveName", required = false, defaultValue = "") String receiveName, // 收货人姓名
        @RequestParam(value = "mobileNumber", required = false, defaultValue = "") String mobileNumber, // 收货手机
        @RequestParam(value = "sellerId", required = false, defaultValue = "-1") int sellerId, // 商家Id
        @RequestParam(value = "financialAffairsCardId", required = false, defaultValue = "0") int financialAffairsCardId, // 财务打款账户
        @RequestParam(value = "logisticsStatus", required = false, defaultValue = "-1") int logisticsStatus // 是否有物流，1是，0否
    )
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        if (number > 0)
        {
            para.put("number", number);
        }
        if (status > 0)
        {
            para.put("status", status);
        }
        if (operationStatus != -1)
        {
            para.put("operationStatus", operationStatus);
        }
        if (orderStatus > 0)
        {
            para.put("orderStatus", orderStatus);
        }
        if (type > 0)
        {
            para.put("type", type);
        }
        if (!"".equals(startTime))
        {
            para.put("startTime", startTime);
        }
        if (!"".equals(endTime))
        {
            para.put("endTime", endTime);
        }
        if (!"".equals(startCheckTime))
        {
            para.put("startCheckTime", startCheckTime);
        }
        if (!"".equals(endCheckTime))
        {
            para.put("endCheckTime", endCheckTime);
        }
        if (moneyStatus > 0)
        {
            para.put("moneyStatus", moneyStatus);
        }
        if (receiveType > 0)
        {
            para.put("receiveType", receiveType);
        }
        if (!"".equals(name))
        {
            para.put("name", name);
        }
        if (!"".equals(receiveName))
        {
            para.put("receiveName", receiveName);
        }
        if (!"".equals(mobileNumber))
        {
            para.put("mobileNumber", mobileNumber);
        }
        if (refundProportionStatus != -1)
        {
            para.put("refundProportionStatus", refundProportionStatus);
        }
        if (sellerId != -1)
        {
            para.put("sellerId", sellerId);
        }
        if (financialAffairsCardId > 0)
        {
            para.put("financialAffairsCardId", financialAffairsCardId);
        }
        para.put("searchOrderChannel", searchOrderChannel);
        para.put("logisticsStatus", logisticsStatus);
        Map<String, Object> result = refundService.findAllRefundInfo(para);
        return JSON.toJSONString(result);
    }
    
    /**
     * 退款处理页面
     * 
     * @param request
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/dealRefund/{id}")
    public ModelAndView dealRefund(HttpServletRequest request, @PathVariable("id") int id)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        Map<String, Object> result = refundService.refundDetail(id);
        int status = Integer.valueOf(result.get("status") + "");
        if (status == 0)
        {
            // 数据错误 404
            mv.setViewName("error/404");
            return mv;
        }
        Map<String, Object> refund = (Map<String, Object>)result.get("refund");
        mv.addObject("refund", refund);
        int refundType = Integer.valueOf(refund.get("type") + "");
        // 查询退款 记录信息
        Map<String, Object> teackMap = null;
        if (refundType == 1)
        {
            // 仅退款
            teackMap = refundService.findRefundOnlyReturnMoneyTeackMap(id);
            //
        }
        else if (refundType == 2)
        {
            // 退款并退货
            teackMap = refundService.findRefundReturnMoneyAndGoodsTeackMap(id);
        }
        mv.addObject("teackMap", teackMap);
        int refundStatus = Integer.valueOf(refund.get("status") + "");
        // 仅退款类型且待打款状态 & 退款并退货且待打款-财务打款状态 --- 时，取打款账户信息
        List<Map<String, Object>> financialAffairsCards = null;
        if (refundStatus == 3)
        {
            financialAffairsCards = refundService.findAllFinancialAffairsCardForDeal();
            mv.addObject("financialAffairsCards", financialAffairsCards);
        }
        if (refundType == 1)
        {
            mv.setViewName("refund/Onlyrefund");
        }
        else if (refundType == 2)
        {
            mv.setViewName("refund/refundAndGoods");
        }
        return mv;
    }
    
    /**
     * 手动输入退款退货
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping("/sendGoods")
    @ResponseBody
    @ControllerLog(description = "退款退货管理-手动输入退款退货")
    public String sendGoods(HttpServletRequest request, //
        @RequestParam(value = "refundId", required = true) int refundId, //
        @RequestParam(value = "channel", required = false, defaultValue = "") String channel, // 物流渠道（为快递公司名称：圆通快递）
        @RequestParam(value = "number", required = false, defaultValue = "") String number// 物流单号
    )
        throws Exception
    {
        try
        {
            Subject currentUser = SecurityUtils.getSubject();
            User user = null;
            if (currentUser != null)
            {
                String username = currentUser.getPrincipal() + "";
                user = userService.findByUsername(username);
            }
            if (user == null)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("status", 0);
                map.put("msg", "操作失败，您没有操作权限！");
                return JSON.toJSONString(map);
            }
            number = number.trim();
            if ("".equals(channel) || "".equals(number) || !StringUtils.isOnlyLettersAndNumber(number) || KdCompanyEnum.getKdCompanyEnumByCompanyName(channel) == null)
            {
                Map<String, Object> result = new HashMap<String, Object>();
                result.put("status", 0);
                result.put("msg", "请检查物流渠道和物流单号是否正确");
                return JSON.toJSONString(result);
            }
            Map<String, Object> result = refundService.saveRefundLogisticsInfo(refundId, number, channel, user);
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error("退款退货物流信息订阅 失败！", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "服务器出错");
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 同意退款
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/agreeRefund")
    @ResponseBody
    @ControllerLog(description = "退款退货管理-同意退款")
    public String agreeRefund(HttpServletRequest request, @RequestParam(value = "id", required = true) int id, //
        @RequestParam(value = "remark", required = false, defaultValue = "") String remark, //
        @RequestParam(value = "money", required = false, defaultValue = "-1") double money, //
        @RequestParam(value = "step", required = true) byte step, //
        @RequestParam(value = "refundReasonId", required = false, defaultValue = "0") int refundReasonId, // 退款原因
        @RequestParam(value = "cardInfo", required = false, defaultValue = "0") int cardId, //
        @RequestParam(value = "modifyRefundType", required = false, defaultValue = "false") boolean modifyRefundType, // step1
                                                                                                                      // 时
                                                                                                                      // 修改
                                                                                                                      // 退款类型
        @RequestParam(value = "cancelOrder", required = false, defaultValue = "false") boolean cancelOrder, // 是否同时取消订单
        @RequestParam(value = "sendGoods", required = false, defaultValue = "false") boolean sendGoods, // step1 时
                                                                                                        // 该订单是否继续发货
        @RequestParam(value = "realSendGoodsCount", required = false, defaultValue = "") String realSendGoodsCount, // step1
                                                                                                                    // 时
                                                                                                                    // 修改
                                                                                                                    // 订单退款商品发货数量
        @RequestParam(value = "type", required = true) int type)
        throws Exception
    {
        if (!"".equals(remark) && remark.length() > 100)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", 0);
            map.put("msg", "备注字数不能超过100个字");
            return JSON.toJSONString(map);
        }
        try
        {
            Subject currentUser = SecurityUtils.getSubject();
            User user = null;
            if (currentUser != null)
            {
                String username = currentUser.getPrincipal() + "";
                user = userService.findByUsername(username);
            }
            if (user == null)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("status", 0);
                map.put("msg", "操作失败，您没有操作权限！");
                //return JSON.toJSONString(map);
            }
            Map<String, Object> result = null;
            result = refundService.updateRefund(type, 1, step, user, id, remark, money, cardId, modifyRefundType, sendGoods, realSendGoodsCount, cancelOrder, refundReasonId);
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error("同意退款申请失败！", e);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", 0);
            map.put("msg", "申请失败");
            return JSON.toJSONString(map);
        }
    }
    
    /**
     * 重置，取消退款
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/cancelRefund")
    @ResponseBody
    @ControllerLog(description = "退款退货管理-取消退款")
    public String cancelRefund(HttpServletRequest request, @RequestParam(value = "id", required = true) int id, @RequestParam(value = "step", required = true) byte step,
        @RequestParam(value = "remark", required = false, defaultValue = "") String remark, @RequestParam(value = "type", required = true) int type)
        throws Exception
    {
        if (!"".equals(remark) && remark.length() > 100)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", 0);
            map.put("msg", "备注字数不能超过100个字");
            return JSON.toJSONString(map);
        }
        try
        {
            Subject currentUser = SecurityUtils.getSubject();
            User user = null;
            if (currentUser != null)
            {
                String username = currentUser.getPrincipal() + "";
                user = userService.findByUsername(username);
            }
            if (user == null)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("status", 0);
                map.put("msg", "操作失败，您没有操作权限！");
                return JSON.toJSONString(map);
            }
            Map<String, Object> result = refundService.updateRefund(type, 3, step, user, id, remark, null, 0, false, true, null, false, 0);
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error("重置，取消退款，失败！", e);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", 0);
            map.put("msg", "操作失败");
            return JSON.toJSONString(map);
        }
    }
    
    /**
     * 关闭退款
     * 
     * @param request
     * @param id
     * @param step
     * @param remark
     * @return
     * @throws Exception
     */
    @RequestMapping("/closeRefund")
    @ResponseBody
    @ControllerLog(description = "退款退货管理-关闭退款")
    public String closeRefund(HttpServletRequest request, @RequestParam(value = "id", required = true) int id, @RequestParam(value = "step", required = true) byte step,
        @RequestParam(value = "remark", required = false, defaultValue = "") String remark, @RequestParam(value = "type", required = true) int type)
        throws Exception
    {
        if (!"".equals(remark) && remark.length() > 100)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", 0);
            map.put("msg", "备注字数不能超过100个字");
            return JSON.toJSONString(map);
        }
        try
        {
            Subject currentUser = SecurityUtils.getSubject();
            User user = null;
            if (currentUser != null)
            {
                String username = currentUser.getPrincipal() + "";
                user = userService.findByUsername(username);
            }
            if (user == null)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("status", 0);
                map.put("msg", "操作失败，您没有操作权限！");
                return JSON.toJSONString(map);
            }
            Map<String, Object> result = refundService.updateRefund(type, 2, step, user, id, remark, null, 0, false, true, null, false, 0);
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error("关闭退款，失败！", e);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", 0);
            map.put("msg", "操作失败");
            return JSON.toJSONString(map);
        }
    }
    
    /**
     * 财务打款 列表
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/adminBankList")
    public ModelAndView adminBankList(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("refund/adminBankList");
        return mv;
    }
    
    /**
     * 异步加载财务打款列表数据
     * 
     * @param request
     * @param page
     * @param rows
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/adminBankInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String adminBankInfo(HttpServletRequest request, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "30") int rows)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        Map<String, Object> result = refundService.findAllFinancialAffairsCard(para);
        return JSON.toJSONString(result);
    }
    
    @RequestMapping(value = "/adminBankInfoCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String adminBankInfoCode()
        throws Exception
    {
        List<Map<String, Object>> codeList = refundService.getAdminBankInfoCode();
        return JSON.toJSONString(codeList);
    }
    
    /**
     * 异步保存财务打款银行账户信息
     * 
     * @param request
     * @param bankType
     * @param cardNumber
     * @param cardName
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveBank", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "退款退货管理-保存财务打款银行账户信息")
    public String saveBank(HttpServletRequest request, @RequestParam(value = "bankOrAlipayCode", required = true) int bankOrAlipayCode,
        @RequestParam(value = "bankType", required = true) int bankType, @RequestParam(value = "cardNumber", required = true) String cardNumber,
        @RequestParam(value = "cardName", required = true) String cardName)
        throws Exception
    {
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("type", bankOrAlipayCode);
            para.put("bankType", bankType);
            para.put("cardNumber", cardNumber);
            para.put("cardName", cardName);
            int status = refundService.saveFinancialAffairsCard(para);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", status);
            result.put("msg", status == 1 ? "保存成功" : "保存失败");
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error("异步保存财务打款银行账户信息 失败", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "保存失败");
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 删除财务打款银行账户信息
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/deleteBank", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "退款退货管理-删除财务打款银行账户信息")
    public String deleteBank(HttpServletRequest request, @RequestParam(value = "ids", required = true) String ids)
        throws Exception
    {
        List<Integer> idList = new ArrayList<Integer>();
        if (ids.indexOf(",") > 0)
        {
            String[] arr = ids.split(",");
            for (String cur : arr)
            {
                idList.add(Integer.valueOf(cur));
            }
        }
        else
        {
            idList.add(Integer.valueOf(ids));
        }
        int status = refundService.deleteFinancialAffairsCardByIds(idList);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", status);
        result.put("msg", status > 1 ? "删除成功" : "删除失败");
        return JSON.toJSONString(result);
    }
    
    /**
     * 退款退货 确认收货
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/receiveGoods", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "退款退货管理-确认收货")
    public String receiveGoods(HttpServletRequest request, int id, @RequestParam(value = "remark", required = false, defaultValue = "") String remark)
        throws Exception
    {
        try
        {
            Subject currentUser = SecurityUtils.getSubject();
            User user = null;
            if (currentUser != null)
            {
                String username = currentUser.getPrincipal() + "";
                user = userService.findByUsername(username);
            }
            if (user == null)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("status", 0);
                map.put("msg", "操作失败，您没有操作权限！");
                return JSON.toJSONString(map);
            }
            Map<String, Object> result = refundService.confirmGoods(id, user, remark);
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error("退款退货 确认收货 保存失败", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "保存失败");
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 根据查询结果导出退款或退货订单
     * 
     * @param request
     * @param number
     * @param status
     * @param type
     * @param startTime
     * @param endTime
     * @param moneyStatus
     * @param receiveType
     * @param name
     * @param receiveName
     * @param mobileNumber
     * @throws Exception
     */
    @RequestMapping("/exportResult")
    @ControllerLog(description = "退款退货管理-导出退款退货订单")
    public void exportResult(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "number", required = false, defaultValue = "0") long number, // 订单号
        @RequestParam(value = "status", required = false, defaultValue = "0") int status, // 退款状态
        @RequestParam(value = "orderStatus", required = false, defaultValue = "0") int orderStatus, // 订单状态
        @RequestParam(value = "searchOrderChannel", required = false, defaultValue = "0") int searchOrderChannel, // 订单来源
                                                                                                                  // 0全部，1：左岸城堡，2左岸城堡，3左岸城堡

        @RequestParam(value = "operationStatus", required = false, defaultValue = "-1") int operationStatus, // 商家订单导出状态
        @RequestParam(value = "type", required = false, defaultValue = "0") int type, // 退款类型
        @RequestParam(value = "refundProportionStatus", required = false, defaultValue = "-1") int refundProportionStatus, // 退款分摊状态
                                                                                                                           // -1：全部
                                                                                                                           // 0:
                                                                                                                           // 未分摊
                                                                                                                           // 1:已分摊
        @RequestParam(value = "startTime", required = false, defaultValue = "") String startTime, // 退款申请时间
        @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime, // 退款申请时间
        @RequestParam(value = "startCheckTime", required = false, defaultValue = "") String startCheckTime, // 操作时间
                                                                                                            // start
        @RequestParam(value = "endCheckTime", required = false, defaultValue = "") String endCheckTime, // 操作时间 end
        @RequestParam(value = "moneyStatus", required = false, defaultValue = "0") int moneyStatus, // 打款状态
        @RequestParam(value = "receiveType", required = false, defaultValue = "0") int receiveType, // 确认收货状态
        @RequestParam(value = "name", required = false, defaultValue = "") String name, // 用户名
        @RequestParam(value = "receiveName", required = false, defaultValue = "") String receiveName, // 收货人姓名
        @RequestParam(value = "logisticsStatus", required = false, defaultValue = "-1") String logisticsStatus, // 是否有物流
        @RequestParam(value = "mobileNumber", required = false, defaultValue = "") String mobileNumber, // 收货手机
        @RequestParam(value = "sellerId", required = false, defaultValue = "-1") int sellerId, // 商家Id
        @RequestParam(value = "financialAffairsCardId", required = false, defaultValue = "0") int financialAffairsCardId // 财务打款账户
    )
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (number > 0)
        {
            para.put("number", number);
        }
        if (status > 0)
        {
            para.put("status", status);
        }
        if (operationStatus != -1)
        {
            para.put("operationStatus", operationStatus);
        }
        if (orderStatus > 0)
        {
            para.put("orderStatus", orderStatus);
        }
        if (type > 0)
        {
            para.put("type", type);
        }
        if (!"".equals(startTime))
        {
            para.put("startTime", startTime);
        }
        if (!"".equals(endTime))
        {
            para.put("endTime", endTime);
        }
        if (!"".equals(startCheckTime))
        {
            para.put("startCheckTime", startCheckTime);
        }
        if (!"".equals(endCheckTime))
        {
            para.put("endCheckTime", endCheckTime);
        }
        if (moneyStatus > 0)
        {
            para.put("moneyStatus", moneyStatus);
        }
        if (receiveType > 0)
        {
            para.put("receiveType", receiveType);
        }
        if (!"".equals(name))
        {
            para.put("name", name);
        }
        if (!"".equals(receiveName))
        {
            para.put("receiveName", receiveName);
        }
        if (!"".equals(mobileNumber))
        {
            para.put("mobileNumber", mobileNumber);
        }
        if (refundProportionStatus != -1)
        {
            para.put("refundProportionStatus", refundProportionStatus);
        }
        if (sellerId != -1)
        {
            para.put("sellerId", sellerId);
        }
        if (financialAffairsCardId > 0)
        {
            para.put("financialAffairsCardId", financialAffairsCardId);
        }
        para.put("searchOrderChannel", searchOrderChannel);
        para.put("from", "export");
        para.put("logisticsStatus", logisticsStatus);
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            Map<String, Object> result = refundService.findAllRefundInfo(para);
            List<Map<String, Object>> rows = (List<Map<String, Object>>)result.get("rows");
            if (rows.size() > 10000)
            {
                String errorMessage = "数据量超过1万，请筛选后再导出";
                OutputStream servletOutPutStream = null;
                response.setCharacterEncoding("utf-8");
                response.setContentType("text/html;charset=utf-8");
                response.setHeader("Content-disposition", "");
                String errorStr = "<script>alert('" + errorMessage + "');window.history.back();</script>";
                if (servletOutPutStream == null)
                {
                    servletOutPutStream = response.getOutputStream();
                }
                servletOutPutStream.write(errorStr.getBytes());
                servletOutPutStream.close();
                return;
            }
            @SuppressWarnings("unchecked")
            Map<String, RefundProportionEntity> rpesMap = (Map<String, RefundProportionEntity>)result.get("rpesMap");
            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet();
            String[] tableHead = null;
            String fileName = "";
            if (type == 0)
            {
                fileName = "打款和收货查询结果";
                tableHead =
                    new String[] {"退款ID", "订单号", "用户名", "退款发起时间", "最新更新时间", "退货需求", "退款状态", "打款状态", "收货状态", "退款金额", "退款数量", "退款商品ID", "退款商品编码", "商品名称", "购买数量", "收货人", "收货手机",
                        "所属商家", "客服处理备注", "退款账号", "左岸城堡退款账户", "财务选择状态", "商家货款", "商家运费", "商家差价", "左岸城堡货款", "左岸城堡运费", "左岸城堡差价", "退款说明"};
            }
            if (type == 1)
            {
                fileName = "退款订单查询结果";
                tableHead =
                    new String[] {"退款ID", "订单号", "用户名", "退款发起时间", "最新更新时间", "退款状态", "打款状态", "退款金额", "退款数量", "退款商品ID", "退款商品编码", "商品名称", "购买数量", "收货人", "收货手机", "所属商家","退款原因","客服处理备注",
                        "退款账号", "左岸城堡退款账户", "财务选择状态", "商家货款", "商家运费", "商家差价", "左岸城堡货款", "左岸城堡运费", "左岸城堡差价", "退款说明"};
            }
            if (type == 2)
            {
                fileName = "退款退货订单查询结果";
                tableHead =
                    new String[] {"退款ID", "订单号", "用户名", "退款发起时间", "最新更新时间", "退款状态", "打款状态", "收货状态", "退款金额", "退款数量", "退款商品ID", "退款商品编码", "商品名称", "购买数量", "收货人", "收货手机", "所属商家", "退款原因",
                        "客服处理备注", "退款账号", "左岸城堡退款账户", "财务选择状态", "商家货款", "商家运费", "商家差价", "左岸城堡货款", "左岸城堡运费", "左岸城堡差价", "退款说明"};
            }
            Row row = sheet.createRow(0);
            sheet.setDefaultColumnWidth(20);
            for (int i = 0; i < tableHead.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(tableHead[i]);
            }
            for (int i = 0; i < rows.size(); i++)
            {
                int cellIndex = 0;
                Row currRow = sheet.createRow(i + 1);
                Map<String, Object> map = rows.get(i);
                currRow.createCell(cellIndex++).setCellValue(map.get("id") + "");
                currRow.createCell(cellIndex++).setCellValue(map.get("orderNumber") + "");
                currRow.createCell(cellIndex++).setCellValue(map.get("username") + "");
                currRow.createCell(cellIndex++).setCellValue(map.get("createTime") + "");
                currRow.createCell(cellIndex++).setCellValue(map.get("updateTime") + "");
                if (type == 0)
                {
                    currRow.createCell(cellIndex++).setCellValue(map.get("type") + "");
                }
                currRow.createCell(cellIndex++).setCellValue(map.get("statusStr") + "");
                currRow.createCell(cellIndex++).setCellValue(map.get("moneyStatus") + "");
                if (type != 1)
                {
                    currRow.createCell(cellIndex++).setCellValue(map.get("receiveStatus") + "");
                }
                currRow.createCell(cellIndex++).setCellValue(map.get("realMoney") + "");
                currRow.createCell(cellIndex++).setCellValue(map.get("count") + "");
                currRow.createCell(cellIndex++).setCellValue(map.get("productId") + "");
                currRow.createCell(cellIndex++).setCellValue(map.get("productCode") + "");
                currRow.createCell(cellIndex++).setCellValue(map.get("productName") + "");
                currRow.createCell(cellIndex++).setCellValue(map.get("productCount") + "");
                currRow.createCell(cellIndex++).setCellValue(map.get("fullName") + "");
                currRow.createCell(cellIndex++).setCellValue(map.get("mobileNumber") + "");
                // 后面数量大的话可以考虑一次性都取出来，不要一次一次取了。
                // 查询商家
                Integer currSellerId = Integer.parseInt(map.get("sellerId") + "");
                SellerEntity seller = sellerService.findSellerById(currSellerId);
                currRow.createCell(cellIndex++).setCellValue(seller.getRealSellerName() + "_" + seller.getSendAddress());
                //退款原因
                if (type == 1 || type == 2) {
                    currRow.createCell(cellIndex++).
                            setCellValue(map.get("refundReason") == null ? "" : (String) map.get("refundReason"));
                }
                // 客服处理备注
                Integer refundId = Integer.parseInt(map.get("id") + "");
                OrderProductRefundTeackEntity teack = refundService.findRefundTeackByRefundIdAndStep(refundId, 1);
                if (teack != null)
                {
                    currRow.createCell(cellIndex++).setCellValue(teack.getRemark());
                }
                else
                {
                    currRow.createCell(cellIndex++).setCellValue("");
                }
                // 退款账号 改来改去改来改去，妹的改的一团乱
                // Integer accountCardId = Integer.parseInt(map.get("accountCardId") + "");
                // Map<String, Object> cardInfo = refundService.findRefundCardInfo(accountCardId);
                //
                // if (cardInfo != null)
                // {
                // currRow.createCell(cellIndex++).setCellValue(cardInfo.get("cardNumber") + "");
                // }
                // else
                // {
                currRow.createCell(cellIndex++).setCellValue(map.get("cardNumber") + "");
                // }
                if (Integer.parseInt(map.get("status") + "") == CommonEnum.RefundStatusEnum.SUCCESS.ordinal())
                {
                    int typeCode = Integer.parseInt(map.get("typeCode") + "");
                    String gegejiaCard = refundService.findGeGeJiaCardByRefundId(refundId, typeCode);
                    currRow.createCell(cellIndex++).setCellValue(gegejiaCard);
                }
                else
                {
                    currRow.createCell(cellIndex++).setCellValue("");
                }
                if (rpesMap != null)
                {
                    RefundProportionEntity r = rpesMap.get(map.get("id") + "");
                    if (r != null)
                    {
                        currRow.createCell(cellIndex++).setCellValue(RefundEnum.REFUND_PROPORTION_TYPE.getDescByCode(r.getType()));
                        currRow.createCell(cellIndex++).setCellValue(r.getSellerMoney());
                        currRow.createCell(cellIndex++).setCellValue(r.getSellerPostageMoney());
                        currRow.createCell(cellIndex++).setCellValue(r.getSellerDifferenceMoney());
                        currRow.createCell(cellIndex++).setCellValue(r.getGegejiaMoney());
                        currRow.createCell(cellIndex++).setCellValue(r.getGegejiaPostageMoney());
                        currRow.createCell(cellIndex++).setCellValue(r.getGegejiaDifferenceMoney());
                    }
                    else
                    {
                        currRow.createCell(cellIndex++).setCellValue("");
                        currRow.createCell(cellIndex++).setCellValue("");
                        currRow.createCell(cellIndex++).setCellValue("");
                        currRow.createCell(cellIndex++).setCellValue("");
                        currRow.createCell(cellIndex++).setCellValue("");
                        currRow.createCell(cellIndex++).setCellValue("");
                        currRow.createCell(cellIndex++).setCellValue("");
                    }
                }
                else
                {
                    currRow.createCell(cellIndex++).setCellValue("");
                    currRow.createCell(cellIndex++).setCellValue("");
                    currRow.createCell(cellIndex++).setCellValue("");
                    currRow.createCell(cellIndex++).setCellValue("");
                    currRow.createCell(cellIndex++).setCellValue("");
                    currRow.createCell(cellIndex++).setCellValue("");
                    currRow.createCell(cellIndex++).setCellValue("");
                }
                currRow.createCell(cellIndex++).setCellValue(map.get("explain") + "");
            }
            response.setContentType("application/vnd.ms-excel");
            // 进行转码，使其支持中文文件名
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xls");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
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
    
    /**
     * 更新Refund
     * 
     * @param request
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateRefund", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "退款退货管理-更新退款退货积分信息")
    public String updateRefund(HttpServletRequest request, @RequestParam(value = "id", required = true) int id,
        @RequestParam(value = "returnAccountPoint", required = false, defaultValue = "-1") int returnAccountPoint,
        @RequestParam(value = "removeAccountPoint", required = false, defaultValue = "-1") int removeAccountPoint)
        throws Exception
    {
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            if (returnAccountPoint != -1)
            {
                para.put("returnAccountPoint", returnAccountPoint);
                
            }
            if (removeAccountPoint != -1)
            {
                para.put("removeAccountPoint", removeAccountPoint);
                
            }
            Map<String, Object> result = refundService.updateRefund(para);
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error("更新退款退货积分信息失败", e);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", 0);
            map.put("msg", "操作失败");
            return JSON.toJSONString(map);
        }
    }
    
    /**
     * 修改退款金额
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateRefundPrice", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "退款退货管理-修改退款金额")
    public String updateRefundPrice(HttpServletRequest request, @RequestParam(value = "id", required = true) int id,
        @RequestParam(value = "newPrice", required = false, defaultValue = "-1") String newPrice)
        throws Exception
    {
        double refundPrice = -1;
        try
        {
            refundPrice = Double.parseDouble(newPrice);
            if (refundPrice < 0)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("status", 0);
                map.put("msg", "金额有错误，请重新填写");
                return JSON.toJSONString(map);
            }
        }
        catch (Exception e)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", 0);
            map.put("msg", "金额有错误，请重新填写");
            return JSON.toJSONString(map);
        }
        try
        {
            Subject currentUser = SecurityUtils.getSubject();
            User user = null;
            if (currentUser != null)
            {
                String username = currentUser.getPrincipal() + "";
                user = userService.findByUsername(username);
            }
            if (user == null)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("status", 0);
                map.put("msg", "操作失败，您没有操作权限！");
                return JSON.toJSONString(map);
            }
            Map<String, Object> result = refundService.updateRefundPrice(id, refundPrice);
            if ("1".equals(result.get("status")))
            {
                log.warn(user.getUsername() + "修改退款退货金额为" + refundPrice);
            }
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error("重置，取消退款，失败！", e);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", 0);
            map.put("msg", "操作失败");
            return JSON.toJSONString(map);
        }
    }
    
    /**
     * 新建退款退货订单 页面
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/add")
    public ModelAndView addForm(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("refund/add");
        return mv;
    }
    
    /**
     * 保存退款退货
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveRefund", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "退款退货管理-保存退款退货")
    public String saveRefund(
        HttpServletRequest request, //
        @RequestParam(value = "orderId", required = true) int orderId, //
        @RequestParam(value = "accountId", required = true) int accountId, //
        @RequestParam(value = "type", required = true) int type, // 类型；1：仅退款，2：退款并退货
        @RequestParam(value = "number", required = true) long number, // 订单编号
        @RequestParam(value = "orderProductId", required = true) int orderProductId, // 订单商品ID
        @RequestParam(value = "selectProductCount", required = true) int selectProductCount, // 订单商品数量
        @RequestParam(value = "accountCardId", required = true) int accountCardId, // 退款账号ID
        @RequestParam(value = "money", required = true) double money, // 退款金额
        @RequestParam(value = "image1", required = false, defaultValue = "") String image1, @RequestParam(value = "image2", required = false, defaultValue = "") String image2,
        @RequestParam(value = "image3", required = false, defaultValue = "") String image3)
        throws Exception
    {
        try
        {
            RefundEntity refund = new RefundEntity();
            refund.setOrderId(orderId);
            refund.setAccountId(accountId);
            if (accountCardId == 0)
            {
                // 原路返回
                refund.setRefundPayType(RefundEnum.REFUND_PAY_TYPE.RETURN_BACK.getCode());
            }
            else
            {
                refund.setRefundPayType(RefundEnum.REFUND_PAY_TYPE.CREATE_NEW_ACCOUNT_CARD.getCode());
                Map<String, Object> cardInfo = refundService.findRefundCardInfo(accountCardId);
                refund.setCardType(Integer.parseInt(cardInfo.get("type") + ""));
                refund.setBankType(Integer.parseInt(cardInfo.get("bankType") + ""));
                refund.setCardName(cardInfo.get("cardName") + "");
                refund.setCardNumber(cardInfo.get("cardNumber") + "");
            }
            
            // refund.setAccountCardId(accountCardId);
            refund.setOrderProductId(orderProductId);
            refund.setCount(selectProductCount);
            refund.setStatus(CommonEnum.RefundStatusEnum.APPLY.ordinal());
            refund.setType(type);
            refund.setApplyMoney(money);
            refund.setRealMoney(money);
            refund.setImage1((!"".equals(image1) && image1.indexOf(ImageUtil.getPrefix()) == -1) ? image1 + ImageUtil.getPrefix()
                + ImageUtil.getSuffix(ImageTypeEnum.v1orderProduct.ordinal()) : image1);
            refund.setImage2((!"".equals(image2) && image2.indexOf(ImageUtil.getPrefix()) == -1) ? image2 + ImageUtil.getPrefix()
                + ImageUtil.getSuffix(ImageTypeEnum.v1orderProduct.ordinal()) : image2);
            refund.setImage3((!"".equals(image3) && image3.indexOf(ImageUtil.getPrefix()) == -1) ? image3 + ImageUtil.getPrefix()
                + ImageUtil.getSuffix(ImageTypeEnum.v1orderProduct.ordinal()) : image3);
            refund.setCreateTime(DateTimeUtil.now());
            refund.setSourceType(2);//表示客服手动创建退款订单
            Map<String, Object> result = refundService.saveRefund(refund);
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", 0);
            map.put("msg", "保存失败");
            return JSON.toJSONString(map);
        }
    }
    
    /**
     * 保存分摊信息
     * 
     * @param request
     * @param refundId
     * @param refundProportionId
     * @param sellerMoney
     * @param sellerPostageMoney
     * @param sellerDifferenceMoney
     * @param gegejiaMoney
     * @param gegejiaPostageMoney
     * @param gegejiaDifferenceMoney
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveFinanceShare", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "退款退货管理-保存分摊信息")
    public String saveFinanceShare(
        HttpServletRequest request, //
        @RequestParam(value = "refundId", required = true) int refundId, //
        @RequestParam(value = "type", required = true) int type, //
        @RequestParam(value = "refundProportionId", required = false, defaultValue = "0") int refundProportionId, //
        @RequestParam(value = "sellerMoney", required = false, defaultValue = "0") double sellerMoney,
        @RequestParam(value = "sellerPostageMoney", required = false, defaultValue = "0") double sellerPostageMoney,
        @RequestParam(value = "sellerDifferenceMoney", required = false, defaultValue = "0") double sellerDifferenceMoney,
        @RequestParam(value = "gegejiaMoney", required = false, defaultValue = "0") double gegejiaMoney,
        @RequestParam(value = "gegejiaPostageMoney", required = false, defaultValue = "0") double gegejiaPostageMoney,
        @RequestParam(value = "gegejiaDifferenceMoney", required = false, defaultValue = "0") double gegejiaDifferenceMoney)
        throws Exception
    {
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("refundId", refundId);
            para.put("id", refundProportionId);
            para.put("type", type);
            para.put("sellerMoney", sellerMoney);
            para.put("sellerPostageMoney", sellerPostageMoney);
            para.put("sellerDifferenceMoney", sellerDifferenceMoney);
            para.put("gegejiaMoney", gegejiaMoney);
            para.put("gegejiaPostageMoney", gegejiaPostageMoney);
            para.put("gegejiaDifferenceMoney", gegejiaDifferenceMoney);
            Map<String, Object> result = refundService.saveOrUpdateFinanceShare(para);
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", 0);
            map.put("msg", "保存失败");
            return JSON.toJSONString(map);
        }
    }
    
    /**
     * 
     * @创建人: zero
     * @创建时间: 2016年3月19日 下午2:00:12
     * @描述:
     *      <p>
     *      (左岸城堡自动退款导出)
     *      </p>
     * @修改人: zero
     * @修改时间: 2016年3月19日 下午2:00:12
     * @修改备注:
     *        <p>
     *        (修改备注)
     *        </p>
     * @param request
     * @param startTime
     * @param endTime
     * @param nodeId
     * @return
     * @returnType ModelAndView
     * @version V1.0
     */
    @RequestMapping("/mwebAutomaticRefundList")
    @ControllerLog(description = "左岸城堡自动退款")
    public ModelAndView exportMwebAutomaticRefund(HttpServletRequest request, @RequestParam(value = "startTime", required = false, defaultValue = "") String startTime,
        @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime, @RequestParam(value = "nodeId", required = false, defaultValue = "0") String nodeId)
    {
        ModelAndView mv = new ModelAndView("refund/mwebAutomaticRefund");
        try
        {
            
            if ("".equals(startTime))
            {
                startTime = DateTime.now().toString("yyyy-MM-dd");
            }
            
            if ("".equals(endTime))
            {
                endTime = DateTime.now().toString("yyyy-MM-dd");
            }
            //
            // mv.addObject("list", refundService.findMwebAutomaticRefund(startTime, endTime));
            mv.addObject("startTime", startTime);
            mv.addObject("endTime", endTime);
            mv.addObject("nodeId", nodeId);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    @RequestMapping("/jsonMwebAutomaticRefundList")
    @ResponseBody
    public Object jsonProductInfo(HttpServletRequest request, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "startTime", required = false, defaultValue = "") String startTime,
        @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime)
        throws Exception
    {
        
        if ("".equals(startTime))
        {
            startTime = DateTime.now().toString("yyyy-MM-dd");
        }
        
        if ("".equals(endTime))
        {
            endTime = DateTime.now().toString("yyyy-MM-dd");
        }
        
        return refundService.findMwebAutomaticRefund(startTime, endTime);
    }
    
    /**
     * 导出左岸城堡自动退款
     * 
     * @param request
     * @param response
     */
    @RequestMapping(value = "/exportMwebAutomaticRefundList")
    @ControllerLog(description = "导出左岸城堡自动退款")
    public void exportMonthList(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "startTime", required = false, defaultValue = "") String startTime,
        @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime)
    {
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        if ("".equals(startTime))
        {
            startTime = DateTime.now().toString("yyyy-MM-dd");
        }
        
        if ("".equals(endTime))
        {
            endTime = DateTime.now().toString("yyyy-MM-dd");
        }
        
        try
        {
            List<MwebAutomaticRefundEntity> list = refundService.findMwebAutomaticRefund(startTime, endTime);
            
            // 创建一个数值格式化对象
            NumberFormat numberFormat = NumberFormat.getInstance();
            // 设置精确到小数点后2位
            numberFormat.setMaximumFractionDigits(2);
            
            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("左岸城堡自动退款数据");
            String[] str = {"id", "订单id", "订单编号", "订单渠道", "下单时间", "付款时间", "交易号", "平台交易号", "付款渠道", "实付金额", "总价", "退款金额", "支付账户号", "退款时间"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            for (int i = 0; i < list.size(); i++)
            {
                MwebAutomaticRefundEntity mwebAutomaticRefundEntity = list.get(i);
                
                int orderChannel = mwebAutomaticRefundEntity.getOrderChannel();
                int payChannel = mwebAutomaticRefundEntity.getPayChannel();
                
                Row r = sheet.createRow(i + 1);
                r.createCell(0).setCellValue(mwebAutomaticRefundEntity.getId() + "");
                r.createCell(1).setCellValue(mwebAutomaticRefundEntity.getOrderId() + "");
                r.createCell(2).setCellValue(mwebAutomaticRefundEntity.getNumber());
                if (CommonEnum.OrderAppChannelEnum.GEGETUAN_WECHAT.ordinal() == orderChannel)
                {
                    r.createCell(3).setCellValue(OrderAppChannelEnum.GEGETUAN_WECHAT.getDescription());
                }
                else if (CommonEnum.OrderAppChannelEnum.GEGETUAN_APP.ordinal() == orderChannel)
                {
                    r.createCell(3).setCellValue(OrderAppChannelEnum.GEGETUAN_APP.getDescription());
                }
                else if (CommonEnum.OrderAppChannelEnum.GEGETUAN_APP_IOS.ordinal() == orderChannel)
                {
                    r.createCell(3).setCellValue(OrderAppChannelEnum.GEGETUAN_APP_IOS.getDescription());
                }
                else if (CommonEnum.OrderAppChannelEnum.GEGETUAN_APP_ANDROID.ordinal() == orderChannel)
                {
                    r.createCell(3).setCellValue(CommonEnum.OrderAppChannelEnum.GEGETUAN_APP_ANDROID.getDescription());
                }
                
                r.createCell(4).setCellValue(mwebAutomaticRefundEntity.getOrderTime());
                r.createCell(5).setCellValue(mwebAutomaticRefundEntity.getPayTime());
                r.createCell(6).setCellValue(mwebAutomaticRefundEntity.getPayTid());
                r.createCell(7).setCellValue(mwebAutomaticRefundEntity.getPayMark());
                r.createCell(8).setCellValue(OrderEnum.PAY_CHANNEL.getDescByCode(payChannel));
                r.createCell(9).setCellValue(numberFormat.format(mwebAutomaticRefundEntity.getRealPrice()));
                r.createCell(10).setCellValue(numberFormat.format(mwebAutomaticRefundEntity.getTotalPrice()));
                r.createCell(11).setCellValue(numberFormat.format(mwebAutomaticRefundEntity.getRefundPrice()));
                r.createCell(12).setCellValue(mwebAutomaticRefundEntity.getPaymentAccount());
                r.createCell(13).setCellValue(mwebAutomaticRefundEntity.getCreateTime());
            }
            
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = "左岸城堡自动退款";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
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


    @RequestMapping("/dealRefundImmediately")
    @ResponseBody
    @ControllerLog(description = "退款退货管理-左岸城堡直接退款")
    public ResultEntity dealRefundImmediately(@RequestParam(value = "id", required = false, defaultValue = "0") int id,@RequestParam(value = "cardId", required = false, defaultValue = "0") int cardId)
    {
        
        try
        {
            return refundService.dealRefundImmediately(id,cardId);
        }
        catch (Exception e)
        {
            log.error("立即打款失败,order_product_refund_id=" + id, e);
            return ResultEntity.getFailResult("打款失败");
        }
    }
}
