
 /**************************************************************************
 * Copyright (c) 2014-2016 浙江格家网络技术有限公司.
 * All rights reserved.
 * 
 * 项目名称：左岸城堡APP
 * 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.ygg.admin.service.hotlist.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ygg.admin.dao.hotlist.SaleBrandDao;
import com.ygg.admin.entity.hotlist.SelaBrandEntity;
import com.ygg.admin.service.hotlist.SaleBrandService;

/**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: SaleBrandServiceImpl.java 9259 2016-03-25 08:23:15Z zhanglide $   
  * @since 2.0
  */
@Service("saleBrandService")
public class SaleBrandServiceImpl implements SaleBrandService {
	/**    */
	@Autowired(required=false)
	@Qualifier("saleBrandDao")
	private SaleBrandDao saleBrandDao;
	
	@Override
	public Map<String, Object> findListInfo(Map<String, Object> param) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> infoList = saleBrandDao.findListInfo(param);
		if (infoList != null && infoList.size() > 0){
            for (Map<String, Object> map : infoList){
            	String s = String.valueOf(map.get("image")+"");
            	if (StringUtils.isNotBlank(s)){
            		map.put("image", "<img style='max-width:100px' src='" + s + "' />");
                }else{
                	 map.put("image", "");
                }
            	String headImage = String.valueOf(map.get("headImage")+"");
            	if (StringUtils.isNotBlank(headImage)){
            		map.put("headImage", "<img style='max-width:100px' src='" + headImage + "' />");
            		map.put("hImage", headImage);
                }else{
                	 map.put("headImage", "");
                	 map.put("hImage", "");
                }
            }
        }
		result.put("rows", infoList);
		result.put("total", saleBrandDao.getCountByParam(param));
		return result;
	}
	
	
	@Override
	public int saveOrUpdateInfo(Map<String, Object> param) throws Exception {
		Object id = param.get("id");
		if(id == null || StringUtils.isBlank(id.toString())) {
			return saleBrandDao.save(param);
		} else {
//			Object isDisplay = param.get("isDisplay");
//			if(isDisplay == null){
//				int ids = Integer.valueOf(id + "").intValue();
//				Map<String, Object> map = findByIdOrProductBaseId(ids,-1);
//				int dbDisplaySales = Integer.valueOf(map.get("displaySales")+"").intValue();
//				int artificialIncrement = Integer.valueOf(param.get("artificialIncrement")+"").intValue();
//				param.put("displaySales", dbDisplaySales+artificialIncrement);
//			}
			return saleBrandDao.update(param);
		}
	}
	
	@Override
	public Map<String, Object> findByIdOrProductBaseId(int id,int productBaseId) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		if(id != -1){
			param.put("id", id);
		}
		if(productBaseId != -1){
			param.put("productBaseId", productBaseId);
		}
		List<Map<String, Object>> list = saleBrandDao.findListInfo(param);
		if(list != null && list.size() > 0)
			return list.get(0);
		return new HashMap<String, Object>();
	}
	 @Override
	 public int delete(int id) throws Exception{
       return saleBrandDao.delete(id);
   }
	 
	 public int getCount(Map<String, Object> para)throws Exception{
		 return saleBrandDao.getCountByParam(para);
	 }
	 
	 /**
	 * 根据基本商品id获取符合要求的热卖单品
	 * @param productBaseId
	 * @return
	 * @throws Exception
	 */
	public SelaBrandEntity getBrandInfo(Map<String, Object> para)throws Exception{
		return saleBrandDao.getBrandInfo(para);
	}
}
