
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
package com.ygg.admin.dao.categoryggrecommend;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.CustomGGRecommendEntity;

/**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: CategoryGGRecommendDao.java 8560 2016-03-09 07:44:57Z zhanglide $   
  * @since 2.0
  */
public interface CategoryGGRecommendDao {
	/**
     * 查询推荐列表
     * @param param
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findRecommendListInfo(Map<String, Object> param)
        throws Exception;
    
    /**
     * 统计条数
     * @param param
     * @return
     * @throws Exception
     */
    int countRecommendByParam(Map<String, Object> param)
        throws Exception;
    
    /**
     * 新增推荐
     * @param param
     * @return
     * @throws Exception
     */
    int saveRecommend(Map<String, Object> param)
        throws Exception;
    
    /**
     * 更新推荐
     * @param param
     * @return
     * @throws Exception
     */
    int updateRecommend(Map<String, Object> param)
        throws Exception;
    
    CustomGGRecommendEntity findCustomGGRecommendById(int id)
        throws Exception;
}
