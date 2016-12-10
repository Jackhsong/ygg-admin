package com.ygg.admin.sdk.montnets.httpsoap;

/**
 * 获取soap请求连接的接口类
 * @author Administrator
 *
 */
public interface Sms extends javax.xml.rpc.Service
{
    public java.lang.String getSmsSoapAddress();
    
    public com.ygg.admin.sdk.montnets.httpsoap.SmsSoap getSmsSoap()
        throws javax.xml.rpc.ServiceException;
    
    public com.ygg.admin.sdk.montnets.httpsoap.SmsSoap getSmsSoap(java.net.URL portAddress)
        throws javax.xml.rpc.ServiceException;
}
