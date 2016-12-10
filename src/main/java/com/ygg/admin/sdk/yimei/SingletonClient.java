package com.ygg.admin.sdk.yimei;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class SingletonClient
{
    private static Client client = null;
    
    private SingletonClient()
    {
    }
    
    public synchronized static Client getClient(String softwareSerialNo, String key)
    {
        if (client == null)
        {
            try
            {
                client = new Client(softwareSerialNo, key);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return client;
    }
    
    public synchronized static Client getClient()
    {
        ResourceBundle bundle = PropertyResourceBundle.getBundle("yimei");
        if (client == null)
        {
            try
            {
                client = new Client(bundle.getString("softwareSerialNo"), bundle.getString("key"));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return client;
    }
    
}
