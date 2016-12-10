package com.ygg.admin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.ygg.admin.code.CustomLayoutRelationTypeEnum;
import com.ygg.admin.code.ProductEnum;
import com.ygg.admin.dao.ProductDao;
import com.ygg.admin.entity.ProductEntity;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.ygg.admin.code.SellerEnum;
import com.ygg.admin.dao.SellerBlacklistDao;
import com.ygg.admin.dao.SellerDao;
import com.ygg.admin.service.SellerBlacklistService;

/**
 * 商家黑名单 服务
 */
@Service("sellerBlacklistService")
public class SellerBlacklistServiceImpl implements SellerBlacklistService
{
    //    private Logger log = Logger.getLogger(SellerBlacklistDaoImpl.class);
    
    @Resource(name = "sellerBlacklistDao")
    private SellerBlacklistDao sellerBlacklistDao;
    
    @Resource
    private SellerDao sellerDao;

    @Resource
    ProductDao productDao;
    
    @Override
    public Map<String, Object> findSellerBlackInfo(int type, int sellerId, int isAvailable, int page, int rows)
        throws Exception
    {
        List<Map<String, Object>> dataRows = sellerBlacklistDao.findSellerBlackInfo(type, sellerId, isAvailable, page, rows);
        int total = 0;
        if (dataRows.size() > 0)
        {
            total = sellerBlacklistDao.countSellerBlackInfo(type, sellerId, isAvailable);
            List<Integer> sellerIdList = new ArrayList<>();
            for (Map<String, Object> row : dataRows)
            {
                sellerIdList.add(Integer.valueOf(row.get("sellerId") + ""));
                row.put("typeStr", SellerEnum.SellerBlackTypeEnum.getDescByCode(Integer.valueOf(row.get("type") + "")));
            }
            
            // 查找商家信息
            Map<String, Object> sellerPara = new HashMap<>();
            sellerPara.put("idList", sellerIdList);
            List<Map<String, Object>> sellerInfoList = sellerDao.findSellerInfoBySellerIdList(sellerPara);
            Map<Integer, Map<String, Object>> sellerInfoMap = Maps.uniqueIndex(sellerInfoList, new Function<Map<String, Object>, Integer>()
            {
                @Override
                public Integer apply(Map<String, Object> map)
                {
                    return Integer.valueOf(map.get("id") + "");
                }
            });
            
            for (Map<String, Object> row : dataRows)
            {
                Integer sId = Integer.valueOf(row.get("sellerId") + "");
                Map<String, Object> sInfo = sellerInfoMap.get(sId);
                row.put("sellerName", sInfo != null ? sInfo.get("realSellerName") : "");
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("rows", dataRows);
        result.put("total", total);
        return result;
    }

    @Override
    public Map<String, Object> saveOrUpdateSellerBlackInfo(Map<String, Object> param)
        throws Exception
    {
        int id = (param.get("id") == null || "".equals(param.get("id"))) ? 0 : Integer.valueOf(param.get("id") + "");
        int type = (param.get("type") == null || "".equals(param.get("type"))) ? 0 : Integer.valueOf(param.get("type") + "");
        int displayType = (param.get("displayType") == null || "".equals(param.get("displayType"))) ? 0 : Integer.valueOf(param.get("displayType") + "");
        int sellerId = Integer.valueOf(param.get("sellerId") + "");
        double thresholdPrice = (param.get("thresholdPrice") == null || "".equals(param.get("thresholdPrice"))) ? 0 : Double.valueOf(param.get("thresholdPrice") + "");
        int onePageId = (param.get("onePageId") == null || "".equals(param.get("onePageId"))) ? 0 : Integer.valueOf(param.get("onePageId") + "");
        int oneActivitiesCustomId = (param.get("oneActivitiesCustomId") == null || "".equals(param.get("oneActivitiesCustomId"))) ? 0 : Integer.valueOf(param.get("oneActivitiesCustomId") + "");
        int oneActivitiesCommonId = (param.get("oneActivitiesCommonId") == null || "".equals(param.get("oneActivitiesCommonId"))) ? 0 : Integer.valueOf(param.get("oneActivitiesCommonId") + "");
        int oneProductId = (param.get("oneProductId") == null || "".equals(param.get("oneProductId"))) ? 0 : Integer.valueOf(param.get("oneProductId") + "");

        if (sellerDao.findSellerById(sellerId) == null)
        {
            throw new RuntimeException("商家爱(id:" + sellerId + ")不存在！");
        }
        if (type == 1)
        {
            // 邮费黑名单
            if (thresholdPrice > 0)
            {
                // 存在包邮门槛时必须设置调整类型
                if (displayType == 1)
                {
                    ProductEntity product = productDao.findProductByID(oneProductId, null);
                    if (product == null)
                    {
                        throw new RuntimeException("商品(id:" + oneProductId + ")不存在！");
                    }
                    param.put("displayId", oneProductId);
                }
                else if (displayType == 2 && oneActivitiesCommonId > 0)
                {
                    param.put("displayId", oneActivitiesCommonId);
                }
                else if (displayType == 3 && oneActivitiesCustomId > 0)
                {
                    param.put("displayId", oneActivitiesCustomId);
                }
                else if (displayType == 4 && onePageId > 0)
                {
                    param.put("displayId", onePageId);
                }
                else
                {
                    throw new RuntimeException("填写了包邮门槛后必须要选择凑单跳转类型！");
                }
            }
            else
            {
                param.put("thresholdPrice", 0);
                param.put("displayType", 0);
                param.put("displayId", 0);
            }
        }
        else
        {
            // 活动黑名单
            param.put("thresholdPrice", 0);
            param.put("displayType", 0);
            param.put("displayId", 0);
            param.put("freightMoney", 0);
        }

        int status = 0;
        if (id == 0)
        {
            status = sellerBlacklistDao.saveSellerBlackInfo(param);
        }
        else
        {
            status = sellerBlacklistDao.updateSellerBlackInfo(param);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("status", status);
        result.put("msg", status == 1 ? "保存成功" : "保存失败");
        return result;
    }

    
    @Override
    public int deleteSellerBlackInfo(int id)
        throws Exception
    {
        return sellerBlacklistDao.deleteSellerBlackInfo(id);
    }
}
