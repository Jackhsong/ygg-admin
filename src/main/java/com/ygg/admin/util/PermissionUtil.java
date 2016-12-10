package com.ygg.admin.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;

import com.ygg.admin.code.ControllerMappingEnum;

public class PermissionUtil
{
    private static Logger logger = Logger.getLogger(PermissionUtil.class);
    
    public static void main(String[] args)
    {
        
        Set<Class<?>> classs = getClasses("com.ygg.admin.controller");
        for (Class<?> clazz : classs)
        {
            System.out.println(clazz.getName() + "---" + clazz.getSimpleName());
        }
    }
    
    public static boolean isControllerMappingContains(String controllerName)
    {
        for (ControllerMappingEnum e : ControllerMappingEnum.values())
        {
            if (e.getControllerName().equals(controllerName))
            {
                return true;
            }
        }
        return false;
    }
    
    public static Set<Class<?>> getClasses(String packageName)
    {
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        // 是否循环迭代  
        boolean recursive = true;
        // 获取包的名字 并进行替换  
        String packageDirName = packageName.replace('.', '/');
        Enumeration<URL> dirs;
        try
        {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements())
            {
                URL url = dirs.nextElement();
                // 得到协议的名称  
                String protocol = url.getProtocol();
                // 如果是以文件的形式保存在服务器上  
                if ("file".equals(protocol))
                {
                    // 获取包的物理路径，以文件的方式扫描整个包下的文件 并添加到集合中  
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    findClassesInPackageByFile(packageName, filePath, recursive, classes);
                }
                else if ("jar".equals(protocol))
                {
                    // 如果是jar包文件,定义一个JarFile  
                    JarFile jar;
                    try
                    {
                        jar = ((JarURLConnection)url.openConnection()).getJarFile();
                        Enumeration<JarEntry> entries = jar.entries();
                        while (entries.hasMoreElements())
                        {
                            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件  
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            // 如果是以/开头的  
                            if (name.charAt(0) == '/')
                            {
                                // 获取后面的字符串  
                                name = name.substring(1);
                            }
                            // 如果前半部分和定义的包名相同  
                            if (name.startsWith(packageDirName))
                            {
                                int idx = name.lastIndexOf('/');
                                // 如果以"/"结尾 是一个包  
                                if (idx != -1)
                                {
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                // 如果可以迭代下去 并且是一个包  
                                if ((idx != -1) || recursive)
                                {
                                    if (name.endsWith(".class") && !entry.isDirectory())
                                    {
                                        // 去掉后面的".class" 获取真正的类名  
                                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                                        //过滤内部类
                                        if (!className.contains("$"))
                                        {
                                            try
                                            {
                                                classes.add(Class.forName(packageName + '.' + className));
                                            }
                                            catch (ClassNotFoundException e)
                                            {
                                                logger.info(className + "not found");
                                            }
                                        }
                                        else
                                        {
                                            logger.debug("扫描到内部" + className + "类跳过");
                                        }
                                    }
                                }
                            }
                        }
                    }
                    catch (IOException e)
                    {
                        logger.error("扫描jar文件出错", e);
                    }
                }
            }
        }
        catch (IOException e)
        {
            logger.error("扫描" + packageName + "出错", e);
        }
        return classes;
    }
    
    public static void findClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, Set<Class<?>> classes)
    {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory())
        {
            return;
        }
        File[] dirfiles = dir.listFiles(new FileFilter()
        {
            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)  
            public boolean accept(File file)
            {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });
        for (File file : dirfiles)
        {
            if (file.isDirectory())
            {
                findClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
            }
            else
            {
                // 如果是java类文件 去掉后面的.class 只留下类名  
                String className = file.getName().substring(0, file.getName().length() - 6);
                if (!className.contains("$"))
                {
                    try
                    {
                        classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
                    }
                    catch (ClassNotFoundException e)
                    {
                        logger.info(className + "not found");
                    }
                }
                else
                {
                    logger.debug("扫描到内部" + className + "类跳过");
                }
            }
        }
    }
}
