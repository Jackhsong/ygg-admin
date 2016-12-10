package com.ygg.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.ygg.admin.dao.BrandDao;
import com.ygg.admin.dao.ProductDao;
import com.ygg.admin.entity.BrandEntity;
import com.ygg.admin.entity.CategoryFirstEntity;
import com.ygg.admin.service.BrandService;
import com.ygg.admin.service.CategoryService;
import com.ygg.admin.service.SaleFlagService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service("brandService")
public class BrandServiceImpl implements BrandService
{

    Logger logger = Logger.getLogger(BrandServiceImpl.class);

    @Resource
    BrandDao brandDao = null;

    @Resource
    ProductDao productDao = null;
    /**商品分类服务    */
    @Resource(name="categoryService")
    private CategoryService categoryService;
    @Resource
    private SaleFlagService saleFlagService;
    @Override
    public int saveOrUpdate(BrandEntity brand)
        throws Exception
    {
        int resultStatus = -1;
        if (brand.getId() == 0)
        {// 新增
            logger.debug("新增品牌信息：" + brand.toString());
            resultStatus = brandDao.save(brand);
            if (CollectionUtils.isNotEmpty(brand.getCategoryFirstIdList())) {
                for(Integer cateId : brand.getCategoryFirstIdList()) {
                    Map m = new HashMap();
                    m.put("brandId", brand.getId());
                    m.put("categoryFirstId", cateId);
                    brandDao.saveRelationBrandCategory(m);
                }
            }
        }
        else
        {// 修改
         //            BrandEntity oldBrand = brandDao.findBrandById(brand.getId());
         //            int oldReturnDistributionProportionType = oldBrand.getReturnDistributionProportionType();
            logger.info("修改品牌信息：" + brand.toString());
            Map<String, Object> para = new HashMap<>();
            para.put("id", brand.getId());
            para.put("name", brand.getName());
            para.put("image", brand.getImage());
            para.put("enName", brand.getEnName());
            para.put("adImage", brand.getAdImage());
            para.put("isShowActivity", brand.getIsShowActivity());
            para.put("activityDisplayId", brand.getActivityDisplayId());
            para.put("activityType", brand.getActivityType());
            para.put("isAvailable", brand.getIsAvailable());
            para.put("returnDistributionProportionType", brand.getReturnDistributionProportionType());
            para.put("categoryFirstId", brand.getCategoryFirstId());
            para.put("hotSpots", brand.getHotSpots());
            para.put("introduce", brand.getIntroduce());
            para.put("stateId", brand.getStateId());
            logger.debug(para);
            resultStatus = brandDao.updateBrandByPara(para);

            //更新品牌一级分类信息 暴力先删除后插入
            if (CollectionUtils.isNotEmpty(brand.getCategoryFirstIdList())) {
                para.clear();
                para.put("brandId", brand.getId());
                brandDao.deleteRelationBrandCategory(para);
                for(Integer cateId : brand.getCategoryFirstIdList()) {
                    para.clear();
                    para.put("brandId", brand.getId());
                    para.put("categoryFirstId", cateId);
                    brandDao.saveRelationBrandCategory(para);
                }
            }

            if (resultStatus == 1)
            {
                //更新该品牌下所有商品的 返分销毛利百分比类型
                Map<String, Object> updatePara = new HashMap<>();
                updatePara.put("returnDistributionProportionType", brand.getReturnDistributionProportionType());
                updatePara.put("brandId", brand.getId());
                productDao.updateProductReturnDistributionProportionTypeByBrandId(updatePara);
            }
        }
        return resultStatus;
    }

