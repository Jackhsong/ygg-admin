package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.RecordDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;

@Repository
public class RecordDaoImpl extends BaseDaoImpl implements RecordDao
{
    
    @Override
    public int insertSmsContentRecord(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().insert("RecordMapper.insertSmsContentRecord", para);
    }
    
    @Override
    public List<Map<String, Object>> findSmsContentRecordByPara(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionAdmin().selectList("RecordMapper.findSmsContentRecordByPara", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public int countSmsContentRecordByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().selectOne("RecordMapper.countSmsContentRecordByPara", para);
    }
    
}
