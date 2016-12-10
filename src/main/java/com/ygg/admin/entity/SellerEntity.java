package com.ygg.admin.entity;

public class SellerEntity extends BaseEntity
{
    private static final long serialVersionUID = 1L;
    
    private String sellerName = "";
    
    private String realSellerName = "";
    
    private byte sellerType = 1;
    
    private String companyName = "";
    
    private String companyAddress = "";
    
    /**发货联系人*/
    private String fhContactPerson = "";
    
    /**发货联系人手机号*/
    private String fhContactMobile = "";
    
    /**发货联系人qq*/
    private String fhqq = "";
    
    /**发货联系人邮箱*/
    private String fhEmail = "";
    
    /**发货联系人旺旺号 */
    private String fhAliwang = "";
    
    /**发货联系人备注*/
    private String fhRemark = "";
    
    private String sendAddress = "";
    
    private String responsibilityPerson = "";
    
    private String warehouse = "";
    
    private byte isAvailable;
    
    private int sendTimeType;
    
    private String sendTimeRemark = "";
    
    /**seller_type等于2时，需要使用该字段，保税区物流单号类型；1：先有物流单号后报关，2：先报关后有物流单号*/
    private int bondedNumberType;
    
    /**是否需要身份证号*/
    private int isNeedIdCardNumber;
    
    /**是否需要身份证图片*/
    private int isNeedIdCardImage;
    
    /** 运费类型 */
    private int freightType;
    
    /** 运费 */
    private float freightMoney = 0.0f;
    
    /** 运费类型其它，freight_type等于4时，需要使用该字段 */
    private String freightOther = "";
    
    /** 发货依据 */
    private int sendCodeType;
    
    /** 发货类型其他， sendCodeType=4时，使用该字段 */
    private String sendCodeRemark = "";
    
    /** 结算周期 */
    private int settlementPeriod;
    
    /** 结算天数，当settlementPeriod=2时使用该字段*/
    private int settlementDay;
    
    /** 其他，当settlementPeriod=3时使用该字段*/
    private String settlementOther = "";
    
    /**售后联系人*/
    private String shContactPerson = "";
    
    /**售后联系人手机号*/
    private String shContactMobile = "";
    
    /**售后联系人qq*/
    private String shqq = "";
    
    /**售后联系人邮箱*/
    private String shEmail = "";
    
    /**售后联系人旺旺号 */
    private String shAliwang = "";
    
    /**售后联系人备注*/
    private String shRemark = "";
    
    /**结算联系人*/
    private String jsContactPerson = "";
    
    /**结算联系人手机号*/
    private String jsContactMobile = "";
    
    /**结算联系人qq*/
    private String jsqq = "";
    
    /**结算联系人邮箱*/
    private String jsEmail = "";
    
    /**结算联系人旺旺号 */
    private String jsAliwang = "";
    
    /**结算联系人备注*/
    private String jsRemark = "";
    
    /** 周末发货备注 */
    private String weekendRemark = "";
    
    /** 默认快递 */
    private String kuaidi = "";
    
    /** 其他快递 */
    private String otherKuaidi = "";
    
    /**周末是否发货，1：周末不发货，2：周六发货，3：周日发货，4：周末发货*/
    private int isSendWeekend = 1;
    
    private String sendWeekendRemark = "";
    
    /**预计发货后到货时间（以天为单位）*/
    private int expectArriveTime;
    
    /**配送地区描述*/
    private String deliverAreaDesc = "";
    
    /**配送地区类型，1：支持地区；2：不支持地区*/
    private int deliverAreaType = 1;
    
    /**假期发货提示*/
    private String holidayTips = "";
    
    /**假期开始时间*/
    private String holidayStartTime = "";
    
    /**假期结束时间*/
    private String holidayEndTime = "";
    
    /**是否使用商家后台*/
    private int isOwner;
    
    /**商家店铺网址*/
    private String shopURL;
    
    /**是否是自营商家，1是，0否*/
    private int isSelfSupport;
    
