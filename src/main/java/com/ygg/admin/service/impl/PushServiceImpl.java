package com.ygg.admin.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.ygg.admin.dao.CrmDao;
import com.ygg.admin.entity.PushEntity;
import com.ygg.admin.service.PushService;
import com.ygg.admin.util.PushBase;

/**
 * 推送到APP端
 * 
 * @author Administrator
 *
 */
@Service("pushService")
public class PushServiceImpl implements PushService
{
    
    /**
     * 左岸城堡 + 左岸城堡iOS【1】-globalFood + 左岸城堡iOS【2】女神版-chengjijin + 左岸城堡iOS【2】女神版-likaijun + 左岸城堡iOS【3】专业版-gegejiazy
     */
    public static final String[] J_APP_IDS = "vxUFsZ2uhe7Bw8DqgOvqRA,ahCvPgc4Y78fIf78iS2IK7,0svukTEPPc9dWKww1emH59,ypGd2BVS5n8P4FEg9G1vh,10uazjaI6D7idA6MLpi2H7".split(",");
    public static final String[] J_APP_KEYS = "ps31GCEY3n6mt3KBIP1LC,XrhE99z5PVA0GD4BVH9mK4,s7IpPTzHid9UthvAfFQzk6,hzycVIsGUL9L5KUymuKeyA,oaP4xWLAlX6Zja1APiAjV3".split(",");
    public static final String[] J_MASTER_SECRETS = "x6iLoqrZB6865AalOWY5UA,go0SzMzUtw6KWQMQL0dsT6,P0PkyVKsbu5rDHEJNPLVIA,zkWmvyDIOtAoZ5kiVIdvP9,eWCQaqecwt9B015VwGkyT8".split(",");
    
    
    /**
     * 左岸城堡（美食版）iOS【1】
     */
    public static final String[] T_APP_IDS = "QMvcDvcfCW8F63dMsRIGDA".split(",");
    public static final String[] T_APP_KEYS = "bgzcjZcUAu7c6x1gkwfce4".split(",");
    public static final String[] T_MASTER_SECRETS = "6lSrIKapHQ8OVROLOfGx49".split(",");
    
    
    @Resource
    private CrmDao crmDao;
    
    @Override
    public Object push(Map<String, String> param, String[] platfrom)
    {
        
        return null;
    }
    
    
    public void pushMessage(PushEntity entity) throws Exception {
        // 1：推送对象，左岸城堡所有APP
        if(StringUtils.equals("1", entity.getPushType())) {
            pushJAppMessages(entity);
        // 2：推送对象，左岸城堡所有APP
        } else if(StringUtils.equals("2", entity.getPushType())) {
            pushTAppMessages(entity);
        // 3：推送对象，用户分组
        } else if(StringUtils.equals("3", entity.getPushType())) {
            // 提取用户分组的类型，
            entity.setPushType(getPushType(entity.getGroupId()));
            // 提取用户分组对应的用户，
            
            
            
            
            
            
            
            
            
            
            //PushBase.pushGroup(appId, appKey, masterSecret, list, entity);
            
        } else {
            throw new RuntimeException("未知的推送类型");
        }
    }
    
    /**
     * 根据用户分组信息，读取对应的推送类型
     * @param groupId
     * @return
     */
    private String getPushType(String groupId) {
        Map<String, Object> group = crmDao.findGroupById(groupId);
        if(group == null || group.get("type") == null)
            throw new RuntimeException("没有可用的分组");

        String type = String.valueOf(group.get("type"));
        // 0：左岸城堡
        if(StringUtils.equals(type, "0"))
            return "1";
        // 1：左岸城堡
        if(StringUtils.equals(type, "1"))
            return "2";
        // 2：左岸城堡

        if(StringUtils.equals(type, "2")) 
            throw new RuntimeException("左岸城堡还没有开通“个推功能”，请联系相关工作人员");
        return "";
    }
    
    /**
     * 推送给左岸城堡所有应用
     * 
     * @param desc
     * @param title
     * @param url
     * @param content
     * @throws Exception 
     */
    public void pushJAppMessages(PushEntity entity) throws Exception {
        for (int i = 0; i < J_APP_IDS.length; i++) {
            PushBase.pushAppMessage(J_APP_IDS[i], J_APP_KEYS[i], J_MASTER_SECRETS[i], entity);
        }
    }
    
    /**
     * 左岸城堡所有应用
     * @param desc
     * @param title
     * @param url
     * @param content
     * @throws Exception 
     */
    public void pushTAppMessages(PushEntity entity) throws Exception {
        for (int i = 0; i < T_APP_IDS.length; i++) {
            PushBase.pushAppMessage(T_APP_IDS[i], T_APP_KEYS[i], T_MASTER_SECRETS[i], entity);
        }
    }
}
