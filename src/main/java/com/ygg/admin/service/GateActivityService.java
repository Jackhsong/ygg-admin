package com.ygg.admin.service;

import java.util.Map;

public interface GateActivityService
{
    
    Map<String, Object> findAllGates(Map<String, Object> para)
        throws Exception;
    
    int saveOrUpdateGate(Map<String, Object> para)
        throws Exception;
    
    int deleteGate(int id)
        throws Exception;
    
    int updateGateDisplay(Map<String, Object> para)
        throws Exception;
    
}
