package com.ygg.admin.entity;


public class MallWindowEntity
{
    /**商城位id*/
    private int id;
    
    /**商城页面id*/
    private int mallPageId;
    
    /**名称*/
    private String name = "";
    
    /**通用专场图片访问URL*/
    private String image = "";
    
    /**是否展示；0：否，1：是*/
    private int isDisplay = 1;
    
    /**备注*/
    private String remark = "";
    
    /**排序值，从大到小排序*/
    private int sequence;
    
    /**创建时间*/
    private String createTime;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getMallPageId()
    {
        return mallPageId;
    }
    
    public void setMallPageId(int mallPageId)
    {
        this.mallPageId = mallPageId;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getImage()
    {
        return image;
    }
    
    public void setImage(String image)
    {
        this.image = image;
    }
    
    public int getIsDisplay()
    {
        return isDisplay;
    }
    
    public void setIsDisplay(int isDisplay)
    {
        this.isDisplay = isDisplay;
    }
    
    public String getRemark()
    {
        return remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
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
    
}
