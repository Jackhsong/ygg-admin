package com.ygg.admin.dao.qqbs;

import java.util.List;

import com.ygg.admin.entity.qqbs.QRCodeEntity;

/**
 * @author wuhy
 * @date 创建时间：2016年5月16日 下午8:02:31
 */
public interface IQqbsQRCodeDao
{
    List<QRCodeEntity> findQRCodesByQueryCriteria(QRCodeEntity queryCriteria);
    
    int countQRCodesByQueryCriteria(QRCodeEntity queryCriteria);
    
    int createQRCode(QRCodeEntity entity);
    
}
