package com.ygg.admin.sdk.yimei;

public class SDKClientProxy implements com.ygg.appnative.sdk.yimei.SDKClient
{
    private String _endpoint = null;
    
    private com.ygg.appnative.sdk.yimei.SDKClient sDKClient = null;
    
    public SDKClientProxy()
    {
        _initSDKClientProxy();
    }
    
    public SDKClientProxy(String endpoint)
    {
        _endpoint = endpoint;
        _initSDKClientProxy();
    }
    
    private void _initSDKClientProxy()
    {
//        try
//        {
//            sDKClient = (new com.ygg.appnative.sdk.yimei.SDKServiceLocator()).getSDKService();
//            if (sDKClient != null)
//            {
//                if (_endpoint != null)
//                    ((javax.xml.rpc.Stub)sDKClient)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
//                else
//                    _endpoint = (String)((javax.xml.rpc.Stub)sDKClient)._getProperty("javax.xml.rpc.service.endpoint.address");
//            }
//            
//        }
//        catch (javax.xml.rpc.ServiceException serviceException)
//        {
//        }
    }
    
    public String getEndpoint()
    {
        return _endpoint;
    }
    
    public void setEndpoint(String endpoint)
    {
        _endpoint = endpoint;
        if (sDKClient != null)
            ((javax.xml.rpc.Stub)sDKClient)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        
    }
    
    public com.ygg.appnative.sdk.yimei.SDKClient getSDKClient()
    {
        if (sDKClient == null)
            _initSDKClientProxy();
        return sDKClient;
    }
    
    public java.lang.String getVersion()
        throws java.rmi.RemoteException
    {
return null;    	
//        if (sDKClient == null)
//            _initSDKClientProxy();
//        return sDKClient.getVersion();
    }
    
    public com.ygg.appnative.sdk.yimei.StatusReport[] getReport(java.lang.String arg0, java.lang.String arg1)
        throws java.rmi.RemoteException
    {
return null;    	
//        if (sDKClient == null)
//            _initSDKClientProxy();
//        return sDKClient.getReport(arg0, arg1);
    }
    
    public int cancelMOForward(java.lang.String arg0, java.lang.String arg1)
        throws java.rmi.RemoteException
    {
return 0;    	
//        if (sDKClient == null)
//            _initSDKClientProxy();
//        return sDKClient.cancelMOForward(arg0, arg1);
    }
    
    public int chargeUp(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, java.lang.String arg3)
        throws java.rmi.RemoteException
    {
return 0;    	
//        if (sDKClient == null)
//            _initSDKClientProxy();
//        return sDKClient.chargeUp(arg0, arg1, arg2, arg3);
    }
    
    public double getBalance(java.lang.String arg0, java.lang.String arg1)
        throws java.rmi.RemoteException
    {
    	return 0;
//    	if (sDKClient == null)
//            _initSDKClientProxy();
//        return sDKClient.getBalance(arg0, arg1);
    }
    
    public double getEachFee(java.lang.String arg0, java.lang.String arg1)
        throws java.rmi.RemoteException
    {
    	return 0;       
//    	if (sDKClient == null)
//            _initSDKClientProxy();
//        return sDKClient.getEachFee(arg0, arg1);
    }
    
    public com.ygg.appnative.sdk.yimei.Mo[] getMO(java.lang.String arg0, java.lang.String arg1)
        throws java.rmi.RemoteException
    {
return null;    	
//        if (sDKClient == null)
//            _initSDKClientProxy();
//        return sDKClient.getMO(arg0, arg1);
    }
    
    public int logout(java.lang.String arg0, java.lang.String arg1)
        throws java.rmi.RemoteException
    {
    	return 0;
//    	if (sDKClient == null)
//            _initSDKClientProxy();
//        return sDKClient.logout(arg0, arg1);
    }
    
    public int registDetailInfo(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, java.lang.String arg3, java.lang.String arg4,
        java.lang.String arg5, java.lang.String arg6, java.lang.String arg7, java.lang.String arg8, java.lang.String arg9)
        throws java.rmi.RemoteException
    {
    	return 0;
//        if (sDKClient == null)
//            _initSDKClientProxy();
//        return sDKClient.registDetailInfo(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
    }
    
    public int registEx(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2)
        throws java.rmi.RemoteException
    {
    	return 0;
//        if (sDKClient == null)
//            _initSDKClientProxy();
//        return sDKClient.registEx(arg0, arg1, arg2);
    }
    
    public int sendSMS(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, java.lang.String[] arg3, java.lang.String arg4,
        java.lang.String arg5, java.lang.String arg6, int arg7, long arg8)
        throws java.rmi.RemoteException
    {
    	return 0;
//        if (sDKClient == null)
//            _initSDKClientProxy();
//        return sDKClient.sendSMS(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
    }
    
    public java.lang.String sendVoice(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, java.lang.String[] arg3, java.lang.String arg4,
        java.lang.String arg5, java.lang.String arg6, int arg7, long arg8)
        throws java.rmi.RemoteException
    {
    	return null;
//        if (sDKClient == null)
//            _initSDKClientProxy();
//        return sDKClient.sendVoice(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
    }
    
    public int serialPwdUpd(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, java.lang.String arg3)
        throws java.rmi.RemoteException
    {
    	return 0;
//        if (sDKClient == null)
//            _initSDKClientProxy();
//        return sDKClient.serialPwdUpd(arg0, arg1, arg2, arg3);
    }
    
    public int setMOForward(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2)
        throws java.rmi.RemoteException
    {
    	return 0;
//        if (sDKClient == null)
//            _initSDKClientProxy();
//        return sDKClient.setMOForward(arg0, arg1, arg2);
    }
    
    public int setMOForwardEx(java.lang.String arg0, java.lang.String arg1, java.lang.String[] arg2)
        throws java.rmi.RemoteException
    {
    	return 0;
//        if (sDKClient == null)
//            _initSDKClientProxy();
//        return sDKClient.setMOForwardEx(arg0, arg1, arg2);
    }
    
}