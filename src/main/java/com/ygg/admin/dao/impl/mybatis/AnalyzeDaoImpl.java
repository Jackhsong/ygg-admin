package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.AnalyzeDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;

@Repository("analyzeDao")
public class AnalyzeDaoImpl extends BaseDaoImpl implements AnalyzeDao
{
    
    @Override
    public List<Map<String, Object>> findProvinceAndCityTurnOver(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = this.getSqlSessionRead().selectList("AnalyzeMapper.findProvinceAndCityTurnOver", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public List<Map<String, Object>> findProductTurnOverAnalyze(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = this.getSqlSessionRead().selectList("AnalyzeMapper.findProductTurnOverAnalyze", para);
        for (Map<String, Object> map : reList)
        {
            int activityType = Integer.parseInt(map.get("isGroup") == null ? "0" : map.get("isGroup") + "");
            if (activityType == 1)
            {
                double groupPrice = Double.parseDouble(map.get("groupPrice") + "");
                map.put("salesPrice", groupPrice);
            }
        }
        return reList;
    }
    
    @Override
    public List<Map<String, Object>> findAllFullCategoryProduct(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = this.getSqlSessionRead().selectList("AnalyzeMapper.findAllFullCategoryProduct", para);
        for (Map<String, Object> map : reList)
        {
            int activityType = Integer.parseInt(map.get("isGroup") == null ? "0" : map.get("isGroup") + "");
            if (activityType == 1)
            {
                double groupPrice = Double.parseDouble(map.get("groupPrice") + "");
                map.put("salesPrice", groupPrice);
            }
        }
        return reList;
    }
    
    @Override
    public List<Map<String, Object>> finSecondCategoryProduct(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = this.getSqlSessionRead().selectList("AnalyzeMapper.finSecondCategoryProduct", para);
        for (Map<String, Object> map : reList)
        {
            int activityType = Integer.parseInt(map.get("isGroup") == null ? "0" : map.get("isGroup") + "");
            if (activityType == 1)
            {
                double groupPrice = Double.parseDouble(map.get("groupPrice") + "");
                map.put("salesPrice", groupPrice);
            }
        }
        return reList;
    }
    
    @Override
    public List<Map<String, Object>> findNoCategoryProduct(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = this.getSqlSessionRead().selectList("AnalyzeMapper.findNoCategoryProduct", para);
        for (Map<String, Object> map : reList)
        {
            int activityType = Integer.parseInt(map.get("isGroup") == null ? "0" : map.get("isGroup") + "");
            if (activityType == 1)
            {
                double groupPrice = Double.parseDouble(map.get("groupPrice") + "");
                map.put("salesPrice", groupPrice);
            }
        }
        return reList;
    }
    
    @Override
    public List<Map<String, Object>> findAllFirstCategoryProduct(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = this.getSqlSessionRead().selectList("AnalyzeMapper.findAllFirstCategoryProduct", para);
        for (Map<String, Object> map : reList)
        {
            int activityType = Integer.parseInt(map.get("isGroup") == null ? "0" : map.get("isGroup") + "");
            if (activityType == 1)
            {
                double groupPrice = Double.parseDouble(map.get("groupPrice") + "");
                map.put("salesPrice", groupPrice);
            }
        }
        return reList;
    }
}
