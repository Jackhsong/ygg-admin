package com.ygg.admin.entity;

public class OrderProductRefundTeackEntity extends BaseEntity
{
    private static final long serialVersionUID = 1L;
    
    private int id;
    
    /**
     * 订单产品退款id
     */
    private int orderProductRefundId;
    
    /**
     * 管理员账号id
     */
    private int managerId;
    
    /**
     * 操作详情
     */
    private String content;
    
    /**
     * 处理步骤，1
     */
    private byte step;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 创建时间
     */
    private String createTime;
    
    public String getContent()
    {
        return content;
    }
    
    public void setContent(String content)
    {
        this.content = content;
    }
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getOrderProductRefundId()
    {
        return orderProductRefundId;
    }
    
    public void setOrderProductRefundId(int orderProductRefundId)
    {
        this.orderProductRefundId = orderProductRefundId;
    }
    
    public int getManagerId()
    {
        return managerId;
    }
    
    public void setManagerId(int managerId)
    {
        this.managerId = managerId;
    }
    
    public byte getStep()
    {
        return step;
    }
    
    public void setStep(byte step)
    {
        this.step = step;
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
    
}
