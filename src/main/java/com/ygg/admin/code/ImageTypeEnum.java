package com.ygg.admin.code;

/**
 * 图片尺寸类型
 * 
 * @author zhangyb
 *
 */
public enum ImageTypeEnum
{
    /** 首页banner */
    v1banner("首页banner", "v1banner", "v1banner"),
    
    /** 首页特卖 */
    v1sell("首页特卖", "v1sell", "v1sell"),
    
    /** 品牌团banner */
    v1brandBanner("品牌团banner", "v1brandBanner", "v1brandbanner"),
    
    /** 详情页主图 */
    v1product("详情页主图", "v1product", "v1product"),
    
    /** 详情页详情图 */
    v1detail("详情页详情图", "v1detail", "v1detail"),
    
    /** 品牌团商品图 */
    v1brandProduct("品牌团商品图", "v1brandProduct", "v1brandproduct"),
    
    /** 购物车商品图  v1cartProduct(old) */
    v1cartProduct("购物车商品图", "newcartproduct", "newcartproduct"),
    
    /** 提交订单商品图/订单详情商品图/订单列表商品图 */
    v1orderProduct("提交订单商品图/订单详情商品图/订单列表商品图", "v1orderProduct", "v1orderproduct"),
    
    /** 首页特卖 - new */
    newsell("首页特卖-new", "newsell", "newsell"),
    
    /** 今日最热 */
    todayhot("今日最热-new", "todayhot", "todayhot"),
    
    /** 主题馆入口图标 */
    mallicon("主题馆入口图标", "mallicon", "mallicon"),
    
    /** 特卖标题左侧国旗 */
    nationalflag("特卖标题左侧国旗", "nationalflag", "nationalflag"),
    
    /**特卖活动单品左右布局图*/
    newlistproduct("特卖活动单品左右布局图", "newlistproduct", "newlistproduct"),
    
    /** 首页特卖 - 1.7new */
    v17newsell("首页特卖-new1.7", "17newsell", "17newsell"),
    
    /** 商品组合-new1.7*/
    v1brandbanner("商品组合-new1.7", "v1brandbanner", "v1brandbanner");
    
    /** 描述 */
    private String description;
    
    /** 又拍云后缀 */
    private String youpaiSuffix;
    
    /** 阿里云后缀 */
    private String aliSuffix;
    
    private ImageTypeEnum(String description, String youpaiSuffix, String aliSuffix)
    {
        this.description = description;
        this.youpaiSuffix = youpaiSuffix;
        this.aliSuffix = aliSuffix;
    }
    
    public String getDescription()
    {
        return description;
    }
    
    public void setDescription(String description)
    {
        this.description = description;
    }
    
    public String getYoupaiSuffix()
    {
        return youpaiSuffix;
    }
    
    public void setYoupaiSuffix(String youpaiSuffix)
    {
        this.youpaiSuffix = youpaiSuffix;
    }
    
    public String getAliSuffix()
    {
        return aliSuffix;
    }
    
    public void setAliSuffix(String aliSuffix)
    {
        this.aliSuffix = aliSuffix;
    }
    
}
