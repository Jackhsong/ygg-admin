package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.SaleWindowEntity;

public interface SaleWindowServcie
{
    
    int save(SaleWindowEntity saleWindow)
        throws Exception;
    
    int update(SaleWindowEntity saleWindow)
        throws Exception;
    
    int countSaleWindow(Map<String, Object> para)
        throws Exception;
    
    List<SaleWindowEntity> findAllSaleWindow(Map<String, Object> para)
        throws Exception;
    
    SaleWindowEntity findSaleWindowById(int id)
        throws Exception;
    
    /**
     * 根据不同的特卖计算总库存
     * 
     * @param type 特卖类型
     * @param displayId 相应ID
     * @return
     * @throws Exception
     */
    int countStock(int type, int displayId)
        throws Exception;
    
    int updateDisplayCode(Map<String, Object> para)
        throws Exception;
    
    /**
     * 更新特卖排序值
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int updateOrder(Map<String, Object> para)
        throws Exception;
    
    /**
     * 封装数据 SaleWindowEntity --> map
     * 
     * @param dataList
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> packageSaleWindowList(List<SaleWindowEntity> saleWindowList, int type, int running)
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
     * 隐藏特卖
     * @param para
     * @return
     */
    int hideSaleWindow(Map<String, Object> para)
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
     * 手动更新明日排序值
     * @return
     * @throws Exception
     */
    int resetTomorrowOrder()
        throws Exception;
    
    /**
     * 查找首页特卖列表
     * @param start
     * @param max
     * @param saleStatus：特卖状态，-1：所有；1：即将开始；2：进行中（早场和晚场），3：已结束
     * @param saleName：特卖名称
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
    String jsonSaleWindows(int page, int rows, int saleStatus, String saleName, int categoryFirstId, int productId, String productName, int brandId, int sellerId, int type,
        int isDisplay, String startTime)
        throws Exception;
    
    /**
     * 根据商品Id检查是否该商品是否被其他特卖关联（只针对单品特卖）
     * @param id
     * @param productId 
     * @return
     * @throws Exception
     */
    String checkSaleWindowRelation(int id, int productId)
        throws Exception;
    
}
