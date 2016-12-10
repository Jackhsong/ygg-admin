package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.BrandEntity;
import com.ygg.admin.entity.RelationSellerDeliverAreaEntity;
import com.ygg.admin.entity.SellerEntity;
import com.ygg.admin.entity.SellerExpandEntity;
import com.ygg.admin.entity.SellerFinanceSettlementPictureEntity;
import com.ygg.admin.entity.SellerMasterAndSlaveEntity;
import com.ygg.admin.entity.SellerOnlineStoreAddressEntity;

public interface SellerService
{
    
    /**
     * 保存商家信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int saveOrUpdate(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据para查询商家，并封装成json字符串返回
     * 
     * @param para
     * @return
     * @throws Exception
     */
    String jsonSellerInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据ID查找SellerEntity简略信息
     * 
     * @param id
     * @return
     * @throws Exception
     */
    Map<String, Object> findSellerInfoById(int id)
        throws Exception;
    
    /**
     * 根据ID查找SellerEntity简略 simple 信息
     * 
     * @param id
     * @return
     * @throws Exception
     */
    SellerEntity findSellerSimpleById(int id)
        throws Exception;
    
    /**
     * 根据ID查找SellerEntity对象
     * 
     * @param id
     * @return
     * @throws Exception
     */
    SellerEntity findSellerById(int id)
        throws Exception;
    
    /**
     * 查询可用的商家信息
     * 
     * @return
     * @throws Exception
     */
    List<SellerEntity> findSellerIsAvailable()
        throws Exception;
    
    /**
     * 查询全部商家信息
     * 
     * @return
     * @throws Exception
     */
    List<SellerEntity> findAllSeller(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据名字统计商家数量
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int countSellerBySellerName(Map<String, Object> para)
        throws Exception;
    
    int batchUpdateSeller(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查找特卖商家周期管理列表
     * @param para
     * @return
     */
    Map<String, Object> findAllSaleWindowForSellerPeriod(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查询商家发货/不发货地区
     * @param id
     * @return
     * @throws Exception
     */
    List<RelationSellerDeliverAreaEntity> findRelationSellerDeliverAreaBySellerId(int id)
        throws Exception;
    
    /**
     * 根据商家Id查找付款信息图片
     * @param id
     * @return
     * @throws Exception
     */
    List<SellerFinanceSettlementPictureEntity> findSellerFinancePictureBysid(int id)
        throws Exception;
    
    /**
     * 根据商家Id查找店铺网址
     * @param id
     * @return
     * @throws Exception
     */
    List<SellerOnlineStoreAddressEntity> findSellerOnlineStoreBysid(int id)
        throws Exception;
    
    /**
     * 根据商家Id查找商家所售品牌
     * @param id
     * @return
     * @throws Exception
     */
    List<BrandEntity> findSellerBrandBysid(int id)
        throws Exception;
    
    /**
     * 根据商家名称查找商家是否存在
     * @param realSellerName
     * @return
     * @throws Exception
     */
    SellerEntity findSellerByRealSellerName(String realSellerName)
        throws Exception;
    
    /**
     * 根据商家id查找商家从帐号
     * @param sellerId
     * @return
     * @throws Exception
     */
    SellerMasterAndSlaveEntity findSellerSlaveBySlaveId(int sellerId)
        throws Exception;
    
    /**
     * 异步加载合并商家列表
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> mergeSellerJsonInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 主帐号Id
     * @param masterId:主帐号Id
     * @param slaveId:从帐号Id
     * @return
     * @throws Exception
     */
    Map<String, Object> mergeSeller(String masterId, List<String> slaveId, int userId)
        throws Exception;
    
    /**
     * 根据主商家ID查找所有从商家信息
     * @param masterId
     * @return
     * @throws Exception
     */
    List<SellerMasterAndSlaveEntity> findSellerSlaveListByMasterId(int masterId)
        throws Exception;
    
    /**
     * 修改商家密码
     * @return
     * @throws Exception
     */
    int updatePassword(SellerExpandEntity sellerExpand)
        throws Exception;
    
    String updateAvailableStatus(int id, int isAvailable)
        throws Exception;
}
