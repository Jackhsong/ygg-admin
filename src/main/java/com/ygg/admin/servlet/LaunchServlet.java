package com.ygg.admin.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import com.ygg.admin.config.YggAdminProperties;
import com.ygg.admin.util.SpringBeanUtil;

//import com.unionpay.acp.sdk.SDKConfig;

/**
 * 初始化的Servlet
 * 
 * @author lihc
 *
 */
public class LaunchServlet extends HttpServlet
{
    
    Logger logger = Logger.getLogger(LaunchServlet.class);
    
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 配置freemarker模版的位置 在init里加载也主要是为保证Configuration实例唯一
     */
    @Override
    public void init(ServletConfig config)
        throws ServletException
    {
        logger.info("--------LaunchServlet--------init-------------");
        super.init(config);
        SpringBeanUtil.initServletContext(config.getServletContext());
        YggAdminProperties.getInstance().init();
    }
    
}
