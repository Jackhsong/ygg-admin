
 /**************************************************************************
 * Copyright (c) 2014-2016 浙江格家网络技术有限公司.
 * All rights reserved.
 * 
 * 项目名称：左岸城堡
APP
 * 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.ygg.admin.dao.indexnavigation.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.dao.indexnavigation.IndexNavigationDao;

/**
  * 新版首页自定义导航Dao
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: IndexNavigationDaoImpl.java 9215 2016-03-24 11:50:02Z zhangyibo $   
  * @since 2.0
  */
@Repository("indexNavigationDao")
public class IndexNavigationDaoImpl extends BaseDaoImpl implements IndexNavigationDao {
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
        List<Map<String, Object>> result = getSqlSessionRead().selectList("IndexNavigationMapper.findNavigationList", param);
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
        return getSqlSession().update("IndexNavigationMapper.updateByParam", param);
    }
    
    @Override
    public Map<String, Object> findNavigationById(int id)
        throws Exception
    {
        return getSqlSessionRead().selectOne("IndexNavigationMapper.findNavigationById", id);
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
        return getSqlSession().insert("IndexNavigationMapper.save", param);
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
        return getSqlSession().update("IndexNavigationMapper.update", param);
    }
    
    @Override
    public Map<String, Object> findById(int id)
        throws Exception
    {
        return getSqlSessionRead().selectOne("IndexNavigationMapper.findNavigationById", id);
    }
    
    @Override
    public int update(int id, int supportAreaType)
        throws Exception
    {
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        param.put("supportAreaType", supportAreaType);
        return getSqlSession().update("IndexNavigationMapper.update", param);
    }
    
    @Override
    public int batchInsertRelationCustomNavigationAndProvince(List<Map<String, Object>> list)
        throws Exception
    {
        return getSqlSession().insert("IndexNavigationMapper.batchInsertRelationCustomNavigationAndProvince", list);
    }

    @Override
    public int batchInsertRelationCustomNavigationAndCity(List<Map<String, Object>> list)
        throws Exception
    {
        return getSqlSession().insert("IndexNavigationMapper.batchInsertRelationCustomNavigationAndCity", list);
    }

    @Override
    public int deleteRelationCustomNavigationAndProvinceById(int naId)
        throws Exception
    {
        return getSqlSession().delete("IndexNavigationMapper.deleteRelationCustomNavigationAndProvinceById", naId);
    }

    @Override
    public int deleteRelationCustomNavigationAndCityById(int naId)
        throws Exception
    {
        return getSqlSession().delete("IndexNavigationMapper.deleteRelationCustomNavigationAndCityById", naId);
    }

    @Override
    public List<String> findRelationProvinceIdByNavId(int naId)
        throws Exception
    {
        return getSqlSession().selectList("IndexNavigationMapper.findRelationProvinceIdByNavId", naId);
    }

    @Override
    public List<Map<String, Object>> findRelationCityIdByNavId(int naId)
        throws Exception
    {
        return getSqlSession().selectList("IndexNavigationMapper.findRelationCityIdByNavId", naId);
    }

    @Override
    public List<Map<String, Object>> findAllCustomNavigationByPara(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = getSqlSessionRead().selectList("IndexNavigationMapper.findNavigationList", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }

    @Override
    public List<Map<String, Object>> findHotCityInfo(Map<String,Object> para)
        throws Exception
    {
        return getSqlSession().selectList("IndexNavigationMapper.findHotCityInfo", para);
    }

    @Override
    public int countHotCityInfo()
        throws Exception
    {
        return getSqlSession().selectOne("IndexNavigationMapper.countHotCityInfo");
    }

    @Override
    public int saveHotCityInfo(String provinceCode, String cityCode)
        throws Exception
    {
        Map<String,Object> para = new HashMap<>();
        para.put("provinceId", provinceCode);
        para.put("cityId", cityCode);
        return getSqlSession().insert("IndexNavigationMapper.saveHotCityInfo", para);
    }
    
    @Override
    public int updateHotCityInfo(Map<String,Object> para)
        throws Exception
    {
        return getSqlSession().update("IndexNavigationMapper.updateHotCityInfo", para);
    }
}
