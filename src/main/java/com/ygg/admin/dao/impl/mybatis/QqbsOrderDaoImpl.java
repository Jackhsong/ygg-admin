package com.ygg.admin.dao.impl.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.QqbsOrderDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;

@Repository("qqbsOrderDao")
public class QqbsOrderDaoImpl extends BaseDaoImpl implements QqbsOrderDao
{
    
    @Override
    public List<Map<String, Object>> findOrderList(Map<String, Object> param)
    {
        return getSqlSessionRead().selectList("QqbsOrderMapper.findOrderList", param);
    }
    
    @Override
    public int countOrderList(Map<String, Object> param)
    {
        return getSqlSessionRead().selectOne("QqbsOrderMapper.countOrderList", param);
    }
    
}
