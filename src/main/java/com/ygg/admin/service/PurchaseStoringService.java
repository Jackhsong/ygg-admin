package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

public interface PurchaseStoringService
{
    
    /**
     * 根据条件查询库存
     * @param param
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findPurchaseStoringByParam(Map<String, Object> param)
        throws Exception;
    
    /**
     * 
     * @param param
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findPurchaseStoringByIds(List<Object> list)
        throws Exception;
    
    /**
     * 确定采购
     * @param infos
     * @return
     * @throws Exception
     */
    int purchaseOrder(List<Map<String, Object>> infos)
        throws Exception;
    
    /**
     * 更新库存信息
     * @param param
     * @return
     * @throws Exception
     */
    int updatePurchaseStoring(Map<String, Object> param)
        throws Exception;
    
    /**
     * 各渠道生成订单后调用。
     * <br>
     * 参数中包含订单的采购商品ID和数量
     * <pre> 如： {@code
     * List<Map<String, Object>> infos = new ArrayList<Map<String, Object>>();
     * Map<String, Object> param1 = new HashMap<String, Object>();
     * param1.put("providerProductId", 123); //采购商品ID
     * param1.put("usedNumber", 2); //数量
     * 
     * Map<String, Object> param2 = new HashMap<String, Object>();
     * param2.put("providerProductId", 456); //采购商品ID
     * param2.put("usedNumber", 1); //数量
     * 
     * infos.add(param1);
     * infos.add(param2);
     * } </pre>
     * 
     * @param infos
     * @return
     * @throws Exception
     */
    int usedProviderProduct(List<Map<String, Object>> infos)
        throws Exception;
    
    /**
     * 各渠道分配库存
     * <pre> 如： {@code
     * Map<String, Object> info = new HashMap<String, Object>();
     * info.put("providerProductId", 123); //采购商品ID
     * info.put("allocationNumber", 20); //本次分配的库存数量
     * 
     * } </pre>
     * @param info
     * @return
     * @throws Exception
     */
    int usedUnallocationStoring(Map<String, Object> info)
        throws Exception;
    
    /**
     * 统计已付款未推送订单使用库存
     * @return
     * @throws Exception
     */
    Map<String, Object> statisticsUnpush()
        throws Exception;
}
