package com.ygg.admin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ygg.admin.code.*;
import com.ygg.admin.code.OrderEnum.ORDER_STATUS;
import com.ygg.admin.config.AreaCache;
import com.ygg.admin.dao.*;
import com.ygg.admin.entity.*;
import com.ygg.admin.exception.ServiceException;
import com.ygg.admin.oauth.ApiCallService;
import com.ygg.admin.oauth.GlobalAlipayRefund;
import com.ygg.admin.oauth.WeiChatRefund;
import com.ygg.admin.sdk.alipay.config.AlipayConfig;
import com.ygg.admin.service.*;
import com.ygg.admin.servlet.Kd100Service;
import com.ygg.admin.util.*;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;

@Service("refundService")
public class RefundServiceImpl implements RefundService
{
    private Logger log = Logger.getLogger(RefundServiceImpl.class);

    DecimalFormat decimalFormat = new DecimalFormat("0.00");

    @Resource
    private AccountDao accountDao;

    @Resource
    private OrderDao orderDao;

    @Resource
    private RefundDao refundDao;

    @Resource
    private ReceiveAddressDao receiveAddressDao;

    @Resource
    private Kd100Service kd100Service;

    @Resource
    private OrderService orderService;

    @Resource
    private ProductDao productDao;

    @Resource
    private UserDao userDao;

    @Resource
    private SellerDao sellerDao;

    @Resource
    private RefundReasonService refundReasonService;

    @Resource
    private SmsService smsService;

    @Resource
    private CommonService commonService;

    @Resource
    private RefundReasonDao refundReasonDao;

    @Resource
    private SystemLogService logService;

    @Override
    public RefundEntity findRefundInfo(int refundId)
            throws Exception
    {
        return refundDao.findRefundById(refundId);
    }

    @Override
    public Map<String, Object> findAllRefundInfo(Map<String, Object> para)
            throws Exception
    {
        String number = para.get("number") != null ? para.get("number") + "" : null;
        Integer status = para.get("status") != null ? Integer.valueOf(para.get("status") + "") : null;
        Integer financialAffairsCardId = para.get("financialAffairsCardId") != null ? Integer.valueOf(para.get("financialAffairsCardId") + "") : null;
        Integer type = para.get("type") != null ? Integer.valueOf(para.get("type") + "") : null;
        String startTime = para.get("startTime") != null ? para.get("startTime") + "" : null;
        String endTime = para.get("endTime") != null ? para.get("endTime") + "" : null;
        String startCheckTime = para.get("startCheckTime") != null ? para.get("startCheckTime") + "" : null;
        String endCheckTime = para.get("endCheckTime") != null ? para.get("endCheckTime") + "" : null;
        Integer moneyStatus = para.get("moneyStatus") != null ? Integer.valueOf(para.get("moneyStatus") + "") : null;
        String receiveType = para.get("receiveType") != null ? para.get("receiveType") + "" : null;
        String name = para.get("name") != null ? para.get("name") + "" : null;
        String receiveName = para.get("receiveName") != null ? para.get("receiveName") + "" : null;
        String mobileNumber = para.get("mobileNumber") != null ? para.get("mobileNumber") + "" : null;
        Integer refundProportionStatus = para.get("refundProportionStatus") != null ? Integer.valueOf(para.get("refundProportionStatus") + "") : null;// 财务是否已分摊
        Integer sellerId = para.get("sellerId") != null ? Integer.valueOf(para.get("sellerId") + "") : null;
        Integer operationStatus = para.get("operationStatus") != null ? Integer.valueOf(para.get("operationStatus") + "") : null;
        Integer orderStatus = para.get("orderStatus") != null ? Integer.valueOf(para.get("orderStatus") + "") : null;
        Integer searchOrderChannel = para.get("searchOrderChannel") != null ? Integer.valueOf(para.get("searchOrderChannel") + "") : null;
        Integer logisticsStatus = para.get("logisticsStatus") != null ? Integer.valueOf(para.get("logisticsStatus").toString()) : -1;
        Map<String, Object> result = new HashMap<>();
        List<Integer> orderIds = null;
        Map<String, Object> daoPara = new HashMap<String, Object>();
        // 1 先根据 用户名 & 收货人姓名 & 收货手机 订单编号 查询订单Ids
        boolean findAllOrderIdsByUserInfo = false;
        if (name != null)
        {
            findAllOrderIdsByUserInfo = true;
            daoPara.put("name", name);
            // daoPara.put("name", "%"+name+"%");
        }
        if (receiveName != null)
        {
            findAllOrderIdsByUserInfo = true;
            daoPara.put("receiveName", receiveName);
            // daoPara.put("receiveName", "%"+receiveName+"%");
        }
        if (mobileNumber != null)
        {
            findAllOrderIdsByUserInfo = true;
            daoPara.put("mobileNumber", mobileNumber);
        }
        if (number != null)
        {
            findAllOrderIdsByUserInfo = true;
            daoPara.put("number", number);
        }
        if (findAllOrderIdsByUserInfo)
        {
            // 条件 查询出来的订单ID比较少。如果会多。就不要放在这里查了。
            orderIds = refundDao.findAllOrderIdsByUserInfo(daoPara);
            if (orderIds.size() < 1)
            {
                result.put("rows", new ArrayList());
                result.put("total", 0);
                return result;
            }
        }

        // 重新赋值
        daoPara = new HashMap<>();
        daoPara.put("idList", orderIds);
        if (sellerId != null)
        {
            daoPara.put("sellerId", sellerId);
        }
        if (operationStatus != null)
        {
            daoPara.put("operationStatus", operationStatus);
            if (operationStatus == 0)
            {
                List<Integer> sellerIdList = sellerDao.findSellerIdWhereIsOwnerEqualsOne();
                if (sellerIdList.size() > 0)
                {
                    daoPara.put("sellerIdList", sellerIdList);
                }
            }
        }
        if (orderStatus != null)
        {
            daoPara.put("orderStatus", orderStatus);
        }
        if (type != null)
        {
            daoPara.put("type", type);
        }
        if (type == null || type == 2)
        {// 退款并退货 才可选择是否收货
            if (receiveType != null)
            {
                daoPara.put("type", 2);
                daoPara.put("isReceive", receiveType);
            }
        }
        if (endTime != null)
        {
            daoPara.put("endTime", endTime);
        }
        if (startTime != null)
        {
            daoPara.put("startTime", startTime);
        }
        if (startCheckTime != null)
        {
            daoPara.put("startCheckTime", startCheckTime);
        }
        if (endCheckTime != null)
        {
            daoPara.put("endCheckTime", endCheckTime);
        }
        if (financialAffairsCardId != null)
        {
            daoPara.put("financialAffairsCardId", financialAffairsCardId);
        }
        List<Integer> statusList = new ArrayList<>();
        if (status != null)
        {
            statusList.add(Integer.valueOf(status));
        }
        if (statusList.size() == 0 && moneyStatus != null)
        {
            if (moneyStatus == 1)
            {// 已打款
                statusList.add(CommonEnum.RefundStatusEnum.SUCCESS.ordinal());
            }
            else if (moneyStatus == 2)
            {// 未打款
                statusList.add(CommonEnum.RefundStatusEnum.APPLY.ordinal());
                statusList.add(CommonEnum.RefundStatusEnum.WAIT_RETURN_OF_GOODS.ordinal());
                statusList.add(CommonEnum.RefundStatusEnum.WAIT_RETURN_OF_MONEY.ordinal());
                statusList.add(CommonEnum.RefundStatusEnum.CLOSE.ordinal());
                statusList.add(CommonEnum.RefundStatusEnum.CANCEL.ordinal());
            }
        }
        if (statusList.size() > 0)
        {
            daoPara.put("statusList", statusList);
        }
        daoPara.put("start", para.get("start"));
        daoPara.put("max", para.get("max"));
        if (refundProportionStatus != null)
        {
            daoPara.put("refundProportionStatus", refundProportionStatus);
        }
        if (searchOrderChannel != null)
        {
            if (searchOrderChannel == 1)
            {
                // 左岸城堡
                daoPara.put("orderType", 1);
            }
            else if (searchOrderChannel == 2)
            {
                // 左岸城堡
                daoPara.put("orderType", 2);
            }
            else if (searchOrderChannel == 3)
            {
                // 左岸城堡

                daoPara.put("orderType", 4);
            }
        }
        daoPara.put("logisticsStatus", logisticsStatus);
        List<RefundEntity> refundList = refundDao.findAllRefundByPara(daoPara);
        int total = 0;
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        if (refundList.size() > 0)
        {
            total = refundDao.countAllRefundByPara(daoPara);

            List<Integer> orderProductIdList = findCustomIdsByType(refundList, "orderProductId");
            List<Integer> orderIdList = findCustomIdsByType(refundList, "orderId");
            List<Integer> idList = findCustomIdsByType(refundList, "id");
            Map<String, Object> infoPara = new HashMap<String, Object>();

            infoPara.put("idList", orderProductIdList);
            // 根据 order_product_id 查询 商品名称、购买数量 , key 为 order_product_id
            Map<String, Map<String, Object>> orderProductInfoMap = refundDao.findAllOrderProductInfoByIds(infoPara);

            infoPara.put("idList", idList);
            // 根据 id 查询 收货状态 key 为 id
            Map<String, Map<String, Object>> receiveStatusInfoMap = refundDao.findAllRefundIsReceiveStatusByIds(infoPara);

            infoPara.put("idList", orderIdList);
            // 根据 order_id 查询 收货人、收货手机 、订单号、用户名 , key为orderId
            Map<String, Map<String, Object>> peopleInfoMap = refundDao.findAllOrderReceiveInfoByIds(infoPara);

            // 遍历复制
            String id = "";
            String orderProductId = "";
            String orderId = "";
            Map<String, Object> currOrderProductInfoMap = null;
            Map<String, Object> currReceiveStatusInfoMap = null;
            Map<String, Object> currPeopleInfoMap = null;
            List<Integer> refundIdList = new ArrayList<Integer>();
            for (RefundEntity refund : refundList)
            {
                refundIdList.add(refund.getId());
                Map<String, Object> row = new HashMap<String, Object>();
                // 订单号、用户名、退款发起时间、最新更新时间、退货需求、退款状态、打款状态、收货状态、退款金额、退款数量、退款商品ID、商品名称、购买数量、收货人、收货手机
                id = refund.getId() + "";
                currReceiveStatusInfoMap = receiveStatusInfoMap.get(id);
                orderProductId = refund.getOrderProductId() + "";
                currOrderProductInfoMap = orderProductInfoMap.get(orderProductId);
                orderId = refund.getOrderId() + "";
                currPeopleInfoMap = peopleInfoMap.get(orderId);
                if (currPeopleInfoMap == null)
                {
                    continue;
                }
//                System.out.println(orderId + ":" + currPeopleInfoMap);
                row.put("id", refund.getId());
                // row.put("accountCardId", refund.getAccountCardId());
                if (refund.getRefundPayType() == RefundEnum.REFUND_PAY_TYPE.CREATE_NEW_ACCOUNT_CARD.getCode())
                {
                    row.put("cardNumber", refund.getCardNumber());
                }
                else
                {
                    row.put("cardNumber", "原路返回");
                }
                int refundReasonId = refund.getRefundReasonId();
                Map<String, Object> var1 = new HashMap();
                var1.put("id", refundReasonId);
                Map<String, Object> ret = refundReasonDao.findRefundReasonById(var1);
                if(ret != null)
                    row.put("refundReason", ret.get("reason") == null ? "": ret.get("reason"));

                int orderType = Integer.parseInt(currPeopleInfoMap.get("orderType") == null ? "1" : currPeopleInfoMap.get("orderType").toString());
                /*String orderChannel = "左岸城堡";
                int appChannel = Integer.valueOf(currPeopleInfoMap.get("appChannel") == null ? "0" : currPeopleInfoMap.get("appChannel") + "");
                if (appChannel == CommonEnum.OrderAppChannelEnum.QUAN_QIU_BU_SHOU.ordinal())
                {
                    orderChannel = "左岸城堡
";
                }
                else if (appChannel == CommonEnum.OrderAppChannelEnum.GEGETUAN_APP.ordinal() || appChannel == CommonEnum.OrderAppChannelEnum.GEGETUAN_APP_ANDROID.ordinal()
                        || appChannel == CommonEnum.OrderAppChannelEnum.GEGETUAN_APP_IOS.ordinal() || appChannel == CommonEnum.OrderAppChannelEnum.GEGETUAN_WECHAT.ordinal())
                {
                    orderChannel = "左岸城堡";
                }*/
                row.put("orderChannel", OrderEnum.ORDER_TYPE.getDescByCode(orderType));
                row.put("orderNumber", currPeopleInfoMap.get("number"));
                row.put("orderStatus", OrderEnum.ORDER_STATUS.getDescByCode(Integer.valueOf(currPeopleInfoMap.get("status") + "")));
                row.put("payTime", DateTimeUtil.timestampObjectToWebString(currPeopleInfoMap.get("payTime")));
                row.put("username", currPeopleInfoMap.get("name"));
                row.put("sellerId", currPeopleInfoMap.get("sellerId"));
                row.put("createTime", DateTimeUtil.timestampStringToWebString(refund.getCreateTime()));
                row.put("updateTime", DateTimeUtil.timestampStringToWebString(refund.getCheckTime()));
                int canReceive = 0;
                if (refund.getType() == 2)
                {
                    row.put("type", "退款并退货");
                    // 收货状态
                    if (currReceiveStatusInfoMap == null)
                    {
                        row.put("receiveStatus", "未收货");
                        row.put("ologNumber", "");
                        row.put("ologChannel", "");
                    }
                    else
                    {
                        Byte receiveStatus = Byte.valueOf(currReceiveStatusInfoMap.get("isReceive") + "");
                        if (receiveStatus.intValue() == 1)
                        {
                            row.put("receiveStatus", "已收货");
                        }
                        else
                        {
                            row.put("receiveStatus", "未收货");
                            canReceive = 1;
                        }
                        row.put("ologNumber", currReceiveStatusInfoMap.get("ologNumber") + "");
                        row.put("ologChannel", currReceiveStatusInfoMap.get("ologChannel") + "");
                    }
                }
                else if (refund.getType() == 1)
                {
                    row.put("type", "仅退款");
                    row.put("receiveStatus", "");
                }
                else
                {
                    row.put("type", "");
                    row.put("receiveStatus", "");
                }
                row.put("typeCode", refund.getType());
                int st = refund.getStatus();
                // 若status == 3 && 无 【处理3】客服审核退货 则提示待客服审核 收货 ； 有则提示待财务打款
                String moneyTip = "待客服审核退货";
                if (st == 3 && refund.getType() == 2)
                {
                    Map<String, Object> teackPara = new HashMap<String, Object>();
                    teackPara.put("step", 3);
                    teackPara.put("refundId", refund.getId());
                    List<OrderProductRefundTeackEntity> reList = refundDao.findOrderProductRefundTeack(teackPara);
                    if (reList.size() > 0)
                    {
                        moneyTip = "打款";
                    }
                }
                row.put("moneyTip", moneyTip);
                row.put("canReceive", canReceive);
                row.put("statusStr", CommonEnum.RefundStatusEnum.getDescriptionByOrdinal(st));
                row.put("status", st);
                if (st == CommonEnum.RefundStatusEnum.SUCCESS.ordinal())
                {
                    row.put("moneyStatus", "已打款");
                }
                else
                {
                    row.put("moneyStatus", "未打款");
                }
                row.put("applyMoney", refund.getApplyMoney());
                row.put("realMoney", refund.getRealMoney());
                row.put("count", refund.getCount());
                row.put("productId", currOrderProductInfoMap.get("productId"));
                row.put("productCode", currOrderProductInfoMap.get("productCode"));
                row.put("productName", currOrderProductInfoMap.get("productName"));
                row.put("productCount", currOrderProductInfoMap.get("productCount"));
                row.put("fullName", currPeopleInfoMap.get("fullName"));
                row.put("mobileNumber", currPeopleInfoMap.get("mobileNumber"));
                row.put("sellerName", currOrderProductInfoMap.get("sellerName"));
                row.put("sendAddress", currOrderProductInfoMap.get("sendAddress"));
                row.put("explain", URLDecoder.decode(refund.getExplain(), "UTF-8"));
                rows.add(row);
            }
            if ("export".equals((para.get("from") + "")))
            {
                Map<String, Object> searchPara = new HashMap<String, Object>();
                searchPara.put("idList", refundIdList);
                List<RefundProportionEntity> rpes = refundDao.findAllRefundProportionByPara(searchPara);
                Map<String, RefundProportionEntity> rpesMap = new HashMap<String, RefundProportionEntity>();
                for (RefundProportionEntity it : rpes)
                {
                    rpesMap.put(it.getRefundId() + "", it);
                }
                result.put("rpesMap", rpesMap);
            }
        }
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }

