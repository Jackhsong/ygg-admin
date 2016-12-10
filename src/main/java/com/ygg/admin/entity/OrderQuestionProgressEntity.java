package com.ygg.admin.entity;

public class OrderQuestionProgressEntity
{
    private int id;
    
    private int questionId;
    
    private String content;
    
    private int status;
    
    private int hasImage;
    
    private int createUser;
    
    private String createTime;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getQuestionId()
    {
        return questionId;
    }
    
    public void setQuestionId(int questionId)
    {
        this.questionId = questionId;
    }
    
    public String getContent()
    {
        return content;
    }
    
    public void setContent(String content)
    {
        this.content = content;
    }
    
    public int getStatus()
    {
        return status;
    }
    
    public void setStatus(int status)
    {
        this.status = status;
    }
    
    public int getHasImage()
    {
        return hasImage;
    }
    
    public void setHasImage(int hasImage)
    {
        this.hasImage = hasImage;
    }
    
    public int getCreateUser()
    {
        return createUser;
    }
    
    public void setCreateUser(int createUser)
    {
        this.createUser = createUser;
    }
    
    public String getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }
    
}
