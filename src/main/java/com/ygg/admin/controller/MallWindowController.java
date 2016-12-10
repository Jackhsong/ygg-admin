package com.ygg.admin.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.axis.utils.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.entity.MallPageEntity;
import com.ygg.admin.entity.MallWindowEntity;
import com.ygg.admin.entity.ProductEntity;
import com.ygg.admin.service.MallWindowService;
import com.ygg.admin.service.ProductService;
import com.ygg.admin.service.SaleWindowServcie;
import com.ygg.admin.util.DateTimeUtil;

/**
 * 商城管理
 * @author xiongl
 *
 */
@Controller
@RequestMapping("mallWindow")
public class MallWindowController
{
    private Logger logger = Logger.getLogger(MallWindowController.class);
    
    @Resource
    private MallWindowService mallWindowService;
    
    @Resource
    private ProductService productService;
    
    @Resource(name = "saleWindowServcie")
    private SaleWindowServcie saleWindowServcie;
    
    /**
     * 主题馆View
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    public ModelAndView list()
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("mallWindow/mallWindowList");
        return mv;
    }
    
    /**
     * 主题馆list
     * @param page
     * @param rows
     * @param id
     * @param name
     * @param isDisplay
     * @return
     */
    @RequestMapping(value = "/jsonMallWindowInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonMallWindowInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "id", required = false, defaultValue = "-1") int id,
        @RequestParam(value = "name", required = false, defaultValue = "") String name, // 特卖名称
        @RequestParam(value = "isDisplay", required = false, defaultValue = "1") int isDisplay // 展现状态
    )
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
            if (id != -1)
            {
                para.put("id", id);
            }
            para.put("isDisplay", isDisplay);
            if (!"".equals(name))
            {
                para.put("name", "%" + name + "%");
            }
            resultMap = mallWindowService.findAllMallWindow(para);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 修改主题馆展现状态
     * @param id
     * @param code：1:展现；0:不展现
     * @return
     */
    @RequestMapping(value = "/updateDisplayCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateDisplayCode(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "code", required = true) int code)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            para.put("id", id);
            para.put("isDisplay", code);
            int status = mallWindowService.updateMallWindowByPara(para);
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
            logger.error(e.getMessage(), e);
        }
        
