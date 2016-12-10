/**************************************************************************
* Copyright (c) 2014-2016 浙江格家网络技术有限公司.
* All rights reserved.
* 
* 项目名称：左岸城堡APP
* 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
*           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
*           识产权保护的内容。                            
***************************************************************************/
package com.ygg.admin.entity.hotlist;

import org.apache.commons.lang.StringUtils;

import com.ygg.admin.code.ProductEnum;
import com.ygg.admin.entity.base.BaseEntity;

/**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: SaleSingleProductEntity.java 9206 2016-03-24 09:13:16Z xiongliang $   
  * @since 2.0
  */
public class SaleSingleProductEntity extends BaseEntity
{
    /**基本商品id*/
    private int productBaseId;
    
    /**商品id*/
    private int productId;
    
    /**商品名称*/
    private String name;
    
    /**商品类型：1：特卖商品，2：商城商品*/
    private int type;
    
    /**实际销量*/
    private int actualSales;
    
    /**人工增量*/
    private int artificialIncrement;
    
    /**显示销量    */
    private int displaySales;
    
    /**是否展现；0：否，1：是    */
    private int isDisplay;
    
    /**商品类型名称    */
    private String typeName;
    
    /**  
     *@return  the productBaseId
     */
    public int getProductBaseId()
    {
        return productBaseId;
    }
    
    /** 
     * @param productBaseId the productBaseId to set
     */
    public void setProductBaseId(int productBaseId)
    {
        this.productBaseId = productBaseId;
    }
    
    /**  
     *@return  the productId
     */
    public int getProductId()
    {
        return productId;
    }
    
    /** 
     * @param productId the productId to set
     */
    public void setProductId(int productId)
    {
        this.productId = productId;
    }
    
    /**  
     *@return  the name
     */
    public String getName()
    {
        return name;
    }
    
    /** 
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**  
     *@return  the type
     */
    public int getType()
    {
        return type;
    }
    
    /** 
     * @param type the type to set
     */
    public void setType(int type)
    {
        this.type = type;
    }
    
    /**  
     *@return  the actualSales
     */
    public int getActualSales()
    {
        return actualSales;
    }
    
    /** 
     * @param actualSales the actualSales to set
     */
    public void setActualSales(int actualSales)
    {
        this.actualSales = actualSales;
    }
    
    /**  
     *@return  the artificialIncrement
     */
    public int getArtificialIncrement()
    {
        return artificialIncrement;
    }
    
    /** 
     * @param artificialIncrement the artificialIncrement to set
     */
    public void setArtificialIncrement(int artificialIncrement)
    {
        this.artificialIncrement = artificialIncrement;
    }
    
    /**  
     *@return  the displaySales
     */
    public int getDisplaySales()
    {
        return displaySales;
    }
    
    /** 
     * @param displaySales the displaySales to set
     */
    public void setDisplaySales(int displaySales)
    {
        this.displaySales = displaySales;
    }
    
    /**  
     *@return  the isDisplay
     */
    public int getIsDisplay()
    {
        return isDisplay;
    }
    
    /** 
     * @param isDisplay the isDisplay to set
     */
    public void setIsDisplay(int isDisplay)
    {
        this.isDisplay = isDisplay;
    }
    
    /**  
     *@return  the typeName
     */
    public String getTypeName()
    {
        String s = ProductEnum.PRODUCT_TYPE.getDescByCode(type);
        if (StringUtils.isNotBlank(s))
        {
            typeName = s;
        }
        return typeName;
    }
    
    /** 
     * @param typeName the typeName to set
     */
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }
    
}
