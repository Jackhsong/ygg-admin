package com.ygg.admin.view.channel;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

/**
 * @author lorabit
 * @since 16-4-15
 */
public class BenBridChannelOrderRetView implements Serializable{

    private static final long serialVersionUID = 4768937416629639262L;


    public static final List<String> displayHeaders = Lists.newArrayList(
            "客户单号", "发货仓库", "服务方式", "商品编码", "数量", "申报价值", "申报价值单位",
            "收货人姓名", "手机号码", "固定电话", "所在省份", "所在城市", "所在区域",
            "详细地址", "邮政编码", "身份证号码", "是否进行身份证验证", "身份证正面URL", "身份证反面URL",
            "支付公司", "支付时间", "支付流水号", "支付金额", "支付金额单位", "是否购买保险", "是否包装加固",
            "发货人", "订单备注", "第三方单号");
    public static final List<String> headers = Lists.newArrayList(
            "number", "send_warehouse", "server_way", "code", "count", "price", "declared_unit",
            "receiver", "phone", "mobile", "province", "city", "district",
            "detail_address", "post_code", "id_card", "isCheckIdCardStr", "id_card_url1", "id_card_url2",
            "payCompany", "order_pay_time", "pay_number", "order_real_price", "pay_unit", "is_buy_finace", "is_pack_reinforce"
            , "seller", "order_remark", "thirdparty_number"
    );

    private String channel;
    private String number;
    private String send_warehouse;
    private String server_way;
    private String code;
    private Integer count;
    private Float price;
    private String declared_unit = "人民币";
    private String receiver;
    private String phone;
    private String mobile;
    private String province;
    private String city;
    private String district;
    private String detail_address;
    private String post_code;
    private String id_card;
    private String isCheckIdCardStr = "Y";
    private String id_card_url1;
    private String id_card_url2;
    private String payCompany;
    private String order_pay_time;
    private String pay_number;
    private Float order_real_price;
    private String pay_unit;
    private String is_buy_finace;
    private String is_pack_reinforce;
    private String seller;
    private String order_remark;
    private String thirdparty_number;

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

    public String getSend_warehouse() {
        return send_warehouse;
    }

    public void setSend_warehouse(String send_warehouse) {
        this.send_warehouse = send_warehouse;
    }

    public String getServer_way() {
        return server_way;
    }

    public void setServer_way(String server_way) {
        this.server_way = server_way;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getDeclared_unit() {
        return declared_unit;
    }

    public void setDeclared_unit(String declared_unit) {
        this.declared_unit = declared_unit;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDetail_address() {
        return detail_address;
    }

    public void setDetail_address(String detail_address) {
        this.detail_address = detail_address;
    }

    public String getPost_code() {
        return post_code;
    }

    public void setPost_code(String post_code) {
        this.post_code = post_code;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getIsCheckIdCardStr() {
        return isCheckIdCardStr;
    }

    public void setIsCheckIdCardStr(String isCheckIdCardStr) {
        this.isCheckIdCardStr = isCheckIdCardStr;
    }

    public String getId_card_url1() {
        return id_card_url1;
    }

    public void setId_card_url1(String id_card_url1) {
        this.id_card_url1 = id_card_url1;
    }

    public String getId_card_url2() {
        return id_card_url2;
    }

    public void setId_card_url2(String id_card_url2) {
        this.id_card_url2 = id_card_url2;
    }

    public String getPayCompany() {
        return payCompany;
    }

    public void setPayCompany(String payCompany) {
        this.payCompany = payCompany;
    }

    public String getOrder_pay_time() {
        return order_pay_time;
    }

    public void setOrder_pay_time(String order_pay_time) {
        this.order_pay_time = order_pay_time;
    }

    public String getPay_number() {
        return pay_number;
    }

    public void setPay_number(String pay_number) {
        this.pay_number = pay_number;
    }


    public String getPay_unit() {
        return pay_unit;
    }

    public void setPay_unit(String pay_unit) {
        this.pay_unit = pay_unit;
    }

    public String getIs_buy_finace() {
        return is_buy_finace;
    }

    public void setIs_buy_finace(String is_buy_finace) {
        this.is_buy_finace = is_buy_finace;
    }

    public String getIs_pack_reinforce() {
        return is_pack_reinforce;
    }

    public void setIs_pack_reinforce(String is_pack_reinforce) {
        this.is_pack_reinforce = is_pack_reinforce;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getOrder_remark() {
        return order_remark;
    }

    public void setOrder_remark(String order_remark) {
        this.order_remark = order_remark;
    }

    public String getThirdparty_number() {
        return thirdparty_number;
    }

    public void setThirdparty_number(String thirdparty_number) {
        this.thirdparty_number = thirdparty_number;
    }


    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getOrder_real_price() {
        return order_real_price;
    }

    public void setOrder_real_price(Float order_real_price) {
        this.order_real_price = order_real_price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }


    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
