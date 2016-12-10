
 /**************************************************************************
 * Copyright (c) 2014-2016 浙江格家网络技术有限公司.
 * All rights reserved.
 * 
 * 项目名称：左岸城堡APP
 * 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.ygg.admin.dao.yw.cash.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.dao.yw.cash.YwCashDao;
import com.ygg.admin.entity.yw.YwRewardEntity;

/***
 * 燕网Dao实现类
 * @author Qiu,Yibo
 *
 * 2016年5月12日
 */
@Repository("ywCashDao")
public class YwCashDaoImpl extends BaseDaoImpl implements YwCashDao {
    
    @Override
    public List<Map<String, Object>> findListInfo(Map<String, Object> param)
        throws Exception{
        return getSqlSessionRead().selectList("YwCashMapper.findListByParam", param);
    }
    
    
    @Override
    public int getCountByParam(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionRead().selectOne("YwCashMapper.getCountByParam", param);
    } 
     
    @Override
    public int updateLog(Map<String, Object> param)
    {
        return getSqlSession().update("YwCashMapper.updateLog", param);
    }
    
    public int updateYwReward(YwRewardEntity ywRewardEntity){
        return this.getSqlSession().update("YwCashMapper.updateYwReward", ywRewardEntity);
    }
    
    /**
     * 获取用户奖励信息
     * @param accountId
     * @return
     * @throws Exception
     */
    public YwRewardEntity getByAccountId(int accountId){
        YwRewardEntity ae = this.getSqlSession().selectOne("YwCashMapper.getByAccountId", accountId);
        return ae;
    }
    
    /**
     * 获取用户奖励信息
     * @param accountId
     * @return
     * @throws Exception
     */
    public Map<String, Object> getInfoByLogId(int logId){
        return this.getSqlSession().selectOne("YwCashMapper.getInfoByLogId", logId);
    }
}
