package com.ygg.admin.service.impl;

import com.ygg.admin.dao.MallCountryDao;
import com.ygg.admin.service.MallCountryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-4-26
 */
@Service
public class MallCountryServiceImpl implements MallCountryService {

    private Logger log = Logger.getLogger(MallCountryServiceImpl.class);

    @Resource
    private MallCountryDao mallCountryDao;

    @Override
    public List<Map<String, Object>> jsonInfo(Map<String, Object> para) throws Exception {
        List<Map<String, Object>> mallCountryList = mallCountryDao.jsonInfo(para);
        for (Map<String, Object> country : mallCountryList) {
            int id = ((Long) country.get("id")).intValue();
            int brandCount = mallCountryDao.countCountryBrandNum(id);
            if (country.get("bannerImage") != null) {
                String bannerImg = "<img alt='' src='" + country.get("bannerImage") + "' style='max-width:90px'/>";
                String bannerImage = "<a target=\"_blank\" href=\" " + country.get("bannerImage") + " \"> " + bannerImg + "</a>";
                country.put("bannerDisplayImg", bannerImage);
            }
            country.put("flagImage", "<img alt='' src='" + country.get("flagImage") + "' style='max-width:90px'/>");
            country.put("brandCount", brandCount);
        }
        return mallCountryList;
    }

    @Override
    public int countJsonInfo(Map<String, Object> para) throws Exception {
        return mallCountryDao.countJsonInfo(para);
    }

    @Override
    public Map<String, Object> findMallCountryBySaleFlagId(int saleFlagId) {
        Map<String, Object> para = new HashMap<>();
        para.put("saleFlagId", saleFlagId);
        List<Map<String, Object>> countryList = mallCountryDao.findAllMallCountryByPara(para);
        if (CollectionUtils.isEmpty(countryList)) {
            return null;
        } else {
            return countryList.get(0);
        }
    }

    @Override
    public int saveOrUpdate(Map<String, Object> data) throws Exception {
        if (data.get("idList") != null) {
            return mallCountryDao.update(data);
        } else {
            return mallCountryDao.save(data);
        }
    }

    @Override
    public int detele(List<Integer> ids) throws Exception {
        int count = 0;
        for (Integer id : ids) {
            //todo delete relative country_brands
            count += mallCountryDao.delete(id);
        }
        return count;
    }
}
