package com.ygg.admin.service.impl;

import com.ygg.admin.dao.ProductUseScopeDao;
import com.ygg.admin.service.ProductUseScopeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-5-7
 */
@Service
public class ProductUseScopeServiceImpl implements ProductUseScopeService {
    @Resource
    private ProductUseScopeDao productUseScopeDao;

    @Override
    public Map<String, Object> jsonListInfo(Map<String, Object> para) {
        List<Map<String, Object>> rows = productUseScopeDao.findProductUseScope(para);
        for (Map<String, Object> row : rows) {
            if (row.get("isAvailable") != null && (Integer)row.get("isAvailable") == 1) {
                row.put("status", "可用");
            } else {
                row.put("status", "不可用");
            }
        }
        int total = productUseScopeDao.countListInfo(para);
        Map<String, Object> ret = new HashMap<>();
        ret.put("rows", rows);
        ret.put("total", total);
        return ret;
    }

    @Override
    public int saveOrUpdate(Map<String, Object> data) {
        if (data.get("idList") != null) {
            return productUseScopeDao.updateProductUserScope(data);
        } else {
            return productUseScopeDao.insertProductUseScope(data);
        }
    }

    @Override
    public int delete(List<Integer> ids) {
        for (Integer id : ids) {
            productUseScopeDao.deleteProductUseScopeById(id);
        }
        return ids.size();
    }

}