    /**运营联系人*/
    private String yyContactPerson = "";
    
    /**运营联系人手机号*/
    private String yyContactMobile = "";
    
    /**运营联系人qq*/
    private String yyqq = "";
    
    /**运营联系人邮箱*/
    private String yyEmail = "";
    
    /**运营联系人旺旺号 */
    private String yyAliwang = "";
    
    public String getWeekendRemark()
    {
        return weekendRemark;
    }
    
    public void setWeekendRemark(String weekendRemark)
    {
        this.weekendRemark = weekendRemark;
    }
    
    public String getRealSellerName()
    {
        return realSellerName;
    }
    
    public void setRealSellerName(String realSellerName)
    {
        this.realSellerName = realSellerName;
    }
    
    public String getWarehouse()
    {
        return warehouse;
    }
    
    public void setWarehouse(String warehouse)
    {
        this.warehouse = warehouse;
    }
    
    public String getResponsibilityPerson()
    {
        return responsibilityPerson;
    }
    
    public void setResponsibilityPerson(String responsibilityPerson)
    {
        this.responsibilityPerson = responsibilityPerson;
    }
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public String getSellerName()
    {
        return sellerName;
    }
    
    public void setSellerName(String sellerName)
    {
        this.sellerName = sellerName;
    }
    
    public byte getSellerType()
    {
        return sellerType;
    }
    
    public void setSellerType(byte sellerType)
    {
        this.sellerType = sellerType;
    }
    
