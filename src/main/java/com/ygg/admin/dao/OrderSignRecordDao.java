package com.ygg.admin.dao;

import java.util.List;

public interface OrderSignRecordDao
{
    List<Integer> findOrderSignRecord()
        throws Exception;
        
    int updateOrderSignRecord(List<Integer> orderIdList)
        throws Exception;
        
}
