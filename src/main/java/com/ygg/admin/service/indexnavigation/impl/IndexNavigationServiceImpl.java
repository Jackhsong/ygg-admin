
 /**************************************************************************
 * Copyright (c) 2014-2016 浙江格家网络技术有限公司.
 * All rights reserved.
 * 
 * 项目名称：左岸城堡APP
 * 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.ygg.admin.service.indexnavigation.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.ygg.admin.config.AreaCache;
import com.ygg.admin.entity.CityEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ygg.admin.code.IndexNavigationEnum;
import com.ygg.admin.code.IndexNavigationEnum.Type;
import com.ygg.admin.dao.AreaDao;
import com.ygg.admin.dao.indexnavigation.IndexNavigationDao;
import com.ygg.admin.service.indexnavigation.IndexNavigationService;

/**
  * 新版首页自定义导航服务
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: IndexNavigationServiceImpl.java 9333 2016-03-28 08:07:03Z zhangyibo $   
  * @since 2.0
  */
@Service("indexNavigationService")
public class IndexNavigationServiceImpl implements IndexNavigationService {
	
    
     /**地区DAO接口 */
    @Resource
    private AreaDao areaDao;
     /**新版首页自定义导航Dao接口*/
    @Autowired(required=false)
    @Qualifier("indexNavigationDao")
    private IndexNavigationDao indexNavigationDao;
    
