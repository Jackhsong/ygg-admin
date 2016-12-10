
 /**************************************************************************
 * Copyright (c) 2014-2016 浙江格家网络技术有限公司.
 * All rights reserved.
 * 
 * 项目名称：左岸城堡APP
 * 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.ygg.admin.service.categorymanager;

import java.util.Map;

/**
  * 品类馆管理服务接口
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: CateGoryManagerService.java 8654 2016-03-10 10:04:07Z zhanglide $   
  * @since 2.0
  */
public interface CateGoryManagerService {
    
    /**
     * 查找所有Page数据
     * @param para 参数
     * @return Map<String, Object>
     * @throws Exception 异常时
     */
    Map<String, Object> findAllPageByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 新增or更新
     * @param para
     * @return 操作成功标示
     * @throws Exception 异常时
     */
    int insertOrUpdatePage(Map<String,Object> para)
        throws Exception;
    
    /**
     * 根据主键查找
     * @param id 主键
     * @return Map<String, Object>
     * @throws Exception 异常时
     */
    public Map<String, Object> findPageById(int id)
            throws Exception;
    
    /**
     * 查找PageModel
     * @param para 参数
     * @return Map<String, Object>
     * @throws Exception 异常时
     */
    public Map<String, Object> findPageModelByPara(Map<String, Object> para)
            throws Exception;
    
    /**
     * 新增or更新
     * @param para
     * @return 操作成功标示
     * @throws Exception 异常时
     */
    public int insertOrUpdatePageModel(Map<String, Object> para)
	        throws Exception;
}
