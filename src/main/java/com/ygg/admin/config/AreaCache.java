package com.ygg.admin.config;

import java.util.HashMap;
import java.util.Map;

/**
 * 地址缓存。项目启动时加载
 */
public class AreaCache
{
    private AreaCache()
    {
    }

    private volatile static AreaCache AREA_CACHE = null;

    public static AreaCache getInstance()
    {
        if (AREA_CACHE == null)
        {
            synchronized (AreaCache.class)
            {
                if (AREA_CACHE == null)
                {
                    AREA_CACHE = new AreaCache();
                }
            }
        }
        return AREA_CACHE;
    }

    // 省集合(key,value)形式
    private Map<String, String> provinceMap = new HashMap<>();

    // 市集合(key,value)形式
    private Map<String, String> cityMap = new HashMap<>();

    // 区集合(key,value)形式
    private Map<String, String> distinctMap = new HashMap<>();

    public Map<String, String> getProvinceMap()
    {
        return provinceMap;
    }

    public void setProvinceMap(Map<String, String> provinceMap)
    {
        this.provinceMap = provinceMap;
    }

    public Map<String, String> getCityMap()
    {
        return cityMap;
    }

    public void setCityMap(Map<String, String> cityMap)
    {
        this.cityMap = cityMap;
    }

    public Map<String, String> getDistinctMap()
    {
        return distinctMap;
    }

    public void setDistinctMap(Map<String, String> distinctMap)
    {
        this.distinctMap = distinctMap;
    }

    /**
     * 根据省份ID获取省份名称
     * @param provinceId
     * @return
     */
    public String getProvinceName(String provinceId)
    {
        if (provinceMap.containsKey(provinceId))
        {
            return provinceMap.get(provinceId);
        }
        return "";
    }

    /**
     * 根据城市ID获取城市名称
     * @param cityId
     * @return
     */
    public String getCityName(String cityId)
    {
        if (cityMap.containsKey(cityId))
        {
            return cityMap.get(cityId);
        }
        return "";
    }

    /**
     * 根据地区ID获取地区名称
     * @param distinctId
     * @return
     */
    public String getDistinctName(String distinctId)
    {
        if (distinctMap.containsKey(distinctId))
        {
            return distinctMap.get(distinctId);
        }
        return "";
    }

}
