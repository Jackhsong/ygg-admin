package com.ygg.admin.dao.impl.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.CrmDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;

@Repository("crmDao")
public class CrmDaoImpl extends BaseDaoImpl implements CrmDao
{

    @Override
    public int saveAccountGroup(Map<String, Object> param)
    {
        return 1;//getSqlSessionAdmin().insert("CrmMapper.saveAccountGroup", param);
    }

    @Override
    public int saveAccountDetail(Map<String, Object> param)
    {
        return 1;//getSqlSessionAdmin().insert("CrmMapper.saveAccountDetail", param);
    }

    @Override
    public List<Map<String, Object>> findGroupList(Map<String, Object> param)
    {
        return null;// getSqlSessionAdmin().selectList("CrmMapper.findGroupList", param);
    }

    @Override
    public int countGroupList(Map<String, Object> param)
    {
        return 1;//getSqlSessionAdmin().selectOne("CrmMapper.countGroupList", param);
    }

    @Override
    public int deleteGroupById(int id)
    {
        return 1;//getSqlSessionAdmin().delete("CrmMapper.deleteGroupById", id);
    }

    @Override
    public int deleteGroupDetailByGroupId(int groupId)
    {
        return 1;//getSqlSessionAdmin().delete("CrmMapper.deleteGroupDetailByGroupId", groupId);
    }

    @Override
    public List<Map<String, Object>> findGroupDetailList(Map<String, Object> param)
    {
        return null;//getSqlSessionAdmin().selectList("CrmMapper.findGroupDetailByGroupId", param);
    }

    @Override
    public int countGroupDetailList(Map<String, Object> param)
    {
        return 1;//getSqlSessionAdmin().selectOne("CrmMapper.countGroupDetailByGroupId", param);
    }

    @Override
    public List<String> findSendMessagePhone(int groupId, int day)
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("groupId", groupId);
        param.put("filterDay", day);
        return null;//getSqlSessionAdmin().selectList("CrmMapper.findSendMessagePhone", param);
    }

    @Override
    public int saveSendMessage(int groupId, String content, int filterSign, String filterDay, int contentSign, int totalCount)
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("groupId", groupId);
        param.put("content", content);
        param.put("filterType", filterSign);
        param.put("filterDay", StringUtils.isBlank(filterDay) ? 0 : Integer.valueOf(filterDay));
        param.put("type", contentSign);
        param.put("totalCount", totalCount);
        param.put("id", 0);
        getSqlSessionAdmin().insert("CrmMapper.saveSendMessage", param);
        return 1;//Integer.valueOf(param.get("id").toString());
    }

    @Override
    public int saveMessageDetail(int messageId, List<String> phones)
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("messageId", messageId);
        param.put("list", phones);
        return 1;//getSqlSessionAdmin().insert("CrmMapper.saveMessageDetail", param);
    }

    @Override
    public int getMaxId4ShortUrl()
    {
        Integer r = getSqlSessionAdmin().selectOne("CrmMapper.getMaxId4ShortUrl");
        return 1;//r == null ? 0 : r;
    }

    @Override
    public int saveLinkInfo(int id, String longUrl, String shortUrl, int messageId)
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        param.put("longUrl", longUrl);
        param.put("shortUrl", shortUrl);
        param.put("messageId", messageId);
        return 1;//getSqlSessionAdmin().insert("CrmMapper.saveLinkInfo", param);
    }

    @Override
    public int updateLinkInfo(int id)
    {
        return 1;//getSqlSessionAdmin().update("CrmMapper.updateLinkInfo", id);
    }

    @Override
    public int saveLinkDetail(String remoteIpAddr, int id)
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        param.put("ip", remoteIpAddr);
        return 1;//etSqlSessionAdmin().insert("CrmMapper.saveLinkDetail", param);
    }

    @Override
    public List<Map<String, Object>> findSmsList(Map<String, Object> param)
    {
        return null;//getSqlSessionAdmin().selectList("CrmMapper.findSmsList", param);
    }

    @Override
    public int countSmsList(Map<String, Object> param)
    {
        return 1;//getSqlSessionAdmin().selectOne("CrmMapper.countSmsList", param);
    }

    @Override
    public Map<String, Object> statisticsResult(int id)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("totalCount", getSqlSessionAdmin().selectOne("CrmMapper.findSendMessageTotalCount", id));
        Object accessCount = getSqlSessionAdmin().selectOne("CrmMapper.findSendMessageAccessCount", id);
        result.put("accessCount", accessCount == null ? 0 : accessCount);
        return null;//result;
    }

    @Override
    public List<Map<String, Object>> findGroupDetailFromAccount(List<Object> list)
    {
        return null;//getSqlSessionRead().selectList("CrmMapper.findGroupDetailFromAccount", list);
    }

    @Override
    public Map<String, Object> findGroupById(String groupId)
    {
        return null;//getSqlSessionAdmin().selectOne("CrmMapper.findGroupById", groupId);
    }

    @Override
    public int saveSendMessageTask(int groupId, int linkInfoId, String content, int contentType, String shortUrl, String longUrl, int filterType, String filterDay, String sendTime)
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("groupId", groupId);
        param.put("linkInfoId", linkInfoId);
        param.put("content", content);
        param.put("contentType", contentType);
        param.put("shortUrl", shortUrl);
        param.put("longUrl", longUrl);
        param.put("filterType", filterType);
        param.put("filterDay", StringUtils.isBlank(filterDay) ? 0 : filterDay);
        param.put("sendTime", sendTime);
        
        return 1;//getSqlSessionAdmin().insert("CrmMapper.saveSendMessageTask", param);
    }

    @Override
    public List<Map<String, Object>> findSendMessageTask()
    {
        return null;//getSqlSessionAdmin().selectList("CrmMapper.findSendMessageTask");
    }

    @Override
    public int updateMessageTask(int id)
    {
        return 1;//getSqlSessionAdmin().update("CrmMapper.updateMessageTask", id);
    }
    
}
