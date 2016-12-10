package com.ygg.admin.entity.yw;

import com.ygg.admin.entity.base.BaseEntity;

public class YwAccountRelaChangeLogEntity extends BaseEntity
{
    
    /**    */
   private static final long serialVersionUID = 8812740968155679132L;
   private int accountId;
   private int tuiAccountId;
   private String operator;
   private String remark;
   
   /**  
    *@return  the accountId
    */
   public int getAccountId()
   {
       return accountId;
   }
   
   /** 
    * @param accountId the accountId to set
    */
   public void setAccountId(int accountId)
   {
       this.accountId = accountId;
   }
   
   /**  
    *@return  the tuiAccountId
    */
   public int getTuiAccountId()
   {
       return tuiAccountId;
   }
   
   /** 
    * @param tuiAccountId the tuiAccountId to set
    */
   public void setTuiAccountId(int tuiAccountId)
   {
       this.tuiAccountId = tuiAccountId;
   }
   
   /**  
    *@return  the operator
    */
   public String getOperator()
   {
       return operator;
   }
   
   /** 
    * @param operator the operator to set
    */
   public void setOperator(String operator)
   {
       this.operator = operator;
   }
   
   /**  
    *@return  the remark
    */
   public String getRemark()
   {
       return remark;
   }
   
   /** 
    * @param remark the remark to set
    */
   public void setRemark(String remark)
   {
       this.remark = remark;
   }
   
}
