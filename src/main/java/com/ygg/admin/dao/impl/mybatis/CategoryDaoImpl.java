package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.CategoryDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.CategoryActivityEntity;
import com.ygg.admin.entity.CategoryEntity;
import com.ygg.admin.entity.CategoryFirstEntity;
import com.ygg.admin.entity.CategoryFirstWindowEntity;
import com.ygg.admin.entity.CategorySecondEntity;
import com.ygg.admin.entity.CategoryThirdEntity;
import com.ygg.admin.entity.RelationCategoryAndProductEntity;

@Repository("categoryDao")
public class CategoryDaoImpl extends BaseDaoImpl implements CategoryDao
{
    @Override
    public CategoryFirstEntity findCategoryFirstById(int firstCategoryId)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("start", 0);
        para.put("max", 1);
        para.put("id", firstCategoryId);
        List<CategoryFirstEntity> reList = this.getSqlSessionRead().selectList("CategoryMapper.findAllCategoryFirst", para);
        if (reList != null && reList.size() > 0)
        {
            return reList.get(0);
        }
        return null;
    }
    
    @Override
    public List<CategoryFirstEntity> findAllCategoryFirst(Map<String, Object> para)
        throws Exception
    {
        List<CategoryFirstEntity> reList = this.getSqlSession().selectList("CategoryMapper.findAllCategoryFirst", para);
        return reList == null ? new ArrayList<CategoryFirstEntity>() : reList;
    }
    
    @Override
    public int countCategoryFirst(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("CategoryMapper.countCategoryFirst", para);
    }
    
    @Override
    public int updateCategoryFirstStatus(Map<String, Object> para)
        throws Exception
    {
        int result = this.getSqlSession().update("CategoryMapper.updateCategoryFirstStatus", para);
        return result > 1 ? 1 : result;
    }
    
    @Override
    public int findCategoryFirstMaxSequence()
        throws Exception
    {
        Integer sequence = this.getSqlSessionRead().selectOne("CategoryMapper.findCategoryFirstMaxSequence");
        return sequence == null ? 1 : sequence.intValue() + 1;
    }
    
    @Override
    public int insertCategoryFirst(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().insert("CategoryMapper.insertCategoryFirst", para);
    }
    
    @Override
    public int updateCategoryFirst(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("CategoryMapper.updateCategoryFirst", para);
    }
    
    @Override
    public CategorySecondEntity findCategorySecondById(int secondCategoryId)
        throws Exception
    {
        
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("start", 0);
        para.put("max", 1);
        para.put("id", secondCategoryId);
        List<CategorySecondEntity> reList = this.getSqlSessionRead().selectList("CategoryMapper.findAllCategorySecond", para);
        if (reList != null && reList.size() > 0)
        {
            return reList.get(0);
        }
        return null;
    }
    
    @Override
    public List<CategorySecondEntity> findAllCategorySecond(Map<String, Object> para)
        throws Exception
    {
        List<CategorySecondEntity> resultList = getSqlSession().selectList("CategoryMapper.findAllCategorySecond", para);
        return resultList == null ? new ArrayList<CategorySecondEntity>() : resultList;
    }
    
    @Override
    public int countCategorySecondInfo(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("CategoryMapper.countCategorySecondInfo", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllCategorySecondInfo(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> resultList = getSqlSessionRead().selectList("CategoryMapper.findAllCategorySecondInfo", para);
        return resultList == null ? new ArrayList<Map<String, Object>>() : resultList;
    }
    
    @Override
    public int findCategorySecondMaxSequence(int categoryFirstId)
        throws Exception
    {
        Integer sequence = this.getSqlSessionRead().selectOne("CategoryMapper.findCategorySecondMaxSequence", categoryFirstId);
        return sequence == null ? 1 : sequence.intValue() + 1;
    }
    
    @Override
    public int insertCategorySecond(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().insert("CategoryMapper.insertCategorySecond", para);
    }
    
    @Override
    public int updateCategorySecond(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("CategoryMapper.updateCategorySecond", para);
    }
    
    @Override
    public int updateCategorySecondStatus(Map<String, Object> para)
        throws Exception
    {
        int result = this.getSqlSession().update("CategoryMapper.updateCategorySecondStatus", para);
        return result > 1 ? 1 : result;
    }
    
    @Override
    public CategorySecondEntity findCategorySecondByPara(Map<String, Object> para)
        throws Exception
    {
        para.put("start", 0);
        para.put("max", 1);
        List<CategorySecondEntity> reList = this.getSqlSessionRead().selectList("CategoryMapper.findAllCategorySecond", para);
        if (reList != null && reList.size() > 0)
        {
            return reList.get(0);
        }
        return null;
    }
    
    @Override
    public int countCategoryThirdInfo(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("CategoryMapper.countCategoryThirdInfo", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllCategoryThirdInfo(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = this.getSqlSessionRead().selectList("CategoryMapper.findAllCategoryThirdInfo", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public List<CategoryThirdEntity> findAllCategoryThird(Map<String, Object> para)
        throws Exception
    {
        List<CategoryThirdEntity> reList = this.getSqlSession().selectList("CategoryMapper.findAllCategoryThird", para);
        return reList == null ? new ArrayList<CategoryThirdEntity>() : reList;
    }
    
    @Override
    public CategoryThirdEntity findCategoryThirdById(int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("start", 0);
        para.put("max", 1);
        para.put("id", id);
        List<CategoryThirdEntity> reList = this.getSqlSession().selectList("CategoryMapper.findAllCategoryThird", para);
        if (reList != null && reList.size() > 0)
        {
            return reList.get(0);
        }
        return null;
    }
    
    @Override
    public CategoryThirdEntity findCategoryThirdByPara(Map<String, Object> para)
        throws Exception
    {
        para.put("start", 0);
        para.put("max", 1);
        List<CategoryThirdEntity> reList = this.getSqlSessionRead().selectList("CategoryMapper.findAllCategoryThird", para);
        if (reList != null && reList.size() > 0)
        {
            return reList.get(0);
        }
        return null;
    }
    
    @Override
    public int insertCategoryThird(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().insert("CategoryMapper.insertCategoryThird", para);
    }
    
    @Override
    public int updateCategoryThird(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("CategoryMapper.updateCategoryThird", para);
    }
    
    @Override
    public int updateCategoryThirdStatus(Map<String, Object> para)
        throws Exception
    {
        int result = this.getSqlSession().update("CategoryMapper.updateCategoryThirdStatus", para);
        return result > 1 ? 1 : result;
    }
    
    @Override
    public int countCategoryFirstWindow(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("CategoryMapper.countCategoryFirstWindow", para);
    }
    
    @Override
    public List<CategoryFirstWindowEntity> findAllCategoryFirstWindow(Map<String, Object> para)
        throws Exception
    {
        List<CategoryFirstWindowEntity> reList = this.getSqlSessionRead().selectList("CategoryMapper.findAllCategoryFirstWindow", para);
        return reList == null ? new ArrayList<CategoryFirstWindowEntity>() : reList;
    }
    
    @Override
    public int insertCategoryFirstWindow(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().insert("CategoryMapper.insertCategoryFirstWindow", para);
    }
    
    @Override
    public int updateCategoryFirstWindow(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("CategoryMapper.updateCategoryFirstWindow", para);
    }
    
    @Override
    public int updateCategoryFirstWindowStatus(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("CategoryMapper.updateCategoryFirstWindowStatus", para);
    }
    
    @Override
    public int countCategoryActivity(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("CategoryMapper.countCategoryActivity", para);
    }
    
    @Override
    public List<CategoryActivityEntity> findAllCategoryActivity(Map<String, Object> para)
        throws Exception
    {
        List<CategoryActivityEntity> reList = this.getSqlSessionRead().selectList("CategoryMapper.findAllCategoryActivity", para);
        return reList == null ? new ArrayList<CategoryActivityEntity>() : reList;
    }
    
    @Override
    public int insertCategoryActivity(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().insert("CategoryMapper.insertCategoryActivity", para);
    }
    
    @Override
    public int updateCategoryActivity(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("CategoryMapper.updateCategoryActivity", para);
    }
    
    @Override
    public int updateCategoryActivityStatus(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("CategoryMapper.updateCategoryActivityStatus", para);
    }
    
    @Override
    public List<CategoryEntity> findCategoryByProductBaseId(int productId)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("baseId", productId);
        List<CategoryEntity> reList = this.getSqlSession().selectList("CategoryMapper.findProductCategory", para);
        return reList == null ? new ArrayList<CategoryEntity>() : reList;
    }
    
    @Override
    public int insertCategory(CategoryEntity category)
        throws Exception
    {
        return this.getSqlSession().insert("CategoryMapper.insertCategory", category);
    }
    
    @Override
    public int deleteCategory(Map<String, Object> mp)
        throws Exception
    {
        return this.getSqlSession().delete("CategoryMapper.deleteCategory", mp);
    }
    
    @Override
    public int updateCategory(CategoryEntity category)
        throws Exception
    {
        
        return this.getSqlSession().update("CategoryMapper.updateCategory", category);
    }
    
    @Override
    public int countProductCategory(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("CategoryMapper.countProductCategory", para);
    }
    
    @Override
    public List<CategoryEntity> findProductCategory(Map<String, Object> para)
        throws Exception
    {
        List<CategoryEntity> reList = this.getSqlSessionRead().selectList("CategoryMapper.findProductCategory", para);
        return reList == null ? new ArrayList<CategoryEntity>() : reList;
    }
    
    @Override
    public int insertRelationCategoryAndProduct(List<Map<String, Object>> productCategoryList)
        throws Exception
    {
        return this.getSqlSession().insert("CategoryMapper.insertRelationCategoryAndProduct", productCategoryList);
    }
    
    @Override
    public int deleteRelationCategoryAndProduct(Map<String, Object> map)
        throws Exception
    {
        return this.getSqlSession().delete("CategoryMapper.deleteRelationCategoryAndProduct", map);
    }
    
    @Override
    public List<Map<String, Object>> findMallProductById(int id)
        throws Exception
    {
        List<Map<String, Object>> reList = this.getSqlSessionRead().selectList("CategoryMapper.findMallProductById", id);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public List<RelationCategoryAndProductEntity> findThirdCatetoryByProductId(int id)
        throws Exception
    {
        List<RelationCategoryAndProductEntity> reList = this.getSqlSession().selectList("CategoryMapper.findThirdCatetoryByProductId", id);
        return reList == null ? new ArrayList<RelationCategoryAndProductEntity>() : reList;
    }
    
    @Override
    public int updateProductCategoryThirdSequence(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("CategoryMapper.updateProductCategoryThirdSequence", para);
    }
    
    @Override
    public boolean checkProductBaseCategoryIsExist(CategoryEntity ce)
        throws Exception
    {
        CategoryEntity cet = this.getSqlSession().selectOne("CategoryMapper.checkProductBaseCategoryIsExist", ce);
        return cet != null;
    }
    
    @Override
    public boolean checkProductCategoryIsExist(Map<String, Object> para)
        throws Exception
    {
        RelationCategoryAndProductEntity rcpe = this.getSqlSession().selectOne("CategoryMapper.checkProductCategoryIsExist", para);
        return rcpe != null;
    }
    
    @Override
    public List<Integer> findCategoryThirdIdByPara(Map<String, Object> para)
        throws Exception
    {
        List<Integer> reList = this.getSqlSessionRead().selectList("CategoryMapper.findCategoryThirdIdByPara", para);
        return reList == null ? new ArrayList<Integer>() : reList;
    }
    
    @Override
    public int deleteRelationCategoryAndProductBaseByCategorySecondId(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().delete("CategoryMapper.deleteRelationCategoryAndProductBaseByCategorySecondId", para);
    }
    
    @Override
    public int updateRelationCategoryAndProductBaseByCategoryThirdId(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("CategoryMapper.updateRelationCategoryAndProductBaseByCategoryThirdId", para);
    }
    
    @Override
    public int deleteRelationCategoryAndProductByCategoryThirdId(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().delete("CategoryMapper.deleteRelationCategoryAndProductByCategoryThirdId", para);
    }
    
    @Override
    public int countThirdCatetoryByProductId(int productId)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("CategoryMapper.countThirdCatetoryByProductId", productId);
    }
    
    @Override
    public List<Map<String, Object>> countCategoryThirdGroupByCategorySecondId()
        throws Exception
    {
        List<Map<String, Object>> reList = this.getSqlSessionRead().selectList("CategoryMapper.countCategoryThirdGroupByCategorySecondId");
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public List<Map<String, Object>> findCategoryThirdInfoByIds(List<Integer> idList)
        throws Exception
    {
        return this.getSqlSessionRead().selectList("CategoryMapper.findCategoryThirdInfoByIds", idList);
    }
    
    @Override
    public List<Map<String, Object>> findCategorySecondInfoByIds(List<Integer> idList)
        throws Exception
    {
        return this.getSqlSessionRead().selectList("CategoryMapper.findCategorySecondInfoByIds", idList);
    }
    
    @Override
    public int updateCategoryThirdDisplay(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("CategoryMapper.updateCategoryThirdDisplay", para);
    }
    
    @Override
    public String findCategoryFirstNameBybid(String productBaseId)
        throws Exception
    {
        String name = this.getSqlSessionRead().selectOne("CategoryMapper.findCategoryFirstNameBybid", productBaseId);
        return name == null ? "无分类" : name;
    }
}
