package com.ygg.admin.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.code.FirstCategoryColorTypeEnum;
import com.ygg.admin.code.FirstCategoryWindowRelationTypeEnum;
import com.ygg.admin.code.ImageTypeEnum;
import com.ygg.admin.entity.ActivitiesCommonEntity;
import com.ygg.admin.entity.CategoryActivityEntity;
import com.ygg.admin.entity.CategoryFirstEntity;
import com.ygg.admin.entity.CategoryFirstWindowEntity;
import com.ygg.admin.entity.CategorySecondEntity;
import com.ygg.admin.entity.CategoryThirdEntity;
import com.ygg.admin.entity.ProductEntity;
import com.ygg.admin.service.ActivitiesCommonService;
import com.ygg.admin.service.CategoryService;
import com.ygg.admin.service.CustomActivitiesService;
import com.ygg.admin.service.ProductService;
import com.ygg.admin.util.ImageUtil;

@Controller
@RequestMapping("/category")
public class CategoryController
{
    
    private static Logger logger = Logger.getLogger(CategoryController.class);
    
    @Resource
    private CategoryService categoryService;
    
    @Resource
    private ProductService productService;
    
    @Resource(name = "activitiesCommonService")
    private ActivitiesCommonService activitiesCommonService;
    
    @Resource
    private CustomActivitiesService customActivitiesService;
    
