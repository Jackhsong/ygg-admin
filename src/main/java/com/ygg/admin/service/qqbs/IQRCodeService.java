package com.ygg.admin.service.qqbs;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.qqbs.QRCodeEntity;

/**
 * @author wuhy
 * @date 创建时间：2016年5月16日 下午5:28:19
 */
public interface IQRCodeService
{
    
    List<QRCodeEntity> findQRCodesByQueryCriteria(QRCodeEntity queryCriteria)
        throws Exception;
    
    Map<String, Object> createQRCodeByAccountId(int accountId);
}