    /** 映射获取list */
    private List<Integer> findCustomIdsByType(List<RefundEntity> infoList, String column)
    {
        List<Integer> ids = new ArrayList<Integer>();
        for (RefundEntity refund : infoList)
        {
            if ("orderProductId".equals(column))
            {
                ids.add(refund.getOrderProductId());
            }
            else if ("orderId".equals(column))
            {
                ids.add(refund.getOrderId());
            }
            else if ("id".equals(column))
            {
                ids.add(refund.getId());
            }
        }
        return ids;
    }

    @Override
    public Map<String, Object> refundDetail(int id)
            throws Exception
    {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> info = new HashMap<>();
        info.put("status", 1);
        RefundEntity refund = refundDao.findRefundById(id);
        if (refund == null)
        {// 退款记录不存在或者
            result.put("status", 0);
            return result;
        }

        Map<String, Object> refundReason = refundReasonService.findRefundReasonById(refund.getRefundReasonId());
        if (refundReason != null)
            info.put("reason", refundReason.get("reason"));

        // 退款订单商品信息
        Map<String, Object> productInfo = refundDao.findOrderProductInfoByOrderProductId(refund.getOrderProductId());
        // 订单
        OrderEntity order = orderDao.findOrderById(refund.getOrderId());
        info.put("appChannel",order.getAppChannel());
        // 用户退款银行信息
        // Map<String, Object> cardInfo = refundDao.findAccountCardById(refund.getAccountCardId());
        // 买家
        AccountEntity account = accountDao.findAccountById(order.getAccountId());
        // 收货信息
        OrderReceiveAddress receiveAddress = receiveAddressDao.findOrderReceiveAddressById(order.getReceiveAddressId());
        if (account == null || order == null || receiveAddress == null)
        {
            info.put("status", 0);
            return info;
        }

        String operationStatus = "";
        SellerEntity seller = sellerDao.findSellerById(order.getSellerId());
        if (seller.getIsOwner() == 1)
        {
            if (order.getStatus() == ORDER_STATUS.REVIEW.getCode()) // 待发货
            {
                if ((order.getOperationStatus() == 1))
                {
                    operationStatus = "该订单已被商家导出，确认时请核实信息。";
                }
                else
                {
                    operationStatus = "订单未被商家导出。";
                }
            }
        }
        info.put("operationStatus", operationStatus);
        info.put("orderType", OrderEnum.ORDER_TYPE.getDescByCode(order.getType()));
        int status = refund.getStatus();
        info.put("status", status);
        info.put("statusStr", CommonEnum.RefundStatusEnum.getDescriptionByOrdinal(status));
        info.put("username", account.getName());
        info.put("id", refund.getId() + "");
        info.put("accountId", refund.getAccountId() + "");
        info.put("orderId", refund.getOrderId() + "");
        info.put("number", order.getNumber());
        info.put("orderStatusCode", order.getStatus());
        info.put("orderStatus", OrderEnum.ORDER_STATUS.getDescByCode(order.getStatus()));
        info.put("totalPrice", (order.getTotalPrice() + order.getAdjustPrice()) + "");
        info.put("realPrice", (order.getRealPrice()) + "");
        info.put("receiveName", receiveAddress.getFullName());
        info.put("receiveMobile", receiveAddress.getMobileNumber());
        // 详细地址
        StringBuffer sb = new StringBuffer("");
        if (receiveAddress.getProvince() != null && !"".equals(receiveAddress.getProvince()))
        {
            sb.append(AreaCache.getInstance().getProvinceName(receiveAddress.getProvince())).append(" ");
        }
        if (receiveAddress.getCity() != null && !"".equals(receiveAddress.getCity()))
        {
            sb.append(AreaCache.getInstance().getCityName(receiveAddress.getCity())).append(" ");
        }
        if (receiveAddress.getDistrict() != null && !"".equals(receiveAddress.getDistrict()))
        {
            sb.append(AreaCache.getInstance().getDistinctName(receiveAddress.getDistrict())).append(" ");
        }
        if (receiveAddress.getDetailAddress() != null && !"".equals(receiveAddress.getDetailAddress()))
        {
            sb.append(receiveAddress.getDetailAddress());
        }
        info.put("address", sb.toString());
        info.put("typeStr", refund.getType() == 1 ? "仅退款" : "退款并退货");
        info.put("type", refund.getType());
        info.put("productId", productInfo.get("id") + "");
        info.put("productName", productInfo.get("name"));
        info.put("salesPrice", productInfo.get("salesPrice") + "");
        info.put("productCount", productInfo.get("productCount"));
        info.put("sellerName", productInfo.get("sellerName"));
        info.put("sellerType", SellerEnum.SellerTypeEnum.getDescByCode(Integer.parseInt(productInfo.get("sellerType") + "")));
        info.put("sendAddress", productInfo.get("sendAddress"));
        info.put("refundProductCount", refund.getCount());
        info.put("refundMoney", refund.getApplyMoney() + "");
        try
        {
            info.put("explain", URLDecoder.decode(refund.getExplain(), "UTF-8"));
        }
        catch (Exception e)
        {
            info.put("explain", refund.getExplain());// 退款说明
        }

        // 计算分摊优惠券、分摊积分、理论申请退款金额 、理论应返还积分、 理论应扣除积分
        Map<String, Object> orderPrductRefundInfo = orderService.calOrderRefundInfoByOrderNumber(Long.parseLong(order.getNumber()), refund.getCount(), refund.getOrderProductId(), 2);
        info.put("orderPrductRefundInfo", orderPrductRefundInfo);

        // 查询财务分摊数据
        RefundProportionEntity rpe = refundDao.findRefundProportionByRefundId(id);
        info.put("realMoney", refund.getRealMoney() + "");
        if (rpe != null)
        {
            info.put("refundProportionType", rpe.getType());
            info.put("refundProportionId", rpe.getId() + "");
            info.put("sellerMoney", rpe.getSellerMoney() + "");
            info.put("sellerPostageMoney", rpe.getSellerPostageMoney() + "");
            info.put("sellerDifferenceMoney", rpe.getSellerDifferenceMoney() + "");
            info.put("gegejiaMoney", rpe.getGegejiaMoney() + "");
            info.put("gegejiaPostageMoney", rpe.getGegejiaPostageMoney() + "");
            info.put("gegejiaDifferenceMoney", rpe.getGegejiaDifferenceMoney() + "");
        }
        else
        {
            // 查询该商品供货价
            Map<String, Object> opinfo = refundDao.findOrderProductCostById(refund.getOrderProductId());
            double cost = Double.valueOf(opinfo.get("cost") + "");
            if (refund.getCount() > 0)
            {
                info.put("sellerMoney", (cost * refund.getCount()));
            }
            else
            {
                info.put("sellerMoney", cost);
            }
            if (order.getFreightMoney() > 0)
            {
                info.put("gegejiaPostageMoney", order.getFreightMoney() + "");
            }
        }

        // 退款账户信息
        info.put("refundPayType", refund.getRefundPayType() + "");
        if (RefundEnum.REFUND_PAY_TYPE.CREATE_NEW_ACCOUNT_CARD.getCode() == refund.getRefundPayType())
        {
            int type = refund.getCardType();
            int bankType = refund.getBankType();
            String bankTypeDesc = "支付宝";
            String bankName = "支付宝";
            if (type == 1)
            {
                bankTypeDesc = "银行";
                bankName = CommonEnum.BankTypeEnum.getDescriptionByOrdinal(bankType);
            }
            info.put("bankTypeDesc", bankTypeDesc);
            info.put("cardName", refund.getCardName());
            info.put("cardNumber", refund.getCardNumber());
            info.put("bankName", bankName);
        }
        else
        {
            info.put("bankTypeDesc", "原支付账户");
            info.put("cardName", "");
            info.put("cardNumber", "");
            info.put("bankName", "");
        }

        // 获取具体退款url 微信 or 支付宝
        // https://mch.tenpay.com/cgi-bin/mch_refundtype_query.cgi?transaction_id=1230273901341507238256288327
        // https://pay.weixin.qq.com/index.php/trade/apply_refund?wxOrderNum=1230273901341507238256288327
        String returnPayUrl = "";
        String returnPayMark = "";
        String returnPayAccount = "";
        /**
         * 名称 收款账号 退款账号 链接地址
         *
         * 11 左岸城堡微信 【左岸城堡全球美食】 1266647901 当订单支付为微信，app渠道为19，展示为 左岸城堡全球美食
         * https://pay.weixin.qq.com/index.php/refundapply?autoWXOrderNum=1002670088201510101148915431
         *
         * 12 左岸城堡微信 【左岸城堡全球零食汇】 1270667101 当订单支付为微信，app渠道为20，展示为 左岸城堡全球零食汇
         * https://pay.weixin.qq.com/index.php/refundapply?autoWXOrderNum=1008240740201510101146127084
         *
         * 13 格家网络支付宝 mohh@gegejia.com 当订单支付为支付宝，app渠道为19和20，展示为 格家网络支付宝
         *
         * 14 格家-国际版hk gegejia_hk@yangege.com 当订单支付为支付宝，app版本号为1.8，展示为 国际支付宝 提供商户交易号
         *
         * 5 支付宝 yangege@yangege.com 原路返回
         * https://tradeeportlet.alipay.com/refund/fpSingleRefund.htm?tradeNo=2015080500001000210061522221&action=refund
         *
         * 8 银联 ywae0937 huanglh@yanggege.com ok
         *
         * 6 萧剑-微信网页版 1231417002 原路返回
         * https://pay.weixin.qq.com/index.php/trade/apply_refund?wxOrderNum=1002250641201508130611141925
         *
         * 7 萧剑-微信APP 1230273901 原路返回
         * https://mch.tenpay.com/cgi-bin/mch_refundtype_query.cgi?transaction_id=1230273901331508136629689294
         *
         * 10 格家-微信网页版 1249303601 原路返回
         * https://pay.weixin.qq.com/index.php/trade/apply_refund?wxOrderNum=1004020928201508130611772286
         *
         * 9 格家-微信APP 1249412201 原路返回
         * https://pay.weixin.qq.com/index.php/trade/apply_refund?wxOrderNum=1004020928201508130611772286
         *
         * 15 格家-微信APP 1289865701 浙江格家微信APPID:wx2fb3d2ceb5f42890
         *
         * 16 左岸城堡app
         *
         */
        int targetFinancialAffairsCardId = getTargetFinancialAffairsCardId(Integer.valueOf(order.getAppChannel()), order.getAppVersion(), order.getPayChannel(), order.getType(), order.getRealPrice());
        // 支付宝： 5（萧剑）， 13（国内格家支付宝），14（国际支付宝）
        if (Arrays.asList(5, 13, 14).contains(targetFinancialAffairsCardId))
        {
            if (targetFinancialAffairsCardId == 14)
            {
                String payMark = orderDao.findPayMarkTidOrderAliPay(order.getId());
                if (payMark != null && !"".equals(payMark))
                {
                    returnPayMark = "国际支付宝，请用商户交易号去国际支付宝系统查询并退款：" + payMark;
                }
            }
            else
            {
                String payId = orderDao.findPayTidOrderAliPay(order.getId());
                if (payId != null && !"".equals(payId))
                {
                    returnPayUrl = "https://tradeeportlet.alipay.com/refund/fpSingleRefund.htm?tradeNo=" + payId + "&action=refund";
                }
                else
                {
                    String payMark = orderDao.findPayMarkTidOrderAliPay(order.getId());
                    if (payMark != null && !"".equals(payMark))
                    {
                        returnPayMark = "系统中暂找不到支付宝交易号，拿个商户交易号去支付宝系统查下吧：" + payMark;
                    }
                }
            }
        }
        else if (Arrays.asList(6, 7, 9, 11, 12, 15, 16, 18, 19).contains(targetFinancialAffairsCardId))
        {
            String payId = orderDao.findPayTidOrderWeixinPay(order.getId());
            boolean hasPayId = false;
            if (payId != null && !"".equals(payId))
            {
                hasPayId = true;
            }

            if (Arrays.asList(11, 12, 15, 18, 19).contains(targetFinancialAffairsCardId))
            {
                if (hasPayId)
                {
                    returnPayUrl = "https://pay.weixin.qq.com/index.php/refundapply?autoWXOrderNum=" + payId;
                }
                else
                {
                    String payMark = orderDao.findPayMarkTidOrderWeixinPay(order.getId());
                    returnPayMark = "系统无交易号，有商户交易号：" + payMark;
                }
            }
            else if (targetFinancialAffairsCardId == 9)
            {
                returnPayAccount = "使用格家登陆";
                if (hasPayId)
                {
                    returnPayUrl = "https://pay.weixin.qq.com/index.php/trade/apply_refund?wxOrderNum=" + payId;
                }
                else
                {
                    returnPayMark = "系统无交易号";
                }
            }
            else if (targetFinancialAffairsCardId == 16)
            {
                // 左岸城堡
                String payMark = orderDao.findPayMarkTidOrderWeixinPay(order.getId());
                returnPayMark = "商户交易号：" + payMark;
            }
            else if (targetFinancialAffairsCardId == 6)
            {
                returnPayAccount = "使用箫剑登陆";
                if (hasPayId)
                {
                    // returnPayUrl = "https://mch.tenpay.com/cgi-bin/mch_refundtype_query.cgi?transaction_id=" +
                    // payId;
                    returnPayUrl = "https://mch.tenpay.com/cgi-bin/spbill_query.cgi?sp_billno=" + payId;
                }
                else
                {
                    returnPayMark = "系统无交易号";
                }
            }
            else if (targetFinancialAffairsCardId == 7)
            {
                returnPayAccount = "使用箫剑登陆";
                if (hasPayId)
                {
                    returnPayUrl = "https://pay.weixin.qq.com/index.php/trade/apply_refund?wxOrderNum=" + payId;
                }
                else
                {
                    returnPayMark = "系统无交易号";
                }
            }
        }
        else if (targetFinancialAffairsCardId == 8)
        {
            String payId = orderDao.findPayTidOrderUnionPay(order.getId());
            if (payId != null && !"".equals(payId))
            {
                returnPayMark = "银联交易号：" + payId;
            }
        }

//        if (order.getPayChannel() == OrderEnum.PAY_CHANNEL.ALIPAY.getCode())
//        {
//            double appVersion = Double.valueOf((order.getAppVersion() == null || "".equals(order.getAppVersion())) ? "0" : order.getAppVersion()).doubleValue();
//
//            // if ("19".equals(order.getAppChannel()) || "20".equals(order.getAppChannel()) ||
//            // "25".equals(order.getAppChannel()))
//            // {
//            // targetFinancialAffairsCardId = 13;
//            // }
//            if (appVersion >= 1.8)
//            {
//                if (order.getRealPrice() <= 0.1f) // 使用国内支付宝
//                {
//                    targetFinancialAffairsCardId = 13;
//                }
//                else
//                {
//                    targetFinancialAffairsCardId = 14;
//                }
//            }
//            else if ("11".equals(order.getAppChannel()))
//            {
//                // 移动网站
//                if (order.getRealPrice() <= 0.1f)
//                {
//                    targetFinancialAffairsCardId = 5;
//                }
//                else
//                {
//                    targetFinancialAffairsCardId = 14;
//                }
//            }
//            else
//            {
//                targetFinancialAffairsCardId = 5;
//            }
//
//            if (appVersion >= 1.8)
//            {
//                if (order.getRealPrice() <= 0.1f) // 使用国内支付宝
//                {
//                    String payId = orderDao.findPayTidOrderAliPay(order.getId());
//                    if (payId != null && !"".equals(payId))
//                    {
//                        returnPayUrl = "https://tradeeportlet.alipay.com/refund/fpSingleRefund.htm?tradeNo=" + payId + "&action=refund";
//                    }
//                    else
//                    {
//                        String payMark = orderDao.findPayMarkTidOrderAliPay(order.getId());
//                        if (payMark != null && !"".equals(payMark))
//                        {
//                            returnPayMark = "系统中暂找不到支付宝交易号，拿个商户交易号去支付宝系统查下吧：" + payMark;
//                        }
//                    }
//                }
//                else
//                {
//                    String payMark = orderDao.findPayMarkTidOrderAliPay(order.getId());
//                    if (payMark != null && !"".equals(payMark))
//                    {
//                        returnPayMark = "国际支付宝，请用商户交易号去国际支付宝系统查询并退款：" + payMark;
//                    }
//                }
//            }
//            else
//            {
//                String payId = orderDao.findPayTidOrderAliPay(order.getId());
//                if (payId != null && !"".equals(payId))
//                {
//                    returnPayUrl = "https://tradeeportlet.alipay.com/refund/fpSingleRefund.htm?tradeNo=" + payId + "&action=refund";
//                }
//                else
//                {
//                    String payMark = orderDao.findPayMarkTidOrderAliPay(order.getId());
//                    if (payMark != null && !"".equals(payMark))
//                    {
//                        returnPayMark = "系统中暂找不到支付宝交易号，拿个商户交易号去支付宝系统查下吧：" + payMark;
//                    }
//                }
//            }
//        }
//        else if (order.getPayChannel() == OrderEnum.PAY_CHANNEL.WEIXIN.getCode())
//        {
//            String payId = orderDao.findPayTidOrderWeixinPay(order.getId());
//            boolean hasPayId = false;
//            if (payId != null && !"".equals(payId))
//            {
//                hasPayId = true;
//            }
//            // 判断退款登陆账号
//            double version = 0;
//            try
//            {
//                version = Double.parseDouble((order.getAppVersion() == null || "".equals(order.getAppVersion())) ? "0" : order.getAppVersion());
//            }
//            catch (Exception e)
//            {
//            }
//
//            if ("28".equals(order.getAppChannel()))
//            {
//                // 左岸城堡app 订单
//                targetFinancialAffairsCardId = 18;
//                if (hasPayId)
//                {
//                    returnPayUrl = "https://pay.weixin.qq.com/index.php/refundapply?autoWXOrderNum=" + payId;
//                }
//                else
//                {
//                    String payMark = orderDao.findPayMarkTidOrderWeixinPay(order.getId());
//                    returnPayMark = "系统无交易号，有商户交易号：" + payMark;
//                }
//            }
//            else if ("24".equals(order.getAppChannel()) || "26".equals(order.getAppChannel()) || "27".equals(order.getAppChannel()))
//            {
//                // 左岸城堡app 订单
//                targetFinancialAffairsCardId = 16;
//                // if (hasPayId)
//                // {
//                // returnPayUrl = "https://pay.weixin.qq.com/index.php/refundapply?autoWXOrderNum=" + payId;
//                // }
//                // else
//                // {
//                String payMark = orderDao.findPayMarkTidOrderWeixinPay(order.getId());
//                returnPayMark = "代财务通知链接，有商户交易号：" + payMark;
//                // }
//            }
//            else if ("22".equals(order.getAppChannel())) // OrderAppChannelEnum.WECHAT_GROUP
//            {
//                targetFinancialAffairsCardId = 15;
//                if (hasPayId)
//                {
//                    returnPayUrl = "https://pay.weixin.qq.com/index.php/refundapply?autoWXOrderNum=" + payId;
//                }
//                else
//                {
//                    String payMark = orderDao.findPayMarkTidOrderWeixinPay(order.getId());
//                    returnPayMark = "系统无交易号，有商户交易号：" + payMark;
//                }
//            }
//            else if (("19".equals(order.getAppChannel()) || "25".equals(order.getAppChannel())) && version > 1.5)
//            {
//                targetFinancialAffairsCardId = 11;
//                if (hasPayId)
//                {
//                    returnPayUrl = "https://pay.weixin.qq.com/index.php/refundapply?autoWXOrderNum=" + payId;
//                }
//                else
//                {
//                    String payMark = orderDao.findPayMarkTidOrderWeixinPay(order.getId());
//                    returnPayMark = "系统无交易号，有商户交易号：" + payMark;
//                }
//            }
//            else if ("20".equals(order.getAppChannel()) && version > 1.5)
//            {
//                targetFinancialAffairsCardId = 12;
//                if (hasPayId)
//                {
//                    returnPayUrl = "https://pay.weixin.qq.com/index.php/refundapply?autoWXOrderNum=" + payId;
//                }
//                else
//                {
//                    String payMark = orderDao.findPayMarkTidOrderWeixinPay(order.getId());
//                    returnPayMark = "系统无交易号，有商户交易号：" + payMark;
//                }
//            }
//            else if (version > 1.5)
//            {
//                returnPayAccount = "使用格家登陆";
//                if (order.getNumber().endsWith("1"))
//                {
//                    // app订单
//                    if (hasPayId)
//                    {
//                        returnPayUrl = "https://pay.weixin.qq.com/index.php/trade/apply_refund?wxOrderNum=" + payId;
//                    }
//                    else
//                    {
//                        returnPayMark = "系统无交易号";
//                    }
//                    targetFinancialAffairsCardId = 9;
//                }
//                else
//                {
//                    // 网页
//                    if (hasPayId)
//                    {
//                        returnPayUrl = "https://pay.weixin.qq.com/index.php/trade/apply_refund?wxOrderNum=" + payId;
//                    }
//                    else
//                    {
//                        returnPayMark = "系统无交易号";
//                    }
//                    targetFinancialAffairsCardId = 10;
//                }
//            }
//            else
//            {
//                returnPayAccount = "使用箫剑登陆";
//                if (order.getNumber().endsWith("1"))
//                {
//                    // app订单
//                    if (hasPayId)
//                    {
//                        // returnPayUrl = "https://mch.tenpay.com/cgi-bin/mch_refundtype_query.cgi?transaction_id=" +
//                        // payId;
//                        returnPayUrl = "https://mch.tenpay.com/cgi-bin/spbill_query.cgi?sp_billno=" + payId;
//                    }
//                    else
//                    {
//                        returnPayMark = "系统无交易号";
//                    }
//                    targetFinancialAffairsCardId = 7;
//                }
//                else
//                {
//                    // 网页
//                    if (hasPayId)
//                    {
//                        returnPayUrl = "https://pay.weixin.qq.com/index.php/trade/apply_refund?wxOrderNum=" + payId;
//                    }
//                    else
//                    {
//                        returnPayMark = "系统无交易号";
//                    }
//                    targetFinancialAffairsCardId = 6;
//                }
//            }
//        }
//        else if (order.getPayChannel() == OrderEnum.PAY_CHANNEL.UNION.getCode())
//        {
//            targetFinancialAffairsCardId = 8;
//            String payId = orderDao.findPayTidOrderUnionPay(order.getId());
//            if (payId != null && !"".equals(payId))
//            {
//                returnPayMark = "银联交易号：" + payId;
//            }
//        }

        info.put("targetFinancialAffairsCardId", targetFinancialAffairsCardId);
        info.put("returnPayUrl", returnPayUrl);
        info.put("returnPayMark", returnPayMark);
        info.put("returnPayAccount", returnPayAccount);
        // 附图
        String image1 = refund.getImage1();
        String image2 = refund.getImage2();
        String image3 = refund.getImage3();
        if (image1 != null && !"".equals(image1))
        {
            info.put("image1", image1);
            info.put("normalImage1", image1.substring(0, image1.lastIndexOf(ImageUtil.getPrefix())));
        }
        if (image2 != null && !"".equals(image2))
        {
            info.put("image2", image2);
            info.put("normalImage2", image2.substring(0, image2.lastIndexOf(ImageUtil.getPrefix())));
        }
        if (image3 != null && !"".equals(image3))
        {
            info.put("image3", image3);
            info.put("normalImage3", image3.substring(0, image3.lastIndexOf(ImageUtil.getPrefix())));
        }

        // 如果订单未发货 ，找出订单商品列表
        if (order.getStatus() == Byte.parseByte(OrderEnum.ORDER_STATUS.REVIEW.getCode() + ""))
        {
            // 获取订单商品退款相关信息
            Map<String, Object> refundPara = new HashMap<String, Object>();
            refundPara.put("orderId", order.getId());
            List<Integer> statusList =
                    new ArrayList<Integer>(
                            Arrays.asList(CommonEnum.RefundStatusEnum.APPLY.ordinal(), CommonEnum.RefundStatusEnum.WAIT_RETURN_OF_GOODS.ordinal(), CommonEnum.RefundStatusEnum.SUCCESS.ordinal(), CommonEnum.RefundStatusEnum.WAIT_RETURN_OF_MONEY.ordinal()));
            refundPara.put("statusList", statusList);
            List<RefundEntity> refunds = refundDao.findAllRefundByPara(refundPara);
            // 获取订单商品相关信息
            List<Map<String, Object>> orderProductList = orderDao.findProductInfoByOrderId(order.getId());
            for (Map<String, Object> it : orderProductList)
            {
                Integer itId = Integer.parseInt(it.get("id") + "");
                int refundCount = 0;
                String refundStatus = "未退款";
                int curr = 0;
                for (RefundEntity currRefund : refunds)
                {
                    if (itId == currRefund.getOrderProductId())
                    {
                        if (currRefund.getId() == refund.getId())
                        {
                            // 当前退款退货商品
                            refundCount = currRefund.getCount();
                            refundStatus = "当前退款商品";
                            curr = 1;
                        }
                        else
                        {
                            // 其他退款退货商品
                            refundCount = currRefund.getCount();
                            refundStatus = CommonEnum.RefundStatusEnum.getDescriptionByOrdinal(currRefund.getStatus());
                        }
                        break;
                    }
                }
                it.put("curr", curr);
                it.put("refundCount", refundCount);
                it.put("refundStatus", refundStatus);
            }
            info.put("orderProductList", orderProductList);
        }

        if (refund.getType() == 1)
        {
            Map<String, Object> logisticsMap = orderDao.findOrderLogisticsByOrderId(refund.getOrderId());
            if (logisticsMap != null && !logisticsMap.isEmpty())
            {
                String channel = logisticsMap.get("channel") + "";
                String number = logisticsMap.get("number") + "";
                info.put("logisticsChannel", channel);
                info.put("logisticsNumber", number);
                Map<String, Object> detailMap = orderDao.findLastLogisticsDetail(channel, number);
                if (detailMap != null && !detailMap.isEmpty())
                {
                    info.put("logisticsDetail", ((Timestamp)detailMap.get("operate_time")).toString() + "&nbsp;" + detailMap.get("content"));
                }
                else
                {
                    info.put("logisticsDetail", "暂无最新物流");
                }
            }
        }
        result.put("refund", info);
        result.put("status", 1);
        return result;
    }

