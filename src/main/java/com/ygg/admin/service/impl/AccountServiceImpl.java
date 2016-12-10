package com.ygg.admin.service.impl;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.cache.CacheManager;
import com.ygg.admin.cache.CacheServiceIF;
import com.ygg.admin.code.AccountEnum;
import com.ygg.admin.code.ProposeTypeEnum;
import com.ygg.admin.code.SellerEnum;
import com.ygg.admin.config.YggAdminProperties;
import com.ygg.admin.dao.AccountDao;
import com.ygg.admin.dao.ActivitiesCommonDao;
import com.ygg.admin.dao.CategoryDao;
import com.ygg.admin.dao.CouponDao;
import com.ygg.admin.dao.ProductDao;
import com.ygg.admin.dao.SellerDao;
import com.ygg.admin.dao.SystemLogDao;
import com.ygg.admin.dao.UserDao;
import com.ygg.admin.entity.AccountEntity;
import com.ygg.admin.entity.User;
import com.ygg.admin.service.AccountService;
import com.ygg.admin.util.CacheConstant;
import com.ygg.admin.util.CommonEnum;
import com.ygg.admin.util.CommonUtil;
import com.ygg.admin.util.DateTimeUtil;
import com.ygg.admin.util.MathUtil;
import com.ygg.admin.util.POIUtil;
import com.ygg.admin.util.StringUtils;

@Service("accountService")
public class AccountServiceImpl implements AccountService
{
    private Logger log = Logger.getLogger(AccountServiceImpl.class);
    
    private CacheServiceIF cache = CacheManager.getClient();
    
    @Resource
    private AccountDao accountDao;
    
    @Resource
    private SystemLogDao logDao;
    
    @Resource
    private UserDao userDao;
    
    @Resource
    private ActivitiesCommonDao activitiesCommonDao;
    
    @Resource
    private ProductDao productDao;
    
    @Resource
    private CategoryDao categoryDao;
    
    @Resource
    private SellerDao sellerDao;
    
    @Resource
    private CouponDao couponDao;
    
    @Override
    public Map<String, Object> jsonAccountInfo(Map<String, Object> para, int searchType)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        int total = 0;
        
        if (searchType == 1) // 正常用户列表
        {
            List<AccountEntity> reList = accountDao.findAllAccountByPara(para);
            if (reList.size() > 0)
            {
                for (AccountEntity curr : reList)
                {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", curr.getId());
                    map.put("name", curr.getName());
                    map.put("nickname", curr.getNickname());
                    map.put("mobileNumber", curr.getMobileNumber());
                    map.put("createTime", curr.getCreateTime());
                    String typeDescString = AccountEnum.ACCOUNT_TYPE.getDescByCode(curr.getType());
                    map.put("type", typeDescString);
                    String totalAmount = accountDao.finAccountTotalMoney(curr.getId());
                    map.put("totalAmount", totalAmount);
                    resultList.add(map);
                }
                total = accountDao.countAccountByPara(para);
            }
        }
        else if (searchType == 2) //推送用户列表
        {
            resultList = accountDao.findAllAccountPushByPara(para);
            if (resultList.size() > 0)
            {
                for (Map<String, Object> it : resultList)
                {
                    byte type = Byte.valueOf(it.get("type") + "");
                    it.put("checkId", it.get("id"));
                    it.put("typeStr", AccountEnum.ACCOUNT_TYPE.getDescByCode(type));
                    it.put("createTime", DateTimeUtil.timestampStringToWebString(it.get("createTime") + ""));
                }
                total = accountDao.countAllAccountPushByPara(para);
            }
        }
        
