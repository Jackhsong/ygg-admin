package com.ygg.admin.entity;

public class OrderQuestionEntity
{
    private int id;
    
    private int orderId;
    
    private int questionTemplateId;
    
    private String templateName;
    
    private String questionDesc;
    
    /**回复顾客时限类型，1：1小时后；2：2小时后；3：自定义时间*/
    private int timeLimitType;
    
    /**timeLimitType=3时，使用该字段*/
    private String customTime;
    
    /**顾客问题进度，1进行中；2已完结*/
    private int customerStatus;
    
    /**商家对接进度，1进行中；2已完结*/
    private int sellerStatus;
    
    private int createUser;
    
    private String createTime;
    
    /**顾客处理完成时间*/
    private String customerFinishTime;
    
    /**商家对接完成时间*/
    private String sellerFinishTime;
    
    /**顾客处理记录明细，实际已经存在order_question_customer_progress表中，MD需求改成这样了*/
    private String customerDealDetail;
    
    /**商家处理记录明细，实际已经存在order_question_seller_progress表中，SB需求改成这样了*/
    private String sellerDealDetail;
    
    /**是否推送给商家，1是，0否*/
    private int isPush;
    
    /**商家反馈记录*/
    private String sellerFeedbackDetail;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getOrderId()
    {
        return orderId;
    }
    
    public void setOrderId(int orderId)
    {
        this.orderId = orderId;
    }
    
    public int getQuestionTemplateId()
    {
        return questionTemplateId;
    }
    
    public void setQuestionTemplateId(int questionTemplateId)
    {
        this.questionTemplateId = questionTemplateId;
    }
    
    public String getQuestionDesc()
    {
        return questionDesc;
    }
    
    public void setQuestionDesc(String questionDesc)
    {
        this.questionDesc = questionDesc;
    }
    
    public int getTimeLimitType()
    {
        return timeLimitType;
    }
    
    public void setTimeLimitType(int timeLimitType)
    {
        this.timeLimitType = timeLimitType;
    }
    
    public String getCustomTime()
    {
        return customTime;
    }
    
    public void setCustomTime(String customTime)
    {
        this.customTime = customTime;
    }
    
    public int getCustomerStatus()
    {
        return customerStatus;
    }
    
    public void setCustomerStatus(int customerStatus)
    {
        this.customerStatus = customerStatus;
    }
    
    public int getSellerStatus()
    {
        return sellerStatus;
    }
    
    public void setSellerStatus(int sellerStatus)
    {
        this.sellerStatus = sellerStatus;
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
    
    public String getTemplateName()
    {
        return templateName;
    }
    
    public void setTemplateName(String templateName)
    {
        this.templateName = templateName;
    }
    
    public String getCustomerFinishTime()
    {
        return customerFinishTime;
    }
    
    public void setCustomerFinishTime(String customerFinishTime)
    {
        this.customerFinishTime = customerFinishTime;
    }
    
    public String getSellerFinishTime()
    {
        return sellerFinishTime;
    }
    
    public void setSellerFinishTime(String sellerFinishTime)
    {
        this.sellerFinishTime = sellerFinishTime;
    }
    
    public String getCustomerDealDetail()
    {
        return customerDealDetail;
    }
    
    public void setCustomerDealDetail(String customerDealDetail)
    {
        this.customerDealDetail = customerDealDetail;
    }
    
    public String getSellerDealDetail()
    {
        return sellerDealDetail;
    }
    
    public void setSellerDealDetail(String sellerDealDetail)
    {
        this.sellerDealDetail = sellerDealDetail;
    }
    
    public int getIsPush()
    {
        return isPush;
    }
    
    public void setIsPush(int isPush)
    {
        this.isPush = isPush;
    }
    
    public String getSellerFeedbackDetail()
    {
        return sellerFeedbackDetail;
    }
    
    public void setSellerFeedbackDetail(String sellerFeedbackDetail)
    {
        this.sellerFeedbackDetail = sellerFeedbackDetail;
    }
    
}
