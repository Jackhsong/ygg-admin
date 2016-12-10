package com.ygg.admin.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import javax.annotation.Resource;

import com.ygg.admin.util.YimeiUtil;
import org.springframework.stereotype.Service;

import com.ygg.admin.dao.IndexSettingDao;
import com.ygg.admin.service.IndexSettingService;
import com.ygg.admin.service.SearchService;
import com.ygg.admin.util.ImageUtil;

@Service("indexSettingService")
public class IndexSettingServiceImpl implements IndexSettingService
{
    @Resource
    private IndexSettingDao indexSettingDao;
    
    @Resource
    private SearchService searchService;
    
    // 信号量
    final Semaphore semp = new Semaphore(1);
    
    @Override
    public Map<String, String> findSettingByKey(Map<String, Object> para)
        throws Exception
    {
        para.put("start", 0);
        para.put("max", 1);
        Map<String, String> resultMap = new HashMap<String, String>();
        List<Map<String, Object>> result = indexSettingDao.findSetting(para);
        if (result.size() > 0)
        {
            resultMap.put("id", result.get(0).get("id") + "");
            resultMap.put("key", result.get(0).get("key") + "");
            resultMap.put("value", result.get(0).get("value") + "");
            resultMap.put("desc", result.get(0).get("desc") + "");
        }
        return resultMap;
    }
    
    @Override
    public int updateConfigStatus(Map<String, Object> para)
        throws Exception
    {
        return indexSettingDao.updateConfigStatus(para);
    }
    
    @Override
    public Map<String, Object> jsonAdvertiseInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = indexSettingDao.findAllAdvertise(para);
        int total = 0;
        if (infoList.size() > 0)
        {
            for (Map<String, Object> map : infoList)
            {
                int isDisplay = Integer.valueOf(map.get("isDisplay") + "").intValue();
                String image = "<a href='" + map.get("image") + "' target='_blank'>" + map.get("image") + "</a>";
                map.put("image", image);
                map.put("displayDesc", isDisplay == 1 ? "展现" : "不展现");
            }
            total = indexSettingDao.countAdvertise(para);
        }
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public int updateAdvertiseSequence(Map<String, Object> para)
        throws Exception
    {
        return indexSettingDao.updateAdvertise(para);
    }
    
    @Override
    public int updateAdvertiseDisplayStatus(Map<String, Object> para)
        throws Exception
    {
        return indexSettingDao.updateAdvertise(para);
    }
    
    @Override
    public int deleteAdvertise(int id)
        throws Exception
    {
        return indexSettingDao.deleteAdvertise(id);
    }
    
    @Override
    public int saveOrUpdateAdvertise(Map<String, Object> para)
        throws Exception
    {
        int id = (int)para.get("id");
        String image = para.get("image") + "";
        Map<String, Object> imageWidthAndHeight = ImageUtil.getProperty(image);
        int width = Integer.valueOf(imageWidthAndHeight.get("width") + "");
        int height = Integer.valueOf(imageWidthAndHeight.get("height") + "");
        BigDecimal pictureSizeMax = new BigDecimal(1234 / 750.0f).add(new BigDecimal(0.01));
        BigDecimal pictureSizeMin = new BigDecimal(1234 / 750.0f).subtract(new BigDecimal(0.01));
        BigDecimal pictureSiz = new BigDecimal(height / (width * 1.0f));
        if (pictureSiz.compareTo(pictureSizeMin) < 0 || pictureSiz.compareTo(pictureSizeMax) > 0)
        {
            // 图片尺寸不符合规则
            return 2;
        }
        para.put("width", width);
        para.put("height", height);
        if (id == 0)
        {
            int sequence = indexSettingDao.findAdvertiseMaxSequence();
            para.put("sequence", sequence);
            return indexSettingDao.addAdvertise(para);
        }
        else
        {
            return indexSettingDao.updateAdvertise(para);
        }
    }
    
    @Override
    public Map<String, Object> findAdvertiseById(int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        para.put("start", 0);
        para.put("max", 1);
        List<Map<String, Object>> infoList = indexSettingDao.findAllAdvertise(para);
        if (infoList == null || infoList.size() == 0)
            return null;
        return infoList.get(0);
    }
    
    @Override
    public int updateAdvertiseVersion(Map<String, Object> para)
        throws Exception
    {
        return indexSettingDao.updateAdvertiseVersion(para);
    }
    
    @Override
    public int updatePlatformVersion(Map<String, Object> para)
        throws Exception
    {
        return indexSettingDao.updatePlatformVersion(para);
    }
    
    @Override
    public Map<String, Object> jsonVestAppInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = indexSettingDao.findAllVestAppInfo(para);
        int total = 0;
        for (Map<String, Object> map : infoList)
        {
            int value = Integer.valueOf(map.get("value") + "").intValue();
            int value1 = Integer.valueOf(map.get("value1") + "").intValue();
            
            map.put("valueStr", (value == 1) ? "已开启" : "已关闭");
            map.put("value1Str", (value1 == 1) ? "是" : "否");
        }
        total = indexSettingDao.countVestAppInfo(para);
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public int saveOrUpdateVestApp(Map<String, Object> para)
        throws Exception
    {
        int id = (int)para.get("id");
        if (id == 0)
        {
            return indexSettingDao.addVestApp(para);
        }
        else
        {
            return indexSettingDao.updateVestApp(para);
        }
    }
    
    @Override
    public int updateVestAppStatus(Map<String, Object> para)
        throws Exception
    {
        return indexSettingDao.updateVestApp(para);
    }
    
    @Override
    public int deleteVestApp(int id)
        throws Exception
    {
        return indexSettingDao.deleteVestApp(id);
    }
    
    @Override
    public int updateWeiXin(Map<String, Object> para)
        throws Exception
    {
        return indexSettingDao.updateWeiXin(para);
    }
    
    @Override
    public int updateVestAppCustomLayoutDisplayStatus(Map<String, Object> para)
        throws Exception
    {
        return indexSettingDao.updateVestApp(para);
    }
    
    @Override
    public int updateSearchIndex()
        throws Exception
    {
        if (semp.availablePermits() < 1)
        {
            return 0;
        }
        try
        {
            // 获取许可
            semp.acquire();
            
            int status = searchService.refreshSearchIndex();
            if (status == 1)
            {
                Map<String, Object> searchPara = new HashMap<>();
                searchPara.put("key", "search_cache_key_version");
                Map<String, String> map = findSettingByKey(searchPara);
                int newValue = 1;
                if (map != null && map.get("value") != null)
                {
                    newValue = Integer.valueOf(map.get("value") + "");
                    newValue++;
                    Map<String, Object> updatePara = new HashMap<>();
                    updatePara.put("id", Integer.valueOf(map.get("id")));
                    updatePara.put("newValue", newValue);
                    status = indexSettingDao.updatePlatformConfigById(updatePara);
                }
            }
            return status;
        }
        catch (Exception e)
        {
            String[] arr = {"13738043225"};
            YimeiUtil.sendSMS(arr, "定时器更新elasticsearch索引失败，请及时处理！", 5);
            throw e;
        }
        finally
        {
            // 访问完后，释放
            semp.release();
        }
        
    }
    
    @Override
    public int updatePlatformConfigById(Map<String, Object> para)
        throws Exception
    {
        return indexSettingDao.updatePlatformConfigById(para);
    }
}
