package com.ygg.admin.service;

public interface RelationDeliverAreaTemplateService
{
    /**
     * 插入模版配送关联地区
     * @param templateId：模版ID
     * @param templateType
     * @param areaCodes
     * @param other
     * @return
     * @throws Exception
     */
    int insertRelationDeliverAreaTemplate(int templateId, String areaCodes, String other)
        throws Exception;
    
    /**
     * 修改模版配送关联地区
     * @param id
     * @param type
     * @param areaCodes
     * @param other
     * @return
     * @throws Exception
     */
    int updateRelationDeliverAreaTemplate(int id, String areaCodes, String other)
        throws Exception;
    
    /**
     * 删除模版关联地区
     * @param id
     * @return
     * @throws Exception
     */
    int deleteRelationDeliverAreaTemplateByTemplateId(int id)
        throws Exception;
    
}
