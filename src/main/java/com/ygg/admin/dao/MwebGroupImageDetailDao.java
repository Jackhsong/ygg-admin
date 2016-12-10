package com.ygg.admin.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.ygg.admin.entity.MwebGroupImageDetailEntity;

public interface MwebGroupImageDetailDao
{
    public List<MwebGroupImageDetailEntity> findMwebGroupImageDetail(MwebGroupImageDetailEntity mwebGroupImageDetailEntity);
    
    public int addMwebGroupImageDetail(MwebGroupImageDetailEntity mwebGroupImageDetailEntity);
    
    public int updateMwebGroupImageDetail(MwebGroupImageDetailEntity mwebGroupImageDetailEntity);
    
    public MwebGroupImageDetailEntity getMwebGroupImageDetailById(int id);
    
    public int deleteMwebGroupImageDetailByNotInIdsAndMwebGroupProductId(JSONObject j);
}