    @Override
    public Map<String, Object> findRefundOnlyReturnMoneyTeackMap(int refundId)
            throws Exception
    {
        /*
         * 仅退款 操作记录信息 【处理1】客服处理退款申请 --> 1 【处理2】财务打款处理 --> 2
         */
        RefundEntity refund = refundDao.findRefundById(refundId);
        Map<String, Object> result = new HashMap<String, Object>();
        // RefundEntity refund = refundDao.findRefundById(refundId);
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("refundId", refundId);
        List<OrderProductRefundTeackEntity> teackList = refundDao.findOrderProductRefundTeack(para);
        for (OrderProductRefundTeackEntity teack : teackList)
        {
            Map<String, Object> currInfo = new HashMap<>();
            User user = userDao.findUserById(teack.getManagerId());
            String dealManager = user == null ? "app用户" : user.getRealname();
            currInfo.put("dealManager", dealManager + " - " + teack.getCreateTime());
            currInfo.put("content", teack.getContent());
            currInfo.put("remark", teack.getRemark());
            byte step = teack.getStep();
            currInfo.put("step", step);
            if (step == 1)
            {
                // 查询实际返回积分和实际扣除积分
                currInfo.put("returnAccountPoint", refund.getReturnAccountPoint());
                currInfo.put("removeAccountPoint", refund.getRemoveAccountPoint());
                Map<String, Object> info = orderService.calOrderRefundInfoByOrderId(refund.getOrderId(), refund.getCount(), refund.getOrderProductId());
                currInfo.put("logicGiveAccountPoint", info.get("logicGiveAccountPoint"));
                currInfo.put("logicRemoveAccountPoint", info.get("logicRemoveAccountPoint"));
            }
            result.put("step" + step, currInfo);
        }
        return result;
    }

