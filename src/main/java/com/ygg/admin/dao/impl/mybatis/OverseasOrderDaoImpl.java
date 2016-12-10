package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ygg.admin.dao.AccountDao;
import org.apache.shiro.authc.Account;
import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.OverseasOrderDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.OverseasOrderInfoForManage;

import javax.annotation.Resource;

@Repository("overseasOrderDao")
public class OverseasOrderDaoImpl extends BaseDaoImpl implements OverseasOrderDao
{
    // ------------------------------海外购订单信息begin------------------------------------------------
    @Override
    public List<Map<String, Object>> findAll(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = this.getSqlSessionRead().selectList("OverseasOrderMapper.findAll", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public List<Map<String, Object>> findAllByNumberList(List<Long> numberList)
        throws Exception
    {
        return this.getSqlSessionRead().selectList("OverseasOrderMapper.findAllByNumberList", numberList);
    }
    
    @Override
    public List<Map<String, Object>> findAllWithOutExportInfo()
        throws Exception
    {
        return this.getSqlSessionRead().selectList("OverseasOrderMapper.findAllWithOutExportInfo");
    }
    
    @Override
    public List<OverseasOrderInfoForManage> findAllOverseasOrder(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectList("OverseasOrderMapper.findAllOverseasOrder", para);
    }
    
    @Override
    public List<OverseasOrderInfoForManage> findAllOverseasOrderWithExport(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectList("OverseasOrderMapper.findAllOverseasOrderWithExport", para);
    }
    
    @Override
    public int countAllOverseasOrder(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("OverseasOrderMapper.countAllOverseasOrder", para);
    }
    
    @Override
    public int countAllOverseasOrderWithExport(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("OverseasOrderMapper.countAllOverseasOrderWithExport", para);
    }
    
    // ------------------------------海外购订单信息end------------------------------------------------
    
    // ------------------------------用户每日导出记录begin------------------------------------------------
    
    @Override
    public List<Map<String, Object>> findOverseasBuyerRecordByIdCard(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = this.getSqlSessionRead().selectList("OverseasOrderMapper.findOverseasBuyerRecordByIdCard", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public int insertOverseasBuyerRecord(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().insert("OverseasOrderMapper.insertOverseasBuyerRecord", para);
    }
    
    // ------------------------------用户每日导出记录end------------------------------------------------
    
    // ------------------------------真实姓名与身份证号映射信息begin------------------------------------------------
    
    @Override
    public List<Map<String, Object>> findAllIdcardRealnameMapping(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = this.getSqlSession().selectList("OverseasOrderMapper.findAllIdcardRealnameMapping", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public int countAllIdcardRealnameMapping(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().selectOne("OverseasOrderMapper.countAllIdcardRealnameMapping", para);
    }
    
    @Override
    public Map<String, Object> findIdcardRealnameMappingByIdCard(String idCard)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("idCard", idCard);
        para.put("start", 0);
        para.put("max", 1);
        List<Map<String, Object>> reList = findAllIdcardRealnameMapping(para);
        if (reList.size() > 0)
        {
            return reList.get(0);
        }
        else
        {
            return null;
        }
    }
    
    @Override
    public int insertIdcardRealnameMapping(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().insert("OverseasOrderMapper.insertIdcardRealnameMapping", para);
    }
    
    @Override
    public int insertIdcardRealnameMappingForYY(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().insert("OverseasOrderMapper.insertIdcardRealnameMappingForYY", para);
    }
    
    @Override
    public int deleteIdcardRealnameMappingById(int id)
        throws Exception
    {
        return this.getSqlSession().delete("OverseasOrderMapper.deleteIdCardMapping", id);
    }
    
    @Override
    public int updateIdcardRealnameMapping(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("OverseasOrderMapper.updateIdCardMapping", para);
    }
    
    @Override
    public int deleteIdcardRealnameMappingByStatusEqualsZero()
        throws Exception
    {
        return getSqlSession().delete("OverseasOrderMapper.deleteIdcardRealnameMappingByStatusEqualsZero");
    }
    
    // ------------------------------真实姓名与身份证号映射信息end------------------------------------------------
    
    // ------------------------------订单合并记录begin------------------------------------------------
    
    @Override
    public int insertHBOrderRecord(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().insert("OverseasOrderMapper.insertHBOrderRecord", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllHBOrderRecord(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = this.getSqlSession().selectList("OverseasOrderMapper.findAllHBOrderRecord", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public int deleteHBOrderRecordById(int id)
        throws Exception
    {
        return this.getSqlSession().delete("OverseasOrderMapper.deleteHBOrderRecord", id);
    }
    
    @Override
    public int countAllHBOrderRecord(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().selectOne("OverseasOrderMapper.countAllHBOrderRecord", para);
    }
    
    // ------------------------------订单合并记录end------------------------------------------------
    
    // -----------------------------海外购商品导出信息begin------------------------------------------------
    
    @Override
    public List<Map<String, Object>> findAllProductExportInfo(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = this.getSqlSessionRead().selectList("OverseasOrderMapper.findAllProductExportInfo", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public List<Map<String, Object>> findAllOverseasProductInfo(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = this.getSqlSession().selectList("OverseasOrderMapper.findAllOverseasProductInfo", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public int countAllOverseasProductInfo(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().selectOne("OverseasOrderMapper.countAllOverseasProductInfo", para);
    }
    
    @Override
    public Map<String, Object> findOverseasProductInfoByProductCode(String code, String sellerName)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("code", code);
        para.put("sellerName", sellerName);
        para.put("start", 0);
        para.put("max", 1);
        List<Map<String, Object>> reList = findAllOverseasProductInfo(para);
        if (reList.size() > 0)
        {
            return reList.get(0);
        }
        else
        {
            return null;
        }
    }
    
    @Override
    public int deleteOverseasProductInfoById(int id)
        throws Exception
    {
        return this.getSqlSession().delete("OverseasOrderMapper.deleteOverseasPro", id);
    }
    
    @Override
    public int deleteOverseasProByStatusEqualsZero()
        throws Exception
    {
        return this.getSqlSession().delete("OverseasOrderMapper.deleteOverseasProByStatusEqualsZero");
    }
    
    @Override
    public int insertOverseasProInfo(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().insert("OverseasOrderMapper.insertOverseasProInfo", para);
    }
    
    @Override
    public int updateOverseasProInfoForYY(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("OverseasOrderMapper.updateOverseasProductInfo", para);
    }
    
    // ------------------------------海外购商品导出信息end------------------------------------------------
    @Override
    public Integer findOverseasOrderExportRecordByNumber(String number)
        throws Exception
    {
        return getSqlSession().selectOne("OverseasOrderMapper.findOverseasOrderExportRecordByNumber", number);
    }
    
    @Override
    public int insertOverseasOrderExportRecord(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("OverseasOrderMapper.insertOverseasOrderExportRecord", para);
    }
    
    @Override
    public int deleteOverseasOrderExportRecord(Long number)
        throws Exception
    {
        return getSqlSession().delete("OverseasOrderMapper.deleteOverseasOrderExportRecord", number);
    }
    
    @Override
    public int deleteOverseasBuyerRecord()
        throws Exception
    {
        return getSqlSession().delete("OverseasOrderMapper.deleteOverseasOrderExportRecord");
    }
    
    @Override
    public List<Map<String, Object>> findOrderAliPay(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("OverseasOrderMapper.findOrderAliPay", para);
    }
    
    @Override
    public List<Map<String, Object>> findOrderUnionPay(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("OverseasOrderMapper.findOrderUnionPay", para);
    }
    
    @Override
    public List<Map<String, Object>> findOrderWeixinPay(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("OverseasOrderMapper.findOrderWeixinPay", para);
    }
    
    @Override
    public int addHbOrderSendRecord(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("OverseasOrderMapper.addHbOrderSendRecord", para);
    }
    
    @Override
    public int countOverseasOrderList(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().selectOne("OverseasOrderMapper.countOverseasOrderList", para);
    }
    
    @Override
    public List<Map<String, Object>> findOverseasOrderList(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().selectList("OverseasOrderMapper.findOverseasOrderList", para);
    }
    
    @Override
    public int countBirdexOrderChange(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().selectOne("OverseasOrderMapper.countBirdexOrderChange", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllBirdexOrderChange(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionAdmin().selectList("OverseasOrderMapper.findAllBirdexOrderChange", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
}
