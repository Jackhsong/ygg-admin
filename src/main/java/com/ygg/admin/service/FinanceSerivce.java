package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

public interface FinanceSerivce
{
    Map<String, Object> findOrderFinanceData(Map<String, Object> para)
        throws Exception;
    
    String exportOrderFinanceDataDetail(Map<String, Object> para)
        throws Exception;
    
    int countAllNum(Map<String, Object> para)
        throws Exception;
    
    /**
     * 检查导入运费结算数据
     * 
     * @param number
     * @param postage
     * @return
     * @throws Exception
     */
    int checkImportPostageSettlementData(String number, String postage, List<Map<String, Object>> simulationList, List<String> alreadyImportOrderList)
        throws Exception;
    
    /**
     * 确认导入运费结算数据
     * 
     * @param number
     * @param postage
     * @return
     * @throws Exception
     */
    int savePostageSettlementData(String number, String postage, String date, List<Map<String, Object>> confirmList)
        throws Exception;
    
    /**
     * 检查导入退款退货结算数据
     * @param refundId
     * @param responsibilitySide
     * @param money
     * @param simulationList
     * @return
     * @throws Exception
     */
    int checkimportRefundSettlementData(String refundId, String responsibilitySide, String money, List<Map<String, Object>> simulationList)
        throws Exception;
    
    /**
     * 确认导入退款退货结算数据
     * @param confirmList
     * @return
     * @throws Exception
     */
    int saveRefundSettlementData(String refundId, String responsibilitySide, String money, String date, List<Map<String, Object>> confirmList)
        throws Exception;

    int cancelRefundSettlementData(String refundId, List<Map<String, Object>> confirmList)
        throws Exception;
    
    /**
     * 导入批量修改供货价数据
     */
    int checkBatchUpdateProductCostPrice(String number, String productType, String productId, String productCost, List<Map<String, Object>> simulationList, Map<String,String> productInfo)
        throws Exception;
    
    /**
     * 确认导入批量修改供货价数据
     */
    int saveBatchUpdateProductCostPrice(String number, String productType, String productId, String productCost, List<Map<String, Object>> confirmList, Map<String,String> productInfo)
        throws Exception;
    
    /**
     * 查找商家结算信息
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> findSellerSettlementStatistics(Map<String, Object> para)
        throws Exception;

    /**
     * 查找商家实时毛利统计 数据
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> findSellerGrossCalculation(Map<String, Object> para)
        throws Exception;

    /**
     * 查找商家实时毛利统计 数据 详细
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> findSellerGrossCalculationDetail(Map<String, Object> para)
        throws Exception;

    /**
     * 导出所有商家实时毛利统计 数据 详细
     * @param para
     * @return
     * @throws Exception
     */
    String  exportAllSellerGrossCalculationDetail(Map<String, Object> para)
        throws Exception;

    /**
     * 查找商家退款结算信息
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> findSellerRefundStatistics(Map<String, Object> para)
        throws Exception;
    
    Map<String, Object> findSellerRefundStatisticsNew(Map<String, Object> para)
        throws Exception;

    int deletePostageSettlementData(String number, List<Map<String, Object>> confirmList)
        throws Exception;

    /**
     * 获取 商家结算周期管理 数据
     * @param para
     * @return
     * @throws Exception
     */
    Map<String,Object> findSellerSettlementPeriodData(Map<String, Object> para)
        throws Exception;

    /**
     * 查找商家结算汇总
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> findSellerSettlementSum(Map<String, Object> para)
        throws Exception;

    /**
     * 查找商家商家结算毛利统计
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> findSellerGrossSettlement(Map<String, Object> para)
        throws Exception;

    /**
     * 检查订单罚款信息
     * @param number
     * @param simulationList
     * @return
     * @throws Exception
     */
    int checkImportPenaltySettlementData(String number, List<Map<String, Object>> simulationList)
            throws Exception;

    /**
     * 保存订单罚款信息
     * @param number
     * @param confirmList
     * @return
     * @throws Exception
     */
    int savePenaltySettlementData(String number, List<Map<String, Object>> confirmList)
            throws Exception;

    /**
     * 保存订单罚款信息
     * @param number
     * @param cancelList
     * @return
     * @throws Exception
     */
    int deletePenaltySettlementData(String number, List<Map<String, Object>> cancelList)
            throws Exception;
}
