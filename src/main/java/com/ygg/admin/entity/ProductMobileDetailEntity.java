package com.ygg.admin.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class ProductMobileDetailEntity extends BaseEntity
{
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    
    private int id;
    
    private int productId;
    
    private String content;
    
    private byte contentType;
    
    private short height;
    
    private short width;
    
    private byte isLink;
    
    private String link;
    
    private byte linkType;
    
    private byte order;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getProductId()
    {
        return productId;
    }
    
    public void setProductId(int productId)
    {
        this.productId = productId;
    }
    
    public String getContent()
    {
        return content;
    }
    
    public void setContent(String content)
    {
        this.content = content;
    }
    
    public byte getContentType()
    {
        return contentType;
    }
    
    public void setContentType(byte contentType)
    {
        this.contentType = contentType;
    }
    
    public short getHeight()
    {
        return height;
    }
    
    public void setHeight(short height)
    {
        this.height = height;
    }
    
    public short getWidth()
    {
        return width;
    }
    
    public void setWidth(short width)
    {
        this.width = width;
    }
    
    public byte getIsLink()
    {
        return isLink;
    }
    
    public void setIsLink(byte isLink)
    {
        this.isLink = isLink;
    }
    
    public String getLink()
    {
        return link;
    }
    
    public void setLink(String link)
    {
        this.link = link;
    }
    
    public byte getLinkType()
    {
        return linkType;
    }
    
    public void setLinkType(byte linkType)
    {
        this.linkType = linkType;
    }
    
    public byte getOrder()
    {
        return order;
    }
    
    public void setOrder(byte order)
    {
        this.order = order;
    }
    
    public int hashCode()
    {
        return HashCodeBuilder.reflectionHashCode(this);
    }
    
    public boolean equals(Object obj)
    {
        return EqualsBuilder.reflectionEquals(this, obj);
    }
}
