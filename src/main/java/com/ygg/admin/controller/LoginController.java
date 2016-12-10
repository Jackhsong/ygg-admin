package com.ygg.admin.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.cache.CacheManager;
import com.ygg.admin.cache.CacheServiceIF;
import com.ygg.admin.entity.User;
import com.ygg.admin.service.ShiroService;
import com.ygg.admin.service.UserService;
import com.ygg.admin.util.CacheConstant;
import com.ygg.admin.util.CommonUtil;
import com.ygg.admin.util.YimeiUtil;

@Controller
@RequestMapping("/auth")
public class LoginController
{
    
    Logger log = Logger.getLogger(LoginController.class);
    
    private CacheServiceIF cache = CacheManager.getClient();
    
    @Resource
    private ShiroService shiroService;
    
    @Resource
    private UserService userService;
    
    /**
     * 登陆页面
     * 
     * @param request
     * @return
     */
    @RequestMapping("/tlogin")
    public ModelAndView loginForm(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("auth/login");
        return mv;
    }
    
    //    /**
    //     * 登陆 ACTION
    //     * 
    //     * @param request
    //     * @return
    //     * @throws Exception
    //     */
    //    @RequestMapping(value = "/jlogin", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    //    @ResponseBody
    //    public String login(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "name", required = true) String name,
    //        @RequestParam(value = "pwd", required = true) String pwd, @RequestParam(value = "code", required = true) String code)
    //        throws Exception
    //    {
    //        response.setContentType("application/json; charset=utf-8");
    //        Object sessionCode = request.getSession().getAttribute("verifyCode");
    //        if (sessionCode == null || !(sessionCode + "").equalsIgnoreCase(code))
    //        {
    //            Map<String, Object> map = new HashMap<String, Object>();
    //            map.put("status", 0);
    //            map.put("msg", "验证码错误");
    //            return JSON.toJSONString(map);
    //        }
    //        Map<String, Object> result = managerService.login(name, pwd);
    //        Object md5pwd = result.get("md5pwd");
    //        if (md5pwd != null)
    //        {
    //            String currentUserId = result.get("userId") + "";
    //            request.getSession().setAttribute("currentUserId", currentUserId);
    //            // 写入用户cookie
    //            String cookie_vlaue = md5pwd + "_" + currentUserId;
    //            Cookie cookie = new Cookie("gegeupd", String.valueOf(cookie_vlaue).toString());
    //            cookie.setMaxAge(60 * 60 * 24 * 30 * 1); // 保留1月
    //            cookie.setPath(request.getContextPath() + "/");
    //            response.addCookie(cookie);
    //        }
    //        log.info(result);
    //        return JSON.toJSONString(result);
    //    }
    
    /**
     * shiro
     * 
     * @param request
     * @param response
     * @param name
     * @param pwd
     * @param code
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/shiroLogin", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "管理员管理-登录")
    public String shiroLogin(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "name", required = true) String name,
        @RequestParam(value = "pwd", required = true) String pwd, @RequestParam(value = "code", required = true) String code)
        throws Exception
    {
        Object sessionCode = request.getSession().getAttribute("verifyCode");
        if (sessionCode == null || !(sessionCode + "").equalsIgnoreCase(code))
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", 0);
            map.put("msg", "验证码错误");
            return JSON.toJSONString(map);
        }
        pwd = CommonUtil.strToMD5(pwd + name);
        Subject currentUser = shiroService.login(name, pwd);
        if (currentUser == null)
        {
            // 登陆失败
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", 0);
            String msg = "用户名或者密码错误";
            //失败判断
            Object lockedName = cache.get(CacheConstant.ADMIN_ADMINREALM_LOCKED_USER + name);
            if (lockedName != null)
            {
                msg = "该用户已锁定";
                cache.delete(CacheConstant.ADMIN_ADMINREALM_LOCKED_USER + name);
            }
            else
            {
                Object errorPassId = cache.get(CacheConstant.ADMIN_ADMINREALM_ERROR_PASSWORD_USER + name);
                if (errorPassId != null)
                {
                    //密码错误
                    Integer loginErrorTimes =
                        Integer.parseInt(request.getSession().getAttribute("loginErrorTimes") == null ? "0" : request.getSession().getAttribute("loginErrorTimes") + "");
                    if (loginErrorTimes >= 3)
                    {
                        //锁定用户
                        int status = userService.updateUserLocked(Integer.parseInt(errorPassId + ""), 1);
                        if (status == 1)
                        {
                            msg = "该用户已锁定";
                            request.getSession().setAttribute("loginErrorTimes", null);
                        }
                        else
                        {
                            log.error("锁定用户失败，用户：" + name);
                        }
                    }
                    else
                    {
                        loginErrorTimes++;
                        request.getSession().setAttribute("loginErrorTimes", loginErrorTimes);
                    }
                    cache.delete(CacheConstant.ADMIN_ADMINREALM_ERROR_PASSWORD_USER + name);
                }
            }
            map.put("msg", msg);
            return JSON.toJSONString(map);
        }
        // 登陆成功
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", 1);
        result.put("msg", "登陆成功");
        return JSON.toJSONString(result);
    }
    
    @RequestMapping("/noPermission")
    public ModelAndView noPermission(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("auth/noPermission");
        return mv;
    }
    
    /**
     * 退出
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/lgout")
    @ControllerLog(description = "管理员管理-登出")
    public ModelAndView lgout(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
        mv.setViewName("auth/login");
        return mv;
    }
    
    /**
     * 跳转到短信验证页面
     * @return
     */
    @RequestMapping("/smsValidate")
    public ModelAndView smsValidate()
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            mv.setViewName("auth/smsValidate");
            
