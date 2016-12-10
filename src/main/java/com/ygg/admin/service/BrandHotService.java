package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-4-25
 */
public interface BrandHotService {

    List<Map<String, Object>> jsonInfo(Map<String, Object> para) throws Exception;

    int countJsonInfo(Map<String, Object> para) throws Exception;

    Map<String, Object> findHotBrandByBrandId(int brandId);

    int saveOrUpdate(Map<String, Object> data);

    int detele(List<Integer> ids);
}
