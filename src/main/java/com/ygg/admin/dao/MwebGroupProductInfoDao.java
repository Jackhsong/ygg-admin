package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.ygg.admin.entity.MwebGroupProductInfoEntity;

public interface MwebGroupProductInfoDao
{
    public int addGroupProductInfo(MwebGroupProductInfoEntity mwebGroupProductInfoEntity);
    
    public List<JSONObject> findAutoTeamList(Map<String, Object> parameter);
    
    public JSONObject findAutoTeamListByMwebGroupProductInfoId(int mwebGroupProductInfoId);
    
    public MwebGroupProductInfoEntity getGroupProductInfoById(int id);
    
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
    
    public int updateAutoTeamConfig(Map<String, Object> parameter);
    
    public int addAutoTeamConfig(Map<String, Object> parameter);
    
    /**
     * 
     * @创建人: zero
     * @创建时间: 2016年5月3日 上午11:42:38
     * @描述:
     *      <p>
     *      (自动参团人数)
     *      </p>
     * @修改人: zero
     * @修改时间: 2016年5月3日 上午11:42:38
     * @修改备注:
     *        <p>
     *        (修改备注)
     *        </p>
     * @param mwebGroupProductInfoId
     * @return
     * @returnType int
     * @version V1.0
     */
    public int countAutoJoinTeamAccount(int mwebGroupProductInfoId);
    
    /**
     * 
     * @创建人: zero
     * @创建时间: 2016年5月5日 下午8:30:07
     * @描述:
     *      <p>
     *      (根据商品id查所有自动团信息)
     *      </p>
     * @修改人: zero
     * @修改时间: 2016年5月5日 下午8:30:07
     * @修改备注:
     *        <p>
     *        (修改备注)
     *        </p>
     * @param mwebGroupProductId
     * @return
     * @returnType List<MwebGroupProductInfoEntity>
     * @version V1.0
     */
    public List<MwebGroupProductInfoEntity> findAutoGroupProductInfoByMwebGroupProductId(int mwebGroupProductId);
    
    public int updateGroupProductInfo(MwebGroupProductInfoEntity mwebGroupProductInfoEntity);
    
}
