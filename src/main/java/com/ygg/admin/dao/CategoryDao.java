package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.CategoryActivityEntity;
import com.ygg.admin.entity.CategoryEntity;
import com.ygg.admin.entity.CategoryFirstEntity;
import com.ygg.admin.entity.CategoryFirstWindowEntity;
import com.ygg.admin.entity.CategorySecondEntity;
import com.ygg.admin.entity.CategoryThirdEntity;
import com.ygg.admin.entity.RelationCategoryAndProductEntity;

public interface CategoryDao
{
    
    /**
     * 根据id查询二级分类
     * @param firstCategoryId
     * @return
     * @throws Exception
     */
    CategoryFirstEntity findCategoryFirstById(int firstCategoryId)
        throws Exception;
    
    /**
     * 一级分类
     * @param para
     * @return
     * @throws Exception
     */
    List<CategoryFirstEntity> findAllCategoryFirst(Map<String, Object> para)
        throws Exception;
    
    /**
     * 更新一级分类状态
     * @param para
     * @return
     */
    int updateCategoryFirstStatus(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查找一级分类最大排序值
     * @return
     * @throws Exception
     */
    int findCategoryFirstMaxSequence()
        throws Exception;
    
    /**
     * 新增一级分类
     * @param para
     * @return
     */
    int insertCategoryFirst(Map<String, Object> para)
        throws Exception;
    
    /**
     * 更新一级分类
     * @param para
     * @return
     * @throws Exception
     */
    int updateCategoryFirst(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计一级分类
     * @param para
     * @return
     */
    int countCategoryFirst(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查询所有二级分类
     * @param para
     * @return
     * @throws Exception
     */
    List<CategorySecondEntity> findAllCategorySecond(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据Id查找二级分类
     * @param secondCategoryId
     * @return
     * @throws Exception
     */
    CategorySecondEntity findCategorySecondById(int secondCategoryId)
        throws Exception;
    
    /**
     * 查询二级分类所有信息
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllCategorySecondInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计所有二级分类
     * @param para
     * @return
     */
    int countCategorySecondInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查找二级分类最大排序值
     * @param categoryFirstId
     * @return
     */
    int findCategorySecondMaxSequence(int categoryFirstId)
        throws Exception;
    
    /**
     * 新增二级分类
     * @param para
     * @return
     */
    int insertCategorySecond(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改二级分类
     * @param para
     * @return
     */
    int updateCategorySecond(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改二级分类展现状态
     * @param para
     * @return
     */
    int updateCategorySecondStatus(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据条件查找二级分类
     * @param para
     * @return
     */
    CategorySecondEntity findCategorySecondByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 三级分类列表
     * @param para
     * @return
     */
    List<Map<String, Object>> findAllCategoryThirdInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计三级分类
     * @param para
     * @return
     */
    int countCategoryThirdInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查找所有三级分类
     * @param para
     * @return
     */
    List<CategoryThirdEntity> findAllCategoryThird(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据ID查找三级分类
     * @param id
     * @return
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
     * 新增三级分类
     * @param para
     * @return
     */
    int insertCategoryThird(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改三级分类
     * @param para
     * @return
     */
    int updateCategoryThird(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改三级分类状态
     * @param para
     * @return
     * @throws Exception
     */
    int updateCategoryThirdStatus(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查找全部一级资源位
     * @param para
     * @return
     * @throws Exception
     */
    List<CategoryFirstWindowEntity> findAllCategoryFirstWindow(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计一级资源位
     * @param para
     * @return
     * @throws Exception
     */
    int countCategoryFirstWindow(Map<String, Object> para)
        throws Exception;
    
    /**
     * 新增一级资源位
     * @param para
     * @return
     * @throws Exception
     */
    int insertCategoryFirstWindow(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改一级分类资源位
     * @param para
     * @return
     * @throws Exception
     */
    int updateCategoryFirstWindow(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改一级分类资源位展现状态
     * @param para
     * @return
     */
    int updateCategoryFirstWindowStatus(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查找全部分类活动
     * @param para
     * @return
     * @throws Exception
     */
    List<CategoryActivityEntity> findAllCategoryActivity(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计分类活动
     * @param para
     * @return
     * @throws Exception
     */
    int countCategoryActivity(Map<String, Object> para)
        throws Exception;
    
    /**
     * 新增分类活动
     * @param para
     * @return
     * @throws Exception
     */
    int insertCategoryActivity(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改分类活动您那个
     * @param para
     * @return
     * @throws Exception
     */
    int updateCategoryActivity(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改分类活动展现状态
     * @param para
     * @return
     */
    int updateCategoryActivityStatus(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据基本商品Id查找分类
     * @param productId
     * @return
     * @throws Exception
     */
    List<CategoryEntity> findCategoryByProductBaseId(int productId)
        throws Exception;
    
    /**
     * 新增分类信息
     * @param category
     * @return
     * @throws Exception
     */
    int insertCategory(CategoryEntity category)
        throws Exception;
    
    /**
     * 更新分类信息
     * @param category
     * @return
     */
    int updateCategory(CategoryEntity category)
        throws Exception;
    
    /**
     * 删除分类信息
     * @param mp
     * @return
     */
    int deleteCategory(Map<String, Object> mp)
        throws Exception;
    
    /**
     * 根据条件查找商品分类信息
     * @param para
     * @return
     */
    List<CategoryEntity> findProductCategory(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计商品分类信息
     * @param para
     * @return
     * @throws Exception
     */
    int countProductCategory(Map<String, Object> para)
        throws Exception;
    
    /**
     * 新增商品分类
     * @param productCategoryList
     * @return
     */
    int insertRelationCategoryAndProduct(List<Map<String, Object>> productCategoryList)
        throws Exception;
    
    /**
     * 删除分类
     * @param mp
     * @return
     */
    int deleteRelationCategoryAndProduct(Map<String, Object> mp)
        throws Exception;
    
    /**
     * 根据商城商品Id查询商品分类
     * @param id
     * @return
     */
    List<Map<String, Object>> findMallProductById(int id)
        throws Exception;
    
    /**
     * 根据商品Id查询三级分类
     * @param id
     * @return
     */
    List<RelationCategoryAndProductEntity> findThirdCatetoryByProductId(int id)
        throws Exception;
    
    /**
     * 修改商品三级分类排序值
     * @param para
     * @return
     */
    int updateProductCategoryThirdSequence(Map<String, Object> para)
        throws Exception;
    
    /**
     * 检查基本商品分类是存在
     * @param ce
     * @return
     */
    boolean checkProductBaseCategoryIsExist(CategoryEntity ce)
        throws Exception;
    
    /**
     * 检查商品分类是否存在
     * @param para
     * @return
     * @throws Exception
     */
    boolean checkProductCategoryIsExist(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据二级分类Id查找三级分类
     * @param para
     * @return
     * @throws Exception
     */
    List<Integer> findCategoryThirdIdByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 删除与二级分类关联的几本商品
     * @param para
     * @return
     * @throws Exception
     */
    int deleteRelationCategoryAndProductBaseByCategorySecondId(Map<String, Object> para)
        throws Exception;
    
    /**
     * 清除三级分类与基本商品的关联
     * @param para
     * @return
     * @throws Exception
     */
    int updateRelationCategoryAndProductBaseByCategoryThirdId(Map<String, Object> para)
        throws Exception;
    
    /**
     * 删除与三级分类关联的商城商品
     * @param para
     * @return
     * @throws Exception
     */
    int deleteRelationCategoryAndProductByCategoryThirdId(Map<String, Object> para)
        throws Exception;
    
    int countThirdCatetoryByProductId(int productId)
        throws Exception;
    
    /**
     * 查找二级分类下的三级分类
     * @return
     */
    List<Map<String, Object>> countCategoryThirdGroupByCategorySecondId()
        throws Exception;
    
    List<Map<String, Object>> findCategoryThirdInfoByIds(List<Integer> idList)
        throws Exception;
    
    List<Map<String, Object>> findCategorySecondInfoByIds(List<Integer> idList)
        throws Exception;
    
    int updateCategoryThirdDisplay(Map<String, Object> para)
        throws Exception;
    
    String findCategoryFirstNameBybid(String productBaseId)
        throws Exception;
    
}
