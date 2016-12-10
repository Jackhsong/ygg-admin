package com.ygg.admin.entity;


public class CustomPageEntity extends BaseEntity
{
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    
    private int id;
    
    private String name;
    
    private String desc;
    
    private String pcDetail;
    
    private String mobileDetail;
    
    private String url;
    
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
    
    public String getPcDetail()
    {
        return pcDetail;
    }
    
    public void setPcDetail(String pcDetail)
    {
        this.pcDetail = pcDetail;
    }
    
    public String getMobileDetail()
    {
        return mobileDetail;
    }
    
    public void setMobileDetail(String mobileDetail)
    {
        this.mobileDetail = mobileDetail;
    }
    
    public String getUrl()
    {
        return url;
    }
    
    public void setUrl(String url)
    {
        this.url = url;
    }
    
    public byte getIsAvailable()
    {
        return isAvailable;
    }
    
    public void setIsAvailable(byte isAvailable)
    {
        this.isAvailable = isAvailable;
    }
    
}
