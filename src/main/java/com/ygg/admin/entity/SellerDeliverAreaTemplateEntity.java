package com.ygg.admin.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;

public class SellerDeliverAreaTemplateEntity
{
    private int id;
    
    private int sellerId;
    
    private String name = "";
    
    private String desc = "";
    
    private byte type;
    
    private List<RelationDeliverAreaTemplateEntity> areas;
    
    private List<RelationDeliverAreaTemplateEntity> exceptAreas;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getSellerId()
    {
        return sellerId;
    }
    
    public void setSellerId(int sellerId)
    {
        this.sellerId = sellerId;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getDesc()
    {
        return desc;
    }
    
    public void setDesc(String desc)
    {
        this.desc = desc;
    }
    
    public byte getType()
    {
        return type;
    }
    
    public void setType(byte type)
    {
        this.type = type;
    }
    
    public List<RelationDeliverAreaTemplateEntity> getAreas()
    {
        return areas;
    }
    
    public void setAreas(List<RelationDeliverAreaTemplateEntity> areas)
    {
        this.areas = areas;
    }
    
    public List<RelationDeliverAreaTemplateEntity> getExceptAreas()
    {
        return exceptAreas;
    }
    
    public void setExceptAreas(List<RelationDeliverAreaTemplateEntity> exceptAreas)
    {
        this.exceptAreas = exceptAreas;
    }
    
    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
    
    public String getStringProvinceCodes()
    {
        Set<String> set = new HashSet<String>();
        for (RelationDeliverAreaTemplateEntity relationTemplateDeliverAreaEntity : areas)
        {
            set.add(relationTemplateDeliverAreaEntity.getProvinceCode());
        }
        StringBuilder sb = new StringBuilder();
        for (String s : set)
        {
            sb.append(s).append(",");
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }
    
    public List<String> getProvinceCodes()
    {
        Set<String> set = new HashSet<String>();
        for (RelationDeliverAreaTemplateEntity relationTemplateDeliverAreaEntity : areas)
        {
            set.add(relationTemplateDeliverAreaEntity.getProvinceCode());
        }
        return new ArrayList<String>(set);
    }
    
    public List<String> getCityCodes()
    {
        Set<String> set = new HashSet<String>();
        for (RelationDeliverAreaTemplateEntity relationTemplateDeliverAreaEntity : areas)
        {
            set.add(relationTemplateDeliverAreaEntity.getCityCode());
        }
        return new ArrayList<String>(set);
    }
    
    public List<String> getDistrictCodes()
    {
        Set<String> set = new HashSet<String>();
        for (RelationDeliverAreaTemplateEntity relationTemplateDeliverAreaEntity : areas)
        {
            set.add(relationTemplateDeliverAreaEntity.getDistrictCode());
        }
        return new ArrayList<String>(set);
    }
}
