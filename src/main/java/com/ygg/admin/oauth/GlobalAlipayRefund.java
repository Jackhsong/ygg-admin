package com.ygg.admin.oauth;

import com.ygg.admin.sdk.alipay.config.AlipayConfig;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 国际支付宝退款
 *
 * @author xiongl
 */
public class GlobalAlipayRefund
{
    
    public GlobalAlipayRefund()
    {
    }
    
    public GlobalAlipayRefund(String out_return_no, String out_trade_no, String return_rmb_amount)
    {
        this.out_return_no = out_return_no;
        this.out_trade_no = out_trade_no;
        this.return_rmb_amount = return_rmb_amount;
    }
    
    /**
     * 必填：接口名称
     */
    private String service = "forex_refund";
    
    /**
     * 必填：签约的支付宝账号对应的支付宝唯一用户号。以 2088 开头的 16 位纯数字组成。
     */
    private String partner = AlipayConfig.GLOBAL_PARTNER;
    
    /**
     * 必填：商户网站使用的编码格式， 如 utf-8、 gbk、 gb2312 等。
     */
    private String _input_charset = AlipayConfig.GLOBAL_INPUT_CHARSET;
    
    /**
     * 必填：DSA、 RSA、 MD5 三个值可选，必须大写,RSA,DSA 需要联系支付宝技术支持单独配置。
     */
    private String sign_type;
    
    /**
     * 必填：签名
     */
    private String sign;
    
    /**
     * 选填：支付宝服务器主动通知商户网站里指定的页面 http 路径。
     */
    private String notify_url;
    
    /**
     * 必填：合作伙伴退款号（确保在合作伙伴系统中唯一）,并且out_return_no 的生成策略必须不能与 out_trade_no的生成策略有交集
     */
    private String out_return_no;
    
    /**
     * 必填：退款对应的交易的外部交易号
     */
    private String out_trade_no;
    
    /**
     * 跟return_rmb_amount二选一：申请的退款外币金额，格式必须符合相应币种的要求，、
     * 比如：日元为整数，人民币最多２位小数。同时必须是
     * 0.01-100000000.00 之间的数值（小数点后最多 2 位）
     *
     */
    private String return_amount;
    
    /**
     * 跟return_amount二选一：申请的人民币退款金额，格式必须符合相应币种要求，
     * 最多保留 2 位小数，同时必须是 0.01-100000000.00 之间的数值
     *
     */
    private String return_rmb_amount;
    
    /**
     * 选填，币种（非人民币金额退款不可空，且必须与创建交易时的币种一致。）
     */
    private String currency = "USD";
    
    /**
     * 选填，退款时间，格式：YYYYMMDDHHMMSS
     */
    private String gmt_return;
    
    /**
     * 选填，买家申请退款原因
     */
    private String reason;
    
    /**
     * 必填，用来区分是哪种业务类型的下单。 PC创建的交易使用NEW_OVERSEAS_SELLER，
     * 无线创建的交易使用NEW_WAP_OVERSEAS_SELLER
     */
    private String product_code;
    
    /**
     * 选填，分账信息
     */
    private String split_fund_info;
    
    public String getService()
    {
        return service;
    }
    
    public void setService(String service)
    {
        this.service = service;
    }
    
    public String getPartner()
    {
        return partner;
    }
    
    public void setPartner(String partner)
    {
        this.partner = partner;
    }
    
    public String get_input_charset()
    {
        return _input_charset;
    }
    
    public void set_input_charset(String _input_charset)
    {
        this._input_charset = _input_charset;
    }
    
    public String getSign_type()
    {
        return sign_type;
    }
    
    public void setSign_type(String sign_type)
    {
        this.sign_type = sign_type;
    }
    
    public String getSign()
    {
        return sign;
    }
    
    public void setSign(String sign)
    {
        this.sign = sign;
    }
    
    public String getNotify_url()
    {
        return notify_url;
    }
    
    public void setNotify_url(String notify_url)
    {
        this.notify_url = notify_url;
    }
    
    public String getOut_return_no()
    {
        return out_return_no;
    }
    
    public void setOut_return_no(String out_return_no)
    {
        this.out_return_no = out_return_no;
    }
    
    public String getOut_trade_no()
    {
        return out_trade_no;
    }
    
    public void setOut_trade_no(String out_trade_no)
    {
        this.out_trade_no = out_trade_no;
    }
    
    public String getReturn_amount()
    {
        return return_amount;
    }
    
    public void setReturn_amount(String return_amount)
    {
        this.return_amount = return_amount;
    }
    
    public String getReturn_rmb_amount()
    {
        return return_rmb_amount;
    }
    
    public void setReturn_rmb_amount(String return_rmb_amount)
    {
        this.return_rmb_amount = return_rmb_amount;
    }
    
    public String getCurrency()
    {
        return currency;
    }
    
    public void setCurrency(String currency)
    {
        this.currency = currency;
    }
    
    public String getGmt_return()
    {
        return gmt_return;
    }
    
    public void setGmt_return(String gmt_return)
    {
        this.gmt_return = gmt_return;
    }
    
    public String getReason()
    {
        return reason;
    }
    
    public void setReason(String reason)
    {
        this.reason = reason;
    }
    
    public String getProduct_code()
    {
        return product_code;
    }
    
    public void setProduct_code(String product_code)
    {
        this.product_code = product_code;
    }
    
    public String getSplit_fund_info()
    {
        return split_fund_info;
    }
    
    public void setSplit_fund_info(String split_fund_info)
    {
        this.split_fund_info = split_fund_info;
    }
    
    public Map<String, String> getParamters()
        throws IllegalAccessException
    {
        Map<String, String> resultMap = new HashMap<>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field f : fields)
        {
            f.setAccessible(true);
            if (f.get(this) != null && f.get(this) != "")
            {
                resultMap.put(f.getName(), f.get(this).toString());
            }
        }
        return resultMap;
    }
}
