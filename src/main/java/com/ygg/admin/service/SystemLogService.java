package com.ygg.admin.service;

import java.util.Map;

public interface SystemLogService
{
    
    Map<String, Object> jsonSystemLogInfo(Map<String, Object> map)
        throws Exception;
    
    /**
     * 记录日志，有详细操作内容，给管理人员看（实现shi一样，全部if-else）,记录在表system_log表中
     * @param para
     * @return
     * @throws Exception
     */
    int logger(Map<String, Object> para)
        throws Exception;
    
    /**
     * 记录日志，给开发人员分析用，切面时限，记录在log表中
     * @param para
     * @return
     * @throws Exception
     */
    int log(Map<String, Object> para)
        throws Exception;
    
    Map<String, Object> jsonLogInfo(Map<String, Object> para)
        throws Exception;

    /**
     * 记录新增对象日志
     * @param objName：对象名称
     * @param level：日志级别，具体请查看LogEnum.Level枚举
     * @param businessType：业务模块，具体请查看LogEnum.BUSINESS_TYPE枚举
     * @param operationType：操作类型，具体请查看LogEnum.OPERATION_TYPE枚举
     * @param obj：新增对象
     */
    void loggerInsert(String objName, int level, int businessType, int operationType, Object obj);
        
    /**
     * 记录新增对象日志
     * @param objName：对象名称
     * @param level：日志级别，具体请查看LogEnum.Level枚举
     * @param businessType：业务模块，具体请查看LogEnum.BUSINESS_TYPE枚举
     * @param operationType：操作类型，具体请查看LogEnum.OPERATION_TYPE枚举
     * @param params：新增对象参数map
     */
    void loggerInsert(String objName, int level, int businessType, int operationType, Map<? extends Object, ? extends Object> params);

    /**
     * 记录更新对象日志
     * @param objName：对象名称
     * @param level：日志级别，具体请查看LogEnum.Level枚举
     * @param businessType：业务模块，具体请查看LogEnum.BUSINESS_TYPE枚举
     * @param operationType：操作类型，具体请查看LogEnum.OPERATION_TYPE枚举
     * @param oldObj：修改之前的对象
     * @param newObj：修改之后的对象
     */
    void loggerUpdate(String objName, int level, int businessType, int operationType, Object oldObj, Object newObj);

    /**
     * 记录更新对象日志
     * @param objName：对象名称
     * @param level：日志级别，具体请查看LogEnum.Level枚举
     * @param businessType：业务模块，具体请查看LogEnum.BUSINESS_TYPE枚举
     * @param operationType：操作类型，具体请查看LogEnum.OPERATION_TYPE枚举
     * @param oldObj：修改之前的对象
     * @param params：修改的参数
     */
    void loggerUpdate(String objName, int level, int businessType, int operationType, Object oldObj, Map<String, ? extends Object> params);

    /**
     * 记录删除对象日志
     * @param objName：对象名称
     * @param level：日志级别，具体请查看LogEnum.Level枚举
     * @param businessType：业务模块，具体请查看LogEnum.BUSINESS_TYPE枚举
     * @param operationType：操作类型，具体请查看LogEnum.OPERATION_TYPE枚举
     * @param obj：删除的对象
     */
    void loggerDelete(String objName, int level, int businessType, int operationType, Object obj);

    /**
     * 记录删除对象日志
     * @param objName：对象名称
     * @param level：日志级别，具体请查看LogEnum.Level枚举
     * @param businessType：业务模块，具体请查看LogEnum.BUSINESS_TYPE枚举
     * @param operationType：操作类型，具体请查看LogEnum.OPERATION_TYPE枚举
     * @param objId：删除对象的id
     */
    void loggerDelete(String objName, int level, int businessType, int operationType, int objId);

    /**
     * 记录删除对象日志
     * @param objName：对象名称
     * @param level：日志级别，具体请查看LogEnum.Level枚举
     * @param businessType：业务模块，具体请查看LogEnum.BUSINESS_TYPE枚举
     * @param operationType：操作类型，具体请查看LogEnum.OPERATION_TYPE枚举
     * @param params：删除的参数
     */
    void loggerDelete(String objName, int level, int businessType, int operationType, Map<? extends Object, ? extends Object> params);

    /**
     * 记录导出日志
     * @param objName
     * @param level
     * @param businessType
     * @param operationType
     * @param params
     */
    void loggerExport(String objName, int level, int businessType, int operationType, Map<? extends Object, ? extends Object> params);
}
