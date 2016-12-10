package com.ygg.common.services.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

/**
 * 图片接口
 * 
 * @author Administrator
 *
 */
public interface ImgYunServiceIF
{
    
    /**
     * 通过图片二进制和文件名上传 fileName命名 eg: yggwebimg/test20150527/yggimg201505270001.jpg 前面两级表示目录,最后一级是文件名
     * 
     * @param file
     * @param fileName
     * @return
     * @throws IOException
     */
    public Map<String, String> uploadFile(byte[] file, String fileName)
        throws IOException;
    
    /**
     * 通过图片文件和文件名上传 fileName命名 eg: yggwebimg/test20150527/yggimg201505270001.jpg 前面两级表示目录,最后一级是文件名
     * 
     * @param file
     * @param fileName
     * @return
     * @throws IOException
     */
    public Map<String, String> uploadFile(File file, String fileName)
        throws IOException;
    
    /**
     * 通过文件流来上传
     * 
     * @param inputStream
     * @param fileName
     * @return
     * @throws IOException
     */
    public Map<String, String> uploadFile(FileInputStream inputStream, String fileName)
        throws IOException;
    
    /**
     * 通过 URL来上传
     * 
     * @param url
     * @param fileName
     * @return
     * @throws IOException
     */
    public Map<String, String> uploadFile(URL url, String fileName)
        throws IOException;
}
