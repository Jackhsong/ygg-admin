package com.ygg.admin.service.impl;

import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.code.BirdexOrderEnum;
import com.ygg.admin.code.ProductEnum;
import com.ygg.admin.config.YggAdminProperties;
import com.ygg.admin.dao.AreaDao;
import com.ygg.admin.dao.BirdexDao;
import com.ygg.admin.dao.OrderDao;
import com.ygg.admin.dao.OverseasOrderDao;
import com.ygg.admin.dao.ProductDao;
import com.ygg.admin.dao.ReceiveAddressDao;
import com.ygg.admin.dao.SellerDao;
import com.ygg.admin.entity.OrderEntity;
import com.ygg.admin.entity.OrderReceiveAddress;
import com.ygg.admin.entity.ProductEntity;
import com.ygg.admin.entity.SellerEntity;
import com.ygg.admin.service.BirdexService;
import com.ygg.admin.util.CommonUtil;
import com.ygg.admin.util.DateTimeUtil;

@Service("birdexService")
public class BirdexServiceImpl implements BirdexService
{
    Logger log = Logger.getLogger(BirdexServiceImpl.class);
    
    @Resource
    private OrderDao orderDao;
    
    @Resource
    private AreaDao areaDao;
    
    @Resource
    private SellerDao sellerDao;
    
    @Resource
    private OverseasOrderDao overseasOrderDao;
    
    @Resource
    private BirdexDao birdexDao;
    
    @Resource
    private ProductDao productDao;
    
    @Resource
    private RestTemplate restTemplate;
    
    @Resource
    private ReceiveAddressDao receiveAddressDao;
    
