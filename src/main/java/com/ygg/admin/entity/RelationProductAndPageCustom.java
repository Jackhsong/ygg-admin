package com.ygg.admin.entity;

public class RelationProductAndPageCustom extends BaseEntity
{
    private static final long serialVersionUID = 1L;
    
    private int id = 0;
    
    private int productId = 0;
    
    private int pageCustomId = 0;
    
    private String marks = "";
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getProductId()
    {
        return productId;
    }
    
    public void setProductId(int productId)
    {
        this.productId = productId;
    }
    
    public int getPageCustomId()
    {
        return pageCustomId;
    }
    
    public void setPageCustomId(int pageCustomId)
    {
        this.pageCustomId = pageCustomId;
    }
    
    public String getMarks()
    {
        return marks;
    }
    
    public void setMarks(String marks)
    {
        this.marks = marks;
    }
    
}