        return JSON.toJSONString(result);
    }
    
    /**
     * 修改主题馆排序值
     * @param id
     * @param sequence
     * @return
     */
    @RequestMapping(value = "/updateSequence", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateSequence(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "sequence", required = true) int sequence)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            para.put("id", id);
            para.put("sequence", sequence);
            int status = mallWindowService.updateMallWindowByPara(para);
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
            logger.error(e.getMessage(), e);
        }
        
        return JSON.toJSONString(result);
    }
    
    /**
     * 主题馆新增页面
     * @return
     */
    @RequestMapping("/addMallWindow")
    public ModelAndView addMallWindow()
    {
        ModelAndView mv = new ModelAndView("mallWindow/update");
        MallWindowEntity mallWindow = new MallWindowEntity();
        mv.addObject("mallWindow", mallWindow);
        return mv;
    }
    
    /**
     * 新增/修改主题馆
     * @param mallWindow
     * @return
     */
    @RequestMapping("/save")
    public ModelAndView save(MallWindowEntity mallWindow)
    {
        ModelAndView mv = null;
        try
        {
            mallWindowService.saveOrUpdate(mallWindow);
            mv = new ModelAndView("redirect:/mallWindow/list");
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            mv = new ModelAndView("error/500");
        }
        return mv;
    }
    
    /**
     * 获取楼层Code,默认获取可用楼层
     * @param isAvailable
     * @param mallPageId
     * @return
     */
    @RequestMapping(value = "/jsonMallPageCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonMallPageCode(@RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable,
        @RequestParam(value = "mallPageId", required = false, defaultValue = "0") int mallPageId)
    {
        List<Map<String, String>> codeList = new ArrayList<Map<String, String>>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("isAvailable", isAvailable);
            List<Map<String, Object>> list = mallWindowService.findAllMallPage(para);
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
                if (mallPageId == Integer.valueOf(entity.get("id") + "").intValue())
                {
                    map.put("selected", "true");
                }
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
     * 编辑主题馆View
     * @param id
     * @return
     */
    @RequestMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") int id)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            MallWindowEntity mallWindow = mallWindowService.findMallWindowById(id);
            if (mallWindow == null)
            {
                mv.setViewName("forward:/error/404");
                return mv;
            }
            mv.addObject("mallWindow", mallWindow);
            mv.setViewName("mallWindow/update");
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            mv.setViewName("error/500");
        }
        return mv;
    }
    
    /**
     * 主题馆页面View
     * @return
     * @throws Exception
     */
    @RequestMapping("/pageList")
    public ModelAndView mallPageList()
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("mallWindow/mallPageList");
        return mv;
    }
    
    /**
     * 主题馆页面List
     * @param page
     * @param rows
     * @param id:页面Id
     * @param name:页面名称
     * @param isAvailable:是否可用
     * @return
     */
    @RequestMapping(value = "/jsonMallPageInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonMallPageInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "id", required = false, defaultValue = "-1") int id,
        @RequestParam(value = "name", required = false, defaultValue = "") String name, @RequestParam(value = "isAvailable", required = false, defaultValue = "-1") int isAvailable)
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
            if (id != -1)
            {
                para.put("id", id);
            }
            if (isAvailable != -1)
            {
                para.put("isAvailable", isAvailable);
            }
            if (!"".equals(name))
            {
                para.put("name", "%" + name + "%");
            }
            resultMap = mallWindowService.findAllMallPageJsonInfo(para);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 新增/修改主题馆页面
     * @param mallPage
     * @return
     */
    @RequestMapping(value = "/saveOrUpdateMallPage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String saveOrUpdateMallPage(MallPageEntity mallPage)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            int result = mallWindowService.saveOrUpdateMallPage(mallPage);
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
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 楼层管理
     * @param id：主题馆页面Id
     * @return
     */
    @RequestMapping("/manageFloor/{id}")
    public ModelAndView manageFloor(@PathVariable("id") int id)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            MallPageEntity mallPage = mallWindowService.findAllMallPageById(id);
            if (mallPage == null)
            {
                mv.setViewName("forward:/error/404");
                return mv;
            }
            mv.addObject("mallPage", mallPage);
            mv.addObject("mallPageId", id + "");
            mv.setViewName("mallWindow/manageFloor");
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            mv.setViewName("error/500");
        }
        return mv;
    }
    
    /**
     * 主题馆楼层列表
     * @param page
     * @param rows
     * @param mallPageId
     * @return
     */
    @RequestMapping(value = "/jsonPageFloorInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonPageFloorInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "mallPageId", required = true) int mallPageId)
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
            para.put("mallPageId", mallPageId);
            resultMap = mallWindowService.findAllMallPageFloorJsonInfo(para);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 修改楼层排序值
     * @param id：楼层Id
     * @param sequence：排序值
     * @param mallPageId：主题馆页面Id
     * @return
     */
    @RequestMapping(value = "/updateFloorSequence", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateFloorSequence(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "sequence", required = true) int sequence,
        @RequestParam(value = "mallPageId", required = true) int mallPageId)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            para.put("id", id);
            para.put("mallPageId", mallPageId);
            para.put("sequence", sequence);
            int status = mallWindowService.updatePageFloorByPara(para);
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
            logger.error(e.getMessage(), e);
        }
        
        return JSON.toJSONString(result);
    }
    
    /**
     * 新增/编辑楼层
     * @param id：楼层Id
     * @param mallPageId：主题馆页面Id
     * @param name：楼层名称
     * @param isDisplay：是否展现：1展现；0不展现
     * @return
     */
    @RequestMapping(value = "/saveOrUpdatePageFloor", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String saveOrUpdatePageFloor(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "mallPageId", required = true) int mallPageId,
        @RequestParam(value = "name", required = true) String name, @RequestParam(value = "isDisplay", required = false, defaultValue = "1") int isDisplay)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            para.put("mallPageId", mallPageId);
            para.put("name", name);
            para.put("isDisplay", isDisplay);
            
            int result = mallWindowService.saveOrUpdatePageFloor(para);
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
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 更改楼层展现状态
     * @param id：楼层Id
     * @param code：是否展现，1展现；0不展现
     * @param mallPageId：主题馆页面Id
     * @return
     */
    @RequestMapping(value = "/updatePageFloorDisplayCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updatePageFloorDisplayCode(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "code", required = true) int code,
        @RequestParam(value = "mallPageId", required = true) int mallPageId)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            para.put("id", id);
            para.put("mallPageId", mallPageId);
            para.put("isDisplay", code);
            int status = mallWindowService.updatePageFloorByPara(para);
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
            logger.error(e.getMessage(), e);
        }
        
        return JSON.toJSONString(result);
    }
    
    /**
     * 商品管理
     * @param id：主题馆页面id
     * @return
     */
    @RequestMapping("/manageProduct/{id}")
    public ModelAndView manageProduct(@PathVariable("id") int id)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            MallPageEntity mallPage = mallWindowService.findAllMallPageById(id);
            if (mallPage == null)
            {
                mv.setViewName("forward:/error/404");
                return mv;
            }
            mv.addObject("mallPageId", id + "");
            mv.setViewName("mallWindow/manageProduct");
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            mv.setViewName("error/500");
        }
        return mv;
    }
    
    /**
     * 页面楼层商品列表
     * @param page
     * @param rows
     * @param mallPageId：页面Id
     * @param pageFloorId：楼层Id
     * @param productId：商品Id
     * @param productId：品牌Id
     * @param productId：商家Id
     * @param productName：商品长名称
     * @param shortName：商品短名称
     * @return
     */
    @RequestMapping(value = "/jsonMallFloorProductInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonMallFloorProductInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "mallPageId", required = true) int mallPageId,
        @RequestParam(value = "pageFloorId", required = false, defaultValue = "0") int pageFloorId,
        @RequestParam(value = "productId", required = false, defaultValue = "0") int productId, @RequestParam(value = "brandId", required = false, defaultValue = "0") int brandId,
        @RequestParam(value = "sellerId", required = false, defaultValue = "0") int sellerId,
        @RequestParam(value = "productName", required = false, defaultValue = "") String productName,
        @RequestParam(value = "productCode", required = false, defaultValue = "") String code,
        @RequestParam(value = "shortName", required = false, defaultValue = "") String shortName)
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
            para.put("mallPageId", mallPageId);
            if (pageFloorId != 0)
            {
                para.put("pageFloorId", pageFloorId);
            }
            if (productId != 0)
            {
                para.put("productId", productId);
            }
            if (brandId != 0)
            {
                para.put("brandId", brandId);
            }
            if (sellerId != 0)
            {
                para.put("sellerId", sellerId);
            }
            if (!"".equals(productName))
            {
                para.put("productName", "%" + productName + "%");
            }
            if (!"".equals(shortName))
            {
                para.put("shortName", "%" + shortName + "%");
            }
            if (!"".equals(code))
            {
                para.put("code", code);
            }
            resultMap = mallWindowService.jsonMallFloorProductInfo(para);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/jsonPageFloorCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonPageFloorCode(@RequestParam(value = "mallPageId", required = false, defaultValue = "0") int mallPageId)
    {
        List<Map<String, String>> codeList = new ArrayList<Map<String, String>>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("mallPageId", mallPageId);
            List<Map<String, Object>> list = mallWindowService.findMallPageFloorCode(para);
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
     * 更改楼层商品排序值
     * @param id
     * @param sequence
     * @return
     */
    @RequestMapping(value = "/updateFloorProductSequence", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateFloorProductSequence(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "sequence", required = true) int sequence)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            para.put("id", id);
            para.put("sequence", sequence);
            int status = mallWindowService.updateFloorProductSequence(para);
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
            logger.error(e.getMessage(), e);
        }
        
        return JSON.toJSONString(result);
    }
    
    /**
     * 删除楼层商品
     * @param id
     * @return
     */
    @RequestMapping(value = "/deletePageFloorProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String deletePageFloorProduct(@RequestParam(value = "id", required = true) int id)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            int status = mallWindowService.deleteRelationMallPageFloorAndProduct(id);
            if (status == 1)
            {
                result.put("status", 1);
            }
            else
            {
                result.put("status", 0);
                result.put("msg", "删除失败");
            }
        }
        catch (Exception e)
        {
            result.put("status", 0);
            result.put("msg", "删除失败");
            logger.error(e.getMessage(), e);
        }
        
        return JSON.toJSONString(result);
    }
    
    /**
     * 批量删除楼层商品
     * @param ids
     * @return
     */
    @RequestMapping(value = "/deletePageFloorProductList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String deletePageFloorProductList(@RequestParam(value = "ids", required = true) String ids)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
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
            
            int resultStatus = mallWindowService.deleteRelationMallPageFloorAndProductList(idList);
            
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
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "删除失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 楼层添加商品
     * @param request
     * @param page
     * @param rows
     * @param code：商品编码
     * @param name：商品名称(长名称)
     * @param status：状态0，不加载；1，加载
     * @param brandId：品牌Id
     * @param sellerId：卖家Id
     * @param mallPageId：主题馆页面Id
     * @param productId：商品Id
     * @param remark：商品备注
     * @param type：商品类型：1：特卖商品；2：商城商品（默认查询商城商品）
     * @param isAvailable：是否可用：1：可用；0：不可用（默认查询可用商品）
     * @return
     */
    @RequestMapping(value = "/jsonProductList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonProductList(HttpServletRequest request, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "code", required = false, defaultValue = "") String code,
        @RequestParam(value = "name", required = false, defaultValue = "") String name, @RequestParam(value = "status", required = false, defaultValue = "0") int status,
        @RequestParam(value = "brandId", required = false, defaultValue = "0") int brandId, @RequestParam(value = "sellerId", required = false, defaultValue = "0") int sellerId,
        @RequestParam(value = "mallPageId", required = true) int mallPageId, @RequestParam(value = "productId", required = false, defaultValue = "0") int productId,
        @RequestParam(value = "remark", required = false, defaultValue = "") String remark, @RequestParam(value = "type", required = false, defaultValue = "2") int type,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            if (status == 0)
            {
                resultMap.put("rows", new ArrayList<Map<String, Object>>());
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
            para.put("mallPageId", mallPageId);
            para.put("type", type);
            para.put("isAvailable", isAvailable);
            
            resultMap = mallWindowService.jsonProductListForAdd(para);
            
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 楼层增加商品
     * @param ids：商品Id拼接的String
     * @param floorId：楼层Id
     * @return
     */
    @RequestMapping(value = "/addProductForPageFloor", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String addProductForPageFloor(@RequestParam(value = "ids", required = true) String ids, @RequestParam(value = "floorId", required = true) int floorId)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
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
                // 逆序
                for (int i = idList.size() - 1; i >= 0; i--)
                {
                    idList_new.add(idList.get(i));
                }
            }
            else
            {
                idList_new.add(Integer.valueOf(ids));
            }
            
            String productStr = "";
            boolean isCanAdd = true;
            for (int productId : idList_new)
            {
                // 检查所有商品的 分销供货价是否满足： salePrice*0.6 < partnerDistributionPrice <= salePrice
                ProductEntity product = productService.findProductById(productId);
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
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("idList_new", idList_new);
                map.put("floorId", floorId);
                
                mallWindowService.addProductForPageFloor(map);
                resultMap.put("status", 1);
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "Id为[" + productStr + "]的商品分销供货价必须大于或等于售价的60%并且小于或等于售价，否则无法保存");
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
    
    /**
     * 今日最热商品View
     * @return
     * @throws Exception
     */
    @RequestMapping("/hotList")
    public ModelAndView hotList()
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("mallWindow/currentHotProductList");
        return mv;
    }
    
    /**
     * 今日最热商品list
     * @param page
     * @param rows
     * @param id
     * @param name
     * @param isDisplay
     * @return
     */
    @RequestMapping(value = "/jsonTodayHotProductInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonTodayHotProductInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "isDisplay", required = false, defaultValue = "-1") int isDisplay,
        @RequestParam(value = "type", required = false, defaultValue = "-1") int type, @RequestParam(value = "isEnd", required = false, defaultValue = "-1") int isEnd,
        @RequestParam(value = "stock", required = false, defaultValue = "-1") int stock, @RequestParam(value = "productId", required = false, defaultValue = "0") int productId,
        @RequestParam(value = "productName", required = false, defaultValue = "") String productName)
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
            para.put("isDisplay", isDisplay);
            
            if (type != -1)
            {
                para.put("type", type);
            }
            
            para.put("isEnd", isEnd);
            para.put("stock", stock);
            if (productId != 0)
            {
                para.put("productId", productId);
            }
            if (!"".equals(productName))
            {
                para.put("productName", "%" + productName + "%");
            }
            resultMap = mallWindowService.findAllCurrentHotProductList(para);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 今日最热排序View
     * @return
     */
    @RequestMapping("/hotProductSequenceList")
    public ModelAndView sequenceList()
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("mallWindow/hotProductSequenceList");
        return mv;
    }
    
    /**
     * 今日最热展现的商品列表
     * @return
     */
    @RequestMapping(value = "/jsonTodayHotDisplayProductInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonTodayHotDisplayProductInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
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
            resultMap = mallWindowService.findAllCurrentHotDisplayProductList(para);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 更新今日最热人工干预值
     * @param id
     * @param sequence
     * @return
     */
    @RequestMapping(value = "/updateHotProductCustomFactor", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateHotProductCustomFactor(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "customFactor", required = true) int customFactor)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            para.put("id", id);
            if (customFactor > 200 || customFactor < -200)
            {
                result.put("status", 0);
                result.put("msg", "人工干预值范围只允许在[-200 ~ 200]");
                return JSON.toJSONString(result);
            }
            para.put("customFactor", customFactor);
            int status = mallWindowService.updateHotProductCustomFactor(para);
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
            logger.error(e.getMessage(), e);
        }
        
        return JSON.toJSONString(result);
    }
    
    /**
     * 删除今日最热商品
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteHotProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String deleteHotProduct(@RequestParam(value = "id", required = true) String id)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            List<Integer> idList = new ArrayList<Integer>();
            if (id.indexOf(",") > 0)
            {
                String[] idStr = id.split(",");
                for (String str : idStr)
                {
                    idList.add(Integer.valueOf(str));
                }
            }
            else
            {
                idList.add(Integer.valueOf(id));
            }
            result.put("idList", idList);
            int status = mallWindowService.deleteHotProduct(result);
            if (status == 1)
            {
                result.put("status", 1);
            }
            else
            {
                result.put("status", 0);
                result.put("msg", "删除失败");
            }
        }
        catch (Exception e)
        {
            result.put("status", 0);
            result.put("msg", "删除失败");
            logger.error(e.getMessage(), e);
        }
        
        return JSON.toJSONString(result);
    }
    
    /**
     * 修改今日最热商品展现状态
     * @param id：product_hot id
     * @param productId：商品id
     * @param isDisplay：展现状态；1展现；0不展现
     * @param type：商品类型；1特卖商品；2商城商品
     * @return
     */
    @RequestMapping(value = "/updateHotProductDisplayStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateHotProductDisplayStatus(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "productId", required = true) int productId,
        @RequestParam(value = "isDisplay", required = true) int isDisplay, @RequestParam(value = "type", required = true) int type)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            // 如果是将商品设为展现
            if (isDisplay == 1)
            {
                // type=1(特卖商品)检查商品的特卖时间是否结束
                ProductEntity product = productService.findProductById(productId);
                if (product == null)
                {
                    result.put("status", 0);
                    result.put("msg", "服务器发生异常");
                    return JSON.toJSONString(result);
                }
                if (type == 1)
                {
                    DateTime startTime = DateTimeUtil.string2DateTime(product.getStartTime(), "yyyy-MM-dd HH:mm:ss.SSS");
                    DateTime endTime = DateTimeUtil.string2DateTime(product.getEndTime(), "yyyy-MM-dd HH:mm:ss.SSS");
                    //检查商品的特卖时间是否结束
                    if (endTime.isBeforeNow() || endTime.isEqualNow())
                    {
                        result.put("status", 0);
                        result.put("msg", "该商品的特卖时间已经结束，不能展现");
                        return JSON.toJSONString(result);
                    }
                    else if (startTime.isAfterNow())
                    {
                        result.put("status", 0);
                        result.put("msg", "该商品的特卖时间还未开始，不能展现");
                        return JSON.toJSONString(result);
                    }
                }
                
                // type=1(特卖商品)或type=2(商城商品)检查商品的可用库存是否大于0
                Map<String, Object> productCountMap = productService.findProductAndCountInfoByProductId(productId);
                if (productCountMap == null)
                {
                    result.put("status", 0);
                    result.put("msg", "服务器发生异常");
                    return JSON.toJSONString(result);
                }
                String stock = productCountMap.get("stock") + "";
                if (StringUtils.isEmpty(stock) || Integer.valueOf(stock).intValue() == 0)
                {
                    result.put("status", 0);
                    result.put("msg", "该商品可用库存为0，不能展现");
                    return JSON.toJSONString(result);
                }
                else if (product.getIsOffShelves() == 1)
                {
                    result.put("status", 0);
                    result.put("msg", "该商品已经下架，不能展现");
                    return JSON.toJSONString(result);
                }
                
            }
            para.put("id", id);
            para.put("isDisplay", isDisplay);
            if (isDisplay == 0)
            {
                para.put("updateByTimer", 0);
            }
            int status = mallWindowService.updateHotProduct(para);
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
            logger.error(e.getMessage(), e);
        }
        
        return JSON.toJSONString(result);
    }
    
    /**
     * 今日最热添加商品List
     * @param request
     * @param page
     * @param rows
     * @param code：商品编码
     * @param name：商品名称(长名称)
     * @param status：状态0，不加载；1，加载
     * @param brandId：品牌Id
     * @param sellerId：卖家Id
     * @param mallPageId：主题馆页面Id
     * @param productId：商品Id
     * @param remark：商品备注
     * @param type：商品类型：1：特卖商品；2：商城商品（默认查询商城商品）
     * @param isAvailable：是否可用：1：可用；0：不可用（默认查询可用商品）
     * @return
     */
    @RequestMapping(value = "/jsonProductForHotProductList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonProductForHotProductList(HttpServletRequest request, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "code", required = false, defaultValue = "") String code,
        @RequestParam(value = "name", required = false, defaultValue = "") String name, @RequestParam(value = "status", required = false, defaultValue = "0") int status,
        @RequestParam(value = "brandId", required = false, defaultValue = "0") int brandId, @RequestParam(value = "sellerId", required = false, defaultValue = "0") int sellerId,
        @RequestParam(value = "productId", required = false, defaultValue = "0") int productId, @RequestParam(value = "remark", required = false, defaultValue = "") String remark,
        @RequestParam(value = "type", required = false, defaultValue = "-1") int type, @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            if (status == 0)
            {
                resultMap.put("rows", new ArrayList<Map<String, Object>>());
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
            if (type != -1)
            {
                para.put("type", type);
            }
            para.put("isAvailable", isAvailable);
            
            resultMap = mallWindowService.jsonHotProductListForAdd(para);
            
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 添加商品到今日最热
     * @param ids：productId:type拼接的字符串，如：1001:1,1004:2
     * @return
     */
    @RequestMapping(value = "/addProductForHotProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String addProductForHotProduct(@RequestParam(value = "ids", required = true) String ids)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            List<String> idList = new ArrayList<String>();
            List<String> idList_new = new ArrayList<String>();
            if (ids.indexOf(",") > 0)
            {
                String[] arr = ids.split(",");
                for (String cur : arr)
                {
                    idList.add(cur);
                }
                // 逆序
                for (int i = idList.size() - 1; i >= 0; i--)
                {
                    idList_new.add(idList.get(i));
                }
            }
            else
            {
                idList_new.add(ids);
            }
            
            String productStr = "";
            boolean isCanAdd = true;
            for (String IdAndType : idList_new)
            {
                String[] arr = IdAndType.split(":");
                int productId = Integer.valueOf(arr[0]).intValue();
                // 检查所有商品的 分销供货价是否满足： salePrice*0.6 < partnerDistributionPrice <= salePrice
                ProductEntity product = productService.findProductById(Integer.valueOf(productId).intValue());
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
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("idList_new", idList_new);
                
                mallWindowService.addProductForHotProduct(map);
                resultMap.put("status", 1);
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "Id为[" + productStr + "]的商品分销供货价必须大于或等于售价的60%并且小于或等于售价，否则无法保存");
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
    
    @RequestMapping(value = "/jsonSaleTimeDiscount", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonSaleTimeDiscount()
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            resultMap = mallWindowService.jsonSaleTimeDiscount();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/updateSaleTimeFactor", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateSaleTimeFactor(@RequestParam(value = "index", required = true) int index, @RequestParam(value = "factor", required = true) int factor)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            if (factor > 100 || factor < 0)
            {
                result.put("status", 0);
                result.put("msg", "折扣值只能在[0 ~ 100]之间");
                return JSON.toJSONString(result);
            }
            para.put("index", index);
            para.put("factor", factor);
            
            int status = mallWindowService.updateSaleTimeFactor(para);
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
            logger.error(e.getMessage(), e);
        }
        
        return JSON.toJSONString(result);
    }
    
    @RequestMapping(value = "/batchUpdateHotProductDisplayStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String batchUpdateHotProductDisplayStatus(@RequestParam(value = "ids", required = true) String ids, @RequestParam(value = "code", required = true) int code)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            String[] paraStr = ids.split(",");
            List<Integer> idList = new ArrayList<Integer>();
            StringBuffer sb = new StringBuffer();
            for (String str : paraStr)
            {
                String[] arr = str.split(":");
                int id = Integer.valueOf(arr[0]).intValue();
                int productId = Integer.valueOf(arr[1]).intValue();
                int type = Integer.valueOf(arr[2]).intValue();
                // 如果是将商品设为展现,则需要判断特卖时间是否结束、商品库存是否为0、商品是否下架
                if (code == 1)
                {
                    
                    // type=1(特卖商品)检查商品的特卖时间是否结束
                    ProductEntity product = productService.findProductById(productId);
                    if (product == null)
                    {
                        sb.append("Id=").append(productId).append("的商品不存在，不能展现<br/>");
                    }
                    else
                    {
                        boolean isDisplay = true;
                        if (type == 1)
                        {
                            DateTime startTime = DateTimeUtil.string2DateTime(product.getStartTime(), "yyyy-MM-dd HH:mm:ss.SSS");
                            DateTime endTime = DateTimeUtil.string2DateTime(product.getEndTime(), "yyyy-MM-dd HH:mm:ss.SSS");
                            //检查商品的特卖时间是否结束
                            if (endTime.isBeforeNow() || endTime.isEqualNow())
                            {
                                sb.append("Id=").append(productId).append("的商品特卖时间已经结束，不能展现<br/>");
                                isDisplay = false;
                            }
                            else if (startTime.isAfterNow())
                            {
                                sb.append("Id=").append(productId).append("的商品特卖时间还未开始，不能展现<br/>");
                                isDisplay = false;
                            }
                        }
                        // 检查商品的库存是否为0
                        Map<String, Object> productCountMap = productService.findProductAndCountInfoByProductId(productId);
                        if (productCountMap.size() == 0 || Integer.valueOf(productCountMap.get("stock") + "").intValue() <= 0)
                        {
                            sb.append("Id=").append(productId).append("的商品库存为0，不能展现<br/>");
                            isDisplay = false;
                        }
                        //检查是否下架
                        else if (product.getIsOffShelves() == 1)
                        {
                            sb.append("Id=").append(productId).append("的商品已经下架，不能展现<br/>");
                            isDisplay = false;
                        }
                        if (isDisplay)
                        {
                            idList.add(id);
                        }
                    }
                }
                else
                {
                    idList.add(id);
                }
            }
            if (idList.size() > 0)
            {
                para.put("idList", idList);
                para.put("code", code);
                if (code == 0)
                {
                    para.put("updateByTimer", 0);
                }
                int status = mallWindowService.updateHotProductDisplayStatus(para);
                if (status == 1)
                {
                    result.put("status", 1);
                    if (sb.length() > 0)
                    {
                        result.put("msg", sb.toString());
                    }
                    else
                    {
                        result.put("msg", "操作成功");
                    }
                }
                else
                {
                    result.put("status", 0);
                    result.put("msg", "操作失败");
                }
            }
            // 有可能全部商品都不符合展现条件
            else
            {
                if (sb.length() > 0)
                {
                    if (code == 1)
                    {
                        result.put("status", 1);
                        result.put("msg", sb.toString());
                    }
                }
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
    
    /**
     * 快速添加商品到今日最热
     * @param ids
     * @return
     */
    @RequestMapping(value = "/quickAddProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String quickAddProduct(@RequestParam(value = "ids", required = true) String ids)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
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
            
            String productStr = "";
            boolean isCanAdd = true;
            for (int productId : idList)
            {
                // 检查所有商品的 分销供货价是否满足： salePrice*0.6 <= partnerDistributionPrice <= salePrice
                ProductEntity product = productService.findProductById(productId);
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
                resultMap = mallWindowService.quickAddProduct(idList);
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "Id为[" + productStr + "]的商品分销供货价必须大于或等于售价的60%并且小于或等于售价，否则无法保存");
            }
            
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "添加失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 添加明日特卖商品到最热尚品池中
     * @return
     */
    @RequestMapping(value = "/addSaleWindowToHotProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String addSaleWindowToHotProduct(@RequestParam(value = "selectedDate", required = true) String selectedDate)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            int result = mallWindowService.addSaleWindowToHotProduct(selectedDate);
            if (result >= 0)
            {
                resultMap.put("status", 1);
                resultMap.put("msg", "成功添加了" + result + "个商品到今日最热商品池中");
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "添加失败");
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
}
