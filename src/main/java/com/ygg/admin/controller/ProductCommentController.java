package com.ygg.admin.controller;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.code.ProductEnum;
import com.ygg.admin.entity.OrderProductCommentEntity;
import com.ygg.admin.service.ProductCommentService;
import com.ygg.admin.util.POIUtil;

/**
 * 商品评价管理
 * @author xiongl
 *
 */
@Controller
@RequestMapping("/comment")
public class ProductCommentController
{
    private Logger log = Logger.getLogger(ProductCommentController.class);
    
    @Resource
    private ProductCommentService productCommentService;
    
    @RequestMapping("/list")
    public ModelAndView list()
    {
        ModelAndView mv = new ModelAndView("account/productCommentList");
        return mv;
    }
    
    /**
     * 
     * @param page
     * @param rows
     * @param accountId：用户Id
     * @param username：用户名
     * @param comment：评论内容
     * @param productId：商品Id
     * @param level：评价等级，1差评，2中评，3好评
     * @return
     */
    @RequestMapping(value = "/jsonProductCommentInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonProductCommentInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows,//
        @RequestParam(value = "accountId", required = false, defaultValue = "0") int accountId,//
        @RequestParam(value = "username", required = false, defaultValue = "") String username,//
        @RequestParam(value = "comment", required = false, defaultValue = "") String comment,//
        @RequestParam(value = "productName", required = false, defaultValue = "") String productName,//
        @RequestParam(value = "productId", required = false, defaultValue = "0") int productId,//
        @RequestParam(value = "sellerId", required = false, defaultValue = "-1") int sellerId,//
        @RequestParam(value = "brandId", required = false, defaultValue = "-1") int brandId,//
        @RequestParam(value = "level", required = false, defaultValue = "0") int level)
    {
        Map<String, Object> resultMap = new HashMap<>();
        try
        {
            Map<String, Object> para = new HashMap<>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            if (accountId != 0)
            {
                para.put("accountId", accountId);
            }
            if (!"".equals(username))
            {
                para.put("username", "%" + username + "%");
            }
            if (sellerId != -1)
            {
                para.put("sellerId", sellerId);
            }
            if (brandId != -1)
            {
                para.put("brandId", brandId);
            }
            if (!"".equals(comment))
            {
                para.put("comment", "%" + comment + "%");
            }
            if (!"".equals(productName))
            {
                para.put("productName", "%" + productName + "%");
            }
            if (productId != 0)
            {
                para.put("productId", productId);
            }
            if (level != 0)
            {
                para.put("level", level);
            }
            resultMap = productCommentService.jsonProductCommentInfo(para);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping("/productCommentDetail/{commentId}")
    public ModelAndView productCommentDetail(@PathVariable("commentId") int id)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            mv.setViewName("account/productCommentDetail");
            Map<String, Object> commentMap = productCommentService.findProductCommentByPara(id);
            mv.addObject("commentMap", commentMap);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    /**
     * 导出记录
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/export")
    @ControllerLog(description = "商品评价管理-导出商品评价")
    public void export(HttpServletResponse response, @RequestParam(value = "accountId", required = false, defaultValue = "0") int accountId,//
        @RequestParam(value = "username", required = false, defaultValue = "") String username,//
        @RequestParam(value = "comment", required = false, defaultValue = "") String comment,//
        @RequestParam(value = "productName", required = false, defaultValue = "") String productName,//
        @RequestParam(value = "productId", required = false, defaultValue = "0") int productId,//
        @RequestParam(value = "sellerId", required = false, defaultValue = "-1") int sellerId,//
        @RequestParam(value = "brandId", required = false, defaultValue = "-1") int brandId,//
        @RequestParam(value = "level", required = false, defaultValue = "0") int level)
        throws Exception
    {
        OutputStream fOut = null;
        Workbook workbook = null;
        String errorMessage = "";
        try
        {
            Map<String, Object> para = new HashMap<>();
            if (accountId != 0)
            {
                para.put("accountId", accountId);
            }
            if (!"".equals(username))
            {
                para.put("username", "%" + username + "%");
            }
            if (sellerId != -1)
            {
                para.put("sellerId", sellerId);
            }
            if (brandId != -1)
            {
                para.put("brandId", brandId);
            }
            if (!"".equals(comment))
            {
                para.put("comment", "%" + comment + "%");
            }
            if (!"".equals(productName))
            {
                para.put("productName", "%" + productName + "%");
            }
            if (productId != 0)
            {
                para.put("productId", productId);
            }
            if (level != 0)
            {
                para.put("level", level);
            }
            Map<String, Object> result = productCommentService.jsonProductCommentInfo(para);
            List<Map<String, Object>> rowsList = (List<Map<String, Object>>)result.get("rows");
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = "用户评论记录";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xlsx");
            String[] str = {"用户ID", "用户名", "商品类型", "商品ID", "商品名称", "商家", "购买数量", "总体印象", "商品评论"};
            workbook = POIUtil.createXSSFWorkbookTemplate(str);
            Sheet sheet = workbook.getSheetAt(0);
            int index = 1;
            for (Map<String, Object> currMap : rowsList)
            {
                int cellIndex = 0;
                Row r = sheet.createRow(index++);
                r.createCell(cellIndex++).setCellValue(currMap.get("accountId") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("username") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("typeDesc") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("productId") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("productName") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("realSellerName") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("productAmount") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("levelDesc") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("content") + "");
            }
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
        }
        catch (Exception e)
        {
            if (errorMessage.equals(""))
            {
                log.error(e.getMessage(), e);
                errorMessage = "系统出错";
            }
            response.setHeader("content-disposition", "");
            response.setContentType("text/html");
            if (fOut == null)
            {
                fOut = response.getOutputStream();
            }
            String errorStr = "<script>alert('" + errorMessage + "');window.history.back();</script>";
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
     * 基本商品评论列表
     * @return
     */
    @RequestMapping("/pcList")
    public ModelAndView productBaseCommentList(@RequestParam(value = "productBaseId", required = false, defaultValue = "0") int productBaseId)
    {
        ModelAndView mv = new ModelAndView("productComment/productCommentList");
        if (productBaseId > 0)
        {
            mv.addObject("productBaseId", productBaseId + "");
        }
        return mv;
    }
    
    /**
     * 
     * @param page
     * @param rows
     * @param productBaseId：基本商品Id
     * @param productBaseName：基本商品名称
     * @param sellerId：商家名称
     * @param brandId：品牌名称
     * @return
     */
    @RequestMapping(value = "/jsonProductBaseCommentInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonProductBaseCommentInfo(
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows,//
        @RequestParam(value = "productBaseId", required = false, defaultValue = "0") int productBaseId,//
        @RequestParam(value = "productBaseName", required = false, defaultValue = "") String productBaseName,//
        @RequestParam(value = "sellerId", required = false, defaultValue = "0") int sellerId,//
        @RequestParam(value = "brandId", required = false, defaultValue = "0") int brandId,
        @RequestParam(value = "goodCommentPercent", required = false, defaultValue = "-1") int goodCommentPercent)
    {
        Map<String, Object> resultMap = new HashMap<>();
        try
        {
            Map<String, Object> para = new HashMap<>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            if (productBaseId != 0)
            {
                para.put("productBaseId", productBaseId);
            }
            if (!"".equals(productBaseName))
            {
                para.put("productBaseName", "%" + productBaseName + "%");
            }
            if (sellerId != 0)
            {
                para.put("sellerId", sellerId);
            }
            if (brandId != 0)
            {
                para.put("brandId", brandId);
            }
            if (goodCommentPercent != -1)
            {
                para.put("goodCommentPercent", goodCommentPercent);
            }
            resultMap = productCommentService.jsonProductBaseCommentInfo(para);
        }
        catch (Exception e)
        {
            log.error("异步加载基本商品出错了！！！", e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/exportProductBaseComment")
    @ControllerLog(description = "基本商品管理-导出基本商品评价")
    public void exportProductBaseComment(HttpServletRequest request,
        HttpServletResponse response, //
        @RequestParam(value = "productBaseId", required = false, defaultValue = "0") int productBaseId,//
        @RequestParam(value = "productBaseName", required = false, defaultValue = "") String productBaseName,//
        @RequestParam(value = "sellerId", required = false, defaultValue = "0") int sellerId,//
        @RequestParam(value = "brandId", required = false, defaultValue = "0") int brandId,
        @RequestParam(value = "goodCommentPercent", required = false, defaultValue = "-1") int goodCommentPercent)
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        if (productBaseId != 0)
        {
            para.put("productBaseId", productBaseId);
        }
        if (!"".equals(productBaseName))
        {
            para.put("productBaseName", "%" + productBaseName + "%");
        }
        if (sellerId != 0)
        {
            para.put("sellerId", sellerId);
        }
        if (brandId != 0)
        {
            para.put("brandId", brandId);
        }
        if (goodCommentPercent != -1)
        {
            para.put("goodCommentPercent", goodCommentPercent);
        }
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            Map<String, Object> resultMap = productCommentService.jsonProductBaseCommentInfo(para);
            if (resultMap == null)
            {
                log.error("导出商品查询结果为空");
                return;
            }
            List<Map<String, Object>> resultList = (List<Map<String, Object>>)resultMap.get("rows");
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = "基本商品评价管理";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            // 产生工作簿对象
            workbook = new HSSFWorkbook();
            // 产生工作表对象
            HSSFSheet sheet = workbook.createSheet();
            String[] str = {"基本商品Id", "商品编码", "品牌", "商品名称", "商家", "好评", "中评", "差评", "总评论", "好评率"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            
            if (resultList != null)
            {
                int index = 1;
                for (Map<String, Object> currMap : resultList)
                {
                    int cellIndex = 0;
                    Row r = sheet.createRow(index++);
                    r.createCell(cellIndex++).setCellValue(currMap.get("productBaseId") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("code") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("brandName") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("productBaseName") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("sellerName") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("goodComment") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("middleComment") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("badComment") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("totalComment") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("goodPercent") + "");
                }
            }
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
        }
        catch (Exception e)
        {
            log.error("导出基本商品评价出错", e);
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
    
    @RequestMapping("/productCommentDetailList")
    public ModelAndView productCommentDetailList()
    {
        ModelAndView mv = new ModelAndView("productComment/productCommentDetailList");
        return mv;
    }
    
    /**
     * 基本商品评价列表
     * @param page
     * @param rows
     * @param productBaseId：基本商品Id
     * @param accountId：用户Id
     * @param username：用户名
     * @param orderNo：订单编号
     * @param comment：评论内容
     * @param isDisplay：是否展示
     * @param level：1：差评，2：中评，3：好评
     * @param isHasImage：是否上传图片，-1：全部，1：是，0：否
     * @return
     */
    @RequestMapping(value = "/jsonProductCommentDetailList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonProductCommentDetailList(
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "30") int rows,//
        @RequestParam(value = "productBaseId", required = false, defaultValue = "0") int productBaseId,//
        @RequestParam(value = "accountId", required = false, defaultValue = "0") int accountId,//
        @RequestParam(value = "username", required = false, defaultValue = "") String username,//
        @RequestParam(value = "orderNo", required = false, defaultValue = "") String orderNo,//
        @RequestParam(value = "comment", required = false, defaultValue = "") String comment,//
        @RequestParam(value = "isDisplay", required = false, defaultValue = "-1") int isDisplay,//
        @RequestParam(value = "level", required = false, defaultValue = "0") int level,//
        @RequestParam(value = "isHasImage", required = false, defaultValue = "-1") int isHasImage,
        @RequestParam(value = "sellerId", required = false, defaultValue = "0") int sellerId,//
        @RequestParam(value = "productName", required = false, defaultValue = "") String productName,//
        @RequestParam(value = "brandId", required = false, defaultValue = "0") int brandId)
    {
        Map<String, Object> resultMap = new HashMap<>();
        try
        {
            Map<String, Object> para = new HashMap<>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            if (productBaseId != 0)
            {
                para.put("productBaseId", productBaseId);
            }
            
            if (accountId != 0)
            {
                para.put("accountId", accountId);
            }
            if (!"".equals(username))
            {
                para.put("username", "%" + username + "%");
            }
            if (!"".equals(productName))
            {
                para.put("productName", "%" + productName + "%");
            }
            if (!"".equals(orderNo))
            {
                para.put("orderNo", orderNo);
            }
            if (!"".equals(comment))
            {
                para.put("comment", "%" + comment + "%");
            }
            if (isDisplay != -1)
            {
                para.put("isDisplay", isDisplay);
            }
            if (level != 0)
            {
                para.put("level", level);
            }
            para.put("isHasImage", isHasImage);
            
            if (sellerId != 0)
            {
                para.put("sellerId", sellerId);
            }
            if (brandId != 0)
            {
                para.put("brandId", brandId);
            }
            
            resultMap = productCommentService.jsonProductCommentDetailList(para);
        }
        catch (Exception e)
        {
            log.error("异步加载基本商品详细评论列表出错了", e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 处理反馈
     * @param id
     * @param dealContent
     * @return
     */
    @RequestMapping(value = "/updateDealContent", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商品评论管理-处理反馈")
    public String updateDealContent(@RequestParam(value = "id", required = true) int id,
        @RequestParam(value = "dealContent", required = false, defaultValue = "") String dealContent)
    {
        Map<String, Object> resultMap = new HashMap<>();
        try
        {
            if (com.ygg.admin.util.StringUtils.isEmpty(dealContent))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "处理说明不能为空");
                return JSON.toJSONString(resultMap);
            }
            int result = productCommentService.updateDealContent(id, dealContent);
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
            log.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "处理失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/updateProductCommentDisplayStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商品评价管理-修改商品评价展现状态")
    public String updateProductCommentDisplayStatus(
            @RequestParam(value = "ids", required = true) String ids, 
            @RequestParam(value = "isDisplay", required = true) int isDisplay)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            String[] idList = StringUtils.split(ids, ",");
            para.put("idList", Arrays.asList(idList));
            // 如果是将评论设为展现
            if (isDisplay == 1)
            {
                List<OrderProductCommentEntity> opceList = productCommentService.findProCommentsByIds(para);
                boolean isIdExist = true;
                String productId = null;
                //验证是否有评论
                for(String id:idList){
                    for(OrderProductCommentEntity opce:opceList){
                        if(id.equals(String.valueOf(opce.getId()))){
                            isIdExist = true;
                            break;
                        }else{
                            isIdExist = false;
                            productId = id;
                        }
                    }
                }
                if (!isIdExist)
                {
                    result.put("status", 0);
                    result.put("msg", "商品ID:"+productId+" 评论不存在");
                    return JSON.toJSONString(result);
                }
                //验证是否有左岸城堡回复
                for(OrderProductCommentEntity opce:opceList){
                    if (opce.getLevel() == ProductEnum.PRODUCT_COMMENT_TYPE.BAD.getCode() && StringUtils.isEmpty(opce.getReply()))
                    {
                        result.put("status", 0);
                        result.put("msg", "商品ID："+opce.getProductId()+" 没有经过左岸城堡回复的差评不能展现");
                        return JSON.toJSONString(result);
                    }
                }
            }
            para.put("isDisplay", isDisplay);
            
            int status = productCommentService.updateProductCommentDisplayStatus(para);
            if (status >= 1)
            {
                result.put("status", 1);
            }
            else
            {
                result.put("status", 0);
                result.put("msg", "操作失败");
            }
        }
        catch (Exception e)
        {
            result.put("status", 0);
            result.put("msg", "操作失败");
            log.error(e.getMessage(), e);
        }
        return JSON.toJSONString(result);
    }
    
    @RequestMapping(value = "/replayProductComment", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商品评价管理-回复商品评论")
    public String replayProductComment(
                @RequestParam(value = "id", required = true) int id, 
                @RequestParam(value = "isDisplay", required = false, defaultValue = "1") int isDisplay,
                @RequestParam(value = "reply", required = true) String reply)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            para.put("id", id);
            
            if(0==isDisplay||1==isDisplay){
                para.put("isDisplay", isDisplay);
            }else if(2==isDisplay){
                //只展现文本
                para.put("isDisplay", 1);
                para.put("isDisplayImage", 0);
            }
            para.put("reply", reply);
            
            int status = productCommentService.replayProductComment(para);
            if (status == 1)
            {
                result.put("status", 1);
            }
            else
            {
                result.put("status", 0);
                result.put("msg", "操作失败");
            }
        }
        catch (Exception e)
        {
            result.put("status", 0);
            result.put("msg", "操作失败");
            log.error(e.getMessage(), e);
        }
        return JSON.toJSONString(result);
    }
    
    @RequestMapping(value = "/viewCommentImage/{id}")
    public ModelAndView viewCommentImage(@PathVariable(value = "id") int id)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            OrderProductCommentEntity opce = productCommentService.findProductCommentById(id);
            if (opce == null)
            {
                mv.setViewName("error/404");
            }
            else
            {
                mv.setViewName("productComment/viewCommentImage");
                mv.addObject("comment", opce);
            }
        }
        catch (Exception e)
        {
            log.error("查看评论Id=" + id + "的评论图出错了", e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    @RequestMapping(value = "/updateProductCommentDisplayTextStatus",produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商品评价管理-只展现文本")
    public String updateProductCommentDisplayTextStatus(
            @RequestParam(value = "ids", required = true) String ids, 
            @RequestParam(value = "isDisplay", required = true) int isDisplay,
            @RequestParam(value = "isDisplayImage", required = true) int isDisplayImage)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            String[] idList = StringUtils.split(ids, ",");
            para.put("idList", Arrays.asList(idList));
            // 如果是将评论设为展现
            if (isDisplay == 1)
            {
                List<OrderProductCommentEntity> opceList = productCommentService.findProCommentsByIds(para);
                boolean isIdExist = true;
                String productId = null;
                //验证是否有评论
                for(String id:idList){
                    for(OrderProductCommentEntity opce:opceList){
                        if(id.equals(String.valueOf(opce.getId()))){
                            isIdExist = true;
                            break;
                        }else{
                            isIdExist = false;
                            productId = id;
                        }
                    }
                }
                if (!isIdExist)
                {
                    result.put("status", 0);
                    result.put("msg", "商品ID:"+productId+" 评论不存在");
                    return JSON.toJSONString(result);
                }
                //验证是否有左岸城堡回复
                for(OrderProductCommentEntity opce:opceList){
                    if (opce.getLevel() == ProductEnum.PRODUCT_COMMENT_TYPE.BAD.getCode() && StringUtils.isEmpty(opce.getReply()))
                    {
                        result.put("status", 0);
                        result.put("msg", "商品ID："+opce.getProductId()+" 没有经过左岸城堡回复的差评不能展现");
                        return JSON.toJSONString(result);
                    }
                }
            }
            para.put("isDisplay", isDisplay);
            para.put("isDisplayImage", isDisplayImage);
            
            int status = productCommentService.updateProductCommentDisplayTextStatus(para);
            if (status >= 1)
            {
                result.put("status", 1);
            }
            else
            {
                result.put("status", 0);
                result.put("msg", "操作失败");
            }
        }
        catch (Exception e)
        {
            result.put("status", 0);
            result.put("msg", "操作失败");
            log.error(e.getMessage(), e);
        }
        return JSON.toJSONString(result);
    }
}
