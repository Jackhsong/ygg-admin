package com.ygg.admin.entity;

public class RelationActivitiesCommonAndProduct
{
    private static final long serialVersionUID = 1L;
    
    private int id;
    
    private int activitiesCommonId;
    
    private int productId;
    
    private int order;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getActivitiesCommonId()
    {
        return activitiesCommonId;
    }
    
    public void setActivitiesCommonId(int activitiesCommonId)
    {
        this.activitiesCommonId = activitiesCommonId;
    }
    
    public int getProductId()
    {
        return productId;
    }
    
    public void setProductId(int productId)
    {
        this.productId = productId;
    }
    
    public int getOrder()
    {
        return order;
    }
    
    public void setOrder(int order)
    {
        this.order = order;
    }
    
}
