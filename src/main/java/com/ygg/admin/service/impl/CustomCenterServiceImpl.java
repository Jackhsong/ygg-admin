package com.ygg.admin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.code.CustomCenterDisplayTypeEnum;
import com.ygg.admin.code.CustomCenterTypeEnum;
import com.ygg.admin.code.ProductEnum;
import com.ygg.admin.dao.ActivitiesCommonDao;
import com.ygg.admin.dao.CustomActivitiesDao;
import com.ygg.admin.dao.CustomCenterDao;
import com.ygg.admin.dao.PageDao;
import com.ygg.admin.dao.ProductDao;
import com.ygg.admin.entity.ActivitiesCommonEntity;
import com.ygg.admin.entity.CustomActivityEntity;
import com.ygg.admin.entity.CustomCenterEntity;
import com.ygg.admin.entity.ProductEntity;
import com.ygg.admin.service.CustomCenterService;
import com.ygg.admin.util.CommonConstant;
import com.ygg.admin.util.ImageUtil;

@Service
public class CustomCenterServiceImpl implements CustomCenterService
{
    @Resource
    private CustomCenterDao customCenterDao;
    
    @Resource
    private ProductDao productDao;
    
    @Resource
    private ActivitiesCommonDao activitiesCommonDao;
    
    @Resource
    private CustomActivitiesDao customActivitiesDao;
    
