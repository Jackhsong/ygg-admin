package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.FreightDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.FreightTemplateEntity;
import com.ygg.admin.entity.ProvinceEntity;
import com.ygg.admin.entity.ProvinceFreightEntity;

@Repository("freightDao")
public class FreightDaoImpl extends BaseDaoImpl implements FreightDao
{
    
    @Override
    public List<FreightTemplateEntity> findAllFreightTemplateByPara(Map<String, Object> para)
        throws Exception
    {
        List<FreightTemplateEntity> resultList = this.getSqlSession().selectList("FreightTemplateMapper.findAllFreightTemplateByPara", para);
        if (resultList == null)
        {
            return new ArrayList<FreightTemplateEntity>();
        }
        return resultList;
    }
    
    @Override
    public int countFreightTemplateByPara(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().selectOne("FreightTemplateMapper.countFreightTemplateByPara", para);
    }
    
    @Override
    public FreightTemplateEntity findFreightTemplateById(int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        para.put("start", 0);
        para.put("max", 1);
        List<FreightTemplateEntity> freightTemplateList = findAllFreightTemplateByPara(para);
        if (freightTemplateList.size() > 0)
        {
            return freightTemplateList.get(0);
        }
        else
        {
            return null;
        }
    }
    
    @Override
    public int save(FreightTemplateEntity entity)
        throws Exception
    {
        return this.getSqlSession().insert("FreightTemplateMapper.save", entity);
    }
    
    @Override
    public int update(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("FreightTemplateMapper.update", para);
    }
    
    @Override
    public int saveProvinceFreight(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().insert("FreightTemplateMapper.saveProvinceFreight", para);
    }
    
    @Override
    public int updateProvinceFreight(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("FreightTemplateMapper.updateProvinceFreight", para);
    }
    
    @Override
    public List<ProvinceFreightEntity> findAllProvinceFreightByTemplateId(int templateId)
        throws Exception
    {
        List<ProvinceFreightEntity> resultList = this.getSqlSession().selectList("FreightTemplateMapper.findAllProvinceFreightByTemplateId", templateId);
        if (resultList == null)
        {
            return new ArrayList<ProvinceFreightEntity>();
        }
        return resultList;
    }
    
    @Override
    public String findProvinceNameByProvinceId(int provinceId)
        throws Exception
    {
        return this.getSqlSession().selectOne("AreaMapper.findProvinceNameByProvinceId", provinceId);
    }
    
    @Override
    public List<ProvinceEntity> findAllProvince()
        throws Exception
    {
        return this.getSqlSession().selectList("AreaMapper.findAllProvince");
    }
    
}
