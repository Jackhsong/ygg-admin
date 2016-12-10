package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.SellerDeliverAreaDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.SellerDeliverAreaTemplateEntity;

@Repository("sellerDeliverAreaDao")
public class SellerDeliverAreaDaoImpl extends BaseDaoImpl implements SellerDeliverAreaDao
{
    
    @Override
    public SellerDeliverAreaTemplateEntity findSellerDeliverAreaTemplateByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("SellerDeliverAreaMapper.findSellerDeliverAreaTemplateByPara", para);
    }
    
    @Override
    public int insertSellerDeliverAreaTemplate(SellerDeliverAreaTemplateEntity areaTemplate)
        throws Exception
    {
        return getSqlSession().insert("SellerDeliverAreaMapper.insertSellerDeliverAreaTemplate", areaTemplate);
    }
    
    @Override
    public int updateSellerDeliverAreaTemplate(SellerDeliverAreaTemplateEntity areaTemplate)
        throws Exception
    {
        return getSqlSession().update("SellerDeliverAreaMapper.updateSellerDeliverAreaTemplate", areaTemplate);
    }
    
    @Override
    public int deleteSellerDeliverAreaTemplate(int id)
        throws Exception
    {
        return getSqlSession().delete("SellerDeliverAreaMapper.deleteSellerDeliverAreaTemplate", id);
    }
    
    @Override
    public List<SellerDeliverAreaTemplateEntity> findSellerDeliverAreaTemplateBysid(int sellerId)
        throws Exception
    {
        List<SellerDeliverAreaTemplateEntity> reList = getSqlSessionRead().selectList("SellerDeliverAreaMapper.findSellerDeliverAreaTemplateBysid", sellerId);
        return reList == null ? new ArrayList<SellerDeliverAreaTemplateEntity>() : reList;
    }
    
    @Override
    public int countSellerDeliverAreaTemplate(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("SellerDeliverAreaMapper.countSellerDeliverAreaTemplate", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllSellerDeliverAreaTemplate(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionRead().selectList("SellerDeliverAreaMapper.findAllSellerDeliverAreaTemplate", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public int countDeliverAreaTemplateFromProductBase(int id)
        throws Exception
    {
        return getSqlSessionRead().selectOne("SellerDeliverAreaMapper.countDeliverAreaTemplateFromProductBase", id);
    }
    
    @Override
    public int addCommonPostage(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("SellerDeliverAreaMapper.addCommonPostage", para);
    }
    
    @Override
    public int countCommonPostage(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("SellerDeliverAreaMapper.countCommonPostage", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllCommonPostage(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("SellerDeliverAreaMapper.findAllCommonPostage", para);
    }
    
    @Override
    public int updateCommonPostage(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("SellerDeliverAreaMapper.updateCommonPostage", para);
    }
    
    @Override
    public int insertRelationTemplateDeliverAreaAndExtraPostage(SellerDeliverAreaTemplateEntity areaTemplate, Set<String> provinceCodes)
        throws Exception
    {
        List<Map<String, Object>> parameter = new ArrayList<>();
        for (String provinceCode : provinceCodes)
        {
            Map<String, Object> map = new HashMap<>();
            map.put("sellerId", areaTemplate.getSellerId());
            map.put("sellerDeliverAreaTemplateId", areaTemplate.getId());
            map.put("provinceCode", provinceCode);
            map.put("isExtra", 0);
            map.put("freightMoney", 0);
            parameter.add(map);
        }
        return getSqlSession().insert("SellerDeliverAreaMapper.insertRelationTemplateDeliverAreaAndExtraPostage", parameter);
    }
    
    @Override
    public List<String> findRelationTemplateDeliverAreaProvinceCodeBytid(int tid)
        throws Exception
    {
        return getSqlSessionRead().selectList("SellerDeliverAreaMapper.findRelationTemplateDeliverAreaProvinceCodeBytid", tid);
    }
    
    @Override
    public int deleteRelationTemplateDeliverAreaAndExtraPostage(SellerDeliverAreaTemplateEntity areaTemplate, Set<String> provinceCodes)
        throws Exception
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("templateId", areaTemplate.getId());
        parameter.put("provinceCodes", new ArrayList<String>(provinceCodes));
        return getSqlSession().delete("SellerDeliverAreaMapper.deleteRelationTemplateDeliverAreaAndExtraPostage", parameter);
    }
    
    @Override
    public int deleteRelationTemplateDeliverAreaAndExtraPostage(int id)
        throws Exception
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("templateId", id);
        return getSqlSession().delete("SellerDeliverAreaMapper.deleteRelationTemplateDeliverAreaAndExtraPostage", parameter);
    }
    
    @Override
    public int countExtraPostage(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("SellerDeliverAreaMapper.countExtraPostage", para);
    }
    
    @Override
    public List<Map<String, Object>> findExtraPostage(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("SellerDeliverAreaMapper.findExtraPostage", para);
    }
    
    @Override
    public int updateExtraPostage(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("SellerDeliverAreaMapper.updateExtraPostage", para);
    }
    
    @Override
    public Integer findCommonExtraPostage(String provinceCode)
        throws Exception
    {
        return getSqlSessionRead().selectOne("SellerDeliverAreaMapper.findCommonExtraPostage", provinceCode);
    }
    
    @Override
    public List<Map<String, Object>> findExtraPostageBytids(List<Integer> templateIds)
        throws Exception
    {
        return getSqlSessionRead().selectList("SellerDeliverAreaMapper.findExtraPostageBytids", templateIds);
    }
}
