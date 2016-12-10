package com.ygg.admin.service.qqbsbrand.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.ygg.admin.code.UrlEnum;
import com.ygg.admin.dao.qqbs.QqbsBrandDao;
import com.ygg.admin.entity.qqbs.QqbsBrandEntity;
import com.ygg.admin.service.qqbsbrand.QqbsBrandService;

@Repository
public class QqbsBrandServiceImpl implements QqbsBrandService
{

    @Resource
    private QqbsBrandDao qqbsBrandDao;
    
    @Override
    public int addBrand(QqbsBrandEntity brand) throws Exception
    {
        return qqbsBrandDao.addBrand(brand);
    }

    @Override
    public int updateBrand(QqbsBrandEntity brand) throws Exception
    {
        return qqbsBrandDao.updateBrand(brand);
    }

    @Override
    public Map<String, Object> findBrandInfo(Map<String, Object> para)
            throws Exception
    {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        List<QqbsBrandEntity> brandList= qqbsBrandDao.findBrandInfo(para);
        
        if (CollectionUtils.isNotEmpty(brandList)){
            for (QqbsBrandEntity brand : brandList){
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
                urlForAc.append(UrlEnum.GroupProQqbs.URL).append(brand.getActivitiesCommonId());
                urlForAc.append("'>" + "组合链接" + "</a>");
                brand.setUrlacId(urlForAc.toString());
            }
        }
        
        int total = qqbsBrandDao.countBrandInfo(para);
        resultMap.put("rows", brandList);
        resultMap.put("total",total);
        return resultMap;
    }

    @Override
    public int updateBrandDisplay(QqbsBrandEntity brand) throws Exception
    {
       return qqbsBrandDao.updateBrandDisplay(brand);
    }

    @Override
    public String getBrandCategoryName(int categoryId) throws Exception
    {
        return qqbsBrandDao.getBrandCategoryName(categoryId);
    }
    
}
