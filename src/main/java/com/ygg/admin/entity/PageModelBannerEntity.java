package com.ygg.admin.entity;

public class PageModelBannerEntity
{
    private int id;
    
    private int pageModelId;
    
    private int type;
    
    private int displayId;
    
    private String desc;
    
    private String image;
    
    private String startTime;
    
    private String endTime;
    
    private int isDisplay;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getPageModelId()
    {
        return pageModelId;
    }
    
    public void setPageModelId(int pageModelId)
    {
        this.pageModelId = pageModelId;
    }
    
    public int getType()
    {
        return type;
    }
    
    public void setType(int type)
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
    
    public String getStartTime()
    {
        return startTime;
    }
    
    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }
    
    public String getEndTime()
    {
        return endTime;
    }
    
    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }
    
    public int getIsDisplay()
    {
        return isDisplay;
    }
    
    public void setIsDisplay(int isDisplay)
    {
        this.isDisplay = isDisplay;
    }
}
