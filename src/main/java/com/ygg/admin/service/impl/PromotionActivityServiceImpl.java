package com.ygg.admin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.dao.ProductDao;
import com.ygg.admin.dao.PromotionActivityDao;
import com.ygg.admin.service.PromotionActivityService;
import com.ygg.admin.util.CommonUtil;

@Service("promotionActivityService")
public class PromotionActivityServiceImpl implements PromotionActivityService
{
    private Logger log = Logger.getLogger(PromotionActivityServiceImpl.class);
    
    @Resource
    private PromotionActivityDao promotionActivityDao;
    
    @Resource
    private ProductDao productDao;
    
    @Override
    public Map<String, Object> findSpecialActivityNewByPara(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> infos = promotionActivityDao.findSpecialActivityNewByPara(para);
        int total = 0;
        if (infos.size() > 0)
        {
            for (Map<String, Object> map : infos)
            {
                int isAvailable = Integer.valueOf(map.get("isAvailable") + "");
                map.put("availableDesc", isAvailable == 1 ? "可用" : "停用");
                map.put("imageURL", "<a href='" + map.get("image") + "' target='_blank'>查看图片</a>");
                map.put("url", "http://m.gegejia.com/ygg/special/sceneWeb/" + map.get("id"));
                //                map.put("createTime", DateTimeUtil.dateToString(DateTimeUtil.string2Date(map.get("createTime") + "")));
            }
            total = promotionActivityDao.countSpecialActivityNewByPara(para);
        }
        result.put("rows", infos);
        result.put("total", total);
        return result;
    }

    @Override
    public List<Map<String, Object>> findSpecialActivityNewByPara()
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        para.put("isAvailable", 1);
        List<Map<String, Object>> infos = promotionActivityDao.findSpecialActivityNewByPara(para);
        return infos;
    }

    @Override
    public int saveSpecialActivityNew(Map<String, Object> para)
        throws Exception
    {
        return promotionActivityDao.saveSpecialActivityNew(para);
    }
    
    @Override
    public int updateSpecialActivityNew(Map<String, Object> para)
        throws Exception
    {
        return promotionActivityDao.updateSpecialActivityNew(para);
    }
    
    @Override
    public Map<String, Object> findSpecialActivityNewProductByPara(Map<String, Object> para)
        throws Exception
    {
        int type = Integer.valueOf(para.get("type") + ""); // 1 : 楼层商品； 2 ： 更多商品
        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, Object>> infoList = promotionActivityDao.findSpecialActivityNewProductByPara(para);
        int total = 0;
        if (infoList.size() > 0)
        {
            if (type == 1)
            {
                for (Map<String, Object> map : infoList)
                {
                    int isDisplay = Integer.valueOf(map.get("isDisplay") + "").intValue();
                    map.put("displayDesc", isDisplay == 1 ? "展现" : "不展现");
                }
            }
            else
            {
                Set<Integer> productIdSet = new HashSet<>();
                for (Map<String, Object> map : infoList)
                {
                    int isDisplay = Integer.valueOf(map.get("isDisplay") + "").intValue();
                    map.put("displayDesc", isDisplay == 1 ? "展现" : "不展现");
                    productIdSet.add(Integer.valueOf(map.get("productId") + ""));
                }
                // 取商品名称
                Map<String, Object> _para = new HashMap<>();
                _para.put("idList", CommonUtil.setToList(productIdSet));
                List<Map<String, Object>> pInfos = productDao.findAllProductSimpleByPara(_para);
                Map<String, Object> _tempMap = new HashMap<>();
                for (Map<String, Object> pInfo : pInfos)
                {
                    String id = pInfo.get("id") + "";
                    _tempMap.put(id, pInfo.get("name") + "");
                }
                for (Map<String, Object> map : infoList)
                {
                    String productId = map.get("productId") + "";
                    map.put("name", _tempMap.get(productId) == null ? "" : _tempMap.get(productId) + "");
                }
            }
            total = promotionActivityDao.countSpecialActivityNewProductByPara(para);
        }
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public int updateSpecialActivityNewProduct(Map<String, Object> para)
        throws Exception
    {
        return promotionActivityDao.updateSpecialActivityNewProduct(para);
    }
    
    @Override
    public int saveSpecialActivityNewProduct(Map<String, Object> para)
        throws Exception
    {
        return promotionActivityDao.saveSpecialActivityNewProduct(para);
    }
    
    @Override
    public Map<String, Object> findSpecialActivityNewById(int id)
        throws Exception
    {
        return promotionActivityDao.findSpecialActivityNewByid(id);
    }
    
    @Override
    public String batchAddPromotionActivityProduct(int activityId, int type, int isDisplay, List<Integer> productIdList)
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        para.put("specialActivityNewId", activityId);
        
        List<Integer> existsProductIds = new ArrayList<Integer>();
        List<Map<String, Object>> products = promotionActivityDao.findSpecialActivityNewProductByPara(para);
        for (Map<String, Object> it : products)
        {
            existsProductIds.add(Integer.parseInt(it.get("productId") + ""));
        }
        
        productIdList.removeAll(existsProductIds);
        int result = 0;
        if (productIdList.size() > 0)
        {
            para.clear();
            para.put("specialActivityNewId", activityId);
            para.put("title", "");
            para.put("keyword", "");
            para.put("type", type);
            para.put("desc", "");
            para.put("isDisplay", isDisplay);
            for (Integer productId : productIdList)
            {
                para.put("productId", productId);
                result += promotionActivityDao.saveSpecialActivityNewProduct(para);
            }
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", 1);
        resultMap.put("msg", "成功添加" + result + "条");
        
        return JSON.toJSONString(resultMap);
    }

    @Override
    public int deleteSpecialActivityNewProductById(int id)
        throws Exception
    {
        return promotionActivityDao.deleteSpecialActivityNewProductById(id);
    }
}
