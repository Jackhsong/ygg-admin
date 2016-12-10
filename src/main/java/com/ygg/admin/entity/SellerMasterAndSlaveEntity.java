package com.ygg.admin.entity;

public class SellerMasterAndSlaveEntity extends BaseEntity
{
    /**
     * 
     */
    private static final long serialVersionUID = -5598641443889039923L;
    
    private int sellerMasterId;
    
    private int sellerSlaveId;
    
    public int getSellerMasterId()
    {
        return sellerMasterId;
    }
    
    public void setSellerMasterId(int sellerMasterId)
    {
        this.sellerMasterId = sellerMasterId;
    }
    
    public int getSellerSlaveId()
    {
        return sellerSlaveId;
    }
    
    public void setSellerSlaveId(int sellerSlaveId)
    {
        this.sellerSlaveId = sellerSlaveId;
    }
    
}
