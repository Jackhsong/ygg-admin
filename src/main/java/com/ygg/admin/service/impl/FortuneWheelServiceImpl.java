package com.ygg.admin.service.impl;

import com.ygg.admin.dao.FortuneWheelDao;
import com.ygg.admin.entity.FortuneWheelEntity;
import com.ygg.admin.service.FortuneWheelService;
import com.ygg.admin.util.DateTimeUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-5-17
 */
@Service
public class FortuneWheelServiceImpl implements FortuneWheelService {

    @Resource
    private FortuneWheelDao fortuneWheelDao;

    @Override
    public Map<String, Object> jsonInfo(Map<String, Object> para) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        List<FortuneWheelEntity> entities = fortuneWheelDao.findByPara(para);
        for(FortuneWheelEntity entity : entities){
            entity.disAssembleExtend();
            entity.setStartTime(DateTimeUtil.timestampStringToWebString( entity.getStartTime()));
            entity.setEndTime(DateTimeUtil.timestampStringToWebString( entity.getEndTime()));
            entity.setCreateTime(DateTimeUtil.timestampStringToWebString(entity.getCreateTime()));
        }
        resultMap.put("rows", entities);
        resultMap.put("total", fortuneWheelDao.countByPara(para));
        return resultMap;
    }

    @Override
    public int save(FortuneWheelEntity entity) throws Exception {
        entity.assembleExtend();
        return fortuneWheelDao.save(entity);
    }

    @Override
    public int update(FortuneWheelEntity entity) throws Exception {
        entity.assembleExtend();
        return fortuneWheelDao.update(entity);
    }

    @Override
    public FortuneWheelEntity findById(int id) throws Exception {
        Map map = new HashMap();
        map.put("id", id);
        List<FortuneWheelEntity> wheels = fortuneWheelDao.findByPara(map);
        if (CollectionUtils.isEmpty(wheels)) {
            return null;
        }
        FortuneWheelEntity entity = wheels.get(0);
        entity.disAssembleExtend();
        entity.setStartTime(DateTimeUtil.timestampStringToWebString( entity.getStartTime()));
        entity.setEndTime(DateTimeUtil.timestampStringToWebString( entity.getEndTime()));
        return entity;
    }

    @Override
    public int updateIsAvailable(int id, int isAvailable) {
        Map para = new HashMap();
        para.put("id", id);
        para.put("isAvailable", isAvailable);
        return fortuneWheelDao.updateByMapPara(para);
    }
}
