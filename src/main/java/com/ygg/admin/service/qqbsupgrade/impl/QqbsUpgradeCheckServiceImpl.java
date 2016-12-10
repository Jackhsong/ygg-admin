
 /**************************************************************************
 * Copyright (c) 2014-2016 浙江格家网络技术有限公司.
 * All rights reserved.
 * 
 * 项目名称：左岸城堡APP
 * 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.ygg.admin.service.qqbsupgrade.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ygg.admin.dao.qqbs.QqbsAccountUpgradeLogDao;
import com.ygg.admin.entity.qqbs.QqbsAccountIdentity;
import com.ygg.admin.service.qqbsupgrade.QqbsUpgradeCheckService;

/**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: QqbsUpgradeCheckServiceImpl.java 11797 2016-05-13 08:32:00Z zhanglide $   
  * @since 2.0
  */
@Service("qqbsUpgradeCheckService")
public class QqbsUpgradeCheckServiceImpl implements QqbsUpgradeCheckService
{
    /**    */
    @Resource(name="qqbsAccountUpgradeLogDao")
    private QqbsAccountUpgradeLogDao qqbsAccountUpgradeLogDao;
    
    
    @Override
    public Map<String, Object> findListInfo(Map<String, Object> param)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = qqbsAccountUpgradeLogDao.findListInfo(param);
        for (Map<String, Object> map : infoList)
        {
            map.put("forceTime", ((Timestamp)map.get("forceTime")).toString());
        }
        result.put("rows", infoList);
        result.put("total", qqbsAccountUpgradeLogDao.getCountByParam(param));
        return result;
    }
    
    
    /**
     * @param accountId
     * @param id
     * @param type 1打款 2拒绝
     * @throws Exception 
     * @see com.ygg.admin.service.qqbscash.QqbsCashService#updateLog(int, int, int)
     */
    public String updateLog(int id , int type){
        Map<String, Object> info = qqbsAccountUpgradeLogDao.getInfoByLogId(id);
        int accountId = Integer.valueOf(info.get("accountId")+"");
        int status = Integer.valueOf(info.get("status")+"");
        //申请身份：0经理 1总监
        int flag = Integer.valueOf(info.get("flag")+"");
        //生效时间
        String forceTime = info.get("forceTime")+"";
        if(status == 0){
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("id", id);
            if(type == 1){
                //通过
                param.put("status", "1");
                param.put("exStatus", "1");
                if(flag == 0){
                    QqbsAccountIdentity accountIdentity = new QqbsAccountIdentity();
                    accountIdentity.setAccountId(accountId);
                    accountIdentity.setManager(1);
                    accountIdentity.setManagerTime(forceTime);
                    accountIdentity.setDirector(0);
                    accountIdentity.setDirectorTime("0000-00-00 00:00:00");
                    accountIdentity.setStatus(1);
                    accountIdentity.setForceTime(forceTime);
                    qqbsAccountUpgradeLogDao.insertAccountIdentity(accountIdentity);
                }else if(flag == 1){
                    Map<String, Object> param1 = new HashMap<String, Object>();
                    param1.put("accountId", accountId);
                    param1.put("director", 1);
                    param1.put("directorTime", forceTime);
                    qqbsAccountUpgradeLogDao.updateAccountIdentity(param1);
                }
            }else if(type == 2){
                //拒绝
                param.put("status", "2");
            }
            qqbsAccountUpgradeLogDao.updateLog(param);
        }else{
            return "该记录已经审核过,不再处理";
        }
        return null;
    }
}
