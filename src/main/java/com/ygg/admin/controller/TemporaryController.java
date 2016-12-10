package com.ygg.admin.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.entity.OrderEntity;
import com.ygg.admin.service.AccountService;
import com.ygg.admin.service.OrderService;
import com.ygg.admin.service.TemporaryService;
import com.ygg.admin.util.POIUtil;

/**
 * 主要用于对线上执行一次性操作
 * 
 * @author xiongl
 *
 */
@Controller
@RequestMapping("/temporary")
public class TemporaryController
{
    private static Logger logger = Logger.getLogger(TemporaryController.class);
    
    @Resource
    private OrderService orderServie;
    
    @Resource
    private AccountService accountService;
    
    @Resource
    private TemporaryService temporaryService;
    
    /**
     * 调整用户积分[已经执行]
     * @return
     * @throws Exception
     */
    /*    @RequestMapping(value = "/adjustAccoutPoint", produces = "application/json;charset=UTF-8")
        @ResponseBody
        public String adjustAccoutPoint()
        {
            Map<String, Object> result = new HashMap<String, Object>();
            try
            {
                List<Map<String, Object>> list = orderServie.findAccountSpending();
                int status = accountService.updateAccountIntegralBySpending(list);
                if (status == 1)
                {
                    result.put("msg", "用户积分记录更新成功");
                    result.put("status", 1);
                }
                else
                {
                    result.put("msg", "用户积分记录更新失败");
                    result.put("status", 0);
                }
            }
            catch (Exception e)
            {
                result.put("msg", "用户积分记录更新出现异常");
                result.put("status", -1);
                logger.error(e.getMessage(), e);
            }
            return JSON.toJSONString(result);
        }*/
    
    /**
     * 将图片从又拍云改成阿里云
     * @return
     */
    @RequestMapping(value = "/replaceImagePrefix", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String replaceImageFromUpaiyunToAliyun()
    {
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            int status = temporaryService.updateImageFromUpaiyunToAliyun();
            if (status == 1)
            {
                result.put("msg", "图片前缀替换成功");
                result.put("status", 1);
            }
            else
            {
                result.put("msg", "图片前缀替换失败");
                result.put("status", 0);
            }
        }
        catch (Exception e)
        {
            result.put("msg", "图片前缀替换出现异常");
            result.put("status", -1);
            logger.error(e.getMessage(), e);
        }
        return JSON.toJSONString(result);
    }
    
    /**
     * 已经执行
     * 更新seller表中sellerType=2或3，设为需要身份证号码
     * @return
     */
    /*    @RequestMapping(value = "/updateSellerIsNeedIdCardNumber", produces = "application/json;charset=UTF-8")
        @ResponseBody
        public String updateSellerIsNeedIdCardNumber()
        {
            Map<String, Object> result = new HashMap<String, Object>();
            try
            {
                int status = temporaryService.updateSellerIsNeedIdCardNumber();
                if (status > 0)
                {
                    result.put("msg", "更新成功");
                    result.put("受影响的行数", status);
                    result.put("status", 1);
                }
                else
                {
                    result.put("msg", "更新失败");
                    result.put("status", 0);
                }
            }
            catch (Exception e)
            {
                result.put("msg", "更新出现异常");
                result.put("status", -1);
                logger.error(e.getMessage(), e);
            }
            return JSON.toJSONString(result);
        }*/
    
    /**
     * 已经执行
     * 将特卖和商家信息添加到商家结算周期管理表中
     * @return
     */
    /*    @RequestMapping(value = "/insertSettlement", produces = "application/json;charset=UTF-8")
        @ResponseBody
        public String addSaleWindowToSellerSettlementTable()
        {
            Map<String, Object> result = new HashMap<String, Object>();
            try
            {
                int status = temporaryService.addSaleWindowToSellerSettlementTable();
                if (status > 0)
                {
                    result.put("msg", "更新成功");
                    result.put("受影响的行数", status);
                    result.put("status", 1);
                }
                else
                {
                    result.put("msg", "更新失败");
                    result.put("status", 0);
                }
            }
            catch (Exception e)
            {
                result.put("msg", "更新出现异常");
                result.put("status", -1);
                logger.error(e.getMessage(), e);
            }
            return JSON.toJSONString(result);
        }*/
    
