package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.SellerExpandEntity;

public interface SellerExpandDao
{
    
    int save(SellerExpandEntity sellerExpand)
        throws Exception;
    
    int update(Map<String, Object> sellerExpandMap)
        throws Exception;
    
    SellerExpandEntity findSellerExpandBysid(int sellerId)
        throws Exception;
    
    /**
     * 同步商家扩展信息
     * @param para
     * @return
     * @throws Exception
     */
    int synchronousSellerExpandInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改商家密码
     * @param sellerExpand
     * @return
     * @throws Exception
     */
    int updatePassword(SellerExpandEntity sellerExpand)
        throws Exception;
    
    /**
     * 修改商家主从帐号状态
     * @param slaveIdList
     * @param status
     * @return
     * @throws Exception
     */
    int updateSellerToSlave(List<String> slaveIdList, int status)
        throws Exception;
    
    /**
     * 将商家设为主帐号
     * @param masterId
     * @return
     * @throws Exception
     */
    int updateSellerToMaster(String masterId)
        throws Exception;
    
    /**
     * 根据商家ID查找商家扩展信息
     * @param sellerIds
     * @return
     * @throws Exception
     */
    List<SellerExpandEntity> findSellerExpandBysids(List<Integer> sellerIds)
        throws Exception;

    /**
     * 
     * @param sellerId
     * @return
     */
    int updateEcount(int sellerId);
    
}
