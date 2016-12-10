package com.ygg.common.services.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.ObjectMetadata;
import com.ygg.common.utils.CommonUtil;
import com.ygg.common.utils.YggCommonProperties;

/**
 * ali OSSYun图片存储服务
 * 
 * @author Administrator
 *
 */
public class OSSImgYunServiceImpl implements OSSImgYunServiceIF
{
    Logger logger = Logger.getLogger(OSSImgYunServiceImpl.class);
    
    private String accessKeyId = YggCommonProperties.getInstance().getProperties("ali_oss_accessKeyId");
    
    private String accessKeySecret = YggCommonProperties.getInstance().getProperties("ali_oss_accessKeySecret");
    
    private String bucketName = YggCommonProperties.getInstance().getProperties("ali_oss_bucketName");
    
    private String domainName = YggCommonProperties.getInstance().getProperties("ali_oss_domainName");
    
    private String endpoint = YggCommonProperties.getInstance().getProperties("ali_oss_endpoint"); // oss-cn-hangzhou-internal.aliyuncs.com
    
    private static OSSClient ossClient = null;
    
    /** 绑定的域名 */
    private final String URL = "http://" + bucketName + "." + domainName; // download base地址
    
    private final String baseUrl = YggCommonProperties.getInstance().getProperties("ali_oss_baseUrl");
    
    /** 根目录 */
    private static final String DIR_ROOT = "/";
    
    public OSSImgYunServiceImpl()
    {
        ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret, getClientConfiguration());
        
