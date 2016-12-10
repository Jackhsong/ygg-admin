
 /**************************************************************************
 * Copyright (c) 2014-2016 浙江格家网络技术有限公司.
 * All rights reserved.
 * 
 * 项目名称：左岸城堡APP
 * 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.ygg.admin.service.categoryregion.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.code.CustomLayoutStyleTypeEnum;
import com.ygg.admin.dao.categoryregion.CategoryRegionDao;
import com.ygg.admin.entity.categoryregion.Page2ModelCustomLayoutEntity;
import com.ygg.admin.service.categoryregion.CategoryRegionService;
import com.ygg.admin.util.ImageUtil;

/**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: CategoryRegionServiceImpl.java 8560 2016-03-09 07:44:57Z zhanglide $   
  * @since 2.0
  */
@Service("categoryRegionService")
public class CategoryRegionServiceImpl implements CategoryRegionService {
	 /**    */
	@Resource(name="categoryRegionDao")
	private CategoryRegionDao categoryRegionDao;
    
    @Override
    public Map<String, Object> jsonCustomRegionInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = categoryRegionDao.findAllCustomRegion(para);
        int total = 0;
        if (infoList.size() > 0)
        {
            for (Map<String, Object> map : infoList)
            {
                int isAvailable = Integer.valueOf(map.get("isAvailable") + "").intValue();
                int isDisplay = Integer.valueOf(map.get("isDisplay") + "").intValue();
                map.put("availableDesc", isAvailable == 1 ? "可用" : "停用");
                map.put("displayDesc", isDisplay == 1 ? "可见" : "不可见");
                map.put("index", map.get("id"));
            }
            total = categoryRegionDao.countCustomRegion(para);
        }
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public int saveOrUpdateCustonRegion(Map<String, Object> para)
        throws Exception
    {
        int id = (int)para.get("id");
        if (id == 0)
        {
            int sequence = categoryRegionDao.findMaxCustomRegionSequence();
            para.put("sequence", sequence);
            return categoryRegionDao.saveCustomRegion(para);
        }
        else
        {
            return categoryRegionDao.updateCustomRegion(para);
        }
    }
    
    @Override
    public int updateCustomRegionSequence(Map<String, Object> para)
        throws Exception
    {
        return categoryRegionDao.updateCustomRegion(para);
    }
    
    @Override
    public int updateCustomRegionAvailableStatus(Map<String, Object> para)
        throws Exception
    {
        categoryRegionDao.updateCustomRegionAvailableStatus(para);
        return 1;
    }
    
    @Override
    public int updateCustomRegionDisplayStatus(Map<String, Object> para)
        throws Exception
    {
        categoryRegionDao.updateCustomRegionDisplayStatus(para);
        return 1;
    }
    
