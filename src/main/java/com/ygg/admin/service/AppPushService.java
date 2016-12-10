package com.ygg.admin.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AppPushService
{
    /**
     * 向单个用户推送消息
     * @param para
     * @return
     * @throws Exception
     */
    public Map<String, Object> pushMessage(List<String> accountIdList, Set<String> pushIdList, byte pushType, String pushUrl, String pushNumber, String pushTitle,
        String pushContent, String pushProductId, String pushWindowId)
        throws Exception;
    
}
