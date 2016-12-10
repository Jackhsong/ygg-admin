package com.ygg.admin.service.impl;

import com.ygg.admin.code.CouponEnum;
import com.ygg.admin.code.SellerEnum;
import com.ygg.admin.dao.*;
import com.ygg.admin.entity.*;
import com.ygg.admin.service.CommonService;
import com.ygg.admin.service.SystemLogService;
import com.ygg.admin.util.CommonEnum;
import com.ygg.admin.util.CommonUtil;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.elasticsearch.common.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 记录日志的方法待改进，当前方法在每段业务代码后面手动增加代码
 * 代码看起来比正常的业务逻辑还多
 * @author Administrator
 *
 */
@Service("logService")
public class SystemLogServiceImpl implements SystemLogService
{
    private static Logger logger = Logger.getLogger(SystemLogServiceImpl.class);
    
    @Resource
    private SystemLogDao logDao;
    
    @Resource(name = "orderDao")
    private OrderDao orderDao;
    
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
    private CommonService commonService;
    
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    
    @Override
    public Map<String, Object> jsonSystemLogInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> logInfoList = logDao.findAllSystemLogList(para);
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        int total = 0;
        if (logInfoList.size() > 0)
        {
            for (Map<String, Object> it : logInfoList)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", it.get("id"));
                map.put("username", it.get("username"));
                map.put("businessType", CommonEnum.BusinessTypeEnum.getDescriptionByOrdinal(Integer.valueOf(it.get("businessType") + "")));
                map.put("operationType", CommonEnum.OperationTypeEnum.getDescriptionByOrdinal(Integer.valueOf(it.get("operationType") + "")));
                map.put("content", it.get("content"));
                map.put("level", CommonEnum.LogLevelEnum.getDescriptionByOrdinal(Integer.valueOf(it.get("level") + "")));
                map.put("createTime", sdf.format((Timestamp)it.get("createTime")));
                resultList.add(map);
            }
            total = logDao.countSystemLog(para);
        }
        resultMap.put("rows", resultList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public int logger(Map<String, Object> para)
        throws Exception
    {
        String username = SecurityUtils.getSubject().getPrincipal() + "";
        para.put("username", username);
        int operationType = Integer.valueOf(para.get("operationType") + "");
        StringBuffer sb = new StringBuffer("用户【" + username + "】");
        
        if (operationType == CommonEnum.OperationTypeEnum.MODIFY_ORDER_PRICE.ordinal()) // 订单改价
        {
            OrderEntity order = (OrderEntity)para.get("object");
            if (order != null)
            {
                sb.append("将id=" + order.getId() + " 的订单的价格从 " + String.format("%.2f", order.getTotalPrice()) + " 改为 " + para.get("newPrice"));
            }
            else
            {
                sb.append("将订单的价格改为" + para.get("newPrice"));
            }
        }
        else if (operationType == CommonEnum.OperationTypeEnum.MODIFY_ORDER_STATUS.ordinal())// 订单状态修改
        {
            sb.append("将id=" + para.get("objectId") + " 的订单的状态从 " + para.get("oldStatus") + " 改为 " + para.get("newStatus"));
        }
        else if (operationType == CommonEnum.OperationTypeEnum.MODIFY_ORDER_REMARK.ordinal())// 商家备注修改
        {
            sb.append("将id=" + para.get("objectId") + " 的订单的商家备注修改为： " + para.get("content"));
        }
        else if (operationType == CommonEnum.OperationTypeEnum.MODIFY_ORDER_REMARK2.ordinal())// 客服备注修改
        {
            sb.append("将id=" + para.get("objectId") + " 的订单的客服备注，增加内容为： " + para.get("content"));
        }
        else if (operationType == CommonEnum.OperationTypeEnum.MODIFY_PRODUCT_STOCK.ordinal())// 调整库存
        {
            Integer stock = Integer.valueOf(para.get("addStockNum") + "");
            if (stock > 0)
            {
                sb.append("将id=" + para.get("objectId") + " 的商品库存增加了 " + stock);
            }
            else
            {
                sb.append("将id=" + para.get("objectId") + " 的商品库存减少了 " + (-stock));
            }
        }
        else if (operationType == CommonEnum.OperationTypeEnum.MODIFY_SELL_COUNT.ordinal())// 调整销量
        {
            Integer num = Integer.valueOf(para.get("sellNum") + "");
            if (num > 0)
            {
                sb.append("将id=" + para.get("objectId") + " 的商品销量增加了 " + num);
            }
            else
            {
                sb.append("将id=" + para.get("objectId") + " 的商品销量减少了 " + (-num));
            }
        }
        else if (operationType == CommonEnum.OperationTypeEnum.MODIFY_PRODUCT_PRICE.ordinal())// 商品改价
        {
            if( para.get("oldSalesPrice")!=null){
                sb.append("将id=" + para.get("objectId") + " 的商品价格从 " + para.get("oldSalesPrice") + " 改为 " + para.get("newSalesPrice"));
            }
            if(para.get("oldPartnerDistributionPrice")!=null){
                sb.append("将id=" + para.get("objectId") + " 的合伙人分销价从 " + para.get("oldPartnerDistributionPrice") + " 改为 " + para.get("newPartnerDistributionPrice"));
            }
            if(para.get("oldBsCommision")!=null){
                sb.append("将id=" + para.get("objectId") + " 的行动派分销佣金从 " + para.get("oldBsCommision") + " 改为 " + para.get("newBsCommision"));
            }
        }
        else if (operationType == CommonEnum.OperationTypeEnum.MODIFY_BASE_PRODUCT_PRICE.ordinal()) {
            if(para.get("oldProposalMarketPrice") != null){
                sb.append("将id=" + para.get("objectId") + " 的基本商品建议市场价格从 " + para.get("oldProposalMarketPrice") + " 改为 " + para.get("newProposalMarketPrice"));
            }
            if(para.get("oldProposalSalesPrice") != null){
                sb.append(" 将id=" + para.get("objectId") + " 的基本商品建议销售价格从 " + para.get("oldProposalSalesPrice") + " 改为 " + para.get("newProposalSalesPrice"));
            }
        }
        else if (operationType == CommonEnum.OperationTypeEnum.MODIFY_SELLER_NAME.ordinal())// 商品商家名称修改
        {
            sb.append("将id=" + para.get("objectId") + " 的商品的商家名称从 " + para.get("oldSeller") + " 改为 " + para.get("newSeller"));
        }
        else if (operationType == CommonEnum.OperationTypeEnum.MODIFY_PRODUCT_STATUS.ordinal())// 修改商品状态
        {
            sb.append("将id=" + para.get("objectId") + " 的商品的状态改为 " + para.get("newStatus"));
        }
        else if (operationType == CommonEnum.OperationTypeEnum.MODIFY_PRODUCT_SALE_STATUS.ordinal())// 商品上下架
        {
            sb.append("将id=" + para.get("objectId") + " 的商品改为 " + para.get("newStatus"));
        }
        else if (operationType == CommonEnum.OperationTypeEnum.MODIFY_SELL_TIME.ordinal())// 修改特卖时间
        {
            sb.append("将id=" + para.get("objectId") + " 的特卖时间从 " + para.get("oldTime") + " 改为 " + para.get("newTime"));
        }
        else if (operationType == CommonEnum.OperationTypeEnum.MODIFY_PRODUCT_TIME.ordinal())// 修改商品销售是时间
        {
            sb.append("将id=" + para.get("objectId") + " 的商品的销售时间从 " + para.get("oldTime") + " 改为 " + para.get("newTime"));
        }
        else if (operationType == CommonEnum.OperationTypeEnum.MODIFY_SELL_PRODUCT_TIME.ordinal())// 修改特卖商品销售时间
        {
            sb.append("将id=" + para.get("saleId") + " 的特卖中商品id=" + para.get("objectId") + " 的商品销售时间从 " + para.get("oldTime") + " 改为 " + para.get("newTime"));
        }
        else if (operationType == CommonEnum.OperationTypeEnum.MODIFY_SELL_PRODUCT.ordinal())// 修改特卖关联商品
        {
            SaleWindowEntity oldSwe = (SaleWindowEntity)para.get("old");
            SaleWindowEntity newSwe = (SaleWindowEntity)para.get("new");
            sb.append("将id=" + para.get("objectId") + " 的特卖");
            if (oldSwe.getType() == newSwe.getType())
            {
                sb.append("关联的")
                    .append(oldSwe.getType() == (byte)1 ? "商品" : oldSwe.getType() == (byte)2 ? "组合特卖" : "自定义特卖")
                    .append("Id从 ")
                    .append(oldSwe.getDisplayId())
                    .append(" 改为 ")
                    .append(newSwe.getDisplayId());
            }
            else
            {
                sb.append("从 ")
                    .append(oldSwe.getType() == (byte)1 ? "单品" : oldSwe.getType() == (byte)2 ? "组合" : "自定义特卖")
                    .append(" 改为 ")
                    .append(newSwe.getType() == (byte)1 ? "单品" : newSwe.getType() == (byte)2 ? "组合" : "自定义特卖")
                    .append(",关联的")
                    .append(oldSwe.getType() == (byte)1 ? "商品" : oldSwe.getType() == (byte)2 ? "组合特卖" : "自定义特卖")
                    .append("Id=")
                    .append(oldSwe.getDisplayId())
                    .append(" 改为 关联的")
                    .append(newSwe.getType() == (byte)1 ? "商品" : newSwe.getType() == (byte)2 ? "组合特卖" : "自定义特卖")
                    .append("Id=")
                    .append(newSwe.getDisplayId());
            }
        }
        else if (operationType == CommonEnum.OperationTypeEnum.MODIFY_BANNER_PRODUCT.ordinal())// 修改banner关联商品
        {
            sb.append("将id=" + para.get("objectId") + " 的banner");
            BannerWindowEntity oldSwe = (BannerWindowEntity)para.get("old");
            BannerWindowEntity newSwe = (BannerWindowEntity)para.get("new");
            if (oldSwe.getType() == newSwe.getType())
            {
                sb.append("关联的")
                    .append(oldSwe.getType() == (byte)1 ? "商品" : oldSwe.getType() == (byte)2 ? "组合特卖" : oldSwe.getType() == (byte)3 ? "自定义特卖" : "自定义页面")
                    .append("Id从 ")
                    .append(oldSwe.getDisplayId())
                    .append(" 改为 ")
                    .append(newSwe.getDisplayId());
            }
            else
            {
                sb.append("从 ")
                    .append(oldSwe.getType() == (byte)1 ? "单品" : oldSwe.getType() == (byte)2 ? "组合" : oldSwe.getType() == (byte)3 ? "自定义特卖" : "自定义页面")
                    .append(" 改为 ")
                    .append(newSwe.getType() == (byte)1 ? "单品" : newSwe.getType() == (byte)2 ? "组合" : newSwe.getType() == (byte)3 ? "自定义特卖" : "自定义页面")
                    .append(",关联的")
                    .append(oldSwe.getType() == (byte)1 ? "商品" : oldSwe.getType() == (byte)2 ? "组合特卖" : oldSwe.getType() == (byte)3 ? "自定义特卖" : "自定义页面")
                    .append("Id=")
                    .append(oldSwe.getDisplayId())
                    .append(" 改为 关联的")
                    .append(newSwe.getType() == (byte)1 ? "商品" : newSwe.getType() == (byte)2 ? "组合特卖" : newSwe.getType() == (byte)3 ? "自定义特卖" : "自定义页面")
                    .append("Id=")
                    .append(newSwe.getDisplayId());
            }
        }
        else if (operationType == CommonEnum.OperationTypeEnum.SEND_COUPON.ordinal())//发放优惠券
        {
            CouponDetailEntity couponDetail = couponDao.findCouponDetailById((int)(para.get("objectId")));
            sb.append("发放了 ").append(para.get("total")).append(" 张").append(getCouponDetailDesc(couponDetail));
            if (couponDetail.getType() == 1)
            {
                sb.append("的优惠券");
            }
            else if (couponDetail.getType() == 2)
            {
                sb.append("的现金券");
            }
        }
        else if (operationType == CommonEnum.OperationTypeEnum.SEND_COUPON_CODE.ordinal())//发放优惠码
        {
            CouponCodeEntity couponCode = (CouponCodeEntity)para.get("object");
            sb.append("发放了 ").append(getCouponCodeDesc(couponCode));
        }
        else if (operationType == CommonEnum.OperationTypeEnum.CREATE_COUPON_DETAIL.ordinal())//新增优惠券类型
        {
            Map<String, Object> map = (Map<String, Object>)para.get("object");
            CouponDetailEntity couponDetail = new CouponDetailEntity();
            couponDetail.setType((int)(map.get("type")));
            couponDetail.setScopeType((int)(map.get("scopeType")));
            couponDetail.setScopeId((int)(map.get("scopeId")));
            couponDetail.setThreshold((int)(map.get("threshold")));
            couponDetail.setReduce((int)(map.get("reduce")));
            couponDetail.setIsRandomReduce((int)(map.get("isRandomReduce")));
            couponDetail.setLowestReduce((int)(map.get("lowestReduce")));
            couponDetail.setMaximalReduce((int)(map.get("maximalReduce")));
            sb.append("新增了 ").append(getCouponDetailDesc(couponDetail));
            if (couponDetail.getType() == 1)
            {
                sb.append("类型的优惠券");
            }
            else if (couponDetail.getType() == 2)
            {
                sb.append("类型的现金券");
            }
        }
        else if (operationType == CommonEnum.OperationTypeEnum.CHANGE_COUPON_DETAIL_STATUS.ordinal())//更改优惠券状态
        {
            sb.append("将 id=").append(para.get("objectId")).append(" 的优惠券类型设为 ").append(para.get("status"));
        }
        else if (operationType == CommonEnum.OperationTypeEnum.CANCEL_PARTNER_STATUS.ordinal())//取消合伙人资格
        {
            sb.append("将 id=").append(para.get("objectId")).append(" 的用户合伙人资格取消 ");
        }
        else if (operationType == CommonEnum.OperationTypeEnum.RESET_PARTNER_STATUS.ordinal())//恢复合伙人资格
        {
            sb.append("将 id=").append(para.get("objectId")).append(" 的用户合伙人资格恢复 ");
        }
        else if (operationType == CommonEnum.OperationTypeEnum.PARTNER_AUDIT_STATUS.ordinal())//合伙人申请审核
        {
            if ("3".equals(para.get("status") + ""))//审核不通过
            {
                sb.append("拒绝了 id=").append(para.get("objectId")).append(" 的用户的合伙人申请 ");
            }
            else if ("4".equals(para.get("status") + ""))//审核通过
            {
                sb.append("通过了 id=").append(para.get("objectId")).append(" 的用户的合伙人申请 ");
            }
        }
        else if (operationType == CommonEnum.OperationTypeEnum.CREATE_PARTNER.ordinal())//手动创建合伙人
        {
            sb.append("将 id=").append(para.get("objectId")).append(" 的用户手动设为合伙人 ");
        }
        else if (operationType == CommonEnum.OperationTypeEnum.MODIFY_PRODUCT_CODE.ordinal())// 修改商品编码
        {
            sb.append("将 id=").append(para.get("objectId")).append(" 的商品编码从 ").append(para.get("oldCode")).append(" 改为").append(para.get("newCode"));
        }
        else if (operationType == CommonEnum.OperationTypeEnum.CREATE_INVITED_RELATION.ordinal())
        {
            sb.append("手动创建了邀请关系，邀请人Id=").append(para.get("faterAccountId")).append("，被邀请人Id=").append(para.get("currentAccountId"));
        }
        else if (operationType == CommonEnum.OperationTypeEnum.EXPORT_ORDER.ordinal())
        {
            sb.append(DateTime.now().toString("yyyy-MM-dd HH:mm:ss")).append("导出了订单,筛选条件为:").append(para.get("filter"));
        }else if(operationType == CommonEnum.OperationTypeEnum.MODIFY_BASE_PRODUCT_SUbMIT.ordinal()) {
            if(para.get("newType") != null) {
                sb.append("将id=" + para.get("objectId") + " 的结算类型从 " + para.get("oldType") + " 改为 " + para.get("newType"));
            }
            if(para.get("newWholesalePrice") != null) {
                sb.append("将id=" + para.get("objectId") + " 的供货价从 " + para.get("oldWholesalePrice") + " 改为 " + para.get("newWholesalePrice"));
            }
            if(para.get("newDeduction") != null) {
                sb.append("将id=" + para.get("objectId") + " 的折扣点从 " + para.get("oldDeduction") + " 改为 " + para.get("newDeduction"));
            }
            if(para.get("newProposalPrice") != null) {
                sb.append("将id=" + para.get("objectId") + " 的建议价从 " + para.get("oldProposalPrice") + " 改为 " + para.get("newProposalPrice"));
            }
            if(para.get("newSelfPurchasePrice") != null) {
                sb.append("将id=" + para.get("objectId") + " 的自营采购价从 " + para.get("oldSelfPurchasePrice") + " 改为 " + para.get("newSelfPurchasePrice"));
            }
        }
        para.put("content", sb.toString());
        return logDao.logger(para);
    }
    
    private String getCouponDetailDesc(CouponDetailEntity couponDetail)
    {
        StringBuilder sb = new StringBuilder();
        if (couponDetail.getIsRandomReduce() == 1)
        {
            sb.append("随机--" + couponDetail.getLowestReduce() + "到" + couponDetail.getMaximalReduce() + "元--");
        }
        if (couponDetail.getType() == 1)
        {
            sb.append("满").append(couponDetail.getThreshold()).append("减").append((couponDetail.getReduce() == 0 ? "X" : couponDetail.getReduce()));
        }
        else if (couponDetail.getType() == 2)
        {
            sb.append((couponDetail.getReduce() == 0 ? "X" : couponDetail.getReduce())).append("元");
        }
        try
        {
            switch (couponDetail.getScopeType())
            {
                case 1:
                    sb.append("全场通用");
                    break;
                case 2:
                    sb.append("仅限专场").append(activitiesCommonDao.findAcCommonById(couponDetail.getScopeId()).getName()).append("使用");
                    break;
                case 3:
                    sb.append("仅限商品").append(productDao.findProductByID(couponDetail.getScopeId(), null).getName()).append("使用");
                    break;
                case 4:
                    sb.append("仅限二级类目").append(categoryDao.findCategorySecondById(couponDetail.getScopeId()).getName()).append("使用");
                    break;
                case 5:
                    sb.append("仅限卖家").append(sellerDao.findSellerById(couponDetail.getScopeId()).getRealSellerName()).append("使用");
                    break;
                case 6:
                    SellerEnum.SellerTypeEnum sellerTypeEnum = SellerEnum.SellerTypeEnum.getSellerTypeEnumByCode(couponDetail.getScopeId());
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
            logger.error(e.getMessage(), e);
        }
        return sb.toString();
    }
    
    private String getCouponCodeDesc(CouponCodeEntity couponCode)
    {
        StringBuilder sb = new StringBuilder();
        try
        {
            //            CouponDetailEntity couponDetail = couponDao.findCouponDetailById(couponCode.getCouponDetailId());
            if (CouponEnum.CouponCodeType.ONE_CODE_MANY_USE.getCode() == couponCode.getType())
            {
                //                sb.append(" 1 个").append("使用范围为").append(getCouponDetailDesc(couponDetail)).append("的无限次优惠码");
                sb.append("1个无限次优惠码");
            }
            else if (CouponEnum.CouponCodeType.ONE_CODE_ONE_USE.getCode() == couponCode.getType())
            {
                sb.append(" ").append(couponCode.getTotal()).append(" 个的单次优惠码");
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        return sb.toString();
    }
    
    @Override
    public int log(Map<String, Object> para)
        throws Exception
    {
        return logDao.log(para);
    }
    
    @Override
    public Map<String, Object> jsonLogInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> logInfoList = logDao.findAllLogList(para);
        int total = 0;
        if (logInfoList.size() > 0)
        {
            for (Map<String, Object> it : logInfoList)
            {
                it.put("createTime", sdf.format((Timestamp)it.get("createTime")));
            }
            total = logDao.countLog(para);
        }
        resultMap.put("rows", logInfoList);
        resultMap.put("total", total);
        return resultMap;
    }

    @Override
    public void loggerInsert(String objName, int level, int businessType, int operationType, Object obj)
    {
        try
        {
            String username = commonService.getCurrentRealName();
            Map<String, Object> loggerMap = new HashMap<>();
            loggerMap.put("level", level);
            loggerMap.put("username", username);
            loggerMap.put("businessType", businessType);
            loggerMap.put("operationType", operationType);
            StringBuffer content = new StringBuffer();
            content.append(username).append(" 新增 ").append(objName.replaceAll("新增", "")).append("，").append("params=").append(CommonUtil.object2Map(obj).toString());
            loggerMap.put("content", content.toString());
            logger.info("新增对象记录日志:" + loggerMap.toString());
            logDao.logger(loggerMap);
        }
        catch (Exception e)
        {
            logger.error("新增对象记录日志出错", e);
        }
    }
    
    @Override
    public void loggerInsert(String objName, int level, int businessType, int operationType, Map<? extends Object, ? extends Object> params)
    {
        try
        {
            String username = commonService.getCurrentRealName();
            Map<String, Object> loggerMap = new HashMap<>();
            loggerMap.put("level", level);
            loggerMap.put("username", username);
            loggerMap.put("businessType", businessType);
            loggerMap.put("operationType", operationType);
            StringBuffer content = new StringBuffer();
            content.append(username).append(" 新增 ").append(objName.replaceAll("新增", "")).append("，").append("params=").append(params.toString());
            loggerMap.put("content", content.toString());
            logger.info("新增对象记录日志:" + loggerMap.toString());
            logDao.logger(loggerMap);
        }
        catch (Exception e)
        {
            logger.error("新增对象记录日志出错", e);
        }
    }
    
    @Override
    public void loggerUpdate(String objName, int level, int businessType, int operationType, Object oldObj, Object newObj)
    {
        try
        {
            String username = commonService.getCurrentRealName();
            Map<String, Object> loggerMap = new HashMap<>();
            loggerMap.put("level", level);
            loggerMap.put("username", username);
            loggerMap.put("businessType", businessType);
            loggerMap.put("operationType", operationType);
            
            StringBuffer content = new StringBuffer();
            content.append(username).append(" 修改 ").append(objName.replaceAll("修改", "")).append("，");
            Map<String, Object> oldMap = CommonUtil.object2Map(oldObj);
            oldMap.remove("createTime");
            oldMap.remove("updateTime");
            Map<String, Object> newMap = CommonUtil.object2Map(newObj);
            newMap.remove("createTime");
            newMap.remove("updateTime");

            StringBuffer sb = new StringBuffer();
            for (String key : oldMap.keySet())
            {
                if (!oldMap.get(key).equals(newMap.get(key)))
                {
                    sb.append("将").append(key).append("从").append(oldMap.get(key)).append("改为").append(newMap.get(key)).append("、");
                }
            }
            if (sb.length() == 0)
            {
                return;
            }
            loggerMap.put("content", content.append(sb.toString()).toString());
            logger.info("修改对象记录日志:" + loggerMap.toString());
            logDao.logger(loggerMap);
        }
        catch (Exception e)
        {
            logger.error("修改对象记录日志出错", e);
        }
    }
    
    @Override
    public void loggerUpdate(String objName, int level, int businessType, int operationType, Object oldObj, Map<String, ? extends Object> params)
    {
        try
        {
            String username = commonService.getCurrentRealName();
            Map<String, Object> loggerMap = new HashMap<>();
            loggerMap.put("level", level);
            loggerMap.put("username", username);
            loggerMap.put("businessType", businessType);
            loggerMap.put("operationType", operationType);
            
            StringBuffer content = new StringBuffer();
            content.append(username).append(" 修改 ").append(objName.replaceAll("修改", "")).append("，");
            Map<String, Object> oldMap = CommonUtil.object2Map(oldObj);
            oldMap.remove("createTime");
            oldMap.remove("updateTime");

            StringBuffer sb = new StringBuffer();
            for (String key : params.keySet())
            {
                if (oldMap.get(key) != null && !oldMap.get(key).equals(params.get(key)))
                {
                    sb.append("将").append(key).append("从").append(oldMap.get(key)).append("改为").append(params.get(key)).append("、");
                }
            }

            if (sb.length() == 0)
            {
                return;
            }
            loggerMap.put("content", content.append(sb.toString()).toString());
            logger.info("修改对象记录日志:" + loggerMap.toString());
            logDao.logger(loggerMap);
        }
        catch (Exception e)
        {
            logger.error("修改对象记录日志出错", e);
        }
    }
    
    @Override
    public void loggerDelete(String objName, int level, int businessType, int operationType, Object obj)
    {
        try
        {
            String username = commonService.getCurrentRealName();
            Map<String, Object> loggerMap = new HashMap<>();
            loggerMap.put("level", level);
            loggerMap.put("username", username);
            loggerMap.put("businessType", businessType);
            loggerMap.put("operationType", operationType);
            
            StringBuffer content = new StringBuffer();
            content.append(username).append(" 删除 ").append(objName.replaceAll("删除", "")).append("，").append("params=").append(CommonUtil.object2Map(obj).toString());
            loggerMap.put("content", content.toString());
            logger.info("删除对象记录日志:" + loggerMap.toString());
            logDao.logger(loggerMap);
        }
        catch (Exception e)
        {
            logger.error("删除对象记录日志出错", e);
        }
    }
    
    @Override
    public void loggerDelete(String objName, int level, int businessType, int operationType, int objId)
    {
        try
        {
            String username = commonService.getCurrentRealName();
            Map<String, Object> loggerMap = new HashMap<>();
            loggerMap.put("level", level);
            loggerMap.put("username", username);
            loggerMap.put("businessType", businessType);
            loggerMap.put("operationType", operationType);
            
            StringBuffer content = new StringBuffer();
            content.append(username).append(" 删除 ").append(objName.replaceAll("删除", "")).append("，").append("id=").append(objId);
            loggerMap.put("content", content.toString());
            logger.info("删除对象记录日志:" + loggerMap.toString());
            logDao.logger(loggerMap);
        }
        catch (Exception e)
        {
            logger.error("删除对象记录日志出错", e);
        }
    }
    
    @Override
    public void loggerDelete(String objName, int level, int businessType, int operationType, Map<? extends Object, ? extends Object> params)
    {
        try
        {
            String username = commonService.getCurrentRealName();
            Map<String, Object> loggerMap = new HashMap<>();
            loggerMap.put("level", level);
            loggerMap.put("username", username);
            loggerMap.put("businessType", businessType);
            loggerMap.put("operationType", operationType);
            
            StringBuffer content = new StringBuffer();
            content.append(username).append(" 删除 ").append(objName.replaceAll("删除", "")).append("，").append("params=").append(params.toString());
            loggerMap.put("content", content.toString());
            logger.info("删除对象记录日志:" + loggerMap.toString());
            logDao.logger(loggerMap);
        }
        catch (Exception e)
        {
            logger.error("删除对象记录日志出错", e);
        }
    }

    @Override
    public void loggerExport(String objName, int level, int businessType, int operationType, Map<? extends Object, ? extends Object> params) {
        try
        {
            String username = commonService.getCurrentRealName();
            Map<String, Object> loggerMap = new HashMap<>();
            loggerMap.put("level", level);
            loggerMap.put("username", username);
            loggerMap.put("businessType", businessType);
            loggerMap.put("operationType", operationType);

            StringBuffer content = new StringBuffer();
            content.append(username).append(" 导出 ").append(objName.replaceAll("导出", "")).append("，").append("筛选条件params=").append(params.toString());
            loggerMap.put("content", content.toString());
            logger.info("导出对象记录日志:" + loggerMap.toString());
            logDao.logger(loggerMap);
        }
        catch (Exception e)
        {
            logger.error("导出对象记录日志出错", e);
        }
    }
}
