package com.ygg.admin.entity;

public class AccountRecommendRelationEntity
{
    private int id;
    
    private int currentAccountId;
    
    private int fatherAccountId;
    
    private int currentIsPartner;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getCurrentAccountId()
    {
        return currentAccountId;
    }
    
    public void setCurrentAccountId(int currentAccountId)
    {
        this.currentAccountId = currentAccountId;
    }
    
    public int getFatherAccountId()
    {
        return fatherAccountId;
    }
    
    public void setFatherAccountId(int fatherAccountId)
    {
        this.fatherAccountId = fatherAccountId;
    }
    
    public int getCurrentIsPartner()
    {
        return currentIsPartner;
    }
    
    public void setCurrentIsPartner(int currentIsPartner)
    {
        this.currentIsPartner = currentIsPartner;
    }
    
}
