package com.ygg.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.ygg.admin.code.SellerEnum;
import com.ygg.admin.dao.*;
import com.ygg.admin.entity.*;
import com.ygg.admin.service.SellerService;
import com.ygg.admin.util.CommonConstant;
import com.ygg.admin.util.DateTimeUtil;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanPropertyValueChangeClosure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service("sellerService")
public class SellerServiceImpl implements SellerService
{
    
    Logger logger = Logger.getLogger(SellerServiceImpl.class);
    
    @Resource
    private SellerDao sellerDao = null;
    
    @Resource(name = "saleWindowDao")
    private SaleWindowDao saleWindowDao = null;
    
    @Resource
    private SellerExpandDao sellerExpandDao;
    
    @Resource
    private SellerDeliverAreaDao sellerDeliverAreaDao;
    
    @Resource
    private RelationDeliverAreaTemplateDao relationDeliverAreaTemplateDao;
    
    @Resource
    private AreaDao areaDao;

    @Resource
    private UserDao userDao;
    
    @SuppressWarnings("unchecked")
    @Override
    public int saveOrUpdate(Map<String, Object> map)
        throws Exception
    {
        int resultStatus = -1;
        boolean transfer = false;
        SellerEntity seller = (SellerEntity)map.get("seller");
        SellerExpandEntity sellerExpand = (SellerExpandEntity)map.get("sellerExpand");
        
        if (seller.getId() > 0)
        {
            SellerEntity sellerOld = sellerDao.findSellerById(seller.getId());
            if (sellerOld.getIsOwner() == CommonConstant.COMMON_NO && seller.getIsOwner() == CommonConstant.COMMON_YES)
            {
                transfer = true;
            }
        }
        if (seller.getId() == 0)
        {
            // 新增
            logger.info("新增商家信息：" + seller.toString());
            SellerEnum.SellerTypeEnum typeEnum = SellerEnum.SellerTypeEnum.getSellerTypeEnumByCode(seller.getSellerType());
            seller.setSellerType((byte)typeEnum.getCode());
            seller.setIsNeedIdCardNumber(typeEnum.getIsNeedIdCardNumber());
            seller.setIsNeedIdCardImage(typeEnum.getIsNeedIdCardImage());
            resultStatus = sellerDao.save(seller);
            
            sellerExpand.setSellerId(seller.getId());
            logger.info("新增商家扩展信息：" + sellerExpand.toString());
            sellerExpandDao.save(sellerExpand);
            
            /* 财务付款图片暂时不用
             * for (Map<String, Object> it : imageList)
            {
                SellerFinanceSettlementPictureEntity sfspe = new SellerFinanceSettlementPictureEntity();
                sfspe.setSellerId(seller.getId());
                sfspe.setImage(it.get("image").toString());
                sellerDao.saveSellerFinancePicture(sfspe);
            }*/
            
        }
        else
        {
            // 修改
            logger.info("修改商家信息：" + seller.toString());
            Map<String, Object> sellerBeanMap = new BeanMap(seller);
            logger.debug(sellerBeanMap);
            resultStatus = sellerDao.updateSellerByPara(sellerBeanMap);
            
            //更新商家扩展信息
            SellerExpandEntity see = sellerExpandDao.findSellerExpandBysid(seller.getId());
            if (see == null)
            {
                sellerExpandDao.save(sellerExpand);
            }
            else
            {
                Map<String, Object> sellerExpandMap = new BeanMap(sellerExpand);
                sellerExpandDao.update(sellerExpandMap);
                
            }
            
            //更新财务付款信息图片,暂时不用
            /*List<Integer> imageIds = new ArrayList<Integer>();
            for (Map<String, Object> it : imageList)
            {
                SellerFinanceSettlementPictureEntity sfspe = new SellerFinanceSettlementPictureEntity();
                int id = Integer.parseInt(it.get("id") + "");
                String image = it.get("image") + "";
                sfspe.setImage(image);
                if (id == 0)
                {
                    sfspe.setSellerId(seller.getId());
                    sellerDao.saveSellerFinancePicture(sfspe);
                    imageIds.add(sfspe.getId());
                }
                else
                {
                    sfspe.setId(id);
                    sellerDao.updateSellerFinancePicture(sfspe);
                    imageIds.add(id);
                }
            }
            if (imageIds.size() > 0)
            {
                Map<String, Object> it = new HashMap<String, Object>();
                it.put("sellerId", seller.getId());
                it.put("idList", imageIds);
                sellerDao.deleteSellerFinancePicture(it);
            }*/
        }
        
        //商家店铺网址，先删除后新增
        sellerDao.deleteSellerOnlineStoreAddress(seller.getId());
        List<String> storeURLList = Arrays.asList(sellerExpand.getShopURL().split(","));
        List<SellerOnlineStoreAddressEntity> storeList = new ArrayList<SellerOnlineStoreAddressEntity>();
        for (String url : storeURLList)
        {
            SellerOnlineStoreAddressEntity sosae = new SellerOnlineStoreAddressEntity();
            sosae.setSellerId(seller.getId());
            sosae.setUrl(url);
            storeList.add(sosae);
        }
        if (storeList.size() > 0)
        {
            sellerDao.saveSellerOnlineStoreAddress(storeList);
        }
        //商家类别处理  暴力点 先删除 后新增
        if(StringUtils.isNotEmpty(sellerExpand.getCategoryId()) && seller.getId() != 0) {
            List<String> categoryIdsList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(sellerExpand.getCategoryId());
            Set<String> categoryIdsSet = Sets.newLinkedHashSet(categoryIdsList);
            sellerDao.deleteAllSellerCategoryBySellerId(seller.getId());
            Map<String, Object> para = new HashMap<>();
            para.put("sellerId", seller.getId());
            for(String id : categoryIdsSet){
                para.put("categoryFirstId", Integer.valueOf(id));
                sellerDao.saveSellerCategoryRelation(para);
            }
        }

        if (seller.getIsOwner() == CommonConstant.COMMON_YES)
        {
            //使用商家后台，更新商家品牌信息
            List<Map<String, Object>> brandList = new ArrayList<Map<String, Object>>();
            List<String> brandIdList = Arrays.asList(sellerExpand.getBrandId().split(","));
            //去重
            Set<String> brandIdSet = new HashSet<String>();
            for (String brandId : brandIdList)
            {
                if (StringUtils.isNotEmpty(brandId))
                {
                    brandIdSet.add(brandId.trim());
                }
            }
            for (String brandId : brandIdSet)
            {
                Map<String, Object> it = new HashMap<String, Object>();
                it.put("sellerId", seller.getId());
                it.put("brandId", brandId.trim());
                brandList.add(it);
            }
            if (brandList != null && brandList.size() > 0)
            {
                //简单粗暴一点，先删除，再新增
                sellerDao.deleteSellerBrand(seller.getId());
                sellerDao.saveSellerBrand(brandList);
            }
        }
        
        //从不使用商家后台到使用商家后台，转换配送地区模版
        if (transfer)
        {
            //查找商家sellerId的所有模版
            List<SellerDeliverAreaTemplateEntity> templateList = sellerDeliverAreaDao.findSellerDeliverAreaTemplateBysid(seller.getId());
            if (templateList != null && !templateList.isEmpty())
            {
                for (SellerDeliverAreaTemplateEntity areaTemplate : templateList)
                {
                    transfer(areaTemplate);
                }
            }
        }
        return resultStatus;
    }
    
