package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

public interface MenuDao
{
    
    /**
     * 创建菜单
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int createMenu(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查询菜单
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllMenuByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 更新菜单
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int updateMenu(Map<String, Object> para)
        throws Exception;
    
    int delete(int id)
        throws Exception;
}
