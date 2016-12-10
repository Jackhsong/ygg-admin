package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ygg.admin.entity.CityEntity;
import com.ygg.admin.entity.DistrictEntity;
import com.ygg.admin.entity.ProvinceEntity;

public interface AreaDao
{
    
    /**
     * 根据省份ID查询省份名称
     * 
     * @param id
     * @return
     * @throws Exception
     */
    String findProvinceNameByProvinceId(int id)
        throws Exception;
    
    /**
     * 查询所有身份信息
     * 
     * @return
     * @throws Exception
     */
    List<ProvinceEntity> findAllProvince()
        throws Exception;
    
    List<CityEntity> findAllCity()
        throws Exception;

    CityEntity findCityByCityId(String cityId)
            throws Exception;
    
    List<DistrictEntity> findAllDistrict()
        throws Exception;
    
    /**
     * 根据城市ID查询城市名称
     * 
     * @param id
     * @return
     * @throws Exception
     */
    String findCityNameByCityId(int id)
        throws Exception;
    
    /**
     * 根据provinceId查询所有城市信息
     * 
     * @return
     * @throws Exception
     */
    List<CityEntity> findAllCityByProvinceId(int provinceId)
        throws Exception;
    
    /**
     * 根据地区ID查询地区名称
     * 
     * @param id
     * @return
     * @throws Exception
     */
    String findDistrictNameByDistrictId(int id)
        throws Exception;
    
    /**
     * 根据cityId查询所有区信息
     * 
     * @return
     * @throws Exception
     */
    List<DistrictEntity> findAllDistrictByCityId(int cityId)
        throws Exception;
    
    /**
     * 更新 city
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int updateCityZipCode(Map<String, Object> para)
        throws Exception;
    
    /**
     * 全部市
     * @param para
     * @return
     */
    List<CityEntity> findAllCity(Map<String, Object> para)
        throws Exception;
    
    /**
     * 全部区
     * @param para
     * @return
     */
    List<DistrictEntity> findAllDistrict(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findCityCodeByPara(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findDistrictCodeByPara(Map<String, Object> para)
        throws Exception;
    
    Set<String> findAllProvinceCode()
        throws Exception;
}
