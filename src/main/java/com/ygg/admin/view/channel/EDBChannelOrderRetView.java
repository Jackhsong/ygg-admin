package com.ygg.admin.view.channel;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

/**
 * @author lorabit
 * @since 16-4- 导入第三方订单 返回的e电宝订单 excel 格式
 */
public class EDBChannelOrderRetView implements Serializable
{
    private static final long serialVersionUID = -7131260151161429365L;


    public static final List<String> displayHeaders = Lists.newArrayList("订单号", "产品条码", "订单状态", "买家id", "买家昵称", "商品名称", "产品规格", "商品单价", "商品数量", "商品总价", "运费", "购买优惠信息", "总金额",
        "买家购买附言", "收货人姓名", "收货地址-省市", "收货地址-街道地址", "邮编", "收货人手机", "收货人电话", "买家选择运送方式", "卖家备忘内容", "订单创建时间", "付款时间", "物流公司", "物流单号", "发货附言", "发票抬头", "电子邮件");

    public static final List<String> headers = Lists.newArrayList("number", "code", "orderStatus", "receiver", "receiver", "productName", "productGuige", "price", "count",
        "product_total_price", "freight", "couponInfo", "order_real_price", "buyer_remark", "receiver", "receiveProvinceAndCity", "receiveAddress", "post_code", "phone", "phone",
        "buyerSelectLogisticWay", "seller_remark", "order_create_time", "order_pay_time", "logisticChannel", "logisticNumber", "send_remark", "invoice_head", "email");

    private String channel;

    private String number;
    
    private String code;
    
    private String orderStatus;
    
    private String receiver;
    
    private String productName;
    
    private String productGuige;
    
    private Float price;
    
    private Integer count;
    
    private Float product_total_price;
    
    private Float freight;
    
    private String couponInfo;
    
    private Float order_real_price;
    
    private String buyer_remark;
    
    private String receiveProvinceAndCity;
    
    private String receiveAddress;
    
    private String post_code;
    
    private String phone;
    
    private String buyerSelectLogisticWay;
    
    private String seller_remark;
    
    private String order_create_time;
    
    private String order_pay_time;
    
    private String logisticChannel;
    
    private String logisticNumber;
    
    private String send_remark;
    
    private String invoice_head;
    
    private String email;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public static List<String> getDisplayHeaders() {
        return displayHeaders;
    }

    public static List<String> getHeaders() {
        return headers;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductGuige() {
        return productGuige;
    }

    public void setProductGuige(String productGuige) {
        this.productGuige = productGuige;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Float getProduct_total_price() {
        return product_total_price;
    }

    public void setProduct_total_price(Float product_total_price) {
        this.product_total_price = product_total_price;
    }

    public Float getFreight() {
        return freight;
    }

    public void setFreight(Float freight) {
        this.freight = freight;
    }

    public String getCouponInfo() {
        return couponInfo;
    }

    public void setCouponInfo(String couponInfo) {
        this.couponInfo = couponInfo;
    }

    public Float getOrder_real_price() {
        return order_real_price;
    }

    public void setOrder_real_price(Float order_real_price) {
        this.order_real_price = order_real_price;
    }

    public String getBuyer_remark() {
        return buyer_remark;
    }

    public void setBuyer_remark(String buyer_remark) {
        this.buyer_remark = buyer_remark;
    }

    public String getReceiveProvinceAndCity() {
        return receiveProvinceAndCity;
    }

    public void setReceiveProvinceAndCity(String receiveProvinceAndCity) {
        this.receiveProvinceAndCity = receiveProvinceAndCity;
    }

    public String getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }

    public String getPost_code() {
        return post_code;
    }

    public void setPost_code(String post_code) {
        this.post_code = post_code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBuyerSelectLogisticWay() {
        return buyerSelectLogisticWay;
    }

    public void setBuyerSelectLogisticWay(String buyerSelectLogisticWay) {
        this.buyerSelectLogisticWay = buyerSelectLogisticWay;
    }

    public String getSeller_remark() {
        return seller_remark;
    }

    public void setSeller_remark(String seller_remark) {
        this.seller_remark = seller_remark;
    }

    public String getOrder_create_time() {
        return order_create_time;
    }

    public void setOrder_create_time(String order_create_time) {
        this.order_create_time = order_create_time;
    }

    public String getOrder_pay_time() {
        return order_pay_time;
    }

    public void setOrder_pay_time(String order_pay_time) {
        this.order_pay_time = order_pay_time;
    }

    public String getLogisticChannel() {
        return logisticChannel;
    }

    public void setLogisticChannel(String logisticChannel) {
        this.logisticChannel = logisticChannel;
    }

    public String getLogisticNumber() {
        return logisticNumber;
    }

    public void setLogisticNumber(String logisticNumber) {
        this.logisticNumber = logisticNumber;
    }

    public String getSend_remark() {
        return send_remark;
    }

    public void setSend_remark(String send_remark) {
        this.send_remark = send_remark;
    }

    public String getInvoice_head() {
        return invoice_head;
    }

    public void setInvoice_head(String invoice_head) {
        this.invoice_head = invoice_head;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
