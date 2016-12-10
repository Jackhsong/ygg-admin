package com.ygg.admin.service;

import com.ygg.admin.entity.ActivitiesOptionalPartEntity;

import java.util.Map;

public interface ActivityOptionalPartService
{
    Map<String, Object> findAllActivityOptionalPart(String status, int page, int rows)
        throws Exception;

    Map<String, Object> saveActivityOptionalPart(ActivitiesOptionalPartEntity activity)
        throws Exception;

    Map<String, Object> updateActivityOptionalPart(ActivitiesOptionalPartEntity activity)
            throws Exception;

    Map<String, Object> updateDisplayStatus(int id, int isAvailable)
            throws Exception;
}
