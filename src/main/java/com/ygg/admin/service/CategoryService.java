package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.CategoryActivityEntity;
import com.ygg.admin.entity.CategoryEntity;
import com.ygg.admin.entity.CategoryFirstEntity;
import com.ygg.admin.entity.CategoryFirstWindowEntity;
import com.ygg.admin.entity.CategorySecondEntity;
import com.ygg.admin.entity.CategoryThirdEntity;

public interface CategoryService
{
    
    CategoryFirstEntity findCategoryFirstById(int firstCategoryId)
        throws Exception;
    
    /**
     * 一级分类列表
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> jsonCategoryFirstInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查找所有一级分类
     * @param para
     * @return
     * @throws Exception
     */
    List<CategoryFirstEntity> findAllCategoryFirst(Map<String, Object> para)
        throws Exception;
    
    /**
     * 更改一级分类状态（是否可用、是否展现）
     * @param para
     * @return
     */
    int updateCategoryFirstStatus(Map<String, Object> para)
        throws Exception;
    
    /**
     * 新增/修改一级分类
     * @param para
     * @return
     */
    int saveOrUpdateCategoryFirst(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改一级分类排序
     * @param para
     * @return
     */
    int updateCategoryFirstSequence(Map<String, Object> para)
        throws Exception;
    
    /**
     * 二级分类列表
     * @param para
     * @return
     */
    Map<String, Object> jsonCategorySecondInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查找所有二级分类
     * @param para
     * @return
     */
    List<CategorySecondEntity> findAllCategorySecond(Map<String, Object> para)
        throws Exception;
    
    /**
     * 
     * @param secondCategoryId
     * @return
     * @throws Exception
     */
    CategorySecondEntity findCategorySecondById(int secondCategoryId)
        throws Exception;
    
    /**
     * 新增/修改二级分类
     * @param para
     * @return
     */
    int saveOrUpdateCategorySecond(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改二级分类可用状态
     * @param para
     * @return
     * @throws Exception
     */
    int updateCategorySecondStatus(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改二级分类排序
     * @param para
     * @return
     * @throws Exception
     */
    int updateCategorySecondSequence(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据添加查找二级分类
     * @param para
     * @return
     */
    CategorySecondEntity findCategorySecondByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 三级分类列表
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> jsonCategoryThirdInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查找所有三级分类信息
     * @param para
     * @return
     * @throws Exception
     */
    List<CategoryThirdEntity> findAllCategoryThird(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据Id查找三级分类信息
     * @param id
     * @return
     * @throws Exception
     */
    CategoryThirdEntity findCategoryThirdById(int id)
        throws Exception;
    
    /**
     * 根据条件查找三级分类
     * @param para
     * @return
     */
    CategoryThirdEntity findCategoryThirdByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 新增/修改三级分类
     * @param para
     * @return
     * @throws Exception
     */
    int saveOrUpdateCategoryThird(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改三级分类展现状态
     * @param para
     * @return
     * @throws Exception
     */
    int updateCategoryThirdStatus(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改三级分类排序
     * @param para
     * @return
     * @throws Exception
     */
    int updateCategoryThirdSequence(Map<String, Object> para)
        throws Exception;
    
    /**
     * 一级分类资源位列表
     * @param para
     * @return
     */
    Map<String, Object> jsonFirstWindowInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据Id查找一级分类资源位
     * @param id
     * @return
     */
    CategoryFirstWindowEntity findCategoryFirstWindowById(int id)
        throws Exception;
    
    /**
     * 根据条件查找一级分类资源位
     * @param para
     * @return
     */
    CategoryFirstWindowEntity findCategoryFirstWindowByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 新增/修改一级分类资源位
     * @param para
     * @return
     * @throws Exception
     */
    int saveOrUpdateCategoryFirstWindow(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改一级分类资源位状态
     * @param para
     * @return
     */
    int updateCategoryFirstWindowStatus(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改一级分类资源位排序
     * @param para
     * @return
     * @throws Exception
     */
    int updateCategoryFirstWindowSequence(Map<String, Object> para)
        throws Exception;
    
    /**
     * 分类活动列表
     * @param para
     * @return
     */
    Map<String, Object> jsonCategoryActivityInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据Id查找分类活动
     * @param id
     * @return
     */
    CategoryActivityEntity findCategoryActivityById(int id)
        throws Exception;
    
    /**
     * 新增/修改特卖活动
     * @param para
     * @return
     */
    int saveOrUpdateCategoryActivity(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改分类活动状态
     * @param para
     * @return
     * @throws Exception
     */
    int updateCategoryActivityStatus(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改分类活动排序值
     * @param para
     * @return
     * @throws Exception
     */
    int updateCategoryActivitySequence(Map<String, Object> para)
        throws Exception;
    
    /**
     * 
     */
    List<CategoryEntity> findCategoryByProductBaseId(int id)
        throws Exception;
    
    /**
     * 
     * @param para
     * @return
     */
    Map<String, Object> jsonProductCategory(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改商品三级分类排序值
     * @param para
     * @return
     * @throws Exception
     */
    int updateProductCategoryThirdSequence(Map<String, Object> para)
        throws Exception;
    
    /**
     * 更改三级分类展现状态
     * @param para
     * @return
     * @throws Exception
     */
    int updateCategoryThirdDisplay(Map<String, Object> para)
        throws Exception;
    
}
