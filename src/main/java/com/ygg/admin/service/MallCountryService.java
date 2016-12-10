package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-4-26
 */

public interface MallCountryService {
    List<Map<String, Object>> jsonInfo(Map<String, Object> para) throws Exception;

    int countJsonInfo(Map<String, Object> para) throws Exception;

    Map<String, Object> findMallCountryBySaleFlagId(int saleFlagId);

    int saveOrUpdate(Map<String, Object> data) throws Exception;

    int detele(List<Integer> ids) throws Exception;

}
