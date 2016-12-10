package com.ygg.admin.util;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.ygg.admin.code.ImageTypeEnum;

public class ImageUtil
{
    
    /**
     * 获得图片前缀
     * @param ordinal
     * @return
     */
    public static String getPrefix()
    {
        String prefix = "";
        if (CommonConstant.imageUseAliOrYoupai.equals("ali"))
        {
            prefix = CommonConstant.ali_prefix;
        }
        else if (CommonConstant.imageUseAliOrYoupai.equals("youpai"))
        {
            prefix = CommonConstant.youpai_prefix;
        }
        return prefix;
    }
    
    /**
     * 获得图片后缀
     * @param ordinal
     * @return
     */
    public static String getSuffix(int ordinal)
    {
        String result = "";
        boolean useAli = true;
        if (CommonConstant.imageUseAliOrYoupai.equals("youpai"))
        {
            useAli = false;
        }
        for (ImageTypeEnum e : ImageTypeEnum.values())
        {
            if (e.ordinal() == ordinal)
            {
                if (useAli)
                {
                    result = e.getAliSuffix();
                }
                else
                {
                    result = e.getYoupaiSuffix();
                }
                return result;
            }
        }
        return "";
    }
    
    public static Map<String, Object> getProperty(String imageUrl)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        BufferedImage image = getBufferedImage(imageUrl);
        // if(image != null){
        // 允许 空指针 异常
        result.put("status", true);
        result.put("height", image.getHeight() + "");
        result.put("width", image.getWidth() + "");
        // }else {
        // result.put("status", false);
        // }
        return result;
    }
    
    /**
     * 
     * @param imgUrl 图片地址
     * @return
     */
    private static BufferedImage getBufferedImage(String imgUrl)
    {
        URL url = null;
        InputStream is = null;
        BufferedImage img = null;
        try
        {
            url = new URL(imgUrl);
            is = url.openStream();
            img = ImageIO.read(is);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
            return null;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            try
            {
                if (is != null)
                {
                    is.close();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return null;
            }
        }
        return img;
    }

    /**
     * 将url存储到本地磁盘
     * @param urlStr
     * @param saveFile
     * @return
     */
    public static long saveToFile(String urlStr, String saveFile)
    {
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        HttpURLConnection httpUrl = null;
        URL url = null;
        int BUFFER_SIZE = 1024;
        byte[] buf = new byte[BUFFER_SIZE];
        int size = 0;
        long totalFileSize = 0;
        try
        {
            url = new URL(urlStr);
            httpUrl = (HttpURLConnection)url.openConnection();
            httpUrl.connect();
            totalFileSize = httpUrl.getContentLengthLong();
            bis = new BufferedInputStream(httpUrl.getInputStream());
            fos = new FileOutputStream(saveFile);
            while ((size = bis.read(buf)) != -1)
            {
                fos.write(buf, 0, size);
            }
            fos.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                fos.close();
                bis.close();
                httpUrl.disconnect();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return totalFileSize;
    }

    public static void main(String[] args){
        Map<String, Object> imageMap = getProperty("http://yangege.b0.upaiyun.com/product/d4bc8b11779.jpg!v1detail");
        System.out.printf(imageMap.toString());
    }
}