package com.ygg.admin.interceptor;

import com.ygg.admin.cache.CacheManager;
import com.ygg.admin.cache.CacheServiceIF;
import com.ygg.admin.util.CacheConstant;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author zhangyb
 *         
 */
public class RestrictAccessInterceptor implements HandlerInterceptor
{
    
    Logger log = Logger.getLogger(RestrictAccessInterceptor.class);
    
    private CacheServiceIF cache = CacheManager.getClient();
    
    /**
     * 代拦截 url map 配置
     */
    Map<String, Integer> restrictAccessMap = new HashMap<String, Integer>()
    {
        {
            put("analyze_todaySaleTop", 3);
            put("analyze_monthAnalyze", 3);
            put("analyze_provinceTurnOverAnalyze", 3);
            put("analyze_cityTurnOverAnalyze", 3);
            put("analyze_newAccountBuyingAnalyze", 3);
            put("analyze_registAnalyze", 7);
            put("analyze_userBehaviorAnalyze", 3);
            put("analyze_userFirstBehaviorAnalyze", 3);
            put("analyze_clientBuyAnalyze", 3);
            put("analyze_appVersionBuyAnalyze", 3);
            put("analyze_seller", 3);
            put("analyze_todaySale", 3);
            put("analyze_productTurnOverAnalyze", 3);
            put("analyze_productCategoryTurnOverAnalyze", 3);
            put("analyze_product", 4);
            put("analyze_eachDayOrderSendTimeAnalyze", 3);
            put("analyze_eachDayOrderLogisticAnalyze", 3);
            put("analyze_sellerSendTimeAnalyze", 3);
            put("analyze_sellerGrossSettlement", 3);
            put("wechatGroupData_todaySaleTop", 3);
            put("wechatGroupData_productDateList", 3);
            put("wechatGroupData_monthList", 3);
            put("qqbsDataAnalyze_todaySaleTop", 3);
            put("qqbsDataAnalyze_product", 3);
            put("seller_jsonSellerGrossCalculation", 4); // 着陆页会发送一次search=0的请求，故这个实际只有30s内3次
        }
    };

    private int limitMin = 30; // 秒
    
//    @Resource
//    private UserService userService;
    
    /**
     * preHandle方法是进行处理器拦截用的，顾名思义，该方法将在Controller处理之前进行调用， SpringMVC中的Interceptor拦截器是链式的，可以同时存在
     * 多个Interceptor，然后SpringMVC会根据声明的前后顺序一个接一个的执行 ，而且所有的Interceptor中的preHandle方法都会在
     * Controller方法调用之前调用。SpringMVC的这种Interceptor链式结构也是可以进行中断的 ，这种中断方式是令preHandle的返
     * 回值为false，当preHandle的返回值为false的时候整个请求就结束了。
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception
    {
        try
        {
            String[] uri = request.getRequestURI().split("/");
            String restrictAccessKey = uri[2] + "_" + uri[3];
            if (restrictAccessMap.containsKey(restrictAccessKey))
            {
                boolean success = false;
                int maxAccessNum = restrictAccessMap.get(restrictAccessKey);
                String username = SecurityUtils.getSubject().getPrincipal() + "";
                // User user = userService.findByUsername(username);
                String cacheKeyNum = CacheConstant.ADMIN_RESTRICT_ACCESS_USER_NUM + username + restrictAccessKey;
                int accessNum = cache.get(cacheKeyNum) == null ? 1 : Integer.valueOf(cache.get(cacheKeyNum) + "");
                if (accessNum <= maxAccessNum)
                {
                    success = true;
                    accessNum++;
                    cache.set(cacheKeyNum, accessNum, limitMin);
                    String cacheKeyLastTime = CacheConstant.ADMIN_RESTRICT_ACCESS_USER_LAST_TIME + username + restrictAccessKey;
                    cache.set(cacheKeyLastTime, System.currentTimeMillis(), limitMin);
                }
                
                if (!success)
                {
                    String cacheKeyLastTime = CacheConstant.ADMIN_RESTRICT_ACCESS_USER_LAST_TIME + username + restrictAccessKey;
                    long lastTime = cache.get(cacheKeyLastTime) == null ? System.currentTimeMillis() : Long.valueOf(cache.get(cacheKeyLastTime) + "");
                    lastTime = lastTime + limitMin * 1000 - System.currentTimeMillis();
                    lastTime = lastTime > 0 ? lastTime : limitMin * 1000;
                    response.setContentType("text/html;charset=UTF-8");
                    PrintWriter writer = response.getWriter();
                    writer.println("<h3>请求过于频繁，请" + lastTime / 1000 + "秒后再试！</h3>");
                    writer.close();
                    return false;
                }
            }
        }
        catch (Exception e)
        {
            log.error("拦截统计相关请求失败！", e);
        }
        return true;
    }
    
    /**
     * 这个方法只会在当前这个Interceptor的preHandle方法返回值为true的时候才会执行。postHandle是进行处理器拦截用的， 它的执行时间是在处理器进行处理之
     * 后，也就是在Controller的方法调用之后执行，但是它会在DispatcherServlet进行视图的渲染之前执行 ，也就是说在这个方法中你可以对ModelAndView进行操
     * 作。这个方法的链式结构跟正常访问的方向是相反的，也就是说先声明的Interceptor拦截器该方法反而会后调用 ，这跟Struts2里面的拦截器的执行过程有点像，
     * 只是Struts2里面的intercept方法中要手动的调用ActionInvocation的invoke方法 ，Struts2中调用ActionInvocation的invoke方法就是调用下一个Interceptor
     * 或者是调用action，然后要在Interceptor之前调用的内容都写在调用invoke之前 ，要在Interceptor之后调用的内容都写在调用invoke方法之后。
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mv)
        throws Exception
    {
    
    }
    
    /**
     * 需要preHandle方法的返回值为true时才会执行。 该方法将在整个请求完成之后，也就是DispatcherServlet渲染了视图执行，这个方法的主要作用是用于清理资源的，
     * 当然这个方法也只能在当前这个Interceptor的preHandle方法的返回值为true时才会执行。
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e)
        throws Exception
    {
    
    }
    
}
