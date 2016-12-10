package com.ygg.admin.dao;

import com.ygg.admin.entity.FortuneWheelEntity;

import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-5-17
 */
public interface FortuneWheelDao {

    int save(FortuneWheelEntity entity);

    int update(FortuneWheelEntity entity);

    List<FortuneWheelEntity> findByPara(Map<String, Object> para);

    int countByPara(Map<String, Object> para);

    int updateByMapPara(Map<String, Object> para);
}
