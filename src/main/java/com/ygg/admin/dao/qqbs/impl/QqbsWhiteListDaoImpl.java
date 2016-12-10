
 /**************************************************************************
 * Copyright (c) 2014-2016 浙江格家网络技术有限公司.
 * All rights reserved.
 * 
 * 项目名称：左岸城堡APP
 * 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.ygg.admin.dao.qqbs.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.dao.qqbs.QqbsWhiteListDao;

/**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: QqbsWhiteListDaoImpl.java 12350 2016-05-19 08:23:40Z zhanglide $   
  * @since 2.0
  */
@Repository("qqbsWhiteListDao")
public class QqbsWhiteListDaoImpl extends BaseDaoImpl implements QqbsWhiteListDao
{
    @Override
    public List<Map<String, Object>> findListInfo(Map<String, Object> param){
        return getSqlSessionRead().selectList("QqbsWhiteListMapper.findListByParam", param);
    }
    @Override
    public int getCountByParam(Map<String, Object> param)
    {
        return getSqlSessionRead().selectOne("QqbsWhiteListMapper.getCountByParam", param);
    } 
     
    
    @Override
    public int updateLog(Map<String, Object> param)
    {
        return getSqlSession().update("QqbsWhiteListMapper.updateLog", param);
    }
    
    public int save(Map<String, Object> param)
    {
        return getSqlSession().insert("QqbsWhiteListMapper.insert", param);
    }
}
