package com.ygg.admin.entity;

public class FreightTemplateEntity extends BaseEntity
{
    private static final long serialVersionUID = 1L;
    
    private int id;
    
    private String name;
    
    private String desc;
    
    private byte isAvailable;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
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
    
    public byte getIsAvailable()
    {
        return isAvailable;
    }
    
    public void setIsAvailable(byte isAvailable)
    {
        this.isAvailable = isAvailable;
    }
    
    @Override
    public String toString()
    {
        return id + ":" + name;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (obj instanceof FreightTemplateEntity)
        {
            FreightTemplateEntity other = (FreightTemplateEntity)obj;
            return this.id == other.id;
        }
        return false;
    }
    
    @Override
    public int hashCode()
    {
        return id;
    }
    
}
