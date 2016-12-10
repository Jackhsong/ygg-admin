
 /**************************************************************************
 * Copyright (c) 2014-2016 浙江格家网络技术有限公司.
 * All rights reserved.
 * 
 * 项目名称：左岸城堡APP
 * 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.ygg.admin.service.categoryclassification.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ygg.admin.code.IndexNavigationEnum.CategoryClassificationType;
import com.ygg.admin.dao.categoryclassification.CategoryClassificationDao;
import com.ygg.admin.service.categoryclassification.CategoryClassificationService;
import com.ygg.admin.util.ImageUtil;

/**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: CategoryClassificationServiceImpl.java 8713 2016-03-11 07:23:46Z zhanglide $   
  * @since 2.0
  */
@Service("categoryClassificationService")
public class CategoryClassificationServiceImpl implements CategoryClassificationService {
	
	@Autowired(required=false)
	@Qualifier("categoryClassificationDao")
	private CategoryClassificationDao categoryClassificationDao;
	
	
	@Override
	public Map<String, Object> findListInfo(Map<String, Object> param) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		
		
		List<Map<String, Object>> infoList = categoryClassificationDao.findListInfo(param);
		if (infoList.size() > 0){
            for (Map<String, Object> map : infoList){
                map.put("imageURL", "<a href='" + map.get("image") + "' target='_blank'>查看图片</a>");
                CategoryClassificationType cct = CategoryClassificationType.parse(String.valueOf(map.get("type")));
                if(cct != null){
                	 map.put("typeName", cct.getTitle());
                }
            }
        }
		result.put("rows", infoList);
		result.put("total", categoryClassificationDao.getCountByParam(param));
		return result;
	}
	
	
	@Override
	public int saveOrUpdateInfo(Map<String, Object> param) throws Exception {
		Object id = param.get("id");
		Object type = param.get("type");
		String thirdCategoryId = (String) param.get("thirdCategoryId");
		if(thirdCategoryId == ""){
			param.put("thirdCategoryId","0");
		}
		if(type != null){
			displayType(param);
		}
		if(id == null || StringUtils.isBlank(id.toString())) {
			return categoryClassificationDao.save(param);
		} else {
			return categoryClassificationDao.update(param);
		}
	}
	
	
	private void displayType(Map<String, Object> param) {
		Object type = param.get("type");
		Map<String, Object> propertysMap = ImageUtil.getProperty((String)param.get("image"));
		short width = Short.valueOf((String)propertysMap.get("width"));
		param.put("height", Short.valueOf((String)propertysMap.get("height")));
		param.put("width", width);
		if(StringUtils.equals("1", type.toString())) {
			param.put("displayId", param.get("oneActivitiesCommonId"));
			param.put("firstCategoryId", "0");
			param.put("secondCategoryId", "0");
			param.put("thirdCategoryId", "0");
		} else if(StringUtils.equals("2", type.toString())) {
			param.put("displayId", param.get("oneActivitiesCustomId"));
			param.put("firstCategoryId", "0");
			param.put("secondCategoryId", "0");
			param.put("thirdCategoryId", "0");
		} else if(StringUtils.equals("3", type.toString())) {
			param.put("displayId", param.get("onePageId"));
			param.put("firstCategoryId", "0");
			param.put("secondCategoryId", "0");
			param.put("thirdCategoryId", "0");
		}else if(StringUtils.equals("4", type.toString())){
			param.put("displayId", "0");
			param.put("firstCategoryId", param.get("firstCategoryId"));
			param.put("secondCategoryId", param.get("secondCategoryId"));
			param.put("thirdCategoryId", param.get("thirdCategoryId"));
		}
	}
	
	@Override
	public Map<String, Object> findById(int id) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		List<Map<String, Object>> list = categoryClassificationDao.findListInfo(param);
		if(list != null && list.size() > 0)
			return list.get(0);
		return new HashMap<String, Object>();
	}
	 @Override
	 public int delete(int id) throws Exception{
        return categoryClassificationDao.delete(id);
    }
}
