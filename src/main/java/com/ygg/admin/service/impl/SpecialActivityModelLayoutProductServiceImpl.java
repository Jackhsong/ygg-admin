package com.ygg.admin.service.impl;

import com.google.common.base.Preconditions;
import com.ygg.admin.dao.SpecialActivityModelLayoutProductDao;
import com.ygg.admin.entity.ProductEntity;
import com.ygg.admin.service.ProductService;
import com.ygg.admin.service.SpecialActivityModelLayoutProductService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("specialActivityModelLayoutProductService")
public class SpecialActivityModelLayoutProductServiceImpl implements SpecialActivityModelLayoutProductService
{
    @Resource
    private SpecialActivityModelLayoutProductDao specialActivityModelLayoutProductDao;

    @Resource
    private ProductService productService;
    
    @Override
    public Map<String, Object> findListByParam(Map<String, Object> param)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", specialActivityModelLayoutProductDao.findListByParam(param));
        result.put("total", specialActivityModelLayoutProductDao.countByParam(param));
        return result;
    }
    
    @Override
    public int saveOrUpdate(Map<String, Object> param)
        throws Exception
    {
        // 如果存在图片，计算图片相关信息
        if(param.get("id") == null || StringUtils.isBlank(param.get("id").toString())) {
            // ID不存在，说明是新增
            param.put("sequence", 0);
            return specialActivityModelLayoutProductDao.save(param);
        } else {
            // ID存在，说明是更新操作
            return specialActivityModelLayoutProductDao.updateByParam(param);
        }
    }

    @Override
    public int saveByQuickAdd(Set<Integer> productIds, int layoutId) throws Exception {
        int successCount = 0;
        Map<String, Object> qPara = new HashMap<>();
        qPara.put("layoutId", layoutId);
        for(Integer id : productIds){
            qPara.put("productId", id);
            List<Map<String, Object>> layoutProducts = specialActivityModelLayoutProductDao.findListByParam(qPara);
            Preconditions.checkArgument(CollectionUtils.isEmpty(layoutProducts), "id为" + id + "的商品已经存在于布局");
            ProductEntity product = productService.findProductById(id);

            Preconditions.checkNotNull(product, "id为"+id+"的商品不存在");
            Map<String, Object> param = new HashMap<>();
            param.put("layoutId", layoutId);
            param.put("desc", product.getName());
            param.put("productId", id);
            param.put("isDisplay", 1);
            int sequence = specialActivityModelLayoutProductDao.findMaxSequence(layoutId) + 1;
            param.put("sequence", sequence);
            successCount += specialActivityModelLayoutProductDao.save(param);
        }
        return successCount;
    }
}
