package com.ygg.admin.service;

import java.util.List;

import com.ygg.admin.entity.MwebGroupImageDetailEntity;

public interface MwebGroupImageDetailService
{
    public int updateMwebGroupImageDetail(MwebGroupImageDetailEntity mwebGroupImageDetailEntity);
    
    public MwebGroupImageDetailEntity getMwebGroupImageDetailById(int id);
    
    public int addMwebGroupImageDetail(MwebGroupImageDetailEntity mwebGroupImageDetailEntity);
    
    public int deleteMwebGroupImageDetailByNotInIdsAndMwebGroupProductId(int mwebGroupProductId, List<Integer> list);
}