    //    @RequestMapping(value = "/resetOverseasProductInfo", produces = "application/json;charset=UTF-8")
    //    @ResponseBody
    //    public String resetOverseasProductInfo()
    //    {
    //        Map<String, Object> result = new HashMap<String, Object>();
    //        try
    //        {
    //            int status = temporaryService.resetOverseasProductInfo();
    //            if (status > 0)
    //            {
    //                result.put("msg", "更新成功");
    //                result.put("受影响的行数", status);
    //                result.put("status", 1);
    //            }
    //            else
    //            {
    //                result.put("msg", "更新失败");
    //                result.put("status", 0);
    //            }
    //        }
    //        catch (Exception e)
    //        {
    //            result.put("msg", "更新出现异常");
    //            result.put("status", -1);
    //            logger.error(e.getMessage(), e);
    //        }
    //        return JSON.toJSONString(result);
    //    }
    
    /**
     * 导入基本商品信息 【已执行】
     * @param request
     * @param file
     * @return
     */
    /*@RequestMapping(value = "/importBaseProductInfo", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String importBaseProductInfo(HttpServletRequest request, @RequestParam("userFile") MultipartFile file)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            long a = System.currentTimeMillis();
            int status = temporaryService.insertProductBaseInfo(file);
            long b = System.currentTimeMillis();
            if (status > 0)
            {
                result.put("msg", "导入成功,共导入 " + status + "条,共耗时 " + (b - a) / 100 + " 秒");
            }
            else
            {
                result.put("msg", "导入失败");
            }
        }
        catch (Exception e)
        {
            result.put("msg", "导入出现异常");
            logger.error(e.getMessage(), e);
        }
        return JSON.toJSONString(result);
    }*/
    
    //    @RequestMapping(value = "/resetOrderRefundCardInfo", produces = "application/json;charset=UTF-8")
    //    @ResponseBody
    //    public String resetOrderRefundCardInfo()
    //    {
    //        Map<String, Object> result = new HashMap<String, Object>();
    //        try
    //        {
    //            int status = temporaryService.resetOrderRefundCardInfo();
    //            if (status > 0)
    //            {
    //                result.put("msg", "更新成功");
    //                result.put("受影响的行数", status);
    //                result.put("status", 1);
    //            }
    //            else
    //            {
    //                result.put("msg", "更新失败");
    //                result.put("status", 0);
    //            }
    //        }
    //        catch (Exception e)
    //        {
    //            result.put("msg", "更新出现异常");
    //            result.put("status", -1);
    //            logger.error(e.getMessage(), e);
    //        }
    //        return JSON.toJSONString(result);
    //    }
    
    /**
     * 2015-06-26 19:50:21 之后新建的基本商品的详情图排序有问题，这里手动重新排序
     * 已经执行
     * @return
     */
    /*    @RequestMapping(value = "/updateProductBaseDetailOrder", produces = "application/json;charset=UTF-8")
        @ResponseBody
        public String updateProductBaseDetailOrder()
        {
            Map<String, Object> result = new HashMap<String, Object>();
            try
            {
                int status = temporaryService.updateProductBaseDetailOrder();
                if (status > 0)
                {
                    result.put("msg", "更新成功");
                    result.put("受影响的行数", status);
                    result.put("status", 1);
                }
                else
                {
                    result.put("msg", "更新失败");
                    result.put("status", 0);
                }
            }
            catch (Exception e)
            {
                result.put("msg", "更新出现异常");
                result.put("status", -1);
                logger.error(e.getMessage(), e);
            }
            return JSON.toJSONString(result);
        }*/
    /**
     * 商城商品编码有问题，重新调整
     * @return
     */
    /*    @RequestMapping(value = "/updateMallProductCode", produces = "application/json;charset=UTF-8")
        @ResponseBody
        public String updateProductBaseDetailOrder()
        {
            Map<String, Object> result = new HashMap<String, Object>();
            try
            {
                int status = temporaryService.updateMallProductCode();
                if (status > 0)
                {
                    result.put("msg", "更新成功");
                    result.put("受影响的行数", status);
                    result.put("status", 1);
                }
                else
                {
                    result.put("msg", "更新失败");
                    result.put("status", 0);
                }
            }
            catch (Exception e)
            {
                result.put("msg", "更新出现异常");
                result.put("status", -1);
                logger.error(e.getMessage(), e);
            }
            return JSON.toJSONString(result);
        }*/
    
    /**
     * 邀请小伙伴积分调整
     * @return
     */
    
