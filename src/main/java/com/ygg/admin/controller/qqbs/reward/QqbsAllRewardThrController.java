package com.ygg.admin.controller.qqbs.reward;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ygg.admin.entity.qqbs.QqbsAccountEntity;
import com.ygg.admin.service.qqbs.reward.QqbsAllRewardThrService;
import com.ygg.admin.util.StringUtils;


@Controller
@RequestMapping("qqbsAllRewThr")
public class QqbsAllRewardThrController
{
    private static Logger logger = Logger.getLogger(QqbsAllRewardThrController.class);
    
    @Resource
    private QqbsAllRewardThrService qqbsAllRewardThrService;
    
    @RequestMapping(value = "/list")
    public ModelAndView list()
    {
        ModelAndView mv = new ModelAndView("qqbsreward/allReward");
        return mv;
    }
    
    @RequestMapping(value = "/getAllRewardThr", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, Object> getAllRewardThr(HttpServletRequest request,  @RequestParam(value = "accountId", required = false) String accountId) throws NumberFormatException, Exception
    {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        if(StringUtils.isNotEmpty(accountId)&&Pattern.compile("[0-9]*").matcher(accountId).matches()){
            resultMap = qqbsAllRewardThrService.getAllRewardThr(Integer.valueOf(accountId));
        }else{
            resultMap.put("rows", "");
        }
        return resultMap;
    }

}
