package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public interface MwebGroupProductInfoService
{
    public List<JSONObject> findAutoTeamList(Map<String, Object> parameter);
    
    /**
     * 
     * @创建人: zero
     * @创建时间: 2016年4月28日 下午2:25:23
     * @描述:
     *      <p>
     *      (获取自动团配置信息)
     *      </p>
     * @修改人: zero
     * @修改时间: 2016年4月28日 下午2:25:23
     * @修改备注:
     *        <p>
     *        (修改备注)
     *        </p>
     * @param id
     * @return
     * @returnType JSONObject
     * @version V1.0
     */
    public JSONObject getAutoTeamConfig(int id);
    
    public JSONObject updateAutoTeamConfig(Map<String, Object> parameter);
    
    /**
     * 
     * @创建人: zero
     * @创建时间: 2016年4月29日 下午5:57:20
     * @描述:
     *      <p>
     *      (自动参团开启与停止)
     *      </p>
     * @修改人: zero
     * @修改时间: 2016年4月29日 下午5:57:20
     * @修改备注:
     *        <p>
     *        (修改备注)
     *        </p>
     * @param parameter
     * @return
     * @returnType int
     * @version V1.0
     */
    public JSONObject setupAutoJoinTeam(int id, int isOpenAutoJoinTeam);
    
}
