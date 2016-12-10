package com.ygg.admin.service;

import java.util.HashMap;
import java.util.Map;

public interface CrmService
{
    
    /**
     * 筛选用户
     * @param param
     * @param level
     * @param commentLevel
     * @param province
     * @param saleFlag
     * @param brand
     * @return
     * @throws Exception
     */
    Map<String, Object> filterByParam(HashMap<String, String> param, String[] level, String[] commentLevel,
        String[] province, String[] saleFlag, String[] brand) throws Exception;

    /**
     * 保存筛选结果
     * @param param
     * @return
     */
    int saveFilterResult(Map<String, String> param);
    
    /**
     * 查询筛选列表
     * @param param
     * @return
     * @throws Exception
     */
    Map<String, Object> findGroupList(Map<String, Object> param) throws Exception;
    
    /**
     * 删除分组信息
     * @param groupId 分组id
     * @return
     * @throws Exception
     */
    int deleteGroupInfo(int groupId) throws Exception;

    /**
     * 查询分组明细
     * @param param
     * @return
     */
    Map<String, Object> findGroupDetailByGroupId(Map<String, Object> param);

    /**
     * 发送短信
     * @param phone 手机号码。仅测试使用
     * @param content 发送内容
     * @param groupId 分组id
     * @param contentType 产品线
     * @param filterType 过滤表中
     * @return
     */
    Object saveSms(String sendType, String phone, String content, int groupId, int contentType, int filterType, String filterDay, String longUrl, String shortUrl, int linkInfoId, String sendTime);

    /**
     * 定时任务，每分钟跑一次
     * @return
     */
    void sendMessageTask();
    
    /**
     * 生成短连接
     * @param url
     * @return
     */
    Map<String, String> generateShortUrl(String url, String groupId) throws Exception;

    /**
     * 统计链接打开的次数
     * @param remoteIpAddr
     * @param id
     */
    void updateStatistics(String remoteIpAddr, int id);

    /**
     * 发送短信列表
     * @param param
     * @return
     */
    Map<String, Object> smsList(Map<String, Object> param);

    /**
     * 统计短信id对应的数据
     * @param id
     * @return
     */
    Map<String, Object> statisticsResult(int id);
    
    void cacheMobileNumber();
    
}
