package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.OverseasOrderInfoForManage;

public interface OverseasOrderDao
{
    
    // ------------------------------海外购订单信息begin------------------------------------------------
    
    /**
     * 查询所有可以导出的海外购订单信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAll(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findAllByNumberList(List<Long> numberList)
        throws Exception;
    
    /**
     * 查询没有设置导出价格和导出名称的订单信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllWithOutExportInfo()
        throws Exception;
    
    List<OverseasOrderInfoForManage> findAllOverseasOrder(Map<String, Object> para)
        throws Exception;
    
    List<OverseasOrderInfoForManage> findAllOverseasOrderWithExport(Map<String, Object> para)
        throws Exception;
    
    int countAllOverseasOrder(Map<String, Object> para)
        throws Exception;
    
    int countAllOverseasOrderWithExport(Map<String, Object> para)
        throws Exception;
    
    // ------------------------------海外购订单信息end------------------------------------------------
    
    // ------------------------------用户每日导出记录begin------------------------------------------------
    
    /**
     * 根据para查询海外购导单记录
     * 
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findOverseasBuyerRecordByIdCard(Map<String, Object> para)
        throws Exception;
    
    /**
     * 插入海外购导单记录
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int insertOverseasBuyerRecord(Map<String, Object> para)
        throws Exception;
    
    // ------------------------------用户每日导出记录end------------------------------------------------
    
    // ------------------------------真实姓名与身份证号映射信息begin------------------------------------------------
    
    /**
     * 根据para查询真实姓名与身份证号映射信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllIdcardRealnameMapping(Map<String, Object> para)
        throws Exception;
    
    int countAllIdcardRealnameMapping(Map<String, Object> para)
        throws Exception;
    
    /** 根据身份证号查询对应关系 */
    Map<String, Object> findIdcardRealnameMappingByIdCard(String idCard)
        throws Exception;
    
    /**
     * 插入真实姓名与身份证号映射信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int insertIdcardRealnameMapping(Map<String, Object> para)
        throws Exception;
    
    /**
     * 运营 手动 插入真实姓名与身份证号映射信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int insertIdcardRealnameMappingForYY(Map<String, Object> para)
        throws Exception;
    
    int deleteIdcardRealnameMappingById(int id)
        throws Exception;
    
    int updateIdcardRealnameMapping(Map<String, Object> para)
        throws Exception;
    
    /**
     * 删除 待添加的 真实姓名与身份证号映射信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int deleteIdcardRealnameMappingByStatusEqualsZero()
        throws Exception;
    
    // ------------------------------真实姓名与身份证号映射信息end------------------------------------------------
    
    // ------------------------------订单合并记录begin------------------------------------------------
    
    /**
     * 插入导出订单合并记录
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int insertHBOrderRecord(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据para查询订单导出合并记录
     * 
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllHBOrderRecord(Map<String, Object> para)
        throws Exception;
    
    int deleteHBOrderRecordById(int id)
        throws Exception;
    
    int countAllHBOrderRecord(Map<String, Object> para)
        throws Exception;
    
    // ------------------------------订单合并记录end------------------------------------------------
    
    // -----------------------------海外购商品导出信息begin------------------------------------------------
    
    /**
     * 查询海外购商品信息 for excel
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllProductExportInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查询所有海外购商品 导出信息 for 列表
     * 
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllOverseasProductInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计海外购商品 导出信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int countAllOverseasProductInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据商品编码查询商品海外购导出信息
     */
    Map<String, Object> findOverseasProductInfoByProductCode(String code, String sellerName)
        throws Exception;
    
    /**
     * 删除海外购商品导出信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int deleteOverseasProductInfoById(int id)
        throws Exception;
    
    /**
     * 删除 待添加的 海外购商品导出信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int deleteOverseasProByStatusEqualsZero()
        throws Exception;
    
    /**
     * 插入海外购商品信息表记录
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int insertOverseasProInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 运营 手动 更新海外购商品信息表记录
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int updateOverseasProInfoForYY(Map<String, Object> para)
        throws Exception;
    
    // ------------------------------海外购商品导出信息end------------------------------------------------
    
    Integer findOverseasOrderExportRecordByNumber(String number)
        throws Exception;
    
    int insertOverseasOrderExportRecord(Map<String, Object> para)
        throws Exception;
    
    int deleteOverseasOrderExportRecord(Long number)
        throws Exception;
    
    int deleteOverseasBuyerRecord()
        throws Exception;
    
    List<Map<String, Object>> findOrderAliPay(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findOrderUnionPay(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findOrderWeixinPay(Map<String, Object> para)
        throws Exception;
    
    /**
     * 插入 合并订单发货信息
     * @param para
     * @return
     * @throws Exception
     */
    int addHbOrderSendRecord(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查找笨鸟订单推送记录
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findOverseasOrderList(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计笨鸟订单推送记录
     * @param para
     * @return
     * @throws Exception
     */
    int countOverseasOrderList(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查找所有笨鸟订单变更记录列表
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllBirdexOrderChange(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计所有笨鸟订单变更记录
     * @param para
     * @return
     * @throws Exception
     */
    int countBirdexOrderChange(Map<String, Object> para)
        throws Exception;
    
}
