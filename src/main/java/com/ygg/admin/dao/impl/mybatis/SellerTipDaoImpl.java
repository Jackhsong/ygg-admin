package com.ygg.admin.dao.impl.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.SellerTipDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;

@Repository("sellerTip")
public class SellerTipDaoImpl extends BaseDaoImpl implements SellerTipDao
{
    
    @Override
    public int save(Map<String, Object> param)
        throws Exception
    {
        return getSqlSession().insert("SellerTipMapper.save", param);
    }
    
    @Override
    public int update(Map<String, Object> param)
        throws Exception
    {
        return getSqlSession().insert("SellerTipMapper.update", param);
    }
    
    @Override
    public int countList(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionRead().selectOne("SellerTipMapper.countList", param);
    }
    
    @Override
    public List<Map<String, Object>> findListInfo(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionRead().selectList("SellerTipMapper.findListInfo", param);
    }
    
    @Override
    public Map<String, Object> findById(int id)
        throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        return getSqlSessionRead().selectOne("SellerTipMapper.findListInfo", param);
    }
    
    @Override
    public int checkTitle(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionRead().selectOne("SellerTipMapper.checkTitle", param);
    }
    
    @Override
    public int checkStatus(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionRead().selectOne("SellerTipMapper.checkStatus", param);
    }
    
}