    /*@RequestMapping("/resetInviteIntegralForm")
    public ModelAndView file1()
    {
        ModelAndView mv = new ModelAndView("tmp/resetInviteIntegralForm");
        return mv;
    }*/
    
    /**
     * 邀请小伙伴积分调整 action
     * @param request
     * @param file
     * @return
     */
    /*@RequestMapping(value = "/resetInviteIntegral", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String resetInviteIntegral(HttpServletRequest request, @RequestParam("userFile") MultipartFile file)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            int status = temporaryService.resetInviteIntegral(file);
            if (status > 0)
            {
                result.put("msg", "成功");
                result.put("nums", status);
            }
            else
            {
                result.put("msg", "失败");
            }
        }
        catch (Exception e)
        {
            result.put("msg", "出现异常");
            logger.error(e.getMessage(), e);
        }
        return JSON.toJSONString(result);
    }*/
    
    /*    @RequestMapping(value = "/resetOrderProductCost", produces = "application/json;charset=UTF-8")
        @ResponseBody
        public String resetOrderProductCost(HttpServletRequest request)
        {
            
            Map<String, Object> result = new HashMap<String, Object>();
            try
            {
                int status = temporaryService.resetOrderProductCost();
                if (status > 0)
                {
                    result.put("msg", "成功");
                    result.put("nums", status);
                }
                else
                {
                    result.put("msg", "失败");
                }
            }
            catch (Exception e)
            {
                result.put("msg", "出现异常");
                logger.error(e.getMessage(), e);
            }
            return JSON.toJSONString(result);
        }*/
    
    /**
     * 将relation_game_and_received_mobile、
     *  relation_spread_channel_and_received_mobile表的数据迁移到relation_activity_and_received_mobile
     *  【已执行】
     * 将relation_game_and_account、relation_spread_channel_and_account表的数据迁移到relation_activity_and_account
     * @param request
     * @return
     */
    /*    @RequestMapping(value = "/moveData", produces = "application/json;charset=UTF-8")
        @ResponseBody
        public String moveData(HttpServletRequest request)
        {
            
            Map<String, Object> result = new HashMap<String, Object>();
            try
            {
                result = temporaryService.moveData();
            }
            catch (Exception e)
            {
                result.put("msg", "出现异常");
                logger.error(e.getMessage(), e);
            }
            return JSON.toJSONString(result);
        }*/
    
    /**
     * 导入基本商品信息view 【已执行】
     * @return
     */
    //    @RequestMapping("/fileForm")
    //    public ModelAndView toFileUpLoad()
    //    {
    //        ModelAndView mv = new ModelAndView("tmp/fileupLoad");
    //        return mv;
    //    }
    
    /**
     * 确认导单文件
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/importBaseProductInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String importBaseProductInfo(@RequestParam("userFile") MultipartFile file)
        throws Exception
    {
        Map<String, Object> map = new HashMap<>();
        try
        {
            Map<String, Object> sheetData = POIUtil.getSheetDataAtIndex(file.getInputStream(), 0, true);
            
            if (sheetData == null || (int)sheetData.get("rowNum") == 0)
            {
                map.put("status", 0);
                map.put("msg", "文件为空请确认！");
                return JSON.toJSONString(map);
            }
            
            List<Map<String, Object>> dataList = (List<Map<String, Object>>)sheetData.get("data");
            int status = temporaryService.updateProductBaseSaleFlag(dataList);
            map.put("status", status);
            map.put("msg", status);
            
            return JSON.toJSONString(map);
        }
        catch (IOException e)
        {
            
        }
        return JSON.toJSONString(map);
    }
    
    /**
     * 基本商品修改正品保证【已执行】
     * @param request
     * @return
     */
    /*@RequestMapping(value = "/qualityPromiseType", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String qualityPromiseType()
    {
        
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            int status = temporaryService.qualityPromiseType();
            result.put("msg", "更新" + status + "条数据");
        }
        catch (Exception e)
        {
            result.put("msg", "出现异常");
            logger.error(e.getMessage(), e);
        }
        return JSON.toJSONString(result);
    }*/
    
    /**
     * 转移成为合伙人没有带有的邀请用户
     * @return
     */
    /*@RequestMapping("/toMovePartner")
    public ModelAndView toMovePartner()
    {
        return new ModelAndView("tmp/move_partner");
    }*/
    
