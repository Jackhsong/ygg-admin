package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.GegeImageDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.GegeImageEntity;

@Repository("geGeImageDao")
public class GegeImageDaoImpl extends BaseDaoImpl implements GegeImageDao
{
    
    @Override
    public List<Map<String, Object>> findImageInfo(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSession().selectList("GegeImageMapper.findImageInfo" + para.get("type"), para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public int countImageInfo(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().selectOne("GegeImageMapper.countImageInfo" + para.get("type"), para);
    }
    
    @Override
    public int save(GegeImageEntity image, String type)
        throws Exception
    {
        
        return getSqlSession().insert("GegeImageMapper.save" + type, image);
    }
    
    @Override
    public int update(GegeImageEntity image, String type)
        throws Exception
    {
        return getSqlSession().update("GegeImageMapper.update" + type, image);
    }
    
    @Override
    public GegeImageEntity findGegeImageById(int id, String type)
        throws Exception
    {
        return getSqlSession().selectOne("GegeImageMapper.findGegeImageById" + type, id);
    }
    
    @Override
    public boolean checkInUse(int id, String type)
        throws Exception
    {
        int result = getSqlSession().selectOne("GegeImageMapper.checkInUse" + type, id);
        return result > 0;
    }
    
    @Override
    public int batchDelete(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("GegeImageMapper.batchDelete" + para.get("type"), para);
    }
    
    @Override
    public boolean checkIsExist(GegeImageEntity image, String type)
        throws Exception
    {
        int result = getSqlSession().selectOne("GegeImageMapper.checkIsExist" + type, image);
        return result > 0;
    }
    
    @Override
    public List<GegeImageEntity> findAllGegeImage(String type)
        throws Exception
    {
        List<GegeImageEntity> reList = getSqlSession().selectList("GegeImageMapper.findAllGegeImage" + type);
        return reList == null ? new ArrayList<GegeImageEntity>() : reList;
    }
}
