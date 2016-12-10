package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.CustomNavigationDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;

@Repository("customNavigationDao")
public class CustomNavigationDaoImpl extends BaseDaoImpl implements CustomNavigationDao
{
    
    @Override
    public List<Map<String, Object>> findNavigationList(int id, String name, int isDisplay, int page, int rows)
        throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        param.put("name", name);
        param.put("isDisplay", isDisplay);
        param.put("start", rows * (page - 1));
        param.put("size", rows);
        List<Map<String, Object>> result = getSqlSessionRead().selectList("CustomNavigationMapper.findNavigationList", param);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public int updateByParam(int id, int sequence, int isDisplay)
        throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        param.put("sequence", sequence);
        param.put("isDisplay", isDisplay);
        return getSqlSession().update("CustomNavigationMapper.updateByParam", param);
    }
    
    @Override
    public Map<String, Object> findNavigationById(int id)
        throws Exception
    {
        return getSqlSessionRead().selectOne("CustomNavigationMapper.findNavigationById", id);
    }
    
    @Override
    public int save(String name, String remark, int type, int displayId, int isDisplay)
        throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("name", name);
        param.put("remark", remark);
        param.put("type", type);
        param.put("displayId", displayId);
        param.put("isDisplay", isDisplay);
        return getSqlSession().insert("CustomNavigationMapper.save", param);
    }
    
    @Override
    public int update(int id, String name, String remark, int type, int displayId, int isDisplay)
        throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        param.put("name", name);
        param.put("remark", remark);
        param.put("type", type);
        param.put("displayId", displayId);
        param.put("isDisplay", isDisplay);
        return getSqlSession().update("CustomNavigationMapper.update", param);
    }
    
    @Override
    public Map<String, Object> findById(int id)
        throws Exception
    {
        return getSqlSessionRead().selectOne("CustomNavigationMapper.findNavigationById", id);
    }
    
    @Override
    public int update(int id, int supportAreaType)
        throws Exception
    {
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        param.put("supportAreaType", supportAreaType);
        return getSqlSession().update("CustomNavigationMapper.update", param);
    }
    
    @Override
    public int batchInsertRelationCustomNavigationAndProvince(List<Map<String, Object>> list)
        throws Exception
    {
        return getSqlSession().insert("CustomNavigationMapper.batchInsertRelationCustomNavigationAndProvince", list);
    }
    
    @Override
    public int deleteRelationCustomNavigationAndProvinceById(int naId)
        throws Exception
    {
        return getSqlSession().delete("CustomNavigationMapper.deleteRelationCustomNavigationAndProvinceById", naId);
    }
    
    @Override
    public List<String> findRelationProvinceIdByNavId(int naId)
        throws Exception
    {
        return getSqlSession().selectList("CustomNavigationMapper.findRelationProvinceIdByNavId", naId);
    }
    
    @Override
    public List<Map<String, Object>> findAllCustomNavigationByPara(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = getSqlSessionRead().selectList("CustomNavigationMapper.findNavigationList", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
}
