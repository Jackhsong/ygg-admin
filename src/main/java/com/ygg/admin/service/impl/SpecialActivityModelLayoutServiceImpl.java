package com.ygg.admin.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.ygg.admin.dao.SpecialActivityModelLayoutDao;
import com.ygg.admin.service.SpecialActivityModelLayoutService;
import com.ygg.admin.util.ImageUtil;

@Service("specialActivityModelLayoutService")
public class SpecialActivityModelLayoutServiceImpl implements SpecialActivityModelLayoutService
{
    @Resource
    private SpecialActivityModelLayoutDao specialActivityModelLayoutDao;
    
    @Override
    public Map<String, Object> findListByParam(Map<String, Object> param)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", specialActivityModelLayoutDao.findListByParam(param));
        result.put("total", specialActivityModelLayoutDao.countByParam(param));
        return result;
    }
    
    @Override
    public int saveOrUpdate(Map<String, Object> param)
        throws Exception
    {
        // 如果存在图片，计算图片相关信息
        if(param.get("image") != null && StringUtils.isNotBlank(param.get("image").toString())) {
            Map<String, Object> imageMap = ImageUtil.getProperty(param.get("image").toString());
            param.put("imageWidth", imageMap.get("width"));
            param.put("imageHeight", imageMap.get("height"));
        }
        if(param.get("id") == null || StringUtils.isBlank(param.get("id").toString())) {
            // ID不存在，说明是新增
            param.put("sequence", 0);
            return specialActivityModelLayoutDao.save(param);
        } else {
            // ID存在，说明是更新操作
            return specialActivityModelLayoutDao.updateByParam(param);
        }
    }

    @Override
    public Map<String, Object> findById(String id)
        throws Exception
    {
        return specialActivityModelLayoutDao.findById(id);
    }
    
}
