package com.ygg.admin.dao.impl.mybatis;

import com.ygg.admin.dao.FortuneWheelPrizeDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.FortuneWheelPrizeEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-5-18
 */
@Repository
public class FortuneWheelPrizeDaoImpl extends BaseDaoImpl implements FortuneWheelPrizeDao {

    @Override
    public int save(FortuneWheelPrizeEntity prize) {
        return getSqlSession().insert("FortuneWheelPrizeMapper.save", prize);
    }

    @Override
    public int update(FortuneWheelPrizeEntity prize) {
        return getSqlSession().update("FortuneWheelPrizeMapper.update", prize);
    }

    @Override
    public List<FortuneWheelPrizeEntity> findByPara(Map<String, Object> para) {
        return getSqlSessionRead().selectList("FortuneWheelPrizeMapper.findByPara", para);
    }

    @Override
    public int updateByPara(Map para) {
        return getSqlSession().update("FortuneWheelPrizeMapper.updateByPara", para);
    }
}
