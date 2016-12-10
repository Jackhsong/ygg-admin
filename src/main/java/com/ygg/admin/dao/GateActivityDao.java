package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.GateEntity;

public interface GateActivityDao
{
    List<Map<String, Object>> findAllGates(Map<String, Object> para)
        throws Exception;
    
    int countGates(Map<String, Object> para)
        throws Exception;
    
    int addGate(GateEntity game)
        throws Exception;
    
    int updateGate(GateEntity game)
        throws Exception;
    
    int addGatePrize(Map<String, Object> para)
        throws Exception;
    
    int updateGatePrize(Map<String, Object> para)
        throws Exception;
    
    int updateGateStatus(Map<String, Object> para)
        throws Exception;
    
    int deleteGate(int id)
        throws Exception;
    
    int deleteGatePrize(int gameId)
        throws Exception;
    
}
