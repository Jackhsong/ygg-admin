package com.ygg.admin.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.ygg.admin.dao.MwebGroupProductCountDao;
import com.ygg.admin.entity.MwebGroupProductCountEntity;
import com.ygg.admin.service.MwebGroupProductCountService;

@Service("mwebGroupProductCountService")
public class MwebGroupProductCountServiceImpl implements MwebGroupProductCountService
{
    @Resource
    private MwebGroupProductCountDao mwebGroupProductCountDao;
    
    @Override
    public MwebGroupProductCountEntity getGroupProductCount(MwebGroupProductCountEntity mwebGroupProductCountEntity)
    {
        // TODO Auto-generated method stub
        return mwebGroupProductCountDao.getGroupProductCount(mwebGroupProductCountEntity);
    }
    
    @Override
    public JSONObject updateGroupProductCount(int mwebGroupProductId, int addStock)
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", 0);
        jsonObject.put("msg", "未知错误");
        MwebGroupProductCountEntity mwebGroupProductCountEntity = new MwebGroupProductCountEntity();
        mwebGroupProductCountEntity.setMwebGroupProductId(mwebGroupProductId);
        
        mwebGroupProductCountEntity = mwebGroupProductCountDao.getGroupProductCount_forUpdate(mwebGroupProductCountEntity);
        if (mwebGroupProductCountEntity == null)
        {
            jsonObject.put("msg", "该商品没有库存信息");
            return jsonObject;
        }
        int id = mwebGroupProductCountEntity.getId();
        int stock = mwebGroupProductCountEntity.getStock();
        
        JSONObject j = new JSONObject();
        j.put("id", id);
        j.put("stock", stock);
        if ((addStock + stock) > -1)
        {
            j.put("addStock", stock + addStock);
        }
        else
        {
            jsonObject.put("msg", "库存修改错误");
            return jsonObject;
        }
        
        int i = mwebGroupProductCountDao.updateGroupProductCount(j);
        if (i == 1)
        {
            jsonObject.put("status", 1);
            jsonObject.put("msg", "修改库存成功");
        }
        else
        {
            jsonObject.put("msg", "修改失败，请刷新重试");
        }
        return jsonObject;
    }
    
}
