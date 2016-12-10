package com.ygg.admin.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

public class ParamTrimFilterFilter implements Filter
{
    
    public void init(FilterConfig filterConfig)
        throws ServletException
    {
        
    }
    
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
        throws IOException, ServletException
    {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)resp;
        chain.doFilter(new ParamTrimRequest(request), response);
    }
    
    public void destroy()
    {
        
    }
    
}

/**
 * 对请求参数进行去空格
 */
class ParamTrimRequest extends HttpServletRequestWrapper
{
    private HttpServletRequest request = null;
    
    public ParamTrimRequest(HttpServletRequest request)
    {
        super(request);
        this.request = request;
    }
    
    @Override
    public String getParameter(String name)
    {
        String value = super.getParameter(name);
        if (value == null)
            return null;
        String method = request.getMethod();
        if ("get".equalsIgnoreCase(method) || "post".equalsIgnoreCase(method))
        {
            value = value.trim();
        }
        return value;
    }
    
    @Override
    public String[] getParameterValues(String name)
    {
        String[] r = super.getParameterValues(name);
        String method = request.getMethod();
        if ("get".equalsIgnoreCase(method) || "post".equalsIgnoreCase(method))
        {
            if (r != null)
            {
                for (int i = 0; i < r.length; i++)
                {
                    if (r != null)
                        r[i] = r[i].trim();
                }
            }
            
        }
        return r;
    }
    
}
