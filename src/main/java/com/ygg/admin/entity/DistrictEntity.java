package com.ygg.admin.entity;

public class DistrictEntity
{
    private int id;
    
    private int cityId;
    
    private int districtId;
    
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
    
    public int getDistrictId()
    {
        return districtId;
    }
    
    public void setDistrictId(int districtId)
    {
        this.districtId = districtId;
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
