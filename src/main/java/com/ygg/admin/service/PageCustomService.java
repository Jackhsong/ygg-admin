package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.PageCustomEntity;

public interface PageCustomService
{
    
    /**
     * 查询可用自定义页面简略数据
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<PageCustomEntity> findAllPageCustomForProduct()
        throws Exception;
    
    /**
     * 根据ID查找自定义页面
     * 
     * @param id
     * @return
     * @throws Exception
     */
    
    PageCustomEntity findPageCustomById(int id)
        throws Exception;
    
    int save(PageCustomEntity entity)
        throws Exception;
    
    int update(PageCustomEntity para)
        throws Exception;
    
    int countPageCustom(Map<String, Object> para)
        throws Exception;
    
    List<PageCustomEntity> findPageCustom(Map<String, Object> para)
        throws Exception;
    
    /**
     * 将文件保存到硬盘
     * 
     * @param entity
     * @return
     * @throws Exception
     */
    int write(PageCustomEntity entity)
        throws Exception;
    
}
