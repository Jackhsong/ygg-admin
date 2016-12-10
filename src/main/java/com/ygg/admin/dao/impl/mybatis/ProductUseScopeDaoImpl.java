package com.ygg.admin.dao.impl.mybatis;

import com.ygg.admin.dao.ProductUseScopeDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-5-7
 */
@Repository
public class ProductUseScopeDaoImpl extends BaseDaoImpl implements ProductUseScopeDao {
    @Override
    public int insertProductUseScope(Map<String, Object> data) {
        return getSqlSession().insert("ProductUseScopeMapper.insertProductUseScope", data);
    }

    @Override
    public int updateProductUserScope(Map<String, Object> data) {
        return getSqlSession().update("ProductUseScopeMapper.updateProductUserScope", data);
    }

    @Override
    public int deleteProductUseScopeById(int id) {
        return getSqlSession().delete("ProductUseScopeMapper.deleteProductUseScopeById", id);
    }

    @Override
    public List<Map<String, Object>> findProductUseScope(Map<String, Object> para) {
        return getSqlSessionRead().selectList("ProductUseScopeMapper.findProductUseScope", para);
    }

    @Override
    public int countListInfo(Map<String, Object> para) {
        return getSqlSessionRead().selectOne("ProductUseScopeMapper.countListInfo", para);
    }
}
