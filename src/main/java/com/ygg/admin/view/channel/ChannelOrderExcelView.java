package com.ygg.admin.view.channel;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-4-14
 */
public class ChannelOrderExcelView implements Serializable
{

    private static final long serialVersionUID = -8727264891115881262L;
    public static final Map<String,String> headerMapper = new LinkedHashMap();
    static {
        headerMapper.put("渠道名", "channel");   //必填
        headerMapper.put("订单编号", "number");   //必填
        headerMapper.put("订单状态", "orderStatus");  //必填
        headerMapper.put("创建时间", "order_create_time");  //必填
        headerMapper.put("付款日期", "order_pay_time"); //必填
        headerMapper.put("收货人", "receiver");  //必填
        headerMapper.put("身份证号码", "id_card");
        headerMapper.put("收货地址", "receive_address");
        headerMapper.put("省", "province"); //必填
        headerMapper.put("市", "city"); //必填
        headerMapper.put("区", "district"); //必填
        headerMapper.put("详细地址", "detail_address");  //必填
        headerMapper.put("联系电话", "phone");  //必填
        headerMapper.put("商品编号", "code");  //必填
        headerMapper.put("商品名称", "productName");  //必填
        headerMapper.put("件数", "count");  //必填
        headerMapper.put("单价", "price");
        headerMapper.put("总价", "product_total_price");   //必填
        headerMapper.put("运费", "freight");
        headerMapper.put("订单总价", "order_total_price");
        headerMapper.put("买家备注", "buyer_remark");
        headerMapper.put("卖家备注", "seller_remark");
        headerMapper.put("发货时间", "deliver_time");
        headerMapper.put("物流公司", "logisticChannel");
        headerMapper.put("物流单号", "logisticNumber");
        headerMapper.put("冻结状态", "isFreezeString");
        headerMapper.put("发件人", "sender");
        headerMapper.put("联系方式", "contact_way");
        headerMapper.put("支付公司", "payCompany");
        headerMapper.put("支付交易号", "pay_number");
        headerMapper.put("邮政编码", "post_code");
        headerMapper.put("总金额", "order_real_price");   //必填
        // .put(""logistic"")
    }
    
    private Long order_id;
    
    private Long product_id;
    
    private Long logistic_id;

    private Long address_id;

    /**
     * channel_order start
     */
    // 第三方平台
    private String channel = "";
    
    private Long channel_id = 0l;
    
    // 订单编号
    private String number = "";
    
    // 订单状态
    private String orderStatus = "";
    
    // 1：未付款，2：待发货，3：已发货，4：交易成功，5：用户取消（待退款团购），6：超时取消（已退款团购），7：团购进行中(团购)
    private Integer status = 2;
    
    // 运费
    private Float freight = 0f;
    
    // 总价
    private Float order_total_price = 0f;
    
    // 订单总价
    private Float order_real_price = -1f;
    
    // 创建时间
    private String order_create_time = "0000-00-00 00:00:00";
    
    // 付款时间
    private String order_pay_time = "0000-00-00 00:00:00";
    
    // 发货时间
    private String deliver_time = "0000-00-00 00:00:00";
    
    // 收货人
    private String receiver = "";
    
    // 身份证
    private String id_card = "";
    
    // 联系电话
    private String phone = "";
    
    // 发件人
    private String sender = "";
    
    // 联系方式
    private String contact_way = "";
    
    // 买家备注
    private String buyer_remark = "";
    
    // 卖家备注
    private String seller_remark = "";
    
    // 支付公司
    private String payCompany = "";
    
    // 付款渠道；1：银联，2：支付宝，3：微信
    private Integer pay_channel = 2;
    
    // 支付交易号
    private String pay_number = "";
    
    // 冻结状态
    private String isFreezeString = "";
    
    // 冻结状态；0：未冻结，1：已冻结，2：已解冻
    private Integer is_freeeze = 0;
    

    /**
     * channel_order End
     */
    
