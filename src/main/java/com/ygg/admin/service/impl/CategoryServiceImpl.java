package com.ygg.admin.service.impl;

import com.ygg.admin.code.FirstCategoryColorTypeEnum;
import com.ygg.admin.code.FirstCategoryWindowRelationTypeEnum;
import com.ygg.admin.dao.CategoryDao;
import com.ygg.admin.entity.*;
import com.ygg.admin.service.CategoryService;
import com.ygg.admin.util.ImageUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService
{
    @Resource
    private CategoryDao categoryDao;
    
    @Override
    public CategoryFirstEntity findCategoryFirstById(int firstCategoryId)
        throws Exception
    {
        return categoryDao.findCategoryFirstById(firstCategoryId);
    }
    
    @Override
    public Map<String, Object> jsonCategoryFirstInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        List<CategoryFirstEntity> infoList = categoryDao.findAllCategoryFirst(para);
        int total = 0;
        
        for (CategoryFirstEntity cfe : infoList)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", cfe.getId());
            map.put("index", cfe.getId());
            map.put("name", cfe.getName());
            map.put("sequence", cfe.getSequence());
            map.put("image1", cfe.getImage1());
            map.put("imageURL1", "<a href='" + cfe.getImage1() + "' target='_blank'>查看</a>");
            map.put("image2", cfe.getImage2());
            map.put("imageURL2", "<a href='" + cfe.getImage2() + "' target='_blank'>查看</a>");
            map.put("tag", cfe.getTag());
            map.put("tags", cfe.getTag().replaceAll("#", "&nbsp;&nbsp;"));
            map.put("colorName", FirstCategoryColorTypeEnum.getName(cfe.getColor()));
            map.put("colorValue", cfe.getColor());
            map.put("isAvailable", cfe.getIsAvailable());
            map.put("isAvailableDesc", cfe.getIsAvailable() == 1 ? "可用" : "停用");
            map.put("isShowInApp", cfe.getIsShowInApp());
            map.put("isShowInAppDesc", cfe.getIsShowInApp() == 1 ? "展现" : "不展现");
            resultList.add(map);
        }
        total = categoryDao.countCategoryFirst(para);
        resultMap.put("rows", resultList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public List<CategoryFirstEntity> findAllCategoryFirst(Map<String, Object> para)
        throws Exception
    {
        return categoryDao.findAllCategoryFirst(para);
    }
    
    @Override
    public int updateCategoryFirstStatus(Map<String, Object> para)
        throws Exception
    {
        return categoryDao.updateCategoryFirstStatus(para);
    }
    
    @Override
    public int saveOrUpdateCategoryFirst(Map<String, Object> para)
        throws Exception
    {
        int id = (int)para.get("id");
        /*
         * Map<String, Object> imageMap1 = ImageUtil.getProperty(para.get("image1") + ""); int width1 =
         * Integer.valueOf(imageMap1.get("width") + "").intValue(); int height1 =
         * Integer.valueOf(imageMap1.get("height") + "").intValue(); Map<String, Object> imageMap2 =
         * ImageUtil.getProperty(para.get("image2") + ""); int width2 = Integer.valueOf(imageMap2.get("width") +
         * "").intValue(); int height2 = Integer.valueOf(imageMap2.get("height") + "").intValue(); if ((width1 != 333 &&
         * height1 != 333) || (width2 != 333 && height2 != 333)) { return 2; }
         */
        if (id == 0)
        {
            int sequence = categoryDao.findCategoryFirstMaxSequence();
            para.put("sequence", sequence);
            return categoryDao.insertCategoryFirst(para);
        }
        else
        {
            return categoryDao.updateCategoryFirst(para);
        }
    }
    
    @Override
    public int updateCategoryFirstSequence(Map<String, Object> para)
        throws Exception
    {
        return categoryDao.updateCategoryFirst(para);
    }
    
    @Override
    public Map<String, Object> jsonCategorySecondInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = categoryDao.findAllCategorySecondInfo(para);
        List<Map<String, Object>> categoryThirdAmountList = categoryDao.countCategoryThirdGroupByCategorySecondId();
        Map<Integer, Integer> thirdAmountMap = new HashMap<>();
        for (Map<String, Object> map : categoryThirdAmountList)
        {
            Integer categorySecondId = Integer.valueOf(map.get("categorySecondId") + "").intValue();
            Integer amount = Integer.valueOf(map.get("amount") + "").intValue();
            thirdAmountMap.put(categorySecondId, amount);
        }
        int total = 0;
        
        for (Map<String, Object> map : infoList)
        {
            Integer id = Integer.valueOf(map.get("id") + "");
            int amount = thirdAmountMap.get(id) == null ? 0 : thirdAmountMap.get(id);
            map.put("index", map.get("id"));
            map.put("amount", amount);
            String imageUrl = StringUtils.isEmpty((String) map.get("image"))? "" :
                    "<a target=\"_blank\" href=\" "+ map.get("image") +" \"> 查看</a>";
            map.put("imageUrl", imageUrl);
            map.put("isAvailableDesc", Integer.valueOf(map.get("isAvailable") + "") == 1 ? "可用" : "停用");
            map.put("isDisplayDesc", Integer.valueOf(map.get("isDisplay") + "") == 1 ? "展现" : "不展现");
            map.put("orderTypeDesc", Integer.valueOf(map.get("orderType") + "") == 1 ? "按销量排序" : "按所包含三级类目的顺序排序");
        }
        total = categoryDao.countCategorySecondInfo(para);
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public List<CategorySecondEntity> findAllCategorySecond(Map<String, Object> para)
        throws Exception
    {
        return categoryDao.findAllCategorySecond(para);
    }
    
    @Override
    public CategorySecondEntity findCategorySecondById(int secondCategoryId)
        throws Exception
    {
        return categoryDao.findCategorySecondById(secondCategoryId);
    }
    
    @Override
    public int saveOrUpdateCategorySecond(Map<String, Object> para)
        throws Exception
    {
        int id = (int)para.get("id");
        int categoryFirstId = (int)para.get("categoryFirstId");
        if (id == 0)
        {
            int sequence = categoryDao.findCategorySecondMaxSequence(categoryFirstId);
            para.put("sequence", sequence);
            return categoryDao.insertCategorySecond(para);
        }
        else
        {
            return categoryDao.updateCategorySecond(para);
        }
    }
    
    @Override
    public int updateCategorySecondStatus(Map<String, Object> para)
        throws Exception
    {
        if (para.get("isAvailable") != null)
        {
            // 二级类目停用时，停用所有的三级类目并且删除关联的商品
            Integer isAvailable = Integer.valueOf(para.get("isAvailable") + "").intValue();
            if (isAvailable == 0)
            {
                // 停用所有被关联的三级分类
                List<Integer> categoryThirdIdList = categoryDao.findCategoryThirdIdByPara(para);
                if (categoryThirdIdList.size() > 0)
                {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("isAvailable", isAvailable);
                    map.put("idList", categoryThirdIdList);
                    updateCategoryThirdStatus(map);
                }
                categoryDao.deleteRelationCategoryAndProductBaseByCategorySecondId(para);
            }
        }
        return categoryDao.updateCategorySecondStatus(para);
    }
    
    @Override
    public int updateCategorySecondSequence(Map<String, Object> para)
        throws Exception
    {
        return categoryDao.updateCategorySecond(para);
    }
    
    @Override
    public CategorySecondEntity findCategorySecondByPara(Map<String, Object> para)
        throws Exception
    {
        return categoryDao.findCategorySecondByPara(para);
    }
    
    @Override
    public Map<String, Object> jsonCategoryThirdInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = categoryDao.findAllCategoryThirdInfo(para);
        int total = 0;
        
        for (Map<String, Object> map : infoList)
        {
            map.put("isAvailableDesc", Integer.valueOf(map.get("isAvailable") + "") == 1 ? "可用" : "停用");
            map.put("isDisplayDesc", Integer.valueOf(map.get("isDisplay") + "") == 1 ? "展现" : "不展现");
            map.put("isHotDesc", Integer.valueOf(map.get("isHot") + "") == 1 ? "是" : "否");
            map.put("isHightLightDesc", Integer.valueOf(map.get("isHighlight") + "") == 1 ? "是" : "否");
            map.put("orderType", Integer.valueOf(map.get("category_third_orderType") + "") == 1 ? "按销量" : "按排序值");
        }
        total = categoryDao.countCategoryThirdInfo(para);
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public List<CategoryThirdEntity> findAllCategoryThird(Map<String, Object> para)
        throws Exception
    {
        return categoryDao.findAllCategoryThird(para);
    }
    
    @Override
    public CategoryThirdEntity findCategoryThirdById(int id)
        throws Exception
    {
        return categoryDao.findCategoryThirdById(id);
    }
    
    @Override
    public CategoryThirdEntity findCategoryThirdByPara(Map<String, Object> para)
        throws Exception
    {
        return categoryDao.findCategoryThirdByPara(para);
    }
    
    @Override
    public int saveOrUpdateCategoryThird(Map<String, Object> para)
        throws Exception
    {
        int id = (int)para.get("id");
        if (id == 0)
        {
            return categoryDao.insertCategoryThird(para);
        }
        else
        {
            return categoryDao.updateCategoryThird(para);
        }
    }
    
    @Override
    public int updateCategoryThirdStatus(Map<String, Object> para)
        throws Exception
    {
        int isAvailable = Integer.valueOf(para.get("isAvailable") + "").intValue();
        // 三级类目停用时，删除所有已经关联的商品
        if (isAvailable == 0)
        {
            categoryDao.deleteRelationCategoryAndProductByCategoryThirdId(para);
            categoryDao.updateRelationCategoryAndProductBaseByCategoryThirdId(para);
        }
        return categoryDao.updateCategoryThirdStatus(para);
    }
    
    @Override
    public int updateCategoryThirdSequence(Map<String, Object> para)
        throws Exception
    {
        return categoryDao.updateCategoryThird(para);
    }
    
    @Override
    public Map<String, Object> jsonFirstWindowInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        List<CategoryFirstWindowEntity> infoList = categoryDao.findAllCategoryFirstWindow(para);
        int total = 0;
        
        for (CategoryFirstWindowEntity cfwe : infoList)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", cfwe.getId());
            map.put("fid", cfwe.getFirstCategoryId());
            map.put("fname", cfwe.getFirstCategoryName());
            map.put("leftType", FirstCategoryWindowRelationTypeEnum.getValue(cfwe.getLeftRelationType()));
            map.put("rightType", FirstCategoryWindowRelationTypeEnum.getValue(cfwe.getRightRelationType()));
            map.put("remark", cfwe.getRemark());
            map.put("sequence", cfwe.getSequence());
            map.put("isAvailable", cfwe.getIsAvailable() == 1 ? "可用" : "停用");
            resultList.add(map);
        }
        total = categoryDao.countCategoryFirstWindow(para);
        resultMap.put("rows", resultList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public CategoryFirstWindowEntity findCategoryFirstWindowById(int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        para.put("start", 0);
        para.put("max", 1);
        List<CategoryFirstWindowEntity> infoList = categoryDao.findAllCategoryFirstWindow(para);
        if (infoList == null || infoList.size() == 0)
        {
            return null;
        }
        return infoList.get(0);
    }
    
    @Override
    public CategoryFirstWindowEntity findCategoryFirstWindowByPara(Map<String, Object> para)
        throws Exception
    {
        para.put("start", 0);
        para.put("max", 1);
        List<CategoryFirstWindowEntity> infoList = categoryDao.findAllCategoryFirstWindow(para);
        if (infoList == null || infoList.size() == 0)
        {
            return null;
        }
        return infoList.get(0);
    }
    
    @Override
    public int saveOrUpdateCategoryFirstWindow(Map<String, Object> para)
        throws Exception
    {
        int id = (int)para.get("id");
        String leftImage = para.get("leftImage") + "";
        Map<String, Object> leftImageMap = ImageUtil.getProperty(leftImage);
        int leftImageWidth = Integer.valueOf(leftImageMap.get("width") + "").intValue();
        int leftImageHeight = Integer.valueOf(leftImageMap.get("height") + "").intValue();
        
        String rightImage = para.get("rightImage") + "";
        Map<String, Object> rightImageMap = ImageUtil.getProperty(rightImage);
        int rightImageWidth = Integer.valueOf(rightImageMap.get("width") + "").intValue();
        int rightImageHeight = Integer.valueOf(rightImageMap.get("height") + "").intValue();
        
        /*
         * if ((leftImageWidth != 333 && leftImageHeight != 444) || (rightImageWidth != 333 && rightImageHeight != 444))
         * { return 2; }
         */
        para.put("leftImageWidth", leftImageWidth);
        para.put("leftImageHeight", leftImageHeight);
        para.put("rightImageWidth", rightImageWidth);
        para.put("rightImageHeight", rightImageHeight);
        if (id == 0)
        {
            return categoryDao.insertCategoryFirstWindow(para);
        }
        else
        {
            return categoryDao.updateCategoryFirstWindow(para);
        }
    }
    
    @Override
    public int updateCategoryFirstWindowSequence(Map<String, Object> para)
        throws Exception
    {
        return categoryDao.updateCategoryFirstWindow(para);
    }
    
    @Override
    public int updateCategoryFirstWindowStatus(Map<String, Object> para)
        throws Exception
    {
        int result = categoryDao.updateCategoryFirstWindowStatus(para);
        return result >= 1 ? 1 : 0;
    }
    
    @Override
    public Map<String, Object> jsonCategoryActivityInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        List<CategoryActivityEntity> infoList = categoryDao.findAllCategoryActivity(para);
        int total = 0;
        
        for (CategoryActivityEntity cae : infoList)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", cae.getId());
            map.put("index", cae.getId());
            map.put("relationType", FirstCategoryWindowRelationTypeEnum.getValue(cae.getRelationType()));
            map.put("remark", cae.getRemark());
            map.put("sequence", cae.getSequence());
            map.put("isAvailable", cae.getIsAvailable() == 1 ? "可用" : "停用");
            resultList.add(map);
        }
        total = categoryDao.countCategoryActivity(para);
        resultMap.put("rows", resultList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public CategoryActivityEntity findCategoryActivityById(int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        para.put("start", 0);
        para.put("max", 1);
        List<CategoryActivityEntity> infoList = categoryDao.findAllCategoryActivity(para);
        if (infoList == null || infoList.size() == 0)
            return null;
        return infoList.get(0);
    }
    
    @Override
    public int saveOrUpdateCategoryActivity(Map<String, Object> para)
        throws Exception
    {
        int id = (int)para.get("id");
        String image = para.get("image") + "";
        Map<String, Object> imageMap = ImageUtil.getProperty(image);
        int width = Integer.valueOf(imageMap.get("width") + "").intValue();
        int height = Integer.valueOf(imageMap.get("height") + "").intValue();
        /*
         * if (width != 333 && height != 444) { return 2; }
         */
        para.put("width", width);
        para.put("height", height);
        if (id == 0)
        {
            return categoryDao.insertCategoryActivity(para);
        }
        else
        {
            return categoryDao.updateCategoryActivity(para);
        }
    }
    
    @Override
    public int updateCategoryActivitySequence(Map<String, Object> para)
        throws Exception
    {
        return categoryDao.updateCategoryActivity(para);
    }
    
    @Override
    public int updateCategoryActivityStatus(Map<String, Object> para)
        throws Exception
    {
        int result = categoryDao.updateCategoryActivityStatus(para);
        return result >= 1 ? 1 : 0;
    }
    
    @Override
    public List<CategoryEntity> findCategoryByProductBaseId(int productId)
        throws Exception
    {
        
        return categoryDao.findCategoryByProductBaseId(productId);
    }
    
    @Override
    public Map<String, Object> jsonProductCategory(Map<String, Object> para)
        throws Exception
    {
        int productId = (int)para.get("productId");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        List<RelationCategoryAndProductEntity> categoryThirdList = categoryDao.findThirdCatetoryByProductId(productId);
        int total = 0;
        if (categoryThirdList.size() > 0)
        {
            for (RelationCategoryAndProductEntity rcape : categoryThirdList)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", rcape.getId());
                map.put("thirdId", rcape.getCategoryThirdId());
                map.put("sequence", rcape.getSequence());
                CategoryThirdEntity cte = categoryDao.findCategoryThirdById(rcape.getCategoryThirdId());
                map.put("categoryName", cte == null ? "" : cte.getName());
                resultList.add(map);
            }
            total = categoryDao.countThirdCatetoryByProductId(productId);
        }
        resultMap.put("rows", resultList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public int updateProductCategoryThirdSequence(Map<String, Object> para)
        throws Exception
    {
        return categoryDao.updateProductCategoryThirdSequence(para);
    }
    
    @Override
    public int updateCategoryThirdDisplay(Map<String, Object> para)
        throws Exception
    {
        return categoryDao.updateCategoryThirdDisplay(para);
    }
}
