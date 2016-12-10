package com.ygg.admin.servlet;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ygg.admin.config.AreaCache;
import com.ygg.admin.dao.AdminDao;
import com.ygg.admin.dao.AreaDao;
import com.ygg.admin.entity.CityEntity;
import com.ygg.admin.entity.DistrictEntity;
import com.ygg.admin.entity.ProvinceEntity;
import com.ygg.admin.util.PermissionUtil;

/**
 * 初始化servlet
 */
public class InitServlet
{
    Logger log = Logger.getLogger(InitServlet.class);
    
    @Resource
    private AreaDao areaDao;
    
    @Resource
    private AdminDao adminDao;
    
    /**
     * 初始化方法
     */
    private void initLoad()
        throws Exception
    {
        loadArea();
    }
    
    private void loadArea()
        throws Exception
    {
        
        log.info("缓存省市区信息 - 开始");
        List<ProvinceEntity> provinceEntityList = areaDao.findAllProvince();
        for (ProvinceEntity entity : provinceEntityList)
        {
            AreaCache.getInstance().getProvinceMap().put(entity.getProvinceId() + "", entity.getName());
        }
        
        List<CityEntity> cityEntityList = areaDao.findAllCity();
        for (CityEntity entity : cityEntityList)
        {
            AreaCache.getInstance().getCityMap().put(entity.getCityId() + "", entity.getName());
        }
        
        List<DistrictEntity> districtEntityList = areaDao.findAllDistrict();
        for (DistrictEntity entity : districtEntityList)
        {
            AreaCache.getInstance().getDistinctMap().put(entity.getDistrictId() + "", entity.getName());
        }
        
        //        System.out.println(AreaCache.getInstance().getProvinceMap());
        //        System.out.println(AreaCache.getInstance().getCityMap());
        //        System.out.println(AreaCache.getInstance().getDistinctMap());
        log.info("缓存省市区信息 - 结束");
        //refreshPermission("com.ygg.admin.controller");
    }
    
    private void refreshPermission(String packageName)
        throws Exception
    {
        log.info("刷新权限 - 开始");
        Set<Class<?>> controllers = PermissionUtil.getClasses(packageName);
        for (Class<?> clazz : controllers)
        {
            String controllerName = clazz.getSimpleName();
            if (PermissionUtil.isControllerMappingContains(controllerName))
            {
                
            }
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods)
            {
                if (method.isAnnotationPresent(RequestMapping.class))
                {
                    RequestMapping rm = method.getAnnotation(RequestMapping.class);
                    String[] values = rm.value();
                    String mapping = Arrays.toString(values).replaceAll("\\[", "").replaceAll("\\]", "").replaceFirst("/", "");
                    Map<String, Object> permission = new HashMap<>();
                    permission.put("category", controllerName);
                    permission.put("permission", controllerName + "_" + mapping.split("/")[0]);
                    permission.put("description", mapping.split("/")[0]);
                    try
                    {
                        if (adminDao.addPermission(permission) > 0)
                        {
                            log.info(String.format("新增权限%s", permission.toString()));
                        }
                    }
                    catch (Exception e)
                    {
                        if (e.getMessage().contains("Duplicate") && e.getMessage().contains("uniq_permission"))
                        {
                            log.debug("权限已经存在：" + permission.toString());
                        }
                        else
                        {
                            throw new ServletException(e);
                        }
                    }
                }
            }
        }
        log.info("刷新权限 - 结束");
    }
}
