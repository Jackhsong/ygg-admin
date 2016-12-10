package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-5-7
 */
public interface ProductUseScopeDao {
    int insertProductUseScope(Map<String, Object> data);

    int updateProductUserScope(Map<String, Object> data);

    int deleteProductUseScopeById(int id);

    List<Map<String, Object>> findProductUseScope(Map<String, Object> para);

    int countListInfo(Map<String, Object> para);

}
