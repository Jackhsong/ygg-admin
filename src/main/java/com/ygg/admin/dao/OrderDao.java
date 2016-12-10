package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.alibaba.fastjson.JSONObject;
import com.ygg.admin.entity.*;
import com.ygg.admin.exception.DaoException;
import com.ygg.admin.view.ClientBuyView;

public interface OrderDao
{
    
    /**
     * 根据para查询订单信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<OrderEntity> findOrder(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据number查询订单
     * 
     * @param number
     * @return
     * @throws Exception
     */
    OrderEntity findOrderByNumber(String number)
        throws Exception;

    /**
     * 根据number查询订单
     *
     * @param number
     * @return
     * @throws Exception
     */
    OrderEntity findOrderByNumber(long number)
            throws Exception;
    
    /**
     * 根据ID查询订单
     * 
     * @param id
     * @return
     * @throws Exception
     */
    OrderEntity findOrderById(int id)
        throws Exception;
    
    /**
     * 根据para更新订单信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int updateOrder(Map<String, Object> para)
        throws Exception;
    
    /**
     * 更新订单收货地址
     * 
     * @return
     * @throws Exception
     */
    int updateOrderAddress(ReceiveAddressEntity entity)
        throws Exception;
    
    /**
     * 根据para更新订单导出状态信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int updateOrderOperationStatus(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据para查询订单数量
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int countOrder(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据收货地址信息查询订单ids查询
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<Integer> findOrderIdsByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 插入订单物流信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int saveOrderLogistics(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据订单ID查询物流信息
     * 
     * @param id
     * @return
     * @throws Exception
     */
    Map<String, Object> findOrderLogisticsByOrderId(int id)
        throws Exception;
    
