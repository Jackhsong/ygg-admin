package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.MwebGroupAccountDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.MwebGroupAccountEntity;

@Repository("mwebGroupAccountDao")
public class MwebGroupAccountDaoImpl extends BaseDaoImpl implements MwebGroupAccountDao
{
    
    @Override
    public List<MwebGroupAccountEntity> findAccoun(MwebGroupAccountEntity account)
        throws Exception
    {
        
        List<MwebGroupAccountEntity> reList = getSqlSessionRead().selectList("mwebGroupAccountMapper.findAccoun", account);
        return reList == null ? new ArrayList<MwebGroupAccountEntity>() : reList;
    }
    
    @Override
    public MwebGroupAccountEntity getAccounById(int id)
        throws Exception
    {
        return getSqlSessionRead().selectOne("mwebGroupAccountMapper.getAccounById", id);
    }
    
    @Override
    public MwebGroupAccountEntity getAccounByOpenId(String openId)
        throws Exception
    {
        return getSqlSessionRead().selectOne("mwebGroupAccountMapper.getAccounByOpenId", openId);
    }
    
    @Override
    public MwebGroupAccountEntity getAccounByUnionId(String unionId)
        throws Exception
    {
        return getSqlSessionRead().selectOne("mwebGroupAccountMapper.getAccounByUnionId", unionId);
    }
    
    @Override
    public MwebGroupAccountEntity getAccounByBaseAccountId(int baseAccountId)
        throws Exception
    {
        return getSqlSessionRead().selectOne("mwebGroupAccountMapper.getAccounByBaseAccountId", baseAccountId);
    }
    
}
