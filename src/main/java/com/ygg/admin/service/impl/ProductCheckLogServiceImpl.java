package com.ygg.admin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygg.admin.config.YggAdminProperties;
import com.ygg.admin.dao.ProductCheckLogDao;
import com.ygg.admin.entity.CategoryEntity;
import com.ygg.admin.entity.CategoryFirstEntity;
import com.ygg.admin.entity.CategorySecondEntity;
import com.ygg.admin.entity.CategoryThirdEntity;
import com.ygg.admin.entity.ProductBaseEntity;
import com.ygg.admin.entity.ProductBaseMobileDetailEntity;
import com.ygg.admin.entity.ProductCheckLogEntity;
import com.ygg.admin.entity.ProductCheckSnapshotEntity;
import com.ygg.admin.service.CategoryService;
import com.ygg.admin.service.ProductBaseService;
import com.ygg.admin.service.ProductCheckLogService;
import com.ygg.admin.service.UserService;
import com.ygg.admin.util.HttpRequestUtil;
import com.ygg.admin.util.ImageUtil;

@Service("productCheckLogService")
public class ProductCheckLogServiceImpl implements ProductCheckLogService {

    Logger log = Logger.getLogger(ProductCheckLogServiceImpl.class);
        
	@Resource
	private ProductCheckLogDao productCheckLogDao;
	@Resource
    private CategoryService categoryService;
	@Resource
    private UserService userService;
	@Resource
	private ProductBaseService productBaseService;
	
	@Override
	public Map<String, Object> findCheckLogList(String submitStartTime, String submitEndTime, String checkStartTime,
			String checkEndTime, String checker, int status, int sellerProductId, int categoryFirstId, int categorySecondId, String name,
			String sellerName,int waitingStatus, int page, int rows) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("submitStartTime", submitStartTime);
		param.put("submitEndTime", submitEndTime);
		param.put("checkStartTime", checkStartTime);
		param.put("checkEndTime", checkEndTime);
		param.put("checker", checker);
		param.put("status", status);
		param.put("sellerProductId", sellerProductId);
		param.put("categoryFirstId", categoryFirstId);
		param.put("categorySecondId", categorySecondId);
		param.put("name", name);
		param.put("sellerName", sellerName);
		param.put("waitingStatus", waitingStatus);
		param.put("start", rows * (page - 1));
		param.put("size", rows);
		List<Map<String, Object>> list = productCheckLogDao.findCheckLogList(param);
		int count = productCheckLogDao.countCheckLogList(param);
		
		if(list != null && list.size() > 0) {
		    // 商品分类ID转化为名称
		    appendCategoryName(list);
		    // 审核人名字
		    appendChecker(list);
		    // 基本商品id
		    appendProductBaseId(list);
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rows", list);
		result.put("total", count);
		return result;
	}
	
	
	/**
	 * 添加审核人名称
	 * 
	 * @param list
	 * @throws Exception
	 */
	private void appendChecker(List<Map<String, Object>> list) throws Exception {
	    List<Map<String, Object>> realNames = null;
        if(list != null && list.size() > 0) {
            List<Object> ids = new ArrayList<Object>();
            for (Map<String, Object> map : list) {
                ids.add(map.get("checker"));
            }
            realNames = userService.findUserByIds(ids);
        }
        if(list != null && list.size() > 0 && realNames != null && realNames.size() > 0) {
            r:for (Map<String, Object> map : list) {
                for (Map<String, Object> realName : realNames) {
                    if((Integer.valueOf((String)map.get("checker"))).intValue() == ((int)realName.get("id"))) {
                        map.put("checker", realName.get("realname"));
                        continue r;
                    }
                }
            }
        }
	}
	
	private void appendProductBaseId(List<Map<String, Object>> list) throws Exception {
	    List<Map<String, Object>> productBaseIdAndSellerProductId = null;
        if(list != null && list.size() > 0) {
            List<Object> ids = new ArrayList<Object>();
            for (Map<String, Object> map : list) {
                ids.add(map.get("sellerProductId"));
            }
            productBaseIdAndSellerProductId = productBaseService.findProductBaseIdBySellerProductId(ids);
        }
        if(list != null && list.size() > 0 && productBaseIdAndSellerProductId != null && productBaseIdAndSellerProductId.size() > 0) {
            r:for (Map<String, Object> map : list) {
                for (Map<String, Object> item : productBaseIdAndSellerProductId) {
                    if((Integer.valueOf(map.get("sellerProductId").toString())).intValue() == (Integer.valueOf(item.get("sellerProductId").toString()).intValue())) {
                        map.put("productBaseId", item.get("productBaseId"));
                        continue r;
                    }
                }
            }
        }
	}
	
