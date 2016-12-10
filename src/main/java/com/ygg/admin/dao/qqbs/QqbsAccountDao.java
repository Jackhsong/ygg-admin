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

import com.ygg.admin.entity.qqbs.QqbsAccountEntity;
import com.ygg.admin.exception.DaoException;

/**
 * TODO 请在此处添加注释
 * 
 * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
 * @version $Id: QqbsAccountDao.java 12195 2016-05-18 05:52:39Z wuhuyun $
 * @since 2.0
 */
public interface QqbsAccountDao
{
    /**
     * @param account
     * @return
     * @throws DaoException
     */
    int updateAccounSpread(Map<String, Object> para);
    
    /**
     * TODO 请在此处添加注释
     * 
     * @param accountId
     * @return
     * @throws DaoException
     */
    QqbsAccountEntity findAccountByAccountId(int accountId);
    
    List<QqbsAccountEntity> findAccountsByQqbsUserQueryCriteria(QqbsAccountEntity qqbsUserQueryCriteria);
    
    int countQqbsAccountByQqbsUserQueryCriteria(QqbsAccountEntity qqbsUserQueryCriteria);
    
    int addPersistentQRCodeToAccount(QqbsAccountEntity qqbsAccountEntity);
}
