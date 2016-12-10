package com.ygg.admin.entity;

public class OrderProductCommentEntity
{
    
    private int id;
    
    private int accountId;
    
    private int orderId;
    
    private int orderProductId;
    
    private int productId;
    
    private int productBaseId;
    
    /**评论等级，1：差评，2：中评，3：好评*/
    private int level;
    
    /**评论内容*/
    private String content;
    
    /**用户上传图片，image1,image2,image3*/
    private String image1;
    
    private String image2;
    
    private String image3;
    
    /**左岸城堡回复*/
    private String reply;
    
    private int isDisplay;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getAccountId()
    {
        return accountId;
    }
    
    public void setAccountId(int accountId)
    {
        this.accountId = accountId;
    }
    
    public int getOrderId()
    {
        return orderId;
    }
    
    public void setOrderId(int orderId)
    {
        this.orderId = orderId;
    }
    
    public int getOrderProductId()
    {
        return orderProductId;
    }
    
    public void setOrderProductId(int orderProductId)
    {
        this.orderProductId = orderProductId;
    }
    
    public int getProductId()
    {
        return productId;
    }
    
    public void setProductId(int productId)
    {
        this.productId = productId;
    }
    
    public int getProductBaseId()
    {
        return productBaseId;
    }
    
    public void setProductBaseId(int productBaseId)
    {
        this.productBaseId = productBaseId;
    }
    
    public int getLevel()
    {
        return level;
    }
    
    public void setLevel(int level)
    {
        this.level = level;
    }
    
    public String getContent()
    {
        return content;
    }
    
    public void setContent(String content)
    {
        this.content = content;
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
    
    public String getReply()
    {
        return reply;
    }
    
    public void setReply(String reply)
    {
        this.reply = reply;
    }
    
    public int getIsDisplay()
    {
        return isDisplay;
    }
    
    public void setIsDisplay(int isDisplay)
    {
        this.isDisplay = isDisplay;
    }
    
}
