package com.ygg.admin.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.ygg.admin.util.Excel.ExcelMaker;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.joda.time.DateTime;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.code.CouponEnum;
import com.ygg.admin.entity.CouponDetailEntity;
import com.ygg.admin.entity.CouponEntity;
import com.ygg.admin.entity.ProductEntity;
import com.ygg.admin.service.AccountService;
import com.ygg.admin.service.ActivitiesCommonService;
import com.ygg.admin.service.CategoryService;
import com.ygg.admin.service.CouponService;
import com.ygg.admin.service.ProductService;
import com.ygg.admin.service.SellerService;
import com.ygg.admin.service.SystemLogService;
import com.ygg.admin.util.CommonEnum;

/**
 * 优惠券管理
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/coupon")
public class CouponController
{
    @Resource
    private CouponService couponService;
    
    @Resource
    private ActivitiesCommonService activitiesCommonService;
    
    @Resource
    private ProductService productService;
    
    @Resource
    private CategoryService categoryService;
    
    @Resource
    private SellerService sellerService;
    
    @Resource
    private AccountService accountService;
    
    @Resource
    private SystemLogService logService;
    
    private Logger logger = Logger.getLogger(CouponController.class);
    
    @RequestMapping("/list")
    public String couponList()
    {
        return "coupon/couponList";
    }
    
    @RequestMapping("/typeList")
    public String couponTypeList()
    {
        return "coupon/couponTypeList";
    }
    
    /**
     * couponDetail列表
     * 
     * @param page
     * @param rows
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jsonCouponDetailInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonCouponDetailInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "type", required = false, defaultValue = "0") int type,
        @RequestParam(value = "scope", required = false, defaultValue = "0") int scope, @RequestParam(value = "isAvailable", required = false, defaultValue = "-1") int isAvailable)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        if (type != 0)
        {
            para.put("type", type);
        }
        if (scope != 0)
        {
            para.put("scope", scope);
        }
        if (isAvailable != -1)
        {
            para.put("isAvailable", isAvailable);
        }
        Map<String, Object> info = couponService.jsonCouponDetailInfo(para);
        return JSON.toJSONString(info);
    }
    
    /**
     * 保存或更新couponDetail
     * 
     * @param id
     * @param type
     * @param threshold
     * @param reduce1
     * @param reduce2
     * @param scopeType
     * @param scopeId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "优惠券管理-新增/编辑优惠券")
    public String saveOrUpdate(@RequestParam(value = "couponTypeId", required = false, defaultValue = "-1") int id, @RequestParam(value = "type", required = true) int type, // 
        @RequestParam(value = "threshold", required = false, defaultValue = "0") int threshold, // 
        @RequestParam(value = "reduce1", required = false, defaultValue = "0") int reduce1, // 
        @RequestParam(value = "reduce2", required = false, defaultValue = "0") int reduce2, // 
        @RequestParam(value = "maximalReduce3", required = false, defaultValue = "0") int maximalReduce3, // 
        @RequestParam(value = "lowestReduce3", required = false, defaultValue = "0") int lowestReduce3, // 
        @RequestParam(value = "threshold4", required = false, defaultValue = "0") int threshold4, //
        @RequestParam(value = "maximalReduce4", required = false, defaultValue = "0") int maximalReduce4, // 
        @RequestParam(value = "lowestReduce4", required = false, defaultValue = "0") int lowestReduce4, //
        @RequestParam(value = "scopeType", required = true) int scopeType, // 
        @RequestParam(value = "scopeId", required = false, defaultValue = "0") int scopeId, // 
        @RequestParam(value = "desc", required = true) String desc)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            if (type == 1)
            {
                para.put("reduce", reduce1);
                para.put("isRandomReduce", 0);
                para.put("maximalReduce", 0);
                para.put("lowestReduce", 0);
            }
            else if (type == 2)
            {
                threshold = 0;
                para.put("reduce", reduce2);
                para.put("isRandomReduce", 0);
                para.put("maximalReduce", 0);
                para.put("lowestReduce", 0);
            }
            else if (type == 3)
            {
                threshold = 0;
                type = 2;//类型3其实就是类型2，这里转换
                para.put("reduce", 0);
                para.put("isRandomReduce", 1);
                para.put("maximalReduce", maximalReduce3);
                para.put("lowestReduce", lowestReduce3);
            }
            else if (type == 4)
            {
                type = 1;//类型4其实就是类型1，这里转换
                para.put("reduce", 0);
                para.put("isRandomReduce", 1);
                para.put("maximalReduce", maximalReduce4);
                para.put("lowestReduce", lowestReduce4);
                threshold = threshold4;
            }
            para.put("threshold", threshold);
            para.put("type", type);
            para.put("scopeType", scopeType);
            para.put("scopeId", scopeId);
            para.put("desc", desc);
            if (id != -1)
            {
                para.put("id", id);
            }
            int result = couponService.saveOrUpdate(para);
            if (result == 1)
            {
                resultMap.put("status", 1);
                // 记录日志
                if (para.get("id") == null)
                {
                    try
                    {
                        Map<String, Object> logInfoMap = new HashMap<String, Object>();
                        logInfoMap.put("businessType", CommonEnum.BusinessTypeEnum.SALE_MANAGEMENT.ordinal());
                        logInfoMap.put("operationType", CommonEnum.OperationTypeEnum.CREATE_COUPON_DETAIL.ordinal());
                        logInfoMap.put("level", CommonEnum.LogLevelEnum.LEVEL_FOUR.ordinal());
                        logInfoMap.put("object", para);
                        logService.logger(logInfoMap);
                    }
                    catch (Exception e)
                    {
                        logger.error(e.getMessage(), e);
                    }
                    
                }
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "保存失败");
            }
            return JSON.toJSONString(resultMap);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "新增失败");
            return JSON.toJSONString(resultMap);
        }
    }
    
    /**
     * couponDetail的使用范围类型
     * 
     * @param value
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jsonScopeTypeCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonScopeTypeCode(@RequestParam(value = "value", required = false, defaultValue = "-1") String value)
        throws Exception
    {
        
        List<Map<String, Object>> codeList = new ArrayList<Map<String, Object>>();
        for (CouponEnum.SCOPE_TYPE it : CouponEnum.SCOPE_TYPE.values())
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("code", it.getCode());
            map.put("text", it.getDesc());
            if (!value.equals("") && it.getCode() == Integer.valueOf(value))
            {
                map.put("selected", true);
            }
            codeList.add(map);
        }
        codeList.add(0, new HashMap<String, Object>()
        {
            {
                put("code", "0");
                put("text", "--请选择--");
            }
        });
        return JSON.toJSONString(codeList);
    }
    
    /**
     * couponDetail的使用范围类型对应的名称
     * 
     * @param scopeId
     * @param scopeType
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/showScopeTypeName", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String showScopeTypeName(@RequestParam(value = "scopeId", required = true) int scopeId)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String scopeName = "";
        
        ProductEntity product = productService.findProductById(scopeId);
        if (product == null)
        {
            scopeName = "商品不存在";
            resultMap.put("status", 0);
        }
        else
        {
            if (product.getIsAvailable() == 0)
            {
                scopeName = "商品不可用";
                resultMap.put("status", 0);
            }
            else
            {
                scopeName = product.getName();
                resultMap.put("status", 1);
            }
        }
        resultMap.put("msg", scopeName);
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 检查couponDetail是否在使用中
     * 
     * @param couponDetailId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/checkIsInUse", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String checkIsInUse(@RequestParam(value = "id", required = true) int couponDetailId)
        throws Exception
    {
        boolean isInUse = couponService.checkIsInUse(couponDetailId);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        CouponDetailEntity couponDetail = couponService.findCouponDetailById(couponDetailId);
        if (couponDetail.getScopeType() == CouponEnum.SCOPE_TYPE.SPECIFIC_PRODUCT.getCode())
        {
            ProductEntity product = productService.findProductById(couponDetail.getScopeId());
            resultMap.put("productName", product == null ? "" : product.getName());
        }
        if (isInUse)
        {
            resultMap.put("status", 1);
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "使用中，不能进行操作");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 删除conponDetail
     * 
     * @param couponDetailId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "优惠券管理-修改优惠券类型可用状态")
    public String updateCouponDetailStatus(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "isAvailable", required = true) int isAvailable)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        int result = couponService.updateCouponDetailStatus(id, isAvailable);
        if (result == 1)
        {
            resultMap.put("status", 1);
            // 优惠券改状态
            try
            {
                Map<String, Object> logInfoMap = new HashMap<String, Object>();
                logInfoMap.put("businessType", CommonEnum.BusinessTypeEnum.SALE_MANAGEMENT.ordinal());
                logInfoMap.put("operationType", CommonEnum.OperationTypeEnum.CHANGE_COUPON_DETAIL_STATUS.ordinal());
                logInfoMap.put("level", CommonEnum.LogLevelEnum.LEVEL_FOUR.ordinal());
                logInfoMap.put("objectId", id);
                logInfoMap.put("status", isAvailable == 0 ? "可用" : "停用");
                logService.logger(logInfoMap);
            }
            catch (Exception e)
            {
                logger.error(e.getMessage(), e);
            }
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "状态更新失败");
        }
        
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 优惠券列表
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jsonCouponInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonCouponInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "couponId", required = false, defaultValue = "0") int couponId,
        @RequestParam(value = "couponDetailType", required = false, defaultValue = "0") int couponDetailType,
        @RequestParam(value = "couponDetailId", required = false, defaultValue = "0") int couponDetailId,
        @RequestParam(value = "startTimeBegin", required = false, defaultValue = "") String startTimeBegin,
        @RequestParam(value = "startTimeEnd", required = false, defaultValue = "") String startTimeEnd,
        @RequestParam(value = "endTimeBegin", required = false, defaultValue = "") String endTimeBegin,
        @RequestParam(value = "endTimeEnd", required = false, defaultValue = "") String endTimeEnd,
        @RequestParam(value = "couponRemark", required = false, defaultValue = "") String couponRemark)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        if (couponId != 0)
        {
            para.put("couponId", couponId);
        }
        if (couponDetailType != 0)
        {
            para.put("couponDetailType", couponDetailType);
        }
        if (couponDetailId != 0)
        {
            para.put("couponDetailId", couponDetailId);
        }
        if (!"".equals(startTimeBegin))
        {
            para.put("startTimeBegin", startTimeBegin);
        }
        if (!"".equals(startTimeEnd))
        {
            para.put("startTimeEnd", startTimeEnd);
        }
        if (!"".equals(endTimeBegin))
        {
            para.put("endTimeBegin", endTimeBegin);
        }
        if (!"".equals(endTimeEnd))
        {
            para.put("endTimeEnd", endTimeEnd);
        }
        if (!"".equals(couponRemark))
        {
            para.put("couponRemark", "%" + couponRemark + "%");
        }
        Map<String, Object> info = couponService.jsonCouponInfo(para);
        return JSON.toJSONString(info);
    }
    
    @RequestMapping(value = "/jsonCouponType", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonCouponType(@RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable,
        @RequestParam(value = "isRandomReduce", required = false, defaultValue = "0") int isRandomReduce, @RequestParam(value = "id", required = false, defaultValue = "0") int id)
        throws Exception
    {
        Date dateTmp1 = new Date();
        List<Map<String, Object>> codeList = couponService.queryAllCouponType(isAvailable, 1 == isRandomReduce, id);
        Date dateTmp2 = new Date();
        long time = dateTmp2.getTime() - dateTmp1.getTime();
        logger.info("jsonCouponType数据查询完成，一共运行时间【" + (time) + "毫秒】");
        return JSON.toJSONString(codeList);
    }
    
    @RequestMapping(value = "/downloadTemplate")
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "type", required = true) int type)
    {
        response.setContentType("application/vnd.ms-excel");
        String codedFileName = "导入模板样式";
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            // 产生工作簿对象
            workbook = new HSSFWorkbook();
            // 产生工作表对象
            HSSFSheet sheet = workbook.createSheet();
            String[] str = null;
            if (type == 1)
            {
                str = new String[] {"用户Id"};
            }
            else if (type == 2)
            {
                str = new String[] {"用户手机号"};
            }
            
            Row row = sheet.createRow(0);
            Cell cell = row.createCell(0);
            cell.setCellValue(str[0]);
            
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
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
     * 向用户批量发送优惠券
     * 
     * @param request
     * @param file:批量导入文件，当operType=1时，文件中只含有accountId;当operType=2时，文件中只含有name并且name都为手机号
     * @param couponType:优惠券类型
     * @param startTime:优惠券有效起始时间
     * @param endTime:优惠券有效截止时间
     * @param remark:备注
     * @param operType:操作类型，operType=1批量向accoutId发放优惠券;operType=2批量向name发放优惠券;operType=3向单个accoutId发放优惠券;operType=4生成单张优惠券
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sendCouponBatch", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "优惠券管理-向用户批量发送优惠券")
    public String sendCouponBatch(HttpServletRequest request, @RequestParam("userFile") MultipartFile file, @RequestParam(value = "couponType", required = true) int couponType,
        @RequestParam(value = "startTime", required = true) String startTime, @RequestParam(value = "endTime", required = true) String endTime,
        @RequestParam(value = "remark", required = false, defaultValue = "") String remark, @RequestParam(value = "operType", required = true) int operType)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            CouponEntity coupon = new CouponEntity();
            coupon.setCouponDetailId(couponType);
            coupon.setStartTime(startTime);
            coupon.setEndTime(endTime);
            coupon.setRemark(remark);
            coupon.setType(2);//批量
            coupon.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
            
            int result = couponService.sendCoupon(coupon, file, operType, null);
            if (result == -1)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "文件不能为空");
            }
            else if (result == 0)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "发送失败");
            }
            else
            {
                // 发放优惠券记录日志
                try
                {
                    Map<String, Object> logInfoMap = new HashMap<String, Object>();
                    logInfoMap.put("businessType", CommonEnum.BusinessTypeEnum.SALE_MANAGEMENT.ordinal());
                    logInfoMap.put("operationType", CommonEnum.OperationTypeEnum.SEND_COUPON.ordinal());
                    logInfoMap.put("level", CommonEnum.LogLevelEnum.LEVEL_FOUR.ordinal());
                    logInfoMap.put("objectId", couponType);
                    logInfoMap.put("total", coupon.getTotal());
                    logService.logger(logInfoMap);
                }
                catch (Exception e)
                {
                    logger.error(e.getMessage(), e);
                }
                resultMap.put("status", 1);
                resultMap.put("num", result);
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "优惠券发放出错了~");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 向单个用户accountId发放优惠券
     * 
     * @param accountId:用户Id
     * @param couponType:优惠券类型
     * @param startTime:优惠券有效起始时间
     * @param endTime:优惠券有效截止时间
     * @param remark:备注
     * @param operType:操作类型，operType=1批量向accoutId发放优惠券;operType=2批量向name发放优惠券;operType=3向单个accoutId发放优惠券;operType=4生成单张优惠券
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sendCouponSingle", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "优惠券管理-向用户发送优惠券")
    public String sendCouponSingle(@RequestParam(value = "userId", required = true) int accountId, @RequestParam(value = "couponType", required = true) int couponType,
        @RequestParam(value = "startTime", required = true) String startTime, @RequestParam(value = "endTime", required = true) String endTime,
        @RequestParam(value = "remark", required = false, defaultValue = "") String remark, @RequestParam(value = "operType", required = false, defaultValue = "3") int operType)
        throws Exception
    {
        CouponEntity coupon = new CouponEntity();
        coupon.setCouponDetailId(couponType);
        coupon.setStartTime(startTime);
        coupon.setEndTime(endTime);
        coupon.setRemark(remark);
        coupon.setType(1);
        coupon.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        
        int result = couponService.sendCoupon(coupon, null, operType, accountId);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (result == 1)
        {
            resultMap.put("status", 1);
            resultMap.put("num", 1);
            // 发放优惠券记录日志
            try
            {
                Map<String, Object> logInfoMap = new HashMap<String, Object>();
                logInfoMap.put("businessType", CommonEnum.BusinessTypeEnum.SALE_MANAGEMENT.ordinal());
                logInfoMap.put("operationType", CommonEnum.OperationTypeEnum.SEND_COUPON.ordinal());
                logInfoMap.put("level", CommonEnum.LogLevelEnum.LEVEL_FOUR.ordinal());
                logInfoMap.put("objectId", couponType);
                logInfoMap.put("total", 1);
                logService.logger(logInfoMap);
            }
            catch (Exception e)
            {
                logger.error(e.getMessage(), e);
            }
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "发送失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 生成单张优惠券，不与任何用户关联 
     * @param couponType:优惠券类型
     * @param startTime:优惠券有效起始时间
     * @param endTime:优惠券有效截止时间
     * @param remark:备注
     * @param operType:操作类型，operType=1批量向accoutId发放优惠券;operType=2批量向name发放优惠券;operType=3向单个accoutId发放优惠券;operType=4生成单张优惠券
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/createSingleCoupon", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "优惠券管理-生成单张优惠券")
    public String createSingleCoupon(@RequestParam(value = "couponType", required = true) int couponType, @RequestParam(value = "startTime", required = true) String startTime,
        @RequestParam(value = "endTime", required = true) String endTime, @RequestParam(value = "remark", required = false, defaultValue = "") String remark,
        @RequestParam(value = "operType", required = false, defaultValue = "4") int operType)
        throws Exception
    {
        CouponEntity coupon = new CouponEntity();
        coupon.setCouponDetailId(couponType);
        coupon.setStartTime(startTime);
        coupon.setEndTime(endTime);
        coupon.setRemark(remark);
        coupon.setType(1);
        coupon.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        
        int result = couponService.sendCoupon(coupon, null, operType, null);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (result == 1)
        {
            resultMap.put("status", 1);
            resultMap.put("num", 1);
            // 发放优惠券记录日志
            try
            {
                Map<String, Object> logInfoMap = new HashMap<String, Object>();
                logInfoMap.put("businessType", CommonEnum.BusinessTypeEnum.SALE_MANAGEMENT.ordinal());
                logInfoMap.put("operationType", CommonEnum.OperationTypeEnum.SEND_COUPON.ordinal());
                logInfoMap.put("level", CommonEnum.LogLevelEnum.LEVEL_FOUR.ordinal());
                logInfoMap.put("objectId", couponType);
                logInfoMap.put("total", 1);
                logService.logger(logInfoMap);
            }
            catch (Exception e)
            {
                logger.error(e.getMessage(), e);
            }
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "发送失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 查看优惠券明细
     * 
     * @param page
     * @param rows
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jsonCouponAccountInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonCouponAccountInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "couponId", required = true) int couponId,
        @RequestParam(value = "accountId", required = false, defaultValue = "0") int accountId,
        @RequestParam(value = "username", required = false, defaultValue = "") String username,
        @RequestParam(value = "phoneNumber", required = false, defaultValue = "") String phoneNumber,
        @RequestParam(value = "startTimeBegin", required = false, defaultValue = "") String startTimeBegin,
        @RequestParam(value = "startTimeEnd", required = false, defaultValue = "") String startTimeEnd,
        @RequestParam(value = "endTimeBegin", required = false, defaultValue = "") String endTimeBegin,
        @RequestParam(value = "endTimeEnd", required = false, defaultValue = "") String endTimeEnd,
        @RequestParam(value = "isUsed", required = false, defaultValue = "-1") int isUsed)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        para.put("couponId", couponId);
        if (accountId != 0)
        {
            para.put("accountId", accountId);
        }
        if (!"".equals(username))
        {
            para.put("username", "%" + username + "%");
        }
        if (!"".equals(phoneNumber))
        {
            para.put("phoneNumber", phoneNumber);
        }
        if (!"".equals(startTimeBegin))
        {
            para.put("startTimeBegin", startTimeBegin);
        }
        if (!"".equals(startTimeEnd))
        {
            para.put("startTimeEnd", startTimeEnd);
        }
        if (!"".equals(endTimeBegin))
        {
            para.put("endTimeBegin", endTimeBegin);
        }
        if (!"".equals(endTimeEnd))
        {
            para.put("endTimeEnd", endTimeEnd);
        }
        if (isUsed != -1)
        {
            para.put("isUsed", isUsed);
        }
        Map<String, Object> info = couponService.jsonCouponAccountInfo(para);
        return JSON.toJSONString(info);
    }
    
    /**
     * 查看同一批次优惠券明细
     * 
     * @param couponId
     * @return
     */
    @RequestMapping("/couponAccountList/{id}")
    public ModelAndView couponAccountList(@PathVariable("id") int couponId)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            String totalMoney = couponService.findCouponTotalMoney(couponId);//优惠券总金额
            int useAmount = couponService.findCouponUsedInfo(couponId);
            mv.addObject("totalAmount", totalMoney);
            mv.addObject("useAmount", useAmount + "");
            mv.addObject("couponId", couponId + "");
            mv.setViewName("coupon/couponDetailList");
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    /**
     * 查看优惠券订单明细
     * 
     * @param couponAccountId
     * @return
     */
    @RequestMapping("/couponOrderDetailList/{id}")
    public ModelAndView couponOrderDetailList(@PathVariable("id") int couponAccountId)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.addObject("couponAccountId", couponAccountId);
        mv.setViewName("coupon/couponOrderDetailList");
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("couponAccountId", couponAccountId);
        Map<String, Object> map = couponService.queryCouponAccount(para);
        mv.addObject("coupon", map);
        return mv;
    }
    
    @RequestMapping(value = "/jsonCouponOrderDetailInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonCouponOrderDetailInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "couponAccountId", required = true) int couponAccountId)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        para.put("couponAccountId", couponAccountId);
        Map<String, Object> info = couponService.jsonCouponOrderDetailInfo(para);
        return JSON.toJSONString(info);
    }
    
    @RequestMapping(value = "/getCouponInfoById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getCouponInfoById(@RequestParam(value = "id", required = true) int couponId)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        CouponEntity coupon = couponService.findCouponById(couponId);
        if (coupon == null)
        {
            resultMap.put("couponInfo", "优惠券不存在");
            return JSON.toJSONString(resultMap);
        }
        CouponDetailEntity detail = couponService.findCouponDetailById(coupon.getCouponDetailId());
        if (detail == null)
        {
            resultMap.put("couponInfo", "优惠券不存在");
            return JSON.toJSONString(resultMap);
        }
        StringBuilder sb = new StringBuilder();
        if (detail.getType() == 1)
        {
            sb.append("满").append(detail.getThreshold()).append("减").append(detail.getReduce()).append("优惠券-").append(detail.getDesc());
        }
        else if (detail.getType() == 2)
        {
            sb.append(detail.getReduce()).append("元现金券-").append(detail.getDesc());
        }
        resultMap.put("couponInfo", sb.toString());
        return JSON.toJSONString(resultMap);
    }


    @RequestMapping("/exportOrderDetail")
    @ControllerLog(description = "优惠券管理-导出优惠券订单明细")
    public void exportOrderDetail(
            @RequestParam(value = "couponId", required = true) int couponId,
            HttpServletResponse response
    ) throws IOException {
        Map<String, Object> para = new HashMap<>();
        para.put("couponId", couponId);
        OutputStream out = null;
        try {
            out =  response.getOutputStream();
            List<Map<String, Object>> usedCouponOrderInfoList = couponService.getUsedCouponOrderInfoByCouponIdf(para);
            response.setCharacterEncoding(Charsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            String name = URLEncoder.encode("优惠券订单明细", "UTF-8");
            response.setHeader("Content-Disposition", String.format("attachment;filename=%s", name + ".xls"));
            ExcelMaker.from(usedCouponOrderInfoList, Lists.newArrayList("number","payTime","realPrice","couponPrice","sellerName"))
                    .displayHeaders(Lists.newArrayList("订单编号", "付费时间", "订单实付金额", "优惠金额","商家"))
                    .writeTo(out);
        } catch (Exception e) {
            logger.error("导出优惠券订单明细", e);
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            response.setHeader("content-disposition", "");
            String errorStr = "<script>alert('系统出错 重试或联系开发');window.history.back();</script>";
            if (out == null)
                out = response.getOutputStream();
            out.write(errorStr.getBytes());
        }finally {
            if(out != null)
                out.close();
        }
    }
}
