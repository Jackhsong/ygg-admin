package com.ygg.admin.code;

public class CustomEnum
{
    /**
     * 自定义活动关联类型
     *
     */
    public enum CUSTOM_ACTIVITY_RELATION
    {
        /** 1：抽奖活动 */
        LOTTERY_ACTIVITY(1, "抽奖活动"),
        
        /** 2：情景特卖活动 */
        SALE_ACTIVITY(2, "情景特卖活动"),
        
        /** 3：礼包活动 */
        GIFT_PACKAGE_ACTIVITY(3, "礼包活动"),
        
        /** 4：其他活动 */
        OTHER_ACTIVITY(4, "其他活动"),
        
        /** 5：任意门*/
        ANY_DOOR(5, "任意门"),
        
        /** 6：精品特卖活动*/
        SIMPLIFY_ACTIVITY(6, "精品特卖活动"),
        
        /** 7：疯狂抽美食活动*/
        CRAZY_FOOD(7, "疯狂抽美食活动"),
        
        /** 8：疯狂派红包活动*/
        RED_PACKETS(8, "疯狂派红包活动"),
        
        /** 9：迎新送券活动 */
        WELCOME_NEW_USER(9, "迎新送券活动"),
        
        /** 10：美食幸运签活动，*/
        DELICIOUS_FOOD(10, "美食幸运签活动"),
        
        /** 11：双11邀请好友红包*/
        INVITE_FRIEND(11, "双11邀请好友红包"),
        
        /** 12：新情景特卖 */
        NEW_SIMPLIFY_ACTIVITY(12, "新情景特卖"),
        
        SPECIAL_GROUP_ACTIVITY(20, "组合搭配情景活动"),
        
        SPECIAL_ACTIVITY_MODEL(22, "情景模版");
        
        private int code;
        
        private String desc;
        
        private CUSTOM_ACTIVITY_RELATION(int code, String desc)
        {
            this.code = code;
            this.desc = desc;
        }
        
        public int getCode()
        {
            return code;
        }
        
        public String getDesc()
        {
            return desc;
        }
        
        public static String getDescrByCode(int code)
        {
            for (CUSTOM_ACTIVITY_RELATION e : CUSTOM_ACTIVITY_RELATION.values())
            {
                if (code == e.code)
                {
                    return e.desc;
                }
            }
            return "";
        }
    }
}
