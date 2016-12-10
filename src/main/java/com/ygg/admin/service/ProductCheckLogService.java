package com.ygg.admin.service;

import java.util.Map;

import com.ygg.admin.entity.ProductCheckSnapshotEntity;

public interface ProductCheckLogService {

	/**
	 * 审核列表
	 * 
	 * @param submitStartTime 提交审核时间
	 * @param submitEndTime
	 * @param checkStartTime 审核时间
	 * @param checkEndTime
	 * @param checker 审核负责人
	 * @param status 审核状态
	 * @param sellerProductId 商品ID
	 * @param categoryFirstId
	 * @param categorySecondId 商品类目
	 * @param name 商品名称
	 * @param sellerName 商家名称
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> findCheckLogList(String submitStartTime, String submitEndTime, String checkStartTime, String checkEndTime,
			String checker, int status, int sellerProductId, int categoryFirstId, int categorySecondId, String name, String sellerName,int waitingStatus, int page,
			int rows) throws Exception;

	/**
	 * 保存审核快照
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	int saveCheckSnapshot(ProductCheckSnapshotEntity entity) throws Exception;

	/**
	 * 查询审核信息
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> findCheckInfo(int id) throws Exception;
	
	/**
	 * 快照信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> findSnapshotInfo(int id) throws Exception;
	
	/**
	 * 查询配送地区快照
	 * 
	 * @param snapshotId
	 * @param type
	 * 
	 * @return
	 */
	Map<String, Object> findDeliverAreaTemplate(int snapshotId, int type);

	/**
	 * 查询运费模板快照
	 * 
	 * @param snapshotId
	 * @param type
	 * 
	 * @return
	 */
	Map<String, Object> findFreightTemplate(int snapshotId, int type);
	
	int updateWaitingStatus(int productBaseId, int sellerProductId) throws Exception;
	
	int updateWaitingStatus(int sellerProductId, String waitingStatus) throws Exception;
}
