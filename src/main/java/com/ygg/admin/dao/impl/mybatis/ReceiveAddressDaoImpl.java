package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.ReceiveAddressDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.OrderReceiveAddress;
import com.ygg.admin.entity.ReceiveAddressEntity;

@Repository("receiveAddressDao")
public class ReceiveAddressDaoImpl extends BaseDaoImpl implements ReceiveAddressDao
{
    
    @Override
    public List<ReceiveAddressEntity> findAllReceiveAddressByPara(Map<String, Object> para)
        throws Exception
    {
        List<ReceiveAddressEntity> reList = getSqlSession().selectList("ReceiveAddressMapper.findAllReceiveAddressByPara", para);
        return reList == null ? new ArrayList<ReceiveAddressEntity>() : reList;
    }
    
    @Override
    public ReceiveAddressEntity findReceiveAddressById(int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        para.put("start", 0);
        para.put("max", 1);
        List<ReceiveAddressEntity> reList = findAllReceiveAddressByPara(para);
        if (reList.size() > 0)
        {
            return reList.get(0);
        }
        else
        {
            return null;
        }
    }
    
    @Override
    public OrderReceiveAddress findOrderReceiveAddressById(int id)
        throws Exception
    {
        return getSqlSession().selectOne("ReceiveAddressMapper.findOrderReceiveAddressById", id);
    }
    
}
