package com.ygg.common.services.image;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;

import com.ygg.common.utils.CommonConst;

public class ImgYunManager
{
    
    private ImgYunManager()
    {
    }
    
    private static ConcurrentMap<String, ImgYunServiceIF> imgClientMap = new ConcurrentHashMap<String, ImgYunServiceIF>();
    
    private static Logger log = Logger.getLogger(ImgYunManager.class);
    
    private static void init(String imgConfig)
    {
        if (imgClientMap.containsKey(imgConfig))
        {
            return;
        }
        try
        {
            if (imgConfig.equals(CommonConst.IMG_ALIYUN))
            {
                OSSImgYunServiceIF ossImgServiceIF = new OSSImgYunServiceImpl();
                imgClientMap.put(imgConfig, ossImgServiceIF);
            }
            else if (imgConfig.equals(CommonConst.IMG_UPYUN))
            {
                UpImgYunServiceIF upImgServiceIF = new UpImgYunServiceImpl();
                imgClientMap.put(imgConfig, upImgServiceIF);
            }
        }
        catch (Exception e)
        {
            log.error("初始化图片服务失败", e);
        }
    }
    
    public static ImgYunServiceIF getClient(String imgConfig)
    {
        if (!imgClientMap.containsKey(imgConfig))
        {
            init(imgConfig);
        }
        return imgClientMap.get(imgConfig);
    }
}
