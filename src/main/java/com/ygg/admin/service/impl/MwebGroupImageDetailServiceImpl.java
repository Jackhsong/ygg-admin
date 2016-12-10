package com.ygg.admin.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.ygg.admin.dao.MwebGroupImageDetailDao;
import com.ygg.admin.entity.MwebGroupImageDetailEntity;
import com.ygg.admin.service.MwebGroupImageDetailService;

@Service("mwebGroupImageDetailService")
public class MwebGroupImageDetailServiceImpl implements MwebGroupImageDetailService
{
    @Resource
    private MwebGroupImageDetailDao mwebGroupImageDetailDao;
    
    @Override
    public int updateMwebGroupImageDetail(MwebGroupImageDetailEntity mwebGroupImageDetailEntity)
    {
        
        return mwebGroupImageDetailDao.updateMwebGroupImageDetail(mwebGroupImageDetailEntity);
    }
    
    @Override
    public MwebGroupImageDetailEntity getMwebGroupImageDetailById(int id)
    {
        // TODO Auto-generated method stub
        return mwebGroupImageDetailDao.getMwebGroupImageDetailById(id);
    }
    
    @Override
    public int addMwebGroupImageDetail(MwebGroupImageDetailEntity mwebGroupImageDetailEntity)
    {
        // TODO Auto-generated method stub
        return mwebGroupImageDetailDao.addMwebGroupImageDetail(mwebGroupImageDetailEntity);
    }
    
    @Override
    public int deleteMwebGroupImageDetailByNotInIdsAndMwebGroupProductId(int mwebGroupProductId, List<Integer> list)
    {
        JSONObject j = new JSONObject();
        j.put("idList", list);
        j.put("mwebGroupProductId", mwebGroupProductId);
        return mwebGroupImageDetailDao.deleteMwebGroupImageDetailByNotInIdsAndMwebGroupProductId(j);
    }
    
}
