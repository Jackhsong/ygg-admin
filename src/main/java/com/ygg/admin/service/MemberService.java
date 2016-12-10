package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.MemberBannerEntity;

public interface MemberService
{
    
    String jsonMemberLevelInfo(Map<String, Object> para)
        throws Exception;
    
    String updateMemberLevelDisplayStatus(List<String> asList, int isDisplay)
        throws Exception;
    
    String jsonMemberProductInfo(Map<String, Object> para)
        throws Exception;
    
    String insertMemberProduct(int levelId, int level, int productId, int point, int sequence, int limitNum, int isSupportCashBuy)
        throws Exception;
    
    String updateMemberProduct(int id, int levelId, int level, int productId, int point, int sequence, int limitNum, int isSupportCashBuy)
        throws Exception;
    
    String deleteMemberProduct(List<String> asList)
        throws Exception;
    
    String updateMemberProductDisplayStatus(List<String> asList, int isDisplay)
        throws Exception;
    
    String updateMemberProductSequence(int id, int sequence)
        throws Exception;
    
    String jsonMemberBannerInfo(Map<String, Object> para)
        throws Exception;
    
    String saveMemberBanner(MemberBannerEntity banner)
        throws Exception;
    
    String updateMemberBanner(MemberBannerEntity banner)
        throws Exception;
    
    String deleteMemberBanner(List<String> asList)
        throws Exception;
    
    String updateMemberBannerDisplayStatus(List<String> asList, int isDisplay)
        throws Exception;
    
    String updateMemberBannerSequence(int id, int sequence)
        throws Exception;
    
    MemberBannerEntity findMemberBannerById(int id)
        throws Exception;
    
}
