
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
import com.ygg.admin.dao.qqbs.QqbsErrorDao;
import com.ygg.admin.entity.qqbs.QqbsAccountRelaChangeLogEntity;

/**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: QqbsErrorDaoImpl.java 10347 2016-04-16 07:27:17Z zhanglide $   
  * @since 2.0
  */
@Repository("qqbsErrorDao")
public class QqbsErrorDaoImpl extends BaseDaoImpl implements QqbsErrorDao
{
    @Override
    public List<Map<String, Object>> findListInfo(Map<String, Object> param){
        return getSqlSessionRead().selectList("QqbsErrorMapper.findListByParam", param);
    }
    @Override
    public int getCountByParam(Map<String, Object> param)
    {
        return getSqlSessionRead().selectOne("QqbsErrorMapper.getCountByParam", param);
    } 
    public int insert(QqbsAccountRelaChangeLogEntity log){
        return this.getSqlSession().insert("QqbsErrorMapper.insert", log);
    }
}
