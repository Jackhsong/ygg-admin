
 /**************************************************************************
 * Copyright (c) 2014-2016 浙江格家网络技术有限公司.
 * All rights reserved.
 * 
 * 项目名称：左岸城堡APP
 * 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.ygg.admin.service.qqbswhitelist;

import java.util.Map;

/**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: QqbsWhiteListService.java 12406 2016-05-20 03:48:16Z zhanglide $   
  * @since 2.0
  */
public interface QqbsWhiteListService
{
    /**
     * 查询推荐列表
     * @param param
     * @return Map<String, Object>
     * @throws Exception
     */
    Map<String, Object> findListInfo(Map<String, Object> param);
    
    
    /**
     * 更新
     * @param id
     * @param type
     */
    public void updateLog(int id , int type);
    
    /**
     * @param param
     */
    public void save(Map<String, Object> param);
    /**
     * 查询用户用没用添加
     * @param param
     * @return
     */
    public int getCountByParam(Map<String, Object> param);
}
