package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-4-25
 */
public interface BrandHotDao {

    List<Map<String, Object>> findAllHotBrandByPara(Map<String, Object> para);

    int countFindAllHotBrandByPara(Map<String, Object> para);

    int save(Map<String, Object> data);

    int delete(int id);

    int update(Map<String, Object> para);
}
