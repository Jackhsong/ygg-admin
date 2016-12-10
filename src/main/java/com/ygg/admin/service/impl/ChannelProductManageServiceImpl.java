package com.ygg.admin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.ygg.admin.code.WareHouseEnum;
import com.ygg.admin.dao.ChannelProductManageDao;
import com.ygg.admin.entity.ChannelProductEntity;
import com.ygg.admin.service.ChannelProductManageService;
import com.ygg.admin.util.POIUtil;

@Repository
public class ChannelProductManageServiceImpl implements ChannelProductManageService
{

    @Resource
    private ChannelProductManageDao channelProductManageDao;
    
    @Override
    public Map<String, Object> addChannelProduct(ChannelProductEntity product)
            throws Exception
    {
        Map<String,Object> statusMap = validateProductCode(product);
        if(channelProductManageDao.addChannelProduct(product)>0){
            statusMap.put("status", 1);
            statusMap.put("msg", "保存成功");
        }
        return statusMap;
    }
    
    /***
     * 校验ProductCode的合法性
     * @param product
     * @return
     */
    private Map<String, Object> validateProductCode(ChannelProductEntity product){
        String productCode = product.getProductCode();
        Map<String,Object> statusMap = new HashMap<String,Object>();
        if (StringUtils.indexOf(productCode, "%") > -1)
        {
            String assembleCount = StringUtils.substringAfterLast(productCode, "%");
            String reallyProductCode = StringUtils.substringBeforeLast(productCode, "%");
            if (!assembleCount.matches("^\\d+$"))
            {
                statusMap.put("status", 0);
                statusMap.put("msg", "商品编码中含有%时，%后面必须为数字");
                return statusMap;
            }
            //TODO 是否需要判断商品编码一个条件的 出商品名称
            if(StringUtils.isEmpty(reallyProductCode)){
                statusMap.put("status", 0);
                statusMap.put("msg", "商品编码中%前需有值");
                return statusMap;
            }
            
            product.setAssembleCount(Integer.valueOf(assembleCount));
            product.setProductCode(productCode);
        }
        return statusMap;
    }

    @Override
    public Map<String, Object> getProductName(Map<String, Object> para) throws Exception
    {
        Map<String,Object> statusMap = new HashMap<String,Object>();
        String productCode = String.valueOf(para.get("productCode"));
        if (StringUtils.indexOf(productCode, "%") > -1)
        {
            String assembleCount = StringUtils.substringAfterLast(productCode, "%");
            String reallyProductCode = StringUtils.substringBeforeLast(productCode, "%");
            if (!assembleCount.matches("^\\d+$"))
            {
                statusMap.put("status", 0);
                statusMap.put("msg", "商品编码中含有%时，%后面必须为数字");
                return statusMap;
            }
            
            //TODO 是否需要判断商品编码一个条件的 出商品名称
            if(StringUtils.isEmpty(reallyProductCode)){
                statusMap.put("status", 0);
                statusMap.put("msg", "商品编码中%前需有值");
                return statusMap;
            }
            productCode = reallyProductCode;
        }
        para.put("productCode", productCode);
        return channelProductManageDao.getProductName(para);
    }

