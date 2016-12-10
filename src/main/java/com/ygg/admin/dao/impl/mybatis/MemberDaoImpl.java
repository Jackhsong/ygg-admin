package com.ygg.admin.dao.impl.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.MemberDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.MemberBannerEntity;
import com.ygg.admin.entity.MemberLevelProductEntity;

@Repository
public class MemberDaoImpl extends BaseDaoImpl implements MemberDao
{
    
    @Override
    public List<Map<String, Object>> findAllMemberLevel(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("MemberMapper.findAllMemberLevel", para);
    }
    
    @Override
    public int countMemberLevel(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("MemberMapper.countMemberLevel", para);
    }
    
    @Override
    public int updateMemberLevelDisplayStatus(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("MemberMapper.updateMemberLevelDisplayStatus", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllMemberProduct(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("MemberMapper.findAllMemberProduct", para);
    }
    
    @Override
    public int countMemberProduct(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("MemberMapper.countMemberProduct", para);
    }
    
    @Override
    public int insertMemberProduct(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("MemberMapper.insertMemberProduct", para);
    }
    
    @Override
    public int updateMemberProduct(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("MemberMapper.updateMemberProduct", para);
    }
    
    @Override
    public int deleteMemberProduct(List<String> idList)
        throws Exception
    {
        return getSqlSession().delete("MemberMapper.deleteMemberProduct", idList);
    }
    
    @Override
    public int updateMemberProductDisplayStatus(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("MemberMapper.updateMemberProductDisplayStatus", para);
    }
    
    @Override
    public int countMemberBanner(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("MemberMapper.countMemberBanner", para);
    }
    
    @Override
    public List<MemberBannerEntity> findAllMemberBanner(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("MemberMapper.findAllMemberBanner", para);
    }
    
    @Override
    public MemberLevelProductEntity findMemberLevelProductByProductId(int productId)
        throws Exception
    {
        return getSqlSessionRead().selectOne("MemberMapper.findMemberLevelProductByProductId", productId);
    }
    
    @Override
    public int saveMemberBanner(MemberBannerEntity banner)
        throws Exception
    {
        return getSqlSession().insert("MemberMapper.saveMemberBanner", banner);
    }
    
    @Override
    public int updateMemberBanner(MemberBannerEntity banner)
        throws Exception
    {
        return getSqlSession().update("MemberMapper.updateMemberBanner", banner);
    }
    
    @Override
    public int deleteMemberBanner(List<String> idList)
        throws Exception
    {
        return getSqlSession().delete("MemberMapper.deleteMemberBanner", idList);
    }
    
    @Override
    public int updateMemberBannerDisplayStatus(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("MemberMapper.updateMemberBannerDisplayStatus", para);
    }
    
    @Override
    public int updateMemberBannerSequence(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("MemberMapper.updateMemberBannerSequence", para);
    }
    
    @Override
    public MemberBannerEntity findMemberBannerById(int id)
        throws Exception
    {
        return getSqlSessionRead().selectOne("MemberMapper.findMemberBannerById", id);
    }
}