    @Override
    public Map<String, Object> findAllBirdexOrder(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> birdexList = orderDao.findAllBirdexOrder(para);
        int total = 0;
        if (birdexList.size() > 0)
        {
            total = orderDao.countAllBirdexOrder(para);
            Set<Integer> orderIdList = new HashSet<Integer>();
            for (Map<String, Object> it : birdexList)
            {
                int orderId = Integer.valueOf(it.get("id") + "");
                orderIdList.add(orderId);
            }
            //结算信息
            List<Map<String, Object>> seinfos = orderDao.findOrderProductSettlementInfo(new ArrayList<Integer>(orderIdList));
            //            System.out.println(seinfos);
            Map<String, List<Integer>> noSettlementOrderInfo = new HashMap<String, List<Integer>>();
            for (Map<String, Object> it : seinfos)
            {
                String orderId = it.get("orderId") + "";
                int productBaseId = Integer.valueOf(it.get("productBaseId") + "");
                int submitType = Integer.parseInt(it.get("submitType") == null ? "1" : it.get("submitType") + "");
                double wholesalePrice = Double.parseDouble(it.get("wholesalePrice") == null ? "0.0" : it.get("wholesalePrice") + "");
                double deduction = Double.parseDouble(it.get("deduction") == null ? "0.0" : it.get("deduction") + "");
                double proposalPrice = Double.parseDouble(it.get("proposalPrice") == null ? "0.0" : it.get("proposalPrice") + "");
                double selfPurchasePrice = Double.parseDouble(it.get("selfPurchasePrice") == null ? "0.0" : it.get("selfPurchasePrice") + "");
                //              计算 单位供货价
                double cost = 0;
                if (submitType == ProductEnum.PRODUCT_SUBMIT_TYPE.MONEY_SUBMIT.getCode())
                {
                    cost = wholesalePrice;
                }
                else if (submitType == ProductEnum.PRODUCT_SUBMIT_TYPE.PERCENT_SUBMIT.getCode())
                {
                    cost = (100 - deduction) * proposalPrice / 100;
                }
                else if (submitType == ProductEnum.PRODUCT_SUBMIT_TYPE.SELF_PURCHASE_PRICE.getCode())
                {
                    cost = selfPurchasePrice;
                }
                if (cost <= 0)
                {
                    List<Integer> list = noSettlementOrderInfo.get(orderId);
                    if (list == null)
                    {
                        list = new ArrayList<Integer>();
                        noSettlementOrderInfo.put(orderId, list);
                    }
                    list.add(productBaseId);
                }
            }
            for (Map<String, Object> it : birdexList)
            {
                int orderId = Integer.valueOf(it.get("id") + "");
                orderIdList.add(orderId);
                int canSend = 1;
                //1. 判断姓名是否非法，如果非法，查看数据库中是否已经存在对应关系的可替换的身份信息
                // 收货人姓名不可以包含"小姐"、"女士"、"先生"
                String fullName = it.get("fullName") + "";
                String idCard = it.get("idCard") + "";
                String wrongMsg = "";
                String dealMsg = "";
                int errorFullName = 0;
                int errorSettlement = 0;
                String errorSettlementEditSetUrl = "";
                //姓名非法性检查
                if (!CommonUtil.validateReceiveName(fullName))
                {
                    //从数据库中查找 是否存在设置
                    Map<String, Object> idCardMapping = orderDao.findIdcardRealnameMappingByIdCard(idCard);
                    if (idCardMapping == null)
                    {
                        //非法 1
                        wrongMsg += "异常1：姓名非法；";
                        canSend = 0;
                        errorFullName = 1;
                    }
                    else if (idCardMapping.get("realName") == null || (idCardMapping.get("realName") + "").equals(""))
                    {
                        //非法 2
                        wrongMsg += "异常2：姓名非法；”设置真实姓名与身份“列表已存在不完善信息，请前去修改。";
                        canSend = 0;
                        errorFullName = 1;
                    }
                    else
                    {
                        String realName = idCardMapping.get("realName") + "";
                        it.put("fullName", realName);
                        dealMsg = "系统自动将非法姓名“" + fullName + "“改为”" + realName + "“。";
                    }
                }
                // 订单商品是否有结算信息检查
                List<Integer> list = noSettlementOrderInfo.get(orderId + "");
                if (list != null)
                {
                    //非法 3
                    wrongMsg += "异常3：订单包含未设置供货价的商品，请添加。";
                    canSend = 0;
                    errorSettlement = 1;
                    for (Integer pbId : list)
                    {
                        String a = "<a target='_blank' href=\"" + para.get("contextPath") + "/productBase/toAddOrUpdate?id=" + pbId + "\">" + "设置供货价" + pbId + "</a> | ";
                        errorSettlementEditSetUrl += a;
                    }
                }
                
                String sendStatus = "未确认";
                if (orderDao.alreadyConfirmOrderByOrderId(orderId))
                {
                    //已经确认过了
                    canSend = 0;
                    sendStatus = "已确认";
                }
                it.put("errorSettlement", errorSettlement);
                it.put("errorSettlementEditSetUrl", errorSettlementEditSetUrl);
                it.put("sendStatus", sendStatus);
                it.put("dealMsg", dealMsg);
                it.put("wrongMsg", wrongMsg);
                it.put("canSend", canSend);
                it.put("errorFullName", errorFullName);
                String p = areaDao.findProvinceNameByProvinceId(Integer.valueOf(it.get("province") + ""));
                String c = areaDao.findCityNameByCityId(Integer.valueOf(it.get("city") + ""));
                String d = areaDao.findDistrictNameByDistrictId(Integer.valueOf(it.get("district") + ""));
                String address = p + c + d + it.get("detailAddress");
                it.put("receiveAddress", address);
                it.put("payTime", DateTimeUtil.timestampStringToWebString(it.get("payTime") == null ? null : it.get("payTime").toString()));
                //冻结状态
                int isFreeze = Integer.valueOf(it.get("isFreeze") == null ? "0" : it.get("isFreeze") + "");
                if (isFreeze == 1)
                {
                    it.put("freezeStatus", "已冻结");
                }
                else
                {
                    it.put("freezeStatus", "");
                }
            }
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", birdexList);
        result.put("total", total);
        return result;
    }
    
    @Override
    public Map findBirdexProductInfo(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = orderDao.findAllBirdexProductInfo(para);
        for (Map<String, Object> map : reList)
        {
            Integer exportPrice = Integer.valueOf(map.get("exportPrice") + "");
            map.put("exportPrice", exportPrice / 100.0);
            if ("1".equals(map.get("status") + ""))
            {
                // 已完善
                map.put("statusMsg", "OK");
                map.put("statusMsgForExport", "OK");
                map.put("orderNumber", "");
            }
            else
            {
                map.put("statusMsg", "<span style=\"color:red\">待添加</span>");
                map.put("statusMsgForExport", "待添加");
            }
            
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rows", reList);
        int total = 0;
        if (reList.size() > 1)
        {
            total = orderDao.countAllBirdexProductInfo(para);
        }
        map.put("total", total);
        return map;
    }
    
    @Override
    public int insertOrUpdateBirdexProductInfo(Map<String, Object> para)
        throws Exception
    {
        try
        {
            String code = para.get("code") + "";
            
            int status = 0;
            para.put("status", 1);
            if (para.get("id") != null)
            {
                status = orderDao.updateBirdexProInfo(para);
            }
            else
            {
                status = orderDao.insertBirdexProInfo(para);
            }
            return status;
        }
        catch (Exception e)
        {
            log.error("保存导单名称失败", e);
            return 0;
        }
    }
    
    @Override
    public int deleteBirdexProductInfoById(int id)
        throws Exception
    {
        int status = orderDao.deleteBirdexProductInfoById(id);
        return status;
    }
    
    @Override
    public Map<String, Object> sendBirdex(List<Integer> idList)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        int status = 1;
        int rightNum = 0;
        int wrongNum = 0;
        int secondNum = 0;
        int freezeNum = 0;
        String msg = "";
        for (int orderId : idList)
        {
            if (!orderDao.alreadyConfirmOrderByOrderId(orderId))
            {
                // 冻结订单 无法 推送
                OrderEntity order = orderDao.findOrderById(orderId);
                if (order.getIsFreeze() != 1)
                {
                    int st = orderDao.insertBirdexOrderConfirm(orderId);
                    if (st == 1)
                    {
                        rightNum++;
                    }
                    else
                    {
                        wrongNum++;
                        log.warn("插入笨鸟订单确认发货表失败！！！订单ID：" + orderId);
                    }
                }
                else
                {
                    freezeNum++;
                }
            }
            else
            {
                secondNum++;
            }
        }
        if (rightNum > 0)
        {
            msg += "成功确认了" + rightNum + "个订单。";
        }
        if (wrongNum > 0)
        {
            msg += "失败了" + wrongNum + "个订单。";
        }
        if (secondNum > 0)
        {
            msg += "重复推送了" + secondNum + "个订单。";
        }
        if (freezeNum > 0)
        {
            msg += "冻结订单无法推送" + freezeNum + "个。";
        }
        status = rightNum > 0 ? 1 : 0;
        result.put("msg", msg);
        result.put("status", status);
        result.put("rightNum", rightNum);
        result.put("wrongNum", wrongNum);
        result.put("secondNum", secondNum);
        return result;
    }
    
    @Override
    public List<Integer> findBirdexSellerId()
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("isBirdex", 1);
        List<SellerEntity> ses = sellerDao.findAllSellerSimpleByPara(para);
        List<Integer> sellerIdList = new ArrayList<Integer>();
        for (SellerEntity it : ses)
        {
            sellerIdList.add(it.getId());
        }
        return sellerIdList;
    }
    