    @SuppressWarnings("unchecked")
	@Override
    public String jsonBrandInfo(Map<String, Object> para)
        throws Exception
    {
        List<BrandEntity> brandList = brandDao.findAllBrandByPara(para);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        int total = 0;
        if (brandList.size() > 0)
        {
           Map<Integer, BrandEntity> idAndBrandMap = new LinkedHashMap<>();
            for (BrandEntity brand : brandList) {  // 多个一级分类设置
                if (!idAndBrandMap.containsKey(brand.getId())) {
//                    if (brand.getRelationCategoryFirstId() != null) {
//                        brand.setCategoryFirstIdList(Lists.newArrayList(brand.getRelationCategoryFirstId()));
//                    }
//                    else {
//                        brand.setCategoryFirstIdList(Lists.newArrayList(brand.getCategoryFirstId()));
//                    }
                    idAndBrandMap.put(brand.getId(), brand);
                } else {
//                    if(brand.getRelationCategoryFirstId() != null) {
//                        List<Integer> cateIds = idAndBrandMap.get(brand.getId()).getCategoryFirstIdList();
//                        cateIds.add(brand.getRelationCategoryFirstId());
//                        idAndBrandMap.get(brand.getId()).setCategoryFirstIdList(cateIds);
//                    }
                }
            }
            brandList = Lists.newArrayList(idAndBrandMap.values());

        	List<CategoryFirstEntity> clist = categoryService.findAllCategoryFirst(null);
        	Map<String, Object> smap = saleFlagService.jsonSaleFlagInfo(null);
        	List<Map<String, Object>> sl = (List<Map<String, Object>>) smap.get("rows");
            for (BrandEntity brand : brandList)
            {
                Map<String, Object> map = new HashMap<>();
                map.put("id", brand.getId());
                map.put("index", brand.getId());
                map.put("name", brand.getName());
                map.put("enName", brand.getEnName());
                String adImage = StringUtils.isEmpty(brand.getAdImage()) ? "" :
                        "<a target=\"_blank\" href=\" "+ brand.getAdImage() +" \"> 查看</a>";
                map.put("adImage" , adImage);
                if ("".equals(brand.getImage()))
                {
                    map.put("image", brand.getImage());
                }
                else
                {
                    map.put("image", "<img style='max-width:100px' src='" + brand.getImage() + "' />");
                }
                List<Integer> cateids = brandDao.findRelationBrandCategoryIdsByBrandId(brand.getId());
                if (CollectionUtils.isNotEmpty(cateids)) {
                    List<String> names = new ArrayList<>();
                    for (Integer cateId : cateids) {
                        for (CategoryFirstEntity cfe : clist) {
                            if (cateId == cfe.getId()) {
                                names.add(cfe.getName());
                            }
                        }
                    }
                    map.put("categoryFirstName", Joiner.on(",").join(names));
                }
                map.put("isAvailable", (brand.getIsAvailable() == (byte)1) ? "可用" : "停用");
                map.put("hotSpots", brand.getHotSpots());
                map.put("introduce", brand.getIntroduce());
                map.put("stateId", brand.getStateId());
                for(Map<String, Object> m : sl){
                	int stateId = Integer.valueOf(m.get("id")+"").intValue();
                	if(stateId == brand.getStateId()){
                		map.put("stateName", m.get("name"));
                	}
                }
                resultList.add(map);
            }
            total = brandDao.countBrandByPara(para);
        }
        resultMap.put("rows", resultList);
        resultMap.put("total", total);
        return JSON.toJSONString(resultMap);
    }

    @Override
    public BrandEntity findBrandById(int id)
        throws Exception
    {
        return brandDao.findBrandById(id);
    }

    @Override
    public List<BrandEntity> findBrandIsAvailable()
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("isAvailable", 1);
        return brandDao.findAllBrandByPara(para);
    }

    @Override
    public List<BrandEntity> findAllBrand(Map<String, Object> para)
        throws Exception
    {
        return brandDao.findAllBrandByPara(para);
    }

    @Override
    public int countBrandByName(Map<String, Object> para)
        throws Exception
    {
        return brandDao.countBrandByName(para);
    }
    @Override
    public int delete(int id) throws Exception{
    	return brandDao.delete(id);
    }

    @Override
	public int updateInfo(Map<String, Object> param) throws Exception {
		return brandDao.update(param);
	}
}
