package com.ygg.admin.dao.impl.mybatis;

import com.ygg.admin.dao.SellerBlacklistDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商家黑名单 dao
 */
@Repository("sellerBlacklistDao")
public class SellerBlacklistDaoImpl extends BaseDaoImpl implements SellerBlacklistDao
{
    @Override
    public List<Map<String, Object>> findSellerBlackInfo(int type, int sellerId, int isAvailable, int page, int rows)
        throws Exception
    {
        Map<String,Object> map = new HashMap<>();
        map.put("type", type > 0 ? type : null);
        map.put("sellerId", sellerId > 0 ? sellerId : null);
        map.put("isAvailable", isAvailable);
        map.put("page", page);
        map.put("rows", rows);
        return getSqlSessionRead().selectList("SellerBlacklistMapper.findSellerBlackInfo", map);
    }
    
    @Override
    public int countSellerBlackInfo(int type, int sellerId, int isAvailable)
        throws Exception
    {
        Map<String,Object> map = new HashMap<>();
        map.put("type", type > 0 ? type : null);
        map.put("sellerId", sellerId > 0 ? sellerId : null);
        map.put("isAvailable", isAvailable);
        return getSqlSessionRead().selectOne("SellerBlacklistMapper.countSellerBlackInfo", map);
    }
    
    @Override
    public int saveSellerBlackInfo(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("SellerBlacklistMapper.saveSellerBlackInfo", para);
    }
    
    @Override
    public int updateSellerBlackInfo(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("SellerBlacklistMapper.updateSellerBlackInfo", para);
    }
    
    @Override
    public int deleteSellerBlackInfo(int id)
        throws Exception
    {
        return getSqlSession().delete("SellerBlacklistMapper.deleteSellerBlackInfo", id);
    }
}
