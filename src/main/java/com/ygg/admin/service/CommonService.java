package com.ygg.admin.service;

public interface CommonService
{
    int getCurrentUserId()
        throws Exception;
    
    String getCurrentUserName()
        throws Exception;
    
    String getCurrentRealName()
        throws Exception;
}
