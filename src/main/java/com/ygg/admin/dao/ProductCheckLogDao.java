package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.ProductCheckLogEntity;
import com.ygg.admin.entity.ProductCheckSnapshotEntity;

public interface ProductCheckLogDao {


	/**
     * 查询审核列表
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findCheckLogList(Map<String, Object> para) throws Exception;
    int countCheckLogList(Map<String, Object> para) throws Exception;
    /**
     * 保存审核列表
     * @param entity
     * @return
     * @throws Exception
     */
    int saveChackLog(ProductCheckLogEntity entity) throws Exception;
    
    /**
     * 保存审核快照
     * @param entity
     * @return
     * @throws Exception
     */
    int saveCheckSnapshot(ProductCheckSnapshotEntity entity) throws Exception;
    
    /**
     * 查询审核信息
     * @param id
     * @return
     * @throws Exception
     */
    Map<String, Object> findCheckInfo(int id) throws Exception;
    
    /**
     * 商品图片详情
     * @param id
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findCheckDetailImg(int id) throws Exception;
    
    /**
     * 审核快照
     * @param id
     * @return
     * @throws Exception
     */
    Map<String, Object> findSnapshotInfo(int id) throws Exception;
    
    /**
     * 保存配送地区快照
     * @param sellerDeliverAreaTemplateId 商品配送模板
     * @param snapshotId 快照ID
     * @return
     */
    int saveProductCheckDeliverAreaTemplateSnapshot(Map<String, Object> param);
    /**
     * 保存运费模板快照
     * @return
     */
    int saveProductCheckFreightTemplateSnapshot(Map<String, Object> param);
    
	/**
	 * 查询配送地区快照
	 * 
	 * @param snapshotId
	 * @return
	 */
	Map<String, Object> findDeliverAreaTemplate(int snapshotId);

	/**
	 * 查询运费模板快照
	 * 
	 * @param snapshotId
	 * @return
	 */
	Map<String, Object> findFreightTemplate(int snapshotId);
    
	int updateWaitingStatus(Map<String, Object> param) throws Exception;
	
	int findWaitingStatusBySellerProductId(int sellerProductId) throws Exception;
	
	Map<String, Object> findWaitingStatusByProductBaseId(int productBaseId) throws Exception;
	
	List<Map<String, String>> findCategoryInfoBySellerProductId(int productSellerId) throws Exception;
}
