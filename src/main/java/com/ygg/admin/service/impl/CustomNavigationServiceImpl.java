package com.ygg.admin.service.impl;

import com.ygg.admin.dao.AreaDao;
import com.ygg.admin.dao.CustomNavigationDao;
import com.ygg.admin.service.CustomNavigationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("customNavigationService")
public class CustomNavigationServiceImpl implements CustomNavigationService
{
    
    @Resource
    private CustomNavigationDao customNavigationDao;
    
    @Resource
    private AreaDao areaDao;
    
    @Override
    public List<Map<String, Object>> findNavigationList(int id, String name, int isDisplay, int page, int rows)
        throws Exception
    {
        List<Map<String, Object>> infos = customNavigationDao.findNavigationList(id, name, isDisplay, page, rows);
        for (Map<String, Object> info : infos)
        {
            int supportAreaType = Integer.valueOf(info.get("supportAreaType") + "");
            if (supportAreaType == 0)
            {
                info.put("supportAreaTypeStr", "全国");
            }
            else
            {
                StringBuilder sb = new StringBuilder();
                List<String> pIdList = customNavigationDao.findRelationProvinceIdByNavId(Integer.valueOf(info.get("id") + ""));
                for (String s : pIdList)
                {
                    sb.append(areaDao.findProvinceNameByProvinceId(Integer.valueOf(s))).append("，");
                }
                info.put("supportAreaTypeStr", sb.toString());
            }
        }
        return infos;
    }
    
    @Override
    public int updateByParam(int id, int sequence, int isDisplay)
        throws Exception
    {
        return customNavigationDao.updateByParam(id, sequence, isDisplay);
    }
    
    @Override
    public int save(String name, String remark, int type, int displayId, int isDisplay)
        throws Exception
    {
        return customNavigationDao.save(name, remark, type, displayId, isDisplay);
    }
    
    @Override
    public int update(int id, String name, String remark, int type, int displayId, int isDisplay)
        throws Exception
    {
        return customNavigationDao.update(id, name, remark, type, displayId, isDisplay);
    }
    
    @Override
    public Map<String, Object> findById(int id)
        throws Exception
    {
        return customNavigationDao.findById(id);
    }
    
    @Override
    public Map<String, Object> findNavigationSupportAreaInfo(int id)
        throws Exception
    {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, String>> plist = new ArrayList<>();
        Map<String, Object> info = customNavigationDao.findById(id);
        String supportAreaType = info.get("supportAreaType") + "";
        result.put("supportAreaType", supportAreaType);
        List<String> provinceIdList = customNavigationDao.findRelationProvinceIdByNavId(id);
        
        // 省份分组列表
        List<Map<String, Object>> list = new ArrayList<>();
        
        // 华东 310000 上海市 320000 江苏省 330000 浙江省 340000 安徽 360000 江西省
        List<String> huadong = Arrays.asList("310000", "320000", "330000", "340000", "360000");
        list.add(groupProvinceInfo(huadong, provinceIdList, "华东"));
        
        // 华北 110000 北京市 120000 天津市 130000 河北省 140000 山西省 150000 内蒙古自治区 370000 山东省
        List<String> huabei = Arrays.asList("120000", "130000", "140000", "150000", "370000", "110000");
        list.add(groupProvinceInfo(huabei, provinceIdList, "华北"));
        
        // 华中 410000 河南省 420000 湖北省 430000 湖南省
        List<String> huazhong = Arrays.asList("410000", "420000", "430000");
        list.add(groupProvinceInfo(huazhong, provinceIdList, "华中"));
        
        // 华南 440000 广东省 350000 福建省 450000 广西 460000 海南省
        List<String> huanan = Arrays.asList("440000", "350000", "450000", "460000");
        list.add(groupProvinceInfo(huanan, provinceIdList, "华南"));
        
        // 东北 210000 辽宁省 220000 吉林省 230000 黑龙江省
        List<String> dongbei = Arrays.asList("210000", "220000", "230000");
        list.add(groupProvinceInfo(dongbei, provinceIdList, "东北"));
        
        // 西北 610000 陕西省 620000 甘肃省 630000 青海省 640000 宁夏 650000 新疆
        List<String> xibei = Arrays.asList("610000", "620000", "630000", "640000", "650000");
        list.add(groupProvinceInfo(xibei, provinceIdList, "西北"));
        
        // 西南 500000 重庆市 510000 四川省 520000 贵州省 530000 云南省 540000 西藏自治区
        List<String> xinan = Arrays.asList("500000", "510000", "520000", "530000", "540000");
        list.add(groupProvinceInfo(xinan, provinceIdList, "西南"));
        
        result.put("pList", list);
        return result;
    }
    
    private Map<String, Object> groupProvinceInfo(List<String> groupProvince, List<String> relationProvince, String groupName)
        throws Exception
    {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        int containsAll = 0;
        if (relationProvince.containsAll(groupProvince))
        {
            containsAll = 1;
        }
        result.put("containsAll", containsAll);
        result.put("name", groupName);
        
        for (String s : groupProvince)
        {
            Map<String, Object> cpmap = new HashMap<>();
            cpmap.put("provinceName", areaDao.findProvinceNameByProvinceId(Integer.valueOf(s)));
            cpmap.put("provinceId", s);
            int selected = 0;
            if (relationProvince.contains(s))
            {
                selected = 1;
            }
            cpmap.put("selected", selected);
            list.add(cpmap);
        }
        result.put("provinceList", list);
        return result;
    }
    
    @Override
    public int updateNavAreaInfo(int id, List<Integer> provinceId, int supportAreaType)
        throws Exception
    {
        customNavigationDao.update(id, supportAreaType);
        customNavigationDao.deleteRelationCustomNavigationAndProvinceById(id);
        if (supportAreaType == 1)
        {
            List<Map<String, Object>> list = new ArrayList<>();
            for (Integer pId : provinceId)
            {
                Map<String, Object> map = new HashMap<>();
                map.put("provinceId", pId);
                map.put("customNavigationId", id);
                list.add(map);
            }
            if (list.size() > 0)
            {
                customNavigationDao.batchInsertRelationCustomNavigationAndProvince(list);
            }
        }
        return 1;
    }
}
