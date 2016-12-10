package com.ygg.admin.entity;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

public class PushEntity implements Serializable
{
    private static final long serialVersionUID = -1547775426036532392L;

    private String platform;
    
    private String pushType;
    
    private String groupId;
    
    private String contentType;
    
    private String productId;
    
    private String sendTime;
    
    private String offline;
    
    private String expireTime;
    
    private String androidTitle;
    
    private String androidContent;
    
    private String iosTitle;
    
    private String iosContent;
    
    private String desc;
    
    public PushEntity() {}
    
    public static PushEntity create(byte[] data) {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try
        {
            bais = new ByteArrayInputStream(data);
            ois = new ObjectInputStream(bais);
            return (PushEntity)ois.readObject();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    public byte[] getData() {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try
        {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            return baos.toByteArray();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        } finally {
            try
            {
                if(oos != null)
                    oos.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    
    
    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public String getPlatform()
    {
        return platform;
    }

    public void setPlatform(String platform)
    {
        this.platform = platform;
    }

    public String getPushType()
    {
        return pushType;
    }
    
    public void setPushType(String pushType)
    {
        this.pushType = pushType;
    }
    
    public String getGroupId()
    {
        return groupId;
    }
    
    public void setGroupId(String groupId)
    {
        this.groupId = groupId;
    }
    
    public String getContentType()
    {
        return contentType;
    }
    
    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }
    
    public String getProductId()
    {
        return productId;
    }
    
    public void setProductId(String productId)
    {
        this.productId = productId;
    }
    
    public String getSendTime()
    {
        return sendTime;
    }
    
    public void setSendTime(String sendTime)
    {
        this.sendTime = sendTime;
    }
    
    public String getOffline()
    {
        return offline;
    }
    
    public void setOffline(String offline)
    {
        this.offline = offline;
    }
    
    public String getExpireTime()
    {
        return expireTime;
    }
    
    public void setExpireTime(String expireTime)
    {
        this.expireTime = expireTime;
    }
    
    public String getAndroidTitle()
    {
        return androidTitle;
    }
    
    public void setAndroidTitle(String androidTitle)
    {
        this.androidTitle = androidTitle;
    }
    
    public String getAndroidContent()
    {
        return androidContent;
    }
    
    public void setAndroidContent(String androidContent)
    {
        this.androidContent = androidContent;
    }
    
    public String getIosContent()
    {
        return iosContent;
    }
    
    public void setIosContent(String iosContent)
    {
        this.iosContent = iosContent;
    }

    public String getIosTitle()
    {
        return iosTitle;
    }

    public void setIosTitle(String iosTitle)
    {
        this.iosTitle = iosTitle;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
    
    
}