    /**
     * channel_order_logistic start
     */
    // 收货地址
    private String receive_address = "";
    
    // 省
    private String province = "";
    
    // 市
    private String city = "";
    
    // 区
    private String district = "";
    
    // 详细地址
    private String detail_address = "";
    
    // 物流公司
    private String logisticChannel = "";
    
    // 物流单号
    private String logisticNumber = "";
    
    // 邮政编码
    private String post_code = "";
    
    /**
     * channel_order_logistic end
     */
    
    /**
     * channel_order_product start
     */
    
    // 商品编号
    private String code = "";
    
    // 商品名称
    private String productName = "";
    
    // 件数
    private Integer count = 0;
    
    // 单价
    private Float price = 0f;
    
    // 总价
    private Float product_total_price = -1f;
    
    // 仓库名 1：杭州仓，2：香港笨鸟仓
    private Integer warehouseType;
    
    private String warehouse;
    
    /**
     * channel_order_product end
     */
    
    public static Map getHeaderMapper()
    {
        return headerMapper;
    }
    
    public String getChannel()
    {
        return channel;
    }
    
    public void setChannel(String channel)
    {
        this.channel = channel;
    }
    
    public Long getOrder_id()
    {
        return order_id;
    }
    
    public void setOrder_id(Long order_id)
    {
        this.order_id = order_id;
    }
    
    public Long getProduct_id()
    {
        return product_id;
    }
    
    public void setProduct_id(Long product_id)
    {
        this.product_id = product_id;
    }
    
    public Long getLogistic_id()
    {
        return logistic_id;
    }
    
    public void setLogistic_id(Long logistic_id)
    {
        this.logistic_id = logistic_id;
    }
    
    public Long getChannel_id()
    {
        return channel_id;
    }
    
    public void setChannel_id(Long channel_id)
    {
        this.channel_id = channel_id;
    }
    
    public String getNumber()
    {
        return number;
    }
    
    public void setNumber(String number)
    {
        this.number = number;
    }
    
    public String getOrderStatus()
    {
        return orderStatus;
    }
    
    public void setOrderStatus(String orderStatus)
    {
        this.orderStatus = orderStatus;
    }
    
    public Integer getStatus()
    {
        return status;
    }
    
    public void setStatus(Integer status)
    {
        this.status = status;
    }
    
    public Float getFreight()
    {
        return freight;
    }
    
    public void setFreight(Float freight)
    {
        this.freight = freight;
    }
    
    public Float getOrder_total_price()
    {
        return order_total_price;
    }
    
    public void setOrder_total_price(Float order_total_price)
    {
        this.order_total_price = order_total_price;
    }
    
    public Float getOrder_real_price()
    {
        return order_real_price;
    }
    
    public void setOrder_real_price(Float order_real_price)
    {
        this.order_real_price = order_real_price;
    }
    
    public String getOrder_create_time()
    {
        return order_create_time;
    }
    
    public void setOrder_create_time(String order_create_time)
    {
        this.order_create_time = order_create_time;
    }
    
    public String getOrder_pay_time()
    {
        return order_pay_time;
    }
    
    public void setOrder_pay_time(String order_pay_time)
    {
        this.order_pay_time = order_pay_time;
    }
    
    public String getDeliver_time()
    {
        return deliver_time;
    }
    
    public void setDeliver_time(String deliver_time)
    {
        this.deliver_time = deliver_time;
    }
    
    public String getReceiver()
    {
        return receiver;
    }
    
    public void setReceiver(String receiver)
    {
        this.receiver = receiver;
    }
    
    public String getId_card()
    {
        return id_card;
    }
    
    public void setId_card(String id_card)
    {
        this.id_card = id_card;
    }
    
    public String getPhone()
    {
        return phone;
    }
    
    public void setPhone(String phone)
    {
        this.phone = phone;
    }
    
    public String getSender()
    {
        return sender;
    }
    
    public void setSender(String sender)
    {
        this.sender = sender;
    }
    