    @Override
    public Map<String, Object> findAllBirdexOrderPushList(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> infoList = overseasOrderDao.findOverseasOrderList(para);
        for (Map<String, Object> map : infoList)
        {
            int pushStatus = (int)map.get("pushStatus");
            int status = (int)map.get("status");
            map.put("pushDesc", BirdexOrderEnum.PUSH_STATUS.getDescByCode(pushStatus));
            map.put("statusDesc", BirdexOrderEnum.LOGISTICS_STATUS.getDescByCode(status));
            map.put("updateTime", ((Timestamp)map.get("updateTime")).toString());
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rows", infoList);
        int total = 0;
        if (infoList.size() > 0)
        {
            total = overseasOrderDao.countOverseasOrderList(para);
        }
        map.put("total", total);
        return map;
    }
    
    @Override
    public String cancelBirdexOrder(Map<String, String> para)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        String url = YggAdminProperties.getInstance().getPropertie("birdex_order_cancel_url");
        String resultStr = postRequestWithJson(JSON.toJSONString(para), url);
        int code = JSON.parseObject(resultStr).getIntValue("code");
        String message = JSON.parseObject(resultStr).getString("message");
        if (code == 0)
        {
            result.put("status", code == 0 ? 1 : 0);
            result.put("msg", code == 0 ? "取消成功" : "取消失败");
        }
        else
        {
            result.put("status", 0);
            result.put("msg", StringUtils.isEmpty(message) ? "取消失败" : message);
        }
        return JSON.toJSONString(result);
    }
    
