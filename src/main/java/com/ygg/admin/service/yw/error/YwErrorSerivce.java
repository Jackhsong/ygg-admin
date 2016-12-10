package com.ygg.admin.service.yw.error;

import java.util.Map;

public interface YwErrorSerivce
{ 
    /**
     * 查询推荐列表
     * @param param
     * @return Map<String, Object>
     * @throws Exception
     */
    Map<String, Object> findListInfo(Map<String, Object> param) throws Exception;
    
    /**
     * 处理用户关系
     * @param accountId 用户ID
     * @param tuiAcountId 推荐人ID
     */
    String updateAccountRela(int accountId,int tuiAcountId,String remark);
}
