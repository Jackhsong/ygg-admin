package com.ygg.admin.dao.impl.mybatis;

import com.ygg.admin.dao.BrandHotDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-4-25
 */
@Repository
public class BrandHotDaoImpl extends BaseDaoImpl implements BrandHotDao {

    Logger log = Logger.getLogger(BrandHotDaoImpl.class);

    @Override
    public List<Map<String, Object>> findAllHotBrandByPara(Map<String, Object> para) {
        return getSqlSessionRead().selectList("BrandHotMapper.findAllHotBrandByPara", para);
    }

    @Override
    public int countFindAllHotBrandByPara(Map<String, Object> para) {
        return getSqlSessionRead().selectOne("BrandHotMapper.countFindAllHotBrandByPara", para);
    }

    @Override
    public int save(Map<String, Object> data) {
        return getSqlSession().insert("BrandHotMapper.create", data);
    }

    @Override
    public int delete(int id) {
        return getSqlSession().delete("BrandHotMapper.delete", id);
    }

    @Override
    public int update(Map<String, Object> para) {
        return getSqlSession().update("BrandHotMapper.update", para);
    }
}
