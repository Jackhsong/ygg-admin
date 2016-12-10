package com.ygg.admin.service;

import java.util.Map;

import com.ygg.admin.entity.CustomFunctionEntity;

public interface CustomFunctionService
{
    /**
     * 异步加载首页自定义功能列表
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> jsonCustomFunctionInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 新增首页自定义功能
     * @param function
     * @return
     * @throws Exception
     */
    int saveCustomFunction(CustomFunctionEntity function)
        throws Exception;
    
    /**
     * 更新首页自定义功能
     * @param function
     * @return
     * @throws Exception
     */
    int updateCustomFunction(CustomFunctionEntity function)
        throws Exception;
    
    /**
     * 更新展现状态
     * @param para
     * @return
     * @throws Exception
     */
    int updateDisplayStatus(Map<String, Object> para)
        throws Exception;
    
}
