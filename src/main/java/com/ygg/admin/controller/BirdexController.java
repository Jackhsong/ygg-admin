package com.ygg.admin.controller;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.entity.CityEntity;
import com.ygg.admin.entity.DistrictEntity;
import com.ygg.admin.entity.ProvinceEntity;
import com.ygg.admin.service.AreaService;
import com.ygg.admin.service.BirdexService;
import com.ygg.admin.service.OverseasOrderService;
import com.ygg.admin.util.CommonUtil;
import com.ygg.admin.util.IdCardUtil;

@Controller
@RequestMapping("/birdex")
public class BirdexController
{
    Logger log = Logger.getLogger(BirdexController.class);
    
    @Resource
    private BirdexService birdexService;
    
    @Resource
    private AreaService areaService;
    
    @Resource
    private OverseasOrderService overseasOrderService;
    
    /**
     * 笨鸟订单列表
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    public ModelAndView list(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("birdex/list");
        return mv;
    }
    
    /**
     * 异步获取笨鸟订单信息
     */
    @RequestMapping(value = "/jsonBirdexInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonInfo(HttpServletRequest request,//
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,//
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows,// 
        @RequestParam(value = "number", required = false, defaultValue = "") String number,//
        @RequestParam(value = "sendStatus", required = false, defaultValue = "-1") int sendStatus//
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
        if (!"".equals(number))
        {
            para.put("number", number);
        }
        para.put("sendStatus", sendStatus);
        para.put("contextPath", request.getContextPath());
        Map<String, Object> result = birdexService.findAllBirdexOrder(para);
        return JSON.toJSONString(result);
    }
    
    /**
     * 单个 or 批量 确认笨鸟订单
     * @param request
     * @param ids
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sendBirdex", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "笨鸟订单管理-确认笨鸟订单")
    public String sendBirdex(HttpServletRequest request, @RequestParam(value = "ids", required = true) String ids)
        throws Exception
    {
        try
        {
            List<Integer> idList = new ArrayList<>();
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
            Map<String, Object> result = birdexService.sendBirdex(idList);
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error("客服确认 笨鸟订单推送失败！！！", e);
            Map<String, Object> map = new HashMap<>();
            map.put("status", 0);
            map.put("msg", "推送失败");
            return JSON.toJSONString(map);
        }
    }
    
    //    @RequestMapping(value = "/birdexProductList")
    //    public ModelAndView birdexProductList(HttpServletRequest request)
    //        throws Exception
    //    {
    //        ModelAndView mv = new ModelAndView();
    //        mv.setViewName("birdex/birdexProductList");
    //        return mv;
    //    }
    //    
    //    @RequestMapping(value = "/jsonBirdexProductInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    //    @ResponseBody
    //    public String jsonBirdexProductInfo(HttpServletRequest request, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
    //        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "exportName", required = false, defaultValue = "") String exportName,
    //        @RequestParam(value = "code", required = false, defaultValue = "") String code)
    //        throws Exception
    //    {
    //        Map<String, Object> para = new HashMap<String, Object>();
    //        if (page == 0)
    //        {
    //            page = 1;
    //        }
    //        para.put("start", rows * (page - 1));
    //        para.put("max", rows);
    //        if (!"".equals(code))
    //        {
    //            para.put("code", code);
    //        }
    //        String jsonStr = JSON.toJSONString(birdexService.findBirdexProductInfo(para));
    //        return jsonStr;
    //    }
    //    
    //    /**
    //     * 更新 or 新增海外购商品信息
    //     */
    //    @RequestMapping(value = "/saveProduct", produces = "application/json;charset=UTF-8")
    //    @ResponseBody
    //    public String saveProduct(
    //        HttpServletRequest request,
    //        @RequestParam(value = "id", required = false, defaultValue = "0") int id,
    //        @RequestParam(value = "code", required = false, defaultValue = "") String code,//
    //        @RequestParam(value = "remark", required = false, defaultValue = "") String remark,
    //        @RequestParam(value = "exportName", required = false, defaultValue = "") String exportName,
    //        @RequestParam(value = "exportPrice", required = false, defaultValue = "0") double exportPrice,// 单位为元，插入时需转为分
    //        @RequestParam(value = "name", required = false, defaultValue = "") String name)
    //        throws Exception
    //    {
    //        Map<String, Object> map = new HashMap<String, Object>();
    //        if ("".equals(code) || "".equals(exportName) || exportPrice == 0)
    //        {
    //            Map<String, Object> result = new HashMap<String, Object>();
    //            result.put("status", 0);
    //            result.put("msg", "商品编码，导出名称，导出价格必填！");
    //            return JSON.toJSONString(result);
    //        }
    //        if (id != 0)
    //        {
    //            map.put("id", id);
    //        }
    //        map.put("code", code);
    //        map.put("remark", remark);
    //        map.put("exportName", exportName);
    //        BigDecimal exportPrice_big = new BigDecimal(exportPrice);
    //        map.put("exportPrice", exportPrice_big.multiply(new BigDecimal("100")).intValue());
    //        map.put("name", name);
    //        try
    //        {
    //            int status = birdexService.insertOrUpdateBirdexProductInfo(map);
    //            Map<String, Object> result = new HashMap<String, Object>();
    //            result.put("status", status);
    //            result.put("msg", status == 1 ? "保存成功" : "保存失败");
    //            return JSON.toJSONString(result);
    //        }
    //        catch (Exception e)
    //        {
    //            log.error("保存失败", e);
    //            Map<String, Object> result = new HashMap<String, Object>();
    //            result.put("status", 0);
    //            result.put("msg", "保存失败");
    //            return JSON.toJSONString(result);
    //        }
    //    }
    //    
    //    /**
    //     * 删除笨鸟商品信息
    //     */
    //    @RequestMapping(value = "/deleteProduct", produces = "application/json;charset=UTF-8")
    //    @ResponseBody
    //    public String deleteProduct(HttpServletRequest request, @RequestParam(value = "id", required = true) int id)
    //        throws Exception
    //    {
    //        try
    //        {
    //            int status = birdexService.deleteBirdexProductInfoById(id);
    //            Map<String, Object> result = new HashMap<String, Object>();
    //            result.put("status", status);
    //            result.put("msg", status == 1 ? "删除成功" : "删除失败");
    //            return JSON.toJSONString(result);
    //        }
    //        catch (Exception e)
    //        {
    //            log.error("删除海外购商品信息失败", e);
    //            Map<String, Object> result = new HashMap<String, Object>();
    //            result.put("status", 0);
    //            result.put("msg", "删除失败");
    //            return JSON.toJSONString(result);
    //        }
    //    }
    