    @RequestMapping("/first")
    public ModelAndView firstList()
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("category/list1");
        return mv;
    }
    
    @RequestMapping("/second")
    public ModelAndView secondList()
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("category/list2");
        return mv;
    }
    
    @RequestMapping("/third")
    public ModelAndView thirdList()
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("category/list3");
        return mv;
    }
    
    /**
     * 一级分类列表
     * 
     * @param page
     * @param rows
     * @param name
     * @param isAvailable
     * @return
     */
    @RequestMapping(value = "/jsonCategoryFirstInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonCategoryFirstInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "name", required = false, defaultValue = "") String name, //
        @RequestParam(value = "isAvailable", required = false, defaultValue = "-1") int isAvailable)
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
            if (!"".equals(name))
            {
                para.put("name", "%" + name + "%");
            }
            if (isAvailable != -1)
            {
                para.put("isAvailable", isAvailable);
            }
            resultMap = categoryService.jsonCategoryFirstInfo(para);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("total", 0);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 新增/修改一级分类
     * 
     * @param id：id=0表示新增
     * @param name：分类名称
     * @param image：分类图标
     * @param tag：分类标签
     * @param isAvailable：是否可用，1可用，0不可用
     * @param isShowInApp：是否在App中展现，1展现，0不展现
     * @return
     */
    @RequestMapping(value = "/saveOrUpdateCategoryFirst", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "分类管理-新增/编辑一级分类")
    public String saveOrUpdateCategoryFirst(//
        @RequestParam(value = "id", required = false, defaultValue = "0") int id, //
        @RequestParam(value = "name", required = false, defaultValue = "") String name, //
        @RequestParam(value = "image1", required = false, defaultValue = "") String image1, //
        @RequestParam(value = "image2", required = false, defaultValue = "") String image2, //
        @RequestParam(value = "tag", required = false, defaultValue = "") String tag, //
        @RequestParam(value = "color", required = false, defaultValue = "") String color, //
        @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable, //
        @RequestParam(value = "isShowInApp", required = false, defaultValue = "0") int isShowInApp)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            if (tag.split("#").length > 2)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "提示性子分类文案最多允许两个");
                return JSON.toJSONString(resultMap);
            }
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            para.put("name", name);
            para.put("image1", image1);
            para.put("image2", image2);
            para.put("tag", tag);
            para.put("color", color);
            para.put("isAvailable", isAvailable);
            para.put("isShowInApp", isShowInApp);
            
            int result = categoryService.saveOrUpdateCategoryFirst(para);
            if (result == 1)
            {
                resultMap.put("status", 1);
            }
            else if (result == 2)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "图片尺寸只能为333x333");
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "保存失败");
            }
            
        }
        catch (Exception e)
        {
            logger.error(e.getCause(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "服务器发生异常，请刷新后重试。如果问题一直存在，请联系管理员");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 更新一级类目状态
     * 
     * @param request
     * @param id：一级类目Id拼接的字符串："1,2,3"
     * @param isAvailable：是否可用，1可用，0停用
     * @param isShowInApp：是否在app中展示，1展示，0不展示
     * @return
     */
    @RequestMapping(value = "/updateCategoryFirstStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "分类管理-修改一级分类状态")
    public String updateCategoryFirstStatus(HttpServletRequest request, @RequestParam(value = "id", required = true) String id,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "-1") int isAvailable,
        @RequestParam(value = "isShowInApp", required = false, defaultValue = "-1") int isShowInApp)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("idList", Arrays.asList(id.split(",")));
            if (isAvailable != -1)
            {
                para.put("isAvailable", isAvailable);
            }
            if (isShowInApp != -1)
            {
                para.put("isShowInApp", isShowInApp);
            }
            int resultStatus = categoryService.updateCategoryFirstStatus(para);
            if (resultStatus == 1)
            {
                resultMap.put("status", 1);
            }
            else
            {
                resultMap.put("status", 0);
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
     * 更新一级分类排序
     * 
     * @param id
     * @param sequence
     * @return
     */
    @RequestMapping(value = "/updateCategoryFirstSequence", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "分类管理-修改一级分类排序值")
    public String updateCategoryFirstSequence(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "sequence", required = true) int sequence)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            para.put("sequence", sequence);
            int resultStatus = categoryService.updateCategoryFirstSequence(para);
            if (resultStatus == 1)
            {
                resultMap.put("status", 1);
            }
            else
            {
                resultMap.put("status", 0);
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
     * 二级分类列表
     * 
     * @param page
     * @param rows
     * @param name
     * @param categoryFirstId：一级分类ID
     * @param isAvailable
     * @return
     */
    @RequestMapping(value = "/jsonCategorySecondInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonCategorySecondInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "name", required = false, defaultValue = "") String name, //
        @RequestParam(value = "fname", required = false, defaultValue = "") String fname, //
        @RequestParam(value = "categoryFirstId", required = false, defaultValue = "0") int categoryFirstId, //
        @RequestParam(value = "isAvailable", required = false, defaultValue = "-1") int isAvailable)
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
            if (!"".equals(name))
            {
                para.put("name", "%" + name + "%");
            }
            if (!"".equals(fname))
            {
                para.put("fname", "%" + fname + "%");
            }
            if (categoryFirstId != 0)
            {
                para.put("categoryFirstId", categoryFirstId);
            }
            if (isAvailable != -1)
            {
                para.put("isAvailable", isAvailable);
            }
            resultMap = categoryService.jsonCategorySecondInfo(para);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("total", 0);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 新增/修改二级类目
     * 
     * @param id
     * @param categoryFirstId：一级类目ID
     * @param name：类目名称
     * @param orderType：子类目排序方式，1按子类商品销量排序，2按子类sequence排序
     * @param isAvailable：1可用，0停用
     * @return
     */
    @RequestMapping(value = "/saveOrUpdateCategorySecond", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "分类管理-新增/编辑二级分类")
    public String saveOrUpdateCategorySecond(
        @RequestParam(value = "id", required = false, defaultValue = "0") int id, //
        @RequestParam(value = "categoryFirstId", required = false, defaultValue = "0") int categoryFirstId, //
        @RequestParam(value = "name", required = false, defaultValue = "") String name, //
        @RequestParam(value = "categoryImage", required = false, defaultValue = "") String image, //分类图
        @RequestParam(value = "orderType", required = false, defaultValue = "1") int orderType, //
        @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable,
        @RequestParam(value = "isDisplay", required = false, defaultValue = "0") int isDisplay)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("categoryFirstId", categoryFirstId);
            para.put("name", name);
            
            CategorySecondEntity cse = categoryService.findCategorySecondByPara(para);
            if (cse != null)
            {
                // 同一个一级分类下，二级分类不允许重名
                if (cse.getId() != id)
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "二级分类【" + name + "】已经存在");
                    return JSON.toJSONString(resultMap);
                }
            }
            
            para.put("id", id);
            para.put("orderType", orderType);
            para.put("isAvailable", isAvailable);
            para.put("isDisplay", isDisplay);
            para.put("image", image);
            
            int result = categoryService.saveOrUpdateCategorySecond(para);
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
            logger.error(e.getCause(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "服务器发生异常，请刷新后重试。如果问题一直存在，请联系管理员");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 修改二级分类可用状态
     * 
     * @param id
     * @param isAvailable：1可用，0不可用
     * @param isDisplay：1展现，0不展现
     * @return
     */
    @RequestMapping(value = "/updateCategorySecondStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "分类管理-修改二级分类状态")
    public String updateCategorySecondStatus(@RequestParam(value = "id", required = true) String id,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "-1") int isAvailable,
        @RequestParam(value = "isDisplay", required = false, defaultValue = "-1") int isDisplay)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("idList", Arrays.asList(id.split(",")));
            if (isAvailable != -1)
            {
                para.put("isAvailable", isAvailable);
            }
            if (isDisplay != -1)
            {
                para.put("isDisplay", isDisplay);
            }
            
            int resultStatus = categoryService.updateCategorySecondStatus(para);
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
     * 修改二级分类排序
     * 
     * @param id
     * @param sequence
     * @return
     */
    @RequestMapping(value = "/updateCategorySecondSequence", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "分类管理-修改二级分类排序值")
    public String updateCategorySecondSequence(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "sequence", required = true) int sequence)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            para.put("sequence", sequence);
            int resultStatus = categoryService.updateCategorySecondSequence(para);
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
     * 三级分了列表
     * 
     * @param page
     * @param rows
     * @param name：三级分类名称
     * @param fname：一级分类名
     * @param sname：二级分类名
     * @param categoryFirstId：一级分类ID
     * @param categorySecondId：二级分类ID
     * @param isAvailable
     * @return
     */
    @RequestMapping(value = "/jsonCategoryThirdInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonCategoryThirdInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "name", required = false, defaultValue = "") String name, //
        @RequestParam(value = "fname", required = false, defaultValue = "") String fname, //
        @RequestParam(value = "categoryFirstId", required = false, defaultValue = "0") int categoryFirstId, //
        @RequestParam(value = "sname", required = false, defaultValue = "") String sname, //
        @RequestParam(value = "categorySecondId", required = false, defaultValue = "0") int categorySecondId, //
        @RequestParam(value = "isAvailable", required = false, defaultValue = "-1") int isAvailable)
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
            if (!"".equals(name))
            {
                para.put("name", "%" + name + "%");
            }
            if (!"".equals(fname))
            {
                para.put("fname", "%" + fname + "%");
            }
            if (categoryFirstId != 0)
            {
                para.put("categoryFirstId", categoryFirstId);
            }
            if (categorySecondId != 0)
            {
                para.put("categorySecondId", categorySecondId);
            }
            if (!"".equals(sname))
            {
                para.put("sname", "%" + sname + "%");
            }
            
            if (isAvailable != -1)
            {
                para.put("isAvailable", isAvailable);
            }
            resultMap = categoryService.jsonCategoryThirdInfo(para);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("total", 0);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 新增/修改三积分类
     * 
     * @param id
     * @param categoryFirstId
     * @param categorySecondId
     * @param name
     * @param isAvailable
     * @param isHot
     * @param isHighlight
     * @return
     */
    @RequestMapping(value = "/saveOrUpdateCategoryThird", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "分类管理-新增/编辑三级分类")
    public String saveOrUpdateCategoryThird(
        @RequestParam(value = "id", required = false, defaultValue = "0") int id, //
        @RequestParam(value = "categoryFirstId", required = false, defaultValue = "0") int categoryFirstId, //
        @RequestParam(value = "categorySecondId", required = false, defaultValue = "0") int categorySecondId, //
        @RequestParam(value = "name", required = false, defaultValue = "") String name, //
        @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable, //
        @RequestParam(value = "isDisplay", required = false, defaultValue = "1") int isDisplay, //
        @RequestParam(value = "isHot", required = false, defaultValue = "0") int isHot, //
        @RequestParam(value = "isHighlight", required = false, defaultValue = "0") int isHighlight,
        @RequestParam(value = "orderType", required = false, defaultValue = "1") int orderType)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("categoryFirstId", categoryFirstId);
            para.put("categorySecondId", categorySecondId);
            para.put("name", name);
            
            CategoryThirdEntity cte = categoryService.findCategoryThirdByPara(para);
            if (cte != null)
            {
                // 同一个二级分类下，三级分类不允许重名
                if (cte.getId() != id)
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "三级分类【" + name + "】已经存在");
                    return JSON.toJSONString(resultMap);
                }
            }
            
            para.put("id", id);
            para.put("isHot", isHot);
            para.put("isHighlight", isHighlight);
            para.put("isAvailable", isAvailable);
            para.put("isDisplay", isDisplay);
            para.put("orderType", orderType);
            int result = categoryService.saveOrUpdateCategoryThird(para);
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
            logger.error(e.getCause(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "服务器发生异常，请刷新后重试。如果问题一直存在，请联系管理员");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 修改三级分类可用状态
     * 
     * @param id
     * @param isAvailable
     * @return
     */
    @RequestMapping(value = "/updateCategoryThirdStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "分类管理-修改三级分类可用状态")
    public String updateCategoryThirdStatus(@RequestParam(value = "id", required = true) String id, @RequestParam(value = "isAvailable", required = true) int isAvailable)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("idList", Arrays.asList(id.split(",")));
            para.put("isAvailable", isAvailable);
            
            int resultStatus = categoryService.updateCategoryThirdStatus(para);
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
     * 修改三级分类展现状态
     * 
     * @param id
     * @param isAvailable
     * @return
     */
    @RequestMapping(value = "/updateCategoryThirdDisplay", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "分类管理-修改三级分类展现状态")
    public String updateCategoryThirdDisplay(@RequestParam(value = "id", required = true) String id, @RequestParam(value = "isDisplay", required = true) int isDisplay)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("idList", Arrays.asList(id.split(",")));
            para.put("isDisplay", isDisplay);
            
            int resultStatus = categoryService.updateCategoryThirdDisplay(para);
            if (resultStatus > 0)
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
     * 修改三级分类排序值
     * 
     * @param id
     * @param sequence
     * @return
     */
    @RequestMapping(value = "/updateCategoryThirdSequence", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "分类管理-修改三级分类排序值")
    public String updateCategoryThirdSequence(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "sequence", required = true) int sequence)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            para.put("sequence", sequence);
            int resultStatus = categoryService.updateCategoryThirdSequence(para);
            if (resultStatus == 1)
            {
                resultMap.put("status", 1);
            }
            else
            {
                resultMap.put("status", 0);
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
     * 一级分类code
     * 
     * @param id
     * @param isAvailable
     * @return
     */
    @RequestMapping(value = "/jsonCategoryFirstCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonCategoryFirstCode(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable,
        @RequestParam(value = "zeroNeed", required = false, defaultValue = "0") int zeroNeed
    )
    {
        List<Map<String, String>> codeList = new ArrayList<Map<String, String>>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("isAvailable", isAvailable);
            List<CategoryFirstEntity> firstCategoryList = categoryService.findAllCategoryFirst(para);
            for (CategoryFirstEntity cfe : firstCategoryList)
            {
                Map<String, String> map = new HashMap<String, String>();
                map.put("code", cfe.getId() + "");
                map.put("text", cfe.getName() + "");
                if (id == cfe.getId())
                {
                    map.put("selected", "true");
                }
                codeList.add(map);
            }
            if(zeroNeed != 0){
                Map<String, String> it = new HashMap<String, String>();
                it.put("code", "-1");
                it.put("text", "-不限-");
                codeList.add(0, it);
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        return JSON.toJSONString(codeList);
    }
    
    /**
     * 二级分类code
     * 
     * @param id：二级分类Id
     * @param categoryFirstId：一级分类Id
     * @param isAvailable：是否可用
     * @return
     */
    @RequestMapping(value = "/jsonCategorySecondCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonCategorySecondCode(//
        @RequestParam(value = "id", required = false, defaultValue = "0") int id, //
        @RequestParam(value = "firstId", required = false, defaultValue = "0") int categoryFirstId, //
        @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable)
    {
        List<Map<String, Object>> codeList = new ArrayList<Map<String, Object>>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("isAvailable", isAvailable);
            if (categoryFirstId != 0)
            {
                para.put("categoryFirstId", categoryFirstId);
            }
            List<CategorySecondEntity> cseList = categoryService.findAllCategorySecond(para);
            for (CategorySecondEntity cse : cseList)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("code", cse.getId());
                map.put("text", cse.getName());
                if (cse.getId() == id)
                {
                    map.put("selected", true);
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
     * 三级分类code
     * 
     * @param id：三级分类Id
     * @param categoryFirstId：一级分类Id
     * @param categorySecondId：二级分类Id
     * @param isAvailable：是否可用
     * @return
     */
    @RequestMapping(value = "/jsonCategoryThirdCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonCategoryThirdCode(//
        @RequestParam(value = "id", required = false, defaultValue = "0") int id, //
        @RequestParam(value = "firstId", required = false, defaultValue = "0") int categoryFirstId, //
        @RequestParam(value = "secondId", required = false, defaultValue = "0") int categorySecondId, //
        @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable)
    {
        List<Map<String, Object>> codeList = new ArrayList<Map<String, Object>>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("isAvailable", isAvailable);
            if (categoryFirstId != 0)
            {
                para.put("categoryFirstId", categoryFirstId);
            }
            if (categorySecondId != 0)
            {
                para.put("categorySecondId", categorySecondId);
            }
            List<CategoryThirdEntity> cteList = categoryService.findAllCategoryThird(para);
            for (CategoryThirdEntity cte : cteList)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("code", cte.getId());
                map.put("text", cte.getName());
                if (cte.getId() == id)
                {
                    map.put("selected", true);
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
    
    @RequestMapping("/fwindow")
    public ModelAndView firstWindow()
    {
        ModelAndView mv = new ModelAndView("category/firstWindowList");
        return mv;
    }
    
    /**
     * 一级分类资源位列表
     * 
     * @param page
     * @param rows
     * @param isAvailable
     * @return
     */
    @RequestMapping(value = "/jsonFirstWindowInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonFirstWindowInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "isAvailable", required = false, defaultValue = "-1") int isAvailable)
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
            if (isAvailable != -1)
            {
                para.put("isAvailable", isAvailable);
            }
            resultMap = categoryService.jsonFirstWindowInfo(para);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("total", 0);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 转发到新增一级资源位页面
     * 
     * @return
     */
    @RequestMapping("/toAddFwindow")
    public ModelAndView toAddFwindow()
    {
        ModelAndView mv = new ModelAndView("category/firstWindowUpdate");
        Map<String, Object> fwindow = new HashMap<String, Object>();
        fwindow.put("id", 0);
        fwindow.put("isAvailable", 0);
        mv.addObject("fwindow", fwindow);
        return mv;
    }
    
    /**
     * 转发到修改一级资源位页面
     * 
     * @return
     */
    @RequestMapping("/toEditFwindow/{id}")
    public ModelAndView toEditFwindow(@PathVariable(value = "id") int id)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            CategoryFirstWindowEntity fwindow = categoryService.findCategoryFirstWindowById(id);
            if (fwindow == null)
            {
                mv.setViewName("error/404");
            }
            else
            {
                mv.setViewName("category/firstWindowUpdate");
                mv.addObject("fwindow", fwindow);
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    /**
     * 新增/修改一级分类资源位
     * 
     * @param id
     * @param categoryFirstId
     * @param leftRelationType
     * @param leftRelationObjectId
     * @param leftImage
     * @param rightRelationType
     * @param rightRelationObjectId
     * @param rightImage
     * @param remark
     * @param isAvailable
     * @return
     */
    @RequestMapping(value = "/saveOrUpdateCategoryFirstWindow", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "分类管理-新增/编辑一级分类资源位")
    public String saveOrUpdateCategoryFirstWindow(@RequestParam(value = "id", required = false, defaultValue = "0") int id, //
        @RequestParam(value = "categoryFirstId", required = false, defaultValue = "0") int categoryFirstId, //
        @RequestParam(value = "leftRelationType", required = false, defaultValue = "1") int leftRelationType, //
        @RequestParam(value = "leftSaleProductId", required = false, defaultValue = "0") int leftSaleProductId, //
        @RequestParam(value = "leftGroupSaleId", required = false, defaultValue = "0") int leftGroupSaleId, //
        @RequestParam(value = "leftMallProductId", required = false, defaultValue = "0") int leftMallProductId, //
        @RequestParam(value = "leftCustomActivityId", required = false, defaultValue = "0") int leftCustomActivityId, //
        @RequestParam(value = "leftImage", required = false, defaultValue = "") String leftImage, //
        @RequestParam(value = "rightRelationType", required = false, defaultValue = "1") int rightRelationType, //
        @RequestParam(value = "rightSaleProductId", required = false, defaultValue = "0") int rightSaleProductId, //
        @RequestParam(value = "rightGroupSaleId", required = false, defaultValue = "0") int rightGroupSaleId, //
        @RequestParam(value = "rightMallProductId", required = false, defaultValue = "0") int rightMallProductId, //
        @RequestParam(value = "rightCustomActivityId", required = false, defaultValue = "0") int rightCustomActivityId, //
        @RequestParam(value = "rightImage", required = false, defaultValue = "") String rightImage, //
        @RequestParam(value = "remark", required = false, defaultValue = "") String remark, //
        @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("categoryFirstId", categoryFirstId);
            CategoryFirstWindowEntity cfwe = categoryService.findCategoryFirstWindowByPara(para);
            if (cfwe != null)
            {
                // 同一个二级分类下，三级分类不允许重名
                if (cfwe.getId() != id)
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "一级类目【" + cfwe.getFirstCategoryName() + "】中已经存在一个banner");
                    return JSON.toJSONString(resultMap);
                }
            }
            
            para.put("id", id);
            para.put("leftRelationType", leftRelationType);
            int leftRelationObjectId = 0;
            if (FirstCategoryWindowRelationTypeEnum.TYPE_OF_SALE_PRODUCT.getCode() == leftRelationType)
            {
                ProductEntity pe = productService.findProductById(leftSaleProductId, 1);
                if (pe == null)
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "Id=" + leftSaleProductId + "的特卖单品不存在");
                    return JSON.toJSONString(resultMap);
                }
                leftRelationObjectId = leftSaleProductId;
            }
            else if (FirstCategoryWindowRelationTypeEnum.TYPE_OF_GROUP_SALE.getCode() == leftRelationType)
            {
                ActivitiesCommonEntity ace = activitiesCommonService.findAcCommonById(leftGroupSaleId);
                if (ace == null)
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "Id=" + leftGroupSaleId + "的特卖组合不存在");
                    return JSON.toJSONString(resultMap);
                }
                leftRelationObjectId = leftGroupSaleId;
            }
            else if (FirstCategoryWindowRelationTypeEnum.TYPE_OF_MALL_PRODUCT.getCode() == leftRelationType)
            {
                ProductEntity pe = productService.findProductById(leftMallProductId, 2);
                if (pe == null)
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "Id=" + leftMallProductId + "的商城单品不存在");
                    return JSON.toJSONString(resultMap);
                }
                leftRelationObjectId = leftMallProductId;
            }
            else if (FirstCategoryWindowRelationTypeEnum.TYPE_OF_CUSTOM_ACTIVITY.getCode() == leftRelationType)
            {
                Map<String, Object> map = customActivitiesService.findCustomActivitiesById(leftCustomActivityId);
                if (map == null || map.size() == 0)
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "Id=" + leftCustomActivityId + "的自定义活动不存在");
                    return JSON.toJSONString(resultMap);
                }
                leftRelationObjectId = leftCustomActivityId;
            }
            para.put("leftRelationObjectId", leftRelationObjectId);
            para.put("leftImage", leftImage);
            para.put("rightRelationType", rightRelationType);
            
            int rightRelationObjectId = 0;
            if (FirstCategoryWindowRelationTypeEnum.TYPE_OF_SALE_PRODUCT.getCode() == rightRelationType)
            {
                ProductEntity pe = productService.findProductById(rightSaleProductId, 1);
                if (pe == null)
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "Id=" + rightSaleProductId + "的特卖单品不存在");
                    return JSON.toJSONString(resultMap);
                }
                rightRelationObjectId = rightSaleProductId;
            }
            else if (FirstCategoryWindowRelationTypeEnum.TYPE_OF_GROUP_SALE.getCode() == rightRelationType)
            {
                ActivitiesCommonEntity ace = activitiesCommonService.findAcCommonById(rightGroupSaleId);
                if (ace == null)
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "Id=" + rightGroupSaleId + "的特卖组合不存在");
                    return JSON.toJSONString(resultMap);
                }
                rightRelationObjectId = rightGroupSaleId;
            }
            else if (FirstCategoryWindowRelationTypeEnum.TYPE_OF_MALL_PRODUCT.getCode() == rightRelationType)
            {
                ProductEntity pe = productService.findProductById(rightMallProductId, 2);
                if (pe == null)
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "Id=" + rightMallProductId + "的商城单品不存在");
                    return JSON.toJSONString(resultMap);
                }
                rightRelationObjectId = rightMallProductId;
            }
            else if (FirstCategoryWindowRelationTypeEnum.TYPE_OF_CUSTOM_ACTIVITY.getCode() == rightRelationType)
            {
                Map<String, Object> map = customActivitiesService.findCustomActivitiesById(rightCustomActivityId);
                if (map == null || map.size() == 0)
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "Id=" + rightCustomActivityId + "的自定义活动不存在");
                    return JSON.toJSONString(resultMap);
                }
                rightRelationObjectId = rightCustomActivityId;
            }
            para.put("rightRelationObjectId", rightRelationObjectId);
            para.put("rightImage", rightImage);
            para.put("remark", remark);
            para.put("isAvailable", isAvailable);
            
            int result = categoryService.saveOrUpdateCategoryFirstWindow(para);
            if (result == 1)
            {
                resultMap.put("status", 1);
            }
            else if (result == 2)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "图片尺寸不符合要求");
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "保存失败");
            }
            
        }
        catch (Exception e)
        {
            logger.error(e.getCause(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "服务器发生异常，请刷新后重试。如果问题一直存在，请联系管理员");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 修改一级分类资源位展现可用状态
     */
    @RequestMapping(value = "/updateCategoryFirstWindowStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "分类管理-修改一级分类资源位展现可用状态")
    public String updateCategoryFirstWindowStatus(@RequestParam(value = "id", required = true) String id, @RequestParam(value = "isAvailable", required = true) int isAvailable)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("idList", Arrays.asList(id.split(",")));
            para.put("isAvailable", isAvailable);
            
            int resultStatus = categoryService.updateCategoryFirstWindowStatus(para);
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
     * 修改一级分类资源位排序
     */
    @RequestMapping(value = "/updateCategoryFirstWindowSequence", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "分类管理-修改一级分类资源位排序")
    public String updateCategoryFirstWindowSequence(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "sequence", required = true) int sequence)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            para.put("sequence", sequence);
            int resultStatus = categoryService.updateCategoryFirstWindowSequence(para);
            if (resultStatus == 1)
            {
                resultMap.put("status", 1);
            }
            else
            {
                resultMap.put("status", 0);
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
     * 分类活动列表
     */
    @RequestMapping("/activity")
    public ModelAndView activityList()
    {
        ModelAndView mv = new ModelAndView("category/categoryActivityList");
        return mv;
    }
    
    /**
     * 分类活动
     * 
     * @param page
     * @param rows
     * @param isAvailable
     * @return
     */
    @RequestMapping(value = "/jsonCategoryActivityInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonCategoryActivityInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "remark", required = false, defaultValue = "") String remark, //
        @RequestParam(value = "isAvailable", required = false, defaultValue = "-1") int isAvailable)
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
            if (isAvailable != -1)
            {
                para.put("isAvailable", isAvailable);
            }
            if (!"".equals(remark))
            {
                para.put("remark", "%" + remark + "%");
            }
            resultMap = categoryService.jsonCategoryActivityInfo(para);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("total", 0);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 转发到新增一级资源位页面
     * 
     * @return
     */
    @RequestMapping("/toAddActivity")
    public ModelAndView toAddActivity()
    {
        ModelAndView mv = new ModelAndView("category/categoryActivityUpdate");
        Map<String, Object> activity = new HashMap<String, Object>();
        activity.put("id", 0);
        activity.put("isAvailable", 0);
        mv.addObject("activity", activity);
        return mv;
    }
    
    /**
     * 转发到修改一级资源位页面
     * 
     * @return
     */
    @RequestMapping("/toEditActivity/{id}")
    public ModelAndView toEditActivity(@PathVariable(value = "id") int id)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            CategoryActivityEntity activity = categoryService.findCategoryActivityById(id);
            if (activity == null)
            {
                mv.setViewName("error/404");
            }
            else
            {
                mv.setViewName("category/categoryActivityUpdate");
                mv.addObject("activity", activity);
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    /**
     * 新增/修改分类活动
     */
    @RequestMapping(value = "/saveOrUpdateCategoryActivity", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "分类管理-新增/编辑分类活动")
    public String saveOrUpdateCategoryActivity(@RequestParam(value = "id", required = false, defaultValue = "0") int id, //
        @RequestParam(value = "relationType", required = false, defaultValue = "1") int relationType, //
        @RequestParam(value = "saleProductId", required = false, defaultValue = "0") int saleProductId, //
        @RequestParam(value = "groupSaleId", required = false, defaultValue = "0") int groupSaleId, //
        @RequestParam(value = "mallProductId", required = false, defaultValue = "0") int mallProductId, //
        @RequestParam(value = "customActivityId", required = false, defaultValue = "0") int customActivityId, //
        @RequestParam(value = "image", required = false, defaultValue = "") String image, //
        @RequestParam(value = "remark", required = false, defaultValue = "") String remark, //
        @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            para.put("relationType", relationType);
            int relationObjectId = 0;
            if (FirstCategoryWindowRelationTypeEnum.TYPE_OF_SALE_PRODUCT.getCode() == relationType)
            {
                ProductEntity pe = productService.findProductById(saleProductId, 1);
                if (pe == null)
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "Id=" + saleProductId + "的特卖单品不存在");
                    return JSON.toJSONString(resultMap);
                }
                relationObjectId = saleProductId;
            }
            else if (FirstCategoryWindowRelationTypeEnum.TYPE_OF_GROUP_SALE.getCode() == relationType)
            {
                ActivitiesCommonEntity ace = activitiesCommonService.findAcCommonById(groupSaleId);
                if (ace == null)
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "Id=" + groupSaleId + "的特卖组合不存在");
                    return JSON.toJSONString(resultMap);
                }
                relationObjectId = groupSaleId;
            }
            else if (FirstCategoryWindowRelationTypeEnum.TYPE_OF_MALL_PRODUCT.getCode() == relationType)
            {
                ProductEntity pe = productService.findProductById(mallProductId, 2);
                if (pe == null)
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "Id=" + mallProductId + "的商城单品不存在");
                    return JSON.toJSONString(resultMap);
                }
                relationObjectId = mallProductId;
            }
            else if (FirstCategoryWindowRelationTypeEnum.TYPE_OF_CUSTOM_ACTIVITY.getCode() == relationType)
            {
                Map<String, Object> map = customActivitiesService.findCustomActivitiesById(customActivityId);
                if (map == null || map.size() == 0)
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "Id=" + customActivityId + "的自定义活动不存在");
                    return JSON.toJSONString(resultMap);
                }
                relationObjectId = customActivityId;
            }
            image = image.indexOf(ImageUtil.getPrefix()) > 0 ? image : (image + ImageUtil.getPrefix() + ImageUtil.getSuffix(ImageTypeEnum.v17newsell.ordinal()));
            para.put("relationObjectId", relationObjectId);
            para.put("image", image);
            para.put("remark", remark);
            para.put("isAvailable", isAvailable);
            
            int result = categoryService.saveOrUpdateCategoryActivity(para);
            if (result == 1)
            {
                resultMap.put("status", 1);
            }
            else if (result == 2)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "图片尺寸不符合要求");
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "保存失败");
            }
            
        }
        catch (Exception e)
        {
            logger.error(e.getCause(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "服务器发生异常，请刷新后重试。如果问题一直存在，请联系管理员");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 修改分类活动可用状态
     * 
     * @param id
     * @param isAvailable
     * @return
     */
    @RequestMapping(value = "/updateCategoryActivityStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "分类管理-修改分类活动可用状态")
    public String updateCategoryActivityStatus(@RequestParam(value = "id", required = true) String id, @RequestParam(value = "isAvailable", required = true) int isAvailable)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("idList", Arrays.asList(id.split(",")));
            para.put("isAvailable", isAvailable);
            
            int resultStatus = categoryService.updateCategoryActivityStatus(para);
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
     * 修改分类活动排序值
     * 
     * @param id
     * @param sequence
     * @return
     */
    @RequestMapping(value = "/updateCategoryActivitySequence", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "分类管理-修改分类活动排序值")
    public String updateCategoryActivitySequence(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "sequence", required = true) int sequence)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            para.put("sequence", sequence);
            int resultStatus = categoryService.updateCategoryActivitySequence(para);
            if (resultStatus == 1)
            {
                resultMap.put("status", 1);
            }
            else
            {
                resultMap.put("status", 0);
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
    
    @RequestMapping(value = "/jsonProductCategory", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonProductCategory(HttpServletRequest request, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "30") int rows, @RequestParam(value = "productId", required = false, defaultValue = "0") int productId,
        @RequestParam(value = "baseId", required = false, defaultValue = "0") int baseId)
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
            para.put("productId", productId);
            para.put("baseId", baseId);
            
            resultMap = categoryService.jsonProductCategory(para);
            
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/updateProductCategoryThirdSequence", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "分类管理-修改商品三级分类排序值")
    public String updateProductCategoryThirdSequence(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "sequence", required = true) int sequence)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (id == 0)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "暂无三级分类");
                return JSON.toJSONString(resultMap);
            }
            para.put("id", id);
            para.put("sequence", sequence);
            int resultStatus = categoryService.updateProductCategoryThirdSequence(para);
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
     * 一级分类颜色
     * 
     * @param color
     * @return
     */
    @RequestMapping(value = "/jsonCategoryColorCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonCategoryColorCode(@RequestParam(value = "color", required = false, defaultValue = "") String color)
    {
        List<Map<String, String>> codeList = new ArrayList<Map<String, String>>();
        try
        {
            for (FirstCategoryColorTypeEnum c : FirstCategoryColorTypeEnum.values())
            {
                Map<String, String> map = new HashMap<String, String>();
                map.put("code", c.getValue());
                map.put("text", c.getName());
                if (color.equals(c.getValue()))
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
}
