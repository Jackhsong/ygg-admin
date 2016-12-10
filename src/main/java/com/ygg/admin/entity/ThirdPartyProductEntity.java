package com.ygg.admin.entity;

public class ThirdPartyProductEntity
{
    private int id;
    
    /**渠道ID*/
    private int channelId;
    
    /**仓库Id*/
    private int storageId;
    
    private String productCode;
    
    /**采购商品Id*/
    private int providerProductId;
    
    /**组合件数*/
    private int groupCount = 1;
    
    /**总库存*/
    private int totalStock;
    
    /**可用库存*/
    private int availableStock;
    
    /**累计销量*/
    private int totalSales;
    
    private Integer isAvailable;
    
    private int createUser;
    
    private String createTime;
    
    private int updateUser;
    
    private String updateTime;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getChannelId()
    {
        return channelId;
    }
    
    public void setChannelId(int channelId)
    {
        this.channelId = channelId;
    }
    
    public String getProductCode()
    {
        return productCode;
    }
    
    public void setProductCode(String productCode)
    {
        this.productCode = productCode;
    }
    
    public int getProviderProductId()
    {
        return providerProductId;
    }
    
    public void setProviderProductId(int providerProductId)
    {
        this.providerProductId = providerProductId;
    }
    
    public int getGroupCount()
    {
        return groupCount;
    }
    
    public void setGroupCount(int groupCount)
    {
        this.groupCount = groupCount;
    }
    
    public int getTotalStock()
    {
        return totalStock;
    }
    
    public void setTotalStock(int totalStock)
    {
        this.totalStock = totalStock;
    }
    
    public int getAvailableStock()
    {
        return availableStock;
    }
    
    public void setAvailableStock(int availableStock)
    {
        this.availableStock = availableStock;
    }
    
    public int getTotalSales()
    {
        return totalSales;
    }
    
    public void setTotalSales(int totalSales)
    {
        this.totalSales = totalSales;
    }
    
    public Integer getIsAvailable()
    {
        return isAvailable;
    }
    
    public void setIsAvailable(Integer isAvailable)
    {
        this.isAvailable = isAvailable;
    }
    
    public int getCreateUser()
    {
        return createUser;
    }
    
    public void setCreateUser(int createUser)
    {
        this.createUser = createUser;
    }
    
    public String getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }
    
    public int getUpdateUser()
    {
        return updateUser;
    }
    
    public void setUpdateUser(int updateUser)
    {
        this.updateUser = updateUser;
    }
    
    public String getUpdateTime()
    {
        return updateTime;
    }
    
    public void setUpdateTime(String updateTime)
    {
        this.updateTime = updateTime;
    }
    
    public int getStorageId()
    {
        return storageId;
    }
    
    public void setStorageId(int storageId)
    {
        this.storageId = storageId;
    }
    
}
