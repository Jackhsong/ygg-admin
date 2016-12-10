package com.ygg.admin.dao.impl.mybatis;

import com.ygg.admin.dao.MallCountryDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-4-26
 */

@Repository
public class MallCountryDaoImpl extends BaseDaoImpl implements MallCountryDao {
    @Override
    public List<Map<String, Object>> findAllMallCountryByPara(Map<String, Object> para) {
        return getSqlSessionRead().selectList("MallCountryMapper.findAllMallCountryByPara", para);
    }

    @Override
    public List<Map<String, Object>> jsonInfo(Map<String, Object> para) {
        return getSqlSessionRead().selectList("MallCountryMapper.jsonInfo", para);
    }

    @Override
    public int countJsonInfo(Map<String, Object> para) {
        return getSqlSessionRead().selectOne("MallCountryMapper.countJsonInfo", para);
    }

    @Override
    public int countCountryBrandNum(int mallCountryId) {
        return getSqlSessionRead().selectOne("MallCountryMapper.countCountryBrandNum", mallCountryId);
    }

    @Override
    public int save(Map<String, Object> data) {
        return getSqlSession().insert("MallCountryMapper.create", data);
    }

    @Override
    public int delete(int id) {
        return getSqlSession().delete("MallCountryMapper.delete", id);
    }

    @Override
    public int update(Map<String, Object> para) {
        return getSqlSession().update("MallCountryMapper.update", para);
    }
}
