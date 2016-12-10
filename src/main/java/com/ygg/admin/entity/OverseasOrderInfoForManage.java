package com.ygg.admin.entity;

import com.ygg.admin.code.OrderEnum;

public class OverseasOrderInfoForManage extends BaseEntity
{
    
    private static final long serialVersionUID = 1L;
    
    private String createTime = "";
    
    private String payTime = "";
    
    private String sendTime = "";
    
    private int id;
    
    private String number = "";
    
    private int status;
    
    private String statusStr;
    
    private float totalPrice;
    
    private String fullName = "";
    
    private String mobileNumber = "";
    
    private String sellerName = "";
    
    private String sendAddress = "";
    
    /** 分仓 */
    private String warehouse = "";
    
    private String exportTime = "";
    
    private int exportStatus = 0;
    
    private String exportStatusStr = "";
    
    public String getWarehouse()
    {
        return warehouse;
    }
    
    public void setWarehouse(String warehouse)
    {
        this.warehouse = warehouse;
    }
    
    public int getExportStatus()
    {
        return exportStatus;
    }
    
    public void setExportStatus(int exportStatus)
    {
        this.exportStatus = exportStatus;
    }
    
    public String getExportStatusStr()
    {
        return exportStatus == 1 ? "已导出" : "未导出";
    }
    
    public void setExportStatusStr(String exportStatusStr)
    {
        this.exportStatusStr = exportStatusStr;
    }
    
    public String getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }
    
    public String getPayTime()
    {
        return payTime;
    }
    
    public void setPayTime(String payTime)
    {
        this.payTime = payTime;
    }
    
    public String getSendTime()
    {
        return sendTime;
    }
    
    public void setSendTime(String sendTime)
    {
        this.sendTime = sendTime;
    }
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public String getNumber()
    {
        return number;
    }
    
    public void setNumber(String number)
    {
        this.number = number;
    }
    
    public String getStatusStr()
    {
        return OrderEnum.ORDER_STATUS.getDescByCode(status);
    }
    
    public void setStatusStr(String statusStr)
    {
        this.statusStr = statusStr;
    }
    
    public int getStatus()
    {
        return status;
    }
    
    public void setStatus(int status)
    {
        this.status = status;
    }
    
    public float getTotalPrice()
    {
        return totalPrice;
    }
    
    public void setTotalPrice(float totalPrice)
    {
        this.totalPrice = totalPrice;
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
    
    public String getExportTime()
    {
        return exportTime;
    }
    
    public void setExportTime(String exportTime)
    {
        this.exportTime = exportTime;
    }
    
}
