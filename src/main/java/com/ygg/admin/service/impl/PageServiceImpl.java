package com.ygg.admin.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ygg.admin.code.CustomEnum;
import com.ygg.admin.code.CustomLayoutStyleTypeEnum;
import com.ygg.admin.code.PageModelTypeEnum;
import com.ygg.admin.code.ProductEnum;
import com.ygg.admin.dao.ActivitiesCommonDao;
import com.ygg.admin.dao.CustomActivitiesDao;
import com.ygg.admin.dao.PageDao;
import com.ygg.admin.dao.ProductDao;
import com.ygg.admin.entity.ActivitiesCommonEntity;
import com.ygg.admin.entity.PageModelBannerEntity;
import com.ygg.admin.entity.PageModelCustomLayoutEntity;
import com.ygg.admin.entity.PageModelEntity;
import com.ygg.admin.entity.ProductEntity;
import com.ygg.admin.service.PageService;
import com.ygg.admin.util.CommonUtil;
import com.ygg.admin.util.DateTimeUtil;
import com.ygg.admin.util.ImageUtil;

@Service("pageService")
public class PageServiceImpl implements PageService
{
    public Logger log = Logger.getLogger(PageServiceImpl.class);
    
    @Resource
    public PageDao pageDao;
    
    @Resource
    public ProductDao productDao;
    
    @Resource
    public CustomActivitiesDao customActivitiesDao;
    
    @Resource
    public ActivitiesCommonDao activitiesCommonDao;
    
    @Override
    public Map<String, Object> findPageById(int id)
        throws Exception
    {
        return pageDao.findPageById(id);
    }
    
