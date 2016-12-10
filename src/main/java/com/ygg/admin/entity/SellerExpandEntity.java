package com.ygg.admin.entity;

/**
 * 商家扩展信息
 * @author xxxx表名真恶心
 *
 */
public class SellerExpandEntity extends BaseEntity
{
    /**
     * 
     */
    private static final long serialVersionUID = 1395373413362937287L;
    
    private int sellerId;
    
    /**审核人Id*/
    private int auditUserId;
    
    /**收件地址省编码*/
    private int receiveProvinceCode;
    
    /**收件地址市编码*/
    private int receiveCityCode;
    
    /**收件地址区编码*/
    private int receiveDistrictCode;
    
    /**收件详细地址*/
    private String receiveDetailAddress = "";
    
    /**收件人*/
    private String receivePerson = "";
    
    /**收件人电话*/
    private String receiveTelephone = "";
    
    /**建议结算方式，1：正常结算，2：扣点*/
    private int proposalSubmitType;
    
    /**建议扣点，proposalSubmitType=2时使用该字段*/
    private float proposalDeduction;
    
    /**日常运营对接人旺旺号*/
    private String rcAliwang = "";
    
    /**日常运营对接人邮箱*/
    private String rcEmail = "";
    
    /**日常运营对接人QQ*/
    private String rcqq = "";
    
    /**日常运营对接人手机*/
    private String rcContactMobile = "";
    
    /**日常运营对接人姓名*/
    private String rcContactPerson = "";
    
    /**商家店铺网址*/
    private String shopURL = "";
    
    /**商家品牌Id*/
    private String brandId = "";

    /**商家类别Id 对应一级类目的Id 保存修改时以逗号分割 */
    private String categoryId = "";

    /**商家登录名（使用商家后台才有值）*/
    private String username = "";
    
    /**商家登录密码（使用商家后台才有值）*/
    private String password = "";
    
    /**商家信息是否完善*/
    private int isInformation;
    
    /**是否是商家主帐号，1是，0否*/
    private int isMaster;

    /**保证金状态 @see DEPOSIT_STATUS */
    private int depositStatus;

    /**保证金金额 */
    private int depositCount;

    
    public int getSellerId()
    {
        return sellerId;
    }
    
    public void setSellerId(int sellerId)
    {
        this.sellerId = sellerId;
    }
    
    public int getAuditUserId()
    {
        return auditUserId;
    }
    
    public void setAuditUserId(int auditUserId)
    {
        this.auditUserId = auditUserId;
    }
    
    public int getReceiveProvinceCode()
    {
        return receiveProvinceCode;
    }
    
    public void setReceiveProvinceCode(int receiveProvinceCode)
    {
        this.receiveProvinceCode = receiveProvinceCode;
    }
    
    public int getReceiveCityCode()
    {
        return receiveCityCode;
    }
    
    public void setReceiveCityCode(int receiveCityCode)
    {
        this.receiveCityCode = receiveCityCode;
    }
    
    public int getReceiveDistrictCode()
    {
        return receiveDistrictCode;
    }
    
    public void setReceiveDistrictCode(int receiveDistrictCode)
    {
        this.receiveDistrictCode = receiveDistrictCode;
    }
    
    public String getReceiveDetailAddress()
    {
        return receiveDetailAddress;
    }
    
    public void setReceiveDetailAddress(String receiveDetailAddress)
    {
        this.receiveDetailAddress = receiveDetailAddress;
    }
    
    public String getReceivePerson()
    {
        return receivePerson;
    }
    
    public void setReceivePerson(String receivePerson)
    {
        this.receivePerson = receivePerson;
    }
    
    public String getReceiveTelephone()
    {
        return receiveTelephone;
    }
    
    public void setReceiveTelephone(String receiveTelephone)
    {
        this.receiveTelephone = receiveTelephone;
    }
    
    public String getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }
    
    public int getProposalSubmitType()
    {
        return proposalSubmitType;
    }
    
    public void setProposalSubmitType(int proposalSubmitType)
    {
        this.proposalSubmitType = proposalSubmitType;
    }
    
    public float getProposalDeduction()
    {
        return proposalDeduction;
    }
    
    public void setProposalDeduction(float proposalDeduction)
    {
        this.proposalDeduction = proposalDeduction;
    }
    
    public String getRcAliwang()
    {
        return rcAliwang;
    }
    
    public void setRcAliwang(String rcAliwang)
    {
        this.rcAliwang = rcAliwang;
    }
    
    public String getRcEmail()
    {
        return rcEmail;
    }
    
    public void setRcEmail(String rcEmail)
    {
        this.rcEmail = rcEmail;
    }
    
    public String getRcqq()
    {
        return rcqq;
    }
    
    public void setRcqq(String rcqq)
    {
        this.rcqq = rcqq;
    }
    
    public String getRcContactMobile()
    {
        return rcContactMobile;
    }
    
    public void setRcContactMobile(String rcContactMobile)
    {
        this.rcContactMobile = rcContactMobile;
    }
    
    public String getRcContactPerson()
    {
        return rcContactPerson;
    }
    
    public void setRcContactPerson(String rcContactPerson)
    {
        this.rcContactPerson = rcContactPerson;
    }
    
    public String getShopURL()
    {
        return shopURL;
    }
    
    public void setShopURL(String shopURL)
    {
        this.shopURL = shopURL;
    }
    
    public String getBrandId()
    {
        return brandId;
    }
    
    public void setBrandId(String brandId)
    {
        this.brandId = brandId;
    }
    
    public String getUsername()
    {
        return username;
    }
    
    public void setUsername(String username)
    {
        this.username = username;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    public int getIsInformation()
    {
        return isInformation;
    }
    
    public void setIsInformation(int isInformation)
    {
        this.isInformation = isInformation;
    }
    
    public int getIsMaster()
    {
        return isMaster;
    }
    
    public void setIsMaster(int isMaster)
    {
        this.isMaster = isMaster;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public int getDepositStatus() {
        return depositStatus;
    }

    public void setDepositStatus(int depositStatus) {
        this.depositStatus = depositStatus;
    }

    public int getDepositCount() {
        return depositCount;
    }

    public void setDepositCount(int depositCount) {
        this.depositCount = depositCount;
    }

    @Override
    public String toString()
    {
        return "[id=" + id + ",sellerId=" + sellerId + "]";
    }
}
