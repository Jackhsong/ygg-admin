package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.CityEntity;
import com.ygg.admin.entity.DistrictEntity;
import com.ygg.admin.entity.ProvinceEntity;

public interface AreaService
{
    /**
     * 获取所有省份信息
     * 
     * @return
     * @throws Exception
     */
    List<ProvinceEntity> allProvince()
        throws Exception;
    
    /**
     * 临时 方法
     * 
     * @param info
     * @return
     * @throws Exception
     */
    int setCityZipCode(List<Map<String, Object>> info)
        throws Exception;
    
    /**
     * 全部市
     * @param para
     * @return
     * @throws Exception
     */
    List<CityEntity> findAllCity(Map<String, Object> para)
        throws Exception;
    
    /**
     * 全部区
     * @param para
     * @return
     * @throws Exception
     */
    List<DistrictEntity> findAllDistrict(Map<String, Object> para)
        throws Exception;
    
    Object findAllProvince()
        throws Exception;
    
    Object findAllCity(int provinceId)
        throws Exception;
    
    Object findAllDistrict(int cityId)
        throws Exception;
}