    public String getContact_way()
    {
        return contact_way;
    }
    
    public void setContact_way(String contact_way)
    {
        this.contact_way = contact_way;
    }
    
    public String getBuyer_remark()
    {
        return buyer_remark;
    }
    
    public void setBuyer_remark(String buyer_remark)
    {
        this.buyer_remark = buyer_remark;
    }
    
    public String getSeller_remark()
    {
        return seller_remark;
    }
    
    public void setSeller_remark(String seller_remark)
    {
        this.seller_remark = seller_remark;
    }
    
    public String getPayCompany()
    {
        return payCompany;
    }
    
    public void setPayCompany(String payCompany)
    {
        this.payCompany = payCompany;
    }
    
    public Integer getPay_channel()
    {
        return pay_channel;
    }
    
    public void setPay_channel(Integer pay_channel)
    {
        this.pay_channel = pay_channel;
    }
    
    public String getPay_number()
    {
        return pay_number;
    }
    
    public void setPay_number(String pay_number)
    {
        this.pay_number = pay_number;
    }
    
    public String getIsFreezeString()
    {
        return isFreezeString;
    }
    
    public void setIsFreezeString(String isFreezeString)
    {
        this.isFreezeString = isFreezeString;
    }
    
    public Integer getIs_freeeze()
    {
        return is_freeeze;
    }
    
    public void setIs_freeeze(Integer is_freeeze)
    {
        this.is_freeeze = is_freeeze;
    }
    
    public String getReceive_address()
    {
        return receive_address;
    }
    
    public void setReceive_address(String receive_address)
    {
        this.receive_address = receive_address;
    }
    
    public String getProvince()
    {
        return province;
    }
    
    public void setProvince(String province)
    {
        this.province = province;
    }
    
    public String getCity()
    {
        return city;
    }
    
    public void setCity(String city)
    {
        this.city = city;
    }
    
    public String getDistrict()
    {
        return district;
    }
    
    public void setDistrict(String district)
    {
        this.district = district;
    }
    
    public String getLogisticChannel()
    {
        return logisticChannel;
    }
    
    public void setLogisticChannel(String logisticChannel)
    {
        this.logisticChannel = logisticChannel;
    }
    
    public String getLogisticNumber()
    {
        return logisticNumber;
    }
    
    public void setLogisticNumber(String logisticNumber)
    {
        this.logisticNumber = logisticNumber;
    }
    
    public String getPost_code()
    {
        return post_code;
    }
    
    public void setPost_code(String post_code)
    {
        this.post_code = post_code;
    }
    
    public String getCode()
    {
        return code;
    }
    
    public void setCode(String code)
    {
        this.code = code;
    }
    
    public String getProductName()
    {
        return productName;
    }
    
    public void setProductName(String productName)
    {
        this.productName = productName;
    }
    
    public Integer getCount()
    {
        return count;
    }
    
    public void setCount(Integer count)
    {
        this.count = count;
    }
    
    public Float getPrice()
    {
        return price;
    }
    
    public void setPrice(Float price)
    {
        this.price = price;
    }
    
    public String getDetail_address()
    {
        return detail_address;
    }
    
    public void setDetail_address(String detail_address)
    {
        this.detail_address = detail_address;
    }
    
    public Float getProduct_total_price()
    {
        return product_total_price;
    }
    
    public void setProduct_total_price(Float product_total_price)
    {
        this.product_total_price = product_total_price;
    }
    
    public Integer getWarehouseType()
    {
        return warehouseType;
    }
    
    public void setWarehouseType(Integer warehouseType)
    {
        this.warehouseType = warehouseType;
    }
    
    public String getWarehouse()
    {
        return warehouse;
    }
    
    public void setWarehouse(String warehouse)
    {
        this.warehouse = warehouse;
    }

    public Long getAddress_id() {
        return address_id;
    }

    public void setAddress_id(Long address_id) {
        this.address_id = address_id;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }
}
