package com.ygg.admin.dao.impl.mybatis;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.ygg.admin.dao.WechatGroupDataDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;

@Repository("wechatGroupDataDao")
public class WechatGroupDataDaoImpl extends BaseDaoImpl implements WechatGroupDataDao
{
    
    @Override
    public List<JSONObject> customSql(JSONObject parameter)
    {
        
        return getSqlSessionRead().selectList("WechatGroupDataMapper.customSql", parameter);
    }
    
}
