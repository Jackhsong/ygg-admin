package com.ygg.admin.service.impl;

import com.ygg.admin.dao.SellerCategoryOperationManagerDao;
import com.ygg.admin.entity.CategoryFirstEntity;
import com.ygg.admin.service.CategoryService;
import com.ygg.admin.service.SellerCategoryOperationManagerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-5-11
 */
@Service
public class SellerCategoryOperationManagerServiceImpl implements SellerCategoryOperationManagerService {
    @Resource
    private SellerCategoryOperationManagerDao operationManagerDao;

    @Resource
    private CategoryService categoryService;

    @Override
    public int saveOrUpdate(Map<String, Object> data) {
        if (data.get("idList") != null) {
            return operationManagerDao.update(data);
        } else {
            return operationManagerDao.save(data);
        }
    }

    @Override
    public Map<String, Object> jsonInfo(Map<String, Object> para) throws Exception {
        Map<String, Object> ret = new HashMap<>();
        List<Map<String, Object>> datas = operationManagerDao.findByPara(para);
        for (Map<String, Object> data : datas) {
            Long id = (Long) data.get("categoryFirstId");
            CategoryFirstEntity category = categoryService.findCategoryFirstById(id == null ? 0 : id.intValue());
            if (category != null) {
                data.put("categoryFirstName", category.getName());
            }
        }
        ret.put("rows", datas);
        ret.put("total", operationManagerDao.countByPara(para));
        return ret;
    }
}