    @Override
    public String jsonSellerInfo(Map<String, Object> para)
        throws Exception
    {
        List<SellerEntity> sellerList = sellerDao.findAllSellerByPara(para);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        int total = 0;
        if (sellerList.size() > 0)
        {
            for (SellerEntity seller : sellerList)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", seller.getId());
                map.put("sellerName", seller.getSellerName());
                //类目
                List<Map<String, Object>> categories = sellerDao.findSellerCategoryByRelation(seller.getId());
                Set<String> names = new LinkedHashSet<>();
                if (CollectionUtils.isEmpty(categories)) {  // 如果商家没有添加类目 则从商家的基本商品中查找 并自动存储商家类别
                    categories = sellerDao.findSellerCategoryByBaseProduct(seller.getId());
                    Map<String, Object> insertPara = new HashMap<>();
                    insertPara.put("sellerId", seller.getId());
                    if (CollectionUtils.isNotEmpty(categories)) {
                        for(Map<String, Object> cat : categories) {
                            insertPara.put("categoryFirstId", cat.get("id"));
                            sellerDao.saveSellerCategoryRelation(insertPara);
                            names.add(StringUtils.trimToNull((String) cat.get("name")));
                        }
                        map.put("categories", Joiner.on(",").skipNulls().join(names));
                    }
                } else {
                    for(Map<String, Object> cat : categories) {
                        names.add(StringUtils.trimToNull((String) cat.get("name")));
                    }
                    map.put("categories", Joiner.on(",").skipNulls().join(names));
                }
                //审核人
                SellerExpandEntity expandInfo = sellerExpandDao.findSellerExpandBysid(seller.getId());
                if(expandInfo != null) {
                    if( expandInfo.getAuditUserId() != 0) {
                        User user = userDao.findUserById(expandInfo.getAuditUserId());
                        if(user != null){
                            map.put("auditUser", user.getRealname());
                        }
                    }
                    map.put("depositStatus", SellerEnum.DEPOSIT_STATUS.getByCode(expandInfo.getDepositStatus()));
                    map.put("depositCount", expandInfo.getDepositCount() == 0 ? null : expandInfo.getDepositCount()+"元");

                }
                map.put("createTime", seller.getCreateTime().toString());
                String remark = SellerEnum.SellerSendCodeTypeEnum.getDescByCode( seller.getSendCodeType());
                if("其他".equalsIgnoreCase(remark)) {
                    remark = seller.getSendCodeRemark();
                }
                map.put("sendTypeDesc", remark);
                map.put("realSellerName", seller.getRealSellerName());
                map.put("companyName", seller.getCompanyName());
                map.put("sellerTypeString", SellerEnum.SellerTypeEnum.getDescByCode(seller.getSellerType()));
                map.put("sendAddress", seller.getSendAddress());
                map.put("responsibilityPerson", seller.getResponsibilityPerson());
                map.put("warehouse", seller.getWarehouse());
                map.put("isAvailable", seller.getIsAvailable());
                map.put("weekendRemark", SellerEnum.WeekendSendTypeEnum.getDescByCode(seller.getIsSendWeekend()));
                map.put("isOwner", seller.getIsOwner());
                resultList.add(map);
            }
            total = sellerDao.countSellerByPara(para);
        }
        resultMap.put("rows", resultList);
        resultMap.put("total", total);
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public Map<String, Object> findSellerInfoById(int id)
        throws Exception
    {
        SellerEntity seller = sellerDao.findSellerById(id);
        Map<String, Object> map = new HashMap<String, Object>();
        if (seller != null)
        {
            map.put("id", seller.getId());
            map.put("isAvailable", seller.getIsAvailable());
            map.put("sellerName", seller.getSellerName());
            map.put("sellerType", seller.getSellerType());
            map.put("companyName", seller.getCompanyName());
            map.put("sendAddress", seller.getSendAddress());
            map.put("sendAddress", seller.getSendAddress());
            map.put("sendTimeRemark", SellerEnum.SellerSendTimeTypeEnum.getDescByCode(seller.getSendTimeType()));
            map.put("warehouse", seller.getWarehouse());
            map.put("freightType", seller.getFreightType());
            map.put("freightMoney", seller.getFreightMoney());
            map.put("freightOther", seller.getFreightOther());
            String sendCodeRemark = seller.getSendCodeType() == 4 ? seller.getSendCodeRemark() : SellerEnum.SellerSendCodeTypeEnum.getDescByCode(seller.getSendCodeType());
            if (seller.getSendCodeType() == SellerEnum.SellerSendCodeTypeEnum.PRODUCTBARCODE.getCode())
            {
                sendCodeRemark += "(所选商家发货类型为按商品条码发货，商品编码自动替换为商品条码)";
            }
            map.put("sendCodeRemark", sendCodeRemark);
            map.put("sendCodeType", seller.getSendCodeType());
            map.put("kuaidi", seller.getKuaidi());
            map.put("sendWeekendRemark", SellerEnum.WeekendSendTypeEnum.getDescByCode(seller.getIsSendWeekend()));
            map.put("expectArriveTime", seller.getExpectArriveTime());
            map.put("deliverAreaDesc", seller.getDeliverAreaDesc());
            map.put("deliverAreaType", seller.getDeliverAreaType());
            map.put("isSelfSupport", seller.getIsSelfSupport());
            
            List<RelationSellerDeliverAreaEntity> areaCodes = sellerDao.findRelationSellerDeliverAreaBySellerId(id);
            if (areaCodes.size() == 1 && areaCodes.get(0).getProvinceCode() == 1)
            {
                areaCodes = new ArrayList<RelationSellerDeliverAreaEntity>();
            }
            map.put("areaCodes", areaCodes);
        }
        return map;
    }
    
