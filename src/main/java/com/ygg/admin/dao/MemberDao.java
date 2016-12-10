package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.MemberBannerEntity;
import com.ygg.admin.entity.MemberLevelProductEntity;

public interface MemberDao
{
    
    List<Map<String, Object>> findAllMemberLevel(Map<String, Object> para)
        throws Exception;
    
    int countMemberLevel(Map<String, Object> para)
        throws Exception;
    
    int updateMemberLevelDisplayStatus(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findAllMemberProduct(Map<String, Object> para)
        throws Exception;
    
    int countMemberProduct(Map<String, Object> para)
        throws Exception;
    
    int insertMemberProduct(Map<String, Object> para)
        throws Exception;
    
    int updateMemberProduct(Map<String, Object> para)
        throws Exception;
    
    int deleteMemberProduct(List<String> idList)
        throws Exception;
    
    int updateMemberProductDisplayStatus(Map<String, Object> para)
        throws Exception;
    
    List<MemberBannerEntity> findAllMemberBanner(Map<String, Object> para)
        throws Exception;
    
    int countMemberBanner(Map<String, Object> para)
        throws Exception;
    
    MemberLevelProductEntity findMemberLevelProductByProductId(int productId)
        throws Exception;
    
    int saveMemberBanner(MemberBannerEntity banner)
        throws Exception;
    
    int updateMemberBanner(MemberBannerEntity banner)
        throws Exception;
    
    int deleteMemberBanner(List<String> idList)
        throws Exception;
    
    int updateMemberBannerDisplayStatus(Map<String, Object> para)
        throws Exception;
    
    int updateMemberBannerSequence(Map<String, Object> para)
        throws Exception;
    
    MemberBannerEntity findMemberBannerById(int id)
        throws Exception;
    
}
