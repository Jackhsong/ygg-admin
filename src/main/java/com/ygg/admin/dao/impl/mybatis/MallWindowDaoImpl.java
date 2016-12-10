package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.MallWindowDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.MallPageEntity;
import com.ygg.admin.entity.MallWindowEntity;

@Repository("mallWindowDao")
public class MallWindowDaoImpl extends BaseDaoImpl implements MallWindowDao
{
    
    @Override
    public List<Map<String, Object>> findJsonMallWindowInfo(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = getSqlSessionRead().selectList("MallWindowMapper.findJsonMallWindowInfo", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public int countMallWindowInfo(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("MallWindowMapper.countMallWindowInfo", para);
    }
    
    @Override
    public int updateMallWindowByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("MallWindowMapper.updateMallWindowByPara", para);
    }
    
    @Override
    public int getMallWindowMaxSequence()
        throws Exception
    {
        Integer sequence = getSqlSessionRead().selectOne("MallWindowMapper.getMallWindowMaxSequence");
        return sequence == null ? 1 : sequence.intValue();
    }
    
    @Override
    public int saveMallWindow(MallWindowEntity mallWindow)
        throws Exception
    {
        return getSqlSession().insert("MallWindowMapper.saveMallWindow", mallWindow);
    }
    
    @Override
    public List<Map<String, Object>> findAllMallPage(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = getSqlSessionRead().selectList("MallWindowMapper.findAllMallPage", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public List<MallWindowEntity> findMallWindowByPara(Map<String, Object> para)
        throws Exception
    {
        List<MallWindowEntity> result = getSqlSessionRead().selectList("MallWindowMapper.findMallWindowByPara", para);
        return result == null ? new ArrayList<MallWindowEntity>() : result;
    }
    
    @Override
    public int countMallPageInfo(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("MallWindowMapper.countMallPageInfo", para);
    }
    
    @Override
    public int saveMallPage(MallPageEntity mallPage)
        throws Exception
    {
        return getSqlSession().insert("MallWindowMapper.saveMallPage", mallPage);
    }
    
    @Override
    public int updateMallPage(MallPageEntity mallPage)
        throws Exception
    {
        return getSqlSession().update("MallWindowMapper.updateMallPage", mallPage);
    }
    
    @Override
    public int countMallPageFloor(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("MallWindowMapper.countMallPageFloor", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllMallPageFloor(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = getSqlSessionRead().selectList("MallWindowMapper.findAllMallPageFloor", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public int savePageFloor(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("MallWindowMapper.savePageFloor", para);
    }
    
    @Override
    public int updatePageFloor(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("MallWindowMapper.updatePageFloor", para);
    }
    
    @Override
    public int getPageFloorMaxSequence(Map<String, Object> para)
        throws Exception
    {
        Integer sequence = getSqlSessionRead().selectOne("MallWindowMapper.getPageFloorMaxSequence", para);
        return sequence == null ? 1 : sequence.intValue();
    }
    
    @Override
    public int countMallFloorProductInfo(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("MallWindowMapper.countMallFloorProductInfo", para);
    }
    
    @Override
    public List<Map<String, Object>> findJsonMallFloorProductInfo(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = getSqlSessionRead().selectList("MallWindowMapper.findJsonMallFloorProductInfo", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public int updateFloorProductSequence(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("MallWindowMapper.updateFloorProductSequence", para);
    }
    
    @Override
    public int deleteRelationMallPageFloorAndProduct(int id)
        throws Exception
    {
        return getSqlSession().delete("MallWindowMapper.deleteRelationMallPageFloorAndProduct", id);
    }
    
    @Override
    public List<Integer> findAllProductIdByMallPageId(int id)
        throws Exception
    {
        List<Integer> reList = getSqlSessionRead().selectList("MallWindowMapper.findAllProductIdByMallPageId", id);
        return reList == null ? new ArrayList<Integer>() : reList;
    }
    
    @Override
    public int countProductInfoForAdd(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("MallWindowMapper.countProductInfoForAdd", para);
    }
    
    @Override
    public List<Map<String, Object>> findProductInfoForAdd(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = getSqlSessionRead().selectList("MallWindowMapper.findProductInfoForAdd", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public List<Integer> findAllProductIdByPageFloorId(int floorId)
        throws Exception
    {
        List<Integer> reList = getSqlSessionRead().selectList("MallWindowMapper.findAllProductIdByPageFloorId", floorId);
        return reList == null ? new ArrayList<Integer>() : reList;
    }
    
    @Override
    public int findMaxSequenceRelationMallPageFloorAndProduct(int floorId)
        throws Exception
    {
        Integer sequence = getSqlSessionRead().selectOne("MallWindowMapper.findMaxSequenceRelationMallPageFloorAndProduct", floorId);
        return sequence == null ? 1 : sequence;
    }
    
    @Override
    public int saveRelationMallPageFloorAndProduct(Map<String, Object> map)
        throws Exception
    {
        return getSqlSession().insert("MallWindowMapper.saveRelationMallPageFloorAndProduct", map);
    }
    
    @Override
    public int countCurrentHotProductInfo(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("MallWindowMapper.countCurrentHotProductInfo", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllCurrentHotProductInfo(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionRead().selectList("MallWindowMapper.findAllCurrentHotProductInfo", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public int updateHotProduct(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("MallWindowMapper.updateHotProduct", para);
    }
    
    @Override
    public int deleteHotProduct(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().delete("MallWindowMapper.deleteHotProduct", para);
    }
    
    @Override
    public List<Integer> findAllHotProductId()
        throws Exception
    {
        List<Integer> reList = getSqlSessionRead().selectList("MallWindowMapper.findAllHotProductId");
        return reList == null ? new ArrayList<Integer>() : reList;
    }
    
    @Override
    public int findMaxSequenceProductHot()
        throws Exception
    {
        
        Integer sequence = getSqlSessionRead().selectOne("MallWindowMapper.findMaxSequenceProductHot");
        return sequence == null ? 1 : sequence;
    }
    
    @Override
    public int saveProductHot(Map<String, Object> map)
        throws Exception
    {
        return getSqlSession().insert("MallWindowMapper.saveProductHot", map);
    }
    
    @Override
    public int countCurrentHotDisplayProduct(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("MallWindowMapper.countCurrentHotDisplayProduct");
    }
    
    @Override
    public List<Map<String, Object>> findAllCurrentHotDisplayProductList(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionRead().selectList("MallWindowMapper.findAllCurrentHotDisplayProductList", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public Map<String, Object> findProductHotTimeFactor()
        throws Exception
    {
        Map<String, Object> result = getSqlSessionRead().selectOne("MallWindowMapper.findProductHotTimeFactor");
        return result == null ? new HashMap<String, Object>() : result;
    }
    
    @Override
    public List<Map<String, Object>> findMallProductInfo(String beginTime)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionRead().selectList("MallWindowMapper.findMallProductInfo", beginTime);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public Map<String, Object> findHotProductInfoById(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> result = getSqlSessionRead().selectOne("MallWindowMapper.findHotProductInfoById", para);
        return result == null ? new HashMap<String, Object>() : result;
    }
    
    @Override
    public int updateHotProductCustomFactor(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("MallWindowMapper.updateHotProductCustomFactor", para);
    }
    
    @Override
    public int updateSaleTimeFactor(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("MallWindowMapper.updateSaleTimeFactor", para);
    }
    
    @Override
    public int countProductHotId(int productId)
        throws Exception
    {
        return getSqlSessionRead().selectOne("MallWindowMapper.countProductHotId", productId);
    }
    
    @Override
    public int updateHotProductDisplayStatus(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("MallWindowMapper.updateHotProductDisplayStatus", para);
    }
}
