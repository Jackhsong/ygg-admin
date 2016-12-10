package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.PageCustomEntity;
import com.ygg.admin.entity.RelationProductAndPageCustom;

public interface PageCustomDao
{
    
    int save(PageCustomEntity entity)
        throws Exception;
    
    int update(Map<String, Object> para)
        throws Exception;
    
    int countPageCustom(Map<String, Object> para)
        throws Exception;
    
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
     * 插入商品与自定义页面关联
     * 
     * @return
     * @throws Exception
     */
    int saveRelationProductAndPageCustom(RelationProductAndPageCustom para)
        throws Exception;
    
    /**
     * 更新商品与自定义页面关联
     * 
     * @return
     * @throws Exception
     */
    int updateRelationProductAndPageCustom(RelationProductAndPageCustom para)
        throws Exception;
    
    /**
     * 删除商品与自定义页面关联
     * 
     * @param id
     * @return
     * @throws Exception
     */
    int deleteRelationProductAndPageCustomById(int id)
        throws Exception;
    
    /**
     * 根据para查询自定义页面数据
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<PageCustomEntity> findAllPageCustomByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据ID查找自定义页面
     * 
     * @param id
     * @return
     * @throws Exception
     */
    PageCustomEntity findPageCustomByid(int id)
        throws Exception;
    
    /**
     * 根据para查询商品与自定义页面管理
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<RelationProductAndPageCustom> findRelationProductAndPageCustom(Map<String, Object> para)
        throws Exception;
    
}