	/**
	 * 转化分类信息
	 * 
	 * @param list
	 * @throws Exception
	 */
	private void appendCategoryName(List<Map<String, Object>> list) throws Exception {
	    for (Map<String, Object> map : list) {
            StringBuffer sb = new StringBuffer();
            Object f = map.get("category_first_id");
            if(f != null) {
                CategoryFirstEntity fen = categoryService.findCategoryFirstById(Long.valueOf(f.toString()).intValue());
                if(fen != null)
                    sb.append(fen.getName());
            }
            Object s = map.get("category_second_id");
            if(s != null) {
                CategorySecondEntity sen = categoryService.findCategorySecondById(Long.valueOf(s.toString()).intValue());
                if(sen != null)
                    sb.append(">").append(sen.getName());
            }
            Object t = map.get("category_third_id");
            if(t != null) {
                CategoryThirdEntity ten = categoryService.findCategoryThirdById(Long.valueOf(t.toString()).intValue());
                if(ten != null)
                    sb.append(">").append(ten.getName());
            }
            map.put("categoryName", sb.toString());
        }
	}

	@Override
	public int saveCheckSnapshot(ProductCheckSnapshotEntity entity) throws Exception {
		// 审核通过
		if (1 == entity.getType())
			entity.setStatus((short) 4);
		else
			entity.setStatus((short) 3);

		int result = productCheckLogDao.saveCheckSnapshot(entity);
		if (result < 1)
			throw new RuntimeException("保存审核快照失败");

//		result = syncFreightTemplate(entity);
//		if (result < 1)
//			throw new RuntimeException("保存审核快照失败");
		result = syncAreaTemplate(entity);
		if (result < 1)
			throw new RuntimeException("保存审核快照失败");
		
		ProductCheckLogEntity checkLog = new ProductCheckLogEntity();
		BeanUtils.copyProperties(entity, checkLog);
		checkLog.setProductCheckSnapshotId(entity.getId());
		checkLog.setSubmitTime(entity.getSubmitTime());
		result = productCheckLogDao.saveChackLog(checkLog);
		if (result < 1)
			throw new RuntimeException("保存审核快照失败");
		
		// 审核通过，维护排期状态
		if(entity.getStatus() == 4) {
		    updateWaitingStatus(0, checkLog.getSellerProductId());
		}
		
		List<NameValuePair> snnsParam = new ArrayList<NameValuePair>();
	    snnsParam.add(new BasicNameValuePair("id", String.valueOf(entity.getSellerProductId())));
	    snnsParam.add(new BasicNameValuePair("status", String.valueOf(entity.getStatus())));
	    snnsParam.add(new BasicNameValuePair("checkContent", entity.getCheckContent()));
	    HttpRequestUtil.reqPost(YggAdminProperties.getInstance().getPropertie("check_url") + "sellerProduct/infoCheck", snnsParam, true);
		return result;
	}
	
	
	/**
	 * 维护排期状态
	 * 
	 * @param productBaseId
	 * @param sellerProductId
	 * @return
	 * @throws Exception
	 */
	public int updateWaitingStatus(int productBaseId, int sellerProductId) throws Exception {
	    int result = 0;
	    if(productBaseId > 0) {
	        // 特卖商品上架情况
	        // 这里为了不印象特卖商品上下架，try-catch
	        try {
	            Map<String, Object> info = productCheckLogDao.findWaitingStatusByProductBaseId(productBaseId);
	            if(info != null && info.size() > 0 && !StringUtils.equals("3", info.get("waitingStatus").toString())) {
	                updateWaitingStatus(Integer.valueOf(info.get("sellerProductId").toString()), "3");
	            }
	        } catch(Exception e) {
	            log.error(String.format("这里为了不印象特卖商品上下架，try-catch。参数信息：productBaseId=%s,sellerProductId=%s", productBaseId, sellerProductId));
	            log.error(e);
	        }
	        
	    } else if (sellerProductId > 0) {
	        // 保存审核快照情况
	        result = productCheckLogDao.findWaitingStatusBySellerProductId(sellerProductId);
	        if(result < 1) {
	            // 首次审核通过
	            updateWaitingStatus(sellerProductId, "1");
	        } else {
	            // 审核通过，沿用之前的排期状态
                updateWaitingStatus(sellerProductId, String.valueOf(result));
	        }
	    }
	    return result;
	}
	
