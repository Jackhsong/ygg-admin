package com.ygg.admin.entity;

public class RelationCategoryAndProductEntity
{
    private int id;
    
    private int productBaseId;
    
    private int productId;
    
    private int categoryThirdId;
    
    private int sequence;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getProductBaseId()
    {
        return productBaseId;
    }
    
    public void setProductBaseId(int productBaseId)
    {
        this.productBaseId = productBaseId;
    }
    
    public int getProductId()
    {
        return productId;
    }
    
    public void setProductId(int productId)
    {
        this.productId = productId;
    }
    
    public int getCategoryThirdId()
    {
        return categoryThirdId;
    }
    
    public void setCategoryThirdId(int categoryThirdId)
    {
        this.categoryThirdId = categoryThirdId;
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
