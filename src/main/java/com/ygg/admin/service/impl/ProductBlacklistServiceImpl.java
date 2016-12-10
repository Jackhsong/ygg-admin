package com.ygg.admin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ygg.admin.code.AccountEnum;
import com.ygg.admin.dao.AccountDao;
import com.ygg.admin.dao.ProductBlacklistDao;
import com.ygg.admin.service.ProductBlacklistService;
import com.ygg.admin.util.DateTimeUtil;

@Service("productBlacklistService")
public class ProductBlacklistServiceImpl implements ProductBlacklistService
{
    Logger log = Logger.getLogger(ProductBlacklistServiceImpl.class);
    
    @Resource
    private ProductBlacklistDao productBlacklistDao;
    
    @Resource
    private AccountDao accountDao;
    
    @Override
    public List<Map<String, Object>> findAllProduct(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> blacklist = productBlacklistDao.findAllProduct(para);
        return blacklist == null ? new ArrayList<Map<String, Object>>() : blacklist;
    }
    
    @Override
    public int add(Map<String, Object> para)
        throws Exception
    {
        return productBlacklistDao.add(para);
    }
    
    @Override
    public int delete(Map<String, Object> para)
        throws Exception
    {
        return productBlacklistDao.delete(para);
    }
    
    @Override
    public boolean exist(Map<String, Object> para)
        throws Exception
    {
        return productBlacklistDao.exist(para);
    }
    
    @Override
    public Map<String, Object> reduceOrderProductBlacklist()
        throws Exception
    {
        List<Map<String, Object>> orderInfoList = productBlacklistDao.findReduceOrderId();
        //key:accountId  value:{availablePoint:xx,totalRealPriceInteger:xx}
        Map<String, Map<String, Object>> infoByAccountId = new HashMap<String, Map<String, Object>>();
        for (Map<String, Object> it : orderInfoList)
        {
            String accountId = it.get("accountId") + "";
            Float realPrice = Float.parseFloat(it.get("realPrice") == null ? "0" : it.get("realPrice") + "");
            Integer curTotalRealPriceInteger = Math.round(realPrice);
            Map<String, Object> accountMap = infoByAccountId.get(accountId);
            if (accountMap == null)
            {
                accountMap = new HashMap<String, Object>();
                infoByAccountId.put(accountId, accountMap);
                Integer availablePoint = Integer.parseInt(it.get("availablePoint") == null ? "0" : it.get("availablePoint") + "");
                accountMap.put("availablePoint", availablePoint);
                accountMap.put("totalRealPriceInteger", curTotalRealPriceInteger);
            }
            else
            {
                Integer totalRealPriceInteger = (Integer)accountMap.get("totalRealPriceInteger");
                totalRealPriceInteger += curTotalRealPriceInteger;
                accountMap.put("totalRealPriceInteger", totalRealPriceInteger);
            }
        }
        for (Map.Entry<String, Map<String, Object>> entry : infoByAccountId.entrySet())
        {
            Integer accountId = Integer.parseInt(entry.getKey());
            Map<String, Object> value = entry.getValue();
            Integer availablePoint = (Integer)value.get("availablePoint");
            Integer totalRealPriceInteger = (Integer)value.get("totalRealPriceInteger");
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("oldAvailablePoint", availablePoint);
            para.put("id", accountId);
            if (availablePoint <= totalRealPriceInteger)
            {
                para.put("newAvailablePoint", 0);
                int status = accountDao.updateIntegral(para);
                if (status == 1)
                {
                    //删除积分record
                    int recordNum = accountDao.deleteAccountAvailablePointRecord(accountId);
                    log.info("1成功删除用户(" + accountId + ")" + recordNum + "条积分记录。");
                }
                else
                {
                    //更新失败
                    log.warn("1更新用户积分失败！！！用户ID：" + accountId);
                }
            }
            else
            {
                para.put("newAvailablePoint", availablePoint - totalRealPriceInteger);
                int status = accountDao.updateIntegral(para);
                if (status == 1)
                {
                    //删除积分record
                    int recordNum = accountDao.deleteAccountAvailablePointRecord(accountId);
                    log.info("2成功删除用户(" + accountId + ")" + recordNum + "条积分记录。");
                    //新增积分record
                    Map<String, Object> integralPara = new HashMap<String, Object>();
                    integralPara.put("accountId", accountId);
                    integralPara.put("operatePoint", availablePoint - totalRealPriceInteger);
                    integralPara.put("totalPoint", availablePoint - totalRealPriceInteger);
                    integralPara.put("operateType", AccountEnum.INTEGRAL_OPERATION_TYPE.SHOPPING_GIFT.getCode());
                    integralPara.put("arithmeticType", 1);
                    integralPara.put("createTime", DateTimeUtil.now());
                    int astatus = accountDao.insertIntegralRecord(integralPara);
                    if (astatus == 1)
                    {
                        log.info("2新增积分记录成功。");
                    }
                    else
                    {
                        log.warn("2新增积分记录失败！！！");
                    }
                }
                else
                {
                    //更新失败
                    log.warn("2更新用户积分失败！！！用户ID：" + accountId);
                }
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", "ok");
        return map;
    }
}
