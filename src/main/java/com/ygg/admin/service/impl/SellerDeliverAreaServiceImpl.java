package com.ygg.admin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanPropertyValueChangeClosure;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.code.SellerEnum;
import com.ygg.admin.dao.AreaDao;
import com.ygg.admin.dao.ProductBaseDao;
import com.ygg.admin.dao.RelationDeliverAreaTemplateDao;
import com.ygg.admin.dao.SellerDeliverAreaDao;
import com.ygg.admin.entity.RelationDeliverAreaTemplateEntity;
import com.ygg.admin.entity.RelationProductBaseDeliverAreaEntity;
import com.ygg.admin.entity.SellerDeliverAreaTemplateEntity;
import com.ygg.admin.service.RelationDeliverAreaTemplateService;
import com.ygg.admin.service.SellerDeliverAreaService;
import com.ygg.admin.util.CommonConstant;

@Service
public class SellerDeliverAreaServiceImpl implements SellerDeliverAreaService
{
    @Resource
    private SellerDeliverAreaDao sellerDeliverAreaDao;
    
    @Resource
    private RelationDeliverAreaTemplateService relationDeliverAreaTemplateService;
    
    @Resource
    private RelationDeliverAreaTemplateDao relationDeliverAreaTemplateDao;
    
    @Resource
    private AreaDao areaDao;
    
    @Resource
    private ProductBaseDao productBaseDao;
    
