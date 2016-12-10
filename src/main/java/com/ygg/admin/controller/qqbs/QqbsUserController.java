package com.ygg.admin.controller.qqbs;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ygg.admin.entity.qqbs.QqbsAccountEntity;
import com.ygg.admin.service.qqbs.user.IQqbsUserService;
import com.ygg.admin.util.StringUtils;

/**
 * @author wuhy
 * @date 创建时间：2016年5月16日 上午9:27:40
 */
@Controller
@RequestMapping("/qqbsUser")
public class QqbsUserController
{
    @Resource
    private IQqbsUserService qqbsUserService;
    
    @RequestMapping(value = "/index")
    public ModelAndView list()
    {
        ModelAndView mv = new ModelAndView("qqbs/qqbsUser/qqbsUserIndex");
        return mv;
    }
    
    @RequestMapping(value = "/query", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, Object> query(HttpServletRequest request, @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "rows", defaultValue = "50") int rows, @RequestParam(value = "accountId", required = false) String accountId,
        @RequestParam(value = "nickName", required = false) String nickName)
    {
        
        return qqbsUserService.findQqbsUsersAndCount(buildQueryCriteria(accountId, nickName, page, rows));
        
    }
    
    private QqbsAccountEntity buildQueryCriteria(String accountId, String nickName, int page, int rows)
    {
        QqbsAccountEntity qqbsUserQueryCriteria = new QqbsAccountEntity();
        qqbsUserQueryCriteria.setAccountId(StringUtils.isEmpty(accountId) ? 0 : Integer.valueOf(accountId));
        qqbsUserQueryCriteria.setNickName(StringUtils.isEmpty(nickName) ? null : nickName);
        if (page == 0)
        {
            page = 1;
        }
        qqbsUserQueryCriteria.setStart(rows * (page - 1));
        qqbsUserQueryCriteria.setMax(rows);
        return qqbsUserQueryCriteria;
    }
    
}
