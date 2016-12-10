package com.ygg.admin.entity;

import java.math.BigDecimal;

import com.ygg.admin.code.OrderEnum;
import org.apache.commons.lang.StringUtils;

import com.ygg.admin.util.CommonConstant;
import com.ygg.admin.util.CommonEnum;

public class OrderDetailInfoForSeller extends BaseEntity
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * String[] str = {"订单编号","订单状态","创建时间","付款日期","收货人","身份证号码","收货地址","省","市","区" ,"详细地址","联系电话",
     * "商品编号","商品名称","件数","单价","总价","运费","订单总价","买家备注","卖家备注","发货时间" ,"物流公司","物流单号"};
     */
    
    /**
     * 订单ID
     */
    private String oId = "";
    
    /**
     * 订单编号
     */
    private String oNumber = "";
    
    /**
     * 订单状态
     */
    private int oStatus;
    
    /**
     * 订单状态 - 语言描述
     */
    private String oStatusDescripton;
    
    /**
     * 订单总价
     */
    private float oTotalPrice;
    
    /** 优惠券金额 */
    private float oCouponPrice;
    
    /** 实付金额 */
    private float oRealPrice;
    
    /** 客服调整价格 */
    private float oAdjustPrice;
    
    /** 使用积分 */
    private int oAccountPoint;
    
    /**
     * 创建时间
     */
    private String oCreateTime = "";
    
    /**
     * 付款日期
     */
    private String oPayTime = "";
    
    /**
     * 付款渠道
     */
    private String payChannel = "";
    
    /**
     * 发货时间
     */
    private String oSendTime = "";
    
    /**
     * 收货人
     */
    private String raFullName = "";
    
    /**
     * 联系电话
     */
    private String raMobileNumber;
    
    /**
     * 身份证号码
     */
    private String raIdCard = "";
    
    /**
     * 收货地址 = 省 + 市 + 区 + 详细地址
     */
    private String address = "";
    
    /**
     * 收货地址 - 省
     */
    private String raProvince = "";
    
    /**
     * 收货地址 - 市
     */
    private String raCity = "";
    
    /**
     * 收货地址 - 区
     */
    private String raDistrict = "";
    
    /**
     * 收货地址 - 详细地址
     */
    private String raDetailAddress = "";
    
    /**
     * 商家ID
     */
    private String sId;
    
    /**
     * 商品编号
     */
    private String productCode = "";
    
    /**
     * 商品名称
     */
    private String productName = "";
    
    /**
     * 件数
     */
    private int productCount;
    
    /**
     * 单价
     */
    private double salesPrice;
    
    private double groupPrice;
    
    private int isGroup;
    
    /**
     * 件数 x 单价
     */
    private double smailTotalPrice;
    
    /**
     * 运费
     */
    private double oFreightMoney;
    
    /**
     * 买家备注 -- 暂不支持
     */
    private String buyerMarks;
    
    /**
     * 卖家备注
     */
    private String sellerMarks;
    
    /**
     * 物流公司
     */
    private String ologChannel = "";
    
    /**
     * 物流编号
     */
    private String ologNumber = "";
    
    /**
     * 物流邮费，暂不处理
     */
    private double ologMoney;
    
    private Byte sellerType = null;
    
    /** 商家名称 */
    private String sellerName;
    
    /** 商家发货地 */
    private String sendAddress;
    
    /** 仓库 */
    private String warehouse;
    
    /** 备注 */
    private String remark;
    
    /** 类型*/
    private String sendType;
    
    /** 是否结算*/
    private int isSettlement;
    
    /** 冻结状态 */
    private int isFreeze;
    
    /**用户名*/
    private String aName;
    
    /**用户Id*/
    private String accountId;
    
    private String kefuRemark = "";
    
    /**客户端渠道*/
    private int appChannel;
    
    /**客户端版本*/
    private String appVersion;
    
    /**订单类型*/
    private int orderType;
    
    
    /**订单类型描述*/
    private String orderTypeDescripton;
    
    // public Map<String,Object> getMapValue(){
    // Map<String, Object> map= new HashMap<String, Object>();
    // map.put("id",oId);
    // map.put("oCreateTime",oCreateTime);
    // map.put("oPayTime",oPayTime);
    // map.put("oStatus", oStatus);
    // map.put("oStatusDescripton",OrderEnum.OrderStatusEnum.getDescByCode(oStatus));
    // map.put("oTotalPrice",oTotalPrice);
    // map.put("raFullName",raFullName);
    // map.put("raMobileNumber",raMobileNumber);
    // map.put("raIdCard", raIdCard);
    // map.put("province", province);
    // map.put("city", city);
    // map.put("district", district);
    // map.put("detailAddress", detailAddress);
    // map.put("", value)
    // map.put("oNumber", oNumber);
    //
    // if (ologChannel != null && !"".equals(ologChannel)) {
    // map.put("ologChannel",ologChannel);
    // }else {
    // map.put("ologChannel","");
    // }
    //
    // if (ologNumber != null && !"".equals(ologNumber)) {
    // map.put("ologNumber",ologNumber);
    // }else {
    // map.put("ologNumber","");
    // }
    // return map;
    // }
    
    public String getsId()
    {
        return sId;
    }
    
    public void setsId(String sId)
    {
        this.sId = sId;
    }
    
    public String getSendType()
    {
        return sendType;
    }
    
    public double getGroupPrice()
    {
        return groupPrice;
    }
    
    public void setGroupPrice(double groupPrice)
    {
        this.groupPrice = groupPrice;
    }
    
    public int getIsGroup()
    {
        return isGroup;
    }
    
    public void setIsGroup(int isGroup)
    {
        this.isGroup = isGroup;
    }
    
    public String getKefuRemark()
    {
        return kefuRemark;
    }
    
    public void setKefuRemark(String kefuRemark)
    {
        this.kefuRemark = kefuRemark;
    }
    
    public String getaName()
    {
        return aName;
    }
    
    public void setaName(String aName)
    {
        this.aName = aName;
    }
    
    public float getoCouponPrice()
    {
        return oCouponPrice;
    }
    
    public void setoCouponPrice(float oCouponPrice)
    {
        this.oCouponPrice = oCouponPrice;
    }
    
    public float getoRealPrice()
    {
        return oRealPrice;
    }
    
    public void setoRealPrice(float oRealPrice)
    {
        this.oRealPrice = oRealPrice;
    }
    
    public float getoAdjustPrice()
    {
        return oAdjustPrice;
    }
    
    public void setoAdjustPrice(float oAdjustPrice)
    {
        this.oAdjustPrice = oAdjustPrice;
    }
    
    public int getoAccountPoint()
    {
        return oAccountPoint;
    }
    
    public void setoAccountPoint(int oAccountPoint)
    {
        this.oAccountPoint = oAccountPoint;
    }
    
    public void setSendType(String sendType)
    {
        this.sendType = sendType;
    }
    
    public String getSendAddress()
    {
        return sendAddress;
    }
    
    public Byte getSellerType()
    {
        return sellerType;
    }
    
    public void setSellerType(Byte sellerType)
    {
        this.sellerType = sellerType;
    }
    
    public void setSendAddress(String sendAddress)
    {
        this.sendAddress = sendAddress;
    }
    
    public String getWarehouse()
    {
        return warehouse;
    }
    
    public void setWarehouse(String warehouse)
    {
        this.warehouse = warehouse;
    }
    
    public String getoId()
    {
        return oId;
    }
    
    public String getRaProvince()
    {
        return raProvince;
    }
    
    public void setRaProvince(String raProvince)
    {
        this.raProvince = raProvince;
    }
    
    public String getRaCity()
    {
        return raCity;
    }
    
    public void setRaCity(String raCity)
    {
        this.raCity = raCity;
    }
    
    public String getRaDistrict()
    {
        return raDistrict;
    }
    
    public void setRaDistrict(String raDistrict)
    {
        this.raDistrict = raDistrict;
    }
    
    public String getRaDetailAddress()
    {
        return raDetailAddress;
    }
    
    public void setRaDetailAddress(String raDetailAddress)
    {
        this.raDetailAddress = raDetailAddress;
    }
    
    public String getSellerName()
    {
        return sellerName;
    }
    
    public void setSellerName(String sellerName)
    {
        this.sellerName = sellerName;
    }
    
    public String getoSendTime()
    {
        return oSendTime;
    }
    
    public void setoSendTime(String oSendTime)
    {
        this.oSendTime = oSendTime;
    }
    
    public double getSmailTotalPrice()
    {
        return smailTotalPrice;
    }
    
    public void setSmailTotalPrice(double smailTotalPrice)
    {
        this.smailTotalPrice = smailTotalPrice;
    }
    
    public String getAddress()
    {
        return address;
    }
    
    public void setAddress(String address)
    {
        this.address = address;
    }
    
    public String getoStatusDescripton()
    {
        return oStatusDescripton;
    }
    
    public void setoStatusDescripton(String oStatusDescripton)
    {
        this.oStatusDescripton = oStatusDescripton;
    }
    
    public void setoId(String oId)
    {
        this.oId = oId;
    }
    
    public String getoNumber()
    {
        return oNumber;
    }
    
    public void setoNumber(String oNumber)
    {
        this.oNumber = oNumber;
    }
    
    public int getoStatus()
    {
        return oStatus;
    }
    
    public void setoStatus(int oStatus)
    {
        this.oStatus = oStatus;
    }
    
    public float getoTotalPrice()
    {
        return new BigDecimal(oTotalPrice).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
    }
    
    public void setoTotalPrice(float oTotalPrice)
    {
        this.oTotalPrice = oTotalPrice;
    }
    
    public String getoCreateTime()
    {
        return oCreateTime;
    }
    
    public void setoCreateTime(String oCreateTime)
    {
        this.oCreateTime = oCreateTime;
    }
    
    public String getoPayTime()
    {
        return oPayTime;
    }
    
    public void setoPayTime(String oPayTime)
    {
        this.oPayTime = oPayTime;
    }
    
    public String getRaFullName()
    {
        return raFullName;
    }
    
    public void setRaFullName(String raFullName)
    {
        this.raFullName = raFullName;
    }
    
    public String getRaMobileNumber()
    {
        return raMobileNumber;
    }
    
    public void setRaMobileNumber(String raMobileNumber)
    {
        this.raMobileNumber = raMobileNumber;
    }
    
    public String getRaIdCard()
    {
        return raIdCard;
    }
    
    public void setRaIdCard(String raIdCard)
    {
        this.raIdCard = raIdCard;
    }
    
    public String getProvince()
    {
        return raProvince;
    }
    
    public void setProvince(String province)
    {
        this.raProvince = province;
    }
    
    public String getCity()
    {
        return raCity;
    }
    
    public void setCity(String city)
    {
        this.raCity = city;
    }
    
    public String getDistrict()
    {
        return raDistrict;
    }
    
    public void setDistrict(String district)
    {
        this.raDistrict = district;
    }
    
    public String getDetailAddress()
    {
        return raDetailAddress;
    }
    
    public void setDetailAddress(String detailAddress)
    {
        this.raDetailAddress = detailAddress;
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
    
    public int getProductCount()
    {
        return productCount;
    }
    
    public void setProductCount(int productCount)
    {
        this.productCount = productCount;
    }
    
    public double getSalesPrice()
    {
        return salesPrice;
    }
    
    public void setSalesPrice(double salesPrice)
    {
        this.salesPrice = salesPrice;
    }
    
    public double getoFreightMoney()
    {
        return oFreightMoney;
    }
    
    public void setoFreightMoney(double oFreightMoney)
    {
        this.oFreightMoney = oFreightMoney;
    }
    
    public String getBuyerMarks()
    {
        return buyerMarks;
    }
    
    public void setBuyerMarks(String buyerMarks)
    {
        this.buyerMarks = buyerMarks;
    }
    
    public String getSellerMarks()
    {
        return sellerMarks;
    }
    
    public void setSellerMarks(String sellerMarks)
    {
        this.sellerMarks = sellerMarks;
    }
    
    public String getOlogChannel()
    {
        return ologChannel;
    }
    
    public void setOlogChannel(String ologChannel)
    {
        this.ologChannel = ologChannel;
    }
    
    public String getOlogNumber()
    {
        return ologNumber;
    }
    
    public void setOlogNumber(String ologNumber)
    {
        this.ologNumber = ologNumber;
    }
    
    public double getOlogMoney()
    {
        return ologMoney;
    }
    
    public void setOlogMoney(double ologMoney)
    {
        this.ologMoney = ologMoney;
    }
    
    public String getRemark()
    {
        return remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public int getIsSettlement()
    {
        return isSettlement;
    }
    
    public void setIsSettlement(int isSettlement)
    {
        this.isSettlement = isSettlement;
    }
    
    public String getPayChannel()
    {
        return payChannel;
    }
    
    public void setPayChannel(String payChannel)
    {
        this.payChannel = payChannel;
    }
    
    public int getIsFreeze()
    {
        return isFreeze;
    }
    
    public void setIsFreeze(int isFreeze)
    {
        this.isFreeze = isFreeze;
    }
    
    public String getAccountId()
    {
        return accountId;
    }
    
    public void setAccountId(String accountId)
    {
        this.accountId = accountId;
    }
    
    public int getAppChannel()
    {
        return appChannel;
    }
    
    public void setAppChannel(int appChannel)
    {
        this.appChannel = appChannel;
    }
    
    public String getAppVersion()
    {
        return appVersion;
    }
    
    public void setAppVersion(String appVersion)
    {
        this.appVersion = appVersion;
    }
    
    public int getOrderType()
    {
        return orderType;
    }
    
    public void setOrderType(int orderType)
    {
        this.orderType = orderType;
    }
    
    public String getOrderTypeDescripton()
    {
        return orderTypeDescripton;
    }

    public void setOrderTypeDescripton(String orderTypeDescripton)
    {
        this.orderTypeDescripton = orderTypeDescripton;
    }

    public String getOrderChannel()
    {
        if (StringUtils.isEmpty(oNumber))
        {
            return "";
        }
        if (oNumber.endsWith(CommonConstant.ORDER_SUFFIX_APP))
        {
            return CommonEnum.OrderAppChannelEnum.getDescriptionByOrdinal(appChannel) + appVersion;
        }
        else if (oNumber.endsWith(CommonConstant.ORDER_SUFFIX_WAP))
        {
            if (orderType == OrderEnum.ORDER_TYPE.HUANQIUBUSHOU.getCode())
            {
                return "左岸城堡";
            }
            else
            {
                return "网页";
            }
        }
        else if (oNumber.endsWith(CommonConstant.ORDER_SUFFIX_GROUP))
        {
            return CommonEnum.OrderAppChannelEnum.getDescriptionByOrdinal(appChannel);
        }
        else
        {
            return "";
        }
    }
}
