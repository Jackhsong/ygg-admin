package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.ProductCheckLogDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.ProductCheckLogEntity;
import com.ygg.admin.entity.ProductCheckSnapshotEntity;

@Repository("productCheckLogDao")
public class ProductCheckLogDaoImpl extends BaseDaoImpl implements ProductCheckLogDao {

	@Override
	public List<Map<String, Object>> findCheckLogList(Map<String, Object> para) throws Exception {
		List<Map<String, Object>> reList = this.getSqlSessionRead().selectList("ProductCheckLogMapper.findCheckLogList", para);
		return reList == null ? new ArrayList<Map<String, Object>>() : reList;
	}
	
	public int countCheckLogList(Map<String, Object> para) throws Exception {
		return this.getSqlSessionRead().selectOne("ProductCheckLogMapper.countCheckLogList", para);
	}

	@Override
	public int saveChackLog(ProductCheckLogEntity entity) throws Exception {
		return this.getSqlSession().insert("ProductCheckLogMapper.saveChackLog", entity);
	}

	@Override
	public int saveCheckSnapshot(ProductCheckSnapshotEntity entity) throws Exception {
		return this.getSqlSession().insert("ProductCheckLogMapper.saveCheckSnapshot", entity);
	}

	@Override
	public Map<String, Object> findCheckInfo(int id) throws Exception {
		Map<String, Object> result = this.getSqlSessionRead().selectOne("ProductCheckLogMapper.findCheckInfo", id);
		return result == null ? new HashMap<String, Object>() : result;
	}
	
	@Override
	public List<Map<String, Object>> findCheckDetailImg(int id) throws Exception {
		List<Map<String, Object>> result = this.getSqlSessionRead().selectList("ProductCheckLogMapper.findCheckDetailImg", id);
		return result == null ? new ArrayList<Map<String, Object>>() : result;
	}

	@Override
	public Map<String, Object> findSnapshotInfo(int id) throws Exception {
		Map<String, Object> result = this.getSqlSessionRead().selectOne("ProductCheckLogMapper.findSnapshotInfo", id);
		return result == null ? new HashMap<String, Object>() : result;
	}

	@Override
	public int saveProductCheckDeliverAreaTemplateSnapshot(Map<String, Object> param) {
		return this.getSqlSession().insert("ProductCheckLogMapper.saveProductCheckDeliverAreaTemplateSnapshot", param);
	}

	@Override
	public int saveProductCheckFreightTemplateSnapshot(Map<String, Object> param) {
		return this.getSqlSession().insert("ProductCheckLogMapper.saveProductCheckFreightTemplateSnapshot", param);
	}

	@Override
	public Map<String, Object> findDeliverAreaTemplate(int snapshotId) {
		Map<String, Object> result = this.getSqlSessionRead().selectOne("ProductCheckLogMapper.findDeliverAreaTemplate", snapshotId);
		return result == null ? new HashMap<String, Object>() : result;
	}

	@Override
	public Map<String, Object> findFreightTemplate(int id) {
		Map<String, Object> result = this.getSqlSessionRead().selectOne("ProductCheckLogMapper.findFreightTemplate", id);
		return result == null ? new HashMap<String, Object>() : result;
	}

    @Override
    public int updateWaitingStatus(Map<String, Object> param)
        throws Exception
    {
        return this.getSqlSession().update("ProductCheckLogMapper.updateWaitingStatus", param);
    }

    @Override
    public int findWaitingStatusBySellerProductId(int sellerProductId)
        throws Exception
    {
        Object obj = this.getSqlSessionRead().selectOne("ProductCheckLogMapper.findWaitingStatusBySellerProductId", sellerProductId);
        return obj == null ? -1 : (int)obj;
    }

    @Override
    public Map<String, Object> findWaitingStatusByProductBaseId(int productBaseId)
        throws Exception
    {
        Map<String, Object> obj = this.getSqlSessionRead().selectOne("ProductCheckLogMapper.findWaitingStatusByProductBaseId", productBaseId);
        return obj == null ? new HashMap<String, Object>() : obj;
    }
	
    @Override
    public List<Map<String, String>> findCategoryInfoBySellerProductId(int productSellerId) throws Exception {
        return this.getSqlSessionRead().selectList("ProductCheckLogMapper.findCategoryInfoBySellerProductId", productSellerId);
    }

}
