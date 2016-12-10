package com.ygg.admin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ygg.admin.dao.AreaDao;
import com.ygg.admin.entity.CityEntity;
import com.ygg.admin.entity.DistrictEntity;
import com.ygg.admin.entity.ProvinceEntity;
import com.ygg.admin.service.AreaService;

@Service("areaService")
public class AreaServiceImpl implements AreaService
{
    @Resource
    private AreaDao areaDao;
    
    @Override
    public List<ProvinceEntity> allProvince()
        throws Exception
    {
        // 所有省份信息
        List<ProvinceEntity> provinceList = areaDao.findAllProvince();
        return provinceList;
    }
    
    @Override
    public int setCityZipCode(List<Map<String, Object>> info)
        throws Exception
    {
        Map<String, String> cityAndZipCode = new HashMap<String, String>();
        List<CityEntity> citys = new ArrayList<CityEntity>();
        List<ProvinceEntity> allProvinces = areaDao.findAllProvince();
        
        for (ProvinceEntity it : allProvinces)
        {
            citys.addAll(areaDao.findAllCityByProvinceId(it.getProvinceId()));
        }
        
        for (CityEntity city : citys)
        {
            String key = city.getCityId() + "";
            String value = getValueByCityName(info, city.getName());
            cityAndZipCode.put(key, value);
        }
        Map<String, Object> par = new HashMap<String, Object>();
        for (Map.Entry<String, String> it : cityAndZipCode.entrySet())
        {
            String key = it.getKey();
            String value = it.getValue();
            par.put("zipCode", value);
            par.put("cityId", key);
            areaDao.updateCityZipCode(par);
        }
        return 1;
    }
    
    private String getValueByCityName(List<Map<String, Object>> info, String name)
    {
        for (Map<String, Object> map : info)
        {
            String city = map.get("city") + "";
            if (city.indexOf(name) > -1)
            {
                Integer zipCode = Integer.valueOf(map.get("zipCode") + "");
                Integer newCode = zipCode / 100 * 100;
                return newCode.toString();
            }
        }
        return "0";
    }
    
    @Override
    public List<CityEntity> findAllCity(Map<String, Object> para)
        throws Exception
    {
        return areaDao.findAllCity(para);
    }
    
    @Override
    public List<DistrictEntity> findAllDistrict(Map<String, Object> para)
        throws Exception
    {
        return areaDao.findAllDistrict(para);
    }
    
    @Override
    public Object findAllCity(int provinceId)
        throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        if (provinceId != -1)
            param.put("provincId", provinceId);
        
        return areaDao.findAllCity(param);
    }
    
    @Override
    public Object findAllDistrict(int cityId)
        throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        if (cityId != -1)
            param.put("cityId", cityId);
        
        return areaDao.findAllDistrict(param);
    }
    
    @Override
    public Object findAllProvince()
        throws Exception
    {
        List<ProvinceEntity> resultDB = areaDao.findAllProvince();
        return resultDB;
    }
    
}
