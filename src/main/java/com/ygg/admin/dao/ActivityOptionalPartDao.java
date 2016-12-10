package com.ygg.admin.dao;

import com.ygg.admin.entity.ActivitiesOptionalPartEntity;
import com.ygg.admin.entity.ActivitiyRelationProductEntity;
import com.ygg.admin.entity.ActivityManjianEntity;

import java.util.List;
import java.util.Map;

public interface ActivityOptionalPartDao
{
    
    List<ActivitiesOptionalPartEntity> findAllActivityOptionalPart(Map<String, Object> para)
        throws Exception;
    
    int countActivityOptionalPart(Map<String, Object> para)
        throws Exception;
    
    int saveActivityOptionalPart(ActivitiesOptionalPartEntity activity)
        throws Exception;
    
    int updateActivityOptionalPart(ActivitiesOptionalPartEntity activity)
        throws Exception;

    ActivitiesOptionalPartEntity findActivityOptionalPartById(int id)
        throws Exception;

}
