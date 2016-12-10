package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.FreightTemplateEntity;

public interface FreightService
{
    /**
     * 保存freightTemplate信息
     * 
     * @param brand
     * @return
     * @throws Exception
     */
    int saveOrUpdate(FreightTemplateEntity freightTemplate)
        throws Exception;
    
    /**
     * 根据para查询邮费模板，并封装成json字符串返回
     * 
     * @param para
     * @return
     * @throws Exception
     */
    String jsonfreightTemplateInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据para查询特定邮费模板ID下的各省份邮费信息，并封装成json字符串返回
     * 
     * @param para
     * @return
     * @throws Exception
     */
    String jsonProvinceFreightInfo(int templateId)
        throws Exception;
    
    /**
     * 根据ID查找邮费模板
     * 
     * @param id
     * @return
     * @throws Exception
     */
    FreightTemplateEntity findFreightTemplateById(int id)
        throws Exception;
    
    /**
     * 修改省份邮费信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int updateProvinceFreight(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查询可用的运费模板信息
     * 
     * @return
     * @throws Exception
     */
    List<FreightTemplateEntity> findFreightTemplateIsAvailable()
        throws Exception;
}
