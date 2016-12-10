package com.ygg.admin.dao.impl.mybatis;

import com.ygg.admin.dao.OrderSignRecordDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zhangyb on 2015/9/22 0022.
 */
@Repository("orderSignRecordDao")
public class OrderSignRecordDaoImpl extends BaseDaoImpl implements OrderSignRecordDao
{
    @Override
    public List<Integer> findOrderSignRecord()
        throws Exception
    {
        return getSqlSessionAdmin().selectList("OrderSignRecordMapper.findOrderSignRecordWhereStatusEqual1");
//        return  null;
    }
    
    @Override
    public int updateOrderSignRecord(List<Integer> orderIdList)
        throws Exception
    {
        return getSqlSessionAdmin().update("OrderSignRecordMapper.updateOrderSignRecord", orderIdList);
//        return 0;
    }
}
