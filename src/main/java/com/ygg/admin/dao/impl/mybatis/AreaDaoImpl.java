package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.AreaDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.CityEntity;
import com.ygg.admin.entity.DistrictEntity;
import com.ygg.admin.entity.ProvinceEntity;

@Repository("areaDao")
public class AreaDaoImpl extends BaseDaoImpl implements AreaDao
{
    
    @Override
    public String findProvinceNameByProvinceId(int id)
        throws Exception
    {
        return getSqlSession().selectOne("AreaMapper.findProvinceNameByProvinceId", id);
    }
    
    @Override
    public String findCityNameByCityId(int id)
        throws Exception
    {
        return getSqlSession().selectOne("AreaMapper.findCityNameByCityId", id);
    }
    
    @Override
    public String findDistrictNameByDistrictId(int id)
        throws Exception
    {
        return getSqlSession().selectOne("AreaMapper.findDistrictNameByDistrictId", id);
    }
    
    @Override
    public List<ProvinceEntity> findAllProvince()
        throws Exception
    {
        return getSqlSessionRead().selectList("AreaMapper.findAllProvince");
    }
    
    @Override
    public List<CityEntity> findAllCity()
        throws Exception
    {
        return getSqlSessionRead().selectList("AreaMapper.findAllCitys");
    }

    @Override
    public CityEntity findCityByCityId(String cityId)
        throws Exception
    {
        return getSqlSessionRead().selectOne("AreaMapper.findCityByCityId", cityId);
    }

    @Override
    public List<DistrictEntity> findAllDistrict()
        throws Exception
    {
        return getSqlSessionRead().selectList("AreaMapper.findAllDistricts");
    }
    
    @Override
    public List<CityEntity> findAllCityByProvinceId(int provinceId)
        throws Exception
    {
        List<CityEntity> reList = getSqlSession().selectList("AreaMapper.findAllCityByProvinceId", provinceId);
        return reList == null ? new ArrayList<CityEntity>() : reList;
    }
    
    @Override
    public List<DistrictEntity> findAllDistrictByCityId(int cityId)
        throws Exception
    {
        List<DistrictEntity> reList = getSqlSession().selectList("AreaMapper.findAllDistrictByCityId", cityId);
        return reList == null ? new ArrayList<DistrictEntity>() : reList;
    }
    
    @Override
    public int updateCityZipCode(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("AreaMapper.updateCityZipCode", para);
    }
    
    @Override
    public List<CityEntity> findAllCity(Map<String, Object> para)
        throws Exception
    {
        List<CityEntity> reList = getSqlSession().selectList("AreaMapper.findAllCity", para);
        return reList == null ? new ArrayList<CityEntity>() : reList;
    }
    
    @Override
    public List<DistrictEntity> findAllDistrict(Map<String, Object> para)
        throws Exception
    {
        List<DistrictEntity> reList = getSqlSession().selectList("AreaMapper.findAllDistrict", para);
        return reList == null ? new ArrayList<DistrictEntity>() : reList;
    }
    
    @Override
    public List<Map<String, Object>> findCityCodeByPara(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionRead().selectList("AreaMapper.findCityCodeByPara", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public List<Map<String, Object>> findDistrictCodeByPara(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionRead().selectList("AreaMapper.findDistrictCodeByPara", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public Set<String> findAllProvinceCode()
        throws Exception
    {
        List<String> reList = getSqlSessionRead().selectList("AreaMapper.findAllProvinceCode");
        return new HashSet<String>(reList);
    }
}