    @Override
    public SellerEntity findSellerSimpleById(int id)
        throws Exception
    {
        SellerEntity seller = sellerDao.findSellerSimpleById(id);
        return seller;
    }
    
    @Override
    public SellerEntity findSellerById(int id)
        throws Exception
    {
        return sellerDao.findSellerById(id);
    }
    
    @Override
    public List<SellerEntity> findSellerIsAvailable()
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("isAvailable", 1);
        para.put("start", 0);
        para.put("max", 1000);
        return sellerDao.findAllSellerByPara(para);
    }
    
    /* (non-Javadoc)
     * @see com.ygg.admin.service.SellerService#findAllSeller(java.util.Map)
     */
    @Override
    public List<SellerEntity> findAllSeller(Map<String, Object> para)
        throws Exception
    {
        List<SellerEntity> sellerList = sellerDao.findAllSellerByPara(para);
        List<Integer> sellerIds = new ArrayList<Integer>();
        for (SellerEntity se : sellerList)
        {
            sellerIds.add(se.getId());
        }
        List<SellerExpandEntity> seeList = sellerExpandDao.findSellerExpandBysids(sellerIds);
        Map<Integer, SellerExpandEntity> seeMap = new HashMap<>();
        for (SellerExpandEntity see : seeList)
        {
            seeMap.put(see.getSellerId(), see);
        }
        for (SellerEntity se : sellerList)
        {
            se.setYyContactPerson(seeMap.get(se.getId()) == null ? "" : seeMap.get(se.getId()).getRcContactPerson());
            se.setYyContactMobile(seeMap.get(se.getId()) == null ? "" : seeMap.get(se.getId()).getRcContactMobile());
            se.setYyEmail(seeMap.get(se.getId()) == null ? "" : seeMap.get(se.getId()).getRcEmail());
            se.setYyqq(seeMap.get(se.getId()) == null ? "" : seeMap.get(se.getId()).getRcqq());
            se.setYyAliwang(seeMap.get(se.getId()) == null ? "" : seeMap.get(se.getId()).getRcAliwang());
        }
        return sellerList;
    }
    
    @Override
    public int countSellerBySellerName(Map<String, Object> para)
        throws Exception
    {
        return sellerDao.countSellerBySellerName(para);
    }
    
    @Override
    public int batchUpdateSeller(Map<String, Object> para)
        throws Exception
    {
        try
        {
            int num = 0;
            List<Integer> idList = (List<Integer>)para.get("idList");
            String person = (String)para.get("person");
            for (Integer id : idList)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", id);
                map.put("responsibilityPerson", person);
                int status = sellerDao.updateSellerByPara(map);
                num += status;
            }
            return num;
        }
        catch (Exception e)
        {
            logger.error("批量修改商家负责人出错", e);
            return 0;
        }
        
    }
    
    @Override
    public Map<String, Object> findAllSaleWindowForSellerPeriod(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> resultList = saleWindowDao.findAllSaleWindowForSellerPeriod(para);
        DateTime now = DateTime.now();
        for (Map<String, Object> map : resultList)
        {
            map.put("typeStr", (Integer.valueOf(map.get("type") + "") == 1 ? "单品" : (Integer.valueOf(map.get("type") + "") == 2 ? "组合" : "自定义")));
            DateTime startTime = DateTimeUtil.string2DateTime(map.get("startTime") + "100000", "yyyyMMddHHmmss");
            DateTime endTime = DateTimeUtil.string2DateTime(map.get("endTime") + "100000", "yyyyMMddHHmmss").plusDays(1);
            map.put("startTime", startTime.toString("yyyy-MM-dd HH:mm:ss"));
            map.put("endTime", endTime.toString("yyyy-MM-dd HH:mm:ss"));
            if (endTime.isBefore(now))
            {
                map.put("days", Days.daysBetween(endTime, now).getDays());
            }
            else
            {
                map.put("days", "未结束");
            }
            Integer settlementPeriod = Integer.valueOf(map.get("settlementPeriod") + "");
            
            StringBuffer sb = new StringBuffer();
            if (settlementPeriod == 1)
            {
                sb.append("日结");
            }
            else if (settlementPeriod == 2)
            {
                sb.append("活动结束后").append(map.get("settlementDay")).append("结");
            }
            else
            {
                sb.append(map.get("settlementOther"));
            }
            map.put("settlement", sb.toString());
            
        }
        int total = saleWindowDao.countAllSaleWindowForSellerPeriod(para);
        resultMap.put("total", total);
        resultMap.put("rows", resultList);
        return resultMap;
    }
    
    @Override
    public List<RelationSellerDeliverAreaEntity> findRelationSellerDeliverAreaBySellerId(int id)
        throws Exception
    {
        
        return sellerDao.findRelationSellerDeliverAreaBySellerId(id);
    }
    
    @Override
    public List<SellerFinanceSettlementPictureEntity> findSellerFinancePictureBysid(int id)
        throws Exception
    {
        return sellerDao.findSellerFinancePictureBysid(id);
    }
    
    @Override
    public List<SellerOnlineStoreAddressEntity> findSellerOnlineStoreBysid(int id)
        throws Exception
    {
        return sellerDao.findSellerOnlineStoreBysid(id);
    }
    
    @Override
    public List<BrandEntity> findSellerBrandBysid(int id)
        throws Exception
    {
        return sellerDao.findSellerBrandBysid(id);
    }
    
    @Override
    public SellerEntity findSellerByRealSellerName(String realSellerName)
        throws Exception
    {
        return sellerDao.findSellerByRealSellerName(realSellerName);
    }
    
    @Override
    public SellerMasterAndSlaveEntity findSellerSlaveBySlaveId(int sellerId)
        throws Exception
    {
        return sellerDao.findSellerSlaveBySlaveId(sellerId);
    }
    
    @Override
    public Map<String, Object> mergeSellerJsonInfo(Map<String, Object> para)
        throws Exception
    {
        List<SellerEntity> masterList = sellerDao.findMasterSellerByPara(para);
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        int total = 0;
        for (SellerEntity seller : masterList)
        {
            List<String> idList = new ArrayList<String>();
            idList.add(seller.getId() + "");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", seller.getId() + "");
            map.put("sellerName", seller.getRealSellerName());
            map.put("sendAddress", seller.getSendAddress());
            List<Map<String, Object>> slaveList = sellerDao.finAllSlaveSeller(seller.getId() + "");
            StringBuffer sb = new StringBuffer();
            for (Map<String, Object> it : slaveList)
            {
                sb.append(it.get("slaveId")).append(",").append(it.get("slaveName")).append(",").append(it.get("sendAddress"));
                if (it.get("warehouse") != null && !"".equals(it.get("warehouse") + ""))
                {
                    sb.append("(").append(it.get("warehouse")).append(")");
                }
                sb.append(";");
                idList.add(it.get("slaveId") + "");
            }
            map.put("slave", sb.toString());
            map.put("ids", Arrays.toString(idList.toArray(new String[idList.size()])).replaceAll("\\[", "").replaceAll("\\]", ""));
            resultList.add(map);
        }
        total = sellerDao.countMergeSeller(para);
        resultMap.put("rows", resultList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> mergeSeller(String masterId, List<String> slaveIdList, int userId)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        
        SellerEntity sellerMaster = sellerDao.findSellerById(Integer.parseInt(masterId));
        if (sellerMaster == null)
        {
            resultMap.put("status", 0);
            resultMap.put("msg", String.format("ID=%s的商家不存在", masterId));
            return resultMap;
        }
        
        if (sellerMaster.getIsOwner() == CommonConstant.COMMON_NO)
        {
            resultMap.put("status", 0);
            resultMap.put("msg", String.format("ID=%s的商家不使用商家后台，不需要关联", masterId));
            return resultMap;
        }
        
        //检查所输入的商家是否存在
        for (String sellerId : slaveIdList)
        {
            SellerEntity seller = sellerDao.findSellerById(Integer.parseInt(sellerId));
            if (seller == null)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "ID=" + sellerId + "的商家不存在");
                return resultMap;
            }
        }
        
        //检查主帐号masterId是否已经作为从帐号存在
        SellerMasterAndSlaveEntity slaveSeller = sellerDao.findSellerSlaveBySlaveId(Integer.parseInt(masterId));
        if (slaveSeller != null)
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "ID=" + masterId + "的商家已经被关联到其他商家，不能再作为主商家");
            return resultMap;
        }
        
        for (String sellerId : slaveIdList)
        {
            //检查从帐号是否已经关联到其他的商家
            SellerMasterAndSlaveEntity slave = sellerDao.findSellerSlaveBySlaveId(Integer.parseInt(sellerId));
            if (slave != null && (slave.getSellerMasterId() != Integer.parseInt(masterId)))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "ID=" + sellerId + "的商家已经关联到ID=" + slave.getSellerMasterId() + "的商家，不能再次关联");
                return resultMap;
            }
            
            //检查从帐号是否已经作为主帐号存在
            List<SellerMasterAndSlaveEntity> slaveList = sellerDao.findSellerSlaveListByMasterId(Integer.parseInt(sellerId));
            if (!slaveList.isEmpty())
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "ID=" + sellerId + "的商家已经作为主商家存在，不能作为被关联商家");
                return resultMap;
            }
        }
        
        //查找修改之前关联的从帐号信息
        List<SellerMasterAndSlaveEntity> oldSlaveList = sellerDao.findSellerSlaveListByMasterId(Integer.parseInt(masterId));
        HashSet<String> slaveSellerIdList = new HashSet<String>();
        for (SellerMasterAndSlaveEntity sms : oldSlaveList)
        {
            slaveSellerIdList.add(sms.getSellerSlaveId() + "");
        }
        
        //插入新的从帐号信息
        List<Integer> idList = new ArrayList<Integer>();
        for (String slaveId : slaveIdList)
        {
            SellerMasterAndSlaveEntity slave = new SellerMasterAndSlaveEntity();
            slave.setSellerMasterId(Integer.parseInt(masterId));
            slave.setSellerSlaveId(Integer.parseInt(slaveId));
            slave.setCreateUser(userId);
            slave.setUpdateUser(userId);
            if (sellerDao.saveSellerMasterAndSlave(slave) == 1)
            {
                idList.add(slave.getId());
            }
        }
        
        if (!idList.isEmpty())
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("idList", idList);
            para.put("masterId", masterId);
            
            //删除已经存在的主从关系记录
            sellerDao.deleteSellerMasterAndSlave(para);
            if (!slaveSellerIdList.isEmpty())
            {
                //将商家设为不使用商家后台
                sellerDao.updateSellerOwnerStatus(new ArrayList<String>(slaveSellerIdList), CommonConstant.COMMON_NO);
                //将商家设为主帐号
                sellerExpandDao.updateSellerToSlave(new ArrayList<String>(slaveSellerIdList), CommonConstant.COMMON_YES);
            }
            
            //将从帐号的公共信息都改成主帐号的公共信息
            SellerEntity masterSeller = sellerDao.findSellerById(Integer.parseInt(masterId));
            SellerExpandEntity masterExpand = sellerExpandDao.findSellerExpandBysid(Integer.parseInt(masterId));
            para.putAll(new BeanMap(masterSeller));
            para.put("idList", slaveIdList);
            sellerDao.synchronousSellerInfo(para);
            
            //将商家扩展信息同步
            para.clear();
            para.putAll(new BeanMap(masterExpand));
            para.put("idList", slaveIdList);
            sellerExpandDao.synchronousSellerExpandInfo(para);
            
            //TODO 同步品牌信息
            
            //TODO 同步发货地区模版
            
            //TODO 同步运费结算模版
            
            //将商家设为使用商家后台
            sellerDao.updateSellerOwnerStatus(slaveIdList, CommonConstant.COMMON_YES);
            //将商家设为从帐号
            sellerExpandDao.updateSellerToSlave(slaveIdList, CommonConstant.COMMON_NO);
            
            resultMap.put("status", 1);
            resultMap.put("msg", "合并成功");
        }
        return resultMap;
    }
    
    @Override
    public List<SellerMasterAndSlaveEntity> findSellerSlaveListByMasterId(int masterId)
        throws Exception
    {
        return sellerDao.findSellerSlaveListByMasterId(masterId);
    }
    
    private void transfer(SellerDeliverAreaTemplateEntity areaTemplate)
        throws Exception
    {
        //查找例外地区
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("sellerDeliverAreaTemplateId", areaTemplate.getId());
        para.put("isExcept", 1);
        List<RelationDeliverAreaTemplateEntity> areaList = relationDeliverAreaTemplateDao.findRelationDeliverAreaTemplateByPara(para);
        if (areaList == null || areaList.isEmpty())
        {
            return;
        }
        
        //例外地区转换
        List<Integer> idList = new ArrayList<Integer>();
        List<RelationDeliverAreaTemplateEntity> filterAreaList = filterExceptArea(areaList, idList, areaTemplate.getId());
        if (filterAreaList == null || filterAreaList.isEmpty())
        {
            return;
        }
        
        //插入转换后的数据
        relationDeliverAreaTemplateDao.insertRelationDeliverAreaTemplate(filterAreaList);
        
        //更新模版限制类型
        areaTemplate.setType((byte)(areaTemplate.getType() + 2));
        sellerDeliverAreaDao.updateSellerDeliverAreaTemplate(areaTemplate);
        
        //删除例外地区数据
        if (idList != null && !idList.isEmpty())
        {
            relationDeliverAreaTemplateDao.deleteRelationDeliverAreaTemplateByIdList(idList);
        }
        
    }
    
    private List<RelationDeliverAreaTemplateEntity> filterExceptArea(List<RelationDeliverAreaTemplateEntity> exceptAreaList, List<Integer> idList, int tempLateId)
        throws Exception
    {
        Set<String> provinceSet = new HashSet<String>();
        Set<String> citySet = new HashSet<String>();
        Set<String> districtSet = new HashSet<String>();
        
        Iterator<RelationDeliverAreaTemplateEntity> it = exceptAreaList.iterator();
        while (it.hasNext())
        {
            RelationDeliverAreaTemplateEntity area = it.next();
            if (StringUtils.equals(area.getDistrictCode(), "1"))
            {
                citySet.add(area.getCityCode());
                provinceSet.add(area.getProvinceCode());
                it.remove();
            }
            idList.add(area.getId());
        }
        
        List<Map<String, Object>> areaList = new ArrayList<Map<String, Object>>();
        if (!provinceSet.isEmpty() && !citySet.isEmpty())
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (provinceSet.size() > 0)
            {
                para.put("provinceIdList", new ArrayList<String>(provinceSet));
            }
            if (citySet.size() > 0)
            {
                para.put("cityIdList", new ArrayList<String>(citySet));
            }
            areaList.addAll(areaDao.findCityCodeByPara(para));
        }
        
        provinceSet.clear();
        citySet.clear();
        
        for (RelationDeliverAreaTemplateEntity area : exceptAreaList)
        {
            provinceSet.add(area.getProvinceCode());
            citySet.add(area.getCityCode());
            districtSet.add(area.getDistrictCode());
        }
        if (!provinceSet.isEmpty() && !citySet.isEmpty() && !districtSet.isEmpty())
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (provinceSet.size() > 0)
            {
                para.put("provinceIdList", new ArrayList<String>(provinceSet));
            }
            if (citySet.size() > 0)
            {
                para.put("cityIdList", new ArrayList<String>(citySet));
            }
            if (districtSet.size() > 0)
            {
                para.put("districtIdList", new ArrayList<String>(districtSet));
            }
            areaList.addAll(areaDao.findDistrictCodeByPara(para));
        }
        
        List<RelationDeliverAreaTemplateEntity> result = new ArrayList<RelationDeliverAreaTemplateEntity>();
        for (Map<String, Object> map : areaList)
        {
            RelationDeliverAreaTemplateEntity area = new RelationDeliverAreaTemplateEntity();
            area.setProvinceCode(map.get("provinceId") == null ? "1" : map.get("provinceId").toString());
            area.setCityCode(map.get("cityId") == null ? "1" : map.get("cityId").toString());
            area.setDistrictCode(map.get("districtId") == null ? "1" : map.get("districtId").toString());
            result.add(area);
        }
        
        if (!result.isEmpty())
        {
            BeanPropertyValueChangeClosure closure = new BeanPropertyValueChangeClosure("sellerDeliverAreaTemplateId", tempLateId);
            CollectionUtils.forAllDo(result, closure);
        }
        return result;
    }
    
    @Override
    public int updatePassword(SellerExpandEntity sellerExpand)
        throws Exception
    {
        return sellerExpandDao.updatePassword(sellerExpand);
    }
    
    @Override
    public String updateAvailableStatus(int id, int isAvailable)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> para = new HashMap<>();
        para.put("id", id);
        para.put("isAvailable", isAvailable);
        if (sellerDao.updateSellerByPara(para) > 0)
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
}
