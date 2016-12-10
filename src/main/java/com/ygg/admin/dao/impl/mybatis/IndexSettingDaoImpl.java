package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.IndexSettingDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;

@Repository("indexSettingDao")
public class IndexSettingDaoImpl extends BaseDaoImpl implements IndexSettingDao
{
    
    @Override
    public List<Map<String, Object>> findSetting(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = this.getSqlSessionRead().selectList("IndexSettingMapper.findSetting", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public int updateConfigStatus(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("IndexSettingMapper.updateConfigStatus", para);
    }
    
    @Override
    public int countAdvertise(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("IndexSettingMapper.countAdvertise", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllAdvertise(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = this.getSqlSessionRead().selectList("IndexSettingMapper.findAllAdvertise", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public int updateAdvertise(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("IndexSettingMapper.updateAdvertise", para);
    }
    
    @Override
    public int deleteAdvertise(int id)
        throws Exception
    {
        return this.getSqlSession().delete("IndexSettingMapper.deleteAdvertise", id);
    }
    
    @Override
    public int addAdvertise(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().insert("IndexSettingMapper.addAdvertise", para);
    }
    
    @Override
    public int findAdvertiseMaxSequence()
        throws Exception
    {
        Integer sequence = this.getSqlSessionRead().selectOne("IndexSettingMapper.findAdvertiseMaxSequence");
        return sequence == null ? 1 : sequence.intValue() + 1;
    }
    
    @Override
    public int updateAdvertiseVersion(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("IndexSettingMapper.updateAdvertiseVersion", para);
    }
    
    @Override
    public int updatePlatformVersion(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("IndexSettingMapper.updatePlatformVersion", para);
    }
    
    @Override
    public int countVestAppInfo(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("IndexSettingMapper.countVestAppInfo", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllVestAppInfo(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = this.getSqlSessionRead().selectList("IndexSettingMapper.findAllVestAppInfo", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public int addVestApp(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().insert("IndexSettingMapper.addVestApp", para);
    }
    
    @Override
    public int updateVestApp(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("IndexSettingMapper.updateVestApp", para);
    }
    
    @Override
    public int deleteVestApp(int id)
        throws Exception
    {
        return this.getSqlSession().update("IndexSettingMapper.deleteVestApp", id);
    }
    
    @Override
    public int updateWeiXin(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("IndexSettingMapper.updateWeiXin", para);
    }

    @Override
    public int updatePlatformConfigById(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("IndexSettingMapper.updatePlatformConfigById", para);
    }
}
