package com.ygg.admin.entity;

public class CategoryActivityEntity
{
    private int id;
    
    private int relationType;
    
    private int relationObjectId;
    
    private String image;
    
    private int imageWidth;
    
    private int imageHeight;
    
    private String remark;
    
    private int sequence;
    
    private int isAvailable;
    
    private String createTime;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getRelationType()
    {
        return relationType;
    }
    
    public void setRelationType(int relationType)
    {
        this.relationType = relationType;
    }
    
    public int getRelationObjectId()
    {
        return relationObjectId;
    }
    
    public void setRelationObjectId(int relationObjectId)
    {
        this.relationObjectId = relationObjectId;
    }
    
    public String getImage()
    {
        return image;
    }
    
    public void setImage(String image)
    {
        this.image = image;
    }
    
    public int getImageWidth()
    {
        return imageWidth;
    }
    
    public void setImageWidth(int imageWidth)
    {
        this.imageWidth = imageWidth;
    }
    
    public int getImageHeight()
    {
        return imageHeight;
    }
    
    public void setImageHeight(int imageHeight)
    {
        this.imageHeight = imageHeight;
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
    
    public int getIsAvailable()
    {
        return isAvailable;
    }
    
    public void setIsAvailable(int isAvailable)
    {
        this.isAvailable = isAvailable;
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
