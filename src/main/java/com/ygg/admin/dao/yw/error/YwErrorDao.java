package com.ygg.admin.dao.yw.error;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.yw.YwAccountRelaChangeLogEntity;

public interface YwErrorDao
{
    /**
     * 查询推荐列表
     * @param param
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findListInfo(Map<String, Object> param);
    
    /**
     * 统计条数
     * @param param
     * @return
     * @throws Exception
     */
    int getCountByParam(Map<String, Object> param);
    
    /**
     * TODO 请在此处添加注释
     * @param log
     * @return
     */
    public int insert(YwAccountRelaChangeLogEntity log);
}
