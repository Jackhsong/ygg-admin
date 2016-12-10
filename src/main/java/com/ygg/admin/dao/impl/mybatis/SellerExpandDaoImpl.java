package com.ygg.admin.dao.impl.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.SellerExpandDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.SellerExpandEntity;

@Repository("sellerExpandDao")
public class SellerExpandDaoImpl extends BaseDaoImpl implements SellerExpandDao
{
    
    @Override
    public int save(SellerExpandEntity sellerExpand)
        throws Exception
    {
        return this.getSqlSession().insert("SellerExpandMapper.save", sellerExpand);
    }
    
    @Override
    public int update(Map<String, Object> sellerExpand)
        throws Exception
    {
        return this.getSqlSession().update("SellerExpandMapper.update", sellerExpand);
    }
    
    @Override
    public SellerExpandEntity findSellerExpandBysid(int sellerId)
        throws Exception
    {
        return this.getSqlSession().selectOne("SellerExpandMapper.findSellerExpandBysid", sellerId);
    }
    
    @Override
    public int synchronousSellerExpandInfo(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("SellerExpandMapper.synchronousSellerExpandInfo", para);
    }
    
    @Override
    public int updateSellerToSlave(List<String> sellerIdList, int status)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("status", status);
        para.put("idList", sellerIdList);
        return getSqlSession().update("SellerExpandMapper.updateSellerToSlave", para);
    }
    
    @Override
    public int updatePassword(SellerExpandEntity sellerExpand)
        throws Exception
    {
        return getSqlSession().update("SellerExpandMapper.updatePassword", sellerExpand);
    }
    
    @Override
    public int updateSellerToMaster(String masterId)
        throws Exception
    {
        return getSqlSession().update("SellerExpandMapper.updateSellerToMaster", masterId);
    }
    
    @Override
    public List<SellerExpandEntity> findSellerExpandBysids(List<Integer> sellerIds)
        throws Exception
    {
        return getSqlSessionRead().selectList("SellerExpandMapper.findSellerExpandBysids", sellerIds);
    }

    @Override
    public int updateEcount(int sellerId)
    {
        return getSqlSession().update("SellerExpandMapper.updateEcount", sellerId);
    }
}
