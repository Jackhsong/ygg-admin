package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-4-26
 */
public interface MallCountryBrandDao {

    List<Map<String, Object>> findAllMallCountryBrandByPara(Map<String, Object> para);

    List<Map<String, Object>> jsonInfo(Map<String, Object> para);

    int countJsonInfo(Map<String, Object> para);

    int save(Map<String, Object> data);

    int delete(int id);

    int update(Map<String, Object> para);
}
