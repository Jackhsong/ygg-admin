
 /**************************************************************************
 * Copyright (c) 2014-2016 浙江格家网络技术有限公司.
 * All rights reserved.
 * 
 * 项目名称：左岸城堡APP
 * 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.ygg.admin.dao.yw.error.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.dao.yw.error.YwFansDao;
import com.ygg.admin.entity.qqbs.QqbsFansEntity;
import com.ygg.admin.entity.yw.YwFansEntity;

/**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: QqbsFansDaoImpl.java 10347 2016-04-16 07:27:17Z zhanglide $   
  * @since 2.0
  */
@Repository("ywFansDao")
public class YwFansDaoImpl extends BaseDaoImpl implements YwFansDao
{
    
    public int insertFans(YwFansEntity fans){
        return this.getSqlSession().insert("YwFansMapper.insertFans", fans);
    }
    
    public List<YwFansEntity> getFansList(Map<String, Object> para){
        return this.getSqlSession().selectList("YwFansMapper.getFansList", para);
    }

}