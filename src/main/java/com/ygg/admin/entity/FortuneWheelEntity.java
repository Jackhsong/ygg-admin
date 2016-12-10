package com.ygg.admin.entity;

import com.ygg.admin.util.SpringBeanUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-5-16
 */
public class FortuneWheelEntity implements Serializable {

    private static final long serialVersionUID = -6286789409417108795L;

    /** */
    private Integer id = 0;
    /** 活动名称 */
    private String name = "";
    /** 是否可用，0:不可用;1:可用 */
//    @JsonProperty("is_available")
    private Integer isAvailable = 0;
    /** 开始时间 */
//    @JsonProperty("start_time")
    private String startTime = "0000-00-00 00:00:00";
    /** 结束时间 */
//    @JsonProperty("end_time")
    private String endTime = "0000-00-00 00:00:00";
    /** 分享图 */
//    @JsonProperty("share_image")
    private String shareImage = "";
    /**分享主标题 */
//    @JsonProperty("share_main_title")
    private String shareMainTitle = "";
    /** 分享副标题 */
//    @JsonProperty("share_second_title")
    private String shareSecondTitle = "";
    /** 扩展信息 */
    private String extend = "";
    /** 背景色 */
//    @JsonProperty("background_color")
    private String backgroundColor = "";
    /** 活动类型, 1:扣积分;2:每日限次数 3:活动期间总次数 */
    private Integer type = 1;
    /** 类型为2：限次数值，0为不限制 */
    private Integer timesLimit = 0;
    /** 限制次数字体颜色 */
    private String timesFontColor = "";
    /** 限制次数图标 */
    private String timesIcon = "";
    /** 类型为1： 每次使用积分 */
    private Integer pointUsed = 0;
    /** 积分字体颜色 */
    private String pointFontColor = "";
    /** 积分图标 */
    private String pointIcon = "";
    /** 现有积分字体颜色 */
    private String ownPointFontColor = "";
    /** 现有积分字体图标 */
    private String ownPointFontIcon = "";

    // extend 字段中扩展信息 ----------------------
    /** 转盘上部图片 */
    private List<String> topPics;
    /** 转盘下部图片 */
    private List<String> bottomPics;
    /** 大转盘背景图 */
    private List<String> backgroundPics;
    /** 轮廓图 */
    private String outlinePic;
    /** 转盘图 */
    private String turntablePic;
    /** 指针图 */
    private String pointerPic;
    /** 指针图 x轴坐标 */
    private Integer pointerCoordinateX;
    /** 指针图 y轴坐标 */
    private Integer pointerCoordinateY;
    /**转盘图 x轴坐标*/
    private Integer turntableCoordinateX;
    /**转盘图 y轴坐标 */
    private Integer turntableCoordinateY;
    /** 弹窗按钮背景色 */
    private String dialogBackgroundImg;
    /** 弹窗按钮背景色 */
    private String dialogBackgroundColor;
    /** 弹窗按钮字体颜色 */
    private String dialogFontColor;
    /** 抽奖次数不足弹窗背景图 */
    private String timesDialogBackgroundImg;
    /**抽奖次数不足弹窗小图片 */
    private String timesDialogSmallImg;
    /**抽奖次数不足随机文案 */
    private List<String> timesDialogTips;
    /** 积分不足弹窗背景图 */
    private String pointDialogBackgroundImg;
    /** 积分不足弹窗小图片 */
    private String pointDialogSmallImg;
    /** 积分不足弹窗随机文案 */
    private List<String> pointDialogTips;

    //=============todo mv
    // 现有积分
//    @JsonProperty("curr_points")
    private Integer currPoints;

    //剩余次数
//    @JsonProperty("remain_times")
    private Integer remainTimes;

    private String createTime;

