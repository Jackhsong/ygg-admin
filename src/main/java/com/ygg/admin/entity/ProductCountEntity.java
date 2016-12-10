package com.ygg.admin.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class ProductCountEntity extends BaseEntity
{
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    
    private int id;
    
    private int productId;
    
    private int sell;
    
    private int stock;
    
    private int lock;
    
    private int stockAlgorithm;
    
    private int restriction;
    
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
    
    public int getSell()
    {
        return sell;
    }
    
    public void setSell(int sell)
    {
        this.sell = sell;
    }
    
    public int getStock()
    {
        return stock;
    }
    
    public void setStock(int stock)
    {
        this.stock = stock;
    }
    
    public int getLock()
    {
        return lock;
    }
    
    public void setLock(int lock)
    {
        this.lock = lock;
    }
    
    public int getStockAlgorithm()
    {
        return stockAlgorithm;
    }
    
    public void setStockAlgorithm(int stockAlgorithm)
    {
        this.stockAlgorithm = stockAlgorithm;
    }
    
    public int getRestriction()
    {
        return restriction;
    }
    
    public void setRestriction(int restriction)
    {
        this.restriction = restriction;
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
