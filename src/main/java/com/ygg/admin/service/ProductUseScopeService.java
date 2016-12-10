package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-5-7
 */
public interface ProductUseScopeService {
    Map<String, Object> jsonListInfo(Map<String,Object> para);

    int saveOrUpdate(Map<String, Object> para);

    int delete (List<Integer> ids);
}