    /**
     * 转移成为合伙人没有带走的用户【已执行】
     * @param phoneNumber
     * @return
     */
    /*    @RequestMapping(value = "/movePartner", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
        @ResponseBody
        public String movePartner(@RequestParam(value = "phoneNumber", required = true) String phoneNumber)
        {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            try
            {
                if (!CommonUtil.isMobile(phoneNumber))
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "手机号不正确");
                    return JSON.toJSONString(resultMap);
                }
                int result = temporaryService.movePartner(phoneNumber);
                resultMap.put("status", 1);
                resultMap.put("msg", "转移" + result + "个");
                
            }
            catch (Exception e)
            {
                logger.error("转移合伙人出错了", e);
                resultMap.put("status", 0);
                resultMap.put("msg", "合伙人出现异常");
            }
            return JSON.toJSONString(resultMap);
        }*/
    
    /**
     * 更新商品评价表的基本商品Id【已执行】
     */
    /*@RequestMapping(value = "/updateProductCommentBaseId", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateProductCommentBaseId()
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            int result = temporaryService.updateProductCommentBaseId();
            logger.info("成功更新" + result + "条");
            resultMap.put("msg", "成功更新" + result + "条");
        }
        catch (Exception e)
        {
            logger.error("更新商品评价表的基本商品Id出错了", e);
            resultMap.put("msg", "更新商品评价表的基本商品Id出错了");
        }
        return JSON.toJSONString(resultMap);
    }*/
    
    /**
     * 部分商品和上家的配送地区选择为全部时，将编码从0修改为1【已执行】
     * @return
     */
    /*@RequestMapping(value = "/updateProductAndSellerDeliverArea", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateProductAndSellerDeliverArea()
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            int result = temporaryService.updateProductAndSellerDeliverArea();
            logger.info("成功更新" + result + "条");
            resultMap.put("msg", "成功更新" + result + "条");
        }
        catch (Exception e)
        {
            logger.error("更新商品评价表的基本商品Id出错了", e);
            resultMap.put("msg", "更新商品评价表的基本商品Id出错了");
        }
        return JSON.toJSONString(resultMap);
    }*/
    
    /**
     * 更新订单问题处理明细【已执行】
     * @return
     */
    /*@RequestMapping(value = "/updateOrderQuestionDealDetail", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateOrderQuestionDealDetail()
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            int result = temporaryService.updateOrderQuestionDealDetail();
            logger.info("成功更新" + result + "条");
            resultMap.put("msg", "成功更新" + result + "条");
        }
        catch (Exception e)
        {
            logger.error("更新订单问题处理明细出错了", e);
            resultMap.put("msg", "更新订单问题处理明细出错了");
        }
        return JSON.toJSONString(resultMap);
    }*/
    
    /**
     * 更新订单问题处理图片
     * @return
     */
    /*@RequestMapping(value = "/updateQuestionProgressImage", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateQuestionProgressImage()
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            int result = temporaryService.updateQuestionProgressImage();
            logger.info("成功更新" + result + "条");
            resultMap.put("msg", "成功更新" + result + "条");
        }
        catch (Exception e)
        {
            logger.error("更新订单问题处理明细出错了", e);
            resultMap.put("msg", "更新订单问题处理明细出错了");
        }
        return JSON.toJSONString(resultMap);
    }*/
    
