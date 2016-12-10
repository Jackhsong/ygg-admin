
 /**************************************************************************
 * Copyright (c) 2014-2016 浙江格家网络技术有限公司.
 * All rights reserved.
 * 
 * 项目名称：左岸城堡APP
 * 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.ygg.admin.service.qqbswhitelist.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ygg.admin.dao.qqbs.QqbsWhiteListDao;
import com.ygg.admin.service.qqbswhitelist.QqbsWhiteListService;

/**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: QqbsWhiteListServiceImpl.java 12406 2016-05-20 03:48:16Z zhanglide $   
  * @since 2.0
  */
@Service("qqbsWhiteListService")
public class QqbsWhiteListServiceImpl implements QqbsWhiteListService
{
    /**    */
    @Resource(name="qqbsWhiteListDao")
    private QqbsWhiteListDao qqbsWhiteListDao;
    
    
    @Override
    public Map<String, Object> findListInfo(Map<String, Object> param)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = qqbsWhiteListDao.findListInfo(param);
        for (Map<String, Object> map : infoList)
        {
            map.put("createTime", ((Timestamp)map.get("createTime")).toString());
        }
        result.put("rows", infoList);
        result.put("total", qqbsWhiteListDao.getCountByParam(param));
        return result;
    }
    
    
    /**
     * @param id
     * @param type 
     * @see com.ygg.admin.service.qqbswhitelist.QqbsWhiteListService#updateLog(int, int)
     */
    public void updateLog(int id , int type){
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        param.put("status",type);
        qqbsWhiteListDao.updateLog(param);
    }
    /**
     * @param param
     */
    public void save(Map<String, Object> param){
        qqbsWhiteListDao.save(param);
    }
    
    
    /**
     * 查询用户用没用添加
     * @param param
     * @return
     */
    public int getCountByParam(Map<String, Object> param)
    {
        return qqbsWhiteListDao.getCountByParam(param);
    } 
    
}


