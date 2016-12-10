package com.ygg.admin.dao.impl.mybatis;

import com.ygg.admin.dao.ChannelProductManageDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.ChannelProductEntity;
import com.ygg.admin.exception.DaoException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ChannelProductManageDaoImpl extends BaseDaoImpl implements ChannelProductManageDao
{

    @Override
    public int addChannelProduct(ChannelProductEntity product)
            throws DaoException
    {
        return getSqlSession().insert("ChannelProMangeMapper.addChannelProduct", product);
    }
    
    @Override
    public int updateChannelProduct(ChannelProductEntity product)
            throws DaoException
    {
        return getSqlSession().update("ChannelProMangeMapper.updateChannelProduct", product);
    }

    @Override
    public Map<String, Object> getProductName(Map<String, Object> para)
            throws DaoException
    {
        return getSqlSessionRead().selectOne("ChannelProMangeMapper.getProductName", para);
    }

    @Override
    public List<Map<String, Object>> jsonChannelProInfo(Map<String, Object> para)
            throws DaoException
    {
        return getSqlSessionRead().selectList("ChannelProMangeMapper.findChannelProInfo", para);
    }

    @Override
    public int countChannelProInfo(Map<String, Object> para) throws DaoException
    {
        return getSqlSessionRead().selectOne("ChannelProMangeMapper.countChannelProInfo",para);
    }

    @Override
    public List<Map<String,Object>> getAllChannelNameAndId() throws DaoException
    {
        return getSqlSessionRead().selectList("ChannelProMangeMapper.getAllChannelNameAndId");
    }

    @Override
    public int batchAddChannelProduct(List<ChannelProductEntity> productList)
            throws DaoException
    {
        return getSqlSession().insert("ChannelProMangeMapper.batchAddChannelProduct",productList);
    }

    @Override
    public List<Map<String, Object>> findChannelProductByPara(Map<String, Object> para) {
        return getSqlSessionRead().selectList("ChannelProMangeMapper.findChannelProductByPara" ,para);
    }

}
