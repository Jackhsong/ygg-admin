package com.ygg.admin.entity;

public class CategoryEntity
{
    private int id;
    
    private int productBaseId;
    
    private int categoryFirstId;
    
    private String categoryFirstName = "";
    
    private int categorySecondId;
    
    private String categorySecondName = "";
    
    private int categoryThirdId;
    
    private String categoryThirdName = "";
    
    private String categoryName = "";
    
    private int sequence;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getProductBaseId()
    {
        return productBaseId;
    }
    
    public void setProductBaseId(int productBaseId)
    {
        this.productBaseId = productBaseId;
    }
    
    public int getCategoryFirstId()
    {
        return categoryFirstId;
    }
    
    public void setCategoryFirstId(int categoryFirstId)
    {
        this.categoryFirstId = categoryFirstId;
    }
    
    public int getCategorySecondId()
    {
        return categorySecondId;
    }
    
    public void setCategorySecondId(int categorySecondId)
    {
        this.categorySecondId = categorySecondId;
    }
    
    public int getCategoryThirdId()
    {
        return categoryThirdId;
    }
    
    public void setCategoryThirdId(int categoryThirdId)
    {
        this.categoryThirdId = categoryThirdId;
    }
    
    public String getCategoryFirstName()
    {
        return categoryFirstName;
    }
    
    public void setCategoryFirstName(String categoryFirstName)
    {
        this.categoryFirstName = categoryFirstName;
    }
    
    public String getCategorySecondName()
    {
        return categorySecondName;
    }
    
    public void setCategorySecondName(String categorySecondName)
    {
        this.categorySecondName = categorySecondName;
    }
    
    public String getCategoryThirdName()
    {
        return categoryThirdName;
    }
    
    public void setCategoryThirdName(String categoryThirdName)
    {
        this.categoryThirdName = categoryThirdName;
    }
    
    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }
    
    public String getCategoryName()
    {
        categoryName = categoryFirstName + "-" + categorySecondName + "-" + categoryThirdName;
        return categoryName;
    }
    
    public int getSequence()
    {
        return sequence;
    }
    
    public void setSequence(int sequence)
    {
        this.sequence = sequence;
    }
    
    @Override
    public String toString()
    {
        return "[ id=" + id + ",productBaseId=" + productBaseId + ",categoryFirstId=" + categoryFirstId + ",categorySecondId=" + categorySecondId + ",categoryThirdId="
            + categoryThirdId + " ]";
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + categoryFirstId;
        result = prime * result + categorySecondId;
        result = prime * result + categoryThirdId;
        result = prime * result + productBaseId;
        return result;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CategoryEntity other = (CategoryEntity)obj;
        if (productBaseId != other.getProductBaseId())
        {
            return false;
        }
        if (categoryThirdId != other.categoryThirdId)
        {
            return false;
        }
        else
        {
            if (categorySecondId != other.categorySecondId)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
    }
}
