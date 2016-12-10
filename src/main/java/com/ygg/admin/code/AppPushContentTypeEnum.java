package com.ygg.admin.code;

/**
 * 推送类型
 * 
 * @author zhangyb
 *
 */
public enum AppPushContentTypeEnum
{
    /** 0 占位符 */
    NORMAL("占位符"),
    
    /** 1 收藏特卖列表页面  格式：1&title&content */
    SPECIAL_SALE_COLLECT("特卖收藏"),
    
    /** 2 单品收藏列表页面 格式： 2&title&content */
    ITEM_COLLECT("单品收藏"),
    
    /** 3 预留促销活动 暂到app首页 格式： 3&title&content  */
    PROMOTION("预留促销活动"),
    
    /** 4 自定义页面 格式： 4&title&content&url */
    CUSTOM_PAGE("自定义页面"),
    
    /** 5 购物车页面 格式：5&title&content */
    CART("购物车"),
    
    /** 6 订单详情页 格式：5&title&content&orderId */
    ORDER_DETAIL("订单详情页"),
    
    /** 7 */
    UNDEFINED("暂时未定义"),
    
    /** 8 特卖商品促销*/
    SALE_PRODUCT_SELLING("特卖商品促销"),
    
    /** 9 商城商品促销*/
    MALL_PRODUCT_SELLING("商城商品促销"),
    
    /** 10 特卖专场促销*/
    SALE_WINDOW_SELLING("特卖专场促销"),
    
    /** 11 商城专场促销*/
    MALL_WINDOW_SELLING("商城专场促销");
    
    private String description;
    
    private AppPushContentTypeEnum(String description)
    {
        this.description = description;
    }
    
    public String getDescription()
    {
        return description;
    }
    
    public void setDescription(String description)
    {
        this.description = description;
    }
    
}
