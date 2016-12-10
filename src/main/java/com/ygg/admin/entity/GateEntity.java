package com.ygg.admin.entity;

public class GateEntity
{
    /**Id*/
    private int id;
    
    /**优惠券ID*/
    private int couponId;
    
    /**任意门主题*/
    private String theme;
    
    /**任意门口令答案 */
    private String answer;
    
    /**任意门口令描述 */
    private String desc;
    
    /**有效期起始时间 */
    private String validTimeStart;
    
    /**有效期结束时间*/
    private String validTimeEnd;
    
    /**领取人数 */
    private int receiveAmount = 0;
    
    /** 任意门带来新注册人数*/
    private int newRegister = 0;
    
    /** 任意门带来交易金额*/
    private float totalMoney = 0.0f;
    
    /** 任意门带来新用户交易金额*/
    private float newBuyerMoney = 0.0f;
    
    /** 链接URL（分享链接）*/
    private String url = "";
    
    /**领券提示文案*/
    private String receiveTip;
    
    /** 是否展现，1展现，0不展现*/
    private int isDisplay = 1;
    
    /**创建时间 */
    private String createTime;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public String getTheme()
    {
        return theme;
    }
    
    public void setTheme(String theme)
    {
        this.theme = theme;
    }
    
    public String getAnswer()
    {
        return answer;
    }
    
    public void setAnswer(String answer)
    {
        this.answer = answer;
    }
    
    public String getDesc()
    {
        return desc;
    }
    
    public void setDesc(String desc)
    {
        this.desc = desc;
    }
    
    public String getValidTimeStart()
    {
        return validTimeStart;
    }
    
    public void setValidTimeStart(String validTimeStart)
    {
        this.validTimeStart = validTimeStart;
    }
    
    public String getValidTimeEnd()
    {
        return validTimeEnd;
    }
    
    public void setValidTimeEnd(String validTimeEnd)
    {
        this.validTimeEnd = validTimeEnd;
    }
    
    public int getReceiveAmount()
    {
        return receiveAmount;
    }
    
    public void setReceiveAmount(int receiveAmount)
    {
        this.receiveAmount = receiveAmount;
    }
    
    public int getNewRegister()
    {
        return newRegister;
    }
    
    public void setNewRegister(int newRegister)
    {
        this.newRegister = newRegister;
    }
    
    public float getTotalMoney()
    {
        return totalMoney;
    }
    
    public void setTotalMoney(float totalMoney)
    {
        this.totalMoney = totalMoney;
    }
    
    public float getNewBuyerMoney()
    {
        return newBuyerMoney;
    }
    
    public void setNewBuyerMoney(float newBuyerMoney)
    {
        this.newBuyerMoney = newBuyerMoney;
    }
    
    public String getUrl()
    {
        return url;
    }
    
    public void setUrl(String url)
    {
        this.url = url;
    }
    
    public int getIsDisplay()
    {
        return isDisplay;
    }
    
    public void setIsDisplay(int isDisplay)
    {
        this.isDisplay = isDisplay;
    }
    
    public String getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }
    
    public String getReceiveTip()
    {
        return receiveTip;
    }
    
    public void setReceiveTip(String receiveTip)
    {
        this.receiveTip = receiveTip;
    }

	public int getCouponId() {
		return couponId;
	}

	public void setCouponId(int couponId) {
		this.couponId = couponId;
	}
}
