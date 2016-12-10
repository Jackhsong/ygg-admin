package com.ygg.admin.dao.impl.mybatis;

import com.ygg.admin.dao.FortuneWheelDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.FortuneWheelEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-5-17
 */
@Repository
public class FortuneWheelDaoImpl extends BaseDaoImpl implements FortuneWheelDao {

    @Override
    public int save(FortuneWheelEntity entity) {
        return getSqlSession().insert("FortuneWheelMapper.save", entity) ;
    }

    @Override
    public int update(FortuneWheelEntity entity) {
        return getSqlSession().update("FortuneWheelMapper.update", entity);
    }

    @Override
    public List<FortuneWheelEntity> findByPara(Map<String, Object> para) {
        return getSqlSessionRead().selectList("FortuneWheelMapper.findByPara", para);
    }

    @Override
    public int countByPara(Map<String, Object> para) {
        return getSqlSessionRead().selectOne("FortuneWheelMapper.countByPara", para);
    }

    @Override
    public int updateByMapPara(Map<String, Object> para) {
        return getSqlSession().update("FortuneWheelMapper.updateByMapPara", para);
    }
}
