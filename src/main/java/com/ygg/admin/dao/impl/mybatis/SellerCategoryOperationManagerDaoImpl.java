package com.ygg.admin.dao.impl.mybatis;

import com.ygg.admin.dao.SellerCategoryOperationManagerDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-5-11
 */
@Repository
public class SellerCategoryOperationManagerDaoImpl extends BaseDaoImpl implements SellerCategoryOperationManagerDao {

    Logger log = Logger.getLogger(SellerCategoryOperationManagerDaoImpl.class);

    @Override
    public int save(Map<String, Object> data) {
        return getSqlSession().insert("SellerCategoryOperationManager.save", data);
    }

    @Override
    public int update(Map<String, Object> data) {
        return getSqlSession().update("SellerCategoryOperationManager.update", data);
    }

    @Override
    public List<Map<String, Object>> findByPara(Map<String, Object> para) {
        return getSqlSessionRead().selectList("SellerCategoryOperationManager.findByPara", para);
    }

    @Override
    public int countByPara(Map<String, Object> para) {
        try {
            return getSqlSessionRead().selectOne("SellerCategoryOperationManager.countByPara", para);
        } catch (Exception e) {
            log.error(e);
            return 0;
        }
    }
}
