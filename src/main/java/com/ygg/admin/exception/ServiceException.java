package com.ygg.admin.exception;

import java.util.HashMap;
import java.util.Map;

public class ServiceException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    
    Map<String, Object> map = new HashMap<String, Object>();
    
    public ServiceException()
    {
        super();
    }
    
    public ServiceException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public ServiceException(String message)
    {
        super(message);
    }
    
    public ServiceException(Throwable cause)
    {
        super(cause);
    }
    
    public Map<String, Object> getMap()
    {
        return map;
    }
    
    public Object getMap(String key)
    {
        if (this.map != null && this.map.containsKey(key))
            return this.map.get(key);
        return null;
    }
    
    public void setMap(Map<String, Object> map)
    {
        this.map = map;
    }
    
    public void putMap(String key, Object value)
    {
        if (this.map == null)
            map = new HashMap<String, Object>();
        
        this.map.put(key, value);
    }
}
