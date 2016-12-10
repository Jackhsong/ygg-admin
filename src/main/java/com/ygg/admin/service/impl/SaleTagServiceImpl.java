package com.ygg.admin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.dao.SaleTagDao;
import com.ygg.admin.entity.SaleTagEntity;
import com.ygg.admin.service.SaleTagService;

@Service("saleTagService")
public class SaleTagServiceImpl implements SaleTagService
{
    
    Logger logger = Logger.getLogger(SaleTagServiceImpl.class);
    
    @Resource(name = "saleTagDao")
    private SaleTagDao saleTagDao = null;
    
    @Override
    public int saveOrUpdate(SaleTagEntity saleTag)
        throws Exception
    {
        int resultStauts = -1;
        if (saleTag.getId() == 0)
        {// 新增
            resultStauts = saleTagDao.save(saleTag);
        }
        else
        {
            resultStauts = saleTagDao.update(saleTag);
        }
        return resultStauts;
    }
    
    @Override
    public String jsonSaleTagInfo(Map<String, Object> para)
        throws Exception
    {
        List<SaleTagEntity> saleTagList = saleTagDao.findAllSaleTag(para);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        int total = 0;
        if (saleTagList != null && saleTagList.size() > 0)
        {
            for (SaleTagEntity saleTag : saleTagList)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", saleTag.getId());
                map.put("name", saleTag.getName());
                if ("".equals(saleTag.getImage()))
                {
                    map.put("image", saleTag.getImage());
                }
                else
                {
                    map.put("image", "<img style='max-width:100px' src='" + saleTag.getImage() + "' />");
                }
                map.put("isAvailable", (saleTag.getIsAvailable() == (byte)1) ? "可用" : "停用");
                resultList.add(map);
            }
            total = saleTagDao.countSaleTag(para);
        }
        resultMap.put("rows", resultList);
        resultMap.put("total", total);
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public SaleTagEntity findSaleTagById(int id)
        throws Exception
    {
        return saleTagDao.findSaleTagById(id);
    }
    
    @Override
    public List<SaleTagEntity> findAllSaleTag(Map<String, Object> para)
        throws Exception
    {
        return saleTagDao.findAllSaleTag(para);
    }
    
    @Override
    public List<Integer> findTagIdsBySaleWindowId(int id)
        throws Exception
    {
        return saleTagDao.findTagIdsBySaleWindowId(id);
    }
    
    @Override
    public void saveSaleWindowAndTag(Map<String, Object> para)
        throws Exception
    {
        int saleWindowId = (int)para.get("saleWindowId");
        String tags = (String)para.get("tags");
        if ("".equals(tags))
        {
            saleTagDao.deleteAllRelationBySaleWindowId(saleWindowId);
            return;
        }
        
        List<Integer> tagIds = new ArrayList<Integer>();
        if (tags.indexOf(",") > 0)
        {
            String[] arrStrings = tags.split(",");
            for (String s : arrStrings)
            {
                tagIds.add(Integer.valueOf(s));
            }
        }
        else
        {
            tagIds.add(Integer.valueOf(tags));
        }
        
        // 先删除特卖也标签的关系
        saleTagDao.deleteAllRelationBySaleWindowId(saleWindowId);
        
        for (Integer id : tagIds)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("saleTagId", id);
            map.put("saleWindowId", saleWindowId);
            saleTagDao.saveRelation(map);
        }
    }
}
