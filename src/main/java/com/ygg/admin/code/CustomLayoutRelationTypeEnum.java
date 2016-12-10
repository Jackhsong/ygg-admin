package com.ygg.admin.code;

/**
 * 自定义布局关联类型枚举：
 * 1：特卖商品，2：通用专场，3：自定义专场，4：商城商品, 5：签到，6：邀请小伙伴
 * @author xiongl
 *
 */
public enum CustomLayoutRelationTypeEnum
{
    /** 0 占位符 */
    NORMAL("占位符"),
    
    /** 1：特卖商品 */
    LAYOUT_RELATION_OF_SALEPRODUCT("特卖商品"),
    
    /** 2：通用专场 */
    LAYOUT_RELATION_OF_COMMONACTIVITY("通用专场"),
    
    /** 3：自定义专场 */
    LAYOUT_RELATION_OF_CUSTOMSALE("自定义活动"),
    
    /** 4：商城商品*/
    LAYOUT_RELATION_OF_MALLPRODUCT("商城商品"),
    
    /** 5：签到 */
    LAYOUT_RELATION_OF_SIGNIN("签到"),
    
    /**6：邀请小伙伴*/
    LAYOUT_RELATION_OF_INVITEFRIEND("邀请小伙伴"),
    
    /** 7：原生自定义页面 */
    APP_CUSTOM_PAGE("原生自定义页面"),
    
    /** 8：图片点击不跳转 */
    PICTURE("图片点击不跳转"),
    
    /** 9：积分商城 */
    INTEGRAL_MALL("积分商城");
    
    private String description;
    
    private CustomLayoutRelationTypeEnum(String description)
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
    
    public static String getDescriptionByOrdinal(int ordinal)
    {
        for (CustomLayoutRelationTypeEnum e : CustomLayoutRelationTypeEnum.values())
        {
            if (ordinal == e.ordinal())
            {
                return e.description;
            }
        }
        return "";
    }
}
