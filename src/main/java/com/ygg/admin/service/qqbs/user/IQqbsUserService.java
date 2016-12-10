package com.ygg.admin.service.qqbs.user;

import java.util.Map;

import com.ygg.admin.entity.qqbs.QqbsAccountEntity;

/**
 * @author wuhy
 * @date 创建时间：2016年5月16日 上午9:30:27
 */
public interface IQqbsUserService
{
    
    Map<String, Object> findQqbsUsersAndCount(QqbsAccountEntity qqbsAccountEntity);
}
