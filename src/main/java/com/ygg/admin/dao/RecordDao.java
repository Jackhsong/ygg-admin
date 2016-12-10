package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

public interface RecordDao
{
    
    // <!--######################## begin ################ 短信内容监控记录
    // ################-->
    
    /**
     * 插入短信发送记录
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int insertSmsContentRecord(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据para查询短信发送记录
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findSmsContentRecordByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据para 统计 短信内容监控记录 总数
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int countSmsContentRecordByPara(Map<String, Object> para)
        throws Exception;
    
    // <!--################## end ################# 短信内容监控记录
    // ##################-->
}
