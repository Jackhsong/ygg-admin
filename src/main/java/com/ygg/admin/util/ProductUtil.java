package com.ygg.admin.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.ygg.admin.code.CustomCenterDisplayTypeEnum;
import com.ygg.admin.code.CustomCenterTypeEnum;
import com.ygg.admin.code.CustomEnum;
import com.ygg.admin.code.CustomLayoutRelationTypeEnum;
import com.ygg.admin.code.PageModelTypeEnum;
import com.ygg.admin.code.ProductEnum;
import com.ygg.admin.code.SaleWindowEnum;
import com.ygg.admin.dao.ActivitiesCommonDao;
import com.ygg.admin.dao.ActivitySimplifyDao;
import com.ygg.admin.dao.BannerWindowDao;
import com.ygg.admin.dao.CustomActivitiesDao;
import com.ygg.admin.dao.CustomCenterDao;
import com.ygg.admin.dao.CustomFunctionDao;
import com.ygg.admin.dao.CustomGGRecommendDao;
import com.ygg.admin.dao.CustomNavigationDao;
import com.ygg.admin.dao.CustomRegionDao;
import com.ygg.admin.dao.IndexSettingDao;
import com.ygg.admin.dao.PageDao;
import com.ygg.admin.dao.ProductDao;
import com.ygg.admin.dao.PromotionActivityDao;
import com.ygg.admin.dao.SaleWindowDao;
import com.ygg.admin.dao.SignInDao;
import com.ygg.admin.dao.SpecialActivityDao;
import com.ygg.admin.entity.ActivitiesCommonEntity;
import com.ygg.admin.entity.BannerWindowEntity;
import com.ygg.admin.entity.CustomActivityEntity;
import com.ygg.admin.entity.CustomCenterEntity;
import com.ygg.admin.entity.CustomFunctionEntity;
import com.ygg.admin.entity.CustomGGRecommendEntity;
import com.ygg.admin.entity.CustomLayoutEntity;
import com.ygg.admin.entity.CustomRegionEntity;
import com.ygg.admin.entity.PageEntity;
import com.ygg.admin.entity.PageModelBannerEntity;
import com.ygg.admin.entity.PageModelCustomLayoutEntity;
import com.ygg.admin.entity.PageModelEntity;
import com.ygg.admin.entity.ProductEntity;
import com.ygg.admin.entity.SaleWindowEntity;

public class ProductUtil
{
    private static CustomRegionDao customRegionDao;
    
    private static ProductDao productDao;
    
    private static ActivitiesCommonDao activitiesCommonDao;
    
    private static PageDao pageDao;
    
    private static CustomActivitiesDao customActivitiesDao;
    
    private static SpecialActivityDao specialActivityDao;
    
    private static ActivitySimplifyDao activitySimplifyDao;
    
    private static PromotionActivityDao promotionActivityDao;
    
    private static BannerWindowDao bannerWindowDao;
    
    private static CustomCenterDao customCenterDao;
    
    private static CustomGGRecommendDao customGGRecommendDao;
    
    private static SaleWindowDao saleWindowDao;
    
    private static SignInDao signInDao;
    
    private static CustomNavigationDao customNavigationDao;
    
    private static CustomFunctionDao customFunctionDao;
    
    private static IndexSettingDao indexSettingDao;
    
    private static Logger logger = Logger.getLogger(ProductUtil.class);
    
