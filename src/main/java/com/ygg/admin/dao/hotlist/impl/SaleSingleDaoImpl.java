
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
package com.ygg.admin.dao.hotlist.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.hotlist.SaleSingleDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.hotlist.SaleSingleProductEntity;

/**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: SaleSingleDaoImpl.java 9145 2016-03-22 08:20:35Z zhanglide $   
  * @since 2.0
  */
@Repository("saleSingleDao")
public class SaleSingleDaoImpl extends BaseDaoImpl implements SaleSingleDao {
	@Override
    public List<Map<String, Object>> findListInfo(Map<String, Object> param)
        throws Exception{
        return getSqlSessionRead().selectList("SaleSingleMapper.findListByParam", param);
    }
	@Override
    public int getCountByParam(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionRead().selectOne("SaleSingleMapper.getCountByParam", param);
    } 
	 
	@Override
    public int save(Map<String, Object> param)
        throws Exception
    {
        return getSqlSession().insert("SaleSingleMapper.save", param);
    }
    
    @Override
    public int update(Map<String, Object> param)
        throws Exception
    {
        return getSqlSession().update("SaleSingleMapper.update", param);
    } 
    @Override
    public int delete(int id) throws Exception{
        return getSqlSession().delete("SaleSingleMapper.delete", id);
    }
    /**
     * 获取热卖商品信息
     * @param para
     * @return List<SaleSingleProduct>
     * @throws Exception 异常时
     */
    public SaleSingleProductEntity getProductInfo(Map<String, Object> para)throws Exception{
        return this.getSqlSession().selectOne("SaleSingleMapper.getProductInfo", para);
    }
}
