package com.ygg.admin.sdk.montnets.httpsoap;

import org.apache.axis.client.Call;

/**
 * 具体方法的接口类
 * @author Administrator
 *
 */
public interface SmsSoap extends java.rmi.Remote
{
    
    /**
     * 短信息发送接口
     * 重要说明：相同信息内容的号码一定要打包发送，一次可以发送100个号码。如果对于相同的信息一条一条发送，系统将会自动停止用户帐号。
     * @param userId  用户账号
     * @param password  用户密码
     * @param pszMobis  目标号码，用英文逗号(,)分隔，最大100个号码。一次提交的号码类型不受限制，但手机会做验证，若有不合法的手机号将会被退回。号码段类型分为：移动、联通、电信手机 注意：请不要使用中文的逗号
     * @param pszMsg  短信内容， 内容长度不大于350个汉字
     * @param iMobiCount  号码个数（最大100个手机）
     * @param pszSubPort  子端口号码，不带请填星号{*} 长度由账号类型定4-6位，通道号总长度不能超过20位。如：10657****主通道号，3321绑定的扩展端口，主+扩展+子端口总长度不能超过20位。
     * @param MsgId   一个8字节64位的大整型（INT64），格式化成的字符串。因此该字段必须为纯数字，且范围不能超过INT64的取值范围（-(2^63）……2^63-1）即-9223372036854775807……9223372036854775808。格式化成字符串后最大长度不超过20个字节。
     * @return
     * @throws java.rmi.RemoteException
     */
    public String MongateSendSubmit(String userId, String password, String pszMobis, String pszMsg, int iMobiCount, String pszSubPort, String MsgId)
        throws java.rmi.RemoteException;
    
    /**
     * 短信息发送接口
     * 重要说明：相同信息内容的号码一定要打包发送，一次可以发送100个号码。如果对于相同的信息一条一条发送，系统将会自动停止用户帐号。
     * @param userId  用户账号
     * @param password  用户密码
     * @param pszMobis  目标号码，用英文逗号(,)分隔，最大100个号码。一次提交的号码类型不受限制，但手机会做验证，若有不合法的手机号将会被退回。号码段类型分为：移动、联通、电信手机 注意：请不要使用中文的逗号
     * @param pszMsg  短信内容， 内容长度不大于350个汉字
     * @param iMobiCount  号码个数（最大100个手机）
     * @param pszSubPort  子端口号码，不带请填星号{*} 长度由账号类型定4-6位，通道号总长度不能超过20位。如：10657****主通道号，3321绑定的扩展端口，主+扩展+子端口总长度不能超过20位。
     * @param MsgId   一个8字节64位的大整型（INT64），格式化成的字符串。因此该字段必须为纯数字，且范围不能超过INT64的取值范围（-(2^63）……2^63-1）即-9223372036854775807……9223372036854775808。格式化成字符串后最大长度不超过20个字节。
     * @return
     * @throws java.rmi.RemoteException
     */
    public String MongateSendSubmit(String userId, String password, String pszMobis, String pszMsg, int iMobiCount, String pszSubPort, String MsgId, Call call)
        throws java.rmi.RemoteException;
    
    /**
     * 短信息发送接口（不同内容群发，可自定义不同流水号，自定义不同扩展号）
     * 注意：由于单个短信结构体中各元素间，以及短信包中各个短信结构体间采用的分隔符分隔，所以要求短信结构体中各元素的值中不能包含分隔符（,和/），否则将导致解析错误。
    		 短信包中最大包含200个短信结构体，超过200个时200个以后的将丢失。
     * @param userId  用户账号
     * @param password  用户密码
     * @param multix_mt  批量短信请求包。该字段中包含N个短信包结构体。每个结构体间用固定的分隔符隔开。
    						N的范围为1~200.
    						分隔符为英文逗号(,).
    						单个短信包结构体的内容见下表.
     * @return    返回值：
    					错误描述对应说明
    					发送成功：信息编号，如：-8485643440204283743或1485643440204283743
    					错误返回： 
    					-1 	参数为空。信息、电话号码等有空指针，登陆失败
    					-11	电话号码中有非数字字符
    					-12	有异常电话号码
    					-101	发送消息等待超时
    					-102	发送或接收消息失败
    					-103	接收消息超时
    					-200	其他错误
    					-999	web服务器内部错误
     * @throws java.rmi.RemoteException
     */
    public String MongateMULTIXSend(String userId, String password, String multix_mt)
        throws java.rmi.RemoteException;
    
    /**
     * 短信息发送接口（不同内容群发，可自定义不同流水号，自定义不同扩展号）
     * 注意：由于单个短信结构体中各元素间，以及短信包中各个短信结构体间采用的分隔符分隔，所以要求短信结构体中各元素的值中不能包含分隔符（,和/），否则将导致解析错误。
    		 短信包中最大包含200个短信结构体，超过200个时200个以后的将丢失。
     * @param userId  用户账号
     * @param password  用户密码
     * @param multix_mt  批量短信请求包。该字段中包含N个短信包结构体。每个结构体间用固定的分隔符隔开。
    						N的范围为1~200.
    						分隔符为英文逗号(,).
    						单个短信包结构体的内容见下表.
     * @return    返回值：
    					错误描述对应说明
    					发送成功：信息编号，如：-8485643440204283743或1485643440204283743
    					错误返回： 
    					-1 	参数为空。信息、电话号码等有空指针，登陆失败
    					-11	电话号码中有非数字字符
    					-12	有异常电话号码
    					-101	发送消息等待超时
    					-102	发送或接收消息失败
    					-103	接收消息超时
    					-200	其他错误
    					-999	web服务器内部错误
     * @throws java.rmi.RemoteException
     */
    public String MongateMULTIXSend(String userId, String password, String multix_mt, Call call)
        throws java.rmi.RemoteException;
    
    /**
     * 查询余额接口
     * @param userId  用户账号
     * @param password  用户密码
     * @return
     * @throws java.rmi.RemoteException
     */
    public int MongateQueryBalance(String userId, String password)
        throws java.rmi.RemoteException;
    
    /**
     * 查询余额接口
     * @param userId  用户账号
     * @param password  用户密码
     * @return
     * @throws java.rmi.RemoteException
     */
    public int MongateQueryBalance(String userId, String password, Call call)
        throws java.rmi.RemoteException;
    
    /**
     * 获取上行/状态报告等
     * @param userId  用户账号
     * @param password  用户密码
     * @param iReqType  请求类型(0: 上行&状态报告 1:上行 2: 状态报告)
     * @return
     * @throws java.rmi.RemoteException
     */
    public String[] MongateGetDeliver(String userId, String password, int iReqType)
        throws java.rmi.RemoteException;
    
    /**
     * 获取上行/状态报告等
     * @param userId  用户账号
     * @param password  用户密码
     * @param iReqType  请求类型(0: 上行&状态报告 1:上行 2: 状态报告)
     * @return
     * @throws java.rmi.RemoteException
     */
    public String[] MongateGetDeliver(String userId, String password, int iReqType, Call call)
        throws java.rmi.RemoteException;
    
    public org.apache.axis.client.Call createCall()
        throws java.rmi.RemoteException;
    
}
