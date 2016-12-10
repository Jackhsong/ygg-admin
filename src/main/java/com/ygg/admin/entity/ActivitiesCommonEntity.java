package com.ygg.admin.entity;

public class ActivitiesCommonEntity extends BaseEntity
{
    private static final long serialVersionUID = 1L;
    
    private int id;
    
    private String name;
    
    private String image;
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String detail;
    
    private String desc;
    
    private String gegesay;
    
    private int gegeImageId;
    
    private String wxShareTitle;
    
    private byte isAvailable = 1;
    
    /** app顶部头区域显示类型，1：默认底色，2：透明底色*/
    private int headType = 1;
    
    /** app分享支持类型，1：全部，2：微信好友及微信朋友圈*/
    private int shareType = 1;
    
    /**1.7及以上版本使用*/
    private String newImage17;
    
    /**1：特卖商品组合，2：商城商品组合*/
    private int type = 1;
    
    public String getWxShareTitle()
    {
        return wxShareTitle;
    }
    
    public void setWxShareTitle(String wxShareTitle)
    {
        this.wxShareTitle = wxShareTitle;
    }
    
    public String getGegesay()
    {
        return gegesay;
    }
    
    public void setGegesay(String gegesay)
    {
        this.gegesay = gegesay;
    }
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getImage()
    {
        return image;
    }
    
    public void setImage(String image)
    {
        this.image = image;
    }
    public String getImage1()
    {
        return image1;
    }
    
    public void setImage1(String image1)
    {
        this.image1 = image1;
    }
    public String getImage2()
    {
        return image2;
    }
    
    public void setImage2(String image2)
    {
        this.image2 = image2;
    }
    public String getImage3()
    {
        return image3;
    }
    
    public void setImage3(String image3)
    {
        this.image3 = image3;
    }
    public String getImage4()
    {
        return image4;
    }
    
    public void setImage4(String image4)
    {
        this.image4 = image4;
    }
    public String getDetail()
    {
        return detail;
    }
    
    public void setDetail(String detail)
    {
        this.detail = detail;
    }
       
    public String getDesc()
    {
        return desc;
    }
    
    public void setDesc(String desc)
    {
        this.desc = desc;
    }
    
    public byte getIsAvailable()
    {
        return isAvailable;
    }
    
    public void setIsAvailable(byte isAvailable)
    {
        this.isAvailable = isAvailable;
    }
    
    public int getGegeImageId()
    {
        return gegeImageId;
    }
    
    public void setGegeImageId(int gegeImageId)
    {
        this.gegeImageId = gegeImageId;
    }
    
    public int getType()
    {
        return type;
    }
    
    public void setType(int type)
    {
        this.type = type;
    }
    
    @Override
    public String toString()
    {
        return id + ":" + name;
    }
    
    public int getHeadType()
    {
        return headType;
    }
    
    public void setHeadType(int headType)
    {
        this.headType = headType;
    }
    
    public int getShareType()
    {
        return shareType;
    }
    
    public void setShareType(int shareType)
    {
        this.shareType = shareType;
    }
    
    public String getNewImage17()
    {
        return newImage17;
    }
    
    public void setNewImage17(String newImage17)
    {
        this.newImage17 = newImage17;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (obj instanceof ActivitiesCommonEntity)
        {
            ActivitiesCommonEntity other = (ActivitiesCommonEntity)obj;
            return this.id == other.id;
        }
        return false;
    }
    
    @Override
    public int hashCode()
    {
        return id;
    }
}
