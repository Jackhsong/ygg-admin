package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.FreightTemplateEntity;
import com.ygg.admin.entity.ProvinceEntity;
import com.ygg.admin.entity.ProvinceFreightEntity;

public interface FreightDao
{
    
    /**
     * 查询邮费模板信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<FreightTemplateEntity> findAllFreightTemplateByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查询邮费模板数量
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int countFreightTemplateByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据ID查询邮费模板
     * 
     * @param id
     * @return
     * @throws Exception
     */
    FreightTemplateEntity findFreightTemplateById(int id)
        throws Exception;
    
    /**
     * 新增邮费模板信息
     * 
     * @param entity
     * @return
     * @throws Exception
     */
    int save(FreightTemplateEntity entity)
        throws Exception;
    
    /**
     * 更新邮费模板信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int update(Map<String, Object> para)
        throws Exception;
    
    /**
     * 插入FreightTemplate下省份的相关邮费数据
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int saveProvinceFreight(Map<String, Object> para)
        throws Exception;
    
    /**
     * 更新FreightTemplate下省份的相关邮费数据
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int updateProvinceFreight(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据templateId查询各个省份的邮费数据
     * 
     * @param templateId
     * @return
     * @throws Exception
     */
    List<ProvinceFreightEntity> findAllProvinceFreightByTemplateId(int templateId)
        throws Exception;
    
    /**
     * 根据省份ID查询身份名称
     * 
     * @param provinceId
     * @return
     * @throws Exception
     */
    String findProvinceNameByProvinceId(int provinceId)
        throws Exception;
    
    /**
     * 查找所有省份信息
     * 
     * @return
     * @throws Exception
     */
    List<ProvinceEntity> findAllProvince()
        throws Exception;
}
