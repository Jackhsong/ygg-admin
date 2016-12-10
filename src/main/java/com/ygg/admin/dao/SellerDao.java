package com.ygg.admin.dao;

import com.ygg.admin.entity.*;

import java.util.List;
import java.util.Map;

public interface SellerDao
{
    
    /**
     * 插入商家信息
     * 
     * @param seller
     * @return
     * @throws Exception
     */
    int save(SellerEntity seller)
        throws Exception;
    
    /**
     * 查询商家信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<SellerEntity> findAllSellerByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查询商家简略信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<SellerEntity> findAllSellerSimpleByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 更加ID查询商家信息
     * 
     * @param id
     * @return
     * @throws Exception
     */
    SellerEntity findSellerById(int id)
        throws Exception;
    
    /**
     * 更加
     * @param sellerType
     * @return
     * @throws Exception
     */
    List<Integer> findIdListBySellerType(int sellerType)
        throws Exception;
    
    List<Integer> findSellerIdWhereIsOwnerEqualsOne()
        throws Exception;
    
    /**
     * 更加ID查询商家简略信息
     * 
     * @param id
     * @return
     * @throws Exception
     */
    SellerEntity findSellerSimpleById(int id)
        throws Exception;
    
    /**
     * 更新商家信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int updateSellerByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查询商家数量
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int countSellerByPara(Map<String, Object> para)
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
    
    /**
     * 根据para统计商家销售数据
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllSellerSaleInfo(Map<String, Object> para)
        throws Exception;
    
    int countAllSellerSaleInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据商品ID列表查询对应的商家ID
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<Integer> findSellerIdByProductIdList(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据商品Id查找卖家信息
     * @param displayId
     * @return
     */
    SellerEntity findSellerByProductId(int displayId)
        throws Exception;
    
    /**
     * 根据通用专场查询卖家信息
     * @param displayId
     * @return
     * @throws Exception
     */
    SellerEntity findAllSellerByActivityCommonId(int displayId)
        throws Exception;
    
    List<Map<String, Object>> findSellerInfoBySellerIdList(Map<String, Object> para)
        throws Exception;
    
    /**
     * 插入商家配送地区表
     * @param rsdae
     * @return
     */
    int insertRelationSellerDeliverArea(RelationSellerDeliverAreaEntity rsdae)
        throws Exception;
    
    /**
     * 修改商家配送地区
     * @param rsdae
     * @return
     */
    int updateRelationSellerDeliverArea(RelationSellerDeliverAreaEntity rsdae)
        throws Exception;
    
    /**
     * 删除商家配送地区
     * @param mp
     * @return
     */
    int deleteRelationSellerDeliverArea(Map<String, Object> mp)
        throws Exception;
    
    /**
     * 根据商家Id查找商家配送地区
     * @param id
     * @return
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
     * 插入商家付款信息图片
     * @param sfspe
     * @return
     * @throws Exception
     */
    int saveSellerFinancePicture(SellerFinanceSettlementPictureEntity sfspe)
        throws Exception;
    
    /**
     * 修改商家付款信息图片
     * @param sfspe
     * @return
     * @throws Exception
     */
    int updateSellerFinancePicture(SellerFinanceSettlementPictureEntity sfspe)
        throws Exception;
    
    /**
     * 删除商家付款信息图片
     * @param it
     * @return
     * @throws Exception
     */
    int deleteSellerFinancePicture(Map<String, Object> it)
        throws Exception;
    
    /**
     * 新增商家店铺网址
     * @param storeList
     * @return
     * @throws Exception
     */
    int saveSellerOnlineStoreAddress(List<SellerOnlineStoreAddressEntity> storeList)
        throws Exception;
    
    /**
     * 更新商家店铺网址
     * @param sosae
     * @return
     * @throws Exception
     */
    int updateSellerOnlineStoreAddress(SellerOnlineStoreAddressEntity sosae)
        throws Exception;
    
