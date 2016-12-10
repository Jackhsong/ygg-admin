package com.ygg.admin.dao.impl.mybatis;

import com.ygg.admin.dao.MallCountryBrandDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-4-26
 */
@Repository
public class MallCountryBrandDaoImpl extends BaseDaoImpl implements MallCountryBrandDao {

    @Override
    public List<Map<String, Object>> findAllMallCountryBrandByPara(Map<String, Object> para) {
        return getSqlSessionRead().selectList("MallCountryBrandMapper.findAllMallCountryBrandByPara", para);
    }

    @Override
    public List<Map<String, Object>> jsonInfo(Map<String, Object> para) {
        return getSqlSessionRead().selectList("MallCountryBrandMapper.jsonInfo", para);
    }

    @Override
    public int countJsonInfo(Map<String, Object> para) {
        return getSqlSessionRead().selectOne("MallCountryBrandMapper.countJsonInfo", para);
    }

    @Override
    public int save(Map<String, Object> data) {
        return getSqlSession().insert("MallCountryBrandMapper.create", data);
    }

    @Override
    public int delete(int id) {
        return getSqlSession().delete("MallCountryBrandMapper.delete", id);
    }

    @Override
    public int update(Map<String, Object> para) {
        return getSqlSession().update("MallCountryBrandMapper.update", para);
    }
}