    public String getCompanyName()
    {
        return companyName;
    }
    
    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }
    
    public String getCompanyAddress()
    {
        return companyAddress;
    }
    
    public void setCompanyAddress(String companyAddress)
    {
        this.companyAddress = companyAddress;
    }
    
    public String getSendAddress()
    {
        if (sendAddress == null)
            sendAddress = "";
        return sendAddress;
    }
    
    public void setSendAddress(String sendAddress)
    {
        this.sendAddress = sendAddress;
    }
    
    public byte getIsAvailable()
    {
        return isAvailable;
    }
    
    public void setIsAvailable(byte isAvailable)
    {
        this.isAvailable = isAvailable;
    }
    
    public String getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }
    
    public int getBondedNumberType()
    {
        return bondedNumberType;
    }
    
    public void setBondedNumberType(int bondedNumberType)
    {
        this.bondedNumberType = bondedNumberType;
    }
    
    public int getIsNeedIdCardNumber()
    {
        return isNeedIdCardNumber;
    }
    
    public void setIsNeedIdCardNumber(int isNeedIdCardNumber)
    {
        this.isNeedIdCardNumber = isNeedIdCardNumber;
    }
    
    public int getIsNeedIdCardImage()
    {
        return isNeedIdCardImage;
    }
    
    public void setIsNeedIdCardImage(int isNeedIdCardImage)
    {
        this.isNeedIdCardImage = isNeedIdCardImage;
    }
    
    public int getSendTimeType()
    {
        return sendTimeType;
    }
    
    public void setSendTimeType(int sendTimeType)
    {
        this.sendTimeType = sendTimeType;
    }
    
    public String getSendTimeRemark()
    {
        return sendTimeRemark;
    }
    
    public void setSendTimeRemark(String sendTimeRemark)
    {
        this.sendTimeRemark = sendTimeRemark;
    }
    
    public int getFreightType()
    {
        return freightType;
    }
    
    public void setFreightType(int freightType)
    {
        this.freightType = freightType;
    }
    
    public float getFreightMoney()
    {
        return freightMoney;
    }
    
    public void setFreightMoney(float freightMoney)
    {
        this.freightMoney = freightMoney;
    }
    
    public String getFreightOther()
    {
        return freightOther;
    }
    
    public void setFreightOther(String freightOther)
    {
        this.freightOther = freightOther;
    }
    
    public int getSendCodeType()
    {
        return sendCodeType;
    }
    
    public void setSendCodeType(int sendCodeType)
    {
        this.sendCodeType = sendCodeType;
    }
    
    public String getSendCodeRemark()
    {
        return sendCodeRemark;
    }
    
    public void setSendCodeRemark(String sendCodeRemark)
    {
        this.sendCodeRemark = sendCodeRemark;
    }
    
    public int getSettlementPeriod()
    {
        return settlementPeriod;
    }
    
    public void setSettlementPeriod(int settlementPeriod)
    {
        this.settlementPeriod = settlementPeriod;
    }
    
    public int getSettlementDay()
    {
        return settlementDay;
    }
    
    public void setSettlementDay(int settlementDay)
    {
        this.settlementDay = settlementDay;
    }
    
    public String getSettlementOther()
    {
        return settlementOther;
    }
    
    public void setSettlementOther(String settlementOther)
    {
        this.settlementOther = settlementOther;
    }
    
    public String getFhContactPerson()
    {
        return fhContactPerson;
    }
    
    public void setFhContactPerson(String fhContactPerson)
    {
        this.fhContactPerson = fhContactPerson;
    }
    
    public String getFhContactMobile()
    {
        return fhContactMobile;
    }
    
    public void setFhContactMobile(String fhContactMobile)
    {
        this.fhContactMobile = fhContactMobile;
    }
    
    public String getFhqq()
    {
        return fhqq;
    }
    
    public void setFhqq(String fhqq)
    {
        this.fhqq = fhqq;
    }
    
    public String getFhEmail()
    {
        return fhEmail;
    }
    
    public void setFhEmail(String fhEmail)
    {
        this.fhEmail = fhEmail;
    }
    
    public String getFhAliwang()
    {
        return fhAliwang;
    }
    
    public void setFhAliwang(String fhAliwang)
    {
        this.fhAliwang = fhAliwang;
    }
    
    public String getFhRemark()
    {
        return fhRemark;
    }
    
    public void setFhRemark(String fhRemark)
    {
        this.fhRemark = fhRemark;
    }
    
    public String getShContactPerson()
    {
        return shContactPerson;
    }
    
    public void setShContactPerson(String shContactPerson)
    {
        this.shContactPerson = shContactPerson;
    }
    
    public String getShContactMobile()
    {
        return shContactMobile;
    }
    
    public void setShContactMobile(String shContactMobile)
    {
        this.shContactMobile = shContactMobile;
    }
    
    public String getShqq()
    {
        return shqq;
    }
    
    public void setShqq(String shqq)
    {
        this.shqq = shqq;
    }
    
    public String getShEmail()
    {
        return shEmail;
    }
    
    public void setShEmail(String shEmail)
    {
        this.shEmail = shEmail;
    }
    
    public String getShAliwang()
    {
        return shAliwang;
    }
    
    public void setShAliwang(String shAliwang)
    {
        this.shAliwang = shAliwang;
    }
    
    public String getShRemark()
    {
        return shRemark;
    }
    
    public void setShRemark(String shRemark)
    {
        this.shRemark = shRemark;
    }
    
    public String getJsContactPerson()
    {
        return jsContactPerson;
    }
    
    public void setJsContactPerson(String jsContactPerson)
    {
        this.jsContactPerson = jsContactPerson;
    }
    
    public String getJsContactMobile()
    {
        return jsContactMobile;
    }
    
    public void setJsContactMobile(String jsContactMobile)
    {
        this.jsContactMobile = jsContactMobile;
    }
    
    public String getJsqq()
    {
        return jsqq;
    }
    
    public void setJsqq(String jsqq)
    {
        this.jsqq = jsqq;
    }
    
    public String getJsEmail()
    {
        return jsEmail;
    }
    
    public void setJsEmail(String jsEmail)
    {
        this.jsEmail = jsEmail;
    }
    
    public String getJsAliwang()
    {
        return jsAliwang;
    }
    
    public void setJsAliwang(String jsAliwang)
    {
        this.jsAliwang = jsAliwang;
    }
    
    public String getJsRemark()
    {
        return jsRemark;
    }
    
    public void setJsRemark(String jsRemark)
    {
        this.jsRemark = jsRemark;
    }
    
    @Override
    public String toString()
    {
        return "[id=" + id + ",realSellerName=" + realSellerName + ",companyName=" + companyName + ",sendAddress=" + sendAddress + "]";
    }
    
    public String getKuaidi()
    {
        return kuaidi;
    }
    
    public void setKuaidi(String kuaidi)
    {
        this.kuaidi = kuaidi;
    }
    
    public String getOtherKuaidi()
    {
        return otherKuaidi;
    }
    
    public void setOtherKuaidi(String otherKuaidi)
    {
        this.otherKuaidi = otherKuaidi;
    }
    
    public int getIsSendWeekend()
    {
        return isSendWeekend;
    }
    
    public void setIsSendWeekend(int isSendWeekend)
    {
        this.isSendWeekend = isSendWeekend;
    }
    
    public String getSendWeekendRemark()
    {
        return sendWeekendRemark;
    }
    
    public void setSendWeekendRemark(String sendWeekendRemark)
    {
        this.sendWeekendRemark = sendWeekendRemark;
    }
    
    public int getExpectArriveTime()
    {
        return expectArriveTime;
    }
    
    public void setExpectArriveTime(int expectArriveTime)
    {
        this.expectArriveTime = expectArriveTime;
    }
    
    public String getDeliverAreaDesc()
    {
        return deliverAreaDesc;
    }
    
    public void setDeliverAreaDesc(String deliverAreaDesc)
    {
        this.deliverAreaDesc = deliverAreaDesc;
    }
    
    public int getDeliverAreaType()
    {
        return deliverAreaType;
    }
    
    public void setDeliverAreaType(int deliverAreaType)
    {
        this.deliverAreaType = deliverAreaType;
    }
    
    public String getHolidayTips()
    {
        return holidayTips;
    }
    
    public void setHolidayTips(String holidayTips)
    {
        this.holidayTips = holidayTips;
    }
    
    public String getHolidayStartTime()
    {
        return holidayStartTime;
    }
    
    public void setHolidayStartTime(String holidayStartTime)
    {
        this.holidayStartTime = holidayStartTime;
    }
    
    public String getHolidayEndTime()
    {
        return holidayEndTime;
    }
    
    public void setHolidayEndTime(String holidayEndTime)
    {
        this.holidayEndTime = holidayEndTime;
    }
    
    public int getIsOwner()
    {
        return isOwner;
    }
    
    public void setIsOwner(int isOwner)
    {
        this.isOwner = isOwner;
    }
    
    public String getShopURL()
    {
        return shopURL;
    }
    
    public void setShopURL(String shopURL)
    {
        this.shopURL = shopURL;
    }
    
    public int getIsSelfSupport()
    {
        return isSelfSupport;
    }
    
    public void setIsSelfSupport(int isSelfSupport)
    {
        this.isSelfSupport = isSelfSupport;
    }
    
    public String getYyContactPerson()
    {
        return yyContactPerson;
    }
    
    public void setYyContactPerson(String yyContactPerson)
    {
        this.yyContactPerson = yyContactPerson;
    }
    
    public String getYyContactMobile()
    {
        return yyContactMobile;
    }
    
    public void setYyContactMobile(String yyContactMobile)
    {
        this.yyContactMobile = yyContactMobile;
    }
    
    public String getYyqq()
    {
        return yyqq;
    }
    
    public void setYyqq(String yyqq)
    {
        this.yyqq = yyqq;
    }
    
    public String getYyEmail()
    {
        return yyEmail;
    }
    
    public void setYyEmail(String yyEmail)
    {
        this.yyEmail = yyEmail;
    }
    
    public String getYyAliwang()
    {
        return yyAliwang;
    }
    
    public void setYyAliwang(String yyAliwang)
    {
        this.yyAliwang = yyAliwang;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (obj instanceof SellerEntity)
        {
            SellerEntity other = (SellerEntity)obj;
            return this.id == other.id;
        }
        return false;
    }
    
    @Override
    public int hashCode()
    {
        return id;
    }
}
