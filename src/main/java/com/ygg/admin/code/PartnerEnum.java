package com.ygg.admin.code;

/**
 * 合伙人相关枚举
 * @author Administrator
 *
 */
public class PartnerEnum
{
    /**
     * 合伙人工作类型枚举
     *
     */
    public enum JOB_TYPE
    {
        STUDENT(1, "学生"),
        
        WORKED(2, "已工作"),
        
        WAITING(3, "待业在家"),
        
        RETIRED(4, "已退休");
        
        final int code;
        
        final String desc;
        
        JOB_TYPE(int code, String desc)
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
        
        public static String getDescByCode(int code)
        {
            for (JOB_TYPE e : JOB_TYPE.values())
            {
                if (code == e.code)
                {
                    return e.desc;
                }
            }
            return "";
        }
        
        public static JOB_TYPE getEnumByCode(int code)
        {
            for (JOB_TYPE e : JOB_TYPE.values())
            {
                if (e.ordinal() == code)
                    return e;
            }
            return null;
        }
    }
    
    /**
     * 合伙人申请状态枚举
     *
     */
    public enum APPLY_STATUS
    {
        /**
         * 申请合伙人状态；1:未申请，2：申请中，3：申请未通过，4：申请通过
         */
        
        NOT_APPLY(1, "未申请"),
        
        APPLYING(2, "申请中"),
        
        NOT_PASSED(3, "申请未通过"),
        
        PASSED(4, "申请通过");
        
        final int code;
        
        final String desc;
        
        APPLY_STATUS(int code, String desc)
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
        
        public static String getDescByCode(int code)
        {
            for (APPLY_STATUS e : APPLY_STATUS.values())
            {
                if (e.code == code)
                {
                    return e.desc;
                }
            }
            return "";
        }
        
        public static APPLY_STATUS getEnumByCode(int code)
        {
            for (APPLY_STATUS e : APPLY_STATUS.values())
            {
                if (e.code == code)
                    return e;
            }
            return null;
        }
    }
    
    /**
     * 合伙人审核状态
     *
     */
    public enum AUDIT_STATUS
    {
        
        /**2*/
        WAIT_AUDIT(2, "待审核"),
        
        /**3*/
        NOT_PASSED(3, "未通过"),
        
        /**4*/
        PASSED(4, "已通过");
        
        final int code;
        
        final String desc;
        
        AUDIT_STATUS(int code, String desc)
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
        
        public static String getDescByCode(int code)
        {
            for (AUDIT_STATUS e : AUDIT_STATUS.values())
            {
                if (code == e.code)
                {
                    return e.desc;
                }
            }
            return "";
        }
        
        public static AUDIT_STATUS getEnumByCode(int code)
        {
            for (AUDIT_STATUS e : AUDIT_STATUS.values())
            {
                if (e.code == code)
                    return e;
            }
            return null;
        }
    }
    
    /**
     * 合伙人月收入枚举
     *
     */
    public enum MONTHLY_INCOME
    {
        
        LOWER_2000(1, "2000元以下"),
        
        IN_2001_5000(2, "2001-5000元"),
        
        IN_5001_8000(3, "5001-8000元"),
        
        IN_8001_10000(4, "8001-10000元"),
        
        IN_10001_15000(5, "10001-15000元"),
        
        OVER_15000(6, "15000元以上");
        
        final int code;
        
        final String desc;
        
        MONTHLY_INCOME(int code, String desc)
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
        
        public static String getDescByCode(int code)
        {
            for (MONTHLY_INCOME e : MONTHLY_INCOME.values())
            {
                if (code == e.code)
                {
                    return e.desc;
                }
            }
            return "";
        }
        
        public static MONTHLY_INCOME getEnumByCode(int code)
        {
            for (MONTHLY_INCOME e : MONTHLY_INCOME.values())
            {
                if (e.code == code)
                    return e;
            }
            return null;
        }
    }
    
    /**
     * 合伙人状态枚举
     *
     */
    public enum PARTNER_STATUS
    {
        
        NO(1, "不是合伙人"),
        
        YES(2, "是合伙人"),
        
        LOCKED(3, "合伙人被禁用");
        
        final int code;
        
        final String desc;
        
        PARTNER_STATUS(int code, String desc)
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
        
        public static String getDescByCode(int code)
        {
            for (PARTNER_STATUS e : PARTNER_STATUS.values())
            {
                if (code == e.code)
                {
                    return e.desc;
                }
            }
            return "";
        }
        
        public static PARTNER_STATUS getEnumByCode(int code)
        {
            for (PARTNER_STATUS e : PARTNER_STATUS.values())
            {
                if (e.code == code)
                    return e;
            }
            return null;
        }
    }
}