    @Override
    public String cancelPushBirdexOrder(int orderId, int pushStatus)
        throws Exception
    {
        int result = 0;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (orderDao.alreadyConfirmOrderByOrderId(orderId))
        {
            result = orderDao.updateBirdexOrderPushStatus(orderId, pushStatus);
        }
        else
        {
            result = orderDao.insertBirdexOrderConfirm(orderId, pushStatus);
        }
        resultMap.put("status", result);
        resultMap.put("msg", result > 0 ? "取消成功" : "取消失败");
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public String findReceiveInfo(String orderNumber)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        
        OrderEntity order = findOrderByNumber(orderNumber);
        if (order == null)
        {
            result.put("status", 0);
        }
        else
        {
            OrderReceiveAddress receiveAddress = receiveAddressDao.findOrderReceiveAddressById(order.getReceiveAddressId());
            if (receiveAddress != null)
            {
                BeanMap map = new BeanMap(receiveAddress);
                result.putAll(map);
                result.put("status", 1);
            }
            else
            {
                result.put("status", 0);
            }
        }
        return JSON.toJSONString(result);
    }
    
    @Override
    public String updateAddress(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        String name = para.get("name") == null ? "" : para.get("name") + "";
        String identityNumber = para.get("identityNumber") + "";
        if (StringUtils.isEmpty(name))
        {
            result.put("status", 0);
            result.put("msg", "姓名不能为空");
            return JSON.toJSONString(result);
        }
        if (!CommonUtil.validateReceiveName(name))
        {
            //从数据库中查找 是否存在设置
            Map<String, Object> idCardMapping = orderDao.findIdcardRealnameMappingByIdCard(identityNumber);
            if (idCardMapping == null)
            {
                result.put("status", 0);
                result.put("msg", "姓名非法");
                return JSON.toJSONString(result);
            }
            else if (idCardMapping.get("realName") == null || (idCardMapping.get("realName") + "").equals(""))
            {
                result.put("status", 0);
                result.put("msg", "姓名非法；”设置真实姓名与身份“列表已存在不完善信息，请前去修改。");
                return JSON.toJSONString(result);
            }
        }
        String url = YggAdminProperties.getInstance().getPropertie("birdex_order_modify_address_url");
        String resultStr = postRequestWithJson(JSON.toJSONString(para), url);
        int code = JSON.parseObject(resultStr).getIntValue("code");
        String message = JSON.parseObject(resultStr).getString("message");
        if (code == 0)
        {
            result.put("status", code == 0 ? 1 : 0);
            result.put("msg", "修改成功");
        }
        else
        {
            result.put("status", 0);
            result.put("msg", message == null ? "修改失败" : message);
        }
        return JSON.toJSONString(result);
    }
    
