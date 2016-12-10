package com.ygg.admin.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.dao.ChannelDao;
import com.ygg.admin.dao.PurchaseDao;
import com.ygg.admin.dao.ThirdPartyProductDao;
import com.ygg.admin.dao.UserDao;
import com.ygg.admin.entity.ThirdPartyProductEntity;
import com.ygg.admin.entity.User;
import com.ygg.admin.service.PurchaseStoringService;
import com.ygg.admin.service.ThirdPartyProductService;
import com.ygg.admin.util.CommonConstant;
import com.ygg.admin.util.POIUtil;

@Repository
public class ThirdPartyProductServiceImpl implements ThirdPartyProductService
{
    
    @Resource
    private ThirdPartyProductDao thirdPartyProductDao;
    
    @Resource
    private PurchaseDao purchaseDao;
    
    @Resource
    private UserDao userDao;
    
    @Resource
    private ChannelDao channelDao;
    
    @Resource
    private PurchaseStoringService purchaseStoringService;
    
    @Override
    public String jsonThirdProductInfo(Map<String, Object> para)
        throws Exception
    {
        Object ppName = para.get("ppName");
        List<Integer> ppIdList = new ArrayList<Integer>();
        Map<String, Object> param = new HashMap<String, Object>();
        if (ppName != null)
        {
            param.put("name", ppName.toString());
            List<Map<String, Object>> ppList = purchaseDao.findProviderProductListInfoByParam(param);
            for (Map<String, Object> it : ppList)
            {
                ppIdList.add(Integer.parseInt(it.get("id") == null ? "0" : it.get("id").toString()));
            }
        }
        if (ppIdList.size() > 0)
        {
            para.put("ppIdList", ppIdList);
        }
        
        List<Map<String, Object>> reList = thirdPartyProductDao.findAllThirdPartyProduct(para);
        for (Map<String, Object> it : reList)
        {
            int providerProductId = Integer.parseInt(it.get("providerProductId") == null ? "0" : it.get("providerProductId").toString());
            Map<String, Object> ppMap = purchaseDao.findProviderProductById(providerProductId);
            if (ppMap != null)
            {
                it.put("storageName", ppMap.get("storageName"));
                it.put("productName", ppMap.get("name"));
            }
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", reList);
        result.put("total", thirdPartyProductDao.countThirdPartyProduct(para));
        return JSON.toJSONString(result);
    }
    
    @Override
    public String saveThirdPartyProduct(ThirdPartyProductEntity product)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        String loginName = SecurityUtils.getSubject().getPrincipal() + "";
        User user = userDao.findUserByUsername(loginName);
        if (user != null)
        {
            product.setCreateUser(user.getId());
            product.setUpdateUser(user.getId());
        }
        
        String productCode = null;
        if (product.getProductCode().indexOf("%") > -1)
        {
            String groupCount = product.getProductCode().substring(product.getProductCode().lastIndexOf("%") + 1);
            if (!groupCount.matches("^\\d+$"))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "商品编码中含有%时，%后面必须为数字");
                return JSON.toJSONString(resultMap);
            }
            product.setGroupCount(Integer.parseInt(groupCount));
            productCode = product.getProductCode().substring(0, product.getProductCode().lastIndexOf("%"));
        }
        else
        {
            product.setGroupCount(1);
            productCode = product.getProductCode();
        }
        
        if (StringUtils.isNotEmpty(productCode))
        {
            Map<String, Object> providerProduct = purchaseDao.findProviderProductByBarCode(productCode);
            if (providerProduct == null)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", String.format("条码=%s的采购商品不存在", productCode));
                return JSON.toJSONString(resultMap);
            }
            else
            {
                product.setProviderProductId(Integer.parseInt(providerProduct.get("id").toString()));
            }
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "商品编码必填");
            return JSON.toJSONString(resultMap);
        }
        
        try
        {
            if (thirdPartyProductDao.saveThirdPartyProduct(product) > 0)
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
        catch (Exception e)
        {
            if (e.getMessage().contains("Duplicate") && e.getMessage().contains("uniq_storage_id_and_channel_id_and_product_code"))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "相同渠道相同仓库下商品编码必须唯一");
                return JSON.toJSONString(resultMap);
            }
            else
            {
                throw new Exception(e);
            }
        }
    }
    
    @Override
    public String updateThirdPartyProduct(ThirdPartyProductEntity product)
        throws Exception
    {
        String loginName = SecurityUtils.getSubject().getPrincipal() + "";
        User user = userDao.findUserByUsername(loginName);
        if (user != null)
        {
            product.setUpdateUser(user.getId());
        }
        Map<String, Object> resultMap = new HashMap<>();
        String productCode = null;
        if (product.getProductCode().indexOf("%") > -1)
        {
            String groupCount = product.getProductCode().substring(product.getProductCode().lastIndexOf("%") + 1);
            if (!groupCount.matches("^\\d+$"))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "商品编码中含有%时，%后面必须为数字");
                return JSON.toJSONString(resultMap);
            }
            product.setGroupCount(Integer.parseInt(groupCount));
            productCode = product.getProductCode().substring(0, product.getProductCode().lastIndexOf("%"));
        }
        else
        {
            product.setGroupCount(1);
            productCode = product.getProductCode();
        }
        
        if (StringUtils.isNotEmpty(productCode))
        {
            Map<String, Object> providerProduct = purchaseDao.findProviderProductByBarCode(productCode);
            if (providerProduct == null)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", String.format("条码=%s的采购商品不存在", productCode));
                return JSON.toJSONString(resultMap);
            }
            else
            {
                product.setProviderProductId(Integer.parseInt(providerProduct.get("id").toString()));
            }
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "商品编码必填");
            return JSON.toJSONString(resultMap);
        }
        
        try
        {
            if (thirdPartyProductDao.updateThirdPartyProduct(product) > 0)
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
        catch (Exception e)
        {
            if (e.getMessage().contains("Duplicate") && e.getMessage().contains("uniq_storage_id_and_channel_id_and_product_code"))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "相同渠道相同仓库下商品编码必须唯一");
                return JSON.toJSONString(resultMap);
            }
            else
            {
                throw new Exception(e);
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public String importThirdPartyProduct(MultipartFile file)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> sheetData = POIUtil.getSheetDataAtIndex(file.getInputStream(), 0, true);
        if (sheetData == null || (int)sheetData.get("rowNum") == 0)
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "文件为空请确认！");
            return JSON.toJSONString(resultMap);
        }
        
        List<ThirdPartyProductEntity> productList = new ArrayList<ThirdPartyProductEntity>();
        List<Map<String, Object>> dataList = (List<Map<String, Object>>)sheetData.get("data");
        int index = 1;
        for (Map<String, Object> it : dataList)
        {
            ThirdPartyProductEntity product = new ThirdPartyProductEntity();
            int result = wrap(it, product);
            if (result == 0)
            {
                productList.add(product);
            }
            else if (result == 1)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", String.format("第%d行渠道名称或商品编码不能为空", index));
                return JSON.toJSONString(resultMap);
            }
            else if (result == 2)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", String.format("第%d行渠道名称不存在，请先添加该渠道或确认该渠道是否正确", index));
                return JSON.toJSONString(resultMap);
            }
            else if (result == 3)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", String.format("第%d行采购商品编码不存在，请先添加该编码的采购商品或确认该编码是否正确", index));
                return JSON.toJSONString(resultMap);
            }
            index++;
        }
        int result = 0;
        if (!productList.isEmpty())
        {
            result = thirdPartyProductDao.batchSaveThirdPartyProduct(productList);
        }
        resultMap.put("status", 1);
        resultMap.put("msg", String.format("成功导入%d条", result));
        return JSON.toJSONString(resultMap);
    }
    
    private int wrap(Map<String, Object> it, ThirdPartyProductEntity product)
        throws Exception
    {
        String channelName = it.get("cell0").toString();
        String productCode = it.get("cell1").toString();
        if (StringUtils.isEmpty(channelName) || StringUtils.isEmpty(productCode))
        {
            product = null;
            return 1;
        }
        int channelId = channelDao.findChannelIdByName(channelName);
        if (channelId == CommonConstant.COMMON_NO)
        {
            product = null;
            return 2;
        }
        product.setChannelId(channelId);
        product.setProductCode(productCode);
        if (productCode.indexOf("%") > -1)
        {
            String groupCount = product.getProductCode().substring(product.getProductCode().indexOf("%") + 1);
            product.setGroupCount(Integer.parseInt(groupCount));
            productCode = product.getProductCode().substring(0, product.getProductCode().indexOf("%"));
        }
        Map<String, Object> providerProduct = purchaseDao.findProviderProductByBarCode(productCode);
        if (providerProduct == null)
        {
            product = null;
            return 3;
        }
        else
        {
            product.setProviderProductId(Integer.parseInt(providerProduct.get("id").toString()));
        }
        
        String loginName = SecurityUtils.getSubject().getPrincipal() + "";
        User user = userDao.findUserByUsername(loginName);
        if (user != null)
        {
            product.setCreateUser(user.getId());
            product.setUpdateUser(user.getId());
        }
        return 0;
    }
    
    @Override
    public String updateThirdPartyProductStatus(String ids, int isAvailable)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (StringUtils.isEmpty(ids))
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "请选择要操作的商品");
            return JSON.toJSONString(resultMap);
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("idList", Arrays.asList(ids.split(",")));
        param.put("isAvailable", isAvailable);
        if (thirdPartyProductDao.updateThirdPartyProductStatus(param) > 0)
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
    public String updateThirdPartyProductStock(int id, int stock, int providerProductId, int groupCount)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> para = new HashMap<>();
        para.put("id", id);
        para.put("stock", stock);
        para.put("subtract", stock < 0 ? 1 : 0);
        if (thirdPartyProductDao.updateThirdPartyProductStock(para) > 0)
        {
            Map<String, Object> info = new HashMap<>();
            info.put("providerProductId", providerProductId); //采购商品ID
            info.put("allocationNumber", groupCount * stock);
            if (purchaseStoringService.usedUnallocationStoring(info) > 0)
            {
                resultMap.put("status", 1);
                resultMap.put("msg", "保存成功");
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "保存失败");
            }
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public String updateThirdPartyProductSales(int id, int sales)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> para = new HashMap<>();
        para.put("id", id);
        para.put("sales", sales);
        if (thirdPartyProductDao.updateThirdPartyProductSales(para) > 0)
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
}
