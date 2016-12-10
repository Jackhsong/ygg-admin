package com.ygg.admin.oauth;

import com.ygg.admin.config.YggAdminProperties;
import com.ygg.admin.util.CommonUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

public class WeiChatRefund
{
    public WeiChatRefund()
    {
        this.nonce_str = CommonUtil.generateUUID();
    }
    
    public WeiChatRefund(String appid, String mchid, String transactionId, String outTradeNo, String outRefundNo, int totalFee, int refundFee)
    {
        this.appid = appid;
        this.mch_id = mchid;
        this.transaction_id = transactionId;
        this.out_trade_no = outTradeNo;
        this.out_refund_no = outRefundNo;
        this.total_fee = totalFee;
        this.refund_fee = refundFee;
        this.nonce_str = CommonUtil.generateUUID();
        this.op_user_id = mchid;
    }
    
    // 公众账号ID
    private String appid;
    
    // 商户号
    private String mch_id;
    
    // 设备号
    private String device_info;
    
    // 随机字符串
    private String nonce_str;
    
    // 签名
    private String sign;
    
    // 商户侧传给微信的订单号
    private String out_trade_no;
    
    // 微信生成的订单号，在支付通知中有返回
    private String transaction_id;
    
    // 商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
    private String out_refund_no;
    
    // 订单总金额
    private int total_fee;
    
    // 退款金额
    private int refund_fee;
    
    // 货币种类
    private String refund_fee_type = "CNY";
    
    // 操作员帐号, 默认为商户号
    private String op_user_id;
    
    public String getAppid()
    {
        return appid;
    }
    
    public void setAppid(String appid)
    {
        this.appid = appid;
    }
    
    public String getMch_id()
    {
        return mch_id;
    }
    
    public void setMch_id(String mch_id)
    {
        this.mch_id = mch_id;
    }
    
    public String getDevice_info()
    {
        return device_info;
    }
    
    public void setDevice_info(String device_info)
    {
        this.device_info = device_info;
    }
    
    public String getNonce_str()
    {
        return nonce_str;
    }
    
    public void setNonce_str(String nonce_str)
    {
        this.nonce_str = nonce_str;
    }
    
    public String getSign()
    {
        return sign;
    }
    
    public void setSign(String sign)
    {
        this.sign = sign;
    }
    
    public String getOut_trade_no()
    {
        return out_trade_no;
    }
    
    public void setOut_trade_no(String out_trade_no)
    {
        this.out_trade_no = out_trade_no;
    }
    
    public String getTransaction_id()
    {
        return transaction_id;
    }
    
    public void setTransaction_id(String transaction_id)
    {
        this.transaction_id = transaction_id;
    }
    
    public String getOut_refund_no()
    {
        return out_refund_no;
    }
    
    public void setOut_refund_no(String out_refund_no)
    {
        this.out_refund_no = out_refund_no;
    }
    
    public int getTotal_fee()
    {
        return total_fee;
    }
    
    public void setTotal_fee(int total_fee)
    {
        this.total_fee = total_fee;
    }
    
    public int getRefund_fee()
    {
        return refund_fee;
    }
    
    public void setRefund_fee(int refund_fee)
    {
        this.refund_fee = refund_fee;
    }
    
    public String getRefund_fee_type()
    {
        return refund_fee_type;
    }
    
    public void setRefund_fee_type(String refund_fee_type)
    {
        this.refund_fee_type = refund_fee_type;
    }
    
    public String getOp_user_id()
    {
        return op_user_id;
    }
    
    public void setOp_user_id(String op_user_id)
    {
        this.op_user_id = op_user_id;
    }
    
    public void createSign(String key)
        throws IllegalAccessException
    {
        ArrayList<String> list = new ArrayList<>();
        Class cls = this.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field f : fields)
        {
            f.setAccessible(true);
            if (f.get(this) != null && f.get(this) != "")
            {
                list.add(f.getName() + "=" + f.get(this) + "&");
                
            }
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++)
        {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "key=" + key;
        this.sign = CommonUtil.md5Encode(result).toUpperCase();
        
    }
    
}