	public int updateWaitingStatus(int sellerProductId, String waitingStatus) throws Exception {
	    Map<String, Object> param = new HashMap<String, Object>();
	    param.put("waitingStatus", waitingStatus);
	    param.put("sellerProductId", sellerProductId);
	    return productCheckLogDao.updateWaitingStatus(param);
	}
	
	/**
	 * 同步配送地区模板
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	private int syncAreaTemplate(ProductCheckSnapshotEntity entity) throws Exception {
		Map<String, Object> map = getAreaTemplate(entity);
		map.put("productCheckSnapshotId", entity.getId());
		int result = productCheckLogDao.saveProductCheckDeliverAreaTemplateSnapshot(map);
		if (result < 1)
			throw new RuntimeException("保存审核快照失败");
		return result;
	}

	/**
	 * 读取商家配送模板
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	private Map<String, Object> getAreaTemplate(ProductCheckSnapshotEntity entity) {
		List<NameValuePair> areaTemplateParam = new ArrayList<NameValuePair>();
		areaTemplateParam.add(new BasicNameValuePair("areaTemplateId", String.valueOf(entity.getSellerDeliverAreaTemplateId())));
		String areaTemplate = HttpRequestUtil.reqPost(YggAdminProperties.getInstance().getPropertie("check_url") + "sellerDeliverAreaTemplate/templateInfo", areaTemplateParam, true);
		JSONObject obj = JSON.parseObject(areaTemplate);
		JSONArray arr = obj.getJSONArray("data");
		JSONObject template = arr.getJSONObject(0);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", template.get("modelName"));
		map.put("areaName", template.get("areaName"));
		map.put("areaDesc", template.get("areaDesc"));
		map.put("type", getDesc(template.getString("ruleType")));
		StringBuffer sb = new StringBuffer();
		JSONArray jsonArray = template.getJSONArray("liwaiArea");
		for (int j = 0; j < jsonArray.size(); j++) {
			JSONObject model = jsonArray.getJSONObject(j);
			sb.append(model.get("areaNames"));
		}
		map.put("liwaiArea", sb.toString());
		return map;
	}
	
	private String getDesc(String type) {
		if(StringUtils.equals(type, "1"))
			return "仅发以下地区";
		if(StringUtils.equals(type, "2"))
			return "以下地区不发货";
		if(StringUtils.equals(type, "3"))
			return "仅发以下地区但有例外地区";
		if(StringUtils.equals(type, "4"))
			return "以下地区不发货但有例外地区";
		return "";
	}
	
	/**
	 * 同步运费模板
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	public int syncFreightTemplate(ProductCheckSnapshotEntity entity) throws Exception {
		// 读取商家后台的运费模板信息
		Map<String, Object> map = getFreightTemplate(entity);
		map.put("productCheckSnapshotId", entity.getId());
		int result = productCheckLogDao.saveProductCheckFreightTemplateSnapshot(map);
		if (result < 1)
			throw new RuntimeException("保存审核快照失败");
		return result;
	}

	/**
	 * 读取商家运费模板
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	private Map<String, Object> getFreightTemplate(ProductCheckSnapshotEntity entity) {
		List<NameValuePair> freightTemplateParam = new ArrayList<NameValuePair>();
		freightTemplateParam.add(new BasicNameValuePair("freightTemplateId", String.valueOf(entity.getSellerFreightTemplateId())));
		String freightTemplate = HttpRequestUtil.reqPost(YggAdminProperties.getInstance().getPropertie("check_url") + "freight/templateInfo", freightTemplateParam, true);
		JSONObject obj = JSON.parseObject(freightTemplate);
		Map<String, Object> map = new HashMap<String, Object>();
		JSONArray arr = obj.getJSONArray("data");
		JSONObject template = arr.getJSONObject(0);
		map.put("name", template.get("modelName"));
		JSONArray jsonArray = template.getJSONArray("model");
		StringBuffer sb = new StringBuffer();
		for (int j = 0; j < jsonArray.size(); j++) {
			JSONObject model = jsonArray.getJSONObject(j);
			sb.append(model.get("money")).append(";");
			sb.append(model.get("cityNames"));
			if(j < jsonArray.size() - 1)
				sb.append("|");
		}
		map.put("area", sb.toString());
		return map;
	}
	
	@Override
	public Map<String, Object> findCheckInfo(int id) throws Exception {
		Map<String, Object> result = productCheckLogDao.findCheckInfo(id);
		appendWidth(result, "image1");
		appendWidth(result, "image2");
		appendWidth(result, "image3");
		appendWidth(result, "image4");
		appendWidth(result, "image5");
		List<Map<String, Object>> detailImg = productCheckLogDao.findCheckDetailImg(id);
		result.put("detailImage", detailImg);
		
		// 商品类目信息，存在一对多的情况
		List<Map<String, String>> categoryInfos = productCheckLogDao.findCategoryInfoBySellerProductId(id);
		result.put("categoryInfos", categoryInfos);
		
        List<Object> list = new ArrayList<Object>(1);
        list.add(id);
        List<Map<String, Object>> productBaseList = productBaseService.findProductBaseIdBySellerProductId(list);
        if(productBaseList == null || productBaseList.size() < 1)
        {
            result.put("productBaseInfo", null);
        } else {
            Map<String, Object> map = productBaseList.get(0);
            Object productBaseId = map.get("productBaseId");
            ProductBaseEntity en = productBaseService.queryProductBaseById(Integer.valueOf(productBaseId.toString()));
            result.put("productBaseInfo", en);
            List<ProductBaseMobileDetailEntity> detailImage = productBaseService.findProductBaseMobileDetailsByProductBaseId(Integer.valueOf(productBaseId.toString()));
            result.put("productBaseDetailImage", detailImage);
            List<CategoryEntity> categoryList = categoryService.findCategoryByProductBaseId(Integer.valueOf(productBaseId.toString()));
            result.put("productBaseCategoryInfos", categoryList);
        }
		return result;
	}
	
	private void appendWidth(Map<String, Object> result, String key) {
	    String src = String.valueOf(result.get(key) == null ? "" : result.get(key));
	    String newsrc = StringUtils.substringBeforeLast(src, "!");
	    if(StringUtils.isNotBlank(newsrc)) {
	        Map<String, Object> propertysMap = ImageUtil.getProperty(newsrc);
	        result.put(key + "_hight", Short.valueOf((String)propertysMap.get("height")));
	        result.put(key + "_width", Short.valueOf((String)propertysMap.get("width")));
	    }
	}

	@Override
	public Map<String, Object> findSnapshotInfo(int id) throws Exception {
		Map<String, Object> result = productCheckLogDao.findSnapshotInfo(id);
		String detailImgs = (String)result.get("detail_image");
		if(StringUtils.isNotBlank(detailImgs)) {
			String[] arr = StringUtils.split(detailImgs, ";");
			result.put("detailImage", arr);
		}
		return result;
	}

	@Override
	public Map<String, Object> findDeliverAreaTemplate(int snapshotId, int type) {
		if(type == 1) {
			return productCheckLogDao.findDeliverAreaTemplate(snapshotId);
		} else {
			ProductCheckSnapshotEntity entity = new ProductCheckSnapshotEntity();
			entity.setSellerDeliverAreaTemplateId(snapshotId);
			return getAreaTemplate(entity);
		}
	}

	@Override
	public Map<String, Object> findFreightTemplate(int snapshotId, int type) {
		if(type == 1) {
			return productCheckLogDao.findFreightTemplate(snapshotId);
		} else {
			ProductCheckSnapshotEntity entity = new ProductCheckSnapshotEntity();
			entity.setSellerFreightTemplateId(snapshotId);
			return getFreightTemplate(entity);
		}
	}

}
