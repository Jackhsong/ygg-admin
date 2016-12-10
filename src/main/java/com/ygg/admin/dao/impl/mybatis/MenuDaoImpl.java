package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.MenuDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;

@Repository("menuDao")
public class MenuDaoImpl extends BaseDaoImpl implements MenuDao
{
    
    @Override
    public int createMenu(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().insert("MenuMapper.createMenu", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllMenuByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().selectList("MenuMapper.findAllMenuByPara", para);
    }
    
    @Override
    public int updateMenu(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().update("MenuMapper.updateMenu", para);
    }
    
    @Override
    public int delete(int id)
        throws Exception
    {
        return getSqlSessionAdmin().delete("MenuMapper.delete", id);
    }
    
}
