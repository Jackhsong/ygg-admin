
 /**************************************************************************
 * Copyright (c) 2014-2016 浙江格家网络技术有限公司.
 * All rights reserved.
 * 
 * 项目名称：左岸城堡APP
 * 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.ygg.admin.dao.welfare.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.dao.welfare.CustomGeGeWelfareDao;

/**
  * 格格福利团Dao
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: CustomGeGeWelfareDaoImpl.java 8472 2016-03-07 01:56:22Z zhanglide $   
  * @since 2.0
  */
@Repository("customGeGeWelfareDao")
public class CustomGeGeWelfareDaoImpl extends BaseDaoImpl implements CustomGeGeWelfareDao {

	@Override
    public List<Map<String, Object>> findListInfo(Map<String, Object> param)
        throws Exception{
        return getSqlSessionRead().selectList("WelfareGroupMapper.findListByParam", param);
    }
	@Override
    public int getCountByParam(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionRead().selectOne("WelfareGroupMapper.getCountByParam", param);
    } 
	 
	@Override
    public int save(Map<String, Object> param)
        throws Exception
    {
        return getSqlSession().insert("WelfareGroupMapper.save", param);
    }
    
    @Override
    public int update(Map<String, Object> param)
        throws Exception
    {
        return getSqlSession().update("WelfareGroupMapper.update", param);
    } 
    @Override
    public int delete(int id) throws Exception{
        return getSqlSession().delete("WelfareGroupMapper.delete", id);
    }
}
