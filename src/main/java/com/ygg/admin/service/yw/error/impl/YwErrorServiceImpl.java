package com.ygg.admin.service.yw.error.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.ygg.admin.dao.yw.error.YwAccountDao;
import com.ygg.admin.dao.yw.error.YwErrorDao;
import com.ygg.admin.dao.yw.error.YwFansDao;
import com.ygg.admin.entity.yw.YwAccountEntity;
import com.ygg.admin.entity.yw.YwAccountRelaChangeLogEntity;
import com.ygg.admin.entity.yw.YwFansEntity;
import com.ygg.admin.service.qqbserror.impl.QqbsErrorServcieImpl;
import com.ygg.admin.service.yw.error.YwErrorSerivce;

@Service("ywErrorServcie")
public class YwErrorServiceImpl implements YwErrorSerivce
{
    @Resource(name = "ywErrorDao")
    private YwErrorDao ywErrorDao;
    
    @Resource(name = "ywAccountDao")
    private YwAccountDao ywAccountDao;
    
    @Resource(name = "ywFansDao")
    private YwFansDao ywFansDao; 
    
    private static Logger logger = Logger.getLogger(QqbsErrorServcieImpl.class);
    
    @Override
    public Map<String, Object> findListInfo(Map<String, Object> param)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = ywErrorDao.findListInfo(param);
        for (Map<String, Object> map : infoList)
        {
            map.put("createTime", ((Timestamp)map.get("createTime")).toString());
        }
        result.put("rows", infoList);
        result.put("total", ywErrorDao.getCountByParam(param));
        return result;
    }
    
    
    /**
     * 处理用户关系
     * @param accountId 用户ID
     * @param tuiAcountId 推荐人ID
     */
    public String updateAccountRela(int accountId,int tuiAcountId,String remark){
        String s = null;
        //1.判断推荐人ID是否存在
        YwAccountEntity tui = ywAccountDao.findAccountByAccountId(tuiAcountId);
        if(tui != null){
            //2.获取用户
            YwAccountEntity one = ywAccountDao.findAccountByAccountId(accountId);
            if(one != null){
                if(tuiAcountId == one.getFatherAccountId()){
                    s =  "处理失败:推荐人ID"+tuiAcountId+"已经是用户ID"+accountId+"的推荐人,不进行处理!";
                }else{
                    if(one.getFatherAccountId() == 0){
                        // 插入QqbsAccountEntity
                        Map<String, Object> para = new HashMap<String, Object>();
                        para.put("id", one.getId());
                        para.put("fatherAccountId", tuiAcountId);
                        ywAccountDao.updateAccounSpread(para);
                        //正式处理开始
                        processFansInfo(one, tuiAcountId);
                        //记录日志
                        YwAccountRelaChangeLogEntity log = new YwAccountRelaChangeLogEntity();
                        log.setAccountId(accountId);
                        log.setTuiAccountId(tuiAcountId);
                        log.setOperator(String.valueOf(SecurityUtils.getSubject().getPrincipal()));
                        log.setRemark(remark);
                        ywErrorDao.insert(log);
                        s = "处理成功";
                    }else
                    {
                        s = "修改失败,用户id="+accountId+"已有推荐人 ";
                    }
                }
            }else{
                s = "处理失败:用户ID="+accountId+"不存在";                
            }
        }else{
            s = "处理失败:推荐人ID="+tuiAcountId+"不存在";
        }
        logger.info(s);
        return s;
    }
    
    
    /**
     * 处理粉丝关系
     * @param accountId 当前用户基本Id
     * @param userInfo 当前用户微信信息
     * @throws Exception
     */
    private void processFansInfo(YwAccountEntity one, int fatherAccountId)
    {
        // 1.当前用户成为推荐人的1级粉丝
        //oneFans指推荐人
        YwAccountEntity tuij = ywAccountDao.findAccountByAccountId(fatherAccountId);
        if (tuij != null)
        {
            YwFansEntity qfe = new YwFansEntity();
            qfe.setAccountId(tuij.getAccountId());
            qfe.setFansAccountId(one.getAccountId());
            qfe.setLevel(1);
            qfe.setFansImage(one.getImage());
            qfe.setFansNickname(one.getNickName());
            qfe.setCreateTime(one.getCreateTime());
            ywFansDao.insertFans(qfe);
            //查找当前用户的一级粉丝，成为推荐人的二级粉丝
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("accountId", one.getAccountId());
            para.put("level", 1);
            List<YwFansEntity> onelist = ywFansDao.getFansList(para);
            insertFans(onelist, 2, tuij.getAccountId());
            //查找当前用户的二级粉丝，成为推荐人的三级粉丝
            para.put("level", 2);
            List<YwFansEntity> twolist = ywFansDao.getFansList(para);
            insertFans(twolist, 3, tuij.getAccountId());
            // 2.当前用户成为推荐人上级的2级粉丝
            // 取推荐人的用户信息信息
            if (tuij.getFatherAccountId() != 0)
            {
                YwAccountEntity secondFans = ywAccountDao.findAccountByAccountId(tuij.getFatherAccountId());
                if (secondFans != null)
                {
                    YwFansEntity efe = new YwFansEntity();
                    efe.setAccountId(secondFans.getAccountId());
                    efe.setFansAccountId(one.getAccountId());
                    efe.setLevel(2);
                    efe.setFansImage(one.getImage());
                    efe.setFansNickname(one.getNickName());
                    efe.setCreateTime(one.getCreateTime());
                    ywFansDao.insertFans(efe);
                    //查找当前用户的一级粉丝 ,成为推荐人父类的三级粉丝
                    insertFans(onelist, 3, tuij.getFatherAccountId());
                    // 3.当前用户成为推荐人上级 上级的3级粉丝
                    // 取推荐人上级的用户信息信息
                    if (secondFans.getFatherAccountId() != 0)
                    {
                        YwAccountEntity thirdFans = ywAccountDao.findAccountByAccountId(secondFans.getFatherAccountId());
                        if (thirdFans != null)
                        {
                            YwFansEntity tfe = new YwFansEntity();
                            tfe.setAccountId(thirdFans.getAccountId());
                            tfe.setFansAccountId(one.getAccountId());
                            tfe.setLevel(3);
                            tfe.setFansImage(one.getImage());
                            tfe.setFansNickname(one.getNickName());
                            tfe.setCreateTime(one.getCreateTime());
                            ywFansDao.insertFans(tfe);
                        }
                    }
                }
            }
        }
        else
        {
            logger.error(" 推荐人ID= " + fatherAccountId + "在qqbs_account表中找不到");
        }
    }
    
    
    /**
     * a的粉丝成为a的父类的粉丝
     * @param list
     * @param level
     * @param fatherAccountId
     */
    private void insertFans(List<YwFansEntity> list,int level,int fatherAccountId){
        if(list != null && list.size()>0){
            for(YwFansEntity qf : list){
                YwFansEntity efe = new YwFansEntity();
                efe.setAccountId(fatherAccountId);
                efe.setFansAccountId(qf.getFansAccountId());
                efe.setLevel(level);
                efe.setFansImage(qf.getFansImage());
                efe.setFansNickname(qf.getFansNickname());
                efe.setCreateTime(qf.getCreateTime());
                ywFansDao.insertFans(efe);
            }
        }
    }
}
