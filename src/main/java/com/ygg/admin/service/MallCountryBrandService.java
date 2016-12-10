package com.ygg.admin.service;

import sun.tools.jar.resources.jar_sv;

import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-4-26
 */
public interface MallCountryBrandService {

    List<Map<String, Object>> findAllMallCountryBrandByPara(Map<String, Object> para) throws Exception;

    List<Map<String, Object>> jsonInfo(Map<String, Object> para) throws Exception;

    int countJsonInfo(Map<String, Object> para) throws Exception;

    int saveOrUpdate(Map<String, Object> data) throws Exception;

    int delete(List<Integer> ids) throws Exception;

}