    public FortuneWheelEntity disAssembleExtend() throws IOException {
        if (StringUtils.isEmpty(this.getExtend())) {
            return this;
        }
//     /*   Map<String, Object> tmp = SpringBeanUtil.mapper.readValue(this.getExtend(), Map.class);
//        this.setTopPics(tmp.get("topPics") == null ? null : (List<String>) tmp.get("topPics"));
//        this.setBottomPics(tmp.get("bottomPics") == null ? null : (List<String>) tmp.get("bottomPics"));
//        this.setBackgroundPics(tmp.get("dialogFontColor") == null ? null : (List<String>) tmp.get("backgroundPics"));
//        this.setOutlinePic(tmp.get("outlinePic") == null ? null : (String) tmp.get("outlinePic"));
//        this.setTurntablePic(tmp.get("turntablePic") == null ? null : (String) tmp.get("turntablePic"));
//        this.setPointerPic(tmp.get("pointerPic") == null? null : (String) tmp.get("pointerPic"));
//        this.setPointerCoordinateX(tmp.get("pointerCoordinateX") == null ? 0 : (Integer) tmp.get("pointerCoordinateX"));
//        this.setPointerCoordinateY(tmp.get("pointerCoordinateY") == null ? 0 : (Integer) tmp.get("pointerCoordinateY"));
//        this.setTurntableCoordinateX(tmp.get("turntableCoordinateX") == null ? 0 : (Integer) tmp.get("turntableCoordinateX"));
//        this.setTurntableCoordinateY(tmp.get("turntableCoordinateY") == null ? 0 : (Integer) tmp.get("turntableCoordinateY"));
//
//        this.setDialogBackgroundImg(tmp.get("dialogBackgroundImg") == null ? null : (String) tmp.get("dialogBackgroundImg"));
//        this.setDialogBackgroundColor(tmp.get("dialogBackgroundColor") == null ? null : (String) tmp.get("dialogBackgroundColor"));
//        this.setDialogFontColor(tmp.get("dialogFontColor") == null ? null : (String) tmp.get("dialogFontColor"));
//        this.setTimesDialogBackgroundImg(tmp.get("timesDialogBackgroundImg") == null ? null : (String) tmp.get("timesDialogBackgroundImg"));
//        this.setTimesDialogSmallImg(tmp.get("timesDialogSmallImg") == null ? null : (String) tmp.get("timesDialogSmallImg"));
//        this.setTimesDialogTips(tmp.get("timesDialogTips") == null ? null : (List<String>) tmp.get("timesDialogTips"));
//        this.setPointDialogBackgroundImg(tmp.get("pointDialogBackgroundImg") == null ? null : (String) tmp.get("pointDialogBackgroundImg"));
//        this.setPointDialogSmallImg(tmp.get("pointDialogSmallImg") == null ? null : (String) tmp.get("pointDialogSmallImg"));
//        this.setPointDia*/logTips(tmp.get("pointDialogTips") == null ? null : (List<String>) tmp.get("pointDialogTips"));
        return this;
    }


