package com.ygg.admin.entity;

import java.util.ArrayList;
import java.util.List;

public class TempProductEntity
{
    private String productBaseId;
    
    private int returnStock;
    
    private List<String> saleProductList = new ArrayList<String>();
    
    private List<String> mallProductList = new ArrayList<String>();
    
    public String getProductBaseId()
    {
        return productBaseId;
    }
    
    public void setProductBaseId(String productBaseId)
    {
        this.productBaseId = productBaseId;
    }
    
    public int getReturnStock()
    {
        return returnStock;
    }
    
    public void setReturnStock(int returnStock)
    {
        this.returnStock = returnStock;
    }
    
    public List<String> getSaleProductList()
    {
        return saleProductList;
    }
    
    public void setSaleProductList(List<String> saleProductList)
    {
        this.saleProductList = saleProductList;
    }
    
    public List<String> getMallProductList()
    {
        return mallProductList;
    }
    
    public void setMallProductList(List<String> mallProductList)
    {
        this.mallProductList = mallProductList;
    }
    
}
