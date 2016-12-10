
 /**************************************************************************
 * Copyright (c) 2014-2016 浙江格家网络技术有限公司.
 * All rights reserved.
 * 
 * 项目名称：左岸城堡APP
 * 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.ygg.admin.service.categoryggrecommend.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.ygg.admin.dao.categoryggrecommend.CategoryGGRecommendDao;
import com.ygg.admin.service.categoryggrecommend.CategoryGGRecommendService;
import com.ygg.admin.util.ImageUtil;

/**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: CategoryGGRecommendServiceImpl.java 9207 2016-03-24 09:17:08Z zhangyibo $   
  * @since 2.0
  */
@Service("categoryGGRecommendService")
public class CategoryGGRecommendServiceImpl implements CategoryGGRecommendService {
	
	@Resource(name="categoryGGRecommendDao")
	private CategoryGGRecommendDao categoryGGRecommendDao;
	
	@Override
	public Map<String, Object> findRecommendListInfo(Map<String, Object> param) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rows", categoryGGRecommendDao.findRecommendListInfo(param));
		result.put("total", categoryGGRecommendDao.countRecommendByParam(param));
		return result;
	}

	@Override
	public int saveOrUpdateRecommend(Map<String, Object> param) throws Exception {
		Object id = param.get("id");
		Object displayType = param.get("displayType");
		if(displayType != null && StringUtils.equals("1", displayType.toString())) {
			displayType1(param);
		}
		if(displayType != null && StringUtils.equals("2", displayType.toString())) {
			displayType2(param);
		}
		if(id == null || StringUtils.isBlank(id.toString())) {
			return categoryGGRecommendDao.saveRecommend(param);
		} else {
			return categoryGGRecommendDao.updateRecommend(param);
		}
	}
	
	private void displayType2(Map<String, Object> param) {
		Object oneType = param.get("oneType");
		Map<String, Object> propertysMap = ImageUtil.getProperty((String)param.get("oneImage"));
		short oneWidth = Short.valueOf((String)propertysMap.get("width"));
		param.put("oneHeight", Short.valueOf((String)propertysMap.get("height")));
		param.put("oneWidth", oneWidth);
		
		short twoWidth = Short.valueOf((String)propertysMap.get("width"));
		propertysMap = ImageUtil.getProperty((String)param.get("twoImage"));
		param.put("twoHeight", Short.valueOf((String)propertysMap.get("height")));
		param.put("twoWidth", twoWidth);
		
		if(StringUtils.equals("2", oneType.toString())) {
			param.put("oneDisplayId", param.get("oneActivitiesCommonId"));
		} else if(StringUtils.equals("3", oneType.toString())) {
			param.put("oneDisplayId", param.get("oneActivitiesCustomId"));
		} else if(StringUtils.equals("7", oneType.toString())) {
			param.put("oneDisplayId", param.get("onePageId"));
		}
		Object twoType = param.get("twoType");
		if(StringUtils.equals("2", twoType.toString())) {
			param.put("twoDisplayId", param.get("twoActivitiesCommonId"));
		} else if(StringUtils.equals("3", twoType.toString())) {
			param.put("twoDisplayId", param.get("twoActivitiesCustomId"));
		} else if(StringUtils.equals("7", twoType.toString())) {
			param.put("twoDisplayId", param.get("twoPageId"));
		}
	}
	
	private void displayType1(Map<String, Object> param) {
		Object oneType = param.get("oneType");
		Map<String, Object> propertysMap = ImageUtil.getProperty((String)param.get("oneImage"));
		short oneWidth = Short.valueOf((String)propertysMap.get("width"));
		param.put("oneHeight", Short.valueOf((String)propertysMap.get("height")));
		param.put("oneWidth", oneWidth);
		if(StringUtils.equals("2", oneType.toString())) {
			param.put("oneDisplayId", param.get("oneActivitiesCommonId"));
		} else if(StringUtils.equals("3", oneType.toString())) {
			param.put("oneDisplayId", param.get("oneActivitiesCustomId"));
		} else if(StringUtils.equals("7", oneType.toString())) {
			param.put("oneDisplayId", param.get("onePageId"));
		}
		param.put("twoHeight", 0);
		param.put("twoWidth", 0);
		param.put("twoType", 0);
		param.put("twoDisplayId", 0);
		param.put("twoRemark", "");
	}

	@Override
	public Map<String, Object> findById(int id) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		List<Map<String, Object>> list = categoryGGRecommendDao.findRecommendListInfo(param);
		if(list != null && list.size() > 0)
			return list.get(0);
		return new HashMap<String, Object>();
	}
}
