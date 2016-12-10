package com.ygg.admin.code;

/**
 * 推送类型
 * 
 * @author zhangyb
 *
 */
public enum AppPushInfoTypeEnum
{
    /** 首页 */
    INDEX("首页", 3),
    
    /** 特卖收藏 */
    SPECIAL_SALE_COLLECT("特卖收藏", 1),
    
    /** 单品收藏 */
    ITEM_COLLECT("单品收藏", 2),
    
    /** 购物车 */
    CART("购物车", 5),
    
    /** 订单详情页 */
    ORDER_DETAIL("订单详情页", 6),
    
    /** 自定义页面 */
    CUSTOM_PAGE("自定义页面", 4),
    
    /** 特卖商品促销*/
    SALE_PRODUCT_SELLING("特卖商品促销", 8),
    
    /** 商城商品促销*/
    MALL_PRODUCT_SELLING("商城商品促销", 9),
    
    /** 特卖专场促销*/
    SALE_WINDOW_SELLING("特卖专场促销", 10),
    
    /** 商城专场促销*/
    MALL_WINDOW_SELLING("商城专场促销", 11);
    
    private String description;
    
    //通知app的消息内容类型
    private int appType;
    
    private AppPushInfoTypeEnum(String description, int appType)
    {
        this.description = description;
        this.appType = appType;
    }
    
    public String getDescription()
    {
        return description;
    }
    
    public void setDescription(String description)
    {
        this.description = description;
    }
    
    public int getAppType()
    {
        return appType;
    }
    
    public void setAppType(int appType)
    {
        this.appType = appType;
    }
    
    public static AppPushInfoTypeEnum getEnumByOrdinal(int ordinal)
    {
        for (AppPushInfoTypeEnum e : AppPushInfoTypeEnum.values())
        {
            if (e.ordinal() == ordinal)
            {
                return e;
            }
        }
        return null;
    }
}
