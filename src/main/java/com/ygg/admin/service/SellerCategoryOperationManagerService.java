package com.ygg.admin.service;

import java.util.Map;

/**
 * @author lorabit
 * @since 16-5-11
 */
public interface SellerCategoryOperationManagerService {

    int saveOrUpdate(Map<String, Object> data);

    Map<String, Object> jsonInfo(Map<String, Object> para) throws Exception;
}