    @Override
    public List<Map<String, Object>> findNavigationList(int id, String name, int isDisplay, int page, int rows)
        throws Exception
    {
        List<Map<String, Object>> infos = indexNavigationDao.findNavigationList(id, name, isDisplay, page, rows);
        for (Map<String, Object> info : infos)
        {
        	String type = String.valueOf(info.get("type"));
        	Type tp = IndexNavigationEnum.Type.parse(type);
        	if(tp != null){
        		 info.put("typeName", tp.getTitle());
        	}
            int supportAreaType = Integer.valueOf(info.get("supportAreaType") + "");
            if (supportAreaType == 0)
            {
                info.put("supportAreaTypeStr", "全国");
            }
            else
            {
                StringBuilder sb = new StringBuilder();
                List<String> pIdList = indexNavigationDao.findRelationProvinceIdByNavId(Integer.valueOf(info.get("id") + ""));
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
        return indexNavigationDao.updateByParam(id, sequence, isDisplay);
    }
    
    @Override
    public int save(String name, String remark, int type, int displayId, int isDisplay)
        throws Exception
    {
        return indexNavigationDao.save(name, remark, type, displayId, isDisplay);
    }
    
    @Override
    public int update(int id, String name, String remark, int type, int displayId, int isDisplay)
        throws Exception
    {
        return indexNavigationDao.update(id, name, remark, type, displayId, isDisplay);
    }
    
    @Override
    public Map<String, Object> findById(int id)
        throws Exception
    {
        return indexNavigationDao.findById(id);
    }
    
    @Override
    public Map<String, Object> findNavigationSupportAreaInfo(int id)
        throws Exception
    {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, String>> plist = new ArrayList<>();
        Map<String, Object> info = indexNavigationDao.findById(id);
        String supportAreaType = info.get("supportAreaType") + "";
        result.put("supportAreaType", supportAreaType);
        List<String> provinceIdList = indexNavigationDao.findRelationProvinceIdByNavId(id);
        
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

        // 热门地区
        Map<String, Map<String, List<String>>> relationCityMap = new HashMap<>();
        List<Map<String, Object>> cityInfoList = indexNavigationDao.findRelationCityIdByNavId(id);
        for (Map<String, Object> map : cityInfoList)
        {
            String cityId = map.get("cityId") + "";
            CityEntity city = areaDao.findCityByCityId(cityId);
            String provinceId = city.getProvinceId() + "";
            List<String> selectedCityList = null;
            List<String> notSelectedCityList = null;
            Map<String, List<String>> currMap = relationCityMap.get(provinceId);
            if (currMap != null)
            {
                selectedCityList = currMap.get("selectedCityList");
                notSelectedCityList = currMap.get("notSelectedCityList");
            }
            else
            {
                selectedCityList = new ArrayList<>();
                notSelectedCityList = new ArrayList<>();
                currMap = new HashMap<>();
                currMap.put("selectedCityList", selectedCityList);
                currMap.put("notSelectedCityList", notSelectedCityList);
                relationCityMap.put(provinceId, currMap);
            }

            int isExcept = Integer.valueOf(map.get("isExcept") + ""); // 是否是例外地区，1是，0否 ，一个导航下所有记录的该值都是一样的
            if (isExcept == 1)
            {
                // 例外
                notSelectedCityList.add(cityId);
            }
            else
            {
                // 支持地区
                selectedCityList.add(cityId);
            }
        }

        List<Map<String, Object>> citys = indexNavigationDao.findHotCityInfo(new HashMap<String, Object>());
        // key: provinceId ，value: 省下对应城市列表
        Map<String, List<Map<String, Object>>> cityMap = new HashMap<>();
        for (Map<String, Object> city : citys)
        {
            String provinceId = city.get("provinceId") + "";
            List<Map<String, Object>> cityList = cityMap.get(provinceId);
            if (cityList == null)
            {
                cityList = new ArrayList<>();
                cityMap.put(provinceId, cityList);
            }
            cityList.add(city);
        }

        List<Map<String, Object>> hotCityList = new ArrayList<>();
        for (Map.Entry<String, List<Map<String, Object>>> entry : cityMap.entrySet())
        {
            Map<String,Object> hotCityMap = new HashMap<>();
            String provinceId = entry.getKey();
            String provinceName = AreaCache.getInstance().getProvinceName(provinceId);

            Map<String, List<String>> relationCity = relationCityMap.get(provinceId) == null ? new HashMap<String, List<String>>() : relationCityMap.get(provinceId);
            List<String> selectedCityList = relationCity.get("selectedCityList") == null ? new ArrayList<String>() : relationCity.get("selectedCityList");
            List<String> notSelectedCityList = relationCity.get("notSelectedCityList") == null ? new ArrayList<String>() : relationCity.get("notSelectedCityList");;

            for (Map<String, Object> map : entry.getValue())
            {
                String cityId = map.get("cityId") + "";
                map.put("cityName", AreaCache.getInstance().getCityName(cityId));
                map.put("cityId", cityId);
                if (selectedCityList.size() > 0)
                {
                    if (selectedCityList.contains(cityId))
                    {
                        map.put("selected", 1);
                    }
                }
                else if (notSelectedCityList.size() > 0 && !notSelectedCityList.contains(cityId))
                {
                    map.put("selected", 1);
                }
            }
            entry.getValue().add(new HashMap<String, Object>()
            {
                {
                    put("cityId", "0");
                    put("cityName", "其他");
                }
            });
            if (notSelectedCityList.size() > 0)
            {
                entry.getValue().get(entry.getValue().size() - 1).put("selected", 1);
            }
            hotCityMap.put("provinceId", provinceId);
            hotCityMap.put("provinceName", provinceName);
            hotCityMap.put("containsAll", 0);
            hotCityMap.put("provinceHotCityList", entry.getValue());
            hotCityList.add(hotCityMap);
        }
        result.put("hotCityList", hotCityList);
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
            cpmap.put("provinceName", AreaCache.getInstance().getProvinceName(s));
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
    public int updateNavAreaInfo(int id, List<Integer> provinceIds, int supportAreaType, List<Map<String, Object>> provinceHotCityList)
        throws Exception
    {
        indexNavigationDao.update(id, supportAreaType);
        indexNavigationDao.deleteRelationCustomNavigationAndProvinceById(id); // 删除关联省份
        indexNavigationDao.deleteRelationCustomNavigationAndCityById(id); // 删除关联城市
        if (supportAreaType == 1)
        {
            // 插入支持省份信息
            List<Map<String, Object>> list = new ArrayList<>();
            for (Integer pId : provinceIds)
            {
                Map<String, Object> map = new HashMap<>();
                map.put("provinceId", pId);
                map.put("customNavigationId", id);
                list.add(map);
            }
            if (list.size() > 0)
            {
                indexNavigationDao.batchInsertRelationCustomNavigationAndProvince(list);
            }

            // 插入支持城市地区
            if (provinceHotCityList.size() > 0)
            {
                List<Map<String, Object>> insertCityList = new ArrayList<>();
                for (Map<String, Object> provinceHotCity : provinceHotCityList)
                {
                    int provinceId = Integer.valueOf(provinceHotCity.get("provinceId") + "");
                    if (provinceIds.contains(provinceId))
                    {
                        List<String> provinceCityList = (List<String>)provinceHotCity.get("provinceCityList");

                        // 若 provinceCityList 包含 0(其他地区) 插入例外地区(即未选中的地区)。 若 不包含 0(其他地区) 插入支持地区(即选中的地区)
                        if (provinceCityList.contains("0"))
                        {
                            Map<String, Object> queryPara = new HashMap<>();
                            queryPara.put("provinceId", provinceId);
                            List<Map<String, Object>> citys = indexNavigationDao.findHotCityInfo(queryPara);
                            for (Map<String, Object> city : citys)
                            {
                                String cityId = city.get("cityId") + "";
                                if (!provinceCityList.contains(cityId))
                                {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("cityId", cityId);
                                    map.put("customNavigationId", id);
                                    map.put("isExcept", 1);
                                    insertCityList.add(map);
                                }
                            }
                        }
                        else
                        {
                            for (String s : provinceCityList)
                            {
                                Map<String, Object> map = new HashMap<>();
                                map.put("cityId", s);
                                map.put("customNavigationId", id);
                                map.put("isExcept", 0);
                                insertCityList.add(map);
                            }
                        }
                    }
                }
                if (insertCityList.size() > 0)
                {
                    indexNavigationDao.batchInsertRelationCustomNavigationAndCity(insertCityList);
                }
            }
        }
        return 1;
    }

    @Override
    public Map<String, Object> findHotCityInfo(int start, int max)
        throws Exception
    {
        Map<String,Object> para = new HashMap<>();
        para.put("start", start);
        para.put("max", max);
        List<Map<String, Object>> hotCityList = indexNavigationDao.findHotCityInfo(para);
        int total = 0;
        if (hotCityList.size() > 0)
        {
            total = indexNavigationDao.countHotCityInfo();
            for (Map<String, Object> map : hotCityList)
            {
                map.put("province", AreaCache.getInstance().getProvinceName(map.get("provinceId") + ""));
                map.put("city", AreaCache.getInstance().getCityName(map.get("cityId") + ""));
            }
        }
        Map<String,Object> result = new HashMap<>();
        result.put("rows", hotCityList);
        result.put("total", total);
        return result;
    }
    
    @Override
    public int saveHotCityInfo(String provinceCode, String cityCode)
        throws Exception
    {
        return indexNavigationDao.saveHotCityInfo(provinceCode, cityCode);
    }
    
    @Override
    public int updateHotCityInfo(int id, int isAvailable, int sequence)
        throws Exception
    {
        Map<String,Object> para = new HashMap<>();
        para.put("id", id);
        para.put("isAvailable", isAvailable);
        para.put("sequence", sequence);
        return indexNavigationDao.updateHotCityInfo(para);
    }

    @Override
    public boolean existsHotCityInfo(String provinceCode, String cityCode)
        throws Exception
    {
        Map<String,Object> para = new HashMap<>();
        para.put("provinceId", provinceCode);
        para.put("cityId", cityCode);
        return indexNavigationDao.findHotCityInfo(para).size() > 0;
    }
}
