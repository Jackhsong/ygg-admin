package com.ygg.admin.entity;

/**
 * 商家主帐号
 * 
 * @author xiongl
 *
 */
public class SellerMasterEntity extends BaseEntity
{
    
    private static final long serialVersionUID = 5664067425935749843L;
    
    public int sellerId;
    
    public String loginName;
    
    public String password;
    
    public int isInformation;
    
    public String brandId;
    
    public int getSellerId()
    {
        return sellerId;
    }
    
    public void setSellerId(int sellerId)
    {
        this.sellerId = sellerId;
    }
    
    public String getLoginName()
    {
        return loginName;
    }
    
    public void setLoginName(String loginName)
    {
        this.loginName = loginName;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    public String getBrandId()
    {
        return brandId;
    }
    
    public void setBrandId(String brandId)
    {
        this.brandId = brandId;
    }
    
    public int getIsInformation()
    {
        return isInformation;
    }
    
    public void setIsInformation(int isInformation)
    {
        this.isInformation = isInformation;
    }
    
    @Override
    public String toString()
    {
        return "[id=" + id + ",sellerId=" + sellerId + ",loginName=" + loginName + "]";
    }
}
