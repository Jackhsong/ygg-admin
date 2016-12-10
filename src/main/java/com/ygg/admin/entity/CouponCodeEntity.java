package com.ygg.admin.entity;

import com.ygg.admin.code.CouponEnum;

/**
 * 优惠码实体
 * 
 * @author zhangyb
 *
 */
public class CouponCodeEntity
{
    private Integer id = null;
    
    /** 优惠券详情id  当changeType为1时使用该字段（为兼容） */
    private Integer couponDetailId = null;
    
    /** 优惠码类型；1：不同账号同一优惠码一次使用，2：不同账号同一优惠码无限次使用 */
    private Byte type = null;
    
    /** 优惠码兑换类型；1：兑换单张优惠券，2：兑换礼包； */
    private Byte changeType = null;
    
    /** 暂时没有用到， 相同账号该批次可用次数，0表示无限制 */
    private Integer sameMaxCount = null;
    
    /** 优惠码总数 */
    private Integer total = null;
    
    /** 一次兑换张数 */
    private Integer changeCount = null;
    
    /** 优惠码值，type等于2时，使用该字段 */
    private String code = null;
    
    /** 兑换开始时间 */
    private String startTime = null;
    
    /** 兑换结束时间 */
    private String endTime = null;
    
    /** 前台展示 使用范围 */
    private String remark = null;
    
    /** 优惠码备注 */
    private String desc = null;
    
    private String createTime = null;
    
    private String updateTime = null;
    
    /** 是否可用；0：否，1：是 */
    private Byte isAvailable = null;
    
    /** 使用范围类型；1：全场商品，2：通用专场商品，3：指定商品，4：二级类目商品，5：卖家商品，6：卖家发货类型商品 */
    private int scopeType;
    
    /** 使用范围id；scope_type=2值为专场id，scope_type=3值为指定商品id，scope_type=4值为二级类目id，scope_type=5值为卖家id，scope_type=6值为卖家发货类型值 */
    private int scopeId;
    
    /** 优惠类型；1：满xx减x，2：满0减x */
    private int cdType;
    
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
    
    /** 前台用户展示是否可用 */
    private String cdDesc;
    
    // *************** 页面展示所用 ***********************************
    
    private String isAvailableStr = "";
    
    private String typeStr = "";
    
    /** 总兑换人数 */
    private String convertNums = "0";
    
    /** 总使用人数 */
    private String usedNums = "0";
    
    private String couponDetailTypeStr = "";
    
    private String couponDetailDesc = "";
    
    public Byte getChangeType()
    {
        return changeType;
    }
    
    public void setChangeType(Byte changeType)
    {
        this.changeType = changeType;
    }
    
    public String getCouponDetailTypeStr()
    {
        return couponDetailTypeStr;
    }
    
    public void setCouponDetailTypeStr(String couponDetailTypeStr)
    {
        this.couponDetailTypeStr = couponDetailTypeStr;
    }
    
    public String getCouponDetailDesc()
    {
        return couponDetailDesc;
    }
    
    public void setCouponDetailDesc(String couponDetailDesc)
    {
        this.couponDetailDesc = couponDetailDesc;
    }
    
    public String getTypeStr()
    {
        if ("".equals(typeStr))
        {
            if (type == CouponEnum.CouponCodeType.ONE_CODE_ONE_USE.getCode())
            {
                typeStr = CouponEnum.CouponCodeType.ONE_CODE_ONE_USE.getShortDesc();
            }
            else if (type == CouponEnum.CouponCodeType.ONE_CODE_MANY_USE.getCode())
            {
                typeStr = CouponEnum.CouponCodeType.ONE_CODE_MANY_USE.getShortDesc();
            }
        }
        return typeStr;
    }
    
    public void setTypeStr(String typeStr)
    {
        this.typeStr = typeStr;
    }
    
    public String getConvertNums()
    {
        return convertNums;
    }
    
    public void setConvertNums(String convertNums)
    {
        this.convertNums = convertNums;
    }
    
    public String getUsedNums()
    {
        return usedNums;
    }
    
    public void setUsedNums(String usedNums)
    {
        this.usedNums = usedNums;
    }
    
    public Integer getId()
    {
        return id;
    }
    
    public void setId(Integer id)
    {
        this.id = id;
    }
    
    public Integer getCouponDetailId()
    {
        return couponDetailId;
    }
    
