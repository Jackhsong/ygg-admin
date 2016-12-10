package com.ygg.admin.service;

import java.util.Date;
import java.util.Map;

public interface OverseasOrderService
{
    
    Map<String, Object> jsonOverseasOrder(Map<String, Object> para)
        throws Exception;
    
    /**
     * 导出所有可以导出的海外订单信息
     * 
     * @param isBigPrice 是否导出总价大于2000元的订单
     * @return
     * @throws Exception
     */
    String overseasAllCanExport(Date lastDate, boolean isBigPrice)
        throws Exception;
    
    /**
     * 检查可导出的海外购订单是否都设置了导出价格和导出名字
     * 
     * @return
     * @throws Exception
     */
    boolean checkOrderExportPriceAndName()
        throws Exception;
    
    /**
     * 以json字符串返回海外购商品导出信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    Map findOverseasProductInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 新增 or 更新 海外购商品导出信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int insertOrUpdateOverseasProductInfo(Map<String, Object> para)
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
    
    int deleteIdcardRealnameMappingById(int id)
        throws Exception;
    
    int insertOrUpdateIdcardRealnameMapping(Map<String, Object> para)
        throws Exception;
    
    Map<String, Object> jsonIdCardMapping(Map<String, Object> para)
        throws Exception;
    
    public Map<String, Object> findAllHBOrderRecord(Map<String, Object> para)
        throws Exception;
    
    int deleteHBOrderRecordById(int id)
        throws Exception;
    
    boolean importTest(Map<String, Object> para)
        throws Exception;
    
    boolean saveOverseasOrder(Map<String, Object> para)
        throws Exception;
    
    int deleteOverseasOrderExportRecord(String number)
        throws Exception;
    
    int deletePro()
        throws Exception;
    
    int deleteIdCardByStatusEqualsZero()
        throws Exception;
    
    int deleteOverseasBuyerRecord()
        throws Exception;
    
    /**
     * 更新订单导出记录
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int updateOverseasStatusExport(Map<String, Object> para)
        throws Exception;
}