    @Override
    public Map<String, Object> jsonChannelProInfo(Map<String, Object> para)
            throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = channelProductManageDao.jsonChannelProInfo(para);
        for(Map<String,Object> map:infoList){
            map.put("wareHouseName", WareHouseEnum.getValueById((int)map.get("wareHouseId")));
        }
        //TODO拆分开
        resultMap.put("rows", infoList);
        int total = channelProductManageDao.countChannelProInfo(para);
        resultMap.put("total",total);
        return resultMap;
    }

    @Override
    public Map<String, Object> updateChannelProduct(ChannelProductEntity product)
            throws Exception
    {
        Map<String,Object> statusMap = validateProductCode(product);
        if(channelProductManageDao.updateChannelProduct(product)>0){
            statusMap.put("status", 1);
            statusMap.put("msg", "更新成功");
        }
        return statusMap;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String,Object> uploadChannelProFile(MultipartFile file) throws Exception
    {
        Map<String, Object> sheetData = POIUtil.getSheetDataAtIndex(file.getInputStream(), 0, true);
        List<Map<String, Object>> dataList = (List<Map<String, Object>>)sheetData.get("data");
        Map<String, Object> statusMap = new HashMap<String, Object>();
        
        List<Map<String,Object>> channelNameAndIdList = channelProductManageDao.getAllChannelNameAndId();
        List<Map<String, Object>> infoList = channelProductManageDao.jsonChannelProInfo(null);
        
        Map<String,String> channelAndCodeMap = new HashMap<String,String>();
        Map<String,String> channelNameAndIdMap = new HashMap<String,String>();
        
        for(Map<String,Object> map:channelNameAndIdList){
            channelNameAndIdMap.put(String.valueOf(map.get("channelName")),String.valueOf(map.get("channelId")));
        }
        
        for(Map<String,Object> map:infoList){
            channelAndCodeMap.put(new StringBuilder().
                                append(map.get("channelId")).append("_").
                                        append(map.get("productCode")).toString(), String.valueOf(map.get("id")));
        }
        
        int index = 1,status=0;String msg = null;
        List<ChannelProductEntity> productList = new ArrayList<ChannelProductEntity>();
        //验证是否有重复的ChannelId-ProductCode
        List<String> channelIdAndProCodeList = new ArrayList<String>();
        for (Map<String, Object> it : dataList)
        {
            index++;
            ChannelProductEntity product = new ChannelProductEntity();
            status = validatePro(it,channelNameAndIdMap,channelAndCodeMap,product);
            switch(status){
            case 1:
                msg =  String.format("第%d行渠道名称或仓库、商品编码、商品名称不能为空", index);
                statusMap.put("status", status);
                statusMap.put("msg", msg);
                return statusMap;
            case 2:
                msg = String.format("第%d行渠道名称不存在，请先添加该渠道或确认该渠道是否正确", index);
                statusMap.put("status", status);
                statusMap.put("msg", msg);
                return statusMap;
            case 3:
                msg =  String.format("第%d行采购商品编码错误，请确认该编码是否正确", index);
                statusMap.put("status", status);
                statusMap.put("msg", msg);
                return statusMap;
            case 4:
                channelProductManageDao.updateChannelProduct(product);
                status = 0;
                break;
            case 5:
//                Map<String,Object> para = new HashMap<String,Object>();
//                para.put("productCode", product.getProductCode());
//                int[] sellerIdArr = WareHouseEnum.getSellerIdById(product.getWareHouseId());
//                List<Integer> sellerIdList = new ArrayList<Integer>();  
//                for(int sellerId:sellerIdArr) sellerIdList.add(sellerId);
//                para.put("sellerIdList",sellerIdList);
//                product.setProductName(String.valueOf(channelProductManageDao.getProductName(para).get("productName")));
                String proCode = product.getProductCode();
                String key = product.getChannelId()+ "_" +proCode;
                if(channelIdAndProCodeList.contains(key)){
                    msg =  String.format("第%d行采购商品编码重复:"+ proCode, index);
                    statusMap.put("status", status);
                    statusMap.put("msg", msg);
                    return statusMap;
                }
                channelIdAndProCodeList.add(key);
                productList.add(product);
                status = 0;
            }
        }
        if(CollectionUtils.isNotEmpty(productList)){
            channelProductManageDao.batchAddChannelProduct(productList);
        }
        statusMap.put("status", status);
        statusMap.put("msg", msg);
        return statusMap;
    }                 
    
    
    public int validatePro(Map<String, Object> map,Map<String,String> channelNameAndIdMap,Map<String,String> channelAndCodeMap,ChannelProductEntity product){
        int status = 0;
        
        String channelName = String.valueOf(map.get("cell0"));
        String wareHouseName = String.valueOf(map.get("cell1"));
        String productCode = String.valueOf(map.get("cell2"));
        String productName = String.valueOf(map.get("cell3"));
        
        if("null".equals(channelName)||StringUtils.isEmpty(channelName)||
                "null".equals(wareHouseName)||StringUtils.isEmpty(wareHouseName)||
                    "null".equals(productCode)||StringUtils.isEmpty(productCode)||
                        "null".equals(productName)||StringUtils.isEmpty(productName)){
            return status = 1;
        }
        
        int wareHouseId = WareHouseEnum.getIdByValue(wareHouseName);
        if(!channelNameAndIdMap.containsKey(channelName)||!(wareHouseId>0)){
            return status = 2;
        }
        
        String assembleCount = null;
        String reallyProductCode = null;
        if (StringUtils.indexOf(productCode, "%") > -1)
        {
            assembleCount = StringUtils.substringAfterLast(productCode, "%");
            reallyProductCode = StringUtils.substringBeforeLast(productCode, "%");
            if (!assembleCount.matches("^\\d+$")||StringUtils.isEmpty(reallyProductCode))
            {
                return status = 3;
            }
        }
        
        //渠道ID+商品编码相同，则覆盖之前上传的信息
        String channelId = channelNameAndIdMap.get(channelName);
        String key = channelId +"_"+ productCode;
        product.setChannelId(Integer.valueOf(channelId));
        product.setWareHouseId(wareHouseId);
        product.setProductCode(productCode);
        product.setProductName(productName);
        if(channelAndCodeMap.containsKey(key)){
            return status = 4;
        }
        
        //TODO refact
        if(!channelAndCodeMap.containsKey(key)){
            assembleCount = StringUtils.isNotEmpty(assembleCount)?assembleCount:"1";
            product.setAssembleCount(Integer.valueOf(assembleCount));
            return status = 5;
        }
        return status;
    }
}