    public void setCouponDetailId(Integer couponDetailId)
    {
        this.couponDetailId = couponDetailId;
    }
    
    public Byte getType()
    {
        return type;
    }
    
    public void setType(Byte type)
    {
        this.type = type;
    }
    
    public Integer getSameMaxCount()
    {
        return sameMaxCount;
    }
    
    public void setSameMaxCount(Integer sameMaxCount)
    {
        this.sameMaxCount = sameMaxCount;
    }
    
    public Integer getTotal()
    {
        return total;
    }
    
    public void setTotal(Integer total)
    {
        this.total = total;
    }
    
    public String getCode()
    {
        return code;
    }
    
    public void setCode(String code)
    {
        this.code = code;
    }
    
    public String getStartTime()
    {
        return startTime;
    }
    
    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }
    
    public String getEndTime()
    {
        return endTime;
    }
    
    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }
    
    public String getRemark()
    {
        return remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public String getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }
    
    public String getUpdateTime()
    {
        return updateTime;
    }
    
    public void setUpdateTime(String updateTime)
    {
        this.updateTime = updateTime;
    }
    
    public Byte getIsAvailable()
    {
        return isAvailable;
    }
    
    public void setIsAvailable(Byte isAvailable)
    {
        this.isAvailable = isAvailable;
    }
    
    public String getIsAvailableStr()
    {
        return isAvailable == 1 ? "可用" : "停用";
    }
    
    public void setIsAvailableStr(String isAvailableStr)
    {
        this.isAvailableStr = isAvailableStr;
    }
    
    public String getDesc()
    {
        return desc;
    }
    
    public void setDesc(String desc)
    {
        this.desc = desc;
    }
    
    public Integer getChangeCount()
    {
        return changeCount;
    }
    
    public void setChangeCount(Integer changeCount)
    {
        this.changeCount = changeCount;
    }
    
    /**  
     *@return  the scopeId
     */
    public int getScopeId()
    {
        return scopeId;
    }
    
    /** 
     * @param scopeId the scopeId to set
     */
    public void setScopeId(int scopeId)
    {
        this.scopeId = scopeId;
    }
    
    /**  
     *@return  the cdType
     */
    public int getCdType()
    {
        return cdType;
    }
    
    /** 
     * @param cdType the cdType to set
     */
    public void setCdType(int cdType)
    {
        this.cdType = cdType;
    }
    
    /**  
     *@return  the threshold
     */
    public int getThreshold()
    {
        return threshold;
    }
    
    /** 
     * @param threshold the threshold to set
     */
    public void setThreshold(int threshold)
    {
        this.threshold = threshold;
    }
    
    /**  
     *@return  the reduce
     */
    public int getReduce()
    {
        return reduce;
    }
    
    /** 
     * @param reduce the reduce to set
     */
    public void setReduce(int reduce)
    {
        this.reduce = reduce;
    }
    
    /**  
     *@return  the isRandomReduce
     */
    public int getIsRandomReduce()
    {
        return isRandomReduce;
    }
    
    /** 
     * @param isRandomReduce the isRandomReduce to set
     */
    public void setIsRandomReduce(int isRandomReduce)
    {
        this.isRandomReduce = isRandomReduce;
    }
    
    /**  
     *@return  the lowestReduce
     */
    public int getLowestReduce()
    {
        return lowestReduce;
    }
    
    /** 
     * @param lowestReduce the lowestReduce to set
     */
    public void setLowestReduce(int lowestReduce)
    {
        this.lowestReduce = lowestReduce;
    }
    
    /**  
     *@return  the maximalReduce
     */
    public int getMaximalReduce()
    {
        return maximalReduce;
    }
    
    /** 
     * @param maximalReduce the maximalReduce to set
     */
    public void setMaximalReduce(int maximalReduce)
    {
        this.maximalReduce = maximalReduce;
    }
    
    /**  
     *@return  the cdDesc
     */
    public String getCdDesc()
    {
        return cdDesc;
    }
    
    /** 
     * @param cdDesc the cdDesc to set
     */
    public void setCdDesc(String cdDesc)
    {
        this.cdDesc = cdDesc;
    }
    
    /**  
     *@return  the scopeType
     */
    public int getScopeType()
    {
        return scopeType;
    }
    
    /** 
     * @param scopeType the scopeType to set
     */
    public void setScopeType(int scopeType)
    {
        this.scopeType = scopeType;
    }
    
}
