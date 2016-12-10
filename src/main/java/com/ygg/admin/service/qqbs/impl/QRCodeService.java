package com.ygg.admin.service.qqbs.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ygg.admin.dao.AccountDao;
import com.ygg.admin.dao.qqbs.IQqbsQRCodeDao;
import com.ygg.admin.dao.qqbs.QqbsAccountDao;
import com.ygg.admin.entity.AccountEntity;
import com.ygg.admin.entity.qqbs.QRCodeEntity;
import com.ygg.admin.entity.qqbs.QqbsAccountEntity;
import com.ygg.admin.sdk.wenxin.WeiXinUtil;
import com.ygg.admin.service.qqbs.IQRCodeService;

/**
 * @author wuhy
 * @date 创建时间：2016年5月16日 下午5:29:12
 */
@Service
public class QRCodeService implements IQRCodeService
{
    @Resource
    private IQqbsQRCodeDao qqbsQRCodeDao;
    
    @Resource(name = "qqbsAccountDao")
    private QqbsAccountDao qqbsAccountDao;
    
    @Resource
    private AccountDao accountDao;
    
    @Override
    public List<QRCodeEntity> findQRCodesByQueryCriteria(QRCodeEntity queryCriteria)
        throws Exception
    {
        List<QRCodeEntity> result = qqbsQRCodeDao.findQRCodesByQueryCriteria(queryCriteria);
        if (!CollectionUtils.isEmpty(result))
        {
            List<Integer> accountIds = new ArrayList<Integer>();
            for (QRCodeEntity entity : result)
            {
                accountIds.add(entity.getAccountId());
                
            }
            List<AccountEntity> accounts = accountDao.findAccountsByAccountIds(accountIds);
            Map<Integer, AccountEntity> accountIdEntityMap = new HashMap<Integer, AccountEntity>();
            for (AccountEntity account : accounts)
            {
                accountIdEntityMap.put(account.getId(), account);
            }
            for (QRCodeEntity qrCode : result)
            {
                qrCode.setAccount(accountIdEntityMap.get(qrCode.getAccountId()));
            }
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> createQRCodeByAccountId(int accountId)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", false);
        if (qqbsAccountDao.findAccountByAccountId(accountId) == null)
        {
            
            result.put("message", "AccountId为" + accountId + "的用户不存在");
            return result;
        }
        if (hasQRCodes(accountId))
        {
            result.put("message", "AccountId为" + accountId + "永久二维码已存在");
            return result;
            
        }
        String qrCodeUrl = WeiXinUtil.buildPersistentQRCode(accountId);
        QRCodeEntity entity = new QRCodeEntity();
        entity.setAccountId(accountId);
        entity.setQrCodeUrl(qrCodeUrl);
        entity.setCreator(String.valueOf(SecurityUtils.getSubject().getPrincipal()));
        qqbsQRCodeDao.createQRCode(entity);
        QqbsAccountEntity qqbsAccount = qqbsAccountDao.findAccountByAccountId(accountId);
        qqbsAccount.setHasPersistentQRCode(1);
        qqbsAccountDao.addPersistentQRCodeToAccount(qqbsAccount);
        result.put("status", true);
        result.put("qrCodeUrl", qrCodeUrl);
        return result;
    }
    
    private boolean hasQRCodes(int accountId)
    {
        QRCodeEntity queryCriteria = new QRCodeEntity();
        queryCriteria.setAccountId(accountId);
        return !CollectionUtils.isEmpty(qqbsQRCodeDao.findQRCodesByQueryCriteria(queryCriteria));
    }
}
