package com.ygg.common.services.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

/**
 * 阿里的oss
 * 
 * @author Administrator
 *
 */
public interface OSSImgYunServiceIF extends ImgYunServiceIF
{
    /**
     * 给定文件目录上传 fileDirectory eg:yggwebimg/test20150527/ ,fileName:yggimg20150526.jpg
     * 
     * @param file
     * @param fileDirectory
     * @param fileName
     * @return
     * @throws IOException
     */
    public Map<String, String> uploadFile(byte[] file, String fileDirectory, String fileName)
        throws IOException;
    
    /**
     * 给定文件目录上传 fileDirectory eg:yggwebimg/test20150527/ ,fileName:yggimg20150526.jpg
     * 
     * @param file
     * @param fileDirectory
     * @param fileName
     * @return
     * @throws IOException
     */
    public Map<String, String> uploadFile(File file, String fileDirectory, String fileName)
        throws IOException;
    
    /**
     * 通过文件流来上传
     * 
     * @param inputStream
     * @param fileName
     * @return
     * @throws IOException
     */
    public Map<String, String> uploadFile(FileInputStream inputStream, String fileDirectory, String fileName)
        throws IOException;
    
    /**
     * 通过 URL来上传
     * 
     * @param url
     * @param fileName
     * @return
     * @throws IOException
     */
    public Map<String, String> uploadFile(URL url, String fileDirectory, String fileName)
        throws IOException;
}
