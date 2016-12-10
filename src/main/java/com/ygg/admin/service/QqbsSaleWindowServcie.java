package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.QqbsSaleWindowEntity;

public interface QqbsSaleWindowServcie
{
    
    int save(QqbsSaleWindowEntity saleWindow)
        throws Exception;
    
    int update(QqbsSaleWindowEntity saleWindow)
        throws Exception;
    
    int countSaleWindow(Map<String, Object> para)
        throws Exception;
    
    List<QqbsSaleWindowEntity> findAllSaleWindow(Map<String, Object> para)
        throws Exception;
    
    QqbsSaleWindowEntity findSaleWindowById(int id)
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
     
    int updateOrder(Map<String, Object> para)
        throws Exception;*/
    
    /**
     * 封装数据 QqbsSaleWindowEntity --> map
     * 
     * @param dataList
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> packageSaleWindowList(List<QqbsSaleWindowEntity> saleWindowList, int type, int running)
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
     * 所有特卖位列表
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> findAllSaleWindowNew(Map<String, Object> para)
        throws Exception;

    int updateOrder(int id, int order);
}
