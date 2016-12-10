package com.ygg.admin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.ygg.admin.dao.SpecialActivityModelDao;
import com.ygg.admin.service.SpecialActivityModelService;
import com.ygg.admin.util.ImageUtil;

@Service("specialActivityModelService")
public class SpecialActivityModelServiceImpl implements SpecialActivityModelService
{
    @Resource
    private SpecialActivityModelDao specialActivityModelDao;
    
    @Override
    public Map<String, Object> findListByParam(Map<String, Object> param)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", specialActivityModelDao.findListByParam(param));
        result.put("total", specialActivityModelDao.countByParam(param));
        return result;
    }
    
    @Override
    public int saveOrUpdate(Map<String, Object> param)
        throws Exception
    {
        // 如果存在图片，计算图片相关信息
        if (param.get("image") != null && StringUtils.isNotBlank(param.get("image").toString()))
        {
            Map<String, Object> imageMap = ImageUtil.getProperty(param.get("image").toString());
            param.put("imageWidth", imageMap.get("width"));
            param.put("imageHeight", imageMap.get("height"));
        }
        if (param.get("id") == null || StringUtils.isBlank(param.get("id").toString()))
        {
            // ID不存在，说明是新增
            return specialActivityModelDao.save(param);
        }
        else
        {
            // ID存在，说明是更新操作
            return specialActivityModelDao.updateByParam(param);
        }
    }
    
    @Override
    public Map<String, Object> findById(String id)
        throws Exception
    {
        return specialActivityModelDao.findById(id);
    }
    
    @Override
    public List<Map<String, Object>> findAllSpecialActivityModel(Map<String, Object> para)
        throws Exception
    {
        return specialActivityModelDao.findListByParam(para);
    }
    
}
