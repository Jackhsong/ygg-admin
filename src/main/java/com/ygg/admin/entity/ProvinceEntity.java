package com.ygg.admin.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class ProvinceEntity extends BaseEntity
{
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    
    private int id;
    
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
    
    public int hashCode()
    {
        return HashCodeBuilder.reflectionHashCode(this);
    }
    
    public boolean equals(Object obj)
    {
        return EqualsBuilder.reflectionEquals(this, obj);
    }
    
}
