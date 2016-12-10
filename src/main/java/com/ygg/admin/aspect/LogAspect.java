package com.ygg.admin.aspect;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.elasticsearch.common.joda.time.DateTime;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.annotation.ServiceLog;
import com.ygg.admin.config.YggAdminProperties;
import com.ygg.admin.entity.User;
import com.ygg.admin.service.SystemLogService;
import com.ygg.admin.service.UserService;
import com.ygg.admin.util.CommonUtil;
import com.ygg.admin.util.MailUtil;
import com.ygg.admin.util.StringUtils;

/**
 * 日志切面
 * @author xiongl
 *
 */
@Aspect
public class LogAspect
{
    private static final Logger logger = Logger.getLogger(LogAspect.class);
    
    private static String[] emails = {"xiongl@yangege.com", "zhangyb@yangege.com"};
    
    @Resource
    private UserService userService;
    
    @Resource
    private SystemLogService logService;
    
    @Pointcut("@annotation(com.ygg.admin.annotation.ControllerLog)")
    public void controllerAspect()
    {
        
    }
    
    @Pointcut("@annotation(com.ygg.admin.annotation.ServiceLog)")
    public void serviceAspect()
    {
        
    }
    
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint)
    {
        try
        {
            HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
            String ip = CommonUtil.getRemoteIpAddr(request);//请求ip
            String uri = request.getRequestURI();//请求uri
            String loginName = SecurityUtils.getSubject().getPrincipal() + "";//当前用户登录名
            String realName = "";
            String userId = "0";
            User user = userService.findByUsername(loginName);
            if (user != null)
            {
                realName = user.getRealname();
                userId = user.getId() + "";
            }
            //方法名：类名.方法名
            String fullMethodName = joinPoint.getTarget().getClass().getSimpleName() + "." + joinPoint.getSignature().getName() + "()";
            
            String description = getControllerLogDescription(joinPoint);//控制器描述
            String params = getParameter(request);//参数
            String operation = getOperation(joinPoint);//操作类型
            
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("userId", userId);
            para.put("username", realName);
            para.put("operation", operation);
            para.put("content", description);
            para.put("method", fullMethodName);
            para.put("params", params);
            para.put("url", uri);
            para.put("ip", ip);
            logService.log(para);
        }
        catch (Exception e)
        {
            logger.error("前置通知出错了", e);
        }
    }
    
    @AfterThrowing(pointcut = "serviceAspect()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e)
    {
        try
        {
            HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
            String ip = CommonUtil.getRemoteIpAddr(request);//请求ip
            String url = request.getRequestURL().toString();//请求url
            String loginName = SecurityUtils.getSubject().getPrincipal() + "";//当前用户登录名
            String realName = "";
            String userId = "";
            User user = userService.findByUsername(loginName);
            if (user != null)
            {
                realName = user.getRealname();
                userId = user.getId() + "";
            }
            //方法名：类名.方法名
            String fullMethodName = joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName();
            String description = getServiceLogDescription(joinPoint);//控制器描述
            String params = getParameter(request);//参数
            String operation = getOperation(joinPoint);//操作类型
            String operationTime = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
            
            StringBuffer sb = new StringBuffer();
            sb.append("用户Id：")
                .append(userId)
                .append("<br/><br/>")
                .append("用户名：")
                .append(realName)
                .append("<br/><br/>")
                .append("操作：")
                .append(operation)
                .append("<br/><br/>")
                .append("业务：")
                .append(description)
                .append("<br/><br/>")
                .append("方法名：")
                .append(fullMethodName)
                .append("<br/><br/>")
                .append("参数：")
                .append(params)
                .append("<br/><br/>")
                .append("IP地址：")
                .append(ip)
                .append("<br/><br/>")
                .append("url：")
                .append(url)
                .append("<br/><br/>")
                .append("操作时间：")
                .append(operationTime)
                .append("<br/><br/>")
                .append("异常信息：")
                .append(e.getMessage())
                .append("<br/><br/>")
                .append(e);
            
            String emailSwitch = YggAdminProperties.getInstance().getPropertie("email_switch");
            if ("1".equals(emailSwitch))
            {
                MailUtil.sendHTMLMail(emails, "异常提醒", sb.toString());
            }
        }
        catch (Exception ex)
        {
            //记录本地异常日志    
            logger.error("异常通知出错了", ex);
        }
        
    }
    
    private String getServiceLogDescription(JoinPoint joinPoint)
        throws Exception
    {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class<?> targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method : methods)
        {
            if (method.isAnnotationPresent(ServiceLog.class))
            {
                if (method.getName().equals(methodName))
                {
                    Class<?>[] clazzs = method.getParameterTypes();
                    if (clazzs.length == arguments.length)//重载方法可能会有问题
                    {
                        description = method.getAnnotation(ServiceLog.class).description();
                        break;
                    }
                }
            }
        }
        return description;
    }
    
    private String getControllerLogDescription(JoinPoint joinPoint)
        throws Exception
    {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class<?> targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method : methods)
        {
            if (method.isAnnotationPresent(ControllerLog.class))
            {
                if (method.getName().equals(methodName))
                {
                    Class<?>[] clazzs = method.getParameterTypes();
                    if (clazzs.length == arguments.length)//重载方法可能会有问题
                    {
                        description = method.getAnnotation(ControllerLog.class).description();
                        break;
                    }
                }
            }
        }
        return description;
    }
    
    private String getParameter(HttpServletRequest request)
        throws Exception
    {
        Map<String, Object> params = new HashMap<String, Object>();
        Enumeration<String> paramNames = request.getParameterNames();
        if (paramNames != null)
        {
            while (paramNames.hasMoreElements())
            {
                String name = paramNames.nextElement();
                String value = request.getParameter(name);
                if (StringUtils.isNotEmpty(value))
                {
                    params.put(name, value);
                }
            }
        }
        return JSON.toJSONString(params);
    }
    
    private String getOperation(JoinPoint joinPoint)
    {
        String methodName = joinPoint.getSignature().getName().toLowerCase();
        if (methodName.startsWith("query") || methodName.startsWith("find") || methodName.startsWith("list") || methodName.startsWith("json") || methodName.endsWith("list"))
        {
            return "查询";
        }
        else if (methodName.startsWith("insert") || methodName.startsWith("add") || methodName.startsWith("save"))
        {
            return "新增";
        }
        else if (methodName.startsWith("update"))
        {
            return "修改";
        }
        else if (methodName.startsWith("delete"))
        {
            return "删除";
        }
        else if (methodName.startsWith("export"))
        {
            return "导出";
        }
        else if (methodName.startsWith("import"))
        {
            return "导入";
        }
        else
        {
            return methodName;
        }
    }
}
