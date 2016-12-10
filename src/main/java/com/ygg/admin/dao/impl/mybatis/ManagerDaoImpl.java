package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.ManagerDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.ManagerEntity;

@Repository("managerDao")
public class ManagerDaoImpl extends BaseDaoImpl implements ManagerDao
{
    
    @Override
    public ManagerEntity findManagerById(int id)
        throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        List<ManagerEntity> reList = getSqlSession().selectList("ManagerMapper.findManagerByPara", map);
        return reList == null ? null : reList.get(0);
    }
    
    @Override
    public int updatePWDByID(String pwd, int id)
        throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("pwd", pwd);
        return getSqlSession().update("ManagerMapper.updatePWD", map);
    }
    
    @Override
    public ManagerEntity findManagerByName(String name)
        throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        List<ManagerEntity> reList = getSqlSession().selectList("ManagerMapper.findManagerByPara", map);
        return (reList != null && reList.size() > 0) ? reList.get(0) : null;
    }
    
    @Override
    public int insertManager(ManagerEntity entity)
        throws Exception
    {
        return getSqlSession().insert("ManagerMapper.addManager", entity);
    }
    
    @Override
    public List<ManagerEntity> findAllManagerByPara(Map<String, Object> para)
        throws Exception
    {
        List<ManagerEntity> reList = getSqlSession().selectList("ManagerMapper.findManagerByPara", para);
        return reList == null ? new ArrayList<ManagerEntity>() : reList;
    }
    
    @Override
    public int countManagerByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().selectOne("ManagerMapper.countManagerByPara", para);
    }
    
}
