package com.ygg.admin.entity;

public class RelationProductBaseDeliverAreaEntity
{
    private int id;
    
    private int productBaseId;
    
    private String provinceCode = "1";
    
    private String cityCode = "1";
    
    private String districtCode = "1";
    
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
    
    public String getProvinceCode()
    {
        return provinceCode;
    }
    
    public void setProvinceCode(String provinceCode)
    {
        this.provinceCode = provinceCode;
    }
    
    public String getCityCode()
    {
        return cityCode;
    }
    
    public void setCityCode(String cityCode)
    {
        this.cityCode = cityCode;
    }
    
    public String getDistrictCode()
    {
        return districtCode;
    }
    
    public void setDistrictCode(String districtCode)
    {
        this.districtCode = districtCode;
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cityCode == null) ? 0 : cityCode.hashCode());
        result = prime * result + ((districtCode == null) ? 0 : districtCode.hashCode());
        result = prime * result + id;
        result = prime * result + productBaseId;
        result = prime * result + ((provinceCode == null) ? 0 : provinceCode.hashCode());
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
        RelationProductBaseDeliverAreaEntity other = (RelationProductBaseDeliverAreaEntity)obj;
        if (cityCode == null)
        {
            if (other.cityCode != null)
                return false;
        }
        else if (!cityCode.equals(other.cityCode))
            return false;
        if (districtCode == null)
        {
            if (other.districtCode != null)
                return false;
        }
        else if (!districtCode.equals(other.districtCode))
            return false;
        if (id != other.id)
            return false;
        if (productBaseId != other.productBaseId)
            return false;
        if (provinceCode == null)
        {
            if (other.provinceCode != null)
                return false;
        }
        else if (!provinceCode.equals(other.provinceCode))
            return false;
        return true;
    }
    
    @Override
    public String toString()
    {
        return "provinceCode=" + provinceCode + ",cityCode=" + cityCode + ",districtCode=" + districtCode;
    }
}
