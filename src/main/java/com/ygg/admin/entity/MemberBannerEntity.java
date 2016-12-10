package com.ygg.admin.entity;

public class MemberBannerEntity
{
    private int id;
    
    private Byte type;
    
    private int displayId;
    
    private String desc;
    
    private String image;
    
    private Byte isDisplay;
    
    private int sequence;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public Byte getType()
    {
        return type;
    }
    
    public void setType(Byte type)
    {
        this.type = type;
    }
    
    public int getDisplayId()
    {
        return displayId;
    }
    
    public void setDisplayId(int displayId)
    {
        this.displayId = displayId;
    }
    
    public String getDesc()
    {
        return desc;
    }
    
    public void setDesc(String desc)
    {
        this.desc = desc;
    }
    
    public String getImage()
    {
        return image;
    }
    
    public void setImage(String image)
    {
        this.image = image;
    }
    
    public Byte getIsDisplay()
    {
        return isDisplay;
    }
    
    public void setIsDisplay(Byte isDisplay)
    {
        this.isDisplay = isDisplay;
    }
    
    public int getSequence()
    {
        return sequence;
    }
    
    public void setSequence(int sequence)
    {
        this.sequence = sequence;
    }
}
