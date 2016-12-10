package com.ygg.admin.entity;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import com.ygg.admin.code.OrderEnum;
import com.ygg.admin.util.CommonEnum;

public class OrderInfoForManage extends BaseEntity
{
    
    private static final long serialVersionUID = 1L;
    
    private String oCreateTime = "";
    
    private String oPayTime = "";
    
    private String oSendTime = "";
    
    private int oId;
    
    private int accountId;
    
    /**
     * 付款渠道
     */
    private int payChannel;
    
    private String oNumber = "";
    
    /**
     * 订单来源
     */
    private String orderChannel = "";
    
    private String appVersion = "";
    
    /**
     * 商家备注
     */
    private String remark = "";
    
    /**客服备注*/
    private String remark2 = "";
    
    /**
     * 订单来源 -- app
     */
    private int oAppChannel = -1;
    
    private int oStatus;
    
    private String oStatusDescripton = "";
    
    private float oTotalPrice;
    
    private float oAdjustPrice;
    
    private float oRealPrice;
    
    private String raFullName = "";
    
    private String raMobileNumber = "";
    
    private String sSellerName = "";
    
    private String sSendAddress = "";
    
    /**
     * 假如没有这个值为 什么 0？
     */
    private int operaStatus;
    
    private String ologChannel = "";
    
    private String ologNumber = "";
    
    private float ologMoney;
    
    private float oSumTotalPrice;
    
    private String orderSource;
    
    private int isSettlement;
    
    public int getAccountId()
    {
        return accountId;
    }
    
    public void setAccountId(int accountId)
    {
        this.accountId = accountId;
    }
    
    public String getAppVersion()
    {
        return appVersion;
    }
    
    public void setAppVersion(String appVersion)
    {
        this.appVersion = appVersion;
    }
    
    public float getoAdjustPrice()
    {
        return oAdjustPrice;
    }
    
    public void setoAdjustPrice(float oAdjustPrice)
    {
        this.oAdjustPrice = oAdjustPrice;
    }
    
    public float getoRealPrice()
    {
        return oRealPrice;
    }
    
    public void setoRealPrice(float oRealPrice)
    {
        this.oRealPrice = oRealPrice;
    }
    
    public String getOrderSource()
    {
        return orderSource;
    }
    
    public void setOrderSource(String orderSource)
    {
        this.orderSource = orderSource;
    }
    
    public String getRemark()
    {
        return remark;
    }
    
    public int getPayChannel()
    {
        return payChannel;
    }
    
    public void setPayChannel(int payChannel)
    {
        this.payChannel = payChannel;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public String getOrderChannel()
    {
        return orderChannel;
    }
    
    public void setOrderChannel(String orderChannel)
    {
        this.orderChannel = orderChannel;
    }
    
    public String getoStatusDescripton()
    {
        return oStatusDescripton;
    }
    
    public void setoStatusDescripton(String oStatusDescripton)
    {
        this.oStatusDescripton = oStatusDescripton;
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
    
    public String getoSendTime()
    {
        return oSendTime;
    }
    
    public void setoSendTime(String oSendTime)
    {
        this.oSendTime = oSendTime;
    }
    
    public int getoId()
    {
        return oId;
    }
    
    public void setoId(int oId)
    {
        this.oId = oId;
    }
    
    public String getNumber()
    {
        return oNumber;
    }
    
    public void setNumber(String number)
    {
        this.oNumber = number;
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
        return oTotalPrice;
    }
    
    public void setoTotalPrice(float oTotalPrice)
    {
        this.oTotalPrice = oTotalPrice;
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
    
    public String getsSellerName()
    {
        return sSellerName;
    }
    
    public void setsSellerName(String sSellerName)
    {
        this.sSellerName = sSellerName;
    }
    
    public String getsSendAddress()
    {
        return sSendAddress;
    }
    
    public void setsSendAddress(String sSendAddress)
    {
        this.sSendAddress = sSendAddress;
    }
    
    public int getOperaStatus()
    {
        return operaStatus;
    }
    
    public void setOperaStatus(int operaStatus)
    {
        this.operaStatus = operaStatus;
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
    
    public float getoSumTotalPrice()
    {
        return oSumTotalPrice;
    }
    
    public String getoNumber()
    {
        return oNumber;
    }
    
    public void setoNumber(String oNumber)
    {
        this.oNumber = oNumber;
    }
    
    public float getOlogMoney()
    {
        return ologMoney;
    }
    
    public void setOlogMoney(float ologMoney)
    {
        this.ologMoney = ologMoney;
    }
    
    public void setoSumTotalPrice(float oSumTotalPrice)
    {
        this.oSumTotalPrice = oSumTotalPrice;
    }
    
    public int getoAppChannel()
    {
        return oAppChannel;
    }
    
    public void setoAppChannel(int oAppChannel)
    {
        this.oAppChannel = oAppChannel;
    }
    
    public int getIsSettlement()
    {
        return isSettlement;
    }
    
    public void setIsSettlement(int isSettlement)
    {
        this.isSettlement = isSettlement;
    }
    
    public String getRemark2()
    {
        return remark2;
    }
    
    public void setRemark2(String remark2)
    {
        this.remark2 = remark2;
    }
    
    public Map<String, Object> getMapValue()
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", oId);
        map.put("accountId", accountId);
        map.put("oCreateTime", oCreateTime);
        map.put("oPayTime", oPayTime);
        map.put("oSendTime", oSendTime);
        map.put("oStatus", oStatus);
        map.put("oPayChannel", OrderEnum.PAY_CHANNEL.getDescByCode(payChannel));
        map.put("remark", remark);
        map.put("remark2", remark2);
        map.put("oStatusDescripton", OrderEnum.ORDER_STATUS.getDescByCode(oStatus));
        map.put("oTotalPrice", new DecimalFormat("0.00").format(oTotalPrice));
        if (oStatus == OrderEnum.ORDER_STATUS.CREATE.getCode())
        {
            map.put("oRealPrice", new DecimalFormat("0.00").format(oTotalPrice - oAdjustPrice));
        }
        else
        {
            map.put("oRealPrice", new DecimalFormat("0.00").format(oRealPrice));
        }
        map.put("raFullName", raFullName);
        map.put("raMobileNumber", raMobileNumber);
        map.put("sSellerName", sSellerName);
        map.put("sSendAddress", sSendAddress);
        map.put("number", oNumber);
        // 尾1:app,2:wap
        if (oNumber.endsWith("1"))
        {
            appVersion = "".equals(appVersion) ? "" : "(" + appVersion + ")";
            map.put("orderChannel", CommonEnum.OrderAppChannelEnum.getDescriptionByOrdinal(oAppChannel) + appVersion);
            
        }
        else if (oNumber.endsWith("2"))
        {
            map.put("orderChannel", "网页");
        }
        map.put("orderSource", orderSource);
        // 待定
        map.put("operaStatus", operaStatus == 1 ? "已导出" : "未导出");
        map.put("ologChannel", ologChannel);
        map.put("ologNumber", ologNumber);
        map.put("ologMoney", ologMoney + "");
        map.put("oSumTotalPrice", oSumTotalPrice);
        map.put("isSettlement", isSettlement == 1 ? "已结算" : "未结算");
        return map;
    }
}
