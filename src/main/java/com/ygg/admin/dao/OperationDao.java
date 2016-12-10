package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.OperationEntity;

public interface OperationDao
{
    
    int save(OperationEntity op)
        throws Exception;
    
    List<OperationEntity> findAllOperationByPara(Map<String, Object> para)
        throws Exception;
    
    int countOperationByPara(Map<String, Object> para)
        throws Exception;
}
