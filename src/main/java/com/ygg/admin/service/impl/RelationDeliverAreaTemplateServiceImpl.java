package com.ygg.admin.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ygg.admin.dao.RelationDeliverAreaTemplateDao;
import com.ygg.admin.entity.RelationDeliverAreaTemplateEntity;
import com.ygg.admin.service.RelationDeliverAreaTemplateService;

@Service
public class RelationDeliverAreaTemplateServiceImpl implements RelationDeliverAreaTemplateService
{
    private static final Logger logger = Logger.getLogger(RelationDeliverAreaTemplateServiceImpl.class);
    
    @Resource
    private RelationDeliverAreaTemplateDao relationDeliverAreaTemplateDao;
    
    @Override
    public int insertRelationDeliverAreaTemplate(int templateId, String areaCodes, String other)
        throws Exception
    {
        int result = 0;
        List<RelationDeliverAreaTemplateEntity> areas = parse(templateId, areaCodes, other);
        if (areas.isEmpty())
        {
            logger.error(String.format("配送地区为空，接收的到的参数为：%s", areaCodes));
        }
        else
        {
            result = relationDeliverAreaTemplateDao.insertRelationDeliverAreaTemplate(areas);
        }
        return result;
    }
    
    @Override
    public int updateRelationDeliverAreaTemplate(int templateId, String areaCodes, String other)
        throws Exception
    {
        List<RelationDeliverAreaTemplateEntity> areas = parse(templateId, areaCodes, other);
        int result = 0;
        if (areas.isEmpty())
        {
            logger.error(String.format("配送地区为空，接收的到的参数为：%s", areaCodes));
        }
        else
        {
            result = relationDeliverAreaTemplateDao.updateRelationDeliverAreaTemplate(templateId, areas);
        }
        return result;
    }
    
    @Override
    public int deleteRelationDeliverAreaTemplateByTemplateId(int id)
        throws Exception
    {
        return relationDeliverAreaTemplateDao.deleteRelationDeliverAreaTemplateByTemplateId(id);
    }
    
    private List<RelationDeliverAreaTemplateEntity> parse(int templateId, String areaCodes, String other)
    {
        List<RelationDeliverAreaTemplateEntity> areaList = new ArrayList<RelationDeliverAreaTemplateEntity>();
        if (StringUtils.isNotEmpty(areaCodes))
        {
            areaList.addAll(wrapArea(templateId, areaCodes));
        }
        
        if (StringUtils.isNotEmpty(other))
        {
            areaList.addAll(wrapExceptArea(templateId, other));
        }
        return areaList;
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
}
