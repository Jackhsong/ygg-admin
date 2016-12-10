package com.ygg.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class YggCommonProperties
{
    
    private Logger logger = Logger.getLogger(YggCommonProperties.class);
    
    private static YggCommonProperties instance = new YggCommonProperties();
    
    private Properties props = null;
    
    private YggCommonProperties()
    {
        props = new Properties();
        init();
    }
    
    private void init()
    {
        
        InputStream in = YggCommonProperties.class.getClassLoader().getResourceAsStream("yggCommon.properties");
        try
        {
            if (props == null)
                props = new Properties();
            
            props.load(in);
            
        }
        catch (IOException e)
        {
            logger.error("IOException", e);
        }
        finally
        {
            try
            {
                in.close();
            }
            catch (IOException e)
            {
                logger.error("IOException", e);
            }
        }
    }
    
    public static YggCommonProperties getInstance()
    {
        return instance;
    }
    
    public String getProperties(String key)
    {
        if (props == null)
            init();
        
        Object value = props.get(key);
        if (value == null)
            return null;
        return value.toString();
    }
}
