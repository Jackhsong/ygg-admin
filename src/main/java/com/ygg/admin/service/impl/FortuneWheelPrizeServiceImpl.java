package com.ygg.admin.service.impl;

import com.ygg.admin.code.FortuneWheelPrizeEnum;
import com.ygg.admin.dao.FortuneWheelPrizeDao;
import com.ygg.admin.entity.CouponDetailEntity;
import com.ygg.admin.entity.CouponEntity;
import com.ygg.admin.entity.FortuneWheelPrizeEntity;
import com.ygg.admin.service.CouponService;
import com.ygg.admin.service.FortuneWheelPrizeService;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-5-18
 */
@Service
public class FortuneWheelPrizeServiceImpl implements FortuneWheelPrizeService {

    @Resource
    private FortuneWheelPrizeDao fortuneWheelPrizeDao;

    @Resource
    private CouponService couponService;

    @Override
    public Map<String, Object> jsonInfo(Map<String, Object> para) throws Exception {
        Map<String, Object> ret = new HashMap();
        List<FortuneWheelPrizeEntity> prizes = fortuneWheelPrizeDao.findByPara(para);
        for (FortuneWheelPrizeEntity prize : prizes) {
            if (prize.getPic() != null) {
                String img = "<img style='width:100px' src='" + prize.getPic() + "' />";
                prize.getMap().put("picImg","<a href=" + prize.getPic() + " target=\"_blank\"> " + img + " </a>");
            }

            String info = "";
            if (prize.getType() == FortuneWheelPrizeEnum.TYPE.XIEXIE.getCode()) {
                info = "谢谢惠顾, 未中奖品";
            } else if (prize.getType() == FortuneWheelPrizeEnum.TYPE.FIXED_JIFEN.getCode()) {
                info = prize.getPointAmount() + "积分";
            } else if (prize.getType() == FortuneWheelPrizeEnum.TYPE.RANDOM_JIFEN.getCode()) {
                info = prize.getMinPoint() + "到" + prize.getMaxPoint() + "积分";
            } else if (prize.getType() == FortuneWheelPrizeEnum.TYPE.COUPON.getCode()) {
                CouponEntity coupon = couponService.findCouponById(prize.getCouponId());
                CouponDetailEntity detail = couponService.findCouponDetailById(coupon.getCouponDetailId());
                if (detail.getType() == 1) {
                    if (detail.getIsRandomReduce() == 0) {
                        info = "满" + detail.getThreshold() + "减" + detail.getReduce() + "元优惠券券";
                    } else if (detail.getIsRandomReduce() == 1) {
                        info = MessageFormatter.arrayFormat("随机--{}到{}--满{}减X优惠券",
                                new Object[]{detail.getLowestReduce(), detail.getMaximalReduce(), detail.getThreshold()}).toString();
                    }

                    if(detail.getScopeType() != 1) {
                        //todo fuck
                    }
                    info += "<br/>" + coupon.getStartTime() + "至" + coupon.getEndTime();
                } else if (detail.getType() == 2) {
                    if (detail.getIsRandomReduce() == 0) {
                        info = detail.getReduce() + "元现金券";
                    } else if (detail.getIsRandomReduce() == 1) {
                        info = MessageFormatter.format("随机--{}到{}--X元现金券", detail.getLowestReduce(), detail.getMaximalReduce()).toString();
                    }
                }
            }
            prize.getMap().put("info", info);
        }
        ret.put("rows", prizes);
        ret.put("total", prizes == null ? 0 : prizes.size());
        return ret;
    }

    @Override
    public int save(FortuneWheelPrizeEntity prize) {
        return fortuneWheelPrizeDao.save(prize);
    }

    @Override
    public int update(FortuneWheelPrizeEntity prize) {
        return fortuneWheelPrizeDao.update(prize);
    }

    @Override
    public int updateIsAvailable(int id, int isAvailable) {
        Map para = new HashMap();
        para.put("id", id);
        para.put("isAvailable", isAvailable);
        return fortuneWheelPrizeDao.updateByPara(para);
    }
}
