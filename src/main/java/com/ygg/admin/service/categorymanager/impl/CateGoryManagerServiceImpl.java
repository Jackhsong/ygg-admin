
 /**************************************************************************
 * Copyright (c) 2014-2016 浙江格家网络技术有限公司.
 * All rights reserved.
 * 
 * 项目名称：左岸城堡APP
 * 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.ygg.admin.service.categorymanager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ygg.admin.code.IndexNavigationEnum;
import com.ygg.admin.dao.categorymanager.CateGoryManagerDao;
import com.ygg.admin.entity.categorymanager.Page2ModelEntity;
import com.ygg.admin.service.categorymanager.CateGoryManagerService;

/**
  * 品类馆管理服务
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: CateGoryManagerServiceImpl.java 8654 2016-03-10 10:04:07Z zhanglide $   
  * @since 2.0
  */
@Service("cateGoryManagerService")
public class CateGoryManagerServiceImpl implements CateGoryManagerService {
	
     /**品类馆管理Dao接口*/
    @Resource(name="cateGoryManagerDao")
    private CateGoryManagerDao cateGoryManagerDao;
    
    
    
    /**
     * @param para 参数
     * @return Map<String, Object>
     * @throws Exception 异常时
     * @see com.ygg.admin.service.categorymanager.CateGoryManagerService#findAllPageByPara(java.util.Map)
     */
    @Override
    public Map<String, Object> findAllPageByPara(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> pageList = cateGoryManagerDao.findAllPageByPara(para);
        int total = 0;
        if (!pageList.isEmpty()){
            total = cateGoryManagerDao.countAllPageByPara(para);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("rows", pageList);
        result.put("total", total);
        return result;
    }
    
	
	/**
	 * @param para
     * @return 操作成功标示
     * @throws Exception 异常时
	 * @see com.ygg.admin.service.categorymanager.CateGoryManagerService#insertOrUpdatePage(java.util.Map)
	 */
	@Override
    public int insertOrUpdatePage(Map<String, Object> para)
        throws Exception{
        int id = Integer.valueOf(para.get("id") + "");
        int status = 0;
        if (id == 0){
        	para.put("id", "0");
            status = cateGoryManagerDao.insertPage(para);
            for(int i=1;i<7;i++){
            	para.put("type", i);
            	para.put("display", "1");
            	para.put("sequence", (7-i));
            	cateGoryManagerDao.insertPageModel(para);
            }
        }
        else{
            status = cateGoryManagerDao.updatePageById(para);
        }
        return status;
    }
	 
	/**
	 * @param id 主键
     * @return Map<String, Object>
     * @throws Exception 异常时
	 * @see com.ygg.admin.service.categorymanager.CateGoryManagerService#findPageById(int)
	 */
	@Override
    public Map<String, Object> findPageById(int id)
        throws Exception
    {
        return cateGoryManagerDao.findPageById(id);
    }
	 
	 
	/**
	 * @param para 参数
     * @return Map<String, Object>
     * @throws Exception 异常时
	 * @see com.ygg.admin.service.categorymanager.CateGoryManagerService#findPageModelByPara(java.util.Map)
	 */
	@Override
    public Map<String, Object> findPageModelByPara(Map<String, Object> para)
        throws Exception
    {
        List<Page2ModelEntity> pmeList = cateGoryManagerDao.findAllPageModelByPara(para);
        List<Map<String, Object>> rows = new ArrayList<>();
        for (Page2ModelEntity model : pmeList)
        {
            Map<String, Object> row = new HashMap<>();
            row.put("id", model.getId() + "");
            row.put("displayStatus", model.getIsDisplay() == 1 ? "展示" : "不展示");
            row.put("sequence", model.getSequence() + "");
            row.put("typeStr", IndexNavigationEnum.PageModelType.parse(model.getType()).getTitle());
            row.put("type", model.getType());
            row.put("isDisplay", model.getIsDisplay());
            rows.add(row);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("total", rows.size());
        result.put("rows", rows);
        return result;
    }
	 
	
	/**
     * @param para
     * @return 操作成功标示
     * @throws Exception 异常时
	 * @see com.ygg.admin.service.categorymanager.CateGoryManagerService#insertOrUpdatePageModel(java.util.Map)
	 */
	@Override
	public int insertOrUpdatePageModel(Map<String, Object> para)
	    throws Exception{
	    int id = Integer.valueOf(para.get("id") + "");
	    int status = 0;
	    if (id == 0){
	        status = cateGoryManagerDao.insertPageModel(para);
	    }
	    else{
	        status = cateGoryManagerDao.updatePageModelById(para);
	    }
	    return status;
	}
	 
}
