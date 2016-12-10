package com.ygg.admin.dao;

import com.alibaba.fastjson.JSONObject;
import com.ygg.admin.entity.MwebGroupProductCountEntity;

public interface MwebGroupProductCountDao
{
    public int addMwebGroupProductCount(MwebGroupProductCountEntity mwebGroupProductCountEntity);
    
    public MwebGroupProductCountEntity getGroupProductCount(MwebGroupProductCountEntity mwebGroupProductCountEntity);
    
    public MwebGroupProductCountEntity getGroupProductCount_forUpdate(MwebGroupProductCountEntity mwebGroupProductCountEntity);
    
    public int updateGroupProductCount(JSONObject jsonObject);
}
