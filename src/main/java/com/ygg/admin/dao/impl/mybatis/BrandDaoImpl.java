package com.ygg.admin.dao.impl.mybatis;

import com.ygg.admin.dao.BrandDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.BrandEntity;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("BrandDao")
public class BrandDaoImpl extends BaseDaoImpl implements BrandDao
{
    private Logger log = Logger.getLogger(BrandDaoImpl.class);

    @Override
    public int save(BrandEntity brand)
        throws Exception
    {
        return getSqlSession().insert("BrandMapper.save", brand);
    }

    @Override
    public int saveRelationBrandCategory(Map<String, Object> para)
            throws Exception
    {
        return getSqlSession().insert("BrandMapper.saveRelationBrandCategory", para);
    }

    @Override
    public int deleteRelationBrandCategory(Map<String, Object> para) {
        return getSqlSession().delete("BrandMapper.deleteRelationBrandCategory", para);
    }

    @Override
    public List<BrandEntity> findAllBrandByPara(Map<String, Object> para)
        throws Exception
    {
        List<BrandEntity> resultList = getSqlSessionRead().selectList("BrandMapper.findAllBrandByPara", para);
        if (resultList == null)
        {
            return new ArrayList<>();
        }
        return resultList;
    }
    
    @Override
    public BrandEntity findBrandById(int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        para.put("start", 0);
        para.put("max", 1);
        List<BrandEntity> brandList = findAllBrandByPara(para);
        if (brandList.size() > 0)
        {
            return brandList.get(0);
        }
        else
        {
            return null;
        }
    }
    
    @Override
    public int updateBrandByPara(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("BrandMapper.update", para);
    }
    
    @Override
    public int countBrandByPara(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().selectOne("BrandMapper.countBrandByPara", para);
    }
    
    @Override
    public int countBrandByName(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().selectOne("BrandMapper.countBrandByName", para);
    }
    @Override
    public int delete(int id) throws Exception{
        return getSqlSession().delete("BrandMapper.delete", id);
    }
    @Override
    public int update(Map<String, Object> param)
        throws Exception
    {
        return getSqlSession().update("BrandMapper.update", param);
    }

    @Override
    public List<Map<String, Object>> findRelationBrandCategory(Map<String, Object> para) {
        return getSqlSessionRead().selectList("BrandMapper.findRelationBrandCategory", para);
    }

    @Override
    public List<Integer> findRelationBrandCategoryIdsByBrandId(int brandId) {
        try {
            return getSqlSessionRead().selectList("BrandMapper.findRelationBrandCategoryIdsByBrandId", brandId);
        }catch (Exception e) {
            log.error(e);
            return new ArrayList<>();
        }
    }
}
