
 /**************************************************************************
 * Copyright (c) 2014-2016 浙江格家网络技术有限公司.
 * All rights reserved.
 * 
 * 项目名称：左岸城堡APP
 * 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.ygg.admin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ygg.admin.code.CustomLayoutRelationTypeEnum;
import com.ygg.admin.code.ProductEnum;
import com.ygg.admin.dao.ProductDao;
import com.ygg.admin.entity.ProductEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ygg.admin.dao.welfare.CustomGeGeWelfareDao;
import com.ygg.admin.service.WelfareGroupService;
import com.ygg.admin.util.ImageUtil;

import javax.annotation.Resource;

 /**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: WelfareGroupServiceImpl.java 9751 2016-04-06 07:56:30Z zhangyibo $   
  * @since 2.0
  */
@Service("welfareGroupService")
public class WelfareGroupServiceImpl implements WelfareGroupService {
	
	 /**格格福利团Dao */
	@Autowired(required=false)
	@Qualifier("customGeGeWelfareDao")
	private CustomGeGeWelfareDao customGeGeWelfareDao;

	 @Resource
	 ProductDao productDao;
	
    @Override
    public Map<String, Object> findListInfo(Map<String, Object> param)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        
        List<Map<String, Object>> infoList = customGeGeWelfareDao.findListInfo(param);
        if (infoList.size() > 0)
        {
            for (Map<String, Object> map : infoList)
            {
                map.put("imageURL", "<a href='" + map.get("image") + "' target='_blank'>查看图片</a>");
            }
        }
        result.put("rows", infoList);
        result.put("total", customGeGeWelfareDao.getCountByParam(param));
        return result;
    }
	
	
	@Override
	public int saveOrUpdateInfo(Map<String, Object> param) throws Exception {
		Object id = param.get("id");
		Object type = param.get("type");
		if(type != null){
			displayType1(param);
		}
		if(id == null || StringUtils.isBlank(id.toString())) {
			return customGeGeWelfareDao.save(param);
		} else {
			return customGeGeWelfareDao.update(param);
		}
	}
	
	
	private void displayType1(Map<String, Object> param) throws Exception {
		Object type = param.get("type");
		Map<String, Object> propertysMap = ImageUtil.getProperty((String)param.get("image"));
		short width = Short.valueOf((String)propertysMap.get("width"));
		param.put("height", Short.valueOf((String)propertysMap.get("height")));
		param.put("width", width);
		if(StringUtils.equals("1", type.toString())) {
			int productId = Integer.valueOf(param.get("oneProductId") + "");
			ProductEntity product = productDao.findProductByID(productId, null);
			if (product == null)
			{
                throw new RuntimeException("商品(id:" + productId + ")不存在！");
			}
			// 特卖商品，关联的类型=1
			if (product.getType() == ProductEnum.PRODUCT_TYPE.SALE.getCode())
			{
				param.put("type", CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_SALEPRODUCT.ordinal());

			}
			// 商城商品，关联的类型=4
			else if (product.getType() == ProductEnum.PRODUCT_TYPE.MALL.getCode())
			{
				param.put("type", CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_MALLPRODUCT.ordinal());
			}
			param.put("displayId", param.get("oneProductId"));
		} else if(StringUtils.equals("2", type.toString())) {
			param.put("displayId", param.get("oneActivitiesCommonId"));
		} else if(StringUtils.equals("3", type.toString())) {
			param.put("displayId", param.get("oneActivitiesCustomId"));
		} else if(StringUtils.equals("7", type.toString())) {
			param.put("displayId", param.get("onePageId"));
		}
	}
	
	@Override
	public Map<String, Object> findById(int id) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		List<Map<String, Object>> list = customGeGeWelfareDao.findListInfo(param);
		if(list != null && list.size() > 0)
		{
			Map<String, Object> map = list.get(0);
			if ("4".equals(map.get("type") + "")) // 商城商品 --》 单品
			{
				map.put("type", "1");
			}
			return map;

		}
		return new HashMap<String, Object>();
	}
	 @Override
	 public int delete(int id) throws Exception{
        return customGeGeWelfareDao.delete(id);
    }
}