    @Override
    public SellerDeliverAreaTemplateEntity findSellerDeliverAreaTemplateByName(String name)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("name", name);
        return sellerDeliverAreaDao.findSellerDeliverAreaTemplateByPara(para);
    }
    
    @Override
    public int insertSellerDeliverAreaTemplate(SellerDeliverAreaTemplateEntity areaTemplate, String areaCodes, String other)
        throws Exception
    {
        Set<String> provinceCodes = new HashSet<>();
        if (sellerDeliverAreaDao.insertSellerDeliverAreaTemplate(areaTemplate) > 0)
        {
            relationDeliverAreaTemplateService.insertRelationDeliverAreaTemplate(areaTemplate.getId(), areaCodes, other);
            if (areaTemplate.getType() == SellerEnum.DELIVER_AREA_SUPPORT_TYPE.SUPPORT.getCode())
            {
                for (String areaCode : areaCodes.split(","))
                {
                    if (StringUtils.isNotEmpty(areaCode))
                    {
                        provinceCodes.add(areaCode.trim());
                    }
                }
            }
            else if (areaTemplate.getType() == SellerEnum.DELIVER_AREA_SUPPORT_TYPE.UNSUPPORT.getCode())
            {
                Set<String> allProvinces = areaDao.findAllProvinceCode();
                for (String areaCode : areaCodes.split(","))
                {
                    allProvinces.remove(areaCode.trim());
                }
                
                for (String area : other.split(";"))
                {
                    if (StringUtils.isNotEmpty(area))
                    {
                        allProvinces.add(area.split(",")[0].trim());
                    }
                }
                provinceCodes.addAll(allProvinces);
            }
            if (!provinceCodes.isEmpty())
            {
                sellerDeliverAreaDao.insertRelationTemplateDeliverAreaAndExtraPostage(areaTemplate, provinceCodes);
            }
        }
        return areaTemplate.getId();
    }
    
    @Override
    public int updateSellerDeliverAreaTemplate(SellerDeliverAreaTemplateEntity areaTemplate, String areaCodes, String other)
        throws Exception
    {
        
        int result = 0;
        if (relationDeliverAreaTemplateService.updateRelationDeliverAreaTemplate(areaTemplate.getId(), areaCodes, other) > 0)
        {
            result = sellerDeliverAreaDao.updateSellerDeliverAreaTemplate(areaTemplate);
        }
        //修改基本商品关联的配送地区
        if (result > 0)
        {
            if (sellerDeliverAreaDao.countDeliverAreaTemplateFromProductBase(areaTemplate.getId()) > 0)
            {
                //找出关联的基本商品
                //修改基本商品配送地区类型
                //删除基本商品配送地区数据
                //插入基本商品新的配送地区数据
                List<Integer> productBaseIdList = productBaseDao.findProductBaseIdBySellerTemplateId(areaTemplate.getId());
                productBaseDao.batchUpdateProductBaseDeliverAreaType(areaTemplate.getType(), productBaseIdList);
                productBaseDao.deleteRelationProductBaseDeliverArea(productBaseIdList);
                
                List<RelationDeliverAreaTemplateEntity> areas = wrapArea(areaTemplate.getId(), areaCodes);
                List<RelationDeliverAreaTemplateEntity> exceptAreas = wrapExceptArea(areaTemplate.getId(), other);
                List<RelationDeliverAreaTemplateEntity> allAreas = filterExceptArea(areas, exceptAreas);
                
                for (int id : productBaseIdList)
                {
                    List<RelationProductBaseDeliverAreaEntity> rsdaeList = new ArrayList<RelationProductBaseDeliverAreaEntity>();
                    copyProperties(allAreas, rsdaeList, id);
                    productBaseDao.insertRelationProductBaseDeliverArea(rsdaeList);
                }
            }
            
            Set<String> newProvinceCodes = new HashSet<>();
            if (areaTemplate.getType() == SellerEnum.DELIVER_AREA_SUPPORT_TYPE.SUPPORT.getCode())
            {
                for (String areaCode : areaCodes.split(","))
                {
                    if (StringUtils.isNotEmpty(areaCode))
                    {
                        newProvinceCodes.add(areaCode.trim());
                    }
                }
            }
            else if (areaTemplate.getType() == SellerEnum.DELIVER_AREA_SUPPORT_TYPE.UNSUPPORT.getCode())
            {
                Set<String> allProvinces = areaDao.findAllProvinceCode();
                for (String areaCode : areaCodes.split(","))
                {
                    allProvinces.remove(areaCode.trim());
                }
                
                for (String area : other.split(";"))
                {
                    if (StringUtils.isNotEmpty(area))
                    {
                        allProvinces.add(area.split(",")[0].trim());
                    }
                }
                newProvinceCodes.addAll(allProvinces);
            }
            
            List<String> oldProvinceCodes = sellerDeliverAreaDao.findRelationTemplateDeliverAreaProvinceCodeBytid(areaTemplate.getId());
            Set<String> addProvinceCodes = new HashSet<>();
            Set<String> delProvinceCodes = new HashSet<>();
            
            for (String provinceCode : newProvinceCodes)
            {
                if (StringUtils.isNotEmpty(provinceCode) && !oldProvinceCodes.contains(provinceCode))
                {
                    addProvinceCodes.add(provinceCode.trim());
                }
            }
            
            for (String provinceCode : oldProvinceCodes)
            {
                if (StringUtils.isNotEmpty(provinceCode) && !newProvinceCodes.contains(provinceCode))
                {
                    delProvinceCodes.add(provinceCode.trim());
                }
            }
            
            if (!addProvinceCodes.isEmpty())
            {
                sellerDeliverAreaDao.insertRelationTemplateDeliverAreaAndExtraPostage(areaTemplate, addProvinceCodes);
            }
            
            if (!delProvinceCodes.isEmpty())
            {
                sellerDeliverAreaDao.deleteRelationTemplateDeliverAreaAndExtraPostage(areaTemplate, delProvinceCodes);
            }
        }
        result = result > 0 ? areaTemplate.getId() : result;
        return result;
    }
    
    @Override
    public int deleteSellerDeliverAreaTemplate(int id)
        throws Exception
    {
        int result = 0;
        if (relationDeliverAreaTemplateService.deleteRelationDeliverAreaTemplateByTemplateId(id) > 0)
        {
            result = sellerDeliverAreaDao.deleteSellerDeliverAreaTemplate(id);
            
            sellerDeliverAreaDao.deleteRelationTemplateDeliverAreaAndExtraPostage(id);
        }
        return result;
    }
    
    @Override
    public List<SellerDeliverAreaTemplateEntity> findSellerDeliverAreaTemplateBysid(int sellerId)
        throws Exception
    {
        return sellerDeliverAreaDao.findSellerDeliverAreaTemplateBysid(sellerId);
    }
    
    @Override
    public Map<String, Object> jsonSellerDeliverAreaTemplate(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Integer> templateIds = new ArrayList<>();
        List<Map<String, Object>> resultList = sellerDeliverAreaDao.findAllSellerDeliverAreaTemplate(para);
        for (Map<String, Object> map : resultList)
        {
            int id = Integer.parseInt(map.get("id") + "");
            int templateType = Integer.parseInt(map.get("templateType") + "");
            map.put("templateType", templateType == 1 ? "发货地区" : "不发货地区");
            templateIds.add(id);
        }
        Map<String, Set<String>> extraProvinces = new HashMap<>();
        List<Map<String, Object>> extraAreas = sellerDeliverAreaDao.findExtraPostageBytids(templateIds);
        for (Map<String, Object> extra : extraAreas)
        {
            String templateId = extra.get("templateId").toString();
            String provinceName = extra.get("provinceName").toString();
            if (extraProvinces.containsKey(templateId))
            {
                extraProvinces.get(templateId).add(provinceName);
            }
            else
            {
                Set<String> provinces = new HashSet<>();
                provinces.add(provinceName);
                extraProvinces.put(templateId, provinces);
            }
        }
        for (Map<String, Object> map : resultList)
        {
            map.put("extraPostage", extraProvinces.get(map.get("id").toString()) == null ? "" : extraProvinces.get(map.get("id").toString()));
        }
        resultMap.put("rows", resultList);
        resultMap.put("total", sellerDeliverAreaDao.countSellerDeliverAreaTemplate(para));
        return resultMap;
    }
    
    @Override
    public int countDeliverAreaTemplateFromProductBase(int id)
        throws Exception
    {
        return sellerDeliverAreaDao.countDeliverAreaTemplateFromProductBase(id);
    }
    
    @Override
    public SellerDeliverAreaTemplateEntity getSellerDeliverAreaTemplateAndRelationArea(int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        SellerDeliverAreaTemplateEntity template = sellerDeliverAreaDao.findSellerDeliverAreaTemplateByPara(para);
        
        para.clear();
        para.put("isExcept", 0);
        para.put("sellerDeliverAreaTemplateId", id);
        List<RelationDeliverAreaTemplateEntity> areas = relationDeliverAreaTemplateDao.findRelationDeliverAreaTemplateByPara(para);
        for (RelationDeliverAreaTemplateEntity area : areas)
        {
            area.setProvinceName(areaDao.findProvinceNameByProvinceId(Integer.parseInt(area.getProvinceCode())));
        }
        
        para.put("isExcept", 1);
        List<RelationDeliverAreaTemplateEntity> exceptAreas = relationDeliverAreaTemplateDao.findRelationDeliverAreaTemplateByPara(para);
        for (RelationDeliverAreaTemplateEntity area : exceptAreas)
        {
            if (area.getProvinceCode().equals("1"))
            {
                area.setProvinceName("全部省");
            }
            else
            {
                area.setProvinceName(areaDao.findProvinceNameByProvinceId(Integer.parseInt(area.getProvinceCode())));
            }
            if (area.getCityCode().equals("1"))
            {
                area.setCityName("全部市");
            }
            else
            {
                area.setCityName(areaDao.findCityNameByCityId(Integer.parseInt(area.getCityCode())));
            }
            if (area.getDistrictCode().equals("1"))
            {
                area.setDistrictName("全部区");
            }
            else
            {
                area.setDistrictName(areaDao.findDistrictNameByDistrictId(Integer.parseInt(area.getDistrictCode())));
            }
        }
        
        if (template == null)
            return template;
        template.setAreas(areas);
        template.setExceptAreas(exceptAreas);
        return template;
    }
    
    @Override
    public SellerDeliverAreaTemplateEntity findSellerDeliverAreaTemplateById(int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        return sellerDeliverAreaDao.findSellerDeliverAreaTemplateByPara(para);
    }
    
    private List<RelationDeliverAreaTemplateEntity> wrapArea(int id, String areaCodes)
    {
        List<RelationDeliverAreaTemplateEntity> areaList = new ArrayList<RelationDeliverAreaTemplateEntity>();
        String[] areaCodeArray = areaCodes.split(",");
        for (String provinceCode : areaCodeArray)
        {
            if (StringUtils.isEmpty(provinceCode))
            {
                continue;
            }
            RelationDeliverAreaTemplateEntity area = new RelationDeliverAreaTemplateEntity();
            area.setSellerDeliverAreaTemplateId(id);
            area.setProvinceCode(provinceCode);
            area.setCityCode("1");
            area.setDistrictCode("1");
            areaList.add(area);
        }
        return areaList;
    }
    
    private List<RelationDeliverAreaTemplateEntity> wrapExceptArea(int id, String areaCodes)
    {
        List<RelationDeliverAreaTemplateEntity> areaList = new ArrayList<RelationDeliverAreaTemplateEntity>();
        String[] areaCodeArray = areaCodes.split(";");
        for (String areaCode : areaCodeArray)
        {
            if (StringUtils.isEmpty(areaCode))
            {
                continue;
            }
            String[] areaArray = areaCode.split(",");
            RelationDeliverAreaTemplateEntity area = new RelationDeliverAreaTemplateEntity();
            area.setSellerDeliverAreaTemplateId(id);
            area.setProvinceCode(areaArray[0]);
            area.setCityCode(areaArray[1]);
            area.setDistrictCode(areaArray[2]);
            area.setIsExcept(1);
            areaList.add(area);
        }
        return areaList;
    }
    
    private List<RelationDeliverAreaTemplateEntity> filterExceptArea(List<RelationDeliverAreaTemplateEntity> areas, List<RelationDeliverAreaTemplateEntity> exceptAreas)
        throws Exception
    {
        
        //去除非例外地区中包含的例外的省份
        Set<RelationDeliverAreaTemplateEntity> removeSet = new HashSet<>();
        for (RelationDeliverAreaTemplateEntity area : areas)
        {
            for (RelationDeliverAreaTemplateEntity except : exceptAreas)
            {
                if (area.getProvinceCode().equals(except.getProvinceCode()))
                {
                    removeSet.add(area);
                }
            }
        }
        areas.removeAll(removeSet);
        
        Set<String> provinceSet = new HashSet<String>();
        Set<String> citySet = new HashSet<String>();
        Set<String> districtSet = new HashSet<String>();
        
        //取出例外地区中精确到市的地区
        Set<String> allCity = new HashSet<>();
        Iterator<RelationDeliverAreaTemplateEntity> it = exceptAreas.iterator();
        while (it.hasNext())
        {
            RelationDeliverAreaTemplateEntity area = it.next();
            citySet.add(area.getCityCode());
            provinceSet.add(area.getProvinceCode());
            if (StringUtils.equals(area.getDistrictCode(), "1"))
            {
                allCity.add(area.getCityCode());
                it.remove();
            }
        }
        List<Map<String, Object>> areaList = new ArrayList<Map<String, Object>>();
        if (!provinceSet.isEmpty() && !citySet.isEmpty())
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (provinceSet.size() > 0)
            {
                para.put("provinceIdList", new ArrayList<String>(provinceSet));
            }
            if (citySet.size() > 0)
            {
                para.put("cityIdList", new ArrayList<String>(citySet));
            }
            areaList.addAll(areaDao.findCityCodeByPara(para));
        }
        
        //取出例外地区中精确到区的地区
        provinceSet.clear();
        citySet.clear();
        for (RelationDeliverAreaTemplateEntity area : exceptAreas)
        {
            if (allCity.contains(area.getCityCode()))
            {
                continue;
            }
            provinceSet.add(area.getProvinceCode());
            citySet.add(area.getCityCode());
            districtSet.add(area.getDistrictCode());
        }
        if (!provinceSet.isEmpty() && !citySet.isEmpty() && !districtSet.isEmpty())
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (provinceSet.size() > 0)
            {
                para.put("provinceIdList", new ArrayList<String>(provinceSet));
            }
            if (citySet.size() > 0)
            {
                para.put("cityIdList", new ArrayList<String>(citySet));
            }
            if (districtSet.size() > 0)
            {
                para.put("districtIdList", new ArrayList<String>(districtSet));
            }
            areaList.addAll(areaDao.findDistrictCodeByPara(para));
        }
        
        for (Map<String, Object> map : areaList)
        {
            RelationDeliverAreaTemplateEntity area = new RelationDeliverAreaTemplateEntity();
            area.setProvinceCode(map.get("provinceId") == null ? "1" : map.get("provinceId").toString());
            area.setCityCode(map.get("cityId") == null ? "1" : map.get("cityId").toString());
            area.setDistrictCode(map.get("districtId") == null ? "1" : map.get("districtId").toString());
            areas.add(area);
        }
        return areas;
    }
    
    private void copyProperties(List<RelationDeliverAreaTemplateEntity> orig, List<RelationProductBaseDeliverAreaEntity> dest, int id)
        throws Exception
    {
        for (RelationDeliverAreaTemplateEntity sellerArea : orig)
        {
            RelationProductBaseDeliverAreaEntity productArea = new RelationProductBaseDeliverAreaEntity();
            PropertyUtils.copyProperties(productArea, sellerArea);
            dest.add(productArea);
        }
        
        BeanPropertyValueChangeClosure closure = new BeanPropertyValueChangeClosure("productBaseId", id);
        CollectionUtils.forAllDo(dest, closure);
    }
    
    @Override
    public String addCommonPostage(String provinceCode, int freightMoney)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> para = new HashMap<>();
        para.put("provinceCode", provinceCode);
        para.put("freightMoney", freightMoney);
        try
        {
            if (sellerDeliverAreaDao.addCommonPostage(para) > 0)
            {
                resultMap.put("status", 1);
                resultMap.put("msg", "添加成功");
            }
            else
            {
                resultMap.put("status", 1);
                resultMap.put("msg", "添加失败");
            }
        }
        catch (Exception e)
        {
            if (e.getMessage().contains("Duplicate") && e.getMessage().contains("uniq_province_code"))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "该省运费已经存在，请勿重复添加");
            }
            else
            {
                throw new Exception(e);
            }
        }
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public Map<String, Object> jsonCommonPostage(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("rows", sellerDeliverAreaDao.findAllCommonPostage(para));
        resultMap.put("total", sellerDeliverAreaDao.countCommonPostage(para));
        return resultMap;
    }
    
    @Override
    public String updateCommonPostage(int id, int freightMoney)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> para = new HashMap<>();
        para.put("id", id);
        para.put("freightMoney", freightMoney);
        if (sellerDeliverAreaDao.updateCommonPostage(para) > 0)
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "添加成功");
        }
        else
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "添加失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public Map<String, Object> jsonExtraPostage(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("total", sellerDeliverAreaDao.countExtraPostage(para));
        resultMap.put("rows", sellerDeliverAreaDao.findExtraPostage(para));
        return resultMap;
    }
    
    @Override
    public String updateExtraPostage(int id, int isExtra, int freightMoney)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        if (freightMoney < 0)
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "运费不能为负数");
            return JSON.toJSONString(resultMap);
        }
        if (freightMoney <= 0 && isExtra == CommonConstant.COMMON_YES)
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "您选择了加运费，但是运费设置不正确");
            return JSON.toJSONString(resultMap);
        }
        if (isExtra == CommonConstant.COMMON_NO)
        {
            freightMoney = 0;
        }
        Map<String, Object> para = new HashMap<>();
        para.put("id", id);
        para.put("isExtra", isExtra);
        para.put("freightMoney", freightMoney);
        if (sellerDeliverAreaDao.updateExtraPostage(para) > 0)
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "修改成功");
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "修改失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public int findCommonExtraPostage(String provinceCode)
        throws Exception
    {
        Integer postage = sellerDeliverAreaDao.findCommonExtraPostage(provinceCode);
        return postage == null ? 0 : postage.intValue();
    }
}
