package com.ygg.admin.service.impl;

import com.ygg.admin.dao.MallCountryBrandDao;
import com.ygg.admin.service.MallCountryBrandService;
import com.ygg.admin.service.SaleFlagService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-4-26
 */
@Service
public class MallCountryBrandServiceImpl implements MallCountryBrandService {

    @Resource
    private MallCountryBrandDao mallCountryBrandDao;

    @Resource
    private SaleFlagService saleFlagService;

    @Override
    public List<Map<String, Object>> findAllMallCountryBrandByPara(Map<String, Object> para) throws Exception {
        return mallCountryBrandDao.findAllMallCountryBrandByPara(para);
    }

    @Override
    public List<Map<String, Object>> jsonInfo(Map<String, Object> para) throws Exception {
        List<Map<String, Object>> brands = mallCountryBrandDao.jsonInfo(para);
        for (Map<String, Object> brand : brands) {
            if (brand.get("image") != null) {
                brand.put("imageUrl", "<img style='max-width:100px' src='" + brand.get("image") + "' />");
            }
            if (brand.get("stateId") != null) {
                Integer stateId = (Integer) brand.get("stateId");
                if (stateId != 0) {
                    String countryName = saleFlagService.findFlagNameById((Integer) brand.get("stateId"));
                    brand.put("countryName", countryName);
                }
            }
            brand.put("id", brand.get("countryBrandId"));
        }

        return brands;
    }

    @Override
    public int countJsonInfo(Map<String, Object> para) throws Exception {
        return mallCountryBrandDao.countJsonInfo(para);
    }

    @Override
    public int saveOrUpdate(Map<String, Object> data) throws Exception {
        if (data.get("idList") != null) {
            return mallCountryBrandDao.update(data);
        } else {
            return mallCountryBrandDao.save(data);
        }
    }

    @Override
    public int delete(List<Integer> ids) throws Exception {
        int count = 0;
        for (Integer id : ids) {
            count += mallCountryBrandDao.delete(id);
        }
        return count;
    }
}
