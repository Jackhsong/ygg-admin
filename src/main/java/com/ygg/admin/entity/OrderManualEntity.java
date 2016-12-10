package com.ygg.admin.entity;

public class OrderManualEntity extends BaseEntity
{
    private static final long serialVersionUID = 1L;
    
    private int id;
    
    /**
     * 订单编号
     */
    private long number;
    
    /**
     * 订单总价
     */
    private double totalPrice;
    
    /**
     * 商家ID
     */
    private int sellerId;
    
    /**
     * 订单类型；1：普通订单，2：海外购订单
     */
    private byte type;
    
    /**
     * 收货人姓名
     */
    private String fullName;
    
    /**
     * 身份证号码
     */
    private String idCard;
    
    /**
     * 手机号码
     */
    private String mobileNumber;
    
    /**
     * 详细地址
     */
    private String detailAddress;
    
    /**
     * 省
     */
    private String province;
    
    /**
     * 市
     */
    private String city;
    
    /**
     * 区
     */
    private String district;
    
    /**
     * 建单原因
     */
    private String remark;
    
    /**
     * 状态
     */
    private byte status;
    
    /**
     * 创建时间
     */
    private String createTime;
    
    /**
     * 发货时间
     */
    private String sendTime;
    
    /**
     * 物流渠道
     */
    private String logisticsChannel;
    
    /**
     * 物流单号
     */
    private String logisticsNumber;
    
    /**
     * 建单备注
     */
    private String desc;
    
    /**1:售后补发货；2:顾客打款过来请求发货*/
    private int sendType;
    
    /**打款账户Id */
    private int transferAccount;
    
    /**打款时间*/
    private String transferTime;
    
    /** 是否结算：1:是；0:否 */
    private int isSettlement;

    /** 订单是否需要结算 */
    private int isNeedSettlement;
    
    // ------------------------展示所用---------begin---------------------------------
    
    /** 商家名称 */
    private String sellerName = null;
    
    /** 发货地 */
    private String sendAddress = null;
    
    /** 状态展示文字 */
    private String statusDescripton = null;
    
    /** 物流渠道 中文描述 */
    private String logisticsChannelDescription = null;
    
    // ------------------------展示所用---------end-----------------------------------
    
    public String getStatusDescripton()
    {
        if (statusDescripton == null)
        {
            if (status == 1)
            {
                statusDescripton = "待发货";
            }
            else if (status == 2)
            {
                statusDescripton = "已发货";
                
            }
            else if (status == 3)
            {
                statusDescripton = "客服取消";
            }
            else
            {
                statusDescripton = "";
            }
        }
        return statusDescripton;
    }
    
    public String getSellerName()
    {
        return sellerName;
    }
    
    public void setSellerName(String sellerName)
    {
        this.sellerName = sellerName;
    }
    
    public String getSendAddress()
    {
        return sendAddress;
    }
    
    public void setSendAddress(String sendAddress)
    {
        this.sendAddress = sendAddress;
    }
    
    public void setStatusDescripton(String statusDescripton)
    {
        this.statusDescripton = statusDescripton;
    }
    
    public String getLogisticsChannelDescription()
    {
        return logisticsChannelDescription;
    }
    
    public int getIsSettlement()
    {
        return isSettlement;
    }
    
    public void setIsSettlement(int isSettlement)
    {
        this.isSettlement = isSettlement;
    }
    
    public void setLogisticsChannelDescription(String logisticsChannelDescription)
    {
        this.logisticsChannelDescription = logisticsChannelDescription;
    }
    
    public String getSendTime()
    {
        return sendTime;
    }
    
    public void setSendTime(String sendTime)
    {
        this.sendTime = sendTime;
    }
    
    public byte getStatus()
    {
        return status;
    }
    
    public void setStatus(byte status)
    {
        this.status = status;
    }
    
    public String getLogisticsChannel()
    {
        return logisticsChannel;
    }
    
    public void setLogisticsChannel(String logisticsChannel)
    {
        this.logisticsChannel = logisticsChannel;
    }
    
    public String getLogisticsNumber()
    {
        return logisticsNumber;
    }
    
    public void setLogisticsNumber(String logisticsNumber)
    {
        this.logisticsNumber = logisticsNumber;
    }
    
    public int getId()
    {
        return id;
    }
    
    public int getSellerId()
    {
        return sellerId;
    }
    
    public void setSellerId(int sellerId)
    {
        this.sellerId = sellerId;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public long getNumber()
    {
        return number;
    }
    
    public void setNumber(long number)
    {
        this.number = number;
    }
    
    public byte getType()
    {
        return type;
    }
    
    public void setType(byte type)
    {
        this.type = type;
    }
    
    public String getFullName()
    {
        return fullName;
    }
    
    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }
    
    public String getIdCard()
    {
        return idCard;
    }
    
    public void setIdCard(String idCard)
    {
        this.idCard = idCard;
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
    
    public String getRemark()
    {
        return remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public String getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }
    
    public double getTotalPrice()
    {
        return totalPrice;
    }
    
    public void setTotalPrice(double totalPrice)
    {
        this.totalPrice = totalPrice;
    }
    
    public String getDesc()
    {
        return desc;
    }
    
    public void setDesc(String desc)
    {
        this.desc = desc;
    }
    
    public int getSendType()
    {
        return sendType;
    }
    
    public void setSendType(int sendType)
    {
        this.sendType = sendType;
    }
    
    public int getTransferAccount()
    {
        return transferAccount;
    }
    
    public void setTransferAccount(int transferAccount)
    {
        this.transferAccount = transferAccount;
    }
    
    public String getTransferTime()
    {
        return transferTime;
    }
    
    public void setTransferTime(String transferTime)
    {
        this.transferTime = transferTime;
    }

    public int getIsNeedSettlement()
    {
        return isNeedSettlement;
    }

    public void setIsNeedSettlement(int isNeedSettlement)
    {
        this.isNeedSettlement = isNeedSettlement;
    }
}
