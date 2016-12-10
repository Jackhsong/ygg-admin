package com.ygg.admin.entity;

/**
 * 游戏entity
 * 
 * @author xiongl
 *         
 */
public class MwebGroupGameEntity
{
    /** 游戏Id */
    private int id;
    
    private int mwebGroupProductId;
    
    private String mwebGroupProductImage;
    
    /** 游戏名称（分享标题） */
    private String gameName;
    
    /** 游戏logo（分享图标） */
    private String gameLogo;
    
    /** 游戏简介（分享分享内容） */
    private String introduce;
    
    /** 游戏链接URL（分享链接） */
    private String gameURL = "";
    
    /** 领取人数 */
    private int receiveAmount = 0;
    
    /** 游戏带来新注册人数 */
    private int newRegister = 0;
    
    /** 游戏带来交易金额 */
    private float totalMoney = 0.0f;
    
    /** 游戏带来新用户交易金额 */
    private float newBuyerMoney = 0.0f;
    
    /** 游戏领奖视图URL */
    private String viewURL = "";
    
    /** 是否可用，1可用，0不可用 */
    private int isAvailable;
    
    /** 创建时间 */
    private String createTime;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public String getGameName()
    {
        return gameName;
    }
    
    public void setGameName(String gameName)
    {
        this.gameName = gameName;
    }
    
    public String getGameLogo()
    {
        return gameLogo;
    }
    
    public void setGameLogo(String gameLogo)
    {
        this.gameLogo = gameLogo;
    }
    
    public String getIntroduce()
    {
        return introduce;
    }
    
    public void setIntroduce(String introduce)
    {
        this.introduce = introduce;
    }
    
    public String getGameURL()
    {
        return gameURL;
    }
    
    public void setGameURL(String gameURL)
    {
        this.gameURL = gameURL;
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
    
    public String getViewURL()
    {
        return viewURL;
    }
    
    public void setViewURL(String viewURL)
    {
        this.viewURL = viewURL;
    }
    
    public int getIsAvailable()
    {
        return isAvailable;
    }
    
    public void setIsAvailable(int isAvailable)
    {
        this.isAvailable = isAvailable;
    }
    
    public String getCreateTime()
    {
        return createTime;
    }
    
    public int getMwebGroupProductId()
    {
        return mwebGroupProductId;
    }
    
    public void setMwebGroupProductId(int mwebGroupProductId)
    {
        this.mwebGroupProductId = mwebGroupProductId;
    }
    
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }
    
    public String getMwebGroupProductImage()
    {
        return mwebGroupProductImage;
    }
    
    public void setMwebGroupProductImage(String mwebGroupProductImage)
    {
        this.mwebGroupProductImage = mwebGroupProductImage;
    }
    
}
