package com.ygg.admin.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-5-18
 */
public class FortuneWheelPrizeEntity {
    private Integer id = 0;
    private Integer fortuneWheelId ;
    /**
     * 类型：0：谢谢惠顾1：固定积分 2：随机积分 3：优惠券
     */
    private Integer type ;
    private Integer couponId = 0;
    private Integer minPoint = 0;
    private Integer maxPoint = 0;
    private Integer pointAmount = 0;
    private Integer prizeAmount = 0;
    private Integer prizeDraw = 0;
    private Integer probability = 0;
    private Integer isAvailable ;
    private String remark = "";
    private String tip = "";
    private String fontColor = "";
    private Integer sequence = 0;
    private String pic = "";
    private String createTime;


    // 用于接口传输额外的数据。。
    private Map<String, Object> map = new HashMap();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        if (id == null) id = 0;
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        if (type == null) type = 0;
        this.type = type;
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        if (couponId == null) couponId = 0;
        this.couponId = couponId;
    }

    public Integer getPointAmount() {
        return pointAmount;
    }

    public void setPointAmount(Integer pointAmount) {
        if (pointAmount == null) pointAmount = 0;
        this.pointAmount = pointAmount;
    }

    public Integer getPrizeAmount() {
        return prizeAmount;
    }

    public void setPrizeAmount(Integer prizeAmount) {
        if (prizeAmount == null) prizeAmount = 0;
        this.prizeAmount = prizeAmount;
    }

    public Integer getProbability() {
        return probability;
    }

    public void setProbability(Integer probability) {
        if (probability == null) probability = 0;
        this.probability = probability;
    }

    public Integer getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Integer isAvailable) {
        if (isAvailable == null) isAvailable = 0;
        this.isAvailable = isAvailable;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        if (remark == null) remark = "";
        this.remark = remark;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        if (tip == null) tip = "";
        this.tip = tip;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        if (fontColor == null) fontColor = "";
        this.fontColor = fontColor;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        if (sequence == null) sequence = 0;
        this.sequence = sequence;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        if (pic == null) pic = "";
        this.pic = pic;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getMinPoint() {
        return minPoint;
    }

    public void setMinPoint(Integer minPoint) {
        if (minPoint == null) minPoint = 0;
        this.minPoint = minPoint;
    }

    public Integer getMaxPoint() {
        return maxPoint;
    }

    public void setMaxPoint(Integer maxPoint) {
        if (maxPoint == null) maxPoint = 0;
        this.maxPoint = maxPoint;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public Integer getFortuneWheelId() {
        return fortuneWheelId;
    }

    public void setFortuneWheelId(Integer fortuneWheelId) {
        this.fortuneWheelId = fortuneWheelId;
    }

    public Integer getPrizeDraw() {
        return prizeDraw;
    }

    public void setPrizeDraw(Integer prizeDraw) {
        this.prizeDraw = prizeDraw;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