    @Override
    public Map<String, Object> findCustomRegionById(int regionId)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", regionId);
        para.put("start", 0);
        para.put("max", 1);
        List<Map<String, Object>> infoList = categoryRegionDao.findAllCustomRegion(para);
        if (infoList == null || infoList.size() == 0)
            return null;
        return infoList.get(0);
    }
    
    @Override
    public Map<String, Object> jsonCustomLayoutInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, Object>> infoList = categoryRegionDao.findAllCustomLayout(para);
        int total = 0;
        if (infoList.size() > 0)
        {
            for (Map<String, Object> map : infoList)
            {
                int isDisplay = Integer.valueOf(map.get("isDisplay") + "").intValue();
                int displayStyle = Integer.valueOf(map.get("displayType") + "").intValue();
                map.put("displayDesc", isDisplay == 1 ? "展现" : "不展现");
                map.put("index", map.get("id"));
                if (displayStyle == CustomLayoutStyleTypeEnum.LAYOUT_STYLE_OF_SINGLE.ordinal())
                {
                    map.put("layout", "一行 1 张");
                    map.put("oneRemark", map.get("oneRemark"));
                    map.put("twoRemark", "-");
                    map.put("threeRemark", "-");
                    map.put("fourRemark", "-");
                }
                else if (displayStyle == CustomLayoutStyleTypeEnum.LAYOUT_STYLE_OF_DOUBLE.ordinal())
                {
                    map.put("layout", "一行 2 张");
                    map.put("oneRemark", map.get("oneRemark"));
                    map.put("twoRemark", map.get("twoRemark"));
                    map.put("threeRemark", "-");
                    map.put("fourRemark", "-");
                }
                else if (displayStyle == CustomLayoutStyleTypeEnum.LAYOUT_STYLE_OF_FOUR.ordinal())
                {
                    map.put("layout", "一行 4 张");
                    map.put("oneRemark", map.get("oneRemark"));
                    map.put("twoRemark", map.get("twoRemark"));
                    map.put("threeRemark", map.get("threeRemark"));
                    map.put("fourRemark", map.get("fourRemark"));
                }
            }
            total = categoryRegionDao.countCustomLayout(para);
        }
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public int updateCustomLayoutDisplayStatus(Map<String, Object> para)
        throws Exception
    {
        categoryRegionDao.updateCustomLayoutDisplayStatus(para);
        return 1;
    }
    
    @Override
    public int updateCustomLayoutSequence(Map<String, Object> para)
        throws Exception
    {
        return categoryRegionDao.updateCustomLayoutSequence(para);
    }
    
    @Override
    public int saveOrUpdateCustomLayout(Map<String, Object> para)
        throws Exception
    {
    	Page2ModelCustomLayoutEntity customLayout = (Page2ModelCustomLayoutEntity)para.get("customLayout");
        Map<String, Object> oneImageWidthAndHeight = ImageUtil.getProperty(customLayout.getOneImage());
        customLayout.setOneWidth(Integer.valueOf(oneImageWidthAndHeight.get("width") + ""));
        customLayout.setOneHeight(Integer.valueOf(oneImageWidthAndHeight.get("height") + ""));
        
        if (customLayout.getDisplayType() == CustomLayoutStyleTypeEnum.LAYOUT_STYLE_OF_DOUBLE.ordinal()
            || customLayout.getDisplayType() == CustomLayoutStyleTypeEnum.LAYOUT_STYLE_OF_FOUR.ordinal())
        {
            Map<String, Object> twoImageWidthAndHeight = ImageUtil.getProperty(customLayout.getTwoImage());
            customLayout.setTwoWidth(Integer.valueOf(twoImageWidthAndHeight.get("width") + ""));
            customLayout.setTwoHeight(Integer.valueOf(twoImageWidthAndHeight.get("height") + ""));
        }
        else
        {
            customLayout.setTwoWidth(0);
            customLayout.setTwoHeight(0);
        }
        
        if (customLayout.getDisplayType() == CustomLayoutStyleTypeEnum.LAYOUT_STYLE_OF_FOUR.ordinal())
        {
            Map<String, Object> threeImageWidthAndHeight = ImageUtil.getProperty(customLayout.getThreeImage());
            customLayout.setThreeWidth(Integer.valueOf(threeImageWidthAndHeight.get("width") + ""));
            customLayout.setThreeHeight(Integer.valueOf(threeImageWidthAndHeight.get("height") + ""));
            
            Map<String, Object> fourImageWidthAndHeight = ImageUtil.getProperty(customLayout.getFourImage());
            customLayout.setFourWidth(Integer.valueOf(fourImageWidthAndHeight.get("width") + ""));
            customLayout.setFourHeight(Integer.valueOf(fourImageWidthAndHeight.get("height") + ""));
        }
        else
        {
            customLayout.setThreeWidth(0);
            customLayout.setThreeHeight(0);
            customLayout.setFourWidth(0);
            customLayout.setFourHeight(0);
        }
        
        if (customLayout.getId() == 0)
        {
//            int regionId = (int)para.get("regionId");
//            int sequence = categoryRegionDao.getCustonLayoutMaxSequence(regionId);
        	return categoryRegionDao.addCustomLayout(customLayout);
//            para.put("layoutId", customLayout.getId());
//            para.put("sequence", sequence);
//            return categoryRegionDao.insertRelationCustomRegionLayout(para);
            
        }
        else
        {
            return categoryRegionDao.updateCustomLayout(customLayout);
        }
    }
    
    @Override
    public Map<String, Object> findCustomLayoutBydId(Map<String, Object> para)
        throws Exception
    {
        para.put("start", 0);
        para.put("max", 1);
        List<Map<String, Object>> infoList = categoryRegionDao.findAllCustomLayout(para);
        if (infoList == null || infoList.size() == 0)
            return null;
        else
            return infoList.get(0);
    }
    
    @Override
    public String deleteCustomLayout(int id)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (categoryRegionDao.deleteCustomLayout(id) > 0)
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
}
