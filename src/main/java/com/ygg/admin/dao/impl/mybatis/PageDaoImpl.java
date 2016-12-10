package com.ygg.admin.dao.impl.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.PageDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.PageEntity;
import com.ygg.admin.entity.PageModelBannerEntity;
import com.ygg.admin.entity.PageModelCustomLayoutEntity;
import com.ygg.admin.entity.PageModelEntity;

@Repository("pageDao")
public class PageDaoImpl extends BaseDaoImpl implements PageDao
{
    @Override
    public List<Map<String, Object>> findAllPageByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("PageMapper.findAllPageByPara", para);
    }
    
    @Override
    public int countAllPageByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("PageMapper.countAllPageByPara", para);
    }
    
    @Override
    public Map<String, Object> findPageById(int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        para.put("id", id);
        List<Map<String, Object>> resultList = findAllPageByPara(para);
        return resultList.isEmpty() ? null : resultList.get(0);
    }
    
    @Override
    public int insertPage(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("PageMapper.insertPage", para);
    }
    
    @Override
    public int updatePageById(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("PageMapper.updatePageById", para);
    }
    
    @Override
    public List<PageModelEntity> findAllPageModelByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("PageMapper.findAllPageModelByPara", para);
    }
    
    @Override
    public PageModelEntity findPageModelById(int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        para.put("id", id);
        List<PageModelEntity> pmeList = findAllPageModelByPara(para);
        return pmeList.isEmpty() ? null : pmeList.get(0);
    }
    
    @Override
    public int insertPageModel(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("PageMapper.insertPageModel", para);
    }
    
    @Override
    public int updatePageModelById(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("PageMapper.updatePageModelById", para);
    }
    
    @Override
    public List<PageModelCustomLayoutEntity> findAllPageModelCustomLayoutByModelId(int modelId)
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        para.put("pageModelId", modelId);
        return getSqlSessionRead().selectList("PageMapper.findAllPageModelCustomLayoutByModelId", para);
    }
    
    @Override
    public int countAllPageModelCustomLayoutByModelId(int modelId)
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        para.put("pageModelId", modelId);
        return getSqlSessionRead().selectOne("PageMapper.countAllPageModelCustomLayoutByModelId", para);
    }
    
    @Override
    public int findPageModelCustomLayoutMaxSequence(int modelId)
        throws Exception
    {
        Integer max = getSqlSession().selectOne("PageMapper.findPageModelCustomLayoutMaxSequence", modelId);
        return max == null ? 1 : max + 1;
    }
    
    @Override
    public PageModelCustomLayoutEntity findPageModelCustomLayoutById(int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        para.put("id", id);
        return getSqlSessionRead().selectOne("PageMapper.findAllPageModelCustomLayoutByModelId", para);
    }
    
    @Override
    public int insertPageModelCustomLayout(PageModelCustomLayoutEntity e)
        throws Exception
    {
        return getSqlSession().insert("PageMapper.insertPageModelCustomLayout", e);
    }
    
    @Override
    public int updatePageModelCustomLayout(PageModelCustomLayoutEntity e)
        throws Exception
    {
        return getSqlSession().update("PageMapper.updatePageModelCustomLayout", e);
    }
    
    @Override
    public int updatePageModelCustomLayoutSimpleData(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("PageMapper.updatePageModelCustomLayoutSimpleData", para);
    }
    
    @Override
    public int insertBatchPageModelRelationRollProduct(List<Map<String, Object>> list)
        throws Exception
    {
        return getSqlSession().insert("PageMapper.insertBatchPageModelRelationRollProduct", list);
    }
    
    @Override
    public int updatePageModelRelationRollProduct(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("PageMapper.updatePageModelRelationRollProduct", para);
    }
    
    @Override
    public int deletePageModelRelationRollProduct(List<Integer> list)
        throws Exception
    {
        return getSqlSession().delete("PageMapper.deletePageModelRelationRollProduct", list);
    }
    
    @Override
    public List<Map<String, Object>> findAllPageModelRelationRollProduct(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("PageMapper.findAllPageModelRelationRollProduct", para);
    }
    
    @Override
    public int countAllPageModelRelationRollProduct(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("PageMapper.countAllPageModelRelationRollProduct", para);
    }
    
    @Override
    public int findMaxSequencePageModelRelationRollProduct(int pageModelId)
        throws Exception
    {
        Integer max = getSqlSessionRead().selectOne("PageMapper.findMaxSequencePageModelRelationRollProduct", pageModelId);
        return max == null ? 1 : max;
    }
    
    @Override
    public int insertBatchPageModelRelationColumnProduct(List<Map<String, Object>> list)
        throws Exception
    {
        return getSqlSession().insert("PageMapper.insertBatchPageModelRelationColumnProduct", list);
    }
    
    @Override
    public int updatePageModelRelationColumnProduct(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("PageMapper.updatePageModelRelationColumnProduct", para);
    }
    
    @Override
    public int deletePageModelRelationColumnProduct(List<Integer> list)
        throws Exception
    {
        return getSqlSession().delete("PageMapper.deletePageModelRelationColumnProduct", list);
    }
    
    @Override
    public List<Map<String, Object>> findAllPageModelRelationColumnProduct(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("PageMapper.findAllPageModelRelationColumnProduct", para);
    }
    
    @Override
    public int countAllPageModelRelationColumnProduct(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("PageMapper.countAllPageModelRelationColumnProduct", para);
    }
    
    @Override
    public int findMaxSequencePageModelRelationColumnProduct(int pageModelId)
        throws Exception
    {
        Integer max = getSqlSessionRead().selectOne("PageMapper.findMaxSequencePageModelRelationColumnProduct", pageModelId);
        return max == null ? 1 : max;
    }
    
    @Override
    public int insertPageModelBanner(PageModelBannerEntity entity)
        throws Exception
    {
        return getSqlSession().insert("PageMapper.insertPageModelBanner", entity);
    }
    
    @Override
    public int updatePageModelBanner(PageModelBannerEntity entity)
        throws Exception
    {
        return getSqlSession().update("PageMapper.updatePageModelBanner", entity);
    }
    
    @Override
    public int updateSimplePageModelBanner(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("PageMapper.updateSimplePageModelBanner", para);
    }
    
    @Override
    public List<PageModelBannerEntity> findAllPageModelBanner(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("PageMapper.findAllPageModelBanner", para);
    }
    
    @Override
    public int countAllPageModelBanner(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("PageMapper.countAllPageModelBanner", para);
    }
    
    @Override
    public PageModelBannerEntity findPageModelBannerById(int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        para.put("id", id);
        List<PageModelBannerEntity> list = getSqlSessionRead().selectList("PageMapper.findAllPageModelBanner", para);
        return list.isEmpty() ? null : list.get(0);
    }
    
    @Override
    public PageEntity findPageBypid(int id)
        throws Exception
    {
        return getSqlSessionRead().selectOne("PageMapper.findPageBypid", id);
    }
    
    @Override
    public List<PageModelEntity> findPageModelBypid(int id)
        throws Exception
    {
        return getSqlSessionRead().selectList("PageMapper.findPageModelBypid", id);
    }
    
    @Override
    public List<PageModelCustomLayoutEntity> findPageModelCustomLayoutByModelId(int id)
        throws Exception
    {
        return getSqlSessionRead().selectList("PageMapper.findPageModelCustomLayoutByModelId", id);
    }
}
