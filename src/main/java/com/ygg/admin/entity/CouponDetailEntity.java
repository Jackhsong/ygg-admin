package com.ygg.admin.entity;

public class CouponDetailEntity
{
    /** 优惠详情id */
    private int id;
    
    /** 优惠类型；1：满xx减x，2：满0减x */
    private int type;
    
    /** 使用范围类型；1：全场商品，2：通用专场商品，3：指定商品，4：二级类目商品，5：卖家商品，6：卖家发货类型商品 */
    private int scopeType;
    
    /** 使用范围id；scope_type=2值为专场id，scope_type=3值为指定商品id，scope_type=4值为二级类目id，scope_type=5值为卖家id，scope_type=6值为卖家发货类型值 */
    private int scopeId;
    
    /** 满xx金额 */
    private int threshold;
    
    /** 减x金额 */
    private int reduce;
    
    /** 是否使用随机优惠金额 */
    private int isRandomReduce;
    
    /** 随机优惠金额最低值 */
    private int lowestReduce;
    
    /** 随机优惠金额最高值 */
    private int maximalReduce;
    
    /** 创建时间 */
    private String createTime;
    
    /** 是否可用 */
    private int isAvailable;
    
    /** 前台用户展示是否可用 */
    private String desc;
    
    public int getIsRandomReduce()
    {
        return isRandomReduce;
    }
    
    public void setIsRandomReduce(int isRandomReduce)
    {
        this.isRandomReduce = isRandomReduce;
    }
    
    public int getLowestReduce()
    {
        return lowestReduce;
    }
    
    public void setLowestReduce(int lowestReduce)
    {
        this.lowestReduce = lowestReduce;
    }
    
    public int getMaximalReduce()
    {
        return maximalReduce;
    }
    
    public void setMaximalReduce(int maximalReduce)
    {
        this.maximalReduce = maximalReduce;
    }
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getType()
    {
        return type;
    }
    
    public void setType(int type)
    {
        this.type = type;
    }
    
    public int getScopeType()
    {
        return scopeType;
    }
    
    public void setScopeType(int scopeType)
    {
        this.scopeType = scopeType;
    }
    
    public int getScopeId()
    {
        return scopeId;
    }
    
    public void setScopeId(int scopeId)
    {
        this.scopeId = scopeId;
    }
    
    public int getThreshold()
    {
        return threshold;
    }
    
    public void setThreshold(int threshold)
    {
        this.threshold = threshold;
    }
    
    public int getReduce()
    {
        return reduce;
    }
    
    public void setReduce(int reduce)
    {
        this.reduce = reduce;
    }
    
    public String getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }
    
    public int getIsAvailable()
    {
        return isAvailable;
    }
    
    public void setIsAvailable(int isAvailable)
    {
        this.isAvailable = isAvailable;
    }
    
    public String getDesc()
    {
        return desc;
    }
    
    public void setDesc(String desc)
    {
        this.desc = desc;
    }
    
    @Override
    public String toString()
    {
        
        return "[满" + threshold + "减" + reduce + "]";
    }
}
