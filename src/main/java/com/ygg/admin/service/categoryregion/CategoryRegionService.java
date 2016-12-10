
 /**************************************************************************
 * Copyright (c) 2014-2016 浙江格家网络技术有限公司.
 * All rights reserved.
 * 
 * 项目名称：左岸城堡APP
 * 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.ygg.admin.service.categoryregion;

import java.util.Map;

/**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: CategoryRegionService.java 8560 2016-03-09 07:44:57Z zhanglide $   
  * @since 2.0
  */
public interface CategoryRegionService {
	Map<String, Object> jsonCustomRegionInfo(Map<String, Object> para)
	        throws Exception;
	    
	    int saveOrUpdateCustonRegion(Map<String, Object> para)
	        throws Exception;
	    
	    int updateCustomRegionSequence(Map<String, Object> para)
	        throws Exception;
	    
	    int updateCustomRegionDisplayStatus(Map<String, Object> para)
	        throws Exception;
	    
	    int updateCustomRegionAvailableStatus(Map<String, Object> para)
	        throws Exception;
	    
	    Map<String, Object> findCustomRegionById(int regionId)
	        throws Exception;
	    
	    Map<String, Object> jsonCustomLayoutInfo(Map<String, Object> para)
	        throws Exception;
	    
	    int updateCustomLayoutDisplayStatus(Map<String, Object> para)
	        throws Exception;
	    
	    int updateCustomLayoutSequence(Map<String, Object> para)
	        throws Exception;
	    
	    int saveOrUpdateCustomLayout(Map<String, Object> para)
	        throws Exception;
	    
	    Map<String, Object> findCustomLayoutBydId(Map<String, Object> para)
	        throws Exception;
	    
	    String deleteCustomLayout(int id)
	        throws Exception;
}
