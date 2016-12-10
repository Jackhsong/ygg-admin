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
package com.ygg.admin.dao.categorysale;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.SaleWindowEntity;

/**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: CategorySaleDao.java 9402 2016-03-30 01:55:10Z xiongliang $   
  * @since 2.0
  */
public interface CategorySaleDao
{
    int save(SaleWindowEntity saleWindow)
        throws Exception;
    
    int update(SaleWindowEntity saleWindow)
        throws Exception;
    
    SaleWindowEntity findSaleWindowById(int id)
        throws Exception;
    
    int updateDisplayCode(Map<String, Object> para)
        throws Exception;
    
    List<SaleWindowEntity> findCategorySaleWindowByPara(Map<String, Object> params)
        throws Exception;
    
    int countCategorySaleWindowByPara(Map<String, Object> params)
        throws Exception;
    
    int deleteCategorySale(int id)
        throws Exception;
    
    List<SaleWindowEntity> findCategorySaleWindowGreatLockIndex(int id, int categoryLockIndex, int page2ModelId)
        throws Exception;
    
    int updateCategoryLockIndex(List<SaleWindowEntity> swes)
        throws Exception;
}
