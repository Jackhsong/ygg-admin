package com.ygg.admin.service;

import java.util.Map;

public interface OutCallServcie
{
    boolean kd100CallBack(String param, String refundId)
        throws Exception;

    
    /**
     * 快递吧回调
     * @param param
     * @return
     * @throws Exception
     */
    boolean kd8CallBack(Map<String, String> param)
        throws Exception;
}
