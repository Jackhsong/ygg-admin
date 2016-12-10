package com.ygg.admin.service;

import com.alibaba.fastjson.JSONObject;
import com.ygg.admin.entity.MwebGroupProductCountEntity;

public interface MwebGroupProductCountService
{
    public MwebGroupProductCountEntity getGroupProductCount(MwebGroupProductCountEntity mwebGroupProductCountEntity);
    
    public JSONObject updateGroupProductCount(int mwebGroupProductId, int addStock);
}
