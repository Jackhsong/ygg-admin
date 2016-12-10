package com.ygg.admin.dao.qqbs.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.dao.qqbs.IQqbsQRCodeDao;
import com.ygg.admin.entity.qqbs.QRCodeEntity;

/**
 * @author wuhy
 * @date 创建时间：2016年5月16日 下午8:03:45
 */
@Repository
public class QqbsQRCodeDao extends BaseDaoImpl implements IQqbsQRCodeDao
{
    
    @Override
    public List<QRCodeEntity> findQRCodesByQueryCriteria(QRCodeEntity queryCriteria)
    {
        return getSqlSessionRead().selectList("QqbsQRCodeMapper.findQRCodesByQueryCriteria", queryCriteria);
    }
    
    @Override
    public int countQRCodesByQueryCriteria(QRCodeEntity queryCriteria)
    {
        return getSqlSessionRead().selectOne("QqbsQRCodeMapper.countQRCodesByQueryCriteria", queryCriteria);
    }
    
    @Override
    public int createQRCode(QRCodeEntity entity)
    {
        
        return getSqlSession().insert("QqbsQRCodeMapper.createQRCode", entity);
    }
    
}
