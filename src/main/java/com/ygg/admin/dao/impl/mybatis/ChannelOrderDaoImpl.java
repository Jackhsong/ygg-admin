package com.ygg.admin.dao.impl.mybatis;

import com.ygg.admin.dao.ChannelOrderDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.view.channel.ChannelOrderExcelView;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-4-13
 */
@Repository
public class ChannelOrderDaoImpl extends BaseDaoImpl implements ChannelOrderDao
{
    private static final Logger logger = Logger.getLogger(ChannelOrderDaoImpl.class);

    @Override
    public Map<String, Object> getChannelOrderInfoByNumberAndChannelId(ChannelOrderExcelView param)
    {
        try {
            //// 故意 主库
            return getSqlSession().selectOne("ChannelOrderMapper.getChannelOrderInfoByNumberAndChannelId", param);
        }catch (Exception e){
            logger.error(e);
            return null;
        }
    }

    @Override
    public long insertChannelOrder(ChannelOrderExcelView order)
    {
        try
        {
            getSqlSession().insert("ChannelOrderMapper.saveChannelOrder", order);
            return order.getOrder_id();
        }
        catch (Exception e)
        {
            logger.error("ChannelOrderMapper.saveChannelOrder error", e);
            return 0l;
        }
    }

    @Override
    public long insertChannelOrderProduct(ChannelOrderExcelView product)
    {
        try {
            return getSqlSession().insert("ChannelOrderMapper.saveChannelOrderProduct", product);
        }catch (Exception e){
            logger.error(e);
            return 0;
        }
    }

    @Override
    public long insertChannelOrderReceiveAddress(ChannelOrderExcelView logistic)
    {
        try {
            return getSqlSession().insert("ChannelOrderMapper.saveChannelOrderReceiveAddress", logistic);
        }catch (Exception e){
            logger.error(e);
            return 0;
        }
    }

    @Override
    public long getChannelOrderProductIdByParam(ChannelOrderExcelView param)
    {
        try {
            // 故意 主库
            Long id = getSqlSession().selectOne("ChannelOrderMapper.getChannelOrderProductIdByParam", param);
            if(id == null)
                return 0;
            return id;
        }catch (Exception e){
            logger.error(e);
            return 0l;
        }
    }

    @Override
    public int updateChannelOrder(ChannelOrderExcelView order)
    {
        return getSqlSession().update("ChannelOrderMapper.updateChannelOrder", order);
    }

    @Override
    public int updateChannelOrderReceiverAddress(ChannelOrderExcelView logistic)
    {
        return getSqlSession().update("ChannelOrderMapper.updateChannelOrderReceiverAddress", logistic);
    }

    @Override
    public int updateChannelOrderProduct(ChannelOrderExcelView product)
    {
        return getSqlSession().update("ChannelOrderMapper.updateChannelOrderProduct", product);
    }

    @Override
    public List<Map<String, Object>> jsonChannelOrderInfo(Map<String, Object> para) {
        return getSqlSessionRead().selectList("ChannelOrderMapper.jsonChannelOrderInfo" ,para);
    }

    @Override
    public List<ChannelOrderExcelView> getChannelOrderAllInfo(Map<String, Object> para) {
        return getSqlSessionRead().selectList("ChannelOrderMapper.getChannelOrderAllInfo" ,para);
    }

    @Override
    public int countJsonChannelOrderInfo(Map<String, Object> para) {
        return getSqlSessionRead().selectOne("ChannelOrderMapper.countJsonChannelOrderInfo" ,para);
    }

}
