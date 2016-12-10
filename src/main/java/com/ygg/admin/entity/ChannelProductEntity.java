package com.ygg.admin.entity;

public class ChannelProductEntity
{

    /**第三方商品ID*/
    private int id;
    
    /**渠道ID*/
    private int channelId;
    
    /**商品编码*/
    private String productCode;
    
    /**商品名称*/
    private String productName;
    
    /**仓库Id*/
    private int wareHouseId;
    
    /**组合销售件数*/
    private int assembleCount = 1;
    
    /**是否可用*/
    private Integer isAvailable;
    
    private int createUser;
    
    private String createTime;
    
    private String updateTime;
    
    /**渠道名称*/
    private String channelName;
    
    /**仓库名称*/
    private String wareHouseName;

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

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public int getWareHouseId()
    {
        return wareHouseId;
    }

    public void setWareHouseId(int wareHouseId)
    {
        this.wareHouseId = wareHouseId;
    }

    public int getAssembleCount()
    {
        return assembleCount;
    }

    public void setAssembleCount(int assembleCount)
    {
        this.assembleCount = assembleCount;
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

    public String getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(String updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getChannelName()
    {
        return channelName;
    }

    public void setChannelName(String channelName)
    {
        this.channelName = channelName;
    }

    public String getWareHouseName()
    {
        return wareHouseName;
    }

    public void setWareHouseName(String wareHouseName)
    {
        this.wareHouseName = wareHouseName;
    }
    
}
