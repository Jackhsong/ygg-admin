package com.ygg.admin.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.code.BrandEnum;
import com.ygg.admin.dao.BrandDao;
import com.ygg.admin.dao.MallCountryDao;
import com.ygg.admin.entity.BrandEntity;
import com.ygg.admin.entity.CategoryFirstEntity;
import com.ygg.admin.entity.ResultEntity;
import com.ygg.admin.service.BrandService;
import com.ygg.admin.service.CategoryService;
import com.ygg.admin.service.SaleFlagService;
import com.ygg.admin.util.Excel.ExcelMaker;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.*;

@Controller
@RequestMapping("/brand")
public class BrandController
{
    
    private static Logger logger = Logger.getLogger(BrandController.class);
    
    @Resource(name = "brandService")
    BrandService brandService = null;

    @Resource
    BrandDao brandDao;

    @Resource(name = "categoryService")
    private CategoryService categoryService;

    @Resource
    private MallCountryDao mallCountryDao;
    @Resource
    private SaleFlagService saleFlagService;
    
    /**
     * 跳转到品牌添加页面
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/add")
    public ModelAndView toAdd(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("brand/update");
        BrandEntity brand = (BrandEntity)request.getSession().getAttribute("wrongBrandInfo");
        if (brand == null)
        {
            brand = new BrandEntity();
        }
        else
        {
            request.getSession().setAttribute("wrongBrandInfo", null);
        }
        // 一级分类
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("isAvailable", 1);
        List<CategoryFirstEntity> catetgoryFirstList = categoryService.findAllCategoryFirst(para);
        List<Map<String, Object>> brandCategoryList = new ArrayList<>();
        Map<String, Object> category = new HashMap<>();
        category.put("cateId", 0);
        category.put("catetgoryFirstList", catetgoryFirstList);
        brandCategoryList.add(category);
        mv.addObject("brandCategoryList", brandCategoryList);
        mv.addObject("stateId", "0");
        mv.addObject("brand", brand);
        return mv;
    }
    
    /**
     * 保存品牌信息
     * 
     * @param request
     * @param editId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ControllerLog(description = "品牌管理-新增/编辑品牌信息")
    public ModelAndView save(HttpServletRequest request, //
        @RequestParam(value = "editId", required = false, defaultValue = "0") int editId, //
        BrandEntity brandEntity, @RequestParam(value = "activityType", required = false, defaultValue = "0") int activityType, // 品牌活动类型；1：组合，2：网页自定义活动，3：原生自定义活动
        @RequestParam(value = "activitiesCommonId", required = false, defaultValue = "0") int activitiesCommonId, //
        @RequestParam(value = "activitiesCustomId", required = false, defaultValue = "0") int activitiesCustomId, //
        @RequestParam(value = "pageId", required = false, defaultValue = "0") int pageId)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        brandEntity.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        brandEntity.setId(editId);

        if (brandEntity.getCategoryFirstIdList() != null) { // 品牌一级分类从一个变成多个
            Set<Integer> categoryIdSet = Sets.newLinkedHashSet(brandEntity.getCategoryFirstIdList());
            categoryIdSet.remove(0);
            brandEntity.setCategoryFirstIdList(Lists.newArrayList(categoryIdSet));
            brandEntity.setCategoryFirstId(brandEntity.getCategoryFirstIdList().get(0));
        }

        if (activityType == BrandEnum.ACTIVITY_TYPE.ACTIVITIESCOMMON.getCode() && activitiesCommonId > 0)
        {
            brandEntity.setActivityDisplayId(activitiesCommonId);
        }
        else if (activityType == BrandEnum.ACTIVITY_TYPE.ACTIVITIESCUSTOM.getCode() && activitiesCustomId > 0)
        {
            brandEntity.setActivityDisplayId(activitiesCustomId);
        }
        else if (activityType == BrandEnum.ACTIVITY_TYPE.PAGE.getCode() && pageId > 0)
        {
            brandEntity.setActivityDisplayId(pageId);
        }
        int resultStatus = brandService.saveOrUpdate(brandEntity);
        if (resultStatus != 1)
        {
            request.getSession().setAttribute("wrongBrandInfo", brandEntity);
            mv.setViewName("redirect:/brand/add");
            return mv;
        }
        mv.setViewName("redirect:/brand/list/1");
        return mv;
    }
    
    /**
     * 品牌管理列表
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/list/{isAvailable}")
    public ModelAndView list(HttpServletRequest request, @PathVariable("isAvailable") int isAvailable)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.addObject("listType", isAvailable == 1 ? "可用品牌" : "停用品牌");
        mv.addObject("isAvailable", isAvailable);
        mv.addObject("stateId", 0);
        mv.addObject("categoryFirstId", 0);
        mv.setViewName("brand/list");
        return mv;
    }
    
    @RequestMapping(value = "/checkName", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String checkName(String name, @RequestParam(value = "editId", required = false, defaultValue = "0") int editId)
        throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        if (editId != 0)
        {// 修改跳过检出
            map.put("status", 1);
            return JSON.toJSONString(map);
        }
        map.put("name", name);
        int resultNum = brandService.countBrandByName(map);
        if (resultNum == 0)
        {
            map.put("status", 1);
        }
        else
        {
            map.put("status", 0);
            map.put("msg", "已经存在该品牌");
            
        }
        return JSON.toJSONString(map);
    }
    
    /**
     * 异步获取品牌信息
     * 
     * @param request
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "/jsonInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonInfo(HttpServletRequest request, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "30") int rows,
        @RequestParam(value = "name", required = false, defaultValue = "") String name,
        @RequestParam(value = "enName", required = false, defaultValue = "") String enName,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "1") byte isAvailable,
        @RequestParam(value = "stateId", required = false, defaultValue = "-1") int stateId,
        @RequestParam(value = "categoryFirstId", required = false, defaultValue = "-1") int categoryFirstId)
//        @RequestParam(value = "categoryFirstIdList", required = false) List<Integer> categoryFirstIdList)
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
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
        if(!"".equals(enName))
        {
            para.put("enName", "%" + enName + "%");
        }
        para.put("isAvailable", isAvailable);
        if (stateId != -1)
        {
            para.put("stateId", stateId);
        }
        if (categoryFirstId != -1)
        {
//            para.put("categoryFirstId", categoryFirstId);
            para.put("categoryFirstIdList", Lists.newArrayList(categoryFirstId));
        }
//        if(CollectionUtils.isNotEmpty(categoryFirstIdList))
//        {
//            para.put("categoryFirstIdList", categoryFirstIdList);
//        }
        String jsonInfoString = brandService.jsonBrandInfo(para);
        return jsonInfoString;
    }
    
    /**
     * 跳转至商家编辑页面
     * 
     * @param request
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ModelAndView editTemplate(HttpServletRequest request, @PathVariable("id") int id)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("brand/update");
        BrandEntity brand = brandService.findBrandById(id);
        if (brand == null)
        {
            mv.setViewName("forward:/error/404");
            return mv;
        }
        mv.addObject("brand", brand);
        // 一级分类
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("isAvailable", 1);
        List<CategoryFirstEntity> catetgoryFirstList = categoryService.findAllCategoryFirst(para);
        mv.addObject("catetgoryFirstList", catetgoryFirstList);
        mv.addObject("stateId", brand.getStateId() + "");
        List<Integer> cateIds =brandDao.findRelationBrandCategoryIdsByBrandId(brand.getId());
        mv.addObject("categoryFirstIdsList",
                CollectionUtils.isEmpty(cateIds) ? Lists.newArrayList(brand.getCategoryFirstId()) : cateIds);
        cateIds = CollectionUtils.isEmpty(cateIds) ? Lists.newArrayList(brand.getCategoryFirstId()) : cateIds;

        List<Map<String, Object>> brandCategoryList = new ArrayList<>();
        for (Integer cateId : cateIds) {
            Map<String, Object> category = new HashMap<>();
            category.put("cateId", cateId);
            category.put("catetgoryFirstList", catetgoryFirstList);
            brandCategoryList.add(category);
        }
        mv.addObject("brandCategoryList", brandCategoryList);

        return mv;
    }
    
    /**
     * 得到品牌信息以json格式返回
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jsonBrandCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonBrandCode(HttpServletRequest request, @RequestParam(value = "isAvailable", required = false, defaultValue = "-1") int isAvailable,
        @RequestParam(value = "brandId", required = false, defaultValue = "-1") int brandId,
        @RequestParam(value = "mallCountryId", required = false, defaultValue = "-1") int mallCountryId)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (isAvailable != -1)
        {
            para.put("isAvailable", isAvailable);
        }
        if(mallCountryId != -1){
            Map map = new HashMap();
            map.put("id", mallCountryId);
            List<Map<String, Object>> mallCountry = mallCountryDao.findAllMallCountryByPara(map);
            para.put("stateId", ((Long) mallCountry.get(0).get("saleFlagId")).intValue());
        }
        List<BrandEntity> brandList = brandService.findAllBrand(para);
        List<Map<String, String>> codeList = new ArrayList<Map<String, String>>();
        Map<String, String> mapAll = new HashMap<String, String>();
        if (isAvailable == -1)
        {
            mapAll.put("code", "0");
            mapAll.put("text", "全部");
            codeList.add(mapAll);
        }
        for (BrandEntity entity : brandList)
        {
            Map<String, String> map = new HashMap<String, String>();
            map.put("code", entity.getId() + "");
            map.put("text", entity.getName());
            if (brandId == entity.getId())
            {
                map.put("selected", "true");
            }
            codeList.add(map);
        }
        return JSON.toJSONString(codeList);
    }
    
    @RequestMapping(value = "/exportBrand")
    @ControllerLog(description = "品牌管理-导出品牌")
    public void exportBrand(HttpServletRequest request,
        HttpServletResponse response,
        @RequestParam(value = "brandName", required = false, defaultValue = "") String name,
        @RequestParam(value = "enName", required = false, defaultValue = "") String enName,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "-1") int isAvailable,
        @RequestParam(value = "stateId", required = false, defaultValue = "-1") int stateId,
        @RequestParam(value = "categoryFirstId", required = false, defaultValue = "-1") int categoryFirstId)
//        @RequestParam(value = "categoryFirstIdList", required = false) List<Integer> categoryFirstIdList)
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        if (isAvailable != -1)
        {
            para.put("isAvailable", isAvailable);
        }
        if (!"".equals(name))
        {
            para.put("name", "%" + name + "%");
        }
        if (!"".equals(enName))
        {
            para.put("enName", "%" + enName + "%");
        }
        if (stateId != -1)
        {
            para.put("stateId", stateId);
        }
        if (categoryFirstId != -1)
        {
            para.put("categoryFirstIdList", Lists.newArrayList(categoryFirstId));
        }
//        if(CollectionUtils.isNotEmpty(categoryFirstIdList))
//        {
//            para.put("categoryFirstIdList", categoryFirstIdList);
//        }

        response.setContentType("application/vnd.ms-excel");
        // 进行转码，使其支持中文文件名
        String codedFileName = java.net.URLEncoder.encode("品牌", "UTF-8");
        response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
        try (OutputStream fOut = response.getOutputStream())
        {
            List<CategoryFirstEntity> clist = categoryService.findAllCategoryFirst(null);
            Map<String, Object> smap = saleFlagService.jsonSaleFlagInfo(null);
            List<Map<String, Object>> sl = (List<Map<String, Object>>)smap.get("rows");
            
            List<BrandEntity> brandList = brandService.findAllBrand(para);
            Map<Integer, BrandEntity> idAndBrandMap = new LinkedHashMap<>();
            for (BrandEntity brand : brandList) {  // 多个一级分类设置
                if (!idAndBrandMap.containsKey(brand.getId())) {
                        idAndBrandMap.put(brand.getId(), brand);
                }
            }
            brandList = Lists.newArrayList(idAndBrandMap.values());

            List<BrandExcelDTO> brandExcelList = new ArrayList<>(brandList.size());
            List<String> displayHeaders = Lists.newArrayList("ID", "品牌名称","品牌英文名" ,"品牌卖点", "品牌介绍", "品牌国家", "品牌类目", "可用状态");
            List<String> headers = Lists.newArrayList("id", "name", "enName","hotSpots", "introduce", "stateId", "categoryFirstId", "isAvailable");
            for (BrandEntity brand : brandList)
            {
                brandExcelList.add(covertToBrandExcelDTO(brand, sl, clist));
            }
            ExcelMaker.from(brandExcelList, headers).displayHeaders(displayHeaders).writeTo(fOut);
            fOut.flush();
        }
        catch (Exception e)
        {
            logger.error("导出品牌出错", e);
        }
    }
    
    @RequestMapping(value = "/deleteBat", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "批量删除")
    public String deleteBat(@RequestParam(value = "ids", required = false, defaultValue = "0") String ids)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            int total = 0;
            String[] arr = ids.split(",");
            for (String it : arr)
            {
                total += brandService.delete(Integer.valueOf(it));
            }
            resultMap.put("status", 1);
            resultMap.put("msg", "成功删除" + total + "条记录。");
            return JSON.toJSONString(resultMap);
        }
        catch (Exception e)
        {
            logger.error("品牌管理批量删除失败", e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
            return JSON.toJSONString(resultMap);
        }
    }
    
    /**
     * 新增、修改
     * 
     * @param param
     * @return
     */
    @RequestMapping("/updateInfo")
    @ResponseBody
    public Object updateInfo(@RequestParam Map<String, Object> param)
    {
        try
        {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 1);
            resultMap.put("data", brandService.updateInfo(param));
            return resultMap;
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", e.getMessage());
            return resultMap;
        }
    }
    
    @SuppressWarnings({"rawtypes", "unchecked"})
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "删除")
    public String delete(HttpServletRequest request, @RequestParam(value = "id", required = true) int id)
        throws Exception
    {
        Map resultMap = new HashMap();
        int resultStatus = brandService.delete(id);
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

    @RequestMapping("/info/{id}")
    @ResponseBody
    public ResultEntity info(@PathVariable int id ){
        try {
            BrandEntity brand = brandService.findBrandById(id);
            return ResultEntity.getSuccessResult(brand);
        } catch (Exception e) {
            logger.error(e);
            return ResultEntity.getFailResult("程序异常");
        }
    }
    
    private BrandExcelDTO covertToBrandExcelDTO(BrandEntity brand, List<Map<String, Object>> sl, List<CategoryFirstEntity> clist)
    {
        BrandExcelDTO dto = new BrandExcelDTO();
        dto.setId(brand.getId());
        
        dto.setName(brand.getName());
        dto.setHotSpots(brand.getHotSpots());
        dto.setIntroduce(brand.getIntroduce());
        dto.setEnName(brand.getEnName());
        dto.setStateId(brand.getStateId() == 0 ? "" : brand.getStateId() + "");
        for (Map<String, Object> m : sl)
        {
            int stateId_ = Integer.valueOf(m.get("id") + "").intValue();
            if (stateId_ == brand.getStateId())
            {
                dto.setStateId(m.get("name") + "");
                break;
            }
        }
        List<Integer> cateids = brandDao.findRelationBrandCategoryIdsByBrandId(brand.getId());
        if (CollectionUtils.isNotEmpty(cateids)) {
            List<String> names = new ArrayList<>();
            for (Integer cateId : cateids) {
                for (CategoryFirstEntity cfe : clist) {
                    if (cateId == cfe.getId()) {
                        names.add(cfe.getName());
                    }
                }
            }
            dto.setCategoryFirstId(Joiner.on(",").join(names));
        }

        dto.setIsAvailable(brand.getIsAvailable() == 1 ? "可用" : "停用");
        return dto;
    }

    @RequestMapping("/findBrandInfoById")
    @ResponseBody
    public ResultEntity findBrandInfoById(@RequestParam(value = "id", required = true) int id)
        throws Exception
    {
        BrandEntity be = brandService.findBrandById(id);
        if (be == null)
        {
            return ResultEntity.getFailResult("品牌不存在！");
        }
        return ResultEntity.getSuccessResult(be.getName());
    }
}

class BrandExcelDTO
{
    int id;

    String enName;
    
    String name;
    
    String hotSpots;
    
    String introduce;
    
    String stateId;
    
    String categoryFirstId;
    
    String isAvailable;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getHotSpots()
    {
        return hotSpots;
    }
    
    public void setHotSpots(String hotSpots)
    {
        this.hotSpots = hotSpots;
    }
    
    public String getIntroduce()
    {
        return introduce;
    }
    
    public void setIntroduce(String introduce)
    {
        this.introduce = introduce;
    }
    
    public String getStateId()
    {
        return stateId;
    }
    
    public void setStateId(String stateId)
    {
        this.stateId = stateId;
    }
    
    public String getCategoryFirstId()
    {
        return categoryFirstId;
    }
    
    public void setCategoryFirstId(String categoryFirstId)
    {
        this.categoryFirstId = categoryFirstId;
    }
    
    public String getIsAvailable()
    {
        return isAvailable;
    }
    
    public void setIsAvailable(String isAvailable)
    {
        this.isAvailable = isAvailable;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }
}
