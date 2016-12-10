package com.ygg.admin.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.ygg.admin.dao.MwebGroupProductInfoDao;
import com.ygg.admin.entity.MwebGroupProductInfoEntity;
import com.ygg.admin.service.MwebGroupProductInfoService;
import com.ygg.admin.util.CommonEnum.TEAM_STATUS;

@Service("mwebGroupProductInfoService")
public class MwebGroupProductInfoServiceImpl implements MwebGroupProductInfoService
{
    @Resource
    private MwebGroupProductInfoDao mwebGroupProductInfoDao;
    
    @Override
    public List<JSONObject> findAutoTeamList(Map<String, Object> parameter)
    {
        
        return mwebGroupProductInfoDao.findAutoTeamList(parameter);
    }
    
    @Override
    public JSONObject getAutoTeamConfig(int id)
    {
        // TODO Auto-generated method stub
        return mwebGroupProductInfoDao.getAutoTeamConfig(id);
    }
    
    @Override
    public JSONObject updateAutoTeamConfig(Map<String, Object> parameter)
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", 0);
        int id = Integer.valueOf(parameter.get("id").toString());
        JSONObject autoTeamConfig = mwebGroupProductInfoDao.getAutoTeamConfig(id);
        if (autoTeamConfig.getInteger("isOpenAutoJoinTeam") == 1)
        {
            jsonObject.put("msg", "当前配置已经启用");
        }
        else
        {
            if (mwebGroupProductInfoDao.updateAutoTeamConfig(parameter) == 1)
            {
                jsonObject.put("status", 1);
            }
        }
        return jsonObject;
    }
    
    @Override
    public JSONObject setupAutoJoinTeam(int id, int isOpenAutoJoinTeam)
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", 0);
        jsonObject.put("id", id);
        jsonObject.put("isOpenAutoJoinTeam", isOpenAutoJoinTeam);
        
        JSONObject autoTeamConfig = mwebGroupProductInfoDao.getAutoTeamConfig(id);
        if (autoTeamConfig == null)
        {
            return jsonObject;
        }
        int mwebGroupProductInfoId = autoTeamConfig.getInteger("mwebGroupProductInfoId");
        int randomStartSecond = autoTeamConfig.getInteger("randomStartSecond");
        int randomEndSecond = autoTeamConfig.getInteger("randomEndSecond");
        int autoJoinTeamNumberLimit = autoTeamConfig.getInteger("autoJoinTeamNumberLimit");
        
        MwebGroupProductInfoEntity mwebGroupProductInfoEntity = mwebGroupProductInfoDao.getGroupProductInfoById(mwebGroupProductInfoId);
        if (mwebGroupProductInfoEntity == null)
        {
            return jsonObject;
        }
        int status = mwebGroupProductInfoEntity.getStatus();
        
        int countAutoJoinTeamAccount = mwebGroupProductInfoDao.countAutoJoinTeamAccount(mwebGroupProductInfoId);
        // 开启
        if (isOpenAutoJoinTeam == 1 && autoTeamConfig.getInteger("isOpenAutoJoinTeam") == 0 && status == TEAM_STATUS.TRANSACTION.getValue()
            && countAutoJoinTeamAccount < autoJoinTeamNumberLimit)
        {
            if (mwebGroupProductInfoDao.updateAutoTeamConfig(jsonObject) == 1)
            {
                jsonObject.put("status", 1);
            }
        }
        // 停止
        else if (isOpenAutoJoinTeam == 0 && autoTeamConfig.getInteger("isOpenAutoJoinTeam") == 1)
        {
            if (mwebGroupProductInfoDao.updateAutoTeamConfig(jsonObject) == 1)
            {
                jsonObject.put("status", 1);
            }
        }
        else
        {
            jsonObject.put("msg", "状态错误");
        }
        
        return jsonObject;
    }
    
}
