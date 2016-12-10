package com.ygg.admin.entity;

public class RelationSellerDeliverAreaEntity
{
    private int id;
    
    private int sellerId;
    
    private int provinceCode = 1;
    
    private int cityCode = 1;
    
    private int districtCode = 1;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getSellerId()
    {
        return sellerId;
    }
    
    public void setSellerId(int sellerId)
    {
        this.sellerId = sellerId;
    }
    
    public int getProvinceCode()
    {
        return provinceCode;
    }
    
    public void setProvinceCode(int provinceCode)
    {
        this.provinceCode = provinceCode;
    }
    
    public int getCityCode()
    {
        return cityCode;
    }
    
    public void setCityCode(int cityCode)
    {
        this.cityCode = cityCode;
    }
    
    public int getDistrictCode()
    {
        return districtCode;
    }
    
    public void setDistrictCode(int districtCode)
    {
        this.districtCode = districtCode;
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + cityCode;
        result = prime * result + districtCode;
        result = prime * result + id;
        result = prime * result + provinceCode;
        result = prime * result + sellerId;
        return result;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RelationSellerDeliverAreaEntity other = (RelationSellerDeliverAreaEntity)obj;
        if (sellerId != other.sellerId)
            return false;
        if (provinceCode != other.provinceCode)
        {
            return false;
        }
        else
        {
            if (cityCode != other.cityCode)
            {
                return false;
            }
            else
            {
                if (districtCode != other.districtCode)
                    return false;
                return true;
            }
        }
    }
    
}