    @Override
    public Map<String, Object> findRefundReturnMoneyAndGoodsTeackMap(int refundId)
            throws Exception
    {
        /*
         * 退款并退货 操作记录信息 【处理1】客服处理退款申请 --> 1 【处理2】顾客退货 --> 2 【处理2】客服审核退货 --> 3 【处理2】财务打款处理 --> 4
         */
        RefundEntity refund = refundDao.findRefundById(refundId);
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("refundId", refundId);
        List<OrderProductRefundTeackEntity> teackList = refundDao.findOrderProductRefundTeack(para);
        for (OrderProductRefundTeackEntity teack : teackList)
        {
            Map<String, Object> currInfo = new HashMap<String, Object>();
            User user = userDao.findUserById(teack.getManagerId());
            String dealManager = user == null ? "app用户" : user.getRealname();
            currInfo.put("dealManager", dealManager + " - " + teack.getCreateTime());
            byte step = teack.getStep();
            String content = teack.getContent();
            if (step == 3)
            {
                // 查询实际返回积分和实际扣除积分
                currInfo.put("returnAccountPoint", refund.getReturnAccountPoint() + "");
                currInfo.put("removeAccountPoint", refund.getRemoveAccountPoint() + "");
                Map<String, Object> info = orderService.calOrderRefundInfoByOrderId(refund.getOrderId(), refund.getCount(), refund.getOrderProductId());
                currInfo.put("logicGiveAccountPoint", info.get("logicGiveAccountPoint"));
                currInfo.put("logicRemoveAccountPoint", info.get("logicRemoveAccountPoint"));
            }
            // 当step2 是特殊处理；来源有两种：1：前台订单发货的时候，调用后台接口是增加处理step2处理；2：客服后台手动取消操作
            else if (step == 2)
            {
                // 查询是否有物流单号，没有的话则取记录 的 content 作为展示内容
                Map<String, Object> logistics = refundDao.findRefundLogisticsByRefundId(refundId);
                if (logistics != null)
                {
                    String channel = logistics.get("channel") + "";
                    String number = logistics.get("number") + "";
                    content = "物流公司：" + channel + "        ";
                    content += "物流单号：" + number;
                    Map<String, Object> logisticMap = new HashMap<>();
                    logisticMap.put("logisticsChannel", channel);
                    logisticMap.put("logisticsNumber", number);
                    List<Map<String, Object>> logisticsList = orderDao.findAllLogisticsDetail(logisticMap);
                    if (logisticsList != null && logisticsList.size() > 0)
                    {
                        currInfo.put("logisticsList", logisticsList);
                    }
                }
                else
                {
                    content = "客服 " + teack.getContent();
                }
            }

            currInfo.put("content", content);
            currInfo.put("remark", teack.getRemark());
            currInfo.put("step", step);
            currInfo.put("createTime", teack.getCreateTime());
            result.put("step" + step, currInfo);
        }
        return result;
    }

