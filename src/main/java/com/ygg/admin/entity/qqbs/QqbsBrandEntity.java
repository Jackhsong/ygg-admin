package com.ygg.admin.entity.qqbs;

public class QqbsBrandEntity
{
    /**左岸城堡
品牌id*/
    private int id;
    
    /**品牌馆栏目id*/
    private int qqbsBrandCategoryId;
    
    /**通用专场组合id*/
    private int activitiesCommonId;
    
    /**通用专场组合链接*/
    private String urlacId;
    
    /**品牌名称*/
    private String name;
    
    /**Brand品牌id*/
    private int brandId;

    /**展示顺序*/
    private int order;
    
    /**品牌图片访问URL*/
    private String image;
    
    /**前台使用*/
    private String imageUrl;
    
    /**是否展现*/
    private int isDisplay;
    
    /**创建时间*/
    private String createTime;
    
    /**更新时间*/
    private String updateTime;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getQqbsBrandCategoryId()
    {
        return qqbsBrandCategoryId;
    }

    public void setQqbsBrandCategoryId(int qqbsBrandCategoryId)
    {
        this.qqbsBrandCategoryId = qqbsBrandCategoryId;
    }

    public int getActivitiesCommonId()
    {
        return activitiesCommonId;
    }

    public void setActivitiesCommonId(int activitiesCommonId)
    {
        this.activitiesCommonId = activitiesCommonId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getBrandId()
    {
        return brandId;
    }

    public void setBrandId(int brandId)
    {
        this.brandId = brandId;
    }

    public int getOrder()
    {
        return order;
    }

    public void setOrder(int order)
    {
        this.order = order;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
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

    public String getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(String updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public String getUrlacId()
    {
        return urlacId;
    }

    public void setUrlacId(String urlacId)
    {
        this.urlacId = urlacId;
    }

    
}