    /**
     * 导出基本商品生产日期、保质期
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/exportBaseProduct")
    public void exportBaseProduct(HttpServletRequest request, HttpServletResponse response)
        // 为0表示不查询结果
        throws Exception
    {
        OutputStream fOut = null;
        Workbook workbook = null;
        try
        {
            
            List<Map<String, Object>> result = temporaryService.findBaseProductSimpleInfo();
            String[] title = {"基本商品ID", "商品名称", "商家", "生产日期", "保质期", "保质期到期提醒日期"};
            workbook = POIUtil.createXSSFWorkbookTemplate(title);
            Sheet sheet = workbook.getSheetAt(0);
            int rIndex = 1;
            for (Map<String, Object> it : result)
            {
                int cellIndex = 0;
                Row r = sheet.createRow(rIndex++);
                r.createCell(cellIndex++).setCellValue(it.get("id") == null ? "0" : it.get("id") + "");
                r.createCell(cellIndex++).setCellValue(it.get("name") == null ? "" : it.get("name") + "");
                r.createCell(cellIndex++).setCellValue(it.get("sellerName") == null ? "" : it.get("sellerName") + "");
                r.createCell(cellIndex++).setCellValue(it.get("manufactureDate") == null ? "" : it.get("manufactureDate") + "");
                r.createCell(cellIndex++).setCellValue(it.get("durabilityPeriod") == null ? "" : it.get("durabilityPeriod") + "");
                r.createCell(cellIndex++).setCellValue("");
            }
            String fileName = "基本商品信息";
            response.setContentType("application/vnd.ms-excel");
            // 进行转码，使其支持中文文件名
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xlsx");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            fOut.close();
        }
        catch (Exception e)
        {
            logger.error("导出出错", e);
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            response.setHeader("content-disposition", "");
            if (fOut == null)
            {
                fOut = response.getOutputStream();
            }
            String errorStr = "<script>alert('系统出错');window.history.back();</script>";
            fOut.write(errorStr.getBytes());
            fOut.flush();
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
    
    @RequestMapping("/fileForm")
    public ModelAndView toFileUpLoad()
    {
        ModelAndView mv = new ModelAndView("tmp/fileupLoad");
        return mv;
    }
    
    @RequestMapping(value = "/readFile", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String readFile(@RequestParam("userFile") MultipartFile file)
        throws Exception
    {
        Map<String, Object> map = new HashMap<>();
        try
        {
            //deleteSon
            Map<String, Object> sheetData = POIUtil.getSheetDataAtIndex(file.getInputStream(), 0, true);
            
            if (sheetData == null || (int)sheetData.get("rowNum") == 0)
            {
                map.put("status", 0);
                map.put("msg", "文件为空请确认！");
                return JSON.toJSONString(map);
            }
            
            int index = 0;
            Set<String> birdexNumber = new HashSet<String>();
            Set<String> otherNumber = new HashSet<String>();
            List<Map<String, Object>> dataList = (List<Map<String, Object>>)sheetData.get("data");
            for (Map<String, Object> it : dataList)
            {
                String number = it.get("cell0") + "";
                OrderEntity oe = orderServie.findOrderByNumber(number.trim());
                if (oe.getSellerId() == 251)
                {
                    birdexNumber.add(number);
                }
                else
                {
                    otherNumber.add(number);
                }
                index++;
            }
            
            logger.info("笨鸟订单：" + birdexNumber.toString());
            logger.info("其他订单：" + otherNumber.toString());
            map.put("msg", "成功读取" + index + "条");
            return JSON.toJSONString(map);
        }
        catch (IOException e)
        {
            
        }
        return JSON.toJSONString(map);
    }
    
    @RequestMapping(value = "/addClassNameAndMethodForPermission", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String addClassNameAndMethodForPermission()
    {
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            long a = System.currentTimeMillis();
            int status = temporaryService.addClassNameAndMethodForPermission();
            long b = System.currentTimeMillis();
            if (status > 0)
            {
                result.put("msg", "共写入 " + status + "条");
            }
            else
            {
                result.put("msg", "写入失败");
            }
        }
        catch (Exception e)
        {
            result.put("msg", "写入异常");
            logger.error(e.getMessage(), e);
        }
        return JSON.toJSONString(result);
    }
    
    @RequestMapping(value = "/updateClassNameAndMethodForPermission", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateClassNameAndMethodForPermission()
    {
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            int status = temporaryService.updateClassNameAndMethodForPermission();
            if (status > 0)
            {
                result.put("msg", "共更新 " + status + "条");
            }
            else
            {
                result.put("msg", "更新失败");
            }
        }
        catch (Exception e)
        {
            result.put("msg", "更新异常");
            logger.error(e.getMessage(), e);
        }
        return JSON.toJSONString(result);
    }
    
    /*    @RequestMapping(value = "/importBaseProductStock", produces = "application/json;charset=UTF-8")
        @ResponseBody
        public String importBaseProductStock(HttpServletRequest request, @RequestParam("userFile") MultipartFile file)
        {
            Map<String, Object> result = new HashMap<String, Object>();
            try
            {
                List<TempProductEntity> reList = temporaryService.importBaseProductStock(file);
                if (reList != null && reList.size() > 0)
                {
                    for (TempProductEntity temp : reList)
                    {
                        temporaryService.returnStock(temp);
                    }
                }
            }
            catch (Exception e)
            {
                result.put("msg", "导入出现异常");
                logger.error(e.getMessage(), e);
            }
            return JSON.toJSONString(result);
        }*/
    
