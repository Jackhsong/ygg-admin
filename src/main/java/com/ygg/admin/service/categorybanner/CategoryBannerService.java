
 /**************************************************************************
 * Copyright (c) 2014-2016 浙江格家网络技术有限公司.
 * All rights reserved.
 * 
 * 项目名称：左岸城堡APP
 * 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.ygg.admin.service.categorybanner;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.categorybanner.CategoryBannerEntity;

/**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: CategoryBannerService.java 8535 2016-03-08 12:45:02Z zhanglide $   
  * @since 2.0
  */
public interface CategoryBannerService {
	int save(CategoryBannerEntity window)
	        throws Exception;
	    
	    int update(CategoryBannerEntity window)
	        throws Exception;
	    
	    int countBannerWindow(Map<String, Object> para)
	        throws Exception;
	    
	    List<CategoryBannerEntity> findAllBannerWindow(Map<String, Object> para)
	        throws Exception;
	    
	    CategoryBannerEntity findBannerWindowById(int id)
	        throws Exception;
	    
	    int updateDisplayCode(Map<String, Object> para)
	        throws Exception;
	    
	    List<Map<String, Object>> packageBannerWindowList(List<CategoryBannerEntity> bList)
	        throws Exception;
	    
	    /**
	     * 更新排序值
	     * 
	     * @param para
	     * @return
	     * @throws Exception
	     */
	    int updateOrder(Map<String, Object> para)
	        throws Exception;
	    
	    boolean checkIsExist(Map<String, Object> para)
	        throws Exception;
	    
	    Map<String, Object> checkProductTime(Map<String, Object> para)
	        throws Exception;
}