            String username = SecurityUtils.getSubject().getPrincipal() + "";
            User user = userService.findByUsername(username);
            if (user == null)
            {
                mv.setViewName("redirect:/auth/tlogin");
            }
            else
            {
                mv.addObject("mobileNumber", user.getMobileNumber());
                String deadline = cache.getString(CacheConstant.ADMIN_USER_LOGIN_FROM_OUTER_SMSCODE + username + user.getMobileNumber());
                if (!StringUtils.isEmpty(deadline))
                {
                    mv.addObject("isSend", 1);
                }
            }
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    /**
     * 验证短信
     * @param request
     * @param response
     * @param mobileNumber：手机号
     * @param smsCode：短信验证码
     * @return
     */
    @RequestMapping(value = "/confirmSmsValidate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String confirmSmsValidate(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "mobileNumber", required = false, defaultValue = "") String mobileNumber,
        @RequestParam(value = "smsCode", required = false, defaultValue = "") String smsCode)
    
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            if ("".equals(mobileNumber))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "手机号不存在");
                return JSON.toJSONString(resultMap);
            }
            if ("".equals(smsCode))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "请输入验证码");
                return JSON.toJSONString(resultMap);
            }
            
            String username = SecurityUtils.getSubject().getPrincipal() + "";
            User user = userService.findByUsername(username);
            if (user == null)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "请登陆");
                return JSON.toJSONString(resultMap);
            }
            
            String deadline = cache.getString(CacheConstant.ADMIN_USER_LOGIN_FROM_OUTER_SMSCODE + username + user.getMobileNumber());
            if (deadline == null)
            {
                resultMap.put("status", 2);
                resultMap.put("msg", "验证码已过期，请重新获取验证码");
                return JSON.toJSONString(resultMap);
            }
            
            if (!mobileNumber.equals(user.getMobileNumber()))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "手机号非本人");
                return JSON.toJSONString(resultMap);
            }
            if (!smsCode.equals(user.getSmsCode()))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "验证码错误");
                return JSON.toJSONString(resultMap);
            }
            resultMap.put("status", 1);
            resultMap.put("msg", "登陆成功");
            cache.set(CacheConstant.ADMIN_USER_LOGIN_FROM_OUTER + username + user.getMobileNumber(), username + user.getMobileNumber(), CacheConstant.ADMIN_USER_LOGIN_FROM_OUTER_CACHE_TIME_HOUR_OF_6);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "服务器发生异常");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 外网登陆用户发送短信验证码
     * @param mobileNumber
     * @return
     */
    @RequestMapping(value = "/sendSmsCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String sendSmsCode(@RequestParam(value = "mobileNumber", required = true) String mobileNumber)
    
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            String username = SecurityUtils.getSubject().getPrincipal() + "";
            Integer count = cache.getInt(CacheConstant.ADMIN_USER_LOGIN_SEND_SMS_COUNT + username + mobileNumber);
            if (count != null && count.intValue() > 10)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "今日发送短信请求已达最大限制，请明日再试。");
                return JSON.toJSONString(resultMap);
            }
            User user = userService.findByUsername(username);
            if (user == null)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "请登陆");
                return JSON.toJSONString(resultMap);
            }
            if (!mobileNumber.equals(user.getMobileNumber()))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "手机号非本人");
                return JSON.toJSONString(resultMap);
            }
            
            int code = CommonUtil.randomIntCode(4);
            String smsContent = "您本次的验证码为：" + code + ",验证码有效时间为10分钟。";
            user.setSmsCode(code + "");
            user.setMobileNumber(mobileNumber);
            userService.updateUser(user);
            YimeiUtil.sendSMS(mobileNumber, smsContent, 5);
            //发送短信从亿美改成梦网
            //MontnetsUtil.sendSms(mobileNumber, smsContent);
            cache.set(CacheConstant.ADMIN_USER_LOGIN_FROM_OUTER_SMSCODE + username + user.getMobileNumber(), DateTime.now().plusMinutes(10).toString("yyyy-MM-dd HH:mm:ss"), CacheConstant.ADMIN_USER_LOGIN_FROM_OUTER_SMS_DEADLINE_CACHE_TIME_MINUTE_OF_10);
            resultMap.put("status", 1);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "服务器发生异常");
        }
        return JSON.toJSONString(resultMap);
    }
}