    /*@RequestMapping(value = "/synchronousProductBaseDeliverAreaTemplate", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String synchronousProductBaseDeliverAreaTemplate()
    {
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            return temporaryService.insertSellerDeliverTemplate();
        }
        catch (Exception e)
        {
            result.put("msg", "导入出现异常");
            logger.error(e.getMessage(), e);
        }
        return JSON.toJSONString(result);
    }*/
    
    /*已执行
    @RequestMapping(value = "/updateProductBaseProposalSalesPrice", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateProductBaseProposalSalesPrice()
    {
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            return temporaryService.updateProductBaseProposalSalesPrice();
        }
        catch (Exception e)
        {
            result.put("msg", "更新出现异常");
            logger.error(e.getMessage(), e);
        }
        return JSON.toJSONString(result);
    }*/
    
    /*@RequestMapping(value = "/updateDeliverTemplate", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateDeliverTemplate()
    {
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            return temporaryService.updateDeliverTemplate();
        }
        catch (Exception e)
        {
            result.put("msg", "更新出现异常");
            logger.error(e.getMessage(), e);
        }
        return JSON.toJSONString(result);
    }*/
    
    /*@RequestMapping(value = "/filterRepeatArea", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String filterRepeatArea()
    {
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            return temporaryService.filterRepeatArea();
        }
        catch (Exception e)
        {
            result.put("msg", "更新出现异常");
            logger.error(e.getMessage(), e);
        }
        return JSON.toJSONString(result);
    }*/
    
    @RequestMapping(value = "/deleteProductBaseDeliverTemplate", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String deleteProductBaseDeliverTemplate()
    {
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            return temporaryService.deleteProductBaseDeliverTemplate();
        }
        catch (Exception e)
        {
            result.put("msg", "更新出现异常");
            logger.error(e.getMessage(), e);
        }
        return JSON.toJSONString(result);
    }
    
