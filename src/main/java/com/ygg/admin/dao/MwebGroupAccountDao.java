package com.ygg.admin.dao;

import java.util.List;

import com.ygg.admin.entity.MwebGroupAccountEntity;

public interface MwebGroupAccountDao
{
    
    public List<MwebGroupAccountEntity> findAccoun(MwebGroupAccountEntity account)
        throws Exception;
        
    public MwebGroupAccountEntity getAccounById(int id)
        throws Exception;
        
    public MwebGroupAccountEntity getAccounByOpenId(String openId)
        throws Exception;
        
    public MwebGroupAccountEntity getAccounByUnionId(String unionId)
        throws Exception;
        
    public MwebGroupAccountEntity getAccounByBaseAccountId(int baseAccountId)
        throws Exception;
}
