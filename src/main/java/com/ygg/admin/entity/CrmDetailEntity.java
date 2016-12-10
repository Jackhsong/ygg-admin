package com.ygg.admin.entity;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class CrmDetailEntity implements Serializable
{
    
    private static final long serialVersionUID = -3443484961911643735L;
    
    private int id;
    private int crmAccountGroupId;
    private int accountId;
    private String phone;
    
    public CrmDetailEntity() {}
    
    public CrmDetailEntity(String json) {
        json = json.replace("{", "").replace("}", "").replaceAll("\"", "");
        String[] arr = StringUtils.split(json, ":");
        this.accountId = Integer.valueOf(arr[0]);
        this.phone = (arr.length == 1) ? "" : arr[1];
    }
    
    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }
    public int getCrmAccountGroupId()
    {
        return crmAccountGroupId;
    }
    public void setCrmAccountGroupId(int crmAccountGroupId)
    {
        this.crmAccountGroupId = crmAccountGroupId;
    }
    public int getAccountId()
    {
        return accountId;
    }
    public void setAccountId(int accountId)
    {
        this.accountId = accountId;
    }
    public String getPhone()
    {
        return phone;
    }
    public void setPhone(String phone)
    {
        this.phone = phone;
    }
    
    
}
