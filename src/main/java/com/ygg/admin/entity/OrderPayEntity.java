package com.ygg.admin.entity;

/**
 * 订单支付信息
 *
 * @author xiongl
 * @create 2016-05-03 14:15
 */
public class OrderPayEntity
{
    private int id;
    
    private int orderId;
    
    private String payMark;
    
    private String payTid;
    
    private int isPay;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getOrderId()
    {
        return orderId;
    }
    
    public void setOrderId(int orderId)
    {
        this.orderId = orderId;
    }
    
    public String getPayMark()
    {
        return payMark;
    }
    
    public void setPayMark(String payMark)
    {
        this.payMark = payMark;
    }
    
    public String getPayTid()
    {
        return payTid;
    }
    
    public void setPayTid(String payTid)
    {
        this.payTid = payTid;
    }
    
    public int getIsPay()
    {
        return isPay;
    }
    
    public void setIsPay(int isPay)
    {
        this.isPay = isPay;
    }
}
