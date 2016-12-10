package com.ygg.admin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ygg.admin.code.ProductEnum;
import com.ygg.admin.dao.GroupBuyDao;
import com.ygg.admin.dao.ProductDao;
import com.ygg.admin.service.GroupBuyService;
import com.ygg.admin.util.MathUtil;
import com.ygg.admin.util.StringUtils;

@Service("groupBuyService")
public class GroupBuyServiceImpl implements GroupBuyService
{
    @Resource
    private ProductDao productDao;
    
    @Resource
    private GroupBuyDao groupBuyDao;
    
    @Override
    public Map<String, Object> analyzeGroupProduct(Map<String, Object> para)
        throws Exception
    {
        int total = 0;
        List<Map<String, Object>> rows = groupBuyDao.findAllGourpProductNums(para);
        if (rows.size() > 0)
        {
            total = groupBuyDao.countAllGourpProductNums(para);
            List<Integer> productIdList = new ArrayList<Integer>();
            for (Map<String, Object> it : rows)
            {
                int productId = Integer.parseInt(it.get("productId") + "");
                productIdList.add(productId);
            }
            Map<String, Object> searchPara = new HashMap<String, Object>();
            searchPara.put("idList", productIdList);
            //商品信息
            List<Map<String, Object>> productSimpleList = productDao.findAllProductSimpleByPara(searchPara);
            Map<String, Map<String, Object>> productInfoMap = new HashMap<String, Map<String, Object>>();
            for (Map<String, Object> it : productSimpleList)
            {
                productInfoMap.put(it.get("id") + "", it);
            }
            //发起成功的团购
            searchPara.put("isSucceed", 1);
            List<Map<String, Object>> successGroupCode = groupBuyDao.findAllGourpProductNums(searchPara);
            Map<String, String> successGroupCodeMap = new HashMap<String, String>();
            for (Map<String, Object> it : successGroupCode)
            {
                successGroupCodeMap.put(it.get("productId") + "", it.get("total") + "");
            }
            //成功团购的人数
            List<Map<String, Object>> successGroupPeople = groupBuyDao.findGroupTotalPeople(searchPara);
            Map<String, String> successGroupPeopleMap = new HashMap<String, String>();
            for (Map<String, Object> it : successGroupPeople)
            {
                successGroupPeopleMap.put(it.get("productId") + "", it.get("total") + "");
            }
            //参与人数
            searchPara.remove("isSucceed");
            List<Map<String, Object>> groupPeople = groupBuyDao.findGroupTotalPeople(searchPara);
            Map<String, String> groupPeopleMap = new HashMap<String, String>();
            for (Map<String, Object> it : groupPeople)
            {
                groupPeopleMap.put(it.get("productId") + "", it.get("total") + "");
            }
            //成功下单人数
            searchPara.put("isGenerateOrder", 1);
            List<Map<String, Object>> generateOrderPeople = groupBuyDao.findGroupTotalPeople(searchPara);
            Map<String, String> generateOrderPeopleMap = new HashMap<String, String>();
            for (Map<String, Object> it : generateOrderPeople)
            {
                generateOrderPeopleMap.put(it.get("productId") + "", it.get("total") + "");
            }
            //实际购买人数
            searchPara.put("isBuy", 1);
            List<Map<String, Object>> buyPeople = groupBuyDao.findGroupTotalPeople(searchPara);
            Map<String, String> buyPeopleMap = new HashMap<String, String>();
            for (Map<String, Object> it : buyPeople)
            {
                buyPeopleMap.put(it.get("productId") + "", it.get("total") + "");
            }
            for (Map<String, Object> it : rows)
            {
                String productId = it.get("productId") + "";
                //商品信息
                Map<String, Object> pinfo = productInfoMap.get(productId);
                it.put("productName", pinfo.get("name") + "");
                it.put("productType", ProductEnum.PRODUCT_TYPE.getDescByCode(Integer.parseInt(pinfo.get("type") + "")));
                it.put("salesPrice", StringUtils.removeLastZero(pinfo.get("salesPrice") + ""));
                double salesPrice = Double.parseDouble(pinfo.get("salesPrice") + "");
                double groupPrice = salesPrice * 0.1 >= 10 ? salesPrice - 10.0 : salesPrice * 0.9;
                it.put("groupPirce", StringUtils.removeLastZero(MathUtil.round(groupPrice, 2)));
                //发起成功的团购
                String groupSuccessNums = successGroupCodeMap.get(productId) == null ? "0" : successGroupCodeMap.get(productId) + "";
                it.put("groupSuccessNums", groupSuccessNums);
                //成功团购的人数
                String groupSuccessPeople = successGroupPeopleMap.get(productId) == null ? "0" : successGroupPeopleMap.get(productId) + "";
                it.put("groupSuccessPeople", groupSuccessPeople);
                //参与人数
                String groupTotalPeople = groupPeopleMap.get(productId) == null ? "0" : groupPeopleMap.get(productId) + "";
                it.put("groupTotalPeople", groupTotalPeople);
                //成功下单人数
                String groupOrderNums = generateOrderPeopleMap.get(productId) == null ? "0" : generateOrderPeopleMap.get(productId) + "";
                it.put("groupOrderNums", groupOrderNums);
                //实际购买人数
                String groupBuyNums = buyPeopleMap.get(productId) == null ? "0" : buyPeopleMap.get(productId) + "";
                it.put("groupBuyNums", groupBuyNums);
            }
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }
}