    @Resource
    private PageDao pageDao;
    
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> jsonCustomCenterInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        int count = 0;
        List<CustomCenterEntity> reList = customCenterDao.findAllCustomCenter(para);
        for (CustomCenterEntity cc : reList)
        {
            Map<String, Object> it = new HashMap<String, Object>();
            it.putAll(new BeanMap(cc));
            it.put("index", cc.getId());
            it.put("displayDesc", cc.getIsDisplay() == 1 ? "展现" : "不展现");
            it.put("displayType", CustomCenterDisplayTypeEnum.getDescByCode(cc.getDisplayType()));
            dataList.add(it);
        }
        count = customCenterDao.countCustomCenter(para);
        result.put("total", count);
        result.put("rows", dataList);
        return result;
    }
    
    @Override
    public String saveCustomCenter(CustomCenterEntity center)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String resultStr = validateParams(center);
        int status = JSON.parseObject(resultStr).getIntValue("status");
        if (status == CommonConstant.COMMON_YES)
        {
            if (customCenterDao.saveCustomCenter(center) > 0)
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
        else
        {
            return resultStr;
        }
    }
    
    @Override
    public String updateCustomCenter(CustomCenterEntity center)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String resultStr = validateParams(center);
        int status = JSON.parseObject(resultStr).getIntValue("status");
        if (status == CommonConstant.COMMON_YES)
        {
            if (customCenterDao.updateCustomCenter(center) > 0)
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
        else
        {
            return resultStr;
        }
    }
    
    @Override
    public int updateDisplayStatus(Map<String, Object> para)
        throws Exception
    {
        return customCenterDao.updateDisplayStatus(para);
    }
    
    @Override
    public CustomCenterEntity findCustomCenterById(int id)
        throws Exception
    {
        return customCenterDao.findCustomCenterById(id);
    }
    
    private String validateParams(CustomCenterEntity center)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        
        if (StringUtils.isEmpty(center.getRemark()))
        {
            result.put("status", 0);
            result.put("msg", "请输入模块备注");
            return JSON.toJSONString(result);
        }
        if (center.getDisplayType() == null)
        {
            result.put("status", 0);
            result.put("msg", "请选择布局方式");
            return JSON.toJSONString(result);
        }
        if (center.getDisplayType() == CustomCenterDisplayTypeEnum.ONE.getCode() || center.getDisplayType() == CustomCenterDisplayTypeEnum.TWO.getCode()
            || center.getDisplayType() == CustomCenterDisplayTypeEnum.THREE.getCode() || center.getDisplayType() == CustomCenterDisplayTypeEnum.FOUR.getCode())
        {
            if (StringUtils.isEmpty(center.getOneImage()))
            {
                result.put("status", 0);
                result.put("msg", "请上传第一张图片");
                return JSON.toJSONString(result);
            }
            
            Map<String, Object> imageMap = ImageUtil.getProperty(center.getOneImage());
            center.setOneWidth(Integer.parseInt(imageMap.get("width") + ""));
            center.setOneHeight(Integer.parseInt(imageMap.get("height") + ""));
            
            if (center.getOneType() == null)
            {
                result.put("status", 0);
                result.put("msg", "请选择第一张关联类型");
                return JSON.toJSONString(result);
            }
            if (!checkTypeAndDisplay(center, 1))
            {
                result.put("status", 0);
                result.put("msg", String.format("Id=%d的%s不存在", center.getOneDisplayId(), CustomCenterTypeEnum.getDescByCode(center.getOneType())));
                return JSON.toJSONString(result);
            }
            if (center.getDisplayType() == CustomCenterDisplayTypeEnum.ONE.getCode())
            {
                center.setOneTitle("");
                center.setOneTitleColor("");
                center.setTwoImage("");
                center.setTwoType((byte)1);
                center.setTwoDisplayId(0);
                center.setTwoRemark("");
                center.setTwoTitle("");
                center.setTwoTitleColor("");
                center.setTwoWidth(0);
                center.setTwoHeight(0);
                center.setThreeImage("");
                center.setThreeType((byte)1);
                center.setThreeDisplayId(0);
                center.setThreeRemark("");
                center.setThreeTitle("");
                center.setThreeTitleColor("");
                center.setThreeWidth(0);
                center.setThreeHeight(0);
                center.setFourImage("");
                center.setFourType((byte)1);
                center.setFourDisplayId(0);
                center.setFourRemark("");
                center.setFourTitle("");
                center.setFourTitleColor("");
                center.setFourWidth(0);
                center.setFourHeight(0);
                
                if (center.getOneWidth() != 750 && center.getOneHeight() != 188)
                {
                    result.put("status", 0);
                    result.put("msg", "一行1张的布局方式图片尺寸要求为750x188，实际尺寸为" + center.getOneWidth() + "x" + center.getOneHeight());
                    return JSON.toJSONString(result);
                }
            }
            else if (center.getDisplayType() == CustomCenterDisplayTypeEnum.TWO.getCode())
            {
                center.setOneTitle("");
                center.setOneTitleColor("");
                if (center.getOneWidth() != 374 && center.getOneHeight() != 188)
                {
                    result.put("status", 0);
                    result.put("msg", "一行2张的布局方式中，第一张图片尺寸要求为374x188，实际尺寸为" + center.getOneWidth() + "x" + center.getOneHeight());
                    return JSON.toJSONString(result);
                }
            }
            else if (center.getDisplayType() == CustomCenterDisplayTypeEnum.THREE.getCode())
            {
                if (StringUtils.isEmpty(center.getOneTitle()))
                {
                    if (center.getOneWidth() != 187 && center.getOneHeight() != 188)
                    {
                        result.put("status", 0);
                        result.put("msg", "一行3张的布局方式中，未填写标题时，第一张图片尺寸要求为187x188，实际尺寸为" + center.getOneWidth() + "x" + center.getOneHeight());
                        return JSON.toJSONString(result);
                    }
                }
                else
                {
                    if (center.getOneWidth() != 60 && center.getOneHeight() != 60)
                    {
                        result.put("status", 0);
                        result.put("msg", "一行3张的布局方式中，填写了标题时，第一张图片尺寸要求为60x60，实际尺寸为" + center.getOneWidth() + "x" + center.getOneHeight());
                        return JSON.toJSONString(result);
                    }
                }
            }
            else if (center.getDisplayType() == CustomCenterDisplayTypeEnum.FOUR.getCode())
            {
                if (StringUtils.isEmpty(center.getOneTitle()))
                {
                    result.put("status", 0);
                    result.put("msg", "一行4张的布局方式中,第一张标题必填");
                    return JSON.toJSONString(result);
                }
                if (center.getOneWidth() != 60 && center.getOneHeight() != 60)
                {
                    result.put("status", 0);
                    result.put("msg", "一行4张的布局方式中，第一张图片尺寸要求为60x60，实际尺寸为" + center.getOneWidth() + "x" + center.getOneHeight());
                    return JSON.toJSONString(result);
                }
            }
        }
        if (center.getDisplayType() == CustomCenterDisplayTypeEnum.TWO.getCode() || center.getDisplayType() == CustomCenterDisplayTypeEnum.THREE.getCode()
            || center.getDisplayType() == CustomCenterDisplayTypeEnum.FOUR.getCode())
        {
            if (StringUtils.isEmpty(center.getTwoImage()))
            {
                result.put("status", 0);
                result.put("msg", "请上传第二张图片");
                return JSON.toJSONString(result);
            }
            
            Map<String, Object> imageMap = ImageUtil.getProperty(center.getTwoImage());
            center.setTwoWidth(Integer.parseInt(imageMap.get("width") + ""));
            center.setTwoHeight(Integer.parseInt(imageMap.get("height") + ""));
            
            if (center.getTwoType() == null)
            {
                result.put("status", 0);
                result.put("msg", "请选择第二张关联类型");
                return JSON.toJSONString(result);
            }
            if (!checkTypeAndDisplay(center, 2))
            {
                result.put("status", 0);
                result.put("msg", String.format("Id=%d的%s不存在", center.getTwoDisplayId(), CustomCenterTypeEnum.getDescByCode(center.getTwoType())));
                return JSON.toJSONString(result);
            }
            if (center.getDisplayType() == CustomCenterDisplayTypeEnum.TWO.getCode())
            {
                center.setOneTitle("");
                center.setOneTitleColor("");
                center.setTwoTitle("");
                center.setTwoTitleColor("");
                center.setThreeImage("");
                center.setThreeType((byte)1);
                center.setThreeDisplayId(0);
                center.setThreeRemark("");
                center.setThreeTitle("");
                center.setThreeTitleColor("");
                center.setThreeWidth(0);
                center.setThreeHeight(0);
                center.setFourImage("");
                center.setFourType((byte)1);
                center.setFourDisplayId(0);
                center.setFourRemark("");
                center.setFourTitle("");
                center.setFourTitleColor("");
                center.setFourWidth(0);
                center.setFourHeight(0);
                
                if (center.getTwoWidth() != 374 && center.getTwoHeight() != 188)
                {
                    result.put("status", 0);
                    result.put("msg", "一行2张的布局方式中，第二张图片尺寸要求为374x188，实际尺寸为" + center.getTwoWidth() + "x" + center.getTwoHeight());
                    return JSON.toJSONString(result);
                }
            }
            else if (center.getDisplayType() == CustomCenterDisplayTypeEnum.THREE.getCode())
            {
                center.setTwoTitle("");
                center.setTwoTitleColor("");
                if (center.getTwoWidth() != 374 && center.getTwoHeight() != 188)
                {
                    result.put("status", 0);
                    result.put("msg", "一行3张的布局方式中，第二张图片尺寸要求为374x188，实际尺寸为" + center.getTwoWidth() + "x" + center.getTwoHeight());
                    return JSON.toJSONString(result);
                }
            }
            else if (center.getDisplayType() == CustomCenterDisplayTypeEnum.FOUR.getCode())
            {
                if (StringUtils.isEmpty(center.getTwoTitle()))
                {
                    result.put("status", 0);
                    result.put("msg", "一行4张的布局方式中,第二张标题必填");
                    return JSON.toJSONString(result);
                }
                if (center.getTwoWidth() != 60 && center.getTwoHeight() != 60)
                {
                    result.put("status", 0);
                    result.put("msg", "一行4张的布局方式中，第二张图片尺寸要求为60x60，实际尺寸为" + center.getTwoWidth() + "x" + center.getTwoHeight());
                    return JSON.toJSONString(result);
                }
            }
        }
        if (center.getDisplayType() == CustomCenterDisplayTypeEnum.THREE.getCode() || center.getDisplayType() == CustomCenterDisplayTypeEnum.FOUR.getCode())
        {
            if (StringUtils.isEmpty(center.getThreeImage()))
            {
                result.put("status", 0);
                result.put("msg", "请上传第三张图片");
                return JSON.toJSONString(result);
            }
            
            Map<String, Object> imageMap = ImageUtil.getProperty(center.getThreeImage());
            center.setThreeWidth(Integer.parseInt(imageMap.get("width") + ""));
            center.setThreeHeight(Integer.parseInt(imageMap.get("height") + ""));
            
            if (center.getThreeType() == null)
            {
                result.put("status", 0);
                result.put("msg", "请选择第三张关联类型");
                return JSON.toJSONString(result);
            }
            if (!checkTypeAndDisplay(center, 3))
            {
                result.put("status", 0);
                result.put("msg", String.format("Id=%d的%s不存在", center.getThreeDisplayId(), CustomCenterTypeEnum.getDescByCode(center.getThreeType())));
                return JSON.toJSONString(result);
            }
            if (center.getDisplayType() == CustomCenterDisplayTypeEnum.THREE.getCode())
            {
                center.setFourImage("");
                center.setFourType((byte)1);
                center.setFourDisplayId(0);
                center.setFourRemark("");
                center.setFourTitle("");
                center.setFourTitleColor("");
                center.setFourWidth(0);
                center.setFourHeight(0);
                
                if (StringUtils.isEmpty(center.getThreeTitle()))
                {
                    if (center.getThreeWidth() != 187 && center.getThreeHeight() != 188)
                    {
                        result.put("status", 0);
                        result.put("msg", "一行3张的布局方式中，未填写标题时，第三张图片尺寸要求为187x188，实际尺寸为" + center.getThreeWidth() + "x" + center.getThreeHeight());
                        return JSON.toJSONString(result);
                    }
                }
                else
                {
                    if (center.getThreeWidth() != 60 && center.getThreeHeight() != 60)
                    {
                        result.put("status", 0);
                        result.put("msg", "一行3张的布局方式中，填写了标题时，第三张图片尺寸要求为60x60，实际尺寸为" + center.getThreeWidth() + "x" + center.getThreeHeight());
                        return JSON.toJSONString(result);
                    }
                }
            }
            else if (center.getDisplayType() == CustomCenterDisplayTypeEnum.FOUR.getCode())
            {
                if (StringUtils.isEmpty(center.getThreeTitle()))
                {
                    result.put("status", 0);
                    result.put("msg", "一行4张的布局方式中,第三张标题必填");
                    return JSON.toJSONString(result);
                }
                if (center.getThreeWidth() != 60 && center.getThreeHeight() != 60)
                {
                    result.put("status", 0);
                    result.put("msg", "一行4张的布局方式中，第三张图片尺寸要求为60x60，实际尺寸为" + center.getThreeWidth() + "x" + center.getThreeHeight());
                    return JSON.toJSONString(result);
                }
            }
        }
        if (center.getDisplayType() == CustomCenterDisplayTypeEnum.FOUR.getCode())
        {
            if (StringUtils.isEmpty(center.getFourTitle()))
            {
                result.put("status", 0);
                result.put("msg", "一行4张的布局方式中,第四张标题必填");
                return JSON.toJSONString(result);
            }
            if (StringUtils.isEmpty(center.getFourImage()))
            {
                result.put("status", 0);
                result.put("msg", "请上传第四张图片");
                return JSON.toJSONString(result);
            }
            
            Map<String, Object> imageMap = ImageUtil.getProperty(center.getFourImage());
            center.setFourWidth(Integer.parseInt(imageMap.get("width") + ""));
            center.setFourHeight(Integer.parseInt(imageMap.get("height") + ""));
            
            if (center.getFourWidth() != 60 && center.getFourHeight() != 60)
            {
                result.put("status", 0);
                result.put("msg", "一行4张的布局方式中，第四张图片尺寸要求为60x60，实际尺寸为" + center.getFourWidth() + "x" + center.getFourHeight());
                return JSON.toJSONString(result);
            }
            
            if (center.getFourType() == null)
            {
                result.put("status", 0);
                result.put("msg", "请选择第四张关联类型");
                return JSON.toJSONString(result);
            }
            if (!checkTypeAndDisplay(center, 4))
            {
                result.put("status", 0);
                result.put("msg", String.format("Id=%d的%s不存在", center.getThreeDisplayId(), CustomCenterTypeEnum.getDescByCode(center.getThreeType())));
                return JSON.toJSONString(result);
            }
        }
        result.put("status", 1);
        return JSON.toJSONString(result);
    }
    
    private boolean checkTypeAndDisplay(CustomCenterEntity center, int type)
        throws Exception
    {
        if (type == 1)
        {
            if (center.getOneType() == CustomCenterTypeEnum.SINGLE_SALE_PRODUCT.getCode())
            {
                ProductEntity pe = productDao.findProductByID(center.getOneDisplayId(), null);
                if (pe == null)
                {
                    return false;
                }
                else
                {
                    if (pe.getType() == ProductEnum.PRODUCT_TYPE.MALL.getCode())
                    {
                        center.setOneType(CustomCenterTypeEnum.SINGLE_MALL_PRODUCT.getCode());
                    }
                }
            }
            else if (center.getOneType() == CustomCenterTypeEnum.GROUP.getCode())
            {
                ActivitiesCommonEntity ac = activitiesCommonDao.findAcCommonById(center.getOneDisplayId());
                if (ac == null)
                {
                    return false;
                }
            }
            else if (center.getOneType() == CustomCenterTypeEnum.CUSTOM_ACTIVITY.getCode())
            {
                CustomActivityEntity cae = customActivitiesDao.findCustomActivitiesId(center.getOneDisplayId());
                if (cae == null)
                {
                    return false;
                }
            }
            else if (center.getOneType() == CustomCenterTypeEnum.CUSTOM_PAGE.getCode())
            {
                Map<String, Object> pageMap = pageDao.findPageById(center.getOneDisplayId());
                if (pageMap == null)
                {
                    return false;
                }
            }
            return true;
        }
        else if (type == 2)
        {
            if (center.getTwoType() == CustomCenterTypeEnum.SINGLE_SALE_PRODUCT.getCode())
            {
                ProductEntity pe = productDao.findProductByID(center.getTwoDisplayId(), null);
                if (pe == null)
                {
                    return false;
                }
                else
                {
                    if (pe.getType() == ProductEnum.PRODUCT_TYPE.MALL.getCode())
                    {
                        center.setTwoType(CustomCenterTypeEnum.SINGLE_MALL_PRODUCT.getCode());
                    }
                }
            }
            else if (center.getTwoType() == CustomCenterTypeEnum.GROUP.getCode())
            {
                ActivitiesCommonEntity ac = activitiesCommonDao.findAcCommonById(center.getTwoDisplayId());
                if (ac == null)
                {
                    return false;
                }
            }
            else if (center.getTwoType() == CustomCenterTypeEnum.CUSTOM_ACTIVITY.getCode())
            {
                CustomActivityEntity cae = customActivitiesDao.findCustomActivitiesId(center.getTwoDisplayId());
                if (cae == null)
                {
                    return false;
                }
            }
            else if (center.getTwoType() == CustomCenterTypeEnum.CUSTOM_PAGE.getCode())
            {
                Map<String, Object> pageMap = pageDao.findPageById(center.getTwoDisplayId());
                if (pageMap == null)
                {
                    return false;
                }
            }
            return true;
        }
        else if (type == 3)
        {
            if (center.getThreeType() == CustomCenterTypeEnum.SINGLE_SALE_PRODUCT.getCode())
            {
                ProductEntity pe = productDao.findProductByID(center.getThreeDisplayId(), null);
                if (pe == null)
                {
                    return false;
                }
                else
                {
                    if (pe.getType() == ProductEnum.PRODUCT_TYPE.MALL.getCode())
                    {
                        center.setThreeType(CustomCenterTypeEnum.SINGLE_MALL_PRODUCT.getCode());
                    }
                }
            }
            else if (center.getThreeType() == CustomCenterTypeEnum.GROUP.getCode())
            {
                ActivitiesCommonEntity ac = activitiesCommonDao.findAcCommonById(center.getThreeDisplayId());
                if (ac == null)
                {
                    return false;
                }
            }
            else if (center.getThreeType() == CustomCenterTypeEnum.CUSTOM_ACTIVITY.getCode())
            {
                CustomActivityEntity cae = customActivitiesDao.findCustomActivitiesId(center.getThreeDisplayId());
                if (cae == null)
                {
                    return false;
                }
            }
            else if (center.getThreeType() == CustomCenterTypeEnum.CUSTOM_PAGE.getCode())
            {
                Map<String, Object> pageMap = pageDao.findPageById(center.getThreeDisplayId());
                if (pageMap == null)
                {
                    return false;
                }
            }
            return true;
        }
        else if (type == 4)
        {
            if (center.getFourType() == CustomCenterTypeEnum.SINGLE_SALE_PRODUCT.getCode())
            {
                ProductEntity pe = productDao.findProductByID(center.getFourDisplayId(), null);
                if (pe == null)
                {
                    return false;
                }
                else
                {
                    if (pe.getType() == ProductEnum.PRODUCT_TYPE.MALL.getCode())
                    {
                        center.setFourType(CustomCenterTypeEnum.SINGLE_MALL_PRODUCT.getCode());
                    }
                }
            }
            else if (center.getFourType() == CustomCenterTypeEnum.GROUP.getCode())
            {
                ActivitiesCommonEntity ac = activitiesCommonDao.findAcCommonById(center.getFourDisplayId());
                if (ac == null)
                {
                    return false;
                }
            }
            else if (center.getFourType() == CustomCenterTypeEnum.CUSTOM_ACTIVITY.getCode())
            {
                CustomActivityEntity cae = customActivitiesDao.findCustomActivitiesId(center.getFourDisplayId());
                if (cae == null)
                {
                    return false;
                }
            }
            else if (center.getFourType() == CustomCenterTypeEnum.CUSTOM_PAGE.getCode())
            {
                Map<String, Object> pageMap = pageDao.findPageById(center.getFourDisplayId());
                if (pageMap == null)
                {
                    return false;
                }
            }
            return true;
        }
        else
        {
            return false;
        }
    }
}
