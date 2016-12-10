package com.ygg.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.ygg.admin.code.ProductEnum;
import com.ygg.admin.service.GroupBuyService;
import com.ygg.admin.service.ProductService;
import com.ygg.admin.util.POIUtil;

@Controller
@RequestMapping("/group")
public class GroupBuyController
{
    Logger log = Logger.getLogger(GroupBuyController.class);
    
    @Resource
    private ProductService productService;
    
    @Resource
    private GroupBuyService groupBuyService;
    
    @RequestMapping("/productList")
    public ModelAndView productList(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("groupBuy/productList");
        return mv;
    }
    
    @RequestMapping(value = "/jsonProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonProduct(HttpServletRequest request,// 
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,//
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows,//
        @RequestParam(value = "searchId", required = false, defaultValue = "0") int id, //商品ID
        @RequestParam(value = "code", required = false, defaultValue = "") String code,//
        @RequestParam(value = "name", required = false, defaultValue = "") String name //商品名称
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
        if (!"".equals(code))
        {
            para.put("likecode", "%" + code + "%");
            //            para.put("code", code);
        }
        para.put("activitiesStatus", ProductEnum.PRODUCT_ACTIVITY_STATUS.GROUP_BUY.getCode());
        Map<String, Object> resultMap = productService.jsonProductForManage(para);
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 修改商品团购信息
     * @param productId
     * @param fileType
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateProductGroupStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "团购管理-修改团购商品信息")
    public String updateProductGroupStatus(@RequestParam(value = "productId", required = false, defaultValue = "0") String productId,//
        @RequestParam(value = "fileType", required = true) byte fileType, //导入类型   1:根据productId  ； 2:根据file 
        @RequestParam(value = "productFile", required = false) MultipartFile file)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            int activitiesStatus = ProductEnum.PRODUCT_ACTIVITY_STATUS.GROUP_BUY.getCode();
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("activitiesStatus", activitiesStatus);
            int total = 0;
            if (fileType == 1)
            {
                para.put("id", productId);
                total = productService.updateProductPara(para, false);
            }
            else if (fileType == 2)
            {
                Map<String, Object> data = POIUtil.getSheetDataAtIndex(file.getInputStream(), 0, true);
                if (data != null)
                {
                    List<Map<String, Object>> idList = (List<Map<String, Object>>)data.get("data");
                    if (idList != null && idList.size() > 0)
                    {
                        for (Map<String, Object> it : idList)
                        {
                            Integer cid = Integer.parseInt(it.get("cell0") == null ? "0" : it.get("cell0") + "");
                            if (cid != 0)
                            {
                                para.put("id", cid);
                                total += productService.updateProductPara(para, false);
                            }
                        }
                    }
                }
            }
            resultMap.put("status", 1);
            resultMap.put("msg", "成功保存" + total + "条记录。");
            return JSON.toJSONString(resultMap);
        }
        catch (Exception e)
        {
            log.error("修改商品团购信息失败", e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
            return JSON.toJSONString(resultMap);
        }
    }
    
    @RequestMapping(value = "/deleteProductGroupStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "团购管理-修改团购商品状态")
    public String deleteProductGroupStatus(@RequestParam(value = "ids", required = false, defaultValue = "0") String ids)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            int activitiesStatus = ProductEnum.PRODUCT_ACTIVITY_STATUS.NORMAL.getCode();
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("activitiesStatus", activitiesStatus);
            int total = 0;
            String[] arr = ids.split(",");
            for (String it : arr)
            {
                para.put("id", it);
                total += productService.updateProductPara(para, false);
            }
            resultMap.put("status", 1);
            resultMap.put("msg", "成功删除" + total + "条记录。");
            return JSON.toJSONString(resultMap);
        }
        catch (Exception e)
        {
            log.error("修改商品团购信息失败", e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
            return JSON.toJSONString(resultMap);
        }
    }
    
    /**
     * 团购商品统计列表
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/groupList")
    public ModelAndView groupList(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("groupBuy/groupList");
        return mv;
    }
    
    @RequestMapping(value = "/jsonGroup", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonGroup(HttpServletRequest request,// 
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,//
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows,//
        @RequestParam(value = "id", required = false, defaultValue = "0") int id, //商品ID
        @RequestParam(value = "name", required = false, defaultValue = "") String name //商品名称
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
            para.put("productId", id);
        }
        if (!"".equals(name))
        {
            para.put("productName", "%" + name + "%");
        }
        para.put("activitiesStatus", ProductEnum.PRODUCT_ACTIVITY_STATUS.GROUP_BUY.getCode());
        Map<String, Object> resultMap = groupBuyService.analyzeGroupProduct(para);
        return JSON.toJSONString(resultMap);
    }
}
