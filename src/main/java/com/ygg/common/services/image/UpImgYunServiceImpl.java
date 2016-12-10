package com.ygg.common.services.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import com.ygg.common.sdk.upyan.UpYun;
import com.ygg.common.utils.YggCommonProperties;

public class UpImgYunServiceImpl implements UpImgYunServiceIF
{
    private static UpYun upYun = null;
    
    private static final String BUCKET_NAME = YggCommonProperties.getInstance().getProperties("upyun_bucketName");
    
    private static final String USER_NAME = YggCommonProperties.getInstance().getProperties("upyun_userName");
    
    private static final String USER_PWD = YggCommonProperties.getInstance().getProperties("upyun_passWord");
    
    /** 绑定的域名 */
    private static final String URL = "http://" + BUCKET_NAME + ".b0.upaiyun.com";
    
    /** 根目录 */
    private static final String DIR_ROOT = "/";
    
    public UpImgYunServiceImpl()
    {
        if (upYun == null)
            upYun = new UpYun("weishang201605", "weishang201605", "weishang201605");
        
    }
    
    @Override
    public Map<String, String> uploadFile(byte[] file, String fileName)
        throws IOException
    {
        // 切换 API 接口的域名接入点，默认为自动识别接入点
        // upyun.setApiDomain(UpYun.ED_AUTO);
        
        // 设置连接超时时间，默认为30秒
        // upyun.setTimeout(60);
        
        // 设置是否开启debug模式，默认不开启
        // upyun.setDebug(true);
        
        // 1.上传文件（文件内容）
        String filePath = DIR_ROOT + fileName;
        
        // 设置待上传文件的 Content-MD5 值
        // 如果又拍云服务端收到的文件MD5值与用户设置的不一致，将回报 406 NotAcceptable 错误
        upYun.setContentMD5(UpYun.md5(file));
        
        // 上传文件，并自动创建父级目录（最多10级）
        boolean result = upYun.writeFile(filePath, file);
        
        return getUploadResult(result, filePath);
    }
    
    @Override
    public Map<String, String> uploadFile(File file, String fileName)
        throws IOException
    {
        
        // 切换 API 接口的域名接入点，默认为自动识别接入点
        // upyun.setApiDomain(UpYun.ED_AUTO);
        
        // 设置连接超时时间，默认为30秒
        // upyun.setTimeout(60);
        
        // 设置是否开启debug模式，默认不开启
        // upyun.setDebug(true);
        
        // 1.上传文件（文件内容）
        String filePath = DIR_ROOT + fileName;
        
        // 设置待上传文件的 Content-MD5 值
        // 如果又拍云服务端收到的文件MD5值与用户设置的不一致，将回报 406 NotAcceptable 错误
        upYun.setContentMD5(UpYun.md5(file));
        
        // 上传文件，并自动创建父级目录（最多10级）
        boolean result = upYun.writeFile(filePath, new FileInputStream(file), true);
        
        return getUploadResult(result, filePath);
    }
    
    private Map<String, String> getUploadResult(boolean result, String filePath)
    {
        Map<String, String> map = new HashMap<String, String>();
        if (result)
        {
            map.put("status", "success");
            map.put("url", URL + filePath);
        }
        else
        {
            map.put("status", "failure");
        }
        return map;
    }
    
    @Override
    public Map<String, String> uploadFile(FileInputStream inputStream, String fileName)
        throws IOException
    {
        String filePath = DIR_ROOT + fileName;
        
        boolean result = upYun.writeFile(filePath, inputStream, true);
        
        return getUploadResult(result, filePath);
    }
    
    @Override
    public Map<String, String> uploadFile(java.net.URL url, String fileName)
        throws IOException
    {
        
        String filePath = DIR_ROOT + fileName;
        try
        {
            return this.uploadFile(new File(url.toURI()), fileName);
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace();
            return getUploadResult(false, filePath);
        }
        
    }
    
}
