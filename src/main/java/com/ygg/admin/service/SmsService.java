package com.ygg.admin.service;

import java.util.Map;

/**
 * 短信发送相关service
 * 
 * @author Administrator
 *
 */
public interface SmsService
{
    
    /**
     * 余额查询
     * 
     * @return
     */
    double queryMoney();
    
    /**
     * 发送短信
     * 
     * @param para
     * @return
     */
    int send(Map<String, Object> para);
    
    /**
     * 获取已发送短信内容
     * 
     * @param para
     * @return
     * @throws Exception
     */
    String jsoninfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 发送梦网短信
     * @param para
     * @return
     * @throws Exception
     */
    int sendMontnets(Map<String, Object> para)
        throws Exception;
    
    int sendMontnetsTuan(Map<String, Object> para)
        throws Exception;

    int sendMontnetsGlobal(Map<String, Object> para)
            throws Exception;

}
