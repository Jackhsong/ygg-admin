
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
package com.ygg.admin.dao.categorymanager;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.categorymanager.Page2ModelEntity;

/**
  * 品类馆管理Dao接口
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: CateGoryManagerDao.java 8678 2016-03-11 01:42:35Z zhanglide $   
  * @since 2.0
  */
public interface CateGoryManagerDao {
    
    /**
     * 根据para查询所有自定义拼装页面
     * @param para 查询条件
     * @return List<Map<String, Object>>
     * @throws Exception 异常时
     */
    List<Map<String, Object>> findAllPageByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计自定义拼装页面总数
     * @param para 查询条件
     * @return 总数 
     * @throws Exception 异常时
     */
    int countAllPageByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据id查询自定义拼装页面
     * @param id 主键
     * @return Map<String, Object>
     * @throws Exception 异常时
     */
    Map<String, Object> findPageById(int id)
        throws Exception;
    
    /**
     * 插入自定义拼装页面 
     * @param para 插入字段
     * @return 成功失败标示
     * @throws Exception 异常时
     */
    int insertPage(Map<String, Object> para)
        throws Exception;
    
    
    /**
     * 更新自定义拼装页面信息
     * @param para 更新字段
     * @return 成功失败标示
     * @throws Exception 异常时
     */
    int updatePageById(Map<String, Object> para)
        throws Exception;
    
    /**
     * 插入PageModel
     * @param para 插入字段
     * @return 成功失败标示
     * @throws Exception 异常时
     */
    public int insertPageModel(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查找pageModel
     * @param para 查询条件
     * @return List<Page2ModelEntity>
     * @throws Exception 异常时
     */
    public List<Page2ModelEntity> findAllPageModelByPara(Map<String, Object> para)
            throws Exception;
    
    /**
     * 更新pageModel
     * @param para 更新字段
     * @return 成功失败标示
     * @throws Exception 异常时
     */
    public int updatePageModelById(Map<String, Object> para)
            throws Exception;
    
    /**
     * 根据主键查找PageModel
     * @param id 主键
     * @return Page2ModelEntity
     * @throws Exception 异常时
     */
    public Page2ModelEntity findPageModelById(int id)
            throws Exception;
}
