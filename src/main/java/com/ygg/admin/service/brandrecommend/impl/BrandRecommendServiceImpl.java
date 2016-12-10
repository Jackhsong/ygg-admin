
/**************************************************************************
* Copyright (c) 2014-2016 浙江格家网络技术有限公司.
* All rights reserved.
* 
* 项目名称：左岸城堡APP
* 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
*           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
*           识产权保护的内容。                            
***************************************************************************/
package com.ygg.admin.service.brandrecommend.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ygg.admin.dao.BrandDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ygg.admin.dao.brandrecommend.BrandRecommendDao;
import com.ygg.admin.service.brandrecommend.BrandRecommendService;
import com.ygg.admin.util.ImageUtil;

import javax.annotation.Resource;

/**
 * 首页品牌推荐服务
 * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
 * @version $Id: BrandRecommendServiceImpl.java 12325 2016-05-19 06:07:54Z zhangyibo $   
 * @since 2.0
 */
@Service("brandRecommendService")
public class BrandRecommendServiceImpl implements BrandRecommendService
{
    
    /**首页品牌推荐Dao接口    */
    @Autowired(required = false)
    @Qualifier("brandRecommendDao")
    private BrandRecommendDao brandRecommendDao;
    
    @Resource
    private BrandDao brandDao;
    
    @Override
    public Map<String, Object> findListInfo(Map<String, Object> param)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        
        List<Map<String, Object>> infoList = brandRecommendDao.findListInfo(param);
        if (infoList.size() > 0)
        {
            for (Map<String, Object> map : infoList)
            {
                map.put("imageURL", "<a href='" + map.get("image") + "' target='_blank'>查看图片</a>");
            }
        }
        result.put("rows", infoList);
        result.put("total", brandRecommendDao.getCountByParam(param));
        return result;
    }
    
    @Override
    public int saveOrUpdateInfo(Map<String, Object> param)
        throws Exception
    {
        Object id = param.get("id");
        Object type = param.get("type");
        if (type != null)
        {
            displayType(param);
        }
        if (id == null || StringUtils.isBlank(id.toString()))
        {
            return brandRecommendDao.save(param);
        }
        else
        {
            return brandRecommendDao.update(param);
        }
    }
    
    private void displayType(Map<String, Object> param)
        throws Exception
    {
		int brandId = Integer.valueOf(param.get("brandId") + "");
		if (brandDao.findBrandById(brandId) == null)
		{
			throw new RuntimeException("品牌不存在！");
		}
        Object type = param.get("type");
        Map<String, Object> propertysMap = ImageUtil.getProperty((String)param.get("image"));
        short width = Short.valueOf((String)propertysMap.get("width"));
        param.put("height", Short.valueOf((String)propertysMap.get("height")));
        param.put("width", width);
        if (StringUtils.equals("2", type.toString()))
        {
            param.put("displayId", param.get("oneActivitiesCommonId"));
        }
        else if (StringUtils.equals("3", type.toString()))
        {
            param.put("displayId", param.get("oneActivitiesCustomId"));
        }
        else if (StringUtils.equals("7", type.toString()))
        {
            param.put("displayId", param.get("onePageId"));
        }
    }
    
    @Override
    public Map<String, Object> findById(int id)
        throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        List<Map<String, Object>> list = brandRecommendDao.findListInfo(param);
        if (list != null && list.size() > 0)
            return list.get(0);
        return new HashMap<String, Object>();
    }
    
    @Override
    public int delete(int id)
        throws Exception
    {
        return brandRecommendDao.delete(id);
    }
}
