package com.ygg.admin.dao;

import com.ygg.admin.entity.FortuneWheelPrizeEntity;

import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-5-18
 */
public interface FortuneWheelPrizeDao {
    int save(FortuneWheelPrizeEntity prize);

    int update(FortuneWheelPrizeEntity prize);

    List<FortuneWheelPrizeEntity> findByPara(Map<String, Object> para);

    int updateByPara(Map para);
}
