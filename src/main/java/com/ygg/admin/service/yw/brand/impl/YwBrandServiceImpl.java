package com.ygg.admin.service.yw.brand.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.ygg.admin.code.UrlEnum;
import com.ygg.admin.dao.yw.brand.YwBrandDao;
import com.ygg.admin.entity.yw.YwBrandEntity;
import com.ygg.admin.service.yw.brand.YwBrandService;

@Repository
public class YwBrandServiceImpl implements YwBrandService
{

    @Resource
    private YwBrandDao ywBrandDao;
    
    @Override
    public int addBrand(YwBrandEntity brand) throws Exception
    {
        return ywBrandDao.addBrand(brand);
    }

    @Override
    public int updateBrand(YwBrandEntity brand) throws Exception
    {
        return ywBrandDao.updateBrand(brand);
    }

    @Override
    public Map<String, Object> findBrandInfo(Map<String, Object> para)
            throws Exception
    {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        List<YwBrandEntity> brandList= ywBrandDao.findBrandInfo(para);
        
        if (CollectionUtils.isNotEmpty(brandList)){
            for (YwBrandEntity brand : brandList){
                String imageUrl = String.valueOf(brand.getImage());
                if (StringUtils.isNotBlank(imageUrl)){
                    brand.setImage("<img style='max-width:100px' src='" + imageUrl + "' />");
                    brand.setImageUrl(imageUrl);
                }else{
                    brand.setImage("");
                    brand.setImageUrl("");
                }
                //添加组合链接
                StringBuilder urlForAc = new StringBuilder();
                urlForAc.append("<a target='_blank' href='");
                urlForAc.append(UrlEnum.GroupProYw.URL).append(brand.getActivitiesCommonId());
                urlForAc.append("'>" + "组合链接" + "</a>");
                brand.setUrlacId(urlForAc.toString());
            }
        }
        
        int total = ywBrandDao.countBrandInfo(para);
        resultMap.put("rows", brandList);
        resultMap.put("total",total);
        return resultMap;
    }

    @Override
    public int updateBrandDisplay(YwBrandEntity brand) throws Exception
    {
       return ywBrandDao.updateBrandDisplay(brand);
    }

    @Override
    public String getBrandCategoryName(int categoryId) throws Exception
    {
        return ywBrandDao.getBrandCategoryName(categoryId);
    }
    
}
