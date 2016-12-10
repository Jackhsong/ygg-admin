/**************************************************************************
* Copyright (c) 2014-2016 浙江格家网络技术有限公司.
* All rights reserved.
* 
* 项目名称：左岸城堡APP
* 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
*           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
*           识产权保护的内容。                            
***************************************************************************/
package com.ygg.admin.service.categorysale;

import java.util.Map;

import com.ygg.admin.entity.SaleWindowEntity;

/**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: CategorySaleService.java 9402 2016-03-30 01:55:10Z xiongliang $   
  * @since 2.0
  */
public interface CategorySaleService
{
    int save(SaleWindowEntity saleWindow)
        throws Exception;
    
    int update(SaleWindowEntity saleWindow)
        throws Exception;
    
    SaleWindowEntity findSaleWindowById(int id)
        throws Exception;
    
    int updateDisplayCode(Map<String, Object> para)
        throws Exception;
    
    /**
     * 检查特卖是否还有关联商品为设置时间
     * 
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> checkProductTime(Map<String, Object> para)
        throws Exception;
    
    /**
     * 检查单品或组合特卖是否存在
     * @param para
     * @return
     * @throws Exception
     */
    boolean checkIsExist(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查找品类馆特卖
     * @param start
     * @param max
     * @param saleStatus：特卖状态，-1：所有；1：即将开始；2：进行中（早场和晚场），3：已结束
     * @param saleName：特卖名称
     * @param page2ModelId：原生自定义页面2模块id
     * @param categoryFirstId：一级分类Id
     * @param productId：商品Id
     * @param productName：商品名称
     * @param brandId：品牌Id
     * @param sellerId：商家Id
     * @param type：特卖类型，-1：全部；1：单品；2：组合；3：自定义活动，4：原生自定义页面
     * @param isDisplay：是否展现，-1：全部，0：不展现，1：展现
     * @param startTime：特卖开售档期
     * @return
     */
    String findCategorySaleWindow(int start, int max, int saleStatus, String saleName, int page2ModelId, int categoryFirstId, int productId, String productName, int brandId,
        int sellerId, int type, int isDisplay, String startTime)
        throws Exception;
    
    /**
     * 修改品类馆特卖排序
     * @param id
     * @param categoryOrder
     * @param categoryLockIndex
     * @return
     * @throws Exception
     */
    String updateCategoryOrderAndIndex(int id, int categoryOrder, int categoryLockIndex)
        throws Exception;
    
    /**
     * 删除品类馆特卖
     * @param id
     * @return
     * @throws Exception
     */
    String deleteCategorySale(int id)
        throws Exception;
}
