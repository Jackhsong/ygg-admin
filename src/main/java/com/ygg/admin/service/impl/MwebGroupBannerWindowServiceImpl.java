package com.ygg.admin.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.ygg.admin.dao.ActivitiesCommonDao;
import com.ygg.admin.dao.CustomActivitiesDao;
import com.ygg.admin.dao.MwebGroupBannerWindowDao;
import com.ygg.admin.dao.PageDao;
import com.ygg.admin.entity.MwebGroupBannerWindowEntity;
import com.ygg.admin.service.MwebGroupBannerWindowService;
import com.ygg.admin.service.MwebGroupProductService;

@Service("mwebGroupBannerWindowService")
public class MwebGroupBannerWindowServiceImpl implements MwebGroupBannerWindowService
{
    @Resource
    private MwebGroupProductService mwebGroupProductService;
    
    @Resource(name = "mwebGroupBannerWindowDao")
    private MwebGroupBannerWindowDao MwebGroupBannerWindowDao;
    
    @Resource(name = "activitiesCommonDao")
    private ActivitiesCommonDao activitiesCommonDao;
    
    @Resource
    private CustomActivitiesDao customActivitiesDao;
    
    @Resource
    private PageDao pageDao;
    
    @Override
    public int save(MwebGroupBannerWindowEntity window)
        throws Exception
    {
        return MwebGroupBannerWindowDao.save(window);
    }
    
    @Override
    public int update(MwebGroupBannerWindowEntity window)
        throws Exception
    {
        return MwebGroupBannerWindowDao.update(window);
    }
    
    @Override
    public int countBannerWindow(Map<String, Object> para)
        throws Exception
    {
        return MwebGroupBannerWindowDao.countBannerWindow(para);
    }
    
    @Override
    public List<MwebGroupBannerWindowEntity> findAllBannerWindow(Map<String, Object> para)
        throws Exception
    {
        return MwebGroupBannerWindowDao.findAllBannerWindow(para);
    }
    
    @Override
    public MwebGroupBannerWindowEntity findBannerWindowById(int id)
        throws Exception
    {
        return MwebGroupBannerWindowDao.findBannerWindowById(id);
    }
    
    @Override
    public int updateDisplayCode(Map<String, Object> para)
        throws Exception
    {
        return MwebGroupBannerWindowDao.updateDisplayCode(para);
    }
    
    @Override
    public List<Map<String, Object>> packageBannerWindowList(List<MwebGroupBannerWindowEntity> bList)
        throws Exception
    {
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        for (MwebGroupBannerWindowEntity bEntity : bList)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", bEntity.getId());
            int displayId = bEntity.getDisplayId();
            map.put("displayId", displayId);
            
            Map<String, Object> parameter = new HashMap<String, Object>();
            parameter.put("id", displayId);
            JSONObject mwebProduct = mwebGroupProductService.findProductAndStockForTeamById(parameter);
            
            map.put("displayName", mwebProduct.get("name"));
            map.put("order", (bEntity.getOrder() == 0) ? "无" : bEntity.getOrder());
            map.put("isDisplay", (bEntity.getIsDisplay() == 1) ? "展现" : "不展现");
            map.put("isDisplayCode", bEntity.getIsDisplay());
            // 特卖状态
            String startTime = bEntity.getStartTime();
            String endTime = bEntity.getEndTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            DateTime startTime_dateTime = new DateTime(sdf.parse(startTime));
            DateTime endTime_dateTime = new DateTime(sdf.parse(endTime));
            
            // 未开始
            if (startTime_dateTime.isAfterNow())
            {
                map.put("bannerStatus", "等待开始");
            }
            // 进行中
            if (startTime_dateTime.isBeforeNow() && endTime_dateTime.isAfterNow())
            {
                map.put("bannerStatus", "进行中");
            }
            // 已结束
            if (endTime_dateTime.isBeforeNow())
            {
                map.put("bannerStatus", "已结束");
            }
            
            // 库存数量
            map.put("desc", bEntity.getDesc());
            map.put("startTime", startTime_dateTime.toString("yyyy-MM-dd HH:mm:ss"));
            map.put("endTime", endTime_dateTime.toString("yyyy-MM-dd HH:mm:ss"));
            StringBuilder imageSB = new StringBuilder("");
            imageSB.append("<img alt='' src='").append(bEntity.getImage()).append("' style='max-height:40px;'/>");
            map.put("bannerImage", imageSB.toString());
            map.put("image", bEntity.getImage());
            resultList.add(map);
        }
        return resultList;
    }
    
    @Override
    public int updateOrder(Map<String, Object> para)
        throws Exception
    {
        return MwebGroupBannerWindowDao.updateOrder(para);
    }
    
}
