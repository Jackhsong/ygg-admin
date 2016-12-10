package com.ygg.admin.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ygg.admin.code.AccountEnum;
import com.ygg.admin.dao.MwebGroupAccountDao;
import com.ygg.admin.dao.MwebGroupCouponDao;
import com.ygg.admin.dao.MwebGroupProductDao;
import com.ygg.admin.entity.MwebGroupAccountEntity;
import com.ygg.admin.entity.MwebGroupCouponDetailEntity;
import com.ygg.admin.entity.MwebGroupCouponEntity;
import com.ygg.admin.service.MwebGroupCouponService;
import com.ygg.admin.util.CommonUtil;

@Service("mwebGroupCouponService")
public class MwebGroupCouponServiceImpl implements MwebGroupCouponService
{
    Logger logger = Logger.getLogger(MwebGroupCouponServiceImpl.class);
    
    @Resource
    private MwebGroupCouponDao mwebGroupCouponDao;
    
    @Resource
    private MwebGroupProductDao mwebGroupProductDao;
    
    @Resource
    private MwebGroupAccountDao mwebGroupAccountDao;
    
    @Override
    public Map<String, Object> jsonCouponDetailInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> couponTypeInfoList = mwebGroupCouponDao.queryCouponDetailInfo(para);
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        int total = 0;
        if (couponTypeInfoList.size() > 0)
        {
            for (Map<String, Object> curr : couponTypeInfoList)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", curr.get("id"));
                map.put("index", curr.get("id"));
                int type = Integer.valueOf(curr.get("type") + "");
                int teamType = Integer.valueOf(curr.get("team_type") + "");
                int isRandomReduce = Integer.valueOf(curr.get("is_random_reduce") + "");
                String typeStr = "";
                int reduce = Integer.valueOf(curr.get("reduce") + "");
                if (isRandomReduce == 1)
                    typeStr = "随机--" + curr.get("lowest_reduce") + "到" + curr.get("maximal_reduce") + "元--";
                if (type == 1)
                    typeStr += "满" + curr.get("threshold") + "减" + (reduce) + "优惠券";
                else if (type == 2)
                    typeStr += (reduce == 0 ? "X" : reduce) + "元现金券";
                map.put("typeStr", typeStr);
                if (type == 1 && isRandomReduce == 1)
                    type = 4;
                if (type == 2 && isRandomReduce == 1)
                    type = 3;
                map.put("type", type);
                String teamTypeStr = "";
                if (teamType == 1)
                {
                    teamTypeStr = "仅用于开团";
                }
                else
                {
                    teamTypeStr = "开团参团均可使用";
                }
                map.put("teamTypeStr", teamTypeStr);
                int scopeType = Integer.parseInt(curr.get("scope_type") + "");
                int scopeId = Integer.parseInt(curr.get("scope_id") + "");
                map.put("scope", getCouponScopeType(scopeType, scopeId));
                map.put("scopeType", curr.get("scope_type"));
                map.put("teamType", curr.get("team_type"));
                map.put("scopeId", curr.get("scope_id"));
                map.put("threshold", curr.get("threshold"));
                map.put("reduce", curr.get("reduce"));
                map.put("lowestReduce", curr.get("lowest_reduce"));
                map.put("maximalReduce", curr.get("maximal_reduce"));
                map.put("isAvailable", curr.get("is_available"));
                map.put("status", Integer.valueOf(curr.get("is_available") + "") == 1 ? "可用" : "停用");
                map.put("desc", curr.get("desc"));
                resultList.add(map);
            }
            total = mwebGroupCouponDao.countCouponDetailInfo(para);
        }
        resultMap.put("rows", resultList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public int saveOrUpdate(Map<String, Object> para)
        throws Exception
    {
        
        if (para.get("id") == null)
        {// 新增
            return mwebGroupCouponDao.addCouponDetail(para);
        }
        else
        {// 修改
            return mwebGroupCouponDao.updateCouponDetail(para);
        }
    }
    
    @Override
    public boolean checkIsInUse(int couponDetailId)
        throws Exception
    {
        int count = mwebGroupCouponDao.countCouponDetailInUse(couponDetailId);
        return count < 1;
    }
    
    @Override
    public int updateCouponDetailStatus(int id, int isAvailable)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        if (isAvailable == 0)
        {
            para.put("isAvailable", 1);
        }
        if (isAvailable == 1)
        {
            para.put("isAvailable", 0);
        }
        return mwebGroupCouponDao.updateCouponDetailStatus(para);
    }
    
    @Override
    public Map<String, Object> jsonCouponInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> couponInfoList = mwebGroupCouponDao.queryCouponInfo(para);
        // List<Map<String, Object>> couponUsedList = mwebGroupCouponDao.findCouponUsedInfo();
        Map<Integer, Integer> couponUsedMap = new HashMap<Integer, Integer>();
        /*
         * for (Map<String, Object> map : couponUsedList) { int couponId = Integer.valueOf(map.get("couponId") == null ?
         * "0" : map.get("couponId") + "").intValue(); int amount = Integer.valueOf(map.get("amount") == null ? "0" :
         * map.get("amount") + "").intValue(); if (couponUsedMap.get(couponId) == null) { couponUsedMap.put(couponId,
         * amount); } else { couponUsedMap.put(couponId, amount + couponUsedMap.get(couponId).intValue()); } }
         */
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        int total = 0;
        if (couponInfoList.size() > 0)
        {
            for (Map<String, Object> curr : couponInfoList)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                int couponId = Integer.valueOf(curr.get("id") + "").intValue();
                map.put("id", curr.get("id"));
                map.put("startTime", (curr.get("startTime")).toString().replace(".0", ""));
                map.put("endTime", (curr.get("endTime")).toString().replace(".0", ""));
                map.put("createTime", (curr.get("createTime")).toString().replace(".0", ""));
                map.put("total", curr.get("total"));
                map.put("remark", curr.get("remark"));
                map.put("type", curr.get("couponType"));
                /* map.put("used", couponUsedMap.get(couponId) == null ? 0 : couponUsedMap.get(couponId)); */
                int couponType = Integer.valueOf(curr.get("couponType") + "");
                int isRandomReduce = Integer.valueOf(curr.get("isRandomReduce") + "").intValue();
                StringBuilder sb = new StringBuilder();
                if (couponType == 1)
                {
                    sb.append("满").append(curr.get("threshold")).append("减");
                    if (isRandomReduce == 1)
                    {
                        sb.append(curr.get("lowestReduce")).append("到").append(curr.get("maximalReduce")).append("随机优惠券");
                    }
                    else
                    {
                        sb.append(curr.get("reduce")).append("优惠券");
                    }
                }
                else if (couponType == 2)
                {
                    
                    if (isRandomReduce == 1)
                    {
                        sb.append(curr.get("lowestReduce")).append("到").append(curr.get("maximalReduce")).append("元现金券");
                    }
                    else
                    {
                        sb.append(curr.get("reduce")).append("元现金券");
                    }
                }
                map.put("couponType", sb.toString());
                int scopeType = Integer.parseInt(curr.get("scopeType") + "");
                int scopeId = Integer.parseInt(curr.get("scopeId") + "");
                String scope = getCouponScopeType(scopeType, scopeId);
                map.put("couponScope", scope);
                resultList.add(map);
            }
            total = mwebGroupCouponDao.countCouponInfo(para);
        }
        resultMap.put("rows", resultList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public List<Map<String, Object>> queryAllCouponType(int isAvailable, boolean needRandomReduce, int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("isAvailable", isAvailable);
        Date dateTmp7 = new Date();
        List<Map<String, Object>> couponTypeInfoList = mwebGroupCouponDao.queryCouponDetailInfo(para);
        Date dateTmp8 = new Date();
        long time8 = dateTmp8.getTime() - dateTmp7.getTime();
        logger.error("jsonCouponType查询，一共运行时间【" + (time8) + "毫秒】");
        
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        if (couponTypeInfoList.size() > 0)
        {
            Date dateTmp9 = new Date();
            for (Map<String, Object> curr : couponTypeInfoList)
            {
                
                int currIsRandomReduce = Integer.valueOf(curr.get("is_random_reduce") + "");
                if (currIsRandomReduce == 1 && !needRandomReduce)
                    continue;
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("code", curr.get("id"));
                if (Integer.valueOf(curr.get("id") + "") == id)
                {
                    map.put("selected", "true");
                }
                int couponType = Integer.valueOf(curr.get("type") + "");
                int scopeId = Integer.valueOf(curr.get("scope_id") + "");
                int scopeType = Integer.valueOf(curr.get("scope_type") + "");
                String desc = curr.get("desc") + "";
                
                StringBuilder sb = new StringBuilder();
                int reduce = Integer.valueOf(curr.get("reduce") + "");
                if (currIsRandomReduce == 1)
                {
                    sb.append("随机--" + curr.get("lowest_reduce") + "到" + curr.get("maximal_reduce") + "元--");
                }
                if (couponType == 1)
                {
                    sb.append("满").append(curr.get("threshold")).append("减").append((reduce == 0 ? "X" : reduce)).append("优惠券");
                }
                else if (couponType == 2)
                {
                    sb.append((reduce == 0 ? "X" : reduce)).append("元现金券");
                }
                sb.append(getCouponScopeType(scopeType, scopeId));
                sb.append("-" + desc);
                map.put("text", sb.toString());
                resultList.add(map);
            }
            Date dateTmp10 = new Date();
            long time9 = dateTmp10.getTime() - dateTmp9.getTime();
            logger.error("jsonCouponType循环，一共运行时间【" + (time9) + "毫秒】");
        }
        return resultList;
    }
    
    @Override
    public int insertCoupon(MwebGroupCouponEntity coupon)
        throws Exception
    {
        return mwebGroupCouponDao.insertCoupon(coupon);
    }
    
    @Override
    public int addCouponAccout(Map<String, Object> para)
        throws Exception
    {
        
        return mwebGroupCouponDao.addCouponAccout(para);
    }
    
    @Override
    public int sendCoupon(MwebGroupCouponEntity coupon, MultipartFile file, int operType, Integer userId)
        throws Exception
    {
        int result = 0;
        int startNum = 0;
        int lastNum = 0;
        Sheet sheet = null;
        // 向单个用户发送优惠券
        if (file == null)
        {
            coupon.setTotal(1);
        }
        else
        {
            // 向多个用户发送优惠券
            Workbook workbook = new HSSFWorkbook(file.getInputStream());
            sheet = workbook.getSheetAt(0);
            startNum = sheet.getFirstRowNum();
            lastNum = sheet.getLastRowNum();
            if (startNum == lastNum)
            {// 可过滤第一行，因为第一行是标题
                return -1;
            }
            coupon.setTotal(lastNum - startNum);
            
        }
        result = mwebGroupCouponDao.insertCoupon(coupon);
        
        long readFileTime = 0l;
        List<Map<String, Object>> addCouponAccoutList = new ArrayList<>();
        // List<Map<String, Object>> insertRegisterMobileCouponList = new ArrayList<>();
        if (result == 1)
        {
            MwebGroupCouponDetailEntity couponDetail = mwebGroupCouponDao.findCouponDetailById(coupon.getCouponDetailId());
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("couponId", coupon.getId());
            para.put("couponDetailId", coupon.getCouponDetailId());
            para.put("startTime", coupon.getStartTime());
            para.put("endTime", coupon.getEndTime());
            para.put("remark", couponDetail == null ? coupon.getRemark() : couponDetail.getDesc());
            para.put("createTime", coupon.getCreateTime());
            para.put("sourceType", 1);
            
            // 向单个用户发送优惠券
            if (operType == 3)
            {
                MwebGroupAccountEntity a = mwebGroupAccountDao.getAccounByBaseAccountId(Integer.valueOf(userId));
                if (a != null)
                {
                    para.put("accountId", a.getId());
                    result = mwebGroupCouponDao.addCouponAccout(para);
                    logger.info("****************向Id=" + userId + "的用户发放优惠券,发放成功**************");
                    return result;
                }
                
            }
            else if (operType == 4)
            {
                // 生成单张优惠券，不与任何用户关联
                logger.info("****************单张优惠券生成成功：id=" + coupon.getId() + "**************");
                return result;
            }
            else
            {
                // 向多个用户发送优惠券
                long begin = System.currentTimeMillis();
                for (int i = startNum + 1; i <= lastNum; i++)
                {
                    Map<String, Object> mp = new HashMap<String, Object>();
                    mp.put("couponId", coupon.getId());
                    mp.put("couponDetailId", coupon.getCouponDetailId());
                    mp.put("startTime", coupon.getStartTime());
                    mp.put("endTime", coupon.getEndTime());
                    mp.put("remark", couponDetail == null ? coupon.getRemark() : couponDetail.getDesc());
                    mp.put("createTime", coupon.getCreateTime());
                    mp.put("sourceType", 1);
                    try
                    {
                        Row row = sheet.getRow(i);
                        Cell cell0 = row.getCell(0);
                        if (cell0 != null)
                        {
                            cell0.setCellType(Cell.CELL_TYPE_STRING);
                        }
                        if (operType == 1)// 向用户ID发放
                        {
                            String accountId = cell0 == null ? "" : cell0.getStringCellValue();
                            if (StringUtils.isEmpty(accountId))
                            {
                                continue;
                            }
                            else
                            {
                                MwebGroupAccountEntity a = mwebGroupAccountDao.getAccounByBaseAccountId(Integer.valueOf(accountId));
                                if (a == null)
                                {
                                    continue;
                                }
                                else
                                {
                                    mp.put("accountId", a.getId());
                                    // logger.info("****************向Id=" + accountId + "的用户发放优惠券**************");
                                    // mwebGroupCouponDao.addCouponAccout(para);
                                    // logger.info("****************发放成功**************");
                                    addCouponAccoutList.add(mp);
                                }
                                
                            }
                        }
                        // else if (operType == 2)// 向用户名发放(仅限用户名为手机号)
                        // {
                        // String phoneNumber = cell0 == null ? "" : cell0.getStringCellValue();
                        // if (StringUtils.isEmpty(phoneNumber))
                        // {
                        // continue;
                        // }
                        // AccountEntity account = accountDao.findAccountByNameAndType(phoneNumber,
                        // AccountEnum.AccountType.MOBILE.getCode());
                        // // logger.info("****************向Name=" + phoneNumber + "的用户发放优惠券**************");
                        // if (account == null)
                        // {
                        // mp.put("phoneNumber", phoneNumber);
                        // // mwebGroupCouponDao.insertRegisterMobileCoupon(para);
                        // // logger.info("****************Name=" + phoneNumber +
                        // // "的用户不存在，插入register_mobile_coupon表**************");
                        // insertRegisterMobileCouponList.add(mp);
                        // }
                        // else
                        // {
                        // mp.put("accountId", account.getId());
                        // // mwebGroupCouponDao.addCouponAccout(para);
                        // // logger.info("****************发放成功**************");
                        // addCouponAccoutList.add(mp);
                        // }
                        // }
                    }
                    catch (Exception e)
                    {
                        logger.error("批量发送优惠券读取第" + i + "行数据出错了！！！");
                    }
                }
                readFileTime = System.currentTimeMillis() - begin;
            }
            
            int total = 0;
            if (!addCouponAccoutList.isEmpty())
            {
                // 分批发送，1500条一批
                List<List<Map<String, Object>>> splitList = CommonUtil.splitList(addCouponAccoutList, 1500);
                int count = 0;
                long start = System.currentTimeMillis();
                for (List<Map<String, Object>> list : splitList)
                {
                    count += mwebGroupCouponDao.batchAddCouponAccout(list);
                }
                total += count;
                logger.info("批量发放了" + count + "张优惠券,用时" + ((System.currentTimeMillis() - start + readFileTime) / 1000) + "s");
            }
            // if (!insertRegisterMobileCouponList.isEmpty())
            // {
            // // 分批发送，1500条一批
            // List<List<Map<String, Object>>> splitList = CommonUtil.splitList(insertRegisterMobileCouponList, 1500);
            // int count = 0;
            // long start = System.currentTimeMillis();
            // for (List<Map<String, Object>> list : splitList)
            // {
            // count += mwebGroupCouponDao.batchInsertRegisterMobileCoupon(list);
            // }
            // total += count;
            // logger.info("批量预发放了" + count + "张优惠券,用时" + ((System.currentTimeMillis() - start + readFileTime) / 1000) +
            // "s");
            // }
            // 批量发送，有可能实际发送的条数不等于excel中数据的条数
            if (coupon.getType() == 2 && coupon.getTotal() != total)
            {
                coupon.setTotal(total);
                mwebGroupCouponDao.updateCouponTotalNum(coupon);
            }
            result = total;
        }
        return result;
    }
    
    @Override
    public Map<String, Object> jsonCouponAccountInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> couponTypeInfoList = mwebGroupCouponDao.queryCouponAccountInfo(para);
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        int total = 0;
        if (couponTypeInfoList.size() > 0)
        {
            for (Map<String, Object> curr : couponTypeInfoList)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", curr.get("id"));
                map.put("accountId", curr.get("accountId"));
                map.put("teamAccountId", curr.get("teamAccountId"));
                map.put("nickName", curr.get("wechatNickName"));
                map.put("phoneNumber", curr.get("phoneNumber"));
                map.put("remark", curr.get("remark"));
                map.put("startTime", ((Timestamp)curr.get("startTime")).toString());
                map.put("endTime", ((Timestamp)curr.get("endTime")).toString());
                map.put("isUsed", "1".equals(curr.get("isUsed") + "") ? "已使用" : "未使用");
                map.put("usedCode", curr.get("isUsed"));
                resultList.add(map);
            }
            total = mwebGroupCouponDao.countCouponAccountInfo(para);
        }
        resultMap.put("rows", resultList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public Map<String, Object> jsonCouponOrderDetailInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> couponTypeInfoList = mwebGroupCouponDao.queryCouponOrderDetailInfo(para);
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        int total = 0;
        if (couponTypeInfoList.size() > 0)
        {
            for (Map<String, Object> curr : couponTypeInfoList)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                BigDecimal accountPoint = new BigDecimal(curr.get("accountPoint") + "").divide(new BigDecimal(100));
                BigDecimal couponPrice = new BigDecimal(curr.get("couponPrice") + "");
                BigDecimal adjustPrice = new BigDecimal(curr.get("adjustPrice") + "");
                map.put("orderId", curr.get("orderId"));
                map.put("orderNumber", curr.get("orderNumber"));
                map.put("payTime", curr.get("payTime") == null ? "" : ((Timestamp)curr.get("payTime")).toString());
                map.put("realPrice", curr.get("realPrice"));
                map.put("couponPrice", accountPoint.add(couponPrice).add(adjustPrice));
                map.put("sellerName", curr.get("sellerName") + (curr.get("sendAddress") + "-" + curr.get("warehouse")));
                resultList.add(map);
            }
            total = mwebGroupCouponDao.countCouponOrderDetailInfo(para);
        }
        resultMap.put("rows", resultList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public Map<String, Object> queryCouponAccount(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = mwebGroupCouponDao.queryCouponAccount(para);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        for (Map<String, Object> map : result)
        {
            resultMap.put("couponDetailId", map.get("couponDetailId"));
            resultMap.put("remark", map.get("remark"));
            resultMap.put("startTime", map.get("startTime"));
            resultMap.put("endTime", map.get("endTime"));
            int couponType = Integer.valueOf(map.get("couponType") + "");
            int scopeId = Integer.valueOf(map.get("scopeId") + "");
            int scopeType = Integer.valueOf(map.get("scopeType") + "");
            StringBuilder sb = new StringBuilder();
            if (couponType == 1)
            {
                sb.append("满").append(map.get("threshold")).append("减").append(map.get("reduce")).append("优惠券");
            }
            else if (couponType == 2)
            {
                sb.append(map.get("reduce")).append("元现金券");
            }
            sb.append(getCouponScopeType(scopeType, scopeId));
            resultMap.put("couponScope", sb);
            resultMap.put("accountId", map.get("accountId"));
            resultMap.put("teamAccountId", map.get("teamAccountId"));
            resultMap.put("accountName", map.get("accountName"));
            resultMap.put("accountType", AccountEnum.MWEB_USER_TYPE.getDescByCode(Integer.valueOf(map.get("accountType") + "")));
            
        }
        return resultMap;
    }
    
    private String getCouponScopeType(int scopeType, int scopeId)
    {
        StringBuilder sb = new StringBuilder(" ");
        try
        {
            // log.error("scopeType【" +(scopeType) + "毫秒】");
            switch (scopeType)
            {
                case 1:
                    sb.append("全场通用");
                    break;
                case 2:
                    sb.append("仅限商品").append(mwebGroupProductDao.getProduct(scopeId).getShortName()).append("使用");
                    break;
                    
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        return sb.toString();
    }
    
    @Override
    public MwebGroupCouponDetailEntity findCouponDetailById(int couponDetailId)
        throws Exception
    {
        return mwebGroupCouponDao.findCouponDetailById(couponDetailId);
    }
    
    @Override
    public MwebGroupCouponEntity findCouponById(int couponId)
        throws Exception
    {
        return mwebGroupCouponDao.findCouponById(couponId);
    }
    
    @Override
    public String findCouponTotalMoney(int couponId)
        throws Exception
    {
        return mwebGroupCouponDao.findCouponTotalMoney(couponId);
    }
    
    @Override
    public Map<String, Object> findCouponInfoByCouponAccountId(int id)
        throws Exception
    {
        Map<String, Object> info = mwebGroupCouponDao.findCouponAccountInfoByCouponAccountId(id);
        int couponDetailId = Integer.valueOf(info.get("couponDetailId") + "");
        MwebGroupCouponDetailEntity cde = mwebGroupCouponDao.findCouponDetailById(couponDetailId);
        String desc = "";
        if (cde.getIsRandomReduce() == 1)
        {
            int reducePrice = Integer.valueOf(info.get("reducePrice") + "");
            if (cde.getType() == 1)
            {
                desc = "满" + cde.getThreshold() + "减" + reducePrice;
            }
            else
            {
                desc = reducePrice + "元现金券";
            }
        }
        else
        {
            if (cde.getType() == 1)
            {
                desc = "满" + cde.getThreshold() + "减" + cde.getReduce();
            }
            else
            {
                desc = cde.getReduce() + "元现金券";
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("desc", desc);
        return result;
    }
    
    @Override
    public int findCouponUsedInfo(int couponId)
        throws Exception
    {
        return mwebGroupCouponDao.findCouponUsedInfo(couponId);
    }
}
