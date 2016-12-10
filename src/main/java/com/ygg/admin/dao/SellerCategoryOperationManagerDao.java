package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-5-11
 */
public interface SellerCategoryOperationManagerDao {
    int save(Map<String, Object> data);

    int update(Map<String, Object> data);

    List<Map<String, Object>> findByPara(Map<String, Object> para);

    int countByPara(Map<String, Object> para);
}