    /**
     * 笨鸟订单推送列表
     * @param request
     * @return
     */
    @RequestMapping("/pushList")
    public ModelAndView pushList(HttpServletRequest request)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            List<ProvinceEntity> provinceList = areaService.allProvince();
            List<CityEntity> cityList = areaService.findAllCity(new HashMap<String, Object>());
            List<DistrictEntity> districtList = areaService.findAllDistrict(new HashMap<String, Object>());
            mv.addObject("provinceList", provinceList);
            mv.addObject("cityList", cityList);
            mv.addObject("districtList", districtList);
            mv.setViewName("birdex/pushList");
            return mv;
        }
        catch (Exception e)
        {
            mv.setViewName("error/404");
            log.error("加载笨鸟订单推送列表出错", e);
            return mv;
        }
    }
    
    /**
     * 异步加载笨鸟推送列表
     * @param page
     * @param rows
     * @param number
     * @param sendStatus
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jsonBirdexPushInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonBirdexPushInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,//
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows,// 
        @RequestParam(value = "number", required = false, defaultValue = "") String number,//
        @RequestParam(value = "logisticsStatus", required = false, defaultValue = "-1") int logisticsStatus,//
        @RequestParam(value = "pushStatus", required = false, defaultValue = "-1") int pushStatus,//
        @RequestParam(value = "pushRemark", required = false, defaultValue = "") String pushRemark//
    )
    {
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            if (!"".equals(number))
            {
                para.put("number", number);
            }
            if (logisticsStatus != -1)
            {
                para.put("logisticsStatus", logisticsStatus);
            }
            if (pushStatus != -1)
            {
                para.put("pushStatus", pushStatus);
            }
            if (!"".equals(pushRemark))
            {
                para.put("pushRemark", "%" + pushRemark + "%");
            }
            Map<String, Object> result = birdexService.findAllBirdexOrderPushList(para);
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error("异步加载笨鸟订单推送列表出错", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("rows", new ArrayList<Map<String, Object>>());
            result.put("count", 0);
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 取消订单（该订单已经推送给笨鸟）
     * @param orderNumber
     * @param remark
     * @return
     */
    @RequestMapping(value = "/cancelBirdexOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "笨鸟订单管理-取消笨鸟订单")
    public String cancelBirdexOrder(@RequestParam(value = "orderNumber", required = false, defaultValue = "") String orderNumber,
        @RequestParam(value = "remark", required = false, defaultValue = "") String remark)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            if (orderNumber.isEmpty())
            {
                result.put("status", 0);
                result.put("message", "订单编号不能为空");
                return JSON.toJSONString(result);
            }
            Map<String, String> para = new HashMap<String, String>();
            para.put("orderNumber", orderNumber);
            para.put("remark", remark);
            para.put("sourceFrom", "gegejia");
            return birdexService.cancelBirdexOrder(para);
        }
        catch (Exception e)
        {
            log.error("取消订单失败", e);
            result.put("status", 0);
            result.put("message", "取消订单失败");
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 取消推送笨鸟订单（订单还未推送给笨鸟）
     * @return
     */
    @RequestMapping(value = "/cancelPushBirdexOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "笨鸟订单管理-取消推送笨鸟订单")
    public String cancelPushBirdexOrder(@RequestParam(value = "orderId", required = false, defaultValue = "0") int orderId,
        @RequestParam(value = "pushStatus", required = false, defaultValue = "0") int pushStatus)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            if (orderId == 0)
            {
                result.put("status", 0);
                result.put("msg", "订单ID不能为空");
                return JSON.toJSONString(result);
            }
            return birdexService.cancelPushBirdexOrder(orderId, pushStatus);
        }
        catch (Exception e)
        {
            log.error("取消推送订单失败", e);
            result.put("status", 0);
            result.put("msg", "取消推送订单失败");
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 根据订单编号查找订单收货信息
     * @param orderNumber
     * @return
     */
    @RequestMapping(value = "/findReceiveInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String findReceiveInfo(@RequestParam(value = "orderNumber", required = false, defaultValue = "") String orderNumber)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            if (orderNumber.isEmpty())
            {
                result.put("status", 0);
                result.put("msg", "订单编号不能为空");
                return JSON.toJSONString(result);
            }
            return birdexService.findReceiveInfo(orderNumber);
        }
        catch (Exception e)
        {
            log.error("获取订单收货地址发生异常", e);
            result.put("status", 0);
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 修改收货地址
     * @param orderNumber
     * @param fullName
     * @param idCard
     * @param mobileNumber
     * @param province
     * @param city
     * @param district
     * @param detailAddress
     * @return
     */
    @RequestMapping(value = "/updateAddress", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "笨鸟订单管理-修改收获地址")
    public String updateAddress(//
        @RequestParam(value = "orderNumber", required = false, defaultValue = "") String orderNumber,//
        @RequestParam(value = "fullName", required = false, defaultValue = "") String fullName,//
        @RequestParam(value = "idCard", required = false, defaultValue = "") String idCard,//
        @RequestParam(value = "mobileNumber", required = false, defaultValue = "") String mobileNumber,//
        @RequestParam(value = "province", required = false, defaultValue = "0") int province,//
        @RequestParam(value = "city", required = false, defaultValue = "0") int city,//
        @RequestParam(value = "district", required = false, defaultValue = "0") int district,//
        @RequestParam(value = "detailAddress", required = false, defaultValue = "") String detailAddress)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            if (orderNumber.isEmpty())
            {
                result.put("status", 0);
                result.put("msg", "订单编号不能为空");
                return JSON.toJSONString(result);
            }
            if (fullName.isEmpty())
            {
                result.put("status", 0);
                result.put("msg", "收货人姓名不能为空");
                return JSON.toJSONString(result);
            }
            if (!IdCardUtil.verify(idCard))
            {
                result.put("status", 0);
                result.put("msg", "身份证号不正确");
                return JSON.toJSONString(result);
            }
            if (!CommonUtil.isMobile(mobileNumber))
            {
                result.put("status", 0);
                result.put("msg", "手机号不正确");
                return JSON.toJSONString(result);
            }
            if (province == 0 || city == 0 || district == 0)
            {
                result.put("status", 0);
                result.put("msg", "省市区必填");
                return JSON.toJSONString(result);
            }
            if (detailAddress.isEmpty())
            {
                result.put("status", 0);
                result.put("msg", "详细地址不能为空");
                return JSON.toJSONString(result);
            }
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("orderNumber", orderNumber);
            para.put("name", fullName);
            para.put("mobile", mobileNumber);
            para.put("identityNumber", idCard);
            para.put("province", province);
            para.put("city", city);
            para.put("district", district);
            para.put("addressDetail", detailAddress);
            para.put("sourceFrom", "gegejia");
            return birdexService.updateAddress(para);
        }
        catch (Exception e)
        {
            log.error("修改订单收货地址发生异常", e);
            result.put("status", 0);
            result.put("msg", "修改订单收货地址失败");
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 修改身份证
     * @param orderNumber
     * @param fullName
     * @param idCard
     * @return
     */
    @RequestMapping(value = "/updateIdCard", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "笨鸟订单管理-修改身份证号")
    public String updateIdCard(//
        @RequestParam(value = "orderNumber", required = false, defaultValue = "") String orderNumber,//
        @RequestParam(value = "fullName", required = false, defaultValue = "") String fullName,//
        @RequestParam(value = "idCard", required = false, defaultValue = "") String idCard)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            if (orderNumber.isEmpty())
            {
                result.put("status", 0);
                result.put("msg", "订单编号不能为空");
                return JSON.toJSONString(result);
            }
            if (fullName.isEmpty())
            {
                result.put("status", 0);
                result.put("msg", "收货人姓名不能为空");
                return JSON.toJSONString(result);
            }
            if (!IdCardUtil.verify(idCard))
            {
                result.put("status", 0);
                result.put("msg", "身份证号不正确");
                return JSON.toJSONString(result);
            }
            Map<String, Object> para = new HashMap<>();
            para.put("orderNumber", orderNumber);
            para.put("name", fullName);
            para.put("identityNumber", idCard);
            para.put("sourceFrom", "gegejia");
            return birdexService.updateIdCard(para);
        }
        catch (Exception e)
        {
            log.error("修改订单身份证发生异常", e);
            result.put("status", 0);
            result.put("msg", "修改订单身份证失败");
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 笨鸟订单变更记录列表
     * @param request
     * @return
     */
    @RequestMapping("/changeList")
    public ModelAndView changeList(HttpServletRequest request)
    {
        ModelAndView mv = new ModelAndView("birdex/changeList");
        return mv;
    }
    
    /**
     * 异步加载笨鸟变更记录列表
     * @param page
     * @param rows
     * @param number
     * @param sendStatus
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jsonBirdexChangeInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonBirdexChangeInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,//
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows,// 
        @RequestParam(value = "number", required = false, defaultValue = "") String number,//
        @RequestParam(value = "type", required = false, defaultValue = "-1") int type,//
        @RequestParam(value = "status", required = false, defaultValue = "-1") int status)
    {
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            if (!"".equals(number))
            {
                para.put("number", number);
            }
            if (type != -1)
            {
                para.put("type", type);
            }
            if (status != -1)
            {
                para.put("status", status);
            }
            Map<String, Object> result = birdexService.findAllBirdexOrderChangeList(para);
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error("异步加载笨鸟订单变更列表出错", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("rows", new ArrayList<Map<String, Object>>());
            result.put("count", 0);
            return JSON.toJSONString(result);
        }
    }
    
    @RequestMapping("/productList")
    public ModelAndView productList()
    {
        ModelAndView mv = new ModelAndView("birdex/productList");
        return mv;
    }
    
    @RequestMapping(value = "/jsonBirdexProductInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonBirdexProductInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,//
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows,// 
        @RequestParam(value = "productId", required = false, defaultValue = "0") int productId,//
        @RequestParam(value = "productCode", required = false, defaultValue = "") String productCode,//
        @RequestParam(value = "productName", required = false, defaultValue = "") String productName)
    {
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            if (productId != 0)
            {
                para.put("productId", productId);
            }
            if (!"".equals(productCode))
            {
                para.put("productCode", productCode);
            }
            if (!"".equals(productName))
            {
                para.put("productName", "%" + productName + "%");
            }
            return birdexService.findAllBirdexProductList(para);
        }
        catch (Exception e)
        {
            log.error("异步加载笨鸟商品列表出错", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("rows", new ArrayList<Map<String, Object>>());
            result.put("count", 0);
            return JSON.toJSONString(result);
        }
    }
    
    @RequestMapping(value = "/saveOrUpdateBirdexProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "笨鸟商品管理-编辑商品报关价")
    public String saveOrUpdateBirdexProduct(//
        @RequestParam(value = "id", required = false, defaultValue = "0") int id,//
        @RequestParam(value = "productId", required = false, defaultValue = "0") int productId,//
        @RequestParam(value = "price", required = false, defaultValue = "0") float price)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            if (id == 0)
            {
                return birdexService.saveBirdexProduct(productId, price);
            }
            else
            {
                return birdexService.updateBirdexProduct(id, productId, price);
            }
        }
        catch (Exception e)
        {
            log.error("编辑商品报关价发生异常", e);
            result.put("status", 0);
            result.put("msg", "编辑商品报关价失败");
            return JSON.toJSONString(result);
        }
    }
    
    @RequestMapping(value = "/deleteBirdexProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "笨鸟商品管理-删除商品报关价")
    public String deleteBirdexProduct(@RequestParam(value = "id", required = false, defaultValue = "0") int id)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            return birdexService.deleteBirdexProduct(id);
        }
        catch (Exception e)
        {
            log.error("编辑商品报关价发生异常", e);
            result.put("status", 0);
            result.put("msg", "编辑商品报关价失败");
            return JSON.toJSONString(result);
        }
    }
    
    @RequestMapping("/birdexStockList")
    public ModelAndView birdexStockList()
    {
        ModelAndView mv = new ModelAndView("birdex/birdexStockList");
        return mv;
    }
    
    @RequestMapping(value = "/jsonBirdexStockInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonBirdexStockInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows)
    {
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            return birdexService.findAllBirdexStockList(para);
        }
        catch (Exception e)
        {
            log.error("异步加载笨鸟库存列表出错", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("rows", new ArrayList<Map<String, Object>>());
            result.put("count", 0);
            return JSON.toJSONString(result);
        }
    }
    
    @RequestMapping(value = "/refreshBirdexStock", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String refreshBirdexStock()
    {
        try
        {
            return birdexService.refreshBirdexStock();
        }
        catch (Exception e)
        {
            log.error("刷新笨鸟商品库存出错", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "刷新失败");
            return JSON.toJSONString(result);
        }
    }
    
    @RequestMapping(value = "/exportResult")
    @ControllerLog(description = "笨鸟库存管理-导出库存列表")
    public void exportResult(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            List<Map<String, Object>> resultList = birdexService.findAllBirdexStock(new HashMap<String, Object>());
            
            if (resultList == null || resultList.isEmpty())
            {
                log.error("导出笨鸟库存查询结果为空");
                return;
            }
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = "笨鸟库存列表";
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet();
            String[] str = {"仓库", "商品编码", "实际库存", "可用库存", "采购商品ID", "采购商品名称"};
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
                    r.createCell(cellIndex++).setCellValue("香港笨鸟仓");
                    r.createCell(cellIndex++).setCellValue(currMap.get("code") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("availableStock") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("actualStock") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("providerProductId") == null ? "匹配失败" : currMap.get("providerProductId").toString());
                    r.createCell(cellIndex++).setCellValue(currMap.get("providerProductName") == null ? "匹配失败" : currMap.get("providerProductName").toString());
                }
            }
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
        }
        catch (Exception e)
        {
            log.error("导出笨鸟库存列表出错", e);
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
     * 笨鸟取消推送订单列表
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/cancelList")
    public ModelAndView cancelList()
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("birdex/cancelList");
        return mv;
    }
    
    @RequestMapping(value = "/jsonBirdexCancelOrderInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonBirdexCancelOrderInfo(//
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,//
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows,// 
        @RequestParam(value = "number", required = false, defaultValue = "") String number)
    {
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            if (!"".equals(number))
            {
                para.put("number", number);
            }
            return birdexService.findAllBirdexCancelOrder(para);
        }
        catch (Exception e)
        {
            log.error("异步加载笨鸟库存列表出错", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("rows", new ArrayList<Map<String, Object>>());
            result.put("count", 0);
            return JSON.toJSONString(result);
        }
    }
    
    @RequestMapping(value = "/updateBirdexOrderConfirmPushStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "笨鸟订单管理-恢复推送")
    public String updateBirdexOrderConfirmPushStatus(@RequestParam(value = "orderId", required = false, defaultValue = "0") int orderId)
    {
        try
        {
            return birdexService.updateBirdexOrderConfirmPushStatus(orderId);
        }
        catch (Exception e)
        {
            log.error("恢复推送笨鸟订单出错", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "推送失败");
            return JSON.toJSONString(result);
        }
    }
}
