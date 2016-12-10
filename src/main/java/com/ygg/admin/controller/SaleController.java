package com.ygg.admin.controller;

import java.io.OutputStream;
import java.math.BigDecimal;
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
import com.ygg.admin.code.ImageTypeEnum;
import com.ygg.admin.code.ProductEnum;
import com.ygg.admin.entity.ActivitiesCommonEntity;
import com.ygg.admin.entity.GegeImageEntity;
import com.ygg.admin.entity.ProductEntity;
import com.ygg.admin.entity.ProductInfoForGroupSale;
import com.ygg.admin.service.ActivitiesCommonService;
import com.ygg.admin.service.GegeImageService;
import com.ygg.admin.service.ProductService;
import com.ygg.admin.util.ImageUtil;
import com.ygg.admin.util.POIUtil;

/**
 * 组合特卖管理Controller
 * 
 * @author Administrator
 *        
 */
@Controller
@RequestMapping("/sale")
public class SaleController
{
    private static Logger logger = Logger.getLogger(SaleController.class);
    
    @Resource(name = "activitiesCommonService")
    private ActivitiesCommonService activitiesCommonService;
    
    @Resource
    private ProductService productService;
    
    @Resource
    private GegeImageService geGeImageService;
    
    /**
     * 转发到组合特卖添加页面
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/addGroupSale")
    public ModelAndView addGroupSale(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("sale/groupSaleForm");
        ActivitiesCommonEntity acCommonEntity = new ActivitiesCommonEntity();
        mv.addObject("acCommon", acCommonEntity);
        // 格格头像
        List<GegeImageEntity> imageList = geGeImageService.findAllGegeImage("sale");
        mv.addObject("gegeImageList", imageList);
        GegeImageEntity geGeImageEntity = geGeImageService.findGegeImageById(acCommonEntity.getGegeImageId(), "sale");
        if (geGeImageEntity == null)
        {
            geGeImageEntity = new GegeImageEntity();
        }
        if (geGeImageEntity.getImage() == null)
        {
            geGeImageEntity.setImage("http://yangege.b0.upaiyun.com/11de43518578b.png");
        }
        
        mv.addObject("geGeImageEntity", geGeImageEntity);
        return mv;
    }
    
    /**
     * 转发到 可用 组合特卖管理页面
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/groupSaleManage")
    public ModelAndView groupSaleManage(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("sale/groupSaleManage");
        return mv;
    }
    
    /**
     * 转发到 停用 组合特卖管理页面
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/groupSaleManageStop")
    public ModelAndView groupSaleManageStop(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("sale/groupSaleManage1");
        return mv;
    }
    
    @RequestMapping(value = "/jsonInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonInfo(HttpServletRequest request, //
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, //
        @RequestParam(value = "rows", required = false, defaultValue = "30") int rows, //
        @RequestParam(value = "id", required = false, defaultValue = "0") int id, //
        @RequestParam(value = "name", required = false, defaultValue = "") String name, //
        @RequestParam(value = "isAvailable", required = false, defaultValue = "-1") byte isAvailable, //
        @RequestParam(value = "productId", required = false, defaultValue = "0") int productId, //
        @RequestParam(value = "productName", required = false, defaultValue = "") String productName, //
        @RequestParam(value = "type", required = false, defaultValue = "-1") int type,
        @RequestParam(value = "productCode", required = false, defaultValue = "") String productCode
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
        if (id != 0)
        {
            para.put("id", id);
        }
        if (!"".equals(name))
        {
            para.put("name", "%" + name + "%");
        }
        if (isAvailable != -1)
        {
            para.put("isAvailable", isAvailable);
        }
        if (productId != 0)
        {
            para.put("productId", productId);
        }
        if (!"".equals(productName))
        {
            para.put("productName", "%" + productName + "%");
        }
        if (type != -1)
        {
            para.put("type", type);
        }
        if(!"".equals(productCode)){
            para.put("productCode", productCode);
        }
        String jsonInfoString = activitiesCommonService.jsonAcCommonInfo(para);
        return jsonInfoString;
    }
    
    @RequestMapping("/editGroupSale/{id}")
    public ModelAndView editGroupSale(HttpServletRequest request, @PathVariable("id") int id)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("sale/groupSaleForm");
        ActivitiesCommonEntity acCommon = activitiesCommonService.findAcCommonById(id);
        
        if (acCommon == null)
        {
            mv.setViewName("forward:/error/404");
            return mv;
        }
        
        // 格格头像
        List<GegeImageEntity> imageList = geGeImageService.findAllGegeImage("sale");
        mv.addObject("gegeImageList", imageList);
        GegeImageEntity geGeImageEntity = geGeImageService.findGegeImageById(acCommon.getGegeImageId(), "sale");
        if (geGeImageEntity == null)
        {
            geGeImageEntity = new GegeImageEntity();
        }
        if (geGeImageEntity.getImage() == null)
        {
            geGeImageEntity.setImage("http://yangege.b0.upaiyun.com/11de43518578b.png");
        }
        
        mv.addObject("geGeImageEntity", geGeImageEntity);
        mv.addObject("acCommon", acCommon);
        return mv;
    }
    
    /**
     * 保存组合特卖信息
     * 
     * @param request
     * @param editId
     * @return
     */
    @RequestMapping("/saveGroupSale")
    @ControllerLog(description = "组合特卖管理-新增/编辑组合特卖信息")
    public ModelAndView saveGroupSale(HttpServletRequest request, @RequestParam(value = "editId", required = false, defaultValue = "0") int editId,
        @RequestParam(value = "name", required = false, defaultValue = "") String name, @RequestParam(value = "desc", required = false, defaultValue = "") String desc,
        @RequestParam(value = "gegesay", required = false, defaultValue = "") String gegesay, @RequestParam(value = "weixin", required = false, defaultValue = "") String weixin,
        @RequestParam(value = "image", required = false, defaultValue = "") String image,
        @RequestParam(value = "gegeImageId", required = false, defaultValue = "1") int gegeImageId,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "0") byte isAvailable,
        @RequestParam(value = "newImage17", required = false, defaultValue = "") String newImage17, @RequestParam(value = "type", required = false, defaultValue = "1") int type)
        throws Exception
    {
        image = newImage17;
        if (image.indexOf(ImageUtil.getPrefix()) > 0)
        {
            image = image.substring(0, image.indexOf(ImageUtil.getPrefix()));
        }

        ModelAndView mv = new ModelAndView();
        ActivitiesCommonEntity aCommonEntity = new ActivitiesCommonEntity();
        aCommonEntity.setDesc(desc);
        aCommonEntity.setGegesay(gegesay);
        aCommonEntity.setWxShareTitle(name);
        aCommonEntity.setImage((image.indexOf(ImageUtil.getPrefix()) > 0) ? image : (image + ImageUtil.getPrefix() + ImageUtil.getSuffix(ImageTypeEnum.v1banner.ordinal())));
        aCommonEntity.setNewImage17((newImage17.indexOf(ImageUtil.getPrefix()) > 0) ? newImage17
            : (newImage17 + ImageUtil.getPrefix() + ImageUtil.getSuffix(ImageTypeEnum.v1brandbanner.ordinal())));
        aCommonEntity.setName(name);
        aCommonEntity.setIsAvailable(isAvailable);
        aCommonEntity.setGegeImageId(gegeImageId);
        aCommonEntity.setType(type);
        int resultStatus = -1;
        if (editId != 0)
        {
            // update
            aCommonEntity.setId(editId);
            resultStatus = activitiesCommonService.update(aCommonEntity);
        }
        else
        {
            // insert
            resultStatus = activitiesCommonService.save(aCommonEntity);
        }
        
        if (resultStatus != 1)
        {
            // 保存失败 转发
            request.getSession().setAttribute("wrongAcCommonInfo", aCommonEntity);
            mv.setViewName("forward:/sale/addGroupSale");
            return mv;
        }
        
        mv.setViewName("redirect:/sale/groupSaleManage");
        return mv;
    }
    
    /**
     * 转发到组合特卖商品管理页面
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/groupSaleProductManage/{id}")
    public ModelAndView groupSaleProductManage(HttpServletRequest request, @PathVariable("id") int id)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            mv.setViewName("sale/groupSaleProductManage");
            ActivitiesCommonEntity ace = activitiesCommonService.findAcCommonById(id);
            mv.addObject("id", id);
            mv.addObject("type", ace.getType());
        }
        catch (Exception e)
        {
            logger.error("Id=" + id + "的商品组合不存在", e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    /**
     * 更新特卖中的商品排序值
     * 
     * @param request
     * @param order
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "组合特卖管理-更新特卖商品排序值")
    public String updateOrder(HttpServletRequest request, @RequestParam(value = "order", required = true) int order, @RequestParam(value = "id", required = true) int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("order", order);
        para.put("id", id);
        int resultStatus = activitiesCommonService.updateOrder(para);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (resultStatus != 1)
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "修改失败");
        }
        else
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "修改成功");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 组合特卖商品信息
     * 
     * @param request
     * @param page
     * @param rows
     * @param id 特卖ID
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jsonGroupSaleProductInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonGroupSaleProductInfo(HttpServletRequest request, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "30") int rows, @RequestParam(value = "id", required = true) int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        para.put("id", id);
        String joString = activitiesCommonService.jsonGroupSaleProductInfo(para);
        return joString;
    }
    
    /**
     * 可用商品列表，供特卖添加，过滤重复商品
     * 
     * @param request
     * @param page
     * @param rows
     * @param id 特卖ID
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jsonProductList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonProductList(HttpServletRequest request, //
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, //
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "code", required = false, defaultValue = "") String code, //
        @RequestParam(value = "name", required = false, defaultValue = "") String name, //
        @RequestParam(value = "status", required = false, defaultValue = "0") int status, //
        @RequestParam(value = "brandId", required = false, defaultValue = "0") int brandId, //
        @RequestParam(value = "sellerId", required = false, defaultValue = "0") int sellerId, //
        @RequestParam(value = "id", required = true) int id, //
        @RequestParam(value = "productId", required = false, defaultValue = "0") int productId, //
        @RequestParam(value = "remark", required = false, defaultValue = "") String remark, //
        @RequestParam(value = "type", required = false, defaultValue = "1") int type, //
        @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable, //
        @RequestParam(value = "isOffShelves", required = false, defaultValue = "0") int isOffShelves)
        throws Exception
    {
        Map resultMap = new HashMap();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        if (status == 0)
        {
            resultMap.put("rows", resultList);
            resultMap.put("total", 0);
            return JSON.toJSONString(resultMap);
        }
        
        Map<String, Object> para = new HashMap<String, Object>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        if (!"".equals(code))
        {
            para.put("code", code);
        }
        if (!"".equals(name))
        {
            para.put("name", "%" + name + "%");
        }
        if (brandId != 0)
        {
            para.put("brandId", brandId);
        }
        if (sellerId != 0)
        {
            para.put("sellerId", sellerId);
        }
        if (productId != 0)
        {
            para.put("productId", productId);
        }
        if (!"".equals(remark))
        {
            para.put("remark", "%" + remark + "%");
        }
        para.put("cid", id);
        para.put("type", type);
        para.put("isAvailable", isAvailable);
        para.put("isOffShelves", isOffShelves);
        
        String jsoString = activitiesCommonService.jsonProductListForAdd(para);
        return jsoString;
    }
    
    @RequestMapping(value = "/deleteGroupSaleProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "组合特卖管理-删除组合特卖商品")
    public String deleteGroupSaleProduct(HttpServletRequest request, @RequestParam(value = "id", required = true) int id)
        throws Exception
    {
        Map resultMap = new HashMap();
        int resultStatus = activitiesCommonService.deleteRelationActivitiesCommonAndProduct(id);
        if (resultStatus != 1)
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "删除失败");
        }
        else
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "删除成功");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/deleteGroupSaleProductList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "组合特卖管理-批量删除组合特卖商品")
    public String deleteGroupSaleProductList(HttpServletRequest request, @RequestParam(value = "ids", required = true) String ids)
        throws Exception
    {
        Map resultMap = new HashMap();
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
        
        int resultStatus = activitiesCommonService.deleteRelationActivitiesCommonAndProductList(idList);
        
        if (resultStatus != 1)
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "删除失败");
        }
        else
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "删除成功");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 特卖添加选中商品
     * 
     * @param request
     * @param ids
     * @param activitiesCommonId 特卖ID
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addGroupSaleProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "组合特卖管理-特卖添加选中商品")
    public String addGroupSaleProduct(HttpServletRequest request, @RequestParam(value = "ids", required = true) String ids,
        @RequestParam(value = "activitiesCommonId", required = true) int activitiesCommonId)
        throws Exception
    {
        
        List<Integer> idList = new ArrayList<Integer>();
        List<Integer> idList_new = new ArrayList<Integer>();
        if (ids.indexOf(",") > 0)
        {
            String[] arr = ids.split(",");
            for (String cur : arr)
            {
                idList.add(Integer.valueOf(cur));
            }
            for (int i = idList.size() - 1; i >= 0; i--)
            {
                idList_new.add(idList.get(i));
            }
        }
        else
        {
            idList_new.add(Integer.valueOf(ids));
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("idList_new", idList_new);
        map.put("activitiesCommonId", activitiesCommonId);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            String productStr = "";
            boolean isCanAdd = true;
            String image1 = "";
            String image2 = "";
            String image3 = "";
            String image4 = "";
            String detail = "";
            for (int productId : idList_new)
            {
                // 检查所有商品的 分销供货价是否满足： salePrice*0.6 < partnerDistributionPrice <= salePrice
                ProductEntity product = productService.findProductById(productId);
                image1 = product.getImage1(); 
                image2 = product.getImage2(); 
                image3 = product.getImage3(); 
                image4 = product.getImage4(); 
                detail = product.getPcDetail(); 
                BigDecimal partnerDistributionPrice = new BigDecimal(product.getPartnerDistributionPrice());
                BigDecimal salePrice = new BigDecimal(product.getSalesPrice());
                if (partnerDistributionPrice.compareTo(salePrice.multiply(new BigDecimal(0.6))) < 0 || partnerDistributionPrice.compareTo(salePrice) > 0)
                {
                    isCanAdd = false;
                    if ("".equals(productStr))
                    {
                        productStr += productId;
                    }
                    else
                    {
                        productStr += "," + productId;
                    }
                }
            }

            if (isCanAdd)
            {
                int status = activitiesCommonService.addGroupSaleProduct(map);
                if (status == 1)
                {
                    resultMap.put("status", 1);
                }
                else
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "保存出错");
                }
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "Id为[" + productStr + "]的特卖商品分销供货价必须大于或等于售价的60%并且小于或等于售价，否则无法保存");
            }
        }
        catch (Exception e)
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "保存出错");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 根据id查询组合特卖信息
     * 
     * @param request
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/findActivitiesCommonInfoById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateOrder(HttpServletRequest request, @RequestParam(value = "id", required = true) int id)
        throws Exception
    {
        ActivitiesCommonEntity ac = activitiesCommonService.findAcCommonById(id);
        if (ac == null)
            ac = new ActivitiesCommonEntity();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("status", 1);
        resultMap.put("name", ac.getName());
        resultMap.put("remark", ac.getDesc());
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 批量添加商品
     * @param request
     * @param ids
     * @param activitiesCommonId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/batchAddGroupSaleProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "组合特卖管理-批量添加商品")
    public String batchAddGroupSaleProduct(//
        @RequestParam(value = "productIds", required = false, defaultValue = "") String productIds,//
        @RequestParam(value = "activitiesCommonId", required = false, defaultValue = "0") int activitiesCommonId,//
        @RequestParam(value = "type", required = false, defaultValue = "1") int type)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (productIds.isEmpty())
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "请输入商品Id");
            return JSON.toJSONString(resultMap);
        }
        if (activitiesCommonId == 0)
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "请选择要添加的组合");
            return JSON.toJSONString(resultMap);
        }
        
        try
        {
            List<Integer> idList_new = new ArrayList<Integer>();
            List<String> productIdList = Arrays.asList(productIds.split(","));
            for (String productId : productIdList)
            {
                // 检查所有商品的 分销供货价是否满足： salePrice*0.6 < partnerDistributionPrice <= salePrice
                ProductEntity product = productService.findProductById(Integer.parseInt(productId.trim()));
                BigDecimal partnerDistributionPrice = new BigDecimal(product.getPartnerDistributionPrice());
                BigDecimal salePrice = new BigDecimal(product.getSalesPrice());
                if (partnerDistributionPrice.compareTo(salePrice.multiply(new BigDecimal(0.6))) < 0 || partnerDistributionPrice.compareTo(salePrice) > 0)
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", String.format("Id为[%s]的商品分销供货价必须大于或等于售价的60%并且小于或等于售价，否则无法保存", productId.trim()));
                    return JSON.toJSONString(resultMap);
                }
                if (product.getType() != type)
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", String.format("当前组合为%s组合,Id=%s的商品类型为%s,无法添加", ProductEnum.PRODUCT_TYPE.getDescByCode(type), productId.trim(), ProductEnum.PRODUCT_TYPE.getDescByCode(product.getType())));
                    return JSON.toJSONString(resultMap);
                }
                idList_new.add(Integer.parseInt(productId.trim()));
            }
            
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("idList_new", idList_new);
            map.put("activitiesCommonId", activitiesCommonId);
            int status = activitiesCommonService.addGroupSaleProduct(map);
            if (status == 1)
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
            logger.error("组合批量添加商品失败", e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存出错");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/exportGroupProduct", produces = "application/json;charset=UTF-8")
    @ControllerLog(description = "商品组合管理-导出商品")
    public void exportGroupProduct(HttpServletRequest request, HttpServletResponse response, //
        @RequestParam(value = "id", required = false, defaultValue = "0") int id)
        throws Exception
    {
        OutputStream fOut = null;
        Workbook workbook = null;
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            
            ActivitiesCommonEntity ace = activitiesCommonService.findAcCommonById(id);
            if (ace == null)
            {
                response.setCharacterEncoding("utf-8");
                response.setContentType("text/html;charset=utf-8");
                response.setHeader("content-disposition", "");
                fOut = response.getOutputStream();
                String errorStr = "<script>alert('组合不存在');window.history.back();</script>";
                fOut.write(errorStr.getBytes());
                fOut.flush();
            }
            else
            {
                List<ProductInfoForGroupSale> productList = activitiesCommonService.findProductInfoForGroupSaleByActivitiesCommonId(para);
                String[] title = {"商品ID", "商品编码", "商品状态", "长名称", "短名称", "商品备注", "销量", "库存", "原价", "现价", "商家", "发货地", "排序值"};
                workbook = POIUtil.createXSSFWorkbookTemplate(title);
                Sheet sheet = workbook.getSheetAt(0);
                int rIndex = 1;
                for (ProductInfoForGroupSale product : productList)
                {
                    int cellIndex = 0;
                    Row r = sheet.createRow(rIndex++);
                    r.createCell(cellIndex++).setCellValue(product.getProductId() + "");
                    r.createCell(cellIndex++).setCellValue(product.getCode());
                    r.createCell(cellIndex++).setCellValue(product.getIsOffShelves() == 1 ? "下架" : "上架");
                    r.createCell(cellIndex++).setCellValue(product.getName());
                    r.createCell(cellIndex++).setCellValue(product.getShortName());
                    r.createCell(cellIndex++).setCellValue(product.getRemark());
                    r.createCell(cellIndex++).setCellValue(product.getSell() + "");
                    r.createCell(cellIndex++).setCellValue(product.getStock() + "");
                    r.createCell(cellIndex++).setCellValue(product.getMarketPrice() + "");
                    r.createCell(cellIndex++).setCellValue(product.getSalesPrice() + "");
                    r.createCell(cellIndex++).setCellValue(product.getSellerName());
                    r.createCell(cellIndex++).setCellValue(product.getSendAddress());
                    r.createCell(cellIndex++).setCellValue(product.getOrder() + "");
                }
                response.setContentType("application/vnd.ms-excel");
                String fileName = ace.getName();
                fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
                response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xlsx");
                fOut = response.getOutputStream();
                workbook.write(fOut);
                fOut.flush();
                fOut.close();
            }
        }
        catch (Exception e)
        {
            logger.error("导出组合商品出错", e);
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
    
    @RequestMapping(value = "/updateProductDisplayStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateProductDisplayStatus(@RequestParam(value = "id", required = true) String id, @RequestParam(value = "code", required = true) int code)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            para.put("isDisplay", code);
            List<Integer> idList = new ArrayList<Integer>();
            String[] idStrArr = id.split(",");
            for (String idStr : idStrArr)
            {
                idList.add(Integer.valueOf(idStr.trim()).intValue());
            }
            para.put("idList", idList);
            int status = activitiesCommonService.updateProductDisplayStatus(para);
            if (status > 0)
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
            logger.error(e.getMessage(), e);
        }
        return JSON.toJSONString(result);
    }
    
    @RequestMapping(value = "/batchUpdateGroupProductTime", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商品组合管理-修改商品时间")
    public String batchUpdateGroupProductTime(@RequestParam(value = "groupIds", required = false, defaultValue = "") String groupIds,
        @RequestParam(value = "startTime", required = false, defaultValue = "") String startTime,
        @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            if (StringUtils.isEmpty(groupIds))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "请输入组合ID");
                return JSON.toJSONString(resultMap);
            }
            if (StringUtils.isEmpty(startTime))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "请选择开始时间");
                return JSON.toJSONString(resultMap);
            }
            if (StringUtils.isEmpty(endTime))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "请选择结束时间");
                return JSON.toJSONString(resultMap);
            }
            return activitiesCommonService.batchUpdateGroupProductTime(groupIds, startTime, endTime);
        }
        catch (Exception e)
        {
            logger.error("商品组合管理批量修改商品时间出错", e);
            resultMap.put("status", 0);
            resultMap.put("msg", "修改失败");
            return JSON.toJSONString(resultMap);
        }
    }
}
