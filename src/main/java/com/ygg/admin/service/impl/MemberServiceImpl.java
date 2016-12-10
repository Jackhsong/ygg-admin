package com.ygg.admin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.code.ProductEnum;
import com.ygg.admin.dao.ActivitiesCommonDao;
import com.ygg.admin.dao.CustomActivitiesDao;
import com.ygg.admin.dao.MemberDao;
import com.ygg.admin.dao.PageDao;
import com.ygg.admin.dao.ProductDao;
import com.ygg.admin.entity.ActivitiesCommonEntity;
import com.ygg.admin.entity.CustomActivityEntity;
import com.ygg.admin.entity.MemberBannerEntity;
import com.ygg.admin.entity.MemberLevelProductEntity;
import com.ygg.admin.entity.ProductEntity;
import com.ygg.admin.service.MemberService;
import com.ygg.admin.util.CommonConstant;

@Service
public class MemberServiceImpl implements MemberService
{
    @Resource
    private MemberDao memberDao;
    
    @Resource
    private ProductDao productDao;
    
    @Resource
    private ActivitiesCommonDao activitiesCommonDao;
    
    @Resource
    private CustomActivitiesDao customActivitiesDao;
    
    @Resource
    private PageDao pageDao;
    
    @Override
    public String jsonMemberLevelInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("rows", memberDao.findAllMemberLevel(para));
        resultMap.put("total", memberDao.countMemberLevel(para));
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public String updateMemberLevelDisplayStatus(List<String> idList, int isDisplay)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("idList", idList);
        para.put("isDisplay", isDisplay);
        if (memberDao.updateMemberLevelDisplayStatus(para) > 0)
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "保存成功");
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public String jsonMemberProductInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("rows", memberDao.findAllMemberProduct(para));
        resultMap.put("total", memberDao.countMemberProduct(para));
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public String insertMemberProduct(int levelId, int level, int productId, int point, int sequence, int limitNum, int isSupportCashBuy)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            ProductEntity pe = productDao.findProductByID(productId, null);
            if (pe == null)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", String.format("ID=%d的商品不存在", productId));
                return JSON.toJSONString(resultMap);
            }
            if (point <= 0)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "换购积分必须大于0");
                return JSON.toJSONString(resultMap);
            }
            if (sequence < 0)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "排序值必须大于或等于0");
                return JSON.toJSONString(resultMap);
            }
            if (limitNum <= 0)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "限购数量必须大于0");
                return JSON.toJSONString(resultMap);
            }
            
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("levelId", levelId);
            para.put("level", level);
            para.put("productId", productId);
            para.put("point", point);
            para.put("sequence", sequence);
            para.put("limitNum", limitNum);
            para.put("isSupportCashBuy", isSupportCashBuy);
            if (memberDao.insertMemberProduct(para) > 0)
            {
                resultMap.put("status", 1);
                resultMap.put("msg", "新增成功");
                return JSON.toJSONString(resultMap);
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "新增失败");
                return JSON.toJSONString(resultMap);
            }
        }
        catch (Exception e)
        {
            if (e.getMessage().contains("uniq_product_id") && e.getMessage().contains("Duplicate"))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "新增失败，ID=" + productId + "的商品已经存在");
                return JSON.toJSONString(resultMap);
            }
            else
            {
                throw new Exception(e);
            }
        }
        
    }
    
    @Override
    public String updateMemberProduct(int id, int levelId, int level, int productId, int point, int sequence, int limitNum, int isSupportCashBuy)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        ProductEntity pe = productDao.findProductByID(productId, null);
        if (pe == null)
        {
            resultMap.put("status", 0);
            resultMap.put("msg", String.format("ID=%d的商品不存在", productId));
            return JSON.toJSONString(resultMap);
        }
        if (point <= 0)
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "换购积分必须大于0");
            return JSON.toJSONString(resultMap);
        }
        if (sequence < 0)
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "排序值必须大于或等于0");
            return JSON.toJSONString(resultMap);
        }
        if (limitNum <= 0)
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "限购数量必须大于0");
            return JSON.toJSONString(resultMap);
        }
        
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        para.put("levelId", levelId);
        para.put("level", level);
        para.put("productId", productId);
        para.put("point", point);
        para.put("sequence", sequence);
        para.put("limitNum", limitNum);
        para.put("isSupportCashBuy", isSupportCashBuy);
        if (memberDao.updateMemberProduct(para) > 0)
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "修改成功");
            return JSON.toJSONString(resultMap);
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "修改失败");
            return JSON.toJSONString(resultMap);
        }
    }
    
    @Override
    public String deleteMemberProduct(List<String> idList)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (memberDao.deleteMemberProduct(idList) > 0)
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "删除成功");
            return JSON.toJSONString(resultMap);
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "删除失败");
            return JSON.toJSONString(resultMap);
        }
    }
    
    @Override
    public String updateMemberProductDisplayStatus(List<String> idList, int isDisplay)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("idList", idList);
        para.put("isDisplay", isDisplay);
        if (memberDao.updateMemberProductDisplayStatus(para) > 0)
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "保存成功");
            return JSON.toJSONString(resultMap);
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "删除失败");
            return JSON.toJSONString(resultMap);
        }
    }
    
    @Override
    public String updateMemberProductSequence(int id, int sequence)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        para.put("sequence", sequence);
        if (memberDao.updateMemberProduct(para) > 0)
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "修改成功");
            return JSON.toJSONString(resultMap);
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "修改失败");
            return JSON.toJSONString(resultMap);
        }
    }
    
    @Override
    public String jsonMemberBannerInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("rows", wrap(memberDao.findAllMemberBanner(para)));
        resultMap.put("total", memberDao.countMemberBanner(para));
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public String saveMemberBanner(MemberBannerEntity banner)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String result = validateParam(banner);
        if (JSON.parseObject(result).getIntValue("status") == CommonConstant.COMMON_YES)
        {
            if (memberDao.saveMemberBanner(banner) > 0)
            {
                resultMap.put("status", 1);
                resultMap.put("msg", "新增成功");
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "新增失败");
            }
            return JSON.toJSONString(resultMap);
        }
        else
        {
            return result;
        }
    }
    
    @Override
    public String updateMemberBanner(MemberBannerEntity banner)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String result = validateParam(banner);
        if (JSON.parseObject(result).getIntValue("status") == CommonConstant.COMMON_YES)
        {
            if (memberDao.updateMemberBanner(banner) > 0)
            {
                resultMap.put("status", 1);
                resultMap.put("msg", "修改成功");
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "修改失败");
            }
            return JSON.toJSONString(resultMap);
        }
        else
        {
            return result;
        }
    }
    
    @Override
    public String deleteMemberBanner(List<String> idList)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (memberDao.deleteMemberBanner(idList) > 0)
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "删除成功");
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "删除失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public String updateMemberBannerDisplayStatus(List<String> idList, int isDisplay)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("idList", idList);
        para.put("isDisplay", isDisplay);
        if (memberDao.updateMemberBannerDisplayStatus(para) > 0)
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "保存成功");
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public String updateMemberBannerSequence(int id, int sequence)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        para.put("sequence", sequence);
        if (memberDao.updateMemberBannerSequence(para) > 0)
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "保存成功");
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public MemberBannerEntity findMemberBannerById(int id)
        throws Exception
    {
        return memberDao.findMemberBannerById(id);
    }
    
    private List<Map<String, Object>> wrap(List<MemberBannerEntity> bannerList)
        throws Exception
    {
        List<Map<String, Object>> reList = new ArrayList<Map<String, Object>>();
        for (MemberBannerEntity banner : bannerList)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", banner.getId());
            map.put("type", banner.getType());
            map.put("displayId", banner.getDisplayId());
            map.put("isDisplay", banner.getIsDisplay());
            map.put("desc", banner.getDesc());
            map.put("image", banner.getImage());
            map.put("sequence", banner.getSequence());
            if (banner.getType() == 1 || banner.getType() == 7)//特卖商品
            {
                ProductEntity pe = productDao.findProductByID(banner.getDisplayId(), ProductEnum.PRODUCT_TYPE.SALE.getCode());
                map.put("displayName", pe == null ? "" : pe.getName());
            }
            else if (banner.getType() == 2)//组合
            {
                ActivitiesCommonEntity ac = activitiesCommonDao.findAcCommonById(banner.getDisplayId());
                map.put("displayName", ac == null ? "" : ac.getName());
            }
            else if (banner.getType() == 3)//自定义活动
            {
                CustomActivityEntity ca = customActivitiesDao.findCustomActivitiesId(banner.getDisplayId());
                map.put("displayName", ca == null ? "" : ca.getRemark());
            }
            else if (banner.getType() == 4 || banner.getType() == 8)//商城商品
            {
                ProductEntity pe = productDao.findProductByID(banner.getDisplayId(), ProductEnum.PRODUCT_TYPE.MALL.getCode());
                map.put("displayName", pe == null ? "" : pe.getName());
            }
            else if (banner.getType() == 5)//原生自定义页面
            {
                Map<String, Object> page = pageDao.findPageById(banner.getDisplayId());
                map.put("displayName", page == null ? "" : page.get("name"));
            }
            else
            {
                map.put("displayName", "");
            }
            reList.add(map);
        }
        return reList;
    }
    
    private String validateParam(MemberBannerEntity banner)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (banner.getType() == 1)
        {
            ProductEntity pe = productDao.findProductByID(banner.getDisplayId(), null);
            if (pe == null)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", String.format("ID=%d的单品不存在", banner.getDisplayId()));
                return JSON.toJSONString(resultMap);
            }
            
            MemberLevelProductEntity mp = memberDao.findMemberLevelProductByProductId(pe.getId());
            if (mp == null)
            {
                if (pe.getType() == ProductEnum.PRODUCT_TYPE.SALE.getCode())
                {
                    banner.setType(Byte.valueOf("1"));
                }
                else if (pe.getType() == ProductEnum.PRODUCT_TYPE.MALL.getCode())
                {
                    banner.setType(Byte.valueOf("4"));
                }
            }
            else
            {
                if (pe.getType() == ProductEnum.PRODUCT_TYPE.SALE.getCode())
                {
                    banner.setType(Byte.valueOf("7"));
                }
                else if (pe.getType() == ProductEnum.PRODUCT_TYPE.MALL.getCode())
                {
                    banner.setType(Byte.valueOf("8"));
                }
            }
        }
        else if (banner.getType() == 2)
        {
            ActivitiesCommonEntity ac = activitiesCommonDao.findAcCommonById(banner.getDisplayId());
            if (ac == null)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", String.format("ID=%d的组合不存在", banner.getDisplayId()));
                return JSON.toJSONString(resultMap);
            }
        }
        else if (banner.getType() == 3)
        {
            CustomActivityEntity ca = customActivitiesDao.findCustomActivitiesId(banner.getDisplayId());
            if (ca == null)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", String.format("ID=%d的自定义活动不存在", banner.getDisplayId()));
                return JSON.toJSONString(resultMap);
            }
        }
        else if (banner.getType() == 5)
        {
            Map<String, Object> page = pageDao.findPageById(banner.getDisplayId());
            if (page == null)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", String.format("ID=%d的原生自定义页面不存在", banner.getDisplayId()));
                return JSON.toJSONString(resultMap);
            }
        }
        else if (banner.getType() == 6)
        {
            banner.setDisplayId(0);
        }
        else if (banner.getType() == 7)
        {
            ProductEntity pe = productDao.findProductByID(banner.getDisplayId(), ProductEnum.PRODUCT_TYPE.SALE.getCode());
            if (pe == null)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", String.format("ID=%d的单品不存在", banner.getDisplayId()));
                return JSON.toJSONString(resultMap);
            }
            
            MemberLevelProductEntity mp = memberDao.findMemberLevelProductByProductId(pe.getId());
            if (mp == null)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", String.format("ID=%d的单品不是积分商品", banner.getDisplayId()));
                return JSON.toJSONString(resultMap);
            }
        }
        else if (banner.getType() == 8)
        {
            ProductEntity pe = productDao.findProductByID(banner.getDisplayId(), ProductEnum.PRODUCT_TYPE.MALL.getCode());
            if (pe == null)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", String.format("ID=%d的单品不存在", banner.getDisplayId()));
                return JSON.toJSONString(resultMap);
            }
            
            MemberLevelProductEntity mp = memberDao.findMemberLevelProductByProductId(pe.getId());
            if (mp == null)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", String.format("ID=%d的单品不是积分商品", banner.getDisplayId()));
                return JSON.toJSONString(resultMap);
            }
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", String.format("不存在的类型Banner%d", banner.getType()));
            return JSON.toJSONString(resultMap);
        }
        resultMap.put("status", 1);
        return JSON.toJSONString(resultMap);
    }
}
