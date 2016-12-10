package com.ygg.admin.entity;

public class CustomActivityEntity
{
    private int id;
    
    /**类型；1：抽奖活动，2：情景特卖活动，3：礼包活动 ， 4：其他活动，5：任意门活动，6：精品特卖活动，7：美食抽奖活动，8：抽红包活动，9：迎新送券活动，10：美食幸运签活动，11：双11邀请好友红包，12：新精品特卖*/
    private byte type;
    
    /**app支持该活动的最低版本号*/
    private float version;
    
    /**类型id*/
    private int typeId;
    
    /**访问该自定义活动对应的URL*/
    private String url;
    
    private String remark;

    /** 微信朋友圈分享标题 from v2.6 */
    private String sharePengYouQuanTitle;
    
    /**分享标题，用于微信好友、微信朋友圈、QQ好友、QQ空间*/
    private String shareTitle;
    
    /**分享内容，用于微信好友、QQ好友*/
    private String shareContentTencent;
    
    /**分享内容，用于新浪微博*/
    private String shareContentSina;
    
    /**分享URL*/
    private String shareUrl;
    
    /**分享图标*/
    private String shareImage;
    
    /**app分享支持类型，1：全部，2：微信好友及微信朋友圈*/
    private byte shareType;
    
    /**app顶部头区域显示类型，1：默认底色，2：透明底色；*/
    private byte headType;
    
    /**是否可用；0：否，1：是*/
    private byte isAvailable;
    
    /**是否隐藏分享按钮*/
    private int isHideShareButton;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public byte getType()
    {
        return type;
    }
    
    public void setType(byte type)
    {
        this.type = type;
    }
    
    public float getVersion()
    {
        return version;
    }
    
    public void setVersion(float version)
    {
        this.version = version;
    }
    
    public int getTypeId()
    {
        return typeId;
    }
    
    public void setTypeId(int typeId)
    {
        this.typeId = typeId;
    }
    
    public String getUrl()
    {
        return url;
    }
    
    public void setUrl(String url)
    {
        this.url = url;
    }
    
    public String getRemark()
    {
        return remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public String getShareTitle()
    {
        return shareTitle;
    }
    
    public void setShareTitle(String shareTitle)
    {
        this.shareTitle = shareTitle;
    }
    
    public String getShareContentTencent()
    {
        return shareContentTencent;
    }
    
    public void setShareContentTencent(String shareContentTencent)
    {
        this.shareContentTencent = shareContentTencent;
    }
    
    public String getShareContentSina()
    {
        return shareContentSina;
    }
    
    public void setShareContentSina(String shareContentSina)
    {
        this.shareContentSina = shareContentSina;
    }
    
    public String getShareUrl()
    {
        return shareUrl;
    }
    
    public void setShareUrl(String shareUrl)
    {
        this.shareUrl = shareUrl;
    }
    
    public String getShareImage()
    {
        return shareImage;
    }
    
    public void setShareImage(String shareImage)
    {
        this.shareImage = shareImage;
    }
    
    public byte getShareType()
    {
        return shareType;
    }
    
    public void setShareType(byte shareType)
    {
        this.shareType = shareType;
    }
    
    public byte getHeadType()
    {
        return headType;
    }
    
    public void setHeadType(byte headType)
    {
        this.headType = headType;
    }
    
    public byte getIsAvailable()
    {
        return isAvailable;
    }
    
    public void setIsAvailable(byte isAvailable)
    {
        this.isAvailable = isAvailable;
    }
    
    public int getIsHideShareButton()
    {
        return isHideShareButton;
    }
    
    public void setIsHideShareButton(int isHideShareButton)
    {
        this.isHideShareButton = isHideShareButton;
    }

    public String getSharePengYouQuanTitle() {
        return sharePengYouQuanTitle;
    }

    public void setSharePengYouQuanTitle(String sharePengYouQuanTitle) {
        this.sharePengYouQuanTitle = sharePengYouQuanTitle;
    }
}