    @Override
    public Map<String, Object> findAllPageByPara(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> pageList = pageDao.findAllPageByPara(para);
        int total = 0;
        if (!pageList.isEmpty())
        {
            total = pageDao.countAllPageByPara(para);
            for (Map<String, Object> map : pageList)
            {
                map.put("availableDesc", Integer.valueOf(map.get("isAvailable") + "") == 1 ? "可用" : "停用");
                map.put("index", map.get("id"));
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("rows", pageList);
        result.put("total", total);
        return result;
    }
    
    @Override
    public int insertOrUpdatePage(Map<String, Object> para)
        throws Exception
    {
        int id = Integer.valueOf(para.get("id") + "");
        int status = 0;
        if (id == 0)
        {
            status = pageDao.insertPage(para);
        }
        else
        {
            status = pageDao.updatePageById(para);
        }
        return status;
    }
    
    @Override
    public Map<String, Object> findPageModelByPara(Map<String, Object> para)
        throws Exception
    {
        List<PageModelEntity> pmeList = pageDao.findAllPageModelByPara(para);
        List<Map<String, Object>> rows = new ArrayList<>();
        for (PageModelEntity model : pmeList)
        {
            Map<String, Object> row = new HashMap<>();
            row.put("id", model.getId() + "");
            row.put("displayStatus", model.getIsDisplay() == 1 ? "展示" : "不展示");
            row.put("name", model.getName());
            row.put("remark", model.getRemark());
            row.put("sequence", model.getSequence() + "");
            row.put("typeStr", PageModelTypeEnum.getDescriptionByOrdinal(model.getType()));
            row.put("type", model.getType());
            row.put("isDisplay", model.getIsDisplay());
            rows.add(row);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("total", rows.size());
        result.put("rows", rows);
        return result;
    }
    
    @Override
    public int insertOrUpdatePageModel(Map<String, Object> para)
        throws Exception
    {
        int id = Integer.valueOf(para.get("id") + "");
        int status = 0;
        if (id == 0)
        {
            status = pageDao.insertPageModel(para);
        }
        else
        {
            status = pageDao.updatePageModelById(para);
        }
        return status;
    }
    
    @Override
    public PageModelEntity findPageModelById(int id)
        throws Exception
    {
        return pageDao.findPageModelById(id);
    }
    
    @Override
    public PageModelCustomLayoutEntity findCustomLayoutInfoById(int id)
        throws Exception
    {
        return pageDao.findPageModelCustomLayoutById(id);
    }
    
    @Override
    public Map<String, Object> findModelCustomLayoutInfo(int modelId)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        List<PageModelCustomLayoutEntity> pcleList = pageDao.findAllPageModelCustomLayoutByModelId(modelId);
        List<Map<String, Object>> rows = new ArrayList<>();
        int total = 0;
        if (pcleList.size() > 0)
        {
            for (PageModelCustomLayoutEntity e : pcleList)
            {
                Map<String, Object> map = new HashMap<>();
                int isDisplay = e.getIsDisplay();
                int displayStyle = e.getDisplayType();
                map.put("displayDesc", isDisplay == 1 ? "展现" : "不展现");
                map.put("isDisplay", isDisplay);
                map.put("id", e.getId());
                map.put("sequence", e.getSequence());
                if (displayStyle == CustomLayoutStyleTypeEnum.LAYOUT_STYLE_OF_SINGLE.ordinal())
                {
                    map.put("layout", "一行 1 张");
                    map.put("oneRemark", e.getOneRemark());
                    map.put("twoRemark", "-");
                    map.put("threeRemark", "-");
                    map.put("fourRemark", "-");
                }
                else if (displayStyle == CustomLayoutStyleTypeEnum.LAYOUT_STYLE_OF_DOUBLE.ordinal())
                {
                    map.put("layout", "一行 2 张");
                    map.put("oneRemark", e.getOneRemark());
                    map.put("twoRemark", e.getTwoRemark());
                    map.put("threeRemark", "-");
                    map.put("fourRemark", "-");
                }
                else if (displayStyle == CustomLayoutStyleTypeEnum.LAYOUT_STYLE_OF_FOUR.ordinal())
                {
                    map.put("layout", "一行 4 张");
                    map.put("oneRemark", e.getOneRemark());
                    map.put("twoRemark", e.getTwoRemark());
                    map.put("threeRemark", e.getThreeRemark());
                    map.put("fourRemark", e.getFourRemark());
                }
                rows.add(map);
            }
            total = pageDao.countAllPageModelCustomLayoutByModelId(modelId);
        }
        resultMap.put("rows", rows);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public int insertOrUpdatePageModelCustomLayout(PageModelCustomLayoutEntity customLayout)
        throws Exception
    {
        Map<String, Object> oneImageWidthAndHeight = ImageUtil.getProperty(customLayout.getOneImage());
        customLayout.setOneWidth(Integer.valueOf(oneImageWidthAndHeight.get("width") + ""));
        customLayout.setOneHeight(Integer.valueOf(oneImageWidthAndHeight.get("height") + ""));
        
        if (customLayout.getDisplayType() == CustomLayoutStyleTypeEnum.LAYOUT_STYLE_OF_DOUBLE.ordinal()
            || customLayout.getDisplayType() == CustomLayoutStyleTypeEnum.LAYOUT_STYLE_OF_FOUR.ordinal())
        {
            Map<String, Object> twoImageWidthAndHeight = ImageUtil.getProperty(customLayout.getTwoImage());
            customLayout.setTwoWidth(Integer.valueOf(twoImageWidthAndHeight.get("width") + ""));
            customLayout.setTwoHeight(Integer.valueOf(twoImageWidthAndHeight.get("height") + ""));
        }
        else
        {
            customLayout.setTwoWidth(0);
            customLayout.setTwoHeight(0);
        }
        
        if (customLayout.getDisplayType() == CustomLayoutStyleTypeEnum.LAYOUT_STYLE_OF_FOUR.ordinal())
        {
            Map<String, Object> threeImageWidthAndHeight = ImageUtil.getProperty(customLayout.getThreeImage());
            customLayout.setThreeWidth(Integer.valueOf(threeImageWidthAndHeight.get("width") + ""));
            customLayout.setThreeHeight(Integer.valueOf(threeImageWidthAndHeight.get("height") + ""));
            
            Map<String, Object> fourImageWidthAndHeight = ImageUtil.getProperty(customLayout.getFourImage());
            customLayout.setFourWidth(Integer.valueOf(fourImageWidthAndHeight.get("width") + ""));
            customLayout.setFourHeight(Integer.valueOf(fourImageWidthAndHeight.get("height") + ""));
        }
        else
        {
            customLayout.setThreeWidth(0);
            customLayout.setThreeHeight(0);
            customLayout.setFourWidth(0);
            customLayout.setFourHeight(0);
        }
        
        if (customLayout.getId() == 0)
        {
            int sequence = pageDao.findPageModelCustomLayoutMaxSequence(customLayout.getPageModelId());
            customLayout.setSequence(sequence);
            return pageDao.insertPageModelCustomLayout(customLayout);
        }
        else
        {
            return pageDao.updatePageModelCustomLayout(customLayout);
        }
    }
    
    @Override
    public int updatePageModelCustomLayoutSimpleData(Map<String, Object> para)
        throws Exception
    {
        return pageDao.updatePageModelCustomLayoutSimpleData(para);
    }
    
    @Override
    public Map<String, Object> findModelRollProductInfo(Map<String, Object> para)
        throws Exception
    {
        int total = 0;
        List<Map<String, Object>> productList = pageDao.findAllPageModelRelationRollProduct(para);
        if (!productList.isEmpty())
        {
            total = pageDao.countAllPageModelRelationRollProduct(para);
            for (Map<String, Object> p : productList)
            {
                int isDisplay = Integer.valueOf(p.get("isDisplay") + "");
                int type = Integer.valueOf(p.get("type") + "");
                p.put("displayDesc", isDisplay == 1 ? "展现" : "不展现");
                p.put("productTypeDesc", ProductEnum.PRODUCT_TYPE.getDescByCode(type));
                if (type != 1)
                {
                    p.put("productStartTime", "");
                    p.put("productEndTime", "");
                }
                else
                {
                    p.put("productStartTime", CommonUtil.date2String(CommonUtil.string2Date(p.get("startTime") + "", "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss"));
                    p.put("productEndTime", CommonUtil.date2String(CommonUtil.string2Date(p.get("endTime") + "", "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss"));
                }
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("rows", productList);
        return result;
    }
    
    @Override
    public int updatePageModelRelationRollProduct(Map<String, Object> para)
        throws Exception
    {
        return pageDao.updatePageModelRelationRollProduct(para);
    }
    
    @Override
    public int deletePageModelRelationRollProduct(List<Integer> list)
        throws Exception
    {
        return pageDao.deletePageModelRelationRollProduct(list);
    }
    
    @Override
    public int insertBatchPageModelRelationRollProduct(List<Map<String, Object>> list, int pageModelId)
        throws Exception
    {
        int maxSequence = pageDao.findMaxSequencePageModelRelationRollProduct(pageModelId);
        maxSequence++;
        for (Map<String, Object> it : list)
        {
            int productId = Integer.valueOf(it.get("productId") + "");
            if (productDao.findProductByID(productId, null) == null)
            {
                return -2;
            }
            it.put("sequence", maxSequence++);
        }
        return pageDao.insertBatchPageModelRelationRollProduct(list);
    }
    
    @Override
    public Map<String, Object> findModelColumnProductInfo(Map<String, Object> para)
        throws Exception
    {
        int total = 0;
        List<Map<String, Object>> productList = pageDao.findAllPageModelRelationColumnProduct(para);
        if (!productList.isEmpty())
        {
            total = pageDao.countAllPageModelRelationColumnProduct(para);
            for (Map<String, Object> p : productList)
            {
                int isDisplay = Integer.valueOf(p.get("isDisplay") + "");
                int type = Integer.valueOf(p.get("type") + "");
                p.put("displayDesc", isDisplay == 1 ? "展现" : "不展现");
                p.put("productTypeDesc", ProductEnum.PRODUCT_TYPE.getDescByCode(type));
                if (type == ProductEnum.PRODUCT_TYPE.SALE.getCode())
                {
                    p.put("productStartTime", CommonUtil.date2String(CommonUtil.string2Date(p.get("startTime") + "", "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss"));
                    p.put("productEndTime", CommonUtil.date2String(CommonUtil.string2Date(p.get("endTime") + "", "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss"));
                }
                else if (type == ProductEnum.PRODUCT_TYPE.MALL.getCode())
                {
                    p.put("productStartTime", "");
                    p.put("productEndTime", "");
                }
                
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("rows", productList);
        return result;
    }
    
    @Override
    public int updatePageModelRelationColumnProduct(Map<String, Object> para)
        throws Exception
    {
        return pageDao.updatePageModelRelationColumnProduct(para);
    }
    
    @Override
    public int deletePageModelRelationColumnProduct(List<Integer> list)
        throws Exception
    {
        return pageDao.deletePageModelRelationColumnProduct(list);
    }
    
    @Override
    public int insertBatchPageModelRelationColumnProduct(List<Map<String, Object>> list, int pageModelId)
        throws Exception
    {
        int maxSequence = pageDao.findMaxSequencePageModelRelationColumnProduct(pageModelId);
        maxSequence++;
        for (Map<String, Object> it : list)
        {
            int productId = Integer.valueOf(it.get("productId") + "");
            if (productDao.findProductByID(productId, null) == null)
            {
                return -2;
            }
            it.put("sequence", maxSequence++);
        }
        return pageDao.insertBatchPageModelRelationColumnProduct(list);
    }
    
    @Override
    public Map<String, Object> findAllPageModelBanner(Map<String, Object> para)
        throws Exception
    {
        List<PageModelBannerEntity> pmbeList = pageDao.findAllPageModelBanner(para);
        int total = 0;
        List<Map<String, Object>> rows = new ArrayList<>();
        if (!pmbeList.isEmpty())
        {
            total = pageDao.countAllPageModelBanner(para);
            for (PageModelBannerEntity it : pmbeList)
            {
                Map<String, Object> row = new HashMap<>();
                row.put("id", it.getId());
                row.put("isDisplay", it.getIsDisplay());
                row.put("displayDesc", it.getIsDisplay() == 1 ? "展现" : "不展现");
                Date start = DateTimeUtil.string2Date(it.getStartTime());
                Date end = DateTimeUtil.string2Date(it.getEndTime());
                if (new Date().before(start))
                {
                    row.put("activityStatus", "即将开始");
                }
                else if (end.before(new Date()))
                {
                    row.put("activityStatus", "已结束");
                }
                else
                {
                    row.put("activityStatus", "进行中");
                }
                String typeStr = "";
                int displayId = it.getDisplayId();
                String displayName = "";
                String displayRemark = "";
                if (it.getType() == 1)
                {
                    typeStr = "单品";
                    ProductEntity product = productDao.findProductByID(displayId, null);
                    displayName = product.getShortName();
                    displayRemark = product.getRemark();
                }
                else if (it.getType() == 2)
                {
                    typeStr = "组合特卖";
                    ActivitiesCommonEntity ac = activitiesCommonDao.findAcCommonById(displayId);
                    displayName = ac.getName();
                    displayRemark = ac.getDesc();
                }
                else if (it.getType() == 3)
                {
                    typeStr = "自定义活动";
                    Map<String, Object> customActivitiesMap = customActivitiesDao.findCustomActivitiesById(displayId);
                    if (customActivitiesMap != null)
                    {
                        int type = Integer.valueOf(customActivitiesMap.get("typeCode") + "").intValue();
                        displayName = CustomEnum.CUSTOM_ACTIVITY_RELATION.getDescrByCode(type);
                        displayRemark = customActivitiesMap.get("remark") + "";
                    }
                }
                else if (it.getType() == 6)
                {
                    typeStr = "点击不跳转";
                }
                row.put("typeStr", typeStr);
                row.put("displayId", displayId);
                row.put("displayName", displayName);
                row.put("displayRemark", displayRemark);
                row.put("imageURL", "<a href='" + it.getImage() + "' target='_blank'>查看图片</a>");
                row.put("desc", it.getDesc());
                row.put("startTime", it.getStartTime());
                row.put("endTime", it.getEndTime());
                rows.add(row);
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("rows", rows);
        return result;
    }
    
    @Override
    public int insertOrUpdatePageModelBanner(PageModelBannerEntity entity)
        throws Exception
    {
        int status = 0;
        if (entity.getId() == 0)
        {
            status = pageDao.insertPageModelBanner(entity);
        }
        else
        {
            status = pageDao.updatePageModelBanner(entity);
        }
        return status;
    }
    
    @Override
    public int updateSimplePageModelBanner(Map<String, Object> para)
        throws Exception
    {
        return pageDao.updateSimplePageModelBanner(para);
    }
    
    @Override
    public PageModelBannerEntity findPageModelBannerById(int id)
        throws Exception
    {
        return pageDao.findPageModelBannerById(id);
    }
    
    @Override
    public Map<String, Object> findProductListForAdd(Map<String, Object> para)
        throws Exception
    {
        //        List<Integer> idList = activitiesCommonDao.findAllProductIdByActivitiesCommonId((int)para.get("cid"));
        //        if (idList != null && idList.size() > 0)
        //        {
        //            para.put("idList", idList);
        //        }
        //        List<ProductInfoForGroupSale> reList = activitiesCommonDao.findProductInfoForGroupSale(para);
        //        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        //        int total = 0;
        //        if (reList.size() > 0)
        //        {
        //            for (ProductInfoForGroupSale curr : reList)
        //            {
        //                Map<String, Object> map = new HashMap<String, Object>();
        //                map.put("id", curr.getProductId());
        //                map.put("showId", curr.getProductId());
        //                map.put("order", curr.getOrder());
        //                map.put("code", curr.getCode());
        //                map.put("name", curr.getName());
        //                map.put("stock", curr.getStock());
        //                map.put("marketPrice", curr.getMarketPrice());
        //                map.put("salesPrice", curr.getSalesPrice());
        //                map.put("sellerName", curr.getSellerName());
        //                map.put("brandName", curr.getBrandName());
        //                map.put("sendAddress", curr.getSendAddress());
        //                map.put("remark", curr.getRemark());
        //                map.put("warehouse", curr.getWarehouse());
        //                resultList.add(map);
        //            }
        //            total = activitiesCommonDao.countProductInfoForGroupSale(para);
        //        }
        Map<String, Object> resultMap = new HashMap<>();
        //        resultMap.put("rows", resultList);
        //        resultMap.put("total", total);
        return resultMap;
    }
}