    @Override
    public String updateIdCard(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        String name = para.get("name") == null ? "" : para.get("name") + "";
        String identityNumber = para.get("identityNumber") + "";
        if (StringUtils.isEmpty(name))
        {
            result.put("status", 0);
            result.put("msg", "姓名不能为空");
            return JSON.toJSONString(result);
        }
        if (!CommonUtil.validateReceiveName(name))
        {
            //从数据库中查找 是否存在设置
            Map<String, Object> idCardMapping = orderDao.findIdcardRealnameMappingByIdCard(identityNumber);
            if (idCardMapping == null)
            {
                result.put("status", 0);
                result.put("msg", "姓名非法");
                return JSON.toJSONString(result);
            }
            else if (idCardMapping.get("realName") == null || (idCardMapping.get("realName") + "").equals(""))
            {
                result.put("status", 0);
                result.put("msg", "姓名非法；”设置真实姓名与身份“列表已存在不完善信息，请前去修改。");
                return JSON.toJSONString(result);
            }
        }
        String url = YggAdminProperties.getInstance().getPropertie("birdex_order_modify_idcard_url");
        String resultStr = postRequestWithJson(JSON.toJSONString(para), url);
        int code = JSON.parseObject(resultStr).getIntValue("code");
        String message = JSON.parseObject(resultStr).getString("message");
        if (code == 0)
        {
            result.put("status", code == 0 ? 1 : 0);
            result.put("msg", "修改成功");
        }
        else
        {
            result.put("status", 0);
            result.put("msg", message == null ? "修改失败" : message);
        }
        return JSON.toJSONString(result);
    }
    
    @Override
    public Map<String, Object> findAllBirdexOrderChangeList(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> infoList = overseasOrderDao.findAllBirdexOrderChange(para);
        for (Map<String, Object> map : infoList)
        {
            int dealType = (int)map.get("dealType");
            int dealStatus = (int)map.get("dealStatus");
            map.put("name", map.get("name") == null ? "-" : map.get("name") + "");
            map.put("mobile", map.get("mobile") == null ? "-" : map.get("mobile") + "");
            map.put("address", map.get("province") == null ? "-" : (map.get("province") + "" + map.get("city") + "" + map.get("district") + map.get("addressDetail")));
            map.put("statusDesc", BirdexOrderEnum.CHANGE_STATUS.getDescByCode(dealStatus));
            map.put("typeDesc", BirdexOrderEnum.CHANGE_TYPE.getDescByCode(dealType));
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rows", infoList);
        int total = 0;
        if (infoList.size() > 0)
        {
            total = overseasOrderDao.countBirdexOrderChange(para);
        }
        map.put("total", total);
        return map;
    }
    
    @Override
    public String findAllBirdexProductList(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("rows", birdexDao.findAllBirdexProduct(para));
        resultMap.put("total", birdexDao.countBirdexProduct(para));
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public String saveBirdexProduct(int productId, float price)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        ProductEntity pe = productDao.findProductByID(productId, null);
        if (pe == null)
        {
            resultMap.put("status", 0);
            resultMap.put("msg", String.format("Id=%d的商品不存在", productId));
            return JSON.toJSONString(resultMap);
        }
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("productId", productId);
        para.put("price", price * 100);
        try
        {
            if (birdexDao.saveBirdexProduct(para) > 0)
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
            if (e.getMessage().contains("uniq_product_id") && e.getMessage().contains("Duplicate"))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", String.format("Id=%d的商品已经存在", productId));
                return JSON.toJSONString(resultMap);
            }
            else
            {
                throw new Exception(e);
            }
        }
    }
    
    @Override
    public String updateBirdexProduct(int id, int productId, float price)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        ProductEntity pe = productDao.findProductByID(productId, null);
        if (pe == null)
        {
            resultMap.put("status", 0);
            resultMap.put("msg", String.format("Id=%d的商品不存在", productId));
            return JSON.toJSONString(resultMap);
        }
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        para.put("productId", productId);
        para.put("price", price * 100);
        if (birdexDao.updateBirdexProduct(para) > 0)
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
    public String deleteBirdexProduct(int id)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (birdexDao.deleteBirdexProduct(id) > 0)
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
    
