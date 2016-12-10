package com.ygg.admin.entity;

public class CategoryFirstEntity extends BaseEntity
{
    private static final long serialVersionUID = 1L;
    
    private int id;
    
    private String name = "";
    
    private String image1 = "";
    
    private String image2 = "";
    
    private int sequence;
    
    private String tag = "";
    
    private int isAvailable;
    
    private int isShowInApp;
    
    private String createTime;
    
    private String color;
    
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
    
    public int getSequence()
    {
        return sequence;
    }
    
    public void setSequence(int sequence)
    {
        this.sequence = sequence;
    }
    
    public String getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }
    
    public String getImage1()
    {
        return image1;
    }
    
    public void setImage1(String image1)
    {
        this.image1 = image1;
    }
    
    public String getImage2()
    {
        return image2;
    }
    
    public void setImage2(String image2)
    {
        this.image2 = image2;
    }
    
    public int getIsAvailable()
    {
        return isAvailable;
    }
    
    public void setIsAvailable(int isAvailable)
    {
        this.isAvailable = isAvailable;
    }
    
    public int getIsShowInApp()
    {
        return isShowInApp;
    }
    
    public void setIsShowInApp(int isShowInApp)
    {
        this.isShowInApp = isShowInApp;
    }
    
    public String getTag()
    {
        return tag;
    }
    
    public void setTag(String tag)
    {
        this.tag = tag;
    }
    
    public String getColor()
    {
        return color;
    }
    
    public void setColor(String color)
    {
        this.color = color;
    }
    
    @Override
    public String toString()
    {
        return "name:" + name;
    }
}