        if (!ossClient.doesBucketExist(bucketName))
            ossClient.createBucket(bucketName);
        // 设置Bucket ACL
        ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
    }
    
    private ClientConfiguration getClientConfiguration()
    {
        ClientConfiguration conf = new ClientConfiguration();
        // 设置HTTP最大连接数为10
        conf.setMaxConnections(10);
        // 设置TCP连接超时为5000毫秒
        conf.setConnectionTimeout(5000);
        // 设置最大的重试次数为3
        conf.setMaxErrorRetry(3);
        // 设置Socket传输数据超时的时间为30000毫秒
        conf.setSocketTimeout(30000);
        
        return conf;
    }
    
    @Override
    public Map<String, String> uploadFile(byte[] file, String fileName)
        throws IOException
    {
        
        InputStream is = new FileInputStream(CommonUtil.byte2File(file));
        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentLength(file.length);
        // 可以在metadata中标记文件类型
        objectMeta.setContentType("image/jpeg");
        String key = fileName;
        String filePath = DIR_ROOT + key;
        try
        {
            ossClient.putObject(bucketName, key, is, objectMeta);
            
            return getUploadResult(true, filePath);
        }
        catch (Exception e)
        {
            logger.error("OSS upload error", e);
            return getUploadResult(false, filePath);
        }
        finally
        {
            if (is != null)
                is.close();
        }
    }
    
    @Override
    public Map<String, String> uploadFile(File file, String fileName)
        throws IOException
    {
        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentLength(file.length());
        // 可以在metadata中标记文件类型
        objectMeta.setContentType("image/jpeg");
        InputStream input = null;
        
        String key = fileName;
        String filePath = DIR_ROOT + key;
        try
        {
            input = new FileInputStream(file);
            ossClient.putObject(bucketName, key, input, objectMeta);
            return getUploadResult(true, filePath);
        }
        catch (Exception e)
        {
            logger.error("OSS upload error", e);
            return getUploadResult(false, filePath);
        }
        finally
        {
            if (input != null)
                input.close();
        }
    }
    
    @Override
    public Map<String, String> uploadFile(byte[] file, String fileDirectory, String fileName)
        throws IOException
    {
        InputStream is = new FileInputStream(CommonUtil.byte2File(file));
        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentLength(file.length);
        // 可以在metadata中标记文件类型
        objectMeta.setContentType("image/jpeg");
        String key = fileDirectory; // 没有文件目录的话,默认放在yggwebimg/目录下
        if (fileName.indexOf(".") > 0)
        {
            String prefix = fileName.substring(0, fileName.indexOf("."));
            String suffix = fileName.substring(fileName.indexOf("."), fileName.length());
            key = key + prefix + System.currentTimeMillis() + suffix;
        }
        else
            key = key + fileName + System.currentTimeMillis() + ".jpg";
        
        String filePath = DIR_ROOT + key;
        try
        {
            ossClient.putObject(bucketName, key, is, objectMeta);
            
            return getUploadResult(true, filePath);
        }
        catch (Exception e)
        {
            logger.error("OSS upload error", e);
            return getUploadResult(false, filePath);
        }
        finally
        {
            if (is != null)
                is.close();
        }
    }
    
    @Override
    public Map<String, String> uploadFile(File file, String fileDirectory, String fileName)
        throws IOException
    {
        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentLength(file.length());
        // 可以在metadata中标记文件类型
        objectMeta.setContentType("image/jpeg");
        InputStream input = null;
        
        String key = fileDirectory; // 没有文件目录的话,默认放在yggwebimg/目录下
        if (fileName.indexOf(".") > 0)
        {
            String prefix = fileName.substring(0, fileName.indexOf("."));
            String suffix = fileName.substring(fileName.indexOf("."), fileName.length());
            key = key + prefix + System.currentTimeMillis() + suffix;
        }
        else
            key = key + fileName + System.currentTimeMillis() + ".jpg";
        
        String filePath = DIR_ROOT + key;
        try
        {
            input = new FileInputStream(file);
            ossClient.putObject(bucketName, key, input, objectMeta);
            
            return getUploadResult(true, filePath);
        }
        catch (Exception e)
        {
            logger.error("OSS upload error", e);
            return getUploadResult(false, filePath);
        }
        finally
        {
            if (input != null)
                input.close();
        }
    }
    
    private Map<String, String> getUploadResult(boolean result, String filePath)
    {
        Map<String, String> map = new HashMap<String, String>();
        if (result)
        {
            map.put("status", "success");
            map.put("url", baseUrl + filePath);
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
        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentLength(inputStream.available());
        // 可以在metadata中标记文件类型
        objectMeta.setContentType("image/jpeg");
        
        String key = fileName;
        String filePath = DIR_ROOT + key;
        try
        {
            ossClient.putObject(bucketName, key, inputStream, objectMeta);
            return getUploadResult(true, filePath);
        }
        catch (Exception e)
        {
            logger.error("OSS upload error", e);
            return getUploadResult(false, filePath);
        }
        finally
        {
            // 外部负责关闭fileInputStream
        }
        
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
    
    @Override
    public Map<String, String> uploadFile(FileInputStream inputStream, String fileDirectory, String fileName)
        throws IOException
    {
        
        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentLength(inputStream.available());
        // 可以在metadata中标记文件类型
        objectMeta.setContentType("image/jpeg");
        
        String key = fileDirectory; // 没有文件目录的话,默认放在yggwebimg/目录下
        if (fileName.indexOf(".") > 0)
        {
            String prefix = fileName.substring(0, fileName.indexOf("."));
            String suffix = fileName.substring(fileName.indexOf("."), fileName.length());
            key = key + prefix + System.currentTimeMillis() + suffix;
        }
        else
            key = key + fileName + System.currentTimeMillis() + ".jpg";
        
        String filePath = DIR_ROOT + key;
        try
        {
            
            ossClient.putObject(bucketName, key, inputStream, objectMeta);
            
            return getUploadResult(true, filePath);
        }
        catch (Exception e)
        {
            logger.error("OSS upload error", e);
            return getUploadResult(false, filePath);
        }
        finally
        {
            // 外部调用负责close inputStream
        }
    }
    
    @Override
    public Map<String, String> uploadFile(java.net.URL url, String fileDirectory, String fileName)
        throws IOException
    {
        String filePath = DIR_ROOT + fileName;
        try
        {
            return this.uploadFile(new File(url.toURI()), fileDirectory, fileName);
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace();
            return getUploadResult(false, filePath);
        }
    }
}
