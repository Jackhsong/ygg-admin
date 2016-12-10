package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

public interface CrmDao
{
    /**
     * 保存分组
     * @param param
     * @return
     */
    int saveAccountGroup(Map<String, Object> param);
    
    /**
     * 保存分组明细
     * @param param
     * @return
     */
    int saveAccountDetail(Map<String, Object> param);

    /**
     * 查询分组
     * @param param
     * @return
     */
    List<Map<String, Object>> findGroupList(Map<String, Object> param);
    
    /**
     * 查询分组信息
     * @param groupId 分组id
     * @return
     */
    Map<String, Object> findGroupById(String groupId);

    /**
     * 统计分组条数
     * @param param
     * @return
     */
    int countGroupList(Map<String, Object> param);
    
    /**
     * 删除分组
     * @param id
     * @return
     */
    int deleteGroupById(int id);
    
    /**
     * 删除分组明细
     * @param groupId
     * @return
     */
    int deleteGroupDetailByGroupId(int groupId);

    /**
     * 分组明细
     * @param param
     * @return
     */
    List<Map<String, Object>> findGroupDetailList(Map<String, Object> param);
    
    /**
     * 分组明细--用户信息
     * @param list
     * @return
     */
    List<Map<String, Object>> findGroupDetailFromAccount(List<Object> list);

    /**
     * 统计分组明细条数
     * @param param
     * @return
     */
    int countGroupDetailList(Map<String, Object> param);

    /**
     * 查询需要发送的用户。有过滤发送用户的策略
     * 
     * @param groupId 分组id
     * @param day 过滤发送用户的策略转换后的结果
     * @return
     */
    List<String> findSendMessagePhone(int groupId, int day);

    /**
     * 保存发送短信记录
     * 
     * @param groupId 分组id
     * @param content 短信内容
     * @param filterSign 过滤发送用户的方式
     * @param filterDay 过滤发送时间
     * @param contentSign 产品线
     * @param totalCount 发送短信总条数
     * @return 发送短信记录ID
     */
    int saveSendMessage(int groupId, String content, int filterSign, String filterDay, int contentSign, int totalCount);

    /**
     * 保存发送短信明细
     * 
     * @param messageId 短信记录id
     * @param phones 接收短信的手机号码
     * @return
     */
    int saveMessageDetail(int messageId, List<String> phones);

    /**
     * 取出当前短信营销链接的最大id
     * 
     * @return
     */
    int getMaxId4ShortUrl();

    /**
     * 保存短信营销链接信息
     * @param id 短信营销链接信息的ID ， 不是自动生成。
     * @param longUrl 长链接
     * @param shortUrl 短链接
     * @param messageId 短息id
     */
    int saveLinkInfo(int id, String longUrl, String shortUrl, int messageId);

    /**
     * 更新短信营销链接信息访问次数
     * @param id
     */
    int updateLinkInfo(int id);

    /**
     * 访问明细
     * @param remoteIpAddr
     * @param id
     */
    int saveLinkDetail(String remoteIpAddr, int id);

    /**
     * 发送短信记录列表
     * @param param
     * @return
     */
    List<Map<String, Object>> findSmsList(Map<String, Object> param);

    /**
     * 统计发送短信记录列表条数
     * @param param
     * @return
     */
    int countSmsList(Map<String, Object> param);

    /**
     * 统计短信id对应的数据
     * @param id
     * @return
     */
    Map<String, Object> statisticsResult(int id);

    int saveSendMessageTask(int groupId, int linkInfoId, String content, int contentType, String shortUrl, String longUrl, int filterType, String filterDay, String sendTime);

    List<Map<String, Object>> findSendMessageTask();

    int updateMessageTask(int id);
    
}


