package com.ygg.admin.entity.qqbs;

import com.ygg.admin.entity.AccountEntity;
import com.ygg.admin.entity.base.BaseEntity;

/**
 * @author wuhy
 * @date 创建时间：2016年5月16日 下午6:12:31
 */
public class QRCodeEntity extends BaseEntity
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -4205378277035813054L;
    
    /**    */
    
    private int accountId;
    
    private String qrCodeUrl;
    
    private String creator;
    
    private AccountEntity account;
    
    public String getCreator()
    {
        return creator;
    }
    
    public void setCreator(String creator)
    {
        this.creator = creator;
    }
    
    public AccountEntity getAccount()
    {
        return account;
    }
    
    public void setAccount(AccountEntity account)
    {
        this.account = account;
    }
    
    public int getAccountId()
    {
        return accountId;
    }
    
    public void setAccountId(int accountId)
    {
        this.accountId = accountId;
    }
    
    public String getQrCodeUrl()
    {
        return qrCodeUrl;
    }
    
    public void setQrCodeUrl(String qrCodeUrl)
    {
        this.qrCodeUrl = qrCodeUrl;
    }
    
}
