
 /**************************************************************************
 * Copyright (c) 2014-2016 浙江格家网络技术有限公司.
 * All rights reserved.
 * 
 * 项目名称：左岸城堡
APP
 * 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.ygg.admin.dao.qqbs.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.dao.qqbs.QqbsAccountUpgradeLogDao;
import com.ygg.admin.entity.qqbs.QqbsAccountIdentity;

/**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: QqbsAccountUpgradeLogDaoImpl.java 11796 2016-05-13 08:31:45Z zhanglide $   
  * @since 2.0
  */
@Repository("qqbsAccountUpgradeLogDao")
public class QqbsAccountUpgradeLogDaoImpl extends BaseDaoImpl implements QqbsAccountUpgradeLogDao
{
    @Override
    public List<Map<String, Object>> findListInfo(Map<String, Object> param){
        return getSqlSessionRead().selectList("QqbsAccountUpgradeLogMapper.findListByParam", param);
    }
    @Override
    public int getCountByParam(Map<String, Object> param)
    {
        return getSqlSessionRead().selectOne("QqbsAccountUpgradeLogMapper.getCountByParam", param);
    } 
     
    
    @Override
    public int updateLog(Map<String, Object> param)
    {
        return getSqlSession().update("QqbsAccountUpgradeLogMapper.updateLog", param);
    }
    
    /**
     * 获取信息
     * @param accountId
     * @return
     * @throws Exception
     */
    public Map<String, Object> getInfoByLogId(int logId){
        return this.getSqlSession().selectOne("QqbsAccountUpgradeLogMapper.getInfoByLogId", logId);
    }
    
    
    
    /**
     *  获取直属代言人的ID
     * @param accountId
     * @return
     * @see com.ygg.admin.dao.qqbs.QqbsAccountUpgradeLogDao#getDirectlyUnderSpokesperson(int)
     */
    @Override
    public List<Integer> getDirectlyUnderSpokesperson(int accountId)
    {
        return this.getSqlSession().selectList("QqbsAccountUpgradeLogMapper.getDirectlyUnderSpokesperson", accountId);
    }
    
    /**
     * 删除直属代言人
     * @param param
     * @return
     * @see com.ygg.admin.dao.qqbs.QqbsAccountUpgradeLogDao#updateLog(java.util.Map)
     */
    @Override
    public int deleteDirectlyUnderSpokesperson(Map<String, Object> param)
    {
        return getSqlSession().update("QqbsAccountUpgradeLogMapper.deleteDirectlyUnderSpokesperson", param);
    }
    
    /**
     * 获取团队成员的ID
     * @param accountId
     * @return
     * @see com.ygg.admin.dao.qqbs.QqbsAccountUpgradeLogDao#getTeam(int)
     */
    @Override
    public List<Integer> getTeam(int accountId)
    {
        return this.getSqlSession().selectList("QqbsAccountUpgradeLogMapper.getTeam", accountId);
    }
    /**
     * 删除团队成员
     * @param param
     * @return
     * @see com.ygg.admin.dao.qqbs.QqbsAccountUpgradeLogDao#updateLog(java.util.Map)
     */
    @Override
    public int deleteTeam(Map<String, Object> param)
    {
        return getSqlSession().update("QqbsAccountUpgradeLogMapper.deleteTeam", param);
    }
    
    /**
     * 插入用户身份表
     * @param accountId
     * @return
     */
    public int insertAccountIdentity(QqbsAccountIdentity accountIdentity){
        return this.getSqlSession().insert("QqbsAccountUpgradeLogMapper.insertAccountIdentity", accountIdentity);
    }
    
    /**
     * 更新用户身份表
     * @param param
     * @return
     * @see com.ygg.admin.dao.qqbs.QqbsAccountUpgradeLogDao#updateAccountIdentity(java.util.Map)
     */
    @Override
    public int updateAccountIdentity(Map<String, Object> param)
    {
        return getSqlSession().update("QqbsAccountUpgradeLogMapper.updateAccountIdentity", param);
    }
}
