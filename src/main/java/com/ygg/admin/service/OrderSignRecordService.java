package com.ygg.admin.service;

public interface OrderSignRecordService
{
    /**
     * 发送签收短息
     * 
     * @return
     */
    int sendMessage()
        throws Exception;
}
