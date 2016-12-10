package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.OrderReceiveAddress;
import com.ygg.admin.entity.ReceiveAddressEntity;

public interface ReceiveAddressDao
{
    /**
     * 根据para查询收货地址信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<ReceiveAddressEntity> findAllReceiveAddressByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据ID查询收货地址信息
     * 
     * @param id
     * @return
     * @throws Exception
     */
    ReceiveAddressEntity findReceiveAddressById(int id)
        throws Exception;
    
    /**
     * 根据ID查询订单收货地址信息
     * 
     * @param id
     * @return
     * @throws Exception
     */
    OrderReceiveAddress findOrderReceiveAddressById(int id)
        throws Exception;
}
