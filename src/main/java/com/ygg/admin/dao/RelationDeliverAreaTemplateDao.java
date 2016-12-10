package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.RelationDeliverAreaTemplateEntity;

public interface RelationDeliverAreaTemplateDao
{
    /**
     * 新增模版关联地区
     * @param areas
     * @return
     * @throws Exception
     */
    int insertRelationDeliverAreaTemplate(List<RelationDeliverAreaTemplateEntity> areas)
        throws Exception;
    
    /**
     * 修改模版关联地区
     * @param templateId
     * @param areas
     * @return
     * @throws Exception
     */
    int updateRelationDeliverAreaTemplate(int templateId, List<RelationDeliverAreaTemplateEntity> areas)
        throws Exception;
    
    /**
     * 删除模版地区关联
     * @param id
     * @return
     * @throws Exception
     */
    int deleteRelationDeliverAreaTemplateByTemplateId(int id)
        throws Exception;
    
    /**
     * 根据条件查找配送地区
     * @param para
     * @return
     * @throws Exception
     */
    List<RelationDeliverAreaTemplateEntity> findRelationDeliverAreaTemplateByPara(Map<String, Object> para)
        throws Exception;
    
    List<RelationDeliverAreaTemplateEntity> findRelationDeliverAreaTemplateByTemplateId(int id)
        throws Exception;
    
    /**
     * 根据Id删除配送地区
     * @param idList
     * @return
     * @throws Exception
     */
    int deleteRelationDeliverAreaTemplateByIdList(List<Integer> idList)
        throws Exception;
    
}
