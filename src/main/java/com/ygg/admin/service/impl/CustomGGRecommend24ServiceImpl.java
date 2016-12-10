package com.ygg.admin.service.impl;

import com.ygg.admin.dao.CustomGGRecommend24Dao;
import com.ygg.admin.service.CustomGGRecommend24Service;
import com.ygg.admin.util.ImageUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("customGGRecommend24Service")
public class CustomGGRecommend24ServiceImpl implements CustomGGRecommend24Service {

	@Resource
	private CustomGGRecommend24Dao customGGRecommend24Dao;
	
	@Override
	public Map<String, Object> findRecommendListInfo(Map<String, Object> param) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rows", customGGRecommend24Dao.findRecommendListInfo(param));
		result.put("total", customGGRecommend24Dao.countRecommendByParam(param));
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
			return customGGRecommend24Dao.saveRecommend(param);
		} else {
			return customGGRecommend24Dao.updateRecommend(param);
		}
	}
	
	private void displayType2(Map<String, Object> param) {
		Object oneType = param.get("oneType");
		Map<String, Object> propertysMap = ImageUtil.getProperty((String)param.get("oneImage"));
		short oneWidth = Short.valueOf((String)propertysMap.get("width"));
//		if(oneWidth != 350)
//			throw new RuntimeException("一行展示2个图片时，只支持宽度为350px的图片");
		param.put("oneHeight", Short.valueOf((String)propertysMap.get("height")));
		param.put("oneWidth", oneWidth);
		
		short twoWidth = Short.valueOf((String)propertysMap.get("width"));
//		if(twoWidth != 350)
//			throw new RuntimeException("一行展示2个图片时，只支持宽度为350px的图片");
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
//		if(oneWidth != 710)
//			throw new RuntimeException("一行展示1个图片时，只支持宽度为710px的图片");
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
		List<Map<String, Object>> list = customGGRecommend24Dao.findRecommendListInfo(param);
		if(list != null && list.size() > 0)
			return list.get(0);
		return new HashMap<String, Object>();
	}

}
