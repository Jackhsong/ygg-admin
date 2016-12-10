package com.ygg.admin.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class ReceiveAddressEntity extends BaseEntity
{
    
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    
    private int id;
    
    private int accountId;
    
    private String fullName;
    
    private String mobileNumber;
    
    private String detailAddress;
    
    private String idCard;
    
    private String province;
    
    private String city;
    
    private String district;
    
    private byte isDefault;
    
    private String updateTime;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getAccountId()
    {
        return accountId;
    }
    
    public void setAccountId(int accountId)
    {
        this.accountId = accountId;
    }
    
    public String getFullName()
    {
        return fullName;
    }
    
    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }
    
    public String getMobileNumber()
    {
        return mobileNumber;
    }
    
    public void setMobileNumber(String mobileNumber)
    {
        this.mobileNumber = mobileNumber;
    }
    
    public String getDetailAddress()
    {
        return detailAddress;
    }
    
    public void setDetailAddress(String detailAddress)
    {
        this.detailAddress = detailAddress;
    }
    
    public String getIdCard()
    {
        return idCard;
    }
    
    public void setIdCard(String idCard)
    {
        this.idCard = idCard;
    }
    
    public String getProvince()
    {
        return province;
    }
    
    public void setProvince(String province)
    {
        this.province = province;
    }
    
    public String getCity()
    {
        return city;
    }
    
    public void setCity(String city)
    {
        this.city = city;
    }
    
    public String getDistrict()
    {
        return district;
    }
    
    public void setDistrict(String district)
    {
        this.district = district;
    }
    
    public byte getIsDefault()
    {
        return isDefault;
    }
    
    public void setIsDefault(byte isDefault)
    {
        this.isDefault = isDefault;
    }
    
    public String getUpdateTime()
    {
        return updateTime;
    }
    
    public void setUpdateTime(String updateTime)
    {
        this.updateTime = updateTime;
    }
    
    public int hashCode()
    {
        return HashCodeBuilder.reflectionHashCode(this);
    }
    
    public boolean equals(Object obj)
    {
        return EqualsBuilder.reflectionEquals(this, obj);
    }
    
}