    @Override
    public Map<String, Object> updateRefund(Integer type, Integer dealType, Byte step, User manager, Integer id, String remark, Double money, int cardId,
                                            boolean modifyRefundType, boolean sendGoods, String realSendGoodsCount, boolean cancelOrder, int refundReasonId)
            throws Exception
    {
        RefundEntity refund = refundDao.findRefundById(id);
        if (refund == null)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", 0);
            map.put("msg", "退款信息不存在");
            return map;
        }

        Map<String, Object> para = new HashMap<String, Object>();
        para.put("checkTime", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        para.put("id", id);

        FinancialAffairsCardEntity face = refundDao.findFinancialAffairsCardById(cardId);
        String cardInfo = face == null?"":face.getCardInfo();
        String content = "";// 操作详情
        boolean isFreezeForever = false;// 是否永久冻结订单
        if (type == 1)
        {// 仅退款处理
            if (dealType == 1)
            {// 同意申请
                if (step == 1)
                {// 待退款
                    para.put("refundReasonId", refundReasonId);
                    para.put("status", CommonEnum.RefundStatusEnum.WAIT_RETURN_OF_MONEY.ordinal());
                    if (money < 0)
                    {
                        money = refund.getApplyMoney();
                    }
                    para.put("realMoney", money);
                    content = money + "";
                    if (cancelOrder)
                    {
                        para.put("cancelOrder", 1);
                        refund.setIsCancelOrder(1);
                    }
                    if (modifyRefundType)
                    {
                        // 修改仅退款为退货并退款
                        para.put("type", 2);
                        para.put("status", CommonEnum.RefundStatusEnum.WAIT_RETURN_OF_GOODS.ordinal());
                    }
                    else
                    {
                        if (sendGoods)
                        {
                            // 继续发货
                            if (!"".equals(realSendGoodsCount))
                            {
                                String[] cs = realSendGoodsCount.split(";");
                                for (String it : cs)
                                {
                                    // 需要加回购物车的数量
                                    int returnToStockNum = 0;

                                    String[] orderProductIdAndSendCount = it.split(",");
                                    Integer orderProductId = Integer.parseInt(orderProductIdAndSendCount[0]);
                                    Integer sendCount = Integer.parseInt(orderProductIdAndSendCount[1]);

                                    Map<String, Object> orderProduct = orderDao.findOrderProductById(orderProductId);
                                    int productCount = Integer.parseInt(orderProduct.get("productCount") + "");
                                    if (productCount > sendCount)
                                    {
                                        returnToStockNum = productCount - sendCount;
                                    }
                                    sendCount = sendCount > productCount ? productCount : sendCount;
                                    int adjustCount = productCount - sendCount;

                                    Map<String, Object> updateOrderProductPara = new HashMap<String, Object>();
                                    updateOrderProductPara.put("id", orderProductId);
                                    updateOrderProductPara.put("adjustCount", -adjustCount);
                                    int st = orderDao.updateOrderProduct(updateOrderProductPara);
                                    if (st == 0)
                                    {
                                        throw new ServiceException();
                                    }

                                    if (returnToStockNum > 0 && orderProductId == refund.getOrderProductId())
                                    {
                                        int productId = Integer.parseInt(orderProduct.get("productId") + "");
                                        ProductCountEntity pce = productDao.findProductCountByProductId(productId);
                                        if (pce != null)
                                        {
                                            if (productDao.updateProductStock(productId, pce.getStock(), returnToStockNum) == 0)
                                            {
                                                log.warn("商品退款加回库存失败！！！退款ID：" + refund.getId() + "，商品ID：" + productId);
                                            }
                                            else
                                            {
                                                log.info("商品退款加回库存成功！退款ID：" + refund.getId() + "，商品ID：" + productId);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else
                        {
                            // 将订单状态置为永久冻结 并将退款商品库存增加回去
                            Map<String, Object> updateFreeze = new HashMap<>();
                            updateFreeze.put("orderId", refund.getOrderId());
                            updateFreeze.put("unfreezeTime", "0000-00-00 00:00:00");
                            updateFreeze.put("status", OrderEnum.ORDER_FREEZE_STATUS.FREEZE_FOREVER.getCode());
                            try
                            {
                                orderDao.updateOrderFreezeRecord(updateFreeze);
                            }
                            catch (Exception e)
                            {
                                log.warn(e.getMessage(), e);
                                throw e;
                            }
                            isFreezeForever = true;

                            // 加回库存
                            List<Map<String, Object>> orderProductList = orderDao.findProductInfoByOrderId(refund.getOrderId());
                            for (Map<String, Object> it : orderProductList)
                            {
                                Integer orderProductId = Integer.valueOf(it.get("id") == null ? "0" : it.get("id") + "");
                                Integer productId = Integer.valueOf(it.get("productId") == null ? "0" : it.get("productId") + "");
                                Integer productCount = Integer.valueOf(it.get("productCount") == null ? "0" : it.get("productCount") + "");
                                if (productId > 0 && productCount > 0 && refund.getOrderProductId() == orderProductId)
                                {
                                    ProductCountEntity pce = productDao.findProductCountByProductId(productId);
                                    if (pce != null)
                                    {
                                        if (productDao.updateProductStock(productId, pce.getStock(), productCount) == 0)
                                        {
                                            log.warn("商品退款加回库存失败！！！退款ID：" + refund.getId() + "，商品ID：" + productId);
                                        }
                                        else
                                        {
                                            log.info("商品退款加回库存成功！退款ID：" + refund.getId() + "，商品ID：" + productId);
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
                else if (step == 2)
                {// 退款成功
                    para.put("status", CommonEnum.RefundStatusEnum.SUCCESS.ordinal());
                    // 记录财务打款账户信息
                    content = cardInfo;
                }
            }
            else if (dealType == 2)
            {// 退款关闭
                para.put("status", CommonEnum.RefundStatusEnum.CLOSE.ordinal());
                content = "关闭退款";
            }
            else if (dealType == 3)
            {// 退款取消
                para.put("status", CommonEnum.RefundStatusEnum.CANCEL.ordinal());
                content = "取消退款";
            }
        }
        else
        {// 默认为退款并退货
            isFreezeForever = true;
            if (dealType == 1)
            {// 同意申请
                if (step == 1)
                {// 待退货
                    para.put("refundReasonId", refundReasonId);
                    para.put("status", CommonEnum.RefundStatusEnum.WAIT_RETURN_OF_GOODS.ordinal());
                    if (money < 0)
                    {
                        money = refund.getApplyMoney();
                    }
                    para.put("realMoney", money);
                    content = money + "";

                    if (modifyRefundType)
                    {
                        // 修改仅退款为退货并退款
                        para.put("type", 1);
                        para.put("status", CommonEnum.RefundStatusEnum.WAIT_RETURN_OF_MONEY.ordinal());
                    }
                }
                else if (step == 3)
                {// 待退款 提交财务打款 客服提交财务打款这里 不更改订单状态
                    para.put("status", CommonEnum.RefundStatusEnum.WAIT_RETURN_OF_MONEY.ordinal());
                    content = "客服审核退货成功";
                }
                else if (step == 4)
                {// 退款成功
                    para.put("status", CommonEnum.RefundStatusEnum.SUCCESS.ordinal());
                    // 记录财务打款账户信息
                    content = cardInfo;
                }
            }
            else if (dealType == 2)
            {// 退款关闭
                para.put("status", CommonEnum.RefundStatusEnum.CLOSE.ordinal());
                content = "关闭退款并退货";
            }
            else if (dealType == 3)
            {// 退款取消
                para.put("status", CommonEnum.RefundStatusEnum.CANCEL.ordinal());
                content = "取消退款并退货";
            }
        }

        // 取消订单冻结状态
        if (!isFreezeForever && (dealType == 2 || dealType == 3 || (type == 1 && step == 1) || (type == 2 && step == 1)))
        {
            // 先查询改订单ID下是否存在其他正在退款中的商品
            Map<String, Object> refundPara = new HashMap<String, Object>();
            refundPara.put("orderId", refund.getOrderId());
            refundPara.put("statusList", Arrays.asList(CommonEnum.RefundStatusEnum.APPLY.ordinal()));// 只取申请中的即可
            List<RefundEntity> res = refundDao.findAllRefundByPara(refundPara);
            if (res.size() == 1 && res.get(0).getId() == refund.getId())
            {
                Map<String, Object> upFreezePara = new HashMap<String, Object>();
                upFreezePara.put("id", refund.getOrderId());
                upFreezePara.put("isFreeze", 2);// 设置为已解冻订单
                int upStatus = orderDao.updateOrder(upFreezePara);
                if (upStatus != 1)
                {
                    log.warn("取消订单冻结状态失败refundid:" + refund.getId());
                    throw new ServiceException();
                }
                Map<String, Object> freezeMap = orderDao.findOrderFreezeByOrderId(refund.getOrderId());
                if (freezeMap != null)
                {
                    // 更新订单冻结记录
                    Map<String, Object> updateFreeze = new HashMap<String, Object>();
                    updateFreeze.put("orderId", refund.getOrderId());
                    updateFreeze.put("unfreezeTime", DateTimeUtil.now());
                    updateFreeze.put("status", OrderEnum.ORDER_FREEZE_STATUS.UN_FREEZE.getCode());
                    int s = orderDao.updateOrderFreezeRecord(updateFreeze);
                    if (s == 0)
                    {
                        throw new ServiceException();
                    }
                }
                log.info("取消订单冻结状态");
            }
            else
            {
                log.info("跳过取消订单冻结状态");
            }
        }
        OrderEntity order = orderDao.findOrderById(refund.getOrderId());
        // 财务退款成功后判断是否需要将 待发货状态 的订单置为 已取消
        if (((type == 1 && step == 2) || (type == 2 && step == 4)) && refund.getIsCancelOrder() == 1)
        {
            // 先查询改订单ID下是否存在其他正在退款中的商品
            Map<String, Object> refundPara = new HashMap<String, Object>();
            refundPara.put("orderId", refund.getOrderId());
            refundPara.put("statusList", Arrays.asList(CommonEnum.RefundStatusEnum.APPLY.ordinal(), CommonEnum.RefundStatusEnum.WAIT_RETURN_OF_GOODS.ordinal(), CommonEnum.RefundStatusEnum.WAIT_RETURN_OF_MONEY.ordinal()));
            List<RefundEntity> res = refundDao.findAllRefundByPara(refundPara);
            if (res.size() == 1 && res.get(0).getId() == refund.getId())
            {
                if (order.getStatus() == Byte.parseByte(OrderEnum.ORDER_STATUS.REVIEW.getCode() + ""))// 待发货订单
                {
                    boolean modifyOrderStatusToCancel = true;
                    // 获取订单商品相关信息
                    List<Map<String, Object>> orderProductList = orderDao.findOrderProductByOrderId(refund.getOrderId());
                    for (Map<String, Object> it : orderProductList)
                    {
                        Integer productCount = Integer.parseInt(it.get("productCount") + "");
                        Integer adjustCount = Integer.parseInt(it.get("adjustCount") + "");
                        if (productCount + adjustCount > 0)
                        {
                            // 存在需要发货的商品，不可将订单置为取消
                            modifyOrderStatusToCancel = false;
                            break;
                        }
                    }
                    if (modifyOrderStatusToCancel)
                    {
                        log.info("修改订单状态");
                        Map<String, Object> updatePara = new HashMap<String, Object>();
                        updatePara.put("id", refund.getOrderId());
                        updatePara.put("status", OrderEnum.ORDER_STATUS.USER_CANCEL.getCode());
                        if (order.getType() == OrderEnum.ORDER_TYPE.GEGETUAN.getCode())
                        {
                            // 左岸城堡订单6 含义其他
                            updatePara.put("status", ORDER_STATUS.TIMEOUT.getCode());
                        }
                        int upStatus = orderDao.updateOrder(updatePara);
                        if (upStatus != 1)
                        {
                            log.warn("订单商品全部已经退款，将订单状态置为用户取消失败。refundid:" + refund.getId());
                            Map<String, Object> result = new HashMap<String, Object>();
                            result.put("status", 0);
                            result.put("msg", "保存失败!");
                            return result;
                        }
                    }
                }
            }
            else
            {
                log.info("跳过修改订单状态");
            }
        }

        if (refundDao.updateRefund(para) == 0)
        {
            throw new ServiceException();
        }

        // 返回用户优惠券
        if (step == 1 && order.getStatus() == ORDER_STATUS.REVIEW.getCode() && order.getAccountCouponId() > 0)
        {
            List<Integer> orderIdList = orderDao.findOrderIdByCouponAccountId(order.getAccountCouponId());
            if (orderIdList.size() > 0)
            {
                boolean isReturnCoupon = true;
                Map<String, Object> _para = new HashMap<>();
                _para.put("orderIdList", orderIdList);
                List<Map<String, Object>> opList = orderDao.findAllOrderProductInfoByPara(_para);
                for (Map<String, Object> it : opList)
                {
                    // 其中有一个不满足就 不返优惠券
                    int opid = Integer.valueOf(it.get("id") + "");
                    if (refundDao.countRefundByOrderProductId(opid) == 0)
                    {
                        isReturnCoupon = false;
                        break;
                    }
                }
                if (isReturnCoupon)
                {
                    AccountCouponEntity ace = accountDao.findAccountCouponById(order.getAccountCouponId());
                    if (ace != null)
                    {
                        ace.setIsUsed(0);
                        ace.setSourceType(CouponEnum.CouponAccountSourceType.ORDER_CANCEL_RETURN.getCode());
                        ace.setRemark("退款返还");
                        Date payTime = CommonUtil.string2Date(order.getPayTime(), "yyyy-MM-dd HH:mm:ss");
                        DateTime payTime_dateTime = new DateTime(payTime);
                        int reDay = Days.daysBetween(payTime_dateTime, DateTime.now()).getDays();
                        reDay = Math.abs(reDay) + 1;
                        ace.setEndTime(DateTime.now().plusDays(reDay).toString("yyyy-MM-dd 23:59:59"));
                        if (accountDao.insertAccountCoupon(ace) == 0)
                        {
                            log.warn("返还优惠券失败！！refundid:" + refund.getId());
                        }
                    }
                }
            }
        }

        // 待发货订单全部同意退款 订单取消
        if (step == 1 && order.getStatus() == ORDER_STATUS.REVIEW.getCode() && refund.getIsCancelOrder() == 1)
        {
            // 查看其他 订单商品是否都已经 cancel
            //            Map<String, Object> searchMap = new HashMap<>();
            //            searchMap.put("orderId", refund.getOrderId());
            //            searchMap.put("refundId", refund.getId());
            //            int count = refundDao.findOtherNotExistsRefundForCancelOrderForStep1(searchMap);
            //            if (count == 0)
            //            {
            log.info("修改订单状态");
            Map<String, Object> updatePara = new HashMap<>();
            updatePara.put("id", refund.getOrderId());
            updatePara.put("status", OrderEnum.ORDER_STATUS.USER_CANCEL.getCode());
            if (orderDao.updateOrder(updatePara) == 0)
            {
                log.warn("更新订单状态为用户取消失败！！refundid:" + refund.getId());
                throw new ServiceException();
            }
            else
            {
                log.warn("更新订单状态为用户取消 成功。refundid:" + refund.getId());
            }
            //            }
        }

        // 插入 更新 记录
        OrderProductRefundTeackEntity teack = new OrderProductRefundTeackEntity();
        teack.setOrderProductRefundId(refund.getId());
        teack.setManagerId(manager != null ? manager.getId() : 0);
        if (remark != null)
        {
            teack.setRemark(remark);
        }
        teack.setContent(content);
        teack.setStep(step);
        if (refundDao.saveOrderProductRefundTeack(teack) == 0)
        {
            throw new ServiceException();
        }

        // 财务操作 当订单状态是用户取消时自动保存财务分摊信息
        if ((order.getStatus() == ORDER_STATUS.USER_CANCEL.getCode()) && ((type == 1 && step == 2) || (type == 2 && step == 4)))
        {
            RefundProportionEntity rpe = refundDao.findRefundProportionByRefundId(id);
            if (rpe == null)
            {
                // 查询该商品供货价
                double cost = Double.valueOf(refundDao.findOrderProductCostById(refund.getOrderProductId()).get("cost") + "");
                if (refund.getCount() > 0)
                {
                    cost = cost * refund.getCount();
                }
                double gegejiaPostageMoney = order.getFreightMoney();
                double gegejiaMoney = refund.getRealMoney() - cost - gegejiaPostageMoney;

                Map<String, Object> _para = new HashMap<>();
                _para.put("refundId", id);
                _para.put("id", 0);
                _para.put("type", 0);
                _para.put("sellerMoney", cost);
                _para.put("sellerPostageMoney", 0);
                _para.put("sellerDifferenceMoney", 0);
                _para.put("gegejiaMoney", gegejiaMoney);
                _para.put("gegejiaPostageMoney", gegejiaPostageMoney);
                _para.put("gegejiaDifferenceMoney", 0);
                log.info(saveOrUpdateFinanceShare(_para));
            }
        }

        // 给商家发送提醒短信
        SellerEntity se = sellerDao.findSellerById(order.getSellerId());
        if (step == 1 && se.getIsOwner() == 1 && order.getStatus() == ORDER_STATUS.REVIEW.getCode() && order.getOperationStatus() == 0
                && CommonUtil.isMobile(se.getFhContactMobile()))
        {
            try
            {
                Map<String, Object> info = orderDao.findReceiveInfoById(order.getReceiveAddressId());
                String fullName = info.get("fullName") + "";

                Map<String, Object> sendPara = new HashMap<>();
                sendPara.put("phoneNumber", se.getFhContactMobile());
                sendPara.put("type", CommonEnum.SmsTypeEnum.REMIND_SELLER_SENDGOODS.ordinal());
                sendPara.put("content", se.getRealSellerName() + "，因用户主动申请退款，您未导出发货的订单（" + order.getNumber() + "，" + fullName + "）中的部分或全部商品已被左岸城堡客服同意退款，请您留意该订单的实际发货情况，防止多发或漏发，谢谢：）");
                sendPara.put("serviceType", SmsServiceTypeEnum.MENGWANG.getValue());
                smsService.sendMontnets(sendPara);
            }
            catch (Exception e)
            {
                log.error("退款退货-给商家发送提醒短信出错！", e);
            }
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", 1);
        return map;
    }

    @Override
    public Map<String, Object> updateRefundPrice(Integer id, Double money)
            throws Exception
    {
        RefundEntity refund = refundDao.findRefundById(id);
        Map<String, Object> result = new HashMap<String, Object>();
        int status = 0;
        String msg = "";
        if (refund.getStatus() == CommonEnum.RefundStatusEnum.WAIT_RETURN_OF_GOODS.ordinal() || refund.getStatus() == CommonEnum.RefundStatusEnum.WAIT_RETURN_OF_MONEY.ordinal())
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            para.put("realMoney", money);
            status = refundDao.updateRefund(para);
            msg = status == 1 ? "修改成功" : "修改失败";
            if (status == 1)
            {
                Map<String, Object> teackPara = new HashMap<String, Object>();
                teackPara.put("refundId", id);
                teackPara.put("step", 1);
                List<OrderProductRefundTeackEntity> teas = refundDao.findOrderProductRefundTeack(teackPara);
                OrderProductRefundTeackEntity te = teas.get(0);
                te.setContent(money + "");
                status = refundDao.updateOrderProductRefundTeack(te);
            }
        }
        else
        {
            msg = "当前退款退货商品状态不允许修改金额";
        }
        result.put("status", status);
        result.put("msg", msg);
        return result;
    }

    @Override
    public Map<String, Object> updateRefund(Map<String, Object> para)
            throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        int status = refundDao.updateRefund(para);
        result.put("status", status);
        return result;
    }

    @Override
    public Map<String, Object> findAllFinancialAffairsCard(Map<String, Object> para)
            throws Exception
    {
        List<Map<String, Object>> reList = refundDao.findAllFinancialAffairsCard(para);
        int total = 0;
        if (reList.size() > 0)
        {
            total = refundDao.countAllFinancialAffairsCard(para);
            for (Map<String, Object> currMap : reList)
            {
                byte type = Byte.valueOf(currMap.get("type") + "").byteValue();
                byte bankType = Byte.valueOf(currMap.get("bankType") + "").byteValue();
                currMap.put("typeStr", type == 1 ? "银行" : "支付宝");
                if (type == 1)
                {
                    // 银行
                    currMap.put("bankTypeStr", CommonEnum.BankTypeEnum.getDescriptionByOrdinal(bankType));
                    // 银行卡号
                    currMap.put("bankCardNumber", currMap.get("cardNumber"));
                    currMap.put("alipayCardNumber", "");
                }
                else
                {
                    currMap.put("bankTypeStr", "");
                    currMap.put("bankCardNumber", "");
                    currMap.put("alipayCardNumber", currMap.get("cardNumber"));
                }
            }
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", total);
        result.put("rows", reList);
        return result;
    }

    @Override
    public int saveFinancialAffairsCard(Map<String, Object> para)
            throws Exception
    {
        return refundDao.saveFinancialAffairsCard(para);
    }

    @Override
    public int deleteFinancialAffairsCardByIds(List<Integer> idList)
            throws Exception
    {
        return refundDao.deleteFinancialAffairsCardByIds(idList);
    }

    @Override
    public List<Map<String, Object>> findAllFinancialAffairsCardForDeal()
            throws Exception
    {
        List<Map<String, Object>> reList = refundDao.findAllFinancialAffairsCard(new HashMap<String, Object>());
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> currMap : reList)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            int id = Integer.parseInt(currMap.get("id") == null ? "0" : currMap.get("id") + "");
            /*byte type = Byte.valueOf(currMap.get("type") + "").byteValue();
            String name = type == 1 ? "银行 " : "支付宝 ";
            name += currMap.get("cardName") + "  ";
            name += currMap.get("cardNumber");*/
            map.put("name", currMap.get("cardName") + "-" + currMap.get("cardNumber"));
            map.put("value", currMap.get("cardName") + "-" + currMap.get("cardNumber"));
            map.put("id", id);
            result.add(map);
        }
        return result;
    }

    @Override
    public Map<String, Object> saveRefundLogisticsInfo(int refundId, String number, String channel, User manager)
            throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();

        KdCompanyEnum company = KdCompanyEnum.getKdCompanyEnumByCompanyName(channel);
        if (company == null)
        {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("errorCode", 1);
            result.put("msg", "物流公司不存在");
            return result;
        }
        // RefundEntity refund = refundDao.findRefundById(refundId);

        Map<String, Object> logistics = refundDao.findRefundLogisticsByRefundId(refundId);
        if (logistics != null)
        {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("errorCode", 2);
            result.put("msg", "该退款退货记录已存在物流信息");
            return result;
        }

        number = number.replaceAll(" ", "");
        para.put("number", number);
        para.put("channel", channel);
        para.put("orderProductRefundId", refundId);
        int status = refundDao.saveRefundLogistics(para);

        if (status != 1)
        {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("errorCode", 0);
            result.put("msg", "未知错误");
            return result;
        }

        // 更新退款退货记录的 status 为待退款 ，该值可能已经被app修改，这里再修改一次也无妨，以防万一
        Map<String, Object> rePara = new HashMap<String, Object>();
        rePara.put("status", 3);
        rePara.put("id", refundId);
        refundDao.updateRefund(rePara);

        // 向快递100发起订阅 先查看数据库中有没有改物流单号信息，有的话则复制插入
        Map<String, Object> lMap = new HashMap<String, Object>();
        lMap.put("number", para.get("number"));
        lMap.put("channel", para.get("channel"));
        lMap.put("orderProductRefundId", refundId);
        List<Map<String, Object>> rList = refundDao.findRefundLogisticsByPara(lMap);
        if (rList.size() < 1)
        {
            // 订阅
            Map<String, String> sendMap = new HashMap<String, String>();
            sendMap.put("id", refundId + "");
            sendMap.put("company", channel);
            sendMap.put("number", number);
            log.info("退款退货  商品物流信息，开始向快递100发起订阅，退款退货ID：" + refundId);
            boolean isSuccess = kd100Service.refundSend(sendMap);
            if (isSuccess)
            {
                log.info("退款退货  向快递100发起订阅成功，退款退货ID：" + refundId);
            }
        }

        // 插入 发货记录
        OrderProductRefundTeackEntity teack = new OrderProductRefundTeackEntity();
        teack.setOrderProductRefundId(refundId);
        teack.setManagerId(manager == null ? 0 : manager.getId());
        teack.setRemark("");
        teack.setContent(channel + ";" + number);
        teack.setStep((byte)2);
        refundDao.saveOrderProductRefundTeack(teack);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", 1);
        return result;
    }

    @Override
    public Map<String, Object> confirmGoods(int id, User manager, String remark)
            throws Exception
    {
        RefundEntity refund = refundDao.findRefundById(id);
        if (refund == null || refund.getType() == 1)
        {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "退款退货信息错误");
            return result;
        }
        Map<String, Object> logistics = refundDao.findRefundLogisticsByRefundId(id);
        if (logistics == null)
        {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "物流信息不存在");
            return result;
        }
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("isReceive", 1);
        para.put("refundId", id);
        int status = refundDao.updateRefundLogistics(para);

        OrderProductRefundTeackEntity teack = new OrderProductRefundTeackEntity();
        teack.setOrderProductRefundId(refund.getId());
        teack.setManagerId(manager.getId());
        teack.setRemark(remark);
        teack.setContent("确认收货");
        teack.setStep((byte)5);
        refundDao.saveOrderProductRefundTeack(teack);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", status);
        result.put("msg", "保存成功");
        if (status != 1)
        {
            result.put("msg", "保存失败");
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getAdminBankInfoCode()
            throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        List<Map<String, Object>> reList = refundDao.findAllFinancialAffairsCard(para);
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> map : reList)
        {
            Map<String, Object> tmp = new HashMap<String, Object>();
            tmp.put("code", map.get("id"));
            tmp.put("text", map.get("cardName")
                    + ((Integer.valueOf(map.get("type") + "") == 1 ? "-" + CommonEnum.BankTypeEnum.getDescriptionByOrdinal((int)map.get("bankType")) + "-" : "-支付宝-"))
                    + map.get("cardNumber"));
            result.add(tmp);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> findAllFinancialAffairsCardById(int transferAccount)
            throws Exception
    {
        return refundDao.findAllFinancialAffairsCardById(transferAccount);
    }

    @Override
    public OrderProductRefundTeackEntity findRefundTeackByRefundIdAndStep(int refundId, int step)
            throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("refundId", refundId);
        para.put("step", step);
        List<OrderProductRefundTeackEntity> reList = refundDao.findOrderProductRefundTeack(para);
        if (reList.size() > 0)
        {
            return reList.get(0);
        }
        return null;
    }

    @Override
    public Map<String, Object> findRefundCardInfo(int accountCardId)
            throws Exception
    {
        Map<String, Object> cardInfo = refundDao.findAccountCardById(accountCardId);
        return cardInfo;
    }

    /** 财务打款账户选择 */
    private int getTargetFinancialAffairsCardId(int appChannel, String appVersion, int payChannel, int orderType, float realPrice)
    {
        int targetFinancialAffairsCardId = 4; // 默认打款账户
        double version = 0;
        if (appVersion != null && !"".equals(appVersion))
        {
            version = Double.valueOf(appVersion);
        }

        if (payChannel == OrderEnum.PAY_CHANNEL.UNION.getCode())
        {
            targetFinancialAffairsCardId = 8;
        }
        else if (payChannel == OrderEnum.PAY_CHANNEL.ALIPAY.getCode())
        {
            targetFinancialAffairsCardId = 5; // 萧剑
            if (orderType == OrderEnum.ORDER_TYPE.GEGEJIA.getCode())
            {
                if (version == 0)
                {
                    // 移动网站
                    if (realPrice <= 0.1f)
                    {
                        targetFinancialAffairsCardId = 5;
                    }
                    else
                    {
                        targetFinancialAffairsCardId = 14; // 左岸城堡国际支付宝
                    }
                }
                else if (version >= 1.8)
                {
                    if(realPrice <= 0.1f)
                    {
                        targetFinancialAffairsCardId = 13; // 左岸城堡国内支付宝
                    }
                    else
                    {
                        targetFinancialAffairsCardId = 14; // 左岸城堡国际支付宝
                    }
                }
            }
            else if (orderType == OrderEnum.ORDER_TYPE.GEGETUAN.getCode() || orderType == OrderEnum.ORDER_TYPE.GEGETUAN_QQG.getCode())
            {
                targetFinancialAffairsCardId = 13; // 左岸城堡国内支付宝
            }

        }
        else if (payChannel == OrderEnum.PAY_CHANNEL.WEIXIN.getCode())
        {
            if (orderType == OrderEnum.ORDER_TYPE.GEGEJIA.getCode())
            {
                if (version == 0)
                {
                    if (appChannel == 28) // 左岸城堡 移动网站
                    {
                        targetFinancialAffairsCardId = 18;
                    }
                    else if (appChannel == 29) // 燕网 移动网站
                    {
                        targetFinancialAffairsCardId = 19;
                    }
                    else
                    {
                        targetFinancialAffairsCardId = 10;
                    }
                }
                else
                {
                    if (version >= 2.0)
                    {
                        if (appChannel == 19 || appChannel == 25)
                        {
                            targetFinancialAffairsCardId = 11;
                        }
                        else if (appChannel == 20)
                        {
                            targetFinancialAffairsCardId = 12;
                        }
                        else
                        {
                            targetFinancialAffairsCardId = 9;
                        }
                    }
                    else if (version >= 1.7)
                    {
                        if (appChannel == 19)
                        {
                            targetFinancialAffairsCardId = 11;
                        }
                        else if (appChannel == 20)
                        {
                            targetFinancialAffairsCardId = 12;
                        }
                        else
                        {
                            targetFinancialAffairsCardId = 9;
                        }
                    }
                    else if (version >= 1.6)
                    {
                        targetFinancialAffairsCardId = 9;
                    }
                    else
                    {
                        targetFinancialAffairsCardId = 7;
                    }
                }
            }
            else if (orderType == OrderEnum.ORDER_TYPE.GEGETUAN.getCode() || orderType == OrderEnum.ORDER_TYPE.GEGETUAN_QQG.getCode())
            {
                if (appChannel == 22)
                {
                    targetFinancialAffairsCardId = 15;
                }
                else
                {
                    targetFinancialAffairsCardId = 16;
                }
            }
            else if (orderType == OrderEnum.ORDER_TYPE.HUANQIUBUSHOU.getCode())
            {
                targetFinancialAffairsCardId = 18;
            }
            else if (orderType == OrderEnum.ORDER_TYPE.YANWANG.getCode())
            {
                targetFinancialAffairsCardId = 19;
            }

        }
        return targetFinancialAffairsCardId;
    }

    @Override
    public Map<String, Object> saveRefund(RefundEntity refund)
            throws Exception
    {
        List<Integer> statusList = new ArrayList<Integer>();
        statusList.add(CommonEnum.RefundStatusEnum.APPLY.ordinal());
        statusList.add(CommonEnum.RefundStatusEnum.WAIT_RETURN_OF_GOODS.ordinal());
        statusList.add(CommonEnum.RefundStatusEnum.WAIT_RETURN_OF_MONEY.ordinal());
        statusList.add(CommonEnum.RefundStatusEnum.SUCCESS.ordinal());
        statusList.add(CommonEnum.RefundStatusEnum.CLOSE.ordinal());
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("orderProductId", refund.getOrderProductId());
        para.put("statusList", statusList);
        List<RefundEntity> res = refundDao.findAllRefundByPara(para);
        if (res.size() > 0)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", 0);
            map.put("msg", "保存失败，该订单商品已经申请了退款退货。");
            return map;
        }
        OrderEntity oe = orderDao.findOrderById(refund.getOrderId());
        if (refund.getRefundPayType() == RefundEnum.REFUND_PAY_TYPE.RETURN_BACK.getCode())
        {
            if (oe.getPayChannel() == OrderEnum.PAY_CHANNEL.UNION.getCode())
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("status", 0);
                map.put("msg", "保存失败，银联支付订单不支持原路返回的退款方式！");
                return map;
            }
        }

        // 财务打款账户选择
        int targetFinancialAffairsCardId = getTargetFinancialAffairsCardId(Integer.valueOf(oe.getAppChannel()), oe.getAppVersion(), oe.getPayChannel(), oe.getType(), oe.getRealPrice());

        refund.setFinancialAffairsCardId(targetFinancialAffairsCardId);

        Map<String, Object> result = new HashMap<String, Object>();
        int status = refundDao.saveRefund(refund);
        if (status == 1)
        {
            // 新增订单退款退货后要冻结相应订单，防止后台发货
            if (orderDao.updateOrderFreeze(refund.getOrderId(), 1) == 1)
            {
                Map<String, Object> freezeMap = orderDao.findOrderFreezeByOrderId(refund.getOrderId());
                if (freezeMap == null)
                {
                    orderDao.insertOrderFreeze(refund.getOrderId());
                }
                else
                {
                    // 更新订单冻结记录
                    orderDao.updateOrderFreezeRecord(Integer.parseInt(freezeMap.get("id") + ""));
                }
            }
        }
        result.put("status", status);
        result.put("msg", status == 1 ? "保存成功" : "保存失败");
        if (status == 1)
        {
            logService.loggerInsert("新建退款退货订单",CommonEnum.LogLevelEnum.LEVEL_ONE.ordinal(),CommonEnum.BusinessTypeEnum.ORDER_MANAGEMENT.ordinal(),CommonEnum.OperationTypeEnum.HAND_CREATE_REFUND_ORDER.ordinal(),refund);
        }
        return result;
    }

    @Override
    public String findGeGeJiaCardByRefundId(int refundId, int type)
            throws Exception
    {
        OrderProductRefundTeackEntity oprte = null;
        if (type == 1)
        {
            oprte = findRefundTeackByRefundIdAndStep(refundId, 2);
        }
        else
        {
            oprte = findRefundTeackByRefundIdAndStep(refundId, 4);
        }
        if (oprte == null)
        {
            return "";
        }
        return oprte.getContent();
    }

    @Override
    public Map<String, Object> saveOrUpdateFinanceShare(Map<String, Object> para)
            throws Exception
    {
        int id = Integer.parseInt(para.get("id") == null ? "0" : para.get("id") + "");
        int status = 0;
        if (id == 0)
        {
            status = refundDao.saveRefundProportionByRefundId(para);
            id = Integer.parseInt(para.get("id") == null ? "0" : para.get("id") + "");
        }
        else
        {
            status = refundDao.updateRefundProportionByRefundId(para);
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", status);
        result.put("msg", status == 1 ? "保存成功" : "保存失败");
        result.put("id", id + "");
        return result;
    }

    @Override
    public List<MwebAutomaticRefundEntity> findMwebAutomaticRefund(String startTime, String endTime)
            throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        return refundDao.findMwebAutomaticRefund(param);
    }

    @Override
    public ResultEntity dealRefundImmediately(int refundId, int cardId)
        throws Exception
    {
        RefundEntity refund = refundDao.findRefundById(refundId);
        if (refund == null)
        {
            return ResultEntity.getFailResult("退款记录不存在");
        }
        
        //退款成功、退款关闭、退款取消不能重复操作
        if (refund.getStatus() == CommonEnum.RefundStatusEnum.SUCCESS.ordinal() || refund.getStatus() == CommonEnum.RefundStatusEnum.CLOSE.ordinal()
            || refund.getStatus() == CommonEnum.RefundStatusEnum.CANCEL.ordinal())
        {
            return ResultEntity.getFailResult(CommonEnum.RefundStatusEnum.getDescriptionByOrdinal(refund.getStatus()));
        }
        
        OrderEntity order = orderDao.findOrderById(refund.getOrderId());
        if (order == null)
        {
            return ResultEntity.getFailResult("退款订单不存在");
        }
        
        OrderPayEntity orderPay = orderDao.findOrderPayByOrderIdAndPayType(order.getId(), order.getPayChannel());
        if (orderPay == null || StringUtils.isEmpty(orderPay.getPayTid()))
        {
            return ResultEntity.getFailResult("打款失败，未找到订单支付信息");
        }
        String payTid = orderPay.getPayTid();
        String payRemark = orderPay.getPayMark();
        
        if (StringUtils.isEmpty(payTid) || StringUtils.isEmpty(payRemark))
        {
            return ResultEntity.getFailResult("打款失败，未找到订单支付信息");
        }
        
        //批量付款订单，要取同一批次的
        List<Integer> orderIds = orderDao.findOrderIdsByPayTidAndPayType(payTid,order.getPayChannel());
        float totalPriceFloat = 0.0f;
        for (Integer id : orderIds)
        {
            OrderEntity oe = orderDao.findOrderById(id);
            totalPriceFloat += oe.getRealPrice();
        }
        int totalFee = Math.round(totalPriceFloat * 100);
        int refundFee = Math.round(Float.parseFloat(refund.getRealMoney() * 100 + ""));
        
        if (refundFee > totalFee)
        {
            return ResultEntity.getFailResult("退款金额不得大于订单实付金额");
        }
        
        FinancialAffairsCardEntity face = refundDao.findFinancialAffairsCardById(cardId);
        if (face == null)
        {
            return ResultEntity.getFailResult("您选择的打款账户不存在");
        }

        boolean isSuccess = false;
        // 退款账户现在没法根据其他条件判断，只能通过id写死判断，以后要优化
        if (cardId == 14)
        {
            //国际支付宝
            GlobalAlipayRefund alipayRefund = new GlobalAlipayRefund(refund.getId() + "", payRemark, decimalFormat.format(refund.getRealMoney()));
            if (order.getNumber().endsWith(CommonConstant.ORDER_SUFFIX_WAP))
            {
                alipayRefund.setProduct_code("NEW_OVERSEAS_SELLER");
            }
            else if (order.getNumber().endsWith(CommonConstant.ORDER_SUFFIX_APP))
            {
                alipayRefund.setProduct_code("NEW_WAP_OVERSEAS_SELLER");
            }
            alipayRefund.setSplit_fund_info("[{'transOut':'" + AlipayConfig.GLOBAL_SPLIT_PARTNER + "','amount':'" + decimalFormat.format(refund.getRealMoney()) + "','currency':'CNY','desc':'国际支付宝订单退款'}]");
            JSONObject jsonObject = ApiCallService.globalAlipayRefund(alipayRefund);
            log.info("支付宝立即退款请求结果：" + jsonObject);
            if ("T".equals(jsonObject.getString("is_success")))
            {
                isSuccess = true;
            }
            else
            {
                log.error(String.format("支付宝立即打款失败，订单id=%d，订单编号=%s，payTid=%s，订单总金额=%d(分)，退款金额=%d(分)，接口回调消息：%s", order.getId(), order.getNumber(), payTid, totalFee, refundFee, jsonObject.toString()));
                String errorMessage = jsonObject.getString("error");
                if ("PURCHASE_TRADE_NOT_EXIST".equals(errorMessage))
                {
                    return ResultEntity.getFailResult("打款失败，交易不存在");
                }
                else if ("REFUNDMENT_VALID_DATE_EXCEED".equals(errorMessage))
                {
                    return ResultEntity.getFailResult("打款失败，退款超过退款周期");
                }
                else if ("TRADE_HAS_CLOSE".equals(errorMessage))
                {
                    return ResultEntity.getFailResult("打款失败，交易已关闭无法退款");
                }
                else if ("RETURN_AMOUNT_EXCEED".equals(errorMessage))
                {
                    return ResultEntity.getFailResult("打款失败，退款金额超额");
                }
                else
                {
                    return ResultEntity.getFailResult("打款失败，(error=" + errorMessage + ")");
                }
            }
        }
        else if (cardId == 9 || cardId == 11 || cardId == 18)
        {
            //微信
            RefundEnum.REFUND_ACCOUNT refundAccount = RefundEnum.REFUND_ACCOUNT.getEnum(face.getCardNumber());
            if (refundAccount == null)
            {
                log.info("当前账户不支持立即打款, financial_affairs_card=" + cardId);
                return ResultEntity.getFailResult("当前账户不支持立即打款");
            }
            
            String mchid = refundAccount.getMchid();
            String appid = refundAccount.getAppid();
            String key = refundAccount.getKey();
            String cert = refundAccount.getCert();
            
            WeiChatRefund refundParam = new WeiChatRefund(appid, mchid, payTid, payRemark, refund.getId() + "", totalFee, refundFee);
            //JSONObject jsonObject = CommonHttpClient.sendXmlRefundHTTP(CommonConstant.WECHAT_REFUND_URL, mchid, cert, CommonUtil.objectToXml(refundParam));
            JSONObject jsonObject = ApiCallService.weChatRefund(refundParam, key, cert);
            log.info("微信立即退款请求结果：" + jsonObject);
            if ("SUCCESS".equals(jsonObject.getString("return_code")) && "SUCCESS".equals(jsonObject.getString("result_code")))
            {
               isSuccess = true;
            }
            else
            {
                log.error(String.format("微信立即打款失败，订单id=%d，订单编号=%s，payTid=%s，订单总金额=%d(分)，退款金额=%d(分)，接口回调消息：%s", order.getId(), order.getNumber(), payTid, totalFee, refundFee, jsonObject.toString()));
                return ResultEntity.getFailResult("退款失败！！！（err_code=" + jsonObject.getString("err_code") + ",err_code_des=" + jsonObject.getString("err_code_des") + "）");
            }
        }
        else
        {
            return ResultEntity.getFailResult("该帐号暂不支持直接打款");
        }

        if (isSuccess)
        {
            Map<String, Object> para = new HashMap<>();
            para.put("checkTime", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
            para.put("id", refundId);
            para.put("status", CommonEnum.RefundStatusEnum.SUCCESS.ordinal());
            if (refundDao.updateRefund(para) == 0)
            {
                throw new ServiceException();
            }
            
            // 插入 更新 记录
            OrderProductRefundTeackEntity teack = new OrderProductRefundTeackEntity();
            teack.setOrderProductRefundId(refund.getId());
            teack.setManagerId(commonService.getCurrentUserId());
            teack.setContent(face.getCardInfo());
            teack.setStep((byte)4);
            teack.setRemark("直接退款");
            if (refundDao.saveOrderProductRefundTeack(teack) == 0)
            {
                throw new ServiceException();
            }
            return ResultEntity.getSuccessResult("退款成功");
        }
        else
        {
            return ResultEntity.getSuccessResult("退款失败");
        }
    }
}