    public FortuneWheelEntity assembleExtend() throws IOException {
        Map<String, Object> tmp = new HashMap();
        tmp.put("topPics", this.getTopPics());
        tmp.put("bottomPics", this.getBottomPics());
        tmp.put("backgroundPics", this.getBackgroundPics());
        tmp.put("outlinePic", this.getOutlinePic());
        tmp.put("turntablePic", this.getTurntablePic());
        tmp.put("pointerPic", this.getPointerPic());
        tmp.put("pointerCoordinateX", this.getPointerCoordinateX());
        tmp.put("pointerCoordinateY", this.getPointerCoordinateY());
        tmp.put("turntableCoordinateX", this.getTurntableCoordinateX());
        tmp.put("turntableCoordinateY", this.getTurntableCoordinateY());

        tmp.put("dialogBackgroundImg", this.getDialogBackgroundImg());
        tmp.put("dialogBackgroundColor", this.getDialogBackgroundColor());
        tmp.put("dialogFontColor", this.getDialogFontColor());
        tmp.put("timesDialogBackgroundImg", this.getTimesDialogBackgroundImg());
        tmp.put("timesDialogSmallImg", this.getTimesDialogSmallImg());
        tmp.put("timesDialogTips", this.getTimesDialogTips());
        tmp.put("pointDialogBackgroundImg", this.getPointDialogBackgroundImg());
        tmp.put("pointDialogSmallImg", this.getPointDialogSmallImg());
        tmp.put("pointDialogTips", this.getPointDialogTips());
//        String s = SpringBeanUtil.mapper.writeValueAsString(tmp);
//        this.setExtend(s);
        return this;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        if(id == null) id =0;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Integer isAvailable) {
        if(isAvailable == null) isAvailable = 0;
        this.isAvailable = isAvailable;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getShareImage() {
        return shareImage;
    }

    public void setShareImage(String shareImage) {
        this.shareImage = shareImage;
    }

    public String getShareMainTitle() {
        return shareMainTitle;
    }

    public void setShareMainTitle(String shareMainTitle) {
        this.shareMainTitle = shareMainTitle;
    }

    public String getShareSecondTitle() {
        return shareSecondTitle;
    }

    public void setShareSecondTitle(String shareSecondTitle) {
        this.shareSecondTitle = shareSecondTitle;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getTimesLimit() {
        return timesLimit;
    }

    public void setTimesLimit(Integer timesLimit) {
        if(timesLimit == null) timesLimit =0;
        this.timesLimit = timesLimit;
    }

    public String getTimesFontColor() {
        return timesFontColor;
    }

    public void setTimesFontColor(String timesFontColor) {
        this.timesFontColor = timesFontColor;
    }

    public String getTimesIcon() {
        return timesIcon;
    }

    public void setTimesIcon(String timesIcon) {
        this.timesIcon = timesIcon;
    }

    public Integer getPointUsed() {
        return pointUsed;
    }

    public void setPointUsed(Integer pointUsed) {
        if(pointUsed == null) pointUsed =0;
        this.pointUsed = pointUsed;
    }

    public String getPointFontColor() {
        return pointFontColor;
    }

    public void setPointFontColor(String pointFontColor) {
        this.pointFontColor = pointFontColor;
    }

    public String getPointIcon() {
        return pointIcon;
    }

    public void setPointIcon(String pointIcon) {
        this.pointIcon = pointIcon;
    }

    public String getOwnPointFontColor() {
        return ownPointFontColor;
    }

    public void setOwnPointFontColor(String ownPointFontColor) {
        this.ownPointFontColor = ownPointFontColor;
    }

    public String getOwnPointFontIcon() {
        return ownPointFontIcon;
    }

    public void setOwnPointFontIcon(String ownPointFontIcon) {
        this.ownPointFontIcon = ownPointFontIcon;
    }

    public List<String> getTopPics() {
        return topPics;
    }

    public void setTopPics(List<String> topPics) {
        this.topPics = topPics;
    }

    public List<String> getBottomPics() {
        return bottomPics;
    }

    public void setBottomPics(List<String> bottomPics) {
        this.bottomPics = bottomPics;
    }

    public List<String> getBackgroundPics() {
        return backgroundPics;
    }

    public void setBackgroundPics(List<String> backgroundPics) {
        this.backgroundPics = backgroundPics;
    }

    public String getOutlinePic() {
        return outlinePic;
    }

    public void setOutlinePic(String outlinePic) {
        this.outlinePic = outlinePic;
    }

    public String getTurntablePic() {
        return turntablePic;
    }

    public void setTurntablePic(String turntablePic) {
        this.turntablePic = turntablePic;
    }

    public String getPointerPic() {
        return pointerPic;
    }

    public void setPointerPic(String pointerPic) {
        this.pointerPic = pointerPic;
    }

    public Integer getPointerCoordinateX() {
        return pointerCoordinateX;
    }

    public void setPointerCoordinateX(Integer pointerCoordinateX) {
        this.pointerCoordinateX = pointerCoordinateX;
    }

    public Integer getPointerCoordinateY() {
        return pointerCoordinateY;
    }

    public void setPointerCoordinateY(Integer pointerCoordinateY) {
        this.pointerCoordinateY = pointerCoordinateY;
    }

    public Integer getTurntableCoordinateX() {
        return turntableCoordinateX;
    }

    public void setTurntableCoordinateX(Integer turntableCoordinateX) {
        this.turntableCoordinateX = turntableCoordinateX;
    }

    public Integer getTurntableCoordinateY() {
        return turntableCoordinateY;
    }

    public void setTurntableCoordinateY(Integer turntableCoordinateY) {
        this.turntableCoordinateY = turntableCoordinateY;
    }

    public Integer getCurrPoints() {
        return currPoints;
    }

    public void setCurrPoints(Integer currPoints) {
        this.currPoints = currPoints;
    }

    public Integer getRemainTimes() {
        return remainTimes;
    }

    public void setRemainTimes(Integer remainTimes) {
        this.remainTimes = remainTimes;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDialogBackgroundImg() {
        return dialogBackgroundImg;
    }

    public void setDialogBackgroundImg(String dialogBackgroundImg) {
        this.dialogBackgroundImg = dialogBackgroundImg;
    }

    public String getDialogBackgroundColor() {
        return dialogBackgroundColor;
    }

    public void setDialogBackgroundColor(String dialogBackgroundColor) {
        this.dialogBackgroundColor = dialogBackgroundColor;
    }

    public String getDialogFontColor() {
        return dialogFontColor;
    }

    public void setDialogFontColor(String dialogFontColor) {
        this.dialogFontColor = dialogFontColor;
    }

    public String getTimesDialogBackgroundImg() {
        return timesDialogBackgroundImg;
    }

    public void setTimesDialogBackgroundImg(String timesDialogBackgroundImg) {
        this.timesDialogBackgroundImg = timesDialogBackgroundImg;
    }

    public String getTimesDialogSmallImg() {
        return timesDialogSmallImg;
    }

    public void setTimesDialogSmallImg(String timesDialogSmallImg) {
        this.timesDialogSmallImg = timesDialogSmallImg;
    }

    public List<String> getTimesDialogTips() {
        return timesDialogTips;
    }

    public void setTimesDialogTips(List<String> timesDialogTips) {
        this.timesDialogTips = timesDialogTips;
    }

    public String getPointDialogBackgroundImg() {
        return pointDialogBackgroundImg;
    }

    public void setPointDialogBackgroundImg(String pointDialogBackgroundImg) {
        this.pointDialogBackgroundImg = pointDialogBackgroundImg;
    }

    public String getPointDialogSmallImg() {
        return pointDialogSmallImg;
    }

    public void setPointDialogSmallImg(String pointDialogSmallImg) {
        this.pointDialogSmallImg = pointDialogSmallImg;
    }

    public List<String> getPointDialogTips() {
        return pointDialogTips;
    }

    public void setPointDialogTips(List<String> pointDialogTips) {
        this.pointDialogTips = pointDialogTips;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }
}
