package com.ygg.admin.entity;

public class RelationDeliverAreaTemplateEntity extends BaseEntity
{
    
    private static final long serialVersionUID = 7023339662434787024L;
    
    private int id;
    
    private int sellerDeliverAreaTemplateId;
    
    private String provinceCode = "1";
    
    private String cityCode = "1";
    
    private String districtCode = "1";
    
    private String provinceName = "";
    
    private String cityName = "";
    
    private String districtName = "";
    
    private int isExcept;
    
    public RelationDeliverAreaTemplateEntity()
    {
    }
    
    public RelationDeliverAreaTemplateEntity(String provinceCode, String cityCode, String districtCode)
    {
        super();
        this.provinceCode = provinceCode;
        this.cityCode = cityCode == null ? "" : cityCode;
        this.districtCode = districtCode == null ? "" : districtCode;
    }
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getSellerDeliverAreaTemplateId()
    {
        return sellerDeliverAreaTemplateId;
    }
    
    public void setSellerDeliverAreaTemplateId(int sellerDeliverAreaTemplateId)
    {
        this.sellerDeliverAreaTemplateId = sellerDeliverAreaTemplateId;
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
    
    public int getIsExcept()
    {
        return isExcept;
    }
    
    public void setIsExcept(int isExcept)
    {
        this.isExcept = isExcept;
    }
    
    public String getProvinceName()
    {
        return provinceName;
    }
    
    public void setProvinceName(String provinceName)
    {
        this.provinceName = provinceName;
    }
    
    public String getCityName()
    {
        return cityName;
    }
    
    public void setCityName(String cityName)
    {
        this.cityName = cityName;
    }
    
    public String getDistrictName()
    {
        return districtName;
    }
    
    public void setDistrictName(String districtName)
    {
        this.districtName = districtName;
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cityCode == null) ? 0 : cityCode.hashCode());
        result = prime * result + ((districtCode == null) ? 0 : districtCode.hashCode());
        result = prime * result + id;
        result = prime * result + ((provinceCode == null) ? 0 : provinceCode.hashCode());
        result = prime * result + sellerDeliverAreaTemplateId;
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
        RelationDeliverAreaTemplateEntity other = (RelationDeliverAreaTemplateEntity)obj;
        return this.toString().equals(other.toString());
        /*        if (cityCode == null)
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
                if (provinceCode == null)
                {
                    if (other.provinceCode != null)
                        return false;
                }
                else if (!provinceCode.equals(other.provinceCode))
                    return false;
                if (sellerDeliverAreaTemplateId != other.sellerDeliverAreaTemplateId)
                    return false;
                return true;*/
    }
    
    @Override
    public String toString()
    {
        return "provinceCode=" + provinceCode + ",cityCode=" + cityCode + ",districtCode=" + districtCode;
    }
}
