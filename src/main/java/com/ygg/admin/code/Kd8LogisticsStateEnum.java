package com.ygg.admin.code;

/**
 * 快递吧物流状态
 * @author Administrator
 *
 */
public enum Kd8LogisticsStateEnum
{
    /**0 是正常的在途*/
    NOT_RECEIVED("未签收", 0),
    
    /**1 正常签收的*/
    RECEIVED("已签收", 1),
    
    /**2 正常签收的*/
    DELEGATE_RECEIVED("代签收", 2),
    
    /**-1 没有跟踪记录,快递吧一般不会回调*/
    NO_RECORD("无记录", -1),
    
    /**-3 没有跟踪记录,快递吧一般不会回调*/
    NO_RECORD_MORE_2DAYS("2天以上无记录(顺丰1天以上无记录)", -3),
    
    /**-4 也是在途状态*/
    NOT_RECEIVED_MORE_6DAYS("6天以上未签收(顺丰4天以上未签收)", -4),
    
    /**-5 疑难件*/
    IN_TROUBLE("疑难件", -5),
    
    /**-6 最后一条跟踪记录是3天之前，实际也是在途状态*/
    NO_NEWS_MORE_3DAYS("3天以上没有新的进展", -6),
    
    /**-18 派送中*/
    ON_SEND("派送中", -18);
    
    final String name;
    
    final int value;
    
    private Kd8LogisticsStateEnum(String name, int value)
    {
        this.name = name;
        this.value = value;
    }
    
    public int getValue()
    {
        return value;
    }
    
    public String getName()
    {
        return name;
    }
    
    public static String getName(int value)
    {
        for (Kd8LogisticsStateEnum e : Kd8LogisticsStateEnum.values())
        {
            if (e.value == value)
            {
                return e.name;
            }
        }
        return "";
    }
}
