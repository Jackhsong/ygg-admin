package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.RelationDeliverAreaTemplateDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.RelationDeliverAreaTemplateEntity;

@Repository("relationDeliverAreaTemplateDao")
public class RelationDeliverAreaTemplateDaoImpl extends BaseDaoImpl implements RelationDeliverAreaTemplateDao
{
    
    @Override
    public int insertRelationDeliverAreaTemplate(List<RelationDeliverAreaTemplateEntity> areas)
        throws Exception
    {
        return getSqlSession().insert("RelationDeliverAreaTemplateMapper.insertRelationDeliverAreaTemplate", areas);
    }
    
    @Override
    public int updateRelationDeliverAreaTemplate(int templateId, List<RelationDeliverAreaTemplateEntity> areas)
        throws Exception
    {
        deleteRelationDeliverAreaTemplateByTemplateId(templateId);
        return insertRelationDeliverAreaTemplate(areas);
    }
    
    public int deleteRelationDeliverAreaTemplateByTemplateId(int templateId)
    {
        return getSqlSession().delete("RelationDeliverAreaTemplateMapper.deleteRelationDeliverAreaTemplateByTemplateId", templateId);
    }
    
    @Override
    public List<RelationDeliverAreaTemplateEntity> findRelationDeliverAreaTemplateByPara(Map<String, Object> para)
        throws Exception
    {
        List<RelationDeliverAreaTemplateEntity> reList = getSqlSessionRead().selectList("RelationDeliverAreaTemplateMapper.findRelationDeliverAreaTemplateByPara", para);
        return reList == null ? new ArrayList<RelationDeliverAreaTemplateEntity>() : reList;
    }
    
    @Override
    public List<RelationDeliverAreaTemplateEntity> findRelationDeliverAreaTemplateByTemplateId(int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("sellerDeliverAreaTemplateId", id);
        return findRelationDeliverAreaTemplateByPara(para);
    }
    
    @Override
    public int deleteRelationDeliverAreaTemplateByIdList(List<Integer> idList)
        throws Exception
    {
        return getSqlSession().delete("RelationDeliverAreaTemplateMapper.deleteRelationDeliverAreaTemplateByIdList", idList);
    }
}
