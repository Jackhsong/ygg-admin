package com.ygg.admin.dao.yw.error.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.dao.yw.error.YwErrorDao;
import com.ygg.admin.entity.yw.YwAccountRelaChangeLogEntity;

@Repository("ywErrorDao")
public class YwErrorDaoImpl extends BaseDaoImpl implements YwErrorDao
{
    @Override
    public List<Map<String, Object>> findListInfo(Map<String, Object> param){
        return getSqlSessionRead().selectList("YwErrorMapper.findListByParam", param);
    }
    @Override
    public int getCountByParam(Map<String, Object> param)
    {
        return getSqlSessionRead().selectOne("YwErrorMapper.getCountByParam", param);
    } 
    public int insert(YwAccountRelaChangeLogEntity log){
        return this.getSqlSession().insert("YwErrorMapper.insert", log);
    }


}