    private String postRequestWithJson(String jsonStr, String url)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<Charset> charset = new ArrayList<Charset>();
        charset.add(Charset.forName("utf-8"));
        headers.setAcceptCharset(charset);
        HttpEntity<String> requestEntity = new HttpEntity<String>(jsonStr, headers);
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        String result = restTemplate.postForObject(url, requestEntity, String.class);
        return result;
    }
    
    private OrderEntity findOrderByNumber(String number)
        throws Exception
    {
        String orderNumber = number;
        if (number.startsWith("G"))
        {
            String childNumbers = orderDao.findHBOrderByParentNumber(number);
            if (StringUtils.isNotEmpty(childNumbers))
            {
                orderNumber = childNumbers.split(";")[0];
            }
        }
        return orderDao.findOrderByNumber(orderNumber);
    }
    
    @Override
    public List<Map<String, Object>> findAllBirdexStock(HashMap<String, Object> para)
        throws Exception
    {
        return birdexDao.findAllBirdexStock(para);
    }
    
    @Override
    public String findAllBirdexStockList(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, Object>> reList = birdexDao.findAllBirdexStock(para);
        for (Map<String, Object> it : reList)
        {
            if (it.get("providerProductId") == null)
            {
                it.put("providerProductId", "匹配失败");
            }
            if (it.get("providerProductName") == null)
            {
                it.put("providerProductName", "匹配失败");
            }
            it.put("warehouse", "香港笨鸟仓");
        }
        resultMap.put("total", birdexDao.countBirdexStock(para));
        resultMap.put("rows", reList);
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public String refreshBirdexStock()
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        String url = YggAdminProperties.getInstance().getPropertie("birdex_order_modify_idcard_url");
        String resultStr = postRequestWithJson(null, url);
        int code = JSON.parseObject(resultStr).getIntValue("code");
        String message = JSON.parseObject(resultStr).getString("message");
        if (code == 0)
        {
            result.put("status", 1);
            result.put("msg", "刷新成功");
        }
        else
        {
            result.put("status", 0);
            result.put("msg", message == null ? "刷新失败" : message);
        }
        return JSON.toJSONString(result);
    }
    
    public String findAllBirdexCancelOrder(java.util.Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> birdexList = birdexDao.findBirdexCancelOrder(para);
        int total = 0;
        if (birdexList.size() > 0)
        {
            total = birdexDao.countBirdexCancelOrder(para);
            for (Map<String, Object> it : birdexList)
            {
                String fullName = it.get("fullName") + "";
                String idCard = it.get("idCard") + "";
                //姓名非法性检查
                if (!CommonUtil.validateReceiveName(fullName))
                {
                    //从数据库中查找 是否存在设置
                    Map<String, Object> idCardMapping = orderDao.findIdcardRealnameMappingByIdCard(idCard);
                    if (idCardMapping != null && idCardMapping.get("realName") != null)
                    {
                        String realName = idCardMapping.get("realName") + "";
                        it.put("fullName", realName);
                    }
                }
                String p = areaDao.findProvinceNameByProvinceId(Integer.valueOf(it.get("province") + ""));
                String c = areaDao.findCityNameByCityId(Integer.valueOf(it.get("city") + ""));
                String d = areaDao.findDistrictNameByDistrictId(Integer.valueOf(it.get("district") + ""));
                String address = p + c + d + it.get("detailAddress");
                it.put("receiveAddress", address);
                it.put("payTime", DateTimeUtil.timestampStringToWebString(it.get("payTime") == null ? null : it.get("payTime").toString()));
                it.put("status", "取消推送");
            }
        }
        resultMap.put("rows", birdexList);
        resultMap.put("total", total);
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public String updateBirdexOrderConfirmPushStatus(int orderId)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        if (birdexDao.updateBirdexOrderConfirmPushStatus(orderId) > 0)
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "推送成功");
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "推送失败");
        }
        return JSON.toJSONString(resultMap);
    }
}