        resultMap.put("rows", resultList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public int getJsonAccountInfoNums(Map<String, Object> para, int searchType)
        throws Exception
    {
        int total = 0;
        if (searchType == 1) // 正常用户列表
        {
            total = accountDao.countAccountByPara(para);
        }
        else if (searchType == 2) //推送用户列表
        {
            total = accountDao.countAllAccountPushByPara(para);
        }
        return total;
    }
    
    @Override
    public Map<String, Object> updatePWD(int accountId, String pwd)
        throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        AccountEntity account = accountDao.findAccountById(accountId);
        if (account.getType() != (byte)1)
        {
            result.put("status", 0);
            result.put("msg", "只有手机用户能够修改密码");
            return result;
        }
        String md5Pwd = CommonUtil.strToMD5(pwd + account.getName());
        map.put("pwd", md5Pwd);
        map.put("id", accountId);
        accountDao.updatePWD(map);
        result.put("status", 1);
        return result;
    }
    
    @Override
    public Map<String, Object> findAllAccountCard(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> cards = accountDao.findAllAccountCard(para);
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        int total = 0;
        if (cards.size() > 0)
        {
            total = accountDao.countAllAccountCard(para);
            for (Map<String, Object> cardInfo : cards)
            {
                Map<String, Object> row = new HashMap<String, Object>();
                row.put("accountId", cardInfo.get("id"));
                row.put("name", cardInfo.get("name"));
                row.put("cardName", cardInfo.get("cardName"));
                int type = Integer.valueOf(cardInfo.get("type") + "");
                int bankType = Integer.valueOf(cardInfo.get("bankType") + "");
                row.put("typeStr", type == 1 ? "银行" : "支付宝");
                if (type == 1)
                {
                    // 银行
                    row.put("bankTypeStr", CommonEnum.BankTypeEnum.getDescriptionByOrdinal(bankType));
                    // 银行卡号
                    row.put("bankCardNumber", cardInfo.get("cardNumber"));
                    row.put("alipayCardNumber", "");
                }
                else
                {
                    row.put("bankTypeStr", "");
                    row.put("bankCardNumber", "");
                    row.put("alipayCardNumber", cardInfo.get("cardNumber"));
                }
                rows.add(row);
            }
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }
    
    @Override
    public Map<String, Object> findAllAccountIntegral(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        List<AccountEntity> aes = accountDao.findAllAccountByPara(para);
        int total = 0;
        if (aes.size() > 0)
        {
            /*List<Integer> idList = new ArrayList<Integer>();
            for (AccountEntity it : aes)
            {
                idList.add(it.getId());
            }
            List<Map<String, Object>> timeList = accountDao.findLastIntegralUpdateTime(idList);
            Map<String, String> timeMap = new HashMap<String, String>();
            for (Map<String, Object> map : timeList)
            {
                String accountId = map.get("accountId") + "";
                Timestamp createTime = (Timestamp)map.get("createTime");
                timeMap.put(accountId, createTime.toString());
            }*/
            for (AccountEntity it : aes)
            {
                Map<String, Object> row = new HashMap<String, Object>();
                row.put("id", it.getId());
                row.put("name", it.getName());
                row.put("mobileNumber", it.getMobileNumber());
                row.put("type", it.getType());
                row.put("typeStr", AccountEnum.ACCOUNT_TYPE.getDescByCode(it.getType()));
                row.put("integral", it.getAvailablePoint());
                // 查询用户积分最后更新时间
                //String lastIntegralUpdateTime = timeMap.get(it.getId() + "");
                //row.put("lastIntegralUpdateTime", lastIntegralUpdateTime == null ? "" : lastIntegralUpdateTime);
                row.put("createTime", DateTimeUtil.timestampStringToWebString(it.getCreateTime()));
                rows.add(row);
            }
            total = accountDao.countAccountByPara(para);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("rows", rows);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public int getAllAccountIntegralNums(Map<String, Object> para)
        throws Exception
    {
        return accountDao.countAccountByPara(para);
    }
    
    @Override
    public Map<String, Object> updateAccountIntegral(int accountId, int integral, boolean isForcibly, String source, String reason)
        throws Exception
    {
        try
        {
            if (source.startsWith("dealrefund"))
            {
                Object thisSource = cache.get(CacheConstant.ADMIN_ACCOUNTSERVICE_UPDATEACCOUNTINTEGRAL + source);
                if (thisSource != null)
                {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("status", 0);
                    map.put("msg", "保存失败，您已处理过相同操作！");
                    return map;
                }
                else
                {
                    cache.set(CacheConstant.ADMIN_ACCOUNTSERVICE_UPDATEACCOUNTINTEGRAL + source, source, 7 * 24 * 60 * 60);
                }
            }
            boolean forciblyUpdate = false;//是否进过强制处理用户积分为0
            int disIntegral = 0;
            Map<String, Object> result = new HashMap<String, Object>();
            AccountEntity account = accountDao.findAccountById(accountId);
            int oldAvailablePoint = account.getAvailablePoint();
            if ((integral < 0) && ((oldAvailablePoint + integral) < 0))
            {
                // 积分不足
                if (!isForcibly)
                {
                    result.put("status", 0);
                    result.put("msg", "积分不足");
                    result.put("remainPoint", oldAvailablePoint);
                    return result;
                }
                disIntegral = (-integral) - oldAvailablePoint;
                integral = -oldAvailablePoint;
                forciblyUpdate = true;
            }
            int newAvailablePoint = oldAvailablePoint + integral;
            Map<String, Object> updatePara = new HashMap<String, Object>();
            updatePara.put("id", accountId);
            updatePara.put("oldAvailablePoint", oldAvailablePoint);
            updatePara.put("newAvailablePoint", newAvailablePoint);
            int status = accountDao.updateIntegral(updatePara);
            if (status == 1)
            {
                if (forciblyUpdate)
                {
                    result.put("remainPoint", oldAvailablePoint);
                    result.put("disIntegral", MathUtil.round(disIntegral / 100.0, 2));
                    result.put("forciblyUpdate", 1);
                }
                else
                {
                    result.put("forciblyUpdate", 0);
                }
                result.put("status", 1);
                // 插入积分变动记录表
                Map<String, Object> integralPara = new HashMap<String, Object>();
                integralPara.put("accountId", accountId);
                integralPara.put("operatePoint", integral < 0 ? -integral : integral);
                integralPara.put("totalPoint", newAvailablePoint);
                integralPara.put("operateType", AccountEnum.INTEGRAL_OPERATION_TYPE.ADMIN_OPERATION.getCode());
                integralPara.put("arithmeticType", integral < 0 ? 2 : 1);
                integralPara.put("createTime", DateTimeUtil.now());
                accountDao.insertIntegralRecord(integralPara);
                //调整用户积分记录日志
                try
                {
                    String username = SecurityUtils.getSubject().getPrincipal() + "";
                    Map<String, Object> logInfoMap = new HashMap<String, Object>();
                    logInfoMap.put("username", username);
                    logInfoMap.put("businessType", CommonEnum.BusinessTypeEnum.USER_MANAGEMENT.ordinal());
                    logInfoMap.put("operationType", CommonEnum.OperationTypeEnum.MODIFY_USER_INTEGRAL.ordinal());
                    logInfoMap.put("level", CommonEnum.LogLevelEnum.LEVEL_THREE.ordinal());
                    logInfoMap.put("content", "用户【" + username + "】将id=" + accountId + " 的用户的积分增加了 " + integral
                        + (source.startsWith("dealrefund") ? "。操作来源：退款退货处理" : "。调整原因：" + reason));
                    logDao.logger(logInfoMap);
                }
                catch (Exception e)
                {
                    log.error(e.getMessage(), e);
                }
                
            }
            else
            {
                result.put("status", 0);
                result.put("msg", "修改积分失败");
            }
            return result;
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "系统繁忙，请稍后再试。");
            return result;
        }
    }
    
    @Override
    public Map<String, Object> findAccountIntegralRecord(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> records = accountDao.findAccountAvailablePointRecord(para);
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        int total = 0;
        if (records.size() > 0)
        {
            for (Map<String, Object> it : records)
            {
                Map<String, Object> row = new HashMap<String, Object>();
                row.put("typeStr", AccountEnum.INTEGRAL_OPERATION_TYPE.getDescByCode(Integer.valueOf(it.get("operateType") + "")));
                int arithmeticType = Integer.valueOf(it.get("arithmeticType") + "");
                int operatePoint = Integer.valueOf(it.get("operatePoint") + "");
                if (arithmeticType == 2)
                {
                    // 减
                    operatePoint = 0 - operatePoint;
                }
                row.put("operatePoint", operatePoint);
                row.put("totalPoint", it.get("totalPoint"));
                Timestamp createTime = (Timestamp)it.get("createTime");
                row.put("createTime", createTime.toString());
                rows.add(row);
            }
            total = accountDao.countAccountAvailablePointRecord(para);
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }
    
    @Override
    public Map<String, Object> findAccountById(int id)
        throws Exception
    {
        AccountEntity account = accountDao.findAccountById(id);
        if (account == null)
            return null;
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("typeStr", AccountEnum.ACCOUNT_TYPE.getDescByCode(account.getType()));
        result.put("id", account.getId() + "");
        result.put("name", account.getName());
        result.put("integral", account.getAvailablePoint() + "");
        return result;
    }
    
    @Override
    public AccountEntity findAccountByName(String name)
        throws Exception
    {
        return accountDao.findAccountByName(name);
    }
    
    @Override
    public int updateAccountIntegralBySpending(List<Map<String, Object>> list)
        throws Exception
    {
        for (Map<String, Object> map : list)
        {
            
            Map<String, Object> integralPara = new HashMap<String, Object>();
            integralPara.put("accountId", map.get("accountId"));
            integralPara.put("operatePoint", new BigDecimal(map.get("totalPrice") + "").setScale(0, BigDecimal.ROUND_HALF_UP));
            integralPara.put("totalPoint", new BigDecimal(map.get("totalPrice") + "").setScale(0, BigDecimal.ROUND_HALF_UP));
            integralPara.put("operateType", AccountEnum.INTEGRAL_OPERATION_TYPE.SHOPPING_GIFT.getCode());
            integralPara.put("arithmeticType", 1);
            integralPara.put("createTime", DateTimeUtil.now());
            log.info("**********用户accountId=" + map.get("accountId") + "新增积分" + new BigDecimal(map.get("totalPrice") + "").setScale(0, BigDecimal.ROUND_HALF_UP) + "**********");
            accountDao.updateAccountIntegral(Integer.valueOf(map.get("accountId") + ""), new BigDecimal(map.get("totalPrice") + "").setScale(0, BigDecimal.ROUND_HALF_UP)
                .intValue());
            accountDao.insertIntegralRecord(integralPara);
        }
        return 1;
    }
    
    @Override
    public List<String> findAccountPushIdByAccountId(int id)
        throws Exception
    {
        return accountDao.findAccountPushIdByAccountId(id);
    }
    
    @Override
    public int saveAccountCard(int accountId, int bankOrAlipay, String cardName, String cardNumber, int bankType)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", 0);
        para.put("accountId", accountId);
        para.put("type", bankOrAlipay);
        para.put("bankType", bankType);
        para.put("cardName", cardName);
        para.put("cardNumber", cardNumber);
        int status = accountDao.saveAccountCard(para);
        if (status == 1)
        {
            return Integer.parseInt(para.get("id") + "");
        }
        return 0;
    }
    
    @Override
    public int resetRecommendedCode()
        throws Exception
    {
        int result = 0;
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("recommendedCode", "");
        List<AccountEntity> acs = accountDao.findAllAccountByPara(para);
        for (AccountEntity it : acs)
        {
            if (it.getRecommendedCode() == null || it.getRecommendedCode().equals(""))
            {
                int turns = 1;
                while (turns++ < 10)
                {
                    try
                    {
                        String code = StringUtils.getGenerateLetterWithNum(6);
                        para.put("recommendedCode", code);
                        List<AccountEntity> existsList = accountDao.findAllAccountByPara(para);
                        if (existsList != null && existsList.size() > 0)
                        {
                            log.info("已存在，跳过code");
                            continue;
                        }
                        Map<String, Object> updatePara = new HashMap<String, Object>();
                        updatePara.put("id", it.getId());
                        updatePara.put("recommendedCode", code);
                        int status = accountDao.updateRecommendedCode(updatePara);
                        if (status == 1)
                        {
                            result++;
                            break;
                        }
                    }
                    catch (Exception e)
                    {
                        log.error("更新用户邀请码失败！", e);
                    }
                }
            }
        }
        return result;
    }
    
    @Override
    public Map<String, Object> findAllProposeInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = accountDao.findAllPropose(para);
        List<User> userList = userDao.findUserByPara(new HashMap<String, Object>());
        Map<String, String> userIdAndNameMap = new HashMap<String, String>();
        for (User user : userList)
        {
            userIdAndNameMap.put(user.getId() + "", user.getRealname());
        }
        int total = 0;
        for (Map<String, Object> map : infoList)
        {
            int type = Integer.valueOf(map.get("type") + "").intValue();
            map.put("content", URLDecoder.decode(map.get("content") + "", "UTF-8"));
            map.put("contact", URLDecoder.decode(map.get("contact") + "", "UTF-8"));
            map.put("proposeType", ProposeTypeEnum.getDesc(type));
            map.put("isBackPointDesc", ((int)map.get("isBackPoint")) == 1 ? "是" : "否");
            map.put("isDealDesc", ((int)map.get("isDeal")) == 1 ? "是" : "否");
            map.put("createTime", ((Timestamp)map.get("createTime")).toString());
            map.put("os", (int)map.get("os") == 1 ? "iOS" : "Android");
            map.put("dealUser", userIdAndNameMap.get(map.get("dealUser") + "") == null ? "" : userIdAndNameMap.get(map.get("dealUser") + ""));
        }
        total = accountDao.countProposes(para);
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public int returnScoreForPropose(int id, int accountId, int score)
        throws Exception
    {
        accountDao.updateAccountIntegral(accountId, score);
        AccountEntity account = accountDao.findAccountById(accountId);
        if (account == null)
        {
            return -1;
        }
        int totalPoint = account.getAvailablePoint();
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("accountId", accountId);
        para.put("operatePoint", score);
        para.put("totalPoint", totalPoint);
        para.put("operateType", AccountEnum.INTEGRAL_OPERATION_TYPE.ADMIN_OPERATION.getCode());
        para.put("arithmeticType", 1);
        accountDao.insertIntegralRecord(para);
        
        para.put("backPoint", score);
        para.put("id", id);
        accountDao.updateAccountPropose(para);
        return 1;
    }
    
    @Override
    public int updateDealContent(int id, String dealContent)
        throws Exception
    {
        String username = SecurityUtils.getSubject().getPrincipal() + "";
        User user = userDao.findUserByUsername(username);
        
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        para.put("dealContent", dealContent);
        para.put("isDeal", 1);
        para.put("dealUser", user == null ? Integer.parseInt(YggAdminProperties.getInstance().getPropertie("admin_account_id")) : user.getId());
        return accountDao.updateDealContent(para);
    }
    
    @Override
    public Map<String, Object> findAllAccountCouponInfo(Map<String, Object> para)
        throws Exception
    {
        Object reduceMin = para.get("reduceMin");
        Object reduceMax = para.get("reduceMax");
        boolean isSearchCouponId = false;
        Set<Integer> couponIdSet = new HashSet<Integer>();
        if (reduceMin != null)
        {
            isSearchCouponId = true;
            Map<String, Object> searchPara = new HashMap<String, Object>();
            searchPara.put("reduceMin", reduceMin);
            //查找随机优惠券面额
            List<Integer> randomCouponIdList = couponDao.findRandomCouponIdByPara(searchPara);
            couponIdSet.addAll(randomCouponIdList);
            //查找非随机优惠券面额
            List<Integer> reduceCouponIdList = couponDao.findReduceCouponIdByPara(searchPara);
            couponIdSet.addAll(reduceCouponIdList);
        }
        if (reduceMax != null)
        {
            isSearchCouponId = true;
            Map<String, Object> searchPara = new HashMap<String, Object>();
            searchPara.put("reduceMax", reduceMax);
            //查找随机优惠券面额Id
            List<Integer> randomCouponIdList = couponDao.findRandomCouponIdByPara(searchPara);
            //查找非随机优惠券面额Id
            List<Integer> reduceCouponIdList = couponDao.findReduceCouponIdByPara(searchPara);
            couponIdSet.addAll(randomCouponIdList);
            couponIdSet.addAll(reduceCouponIdList);
        }
        if (isSearchCouponId)
        {
            para.put("couponIdList", new ArrayList<Integer>(couponIdSet));
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = accountDao.findAccountCouponInfo(para);
        int total = 0;
        for (Map<String, Object> curr : infoList)
        {
            int type = Integer.valueOf(curr.get("couponDetailType") + "");
            int isRandomReduce = Integer.valueOf(curr.get("isRandomReduce") + "");
            String typeStr = "";
            int reduce = Integer.valueOf(curr.get("reduce") + "");
            if (isRandomReduce == 1)
            {
                curr.put("reduce", curr.get("reducePrice"));
                typeStr = "随机--" + curr.get("lowestReduce") + "到" + curr.get("maximalReduce") + "元--";
            }
            if (type == 1)
            {
                typeStr += "满" + curr.get("threshold") + "减" + (reduce) + "优惠券";
            }
            else if (type == 2)
            {
                typeStr += (reduce == 0 ? "X" : reduce) + "元现金券";
            }
            int scopeType = Integer.parseInt(curr.get("scopeType") + "");
            int scopeId = Integer.parseInt(curr.get("scopeId") + "");
            curr.put("couponDetailtypeStr", typeStr);
            curr.put("couponScope", getCouponScopeType(scopeType, scopeId));
            curr.put("isUsedStr", (int)curr.get("isUsed") == 1 ? "已使用" : "未使用");
            curr.put("acquireTime", curr.get("acquireTime") == null ? "" : ((Timestamp)curr.get("acquireTime")).toString());
            curr.put("validTimeBegin", curr.get("validTimeBegin") == null ? "" : ((Timestamp)curr.get("validTimeBegin")).toString());
            curr.put("validTimeEnd", curr.get("validTimeEnd") == null ? "" : ((Timestamp)curr.get("validTimeEnd")).toString());
        }
        total = accountDao.countAccountCouponInfo(para);
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    private String getCouponScopeType(int scopeType, int scopeId)
    {
        StringBuilder sb = new StringBuilder(" ");
        try
        {
            switch (scopeType)
            {
                case 1:
                    sb.append("全场通用");
                    break;
                case 2:
                    sb.append("仅限专场").append(activitiesCommonDao.findAcCommonById(scopeId).getName()).append("使用");
                    break;
                case 3:
                    sb.append("仅限商品").append(productDao.findProductByID(scopeId, null).getName()).append("使用");
                    break;
                case 4:
                    sb.append("仅限二级类目").append(categoryDao.findCategorySecondById(scopeId).getName()).append("使用");
                    break;
                case 5:
                    sb.append("仅限卖家").append(sellerDao.findSellerById(scopeId).getRealSellerName()).append("使用");
                    break;
                case 6:
                    SellerEnum.SellerTypeEnum sellerTypeEnum = SellerEnum.SellerTypeEnum.getSellerTypeEnumByCode(scopeId);
                    sb.append("仅限发货类型为").append(sellerTypeEnum.getDesc());
                    if (sellerTypeEnum.getCode() == SellerEnum.SellerTypeEnum.HONG_KONG.getCode())
                    {
                        sb.append(sellerTypeEnum.getIsNeedIdCardImage() == 1 ? "(身份证照片)" : sellerTypeEnum.getIsNeedIdCardNumber() == 1 ? "(仅身份证号)" : "");
                    }
                    sb.append("使用");
                    break;
            }
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
        return sb.toString();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public String importAccountIntegral(MultipartFile file)
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
        List<Map<String, Object>> dataList = (List<Map<String, Object>>)sheetData.get("data");
        List<Map<String, Object>> updateList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> insertList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < dataList.size(); i++)
        {
            int accountId = Integer.parseInt(dataList.get(i).get("cell0") == null ? "0" : dataList.get(i).get("cell0") + "");
            int operateType = Integer.parseInt(dataList.get(i).get("cell1") == null ? "1" : dataList.get(i).get("cell1") + "");
            int point = Integer.parseInt(dataList.get(i).get("cell2") == null ? "0" : dataList.get(i).get("cell2") + "");
            AccountEntity ae = accountDao.findAccountById(accountId);
            if (ae == null)
            {
                continue;
            }
            if (operateType != 1 && operateType != 2)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "操作类型只能为1（加）或2（减），第" + (i + 1) + "行操作类型不正确");
                return JSON.toJSONString(resultMap);
            }
            if (point < 0)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "积分只能为正整数，第" + (i + 1) + "行积分不正确");
                return JSON.toJSONString(resultMap);
            }
            
            if (operateType == 2)
            {
                point = -1 * point;
            }
            int newPoint = ae.getAvailablePoint() + point;
            if (newPoint < 0)
            {
                newPoint = 0;
            }
            Map<String, Object> update = new HashMap<String, Object>();
            update.put("id", accountId);
            update.put("oldAvailablePoint", ae.getAvailablePoint());
            update.put("newAvailablePoint", newPoint);
            updateList.add(update);
            
            Map<String, Object> insert = new HashMap<String, Object>();
            insert.put("accountId", accountId);
            insert.put("operatePoint", point < 0 ? -point : point);
            insert.put("totalPoint", newPoint);
            insert.put("operateType", AccountEnum.INTEGRAL_OPERATION_TYPE.ADMIN_OPERATION.getCode());
            insert.put("arithmeticType", operateType);
            insert.put("createTime", DateTimeUtil.now());
            insertList.add(insert);
        }
        
        if (updateList.size() > 0)
        {
            accountDao.batchUpdateIntegral(updateList);
        }
        if (insertList.size() > 0)
        {
            accountDao.batchInsertIntegralRecord(insertList);
        }
        resultMap.put("status", 1);
        resultMap.put("msg", "导入成功");
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public Map<String, Object> findAccountBlacklist(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> blackList = accountDao.findAccountBlacklist(para);
        int total = 0;
        if (blackList.size() > 0)
        {
            total = accountDao.countAccountBlacklist(para);
            for (Map<String, Object> map : blackList)
            {
                int partnerStatus = Integer.valueOf(map.get("partnerStatus") + "");
                map.put("partnerStatus", partnerStatus == 1 ? "不是合伙人" : (partnerStatus == 2 ? "合伙人" : "合伙人被禁用"));
                map.put("freezeTime", DateTimeUtil.timestampObjectToWebString(map.get("freezeTime")));
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("rows", blackList);
        result.put("total", total);
        return result;
    }
    
    @Override
    public int deleteBlacklist(int accountId)
        throws Exception
    {
        return accountDao.deleteBlacklist(accountId);
    }
    
    @Override
    public int addBlacklist(int accountId, String remark)
        throws Exception
    {
        if (accountDao.findAccountBlacklistByAccountId(accountId) != null)
        {
            return 1;
        }
        return accountDao.insertBlacklist(accountId, remark);
    }
}
