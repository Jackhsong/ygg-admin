package com.ygg.admin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ygg.admin.dao.GegeImageDao;
import com.ygg.admin.entity.GegeImageEntity;
import com.ygg.admin.service.GegeImageService;

@Service("geGeImageService")
public class GegeImageServiceImpl implements GegeImageService
{
    
    @Resource
    private GegeImageDao geGeImageDao;
    
    @Override
    public Map<String, Object> jsonImageInfo(Map<String, Object> para)
        throws Exception
    {
        
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> imageInfoList = geGeImageDao.findImageInfo(para);
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        int total = 0;
        if (imageInfoList.size() > 0)
        {
            for (Map<String, Object> curr : imageInfoList)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", curr.get("id"));
                map.put("index", curr.get("id"));
                map.put("name", curr.get("category_name"));
                if ("".equals(curr.get("image")))
                {
                    map.put("image", curr.get("image"));
                }
                else
                {
                    map.put("image", "<img style='max-width:100px' src='" + curr.get("image") + "' />");
                }
                map.put("isAvailable", curr.get("is_available"));
                resultList.add(map);
            }
            total = geGeImageDao.countImageInfo(para);
        }
        resultMap.put("rows", resultList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public int saveOrUpdate(GegeImageEntity image, String type)
        throws Exception
    {
        int resultStauts = -1;
        if (image.getId() == -1) // 新增
        {
            resultStauts = geGeImageDao.save(image, type);
        }
        else
        // 修改
        {
            resultStauts = geGeImageDao.update(image, type);
        }
        return resultStauts;
    }
    
    @Override
    public GegeImageEntity findGegeImageById(int id, String type)
        throws Exception
    {
        return geGeImageDao.findGegeImageById(id, type);
    }
    
    @Override
    public boolean checkIsUse(int id, String type)
        throws Exception
    {
        
        return geGeImageDao.checkInUse(id, type);
    }
    
    @Override
    public int delete(Map<String, Object> para)
        throws Exception
    {
        GegeImageEntity image = new GegeImageEntity();
        image.setId(Integer.valueOf(para.get("id") + ""));
        image.setIsAvailable(Integer.valueOf(para.get("isAvailable") + ""));
        return geGeImageDao.update(image, para.get("type") + "");
    }
    
    @Override
    public int batchDelete(Map<String, Object> para)
        throws Exception
    {
        return geGeImageDao.batchDelete(para);
    }
    
    @Override
    public boolean checkIsExist(GegeImageEntity image, String type)
        throws Exception
    {
        return geGeImageDao.checkIsExist(image, type);
    }
    
    @Override
    public List<GegeImageEntity> findAllGegeImage(String type)
        throws Exception
    {
        
        return geGeImageDao.findAllGegeImage(type);
    }
}
