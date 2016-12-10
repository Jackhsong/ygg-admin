package com.ygg.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.entity.CityEntity;
import com.ygg.admin.entity.DistrictEntity;
import com.ygg.admin.entity.ProvinceEntity;
import com.ygg.admin.service.AreaService;

@Controller
@RequestMapping("/area")
public class AreaController
{
    @Resource
    private AreaService areaService;
    
    private static Logger logger = Logger.getLogger(AreaController.class);
    
    /**
     * 省份编码
     * @param code
     * @return
     */
    @RequestMapping(value = "/jsonProvinceCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonProvinceCode(@RequestParam(value = "code", required = false, defaultValue = "0") int code)
    {
        List<Map<String, Object>> codeList = new ArrayList<Map<String, Object>>();
        try
        {
            List<ProvinceEntity> peList = areaService.allProvince();
            for (ProvinceEntity ape : peList)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("code", ape.getProvinceId());
                map.put("text", ape.getName());
                if (code == ape.getProvinceId())
                {
                    map.put("selected", true);
                }
                codeList.add(map);
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        return JSON.toJSONString(codeList);
    }
    
    /**
     * 市编码
     * @param code
     * @param provinceCode
     * @return
     */
    @RequestMapping(value = "/jsonCityCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonCityCode(@RequestParam(value = "code", required = false, defaultValue = "0") int code,
        @RequestParam(value = "provinceCode", required = false, defaultValue = "0") int provinceCode)
    {
        List<Map<String, Object>> codeList = new ArrayList<Map<String, Object>>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (provinceCode != 0)
            {
                para.put("provincId", provinceCode);
            }
            List<CityEntity> aceList = areaService.findAllCity(para);
            for (CityEntity ace : aceList)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("code", ace.getCityId());
                map.put("text", ace.getName());
                if (code == ace.getCityId())
                {
                    map.put("selected", true);
                }
                codeList.add(map);
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        return JSON.toJSONString(codeList);
    }
    
    /**
     * 区编码
     * @param code
     * @param cityCode
     * @return
     */
    @RequestMapping(value = "/jsonDistrictCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonDistrictCode(@RequestParam(value = "code", required = false, defaultValue = "0") int code,
        @RequestParam(value = "cityCode", required = false, defaultValue = "0") int cityCode)
    {
        List<Map<String, Object>> codeList = new ArrayList<Map<String, Object>>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (cityCode != 0)
            {
                para.put("cityId", cityCode);
            }
            List<DistrictEntity> adeList = areaService.findAllDistrict(para);
            for (DistrictEntity ade : adeList)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("code", ade.getDistrictId());
                map.put("text", ade.getName());
                if (code == ade.getDistrictId())
                {
                    map.put("selected", true);
                }
                codeList.add(map);
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        return JSON.toJSONString(codeList);
    }
    
    /**
     * 加载省
     * @return
     * @throws Exception
     */
    @RequestMapping("/loadAllProvince")
    @ResponseBody
    public Object loadAllProvince()
        throws Exception
    {
        return areaService.findAllProvince();
    }
    
    /**
     * 加载市
     * @return
     * @throws Exception
     */
    @RequestMapping("/loadAllCity")
    @ResponseBody
    public Object loadAllCity(@RequestParam(value = "provinceId", defaultValue = "-1", required = false) int provinceId)
        throws Exception
    {
        return areaService.findAllCity(provinceId);
    }
    
    /**
     * 加载区县
     * @return
     * @throws Exception
     */
    @RequestMapping("/loadAllDistrict")
    @ResponseBody
    public Object loadAllDistrict(@RequestParam(value = "cityId", defaultValue = "-1", required = false) int cityId)
        throws Exception
    {
        return areaService.findAllDistrict(cityId);
    }
}
