package com.ygg.admin.dao.impl.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.GegetuanDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;

@Repository("gegetuanDao")
public class GegetuanDaoImpl extends BaseDaoImpl implements GegetuanDao {

	@Override
	public int countListInfo(Map<String, Object> param) throws Exception {
		return getSqlSessionRead().selectOne("GegetuanMapper.countListInfo", param);
	}

	@Override
	public List<Map<String, Object>> findListInfo(Map<String, Object> param) throws Exception {
		return getSqlSessionRead().selectList("GegetuanMapper.findListInfo", param);
	}

}
