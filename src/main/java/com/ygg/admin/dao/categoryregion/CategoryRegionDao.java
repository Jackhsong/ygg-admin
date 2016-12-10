
 /**************************************************************************
 * Copyright (c) 2014-2016 浙江格家网络技术有限公司.
 * All rights reserved.
 * 
 * 项目名称：左岸城堡
APP
 * 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.ygg.admin.dao.categoryregion;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.categoryregion.Page2ModelCustomLayoutEntity;
import com.ygg.admin.entity.categoryregion.Page2ModelCustomRegionEntity;

/**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: CategoryRegionDao.java 8560 2016-03-09 07:44:57Z zhanglide $   
  * @since 2.0
  */
public interface CategoryRegionDao {
	List<Map<String, Object>> findAllCustomRegion(Map<String, Object> para)
	        throws Exception;
	    
	    int countCustomRegion(Map<String, Object> para)
	        throws Exception;
	    
	    int saveCustomRegion(Map<String, Object> para)
	        throws Exception;
	    
	    int updateCustomRegion(Map<String, Object> para)
	        throws Exception;
	    
	    int updateCustomRegionAvailableStatus(Map<String, Object> para)
	        throws Exception;
	    
	    int updateCustomRegionDisplayStatus(Map<String, Object> para)
	        throws Exception;
	    
	    int findMaxCustomRegionSequence()
	        throws Exception;
	    
	    List<Map<String, Object>> findAllCustomLayout(Map<String, Object> para)
	        throws Exception;
	    
	    int countCustomLayout(Map<String, Object> para)
	        throws Exception;
	    
	    int updateCustomLayoutDisplayStatus(Map<String, Object> para)
	        throws Exception;
	    
	    int updateCustomLayoutSequence(Map<String, Object> para)
	        throws Exception;
	    
	    int addCustomLayout(Page2ModelCustomLayoutEntity customLayout)
	        throws Exception;
	    
	    int updateCustomLayout(Page2ModelCustomLayoutEntity customLayout)
	        throws Exception;
	    
	    int getCustonLayoutMaxSequence(int regionId)
	        throws Exception;
	    
	    int insertRelationCustomRegionLayout(Map<String, Object> para)
	        throws Exception;
	    
	    int deleteCustomLayout(int id)
	        throws Exception;
	    
	    Page2ModelCustomLayoutEntity findCustomLayoutById(int customLayoutId)
	        throws Exception;
	    
	    Page2ModelCustomRegionEntity findCustomRegionById(int id)
	        throws Exception;
	    
	    List<Integer> findCustomLayoutIdByCustomRegionId(int id)
	        throws Exception;
}