    /**
     * 删除商家店铺网址
     * @param sellerId
     * @return
     * @throws Exception
     */
    int deleteSellerOnlineStoreAddress(int sellerId)
        throws Exception;
    
    /**
     * 根据商家Id查找商家店铺网址
     * @param id
     * @return
     * @throws Exception
     */
    List<SellerOnlineStoreAddressEntity> findSellerOnlineStoreBysid(int id)
        throws Exception;
    
    /**
     * 根据商家ID集合查询所有商家名称
     * @param idList
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findSellerRealNameByIds(List<Integer> idList)
        throws Exception;
    
    /**
     * 根据商家id查找商家所有品牌
     * @param id
     * @return
     * @throws Exception
     */
    List<BrandEntity> findSellerBrandBysid(int id)
        throws Exception;
    
    /**
     * 删除商家品牌信息
     * @param id
     * @return
     * @throws Exception
     */
    int deleteSellerBrand(int id)
        throws Exception;
    
    /**
     * 新增商家品牌信息
     * @param brandList
     * @return
     * @throws Exception
     */
    int saveSellerBrand(List<Map<String, Object>> brandList)
        throws Exception;
    
    /**
     * 修改商家主帐号信息
     * @param sellerMaster
     * @return
     * @throws Exception
     */
    int updateSellerMaster(SellerMasterEntity sellerMaster)
        throws Exception;
    
    /**
     * 根据商家真实名称查找商家
     * @param realSellerName
     * @return
     * @throws Exception
     */
    SellerEntity findSellerByRealSellerName(String realSellerName)
        throws Exception;
    
    /**
     * 根据商家Id查找商家从帐号信息
     * @param sellerId
     * @return
     * @throws Exception
     */
    SellerMasterAndSlaveEntity findSellerSlaveBySlaveId(int sellerId)
        throws Exception;
    
    /**
     * 查找所有从商家信息
     * @param masterId
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> finAllSlaveSeller(String masterId)
        throws Exception;
    
    /**
     * 查找所有的被关联商家
     * @param parseInt
     * @return
     * @throws Exception
     */
    List<SellerMasterAndSlaveEntity> findSellerSlaveListByMasterId(int parseInt)
        throws Exception;
    
    /**
     * 新增商家主从关系
     * @param slave
     * @return
     * @throws Exception
     */
    int saveSellerMasterAndSlave(SellerMasterAndSlaveEntity slave)
        throws Exception;
    
    /**
     * 同步商家信息
     * @param para
     * @return
     * @throws Exception
     */
    int synchronousSellerInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计合并商家信息
     * @param para
     * @return
     * @throws Exception
     */
    int countMergeSeller(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查找商家主帐号
     * @param para
     * @return
     * @throws Exception
     */
    List<SellerEntity> findMasterSellerByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 新增商家主帐号信息
     * @param sellerMaster
     * @return
     * @throws Exception
     */
    int saveSellerMaster(SellerMasterEntity sellerMaster)
        throws Exception;
    
    /**
     * 删除商家主从关系
     * @param para
     * @return
     * @throws Exception
     */
    int deleteSellerMasterAndSlave(Map<String, Object> para)
        throws Exception;
    
    /**
     * 更新商家是否使用主帐号
     * @param slaveIdList
     * @param status：1使用，0不使用
     * @return
     */
    int updateSellerOwnerStatus(List<String> slaveIdList, int status);
    
    /**
     * 查询已注册E店宝所有商家ID列表
     * @return
     * @throws Exception
     */
    List<Integer> findEdbSellerIdList()
        throws Exception;

    /**
     * 根据关联表查询商家的类目
     */
    List<Map<String, Object>> findSellerCategoryByRelation(int sellerId);

    /**
     * 当商家类目不存在时
     * 根据基本商品查询商家类目
     */
    List<Map<String, Object>> findSellerCategoryByBaseProduct(int sellerId);

    /** 保存商家类目*/
    int saveSellerCategoryRelation(Map<String, Object> para);

    int deleteAllSellerCategoryBySellerId(int sellerId);
}
