package com.ygg.admin.service.impl;

import com.ygg.admin.dao.BrandHotDao;
import com.ygg.admin.entity.BrandEntity;
import com.ygg.admin.service.BrandHotService;
import com.ygg.admin.service.BrandService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-4-25
 */
@Service
public class BrandHotServiceImpl implements BrandHotService {

    private Logger log = Logger.getLogger(BrandHotServiceImpl.class);

    @Resource
    private BrandHotDao brandHotDao;

    @Resource
    private BrandService brandService;

    @Override
    public List<Map<String, Object>> jsonInfo(Map<String, Object> para) throws Exception {
        List<Map<String, Object>> hotBrands = brandHotDao.findAllHotBrandByPara(para);
        for (Map<String, Object> hotBrand : hotBrands) {
            int brandId = ((Long) hotBrand.get("brandId")).intValue();
            BrandEntity brand = brandService.findBrandById(brandId);
            hotBrand.put("name", brand.getName());
            hotBrand.put("introduce", brand.getIntroduce());
            if (hotBrand.get("image") != null) {
                String img = "<img style='max-width:100px' src='" + hotBrand.get("image") + "' />";
                hotBrand.put("imageImg", "<a href=" + hotBrand.get("image") + " target=\"_blank\"> " + img + " </a>");
            }
        }
        return hotBrands;

    }

    @Override
    public int countJsonInfo(Map<String, Object> para) throws Exception {
        return brandHotDao.countFindAllHotBrandByPara(para);
    }

    @Override
    public Map<String, Object> findHotBrandByBrandId(int brandId) {
        Map<String, Object> para = new HashMap<>();
        para.put("brandId", brandId);
        List<Map<String, Object>> brands = brandHotDao.findAllHotBrandByPara(para);
        if (CollectionUtils.isEmpty(brands)) {
            return null;
        } else {
            return brands.get(0);
        }
    }

    @Override
    public int saveOrUpdate(Map<String, Object> data) {
        if (data.get("idList") != null) {
            return brandHotDao.update(data);
        } else {
            return brandHotDao.save(data);
        }
    }

    @Override
    public int detele(List<Integer> ids) {
        int count = 0;
        for (Integer id : ids) {
            count += brandHotDao.delete(id);
        }
        return count;
    }

}
