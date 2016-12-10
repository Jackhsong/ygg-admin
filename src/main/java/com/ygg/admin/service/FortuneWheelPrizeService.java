package com.ygg.admin.service;

import com.ygg.admin.entity.FortuneWheelPrizeEntity;

import java.util.Map;

/**
 * @author lorabit
 * @since 16-5-18
 */
public interface FortuneWheelPrizeService {

    Map<String, Object> jsonInfo(Map<String, Object> para) throws Exception;

    int save(FortuneWheelPrizeEntity prize);

    int update(FortuneWheelPrizeEntity peize);

    int updateIsAvailable(int id, int isAvailable);

}
