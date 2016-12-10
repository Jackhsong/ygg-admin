package com.ygg.admin.service;

import com.ygg.admin.entity.FortuneWheelEntity;

import java.util.Map;

/**
 * @author lorabit
 * @since 16-5-17
 */
public interface FortuneWheelService {

    Map<String, Object> jsonInfo(Map<String, Object> para) throws Exception;

    int save(FortuneWheelEntity entity) throws Exception;

    int update(FortuneWheelEntity entity) throws Exception;

    FortuneWheelEntity findById(int id) throws Exception;

    int updateIsAvailable(int id, int isAvailable);

}