    @RequestMapping(value = "/exportHBOrderNumber")
    public void exportHBOrderNumber(HttpServletRequest request, HttpServletResponse response)
        // 为0表示不查询结果
        throws Exception
    {
        OutputStream fOut = null;
        Workbook workbook = null;
        try
        {
            Map<String, String> result = temporaryService.findHBOrderNumber();
            String[] title = {"HB订单号", "子订单号"};
            workbook = POIUtil.createXSSFWorkbookTemplate(title);
            Sheet sheet = workbook.getSheetAt(0);
            int rIndex = 1;
            for (Map.Entry<String, String> entry : result.entrySet())
            {
                int cellIndex = 0;
                Row r = sheet.createRow(rIndex++);
                r.createCell(cellIndex++).setCellValue(entry.getValue());
                r.createCell(cellIndex++).setCellValue(entry.getKey());
            }
            String fileName = "订单号";
            response.setContentType("application/vnd.ms-excel");
            // 进行转码，使其支持中文文件名
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xlsx");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            fOut.close();
        }
        catch (Exception e)
        {
            logger.error("导出出错", e);
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            response.setHeader("content-disposition", "");
            if (fOut == null)
            {
                fOut = response.getOutputStream();
            }
            String errorStr = "<script>alert('系统出错');window.history.back();</script>";
            fOut.write(errorStr.getBytes());
            fOut.flush();
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
     * 更新用户等级
     * @return
     */
    /*@RequestMapping(value = "/saveAccountLevelInfo", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String saveAccountLevelInfo()
    {
        Map<String, Object> result = new HashMap<>();
        try
        {
            return temporaryService.saveAccountLevelInfo();
        }
        catch (Exception e)
        {
            result.put("msg", "更新出现异常");
            logger.error(e.getMessage(), e);
        }
        return JSON.toJSONString(result);
    }*/
    
    /**
     * 更新用户等级
     * @return
     */
    @RequestMapping(value = "/updateSellerEdbInfo", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateSellerEdbInfo()
    {
        Map<String, Object> result = new HashMap<>();
        try
        {
            result.put("status", temporaryService.updateSellerEdbInfo());
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            result.put("msg", "updateSellerEdbInfo出现异常");
            logger.error(e.getMessage(), e);
        }
        return JSON.toJSONString(result);
    }

    /**
     * 更新用户等级
     * @return
     */
    @RequestMapping(value = "/updateAccountSecretKey", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateAccountSecretKey()
    {
        Map<String, Object> result = new HashMap<>();
        try
        {
            for (int i = 1 ; i <= 5; i++)
            {
                int page = 1;
                int rows = 2000;
                while (true)
                {
                    Map<String, Object> para = new HashMap<>();
                    para.put("start", rows * (page - 1));
                    para.put("max", rows);
                    para.put("type", i);
                    if (temporaryService.updateAccountSecretKey(para) <= 0)
                    {
                        break;
                    }
                    page++;
                }
            }
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            result.put("msg", "updateAccountSecretKey出现异常");
            logger.error(e.getMessage(), e);
        }
        return JSON.toJSONString(result);
    }
    
    /**
     * 更新退款退货订单信息
     * @return
     */
    @RequestMapping(value = "/updateOrderProductInfo", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateOrderProductInfo()
    {
        Map<String, Object> result = new HashMap<>();
        try
        {
            result.put("status", temporaryService.updateOrderProductInfo());
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            result.put("msg", "updateOrderProductInfo异常");
            logger.error(e.getMessage(), e);
        }
        return JSON.toJSONString(result);
    }
    
    /**
     * 更新基本商品组合件数
     * @return
     */
    @RequestMapping(value = "/updateProductBaseGroupCount", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateProductBaseGroupCount()
    {
        try
        {
            return temporaryService.updateProductBaseGroupCount();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return "{\"msg\":\"updateProductBaseGroupCount出现异常\"}";
        }
    }
    
    /**
     * 更新基本商品累计销量
     * @return
     */
    /*@RequestMapping(value = "/updateProductBaseTotalSales", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateProductBaseTotalSales()
    {
        try
        {
            return temporaryService.updateProductBaseTotalSales();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return "{\"msg\":\"updateProductBaseTotalSales出现异常\"}";
        }
    }*/
    
    /**
     * 更新基本商品的采购商品Id
     * @return
     */
    @RequestMapping(value = "/updateProductBaseProviderProductId", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateProductBaseProviderProductId()
    {
        try
        {
            return temporaryService.updateProductBaseProviderProductId();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return "{\"msg\":\"updateProductBaseProviderProductId出现异常\"}";
        }
    }
    
    /* @RequestMapping(value = "/updateOrderExpireTime", produces = "application/json;charset=UTF-8")
     @ResponseBody
     public String updateOrderExpireTime()
     {
         try
         {
             return temporaryService.updateOrderExpireTime();
         }
         catch (Exception e)
         {
             logger.error(e.getMessage(), e);
             return "{\"msg\":\"updateOrderExpireTime出现异常\"}";
         }
     }*/
    
    /*@RequestMapping(value = "/updateAccountBirthDay", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateAccountBirthDay()
    {
        try
        {
            return temporaryService.updateAccountBirthDay();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return "{\"msg\":\"updateAccountBirthDay出现异常\"}";
        }
    }*/
    
    /*@RequestMapping(value = "/updateAccountPetname", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateAccountPetname()
    {
        try
        {
            return temporaryService.updateAccountPetname();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return "{\"msg\":\"updateAccountPetname出现异常\"}";
        }
    }*/
    
    /*@RequestMapping(value = "/exportOrderRefundExplain")
    public void exportOrderRefundExplain(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        OutputStream fOut = null;
        Workbook workbook = null;
        try
        {
            
            List<Map<String, Object>> result = temporaryService.findOrderRefundExplain("2016-01-01 00:00:00");
            String[] title = {"订单编号", "退款原因"};
            workbook = POIUtil.createXSSFWorkbookTemplate(title);
            Sheet sheet = workbook.getSheetAt(0);
            int rIndex = 1;
            for (Map<String, Object> it : result)
            {
                int cellIndex = 0;
                Row r = sheet.createRow(rIndex++);
                r.createCell(cellIndex++).setCellValue(it.get("number") + "");
                r.createCell(cellIndex++).setCellValue(URLDecoder.decode(it.get("explain") == null ? "" : it.get("explain") + "", "UTF-8"));
            }
            String fileName = "订单退款原因";
            response.setContentType("application/vnd.ms-excel");
            // 进行转码，使其支持中文文件名
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xlsx");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            fOut.close();
        }
        catch (Exception e)
        {
            logger.error("导出出错", e);
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            response.setHeader("content-disposition", "");
            if (fOut == null)
            {
                fOut = response.getOutputStream();
            }
            String errorStr = "<script>alert('系统出错');window.history.back();</script>";
            fOut.write(errorStr.getBytes());
            fOut.flush();
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
    }*/
    
    /*@RequestMapping(value = "/updateOrderLogisticsTimeout", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateOrderLogisticsTimeout()
    {
        try
        {
            return temporaryService.updateOrderLogisticsTimeout();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return "{\"msg\":\"updateOrderLogisticsTimeout出现异常\"}";
        }
    }*/
    
    /*@RequestMapping(value = "/updateOrderProductComment", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateOrderProductComment()
    {
        try
        {
            return temporaryService.updateOrderProductComment();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return "{\"msg\":\"updateOrderProductComment出现异常\"}";
        }
    }*/
    
    /*@RequestMapping(value = "/updateLogisticsTimeoutOrderType", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateLogisticsTimeoutOrderType()
    {
        try
        {
            return temporaryService.updateLogisticsTimeoutOrderType();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return "{\"msg\":\"updateLogisticsTimeoutOrderType出现异常\"}";
        }
    }*/
    
    /*@RequestMapping(value = "/updateDeliverAreaAndExtraPostage", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateDeliverAreaAndExtraPostage()
    {
        try
        {
            return temporaryService.updateDeliverAreaAndExtraPostage();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return "{\"msg\":\"updateDeliverAreaAndExtraPostage出现异常\"}";
        }
    }*/
    
    /**
     * 调整基本商品sale_stock、mall_stock
     * @return
     */
    /*@RequestMapping(value = "/updateProductBaseStock", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateProductBaseStock()
    {
        try
        {
            return temporaryService.updateProductBaseStock();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return "{\"msg\":\"updateProductBaseStock出现异常\"}";
        }
    }*/

    /*@RequestMapping(value = "/exportOrderTimes")
    public void exportOrderTimes(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "minDay", required = false, defaultValue = "0") int minDay,
        @RequestParam(value = "maxDay", required = false, defaultValue = "90") int maxDay)
            throws Exception
    {
        OutputStream fOut = null;
        Workbook workbook = null;
        try
        {
            
            List<Map<String, String>> result = temporaryService.findOrderTimes(minDay,maxDay);
            String[] title = {"总购频次", "消费金额","手机用户","联合登录用户"};
            workbook = POIUtil.createXSSFWorkbookTemplate(title);
            Sheet sheet = workbook.getSheetAt(0);
            int rIndex = 1;
            for (Map<String, String> it : result)
            {
                int cellIndex = 0;
                Row r = sheet.createRow(rIndex++);
                r.createCell(cellIndex++).setCellValue(it.get("totalTimes"));
                r.createCell(cellIndex++).setCellValue(it.get("totalPrice"));
                r.createCell(cellIndex++).setCellValue(it.get("mobileCount"));
                r.createCell(cellIndex++).setCellValue(it.get("unionCount"));
            }
            String fileName = "用户购买频次分析（"+minDay+"-"+maxDay+"）";
            response.setContentType("application/vnd.ms-excel");
            // 进行转码，使其支持中文文件名
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xlsx");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            fOut.close();
        }
        catch (Exception e)
        {
            logger.error("导出出错", e);
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            response.setHeader("content-disposition", "");
            if (fOut == null)
            {
                fOut = response.getOutputStream();
            }
            String errorStr = "<script>alert('系统出错');window.history.back();</script>";
            fOut.write(errorStr.getBytes());
            fOut.flush();
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
    }*/

    /*@RequestMapping(value = "/updateProductDetailImge", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateProductDetailImge()
    {
        try
        {
            return temporaryService.updateProductDetailImge();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return "{\"msg\":\"updateProductDetailImge出现异常\"}";
        }
    }

    @RequestMapping(value = "/insertProductDetailImge", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String insertProductDetailImge()
    {
        try
        {
            return temporaryService.insertProductDetailImge();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return "{\"msg\":\"insertProductDetailImge出现异常\"}";
        }
    }*/

    /*@RequestMapping(value = "/insertProductHistorySalesVolume", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String insertProductHistorySalesVolume(@RequestParam(value = "start",required = true) String start, @RequestParam(value = "end", required = true) String end)
    {
        try
        {
            return temporaryService.insertProductHistorySalesVolume(start, end);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return "{\"msg\":\"insertProductHistorySalesVolume出现异常\"}";
        }
    }*/
}
