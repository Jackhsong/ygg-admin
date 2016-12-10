package com.ygg.admin.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResultEntity implements Serializable
{
    
    private static final long serialVersionUID = -5215290655607788869L;
    
    private int status = 0;
    
    private String message = "";
    
    private int total = 0;
    
    private List<?> rows;
    
    private Object data;

    public ResultEntity(){

    }
    
    public ResultEntity(int status)
    {
        this.status = status;
    }
    
    public ResultEntity(int status, String message)
    {
        this.status = status;
        this.message = message;
    }
    
    public ResultEntity(int status, Object data)
    {
        this.status = status;
        this.data = data;
    }
    
    public ResultEntity(int total, List<?> rows)
    {
        this.total = total;
        this.rows = rows;
    }
    
    /**
     * 成功时返回 <br>
     * 里面只包含一个状态属性<code>status</code>，值为1 <br>
     * 对应的JSON格式为：<code>{"status":1}</code>
     *
     * @return
     */
    public static ResultEntity getSuccessResult()
    {
        return new ResultEntity(1);
    }
    
    public static ResultEntity getSuccessResult(String message)
    {
        return new ResultEntity(1, message);
    }
    
    /**
     * 成功时返回 <br>
     * 里面只包含一个状态属性<code>status</code>，值为1 <br>
     * 对应的JSON格式为：<code>{"status":1}</code>
     *
     * @return
     */
    public static ResultEntity getSuccessResult(Object object)
    {
        return new ResultEntity(1, object);
    }
    
    /**
     * 失败时返回 <br>
     * 里面包含一个状态属性<code>status</code>，值为0 <br>
     * 包含一个失败说明<code>message</code>，值为传递的参数 <br>
     * 对应的JSON格式为：<code>{"status":1,"message":xxx}</code>
     * 
     * @param message
     *            失败说明
     * @return
     */
    public static ResultEntity getFailResult(String message)
    {
        return new ResultEntity(0, message);
    }
    
    /**
     * 失败时返回列表
     * @return
     */
    public static ResultEntity getFailResultList()
    {
        return new ResultEntity(0, new ArrayList<>());
    }
    
    /**
     * 返回列表
     * @param total：总条数
     * @param rows：数据list
     * @return
     */
    public static ResultEntity getResultList(int total, List<?> rows)
    {
        return new ResultEntity(total, rows);
    }
    
    public int getStatus()
    {
        return status;
    }
    
    public void setStatus(int status)
    {
        this.status = status;
    }
    
    public String getMessage()
    {
        return message;
    }
    
    public void setMessage(String message)
    {
        this.message = message;
    }
    
    public Object getData()
    {
        return data;
    }
    
    public void setData(Object data)
    {
        this.data = data;
    }
    
    public int getTotal()
    {
        return total;
    }
    
    public void setTotal(int total)
    {
        this.total = total;
    }
    
    public List<?> getRows()
    {
        return rows;
    }
    
    public void setRows(List<?> rows)
    {
        this.rows = rows;
    }
    
}
