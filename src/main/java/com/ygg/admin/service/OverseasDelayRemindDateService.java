package com.ygg.admin.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface OverseasDelayRemindDateService
{
    /**
     * 删除发送短信时间
     * 
     * @param para
     * @return
     */
    int delete(Map<String, Object> para)
        throws Exception;
    
    /**
     * 保存或更新短信发送时间
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int saveOrUpdate(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查询全部的发送时间表
     * 
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> jsonInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据年份删除之前的数据
     * 
     * @param string
     * @return
     */
    boolean deleteByYear(String year)
        throws Exception;
    
    /**
     * 导入发送短信时间表
     * 
     * @param file
     * @param year
     * @return
     * @throws Exception
     */
    Map<String, Object> importDelayDate(MultipartFile file, String year)
        throws Exception;
    
    /**
     * 验证是否存在
     * 
     * @param para
     * @return
     * @throws Exception
     */
    boolean isExist(Map<String, Object> para)
        throws Exception;
    
}