    /**
     * 根据channel，number查询物流信息
     * 
     * @return
     * @throws Exception
     */
    OrderLogistics findOrderLogisticsBychannelAndNumber(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查询物流信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<OrderLogistics> findOrderLogistics(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改订单物流信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int updateOrderLogistics(Map<String, Object> para)
        throws Exception;
    
    /**
     * 插入订单物流详细信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int saveLogisticsDetail(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计订单物流详细信息数量
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int countLogisticsDetail(Map<String, Object> para)
        throws Exception;
    
    /**
     * 删除订单物流详细信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int deleteLogisticsDetail(Map<String, Object> para)
        throws Exception;
    
    /**
     * 是否存在相同的物流详细信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int existsLogisticsDetail(Map<String, Object> para)
        throws Exception;
    
    /**
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllLogisticsDetail(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查询所有订单商品信息
     * 
     * @param id
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllOrderProductInfo(int id)
        throws Exception;
    
    /**
     * 根据id查询订单商品信息
     * 
     * @param id
     * @return
     * @throws Exception
     */
    Map<String, Object> findOrderProductById(int id)
        throws Exception;
    
    /**
     * 根据orderId查询订单商品信息
     * 
     * @param orderId
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findOrderProductByOrderId(int orderId)
        throws Exception;
    
    /**
     * 更新订单商品信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int updateOrderProduct(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查询订单列表信息信息
     * 
     * @param map
     * @return
     * @throws Exception
     */
    //    List<OrderInfoForManage> findAllOrderInfoForManage(Map<String, Object> map)
    //        throws Exception;
    
    /**
     * 统计订单数量
     * 
     * @param map
     * @return
     * @throws Exception
     */
    //    int countAllOrderInfoForManage(Map<String, Object> map)
    //        throws Exception;
    
    /**
     * 查询订单详细信息给商家发货
     * 
     * @param map
     * @return
     * @throws Exception
     */
    List<OrderDetailInfoForSeller> findAllOrderInfoForSeller(Map<String, Object> map)
        throws Exception;
    
    /**
     * 查询结算订单详细信息给商家发货
     * 
     * @param map
     * @return
     * @throws Exception
     */
    List<OrderDetailInfoForSeller> findSellerUnSettleOrders(Map<String, Object> map)
        throws Exception;
    
    /**
     * 统计 时间段 内未发货订单 信息 根据商家分组
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllUnSendGoodInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 超24小时未发货明细
     * 
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllUnSendGoodsDetail(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计 超24小时未发货明细 数量
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int countAllUnSendGoodsDetail(Map<String, Object> para)
        throws Exception;
    
    // --------------------begin------------------------订单发货记录--------------------------------------------
    
    /** 插入订单发货记录 */
    int insertOrderSendRecord(Map<String, Object> para)
        throws Exception;
    
    /** 根据OrderId查询订单发货记录 */
    Map<String, Object> findOrderSendRecordByOrderId(Integer orderId)
        throws Exception;
    
    /** 根据para查询订单发货记录 */
    List<Map<String, Object>> findAllOrderSendRecordInfo(Map<String, Object> para)
        throws Exception;
    
    /** 更新订单发货记录 */
    int updateOrderSendRecord(Map<String, Object> para)
        throws Exception;
    
    // --------------------end------------------------订单发货记录--------------------------------------------
    
    Map<String, Object> findReceiveInfoById(int id)
        throws Exception;
    
    /**
     * 订单数据查询，，用来月度数据统计
     */
    List<Map<String, Object>> findOrderSalesRecord(Map<String, Object> para)
        throws Exception;
    
    /**
     * 判断订单是否是海外购订单
     * 
     * @param orderId
     * @return
     * @throws Exception
     */
    boolean checkOrderIsOverseasOrder(int orderId)
        throws Exception;
    
    /**
     * 查询 订单渠道 list
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllOrderChannel(Map<String, Object> para)
        throws Exception;
    
    /**
     * count 订单渠道
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int countOrderChannel(Map<String, Object> para)
        throws Exception;
    
    /**
     * 插入订单渠道信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int saveOrderChannel(Map<String, Object> para)
        throws Exception;
    
    /**
     * 更新订单渠道信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int updateOrderChannel(Map<String, Object> para)
        throws Exception;
    
    /**
     * 删除订单渠道信息
     * 
     * @param id
     * @return
     * @throws Exception
     */
    int deleteOrderChannel(int id)
        throws Exception;
    
    /**
     * 根据渠道来源统计订单
     * 
     * @param id
     * @return
     * @throws Exception
     */
    int countOrderBySourceChannelId(int id)
        throws Exception;
    
    /**
     * 根据订单Id查询商品
     * 
     * @param id
     * @return
     */
    List<Integer> findProductIdListByOrderId(int id)
        throws Exception;
    
    /**
     * 查询今日在售相关订单商品记录信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllTodaySaleRelOrderProduct(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查询今日在售左岸城堡
订单商品记录信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findQqbsAllTodaySaleRelOrderProduct(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据productIdList查询商品库存
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findProductStockAndNameByProductIdList(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据订单ID查询订单商品总价
     * 
     * @param orderId
     * @return
     * @throws Exception
     */
    String sumOrderPrice(Integer orderId)
        throws Exception;
    
    List<Map<String, Object>> findAccountSpending()
        throws Exception;
    
    /**
     * 查询假单列表
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> queryAllFakeOrder(Map<String, Object> para)
        throws Exception;
    
    int countAllFakeOrder(Map<String, Object> para)
        throws Exception;
    
    /**
     * 假单重新订阅
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int orderLogisticsAgain(Map<String, Object> para)
        throws Exception;
    
    /**
     * 更新订单的结算状态
     * 
     * @param para
     * @return
     */
    int updateOrderSettlement(Map<String, Object> para)
        throws Exception;
    
    /**
     * 更新订单是否需要结算
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int updateOrderIsNeedSettlement(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据订单Id查询商品id和name
     * 
     * @param id
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findProductInfoByOrderId(int id)
        throws Exception;
    
    /**
     * 查询所有冻结订单
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllOrderFreeze(Map<String, Object> para)
        throws Exception;
    
    /**
     * count 所有冻结订单
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int countOrderFreeze(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查询订单冻结记录
     * 
     * @return
     * @throws DaoException
     */
    Map<String, Object> findOrderFreezeByOrderId(int id)
        throws DaoException;
    
    /**
     * 插入订单冻结表
     *
     * @param orderId
     * @return
     * @throws DaoException
     */
    int insertOrderFreeze(int orderId)
        throws DaoException;
    
    /**
     * 根据订单id修改订单锁定状态
     *
     * @return
     */
    int updateOrderFreeze(int orderId, int lock)
        throws DaoException;
    
    /**
     * 更新订单冻结记录
     * 
     * @param para
     * @return
     * @throws DaoException
     */
    int updateOrderFreezeRecord(Map<String, Object> para)
        throws DaoException;
    
    /**
     * 更新订单冻结记录
     *
     * @return
     * @throws DaoException
     */
    int updateOrderFreezeRecord(int orderFreezeRecordId)
        throws DaoException;
    
    /**
     * 查询解冻时间段内所有冻结订单ID
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<Integer> findFreezeOrderIdByPara(Map<String, Object> para)
        throws Exception;
    
    List<Integer> findOrderAccountIdByPara(Map<String, Object> para)
        throws Exception;
    
    int findOrderIdByNumber(long number)
        throws Exception;
    
    Map<String, Object> findOrderSettlementByNumber(long number)
        throws Exception;
    
    int insertOrderSettlement(Map<String, Object> para)
        throws Exception;
    
    int deleteOrderSettlement(int orderId)
        throws Exception;
    
    int updateOrderProductCost(Map<String, Object> para)
        throws Exception;
    
    String findPayTidOrderAliPay(int orderId)
        throws Exception;
    
    String findPayMarkTidOrderAliPay(int orderId)
        throws Exception;
    
    String findPayMarkTidOrderWeixinPay(int orderId)
        throws Exception;
    
    String findPayTidOrderWeixinPay(int orderId)
        throws Exception;
    
    String findPayTidOrderUnionPay(int orderId)
        throws Exception;
    
    List<Map<String, Object>> findAllBirdexOrder(Map<String, Object> para)
        throws Exception;
    
    int countAllBirdexOrder(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findAllBirdexOrderWithOutExportInfo()
        throws Exception;
    
    /**
     * 查询所有笨鸟商品 导出信息 for 列表
     * 
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllBirdexProductInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计笨鸟商品 导出信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int countAllBirdexProductInfo(Map<String, Object> para)
        throws Exception;
    
    int updateBirdexProInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 插入笨鸟商品信息表记录
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int insertBirdexProInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 删除海外购商品导出信息
     * 
     * @return
     * @throws Exception
     */
    int deleteBirdexProductInfoById(int id)
        throws Exception;
    
    /**
     * 插入笨鸟订单确认发货记录
     * 
     * @param orderId
     * @return
     * @throws Exception
     */
    int insertBirdexOrderConfirm(int orderId)
        throws Exception;
    
    boolean alreadyConfirmOrderByOrderId(int orderId)
        throws Exception;
    
    Map<String, Object> findIdcardRealnameMappingByIdCard(String idCard)
        throws Exception;
    
    List<Map<String, Object>> findOrderProductSettlementInfo(List<Integer> orderIdList)
        throws Exception;
    
    /**
     * 客户端成交统计
     * 
     * @param searchPara
     * @return
     */
    List<ClientBuyView> clientBuyAnalyze(Map<String, Object> searchPara)
        throws Exception;
    
    /**
     * 用户购买行为统计
     * 
     * @param para
     * @return
     */
    List<Map<String, Object>> userBehaviorAnalyze(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计0次购买用户
     * 
     * @param searchPara
     * @return
     * @throws Exception
     */
    int countZeroBuyUser(Map<String, Object> searchPara)
        throws Exception;
    
    /**
     * 查找前段时间未购买段时间购买的用户
     * 
     * @param para
     * @return
     */
    Map<String, Object> countNextBuyUser(Map<String, String> para)
        throws Exception;
    
    /**
     * 0次购买用户Id
     * 
     * @param searchPara
     * @return
     */
    List<Integer> findZeroBuyAccount(Map<String, Object> searchPara)
        throws Exception;
    
    /**
     * 根据订单Id查找收货人手机号
     * 
     * @param orderId
     * @return
     * @throws Exception
     */
    String findReceiveMobileNumberByOrderId(String orderId)
        throws Exception;
    
    /**
     * 根据订单Id查找商品信息
     * 
     * @param orderId
     * @return
     */
    List<Map<String, Object>> findProductNameAndTypeByOrderId(String orderId)
        throws Exception;
    
    List<Map<String, Object>> findSaleDataByDate(Map<String, Object> para)
        throws Exception;
    
    /**
     * 左岸城堡
数据魔方
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findQqbsSaleDataByDate(Map<String, Object> para)
            throws Exception;
    
    /**
     * 根据合并订单号查询子订单号
     *
     * @param number
     * @return
     * @throws Exception
     */
    String findHBOrderByParentNumber(String number)
        throws Exception;
    
    /**
     * 根据物流公司名称和物流单号查找所有订单物流信息
     * 
     * @param searchMap
     * @return
     * @throws Exception
     */
    List<OrderLogistics> findAllOrderLogisticsBychannelAndNumber(Map<String, String> searchMap)
        throws Exception;
    
    /**
     * 根据订单ID查询记录
     * 
     * @param orderId
     * @return
     * @throws Exception
     */
    int findOrderSignRecordIdByOrderId(int orderId)
        throws Exception;
    
    /**
     * 插入待发送已签收订单记录
     * 
     * @param orderId
     * @return
     * @throws Exception
     */
    int addOrderSignRecordIdByOrderId(int orderId)
        throws Exception;
    
    /**
     * 查询付款订单信息（每日订单发货时效统计）
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findOrderPayRecordForSendTimeAnalyze(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查询订单发货信息（每日发货后有物流信息时效统计）
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findOrderPayRecordForLogisticAnalyze(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据物流公司和物流编码查找订单物流创建时间
     * 
     * @param channel
     * @param number
     * @return
     * @throws Exception
     */
    DateTime finOrderLogisticsTimeByChannelAndNumber(String channel, String number)
        throws Exception;
    
    List<OrderInfoForManage> findAllOrderSendTimeAnalyze(Map<String, Object> para)
        throws Exception;
    
    int countAllOrderSendTimeAnalyze(Map<String, Object> para)
        throws Exception;
    
    List<OrderInfoForManage> findAllOrderLogisticAnalyzeDetail(Map<String, Object> para)
        throws Exception;
    
    int countAllOrderLogisticAnalyzeDetail(Map<String, Object> para)
        throws Exception;
    
    List<Integer> findOldBuyAccountIds(String payTime)
        throws Exception;
    
    List<Map<String, Object>> userFirstBehaviorAnalyze(Map<String, Object> searchPara)
        throws Exception;
    
    int countFirstZeroBuyUser(Map<String, Object> searchPara)
        throws Exception;
    
    Map<String, Object> countFirstNextBuyUser(Map<String, Object> seach)
        throws Exception;

    /**
     * 左岸城堡
销量统计
     */
    List<Map<String, Object>> findOrderSalesRecordForMonthAnalyze(Map<String, Object> para)
        throws Exception;

    /**
     * 全平台销量统计
     */
    List<Map<String, Object>> findOrderSalesRecordForPlatformMonthAnalyze(Map<String, Object> para)
            throws Exception;
    
    /**
     * 根据para-app_channel=28查询左岸城堡
订单信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findOrderSalesRecordForQqbsMonthAnalyze(Map<String, Object> para)
            throws Exception;
        
    /**
     * 根据para-type = 5 查询燕网订单信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findOrderSalesRecordForYWMonthAnalyze(Map<String, Object> para) 
            throws Exception;
    
    /**
     * 根据para查询订单简要信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findSimpleOrderInfoByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据para查询订单商品信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllOrderProductInfoByPara(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findAllProblemOrder(Map<String, Object> para)
        throws Exception;
    
    int countAllProblemOrder(Map<String, Object> para)
        throws Exception;
    
    /**
     * 批量插入外部订单合并记录
     * 
     * @param insertList
     * @return
     * @throws Exception
     */
    int batchInsertOutOrderHbRecord(List<Map<String, Object>> insertList)
        throws Exception;
    
    /**
     * 根据合并订单号列表查询记录
     * 
     * @param numberList
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findOutOrderHbRecordByHbNumberList(List<Long> numberList)
        throws Exception;
    
    /**
     * 根据优惠券ID查询关联的订单信息
     * 
     * @param couponAccountId
     * @return
     * @throws Exception
     */
    List<Integer> findOrderIdByCouponAccountId(int couponAccountId)
        throws Exception;
    
    /**
     * 根据手机号和收货人名称查询收货地址ID
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<Integer> findOrderReceiveAddressIdListByFullNameAndPhone(Map<String, Object> para)
        throws Exception;
    
    List<Integer> findOrderIdListByOrderLogisticsNumber(String number)
        throws Exception;

    List<Map<String, Object>> findAllOrderInfoForList(Map<String, Object> para)
        throws Exception;
    
    int countAllOrderInfoForList(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findAllOrderInfoForList2(Map<String, Object> para)
        throws Exception;
    
    int countAllOrderInfoForList2(Map<String, Object> para)
        throws Exception;
    
    List<Integer> findAllOrderIdList(Map<String, Object> para)
        throws Exception;
    
    List<Integer> findAllOrderIdList2(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findReceiveInfoByIdList(List<Integer> list)
        throws Exception;
    
    List<Map<String, Object>> findSourceChannelInfoByIdList(List<Integer> list)
        throws Exception;
    
    List<Map<String, Object>> findLogisticsInfoByIdList(List<Integer> list)
        throws Exception;
    
    List<Map<String, Object>> find_PayMarkTidOrderAliPay(int orderId)
        throws Exception;
    
    List<Map<String, Object>> find_PayMarkTidOrderWeixinPay(int orderId)
        throws Exception;
    
    List<Map<String, Object>> find_PayTidOrderUnionPay(int orderId)
        throws Exception;
    
    OrderEntity getOrderById(int id);
    
    List<JSONObject> findOrderAliPay(JSONObject j);
    
    List<JSONObject> findOrderWeixinPay(JSONObject j);
    
    List<JSONObject> findOrderUnionPay(JSONObject j);
    
    int updateBirdexOrderPushStatus(int orderId, int pushStatus)
        throws Exception;
    
    int insertBirdexOrderConfirm(int orderId, int pushStatus)
        throws Exception;
    
    List<Map<String, Object>> findOrderProductCount(Map<String, Object> para)
        throws Exception;
    
    /**
     * 插入E店宝确认记录
     * @param orderId
     * @param isPush
     * @return
     * @throws Exception
     */
    int addOrderEdbConfirm(int orderId, int isPush)
        throws Exception;
    
    /**
     * 查询E店宝确认记录
     * @param orderId
     * @return
     * @throws Exception
     */
    Map<String, Object> findOrderEdbConfirmByOrderId(int orderId)
        throws Exception;
    
    /**
     * 根据订单列表查询E店宝确认记录
     * @param orderIdList
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findOrderEdbConfirmByOrderIdList(List<Integer> orderIdList)
        throws Exception;
    
    /**
     * 查询E店宝订单列表
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findEdbOrder(Map<String, Object> para)
        throws Exception;
    
    /**
     * count E店宝订单
     * @param para
     * @return
     * @throws Exception
     */
    int countEdbOrder(Map<String, Object> para)
        throws Exception;
    
    /**
     * 更新E店宝确认记录
     * @param orderId
     * @param isPush
     * @return
     * @throws Exception
     */
    int updateOrderEdbConfirm(int orderId, int isPush)
        throws Exception;
    
    /**
     * 查询订单ID列表查询订单E店宝推送记录
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findOrderEdbSendRecordByOrderIdList(List<Integer> orderIdList)
        throws Exception;
    
    /**
     * 查询订单ID查询订单E店宝推送记录
     * @param orderId
     * @return
     * @throws Exception
     */
    Map<String, Object> findOrderEdbSendRecordByOrderId(int orderId)
        throws Exception;
    
    List<Map<String, Object>> findPushSuccessEdbOrder(Map<String, Object> para)
        throws Exception;
    
    int countPushSuccessEdbOrder(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findPushErrorEdbOrder(Map<String, Object> para)
        throws Exception;
    
    int countPushErrorEdbOrder(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findWaitPushEdbOrder(Map<String, Object> para)
        throws Exception;
    
    int countWaitPushEdbOrder(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findNoPushEdbOrder(Map<String, Object> para)
        throws Exception;
    
    int countNoPushEdbOrder(Map<String, Object> para)
        throws Exception;
    
    int countPushErrorEdbOrder()
        throws Exception;
    
    List<Map<String, Object>> findOrderCheckListByOrderList(List<Integer> orderIdList)
        throws Exception;
    
    int saveOrderCheck(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据物流公司和物流单号查找最新物流信息
     * @param channel
     * @param number
     * @return
     */
    Map<String, Object> findLastLogisticsDetail(String channel, String number)
        throws Exception;
    
    List<Map<String, Object>> findRefundForEveryday(Map<String, Object> param)
        throws Exception;
    
    List<Map<String, Object>> findCustomerRefundForEveryday(Map<String, Object> param)
        throws Exception;
    
    List<Map<String, Object>> findRefundForSeller(Map<String, Object> param)
        throws Exception;
    
    List<Map<String, Object>> findCustomerRefundForSeller(Map<String, Object> param)
        throws Exception;

    Map<String, Object> findOrderTimeoutSettlementByOrderId(int orderId)
        throws Exception;
        
    int saveOrderTimeoutSettlement(Map<String, Object> para)
        throws Exception;

    int deleteOrderTimeoutSettlement(int orderId)
            throws Exception;

    /**
     * 根据同一批次编号超找订单
     * @param sameBatchNumber：同一批订单编号标识
     * @return
     * @throws Exception
     */
    List<OrderEntity> findOrdersBySameBatchNumber(String sameBatchNumber)
        throws Exception;
        
    /**
     * 根据微信支付payTid查找订单id
     * @param payTid
     * @return
     * @throws Exception
     */
    List<Integer> findOrderIdsByWeiXinPayTid(String payTid)
        throws Exception;

    /**
     * 根据订单id和订单支付方式查找订单支付信息
     * @param orderId
     * @param payType
     * @return
     * @throws Exception
     */
    OrderPayEntity findOrderPayByOrderIdAndPayType(int orderId, int payType)
        throws Exception;

    /**
     * 根据订单支付信息和付款方式查找同以批次付款订单
     * @param payTid
     * @param payType
     * @return
     * @throws Exception
     */
    List<Integer> findOrderIdsByPayTidAndPayType(String payTid, int payType)
        throws Exception;
    
    
    /**
     * 查询今日在售燕网订单商品记录信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findYWAllTodaySaleRelOrderProduct(
            Map<String, Object> para) throws Exception;

    /**
     * 左岸城堡
数据魔方-折线图
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findYWSaleDataByDate(Map<String, Object> para)
            throws Exception;
}