    /**
     * 根据自定义板块Id查找商品Id
     * @param id：自定义板块Id
     * @return
     */
    public static List<Integer> findProductIdByCustomRegionId(int id)
    {
        Set<Integer> productSet = new HashSet<Integer>();
        try
        {
            if (customRegionDao == null)
            {
                customRegionDao = SpringBeanUtil.getBean(CustomRegionDao.class);
            }
            CustomRegionEntity region = customRegionDao.findCustomRegionById(id);
            if (region != null && region.getIsAvailable() == CommonConstant.COMMON_YES && region.getIsDisplay() == CommonConstant.COMMON_YES)
            {
                List<Integer> layoutIdList = customRegionDao.findCustomLayoutIdByCustomRegionId(id);
                if (layoutIdList != null)
                {
                    for (int layoutId : layoutIdList)
                    {
                        productSet.addAll(findProductIdByCustomLayoutId(layoutId));
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error("根据自定义板块查找商品ID出错", e);
        }
        return new ArrayList<Integer>(productSet);
    }
    
    /**
     * 查找所有展现、可用自定义板块关联商品Id
     * @return
     */
    public static List<Integer> findAllDisplayAndAvailableCustomRegion()
    {
        Set<Integer> productSet = new HashSet<Integer>();
        try
        {
            if (customRegionDao == null)
            {
                customRegionDao = SpringBeanUtil.getBean(CustomRegionDao.class);
            }
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("isAvailable", CommonConstant.COMMON_YES);
            para.put("isDisplay", CommonConstant.COMMON_YES);
            List<Map<String, Object>> list = customRegionDao.findAllCustomRegion(para);
            for (Map<String, Object> it : list)
            {
                int id = Integer.parseInt(it.get("id") + "");
                productSet.addAll(findProductIdByCustomRegionId(id));
            }
        }
        catch (Exception e)
        {
            logger.error("查找所有可用自定义板块关联商品Id出错", e);
        }
        return new ArrayList<Integer>(productSet);
    }
    
    /**
     * 根据自定义布局Id查找关联商品Id
     * @param id：自定义布局Id
     * @return
     */
    public static List<Integer> findProductIdByCustomLayoutId(int id)
    {
        Set<Integer> productSet = new HashSet<Integer>();
        try
        {
            if (customRegionDao == null)
            {
                customRegionDao = SpringBeanUtil.getBean(CustomRegionDao.class);
            }
            if (productDao == null)
            {
                productDao = SpringBeanUtil.getBean(ProductDao.class);
            }
            CustomLayoutEntity layout = customRegionDao.findCustomLayoutById(id);
            if (layout != null && layout.getIsDisplay() == CommonConstant.COMMON_YES)
            {
                if (layout.getOneType() == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_SALEPRODUCT.ordinal())
                {
                    Integer productId = findProductId(layout.getOneDisplayId());
                    if (productId != null)
                    {
                        productSet.add(productId);
                    }
                }
                if (layout.getOneType() == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_COMMONACTIVITY.ordinal())
                {
                    productSet.addAll(findProductIdByActivitiesCommonId(layout.getOneDisplayId()));
                }
                if (layout.getOneType() == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_CUSTOMSALE.ordinal())
                {
                    productSet.addAll(findProductIdByCustomActivityId(layout.getOneDisplayId()));
                }
                if (layout.getOneType() == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_MALLPRODUCT.ordinal())
                {
                    Integer productId = findProductId(layout.getOneDisplayId());
                    if (productId != null)
                    {
                        productSet.add(productId);
                    }
                }
                if (layout.getOneType() == CustomLayoutRelationTypeEnum.APP_CUSTOM_PAGE.ordinal())
                {
                    productSet.addAll(findProductIdByPageId(layout.getOneDisplayId()));
                }
                
                if (layout.getTwoType() == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_SALEPRODUCT.ordinal())
                {
                    Integer productId = findProductId(layout.getTwoDisplayId());
                    if (productId != null)
                    {
                        productSet.add(productId);
                    }
                }
                if (layout.getTwoType() == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_COMMONACTIVITY.ordinal())
                {
                    productSet.addAll(findProductIdByActivitiesCommonId(layout.getTwoDisplayId()));
                }
                if (layout.getTwoType() == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_CUSTOMSALE.ordinal())
                {
                    productSet.addAll(findProductIdByCustomActivityId(layout.getTwoDisplayId()));
                }
                if (layout.getTwoType() == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_MALLPRODUCT.ordinal())
                {
                    Integer productId = findProductId(layout.getTwoDisplayId());
                    if (productId != null)
                    {
                        productSet.add(productId);
                    }
                }
                if (layout.getTwoType() == CustomLayoutRelationTypeEnum.APP_CUSTOM_PAGE.ordinal())
                {
                    productSet.addAll(findProductIdByPageId(layout.getTwoDisplayId()));
                }
                if (layout.getThreeType() == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_SALEPRODUCT.ordinal())
                {
                    Integer productId = findProductId(layout.getThreeDisplayId());
                    if (productId != null)
                    {
                        productSet.add(productId);
                    }
                }
                if (layout.getThreeType() == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_COMMONACTIVITY.ordinal())
                {
                    productSet.addAll(findProductIdByActivitiesCommonId(layout.getThreeDisplayId()));
                }
                if (layout.getThreeType() == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_CUSTOMSALE.ordinal())
                {
                    productSet.addAll(findProductIdByCustomActivityId(layout.getThreeDisplayId()));
                }
                if (layout.getThreeType() == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_MALLPRODUCT.ordinal())
                {
                    Integer productId = findProductId(layout.getThreeDisplayId());
                    if (productId != null)
                    {
                        productSet.add(productId);
                    }
                }
                if (layout.getThreeType() == CustomLayoutRelationTypeEnum.APP_CUSTOM_PAGE.ordinal())
                {
                    productSet.addAll(findProductIdByPageId(layout.getThreeDisplayId()));
                }
                
                if (layout.getFourType() == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_SALEPRODUCT.ordinal())
                {
                    Integer productId = findProductId(layout.getFourDisplayId());
                    if (productId != null)
                    {
                        productSet.add(productId);
                    }
                }
                if (layout.getFourType() == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_COMMONACTIVITY.ordinal())
                {
                    productSet.addAll(findProductIdByActivitiesCommonId(layout.getFourDisplayId()));
                }
                if (layout.getFourType() == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_CUSTOMSALE.ordinal())
                {
                    productSet.addAll(findProductIdByCustomActivityId(layout.getFourDisplayId()));
                }
                if (layout.getFourType() == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_MALLPRODUCT.ordinal())
                {
                    Integer productId = findProductId(layout.getFourDisplayId());
                    if (productId != null)
                    {
                        productSet.add(productId);
                    }
                }
                if (layout.getFourType() == CustomLayoutRelationTypeEnum.APP_CUSTOM_PAGE.ordinal())
                {
                    productSet.addAll(findProductIdByPageId(layout.getFourDisplayId()));
                }
            }
        }
        catch (Exception e)
        {
            logger.error("自定义布局查找商品ID出错", e);
        }
        return new ArrayList<Integer>(productSet);
    }
    
    /**
     * 查找所有展现、可用自定义布局关联商品Id
     * @return
     */
    public static List<Integer> findAllDisplayAndAvailableCustomLayout()
    {
        Set<Integer> productSet = new HashSet<Integer>();
        try
        {
            if (customRegionDao == null)
            {
                customRegionDao = SpringBeanUtil.getBean(CustomRegionDao.class);
            }
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("isDisplay", CommonConstant.COMMON_YES);
            List<Map<String, Object>> list = customRegionDao.findAllCustomLayout(para);
            for (Map<String, Object> it : list)
            {
                int layoutId = Integer.parseInt(it.get("layoutId") + "");
                productSet.addAll(findProductIdByCustomLayoutId(layoutId));
            }
        }
        catch (Exception e)
        {
            logger.error("自定义布局查找商品ID出错", e);
        }
        return new ArrayList<Integer>(productSet);
    }
    
    /**
     * 根据组合Id查找商品ID
     * zhangld
     * @param id：通用专场Id
     * @return
     */
    public static List<Integer> findProductIdByActivitiesCommonId(int id)
    {
        Set<Integer> productSet = new HashSet<Integer>();
        try
        {
            if (activitiesCommonDao == null)
            {
                activitiesCommonDao = SpringBeanUtil.getBean(ActivitiesCommonDao.class);
            }
            ActivitiesCommonEntity ac = activitiesCommonDao.findActivitiesCommonById(id);
            if (ac != null && ac.getIsAvailable() == CommonConstant.COMMON_YES)
            {
                List<Integer> productIdList = activitiesCommonDao.findProductIdByActivitiesCommonId(id);
                for (Integer pid : productIdList)
                {
                    Integer productId = findProductId(pid);
                    if (productId != null)
                    {
                        productSet.add(productId);
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error("根据组合查找商品ID出错", e);
        }
        return new ArrayList<Integer>(productSet);
    }
    
    /**
     * 查找所有可用组合关联商品Id
     * @return
     */
    public static List<Integer> findAllDisplayAndAvailableActivitiesCommon()
    {
        Set<Integer> productSet = new HashSet<Integer>();
        try
        {
            if (activitiesCommonDao == null)
            {
                activitiesCommonDao = SpringBeanUtil.getBean(ActivitiesCommonDao.class);
            }
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("isAvailable", CommonConstant.COMMON_YES);
            List<ActivitiesCommonEntity> list = activitiesCommonDao.findAllAcCommonByPara(para);
            for (ActivitiesCommonEntity ac : list)
            {
                productSet.addAll(findProductIdByActivitiesCommonId(ac.getId()));
            }
        }
        catch (Exception e)
        {
            logger.error("根据专场查找商品ID出错", e);
        }
        return new ArrayList<Integer>(productSet);
    }
    
    /**
     * 根据原生自定义页面Id查找商品ID
     * zhangld  原生自定义
     * @param id：原生自定义页面Id
     * @return
     */
    public static List<Integer> findProductIdByPageId(int id)
    {
        Set<Integer> productSet = new HashSet<Integer>();
        try
        {
            if (pageDao == null)
            {
                pageDao = SpringBeanUtil.getBean(PageDao.class);
            }
            PageEntity page = pageDao.findPageBypid(id);
            if (page == null || page.getIsAvailable() == CommonConstant.COMMON_NO)
            {
                return new ArrayList<Integer>(productSet);
            }
            List<PageModelEntity> modelList = pageDao.findPageModelBypid(id);
            for (PageModelEntity model : modelList)
            {
                if (model.getIsAvailable() == CommonConstant.COMMON_NO || model.getIsDisplay() == CommonConstant.COMMON_NO)
                {
                    continue;
                }
                if (model.getType() == PageModelTypeEnum.CUSTOM.ordinal())
                {
                    List<PageModelCustomLayoutEntity> customLayoutList = pageDao.findPageModelCustomLayoutByModelId(model.getId());
                    for (PageModelCustomLayoutEntity layout : customLayoutList)
                    {
                        if (layout.getIsDisplay() == CommonConstant.COMMON_NO)
                        {
                            continue;
                        }
                        if (layout.getOneType() == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_SALEPRODUCT.ordinal())
                        {
                            Integer productId = findProductId(layout.getOneDisplayId());
                            if (productId != null)
                            {
                                productSet.add(productId);
                            }
                        }
                        if (layout.getOneType() == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_COMMONACTIVITY.ordinal())
                        {
                            productSet.addAll(findProductIdByActivitiesCommonId(layout.getOneDisplayId()));
                        }
                        if (layout.getOneType() == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_CUSTOMSALE.ordinal())
                        {
                            productSet.addAll(findProductIdByCustomActivityId(layout.getOneDisplayId()));
                        }
                        if (layout.getOneType() == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_MALLPRODUCT.ordinal())
                        {
                            Integer productId = findProductId(layout.getOneDisplayId());
                            if (productId != null)
                            {
                                productSet.add(productId);
                            }
                        }
                        
                        if (layout.getTwoType() == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_SALEPRODUCT.ordinal())
                        {
                            Integer productId = findProductId(layout.getTwoDisplayId());
                            if (productId != null)
                            {
                                productSet.add(productId);
                            }
                        }
                        if (layout.getTwoType() == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_COMMONACTIVITY.ordinal())
                        {
                            productSet.addAll(findProductIdByActivitiesCommonId(layout.getTwoDisplayId()));
                        }
                        if (layout.getTwoType() == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_CUSTOMSALE.ordinal())
                        {
                            productSet.addAll(findProductIdByCustomActivityId(layout.getTwoDisplayId()));
                        }
                        if (layout.getTwoType() == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_MALLPRODUCT.ordinal())
                        {
                            Integer productId = findProductId(layout.getTwoDisplayId());
                            if (productId != null)
                            {
                                productSet.add(productId);
                            }
                        }
                        if (layout.getThreeType() == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_SALEPRODUCT.ordinal())
                        {
                            Integer productId = findProductId(layout.getThreeDisplayId());
                            if (productId != null)
                            {
                                productSet.add(productId);
                            }
                        }
                        if (layout.getThreeType() == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_COMMONACTIVITY.ordinal())
                        {
                            productSet.addAll(findProductIdByActivitiesCommonId(layout.getThreeDisplayId()));
                        }
                        if (layout.getThreeType() == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_CUSTOMSALE.ordinal())
                        {
                            productSet.addAll(findProductIdByCustomActivityId(layout.getThreeDisplayId()));
                        }
                        if (layout.getThreeType() == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_MALLPRODUCT.ordinal())
                        {
                            Integer productId = findProductId(layout.getThreeDisplayId());
                            if (productId != null)
                            {
                                productSet.add(productId);
                            }
                        }
                        
                        if (layout.getFourType() == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_SALEPRODUCT.ordinal())
                        {
                            Integer productId = findProductId(layout.getFourDisplayId());
                            if (productId != null)
                            {
                                productSet.add(productId);
                            }
                        }
                        if (layout.getFourType() == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_COMMONACTIVITY.ordinal())
                        {
                            productSet.addAll(findProductIdByActivitiesCommonId(layout.getFourDisplayId()));
                        }
                        if (layout.getFourType() == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_CUSTOMSALE.ordinal())
                        {
                            productSet.addAll(findProductIdByCustomActivityId(layout.getFourDisplayId()));
                        }
                        if (layout.getFourType() == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_MALLPRODUCT.ordinal())
                        {
                            Integer productId = findProductId(layout.getFourDisplayId());
                            if (productId != null)
                            {
                                productSet.add(productId);
                            }
                        }
                    }
                }
                else if (model.getType() == PageModelTypeEnum.TWO_PRODUCT_LIST.ordinal())
                {
                    Map<String, Object> para = new HashMap<String, Object>();
                    para.put("pageModelId", model.getId());
                    para.put("isDisplay", 1);
                    List<Map<String, Object>> pageModelProductList = pageDao.findAllPageModelRelationColumnProduct(para);
                    for (Map<String, Object> it : pageModelProductList)
                    {
                        Integer productId = findProductId(Integer.parseInt(it.get("productId") + ""));
                        if (productId != null)
                        {
                            productSet.add(productId);
                        }
                    }
                }
                else if (model.getType() == PageModelTypeEnum.ROLL_PRODUCT.ordinal())
                {
                    Map<String, Object> para = new HashMap<String, Object>();
                    para.put("pageModelId", model.getId());
                    para.put("isDisplay", 1);
                    List<Map<String, Object>> pageModelProductList = pageDao.findAllPageModelRelationRollProduct(para);
                    for (Map<String, Object> it : pageModelProductList)
                    {
                        Integer productId = findProductId(Integer.parseInt(it.get("productId") + ""));
                        if (productId != null)
                        {
                            productSet.add(productId);
                        }
                    }
                }
                else if (model.getType() == PageModelTypeEnum.FULL_BANNER.ordinal())
                {
                    Map<String, Object> para = new HashMap<String, Object>();
                    para.put("pageModelId", model.getId());
                    para.put("bannerStatus", 2);
                    para.put("isDisplay", 1);
                    List<PageModelBannerEntity> bannerList = pageDao.findAllPageModelBanner(para);
                    for (PageModelBannerEntity banner : bannerList)
                    {
                        if (banner.getType() == 1)
                        {
                            Integer productId = findProductId(banner.getDisplayId());
                            if (productId != null)
                            {
                                productSet.add(productId);
                            }
                        }
                        else if (banner.getType() == 2)
                        {
                            productSet.addAll(findProductIdByActivitiesCommonId(banner.getDisplayId()));
                        }
                        else if (banner.getType() == 3)
                        {
                            productSet.addAll(findProductIdByCustomActivityId(banner.getDisplayId()));
                        }
                        else if (banner.getType() == 4)
                        {
                            Integer productId = findProductId(banner.getDisplayId());
                            if (productId != null)
                            {
                                productSet.add(productId);
                            }
                        }
                        else if (banner.getType() == 5)
                        {
                            productSet.addAll(findProductIdByPageId(banner.getDisplayId()));
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error("根据专场查找商品ID出错", e);
        }
        return new ArrayList<Integer>(productSet);
    }
    
    /**
     * 查找所有展现、可用原生页面关联商品Id
     * @return
     */
    public static List<Integer> findAllDisplayAndAvailablePage()
    {
        Set<Integer> productSet = new HashSet<Integer>();
        try
        {
            if (pageDao == null)
            {
                pageDao = SpringBeanUtil.getBean(PageDao.class);
            }
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("isAvailable", CommonConstant.COMMON_YES);
            List<Map<String, Object>> list = pageDao.findAllPageByPara(para);
            for (Map<String, Object> it : list)
            {
                int pageId = Integer.parseInt(it.get("id") + "");
                productSet.addAll(findProductIdByPageId(pageId));
            }
        }
        catch (Exception e)
        {
            logger.error("根据专场查找商品ID出错", e);
        }
        return new ArrayList<Integer>(productSet);
    }
    
    /**
     * 根据自定义活动查找关联的商品Id
     * zhangld
     * @param id：自定义活动Id
     * @return
     */
    public static List<Integer> findProductIdByCustomActivityId(int id)
    {
        Set<Integer> productSet = new HashSet<Integer>();
        try
        {
            if (customActivitiesDao == null)
            {
                customActivitiesDao = SpringBeanUtil.getBean(CustomActivitiesDao.class);
            }
            CustomActivityEntity ca = customActivitiesDao.findCustomActivitiesId(id);
            if (ca != null && ca.getIsAvailable() == CommonConstant.COMMON_YES)
            {
                if (ca.getType() == CustomEnum.CUSTOM_ACTIVITY_RELATION.SALE_ACTIVITY.getCode())
                {
                    productSet.addAll(findProductIdBySpecialActivityId(ca.getTypeId()));
                }
                else if (ca.getType() == CustomEnum.CUSTOM_ACTIVITY_RELATION.SIMPLIFY_ACTIVITY.getCode())
                {
                    productSet.addAll(findProductIdBySimplifyActivityId(ca.getTypeId()));
                }
                else if (ca.getType() == CustomEnum.CUSTOM_ACTIVITY_RELATION.NEW_SIMPLIFY_ACTIVITY.getCode())
                {
                    productSet.addAll(findProductIdBySpecialActivityNewId(ca.getTypeId()));
                }
            }
            
        }
        catch (Exception e)
        {
            logger.error("根据自定义活动ID查找商品ID出错", e);
        }
        return new ArrayList<Integer>(productSet);
    }
    
    /**
     * 查找所有展现、可用自定义活动关联商品Id
     * @return
     */
    public static List<Integer> findAllDisplayAndAvailableCustomActivity()
    {
        Set<Integer> productSet = new HashSet<Integer>();
        try
        {
            if (customActivitiesDao == null)
            {
                customActivitiesDao = SpringBeanUtil.getBean(CustomActivitiesDao.class);
            }
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("isAvailable", CommonConstant.COMMON_YES);
            List<Map<String, Object>> list = customActivitiesDao.findCustomActivitiesInfo(para);
            for (Map<String, Object> it : list)
            {
                int id = Integer.parseInt(it.get("id") + "");
                productSet.addAll(findProductIdByCustomActivityId(id));
            }
        }
        catch (Exception e)
        {
            logger.error("根据自定义活动ID查找商品ID出错", e);
        }
        return new ArrayList<Integer>(productSet);
    }
    
    /**
     * 根据情景特卖活动Id查找关联商品Id
     * @param id：情景特卖活动Id
     * @return
     */
    public static List<Integer> findProductIdBySpecialActivityId(int id)
    {
        
        Set<Integer> productSet = new HashSet<Integer>();
        try
        {
            if (specialActivityDao == null)
            {
                specialActivityDao = SpringBeanUtil.getBean(SpecialActivityDao.class);
            }
            if (productDao == null)
            {
                productDao = SpringBeanUtil.getBean(ProductDao.class);
            }
            List<Map<String, Object>> list = specialActivityDao.findSpecialActivityLayouProductBySpecialActivityId(id);
            for (Map<String, Object> it : list)
            {
                int displayType = Integer.parseInt(it.get("displayType") + "");
                int oneType = Integer.parseInt(it.get("oneType") + "");
                int oneDisplayId = Integer.parseInt(it.get("oneDisplayId") + "");
                if (oneType == 1)
                {
                    Integer productId = findProductId(oneDisplayId);
                    if (productId != null)
                    {
                        productSet.add(productId);
                    }
                }
                else if (oneType == 2)
                {
                    productSet.addAll(findProductIdByActivitiesCommonId(oneDisplayId));
                }
                if (displayType == 2)
                {
                    int twoType = Integer.parseInt(it.get("twoType") + "");
                    int twoDisplayId = Integer.parseInt(it.get("twoDisplayId") + "");
                    if (twoType == 1)
                    {
                        Integer productId = findProductId(twoDisplayId);
                        if (productId != null)
                        {
                            productSet.add(productId);
                        }
                    }
                    else if (twoType == 2)
                    {
                        productSet.addAll(findProductIdByActivitiesCommonId(twoDisplayId));
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error("根据情景特卖活动ID查找商品ID出错", e);
        }
        return new ArrayList<Integer>(productSet);
    }
    
    /**
     * 查找所有展现、可用情景特卖关联商品Id
     * @return
     */
    public static List<Integer> findAllDisplayAndAvailableSpecialActivity()
    {
        Set<Integer> productSet = new HashSet<Integer>();
        try
        {
            if (specialActivityDao == null)
            {
                specialActivityDao = SpringBeanUtil.getBean(SpecialActivityDao.class);
            }
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("isAvailable", CommonConstant.COMMON_YES);
            List<Map<String, Object>> list = specialActivityDao.findAllSpecialActivity(para);
            for (Map<String, Object> it : list)
            {
                int id = Integer.parseInt(it.get("id") + "");
                productSet.addAll(findProductIdBySpecialActivityId(id));
            }
        }
        catch (Exception e)
        {
            logger.error("根据情景特卖活动ID查找商品ID出错", e);
        }
        return new ArrayList<Integer>(productSet);
    }
    
    /**
     * 根据精品特卖活动Id查找关联商品Id
     * @param id：精品特卖活动Id
     * @return
     */
    public static List<Integer> findProductIdBySimplifyActivityId(int id)
    {
        Set<Integer> productSet = new HashSet<Integer>();
        try
        {
            if (activitySimplifyDao == null)
            {
                activitySimplifyDao = SpringBeanUtil.getBean(ActivitySimplifyDao.class);
            }
            List<Integer> productIdList = activitySimplifyDao.findActivitySimplifyProductIdBySimplifyActivityId(id);
            for (Integer pid : productIdList)
            {
                Integer productId = findProductId(pid);
                if (productId != null)
                {
                    productSet.add(productId);
                }
            }
        }
        catch (Exception e)
        {
            logger.error("根据精品特卖活动ID查找商品ID出错", e);
        }
        return new ArrayList<Integer>(productSet);
    }
    
    /**
     * 查找所有可用精品特卖关联商品ID
     * @return
     */
    public static List<Integer> findAllDisplayAndAvailableSimplifyActivity()
    {
        Set<Integer> productSet = new HashSet<Integer>();
        try
        {
            if (activitySimplifyDao == null)
            {
                activitySimplifyDao = SpringBeanUtil.getBean(ActivitySimplifyDao.class);
            }
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("isAvailable", CommonConstant.COMMON_YES);
            List<Map<String, Object>> list = activitySimplifyDao.findAllActivitySimplify(para);
            for (Map<String, Object> it : list)
            {
                int id = Integer.parseInt(it.get("id") + "");
                productSet.addAll(findProductIdBySimplifyActivityId(id));
            }
        }
        catch (Exception e)
        {
            logger.error("查找所有可用精品特卖关联商品ID出错", e);
        }
        return new ArrayList<Integer>(productSet);
    }
    
    /**
     * 根据新情景特卖活动Id查找关联商品Id
     * @param id：新情景特卖活动Id
     * @return
     */
    public static List<Integer> findProductIdBySpecialActivityNewId(int id)
    {
        Set<Integer> productSet = new HashSet<Integer>();
        try
        {
            if (promotionActivityDao == null)
            {
                promotionActivityDao = SpringBeanUtil.getBean(PromotionActivityDao.class);
            }
            List<Integer> productIdList = promotionActivityDao.findSpecialActivityNewProductByid(id);
            for (Integer pid : productIdList)
            {
                Integer productId = findProductId(pid);
                if (productId != null)
                {
                    productSet.add(productId);
                }
            }
        }
        catch (Exception e)
        {
            logger.error("根据新情景特卖活动ID查找商品ID出错", e);
        }
        return new ArrayList<Integer>(productSet);
    }
    
    /**
     * 查找所有可用新情景特卖活动关联商品ID
     * @return
     */
    public static List<Integer> findAllDisplayAndAvailableSpecialActivityNew()
    {
        Set<Integer> productSet = new HashSet<Integer>();
        try
        {
            if (promotionActivityDao == null)
            {
                promotionActivityDao = SpringBeanUtil.getBean(PromotionActivityDao.class);
            }
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("isAvailable", CommonConstant.COMMON_YES);
            List<Map<String, Object>> list = promotionActivityDao.findSpecialActivityNewByPara(para);
            for (Map<String, Object> it : list)
            {
                int id = Integer.parseInt(it.get("id") + "");
                productSet.addAll(findProductIdBySpecialActivityNewId(id));
            }
        }
        catch (Exception e)
        {
            logger.error("查找所有可用新情景特卖活动关联商品ID出错", e);
        }
        return new ArrayList<Integer>(productSet);
    }
    
    /**
     * 根据BannerId查找关联商品Id
     * @param id
     * @return
     */
    public static List<Integer> findProductIdByBannerWindowId(int id)
    {
        Set<Integer> productSet = new HashSet<Integer>();
        try
        {
            if (bannerWindowDao == null)
            {
                bannerWindowDao = SpringBeanUtil.getBean(BannerWindowDao.class);
            }
            
            if (productDao == null)
            {
                productDao = SpringBeanUtil.getBean(ProductDao.class);
            }
            BannerWindowEntity banner = bannerWindowDao.findBannerWindowById(id);
            if (banner != null && banner.getIsDisplay() == CommonConstant.COMMON_YES)
            {
                DateTime start = new DateTime(DateTimeUtil.string2Date(banner.getStartTime()).getTime());
                DateTime end = new DateTime(DateTimeUtil.string2Date(banner.getEndTime()).getTime());
                if (start.isBeforeNow() && end.isAfterNow())
                {
                    if (banner.getType() == 1)
                    {
                        Integer productId = findProductId(banner.getDisplayId());
                        if (productId != null)
                        {
                            productSet.add(productId);
                        }
                    }
                    else if (banner.getType() == 2)
                    {
                        productSet.addAll(findProductIdByActivitiesCommonId(banner.getDisplayId()));
                    }
                    else if (banner.getType() == 3)
                    {
                        productSet.addAll(findProductIdByCustomActivityId(banner.getDisplayId()));
                    }
                    else if (banner.getType() == 4)
                    {
                        Integer productId = findProductId(banner.getDisplayId());
                        if (productId != null)
                        {
                            productSet.add(productId);
                        }
                    }
                    else if (banner.getType() == 5)
                    {
                        productSet.addAll(findProductIdByPageId(banner.getDisplayId()));
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error("根据Banner ID查找商品ID出错", e);
        }
        return new ArrayList<Integer>(productSet);
    }
    
    /**
     * 查找所有可用Banner关联商品Id
     * @return
     */
    public static List<Integer> findAllDisplayAndAvailableBannerWindow()
    {
        Set<Integer> productSet = new HashSet<Integer>();
        try
        {
            if (bannerWindowDao == null)
            {
                bannerWindowDao = SpringBeanUtil.getBean(BannerWindowDao.class);
            }
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("status", 2);
            para.put("isDisplay", CommonConstant.COMMON_YES);
            List<BannerWindowEntity> list = bannerWindowDao.findAllBannerWindow(para);
            for (BannerWindowEntity banner : list)
            {
                productSet.addAll(findProductIdByBannerWindowId(banner.getId()));
            }
        }
        catch (Exception e)
        {
            logger.error("查找所有可用Banner关联商品ID出错", e);
        }
        return new ArrayList<Integer>(productSet);
    }
    
    /**
     * 根据自定义个人中心活动Id查找关联商品
     * @param id
     * @return
     */
    public static List<Integer> findProductIdByCustomCenterId(int id)
    {
        Set<Integer> productSet = new HashSet<Integer>();
        try
        {
            if (customCenterDao == null)
            {
                customCenterDao = SpringBeanUtil.getBean(CustomCenterDao.class);
            }
            if (productDao == null)
            {
                productDao = SpringBeanUtil.getBean(ProductDao.class);
            }
            CustomCenterEntity center = customCenterDao.findCustomCenterById(id);
            if (center != null && center.getIsDisplay() == CommonConstant.COMMON_YES)
            {
                if (center.getDisplayType() == CustomCenterDisplayTypeEnum.ONE.getCode() || center.getDisplayType() == CustomCenterDisplayTypeEnum.TWO.getCode()
                    || center.getDisplayType() == CustomCenterDisplayTypeEnum.THREE.getCode() || center.getDisplayType() == CustomCenterDisplayTypeEnum.FOUR.getCode())
                {
                    if (center.getOneType() == CustomCenterTypeEnum.SINGLE_SALE_PRODUCT.getCode())
                    {
                        Integer productId = findProductId(center.getOneDisplayId());
                        if (productId != null)
                        {
                            productSet.add(productId);
                        }
                    }
                    else if (center.getOneType() == CustomCenterTypeEnum.GROUP.getCode())
                    {
                        productSet.addAll(findProductIdByActivitiesCommonId(center.getOneDisplayId()));
                    }
                    else if (center.getOneType() == CustomCenterTypeEnum.CUSTOM_ACTIVITY.getCode())
                    {
                        productSet.addAll(findProductIdByCustomActivityId(center.getOneDisplayId()));
                    }
                    else if (center.getOneType() == CustomCenterTypeEnum.SINGLE_MALL_PRODUCT.getCode())
                    {
                        Integer productId = findProductId(center.getOneDisplayId());
                        if (productId != null)
                        {
                            productSet.add(productId);
                        }
                    }
                    else if (center.getOneType() == CustomCenterTypeEnum.CUSTOM_PAGE.getCode())
                    {
                        productSet.addAll(findProductIdByPageId(center.getOneDisplayId()));
                    }
                }
                if (center.getDisplayType() == CustomCenterDisplayTypeEnum.TWO.getCode() || center.getDisplayType() == CustomCenterDisplayTypeEnum.THREE.getCode()
                    || center.getDisplayType() == CustomCenterDisplayTypeEnum.FOUR.getCode())
                {
                    if (center.getTwoType() == CustomCenterTypeEnum.SINGLE_SALE_PRODUCT.getCode())
                    {
                        Integer productId = findProductId(center.getTwoDisplayId());
                        if (productId != null)
                        {
                            productSet.add(productId);
                        }
                    }
                    else if (center.getTwoType() == CustomCenterTypeEnum.GROUP.getCode())
                    {
                        productSet.addAll(findProductIdByActivitiesCommonId(center.getTwoDisplayId()));
                    }
                    else if (center.getTwoType() == CustomCenterTypeEnum.CUSTOM_ACTIVITY.getCode())
                    {
                        productSet.addAll(findProductIdByCustomActivityId(center.getTwoDisplayId()));
                    }
                    else if (center.getTwoType() == CustomCenterTypeEnum.SINGLE_MALL_PRODUCT.getCode())
                    {
                        Integer productId = findProductId(center.getTwoDisplayId());
                        if (productId != null)
                        {
                            productSet.add(productId);
                        }
                    }
                    else if (center.getTwoType() == CustomCenterTypeEnum.CUSTOM_PAGE.getCode())
                    {
                        productSet.addAll(findProductIdByPageId(center.getTwoDisplayId()));
                    }
                }
                
                if (center.getDisplayType() == CustomCenterDisplayTypeEnum.THREE.getCode() || center.getDisplayType() == CustomCenterDisplayTypeEnum.FOUR.getCode())
                {
                    if (center.getThreeType() == CustomCenterTypeEnum.SINGLE_SALE_PRODUCT.getCode())
                    {
                        Integer productId = findProductId(center.getThreeDisplayId());
                        if (productId != null)
                        {
                            productSet.add(productId);
                        }
                    }
                    else if (center.getThreeType() == CustomCenterTypeEnum.GROUP.getCode())
                    {
                        productSet.addAll(findProductIdByActivitiesCommonId(center.getThreeDisplayId()));
                    }
                    else if (center.getThreeType() == CustomCenterTypeEnum.CUSTOM_ACTIVITY.getCode())
                    {
                        productSet.addAll(findProductIdByCustomActivityId(center.getThreeDisplayId()));
                    }
                    else if (center.getThreeType() == CustomCenterTypeEnum.SINGLE_MALL_PRODUCT.getCode())
                    {
                        Integer productId = findProductId(center.getThreeDisplayId());
                        if (productId != null)
                        {
                            productSet.add(productId);
                        }
                    }
                    else if (center.getThreeType() == CustomCenterTypeEnum.CUSTOM_PAGE.getCode())
                    {
                        productSet.addAll(findProductIdByPageId(center.getThreeDisplayId()));
                    }
                }
                
                if (center.getDisplayType() == CustomCenterDisplayTypeEnum.FOUR.getCode())
                {
                    if (center.getFourType() == CustomCenterTypeEnum.SINGLE_SALE_PRODUCT.getCode())
                    {
                        Integer productId = findProductId(center.getFourDisplayId());
                        if (productId != null)
                        {
                            productSet.add(productId);
                        }
                    }
                    else if (center.getFourType() == CustomCenterTypeEnum.GROUP.getCode())
                    {
                        productSet.addAll(findProductIdByActivitiesCommonId(center.getFourDisplayId()));
                    }
                    else if (center.getFourType() == CustomCenterTypeEnum.CUSTOM_ACTIVITY.getCode())
                    {
                        productSet.addAll(findProductIdByCustomActivityId(center.getFourDisplayId()));
                    }
                    else if (center.getFourType() == CustomCenterTypeEnum.SINGLE_MALL_PRODUCT.getCode())
                    {
                        Integer productId = findProductId(center.getFourDisplayId());
                        if (productId != null)
                        {
                            productSet.add(productId);
                        }
                    }
                    else if (center.getFourType() == CustomCenterTypeEnum.CUSTOM_PAGE.getCode())
                    {
                        productSet.addAll(findProductIdByPageId(center.getFourDisplayId()));
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error("根据Banner ID查找商品ID出错", e);
        }
        return new ArrayList<Integer>(productSet);
    }
    
    /**
     * 查找所有展现、可用个人中心自定义活动
     * @return
     */
    public static List<Integer> findAllDisplayAndAvailableCustomCenter()
    {
        Set<Integer> productSet = new HashSet<Integer>();
        try
        {
            if (customCenterDao == null)
            {
                customCenterDao = SpringBeanUtil.getBean(CustomCenterDao.class);
            }
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("isDisplay", CommonConstant.COMMON_YES);
            List<CustomCenterEntity> list = customCenterDao.findAllCustomCenter(para);
            for (CustomCenterEntity center : list)
            {
                productSet.addAll(findProductIdByCustomCenterId(center.getId()));
            }
        }
        catch (Exception e)
        {
            logger.error("根据Banner ID查找商品ID出错", e);
        }
        return new ArrayList<Integer>(productSet);
    }
    
    /**
     * 根据格格推荐Id查找关联商品Id
     * @param id
     * @return
     */
    public static List<Integer> findProductIdByCustomGegeRecommendId(int id)
    {
        Set<Integer> productSet = new HashSet<Integer>();
        try
        {
            if (customGGRecommendDao == null)
            {
                customGGRecommendDao = SpringBeanUtil.getBean(CustomGGRecommendDao.class);
            }
            CustomGGRecommendEntity recommend = customGGRecommendDao.findCustomGGRecommendById(id);
            if (recommend != null && recommend.getIsDisplay() == CommonConstant.COMMON_YES)
            {
                DateTime startTime = new DateTime(DateTimeUtil.string2Date(recommend.getStartTime()).getTime());
                DateTime endTime = new DateTime(DateTimeUtil.string2Date(recommend.getEndTime()).getTime());
                if (startTime.isBeforeNow() && endTime.isAfterNow())
                {
                    if (recommend.getOneType() == 2)
                    {
                        //通用专场
                        productSet.addAll(findProductIdByActivitiesCommonId(recommend.getOneDisplayId()));
                    }
                    else if (recommend.getOneType() == 3)
                    {
                        //自定义活动
                        productSet.addAll(findProductIdByCustomActivityId(recommend.getOneDisplayId()));
                    }
                    else if (recommend.getOneType() == 7)
                    {
                        //原生页面
                        productSet.addAll(findProductIdByPageId(recommend.getOneDisplayId()));
                    }
                    
                    if (recommend.getDisplayType() == 2)
                    {
                        if (recommend.getTwoType() == 2)
                        {
                            //通用专场
                            productSet.addAll(findProductIdByActivitiesCommonId(recommend.getTwoDisplayId()));
                        }
                        else if (recommend.getTwoType() == 3)
                        {
                            //自定义活动
                            productSet.addAll(findProductIdByCustomActivityId(recommend.getTwoDisplayId()));
                        }
                        else if (recommend.getTwoType() == 7)
                        {
                            //原生页面
                            productSet.addAll(findProductIdByPageId(recommend.getTwoDisplayId()));
                        }
                    }
                }
            }
            
        }
        catch (Exception e)
        {
            logger.error("根据格格推荐ID查找商品ID出错", e);
        }
        return new ArrayList<Integer>(productSet);
    }
    
    /**
     * 查找所有展现、可用格格推荐
     * @return
     */
    public static List<Integer> findAllDisplayAndAvailableCustomGegeRecommend()
    {
        Set<Integer> productSet = new HashSet<Integer>();
        try
        {
            if (customGGRecommendDao == null)
            {
                customGGRecommendDao = SpringBeanUtil.getBean(CustomGGRecommendDao.class);
            }
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("isDisplay", CommonConstant.COMMON_YES);
            param.put("status", 1);
            List<Map<String, Object>> list = customGGRecommendDao.findRecommendListInfo(param);
            for (Map<String, Object> it : list)
            {
                int id = Integer.parseInt(it.get("id") + "");
                productSet.addAll(findProductIdByCustomGegeRecommendId(id));
            }
        }
        catch (Exception e)
        {
            logger.error("查找所有可用格格推荐关联商品ID出错", e);
        }
        return new ArrayList<Integer>(productSet);
    }
    
    /**
     * 根据特卖Id查找关联商品Id
     * @param id：特卖位Id
     * @return
     */
    public static List<Integer> findProductIdBySaleWindowId(int id)
    {
        Set<Integer> productSet = new HashSet<Integer>();
        try
        {
            if (saleWindowDao == null)
            {
                saleWindowDao = SpringBeanUtil.getBean(SaleWindowDao.class);
            }
            if (productDao == null)
            {
                productDao = SpringBeanUtil.getBean(ProductDao.class);
            }
            SaleWindowEntity saleWindow = saleWindowDao.findSaleWindowById(id);
            if (saleWindow != null && saleWindow.getIsDisplay() == CommonConstant.COMMON_YES)
            {
                DateTime startTime = new DateTime(DateTimeUtil.string2Date(saleWindow.getStartTime() + "").getTime());
                DateTime endTime = new DateTime(DateTimeUtil.string2Date((saleWindow.getEndTime() + 1) + "").getTime());
                
                if (saleWindow.getSaleTimeType() == SaleWindowEnum.SALE_TIME_TYPE.SALE_10.getCode())
                {
                    //10点场
                    startTime.withHourOfDay(10).withMinuteOfHour(0).withSecondOfMinute(0);
                    endTime.withHourOfDay(10).withMinuteOfHour(0).withSecondOfMinute(0);
                }
                else if (saleWindow.getSaleTimeType() == SaleWindowEnum.SALE_TIME_TYPE.SALE_20.getCode())
                {
                    //20点场
                    startTime.withHourOfDay(20).withMinuteOfHour(0).withSecondOfMinute(0);
                    endTime.withHourOfDay(20).withMinuteOfHour(0).withSecondOfMinute(0);
                }
                
                if (startTime.isBeforeNow() && endTime.isAfterNow())
                {
                    if (saleWindow.getType() == 1)
                    {
                        Integer productId = findProductId(saleWindow.getDisplayId());
                        if (productId != null)
                        {
                            productSet.add(productId);
                        }
                    }
                    else if (saleWindow.getType() == 2)
                    {
                        productSet.addAll(findProductIdByActivitiesCommonId(saleWindow.getDisplayId()));
                    }
                    else if (saleWindow.getType() == 3)
                    {
                        productSet.addAll(findProductIdByCustomActivityId(saleWindow.getDisplayId()));
                    }
                    else if (saleWindow.getType() == 4)
                    {
                        productSet.addAll(findProductIdByPageId(saleWindow.getDisplayId()));
                    }
                }
                
            }
        }
        catch (Exception e)
        {
            logger.error("根据格格推荐ID查找商品ID出错", e);
        }
        return new ArrayList<Integer>(productSet);
    }
    
    /**
     * 查找所有在售、可用特卖关联的商品
     * @return
     */
    public static List<Integer> findAllDisplayAndAvailableSaleWindow()
    {
        Set<Integer> productSet = new HashSet<Integer>();
        try
        {
            if (saleWindowDao == null)
            {
                saleWindowDao = SpringBeanUtil.getBean(SaleWindowDao.class);
            }
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("isDisplay", CommonConstant.COMMON_YES);
            para.put("status", 2);
            List<SaleWindowEntity> list = saleWindowDao.findAllSaleWindow(para);
            for (SaleWindowEntity sale : list)
            {
                productSet.addAll(findProductIdBySaleWindowId(sale.getId()));
            }
        }
        catch (Exception e)
        {
            logger.error("查找所有在售、可用特卖关联商品ID出错", e);
        }
        return new ArrayList<Integer>(productSet);
    }
    
    /**
     * 根据Id查找自定义导航关联的商品Id
     * @return
     */
    public static List<Integer> findProductIdByCustomNavigationId(int id)
    {
        Set<Integer> productSet = new HashSet<Integer>();
        try
        {
            if (customNavigationDao == null)
            {
                customNavigationDao = SpringBeanUtil.getBean(CustomNavigationDao.class);
            }
            Map<String, Object> map = customNavigationDao.findNavigationById(id);
            if (map != null)
            {
                int isDisplay = Integer.parseInt(map.get("isDisplay") + "");
                int type = Integer.parseInt(map.get("customNavigationType") + "");
                int displayId = Integer.parseInt(map.get("displayId") + "");
                if (isDisplay == CommonConstant.COMMON_YES)
                {
                    if (type == 1)
                    {
                        productSet.addAll(findProductIdByActivitiesCommonId(displayId));
                    }
                    else if (type == 2)
                    {
                        productSet.addAll(findProductIdByCustomActivityId(displayId));
                    }
                    else if (type == 3)
                    {
                        productSet.addAll(findProductIdByPageId(displayId));
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error("根据Id查找自定义导航关联的商品Id出错", e);
        }
        return new ArrayList<Integer>(productSet);
    }
    
    /**
     * 查找所有展现、可用自定义导航关联的商品Id
     * @return
     */
    public static List<Integer> findAllDisplayAndAvailableCustomNavigation()
    {
        Set<Integer> productSet = new HashSet<Integer>();
        try
        {
            if (customNavigationDao == null)
            {
                customNavigationDao = SpringBeanUtil.getBean(CustomNavigationDao.class);
            }
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("isDisplay", CommonConstant.COMMON_YES);
            List<Map<String, Object>> list = customNavigationDao.findAllCustomNavigationByPara(para);
            for (Map<String, Object> it : list)
            {
                int id = Integer.parseInt(it.get("id") + "");
                productSet.addAll(findProductIdByCustomNavigationId(id));
            }
        }
        catch (Exception e)
        {
            logger.error("根据Id查找自定义导航关联的商品Id出错", e);
        }
        return new ArrayList<Integer>(productSet);
    }
    
    /**
     * 根据首页自定义功能Id找关联商品Id
     * @return
     */
    public static List<Integer> findProductIdByCustomFunctionId(int id)
    {
        Set<Integer> productSet = new HashSet<Integer>();
        try
        {
            if (customFunctionDao == null)
            {
                customFunctionDao = SpringBeanUtil.getBean(CustomFunctionDao.class);
            }
            if (indexSettingDao == null)
            {
                indexSettingDao = SpringBeanUtil.getBean(IndexSettingDao.class);
            }
            CustomFunctionEntity function = customFunctionDao.findCustomFunctionById(id);
            if (function.getIsDisplay() == CommonConstant.COMMON_YES)
            {
                if (function.getOneType() == 3 || function.getTwoType() == 3 || function.getThreeType() == 3)
                {
                    Map<String, Object> para = new HashMap<String, Object>();
                    para.put("key", "left_page_id");
                    List<Map<String, Object>> result1 = indexSettingDao.findSetting(para);
                    if (result1 != null && result1.size() > 0)
                    {
                        int pageId = Integer.parseInt(result1.get(0).get("value") + "");
                        productSet.addAll(findProductIdByPageId(pageId));
                    }
                    
                    para.put("key", "right_page_id");
                    List<Map<String, Object>> result2 = indexSettingDao.findSetting(para);
                    if (result2 != null && result2.size() > 0)
                    {
                        int pageId = Integer.parseInt(result2.get(0).get("value") + "");
                        productSet.addAll(findProductIdByPageId(pageId));
                    }
                }
            }
            
        }
        catch (Exception e)
        {
            logger.error("根据首页自定义功能Id找关联商品Id出错", e);
        }
        return new ArrayList<Integer>(productSet);
    }
    
    /**
     * 查找所有展现、可用自定义功能
     * @return
     */
    public static List<Integer> findAllDisplayAndAvailableCustomFunction()
    {
        Set<Integer> productSet = new HashSet<Integer>();
        try
        {
            if (customFunctionDao == null)
            {
                customFunctionDao = SpringBeanUtil.getBean(CustomFunctionDao.class);
            }
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("isDisplay", CommonConstant.COMMON_YES);
            List<CustomFunctionEntity> list = customFunctionDao.findAllCustomFunction(para);
            for (CustomFunctionEntity function : list)
            {
                productSet.addAll(findProductIdByCustomFunctionId(function.getId()));
            }
        }
        catch (Exception e)
        {
            logger.error("查找所有展现、可用自定义功能关联商品Id出错", e);
        }
        return new ArrayList<Integer>(productSet);
    }
    
    /**
     * 根据商城品牌原生自定义页面查找关联商品Id
     * @return
     */
    public static List<Integer> findProductIdByBrandPageId()
    {
        Set<Integer> productSet = new HashSet<Integer>();
        try
        {
            if (indexSettingDao == null)
            {
                indexSettingDao = SpringBeanUtil.getBean(IndexSettingDao.class);
            }
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("key", "brand_page_id");
            List<Map<String, Object>> result = indexSettingDao.findSetting(para);
            if (result != null && result.size() > 0)
            {
                int pageId = Integer.parseInt(result.get(0).get("value") + "");
                productSet.addAll(findProductIdByPageId(pageId));
            }
        }
        catch (Exception e)
        {
            logger.error("根据商城品牌原生自定义页面查找关联商品Id出错", e);
        }
        return new ArrayList<Integer>(productSet);
    }
    
    /**
     * 查找所有展现的签到商品Id
     * @return
     */
    public static List<Integer> findAllSigninProduct()
    {
        Set<Integer> productSet = new HashSet<Integer>();
        try
        {
            if (signInDao == null)
            {
                signInDao = SpringBeanUtil.getBean(SignInDao.class);
            }
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("isDisplay", CommonConstant.COMMON_YES);
            List<Map<String, Object>> list = signInDao.findAllSigninProduct(para);
            for (Map<String, Object> it : list)
            {
                int productId = Integer.parseInt(it.get("productId") + "");
                int type = Integer.parseInt(it.get("type") + "");
                int isOffShelves = Integer.parseInt(it.get("isOffShelves") + "");
                int isAvailable = Integer.parseInt(it.get("isAvailable") + "");
                String startTime = it.get("startTime") == null ? "" : it.get("startTime").toString();
                String endTime = it.get("endTime") == null ? "" : it.get("endTime").toString();
                if (isOffShelves == CommonConstant.COMMON_NO && isAvailable == CommonConstant.COMMON_YES)
                {
                    if (type == ProductEnum.PRODUCT_TYPE.MALL.getCode())
                    {
                        productSet.add(productId);
                    }
                    else if (type == ProductEnum.PRODUCT_TYPE.SALE.getCode())
                    {
                        DateTime start = new DateTime(DateTimeUtil.string2Date(startTime).getTime());
                        DateTime end = new DateTime(DateTimeUtil.string2Date(endTime).getTime());
                        if (start.isBeforeNow() && end.isAfterNow())
                        {
                            productSet.add(productId);
                        }
                    }
                }
                else
                {
                    continue;
                }
            }
        }
        catch (Exception e)
        {
            logger.error("查找签到商品ID出错", e);
        }
        return new ArrayList<Integer>(productSet);
        
    }
    
    private static Integer findProductId(int id)
    {
        Integer productId = null;
        try
        {
            if (productDao == null)
            {
                productDao = SpringBeanUtil.getBean(ProductDao.class);
            }
            ProductEntity pe = productDao.findProductByID(id, null);
            if (pe != null && pe.getIsAvailable() == CommonConstant.COMMON_YES && pe.getIsOffShelves() == CommonConstant.COMMON_NO)
            {
                if (pe.getType() == ProductEnum.PRODUCT_TYPE.MALL.getCode())
                {
                    return pe.getId();
                }
                else if (pe.getType() == ProductEnum.PRODUCT_TYPE.SALE.getCode())
                {
                    DateTime startTime = new DateTime(DateTimeUtil.string2Date(pe.getStartTime()).getTime());
                    DateTime endTime = new DateTime(DateTimeUtil.string2Date(pe.getEndTime()).getTime());
                    if (startTime.isBeforeNow() && endTime.isAfterNow())
                    {
                        return pe.getId();
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error("查找商品Id出错", e);
        }
        return productId;
        
    }
    
    /**
     * 查找所有关联展现、可用商品Id
     * @return
     */
    public static List<Integer> findAllDisplayAndAvailableProductId()
    {
        Set<Integer> productSet = new HashSet<Integer>();
        try
        {
            productSet.addAll(findAllDisplayAndAvailableCustomRegion());//所有展现自定义板块关联商品Id
            //productSet.addAll(findAllDisplayAndAvailableActivitiesCommon());
            //productSet.addAll(findAllDisplayAndAvailablePage());
            //productSet.addAll(findAllDisplayAndAvailableCustomActivity());
            //productSet.addAll(findAllDisplayAndAvailableSpecialActivity());
            //productSet.addAll(findAllDisplayAndAvailableSimplifyActivity());
            //productSet.addAll(findAllDisplayAndAvailableSpecialActivityNew());
            productSet.addAll(findAllDisplayAndAvailableBannerWindow());//所有展现Banner关联商品Id
            productSet.addAll(findAllDisplayAndAvailableCustomCenter());//所有展现个人中心关联商品Id
            productSet.addAll(findAllDisplayAndAvailableCustomGegeRecommend());//所有展现格格推荐关联商品Id
            //productSet.addAll(findAllDisplayAndAvailableSaleWindow());
            productSet.addAll(findAllDisplayAndAvailableCustomNavigation());//所有展现自定义导航关联商品Id
            productSet.addAll(findAllDisplayAndAvailableCustomFunction());//所有展现自定义功能关联商品Id
            productSet.addAll(findProductIdByBrandPageId());//商城品牌原生自定义页面关联商品Id
            //productSet.addAll(findAllSigninProduct());//签到商品Id
        }
        catch (Exception e)
        {
            logger.error("查找所有关联展现、可用商品Id出错", e);
        }
        return new ArrayList<Integer>(productSet);
    }
    
    public static String getViewURL(int productId)
        throws Exception
    {
        if (productDao == null)
        {
            productDao = SpringBeanUtil.getBean(ProductDao.class);
        }
        
        ProductEntity pe = productDao.findProductByID(productId, null);
        StringBuffer url = new StringBuffer("<a target='_blank' href='http://m.gegejia.com/");
        if (pe.getType() == ProductEnum.PRODUCT_TYPE.SALE.getCode())
        {
            url.append("item-");
        }
        else if (pe.getType() == ProductEnum.PRODUCT_TYPE.MALL.getCode())
        {
            url.append("mitem-");
        }
        url.append(pe.getId()).append(".htm'>").append(pe.getName()).append("</a>");
        return url.toString();
    }
}
