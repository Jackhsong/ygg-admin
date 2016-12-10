package com.ygg.admin.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Workbook;

import com.ygg.admin.entity.OrderDetailInfoForSeller;
import com.ygg.admin.entity.OrderEntity;
import com.ygg.admin.entity.ReceiveAddressEntity;

public interface OrderService
{
    
    /**
     * 根据para查询订单，并封装成json字符串返回
     * 
     * @param para
     * @return
     * @throws Exception
     */
    String jsonOrderInfo(Map<String, Object> para)
        throws Exception;
    
    //    /**
    //     * 根据para查询订单，并封装成json字符串返回
    //     *
    //     * @param para
    //     * @return
    //     * @throws Exception
    //     */
    //    Map<String, Object> jsonOrderInfoTest(Map<String, Object> para)
    //        throws Exception;
    
    /**
     * 订单页面数据查询
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> ajaxOrderInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 获得订单导出总条数
     * @param para
     * @return
     * @throws Exception
     */
    int getExportOrderNums(Map<String, Object> para)
        throws Exception;
    
    /**
     * 导出订单明细
     * @param para
     * @return
     * @throws Exception
     */
    String exportAllStatusOrder(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据para进行订单发货
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int sendOrder(Map<String, Object> para)
        throws Exception;
    
    /**
     * 无物流信息订单发货
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int sendOrderWithOutLogistics(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据para进行合并订单发货
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int sendOrderHB(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据para进行模拟订单发货
     * 
     * @param para
     * @return
     * @throws Exception
     */
    boolean sendOrderTest(Map<String, Object> para)
        throws Exception;
    
    /**
     * 快递100回调接口
     * 
     * @param param
     * @return
     * @throws Exception
     */
    boolean kd100CallBack(String param, String orderId)
        throws Exception;
    
    /**
     * 返回订单详细信息
     * 
     * @param orderId
     * @return
     * @throws Exception
     */
    Map<String, Object> findOrderDetailInfo(int orderId, int from)
        throws Exception;
    
    /**
     * 以json字符串格式返回订单商品信息
     * 
     * @param orderId
     * @return
     * @throws Exception
     */
    String orderProductJsonStr(int orderId)
        throws Exception;
    
    /**
     * 查询订单详细信息forSeller
     * 
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, List<OrderDetailInfoForSeller>> findAllOrderInfoForSeller(Map<String, Object> para)
        throws Exception;
    
    
    /**
     * 查询结算订单详细信息forSeller
     * 
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, List<OrderDetailInfoForSeller>> findSellerUnSettleOrders(Map<String, Object> para)
            throws Exception;  
    
    /**
     * 导出订单列表结果
     * @param para
     * @return
     * @throws Exception
     */
    //    Workbook exportResult(Map<String, Object> para)
    //        throws Exception;
    
    /**
     * 
     * @param para
     * @return
     * @throws Exception
     */
    String exportForSeller(Map<String, Object> para)
        throws Exception;
    
    
    /**
     * 导出结算订单
     * @param para
     * @return
     * @throws Exception
     */
    String exportForSellerUnSettleOrders(Map<String, Object> para)
        throws Exception;
    
    /**
     * 更新订单导出状态
     * 
     * @throws Exception
     */
    void updateOrderOperationStatus(Set<Integer> orderIdList)
        throws Exception;
    
    /**
     * 根据number查询订单信息
     * 
     * @param number
     * @return
     * @throws Exception
     */
    OrderEntity findOrderByNumber(String number)
        throws Exception;
    
    /**
     * 修改订单状态
     * @param id：订单Id
     * @param number：订单编号
     * @param oldStatus：修改前的状态值
     * @param newStatus：修改后的状态值
     * @param mark：备注
     * @return
     * @throws Exception
     */
    String updateOrderStatus(int id, String number, int oldStatus, int newStatus, String mark)
        throws Exception;
    
    /**
     * 修改订单信息
     * 
     * @return
     * @throws Exception
     */
    int updateOrder(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改订单价格
     * 
     * @return
     * @throws Exception
     */
    int updatePrice(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改订单地址信息
     * 
     * @return
     * @throws Exception
     */
    Map<String, Object> updateAddress(ReceiveAddressEntity entity, int tradeId)
        throws Exception;
    
    /**
     * 根据para查询订单操作记录，并封装成json字符串返回
     * 
     * @param para
     * @return
     * @throws Exception
     */
    String jsonOrderOperationInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 获得所有市
     */
    String jsonAllCityByProvinceId(int id)
        throws Exception;
    
    /**
     * 获得所有地区
     */
    String jsonAllDistrictByCityId(int id)
        throws Exception;
    
    /**
     * 时间段 内未发货订单 信息 根据商家分组
     * 
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllUnSendGoodInfo()
        throws Exception;
    
    /**
     * 超24小时未发货明细
     * 
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> findAllUnSendGoodsDetail(Map<String, Object> para)
        throws Exception;
    
    /**
     * 订单来源 渠道 list
     * 
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> findAllOtherSource(Map<String, Object> para)
        throws Exception;
    
    /**
     * 页面搜索 所需 相关信息，供下拉列表使用
     * 
     * @param type
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> searchOtherSourceNeedsInfo(int type)
        throws Exception;
    
    /**
     * 保存订单渠道信息
     * 
     * @param name
     * @param person
     * @return
     * @throws Exception
     */
    Map<String, Object> saveOrUpdateChannel(Integer id, String name, String person)
        throws Exception;
    
    /**
     * 删除订单渠道信息
     * 
     * @param id
     * @return
     * @throws Exception
     */
    Map<String, Object> deleteOrderChannel(int id)
        throws Exception;
    
    /**
     * 根据Id获取订单
     * 
     * @param id
     * @return
     * @throws Exception
     */
    OrderEntity findOrderById(int id)
        throws Exception;
    
    /**
     * 统计用户消费金额
     * 
     * @return
     */
    List<Map<String, Object>> findAccountSpending()
        throws Exception;
    
    /**
     * 查询假单列表
     * 
     * @param para
     * @return
     */
    Map<String, Object> getFakeOrderJsonInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 假单重新发起订阅
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int orderLogisticsAgain(Map<String, Object> para)
        throws Exception;
    
    /**
     * 模拟导入订单结算状态
     * @param para
     * @return
     */
    boolean importTest(Map<String, Object> para)
        throws Exception;
    
    /**
     * 更新订单的结算状态
     * @param para
     * @return
     * @throws Exception
     */
    int updateOrderSettlement(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据 number 获取订单信息   用于创建手动订单
     * @param number
     * @return
     * @throws Exception
     */
    Map<String, Object> getOrderRefundInfo(Long number)
        throws Exception;
    
    /**
     * 计算订单商品退款 对应相应的积分优惠券金额等信息
     * @param number 订单编号
     * @param selectProductCount
     * @param orderProductId
     * @param from 1 : 新建退款单，2: 退款详情
     * @return
     * @throws Exception
     */
    Map<String, Object> calOrderRefundInfoByOrderNumber(long number, int selectProductCount, int orderProductId, int from)
        throws Exception;
    
    /**
     * 计算订单商品退款 对应相应的积分优惠券金额等信息
     * @param orderId 订单Id
     * @param selectProductCount
     * @param orderProductId
     * @return
     * @throws Exception
     */
    Map<String, Object> calOrderRefundInfoByOrderId(int orderId, int selectProductCount, int orderProductId)
        throws Exception;
    
    /**
     * 查询所有冻结订单
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> findAllOrderFreeze(Map<String, Object> para)
        throws Exception;
    
    Map<String, Object> orderSendTimeAnalyzeDetail(Map<String, Object> para)
        throws Exception;
    
    Map<String, Object> orderLogisticAnalyzeDetail(Map<String, Object> para)
        throws Exception;
    
    Map<String, Object> findAllProblemOrderList(Map<String, Object> para)
        throws Exception;
    
    /**
     * 贝贝网订单转笨鸟订单
     * @return
     * @throws Exception
     */
    Workbook saveOrderHBFromBeiBeiForBirdex(List<Map<String, Object>> data, String[] exportTitle)
        throws Exception;
    
    /**
     * 贝贝网订单发货导出
     * @param data
     * @param exportTitle
     * @return
     * @throws Exception
     */
    Workbook saveOrderSendGoodsFromBirdexForBeiBei(List<Map<String, Object>> data, String[] exportTitle)
        throws Exception;
    
    /**
     * 根据订单号导出发货文件
     * @param data
     * @param exportTitle
     * @return
     * @throws Exception
     */
    Workbook exportSendGoodsInfoByNumbers(List<Map<String, Object>> data, String[] exportTitle)
        throws Exception;
    
    /**
     * 查询已确认E店宝订单
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> findEdbOrderConfirmed(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查询未确认E店宝订单
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> findEdbOrderUnconfirmed(Map<String, Object> para)
        throws Exception;
    
    /**
     * 确认E店宝订单
     * @param idList
     * @return
     * @throws Exception
     */
    Map<String, Object> confirmEdbOrder(List<Integer> idList, int isPush)
        throws Exception;
    
    /**
     * 获取今日E店宝推送记录信息
     * @return
     * @throws Exception
     */
    Map<String, Object> getConfirmedCountInfo()
        throws Exception;
    
    /**
     * 订单审核
     * @return
     * @throws Exception
     */
    Map<String, Object> saveCheckOrder(int orderId, int checkStatus, String remark)
        throws Exception;
    
    /**
     * 更新EDB订单是否推送任务
     * @throws Exception
     */
    void updateEdbIsPushStatus()
        throws Exception;
}
