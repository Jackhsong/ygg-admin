
 /**************************************************************************
 * Copyright (c) 2014-2016 浙江格家网络技术有限公司.
 * All rights reserved.
 * 
 * 项目名称：左岸城堡APP
 * 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.ygg.admin.dao.qqbs;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.qqbs.QqbsAccountIdentity;

/**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: QqbsAccountUpgradeLogDao.java 11796 2016-05-13 08:31:45Z zhanglide $   
  * @since 2.0
  */
public interface QqbsAccountUpgradeLogDao
{
    public List<Map<String, Object>> findListInfo(Map<String, Object> param);
    
    public int getCountByParam(Map<String, Object> param);
     
    
    public int updateLog(Map<String, Object> param);
    
    /**
     * 获取用户奖励信息
     * @param accountId
     * @return
     * @throws Exception
     */
    public Map<String, Object> getInfoByLogId(int logId);
    
    
    /**
     * 获取直属代言人的ID
     * @param accountId
     * @return
     */
    public List<Integer> getDirectlyUnderSpokesperson(int accountId);
    /**
     * 删除直属代言人
     * @param param
     * @return
     */
    public int deleteDirectlyUnderSpokesperson(Map<String, Object> param);
    /**
     * 获取团队成员的ID
     * @param accountId
     * @return
     */
    public List<Integer> getTeam(int accountId);
    /**
     * 删除团队成员
     * @param param
     * @return
     */
    public int deleteTeam(Map<String, Object> param);
    
    /**
     * 插入用户身份表
     * @param accountId
     * @return
     */
    public int insertAccountIdentity(QqbsAccountIdentity accountIdentity);
    
    /**
     * 更新用户身份表
     * @param param
     * @return
     */
    public int updateAccountIdentity(Map<String, Object> param);
}
