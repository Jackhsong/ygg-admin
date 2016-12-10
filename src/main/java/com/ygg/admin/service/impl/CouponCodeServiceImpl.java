package com.ygg.admin.service.impl;

import com.ygg.admin.code.AccountEnum;
import com.ygg.admin.code.CouponEnum;
import com.ygg.admin.code.SellerEnum;
import com.ygg.admin.dao.*;
import com.ygg.admin.entity.CouponCodeEntity;
import com.ygg.admin.entity.CouponDetailEntity;
import com.ygg.admin.exception.ServiceException;
import com.ygg.admin.service.CouponCodeService;
import com.ygg.admin.util.DateTimeUtil;
import com.ygg.admin.util.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class CouponCodeServiceImpl implements CouponCodeService
{
    Logger log = Logger.getLogger(CouponCodeServiceImpl.class);
    
    @Resource
    private CouponCodeDao couponCodeDao;
    
    @Resource
    private CouponDao couponDao;
    
    @Resource
    private ActivitiesCommonDao activitiesCommonDao;
    
    @Resource
    private ProductDao productDao;
    
    @Resource
    private CategoryDao categoryDao;
    
    @Resource
    private SellerDao sellerDao;
    
    @Resource
    private AccountDao accountDao;
    
    @Override
    public Map<String, Object> addCouponCode(List<Map<String, Object>> couponDetailIdAndCountList, String startTime, String endTime, String desc, int nums, int type,
        String customCode)
        throws Exception
    {
        
        Date startTimeDate = DateTimeUtil.string2Date(startTime);
        Date endTimeDate = DateTimeUtil.string2Date(endTime);
        if (startTimeDate.after(endTimeDate))
        {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "优惠码开始时间不得大于结束时间");
            return result;
        }
        
        if (type == CouponEnum.CouponCodeType.ONE_CODE_MANY_USE.getCode())
        {
            boolean isFlag = false;
            String msg = "";
            if (customCode != null && customCode.length() > 15)
            {
                isFlag = true;
                msg = "自定义优惠码长度不得超过15个字符";
            }
            else if (Pattern.matches("[a-zA-Z]{9}", customCode))
            {
                isFlag = true;
                msg = "不允许生成9位全英文优惠码";
            }
            if (isFlag)
            {
                Map<String, Object> result = new HashMap<String, Object>();
                result.put("status", 0);
                result.put("msg", msg);
                return result;
            }
        }
        
        CouponCodeEntity couponCode = new CouponCodeEntity();
        //        couponCode.setCouponDetailId(couponDetailId);
        couponCode.setSameMaxCount(1);
        couponCode.setStartTime(startTime);
        couponCode.setEndTime(endTime);
        couponCode.setIsAvailable((byte)1);
        couponCode.setCreateTime(DateTimeUtil.now());
        couponCode.setType((byte)type);
        couponCode.setDesc(desc);
        //        couponCode.setRemark(couponDetailDesc);
        
        if (couponDetailIdAndCountList.size() == 1)
        {
            //新增优惠码，包含一张优惠券。本来写好的代码，次奥，天天改，天天改
            Map<String, Object> couponDetailIdAndCount = couponDetailIdAndCountList.get(0);
            int couponDetailId = Integer.parseInt(couponDetailIdAndCount.get("couponDetailId") + "");
            int count = Integer.parseInt(couponDetailIdAndCount.get("count") + "");
            String couponDetailDesc = couponCodeDao.findCouponDetailDesc(couponDetailId);
            if (couponDetailDesc == null)
            {
                Map<String, Object> result = new HashMap<String, Object>();
                result.put("status", 0);
                result.put("msg", "优惠券类型不存在");
                return result;
            }
            couponCode.setCouponDetailId(couponDetailId);
            couponCode.setRemark(couponDetailDesc);
            couponCode.setChangeType(Byte.parseByte(CouponEnum.CouponCodeChangeType.SINGLE.getCode() + ""));
            couponCode.setChangeCount(count);
        }
        else
        {
            //插入礼包
            couponCode.setCouponDetailId(0);
            couponCode.setRemark("");
            couponCode.setChangeType(Byte.parseByte(CouponEnum.CouponCodeChangeType.LIBAO.getCode() + ""));
            couponCode.setChangeCount(0);
        }
        
        //插入优惠码
        int status = 0;
        if (type == CouponEnum.CouponCodeType.ONE_CODE_ONE_USE.getCode())
        {
            couponCode.setCode("");
            couponCode.setTotal(nums);
            status = couponCodeDao.insertCouponCode(couponCode);
            if (status == 1)
            {
                int couponCodeId = couponCode.getId();
                Map<String, Object> couponCodeDetail = new HashMap<String, Object>();
                for (int i = 0; i < nums; i++)
                {
                    couponCodeDetail.remove("couponCodeId");
                    couponCodeDetail.remove("code");
                    couponCodeDetail.remove("createTime");
                    String code = StringUtils.generateCouponCode();
                    couponCodeDetail.put("couponCodeId", couponCodeId);
                    couponCodeDetail.put("code", code);
                    couponCodeDetail.put("createTime", DateTimeUtil.now());
                    couponCodeDao.insertCouponCodeDetail(couponCodeDetail);
                }
            }
        }
        else if (type == CouponEnum.CouponCodeType.ONE_CODE_MANY_USE.getCode())
        {
            couponCode.setCode("");// 后面更新插入
            couponCode.setTotal(1);
            status = couponCodeDao.insertCouponCode(couponCode);
            if (status == 1)
            {
                // 插入优惠码
                status = updateCouponCode(couponCode.getId(), customCode);
                if (status != 1)
                {
                    ServiceException se = new ServiceException();
                    se.putMap("status", status);
                    se.putMap("msg", status == 2 ? "该自定义编码已存在 且可用状态" : "生成优惠码失败");
                    throw se;
                }
            }
        }
        
        //如果是礼包，插入对应优惠券列表
        if (couponDetailIdAndCountList.size() > 1 && (couponCode.getId() != null))
        {
            for (Map<String, Object> idAndCount : couponDetailIdAndCountList)
            {
                int couponDetailId = Integer.parseInt(idAndCount.get("couponDetailId") + "");
                int count = Integer.parseInt(idAndCount.get("count") + "");
                
                Map<String, Object> insertPara = new HashMap<String, Object>();
                insertPara.put("couponCodeId", couponCode.getId());
                insertPara.put("couponDetailId", couponDetailId);
                insertPara.put("count", count);
                int istatus = couponCodeDao.insertCouponCodeGiftBag(insertPara);
                if (istatus != 1)
                {
                    ServiceException se = new ServiceException();
                    se.putMap("status", status);
                    se.putMap("msg", "生成优惠码失败");
                    throw se;
                }
            }
        }
        
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", 1);
        if (status == 0)
        {
            result.put("status", 0);
            result.put("msg", "新增优惠码出错");
        }
        return result;
    }
    
    /**
     * 更新优惠码，
     * 
     * @return
     */
    private synchronized int updateCouponCode(int id, String customCode)
    {
        if (customCode == null || "".equals(customCode))
        {
            // 4位 -> 279841个 ; 5位 -> 6436343 ; 6位 -> 148035889 ; 7位 -> 3404825447
            int num = 4;
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("type", CouponEnum.CouponCodeType.ONE_CODE_MANY_USE.getCode());
            try
            {
                int count = couponCodeDao.countAllCouponCode(para);
                if (count > 10000000)
                {
                    num = 8;
                }
                else if (count > 1000000)
                {
                    num = 7;
                }
                else if (count > 100000)
                {
                    num = 6;
                }
                else if (count > 5000)
                {
                    num = 5;
                }
            }
            catch (Exception e1)
            {
                log.error(e1.getMessage(), e1);
                return 0;
            }
            
            Map<String, Object> upPara = new HashMap<String, Object>();
            int status = 0;
            while (true)
            {
                try
                {
                    String code = StringUtils.getGenerateLetterWithNum(num);
                    // 没有取出所有type=2的code，就单个判断
                    upPara.put("code", code);
                    List<CouponCodeEntity> reList = couponCodeDao.findAllCouponCode(upPara);
                    if (reList != null && reList.size() > 0)
                    {
                        log.info("已存在，跳过code");
                        continue;
                    }
                    upPara.remove("id");
                    upPara.remove("code");
                    upPara.put("id", id);
                    upPara.put("code", code);
                    status = couponCodeDao.updateCouponCode(upPara);
                    if (status == 1)
                    {
                        break;
                    }
                }
                catch (Exception e)
                {
                    log.error(e.getMessage(), e);
                }
            }
            return status;
        }
        else
        {
            int status = 0;
            try
            {
                Map<String, Object> upPara = new HashMap<String, Object>();
                upPara.put("code", customCode);
                List<CouponCodeEntity> reList = couponCodeDao.findAllCouponCode(upPara);
                if (reList != null && reList.size() > 0)
                {
                    boolean canAdd = true;
                    for(CouponCodeEntity entity : reList){
                        if(entity.getIsAvailable() == 1){
                            log.info("已存在该自定义优惠码");
                            status = 2;
                            canAdd = false;
                            break;
                        }
                    }
                    if(canAdd) {
                        upPara.put("id", id);
                        upPara.put("code", customCode);
                        status = couponCodeDao.updateCouponCode(upPara);
                    }
                }
                else
                {
                    upPara.put("id", id);
                    upPara.put("code", customCode);
                    status = couponCodeDao.updateCouponCode(upPara);
                }
            }
            catch (Exception e)
            {
                log.error(e.getMessage(), e);
            }
            return status;
        }
    }
    
    @Override
    public Map<String, Object> findCouponCode(Map<String, Object> para)
        throws Exception
    {
        List<CouponCodeEntity> couponCodes = couponCodeDao.findAllCouponCode(para);
        int total = 0;
        if (couponCodes.size() > 0)
        {
            Date dateTmp1 = new Date();
            //获取优惠码对应的优惠券类型和用户所见
            for (CouponCodeEntity it : couponCodes)
            {
                if (it.getCouponDetailId() > 0)
                {
                    it.setCouponDetailTypeStr(getCouponScopeStr(it.getScopeType(), it.getScopeId(), it.getCdType(), it.getThreshold(), it.getReduce(), it.getIsRandomReduce(), it.getLowestReduce(), it.getMaximalReduce()));
                    it.setCouponDetailDesc(it.getCdDesc());
                }
                else
                {
                    it.setCouponDetailTypeStr("礼包");
                }
            }
            // 统计优惠码兑换次数和和相应的优惠券使用次数
            List<Integer> one2oneIdList = new ArrayList<Integer>();
            List<Integer> one2manyIdList = new ArrayList<Integer>();
            List<Integer> allIdList = new ArrayList<Integer>();
            for (CouponCodeEntity it : couponCodes)
            {
                allIdList.add(it.getId());
                if (it.getType() == CouponEnum.CouponCodeType.ONE_CODE_ONE_USE.getCode())
                {
                    one2oneIdList.add(it.getId());
                }
                else if (it.getType() == CouponEnum.CouponCodeType.ONE_CODE_MANY_USE.getCode())
                {
                    one2manyIdList.add(it.getId());
                }
            }
            Map<String, Object> o2oMap = null;
            Map<String, Object> o2mMap = null;
            Map<String, Object> allMap = null;
            if (one2oneIdList.size() > 0)
            {
                o2oMap = list2map(couponCodeDao.findConvertNumsByCouponCodeIdWithTypeOne2One(one2oneIdList));
            }
            if (one2manyIdList.size() > 0)
            {
                o2mMap = list2map(couponCodeDao.findConvertNumsByCouponCodeIdWithTypeOne2Many(one2manyIdList));
            }
            if (allIdList.size() > 0)
            {
                allMap = list2map(couponCodeDao.findUsedNumsByCouponCodeId(allIdList));
            }
            for (CouponCodeEntity it : couponCodes)
            {
                String id = it.getId() + "";
                if ((allMap != null) && (allMap.get(id) != null))
                {
                    //实际使用次数
                    it.setUsedNums(allMap.get(id) + "");
                }
                if (it.getType() == CouponEnum.CouponCodeType.ONE_CODE_ONE_USE.getCode())
                {
                    if ((o2oMap != null) && (o2oMap.get(id) != null))
                    {
                        //兑换次数
                        it.setConvertNums(o2oMap.get(id) + "");
                    }
                }
                else if (it.getType() == CouponEnum.CouponCodeType.ONE_CODE_MANY_USE.getCode())
                {
                    if ((o2mMap != null) && (o2mMap.get(id) != null))
                    {
                        //兑换次数
                        it.setConvertNums(o2mMap.get(id) + "");
                    }
                }
            }
            total = couponCodeDao.countAllCouponCode(para);
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", couponCodes);
        result.put("total", total);
        return result;
    }
    
    private Map<String, Object> list2map(List<Map<String, Object>> list)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        for (Map<String, Object> it : list)
        {
            String couponCodeId = it.get("couponCodeId") + "";
            String total = it.get("total") + "";
            result.put(couponCodeId, total);
        }
        return result;
    }
    
    @Override
    public Map<String, Object> updateCouponAvailable(int id, byte isAvailable)
        throws Exception
    {
        try
        {
            Map<String, Object> updatePara = new HashMap<String, Object>();
            updatePara.put("id", id);
            if (isAvailable == 0)
            {
                CouponCodeEntity couponCodeEntity = couponCodeDao.findCouponCodeById(id);
                Map<String, Object> upPara = new HashMap<>();
                upPara.put("code", couponCodeEntity.getCode());
                List<CouponCodeEntity> reList = couponCodeDao.findAllCouponCode(upPara);
                if(CollectionUtils.isNotEmpty(reList)){
                    for(CouponCodeEntity entity : reList){
                        if(entity.getIsAvailable() == 1){
                            throw new Exception("相同优惠码 已经有可用状态存在");
                        }
                    }
                }
                updatePara.put("isAvailable", 1);
                updatePara.put("oldIsAvailable", isAvailable);
            }
            else
            {
                updatePara.put("isAvailable", 0);
                updatePara.put("oldIsAvailable", isAvailable);
            }
            int status = couponCodeDao.updateCouponCode(updatePara);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", status);
            if (status != 1)
            {
                result.put("msg", "修改失败，请刷新页面重试");
            }
            return result;
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", e.getMessage());
            return result;
        }
    }
    
    @Override
    public Map<String, Object> findCouponCodeLiBaoDetail(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        int couponCodeId = Integer.valueOf(para.get("couponCodeId") + "");
        CouponCodeEntity couponCode = couponCodeDao.findCouponCodeById(couponCodeId);
        List<CouponDetailEntity> cdes = couponCodeDao.findCouponDetailByCouponCodeId(couponCodeId);
        Map<String, String> giftBagMap = new HashMap<String, String>();
        List<Map<String, Object>> giftBag = couponCodeDao.findCouponCodeGiftBag(couponCodeId);
        for (Map<String, Object> it : giftBag)
        {
            String couponDetailId = it.get("couponDetailId") + "";
            String count = it.get("changeCount") + "";
            giftBagMap.put(couponDetailId, count);
        }
        StringBuffer sb = new StringBuffer();
        int index = 1;
        for (CouponDetailEntity couponDetail : cdes)
        {
            String count = giftBagMap.get(couponDetail.getId() + "");
            sb.append("第" + (index++) + "张优惠券" + "(" + count + "张)" + "：");
            sb.append(getCouponScopeStr(couponDetail.getScopeType(), couponDetail.getScopeId(), couponDetail.getType(), couponDetail.getThreshold(), couponDetail.getReduce(), couponDetail.getIsRandomReduce(), couponDetail.getLowestReduce(), couponDetail.getMaximalReduce()));
            sb.append("。");
        }
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        int total = 0;
        if (couponCode != null)
        {
            if (couponCode.getType() == CouponEnum.CouponCodeType.ONE_CODE_ONE_USE.getCode())
            {
                // 1 to 1
                List<Map<String, Object>> o2m = couponCodeDao.findCouponCodeLiBaoDetailByCouponCodeId(para);
                if (o2m.size() > 0)
                {
                    for (Map<String, Object> it : o2m)
                    {
                        it.put("code", it.get("code") + "");
                        if (it.get("accountType") != null)
                        {
                            it.put("accountTypeStr", AccountEnum.ACCOUNT_TYPE.getDescByCode(Integer.valueOf(it.get("accountType") + "")));
                        }
                        else
                        {
                            it.put("accountTypeStr", "");
                        }
                        it.put("couponTypeStr", sb.toString());
                        it.put("limitPeople", CouponEnum.CouponCodeType.ONE_CODE_ONE_USE.getShortDesc());
                        int convert = Integer.valueOf(it.get("couponCodeIsUsed") + "");
                        it.put("convert", convert == 1 ? "已兑换" : "未兑换");
                        it.put("type", couponCode.getType());
                        rows.add(it);
                    }
                    total = couponCodeDao.countCouponCodeDetailByCouponCodeId(para);
                }
            }
            else if (couponCode.getType() == CouponEnum.CouponCodeType.ONE_CODE_MANY_USE.getCode())
            {
                // 1 to many
                List<Map<String, Object>> o2m = couponCodeDao.findCouponCodeLiBaoCommonByCouponCodeId(para);
                if (o2m.size() > 0)
                {
                    for (Map<String, Object> it : o2m)
                    {
                        it.put("code", couponCode.getCode());
                        it.put("accountTypeStr", AccountEnum.ACCOUNT_TYPE.getDescByCode(Integer.valueOf(it.get("accountType") + "")));
                        it.put("couponTypeStr", sb.toString());
                        it.put("limitPeople", CouponEnum.CouponCodeType.ONE_CODE_MANY_USE.getShortDesc());
                        it.put("convert", "已兑换");
                        it.put("type", couponCode.getType());
                        rows.add(it);
                    }
                    total = couponCodeDao.countCouponCodeCommonByCouponCodeId(para);
                }
            }
        }
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }
    
    @Override
    public Map<String, Object> findCouponCodeDetail(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        int couponCodeId = Integer.valueOf(para.get("couponCodeId") + "");
        CouponCodeEntity couponCode = couponCodeDao.findCouponCodeById(couponCodeId);
        // 查询couponDetail
        CouponDetailEntity couponDetail = couponCodeDao.findCouponDetailById(couponCode.getCouponDetailId());
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        int total = 0;
        if (couponCode != null)
        {
            if (couponCode.getType() == CouponEnum.CouponCodeType.ONE_CODE_ONE_USE.getCode())
            {
                // 1 to 1
                List<Map<String, Object>> o2m = couponCodeDao.findCouponCodeDetailByCouponCodeId(para);
                if (o2m.size() > 0)
                {
                    for (Map<String, Object> it : o2m)
                    {
                        //计算reducePrice
                        if (couponDetail.getIsRandomReduce() == 0)
                        {
                            it.put("reducePrice", couponDetail.getReduce());
                        }
                        else
                        {
                            it.put("reducePrice", it.get("reducePrice"));
                        }
                        
                        it.put("code", it.get("code") + "");
                        if (it.get("accountType") != null)
                        {
                            it.put("accountTypeStr", AccountEnum.ACCOUNT_TYPE.getDescByCode(Integer.valueOf(it.get("accountType") + "")));
                        }
                        else
                        {
                            it.put("accountTypeStr", "");
                        }
                        String couponTypeStr =
                            getCouponScopeStr(couponDetail.getScopeType(), couponDetail.getScopeId(), couponDetail.getType(), couponDetail.getThreshold(), couponDetail.getReduce(), couponDetail.getIsRandomReduce(), couponDetail.getLowestReduce(), couponDetail.getMaximalReduce());
                        it.put("couponTypeStr", couponTypeStr);
                        it.put("limitPeople", CouponEnum.CouponCodeType.ONE_CODE_ONE_USE.getShortDesc());
                        int convert = Integer.valueOf(it.get("couponCodeIsUsed") + "");
                        it.put("convert", convert == 1 ? "已兑换" : "未兑换");
                        if (it.get("accountIsUsed") != null)
                        {
                            Integer accountIsUsed = Integer.valueOf(it.get("accountIsUsed") + "");
                            if (accountIsUsed == 0)
                            {
                                it.put("used", "未使用");
                                it.put("usedCode", "0");
                            }
                            else
                            {
                                it.put("used", "已使用");
                                it.put("usedCode", "1");
                            }
                        }
                        else
                        {
                            it.put("used", "");
                        }
                        it.put("type", couponCode.getType());
                        rows.add(it);
                    }
                    total = couponCodeDao.countCouponCodeDetailByCouponCodeId(para);
                }
            }
            else if (couponCode.getType() == CouponEnum.CouponCodeType.ONE_CODE_MANY_USE.getCode())
            {
                // 1 to many
                List<Map<String, Object>> o2m = couponCodeDao.findCouponCodeCommonByCouponCodeId(para);
                if (o2m.size() > 0)
                {
                    for (Map<String, Object> it : o2m)
                    {
                        //计算reducePrice
                        if (couponDetail.getIsRandomReduce() == 0)
                        {
                            it.put("reducePrice", couponDetail.getReduce());
                        }
                        else
                        {
                            it.put("reducePrice", it.get("reducePrice"));
                        }
                        
                        it.put("code", couponCode.getCode());
                        it.put("accountTypeStr", AccountEnum.ACCOUNT_TYPE.getDescByCode(Integer.valueOf(it.get("accountType") + "")));
                        String couponTypeStr =
                            getCouponScopeStr(couponDetail.getScopeType(), couponDetail.getScopeId(), couponDetail.getType(), couponDetail.getThreshold(), couponDetail.getReduce(), couponDetail.getIsRandomReduce(), couponDetail.getLowestReduce(), couponDetail.getMaximalReduce());
                        it.put("couponTypeStr", couponTypeStr);
                        it.put("limitPeople", CouponEnum.CouponCodeType.ONE_CODE_MANY_USE.getShortDesc());
                        it.put("convert", "已兑换");
                        Integer accountIsUsed = Integer.valueOf(it.get("accountIsUsed") + "");
                        if (accountIsUsed == 0)
                        {
                            it.put("used", "未使用");
                            it.put("usedCode", "0");
                        }
                        else
                        {
                            it.put("used", "已使用");
                            it.put("usedCode", "1");
                        }
                        it.put("type", couponCode.getType());
                        rows.add(it);
                    }
                    total = couponCodeDao.countCouponCodeCommonByCouponCodeId(para);
                }
            }
        }
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }
    
    private String getCouponScopeStr(int scopeType, int scopeId, int couponType, int threshold, int reduce, int isRandomReduce, int lowestReduce, int maximalReduce)
    {
        StringBuilder sb = new StringBuilder();
        if (isRandomReduce == 1)
        {
            sb.append("随机--" + lowestReduce + "到" + maximalReduce + "元--");
        }
        if (couponType == 1)
        {
            sb.append("满").append(threshold).append("减").append((reduce == 0 ? "X" : reduce)).append("优惠券");
        }
        else if (couponType == 2)
        {
            sb.append((reduce == 0 ? "X" : reduce)).append("元现金券");
        }
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
    
    @Override
    public Map<String, Object> queryCouponAccount(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        int type = Integer.valueOf(para.get("type") + "");
        resultMap.put("useTimes", type == 1 ? "单次" : "多次");
        List<Map<String, Object>> result = type == 1 ? couponCodeDao.queryCouponAccountWithTypeOne2One(para) : couponCodeDao.queryCouponAccountWithTypeOne2Many(para);
        for (Map<String, Object> map : result)
        {
            resultMap.put("couponDetailId", map.get("couponDetailId"));
            resultMap.put("desc", map.get("desc"));
            resultMap.put("startTime", map.get("startTime"));
            resultMap.put("endTime", map.get("endTime"));
            int couponType = Integer.valueOf(map.get("couponType") + "");
            int scopeId = Integer.valueOf(map.get("scopeId") + "");
            int scopeType = Integer.valueOf(map.get("scopeType") + "");
            int threshold = Integer.valueOf(map.get("threshold") + "");
            int reduce = Integer.valueOf(map.get("reduce") + "");
            int isRandomReduce = Integer.valueOf(map.get("isRandomReduce") + "");
            int lowestReduce = Integer.valueOf(map.get("lowestReduce") + "");
            int maximalReduce = Integer.valueOf(map.get("maximalReduce") + "");
            String sb = getCouponScopeStr(scopeType, scopeId, couponType, threshold, reduce, isRandomReduce, lowestReduce, maximalReduce);
            resultMap.put("couponScope", sb);
            resultMap.put("couponCode", map.get("couponCode"));
            resultMap.put("accountId", map.get("accountId"));
            resultMap.put("accountName", map.get("accountName"));
            resultMap.put("accountType", AccountEnum.ACCOUNT_TYPE.getDescByCode(Integer.valueOf(map.get("accountType") + "")));
            
        }
        return resultMap;
    }
    
    @Override
    public Map<String, Object> findCouponCodeLiBaoInfo(int couponCodeId)
        throws Exception
    {
        List<Map<String, Object>> couponDetailIdList = new ArrayList<Map<String, Object>>();
        List<CouponDetailEntity> cdes = couponCodeDao.findCouponDetailByCouponCodeId(couponCodeId);
        for (CouponDetailEntity it : cdes)
        {
            String text =
                getCouponScopeStr(it.getScopeType(), it.getScopeId(), it.getType(), it.getThreshold(), it.getReduce(), it.getIsRandomReduce(), it.getLowestReduce(), it.getMaximalReduce());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("text", text);
            map.put("value", it.getId());
            couponDetailIdList.add(map);
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("couponDetailIdList", couponDetailIdList);
        return result;
    }
    
    @Override
    public String findCouponCodeTotoalMoney(String id)
        throws Exception
    {
        return couponCodeDao.findCouponCodeTotoalMoney(id);
    }
    
}
