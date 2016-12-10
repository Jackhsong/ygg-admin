package com.ygg.admin.dao.impl.mybatis;

import com.ygg.admin.dao.SpecialActivityModelLayoutProductDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("specialActivityModelLayoutProductDao")
public class SpecialActivityModelLayoutProductDaoImpl extends BaseDaoImpl implements SpecialActivityModelLayoutProductDao
{

    @Override
    public List<Map<String, Object>> findListByParam(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionRead().selectList("SpecialActivityModelLayoutProductMapper.findListByParam", param);
    }

    @Override
    public int countByParam(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionRead().selectOne("SpecialActivityModelLayoutProductMapper.countByParam", param);
    }

    @Override
    public int updateByParam(Map<String, Object> param)
        throws Exception
    {
        return getSqlSession().update("SpecialActivityModelLayoutProductMapper.updateByParam", param);
    }

    @Override
    public int save(Map<String, Object> param)
        throws Exception
    {
        return getSqlSession().insert("SpecialActivityModelLayoutProductMapper.save", param);
    }

    @Override
    public int findMaxSequence(int layoutId) throws Exception {
        try {
            return getSqlSession().selectOne("SpecialActivityModelLayoutProductMapper.findMaxSequence", layoutId);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

}
