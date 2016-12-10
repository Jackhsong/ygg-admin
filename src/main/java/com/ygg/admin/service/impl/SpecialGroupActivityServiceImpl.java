package com.ygg.admin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.code.ActivityEnum;
import com.ygg.admin.dao.ActivitiesCommonDao;
import com.ygg.admin.dao.CustomActivitiesDao;
import com.ygg.admin.dao.ProductDao;
import com.ygg.admin.dao.SpecialGroupActivityDao;
import com.ygg.admin.entity.ProductEntity;
import com.ygg.admin.service.SpecialGroupActivityService;
import com.ygg.admin.util.ImageUtil;

@Service
public class SpecialGroupActivityServiceImpl implements SpecialGroupActivityService
{
    @Resource
    private SpecialGroupActivityDao specialGroupActivityDao;
    
    @Resource
    private ProductDao productDao;
    
    @Resource
    private ActivitiesCommonDao activitiesCommonDao;
    
    @Resource
    private CustomActivitiesDao customActivitiesDao;
    
    @Override
    public String findSpecialGroupActivityByPara(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, Object>> rows = specialGroupActivityDao.findAllSpecialGroupActivity(para);
        resultMap.put("rows", rows);
        resultMap.put("total", specialGroupActivityDao.countSpecialGroupActivity(para));
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public String saveSpecialGroup(String title, int isAvailable)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        if (StringUtils.isEmpty(title))
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "请输入活动名称");
            return JSON.toJSONString(resultMap);
        }
        Map<String, Object> para = new HashMap<>();
        para.put("title", title);
        para.put("isAvailable", isAvailable);
        if (specialGroupActivityDao.saveSpecialGroup(para) > 0)
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "保存成功");
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public String updateSpecialGroup(int id, String title, int isAvailable)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        if (StringUtils.isEmpty(title))
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "请输入活动名称");
            return JSON.toJSONString(resultMap);
        }
        Map<String, Object> para = new HashMap<>();
        para.put("id", id);
        para.put("title", title);
        para.put("isAvailable", isAvailable);
        if (specialGroupActivityDao.updateSpecialGroup(para) > 0)
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "保存成功");
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public Map<String, Object> findSpecialGroupActivityById(int specialGroupActivityId)
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        para.put("id", specialGroupActivityId);
        List<Map<String, Object>> reList = specialGroupActivityDao.findAllSpecialGroupActivity(para);
        if (reList != null && !reList.isEmpty())
        {
            return reList.get(0);
        }
        return null;
    }
    
    @Override
    public Map<String, Object> findSpecialGroupActivityProductByPara(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, Object>> productInfo = specialGroupActivityDao.findSpecialGroupActivityProductByPara(para);
        List<Map<String, Object>> rows = new ArrayList<>();
        for (Map<String, Object> product : productInfo)
        {
            Map<String, Object> item = new HashMap<>();
            int type = Integer.parseInt(product.get("type").toString());
            item.put("id", product.get("id"));
            item.put("activityId", product.get("special_activity_group_id"));
            item.put("type", product.get("type"));
            if (type == 1)
            {
                item.put("layoutType", product.get("layout_type"));
                item.put("oneRemark", product.get("one_remark"));
                item.put("oneImageUrl", product.get("one_image_url"));
                item.put("oneType", product.get("one_type"));
                item.put("oneDisplayId", product.get("one_display_id"));
                item.put("twoRemark", product.get("two_remark"));
                item.put("twoImageUrl", product.get("two_image_url"));
                item.put("twoType", product.get("two_type"));
                item.put("twoDisplayId", product.get("two_display_id"));
            }
            else if (type == 2)
            {
                int productId = Integer.parseInt(product.get("product_id").toString());
                item.put("productId", productId);
                
                ProductEntity pe = productDao.findProductByID(productId, null);
                item.put("productName", pe == null ? "" : pe.getName());
            }
            item.put("isDisplay", product.get("is_display"));
            item.put("sequence", product.get("sequence"));
            rows.add(item);
        }
        resultMap.put("rows", rows);
        resultMap.put("total", specialGroupActivityDao.countSpecialGroupActivityProduct(para));
        return resultMap;
    }
    
    @Override
    public String editSpecialGroupActivityProduct(int id, int activityId, int type, int layoutType, int oneType, String oneRemark, String oneImageUrl, int oneDisplayId,
        int twoType, String twoRemark, String twoImageUrl, int twoDisplayId, int productId, int isDisplay)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        
        Object oneImageWidth = 0;
        Object oneImageHeight = 0;
        Object twoImageWidth = 0;
        Object twoImageHeight = 0;
        
        if (type == 1)
        {
            if (StringUtils.isEmpty(oneRemark))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "请输入第一张备注");
                return JSON.toJSONString(resultMap);
            }
            if (StringUtils.isEmpty(oneImageUrl))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "请上传第一张图片");
                return JSON.toJSONString(resultMap);
            }
            
            Map<String, Object> oneImage = ImageUtil.getProperty(oneImageUrl);
            oneImageWidth = oneImage.get("width") == null ? 0 : oneImage.get("width");
            oneImageHeight = oneImage.get("height") == null ? 0 : oneImage.get("height");
            
            if (oneType == ActivityEnum.SPECIAL_ACTIVITY_GROUP_TYPE.PRODUCT.getValue())
            {
                if (productDao.findProductByID(oneDisplayId, null) == null)
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", String.format("ID=%d的商品不存在", oneDisplayId));
                    return JSON.toJSONString(resultMap);
                }
            }
            else if (oneType == ActivityEnum.SPECIAL_ACTIVITY_GROUP_TYPE.GROUP.getValue())
            {
                if (activitiesCommonDao.findActivitiesCommonById(oneDisplayId) == null)
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", String.format("ID=%d的组合不存在", oneDisplayId));
                    return JSON.toJSONString(resultMap);
                }
            }
            else if (oneType == ActivityEnum.SPECIAL_ACTIVITY_GROUP_TYPE.CUSTOM_ACTIVITY.getValue())
            {
                if (customActivitiesDao.findCustomActivitiesById(oneDisplayId) == null)
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", String.format("ID=%d的自定义活动不存在", oneDisplayId));
                    return JSON.toJSONString(resultMap);
                }
            }
            else if (oneType == ActivityEnum.SPECIAL_ACTIVITY_GROUP_TYPE.NONE.getValue())
            {
                oneDisplayId = 0;
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "第一张关联类型不正确");
                return JSON.toJSONString(resultMap);
            }
            
            if (layoutType == 2)
            {
                if (StringUtils.isEmpty(twoRemark))
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "请输入第二张备注");
                    return JSON.toJSONString(resultMap);
                }
                if (StringUtils.isEmpty(twoImageUrl))
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "请上传第二张图片");
                    return JSON.toJSONString(resultMap);
                }
                
                Map<String, Object> twoImage = ImageUtil.getProperty(twoImageUrl);
                twoImageWidth = twoImage.get("width") == null ? 0 : twoImage.get("width");
                twoImageHeight = twoImage.get("height") == null ? 0 : twoImage.get("height");
                
                if (twoType == ActivityEnum.SPECIAL_ACTIVITY_GROUP_TYPE.PRODUCT.getValue())
                {
                    if (productDao.findProductByID(twoDisplayId, null) == null)
                    {
                        resultMap.put("status", 0);
                        resultMap.put("msg", String.format("ID=%d的商品不存在", twoDisplayId));
                        return JSON.toJSONString(resultMap);
                    }
                }
                else if (twoType == ActivityEnum.SPECIAL_ACTIVITY_GROUP_TYPE.GROUP.getValue())
                {
                    if (activitiesCommonDao.findActivitiesCommonById(twoDisplayId) == null)
                    {
                        resultMap.put("status", 0);
                        resultMap.put("msg", String.format("ID=%d的组合不存在", twoDisplayId));
                        return JSON.toJSONString(resultMap);
                    }
                }
                else if (twoType == ActivityEnum.SPECIAL_ACTIVITY_GROUP_TYPE.CUSTOM_ACTIVITY.getValue())
                {
                    if (customActivitiesDao.findCustomActivitiesById(twoDisplayId) == null)
                    {
                        resultMap.put("status", 0);
                        resultMap.put("msg", String.format("ID=%d的自定义活动不存在", twoDisplayId));
                        return JSON.toJSONString(resultMap);
                    }
                }
                else if (twoType == ActivityEnum.SPECIAL_ACTIVITY_GROUP_TYPE.NONE.getValue())
                {
                    twoDisplayId = 0;
                }
                else
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "第二张关联类型不正确");
                    return JSON.toJSONString(resultMap);
                }
            }
        }
        else if (type == 2)
        {
            if (productDao.findProductByID(productId, null) == null)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", String.format("ID=%d的商品不存在", productId));
                return JSON.toJSONString(resultMap);
            }
        }
        
        Map<String, Object> para = new HashMap<>();
        para.put("id", id);
        para.put("activityId", activityId);
        para.put("type", type);
        para.put("layoutType", layoutType);
        para.put("oneType", oneType);
        para.put("oneDisplayId", oneDisplayId);
        para.put("oneRemark", oneRemark);
        para.put("oneImageUrl", oneImageUrl);
        para.put("oneImageWidth", oneImageWidth);
        para.put("oneImageHeight", oneImageHeight);
        para.put("twoType", twoType);
        para.put("twoDisplayId", twoDisplayId);
        para.put("twoRemark", twoRemark);
        para.put("twoImageUrl", twoImageUrl);
        para.put("twoImageWidth", twoImageWidth);
        para.put("twoImageHeight", twoImageHeight);
        para.put("productId", productId);
        para.put("isDisplay", isDisplay);
        if (id == 0)
        {
            specialGroupActivityDao.insertSpecialGroupActivityProduct(para);
        }
        else
        {
            specialGroupActivityDao.updateSpecialGroupActivityProduct(para);
        }
        
        resultMap.put("status", 1);
        resultMap.put("msg", "保存成功");
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public String updateSpecialGroupActivityProductSequence(int id, int sequence)
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        para.put("id", id);
        para.put("sequence", sequence);
        Map<String, Object> resultMap = new HashMap<>();
        if (specialGroupActivityDao.updateSpecialGroupActivityProduct(para) > 0)
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "保存成功");
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public String deleteSpecialGroupActivityProduct(int id)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        if (specialGroupActivityDao.deleteSpecialGroupActivityProduct(id) > 0)
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "删除成功");
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "删除失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public String updateSpecialGroupActivityProductDisplay(int id, int isDisplay)
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        para.put("id", id);
        para.put("isDisplay", isDisplay);
        Map<String, Object> resultMap = new HashMap<>();
        if (specialGroupActivityDao.updateSpecialGroupActivityProduct(para) > 0)
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "保存成功");
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public String batchAddSpecialGroupActivityProduct(int activityId, int type, int isDisplay, List<Integer> productIdList)
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        para.put("activityId", activityId);
        
        List<Integer> existsProductIds = new ArrayList<Integer>();
        List<Map<String, Object>> products = specialGroupActivityDao.findSpecialGroupActivityProductByPara(para);
        for (Map<String, Object> it : products)
        {
            existsProductIds.add(Integer.parseInt(it.get("product_id") + ""));
        }
        
        productIdList.removeAll(existsProductIds);
        int result = 0;
        if (productIdList.size() > 0)
        {
            para.clear();
            para.put("activityId", activityId);
            para.put("type", type);
            para.put("layoutType", 1);
            para.put("oneType", 1);
            para.put("oneDisplayId", 0);
            para.put("oneRemark", "");
            para.put("oneImageUrl", "");
            para.put("oneImageWidth", 0);
            para.put("oneImageHeight", 0);
            para.put("twoType", 1);
            para.put("twoDisplayId", 0);
            para.put("twoRemark", "");
            para.put("twoImageUrl", "");
            para.put("twoImageWidth", 0);
            para.put("twoImageHeight", 0);
            para.put("isDisplay", isDisplay);
            for (Integer productId : productIdList)
            {
                para.put("productId", productId);
                result += specialGroupActivityDao.insertSpecialGroupActivityProduct(para);
            }
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", 1);
        resultMap.put("msg", "成功添加" + result + "条");
        
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public List<Map<String, Object>> findAllSpecialGroupActivity(Map<String, Object> para)
        throws Exception
    {
        
        return specialGroupActivityDao.findAllSpecialGroupActivity(para);
    }
}
