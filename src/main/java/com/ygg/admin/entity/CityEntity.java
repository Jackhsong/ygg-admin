package com.ygg.admin.entity;

public class CityEntity
{
    private int id;
    
    private int cityId;
    
    private int provinceId;
    
    private String name;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getCityId()
    {
        return cityId;
    }
    
    public void setCityId(int cityId)
    {
        this.cityId = cityId;
    }
    
    public int getProvinceId()
    {
        return provinceId;
    }
    
    public void setProvinceId(int provinceId)
    {
        this.provinceId = provinceId;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
}
