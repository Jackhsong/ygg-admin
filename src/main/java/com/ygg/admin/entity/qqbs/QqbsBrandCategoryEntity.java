package com.ygg.admin.entity.qqbs;

public class QqbsBrandCategoryEntity
{
    /**栏目ID */
    private int id;
    
    /**栏目名称 */
    private String name;
    
    /**排序*/
    private int order;
    
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getOrder()
    {
        return order;
    }

    public void setOrder(int order)
    {
        this.order = order;
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
    
    
}
